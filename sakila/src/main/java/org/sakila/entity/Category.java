package org.sakila.entity;

public class Category {
    private int categoryId;
    private String name;
    private String lastUpdate;

    public Category() {}
    public Category(int categoryId, String name, String lastUpdate) {
        this.categoryId = categoryId; this.name = name; this.lastUpdate = lastUpdate;
    }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    @Override public String toString() {
        return "【类别】类别编号=" + categoryId
                + " | 类别名称=" + name
                + " | 最后更新时间=" + lastUpdate;
    }
}
