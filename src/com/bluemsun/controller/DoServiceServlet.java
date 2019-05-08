package com.bluemsun.controller;

import com.bluemsun.entity.BuyRecord;
import com.bluemsun.entity.Goods;
import com.bluemsun.entity.Student;
import com.bluemsun.entity.TempBuyInfo;
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
            case "queryInfo":
                queryInfo(request,response);break;//查询用户基本信息
            case "queryGoodsByBarcode":
                queryGoodsByBarcode(request,response);break;//根据条形码查询商品信息
            case "settlement":
                settlement(request,response);break;//结算
            case "deleteGoods":
                deleteGoods(request,response);break;
            case "safeOut":
                safeOut(request,response);break;
            default:
                response.sendRedirect("http://wxy.nenu.edu.cn/aixinWeb/supermarket/index.html");
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
        String stuID=request.getParameter("stuID");
        HttpSession session=request.getSession(true);
        if(session.getAttribute("goodsNumMap")==null){
            Map<String ,TempBuyInfo> goodsNumMap=new HashMap<String,TempBuyInfo>();//用于存储当前已经选择的物品的数量
            session.setAttribute("goodsNumMap",goodsNumMap);
        }
        HashMap<String, TempBuyInfo> goodsMap=(HashMap<String, TempBuyInfo>)session.getAttribute("goodsNumMap");//获取存放条形码数当前选择数量的map
        int num=0;
        Goods goods=doService.getGoodsByCode(barCode);//获取新扫描的货物
        if(goods!=null){
            TempBuyInfo buyInfo=doService.getBuyInfo(barCode,stuID);
            if(goodsMap.get(barCode)==null) {
                goodsMap.put(barCode, buyInfo);
            }
            num=goodsMap.get(barCode).getBuyGoodsNum()+1;//当前选择购买的数量
        }

        //status=1:添加成功 status=0：库存不足 status=-1：超过限购
        int status=1;
        if(goods==null||goods.getNum()<num) {
            status=0;
            goods=null;
        //当限购量是-1时，表示不存在限购
        //当前添加量加上已经购买量>限购量则说明达到限购
        }else if(goodsMap.get(barCode).getLimitBuyNum()!=-1&&(num+goodsMap.get(barCode).getRecordBuyNum())>(goodsMap.get(barCode).getLimitBuyNum())){

            status=-1;
            goods=null;
        } else {
            status=1;
            goodsMap.get(barCode).setBuyGoodsNum(num);
        }
        Map<String,Object> dataMap=new HashMap<String,Object>();
        dataMap.put("status",status);
        dataMap.put("goods",goods);
        try {
            response.getWriter().write(JSONObject.fromObject(dataMap).toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void deleteGoods(HttpServletRequest request, HttpServletResponse response){
        String barcode=request.getParameter("barCode");
        System.out.println("********************"+barcode+"********");
        HttpSession session=request.getSession(true);
        HashMap<String,TempBuyInfo> goodsMap=(HashMap<String,TempBuyInfo>)session.getAttribute("goodsNumMap");
        System.out.println(goodsMap);
        int num=goodsMap.get(barcode).getBuyGoodsNum()-1;
        goodsMap.get(barcode).setBuyGoodsNum(num);
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
        float balanceRiyong=Float.parseFloat(request.getParameter("balanceRiyong"));
        float balanceFuzhuang=Float.parseFloat(request.getParameter("balanceFuzhuang"));
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
