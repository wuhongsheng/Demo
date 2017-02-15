package com.titan.api;


import com.titan.model.Contributor;
import com.titan.retrofit.converter.gson.GsonConverterFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors1(
            @Path("owner") String owner,
            @Path("repo") String repo, Callback<List<Contributor>> response);
    class Factory {
        public static GitHubService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(GitHubService.class);
        }
    }

}
