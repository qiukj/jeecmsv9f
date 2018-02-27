package com.jeecms.cms.api;

public class Constants {
	public static final String THIRD_SOURCE_WEIXIN_APP="weixinApp";
	
	public static final String COMMON_PARAM_APPID="appId";
	
	public static final String COMMON_PARAM_SESSIONKEY="sessionKey";
	
	public static final String COMMON_PARAM_SIGN="sign";
	
	public static final String STATIC_CURR_COUNT_KEY="currStaticCount";
	public static final String STATIC_TOTAL_COUNT_KEY="totalStaticCount";
	
	//超时时间
	public static final int USER_OVER_TIME=20;
	
	//内容地址返回http
	public static final int URL_HTTP=1;
	//内容地址返回https
	public static final int URL_HTTPS=0;
	/**
	 * 图片占位符开始
	 */
	public static final String 	API_PLACEHOLDER_IMAGE_BEGIN="_img_start_";
	/**
	 * 图片占位符结束
	 */
	public static final String 	API_PLACEHOLDER_IMAGE_END="_img_end_";
	/**
	 * 视频占位符开始
	 */
	public static final String 	API_PLACEHOLDER_VIDEO_BEGIN="_video_start_";
	/**
	 * 视频占位符结束
	 */
	public static final String 	API_PLACEHOLDER_VIDEO_END="_video_end_";
	/**
	 * API接口调用状态-成功
	 */
	public static final String 	API_STATUS_SUCCESS="true";
	public static final String 	G_API_STATUS_SUCCESS="true";
	/**
	 * API接口调用状态失败
	 */
	public static final String 	API_STATUS_FAIL="false";
	public static final String 	G_API_STATUS_FAIL="false";
	/**
	 * API接口消息-成功
	 */
	public static final String 	API_MESSAGE_SUCCESS="success";
	public static final String 	G_API_MESSAGE_SUCCESS="success";
	/**
	 * API接口消息-缺少参数
	 */
	public static final String 	API_MESSAGE_PARAM_REQUIRED="param_required";
	public static final String 	G_API_MESSAGE_PARAM_REQUIRED="param_required";
	/**
	 * API接口消息-参数错误
	 */
	public static final String 	API_MESSAGE_PARAM_ERROR="param_error";
	public static final String 	G_API_MESSAGE_PARAM_ERROR="param_error";
	
	/**
	 * API接口消息-appId参数错误
	 */
	public static final String 	API_MESSAGE_APP_PARAM_ERROR="appId_not_exist_or_appId_disabled";
	/**
	 * API接口消息-用户未找到
	 */
	public static final String 	API_MESSAGE_USER_NOT_FOUND="user_not_found";
	/**
	 * API接口消息-内容未找到
	 */
	public static final String 	API_MESSAGE_CONTENT_NOT_FOUND="content_not_found";
	/**
	 * API接口消息-用户未登录
	 */
	public static final String 	API_MESSAGE_USER_NOT_LOGIN="user_not_login";
	/**
	 * API接口消息-SESSION错误
	 */
	public static final String 	API_MESSAGE_SESSION_ERROR="session_error";
	/**
	 * API接口消息-密码错误
	 */
	public static final String 	API_MESSAGE_PASSWORD_ERROR="password_error";
	/**
	 * API接口消息-用户名已存在
	 */
	public static final String 	API_MESSAGE_USERNAME_EXIST="username_exist";
	/**
	 * API接口消息-原密码错误已存在
	 */
	public static final String 	API_MESSAGE_ORIGIN_PWD_ERROR="origin_password_invalid";
	/**
	 * API接口消息-上传失败
	 */
	public static final String 	API_MESSAGE_UPLOAD_ERROR="upload_file_error";
	/**
	 * API接口消息-订单编号已经使用
	 */
	public static final String 	API_MESSAGE_ORDER_NUMBER_USED="order_number_used";
	/**
	 * API接口消息-订单编号错误
	 */
	public static final String 	API_MESSAGE_ORDER_NUMBER_ERROR="order_number_error";
	/**
	 * API接口消息-订单金额不足
	 */
	public static final String 	API_MESSAGE_ORDER_AMOUNT_NOT_ENOUGH="order_amount_not_enough";
	/**
	 * API接口消息-重复请求API
	 */
	public static final String 	API_MESSAGE_REQUEST_REPEAT="request_api_repeat";
	
	/**
	 * API接口消息-用户没有权限
	 */
	public static final String 	API_MESSAGE_USER_NOT_HAS_PERM="user_has_not_perm";
	/**
	 * API接口消息-用户超时
	 */
	public static final String 	API_MESSAGE_USER_OVER_TIME="user_over_time";
	/**
	 * API接口消息-用户身份伪造
	 */
	public static final String 	API_MESSAGE_USER_FORGERY="user_identity_forgery";
	
