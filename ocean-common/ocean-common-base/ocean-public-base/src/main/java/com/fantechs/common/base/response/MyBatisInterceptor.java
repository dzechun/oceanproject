package com.fantechs.common.base.response;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fantechs.common.base.dto.Query;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.*;
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
import java.util.*;

/**
 * mybatis 拦截器(重写Sql)
 *
 * @author keguang
 * @date 2022/05/20
 */
@Intercepts(
        {@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Slf4j
public class MyBatisInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(MyBatisInterceptor.class);

    private static final String PARAM_NAME = "query";
    private static final String BOUND_SQL_NAME = "sql";
    private static final String FULL_FROM_QUERY = "#FULL_FROM_QUERY";
    private static final String PAGE_STR = "select count(0) from (";
    private static final String CONDITION_SQL = " 1 = 1 ";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //重写全表单查询Sql
        RoutingStatementHandler targetObject = (RoutingStatementHandler) invocation.getTarget();
        if (ObjectUtil.isNotNull(targetObject)) {
            ParameterHandler parameterHandler = targetObject.getParameterHandler();
            if (ObjectUtil.isNotNull(parameterHandler)) {
                HashMap<String, Object> hashMapParam = convertHashMap(parameterHandler);
                if (!CollectionUtils.isEmpty(hashMapParam) && !StringUtils.isEmpty(hashMapParam.get(PARAM_NAME))) {
                    BoundSql boundSql = targetObject.getBoundSql();
                    String sql = boundSql.getSql().trim();
                    if (sql.contains(FULL_FROM_QUERY)) {
                        String updateSql = overrideSql(hashMapParam, boundSql.getSql().trim());
                        Field field = boundSql.getClass().getDeclaredField(BOUND_SQL_NAME);
                        field.setAccessible(true);
                        field.set(boundSql, updateSql);
                    }
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
    private String overrideSql(HashMap<String, Object> parameterObject, String sql) {
        try {
            logger.info("进入重写全表单查询parameterObject：{}", parameterObject);
            if (sql.toLowerCase().contains(PAGE_STR)) {
                //分页查询统计总数的Sql
                logger.info("进入重写分页统计总数的sql：{}", sql);
                return sql.replace(FULL_FROM_QUERY, overrideSqlCondition(parameterObject, sql, 1));
            } else {
                //查询具体字段的Sql
                logger.info("进入重写分页查询列表的sql：{}", sql);
                return sql.replace(FULL_FROM_QUERY, overrideSqlCondition(parameterObject, sql, 2));
            }
        } catch (Exception e) {
            logger.error("重写Sql语句异常，取消重写：{}", e.getMessage());
            return sql.replace(FULL_FROM_QUERY, CONDITION_SQL);
        }
    }

    /**
     * 重写全表单
     * <p>
     * <p>
     * Sql语句的条件
     *
     * @param parameterObject
     * @param sql
     * @return
     */
    private String overrideSqlCondition(HashMap<String, Object> parameterObject, String sql, Integer type) throws JSQLParserException {
        // 小写sql,用于匹配
        StringBuilder sb = new StringBuilder();
        sb.append(CONDITION_SQL);
        String strSql = sql.replace(FULL_FROM_QUERY, CONDITION_SQL);
        List<String> filedList;
        if (type == 1) {
            // 获取分页统计总数Sql语句的字段名称列表
            filedList = selectItemsTotal(strSql);
        } else {
            // 获取分页查询列表的Sql语句的字段名称列表
            filedList = selectItemsList(strSql);
        }
        if (ObjectUtil.isNotNull(filedList)) {
            //匹对前端传进来的条件
            Object strParam = parameterObject.get(PARAM_NAME);
            if (ObjectUtil.isNotNull(strParam)) {
                HashMap<String, Query> queryMap = (HashMap<String, Query>) strParam;
                for (Map.Entry<String, Query> entry : queryMap.entrySet()) {
                    if (ObjectUtil.isNotNull(entry.getValue())) {
                        for (String filed : filedList) {
                            // 将字段去表名、转驼峰
                            String filedName = StrUtil.toCamelCase(filed);
                            String point = ".";
                            if (filedName.contains(point)) {
                                filedName = filedName.replace(filedName.substring(0, filedName.indexOf(point) + 1), "");
                            }

                            //判断条件，进行匹对
                            if (filedName.contains(entry.getKey())) {
                                Query query = entry.getValue();
                                sb.append(" ").append(query.getJoinQuery()).append(" ");
                                if (filedName.toLowerCase().contains("as")) {
                                    sb.append(" ").append(filed.substring(0, filed.indexOf(" "))).append(" ");
                                } else {
                                    sb.append(" ").append(filed).append(" ");
                                }
                                sb.append(" ").append(query.getSql()).append(" ");
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 查询sql字段名称
     *
     * @param sql sql语句
     * @return
     * @throws JSQLParserException
     */
    public static List<String> selectItemsTotal(String sql)
            throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        FromItem fromItem = plain.getFromItem();

        String tableName = fromItem.getAlias().getName();
        String strSql = sql.replace(PAGE_STR, "").replace(") " + tableName, "");
        CCJSqlParserManager parserManager1 = new CCJSqlParserManager();
        Select select1 = (Select) parserManager1.parse(new StringReader(strSql));
        PlainSelect plain1 = (PlainSelect) select1.getSelectBody();
        List<SelectItem> selectItems = plain1.getSelectItems();
        List<String> strItems = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                strItems.add(item.toString());
            }
        }
        return strItems;
    }

    /**
     * @Description: 查询sql字段
     * @Author: ywj
     * @Param: [sql]
     * @Return: java.util.List<java.lang.String>
     * @Date: 2020/12/25 15:03
     **/
    public static List<String> selectItemsList(String sql)
            throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<SelectItem> selectItems = plain.getSelectItems();
        List<String> strItems = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                strItems.add(item.toString());
            }
        }
        return strItems;
    }

    /**
     * 入参转换到HashMap
     * @param parameterHandler
     * @return
     */
    public static HashMap<String, Object> convertHashMap(ParameterHandler parameterHandler) throws IllegalAccessException  {
        try {
            return (HashMap) parameterHandler.getParameterObject();
        } catch (Exception e) {
            try {
                HashMap<String, Object> hashMap = new HashMap<>();
                Field[] fields = parameterHandler.getParameterObject().getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if(PARAM_NAME.equals(field.getName())){
                        hashMap.put(field.getName(), field.get(parameterHandler.getParameterObject()));
                    }
                }
                return hashMap;
            }catch (Exception ex){
                logger.error("全表单查询入参转换异常，此报错可以忽略！");
            }
        }
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