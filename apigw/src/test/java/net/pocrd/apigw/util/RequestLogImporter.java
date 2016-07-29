package net.pocrd.apigw.util;

import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.Environment;

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
public class RequestLogImporter extends LogImporter {
    private static final int      REQUEST_SPLITER_SIZE     = 5;
    private static final String   create_request_log_talbe =
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
                    + "`request_url`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '客户端请求字符串',"
                    + "`user_agent`  varchar(500) NULL COMMENT 'http User-Agent',"
                    + "`token`  varchar(200) NULL COMMENT '用户身份凭据',"
                    + "`url_parse_code`  int default 0 COMMENT 'http请求解析结果',"
                    + "PRIMARY KEY (`id`))"
                    + "ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci "
                    + "AUTO_INCREMENT=1";
    private static final String[] request_log_add_index    =
            { "alter table `{tableName}` add INDEX `INDEX_ACCESS_TIME` (`access_time`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_USER_ID` (`userId`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_APP_ID` (`appId`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_DEVICE_ID` (`deviceId`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_CLIENT_IP` (`client_ip`) USING Hash;",
                    "alter table `{tableName}` add INDEX `INDEX_CALL_ID` (`callId`) USING Hash;"
            };
    private static final String   insert_request_log       =
            "insert into `{tableName}` (`access_time`, `access_time_string`, `thread_name`, `userId`, `appId`, `deviceId`, `client_ip`, `callId`, `request_url`, `user_agent`, `token`, `url_parse_code`)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?);";

    @Ignore
    @Test
    public void testRequestLogImport() {
        File file = new File(logPath + "/access");
        if (file.exists() && file.isDirectory()) {
            File[] requests = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.startsWith("request") && s.endsWith(".log");
                }
            });
            if (requests != null) {
                for (File f : requests) {
                    importRequestLogs(f);
                }
            }
        }
    }

    private void fillRequestLog(String line, PreparedStatement pst) throws SQLException {
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
            pst.setString(9, element.substring(pp.getIndex())); // request url
        }
        String user_agent = es[1];
        if (user_agent != null && user_agent.length() > 500) {
            user_agent = user_agent.substring(0, 500);
        }
        pst.setString(10, user_agent); // user agent
        String token = es[3];
        if (token != null) {
            if (es[3].length() > 200) {
                token = token.substring(0, 200);
            } else if ("null".equals(token)) {
                token = null;
            }
        }
        pst.setString(11, token); // user token
        {
            pst.setInt(12, 0); // set default value for url parse code
            if (es.length > 5) {
                String code = es[5];
                if (code != null && code.length() > 0) {
                    if ("FATAL_ERROR".equals(code)) {
                        pst.setInt(12, -104); // url parse code
                    } else {
                        pst.setInt(12, Integer.parseInt(code)); // url parse code
                    }
                }
            }
        }
    }

    private void importRequestLogs(File file) {
        Connection conn = getConnection();
        if (conn == null) {
            return;
        }
        String tableName = "request-" + getFileDate(file);
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
                    statement.execute(create_request_log_talbe.replace("{tableName}", tableName));
                } finally {
                    closeQuietly(null, null, statement);
                    conn.commit();
                }
            }
            pst = conn.prepareCall(insert_request_log.replace("{tableName}", tableName));

            int lineNumber = 0;
            int num = 0;
            new_line:
            while ((line = br.readLine()) != null) {
                if ((num = spliterNumber(line)) < REQUEST_SPLITER_SIZE) {
                    do {
                        int n = 0;
                        String l = br.readLine();
                        if (l == null) {
                            logger.error("unfinished line:" + line);
                            break new_line;
                        } else {
                            n = spliterNumber(l);
                            if (n == REQUEST_SPLITER_SIZE && num != 0) {
                                logger.error("broken line:" + line);
                                num = n;
                                line = l;
                                break;
                            } else {
                                line += l;
                                num += n;
                            }
                        }
                    } while (num < REQUEST_SPLITER_SIZE);
                }
                if (num != REQUEST_SPLITER_SIZE) {
                    logger.error("broken line:" + line);
                    continue;
                }
                fillRequestLog(line, pst);
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
                    for (String sql : request_log_add_index) {
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
