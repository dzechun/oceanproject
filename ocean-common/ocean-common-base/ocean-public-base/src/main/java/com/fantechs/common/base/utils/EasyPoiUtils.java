package com.fantechs.common.base.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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


    /**
     * 自定义导出
     * @param dataList
     * @param title
     * @param sheetName
     * @param fileName
     * @param response
     * @param <T>
     */
    public static<T> void customExportExcel(List<T> dataList, List<Map<String, Object>> customFormMapList, String title, String sheetName, String fileName, HttpServletResponse response) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String fromRout = httpServletRequest.getHeader("fromRout");

        List beanList =new ArrayList();
        // customFormMapList.forEach(item -> {
        //     beanList.add(new ExcelExportEntity((String) item.get("itemName"), (String) item.get("itemKey")));
        // });

        beanList.add(new ExcelExportEntity("学生姓名", "name"));
        beanList.add(new ExcelExportEntity("学生性别", "sex"));
        beanList.add(new ExcelExportEntity("进校日期", "registrationDate"));
        List list =new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Map map = new HashMap<>();
            map.put("name", i + "aa");
            map.put("sex", i + "bb");
            map.put("registrationDate", i + "cc");
            map.put("registrationDate1", i + "cc");
            map.put("registrationDate2", i + "cc");
            map.put("registrationDate3", i + "cc");
            list.add(map);
        }

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("测试", "测试"), beanList, list);
        if (workbook != null) {
            downLoadExcel("ff.xls", response, workbook);
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
        params.setTitleRows(1);  //导入数据的时候排除标题行  默认0
        params.setHeadRows(1);  //导入数据的时候排除表头 默认1
        InputStream inputStream = file.getInputStream();
        try {
            return ExcelImportUtil.importExcel(inputStream, clz, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 导出多个Sheet
     * @param map：多个Sheet数据集合
     * @param clzList：每个Sheet对应的实体类
     * @param fileName：导出的文件名称
     * @param response：响应对象
     */
    public static<T> void exportExcelSheets(Map<String,String> map,List<Class<?>> clzList, String fileName, HttpServletResponse response) throws IOException {
        Set<String> sheetNames = map.keySet();
        List<Map<String, Object>> sheetsList = new ArrayList<>();

        int i = 0;
        for (String sheetName : sheetNames) {
            ExportParams reportWorkExportParams = new ExportParams();
            reportWorkExportParams.setSheetName(sheetName);
            reportWorkExportParams.setTitle(sheetName);
            // 创建sheet1使用得map
            Map<String, Object> exportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            exportMap.put("title", reportWorkExportParams);
            // 模版导出对应得实体类型
            exportMap.put("entity", clzList.get(i));
            // sheet中要填充得数据
            exportMap.put("data", JsonUtils.jsonToList(map.get(sheetName),clzList.get(i)));
            sheetsList.add(exportMap);
            i++;
        }

        // 执行方法
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }



    /**
     * 导出多个Sheet
     * @param map：多个Sheet数据集合
     * @param clzList：每个Sheet对应的实体类
     * @param fileName：导出的文件名称
     * @param response：响应对象
     */
    public static<T> void exportExcelSheetList(Map<String, Object> map,List<Class<?>> clzList, String fileName, HttpServletResponse response) throws IOException {
        Set<String> sheetNames = map.keySet();
        List<Map<String, Object>> sheetsList = new ArrayList<>();

        int i = 0;
        for (String sheetName : sheetNames) {
            ExportParams reportWorkExportParams = new ExportParams();
            reportWorkExportParams.setSheetName(sheetName);
            reportWorkExportParams.setTitle(sheetName);
            // 创建sheet1使用得map
            Map<String, Object> exportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            exportMap.put("title", reportWorkExportParams);
            // 模版导出对应得实体类型
            exportMap.put("entity", clzList.get(i));
            // sheet中要填充得数据
            exportMap.put("data", map.get(sheetName));
            sheetsList.add(exportMap);
            i++;
        }

        // 执行方法
        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

}
