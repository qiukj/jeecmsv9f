package com.jeecms.cms.api;

public class ResponseCode {
	/**
	 * 登陆状态
	 */
	public static final String API_CODE_USER_STATUS_LOGIN="\"1\"";
	/**
	 * 退出状态
	 */
	public static final String API_CODE_USER_STATUS_LOGOUT="\"2\"";
	/**
	 * 超时状态
	 */
	public static final String API_CODE_USER_STATUS_OVER_TIME="\"3\"";
	/**
	 * 伪造身份
	 */
	public static final String API_CODE_USER_STATUS_FORGERY="\"4\"";
	/**
	 * 成功
	 */
	public static final String API_CODE_CALL_SUCCESS="\"200\"";
	public static final String G_API_CODE_CALL_SUCCESS="200";
	/**
	 * 未知错误
	 */
	public static final String API_CODE_CALL_FAIL="\"101\"";
	
	/**
	 * 缺少参数
	 */
	public static final String API_CODE_PARAM_REQUIRED="\"201\"";
	public static final String G_API_CODE_PARAM_REQUIRED="201";
	/**
	 * 参数错误
	 */
	public static final String API_CODE_PARAM_ERROR="\"202\"";
	public static final String G_API_CODE_PARAM_ERROR="202";
	/**
	 * APPID错误或被禁用
	 */
	public static final String API_CODE_APP_PARAM_ERROR="\"203\"";
	/**
	 * 签名错误
	 */
	public static final String API_CODE_SIGN_ERROR="\"204\"";
	/**
	 * 不能删除
	 */
	public static final String API_CODE_DELETE_ERROR="\"205\"";
	/**
	 * 不存在该对象
	 */
	public static final String API_CODE_NOT_FOUND="\"206\"";
	/**
	 * 用户会话错误[已退出或者未登录]
	 */
	public static final String API_CODE_SESSION_ERROR="\"207\"";
	/**
	 *重复请求API
	 */
	public static final String API_CODE_REQUEST_REPEAT="\"208\"";
	/**
	 *用户没有权限
	 */
	public static final String API_CODE_USER_NOT_HAS_PERM="\"209\"";
	/**
	 *数据引用错误
	 */
	public static final String API_CODE_DATA_INTERGER_VIOLATION="\"210\"";
	/**
	 *sql语法错误
	 */
	public static final String API_CODE_SQL_ERROR="\"211\"";
	/**
	 *参数类型错误
	 */
	public static final String API_CODE_PARAM_TYPE_ERROR="\"212\"";
	/**
	 *参数绑定错误
	 */
	public static final String API_CODE_PARAM_BIND_ERROR="\"213\"";
	/**
	 * 防火墙禁止访问
	 */
	public static final String API_CODE_FIREWALL_FORBID="\"214\"";
	/**
	 * 用户未找到
	 */
	public static final String API_CODE_USER_NOT_FOUND="\"301\"";
	/**
	 * 用户未登陆
	 */
	public static final String API_CODE_USER_NOT_LOGIN="\"302\"";
	/**
	 * 道具功能已关闭
	 */
	public static final String API_CODE_MAGIC_CLOSE="\"303\"";
	/**
	 *密码错误
	 */
	public static final String API_CODE_PASSWORD_ERROR="\"304\"";
	/**
	 *用户名已存在
	 */
	public static final String API_CODE_USERNAME_EXIST="\"305\"";
	/**
	 *原密码错误
	 */
	public static final String API_CODE_ORIGIN_PWD_ERROR="\"306\"";
	/**
	 *上传错误
	 */
	public static final String API_CODE_UPLOAD_ERROR="\"307\"";
	/**
	 *订单编号已经使用
	 */
	public static final String API_CODE_ORDER_NUMBER_USED="\"308\"";
	/**
	 *订单编号错误
	 */
	public static final String API_CODE_ORDER_NUMBER_ERROR="\"309\"";
	/**
	 *订单金额不足
	 */
	public static final String API_CODE_ORDER_AMOUNT_NOT_ENOUGH="\"310\"";
	/**
	 *礼物数量不足
	 */
	public static final String API_CODE_GIFT_NOT_ENOUGH="\"311\"";
	/**
	 * 用户账户未找到
	 */
	public static final String API_CODE_USER_ACCOUNT_NOT_FOUND="\"312\"";
	/**
	 * 用户余额不足
	 */
	public static final String API_CODE_USER_BALANCE_NOT_ENOUGH="\"313\"";
	/**
	 * 提现金额太小
	 */
	public static final String API_CODE_DRAW_LESS="\"314\"";
	/**
	 * 购买数量超出库存
	 */
	public static final String API_CODE_MAGIC_HAS_NOT_ENOUGH="\"315\"";
	/**
	 * 用户剩余的包容量不够
	 */
	public static final String API_CODE_MAGIC_PACKET_NOT_ENOUGH="\"316\"";
	/**
	 * 积分不足
	 */
	public static final String API_CODE_POINT_NOT_ENOUGH="\"317\"";
	/**
	 * 威望不足
	 */
	public static final String API_CODE_PRESTIGE_NOT_ENOUGH="\"318\"";
	/**
	 * 道具数量不足
	 */
	public static final String API_CODE_MAGIC_NUM_NOT_ENOUGH="\"319\"";
	/**
	 * 道具禁止使用
	 */
	public static final String API_CODE_MAGIC_FORBIDDEN="\"320\"";
	/**
	 * 帖子包含敏感词
	 */
	public static final String API_CODE_POST_TXT_HAS_SENSITIVE="\"321\"";
	/**
	 * 没有权限在该版本下发帖子
	 */
	public static final String API_CODE_POST_HAS_NOT_PERM="\"322\"";
	/**
	 *非版主不能操作
	 */
	public static final String API_CODE_NOT_MODERATOR="\"323\"";
	/**
	 *非本人信息
	 */
	public static final String API_CODE_NOT_YOUR_INFO="\"324\"";
	/**
	 *会员功能关闭
	 */
	public static final String API_CODE_MEMBER_CLOSE="\"325\"";
	/**
	 * 文件未找到
	 */
	public static final String API_CODE_FILE_NOT_FOUNT="\"326\"";
	/**
	 * 插件被使用中
	 */
	public static final String API_CODE_PLUG_IN_USED="\"327\"";
	/**
	 * 插件新功能冲突
	 */
	public static final String API_CODE_PLUG_CONFLICT="\"328\"";
	/**
	 * 不能卸载
	 */
	public static final String API_CODE_CANNOT_UNINSTALL="\"329\"";
	/**
	 * 用户未设置邮箱
	 */
	public static final String API_CODE_EMAIL_NOT_SET = "\"330\"";
	/**
	 * 用户输入错误
	 */
	public static final String API_CODE_INPUT_ERROR = "\"331\"";
	/**
	 * 服务器邮箱设置错误
	 */
	public static final String API_CODE_EMAIL_SETTING_ERROR="\"332\"";
	/**
	 * 服务器邮箱模板错误
	 */
	public static final String API_CODE_EMAIL_TEMPLATE_ERROR="\"333\"";
	/**
	 * 发送邮件异常
	 */
	public static final String API_CODE_EMAIL_SEND_EXCEPTION="\"334\"";
	/**
	 * 禁言错误
	 */
	public static final String API_CODE_FORBIDDEN_ERROR = "\"335\"";
	/**
	 * 礼物收益余额不足
	 */
	public static final String API_CODE_DRAW_BALANCE_NOT_ENOUGH="\"336\"";
	/**
	 * 文件名错误
	 */
	public static final String API_CODE_FILENAME_ERROR="\"337\"";
	/**
	 * 板块路径已存在
	 */
	public static final String API_CODE_FORUM_PATH_EXIST="\"338\"";
	
