package com.fantechs.common.base.response;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * mybatis 拦截器
 */
@Intercepts(
        {@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Slf4j
public class MyBatisInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(MyBatisInterceptor.class);

    private static final String PARAM_NAME = "query";
    private static final String BOUNDSQL_NAME = "sql";
    private static final String FULL_FROM_QUERY= "#FULL_FROM_QUERY";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 重写全表单查询Sql
        RoutingStatementHandler targetObject = (RoutingStatementHandler) invocation.getTarget();
        Object sd = invocation.proceed();
        if (ObjectUtil.isNotNull(targetObject)) {
            ParameterHandler parameterHandler = targetObject.getParameterHandler();
            if (ObjectUtil.isNotNull(parameterHandler)) {
                HashMap parameterObject = (HashMap) parameterHandler.getParameterObject();
                if (!CollectionUtils.isEmpty(parameterObject) && !StringUtils.isEmpty(parameterObject.get(PARAM_NAME))) {
                    BoundSql boundSql = targetObject.getBoundSql();
                    String sql = boundSql.getSql().trim();
                    String updateSql = overrideSql(parameterObject, boundSql.getSql().trim());
                    Field field = boundSql.getClass().getDeclaredField(BOUNDSQL_NAME);
                    field.setAccessible(true);
                    field.set(boundSql, updateSql);
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 重写Sql语句
     *
     * @param parameterObject
     * @param sql
     * @return
     */
    private String overrideSql(HashMap parameterObject, String sql) {
        try {
            logger.info("进入重写全表单查询param：{}", parameterObject);
            StringBuffer sb = new StringBuffer();
            String totalSql = "select count(0) from";
            if (sql.toLowerCase().contains(totalSql)) {
                //分页查询统计总数的Sql
                logger.info("进入重写分页统计总数的sql");
                overrideTotalNumSql(sb, parameterObject, sql);
            } else {
                //查询具体字段的Sql
                logger.info("进入重写分页查询列表的sql");
                overrideListSql(sb, parameterObject, sql);
            }
            return sql;
        } catch (Exception e) {
            logger.error("重写Sql语句异常，取消重写", e.getMessage());
            return sql;
        }
    }

    /**
     * 重写分页统计总数的sql
     *
     * @param sb
     * @param parameterObject
     * @param sql
     * @return
     */
    private StringBuffer overrideTotalNumSql(StringBuffer sb, HashMap parameterObject, String sql) throws Exception {

        //小写sql,用于匹配
        String lowerCaseSql = sql.toLowerCase();
        String strSql = sql.replace(FULL_FROM_QUERY,"");
        List<String> filedList =  selectItems(strSql);
        Statement statement = CCJSqlParserUtil.parse(new StringReader(strSql));
        if(ObjectUtil.isNotNull(statement)){

        }
        return sb;
    }

    /**
     * 重写分页查询列表的sql
     *
     * @param sb
     * @param parameterObject
     * @param sql
     * @return
     */
    private StringBuffer overrideListSql(StringBuffer sb, HashMap parameterObject, String sql) throws JSQLParserException {
        String lowerCaseSql = sql.toLowerCase();
        Statement statement = CCJSqlParserUtil.parse(new StringReader(sql));
        if (statement instanceof Select) {
            Select isTatement = (Select) statement;
            System.out.println();
        }

        return sb;
    }



    /**
     *
     *@Description: 查询sql字段
     *@Author: ywj
     *@Param: [sql]
     *@Return: java.util.List<java.lang.String>
     *@Date: 2020/12/25 15:03
     **/
    public static List<String> selectItems(String sql)
            throws JSQLParserException, NoSuchFieldException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        FromItem  fromItem = plain.getFromItem();
        Object object = fromItem.getClass().getDeclaredField("Select");
        Select select1 =  (Select) object;
        SelectBody plainSelect = select1.getSelectBody();
//        List<SelectItem> selectItems = plainSelect.getSelectItems();
//        List<String> strItems = new ArrayList<>();
//        if (selectItems != null) {
//            for (SelectItem item : selectItems) {
//                strItems.add(item.toString());
//            }
//        }
        return null;
    }







    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}