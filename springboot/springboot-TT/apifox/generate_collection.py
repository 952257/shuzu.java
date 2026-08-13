#!/usr/bin/env python3
"""Apifox 用例：仅覆盖分工表中的物业接口。"""
import json
import uuid
from pathlib import Path

CID = "2022081539020475"
OWNER = "6022081500000001"
ROOM = "5022081500000001"
UNIT = "4022081500000001"
FLOOR = "3022081500000001"
ACCT = "C022081500000001"
STAFF = "1000000002"

OK = """var json = pm.response.json();
pm.test("HTTP 200", function () { pm.response.to.have.status(200); });
pm.test("业务成功 code=0", function () { pm.expect(json.code).to.eql(0); });
"""

OK_PAGE = OK + """
pm.test("分页结构完整", function () {
  pm.expect(json.data).to.be.an("array");
  pm.expect(json).to.have.property("total");
});
"""

OK_LOGIN = """var json = pm.response.json();
pm.test("HTTP 200", function () { pm.response.to.have.status(200); });
pm.test("登录成功", function () {
  pm.expect(json.code).to.eql(0);
  pm.expect(json.data.token).to.be.a("string").and.to.have.lengthOf.at.least(20);
  pm.expect(json.data.userId).to.be.a("string");
  pm.expect(json.data.role).to.be.oneOf(["ADMIN", "STAFF"]);
});
if (json && json.data && json.data.token) {
  pm.environment.set("ttToken", json.data.token);
  pm.collectionVariables.set("ttToken", json.data.token);
}
"""

PREREQUEST = """var name = (pm.info && pm.info.requestName) ? pm.info.requestName : "";
if (name.indexOf("登录") === 0) { return; }
var token = pm.environment.get("ttToken") || pm.collectionVariables.get("ttToken");
if (token) { return; }
var base = pm.environment.get("ttBaseUrl") || pm.collectionVariables.get("ttBaseUrl") || "http://localhost:8088";
pm.sendRequest({
  url: base + "/app/login.pcUserLogin",
  method: "POST",
  header: { "Content-Type": "application/json" },
  body: { mode: "raw", raw: JSON.stringify({ username: "admin", passwd: "admin" }) }
}, function (err, res) {
  if (!res) { return; }
  var json = res.json();
  if (json && json.data && json.data.token) {
    pm.environment.set("ttToken", json.data.token);
    pm.collectionVariables.set("ttToken", json.data.token);
  }
});
"""


def uid():
    return str(uuid.uuid4())


def lines(script):
    return script.replace("\r\n", "\n").split("\n")


def fail_biz(code=None, msg=None):
    parts = [
        "var json = pm.response.json();",
        'pm.test("HTTP 200", function () { pm.response.to.have.status(200); });',
        'pm.test("业务失败 code!=0", function () { pm.expect(json.code).to.not.eql(0); });',
    ]
    if code is not None:
        parts.append(f'pm.test("错误码 {code}", function () {{ pm.expect(json.code).to.eql({code}); }});')
    if msg:
        parts.append(
            f'pm.test("错误文案", function () {{ pm.expect(json.msg).to.eql({json.dumps(msg, ensure_ascii=False)}); }});'
        )
    return "\n".join(parts) + "\n"


def save_id(var_name):
    return OK + f"""
pm.test("返回新建ID", function () {{
  pm.expect(json.data).to.be.a("string").and.to.have.lengthOf.at.least(4);
}});
if (json && json.code === 0 && json.data) {{
  pm.environment.set("{var_name}", json.data);
  pm.collectionVariables.set("{var_name}", json.data);
}}
"""


