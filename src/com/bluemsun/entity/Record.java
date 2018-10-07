package com.bluemsun.entity;

/**
 * Created by mafx on 2018/8/2.
 */
public class Record {
    private int aixin_studentId;
    private int aixin_goodsId;
    private String time;
    private String totalMoney;
    private int compus;
    private String userId;

    public int getAixin_studentId() {
        return aixin_studentId;
    }

    public void setAixin_studentId(int aixin_studentId) {
        this.aixin_studentId = aixin_studentId;
    }

    public int getAixin_goodsId() {
        return aixin_goodsId;
    }

    public void setAixin_goodsId(int aixin_goodsId) {
        this.aixin_goodsId = aixin_goodsId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getCompus() {
        return compus;
    }

    public void setCompus(int compus) {
        this.compus = compus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
