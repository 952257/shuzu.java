package org.sakila.entity;

/**
 * 员工实体类，对应数据库 staff 表。
 * 员工隶属于某个商店(store_id)，关联一个地址(address_id)。
 * store 和 staff 存在循环外键（店长），因此插入时需临时关闭外键检查。
 * active 表示是否在职，username/password 用于系统登录。
 */
public class Staff {
    private int staffId;          // 员工主键
    private String firstName;     // 名
    private String lastName;      // 姓
    private int addressId;        // 住址ID（外键 → address）
    private String email;         // 电子邮箱
    private int storeId;          // 所属商店ID（外键 → store）
    private boolean active;       // 是否在职：true=是, false=否
    private String username;      // 登录用户名
    private String password;      // 登录密码
    private String lastUpdate;    // 最后更新时间
    private String addressText;   // 联表查询带出的地址文本（非表字段）
    private String cityName;      // 联表查询带出的城市名（非表字段）

    public Staff() {}
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getStoreId() { return storeId; }
    public void setStoreId(int storeId) { this.storeId = storeId; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    public String getAddressText() { return addressText; }
    public void setAddressText(String addressText) { this.addressText = addressText; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    @Override
    public String toString() {
        return "【员工】员工编号=" + staffId
                + " | 名=" + firstName
                + " | 姓=" + lastName
                + " | 地址编号=" + addressId
                + " | 邮箱=" + email
                + " | 所属商店编号=" + storeId
                + " | 是否在职=" + (active ? "是" : "否")
                + " | 用户名=" + username
                + " | 最后更新时间=" + lastUpdate;
    }
}
