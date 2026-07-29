package org.sakila.entity;

import java.math.BigDecimal;

public class Payment {
    private int paymentId;
    private int customerId;
    private int staffId;
    private Integer rentalId;
    private BigDecimal amount;
    private String paymentDate;
    private String lastUpdate;
    private String customerName;
    private String staffName;

    public Payment() {}
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    @Override
    public String toString() {
        return "【支付】支付编号=" + paymentId
                + " | 客户编号=" + customerId
                + " | 员工编号=" + staffId
                + " | 租赁编号=" + rentalId
                + " | 支付金额=" + amount
                + " | 支付时间=" + paymentDate
                + " | 最后更新时间=" + lastUpdate;
    }
}
