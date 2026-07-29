package org.sakila.entity;

public class Address {
    private int addressId;
    private String address;
    private String address2;
    private String district;
    private int cityId;
    private String postalCode;
    private String phone;
    private String lastUpdate;
    private String cityName;
    private String countryName;
    /** 位置原文，如 POINT(79.02 26.77) */
    private String locationText;
    /** 经度 */
    private Double longitude;
    /** 纬度 */
    private Double latitude;

    public Address() {}
    public Address(int addressId, String address, String address2, String district,
                   int cityId, String postalCode, String phone, String lastUpdate) {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.district = district;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
        this.lastUpdate = lastUpdate;
    }
    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    public String getLocationText() { return locationText; }
    public void setLocationText(String locationText) { this.locationText = locationText; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    @Override
    public String toString() {
        return "【地址】地址编号=" + addressId
                + " | 地址=" + address
                + " | 地址补充=" + address2
                + " | 地区=" + district
                + " | 城市编号=" + cityId
                + " | 邮政编码=" + postalCode
                + " | 电话=" + phone
                + " | 经度=" + longitude
                + " | 纬度=" + latitude
                + " | 位置=" + locationText
                + " | 最后更新时间=" + lastUpdate;
    }
}
