function _change() {
	$("#vCode").attr("src", "/sshgoods/verifyCodeServlet?" + new Date().getTime());
}