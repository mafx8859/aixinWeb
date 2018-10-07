package com.bluemsun.entity;

/**
 * Created by mafx on 2018/8/1.
 */
public class Student {
    private int id;
    private String stuNum;
    private String name;
    private String departmentName;
    private String specialName;
    private String grade;
    private String imburseTypeName;
    private float balanceRiyong;
    private float balanceFuzhuang;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getImburseTypeName() {
        return imburseTypeName;
    }

    public void setImburseTypeName(String imburseTypeName) {
        this.imburseTypeName = imburseTypeName;
    }

    public float getBalanceRiyong() {
        return balanceRiyong;
    }

    public void setBalanceRiyong(float balanceRiyong) {
        this.balanceRiyong = balanceRiyong;
    }

    public float getBalanceFuzhuang() {
        return balanceFuzhuang;
    }

    public void setBalanceFuzhuang(float balanceFuzhuang) {
        this.balanceFuzhuang = balanceFuzhuang;
    }
}
