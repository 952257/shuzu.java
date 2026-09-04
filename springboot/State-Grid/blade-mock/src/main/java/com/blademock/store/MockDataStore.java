package com.blademock.store;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class MockDataStore {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Map<String, IssuedToken> tokens = new ConcurrentHashMap<>();
    private final Map<Long, RemoteUser> users = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(10010);

    @PostConstruct
    public void init() {
        addUser(10001L, "admin", "管理员", "admin@grid.cn", "13800000001", 0);
        addUser(10002L, "zhangsan", "张三", "zs@grid.cn", "13800000002", 0);
        addUser(10003L, "lisi", "李四", "ls@grid.cn", "13800000003", 0);
        addUser(10004L, "wangwu", "王五", "ww@grid.cn", "13800000004", 0);
        addUser(10005L, "zhaoliu", "赵六", "zl@grid.cn", "13800000005", 0);
        addUser(10006L, "sunqi", "孙七", "sq@grid.cn", "13800000006", 0);
        addUser(10007L, "zhouba", "周八", "zb@grid.cn", "13800000007", 0);
        addUser(10008L, "wujiu", "吴九", "wj@grid.cn", "13800000008", 0);
        addUser(10009L, "zhengshi", "郑十", "zs10@grid.cn", "13800000009", 1);
        addUser(10010L, "qianyi", "钱一", "qy@grid.cn", "13800000010", 0);
    }

    public IssuedToken issueToken(String account) {
        IssuedToken token = new IssuedToken();
        token.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
        token.setRefreshToken(UUID.randomUUID().toString().replace("-", ""));
        token.setAccount(account);
        token.setExpireAt(System.currentTimeMillis() + 3600_000);
        tokens.put(token.getAccessToken(), token);
        return token;
    }

    public boolean validToken(String accessToken) {
        IssuedToken token = tokens.get(accessToken);
        return token != null && token.getExpireAt() > System.currentTimeMillis();
    }

    public PageResult page(long current, long size) {
        List<RemoteUser> all = users.values().stream()
                .sorted(Comparator.comparing(RemoteUser::getId))
                .collect(Collectors.toList());
        long total = all.size();
        long pages = Math.max((total + size - 1) / size, 1);
        int from = (int) Math.max((current - 1) * size, 0);
        int to = (int) Math.min(from + size, total);
        List<RemoteUser> records = from >= total ? new ArrayList<>() : new ArrayList<>(all.subList(from, to));
        PageResult result = new PageResult();
        result.setRecords(records);
        result.setTotal(total);
        result.setSize(size);
        result.setCurrent(current);
        result.setPages(pages);
        return result;
    }

    public RemoteUser add(String account, String name) {
        long id = idSeq.incrementAndGet();
        return addUser(id, account, name, account + "@grid.cn", "1390000" + String.format("%04d", id % 10000), 0);
    }

    public RemoteUser logicalDelete(Long id) {
        RemoteUser user = users.get(id);
        if (user != null) {
            user.setIsDeleted(1);
            user.setUpdateTime(now());
        }
        return user;
    }

    private RemoteUser addUser(Long id, String account, String name, String email, String phone, int deleted) {
        RemoteUser user = new RemoteUser();
        user.setId(id);
        user.setTenantId("000000");
        user.setAccount(account);
        user.setName(name);
        user.setRealName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoleId("1");
        user.setDeptId(id % 2 == 0 ? "1" : "2");
        user.setStatus(1);
        user.setIsDeleted(deleted);
        user.setCreateTime(now());
        user.setUpdateTime(now());
        users.put(id, user);
        return user;
    }

    private String now() {
        return LocalDateTime.now().format(FMT);
    }

    @Data
    public static class IssuedToken {
        private String accessToken;
        private String refreshToken;
        private String account;
        private long expireAt;
    }

    @Data
    public static class RemoteUser {
        private Long id;
        private String tenantId;
        private String account;
        private String name;
        private String realName;
        private String email;
        private String phone;
        private String roleId;
        private String deptId;
        private Integer status;
        private Integer isDeleted;
        private String createTime;
        private String updateTime;
    }

    @Data
    public static class PageResult {
        private List<RemoteUser> records;
        private Long total;
        private Long size;
        private Long current;
        private Long pages;
    }
}
