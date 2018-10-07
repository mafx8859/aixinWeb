var userName = $('#userName');
var password = $('#password');
userName.blur(function () {
	if (userName.val() == "" || userName.val() == null) {
		$('#userName-error').show();
		userName.addClass("field_message");
	}
	else {
		$('#userName-error').hide();
		userName.removeClass("field_message");
	}
});
password.blur(function () {
	if (password.val() == "" || password.val() == null) {
		$('#password-error').show();
		password.addClass("field_message");
	}
	else {
		$('#password-errorr').hide();
		password.removeClass("field_message");
	}
});
$(function(){
	$("#thesubmit").click(function(){
		var un=$("#userName").val();
		var pw=$("#password").val();
		if(un==""||(un!="" && !/^[^ ]+$/.test(un)))
		{
			alert("输入格式不合法");
		}
		else{
				$.ajax({
					type:"POST",
					url:"http://wxy.nenu.edu.cn/aixinWeb/doLogin?flag=login",
					dataType:"json",
					data:{
						username:un,
						password:pw
					},
				  	success:function(x,y){
				  		if(x.stateCode == '0')
				  		{
				  			alert(x.message);
				  		}
				  		else{
				  			window.location.href = "html/control.html";
				  		}		
				  	},
				  	error:function(jqXHR){
				  		console.log(jqXHR);
				  	}
				});
		}
		
	});
})