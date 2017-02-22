package com.titan.service;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by whs on 2017/2/17
 * Retrofit 初始化
 */


public class RetrofitHelper {
    private Context mCntext;

    private OkHttpClient client = new OkHttpClient();
   /* private OkHttpClient client1=new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            return null;
        }
    }).build();*/
    //GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    //SimpleXmlConverterFactory factory = SimpleXmlConverterFactory.create(new xml);
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    public static RetrofitHelper getInstance(Context context){
        if (instance == null){
            instance = new RetrofitHelper(context);
        }
        return instance;
    }
    private RetrofitHelper(Context mContext){
        mCntext = mContext;
        init();
    }

    private void init() {
        resetApp();

    }

    private void resetApp() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://101.201.54.143:8068/")
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public RetrofitService getServer(){

        return mRetrofit.create(RetrofitService.class);
    }
}
