package com.titan.service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 用户角色返回body
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "soap12:Body", strict = false)
public class Body {

    public Islogin getCheckLogin() {
        return checkLogin;
    }

    public void setCheckLogin(Islogin checkLogin) {
        this.checkLogin = checkLogin;
    }

    /* @Element(name = "getWeatherbyCityName", required = false)
        public RequestModel getWeatherbyCityName;*/
    @Element(name = "Islogin", required = false)
    private Islogin checkLogin;
}
