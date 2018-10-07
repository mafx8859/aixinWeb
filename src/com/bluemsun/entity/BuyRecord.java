package com.bluemsun.entity;

/**
 * Created by mafx on 2018/8/1.
 */
public class BuyRecord {
    private String categoryName;
    private float totalMoney;
    private String compus;
    private String time;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getCompus() {
        return compus;
    }

    public void setCompus(String compus) {
        this.compus = compus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
