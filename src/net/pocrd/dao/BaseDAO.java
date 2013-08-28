package net.pocrd.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import net.pocrd.util.C3P0Utils;
import net.pocrd.util.CommonConfig;
import net.pocrd.webapi.book.GetBookInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseDAO {
    private final static Logger logger = LogManager.getLogger(GetBookInfo.class);
    // public static Connection getConnection() {
    // Connection conn = null;
    // try {
    // conn = C3P0Utils.getConnection();
    // } catch (Exception e) {
    // logger.error(e);
    // }
    // return conn;
    // }

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            logger.error(e);
        } catch (IllegalAccessException e) {
            logger.error(e);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager
                    .getConnection(CommonConfig.Instance.connectString);
        } catch (SQLException e) {
            logger.error(e);
        }
        return conn;
    }

    public static final void closeQuietly(Connection conn, ResultSet rs, Statement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }
}
