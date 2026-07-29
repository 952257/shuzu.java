public enum Demo_enum {
    /**
     * 枚举：关键字 enum
     *  列举出，一组常量，是引用数据类型，Java的一种特殊的类
     *
     * 枚举值是当前枚举类的实例对象（等同于：Demo_enum BEIJING = new Demo_enum()）
     * 枚举类的构造器默认就是private，只能是私有构造器
     * 注意：枚举类的顶部必须是枚举值
     */
    // Demo_enum BEIJING = new Demo_enum();
    // Demo_enum BEIJING = new Demo_enum("北京", "100000");
    BEIJING("北京", 100000),
    NANJING("南京", 200000),
    SHANGHAI("上海", 300000);

    private Demo_enum(String cityName, int cityCode){
        this.cityName = cityName;
        this.cityCode = cityCode;
    }

    private String cityName;
    private int cityCode;

    public String getCityName() {
        return cityName;
    }

    public int getCityCode() {
        return cityCode;
    }
}

class Main{
    String name;
    private Main(String name){
        this.name = name;
    }
    public static void main(String[] args) {
        System.out.println(Demo_enum.NANJING.getCityName());
        System.out.println(Demo_enum.NANJING.getCityCode());
        System.out.println(Demo_enum.BEIJING.getCityName());

    }
}


