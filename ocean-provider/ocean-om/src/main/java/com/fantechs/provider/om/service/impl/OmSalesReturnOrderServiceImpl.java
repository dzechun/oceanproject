package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrder;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrder;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderMapper;
import com.fantechs.provider.om.service.OmSalesReturnOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/06/21.
 */
@Service
public class OmSalesReturnOrderServiceImpl extends BaseService<OmSalesReturnOrder> implements OmSalesReturnOrderService {

    @Resource
    private OmSalesReturnOrderMapper omSalesReturnOrderMapper;
    @Resource
    private OmSalesReturnOrderDetMapper omSalesReturnOrderDetMapper;
    @Resource
    private OmHtSalesReturnOrderMapper omHtSalesReturnOrderMapper;
    @Resource
    private OmHtSalesReturnOrderDetMapper omHtSalesReturnOrderDetMapper;

    @Override
    public List<OmSalesReturnOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omSalesReturnOrderMapper.findList(map);
    }

    @Override
    public List<OmHtSalesReturnOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtSalesReturnOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmSalesReturnOrder record) {
        SysUser sysUser = currentUser();
        record.setSalesReturnOrderCode(CodeUtils.getId("XSTH-"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = omSalesReturnOrderMapper.insertUseGeneratedKeys(record);
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : record.getOmSalesReturnOrderDets()) {
            omSalesReturnOrderDet.setSalesReturnOrderId(record.getSalesReturnOrderId());
            omSalesReturnOrderDet.setCreateTime(new Date());
            omSalesReturnOrderDet.setCreateUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setModifiedTime(new Date());
            omSalesReturnOrderDet.setModifiedUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        num+=omSalesReturnOrderDetMapper.insertList(record.getOmSalesReturnOrderDets());
        num+=this.addHt(record, record.getOmSalesReturnOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmSalesReturnOrder entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        //删除原有明细
        Example example = new Example(OmSalesReturnOrderDet.class);
        example.createCriteria().andEqualTo("salesReturnOrderId",entity.getSalesReturnOrderId());
        omSalesReturnOrderDetMapper.deleteByExample(example);
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : entity.getOmSalesReturnOrderDets()) {
            omSalesReturnOrderDet.setSalesReturnOrderId(entity.getSalesReturnOrderId());
            omSalesReturnOrderDet.setCreateTime(new Date());
            omSalesReturnOrderDet.setCreateUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setModifiedTime(new Date());
            omSalesReturnOrderDet.setModifiedUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        int num=omSalesReturnOrderDetMapper.insertList(entity.getOmSalesReturnOrderDets());
        num+=omSalesReturnOrderMapper.updateByPrimaryKeySelective(entity);
        num+=this.addHt(entity, entity.getOmSalesReturnOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmSalesReturnOrder omSalesReturnOrder = omSalesReturnOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omSalesReturnOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
            Example example = new Example(OmSalesReturnOrderDet.class);
            example.createCriteria().andEqualTo("salesReturnOrderId",omSalesReturnOrder.getSalesReturnOrderId());
            omSalesReturnOrderDetMapper.deleteByExample(example);
        }
        return omSalesReturnOrderMapper.deleteByIds(ids);
    }

    /**
     * 添加历史记录
     * @param omSalesReturnOrder
     * @param omSalesReturnOrderDets
     * @return
     */
    private int addHt(OmSalesReturnOrder omSalesReturnOrder, List<OmSalesReturnOrderDet> omSalesReturnOrderDets){
        int num = 0;
        if(StringUtils.isNotEmpty(omSalesReturnOrder)){
            OmHtSalesReturnOrder omHtSalesReturnOrder = new OmHtSalesReturnOrder();
            BeanUtil.copyProperties(omSalesReturnOrder,omHtSalesReturnOrder);
            num+=omHtSalesReturnOrderMapper.insertSelective(omHtSalesReturnOrder);
        }
        if(StringUtils.isNotEmpty(omSalesReturnOrderDets)){
            for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
                OmHtSalesReturnOrderDet omHtSalesReturnOrderDet = new OmHtSalesReturnOrderDet();
                BeanUtil.copyProperties(omSalesReturnOrderDet,omHtSalesReturnOrderDet);
                num+=omHtSalesReturnOrderDetMapper.insertSelective(omHtSalesReturnOrderDet);
            }
        }
        return num;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }

}
