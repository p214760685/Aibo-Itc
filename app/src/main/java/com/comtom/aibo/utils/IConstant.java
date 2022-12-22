package com.comtom.aibo.utils;

public interface IConstant {

	String strComtomSharedPreferences = "COMTOMSharedPreferences";

	// 进入登入界面的类型
	String strEnternLogonUIType_KEY = "ENTERNLOGONUITYPE_KEY";
	int nLogOutEnternLogonui = 1; // 注销进入
	int nRelogonEnternLogonui = 2; // 切换账号进入

	String strUserType_KEY = "USERTYPE_IConstant";
	int mdefaultUserType = 0;

	String strServerIp_KEY = "SERVERIP_IConstant";
	String strServerPort_KEY = "SERVERPORT_IConstant";
	String strbSavePassDefaul_KEY = "SAVEPASS_IConstant";

	String strdefaultServerIp = "10.0.0.1";
//	String strdefaultServerIp = "192.168.7.110";
	// String strdefaultServerIp = "192.168.20.222";;//"192.168.7.222";
	int mdefaultServerport = 8000;
	boolean bSavePassDefaul = true;
	boolean SearchSetting_Min_Default = true;
	
	int TypeCodeYanxi = 200;
	
	

	int mdefaultServerUdpPort = 15009;

	String strSearchSettings_Min = "SEARCHSETTING_MIN";
	String strInstalled_KEY = "INSTALLED_IConstant";
	String strUserName_KEY = "USERNAME_IConstant";
	String strUserPassword_KEY = "USERPASSWORD_IConstant";
	String strUserLevel_KEY = "USERLEVEL_IConstant";
	String FirstStartUp_KEY = "FirstStartUp_Iconstant";
	String taskNameIndex_KEY = "taskNameIndex";
	String speakNameIndex_KEY = "speakNameIndex";

	String strdefaultUserName = "admin";
	String strdefaultUserPassword = "";

	String strTermArrayPos_KEY = "TERMARRAYPOS_KEY";

	int mMaxVol = 56;
	int mMinVol = 0;

	int mMaxRepeatNum = 99;
	int mMinRepeatNum = 1;

	String strSelTermSubData_KEY = "SELTERMSUBDATA_KEY";
	String strSelSpeakID_KEY = "SELSPEAKID_KEY";
	String strSelSpeakName_KEY = "SELSPEAKNAME_KEY";
	
	String strSelTerm_KEY = "SELTERM_KEY";
	String strSelProgram_KEY = "SELPROGRAM_KEY";
	String strCirclePlay_KEY = "CIRCLEPLAY_KEY";
	String strRandomPlay_KEY = "RANDOMPLAY_KEY";
	String strTaskName_KEY = "TASKNAME_KEY";

	// 会话的类型
	int nsessiontype_mp3_stream = 0x00010202; // mp3流数据
	int nsessiontype_pcm_stream = 0x00010000; // pcm流数据
	int nsessiontype_server_local_file = 0x00010001; // 服务器本地文件
	int nsessiontype_client_local_file = 0x00010102; // 客户端本地文件

	int nPriority_RealStream_Play = 500;
	int nPriority_LocalFile_Play = 400;

	int nplayProgram_Timer = 0;

	// 课程任务
	// 类型
	int JOB_TYPE_DAY = 0x01;
	int JOB_TYPE_WEEK = 0x02;
	int JOB_TYPE_MONTH = 0x03;
	int JOB_TYPE_ONCE = 0x04;
	int JOB_TYPE_START = 0x05;

	// 任务掩码
	int JOB_FLAG_EDATE = 0x01;
	int JOB_FLAG_ETIME = 0x04;
	int JOB_FLAG_MDATE = 0x10;

	String strType_CourseTask_KEY = "TYPE_COURSETASK_KEY";
	String strMask_CourseTask_KEY = "MASK_COURSETASK_KEY";
	String strDuration_CourseTask_KEY = "DURATION_COURSETASK_KEY";
	String strStartTime_CourseTask_KEY = "STARTTIME_COURSETASK_KEY";
	String strEndTime_CourseTask_KEY = "ENDTIME_COURSETASK_KEY";
	String strName_CourseTask_KEY = "NAME_COURSETASK_KEY";
	String strOwner_CourseTask_KEY = "OWNER_COURSETASK_KEY";
	String strRepeatNum_CourseTask_KEY = "REPEATNUM_COURSETASK_KEY";
	String strPlayVol_CourseTask_KEY = "PLAYVOL_COURSETASK_KEY";
	String strPlayMode_CourseTask_KEY = "PLAYMODE_COURSETASK_KEY";
	String strJabdata_CourseTask_KEY = "JOBDATA_COURSETASK_KEY";

