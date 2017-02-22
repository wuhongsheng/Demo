package com.titan.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by whs on 2017/2/17
 * Retrofit 接口
 */

public interface RetrofitService {
   /* @Headers({
            "Content-Type: text/xml; charset=utf-8",
            "SOAPAction: http://tempuri.org/Islogin"
    })
    @POST("AppWebService.asmx")
    Observable<Boolean> Islogin(@Query("date") String date, @Query("action") String action);*/
    @Headers({
            //"Content-Type: text/xml; charset=utf-8",
            "Content-Type: application/soap+xml",
            "SOAPAction: http://tempuri.org/Islogin"
    })
    @POST("/AppWebService.asmx")
    Observable<Envelope> Checklogin(@Body Envelope responseEnvelope);
    @Headers({
            //"Content-Type: text/xml; charset=utf-8",
            "Content-Type: application/soap+xml",
            "SOAPAction: http://tempuri.org/Islogin"
    })
    @POST("/AppWebService.asmx")
    Call<Envelope> Checklogin1(@Body Envelope responseEnvelope);
    @GET("/AppWebService.asmx/Islogin")
    Observable<String> Checklogin2(@Query("usercode") String username,@Query("password") String password);
}
