CREATE TABLE `request` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`access_time`  bigint(20) NULL COMMENT '访问时间',
`access_time_string`  varchar(30) NULL COMMENT '访问时间的字符串值',
`thread_name`  varchar(30) NULL COMMENT '线程名',
`userId`  varchar(30) NULL COMMENT '用户id',
`appId`  varchar(30) NULL COMMENT '应用id',
`deviceId`  varchar(30) NULL COMMENT '设备id',
`client_ip`  varchar(30) NULL COMMENT '客户ip',
`callId`  varchar(50) NULL COMMENT 'http调用id',
`request_url`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '客户端请求字符串',
`user_agent`  varchar(500) NULL COMMENT 'http User-Agent',
`token`  varchar(200) NULL COMMENT '用户身份凭据',
`url_parse_code`  int NOT NULL default 0 COMMENT 'http请求解析结果',
PRIMARY KEY (`id`))
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci AUTO_INCREMENT=1;

alter table `request` add INDEX `INDEX_ACCESS_TIME` (`access_time`) USING Hash;
alter table `request` add INDEX `INDEX_USER_ID` (`userId`) USING Hash;
alter table `request` add INDEX `INDEX_APP_ID` (`appId`) USING Hash;
alter table `request` add INDEX `INDEX_DEVICE_ID` (`deviceId`) USING Hash;
alter table `request` add INDEX `INDEX_CLIENT_IP` (`client_ip`) USING Hash;
alter table `request` add INDEX `INDEX_CALL_ID` (`callId`) USING Hash;

/*****************************************************************************************************************************/

CREATE TABLE `access` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`access_time`  bigint(20) NULL COMMENT '访问时间',
`access_time_string`  varchar(30) NULL COMMENT '访问时间的字符串值',
`thread_name`  varchar(30) NULL COMMENT '线程名',
`userId`  varchar(30) NULL COMMENT '用户id',
`appId`  varchar(30) NULL COMMENT '应用id',
`deviceId`  varchar(30) NULL COMMENT '设备id',
`client_ip`  varchar(30) NULL COMMENT '客户ip',
`callId`  varchar(50) NULL COMMENT 'http调用id',
`cost`  int NOT NULL COMMENT '本接口消耗的时间',
`method`  varchar(50) NOT NULL COMMENT '接口名',
`return_code`  int NOT NULL COMMENT '对外返回code',
`real_code`  int NOT NULL COMMENT '业务接口返回code / 隐藏不对外暴露的code',
`result_length`  int NOT NULL COMMENT '接口返回数据的未压缩长度',
`request_parameter`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '接口相关的参数',
`response_log`  varchar(1000) NULL COMMENT '业务接口返回的关键统计数据',
PRIMARY KEY (`id`))
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci AUTO_INCREMENT=1;

alter table `access` add INDEX `INDEX_ACCESS_TIME` (`access_time`) USING Hash;
alter table `access` add INDEX `INDEX_USER_ID` (`userId`) USING Hash;
alter table `access` add INDEX `INDEX_APP_ID` (`appId`) USING Hash;
alter table `access` add INDEX `INDEX_DEVICE_ID` (`deviceId`) USING Hash;
alter table `access` add INDEX `INDEX_CLIENT_IP` (`client_ip`) USING Hash;
alter table `access` add INDEX `INDEX_CALL_ID` (`callId`) USING Hash;
alter table `access` add INDEX `INDEX_METHOD_ID` (`method`) USING Btree;

/*****************************************************************************************************************************/

