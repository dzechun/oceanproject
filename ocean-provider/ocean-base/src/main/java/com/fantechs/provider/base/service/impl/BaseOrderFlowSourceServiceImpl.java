package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseOrderFlowSourceImport;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlowSource;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlowSourceDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseOrderFlowSourceDetMapper;
import com.fantechs.provider.base.mapper.BaseOrderFlowSourceMapper;
import com.fantechs.provider.base.service.BaseOrderFlowSourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2022/02/15.
 */
@Service
public class BaseOrderFlowSourceServiceImpl extends BaseService<BaseOrderFlowSource> implements BaseOrderFlowSourceService {

    @Resource
    private BaseOrderFlowSourceMapper baseOrderFlowSourceMapper;
    @Resource
    private BaseOrderFlowSourceDetMapper baseOrderFlowSourceDetMapper;

    @Override
    public List<BaseOrderFlowSource> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseOrderFlowSourceMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseOrderFlowSource record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseOrderFlowSource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("orderTypeCode", record.getOrderTypeCode());//单据节点编码
        List<BaseOrderFlowSource> baseOrderFlowSources = baseOrderFlowSourceMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseOrderFlowSources)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"单据节点的单据流维度已存在");
        }

        record.setCreateUserId(currentUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUser.getUserId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(currentUser.getOrganizationId());
        int i = baseOrderFlowSourceMapper.insertUseGeneratedKeys(record);

        List<BaseOrderFlowSourceDet> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (BaseOrderFlowSourceDet baseOrderFlowSourceDet : list){
                baseOrderFlowSourceDet.setOrderFlowSourceId(record.getOrderFlowSourceId());
                baseOrderFlowSourceDet.setCreateUserId(currentUser.getUserId());
                baseOrderFlowSourceDet.setCreateTime(new Date());
                baseOrderFlowSourceDet.setModifiedTime(new Date());
                baseOrderFlowSourceDet.setModifiedUserId(currentUser.getUserId());
                baseOrderFlowSourceDet.setStatus((byte)1);
                baseOrderFlowSourceDet.setOrgId(currentUser.getOrganizationId());
            }
            baseOrderFlowSourceDetMapper.insertList(list);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseOrderFlowSource entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseOrderFlowSource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("orderTypeCode", entity.getOrderTypeCode());//单据节点编码
        criteria.andNotEqualTo("orderFlowSourceId", entity.getOrderFlowSourceId());//单据节点编码
        List<BaseOrderFlowSource> baseOrderFlowSources = baseOrderFlowSourceMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseOrderFlowSources)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"单据节点的单据流维度已存在");
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUser.getUserId());
        int i = baseOrderFlowSourceMapper.updateByPrimaryKeySelective(entity);

        Example example1 = new Example(BaseOrderFlowSourceDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("orderFlowSourceId",entity.getOrderFlowSourceId());
        baseOrderFlowSourceDetMapper.deleteByExample(example1);

        List<BaseOrderFlowSourceDet> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (BaseOrderFlowSourceDet baseOrderFlowSourceDet : list){
                baseOrderFlowSourceDet.setOrderFlowSourceId(entity.getOrderFlowSourceId());
                baseOrderFlowSourceDet.setCreateUserId(currentUser.getUserId());
                baseOrderFlowSourceDet.setCreateTime(new Date());
                baseOrderFlowSourceDet.setModifiedTime(new Date());
                baseOrderFlowSourceDet.setModifiedUserId(currentUser.getUserId());
                baseOrderFlowSourceDet.setStatus((byte)1);
                baseOrderFlowSourceDet.setOrgId(currentUser.getOrganizationId());
            }
            baseOrderFlowSourceDetMapper.insertList(list);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        String[] split = ids.split(",");
        for (String s : split){
            Example example = new Example(BaseOrderFlowSourceDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orderFlowSourceId",s);
            baseOrderFlowSourceDetMapper.deleteByExample(example);
        }
        return baseOrderFlowSourceMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseOrderFlowSourceImport> baseOrderFlowSourceImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseOrderFlowSourceImport> orderFlowSourceImports = new LinkedList<>();

        for (int i = 0; i < baseOrderFlowSourceImports.size(); i++) {
            BaseOrderFlowSourceImport baseOrderFlowSourceImport = baseOrderFlowSourceImports.get(i);
            Integer operationModule = baseOrderFlowSourceImport.getOperationModule();
            String orderTypeCode = baseOrderFlowSourceImport.getOrderTypeCode();
            String nextOrderTypeCode = baseOrderFlowSourceImport.getNextOrderTypeCode();

            if (StringUtils.isEmpty(operationModule,orderTypeCode,nextOrderTypeCode)){
                fail.add(i + 4);
                continue;
            }

            Example example = new Example(BaseOrderFlowSource.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("orderTypeCode", orderTypeCode);//单据节点编码
            List<BaseOrderFlowSource> baseOrderFlowSources = baseOrderFlowSourceMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseOrderFlowSources)) {
                fail.add(i + 4);
                continue;
            }

            orderFlowSourceImports.add(baseOrderFlowSourceImport);
        }

        //对合格数据进行分组
        HashMap<String, List<BaseOrderFlowSourceImport>> map = orderFlowSourceImports.stream().collect(Collectors.groupingBy(BaseOrderFlowSourceImport::getOrderTypeCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<BaseOrderFlowSourceImport> baseOrderFlowSourceImports1 = map.get(code);

            //若单据节点已存在则只增加子表，不存在则新增父表和子表
            Example example5 = new Example(BaseOrderFlowSource.class);
            Example.Criteria criteria5 = example5.createCriteria();
            criteria5.andEqualTo("orderTypeCode",baseOrderFlowSourceImports1.get(0).getOrderTypeCode());
            BaseOrderFlowSource baseOrderFlowSource = baseOrderFlowSourceMapper.selectOneByExample(example5);

            if(StringUtils.isEmpty(baseOrderFlowSource)){
                //新增父表
                baseOrderFlowSource = new BaseOrderFlowSource();
                BeanUtils.copyProperties(baseOrderFlowSourceImports1.get(0),baseOrderFlowSource);
                baseOrderFlowSource.setCreateTime(new Date());
                baseOrderFlowSource.setCreateUserId(currentUser.getUserId());
                baseOrderFlowSource.setModifiedTime(new Date());
                baseOrderFlowSource.setModifiedUserId(currentUser.getUserId());
                baseOrderFlowSource.setStatus((byte) 1);
                baseOrderFlowSource.setOrgId(currentUser.getOrganizationId());
                baseOrderFlowSourceMapper.insertUseGeneratedKeys(baseOrderFlowSource);
            }

            //新增明细
            List<BaseOrderFlowSourceDet> dets = new LinkedList<>();
            for (BaseOrderFlowSourceImport baseOrderFlowSourceImport : baseOrderFlowSourceImports1) {
                BaseOrderFlowSourceDet baseOrderFlowSourceDet = new BaseOrderFlowSourceDet();
                BeanUtils.copyProperties(baseOrderFlowSourceImport,baseOrderFlowSourceDet);
                baseOrderFlowSourceDet.setOrderFlowSourceId(baseOrderFlowSource.getOrderFlowSourceId());
                dets.add(baseOrderFlowSourceDet);
            }
            success += baseOrderFlowSourceDetMapper.insertList(dets);
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }
}
