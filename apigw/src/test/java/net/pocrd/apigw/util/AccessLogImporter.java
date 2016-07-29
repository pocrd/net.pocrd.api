package net.pocrd.apigw.util;

import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rendong on 14/11/30.
 */
public class AccessLogImporter extends LogImporter {
    private static final int      ACCESS_SPLITER_SIZE     = 6;
    private static final String   create_access_log_talbe =
            "CREATE TABLE `{tableName}` ("
                    + "`id`  bigint(20) NOT NULL AUTO_INCREMENT,"
                    + "`access_time`  bigint(20) NULL COMMENT '访问时间',"
                    + "`access_time_string`  varchar(30) NULL COMMENT '访问时间的字符串值',"
                    + "`thread_name`  varchar(30) NULL COMMENT '线程名',"
                    + "`userId`  varchar(30) NULL COMMENT '用户id',"
                    + "`appId`  varchar(30) NULL COMMENT '应用id',"
                    + "`deviceId`  varchar(30) NULL COMMENT '设备id',"
                    + "`client_ip`  varchar(30) NULL COMMENT '客户ip',"
                    + "`callId`  varchar(50) NULL COMMENT 'http调用id',"
                    + "`cost`  int NOT NULL COMMENT '本接口消耗的时间',"
                    + "`method`  varchar(50) NOT NULL COMMENT '接口名',"
                    + "`return_code`  int NOT NULL COMMENT '对外返回code',"
                    + "`real_code`  int NOT NULL COMMENT '业务接口返回code / 隐藏不对外暴露的code',"
                    + "`result_length`  int NOT NULL COMMENT '接口返回数据的未压缩长度',"
                    + "`request_parameter`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '接口相关的参数',"
                    + "`response_log`  varchar(1000) NULL COMMENT '业务接口返回的关键统计数据',"
                    + "PRIMARY KEY (`id`))"
                    + "ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci AUTO_INCREMENT=1;";
    private static final String[] access_log_add_index    =
            { "alter table `{tableName}` add INDEX `INDEX_ACCESS_TIME` (`access_time`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_USER_ID` (`userId`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_APP_ID` (`appId`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_DEVICE_ID` (`deviceId`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_CLIENT_IP` (`client_ip`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_CALL_ID` (`callId`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_METHOD_ID` (`method`) USING Btree;"
            };
    private static final String   insert_access_log       =
            "insert into `{tableName}` (`access_time`, `access_time_string`, `thread_name`, `userId`, `appId`, `deviceId`, `client_ip`, `callId`, `cost`, `method`, `return_code`, `real_code`, `result_length`, `request_parameter`, `response_log`)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    @Ignore
    @Test
    public void testAccessLogImport() {
        File file = new File(logPath + "/access");
        if (file.exists() && file.isDirectory()) {
            File[] accesses = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.startsWith("access") && s.endsWith(".log");
                }
            });
            if (accesses != null) {
                for (File f : accesses) {
                    importAccessLogs(f);
                }
            }
        }
    }

    private void fillAccessLog(String line, PreparedStatement pst) throws SQLException {
        String[] es = line.split(LOG_SPLITER);
        {
            String element = es[0];
            ParsePosition pp = new ParsePosition(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = sdf.parse(element, pp);
            pst.setLong(1, date.getTime());  // long型时间
            pst.setString(2, sdf.format(date)); // String型时间
            int index1 = element.indexOf("]: ", pp.getIndex());
            if (index1 > 0) {
                pst.setString(3, element.substring(pp.getIndex() + 2, index1)); // 线程名
                pp.setIndex(index1);
            }
            int index2 = element.indexOf("x:(", pp.getIndex());
            int index3 = element.indexOf(") msg:", pp.getIndex());
            if (index2 > 0 && index3 > 0) {
                String info = element.substring(index2 + 1, index3);
                pst.setString(4, getValueAfterKey(info, "_uid=")); // 用户id
                pst.setString(5, getValueAfterKey(info, "_aid=")); // app id
                pst.setString(6, getValueAfterKey(info, "_did=")); // device id
                pst.setString(7, getValueAfterKey(info, "_cip=")); // client ip
                pst.setString(8, getValueAfterKey(info, "_cid=")); // call id
                pp.setIndex(index3 + 6);
            }
            pst.setInt(9, Integer.parseInt(element.substring(pp.getIndex()))); // cost
        }
        pst.setString(10, es[1]); // method
        pst.setInt(11, Integer.parseInt(es[2])); // return_code
        pst.setInt(12, Integer.parseInt(es[3])); // real_code
        pst.setInt(13, Integer.parseInt(es[4])); // result_length
        if (es.length > 5) {
            pst.setString(14, es[5]);
        } else {
            pst.setString(14, null);
        }
        pst.setString(15, null);
        if (es.length > 6) {
            String log = es[6];
            if (log != null && log.length() > 0) {
                if (!"null".equals(log)) {
                    pst.setString(15, log); // response_log
                }
            }
        }
    }

    private void importAccessLogs(File file) {
        Connection conn = getConnection();
        if (conn == null) {
            return;
        }
        String tableName = "access-" + getFileDate(file);
        BufferedReader br = null;
        PreparedStatement pst = null;
        String line = null;

        try {
            System.out.print("file " + file.getName() + " ");
            conn.setAutoCommit(false);
            br = new BufferedReader(new FileReader(file));
            boolean tableExist = isTableExist(conn, tableName);
            if (!tableExist) {
                Statement statement = conn.createStatement();
                try {
                    statement.execute(create_access_log_talbe.replace("{tableName}", tableName));
                } finally {
                    closeQuietly(null, null, statement);
                    conn.commit();
                }
            }
            pst = conn.prepareCall(insert_access_log.replace("{tableName}", tableName));

            int lineNumber = 0;
            int num = 0;
            new_line:
            while ((line = br.readLine()) != null) {
                if ((num = spliterNumber(line)) < ACCESS_SPLITER_SIZE) {
                    do {
                        int n = 0;
                        String l = br.readLine();
                        if (l == null) {
                            logger.error("unfinished line:" + line);
                            break new_line;
                        } else {
                            n = spliterNumber(l);
                            if (n == ACCESS_SPLITER_SIZE && num != 0) {
                                logger.error("broken line:" + line);
                                num = n;
                                line = l;
                                break;
                            } else {
                                line += l;
                                num += n;
                            }
                        }
                    } while (num < ACCESS_SPLITER_SIZE);
                }
                if (num != ACCESS_SPLITER_SIZE) {
                    logger.error("broken line:" + line);
                    continue;
                }
                fillAccessLog(line, pst);
                lineNumber++;
                try {
                    pst.executeUpdate();
                } catch (SQLException se) {
                    logger.error("line error:" + line, se);
                    System.out.println(se.getMessage() + "line error:" + line);
                }
                if (lineNumber % numberToCommit == 0) {
                    conn.commit();
                    System.out.print(".");
                }
            }
            if (lineNumber % numberToCommit != 0) {
                conn.commit();
            }
            if (!tableExist) {
                Statement statement = conn.createStatement();
                try {
                    for (String sql : access_log_add_index) {
                        statement.execute(sql.replace("{tableName}", tableName));
                    }
                } finally {
                    closeQuietly(null, null, statement);
                    conn.commit();
                }
            }
            System.out.println(" imported with " + lineNumber + " lines.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception e1) {
                    logger.error(e1.getMessage(), e1);
                }
            }
            throw new RuntimeException(" line:" + line, e);
        } finally {
            closeQuietly(conn, null, pst);
        }
    }
}
