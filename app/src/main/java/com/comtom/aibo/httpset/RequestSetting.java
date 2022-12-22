package com.comtom.aibo.httpset;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.comtom.aibo.utils.IConstant;
import com.haozi.dev.smartframe.rxhttp.request.setting.DefaultRequestSetting;
import com.haozi.dev.smartframe.rxhttp.request.setting.ParameterGetter;
import com.haozi.dev.smartframe.utils.tool.ShareDate;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class RequestSetting extends DefaultRequestSetting {

    private boolean isDebug = true ;
    private Context context ;

    public RequestSetting(Context context){
        this.context = context ;
    }


    @NonNull
    @Override
    public String getBaseUrl() {
        return "http://" + ShareDate.getShare(context,IConstant.strServerIp_KEY,"10.0.0.1")  + ":8081/";
    }

    @Nullable
    @Override
    public Map<String, String> getRedirectBaseUrl() {
        Map<String, String> url = new HashMap<>();
        url.put("BaseUrl","http://" + ShareDate.getShare(context,IConstant.strServerIp_KEY,"10.0.0.1")  + ":8081/");
        return url;
    }

    @Override
    public String getSuccessCode() {
        return "0";
    }

    @Override
    public long getTimeout() {
        return 20000;
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }

    @Nullable
    @Override
    public Map<String, ParameterGetter> getDynamicHeaderParameter() {
        Map<String, ParameterGetter> parameters = new HashMap<>(2);
        parameters.put("Cookie", new ParameterGetter() {
            @Override
            public String get() {
//                return ShareDate.getShare(context,"token","");
                return "JSESSIONID=" + ShareDate.getShare(context,"token","");
            }
        });
        return parameters;
    }

//        @Override
//    public Map<String, String> getStaticPublicQueryParameter() {
//        Map<String, String> parameters = new HashMap<>(2);
////        parameters.put("Cookie", "JSESSIONID=" + ShareDate.getShare(context,"token",""));
//        parameters.put("version_code", "1");
//        parameters.put("device_num", "666");
//        return parameters;
//    }

//    @Override
//    public Map<String, ParameterGetter> getDynamicPublicQueryParameter() {
//        Map<String, ParameterGetter> parameters = new HashMap<>(2);
//        if(!ShareDate.getShare(context,"token","").equals("")) {
//            parameters.put("Cookie", new ParameterGetter() {
//                @Override
//                public String get() {
//                    return "JSESSIONID=" + ShareDate.getShare(context, "token", "");
//                }
//            });
//        }
//        return parameters;
//    }

    @Override
    public boolean ignoreSslForHttps() {
        return true;
    }

    @Override
    public void setOkHttpClient(OkHttpClient.Builder builder) {
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }


}
