package com.titan.retrofitdemo;

import android.app.Application;
import android.content.Context;

import com.titan.api.GitHubService;

/**
 * Created by Whs on 2016/12/7 0007
 */
public class MyApplication extends Application {
    public GitHubService gitHubService;
    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }
    public GitHubService getGithubService() {
        if (gitHubService == null) {
            gitHubService = GitHubService.Factory.create();
        }
        return gitHubService;
    }
}
