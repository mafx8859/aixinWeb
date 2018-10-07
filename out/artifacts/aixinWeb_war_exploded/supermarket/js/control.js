var coinRiyong = 0;
var coinFuzhuang = 0;
var ownRiyong = 0;
var ownFuzhuang = 0;
var jqxhr;
//设置ajax请求完成后运行的函数,
$.ajaxSetup({
    complete:function(){
        if("REDIRECT" == jqxhr.getResponseHeader("REDIRECT")){ //若HEADER中含有REDIRECT说明后端想重定向，
            var win = window;
            while(win != win.top){
                win = win.top;
            }
            win.location.href = jqxhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
        }
    }
});
$(function(){
    function loadID(){
        $.ajax({
            async:false,
            type:"POST",
            url:"http://wxy.nenu.edu.cn/aixinWeb/doLogin?flag=getUsername",
            dataType:"json",
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success:function(x,y,xhr){
                $('#sellerID').html(x.username);
                jqxhr = xhr;
            },
            error:function(jqXHR){
                console.log(jqXHR);
            }
        });
        return jqxhr;
    };
    loadID();

    $("#thesearch").click(function(){
        var stuID = $("#stuID").val();
        var thelist = $("#buyRecord");
        thelist.children().remove();
        if(stuID==""||(stuID!="" && !/^[^ ]+$/.test(stuID)))
        {
            alert("输入格式不合法");
        }
        else{
            $.ajax({
                async:false,
                type:"POST",
                url:"http://wxy.nenu.edu.cn/aixinWeb/doService?flag=queryInfo",
                dataType:"json",
                data:{
                    stuID:stuID
                },
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                success:function(x,y,xhr){
                    jqxhr = xhr;
                    $("#stuName").val(x.stuInfo.name);
                    $("#departmentName").val(x.stuInfo.departmentName);
                    $("#specialName").val(x.stuInfo.specialName);
                    $("#grade").val(x.stuInfo.grade);
                    $("#imburseTypeName").val(x.stuInfo.imburseTypeName);
                    $("#balanceRiyong").val(x.stuInfo.balanceRiyong);
                    ownRiyong = x.stuInfo.balanceRiyong;
                    ownFuzhuang = x.stuInfo.balanceFuzhuang;
                    $("#balanceFuzhuang").val(x.stuInfo.balanceFuzhuang);
                    for (var i = 0; i < x.buyRecord.length; i++) {
                        if (x.buyRecord[i].compus == '0') {
                            var part = "本部";
                        }
                        else {
                            var part = "净月";
                        }
                        thelist.append("<tr><td>" + x.buyRecord[i].categoryName + "</td>"
                            +"<td>" + part+ "</td>"
                            +"<td>" + x.buyRecord[i].time + "</td>"
                            +"<td>" + x.buyRecord[i].totalMoney + "</td></tr>");
                    };
                },
                error:function(jqXHR){
                    console.log(jqXHR);
                }
            });
            return jqxhr;
        }

    });


    $('#theconfig').click(function(){
        var stuName = $('#stuName').val();
        var barCode = $('#barCode').val();
        if (stuName == '' || stuName == null) {
            alert("请输入学生学号");
        }
        else if (barCode == '' || barCode == null) {
            alert("请输入条形码");
        }
        else{
            $.ajax({
                async:false,
                type:"POST",
                url:"http://wxy.nenu.edu.cn/aixinWeb/doService?flag=queryGoodsByBarcode",
                dataType:"json",
                data:{
                    barCode:barCode
                },
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                success:function(x,y,xhr){
                    jqxhr = xhr;
                    if(x == null){
                        alert("该商品不存在或库存不足");
                    }
                    else{
                        var shopList = $('#shopList');
                        var thetype;
                        if (x.coinType == 0) {
                            thetype = "日用币";
                            totalRiyong(x.price);
                        }
                        else {
                            thetype = "服装币";
                            totalFuzhuang(x.price);
                        }
                        shopList.append("<tr><td class='thebarcode'>" + x.barcode + "</td><td>"
                            + x.categoryName + "</td><td>"
                            + "<span class='theprice'>" + x.price +"</span>" + "&nbsp" + "<span class='thecointype'>"+ thetype + "</span>" + "</td><td>"
                            + "<button class='deltheDom'>删除</button></td></tr>");

                        delDom();
                    }

                },
                error:function(jqXHR){
                    console.log(jqXHR);
                }
            });
            return jqxhr;
        }
    });
})
window.onload = function(e){
    var code = "";
    var lastTime,nextTime;
    var lastCode,nextCode;

    document.onkeypress = function(e) {
        nextCode = e.which;
        nextTime = new Date().getTime();

        if(lastCode != null && lastTime != null && nextTime - lastTime <= 30) {
            code += String.fromCharCode(lastCode);
        } else if(lastCode != null && lastTime != null && nextTime - lastTime > 100){
            code = "";
        }

        lastCode = nextCode;
        lastTime = nextTime;
    }

    this.onkeypress = function(e){
        if(e.which == 13){
            var stuName = $('#stuName').val();
            var barCode = $('#barCode').val();
            if (stuName == '' || stuName == null) {
                alert("请输入学生学号");
            }
            else{
                $.ajax({
                    async:false,
                    type:"POST",
                    url:"http://wxy.nenu.edu.cn/aixinWeb/doService?flag=queryGoodsByBarcode",
                    dataType:"json",
                    data:{
                        barCode:code
                    },
                    xhrFields: {
                        withCredentials: true
                    },
                    crossDomain: true,
                    success:function(x,y,xhr){
                        jqxhr = xhr;
                        if(x == null){
                            alert("该商品不存在或库存不足");
                        }
                        else{
                            var shopList = $('#shopList');
                            var thetype;
                            if (x.coinType == 0) {
                                thetype = "日用币";
                                totalRiyong(x.price);
                            }
                            else {
                                thetype = "服装币";
                                totalFuzhuang(x.price);
                            }
                            shopList.append("<tr><td class='thebarcode'>" + x.barcode + "</td><td>"
                                + x.categoryName + "</td><td>"
                                + "<span class='theprice'>" + x.price +"</span>" + "&nbsp" + "<span class='thecointype'>"+ thetype + "</span>" + "</td><td>"
                                + "<button class='deltheDom'>删除</button></td></tr>");
                            delDom();
                        }

                    },
                    error:function(jqXHR){
                        console.log(jqXHR);
                    }
                });
                return jqxhr;
            };
            code = "";
        }
    }
}
function delDom() {

    var deltheDom = $('.deltheDom');
    for (var i = 0; i < deltheDom.length; i++) {
        deltheDom[i].onclick=(function(i){
            return function(){
                var thebarcode = $(this).parent().siblings('.thebarcode').text();
                var that = $(this);
                var con;
                con=confirm("确认删除该商品?");
                if (con == true) {
                    $.ajax({
                        async:false,
                        type:"POST",
                        url:"http://wxy.nenu.edu.cn/aixinWeb/doService?flag=deleteGoods",
                        dataType:"json",
                        data:{
                            barCode:thebarcode
                        },
                        xhrFields: {
                            withCredentials: true
                        },
                        crossDomain: true,
                        success:function(x,y,xhr){
                            jqxhr = xhr;
                            if (that.parent().siblings().children('.thecointype').text() == "日用币") {
                                var coin = that.parent().siblings().children('.theprice').text();
                                delRiyong(coin);
                            }
                            else {
                                var coin = that.parent().siblings().children('.theprice').text();
                                delFuzhuang(coin);
                            }
                            that.parent().parent().remove();
                        },
                        error:function(jqXHR){
                            console.log(jqXHR);
                            console.log("删除失败");
                        }
                    });
                    return jqxhr;
                }
            }
        })(i)
    }
};
function totalRiyong(a){
    coinRiyong += a;
    $('#shopRiyong').val(coinRiyong);
};
function totalFuzhuang(a){
    coinFuzhuang += a;
    $('#shopFuzhuang').val(coinFuzhuang);
};
function delRiyong(a){
    coinRiyong -= a;
    $('#shopRiyong').val(coinRiyong);
};
function delFuzhuang(a){
    coinFuzhuang -= a;
    $('#shopFuzhuang').val(coinFuzhuang);
};
$('#balance').click(function(){
    var lastsubmit = confirm("确认结算?");
    if (lastsubmit == true ) {
        if ($('#stuName').val() == '' || $('#stuName').val() == null) {
            alert("请输入学生学号");
        }
        else if (coinFuzhuang > ownFuzhuang || coinRiyong > ownRiyong) {
            alert("余额不足");
        }
        else{
            var allCode = new Array();
            var lastID = $("#stuID").val()
            $(".thebarcode").each(function(){
                allCode.push($(this).html());
            });
            $.ajax({
                async:false,
                type:"POST",
                url:"http://wxy.nenu.edu.cn/aixinWeb/doService?flag=settlement",
                dataType:"json",
                data: {
                    stuID:lastID,
                    barcodeArry:allCode,
                    balanceRiyong:coinRiyong,
                    balanceFuzhuang:coinFuzhuang
                },
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                traditional: true,
                success:function(x,y,xhr){
                    jqxhr = xhr;
                    alert("购买成功");
                    $('#shopList').empty();
                    coinRiyong = 0;
                    coinFuzhuang = 0;
                    $('#shopRiyong').val(coinRiyong);
                    $('#shopFuzhuang').val(coinFuzhuang);
                    $("#thesearch").click();
                },
                error:function(jqXHR){
                    console.log(jqXHR);
                }
            });
            return jqxhr;
        }

    }
});