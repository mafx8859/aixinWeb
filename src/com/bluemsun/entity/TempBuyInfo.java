package com.bluemsun.entity;

/**
 * Created by mafx on 2019/5/4.
 */
public class TempBuyInfo {
    private int buyGoodsNum;
    private int limitBuyNum;
    private String limitBuyType;
    private int recordBuyNum;

    public int getRecordBuyNum() {
        return recordBuyNum;
    }

    public void setRecordBuyNum(int recordBuyNum) {
        this.recordBuyNum = recordBuyNum;
    }

    public int getBuyGoodsNum() {
        return buyGoodsNum;
    }

    public void setBuyGoodsNum(int buyGoodsNum) {
        this.buyGoodsNum = buyGoodsNum;
    }

    public int getLimitBuyNum() {
        return limitBuyNum;
    }

    public void setLimitBuyNum(int limitBuyNum) {
        this.limitBuyNum = limitBuyNum;
    }

    public String getLimitBuyType() {
        return limitBuyType;
    }

    public void setLimitBuyType(String limitBuyType) {
        this.limitBuyType = limitBuyType;
    }


}
