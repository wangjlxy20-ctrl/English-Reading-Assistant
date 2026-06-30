package com.wjl.englishreadingassistant.enums;

public enum EmbeddingStatusEnum {
    /*
    * 0 :Pending embedding generation
    * 1 :Embedding generated successful
    * 2 :Embedding generated failed
    * */
    PENDING(0,"pending embedding generation"),
    SUCCESS(1,"embedding generated successful"),
    FAILED(2,"Embedding generated failed");

    private final Integer code;
    private final String desc;

    EmbeddingStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
