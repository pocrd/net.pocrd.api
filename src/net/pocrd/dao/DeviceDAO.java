package net.pocrd.dao;

import java.util.List;

import net.pocrd.annotation.CacheMethod;
import net.pocrd.annotation.CacheParameter;
import net.pocrd.api.util.ApiCacheKey;
import net.pocrd.facade.entity.BindingInfo;
import net.pocrd.facade.entity.DeviceInfo;

public class DeviceDAO {
    //private final static Logger logger = LogManager.getLogger(DeviceDAO.class);

    @CacheMethod(enable = true, key = ApiCacheKey.DAO_DeviceRsaPublic, expire = 600)
    public String getRsaPublic(@CacheParameter long sn, @CacheParameter int appid) {
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid)
        // };
        //
        // using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, "select rsapublic from TB_DEVICE_PUBLICKEY where sn=:sn and appid=:appid and status=0", paras))
        // {
        // while (dr.Read())
        // {
        // return Field.GetString(dr, "rsapublic");
        // }
        // }

        return null;
    }

    public boolean prebind(long sn, long uid) {
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("sdid", DbType.AnsiString, sdid),
        // _oracleAdoHelper.GetParameter("pt", DbType.AnsiString, pt)
        // };
        //
        // return 1 == _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, @"insert into TB_DEVICE_PREBIND(SN, SDID, PT, CREATETIME) values(:sn, :sdid, :pt, sysdate)", paras);
        return false;
    }

    public BindingInfo getPrebindInfo(String sn) {
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn)
        // };
        //
        // string sql =
        // "select sdid, pt from TB_DEVICE_PREBIND where sn=:sn and rownum < 2 and createtime > sysdate-30/(24*60) order by createtime desc";
        //
        // using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, sql, paras))
        // {
        // if (dr.Read())
        // {
        // Biz_BindingInfo info = new Biz_BindingInfo();
        // info.pt = Field.GetString(dr, "pt");
        // info.sdid = Field.GetString(dr, "sdid");
        // return info;
        // }
        // }

        return null;
    }

    public boolean registDevice(long sn, long uid, String certHash, String deviceType, String deviceKey, String deviceInfo, String authType) {
        // if (!DeviceExist(sn, false))
        // {
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("certhash", DbType.AnsiString, certHash),
        // _oracleAdoHelper.GetParameter("devicetype", DbType.AnsiString, deviceType),
        // _oracleAdoHelper.GetParameter("devicekey", DbType.AnsiString, deviceKey),
        // _oracleAdoHelper.GetParameter("deviceinfo", DbType.AnsiString, deviceInfo),
        // _oracleAdoHelper.GetParameter("creater", DbType.AnsiString, sdid),
        // _oracleAdoHelper.GetParameter("creatertype", DbType.AnsiString, authType),
        // };
        //
        // return 1 == _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, @"insert into TB_DEVICE(SN, CERTHASHCODE, DEVICETYPE, DEVICEKEY, DEVICEINFO, CREATER, CREATERTYPE)
        // values(:sn, :certhash, :devicetype, :devicekey, :deviceinfo, :creater, :creatertype)", paras);
        // }
        // else
        // {
        // return true;
        // }
        return false;
    }

    @CacheMethod(enable = true, key = ApiCacheKey.DAO_DeviceInfo, expire = 600)
    public DeviceInfo getDeviceInfo(@CacheParameter long sn) {
        // Biz_DeviceInfo info = null;
        //
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // };
        //
        // using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, "select * from TB_DEVICE where sn = :sn", paras))
        // {
        // if (dr.Read())
        // {
        // info = new Biz_DeviceInfo();
        // info.certhash = Field.GetString(dr, "certhashcode");
        // info.creater = Field.GetString(dr, "creater");
        // info.createrType = Field.GetString(dr, "creatertype");
        // info.deviceInfo = Field.GetString(dr, "deviceinfo");
        // info.deviceKey = Field.GetString(dr, "devicekey");
        // info.deviceType = Field.GetString(dr, "devicetype");
        // info.sn = Field.GetString(dr, "sn");
        // info.status = Field.GetInt32(dr, "status");
        // info.groupid = Field.GetString(dr,"groupid");
        // }
        // }
        //
        // if (info != null)
        // {
        // paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // };
        //
        // _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, "update TB_DEVICE set logintime = sysdate where sn = :sn and (logintime < trunc(sysdate) or logintime is NULL)", paras);
        // }
        // return info;
        return null;
    }

    public void updateCertificate(long sn, String certHash, String cert) {
        // try
        // {
        // if (!CertExist(certHash))
        // {
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("certhash", DbType.AnsiString, certHash),
        // _oracleAdoHelper.GetParameter("cert", DbType.AnsiString, cert),
        // };
        //
        // _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, @"insert into TB_CERTIFICATE(SN, CERTHASHCODE, CERTIFICATE)values(:sn, :certhash, :cert)", paras);
        // }
        // }
        // catch (Exception e)
        // {
        // logger.Error(e);
        // }
    }

    public boolean bindSnWithUser(long sn, int appid, long uid, String userType) {
        // IDataParameter[] paras = new IDataParameter[] { _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid), _oracleAdoHelper.GetParameter("sdid", DbType.AnsiString, sdid),
        // _oracleAdoHelper.GetParameter("pt", DbType.AnsiString, pt), _oracleAdoHelper.GetParameter("usertype", DbType.AnsiString, userType), };
        //
        // if (BindingExist(sn, appid, sdid, false)) {
        // return 1 == _oracleAdoHelper
        // .ExecuteNonQuery(
        // CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text,
        // "update TB_DEVICE_BINDING set STATUS=0, UPDATETIME=sysdate where SN=:sn and APPID=:appid and SDID=:sdid and PT=:pt and USERTYPE=:usertype",
        // paras);
        // } else {
        // return 1 == _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB, CommandType.Text,
        // "insert into TB_DEVICE_BINDING(SN, APPID, SDID, PT, USERTYPE)values(:sn, :appid, :sdid, :pt, :usertype)", paras);
        // }
        return false;
    }

    public boolean bindSnWithUserForInsertOnly(long sn, int appid, long uid, String userType) {
        // IDataParameter[] paras = new IDataParameter[] { _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid), _oracleAdoHelper.GetParameter("sdid", DbType.AnsiString, sdid),
        // _oracleAdoHelper.GetParameter("pt", DbType.AnsiString, pt), _oracleAdoHelper.GetParameter("usertype", DbType.AnsiString, userType), };
        //
        // if (!BindingExist(sn, appid, sdid, false)) {
        // return 1 == _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB, CommandType.Text,
        // "insert into TB_DEVICE_BINDING(SN, APPID, SDID, PT, USERTYPE)values(:sn, :appid, :sdid, :pt, :usertype)", paras);
        // }
        return false;
    }

    public boolean unbindSnWithUser(long sn, int appid, long uid) {
        // if (BindingExist(sn, appid, sdid, false)) {
        // IDataParameter[] paras = new IDataParameter[] { _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid), _oracleAdoHelper.GetParameter("sdid", DbType.AnsiString, sdid), };
        //
        // return 1 == _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB, CommandType.Text,
        // "update TB_DEVICE_BINDING set STATUS=1, UPDATETIME=sysdate where SN=:sn and APPID=:appid and SDID=:sdid", paras);
        // } else {
        // return true;
        // }
        return true;
    }

    public boolean unbindSn(long sn, int appid) {
        // IDataParameter[] paras = new IDataParameter[] { _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid), };
        //
        // return 0 < _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB, CommandType.Text,
        // "update TB_DEVICE_BINDING set STATUS=1, UPDATETIME=sysdate where SN=:sn and APPID=:appid", paras);
        return false;
    }

    public boolean unbindUser(long uid) {
        // IDataParameter[] paras = new IDataParameter[] { _oracleAdoHelper.GetParameter("sdid", DbType.AnsiString, sdid), };
        //
        // return 0 < _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB, CommandType.Text,
        // "update TB_DEVICE_BINDING set STATUS=1, UPDATETIME=sysdate where SDID = :sdid", paras);
        return false;
    }

    public boolean updateRsaPublic(long sn, int appid, String rsaPublic) {
        // IDataParameter[] paras = new IDataParameter[] { _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid),
        // _oracleAdoHelper.GetParameter("rsapublic", DbType.AnsiString, rsaPublic), };
        //
        // if (RsaPublicExist(sn, appid, false)) {
        // return 0 < _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB, CommandType.Text,
        // "update TB_DEVICE_PUBLICKEY set RSAPUBLIC=:rsapublic, UPDATETIME=sysdate where sn=:sn and appid=:appid", paras);
        // } else {
        // return 0 < _oracleAdoHelper.ExecuteNonQuery(CommonConfig.Instance.ConnectionStringMainDB, CommandType.Text,
        // "insert into TB_DEVICE_PUBLICKEY(SN, APPID, RSAPUBLIC) values(:sn, :appid, :rsapublic)", paras);
        // }
        return false;
    }

    public List<BindingInfo> getBindingInfos(long sn, int appid, boolean statusEnable) {
        // List<Biz_BindingInfo> infos = new List<Biz_BindingInfo>();
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid)
        // };
        //
        // string sql = statusEnable
        // ? "select appid, pt, sn, status, sdid, usertype from TB_DEVICE_BINDING where sn=:sn and appid=:appid and status=0"
        // : "select appid, pt, sn, status, sdid, usertype from TB_DEVICE_BINDING where sn=:sn and appid=:appid";
        //
        // using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, sql, paras))
        // {
        // while (dr.Read())
        // {
        // Biz_BindingInfo info = new Biz_BindingInfo();
        // info.appid = Field.GetString(dr, "appid");
        // info.pt = Field.GetString(dr, "pt");
        // info.sn = Field.GetString(dr, "sn");
        // info.status = Field.GetInt32(dr, "status");
        // info.sdid = Field.GetString(dr, "sdid");
        // info.usertype = Field.GetString(dr, "usertype");
        // infos.Add(info);
        // }
        // }
        //
        // return infos;
        return null;
    }

    public BindingInfo getBindingInfo(long sn, int appid, long uid, boolean statusEnable)
    {
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid),
        // _oracleAdoHelper.GetParameter("sdid", DbType.AnsiString, sdid)
        // };
        //
        // string sql = statusEnable
        // ? "select appid, pt, sn, status, sdid, usertype from TB_DEVICE_BINDING where sn=:sn and appid=:appid and sdid=:sdid and status=0"
        // : "select appid, pt, sn, status, sdid, usertype from TB_DEVICE_BINDING where sn=:sn and appid=:appid and sdid=:sdid";
        //
        // using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, sql, paras))
        // {
        // while (dr.Read())
        // {
        // Biz_BindingInfo info = new Biz_BindingInfo();
        // info.appid = Field.GetString(dr, "appid");
        // info.pt = Field.GetString(dr, "pt");
        // info.sn = Field.GetString(dr, "sn");
        // info.status = Field.GetInt32(dr, "status");
        // info.sdid = Field.GetString(dr, "sdid");
        // info.usertype = Field.GetString(dr, "usertype");
        // return info;
        // }
        // }

        return null;
    }

    public boolean deviceExist(long sn, boolean statusEnable)
    {
        // IDataParameter[] paras = new IDataParameter[]
        // {
        // _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
        // };
        //
        // string sql = statusEnable
        // ? "select * from TB_DEVICE where sn = :sn and status = 0"
        // : "select * from TB_DEVICE where sn = :sn";
        //
        // using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
        // CommandType.Text, sql, paras))
        // {
        // while (dr.Read())
        // {
        // return true;
        // }
        // }
        //
        return false;
    }

    public boolean bindingExist(long sn, int appid, long uid, boolean statusEnable)
    {
//        IDataParameter[] paras = new IDataParameter[]
//                {
//                    _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
//                    _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid),
//                    _oracleAdoHelper.GetParameter("sdid", DbType.AnsiString, sdid),
//                };
//
//        string sql = statusEnable
//            ? "select * from TB_DEVICE_BINDING where sn = :sn and sdid = :sdid and appid = :appid and status = 0"
//            : "select * from TB_DEVICE_BINDING where sn = :sn and sdid = :sdid and appid = :appid";
//
//        using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
//CommandType.Text, sql, paras))
//        {
//            while (dr.Read())
//            {
//                return true;
//            }
//        }

        return false;
    }

    public boolean rsaPublicExist(long sn, int appid, boolean statusEnable)
    {
//        IDataParameter[] paras = new IDataParameter[]
//                {
//                    _oracleAdoHelper.GetParameter("sn", DbType.AnsiString, sn),
//                    _oracleAdoHelper.GetParameter("appid", DbType.AnsiString, appid),
//                };
//
//        string sql = statusEnable
//            ? "select rsapublic from TB_DEVICE_PUBLICKEY where sn=:sn and appid=:appid and status = 0"
//            : "select rsapublic from TB_DEVICE_PUBLICKEY where sn=:sn and appid=:appid";
//
//        using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
//            CommandType.Text, sql, paras))
//        {
//            while (dr.Read())
//            {
//                return true;
//            }
//        }

        return false;
    }

    public boolean certExist(String certhash)
    {
//        IDataParameter[] paras = new IDataParameter[]
//                {
//                    _oracleAdoHelper.GetParameter("certhash", DbType.AnsiString, certhash),
//                };
//
//        using (IDataReader dr = _oracleAdoHelper.ExecuteReader(CommonConfig.Instance.ConnectionStringMainDB,
//CommandType.Text, "select * from TB_CERTIFICATE where CERTHASHCODE = :certhash", paras))
//        {
//            while (dr.Read())
//            {
//                return true;
//            }
//        }

        return false;
    }
}
