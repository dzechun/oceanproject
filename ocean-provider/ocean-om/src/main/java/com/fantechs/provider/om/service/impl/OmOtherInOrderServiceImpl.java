package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmOtherInOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtOtherInOrder;
import com.fantechs.common.base.general.entity.om.OmHtOtherInOrderDet;
import com.fantechs.common.base.general.entity.om.OmOtherInOrder;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmOtherInOrderDetMapper;
import com.fantechs.provider.om.mapper.OmOtherInOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderMapper;
import com.fantechs.provider.om.service.OmOtherInOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/21.
 */
@Service
public class OmOtherInOrderServiceImpl extends BaseService<OmOtherInOrder> implements OmOtherInOrderService {

    @Resource
    private OmOtherInOrderMapper omOtherInOrderMapper;
    @Resource
    private OmOtherInOrderDetMapper omOtherInOrderDetMapper;
    @Resource
    private OmHtOtherInOrderMapper omHtOtherInOrderMapper;
    @Resource
    private OmHtOtherInOrderDetMapper omHtOtherInOrderDetMapper;

    @Override
    public List<OmOtherInOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omOtherInOrderMapper.findList(map);
    }

    @Override
    public List<OmOtherInOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtOtherInOrderMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmOtherInOrder record) {
        SysUser sysUser = currentUser();
        record.setOtherInOrderCode(CodeUtils.getId("OTIN-"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = omOtherInOrderMapper.insertUseGeneratedKeys(record);
        for (OmOtherInOrderDet omOtherInOrderDet : record.getOmOtherInOrderDets()) {
            omOtherInOrderDet.setOtherInOrderId(record.getOtherInOrderId());
            omOtherInOrderDet.setCreateTime(new Date());
            omOtherInOrderDet.setCreateUserId(sysUser.getUserId());
            omOtherInOrderDet.setModifiedTime(new Date());
            omOtherInOrderDet.setModifiedUserId(sysUser.getUserId());
            omOtherInOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        num+=omOtherInOrderDetMapper.insertList(record.getOmOtherInOrderDets());
        num+=this.addHt(record, record.getOmOtherInOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmOtherInOrder entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setOrgId(sysUser.getOrganizationId());
        //删除原有明细
        Example example = new Example(OmOtherInOrderDet.class);
        example.createCriteria().andEqualTo("otherInOrderId",entity.getOtherInOrderId());
        omOtherInOrderDetMapper.deleteByExample(example);
        for (OmOtherInOrderDet omOtherInOrderDet : entity.getOmOtherInOrderDets()) {
            omOtherInOrderDet.setOtherInOrderId(entity.getOtherInOrderId());
            omOtherInOrderDet.setCreateTime(new Date());
            omOtherInOrderDet.setCreateUserId(sysUser.getUserId());
            omOtherInOrderDet.setModifiedTime(new Date());
            omOtherInOrderDet.setModifiedUserId(sysUser.getUserId());
            omOtherInOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        int num=omOtherInOrderDetMapper.insertList(entity.getOmOtherInOrderDets());
        num+=omOtherInOrderMapper.updateByPrimaryKeySelective(entity);
        num+=this.addHt(entity, entity.getOmOtherInOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrIds = ids.split(",");
        for (String id : arrIds) {
            OmOtherInOrder omOtherInOrder = omOtherInOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omOtherInOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
            //删除明细
            Example example = new Example(OmOtherInOrderDet.class);
            example.createCriteria().andEqualTo("otherInOrderId",omOtherInOrder.getOtherInOrderId());
            omOtherInOrderDetMapper.deleteByExample(example);
        }
        return omOtherInOrderMapper.deleteByIds(ids);
    }

    /**
     * 添加历史记录
     * @return
     */
    private int addHt(OmOtherInOrder omOtherInOrder,List<OmOtherInOrderDet> omOtherInOrderDets){
        int num = 0;
        if(StringUtils.isNotEmpty(omOtherInOrder)){
            OmHtOtherInOrder omHtOtherInOrder = new OmHtOtherInOrder();
            BeanUtil.copyProperties(omOtherInOrder,omHtOtherInOrder);
            num+=omHtOtherInOrderMapper.insertSelective(omHtOtherInOrder);
        }
        if(StringUtils.isNotEmpty(omOtherInOrderDets)){
            for (OmOtherInOrderDet omOtherInOrderDet : omOtherInOrderDets) {
                OmHtOtherInOrderDet omHtOtherInOrderDet = new OmHtOtherInOrderDet();
                BeanUtil.copyProperties(omOtherInOrderDet,omHtOtherInOrderDet);
                num+=omHtOtherInOrderDetMapper.insertSelective(omHtOtherInOrderDet);
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
