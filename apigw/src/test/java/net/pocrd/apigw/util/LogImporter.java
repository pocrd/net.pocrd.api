package net.pocrd.apigw.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

/**
 * Created by rendong on 14/11/29.
 */
public class LogImporter {
    protected static final Logger logger             = LogManager.getLogger(LogImporter.class);
    protected static final String LOG_SPLITER        = new String(new char[] { ' ', 1 });
    protected static final String logPath            = "/Users/rendong/software/mydata";
    protected static final String dbConnectionString = "jdbc:mysql://localhost:3306/logs?autoReconnect=true&useUnicode=true&characterset=utf-8";
    protected static final int    numberToCommit     = 2000;

    protected static DataSource datasource = null;

    protected static int spliterNumber(String line) {
        int num = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == 1) {
                num++;
            }
        }
        return num;
    }

    protected static String getValueAfterKey(String source, String key) {
        int index1 = source.indexOf(key);
        if (index1 < 0) {
            return null;
        }
        int index2 = source.indexOf(",", index1);
        if (index2 < 0) {
            return source.substring(index1 + key.length());
        } else {
            return source.substring(index1 + key.length(), index2);
        }
    }

    protected static boolean isTableExist(Connection conn, String name) throws SQLException {
        ResultSet rs = null;
        try {
            rs = conn.getMetaData().getTables(null, null, name, null);
            return rs.next();
        } finally {
            closeQuietly(null, rs, null);
        }
    }

    protected static String getFileDate(File file) {
        String name = file.getName();
        int first = name.indexOf("-");
        int last = name.lastIndexOf("-");
        if (first >= 0 && last >= 0 && last - first > 0) {
            return name.substring(first + 1, last);
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(new java.util.Date(file.lastModified()));
    }

    public static final Connection getConnection() {
        try {
            return datasource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static final void closeQuietly(Connection conn, ResultSet rs, Statement st) {
        SQLException dbException = null;
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("result set close failed.", e);
            dbException = e;
        }
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            logger.error("statement close failed.", e);
            if (dbException != null) {
                dbException = e;
            }
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("connection close failed.", e);
            if (dbException != null) {
                dbException = e;
            }
        }

        if (dbException != null) {
            throw new RuntimeException(dbException);
        }
    }

    static {
        PoolProperties p = new PoolProperties();
        p.setUrl(dbConnectionString);
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername("haitao");
        p.setPassword("haitao123");
        p.setJmxEnabled(false);
        p.setTestWhileIdle(true);
        p.setTestOnBorrow(true);
        p.setTestOnReturn(false);
        p.setValidationQuery("select 1");
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setMinIdle(10);
        p.setMaxIdle(100);
        p.setLogAbandoned(false);
        p.setRemoveAbandoned(true);
        p.setRemoveAbandonedTimeout(60);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource = new DataSource();
        datasource.setPoolProperties(p);
    }
}
