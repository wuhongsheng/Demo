package com.titan.service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by whs on 2017/2/20
 */
@Root(name = "Islogin",strict = false)
@Namespace(reference = "http://tempuri.org/")
public class Islogin {
    @Element(name = "usercode",required = false)
    private String loginname;
    @Element(name = "password",required = false)
    private String loginpsw;

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLoginpsw() {
        return loginpsw;
    }

    public void setLoginpsw(String loginpsw) {
        this.loginpsw = loginpsw;
    }
}
