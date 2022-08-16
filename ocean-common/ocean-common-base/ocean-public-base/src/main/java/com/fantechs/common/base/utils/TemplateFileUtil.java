package com.fantechs.common.base.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by wcz on 2020/8/17.
 */
@Slf4j
public class TemplateFileUtil {
    public static final String OCEAN_SECURITY_EXCEL = "templates/excel/";

    /**
     * 获取resource目录下的资源文件
     *
     * @param dir              存放目录
     * @param templateFileName 模板文件名
     * @return
     */
    public static File getTemplate(String dir, String templateFileName) {
        if (StringUtils.isEmpty(templateFileName)) {
            return null;
        }
        File templateFile;
        InputStream in = null;
        try {
            if (dir == null) {
                dir = "";
            }
            String templateFolder = StrUtil.removeSuffix(StrUtil.removePrefix(dir, "/"), "/");

            String templateFilePath = templateFolder + StrUtil.prependIfMissing(templateFileName, "/");
            //创建本地目录
            if (!FileUtil.exist(templateFolder)) {
                FileUtil.mkdir(templateFolder);
            }
            //从jar包获取文件流
            in = TemplateFileUtil.class.getClassLoader().getResourceAsStream(templateFilePath);
            //创建临时存储模板文件
            templateFile = FileUtil.newFile(templateFilePath);
            //已存在该模板文件，先删除，再写入
            if(FileUtil.exist(templateFile)){
                FileUtil.del(templateFile);
            }
            FileUtil.writeFromStream(in, templateFile);
            if (FileUtil.isEmpty(templateFile)) {
                FileUtil.del(templateFile);
                return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return templateFile;
    }


    /**
     * 下载模板
     *
     * @param dir
     * @param templateFileName
     * @param response
     */
    public static void downloadTemplate(String dir, String templateFileName, HttpServletResponse response) {
        File templateFile = TemplateFileUtil.getTemplate(dir, templateFileName);
        downloadTemplate(templateFile, templateFileName, response);
    }

    /**
     * @param templateFile
     * @param response
     */
    public static void downloadTemplate(File templateFile, String templateFileName, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            response.setCharacterEncoding(CharsetUtil.UTF_8);
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("Content-Disposition", "filename=" + templateFileName);
            out = response.getOutputStream();
            FileUtil.writeToStream(templateFile, out);
            out.flush();
        } catch (Exception e) {
            log.error("下载模板失败！", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            FileUtil.del(templateFile);
        }
    }

    /**
     * 添加数据有效性校验
     *
     * @param templateFile       模板文件
     * @param sheetNo            sheet序号
     * @param firstRow           开始行
     * @param lastRow            结束行
     * @param firstCol           开始列
     * @param lastCol            结束列
     * @param explicitListValues 有效性检查的下拉列表
     * @throws IllegalArgumentException 如果传入的行或者列小于0(< 0)或者结束行/列比开始行/列小
     */
    public static void setValidationData(File templateFile, int sheetNo, int firstRow, int lastRow,
                                         int firstCol, int lastCol, String[] explicitListValues) {
        OutputStream out = null;
        try {
            InputStream in = new FileInputStream(templateFile);
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            in.close();
            Sheet sheet = workbook.getSheetAt(sheetNo);
            setValidationData(sheet, firstRow, lastRow, firstCol, lastCol, explicitListValues);
            out = new FileOutputStream(templateFile);
            workbook.write(out);
            out.flush();
            workbook.close();
        } catch (Exception e) {
            log.error("设置有效性校验失败！", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }


    /**
     * POI添加数据有效性检查.
     *
     * @param sheet              要添加此检查的Sheet
     * @param firstRow           开始行
     * @param lastRow            结束行
     * @param firstCol           开始列
     * @param lastCol            结束列
     * @param explicitListValues 有效性检查的下拉列表
     * @throws IllegalArgumentException 如果传入的行或者列小于0(< 0)或者结束行/列比开始行/列小
     */
    private static void setValidationData(Sheet sheet, int firstRow, int lastRow,
                                          int firstCol, int lastCol, String[] explicitListValues) throws IllegalArgumentException {
        if (firstRow < 0 || lastRow < 0 || firstCol < 0 || lastCol < 0 || lastRow < firstRow || lastCol < firstCol) {
            throw new IllegalArgumentException("Wrong Row or Column index : " + firstRow + ":" + lastRow + ":" + firstCol + ":" + lastCol);
        }
        if (sheet instanceof XSSFSheet) {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                    .createExplicitListConstraint(explicitListValues);
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        } else if (sheet instanceof HSSFSheet) {
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(explicitListValues);
            DataValidation validation = new HSSFDataValidation(addressList, dvConstraint);
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        }
    }
}
