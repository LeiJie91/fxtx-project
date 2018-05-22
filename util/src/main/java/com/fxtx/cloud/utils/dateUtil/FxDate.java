package com.fxtx.cloud.utils.dateUtil;

/**
 * 时间类型
 * \* Created with IntelliJ IDEA.
 * \* User: wugong.jie
 * \* Date: 2017/11/10 10:54
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class FxDate {
    private String id ;
    private String value ;
    private String parent ;
    public FxDate(String parent,String id,String value) {
        this.id=id ;
        this.parent=parent ;
        this.value=value ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}