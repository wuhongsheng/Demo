package com.titan.spatialite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by whs on 2017/10/20
 */

public class DbHelper {

    /**
     * 获取连接
     * @param prop
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(Properties prop) throws SQLException {
        return DriverManager.getConnection("jdbc:spatialite::memory:", prop);
    }

    public static Connection getConnection(String path,Properties prop) throws SQLException {
        return DriverManager.getConnection("jdbc:spatialite:'"+path+"'", prop);
    }

}
