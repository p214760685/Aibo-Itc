package com.comtom.aibo.data;


import com.comtom.aibo.utils.IConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ReplayData> mReplayList;
	private List<User> mUserList;

	public DataBaseInfo() {
		mReplayList = new ArrayList<ReplayData>();
		mUserList = new ArrayList<User>();
	}

	public void add(ReplayData nReplayData) {
		mReplayList.add(nReplayData);
	}

	public void add(String name, String addr) {

		ReplayData nReplayData = new ReplayData(name, addr);
		add(nReplayData);
	}

	public void set(String name, String addr, int pos) {

		ReplayData nReplayData = mReplayList.get(pos);
		nReplayData.name = name;
		nReplayData.addr = addr;
	}

	public ReplayData get(int nindex) {
		return mReplayList.get(nindex);
	}

	public void remove(int nindex) {
		mReplayList.remove(nindex);
	}

	public List<ReplayData> getReplayList() {
		return mReplayList;
	}

	// 用户对象序列号
	public void addUser(User user) {
		int nsize = mUserList.size();
		mUserList.add(user);
		if (nsize + 1 > IConstant.MAX_HISTORY_USER_COUNT) {
			mUserList.remove(0);
		}
	}

	/**
	 * 检查用户是否已经存在,如果存在 则会更新密码和用户类型,保证该用户信息的统一
	 * @param userName  判断的用户名称
	 * @param userPass  待更新的用户密码
	 * @param nUserType 待更新的用户类型
	 * @return
	 */
	public boolean Exist(String userName,String userPass,int nUserType, boolean chbSave) {
		int nsize = mUserList.size();
		for (int i = 0; i < nsize; i++) {
			User user2 = mUserList.get(i);
			if (0 == user2.strUserName.compareTo(userName)) {
				user2.strUserPassword = chbSave ? userPass : "";
				user2.mUserType = nUserType;
				return true;
			}
		}
		return false;
	}

	public User removeUser(int nindex) {
		return mUserList.remove(nindex);
	}

	public List<User> getUserList() {
		return mUserList;
	}

}
