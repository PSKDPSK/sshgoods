//希望传入的e是jquery对象
function showHideError(e) {
	var txt = e.text();
	if (!txt) {
		e.css("display", "none");
	} else {
		e.css("display", "");
	}
}
function showHideErrorByLabelId(ele,txt) {
	var labelId = ele.attr("id") + "Error";
	$("#" + labelId).text(txt);
	showHideError($("#" + labelId));
}

function showHideErrorByLabelIdOther(ele,txt) {
	var labelId = ele.attr("id") + "Error";
	$("#" + labelId).text(txt);
	$("#"+labelId).attr("class","errorClass2");
	showHideError($("#" + labelId));
}

//校验登入名的文本框
function validateLoginname() {
	var bl=false;
	var value = $.trim($("#loginname").val());
	if(!value){
		showHideErrorByLabelId($("#loginname"),"用户名不能为空");
	}else if(value.length<6 || value.length>20){
		showHideErrorByLabelId($("#loginname"),"用户名长度要在6~20之间");
	}else{
		$.post(
			"/sshgoods/existLoginname",
			{"loginname":value},	
			function(data){
				if(data.status == "exist"){
					showHideErrorByLabelId($("#loginname"),"用户名被占用");
				}else{
					showHideErrorByLabelIdOther($("#loginname")," ");
					 bl=true;
				}
			},
			"json"
		)
	}
	return bl;
}
//校验密码的文本框
function validateLoginpass() {
	var bl = false;
	var value = $.trim($("#loginpass").val());
	if(!value) {
		// 非空校验
		$("#loginpassError").css("display", "");
		$("#loginpassError").text("密码不能为空！");
	} else if(value.length < 6 || value.length > 20) {
		//长度校验
		$("#loginpassError").css("display", "");
		$("#loginpassError").text("密码长度必须在6 ~ 20之间！");
		
	}else{
		showHideErrorByLabelIdOther($("#loginpass")," ");
		bl = true;
	}
	return bl;
}

//校验确认密码的文本框
function validateReloginpass() {
	var bl = false;
	var value = $("#reloginpass").val();
	if(!value) {
		// 非空校验
		$("#reloginpassError").css("display", "");
		$("#reloginpassError").text("确认密码不能为空！");
	} else if(value != $("#loginpass").val()) {
		//两次输入是否一致
		$("#reloginpassError").css("display", "");
		$("#reloginpassError").text("两次密码输入不一致！");
	}else{
		showHideErrorByLabelIdOther($("#reloginpass")," ");
		bl = true;
	}
	return bl;
}
//校验邮箱的文本框
function validateEmail() {
	var bl = false;
	var value = $.trim($("#email").val());
	if(!value) {
		// 非空校验
		$("#emailError").css("display", "");
		$("#emailError").text("Email不能为空！");
	} else if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)) {
		//格式校验
		$("#emailError").css("display", "");
		$("#emailError").text("错误的Email格式！");
	} else {
		//Email是否被注册过
		$.post(
			"/sshgoods/validateEmail",
			{"email":value},
			function(data){
				if(data.status == "exist"){
					showHideErrorByLabelId($("#email"),"邮箱被占用");
				}else{
					showHideErrorByLabelIdOther($("#email")," ");
					bl = true;
				}
			},
			"json"
		)
	}
	return bl;	
}
//校验验证码的文本框
function validateVerifyCode() {
	var bl = true;
	var value = $.trim($("#verifyCode").val());
	if(!value) {
		//非空校验
		$("#verifyCodeError").css("display", "");
		$("#verifyCodeError").text("验证码不能为空！");
	} else if(value.length != 4) {
		//长度不为4就是错误的
		$("#verifyCodeError").css("display", "");
		$("#verifyCodeError").text("错误的验证码！");
	} else {
		$.post(
			"/sshgoods/validateVerifyCode",
			{"verifyCode":value},
			function(data){
				if(data.status == "nopass"){
					showHideErrorByLabelId($("#verifyCode"),"验证码不正确");
				}else{
					showHideErrorByLabelIdOther($("#verifyCode")," ");
					bl = true;
				}
			},
			"json"			
		)
	}
	return bl;
}

function invokeValidateFunction(iid) {
	iid = iid.substring(0,1).toUpperCase() + iid.substring(1);
	var fname = "validate" + iid;
	return eval(fname + "()");
}

$(function() {
	$("#repImg").click(function() {
		$("#vCode").attr("src","/sshgoods/verifyCodeServlet?a=" + new Date().getTime());
	});
	// 当光标划过按钮的时候，替换响应图片
	$("#btnSubmit").hover(
		// 当光标放入执行
		function() {
			$(this).attr("src", "/sshgoods/images/regist1.jpg");
		},
		// 当光标离开时执行
		function() {
			$(this).attr("src", "/sshgoods/images/regist2.jpg");
		});
	// 当光标划过按钮的时候，替换响应图片
	$(".errorClass").each(function() {
		showHideError($(this));
	});
	//获得焦点事件
	$(".input").focus(function() {
		showHideErrorByLabelId($(this),"");
	});
	//失去焦点事件
	$(".input").blur(function(){
		var iid=$(this).attr("id");
		invokeValidateFunction(iid);
	});
	//提交按钮被点击事件
	$("#regform").submit(function(){
		var bl = true;
		$(".input").each(function() {
			var inputName = $(this).attr("name");
			bl = invokeValidateFunction(inputName);
		});
		return bl;
	});	
});