def req(name, method, path, query=None, body=None, tests=OK, auth=None, desc=""):
    url_path = path if path.startswith("/") else "/" + path
    q = query or []
    raw = "{{ttBaseUrl}}/app" + url_path
    if q:
        raw += "?" + "&".join(f"{i['key']}={i['value']}" for i in q)
    item = {
        "name": name,
        "request": {
            "method": method,
            "header": [{"key": "Content-Type", "value": "application/json"}] if method in ("POST", "PUT", "DELETE") else [],
            "url": {
                "raw": raw,
                "host": ["{{ttBaseUrl}}"],
                "path": ["app"] + url_path.strip("/").split("/"),
                "query": q,
            },
            "description": desc,
        },
        "event": [{"listen": "test", "script": {"type": "text/javascript", "exec": lines(tests)}}],
    }
    if body is not None:
        item["request"]["body"] = {
            "mode": "raw",
            "raw": json.dumps(body, ensure_ascii=False, indent=2),
            "options": {"raw": {"language": "json"}},
        }
    if auth == "noauth":
        item["request"]["auth"] = {"type": "noauth"}
    return item


def q(**kwargs):
    return [{"key": k, "value": str(v), "disabled": False} for k, v in kwargs.items()]


def folder(name, items, desc=""):
    return {"name": name, "description": desc, "item": items}


page_q = q(communityId=CID, page=1, row=10)

