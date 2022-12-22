package com.comtom.aibo.httpset;

import android.content.Context;
import android.content.Intent;

import com.comtom.aibo.entity.FileBean;
import com.comtom.aibo.entity.GroupBean;
import com.comtom.aibo.entity.HandleBean;
import com.comtom.aibo.entity.LoginBean;
import com.comtom.aibo.entity.PlayBean;
import com.comtom.aibo.entity.ProgInfoBean;
import com.comtom.aibo.entity.RealPlay;
import com.comtom.aibo.entity.ResponseBean;
import com.comtom.aibo.entity.TermIDBean;
import com.comtom.aibo.entity.TermsDetails;
import com.comtom.aibo.module.base.LoginNewActivity;
import com.comtom.aibo.utils.IConstant;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;
import com.haozi.dev.smartframe.rxhttp.request.Api;
import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;
import com.haozi.dev.smartframe.utils.tool.ShareDate;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * 网络请求本地库
 */
public class HttpApi extends Api {

    public static Service api() {
        return Api.api(Service.class);
    }


    public interface Service {

        /**
         * 登录
         */
        @POST("login")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<LoginBean>> login(@Body Map<String, Object> map);

        /**
         * 终端
         */
        @POST("getTermIds")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<TermIDBean>> getTermIds(@Body Map<String, Object> map);

        /**
         * 分组终端
         */
        @POST("getGroups")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<GroupBean>> getGroups(@Body Map<String, Object> map);

        /**
         * 终端详情
         */
        @POST("getTermState")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<TermsDetails>> getTermState(@Body Map<String, Object> map);

        /**
         * 实时对讲
         */
        @POST("RealPlayStart")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<RealPlay>> RealPlayStart(@Body Map<String, Object> map);

        /**
         * 设置指定会话下所有终端音量
         */
        @POST("SessionVolSet")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<BaseBean>> SessionVolSet(@Body Map<String, Object> map);

        /**
         * 停止实时对讲
         */
        @POST("RealPlayStop")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<BaseBean>> RealPlayStop(@Body Map<String, Object> map);

        /**
         * 创建文件广播会话
         */
        @POST("FileSessionCreate")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<RealPlay>> FileSessionCreate(@Body Map<String, Object> map);

        /**
         * 播放媒体库文件
         */
        @POST("FileSessionSetProg")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<PlayBean>> FileSessionSetProg(@Body Map<String, Object> map);

        /**
         * 删除文件广播会话
         */
        @POST("FileSessionDestory")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<BaseBean>> FileSessionDestory(@Body Map<String, Object> map);

        /**
         * 文件广播播放控制
         */
        @POST("FileSessionSetStat")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<BaseBean>> FileSessionSetStat(@Body Map<String, Object> map);

        /**
         * 获取目录下所有的节目和子目录
         */
        @POST("MLListDir")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<ProgInfoBean>> MLListDir(@Body Map<String, Object> map);

        /**
         * 文件上传配置
         */
        @POST("FileUpload")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<FileBean>> FileUpload(@Body Map<String, Object> map);

        /**
         * 创建媒体库节点
         */
        @POST("MLCreateNode")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<HandleBean>> MLCreateNode(@Body Map<String, Object> map);

        /**
         * 获取媒体库节点的进度
         */
        @POST("MLCreateNodeProcess")
        @Headers({Header.BASE_URL_REDIRECT + ":" + "BaseUrl"})
        Observable<ResponseBean<HandleBean>> MLCreateNodeProcess(@Body Map<String, Object> map);
    }

    /**
     * 错误状态码处理
     */
    public  static void failDeal(String code , String msg, Context context){
        if(code.equals("8000")){
            UtilToast.showToast(context,"接口未实现或者无效的接口名");
        }else if(code.equals("8001")){
            UtilToast.showToast(context,"token无效，请重新登录");
            context.startActivity(new Intent(context, LoginNewActivity.class));
        }else if(code.equals("1000")){
            UtilToast.showToast(context,"参数错误，缺少必须的参数");
        }else if(code.equals("1001")){
            UtilToast.showToast(context,"用户名、密码不正确");
        }else if(code.equals("1002")){
            UtilToast.showToast(context,"发起呼叫终端不在线");
        }else if(code.equals("1002")){
            UtilToast.showToast(context,"发起呼叫终端不在线");
        }else if(code.equals("1003")){
            UtilToast.showToast(context,"无效的终端ID");
        }else if(code.equals("1004")){
            UtilToast.showToast(context,"发起呼叫终端正忙");
        }else if(code.equals("1005")){
            UtilToast.showToast(context,"无效的终端通话编码");
        }else if(code.equals("1006")){
            UtilToast.showToast(context,"串口无效");
        }else if(code.equals("1007")){
            UtilToast.showToast(context,"串口操作超时");
        }else if(code.equals("1008")){
            UtilToast.showToast(context,"串口数据错误");
        }else{
            UtilToast.showToast(context,msg);
        }
    }
}
