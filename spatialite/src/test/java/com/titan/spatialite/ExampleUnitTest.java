package com.titan.spatialite;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void exportShp(){

        Properties prop = new Properties();
        prop.setProperty("enable_shared_cache", "true");
        prop.setProperty("enable_load_extension", "true");
        prop.setProperty("enable_spatialite", "true");
        Connection conn = null;
        String path="/Users/whs/Documents/云南审计眼/db_test.sqlite";
        try {
            //conn = DbHelper.getConnection(path,prop);
            conn= DriverManager.getConnection("jdbc:spatialite:"+path+"", prop);
            Statement stat = conn.createStatement();
            /*stat.execute("select SPATIALITE_SECURITY=relaxed");
            stat.close();*/
           //**
            stat = conn.createStatement();
            stat.execute("select ExportSHP('fxh','Geometry','Test','UTF-8','MULTIPOLYGON')");

            //ResultSet rs = stat.executeQuery("SELECT * FROM geometry_columns");
            stat.close();
            if (conn != null)
                conn.close();
            System.out.print("成功:");

        }
        catch (SQLException e) {
            //e.printStackTrace();
            System.out.print("异常:"+e);
        }
    }
    /*public static Connection getConnection(String path,Properties prop) throws SQLException {
        return DriverManager.getConnection("jdbc:spatialite:'"+path+"'", prop);
    }*/
}