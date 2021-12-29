package com.fantechs.service.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by lfz on 2020/10/19.
 */
public class MyBatisPlugin extends PluginAdapter {

    //添加注释总开关，generator.xml中设置为true！！！
    private boolean addRemarkComments = false;
    //是否需要添加swaggerui注释
    private boolean addSwaggerUi = true;
    //是否需要添加顶部作者姓名和创建时间
    private boolean addAuthorAndDate = true;
    //是否需要Lombok添加@Data注释
    private boolean addLombok = true;
    private Properties properties;
    private static final String LOMBOK_DATA = "lombok.Data";
    private static final String EXAMPLE_SUFFIX = "Example";
    private static final String API_MODEL_PROPERTY_FULL_CLASS_NAME = "io.swagger.annotations.ApiModelProperty";
    private static final String MODEL_EXCEL_FULL_CLASS_NAME = "cn.afterturn.easypoi.excel.annotation.Excel";
    private static final String MODEL_JSONFIEL_FULL_CLASS_NAME = "com.alibaba.fastjson.annotation.JSONField;";
    private static final String MODEL_VALIDGROUP_FULL_CLASS_NAME = "com.fantechs.common.base.support.ValidGroup;";



    @Override
    public boolean validate(List<String> warnings) {
        properties =   new Properties();
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加domain的import
        topLevelClass.addImportedType("lombok.Data");

        //添加domain的注解
        topLevelClass.addAnnotation("@Data");

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.setSuperClass("ValidGroup");
        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                topLevelClass.addJavaDocLine(" * " + remarkLine);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" * ").append(introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @author ").append(System.getProperties().getProperty("user.name"));
        topLevelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @date ");
        sb.append(getDateString());
        topLevelClass.addJavaDocLine(sb.toString());
        topLevelClass.addJavaDocLine(" */");
        //给model的字段添加swagger注解
        if (addSwaggerUi) {
            //只在model中添加swagger注解类的导入
            topLevelClass.addImportedType(new FullyQualifiedJavaType(API_MODEL_PROPERTY_FULL_CLASS_NAME));
            topLevelClass.addImportedType(new FullyQualifiedJavaType(MODEL_EXCEL_FULL_CLASS_NAME));
            topLevelClass.addImportedType(new FullyQualifiedJavaType(MODEL_JSONFIEL_FULL_CLASS_NAME));
            topLevelClass.addImportedType(new FullyQualifiedJavaType(MODEL_VALIDGROUP_FULL_CLASS_NAME));
        }
        if (addLombok) {
                topLevelClass.addImportedType(new FullyQualifiedJavaType(LOMBOK_DATA));
        }
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
//        field.addJavaDocLine("/**");
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                if (addSwaggerUi) {
                    field.addJavaDocLine("@ApiModelProperty(name=\""+field.getName()+"\",value = \"" + remarks + "\")");
                    if(remarks.indexOf("时间")>0){
                        field.addJavaDocLine("@Excel(name = \""+remarks+"\", height = 20, width = 30,orderNum=\"\",exportFormat =\"yyyy-MM-dd HH:mm:ss\") ");
                        field.addJavaDocLine("@@JsonFormat(pattern = \"yyyy-MM-dd\", timezone = \"GMT+8\")");
                        field.addJavaDocLine("@DateTimeFormat(pattern = \"yyyy-MM-dd\")");
                    }else{
                        field.addJavaDocLine("@Excel(name = \""+remarks+"\", height = 20, width = 30,orderNum=\"\") ");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加Mapper的import
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        //添加Mapper的注解
        interfaze.addAnnotation("@Mapper");
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成getter
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成setter
        return false;
    }

    protected String getDateString() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
