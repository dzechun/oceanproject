package com.fantechs.provider.bcm.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.TimeZone;

/**
 * @author Mr.Lei
 * @create 2020/12/17
 */
@Component
public class FTPUtil {
    private Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private FTPClient ftpClient = null;
    private boolean isLogin = false;

    /**
     * 创建FTP连接
     *
     * @param url      地址
     * @param port     端口
     * @param userName 用户名
     * @param password 密码
     * @return
     * @throws IOException
     */
    public boolean connectFTP(String url, int port, String userName, String password) throws IOException {
        this.ftpClient = new FTPClient();
        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        ftpClientConfig.setServerLanguageCode("zh");
        this.ftpClient.setControlEncoding("GBK");
        this.ftpClient.configure(ftpClientConfig);
        this.ftpClient.connect(url.trim(), port);
        //ftp连接回答返回码
        int reply = this.ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            this.ftpClient.disconnect();
            logger.info("连接FTP服务器失败，code:\t{}", reply);
            return this.isLogin;
        }
        this.ftpClient.login(userName.trim(), password.trim());
        //设置传输协议
        this.ftpClient.enterLocalPassiveMode();
        this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        logger.info("用户：{}登录FTP服务器：{}", userName, url);
        this.isLogin = true;
        this.ftpClient.setBufferSize(1024 * 2);
        this.ftpClient.setDataTimeout(30 * 1000);
        return this.isLogin;
    }

    /**
     * 文件上传
     *
     * @param localFile        文件
     * @param remotUploadePath 存放路径
     * @return
     */
    public boolean uploadFile(File localFile, String remotUploadePath) {
        BufferedInputStream inputStream = null;
        boolean success = false;
        try {
            //选择存放路径 true：路径存在 flase：路径不存在
            String gbk = new String(remotUploadePath.trim().getBytes("GBK"), "ISO-8859-1");
            boolean isDirectory = this.ftpClient.changeWorkingDirectory(remotUploadePath.trim());
            if(!isDirectory){
                //创建文件夹
                this.ftpClient.makeDirectory(gbk);
                this.ftpClient.changeWorkingDirectory(remotUploadePath.trim());
            }
            inputStream = new BufferedInputStream(new FileInputStream(localFile));
            logger.info("{}开始上传", localFile.getName());
            success = this.ftpClient.storeFile(new String(localFile.getName().getBytes("GBK"),"ISO-8859-1"), inputStream);
            if (success) {
                logger.info("上传成功");
                return success;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * 文件删除
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return
     */
    public boolean deleteFile(String filePath, String fileName) {
        boolean success = false;
        try {
            boolean isDirectory = this.ftpClient.changeWorkingDirectory(filePath);
            if(!isDirectory){
                logger.info("文件路径不存在");
            }
            success = this.ftpClient.deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("删除文件异常");
        }
        return success;
    }

    /**
     * 退出登录关闭连接
     */
    public void loginOut() {
        if (null != this.ftpClient && this.ftpClient.isConnected()) {
            try {
                boolean logout = this.ftpClient.logout();
                if (logout) {
                    logger.info("成功退出FTP服务器");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("退出FTP服务器异常：\t{}", e.getMessage());
            } finally {
                try {
                    //关闭FTP服务器连接
                    this.ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("关闭FTP服务器异常：\t{}", e.getMessage());
                }
            }
        }
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }
}