collection = {
    "info": {
        "_postman_id": uid(),
        "name": "TT物业-分工接口测试",
        "description": """仅覆盖分工表中的接口（不含商城/物联网/公告问卷等）。

导入：Apifox → Postman。环境选「TT物业-本地8088」。
地址变量 ttBaseUrl = http://localhost:8088，不要用 8080。
先跑「00-登录」，或依赖集合前置自动登录（admin/admin）。
带序号的闭环用例必须按顺序跑，后置脚本会把新建 ID 写入环境变量。
""",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
    },
    "auth": {
        "type": "bearer",
        "bearer": [{"key": "token", "value": "{{ttToken}}", "type": "string"}],
    },
    "event": [
        {"listen": "prerequest", "script": {"type": "text/javascript", "exec": lines(PREREQUEST)}}
    ],
    "variable": [
        {"key": "ttBaseUrl", "value": "http://localhost:8088"},
        {"key": "ttToken", "value": ""},
        {"key": "communityId", "value": CID},
    ],
    "item": [
        folder(
            "00-登录",
            [
                req("登录-账号密码成功", "POST", "/login.pcUserLogin",
                    body={"username": "admin", "passwd": "admin"}, tests=OK_LOGIN, auth="noauth",
                    desc="成功后写入 ttToken。"),
                req("登录-密码错误", "POST", "/login.pcUserLogin",
                    body={"username": "admin", "passwd": "wrong"},
                    tests=fail_biz(401, "用户或密码错误"), auth="noauth"),
            ],
            "登录是后续接口的前置，不算分工模块。",
        ),
        folder(
            "01-小区物业楼栋（沈炫）",
            [
                req("物业-列表", "GET", "/property.listProperty", query=q(page=1, row=10), tests=OK_PAGE),
                req("物业-1新增", "POST", "/property.saveProperty",
                    body={"name": "用例物业", "tel": "13800000000", "address": "测试地址", "corporation": "张三", "foundingTime": "2020-01-01", "state": "48001"},
                    tests=save_id("caseStoreId")),
                req("物业-2修改", "PUT", "/property.updateProperty",
                    body={"storeId": "{{caseStoreId}}", "name": "用例物业-改", "tel": "13800000001"}),
                req("物业-3删除", "DELETE", "/property.deleteProperty",
                    body={"storeId": "{{caseStoreId}}"}),
                req("物业-名称为空", "POST", "/property.saveProperty",
                    body={"tel": "13800000000"}, tests=fail_biz(1001, "物业公司名称不能为空")),
                req("小区-列表", "GET", "/community.listCommunitys", query=page_q, tests=OK_PAGE),
                req("小区-按名称", "GET", "/community.listCommunitys", query=q(name="测试", page=1, row=10), tests=OK_PAGE),
                req("小区-1新增", "POST", "/community.saveCommunity",
                    body={"name": "用例小区", "address": "西宁市", "cityCode": "630104", "cityName": "城西区", "tel": "18900001111", "storeId": "10001", "nearbyLandmarks": "中心广场", "state": "1100"},
                    tests=save_id("caseCommunityId")),
                req("小区-2修改", "PUT", "/community.updateCommunity",
                    body={"communityId": "{{caseCommunityId}}", "name": "用例小区-改", "address": "西宁市城西区", "nearbyLandmarks": "中心广场"}),
                req("小区-3删除", "DELETE", "/community.deleteCommunity",
                    body={"communityId": "{{caseCommunityId}}"}),
                req("小区-名称为空", "POST", "/community.saveCommunity",
                    body={"address": "西宁市", "cityCode": "630104"}, tests=fail_biz(1001, "小区名称不能为空")),
                req("楼栋-列表", "GET", "/floor.queryFloors", query=page_q, tests=OK_PAGE),
                req("楼栋-1新增", "POST", "/floor.saveFloor",
                    body={"communityId": CID, "floorNum": "T{{$timestamp}}", "name": "用例楼栋", "floorArea": 1000, "seq": 99},
                    tests=save_id("caseFloorId")),
                req("楼栋-2修改", "PUT", "/floor.editFloor",
                    body={"floorId": "{{caseFloorId}}", "name": "用例楼栋-改"}),
                req("楼栋-3删除", "DELETE", "/floor.deleteFloor",
                    body={"floorId": "{{caseFloorId}}"}),
                req("楼栋-名称为空", "POST", "/floor.saveFloor",
                    body={"communityId": CID, "floorNum": "99"}, tests=fail_biz(1001, "楼栋名称不能为空")),
            ],
            "小区、物业、楼栋增删改查。闭环用新建数据，不改演示小区。",
        ),
        folder(
            "02-单元与工单（陈金龙）",
            [
                req("单元-列表", "GET", "/unit.queryUnits", query=q(floorId=FLOOR, page=1, row=10), tests=OK_PAGE),
                req("单元-1新增", "POST", "/unit.saveUnit",
                    body={"floorId": FLOOR, "unitNum": "T{{$timestamp}}", "layerCount": 6, "lift": "1010", "unitArea": 800},
                    tests=save_id("caseUnitId")),
                req("单元-2修改", "PUT", "/unit.updateUnit",
                    body={"unitId": "{{caseUnitId}}", "unitNum": "9", "layerCount": 8}),
                req("单元-3删除", "DELETE", "/unit.deleteUnit",
                    body={"unitId": "{{caseUnitId}}"}),
                req("单元-缺楼栋", "POST", "/unit.saveUnit",
                    body={"unitNum": "2"}, tests=fail_biz(1001, "楼栋ID不能为空")),
                req("报修-列表", "GET", "/repair.listRepairs", query=page_q, tests=OK_PAGE),
                req("报修-1新增", "POST", "/repair.saveRepair",
                    body={"communityId": CID, "repairName": "用例报修", "tel": "13800001111", "repairObjName": "1-1-101", "context": "水管漏水", "repairType": "1001"},
                    tests=save_id("caseRepairId")),
                req("报修-2派单", "POST", "/repair.dispatchRepair",
                    body={"repairId": "{{caseRepairId}}", "staffId": STAFF, "staffName": "吴学文"}),
                req("报修-3完成", "POST", "/repair.finishRepair",
                    body={"repairId": "{{caseRepairId}}"}),
                req("报修-4删除", "DELETE", "/repair.deleteRepair",
                    body={"repairId": "{{caseRepairId}}"}),
                req("报修-缺报修人", "POST", "/repair.saveRepair",
                    body={"communityId": CID, "context": "漏水"}, tests=fail_biz(1001, "报修人不能为空")),
                req("投诉-列表", "GET", "/complaint.listComplaints", query=page_q, tests=OK_PAGE),
                req("投诉-1新增", "POST", "/complaint.saveComplaint",
                    body={"communityId": CID, "typeCd": "809001", "complaintName": "用例投诉", "tel": "13800001111", "context": "楼道堆物"},
                    tests=save_id("caseComplaintId")),
                req("投诉-2办理", "POST", "/complaint.auditComplaint",
                    body={"complaintId": "{{caseComplaintId}}"}),
                req("投诉-3删除", "DELETE", "/complaint.deleteComplaint",
                    body={"complaintId": "{{caseComplaintId}}"}),
                req("投诉-缺内容", "POST", "/complaint.saveComplaint",
                    body={"communityId": CID, "complaintName": "用例"}, tests=fail_biz(1001, "投诉内容不能为空")),
            ],
            "单元 CRUD + 工单（报修、投诉）。",
        ),
        folder(
            "03-房屋交房退房车位（包磊）",
            [
                req("房屋-列表", "GET", "/room.queryRooms", query=q(communityId=CID, page=1, row=10), tests=OK_PAGE),
                req("房屋-未售", "GET", "/room.queryRoomsWithOutSell", query=q(communityId=CID, page=1, row=10), tests=OK_PAGE),
                req("房屋-已入住", "GET", "/room.queryRoomsWithSell", query=q(communityId=CID, page=1, row=10), tests=OK_PAGE),
                req("房屋-按业主", "GET", "/room.queryRoomsByOwner", query=q(ownerId=OWNER, page=1, row=10), tests=OK_PAGE),
                req("房屋-1新增", "POST", "/room.saveRoom",
                    body={"communityId": CID, "unitId": UNIT, "roomNum": "T{{$timestamp}}", "layer": "9", "apartment": "两室一厅", "builtUpArea": 90, "roomArea": 80, "state": "2001", "roomSubType": "110"},
                    tests=save_id("caseRoomId")),
                req("房屋-2修改", "PUT", "/room.updateRoom",
                    body={"roomId": "{{caseRoomId}}", "apartment": "三室两厅"}),
                req("房屋-3交房", "POST", "/room.sellRoom",
                    body={"roomId": "{{caseRoomId}}", "ownerId": OWNER}),
                req("房屋-4退房", "POST", "/room.exitRoom",
                    body={"roomId": "{{caseRoomId}}", "ownerId": OWNER}),
                req("房屋-5删除", "DELETE", "/room.deleteRoom",
                    body={"roomId": "{{caseRoomId}}"}),
                req("房屋-缺房号", "POST", "/room.saveRoom",
                    body={"communityId": CID, "unitId": UNIT}, tests=fail_biz(1001, "房屋编号不能为空")),
                req("交房-缺业主", "POST", "/room.sellRoom",
                    body={"roomId": ROOM}, tests=fail_biz(1001, "业主ID不能为空")),
                req("车位-列表", "GET", "/parkingSpace.queryParkingSpaces", query=page_q, tests=OK_PAGE),
                req("车位-1新增", "POST", "/parkingSpace.saveParkingSpace",
                    body={"communityId": CID, "num": "T-{{$timestamp}}", "parkingType": "1", "state": "F", "area": 12},
                    tests=save_id("casePsId")),
                req("车位-2修改", "PUT", "/parkingSpace.editParkingSpace",
                    body={"psId": "{{casePsId}}", "area": 15}),
                req("车位-3出售", "POST", "/parkingSpace.sellParkingSpace",
                    body={"psId": "{{casePsId}}"}),
                req("车位-4退租", "POST", "/parkingSpace.exitParkingSpace",
                    body={"psId": "{{casePsId}}"}),
                req("车位-5删除", "DELETE", "/parkingSpace.deleteParkingSpace",
                    body={"psId": "{{casePsId}}"}),
                req("车位-缺编号", "POST", "/parkingSpace.saveParkingSpace",
                    body={"communityId": CID}, tests=fail_biz(1001, "车位编号不能为空")),
            ],
            "房屋、交房、退房、车位。不改 1-1-101 种子房。",
        ),
        folder(
            "04-业主家庭账户（王新宇）",
            [
                req("业主-列表", "GET", "/owner.queryOwners", query=q(communityId=CID, ownerTypeCd="1001", page=1, row=10), tests=OK_PAGE),
                req("业主-按姓名", "GET", "/owner.queryOwners", query=q(communityId=CID, name="李明", page=1, row=10), tests=OK_PAGE),
                req("业主-1新增", "POST", "/owner.saveOwner",
                    body={"communityId": CID, "name": "用例业主", "link": "13700001111", "idCard": "630104199001010011", "sex": "0", "ownerTypeCd": "1001", "personRole": "1", "state": "2000"},
                    tests=save_id("caseOwnerId")),
                req("业主-2修改", "PUT", "/owner.editOwner",
                    body={"memberId": "{{caseOwnerId}}", "ownerId": "{{caseOwnerId}}", "name": "用例业主-改", "link": "13700001112"}),
                req("家庭-1新增成员", "POST", "/owner.saveOwner",
                    body={"communityId": CID, "ownerId": "{{caseOwnerId}}", "name": "用例家属", "link": "13700002222", "ownerTypeCd": "1002", "personRole": "2", "state": "2000"},
                    tests=save_id("caseMemberId"),
                    desc="ownerTypeCd=1002 表示家庭成员，ownerId 指向业主。"),
                req("家庭-2成员列表", "GET", "/owner.queryOwnerMembers", query=q(ownerId="{{caseOwnerId}}", page=1, row=10), tests=OK_PAGE),
                req("家庭-3删除成员", "DELETE", "/owner.deleteOwner",
                    body={"memberId": "{{caseMemberId}}"}),
                req("业主-3删除", "DELETE", "/owner.deleteOwner",
                    body={"memberId": "{{caseOwnerId}}"}),
                req("业主-缺手机号", "POST", "/owner.saveOwner",
                    body={"communityId": CID, "name": "无名"}, tests=fail_biz(1001, "手机号不能为空")),
                req("账户-列表", "GET", "/account.queryOwnerAccount", query=q(communityId=CID, objId=OWNER, page=1, row=10), tests=OK_PAGE),
                req("账户-明细", "GET", "/account.listAccountDetail", query=q(acctId=ACCT, page=1, row=10), tests=OK_PAGE),
                req("账户-1预存", "POST", "/account.ownerPrestoreAccount",
                    body={"acctId": ACCT, "amount": "1", "remark": "用例预存"},
                    tests=save_id("caseAcctDetailId"),
                    desc="预存 1 元后务必跑下一条撤销，把余额改回去。"),
                req("账户-2撤销明细", "POST", "/account.cancelAccountDetail",
                    body={"detailId": "{{caseAcctDetailId}}"}),
                req("账户-预存金额非法", "POST", "/account.ownerPrestoreAccount",
                    body={"acctId": ACCT, "amount": "0", "remark": "非法"}, tests=fail_biz(1001, "预存金额必须大于0")),
            ],
            "业主、家庭成员、账户预存/撤销。预存后必须撤销。",
        ),
        folder(
            "05-费用（冯伟）",
            [
                req("费用项-列表", "GET", "/feeConfig.listFeeConfigs", query=page_q, tests=OK_PAGE),
                req("费用项-1新增", "POST", "/feeConfig.saveFeeConfig",
                    body={"communityId": CID, "feeName": "用例卫生费", "feeTypeCd": "888800010004", "feeFlag": "1003006", "squarePrice": 0.5, "additionalAmount": 0, "paymentCycle": "12"},
                    tests=save_id("caseConfigId")),
                req("费用项-2修改", "PUT", "/feeConfig.updateFeeConfig",
                    body={"configId": "{{caseConfigId}}", "feeName": "用例卫生费-改"}),
                req("账单-列表", "GET", "/fee.listFee", query=q(communityId=CID, page=1, row=10), tests=OK_PAGE),
                req("账单-收费中", "GET", "/fee.listFee", query=q(communityId=CID, state="2008001", page=1, row=10), tests=OK_PAGE),
                req("账单-queryFee", "GET", "/fee.queryFee", query=q(communityId=CID, page=1, row=10), tests=OK_PAGE),
                req("账单-1创建", "POST", "/fee.saveFee",
                    body={"communityId": CID, "configId": "{{caseConfigId}}", "payerObjId": ROOM, "payerObjType": "3333", "amount": 10.5},
                    tests=save_id("caseFeeId")),
                req("账单-2缴费", "POST", "/fee.payFee",
                    body={"feeId": "{{caseFeeId}}", "receivedAmount": 10.5, "cycles": 1},
                    tests=save_id("caseFeeDetailId")),
                req("缴费记录-列表", "GET", "/fee.queryFeeDetail", query=q(communityId=CID, page=1, row=10), tests=OK_PAGE),
                req("账单-3结束费用", "DELETE", "/fee.deleteFee",
                    body={"feeId": "{{caseFeeId}}"}),
                req("费用项-3删除", "DELETE", "/feeConfig.deleteFeeConfig",
                    body={"configId": "{{caseConfigId}}"}),
                req("创建账单-缺费用项", "POST", "/fee.saveFee",
                    body={"communityId": CID, "payerObjId": ROOM}, tests=fail_biz(1001, "费用项ID不能为空")),
                req("抄表-列表", "GET", "/meterWater.listMeterWaters", query=page_q, tests=OK_PAGE),
                req("抄表-1新增", "POST", "/meterWater.saveMeterWater",
                    body={"communityId": CID, "objId": ROOM, "objType": "3333", "meterType": "2020", "preDegrees": 100, "curDegrees": 125},
                    tests=save_id("caseWaterId")),
                req("抄表-2修改", "PUT", "/meterWater.updateMeterWater",
                    body={"waterId": "{{caseWaterId}}", "curDegrees": 130}),
                req("抄表-3删除", "DELETE", "/meterWater.deleteMeterWater",
                    body={"waterId": "{{caseWaterId}}"}),
            ],
            "费用项、创建费、查询、缴费、结束费用、缴费记录、水电抄表。",
        ),
    ],
}


