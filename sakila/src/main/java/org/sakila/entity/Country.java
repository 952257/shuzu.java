package org.sakila.entity;

/**
 * 实体类：对应 country 表
 */
public class Country {

    private int countryId;
    private String country;
    private String lastUpdate;

    public Country() {
    }

    public Country(int countryId, String country, String lastUpdate) {
        this.countryId = countryId;
        this.country = country;
        this.lastUpdate = lastUpdate;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "【国家】国家编号=" + countryId
                + " | 国家名称=" + country
                + " | 最后更新时间=" + lastUpdate;
    }
}
