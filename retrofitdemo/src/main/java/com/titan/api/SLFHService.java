package com.titan.api;


import com.titan.api.request.RequestEnvelope;
import com.titan.api.response.ResponseEnvelope;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SLFHService {
   /* @Headers({"Content-Type: text/xml;charset=UTF-8", "SOAPAction: http://WebXml.com.cn/getWeatherbyCityName"})//请求的Action，类似于方法名
    @POST("WeatherWebService.asmx")*/
    @Headers({"SOAPAction: getRoleinfo"})//请求的Action，类似于方法名
    @POST("service?wsdl")//请求的地址
    Call<ResponseEnvelope> getRoleInfo(@Body RequestEnvelope requestEnvelope);

    @Headers({"SOAPAction: CheckLogin"})//请求的Action，类似于方法名
    @POST("WebService1.asmx?wsdl")//请求的地址
    Call<ResponseEnvelope> checkLogin(@Body RequestEnvelope requestEnvelope);
}
