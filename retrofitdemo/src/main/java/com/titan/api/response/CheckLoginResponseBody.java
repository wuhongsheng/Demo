package com.titan.api.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Whs on 2016/12/26 0026.
 */
@Root(name = "Body")
public class CheckLoginResponseBody {
    @Element(name = "CheckLoginResponse", required = false)
    public String getCheckLoginResponse;
}
