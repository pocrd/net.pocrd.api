package net.pocrd.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author guankaiqiang 
 * Test
 */
public class TestDAO extends BaseDAO {
    private final static Logger logger = LogManager.getLogger(TestDAO.class);
    public void testC3p0() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet result = null;
        int totalcount = 0;
        try {
            pst = conn.prepareCall("select count(1) from tb_product");
            result = pst.executeQuery();
            if (result.next()) {
                totalcount = result.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            closeQuietly(conn, result, pst);
        }
    }
    public void testCommonConnect(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn=null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://112.124.17.212:3306/test?useUnicode=true&amp;characterset=utf-8&user=gkq&password=gkq1990");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        PreparedStatement pst = null;
        ResultSet result = null;
        int totalcount = 0;
        try {
            pst = conn.prepareCall("select count(1) from tb_product");
            result = pst.executeQuery();
            if (result.next()) {
                totalcount = result.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            closeQuietly(conn, result, pst);
        }
    }
}
