package com.bluemsun.controller;

import com.bluemsun.entity.BuyRecord;
import com.bluemsun.entity.Goods;
import com.bluemsun.entity.Student;
import com.bluemsun.service.DoService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mafx on 2018/8/1.
 */
public class DoServiceServlet extends HttpServlet {
    private DoService doService=new DoService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String flag=request.getParameter("flag");
        if(flag.length()>=19&&flag.substring(0,19).equals("queryGoodsByBarcode")){
            flag="queryGoodsByBarcode";
        }
        switch (flag){
            case "queryInfo":queryInfo(request,response);break;//查询用户基本信息
            case "queryGoodsByBarcode":queryGoodsByBarcode(request,response);break;//根据条形码查询商品信息
            case "settlement":settlement(request,response);break;//结算
            case "deleteGoods":deleteGoods(request,response);break;
            case "safeOut":safeOut(request,response);break;
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    private void safeOut(HttpServletRequest request, HttpServletResponse response){
        HttpSession session=request.getSession(true);
        session.invalidate();
        try {
            response.sendRedirect("http://wxy.nenu.edu.cn/aixinWeb/supermarket/index.html");
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }
    private void queryInfo(HttpServletRequest request, HttpServletResponse response){
        String stuID=request.getParameter("stuID");
        Map<String,Object> infoMap=new HashMap<String,Object>();
        Student student=doService.getStuInfoByStuId(stuID);//根据学号获取到学生信息
        student.setStuNum(stuID);
        int id=student.getId();
        List<BuyRecord> buyRecordList=doService.getBuyRecordById(id);
        infoMap.put("stuInfo",student);
        infoMap.put("buyRecord",buyRecordList);
        try {
            response.getWriter().write(JSONObject.fromObject(infoMap).toString());
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }
    private void queryGoodsByBarcode(HttpServletRequest request, HttpServletResponse response){
        String barCode=request.getParameter("barCode");
        System.out.println("********************"+barCode+"********");
        HttpSession session=request.getSession(true);
        /*if(session.getAttribute("goodsMap")==null){
            Map<String,Goods> goodsMap=new HashMap<String,Goods>();
            session.setAttribute("goodsMap",goodsMap);//将存储物品的条形码和具体信息的map存入session
        }*/
        if(session.getAttribute("goodsNumMap")==null){
            Map<String ,Integer> goodsNumMap=new HashMap<String,Integer>();//用于存储当前已经选择的物品的数量
            session.setAttribute("goodsNumMap",goodsNumMap);
        }
        HashMap<String,Integer> goodsMap=(HashMap<String,Integer>)session.getAttribute("goodsNumMap");//获取存放条形码数当前选择数量的map
        if(goodsMap.get(barCode)==null){
            goodsMap.put(barCode,0);
        }
        Goods goods=doService.getGoodsByCode(barCode);//获取新扫描的货物
        int num=goodsMap.get(barCode)+1;//当前选择购买的数量
        if(goods==null||goods.getNum()<num) {
            goods=null;
        }else {
           goodsMap.replace(barCode,num);//替换原来的数量
           session.setAttribute("goodsMap",goodsMap);
        }
        try {
            response.getWriter().write(JSONObject.fromObject(goods).toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void deleteGoods(HttpServletRequest request, HttpServletResponse response){
        String barcode=request.getParameter("barCode");
        System.out.println("********************"+barcode+"********");
        HttpSession session=request.getSession(true);
        HashMap<String,Integer> goodsMap=(HashMap<String,Integer>)session.getAttribute("goodsNumMap");
        System.out.println(goodsMap);
        int num=Integer.parseInt(goodsMap.get(barcode).toString())-1;
        goodsMap.replace(barcode,num);
        session.setAttribute("goodsMap",goodsMap);
        String message="{\"stateCode\":\"1\"}";
        try {
            response.getWriter().write(JSONObject.fromObject(message).toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    private void settlement(HttpServletRequest request, HttpServletResponse response){
        String[] barcodeArry=request.getParameterValues("barcodeArry");
        System.out.println("------"+request.getParameter("balanceRiyong"));
        int balanceRiyong=Integer.parseInt(request.getParameter("balanceRiyong"));
        int balanceFuzhuang=Integer.parseInt(request.getParameter("balanceFuzhuang"));
        String userId=request.getSession().getAttribute("username").toString();
        /*String userId="534";
        System.out.println(request.getSession());
        if(request.getSession(true)!=null){
            System.out.print(request.getSession(true).getAttribute("username"));
        }*/
        String stuID=request.getParameter("stuID");
        String message;//反馈信息
        int stateCode;//状态码，1：成功 0：失败
        String jsonMessage;//返回该前台的json数据
        if(doService.updateStuMoneyService(stuID,balanceRiyong,balanceFuzhuang)){//更新用户假币
            doService.updateGoodsNumAndAddRecord(barcodeArry,stuID,userId);//更新货物库存和添加购买记录
            stateCode=1;
            message="购买成功！！！";
            request.getSession().removeAttribute("goodsNumMap");//清空goodsNumMap
        }else{
            stateCode=0;
            message="余额不足！！！";
        }
        jsonMessage = "{\"stateCode\":\"" + stateCode + "\",\"message\":\"" + message + "\"}";
        try {
            response.getWriter().write(JSONObject.fromObject(jsonMessage ).toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /*public static void main(String[] args){
        String[] barcodeArry={"6958966816521","6958966816521","6958966816521"};
        int balanceRiyong=1;
        int balanceFuzhuang=0;
        String stuID="2016012044";
        String userId="534";
        DoService doService=new DoService();
        if(doService.updateStuMoneyService(stuID,balanceRiyong,balanceFuzhuang)){//更新用户假币
            doService.updateGoodsNumAndAddRecord(barcodeArry,stuID,userId);//更新货物库存和添加购买记录
        }
    }*/
}
