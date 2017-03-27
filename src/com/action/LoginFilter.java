/*package com.action;

import java.util.Map;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class LoginFilter  extends AbstractInterceptor{

	@SuppressWarnings("rawtypes")
	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		Map  map=arg0.getInvocationContext().getSession();
         if(map.get("user")==null){  
        	 ActionContext.getContext().put("message", "请您先登录在进行操作！");
            return "success";
           }
         else{     
            String result=arg0.invoke();
            return result;
        }
	}
}*/
