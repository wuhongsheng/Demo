package com.titan.api.request;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Whs on 2016/12/26 0026.
 */
public class UserModel {
    @Attribute(name = "xmlns")
    public String cityNameAttribute;

    @Element(name = "username", required = false)
    public String username;     //用户名
    @Element(name = "password", required = false)
    public String password;     //密码
}
