package com.example.zjy.zjywidget;

/**
 * Created by 74215 on 2019/2/12.
 */

public class ViewItemBean {

    private String name;
    private String descirbe;
    private Class testClass;

    public ViewItemBean(String name, String descirbe, Class testClass) {
        this.name = name;
        this.descirbe = descirbe;
        this.testClass = testClass;
    }

    public Class getTestClass() {
        return testClass;
    }

    public void setTestClass(Class testClass) {
        this.testClass = testClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescirbe() {
        return descirbe;
    }

    public void setDescirbe(String descirbe) {
        this.descirbe = descirbe;
    }
}
