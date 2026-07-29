package org.sakila.entity;

/**
 * 客户实体类，对应数据库 customer 表。
 * 客户隶属于某个商店(store_id)，关联一个地址(address_id)。
 * active 表示是否为活跃客户，create_date 为注册时间（插入时由数据库 NOW() 自动生成）。
 */
public class Customer {
    private int customerId;       // 客户主键
    private int storeId;          // 所属商店ID（外键 → store）
    private String firstName;     // 名
    private String lastName;      // 姓
    private String email;         // 电子邮箱
    private int addressId;        // 地址ID（外键 → address）
    private boolean active;       // 是否活跃：true=是, false=否
    private String createDate;    // 注册时间（由数据库自动填充）
    private String lastUpdate;    // 最后更新时间
    private String addressText;   // 联表查询带出的地址文本（非表字段）
    private String cityName;      // 联表查询带出的城市名（非表字段）

    public Customer() {}
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getStoreId() { return storeId; }
    public void setStoreId(int storeId) { this.storeId = storeId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getCreateDate() { return createDate; }
    public void setCreateDate(String createDate) { this.createDate = createDate; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    public String getAddressText() { return addressText; }
    public void setAddressText(String addressText) { this.addressText = addressText; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    @Override
    public String toString() {
        return "【客户】客户编号=" + customerId
                + " | 所属商店编号=" + storeId
                + " | 名=" + firstName
                + " | 姓=" + lastName
                + " | 邮箱=" + email
                + " | 地址编号=" + addressId
                + " | 是否激活=" + (active ? "是" : "否")
                + " | 创建时间=" + createDate
                + " | 最后更新时间=" + lastUpdate;
    }
}
