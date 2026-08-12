package com.springboot.others.excel;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping
public class ExcelController {

    @Resource
    private MemberService memberService;
    /**
     * 普通导出方式
     */
    @RequestMapping("/export1")
    public void exportMembers1(HttpServletResponse response) throws IOException {
        List<Member> members = memberService.getAllMember();

        // 设置文本类型
        response.setContentType("application/vnd.ms-excel");
        // 设置字符编码
        response.setCharacterEncoding("utf-8");
        // 设置响应头
        response.setHeader("Content-disposition", "attachment;filename=demo.xlsx");
        EasyExcel.write(response.getOutputStream(), Member.class)
                .sheet("成员列表").doWrite(members);
    }

    /**
     * 从Excel导入会员列表
     */
    @PostMapping("/import1")
    @ResponseBody
    public void importMemberList(@RequestPart("file") MultipartFile file) throws IOException {
        List<Member> list = EasyExcel.read(file.getInputStream())
                .head(Member.class)
                .sheet()
                .doReadSync();
        for (Member member : list) {
            System.out.println(member);
        }
    }
}
