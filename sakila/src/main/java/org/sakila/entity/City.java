package org.sakila.entity;

public class City {
    private int cityId;
    private String city;
    private int countryId;
    private String lastUpdate;
    private String countryName;

    public City() {}
    public City(int cityId, String city, int countryId, String lastUpdate) {
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
        this.lastUpdate = lastUpdate;
    }
    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public int getCountryId() { return countryId; }
    public void setCountryId(int countryId) { this.countryId = countryId; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    @Override
    public String toString() {
        return "【城市】城市编号=" + cityId
                + " | 城市名称=" + city
                + " | 国家编号=" + countryId
                + " | 最后更新时间=" + lastUpdate;
    }
}
