package com.fantechs.common.base.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: bingo.ren
 * @Date: 2020/4/28 9:04
 * @Description: HTTP相关操作
 * @Version: 1.0
 */
public class HTTPUtils {

    /**
     * 获取下载输出流
     * @param response
     * @param fileName
     * @return
     * @throws IOException
     */
    public static ServletOutputStream getDownload(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),
                "ISO-8859-1"));
        response.flushBuffer();
        return response.getOutputStream();
    }
}
