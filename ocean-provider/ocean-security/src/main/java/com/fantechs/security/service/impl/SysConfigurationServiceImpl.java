package com.fantechs.security.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.security.SysConfigurationDetDto;
import com.fantechs.common.base.general.dto.security.SysConfigurationDto;
import com.fantechs.common.base.general.dto.security.SysFieldDto;
import com.fantechs.common.base.general.dto.security.SysTableDto;
import com.fantechs.common.base.general.entity.security.SysConfiguration;
import com.fantechs.common.base.general.entity.security.SysConfigurationDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysConfigurationDetMapper;
import com.fantechs.security.mapper.SysConfigurationMapper;
import com.fantechs.security.service.SysConfigurationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/12/09.
 */
@Service
public class SysConfigurationServiceImpl extends BaseService<SysConfiguration> implements SysConfigurationService {

    @Resource
    private SysConfigurationMapper sysConfigurationMapper;
    @Resource
    private SysConfigurationDetMapper sysConfigurationDetMapper;

    @Override
    public List<SysConfigurationDto> findList(Map<String, Object> map) {
        return sysConfigurationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SysConfiguration> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysConfiguration sysConfiguration) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SysConfiguration.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("configurationSource", sysConfiguration.getConfigurationSource());
        criteria.andEqualTo("configurationTarget", sysConfiguration.getConfigurationTarget());
        criteria.andEqualTo("orgId", user.getOrganizationId());
        List<SysConfiguration> sysConfigurationList=sysConfigurationMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(sysConfigurationList)&&sysConfigurationList.size()>0){
            throw new BizErrorException("已存在业务单据");
        }

        sysConfiguration.setOrgId(user.getOrganizationId());
        sysConfiguration.setCreateUserId(user.getUserId());
        sysConfiguration.setModifiedUserId(user.getUserId());

        int i= sysConfigurationMapper.insertUseGeneratedKeys(sysConfiguration);
        List<SysFieldDto> list=sysConfigurationMapper.findFieldList(sysConfiguration.getConfigurationTarget());
        List<SysConfigurationDet> dets = new LinkedList<>();
        for(SysFieldDto sysFieldDto :list){
            //初始化配置
            SysConfigurationDet sysConfigurationDet =  new SysConfigurationDet();
            sysConfigurationDet.setConfigurationId(sysConfiguration.getConfigurationId());
            sysConfigurationDet.setTargetField(sysFieldDto.getColumnName());
            sysConfigurationDet.setCreateUserId(user.getUserId());
            sysConfigurationDet.setModifiedUserId(user.getUserId());
            sysConfigurationDet.setModifiedTime(new Date());
            sysConfigurationDet.setCreateTime(new Date());
            sysConfigurationDet.setIsDelete((byte)1);
            sysConfigurationDet.setStatus((byte)1);
            dets.add(sysConfigurationDet);
        }
        if(StringUtils.isNotEmpty(dets)&&dets.size()>0){
            sysConfigurationDetMapper.insertList(dets);
        }
        return i;
    }

    @Override
    public List<SysTableDto> findTablesByName(String tableName) {
        return sysConfigurationMapper.findTbalesByName(tableName);
    }

    @Override
    public  List<SysFieldDto> findFieldList(String tableName) {
        return sysConfigurationMapper.findFieldList(tableName);
    }


    @Override
    public int batchDelete(String ids) {
        Example example = new Example(SysConfigurationDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("configurationId", StringToList(ids));
        sysConfigurationDetMapper.deleteByExample(example);
        return sysConfigurationMapper.deleteByIds(ids);
    }

    /**
     * String convert List.
     *
     * @param str example:"1,2,3,4"
     * @return List  List
     */
    public List StringToList(String str) {
        return Arrays.asList(str.split(","))
                .parallelStream()
                .map(a -> a.trim())
                .collect(Collectors.toList());
    }



    @Override
    public int push(Long id) {
        int i = 0;
        SysConfiguration sysConfiguration =  sysConfigurationMapper.selectByPrimaryKey(id);
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("configurationId",id);
        List<SysConfigurationDetDto> list = sysConfigurationDetMapper.findList(map);
        if(StringUtils.isNotEmpty(sysConfiguration)&&StringUtils.isNotEmpty(list)){
            StringBuffer sql  = new StringBuffer();
            sql.append("insert into ");
            sql.append(sysConfiguration.getConfigurationTarget());
            sql.append("(");
            for(SysConfigurationDet sysConfigurationDet :list){
                //此字段不开启下推
                if(sysConfigurationDet.getStatus()==(byte)0){
                    continue;
                }
                sql.append(sysConfigurationDet.getTargetField()).append(",");
            }
            sql=new StringBuffer(sql.substring(0,sql.length()-1));
            sql.append(") ");
            sql.append("select ");
            for(SysConfigurationDet sysConfigurationDet :list){
                //是否是转换固定值
                if(StringUtils.isNotEmpty(sysConfigurationDet.getFixedValue())){
                    sql.append(sysConfigurationDet.getFixedValue()).append(",");
                }else {
                    sql.append( sysConfigurationDet.getSourceField()).append(",");
                }
            }
            sql=new StringBuffer(sql.substring(0,sql.length()-1));
            sql.append(" from ");
            sql.append(sysConfiguration.getConfigurationSource());
            sql.append(" where 1=1 ");
            for(SysConfigurationDet sysConfigurationDet :list){
                //条件范围为空
                if(StringUtils.isEmpty(sysConfigurationDet.getIsCondition())){
                        continue;
                }

                //拼接范围 0-and 1-or
                if(sysConfigurationDet.getIsCondition().intValue()==1){
                    sql.append(" or ");
                }else{
                    sql.append(" and ");
                }
                sql.append(sysConfigurationDet.getSourceField());
                sql.append(getScopeType(sysConfigurationDet.getIsScope()+""));
                //处理特殊查询
                if(sysConfigurationDet.getIsScope().intValue()>7){
                    //in、not in
                    if(sysConfigurationDet.getIsScope().intValue()<10){
                        sql.append("(");
                    }else{
                        sql.append("'");
                    }
                    String[] filterValueArr = sysConfigurationDet.getFilterValue().split(",");
                    for(String filterValue : filterValueArr){
                        //模糊查询
                        if(sysConfigurationDet.getIsScope().intValue()==10){
                            sql.append(""+filterValue+"|");
                        }else{
                            sql.append("'"+filterValue+"',");
                        }

                    }
                    sql = new StringBuffer(sql.substring(0,sql.length()-1));
                    if(sysConfigurationDet.getIsScope().intValue()<10){
                        sql.append(")");
                    }else{
                        sql.append("'");
                    }
                }else{
                    if(sysConfigurationDet.getIsScope().intValue()!=6&&
                        sysConfigurationDet.getIsScope().intValue()!=7){
                        sql.append("'"+sysConfigurationDet.getFilterValue()+"'");
                    }
                }
            }
            //执行手动拼接完sql
           i= sysConfigurationMapper.insertSelect(sql.toString());
        }

        return i;
    }

    public static  String getScopeType(String isScope){
       String scopeType = null;
        //0-等于 1-不等于 2-大于  3-大于等于 4-小于 5-小于等于 6-为空  7-不为空 8-包含 9-不包含 10-模糊
        switch(isScope)
        {
            case "0" :
                scopeType=" = ";
                break;
            case "1" :
                scopeType=" != ";
                break;
            case "2" :
                scopeType=" < ";
                break;
            case "3" :
                scopeType=" <= ";
                break;
            case "4":
                scopeType=" > ";
                break;
            case "5" :
                scopeType=" >= ";
                break;
            case "6" :
                scopeType=" is null ";
                break;
            case "7" :
                scopeType=" is not null";
                break;
            case "8" :
                scopeType=" in ";
                break;
            case "9" :
                scopeType=" not in ";
                break;
            case "10":
                scopeType="  regexp ";
                break;
            default :
        }
        return scopeType;
    }
}
