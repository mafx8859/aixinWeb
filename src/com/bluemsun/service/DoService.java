package com.bluemsun.service;

import com.bluemsun.dao.DoServiceDao;
import com.bluemsun.entity.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mafx on 2018/8/1.
 */
public class DoService {
    private DoServiceDao doServiceDao=new DoServiceDao();
    public Student getStuInfoByStuId(String stuID){
        return doServiceDao.getStuInfoByStuIdDao(stuID);
    }
    public List<BuyRecord> getBuyRecordById(int id){
        return doServiceDao.getBuyRecordByIdDao(id);
    }
    public Goods getGoodsByCode(String barcode){
        return doServiceDao.getGoodsByCodeDao(barcode);
    }
    public void updateGoodsNumAndAddRecord(String[] barcodeArry,String stuID,String userId){
        doServiceDao.updateNumDao(barcodeArry);
        Date date=new Date();
        SimpleDateFormat Format;
        Format= new SimpleDateFormat("yyyyMMdd");
        String dateString=Format.format(date);
        for (String i:barcodeArry){
            Record record=doServiceDao.getRecordInfo(i,stuID);
            record.setTime(dateString);
            record.setUserId(userId);
            doServiceDao.saveRecord(record);
        }
    }
    public boolean updateStuMoneyService(String stuID,float balanceRiyong,float balanceFuzhuang){
        return doServiceDao.updateStuMoney(stuID,balanceRiyong,balanceFuzhuang);
    }
    public TempBuyInfo getBuyInfo(String barCode,String stuID){
        Student student=getStuInfoByStuId(stuID);
        return doServiceDao.getGetBuyInfoDao(barCode,student.getId());
    }
}
