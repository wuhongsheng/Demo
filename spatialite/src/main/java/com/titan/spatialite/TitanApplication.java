package com.titan.spatialite;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Created by Whs on 2016/12/9 0009
 */
public class TitanApplication extends Application {


    /** 震动器（百度定位） */
    Vibrator mVibrator;
    static Context mContext;
    public  static  TitanApplication singleton;


    /** 数据存储路径 */
    static  String filePath = null;
    /** 是否首次定位 */
    boolean isfirstlogin=true;
    private LinkedList<Activity> activityLinkedList = new LinkedList<Activity>();
    public static ActivityManager instance;
    //推送消息接收界面
    public static MainActivity mainActivity;
    //透传数据
    public static String  payloadData;
    /** 用户信息 */
    public static final String PREFS_NAME = "MyPrefsFile";
    //是否记住用户信息
    public static final String KEYNAME_REMEMBER = "isremember";
    public static final String KEYNAME_USERNAME = "username";
    public static final String KEYNAME_PSD = "password";
    public static final String KEYNAME_ISTRACK = "istrack";
    public static final String KEYNAME_UPTRACKPOINT = "uptrackpoint";
    public static SharedPreferences mSharedPreferences;

    public static TitanApplication getInstance() {
        if(singleton!=null){
            return singleton;
        }else {
            return new TitanApplication();
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this.getApplicationContext();
        singleton=this;
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        Properties prop = new Properties();
        prop.setProperty("enable_shared_cache", "true");
        prop.setProperty("enable_load_extension", "true");
        prop.setProperty("enable_spatialite", "true");
        Connection conn = null;
        try {
            conn = DbHelper.getConnection(prop);
            Statement stat = conn.createStatement();
            stat.execute("SELECT InitSpatialMetaData()");
            stat.close();
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM geometry_columns");
            stat.close();
            if (conn != null)
                conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
