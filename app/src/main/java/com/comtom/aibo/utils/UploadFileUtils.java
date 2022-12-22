//package com.comtom.aibo.utils;
//
//import com.haozi.dev.smartframe.utils.tool.UtilLog;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//public class UploadFileUtils {
//
//    private static boolean flag;
//
//    /**
//     * 需在在子线程下执行
//     * pathname 为：服务器存储地址 ，remote 为：上传到ftp服务器之后，文件的名字 ，path 为：要上传文件的地址
//     * @param username
//     * @param password
//     * @param pathname
//     * @param remote
//     * @param path
//     * @return
//     */
//
//    public static boolean uploadfile(String username,String password,String pathname,String remote,String path ) {
//        UtilLog.showNet("loginASDAAAAAAAAAAAAAAAAAA:");
//        //服务器域名
//        String host = "  ";
//        int port = 21;
////        String username = "";
////        String password = "";
//        //  String pathname = "E:/ftp";  //上传到服务器之后存放的位置
//        //  String remote = "9.mp4"; //上传到ftp服务器之后，文件的名字
//        InputStream is = null;
//        try {
//            UtilLog.showNet("11111:");
//            is = new FileInputStream(path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        //uploadFile(host, port, username, password, pathname, remote,is);
//        try {
//            UtilLog.showNet("2222:");
//            //创建FTPClient对象
//            FTPClient client = new FTPClient();
//            Log.e("TAG","3333:");
//            //链接ftp服务
//            client.connect(host, port);
//            Log.e("TAG","555:");
//            //登录ftp服务器
//            boolean login = client.login(username, password);
//            // System.out.println("login:" + login);
//            Log.e("TAG","login:" + login);
//            //判断pathname是否存在,//切换ftp服务的工作目录
//            Log.e("TAG","666:");
//            if (!client.changeWorkingDirectory(pathname)) {
//
//                client.makeDirectory(pathname); //创建工作目录
//
//                client.changeWorkingDirectory(pathname);
//            }
//
//            //设置上传的文件类型
//            client.setFileType(FTP.BINARY_FILE_TYPE);
//
//            //创建InputStream对象
//            //InputStream is=new FileInputStream("F:/2.png");
//
//            //完成文件上传
//            flag = client.storeFile(remote, is);
//
//            Log.e("TAG","flag:" + flag);
//            client.logout(); //退出
//
//            client.disconnect(); //断开链接
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return flag;
//    }
//}
