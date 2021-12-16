package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseOrderFlowMapper;
import com.fantechs.provider.base.service.BaseOrderFlowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/07.
 */
@Service
public class BaseOrderFlowServiceImpl extends BaseService<BaseOrderFlow> implements BaseOrderFlowService {

    @Resource
    private BaseOrderFlowMapper baseOrderFlowMapper;

    @Override
    public List<BaseOrderFlowDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseOrderFlowMapper.findList(map);
    }

    @Override
    public BaseOrderFlow findOrderFlow(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orderTypeCode"))){
            throw new BizErrorException("单据类型编码不能为空");
        }
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseOrderFlowMapper.findOrderFlow(map);
    }

    @Override
    public int save(BaseOrderFlow record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseOrderFlow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", record.getBusinessType());//业务类型
        criteria.andEqualTo("orderTypeCode", record.getOrderTypeCode());//单据节点编码
        criteria.andEqualTo("orderFlowDimension", record.getOrderFlowDimension());//单据流维度
        if(record.getOrderFlowDimension()==(byte)2)
            criteria.andEqualTo("supplierId", record.getSupplierId());
        else if(record.getOrderFlowDimension()==(byte)3)
            criteria.andEqualTo("materialId", record.getMaterialId());

        List<BaseOrderFlow> baseOrderFlows = baseOrderFlowMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseOrderFlows)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"单据节点和单据流维度已存在");
        }

        record.setCreateUserId(currentUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUser.getUserId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(currentUser.getOrganizationId());
        return baseOrderFlowMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseOrderFlow entity) {
        int i=0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(currentUser.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(currentUser.getOrganizationId());
        i=baseOrderFlowMapper.updateByPrimaryKeySelective(entity);

        Example example = new Example(BaseOrderFlow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", entity.getBusinessType());//业务类型
        criteria.andEqualTo("orderNode", entity.getOrderNode());//单据节点
        criteria.andEqualTo("orderFlowDimension", entity.getOrderFlowDimension());//单据流维度
        if(entity.getOrderFlowDimension()==(byte)2)
            criteria.andEqualTo("supplierId", entity.getSupplierId());
        else if(entity.getOrderFlowDimension()==(byte)3)
            criteria.andEqualTo("materialId", entity.getMaterialId());
        List<BaseOrderFlow> baseOrderFlows = baseOrderFlowMapper.selectByExample(example);
        if (baseOrderFlows.size()>1) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"单据节点和单据流维度已存在");
        }

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseOrderFlow baseOrderFlow = baseOrderFlowMapper.selectByPrimaryKey(Long.valueOf(id));
            if (StringUtils.isEmpty(baseOrderFlow)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return baseOrderFlowMapper.deleteByIds(ids);
    }
}
