package com.titan.api.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import retrofit2.http.Body;

/**
 * 用户角色返回总信息
 * Created by SmileXie on 16/7/15.
 */
@Root(name = "soap12:Envelope")
@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
        @Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd"),
        @Namespace(reference = "http://www.w3.org/2003/05/soap-envelope/", prefix = "soap12"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/encoding/", prefix = "enc"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soapenv")
})
public class ResponseEnvelope {

    /*@Element(name = "soap12:Body",required = false)
        public Header header;*/
    @Element(name = "soap12:Body",required = false)
    public Body body;
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }


}