def count_items(items):
    n = 0
    for it in items:
        if "item" in it:
            n += count_items(it["item"])
        else:
            n += 1
    return n


env = {
    "id": uid(),
    "name": "TT物业-本地8088",
    "values": [
        {"key": "ttBaseUrl", "value": "http://localhost:8088", "type": "default", "enabled": True},
        {"key": "ttToken", "value": "", "type": "secret", "enabled": True},
        {"key": "communityId", "value": CID, "type": "default", "enabled": True},
        {"key": "ownerId", "value": OWNER, "type": "default", "enabled": True},
        {"key": "roomId", "value": ROOM, "type": "default", "enabled": True},
        {"key": "unitId", "value": UNIT, "type": "default", "enabled": True},
        {"key": "floorId", "value": FLOOR, "type": "default", "enabled": True},
        {"key": "acctId", "value": ACCT, "type": "default", "enabled": True},
        {"key": "caseStoreId", "value": "", "type": "default", "enabled": True},
        {"key": "caseCommunityId", "value": "", "type": "default", "enabled": True},
        {"key": "caseFloorId", "value": "", "type": "default", "enabled": True},
        {"key": "caseUnitId", "value": "", "type": "default", "enabled": True},
        {"key": "caseRoomId", "value": "", "type": "default", "enabled": True},
        {"key": "casePsId", "value": "", "type": "default", "enabled": True},
        {"key": "caseOwnerId", "value": "", "type": "default", "enabled": True},
        {"key": "caseMemberId", "value": "", "type": "default", "enabled": True},
        {"key": "caseAcctDetailId", "value": "", "type": "default", "enabled": True},
        {"key": "caseConfigId", "value": "", "type": "default", "enabled": True},
        {"key": "caseFeeId", "value": "", "type": "default", "enabled": True},
        {"key": "caseFeeDetailId", "value": "", "type": "default", "enabled": True},
        {"key": "caseRepairId", "value": "", "type": "default", "enabled": True},
        {"key": "caseComplaintId", "value": "", "type": "default", "enabled": True},
        {"key": "caseWaterId", "value": "", "type": "default", "enabled": True},
    ],
}

out = Path(__file__).resolve().parent
(out / "TT-物业接口.postman_collection.json").write_text(
    json.dumps(collection, ensure_ascii=False, indent=2), encoding="utf-8"
)
(out / "TT-物业环境.postman_environment.json").write_text(
    json.dumps(env, ensure_ascii=False, indent=2), encoding="utf-8"
)
print("requests", count_items(collection["item"]))
print("folders", len(collection["item"]))
