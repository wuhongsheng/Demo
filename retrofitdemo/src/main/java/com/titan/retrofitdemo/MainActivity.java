package com.titan.retrofitdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
                    String Result = responseEnvelope.body.getCheckLoginResponse;
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
}