	public static final String API_MESSAGE_ACCOUNT_DISABLED="disabled";
	/**
	 * API接口消息 -对象未找到
	 */
	public static final String 	API_MESSAGE_OBJECT_NOT_FOUND="object_not_found";
	/**
	 * API接口调用服务器响应错误
	 */
	public static final String 	API_STATUS_APPLICATION_ERROR="application_error";
	/**
	 * API接口消息-文件不存在
	 */
	public static final String API_MESSAGE_FILE_NOT_FOUNT="file_not_found";
	/**
	 * API接口消息-数据引用错误
	 */
	public static final String API_MESSAGE_DATA_INTERGER_VIOLATION="DataIntegrityViolation";
	/**
	 * API接口消息-sql错误
	 */
	public static final String API_MESSAGE_SQL_ERROR="sql_error";
	/**
	 * API接口消息-参数类型错误
	 */
	public static final String API_MESSAGE_PARAM_TYPE_ERROR="param_type_error";
	/**
	 * API接口消息-参数绑定错误
	 */
	public static final String API_MESSAGE_PARAM_BIND_ERROR="param_bind_error";
	/**
	 * API接口消息 -删除错误
	 */
	public static final String 	API_MESSAGE_DELETE_ERROR="delete_error";
	/**
	 * API接口消息-模型已存在
	 */
	public static final String API_MESSAGE_MODEL_EXIST = "model_is_exist";
	/**
	 * API接口消息-字段已存在
	 */
	public static final String API_MESSAGE_FIELD_EXIST = "field_is_exist";
	/**
	 * API接口消息-访问路径已存在
	 */
	public static final String API_MESSAGE_ACCESSPATH_EXIST="accessPath_is_exist";
	/**
	 * API接口消息-域名已存在
	 */
	public static final String API_MESSAGE_DOMAIN_EXIST="domain_is_exist";
	/**
	 * API接口消息-角色等级错误
	 */
	public static final String API_MESSAGE_ROLE_LEVEL_ERROR="role_level_error";
	/**
	 * API接口消息-创建微信菜单错误
	 */
	public static final String API_MESSAGE_CREATE_MENU_ERROR="create_menu_error";
	/**
	 * API接口消息-发送微信消息错误
	 */
	public static final String API_MESSAGE_SEND_TO_WEXIN_ERROR="send_to_weixin_error";
	/**
	 * API接口消息-内容类型ID已存在
	 */
	public static final String API_MESSAGE_CONTENTTYPE_ID_EXIST="contentType_id_is_exist";
	/**
	 * API接口消息-模板未找到
	 */
	public static final String API_MESSAGE_TEMPLATE_NOT_FOUNT="template_not_found";
	/**
	 * API接口消息-模板错误
	 */
	public static final String API_MESSAGE_TEMPLATE_PARESE_ERROR="template_parese_exception";
	/**
	 * API接口消息-静态化未开启
	 */
	public static final String API_MESSAGE_STATIC_PAGE_NOT_OPEN="static_page_not_open";
	/**
	 * API接口消息-内容未终审
	 */
	public static final String API_MESSAGE_CONTENT_NOT_CHECKED="content_not_checked";
	/**
	 * API接口消息-部门名称已存在
	 */
	public static final String API_MESSAGE_DEPARTMENT_NAME_EXIST="department_name_exist";
	/**
	 * API接口消息-生成错误
	 */
	public static final String API_MESSAGE_CREATE_ERROR="create_exception";
	/**
	 * API接口消息-tag名称已存在
	 */
	public static final String API_MESSAGE_TAG_NAME_EXIST="tag_name_exist";
	/**
	 * API接口消息-恢复错误
	 */
	public static final String API_MESSAGE_DB_REVERT_ERROR="db_revert_error";
	/**
	 * API接口消息-导出错误
	 */
	public static final String API_MESSAGE_EXPORT_ERROR="export_error";
	
	/**
	 * API接口消息-超时
	 */
	public static final String 	API_MESSAGE_USER_STATUS_OVER_TIME="over_time";
	
	/**
	 * API接口消息-登陆状态
	 */
	public static final String 	API_MESSAGE_USER_STATUS_LOGIN="login";
	/**
	 * API接口消息-未登陆状态
	 */
	public static final String 	API_MESSAGE_USER_STATUS_NOT_LOGIN="no_login";

	/**
	 * API接口消息-防火墙禁止访问
	 */
	public static final String 	API_MESSAGE_FIREWALL_FORBID="fire_wall_forbid";

	/**
	 * API接口消息-AES128解密错误
	 */
	public static final String API_MESSAGE_AES128_ERROR="aes128_error";
	/**
	 * API接口消息-转账失败
	 */
	public static final String API_MESSAGE_PAY_ERROR="pay_error";
	
	public static final String 	API_ARRAY_SPLIT_STR=",";
	public static final String 	API_LIST_SPLIT_STR=";";
	
	public static final String API_MESSAGE_NOT_CONTENT_ERROR="not_content_error";
	
	public static final int DISTRIBUTE_THREAD_COUNT=10;
	
	/**
	 *最大静态化数量
	 */
	public static final int MAX_STATIC_COUNT = 10;
}
