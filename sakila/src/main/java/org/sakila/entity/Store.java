package org.sakila.entity;

public class Store {
    private int storeId;
    private int managerStaffId;
    private int addressId;
    private String lastUpdate;
    private String managerName;
    private String addressText;

    public Store() {}
    public Store(int storeId, int managerStaffId, int addressId, String lastUpdate) {
        this.storeId = storeId; this.managerStaffId = managerStaffId;
        this.addressId = addressId; this.lastUpdate = lastUpdate;
    }
    public int getStoreId() { return storeId; }
    public void setStoreId(int storeId) { this.storeId = storeId; }
    public int getManagerStaffId() { return managerStaffId; }
    public void setManagerStaffId(int managerStaffId) { this.managerStaffId = managerStaffId; }
    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
    public String getAddressText() { return addressText; }
    public void setAddressText(String addressText) { this.addressText = addressText; }

    @Override
    public String toString() {
        return "【商店】商店编号=" + storeId
                + " | 店长员工编号=" + managerStaffId
                + " | 地址编号=" + addressId
                + " | 最后更新时间=" + lastUpdate;
    }
}
