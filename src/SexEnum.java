/**
 * 枚举
 */
public enum SexEnum {
    MALE("男", 1),
    FEMALE("女", 0);

    SexEnum(String sexName, int sexCode){
        this.sexName = sexName;
        this.sexCode = sexCode;
    }


    private String sexName;
    private int sexCode;

    public String getSexName() {
        return sexName;
    }

    public int getSexCode() {
        return sexCode;
    }
}
