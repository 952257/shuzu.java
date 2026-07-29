package org.sakila.entity;

public class Rental {
    private int rentalId;
    private String rentalDate;
    private int inventoryId;
    private int customerId;
    private String returnDate;
    private int staffId;
    private String lastUpdate;
    private String customerName;
    private String staffName;
    private String filmTitle;
    private Integer filmId;

    public Rental() {}
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    public String getRentalDate() { return rentalDate; }
    public void setRentalDate(String rentalDate) { this.rentalDate = rentalDate; }
    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getFilmTitle() { return filmTitle; }
    public void setFilmTitle(String filmTitle) { this.filmTitle = filmTitle; }
    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    @Override
    public String toString() {
        return "【租赁】租赁编号=" + rentalId
                + " | 租赁时间=" + rentalDate
                + " | 库存编号=" + inventoryId
                + " | 客户编号=" + customerId
                + " | 归还时间=" + returnDate
                + " | 员工编号=" + staffId
                + " | 最后更新时间=" + lastUpdate;
    }
}
