package com.fantechs.provider.base.util;

import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
     * Description: 从FTP服务器下载文件
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @return
     */
    public InputStream downFile(String remotePath,String fileName) {
        InputStream in = null;
        try {
            if(!this.ftpClient.printWorkingDirectory().equals(remotePath)){
                this.ftpClient.changeWorkingDirectory(remotePath);//转移到FTP服务器目录
            }
            in = this.ftpClient.retrieveFileStream(fileName);
            return in;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.ftpClient.isConnected()) {
                try {
                    this.ftpClient.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return in;
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
    public static void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    /**
     * 下载压缩文件
     * @param response
     * @param zipFileName
     */
    public void downloadZipFiles(HttpServletResponse response,List<InputStream> inputStreams, String zipFileName,String[] fileName) {
        try {

            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");// 不同类型的文件对应不同的MIME类型 // 重点突出
            // 对文件名进行编码处理中文问题
            zipFileName = new String(zipFileName.getBytes(), StandardCharsets.UTF_8);
            // inline在浏览器中直接显示，不提示用户下载
            // attachment弹出对话框，提示用户进行下载保存本地
            // 默认为inline方式
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipFileName+".zip", "UTF-8"));

            // --设置成这样可以不用保存在本地，再输出， 通过response流输出,直接输出到客户端浏览器中。
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            zipFile(inputStreams,zos,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 压缩文件
     * @param inputStreams
     * @param zos
     */
    public static  void zipFile(List<InputStream> inputStreams, ZipOutputStream zos, String[] fileName) {
        //设置读取数据缓存大小
        byte[] buffer = new byte[4096];
        try {
            //循环读取文件路径集合，获取每一个文件的路径
            int i=0;
            for (InputStream inputStream : inputStreams) {
                //判断文件是否存在
                //判断是否属于文件，还是文件夹
                //创建输入流读取文件
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                //将文件写入zip内，即将文件进行打包
                zos.putNextEntry(new ZipEntry(fileName[i]));
                //写入文件的方法，同上
                int size = 0;
                //设置读取数据缓存大小
                while ((size = bis.read(buffer)) > 0) {
                    zos.write(buffer, 0, size);
                }
                //关闭输入输出流
                zos.closeEntry();
                bis.close();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != zos) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
