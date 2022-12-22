package com.comtom.aibo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import com.comtom.aibo.R;
import com.comtom.aibo.app.AirApp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UIUtil {
	private static Toast sToast = null;

	public static int taskNameIndex = 1;
	public static int speakNameIndex = 1;

	/**
	 * Toast
	 * 
	 * @param msg
	 * @param duration
	 */
	public static Toast showToast(String msg, int duration) {
		if (sToast == null) {
			sToast = Toast.makeText(AirApp.instance().getApplicationContext(),
					msg, duration);
		} else {
			sToast.setDuration(duration);
			sToast.setText(msg);
		}

		sToast.show();

		return sToast;
	}

	public static void cancelToast() {
		if (sToast != null) {
			sToast.cancel();
			sToast = null;
		}
	}

	//
	public static boolean isIpAddress(String value) {

		if (TextUtils.isEmpty(value)) {
			return false;
		}

		int start = 0;
		int end = value.indexOf('.');
		int numBlocks = 0;

		while (start <= value.length()) {

			if (end == -1) {
				end = value.length();
			}

			try {
				String strtmp = value.substring(start, end);
				if (TextUtils.isEmpty(value)) {
					return false;
				}
				int block = Integer.parseInt(strtmp);
				if ((block > 255) || (block < 0)) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}

			numBlocks++;

			start = end + 1;
			end = value.indexOf('.', start);
		}

		return numBlocks == 4;
	}

	public static void showProgress(Activity activity, int nresid) {
		try {
			activity.findViewById(R.id.logoningtip_id).setVisibility(
					View.VISIBLE);

			TextView textView = (TextView) activity
					.findViewById(R.id.tip_progress_id);
			textView.setText(nresid);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean isShowProgress(Activity activity) {
		try {
			if (activity.findViewById(R.id.logoningtip_id).getVisibility() == View.VISIBLE) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static void showProgress(Activity activity, String strInfo) {
		try {
			if (null != activity) {
				View view = activity.findViewById(R.id.logoningtip_id);
				if (null != view) {
					view.setVisibility(View.VISIBLE);
				}

				TextView textView = (TextView) activity
						.findViewById(R.id.tip_progress_id);
				if (null != textView && null != strInfo) {
					textView.setText(strInfo);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void hideProgress(Activity activity) {
		try {
			View view = null;
			if (null != activity
					&& (null != (view = activity
							.findViewById(R.id.logoningtip_id)))) {
				view.setVisibility(View.GONE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void showProgress(View view, int nresid) {
		view.findViewById(R.id.logoningtip_id).setVisibility(View.VISIBLE);

		TextView textView = (TextView) view.findViewById(R.id.tip_progress_id);
		textView.setText(nresid);
	}

	public static void hideProgress(View view) {
		view.findViewById(R.id.logoningtip_id).setVisibility(View.GONE);
	}

	static int msreenWidth, msreenHeight;

	public static void setScreenWidth(int nwidth) {
		msreenWidth = nwidth;
	}

	public static void setScreenHeight(int nheight) {
		msreenHeight = nheight;
	}

	public static void setScreenWidthAndHeight(int nwidth, int nheight) {
		msreenWidth = nwidth;
		msreenHeight = nheight;
	}

	public static int getScreenWidth() {
		return msreenWidth;
	}

	public static int getScreenHeight() {
		return msreenHeight;
	}

	/**
	 * 
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * 缃戠粶搴�
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] toHH(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

	public static byte[] toHHS(short n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) (n >> 8 & 0xff);

		return b;
	}

	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;// 寰�珮浣嶆父
		}
		return value;
	}

	public static void Udp_sendto(byte[] sendbuff, int nsendlen,
			String strUdpIp, int nUdpPort) {
		if (nsendlen <= 0 || null == sendbuff) {
			return;
		}

		try {
			InetAddress ip = InetAddress.getByName(strUdpIp);

			DatagramSocket sendSocket = new DatagramSocket();// 閲囩敤UDP鍙戦�鏁版嵁
			DatagramPacket sendPacket = new DatagramPacket(sendbuff, nsendlen);
			sendPacket.setAddress(ip);
			sendPacket.setPort(nUdpPort);

			sendSocket.send(sendPacket);

			sendSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void destroy() {
		sToast = null;
	}

	public static String getCurTime1() {// YY-MM-DD
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String nowTime = format.format(new Date());

		return nowTime;
	}

	public static String getCurTime2() {// YY-MM-DD HH:MM:SS

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = format.format(new Date());

		return nowTime;
	}

	public static String getCurTime3() {// HH:MM

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String nowTime = format.format(new Date());

		return nowTime;
	}

	/**
	 * NAME=AAA\tSEX=2\n
	 * 
	 * @param strTotal
	 * @param strKey
	 * @param strEnd
	 * @return
	 */
	public static String getSubValue(String strTotal, String strKey) {
		String strValue = null;
		int npos = strTotal.indexOf(strKey);
		if (-1 != npos) {
			int nendpos = strTotal.indexOf("\t", npos + strKey.length());
			if (-1 != nendpos) {
				strValue = strTotal.substring(npos + strKey.length() + 1,
						nendpos);
			} else {
				strValue = strTotal.substring(npos + strKey.length() + 1);
				strValue = strValue.trim();
			}
		}

		return strValue;
	}

	public static ArrayList<String> getStringArray(String strTotal,
			String strSplit) {
		ArrayList<String> value = new ArrayList<String>();

		String strSub = "";
		int npos = -1;
		while (-1 != (npos = strTotal.indexOf(strSplit))) {
			strSub = strTotal.substring(0, npos);
			if (!TextUtils.isEmpty(strSub)) {
				value.add(strSub);
			}
			strTotal = strTotal.substring(npos + strSplit.length());
		}
		strSub = strTotal.trim();
		if (!TextUtils.isEmpty(strSub)) {
			value.add(strSub);
		}

		return value;
	}

	public static String getExtByFileFullPath(String strFileFullPath) {
		String ext = "";

		String strName = strFileFullPath;
		int npos = strFileFullPath.lastIndexOf("/");
		if (-1 != npos) {
			strName = strFileFullPath.substring(npos + 1);
		}

		strName = strName.toLowerCase();
		npos = strName.lastIndexOf(".");
		if (-1 != npos) {
			ext = strName.substring(npos + 1);
			ext = ext.trim();
		}

		return ext;
	}

	public static String getNameByFileFullPath(String strFileFullPath) {
		String strName = strFileFullPath;
		int npos = strFileFullPath.lastIndexOf("/");// 目录
		if (-1 != npos) {
			int npos2 = strFileFullPath.lastIndexOf(".");
			if (-1 != npos2) {
				strName = strFileFullPath.substring(npos + 1, npos2);
			}
		}
		return strName;
	}

	public static String getDirByFileFullPath(String strFileFullPath) {
		String strDir = "";
		int npos = strFileFullPath.lastIndexOf("/");

		strDir = strFileFullPath.substring(0, npos);

		return strDir;
	}

	public static String getSDCardPath() {
		String strPath = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			strPath = Environment.getExternalStorageDirectory().toString();
		}

		return strPath;
	}

	/**
	 * 
	 * 
	 * @param fis
	 * @return byte[]
	 */

	public static Object file2Object(FileInputStream fis/* String fileName */) {

		// FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			// fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			Object object = ois.readObject();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * object杞寲涓烘枃浠�
	 * 
	 * @param obj
	 * @param fos
	 */
	public static void object2File(Object obj, FileOutputStream fos) {
		ObjectOutputStream oos = null;
		try {
			// fos = new FileOutputStream(new File(outputFile));
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public static String getAppDir(Context context) {
		try {
			return context.getApplicationContext().getFilesDir()
					.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/data/data/com.comtom.ipaircontrol/files";
	}

	// tid=aaa&p=ggg
	public static String getSubString(String data, String tag, String strSplite) {
		String strResult = "";

		String strSrc = data;
		int npos = strSrc.indexOf(tag + "=");
		if (-1 != npos) {
			int nendpos = strSrc.indexOf(strSplite, npos);
			if (-1 != nendpos) {
				strResult = strSrc.substring(npos + tag.length() + 1, nendpos);
				strResult = strResult.trim();
			} else {
				strResult = strSrc.substring(npos + tag.length() + 1);
				strResult = strResult.trim();
			}
		}

		return strResult;
	}

	// (aaa)
	public static String getSubString2(String data, String tag, String strSplite) {
		String strResult = "";

		String strSrc = data;
		int npos = strSrc.indexOf(tag);
		if (-1 != npos) {
			int nendpos = strSrc.indexOf(strSplite, npos);
			if (-1 != nendpos) {
				strResult = strSrc.substring(npos + tag.length(), nendpos);
				strResult = strResult.trim();
			} else {
				strResult = strSrc.substring(npos + tag.length());
				strResult = strResult.trim();
			}
		}

		return strResult;
	}

	static public String charToHex(char c) {
		String result;
		char first, second;

		first = (char) ((c & 0xF0) / 16);
		first += first > 9 ? 'A' - 10 : '0';
		second = (char) (c & 0x0F);
		second += second > 9 ? 'A' - 10 : '0';

		result = String.valueOf(first) + String.valueOf(second);

		return result;
	}

	// = % &
	static public String urlEncode(String src, char sep) {
		String result = "";
		int len = src.length();

		for (int i = 0; i < len; ++i) {

			char ch = src.charAt(i);
			if (ch == sep || ch == '=' || ch == '%') {
				result += "%" + charToHex(ch);
			} else {
				result += ch;
			}

		}

		return result;
	}

	// = % &
	static public String urlDecode(String src) {
		String result = "";
		int len = src.length();

		src = src.replaceAll("%25", "%");
		src = src.replaceAll("%26", "&");
		src = src.replaceAll("%3D", "=");
		src = src.replaceAll("%3d", "=");

		result = src;
		return result;
	}

	public static boolean HideSoftInput(Context context, View view) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (null != imm) {
				return imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	private static WakeLock wakeLock = null;

	static public void acquireWakeLock(Context context) {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "comtom");
			wakeLock.acquire();
		}
	}

	static public void releaseWakeLock() {
		try {
			if (wakeLock != null && wakeLock.isHeld()) {
				wakeLock.release();
				wakeLock = null;
			}
		} catch (Exception ex) {

		}
	}

	static public String getTimeAttr(String strStartTime, String strEndTime,
			int nType, int nMask, int nJobdata, int nDuration) {
		String strTimeArrt = "";

		int npos = strStartTime.indexOf(" ");
		if (-1 != npos) {
			int nenddatapos = -1;
			if (null != strEndTime) {
				nenddatapos = strEndTime.indexOf(" ");
			}
			if (IConstant.JOB_FLAG_EDATE == (nMask & IConstant.JOB_FLAG_EDATE)
					&& -1 != nenddatapos) {
				strTimeArrt += "从" + strStartTime.substring(0, npos) + "到"
						+ strEndTime.substring(0, nenddatapos);
			} else {
				if (nDuration > 0) {
					strTimeArrt += "从" + strStartTime.substring(0, npos) + "起";
				}
			}

			switch (nType) {
			case IConstant.JOB_TYPE_DAY: {
				if (1 == nJobdata) {
					strTimeArrt += ",每天";
				} else {
					strTimeArrt += ",每" + nJobdata + "天";
				}
			}
				break;
			case IConstant.JOB_TYPE_WEEK: {

				int nInteraval = (nJobdata >> 16);

				if (1 == nInteraval) {
					strTimeArrt += ",每周";
				} else {
					strTimeArrt += ",每" + nInteraval + "周的";
				}

				boolean bHaveDot = false;
				if (0x01 == (nJobdata & 0x01)) {
					strTimeArrt += "星期一";
					bHaveDot = true;
				}
				if (0x02 == (nJobdata & 0x02)) {
					if (bHaveDot) {
						strTimeArrt += ",";
					}

					strTimeArrt += "星期二";
					bHaveDot = true;
				}
				if (0x04 == (nJobdata & 0x04)) {
					if (bHaveDot) {
						strTimeArrt += ",";
					}

					strTimeArrt += "星期三";
					bHaveDot = true;
				}
				if (0x08 == (nJobdata & 0x08)) {
					if (bHaveDot) {
						strTimeArrt += ",";
					}

					strTimeArrt += "星期四";
					bHaveDot = true;
				}
				if (0x10 == (nJobdata & 0x10)) {
					if (bHaveDot) {
						strTimeArrt += ",";
					}

					strTimeArrt += "星期 五";
					bHaveDot = true;
				}
				if (0x20 == (nJobdata & 0x20)) {
					if (bHaveDot) {
						strTimeArrt += ",";
					}

					strTimeArrt += "星期六";
					bHaveDot = true;
				}
				if (0x40 == (nJobdata & 0x40)) {
					if (bHaveDot) {
						strTimeArrt += ",";
					}

					strTimeArrt += "星期天";
					bHaveDot = true;
				}
			}
				break;
			case IConstant.JOB_TYPE_MONTH: {
				String str2 = "";

				int nWeekData = (nJobdata >> 16); // 高两位

				boolean bHaveDot = false;
				int nHavaMonth = 0;
				if (0x01 == (nWeekData & 0x01)) {
					nHavaMonth++;
					bHaveDot = true;
					str2 += "一月";
				}
				if (0x02 == (nWeekData & 0x02)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "二月";
				}
				if (0x04 == (nWeekData & 0x04)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "三月";
				}
				if (0x08 == (nWeekData & 0x08)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "四月";
				}
				if (0x10 == (nWeekData & 0x10)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "五月";
				}
				if (0x20 == (nWeekData & 0x20)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "六月";
				}
				if (0x40 == (nWeekData & 0x40)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "七月";
				}
				if (0x80 == (nWeekData & 0x80)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "八月";
				}
				if (0x100 == (nWeekData & 0x100)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "九月";
				}
				if (0x200 == (nWeekData & 0x200)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "十月";
				}
				if (0x400 == (nWeekData & 0x400)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "十一月";
				}
				if (0x800 == (nWeekData & 0x800)) {
					nHavaMonth++;
					if (bHaveDot) {
						str2 += ",";
					}
					bHaveDot = true;
					str2 += "十二月";
				}
				if (nHavaMonth >= 12) {
					str2 = "每月";
				}

				String str1 = "";
				if (0x10 == (nMask & 0x10)) {
					int nWeekNo = ((nJobdata >> 8) & 0xff); // 取 低第二 字节
					int nWeekNo1 = (nWeekNo >> 4); // 取高四位
					int nWeekNo2 = (nWeekNo & 0x0F); // 取第四位

					String strTmp = "";
					switch (nWeekNo2) {
					case 0: {
						strTmp = "一";
					}
						break;
					case 1: {
						strTmp = "二";
					}
						break;
					case 2: {
						strTmp = "三";
					}
						break;
					case 3: {
						strTmp = "四";
					}
						break;
					case 4: {
						strTmp = "五";
					}
						break;
					case 5: {
						strTmp = "六";
					}
						break;
					case 6: {
						strTmp = "天";
					}
						break;
					default:
						break;
					}

					String strtmp2 = "";
					switch (nWeekNo1) {
					case 0: {
						strtmp2 = "第一个";
					}
						break;
					case 1: {
						strtmp2 = "第二个";
					}
						break;
					case 2: {
						strtmp2 = "第三个";
					}
						break;
					case 3: {
						strtmp2 = "第四个";
					}
						break;
					case 4: {
						strtmp2 = "最后一个";
					}
						break;
					default:
						break;
					}

					str1 += strtmp2 + "星期" + strTmp;
				} else {

					int nDayNo = (nJobdata & 0xFF);
					str1 += "第" + nDayNo + "天";
				}

				strTimeArrt += ",在" + str2 + str1;
			}
				break;
			case IConstant.JOB_TYPE_ONCE: {
				strTimeArrt = strStartTime.substring(0, npos);
			}
				break;
			case IConstant.JOB_TYPE_START: {

			}
				break;
			default:
				break;
			}

			if (nDuration > 0) {
				int nhour = nDuration / 3600;
				int nminute = (nDuration % 3600) / 60;
				int nsecond = (nDuration % 3600) % 60;

				strTimeArrt += ",从" + strStartTime.substring(npos + 1) + "起";

				strTimeArrt += ",持续";
				if (nhour > 0) {
					strTimeArrt += nhour + "小时";
				}
				if (nminute > 0) {
					strTimeArrt += nminute + "分钟";
				}
				if (nsecond > 0) {
					strTimeArrt += nsecond + "秒";
				}
			} else {
				strTimeArrt += "," + strStartTime.substring(npos + 1);
			}
		}

		return strTimeArrt;
	}
	//获取版本渠道名称
	@SuppressLint("WrongConstant")
	public static String getMetaDate(Context context){
		Object channel = null;
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo;
		try {
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
			if(applicationInfo != null && applicationInfo.metaData != null){
				channel = applicationInfo.metaData.get("qudao");
			}
		} catch (NameNotFoundException e) {
			// TODO: handle exception
			throw new RuntimeException("Could not read the name in the manifest file.", e);
		}
		if(channel == null){
			throw new RuntimeException("The name qudao is not defined in the manifest file's meta data");
		}
		Log.i("type",channel+"--->qudao");
		return channel.toString();
	}
}
