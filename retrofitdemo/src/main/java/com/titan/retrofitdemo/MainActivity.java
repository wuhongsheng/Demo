package com.titan.retrofitdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.titan.api.GitHubService;
import com.titan.api.request.RequestBody;
import com.titan.api.request.RequestEnvelope;
import com.titan.api.request.UserModel;
import com.titan.api.response.ResponseEnvelope;
import com.titan.model.Contributor;
import com.titan.retrofit.converter.RetrofitGenerator;
import com.titan.retrofit.converter.gson.GsonConverterFactory;
import com.titan.service.Body;
import com.titan.service.Envelope;
import com.titan.service.Islogin;
import com.titan.service.RetrofitHelper;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String API_URL = "https://api.github.com";
    TextView tv;
    String info = "";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.info);
        mContext=this;
    }

    public void onGetInfo(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Create a very simple REST adapter which points the GitHub API.
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                // Create an instance of our GitHub API interface.
               // GitHubService github = retrofit.create(GitHubService.class);
                MyApplication myApplication=MyApplication.get(MainActivity.this);
                GitHubService github =myApplication.getGithubService();
                // Create a call instance for looking up Retrofit contributors.
                Call<List<Contributor>> call = github.contributors("square", "retrofit");
                // Fetch and print a list of the contributors to the library.
                List<Contributor> contributors = null;

                try {
                    contributors = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Contributor contributor : contributors) {
                    info = info + "\n" + contributor.login + " (" + contributor.contributions + ")";
                    System.out.println(contributor.login + " (" + contributor.contributions + ")");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(info);
                    }
                });

            }
        }).start();

    }

    /**
     * 登录
     * @param view
     */
    public void onLogin(View view) {
        RequestEnvelope requestEnvelop = new RequestEnvelope();
        RequestBody requestBody = new RequestBody();
        UserModel requestModel = new UserModel();
        requestModel.username = "admin";
        requestModel.password="otitangis";
        requestModel.cityNameAttribute = "http://tempuri.org/";
        requestBody.checkLogin = requestModel;
        requestEnvelop.body = requestBody;
        Call<ResponseEnvelope> call = RetrofitGenerator.getSLFHService().checkLogin(requestEnvelop);
        call.enqueue(new Callback<ResponseEnvelope>() {
            @Override
            public void onResponse(Call<ResponseEnvelope> call, Response<ResponseEnvelope> response) {
                ResponseEnvelope responseEnvelope = response.body();
                if (responseEnvelope != null ) {
                   String Result = responseEnvelope.getBody().toString();
                    //adapter = new WeatherResponseAdapter(weatherResult);
                    //binding.rvElements.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseEnvelope> call, Throwable t) {
                Toast.makeText(mContext,"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 登录1
     * @param view
     */
    public void onLogin1(View view) {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                post();
            }
        }).start();*/
        Islogin islogin=new Islogin();
        islogin.setLoginname("admin");
        islogin.setLoginpsw("kmlyadmin2016");
        Body body=new Body();
        body.setCheckLogin(islogin);

        Envelope envelope=new Envelope();
        envelope.setBody(body);
        Observable<String> observable= RetrofitHelper.getInstance(mContext).getServer().Checklogin2("admin","kmlyadmin2016");
        /*call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res=response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });*/
        observable.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI
                .subscribe(new Observer<String>() {//订阅
                    @Override
                    public void onCompleted() {
                        //所有事件都完成，可以做些操作。。。

                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(); //请求过程中发生错误
                        Toast.makeText(mContext,"登陆失败"+e.toString(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(mContext,"登陆成功"+ s,Toast.LENGTH_LONG).show();
                    }





                });
    }
    private  void post(){
        OkHttpClient client=new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request=chain.request();
                String re=request.body().toString();
                String rs=chain.proceed(request).body().string();
                return null;
            }
        }).build();
        OkHttpClient client1= new OkHttpClient();

       /* client.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                return null;
            }
        });*/
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("http://101.201.54.143:8068/")
                .client(client1)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //RetrofitService retrofitService= RetrofitHelper.getInstance(mContext).getServer();
        Islogin islogin=new Islogin();
        islogin.setLoginname("admin");
        islogin.setLoginpsw("kmlyadmin2016");
        Body body=new Body();
        body.setCheckLogin(islogin);

        Envelope envelope=new Envelope();
        envelope.setBody(body);
        String dd=envelope.toString();
        Call<Envelope> call= RetrofitHelper.getInstance(mContext).getServer().Checklogin1(envelope);
        call.enqueue(new Callback<Envelope>() {
            @Override
            public void onResponse(Call<Envelope> call, Response<Envelope> response) {
                String request=call.request().toString();
                String response1= response.toString();

               // Log.e("dd",response.body().toString());
            }

            @Override
            public void onFailure(Call<Envelope> call, Throwable t) {
                Log.e("error",t.toString());
            }
        });
    }
}
