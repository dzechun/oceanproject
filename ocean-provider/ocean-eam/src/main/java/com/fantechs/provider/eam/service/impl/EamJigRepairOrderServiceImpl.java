package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigRepairOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProjectItem;
import com.fantechs.common.base.general.entity.eam.EamJigRepairOrder;
import com.fantechs.common.base.general.entity.eam.EamJigRepairOrderReplacement;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRepairOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigRepairOrderMapper;
import com.fantechs.provider.eam.mapper.EamJigRepairOrderMapper;
import com.fantechs.provider.eam.mapper.EamJigRepairOrderReplacementMapper;
import com.fantechs.provider.eam.service.EamJigRepairOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */
@Service
public class EamJigRepairOrderServiceImpl extends BaseService<EamJigRepairOrder> implements EamJigRepairOrderService {

    @Resource
    private EamJigRepairOrderMapper eamJigRepairOrderMapper;
    @Resource
    private EamJigRepairOrderReplacementMapper eamJigRepairOrderReplacementMapper;
    @Resource
    private EamHtJigRepairOrderMapper eamHtJigRepairOrderMapper;

    @Override
    public List<EamJigRepairOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigRepairOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigRepairOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(record);

        record.setJigRepairOrderCode(CodeUtils.getId("WX-"));
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrderStatus((byte)1);
        eamJigRepairOrderMapper.insertUseGeneratedKeys(record);

        //治具维修单替换件
        List<EamJigRepairOrderReplacement> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigRepairOrderReplacement eamJigRepairOrderReplacement : list){
                eamJigRepairOrderReplacement.setJigRepairOrderId(record.getJigRepairOrderId());
                eamJigRepairOrderReplacement.setCreateUserId(user.getUserId());
                eamJigRepairOrderReplacement.setCreateTime(new Date());
                eamJigRepairOrderReplacement.setModifiedUserId(user.getUserId());
                eamJigRepairOrderReplacement.setModifiedTime(new Date());
                eamJigRepairOrderReplacement.setStatus(StringUtils.isEmpty(eamJigRepairOrderReplacement.getStatus())?1: eamJigRepairOrderReplacement.getStatus());
                eamJigRepairOrderReplacement.setOrgId(user.getOrganizationId());
            }
            eamJigRepairOrderReplacementMapper.insertList(list);
        }

        EamHtJigRepairOrder eamHtJigRepairOrder = new EamHtJigRepairOrder();
        BeanUtils.copyProperties(record,eamHtJigRepairOrder);
        int i = eamHtJigRepairOrderMapper.insert(eamHtJigRepairOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigRepairOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigRepairOrderMapper.updateByPrimaryKeySelective(entity);

        //删除原治具维修单替换件
        Example example1 = new Example(EamJigRepairOrderReplacement.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigRepairOrderId", entity.getJigRepairOrderId());
        eamJigRepairOrderReplacementMapper.deleteByExample(example1);

        //治具维修单替换件
        List<EamJigRepairOrderReplacement> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigRepairOrderReplacement eamJigRepairOrderReplacement : list){
                eamJigRepairOrderReplacement.setJigRepairOrderId(entity.getJigRepairOrderId());
                eamJigRepairOrderReplacement.setCreateUserId(user.getUserId());
                eamJigRepairOrderReplacement.setCreateTime(new Date());
                eamJigRepairOrderReplacement.setModifiedUserId(user.getUserId());
                eamJigRepairOrderReplacement.setModifiedTime(new Date());
                eamJigRepairOrderReplacement.setStatus(StringUtils.isEmpty(eamJigRepairOrderReplacement.getStatus())?1: eamJigRepairOrderReplacement.getStatus());
                eamJigRepairOrderReplacement.setOrgId(user.getOrganizationId());
            }
            eamJigRepairOrderReplacementMapper.insertList(list);
        }

        EamHtJigRepairOrder eamHtJigRepairOrder = new EamHtJigRepairOrder();
        BeanUtils.copyProperties(entity,eamHtJigRepairOrder);
        int i = eamHtJigRepairOrderMapper.insert(eamHtJigRepairOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigRepairOrder> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigRepairOrder eamJigRepairOrder = eamJigRepairOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigRepairOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigRepairOrder eamHtJigRepairOrder = new EamHtJigRepairOrder();
            BeanUtils.copyProperties(eamJigRepairOrder,eamHtJigRepairOrder);
            htList.add(eamHtJigRepairOrder);

            //删除治具维修单替换件
            Example example1 = new Example(EamJigRepairOrderReplacement.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigRepairOrderId", id);
            eamJigRepairOrderReplacementMapper.deleteByExample(example1);
        }

        eamHtJigRepairOrderMapper.insertList(htList);

        return eamJigRepairOrderMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(EamJigRepairOrder eamJigRepairOrder){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJigRepairOrder.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("jigRepairOrderCode",eamJigRepairOrder.getJigRepairOrderCode());
        if (StringUtils.isNotEmpty(eamJigRepairOrder.getJigRepairOrderId())){
            criteria.andNotEqualTo("jigRepairOrderId",eamJigRepairOrder.getJigRepairOrderId());
        }
        EamJigRepairOrder jigRepairOrder = eamJigRepairOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigRepairOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }
}
