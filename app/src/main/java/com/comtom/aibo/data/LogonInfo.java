package com.comtom.aibo.data;

public class LogonInfo {
	String strUserName;
	String strUserPassword;
	int mUserType;

	String strServerIp;
	int mServerPort;
	int mUserLevel = 0; // 用户水平,0是管理员
	
	int typecode = 0;//是否为演示版本等
	int termMaxCount = -1;  //当为演示版本时,终端数量可能受限制

	static LogonInfo mLogonInfo;

	static public LogonInfo getInstance() {
		if (null == mLogonInfo) {
			mLogonInfo = new LogonInfo();
		}
		return mLogonInfo;
	}

	public void setUserLevel(int nUserLevel) {
		this.mUserLevel = nUserLevel;
	}

	public void setUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public void setUserPassword(String strUserPassword) {
		this.strUserPassword = strUserPassword;
	}

	public void setUserType(int nUserType) {
		this.mUserType = nUserType;
	}

	public void setServerIp(String strServerIp) {
		this.strServerIp = strServerIp;
	}

	public void setServerPort(int nServerPort) {
		this.mServerPort = nServerPort;
	}
	
	public void setTypeCode(int typecode) {
		this.typecode = typecode;
	}
	
	public void setTermMaxCount(int termMaxCount) {
		this.termMaxCount = termMaxCount;
	}

	public int getUserLevel() {
		return this.mUserLevel;
	}

	public String getUserName() {
		return strUserName;
	}

	public String getUserPassword() {
		return strUserPassword;
	}

	public int getUserType() {
		return mUserType;
	}

	public String getServerIp() {
		return strServerIp;
	}

	public int getServerPort() {
		return mServerPort;
	}
	
	public int getTypeCode() {
		return this.typecode;
	}
	
	public int getTermMaxCount() {
		return this.termMaxCount;
	}

	// 是不是管理员
	public boolean IsManager() {
		return ((0 == mUserLevel || 10 == mUserLevel) ? true : false);
	}

	public static void destroy() {
		mLogonInfo = null;
	}

	private LogonInfo() {

	}
}