	// 主要是 编辑进入 定时任务的新建Activity 还是 新建按钮进入
	String strEnternType_CourseTask_KEY = "ENTERNTYPE_COURSETASK_KEY";
	int nNew_EnrernType_CourseTask_KEY = 0;
	int nEdit_EnrernType_CourseTask_KEY = 1;

	String strTermData_KEY = "TERMARRAYDATA_KEY";
	String strEnternType_Term_KEY = "ENTERNTYPE_TERM_KEY";
	int nNew_EnrernType_Term_KEY = 0;
	int nEdit_EnrernType_Term_KEY = 1;

	// 退出应用
	int mExitApp_CONST_MSG = 1;
	int mLogout_CONST_MSG = 2;
	int mYanxi_CONST_MSG = 3;

	// 节目长按弹出
	int mFileType_File_Program = 1;
	int mFileType_Dir_Program = 2;

	String strDirPos_NewProgram_KEY = "DIRPOS_NEWPROGRAM_KEY";
	String strIsDir_NewProgram_KEY = "ISDIR_NEWPROGRAM_KEY";
	String strIsPublic_NewProgram_KEY = "ISPUBLIC_NEWPROGRAM_KEY";
	String strName_NewProgram_KEY = "NAME_NEWPROGRAM_KEY";
	String strAddRoot_NewProgram_KEY = "ADDROOT_NEWPROGRAM_KEY";
	String strFileTimeLen_NewProgram_KEY = "FILETIMELEN_NEWPROGRAM_KEY";
	String strProgramID_NewProgram_KEY = "PROGRAMID_NEWPROGRAM_KEY";
	String strPosition_NewProgram_KEY = "POSITION_NEWPROGRAM_KEY";
	String strEnternType_NewProgram_KEY = "ENTERNTYPE_NEWPROGRAM_KEY";

	int mLongClick_NewProgram = 0;
	int mFunMenu_NewProgram = 1;

	String strSelPath_NewProgram_KEY = "SELPATH_NEWPROGRAM_KEY";

	String mFileSelectType_KEY = "FILESELECTTYPE_KEY";
	String mTermSelectType_KEY = "TERMSELECTTYPE_KEY";
	int mTermSelect_Check_Type = 0;
	int mTermSelect_Radio_Type = 1;
	int mUploadFile_Type = 2;

	String strSave_SelFilePath_Dir_KEY = "SAVE_SELFILEPATH_DIR_KEY";

	int nMp3_Frmes_Max = 3;
	int nMp3_Frmes_Min = 2;

	String mReplayFileName = "replaydata.bin";

	int mUnknown_ServiceState = -1;
	int mClosed_ServiceState = 0;
	int mStarted_ServiceState = 1;

	int MSG_REFRESH_UI = 1;
	int MSG_NOHAVE_TERMS = 2;
	int MSG_CONNECTSERVER_FAIL = 3;
	int MSG_DOVERB_FAIL = 4;
	int MSG_START_SOUNDCARD_PLAY_SUCCESS = 5;
	int MSG_STOP_SOUNDCARD_PLAY_SUCCESS = 6;
	int MSG_SOUNDCARD_PLAY_STATUS_FAIL = 7;

	// 是不是试用版本
	boolean mTryUse = false;
	String mExpireData = "2014-05-01"; // 试用到期日期

	String ACTION_NEW = "new";
	String ACTION_EDIT = "edit";
	String POSITION_INTENT_DATA = "position";

	int MAX_HISTORY_USER_COUNT = 4;
	int MAX_UPLOADFILE_COUNT = 5;

	
	//定制版
	String ZX = "zx";
	String ITC = "itc";
	String THREEA = "3a";
	String CRX = "crx";
	String SL = "sl";
	String LS = "ls";
	String DC = "dc";//东创
	String CEOPA = "ceopa";
	
}
