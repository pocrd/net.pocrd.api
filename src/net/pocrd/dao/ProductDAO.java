package net.pocrd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.pocrd.dao.entity.ProductInfo;
import net.pocrd.data.Page;

/**
 * @author guankaiqiang TODO:ORM，使用了Tomcat JDBC连接池
 */
public class ProductDAO extends BaseDAO {
    private final static Logger logger = LogManager.getLogger(ProductDAO.class);
    /**
     * 最简单的实现
     * 
     * @param ceiling
     * @param cycle
     * @param categoryId
     * @param pageIndex
     * @param pageSize
     * @param orderby
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("finally")
    public Page<ProductInfo> getProductInfoList(int ceiling, int cycle, int categoryId, int pageIndex, int pageSize, int orderby) {
        Connection conn = getConnection();
        if (conn == null) return null;
        PreparedStatement pst = null;
        ResultSet result = null;
        Page<ProductInfo> products = null;
        try {
            // 1.获取总数
            int totalcount = 0;
            pst = conn.prepareCall("select count(1) from tb_product where state= 0 and ceiling=? and cycle=? and categoryId=?");
            pst.setObject(1, ceiling);
            pst.setObject(2, cycle);
            pst.setObject(3, categoryId);
            result = pst.executeQuery();
            if (result.next()) {
                totalcount = result.getInt(1);
            }
            // 2.获取分页内容
            int startIndex = (pageIndex - 1) * pageSize;
            if (startIndex + 1 > totalcount) {
                return null;
            }
            products = new Page<ProductInfo>(totalcount, pageIndex, pageSize);
            String sql = null;
            if (orderby == 1)
                sql = "select t.*,t2.corporationName,t2.description,t2.officialSite,t3.categoryName "
                        + "from tb_product t LEFT JOIN tb_corporation t2 on t.corporationId=t2.corporationId "
                        + "LEFT JOIN tb_product_cate t3 on t.categoryId=t3.categoryId "
                        + "where t.state= 0 and t.ceiling=? and t.cycle=? and t.categoryId=? order by t.monthlyPayment desc limit ?,?";
            else if (orderby == 2)
                sql = "select t.*,t2.corporationName,t2.description,t2.officialSite,t3.categoryName "
                        + "from tb_product t LEFT JOIN tb_corporation t2 on t.corporationId=t2.corporationId "
                        + "LEFT JOIN tb_product_cate t3 on t.categoryId=t3.categoryId "
                        + "where t.state= 0 and t.ceiling=? and t.cycle=? and t.categoryId=? order by t.interest desc limit ?,?";
            else sql = "select t.*,t2.corporationName,t2.description,t2.officialSite,t3.categoryName "
                    + "from tb_product t LEFT JOIN tb_corporation t2 on t.corporationId=t2.corporationId "
                    + "LEFT JOIN tb_product_cate t3 on t.categoryId=t3.categoryId "
                    + "where t.state= 0 and t.ceiling=? and t.cycle=? and t.categoryId=? limit ?,?";
            pst = conn.prepareCall(sql);
            pst.setObject(1, ceiling);
            pst.setObject(2, cycle);
            pst.setObject(3, categoryId);
            pst.setObject(4, startIndex);
            pst.setObject(5, pageSize);
            result = pst.executeQuery();
            ProductInfo info = null;
            while (result.next()) {
                info = new ProductInfo();
                info.setProductId(result.getInt("productid"));
                info.setProductName(result.getString("productname"));
                info.setCorporationId(result.getInt("corporationid"));
                info.setCorporationName(result.getString("corporationName"));
                info.setCeiling(result.getInt("ceiling"));
                info.setCycle(result.getInt("cycle"));
                info.setRate(result.getDouble("rate"));
                info.setInterest(result.getInt("interest"));
                info.setMonthlyPayment(result.getInt("monthlyPayment"));
                info.setCategoryId(result.getInt("categoryId"));
                info.setCategoryName(result.getString("categoryName"));
                info.setAgeRange(result.getInt("floorAge") + "-" + result.getInt("upperAge"));
                info.setFloorWorkExp(result.getInt("floorWorkExp"));
                info.setFloorSalary(result.getInt("floorSalary"));
                info.setReleaseTime(dateFormat(result.getDate("releasetime")));
                {// 这四个字段后期可能是作为多选形式存在，并且可能参与查找匹配
                    info.setIndustry(getIndustryName(result.getInt("industry")));
                    info.setLocation(getLocationName(result.getInt("location")));
                    info.setMortgage(getMortgageName(result.getInt("mortgage")));
                    info.setMaterial(result.getString("material"));
                }
                info.setOtherReq(result.getString("otherReq"));
                products.addItem(info);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            closeQuietly(conn, result, pst);
            return products;
        }
    }

    private static String dateFormat(Date date) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(date != null ? date : new Date());
    }

    private static String getLocationName(int location) {
        if (location == 0)
            return "上海";
        else throw new IllegalArgumentException("尚未定义的城市编号:" + location);
    }

    private static String getIndustryName(int industry) {
        if (industry == 0)
            return "无限制";
        else throw new IllegalArgumentException("尚未定义的行业编号:" + industry);
    }

    private static String getMortgageName(int mortgage) {
        if (mortgage == 0)
            return "无抵押";
        else throw new IllegalArgumentException("尚未定义的抵押类型:" + mortgage);
    }
}
