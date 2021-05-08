package com.example.zjy.zjywidget;

/**
 * Created by Yang on 2019/2/12.
 */

public class ViewItemBean {

    private String name;
    private String describe;
    private int previewGif;
    private Class testClass;

    public ViewItemBean(String name, String describe, int previewGif, Class testClass) {
        this.name = name;
        this.describe = describe;
        this.previewGif = previewGif;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getPreviewGif() {
        return previewGif;
    }

    public void setPreviewGif(int previewGif) {
        this.previewGif = previewGif;
    }
}
