package com.comtom.aibo.utils;


import android.os.AsyncTask;

import com.haozi.dev.smartframe.utils.tool.UtilLog;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;


/**
 * FTP客户端工具
 *
 */
public final class FTPToolkit {

    private FTPToolkit() {
    }

    /**
     * 创建FTP连接
     *
     * @param host
     *            主机名或IP
     * @param port
     *            ftp端口
     * @param username
     *            ftp用户名
     * @param password
     *            ftp密码
     * @return 一个客户端
     * @throws Exception
     */
    public static FTPClient makeFtpConnection(String host, int port,
                                              String username, String password) throws Exception {
        FTPClient client = new FTPClient();
        try {
            client.connect(host, port);
            if(username != null && password != null) {
                client.login(username, password);
            }
        } catch (Exception e) {
            UtilLog.showNet("login==err==" + e.getMessage());
            throw new Exception(e);
        }
        return client;
    }

    /**
     * FTP上传本地文件到FTP的一个目录下
     *
     * @param client
     *            FTP客户端
     * @param localfile
     *            本地文件
     * @param remoteFolderPath
     *            FTP上传目录
     * @throws Exception
     */
    public static void upload(FTPClient client, File localfile,
                              String remoteFolderPath, FTPDataTransferListener listener) throws Exception {
        try {
            UtilLog.showNet("remoteFolderPath==" + remoteFolderPath);
//            int a = isExist(client,remoteFolderPath);
//            UtilLog.showNet("a====" + a);
            client.changeDirectory("/");
            if (!localfile.exists())
                throw new Exception("the upload FTP file"
                        + localfile.getPath() + "not exist!");
            if (!localfile.isFile())
                throw new Exception("the upload FTP file"
                        + localfile.getPath() + "is a folder!");
            if (listener != null)
                client.upload(remoteFolderPath,new FileInputStream(localfile),0,0,listener);
//                client.upload(localfile, listener);
            else
                client.upload(localfile);
            client.changeDirectory("/");
        } catch (Exception e) {
            UtilLog.showNet("upload==err==" + e.getMessage());
            throw new Exception(e);
        }
    }

    /**
     * FTP上传本地文件到FTP的一个目录下
     *
     * @param client
     *            FTP客户端
     * @param localfilepath
     *            本地文件路径
     * @param remoteFolderPath
     *            FTP上传目录
     * @throws Exception
     */
    public static void upload(FTPClient client, String localfilepath,
                              String remoteFolderPath, FTPDataTransferListener listener) throws Exception {
        File localfile = new File(localfilepath);
        upload(client, localfile, remoteFolderPath, listener);
    }

    /**
     * 批量上传本地文件到FTP指定目录上
     *
     * @param client
     *            FTP客户端
     * @param localFilePaths
     *            本地文件路径列表
     * @param remoteFolderPath
     *            FTP上传目录
     * @throws Exception
     */
    public static void uploadListPath(FTPClient client,
                                      List<String> localFilePaths, String remoteFolderPath, MyFtpListener listener) throws Exception {
        remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
        try {
            client.changeDirectory(remoteFolderPath);
            for (String path : localFilePaths) {
                File file = new File(path);
                if (!file.exists())
                    throw new Exception("the upload FTP file" + path + "not exist!");
                if (!file.isFile())
                    throw new Exception("the upload FTP file" + path
                            + "is a folder!");
                if (listener != null)
                    client.upload(file, listener);
                else
                    client.upload(file);
            }
            client.changeDirectory("//");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    /**
     * 批量上传本地文件到FTP指定目录上
     *
     * @param client
     *            FTP客户端
     * @param localFiles
     *            本地文件列表
     * @param remoteFolderPath
     *            FTP上传目录
     * @throws Exception
     */
    public static void uploadListFile(FTPClient client, List<File> localFiles,
                                      String remoteFolderPath, MyFtpListener listener) throws Exception {
        try {
            client.changeDirectory(remoteFolderPath);
            remoteFolderPath = PathToolkit.formatPath4FTP(remoteFolderPath);
            for (File file : localFiles) {
                if (!file.exists())
                    throw new Exception("the upload FTP file" + file.getPath()
                            + "not exist!");
                if (!file.isFile())
                    throw new Exception("the upload FTP file" + file.getPath()
                            + "is a folder!");
                if (listener != null)
                    client.upload(file, listener);
                else
                    client.upload(file);
            }
            client.changeDirectory("/");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    /**
     * 判断一个FTP路径是否存在，如果存在返回类型(FTPFile.TYPE_DIRECTORY=1、FTPFile.TYPE_FILE=0、
     * FTPFile.TYPE_LINK=2) 如果文件不存在，则返回一个-1
     *
     * @param client
     *            FTP客户端
     * @param remotePath
     *            FTP文件或文件夹路径
     * @return 存在时候返回类型值(文件0，文件夹1，连接2)，不存在则返回-1
     */
    public static int isExist(FTPClient client, String remotePath) {
        remotePath = PathToolkit.formatPath4FTP(remotePath);
        FTPFile[] list = null;
        try {
            list = client.list(remotePath);
        } catch (Exception e) {
            return -1;
        }
        if (list.length > 1)
            return FTPFile.TYPE_DIRECTORY;
        else if (list.length == 1) {
            FTPFile f = list[0];
            if (f.getType() == FTPFile.TYPE_DIRECTORY)
                return FTPFile.TYPE_DIRECTORY;
            // 假设推理判断
            String _path = remotePath + "/" + f.getName();
            try {
                int y = client.list(_path).length;
                if (y == 1)
                    return FTPFile.TYPE_DIRECTORY;
                else
                    return FTPFile.TYPE_FILE;
            } catch (Exception e) {
                return FTPFile.TYPE_FILE;
            }
        } else {
            try {
                client.changeDirectory(remotePath);
                return FTPFile.TYPE_DIRECTORY;
            } catch (Exception e) {
                return -1;
            }
        }
    }
    public static long getFileLength(FTPClient client, String remotePath) throws Exception {
        String remoteFormatPath = PathToolkit.formatPath4FTP(remotePath);
        if(isExist(client, remotePath) == 0) {
            FTPFile[] files = client.list(remoteFormatPath);
            return files[0].getSize();

        }else {
            throw new Exception("get remote file length error!");
        }
    }

    /**
     * 关闭FTP连接，关闭时候像服务器发送一条关闭命令
     *
     * @param client
     *            FTP客户端
     * @return 关闭成功，或者链接已断开，或者链接为null时候返回true，通过两次关闭都失败时候返回false
     */

    public static boolean closeConnection(FTPClient client) {
        if (client == null)
            return true;
        if (client.isConnected()) {
            try {
                client.logout();
                client.disconnect(true);
                return true;
            } catch (Exception e) {
                try {
                    client.disconnect(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

}