	public static final String API_CODE_ACCOUNT_DISABLED="\"339\"";
	/**
	 * 模型已存在
	 */
	public static final String API_CODE_MODEL_EXIST="\"340\"";
	/**
	 * 字段已存在
	 */
	public static final String API_CODE_FIELD_EXIST="\"341\"";
	/**
	 * 访问路径已存在
	 */
	public static final String API_CODE_ACCESSPATH_EXIST="\"342\"";
	/**
	 * 域名已存在
	 */
	public static final String API_CODE_DOMAIN_EXIST="\"343\"";
	/**
	 * 角色等级错误
	 */
	public static final String API_CODE_ROLE_LEVEL_ERROR="\"344\"";
	/**
	 * 内容模型ID已存在
	 */
	public static final String API_CODE_CONTENTTYPE_ID_EXIST="\"345\"";
	/**
	 * 部门名称已存在
	 */
	public static final String API_CODE_DEPARTMENT_EXIST="\"346\"";
	/**
	 * 生成错误
	 */
	public static final String API_CODE_CREATE_EXCEPTION="\"347\"";
	/**
	 * tag名称已存在
	 */
	public static final String API_CODE_TAG_NAME_EXIST="\"348\"";
	/**
	 * 恢复错误
	 */
	public static final String API_CODE_DB_REVERT_ERROR="\"349\"";
	/**
	 * 导出错误
	 */
	public static final String API_CODE_EXPORT_ERROR="\"350\"";
	/**
	 * AES128解密错误
	 */
	public static final String API_CODE_AES128_ERROR="\"351\"";
	/**
	 * 支付错误
	 */
	public static final String API_CODE_PAY_ERROR="\"352\"";
}
