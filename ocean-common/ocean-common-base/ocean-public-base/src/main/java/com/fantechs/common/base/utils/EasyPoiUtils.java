package com.fantechs.common.base.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

public final class EasyPoiUtils {

    private EasyPoiUtils() {}

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new  RuntimeException(e);
        }
    }

    private static<T> void defaultExport(List<T> dataList, Class<?> clz, String fileName, HttpServletResponse response, ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, clz, dataList);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static<T> void exportExcel(List<T> dataList, String title, String sheetName, Class<?> clz, String fileName, boolean isCreateHeader, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(dataList, clz, fileName, response, exportParams);
    }

    public static<T> void exportExcel(List<T> dataList, String title, String sheetName, Class<?> clz, String fileName, HttpServletResponse response) {
        defaultExport(dataList, clz, fileName, response, new ExportParams(title, sheetName));
    }

    private static void defaultExport(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(dataList, ExcelType.XSSF);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static void exportExcel(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response) {
        defaultExport(dataList, fileName, response);
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> clz) {
        if (filePath == null || filePath.equals("")) {
            return null;
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);

        try {
            return ExcelImportUtil.importExcel(new File(filePath), clz, params);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> clz) {
        if (file == null) {
            return null;
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);

        try {
            return ExcelImportUtil.importExcel(file.getInputStream(), clz, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clz) throws IOException {
        if (file == null) {
            return null;
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(1);  //????????????????????????????????????  ??????0
        params.setHeadRows(1);  //????????????????????????????????? ??????1
        InputStream inputStream = file.getInputStream();
        try {
            return ExcelImportUtil.importExcel(inputStream, clz, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ????????????Sheet
     * @param map?????????Sheet????????????
     * @param clzList?????????Sheet??????????????????
     * @param fileName????????????????????????
     * @param response???????????????
     */
    public static<T> void exportExcelSheets(Map<String,String> map,List<Class<?>> clzList, String fileName, HttpServletResponse response) throws IOException {
        Set<String> sheetNames = map.keySet();
        List<Map<String, Object>> sheetsList = new ArrayList<>();

        int i = 0;
        for (String sheetName : sheetNames) {
            ExportParams reportWorkExportParams = new ExportParams();
            reportWorkExportParams.setSheetName(sheetName);
            reportWorkExportParams.setTitle(sheetName);
            // ??????sheet1?????????map
            Map<String, Object> exportMap = new HashMap<>();
            // title????????????ExportParams????????????????????????ExportParams????????????sheetName
            exportMap.put("title", reportWorkExportParams);
            // ?????????????????????????????????
            exportMap.put("entity", clzList.get(i));
            // sheet?????????????????????
            exportMap.put("data", JsonUtils.jsonToList(map.get(sheetName),clzList.get(i)));
            sheetsList.add(exportMap);
            i++;
        }

        // ????????????
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }



    /**
     * ????????????Sheet
     * @param map?????????Sheet????????????
     * @param clzList?????????Sheet??????????????????
     * @param fileName????????????????????????
     * @param response???????????????
     */
    public static<T> void exportExcelSheetList(Map<String, Object> map,List<Class<?>> clzList, String fileName, HttpServletResponse response) throws IOException {
        Set<String> sheetNames = map.keySet();
        List<Map<String, Object>> sheetsList = new ArrayList<>();

        int i = 0;
        for (String sheetName : sheetNames) {
            ExportParams reportWorkExportParams = new ExportParams();
            reportWorkExportParams.setSheetName(sheetName);
            reportWorkExportParams.setTitle(sheetName);
            // ??????sheet1?????????map
            Map<String, Object> exportMap = new HashMap<>();
            // title????????????ExportParams????????????????????????ExportParams????????????sheetName
            exportMap.put("title", reportWorkExportParams);
            // ?????????????????????????????????
            exportMap.put("entity", clzList.get(i));
            // sheet?????????????????????
            exportMap.put("data", map.get(sheetName));
            sheetsList.add(exportMap);
            i++;
        }

        // ????????????
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

}
