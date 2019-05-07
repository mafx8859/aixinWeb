package com.bluemsun.dao;

import com.bluemsun.entity.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mafx on 2018/8/1.
 */
public class DoServiceDao {
    private Connection conn;
    private QueryRunner queryRunner=new QueryRunner();
    public Student getStuInfoByStuIdDao(String stuID){
       String sql="SELECT info_student.id,info_student.name,info_student.grade," +
               "param_department.departmentName,param_special.specialName,param_imbursetype.imburseTypeName," +
               "aixin_student.balanceRiyong,aixin_student.balanceFuzhuang from info_student,param_department,param_special,param_imbursetype,aixin_student " +
               "where aixin_student.info_studentId=info_student.id and info_student.departmentId=param_department.id and info_student.specialNum=param_special.id and info_student.imburseType=param_imbursetype.id" +
               " and info_student.stuNum='"+stuID+"'";
       conn=JDBCUtil.getConnection();
       Student student=null;
       try {
           student = queryRunner.query(conn, sql, new BeanHandler<Student>(Student.class));
       }catch (SQLException ex){
           ex.printStackTrace();
       }finally {
           try {
               conn.close();
           }catch (Exception ex){
               ex.printStackTrace();
           }
       }
       return student;
    }
    public List<BuyRecord> getBuyRecordByIdDao(int id,String startTime,int goodsId) {
        String sql = "SELECT aixin_record.time,aixin_record.totalMoney,aixin_record.compus,aixin_goods.categoryName from aixin_record,aixin_goods,aixin_student " +
                "where aixin_student.id=aixin_record.aixin_studentId and aixin_record.aixin_goodsId=aixin_goods.id and aixin_student.info_studentId=? and aixin_record.time > ? and aixin_record.aixin_goodsId=? order by aixin_record.id desc";
        conn = JDBCUtil.getConnection();
        Object[] param={id,startTime,goodsId};
        List<BuyRecord> buyRecordList=null;
        try {
            buyRecordList = queryRunner.query(conn, sql, new BeanListHandler<BuyRecord>(BuyRecord.class),param);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return buyRecordList;
    }
    public List<BuyRecord> getBuyRecord(int id,String sql) {

        conn = JDBCUtil.getConnection();
        Calendar date=Calendar.getInstance();
        String year=date.get(Calendar.YEAR)+"";

        int mouth=date.get(Calendar.MONTH)+1;
        String mouthStr;
        if (mouth<10){
            mouthStr="0"+mouth;
        }else {
            mouthStr=mouth+"";
        }
        String regDate="%"+year+"-"+mouthStr+"%";
        System.out.println(regDate);
        Object[] param={id,regDate};
        List<BuyRecord> buyRecordList=null;
        try {
            buyRecordList = queryRunner.query(conn, sql, new BeanListHandler<BuyRecord>(BuyRecord.class),param);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return buyRecordList;
    }
    public List<BuyRecord> getBuyRecordByIdDao(int id){
        String sql = "SELECT aixin_record.time,aixin_record.totalMoney,aixin_record.compus,aixin_goods.categoryName from aixin_record,aixin_goods,aixin_student " +
                "where aixin_student.id=aixin_record.aixin_studentId and aixin_record.aixin_goodsId=aixin_goods.id and aixin_student.info_studentId=? and aixin_record.time like ? order by aixin_record.id desc";
        return getBuyRecord(id,sql);
    }
    public Goods getGoodsByCodeDao(String barcode) {
        String sql = "SELECT aixin_goods.barcode,aixin_goods.categoryName,aixin_goods.price,aixin_goods.type as 'coinType',aixin_cangku.num FROM aixin_goods,aixin_cangku where aixin_goods.id=aixin_cangku.goodsId and barcode=" + barcode;
        conn = JDBCUtil.getConnection();
        Goods goods = null;
        try {
            goods = queryRunner.query(conn, sql, new BeanHandler<Goods>(Goods.class));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return goods;
    }
    public void updateNumDao(String[] barcodeArry){
        String sql="update aixin_cangku,aixin_goods set aixin_cangku.num=aixin_cangku.num-1 where aixin_cangku.goodsId=aixin_goods.id and aixin_goods.barcode=?";
        conn = JDBCUtil.getConnection();
        for(String i:barcodeArry) {
            try{
                queryRunner.update(conn, sql, i);
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        try {
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Record getRecordInfo(String barcode,String stuID){
        String sql="select aixin_student.id as aixin_studentId,aixin_goods.id as aixin_goodsId,aixin_goods.price as totalMoney,info_student.campus-1 as compus " +
                "from aixin_student,aixin_goods,info_student where aixin_student.info_studentId=info_student.id and " +
                "info_student.stuNum=? and aixin_goods.barcode=?";
        conn=JDBCUtil.getConnection();
        Object[] param={stuID,barcode};
        Record record=null;
        try {
            record = queryRunner.query(conn, sql, new BeanHandler<Record>(Record.class), param);
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return record;
    }
    public void saveRecord(Record record){
        String sql="insert into aixin_record(aixin_studentId,aixin_goodsId,time,totalMoney,compus,userId)" +
                    "values(?,?,?,?,?,?)";
        Object[] param={record.getAixin_studentId(),record.getAixin_goodsId(),record.getTime(),record.getTotalMoney(),record.getCompus()+"",record.getUserId()};
        conn=JDBCUtil.getConnection();
        try {
            queryRunner.update(conn,sql,param);
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
    public boolean updateStuMoney(String stuID,float balanceRiyong,float balanceFuzhuang){
        String sql1="select aixin_student.balanceRiyong,aixin_student.balanceFuzhuang from aixin_student,info_student where info_student.id=aixin_student.info_studentId and info_student.stuNum="+stuID;

        String sql2="update aixin_student,info_student set aixin_student.balanceRiyong=?,aixin_student.balanceFuzhuang=? " +
                "where info_student.id=aixin_student.info_studentId and info_student.stuNum=?";
        conn=JDBCUtil.getConnection();
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            preparedStatement=conn.prepareStatement(sql1);
            resultSet=preparedStatement.executeQuery();
            resultSet.first();
            float newBalanceRiyong=Float.parseFloat(resultSet.getString("balanceRiyong"))-balanceRiyong;
            float newBalanceFuzhuang=Float.parseFloat(resultSet.getString("balanceFuzhuang"))-balanceFuzhuang;
            Object[] param={newBalanceRiyong,newBalanceFuzhuang,stuID};
            if(newBalanceFuzhuang<0||newBalanceFuzhuang<0){
                try {
                    resultSet.close();
                    preparedStatement.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
            queryRunner.update(conn, sql2,param);
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            try {
                resultSet.close();
                preparedStatement.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public TempBuyInfo getGetBuyInfoDao(String barCode,int stuID) {
        String sql1="select limitBuyNum,limitBuyType,id from aixin_goods where barcode=?";
        Connection conn= JDBCUtil.getConnection();
        TempBuyInfo buyInfo=new TempBuyInfo();
        PreparedStatement ps=null;
        ResultSet resultSet=null;
        int goodsId=0;
        StringBuilder sql3=null;
        try {
            ps=conn.prepareStatement(sql1);
            ps.setString(1,barCode);
            resultSet=ps.executeQuery();
            resultSet.first();
            buyInfo.setLimitBuyNum(resultSet.getInt(1));
            buyInfo.setLimitBuyType(resultSet.getString(2));
            goodsId=resultSet.getInt(3);
            String sqlTemp="SELECT aixin_record.time,aixin_record.totalMoney,aixin_record.compus,aixin_goods.categoryName from aixin_record,aixin_goods,aixin_student " +
                    "where aixin_student.id=aixin_record.aixin_studentId and aixin_record.aixin_goodsId=aixin_goods.id and aixin_record.aixin_goodsId="+goodsId+" and aixin_student.info_studentId=? and aixin_record.time like ? order by aixin_record.id desc";
            sql3=new StringBuilder(sqlTemp);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(!(buyInfo.getLimitBuyNum()==-1)) {
            if (buyInfo.getLimitBuyType().equals("0")) {
                buyInfo.setRecordBuyNum(getBuyRecord(stuID,sql3.toString()).size());
            } else {
                List<BuyRecord> recordList=getBuyRecordByIdDao(stuID,getSemesterStartTime(),goodsId);
                int recordNum;
                if(recordList==null){
                    recordNum=0;
                }else {
                    recordNum=recordList.size();
                }
                buyInfo.setRecordBuyNum(recordNum);
            }
        }
        return buyInfo;
    }
    public String getSemesterStartTime(){
        Calendar date=Calendar.getInstance();
        String year=date.get(Calendar.YEAR)+"";
        int mouth=date.get(Calendar.MONTH)+1;
        StringBuilder startTime=new StringBuilder();
        if(mouth>8){
            //秋季学期
            mouth=8;

        }else{
            //春季学期
            mouth=3;
        }
        String mouthStr;
        if (mouth<10){
            mouthStr="0"+mouth;
        }else {
            mouthStr=mouth+"";
        }
        startTime.append(year+"-"+mouthStr);
        return startTime.toString();
    }


}


