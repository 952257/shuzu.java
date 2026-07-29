package org.sakila.entity;

public class Language {
    private int languageId;
    private String name;
    private String lastUpdate;

    public Language() {}
    public Language(int languageId, String name, String lastUpdate) {
        this.languageId = languageId; this.name = name; this.lastUpdate = lastUpdate;
    }
    public int getLanguageId() { return languageId; }
    public void setLanguageId(int languageId) { this.languageId = languageId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    @Override public String toString() {
        return "【语言】语言编号=" + languageId
                + " | 语言名称=" + name
                + " | 最后更新时间=" + lastUpdate;
    }
}
