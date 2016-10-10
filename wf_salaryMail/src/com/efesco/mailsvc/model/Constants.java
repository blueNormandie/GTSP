package com.efesco.mailsvc.model;

public class Constants {
	
	public static final String Email_HOST = "localhsot"; // smtp服务器
	public static final String Email_USER = "zwb"; // 用户名
	public static final String Email_PWD = "zwb123"; // 密码
	//是否结束
	public static final String FINISHED_TRUE = "1";
	public static final String FINISHED_FALSE = "0";
	//处理状态
	public static final String PROC_STATUS_NOTDEAL = "1";
	public static final String PROC_STATUS_FINISH = "2";
	public static final String PROC_STATUS_FAIL = "3";
	public static final String PROC_STATUS_NOTNOW = "9";
   //邮件发送的五种状态，邮件任务和邮件明细共用
    public static final EmailStatusObject STATUS_WAIT_SEND =  new EmailStatusObject("1", "待发送");
	public static final EmailStatusObject STATUS_NOW_SENDING =new  EmailStatusObject("2", "正在发送");
	public static final EmailStatusObject STATUS_SEND_SUCCESS = new EmailStatusObject("3", "发送成功");
	public static final EmailStatusObject STATUS_SEND_FAIL = new EmailStatusObject("4", "发送失败");
	public static final EmailStatusObject STATUS_CANNOT_SEND = new EmailStatusObject("5", "不可发送");
	public static final EmailStatusObject STATUS_CANCEL = new EmailStatusObject("6", "已取消");
	//特殊的业务类别代码，表示所有业务都生效
	public static final String BIZ_TYPE_ALL = "1";   
	//锁定检查间隔（毫秒数）
	public static final long LOCK_CHECK_INTERVAL = 1000;
	//接收者类型
	public static final String[] RCV_TYPE_ARR = new String[] {"EMP", "SUPL", "LD", "COMM"};
	//发送失败原因
	public static final FailReason FAIL_REASON_00 = new FailReason(0, "地址错误或者获取邮件正文失败");
	public static final FailReason FAIL_REASON_03 = new FailReason(101, "客户端网络故障");
	public static final FailReason FAIL_REASON_04 = new FailReason(305, "服务器端返回错误，错误的返回值");
	public static final FailReason FAIL_REASON_05 = new FailReason(307, "目标邮箱地址错误");
	public static final FailReason FAIL_REASON_07 = new FailReason(997, "平台返回找不到超时的邮件，该信息是否成功无法确定");
	public static final FailReason FAIL_REASON_08 = new FailReason(303, "由于客户端网络问题导致信息发送超时，该邮件是否成功下发无法确定");
}
