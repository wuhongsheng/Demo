package com.titan.demo;
/**   
* @Title: OtitanSpatialiteHelper.java 
* @Package com.otitan.android 
* @Description: 
* @author whs HDstudio  
* @date 2015-9-6 下午2:55:16 
* @version V1.0   
*/
import abc.abc.abc.ResourcesManager;
import abc.abc.abc.TestActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class OtitanSpatialiteHelper extends SQLiteOpenHelper {
	public OtitanSpatialiteHelper(Context context,String dburl) {
		
		super(context, dburl, null, 1);
	}


    @Override
    public void onCreate(SQLiteDatabase db) {
          // "ROCK.db" db.execSQL("create table Rock (id integer primary key autoincrement,name varchar(20),latitude double,longitude  double)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


	
}
