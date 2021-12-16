package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtOtherOutOrderDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.om.OmHtOtherOutOrder;
import com.fantechs.common.base.general.entity.om.OmHtOtherOutOrderDet;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.om.mapper.OmOtherOutOrderDetMapper;
import com.fantechs.provider.om.mapper.OmOtherOutOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherOutOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherOutOrderMapper;
import com.fantechs.provider.om.service.OmOtherOutOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/06/23.
 */
@Service
public class OmOtherOutOrderServiceImpl extends BaseService<OmOtherOutOrder> implements OmOtherOutOrderService {

    @Resource
    private OmOtherOutOrderMapper omOtherOutOrderMapper;
    @Resource
    private OmOtherOutOrderDetMapper omOtherOutOrderDetMapper;
    @Resource
    private OmHtOtherOutOrderMapper omHtOtherOutOrderMapper;
    @Resource
    private OmHtOtherOutOrderDetMapper omHtOtherOutOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<OmOtherOutOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omOtherOutOrderMapper.findList(map);
    }

    @Override
    public List<OmHtOtherOutOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtOtherOutOrderMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pushDown(List<OmOtherOutOrderDetDto> omOtherOutOrderDets) {
        int i = 0;
        for (OmOtherOutOrderDetDto omOtherOutOrderDetDto : omOtherOutOrderDets){
            BigDecimal add = omOtherOutOrderDetDto.getTotalIssueQty().add(omOtherOutOrderDetDto.getIssueQty());
            if(add.compareTo(omOtherOutOrderDetDto.getOrderQty()) == 1){
                throw new BizErrorException("下发数量不能大于订单数量");
            }
        }

        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("OUT-OOO");
        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
        if(StringUtils.isEmpty(baseOrderFlow)){
            throw new BizErrorException("未找到当前单据配置的下游单据");
        }

        if("".equals(baseOrderFlow.getNextOrderTypeCode())){

        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmOtherOutOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setOtherOutOrderCode(CodeUtils.getId("OUT-OOO"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setOrderStatus((byte)1);
        int num = omOtherOutOrderMapper.insertUseGeneratedKeys(record);
        List<OmOtherOutOrderDetDto> omOtherOutOrderDets = record.getOmOtherOutOrderDets();
        if(StringUtils.isNotEmpty(omOtherOutOrderDets)){
            for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrderDets) {
                omOtherOutOrderDet.setOtherOutOrderId(record.getOtherOutOrderId());
                omOtherOutOrderDet.setCreateTime(new Date());
                omOtherOutOrderDet.setCreateUserId(sysUser.getUserId());
                omOtherOutOrderDet.setModifiedTime(new Date());
                omOtherOutOrderDet.setModifiedUserId(sysUser.getUserId());
                omOtherOutOrderDet.setOrgId(sysUser.getOrganizationId());
            }
            num+=omOtherOutOrderDetMapper.insertList(omOtherOutOrderDets);
        }

        this.addHt(record,omOtherOutOrderDets);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmOtherOutOrder entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        int num = omOtherOutOrderMapper.updateByPrimaryKeySelective(entity);
        //删除原有明细
        Example example = new Example(OmOtherOutOrderDet.class);
        example.createCriteria().andEqualTo("otherOutOrderId",entity.getOtherOutOrderId());
        omOtherOutOrderDetMapper.deleteByExample(example);

        List<OmOtherOutOrderDetDto> omOtherOutOrderDets = entity.getOmOtherOutOrderDets();
        if(StringUtils.isNotEmpty(omOtherOutOrderDets)) {
            for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrderDets) {
                omOtherOutOrderDet.setOtherOutOrderId(entity.getOtherOutOrderId());
                omOtherOutOrderDet.setCreateTime(new Date());
                omOtherOutOrderDet.setCreateUserId(sysUser.getUserId());
                omOtherOutOrderDet.setModifiedTime(new Date());
                omOtherOutOrderDet.setModifiedUserId(sysUser.getUserId());
                omOtherOutOrderDet.setOrgId(sysUser.getOrganizationId());
            }
            num += omOtherOutOrderDetMapper.insertList(omOtherOutOrderDets);
        }

        this.addHt(entity,omOtherOutOrderDets);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmOtherOutOrder omOtherOutOrder = omOtherOutOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omOtherOutOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }

            Example example = new Example(OmOtherOutOrderDet.class);
            example.createCriteria().andEqualTo("otherOutOrderId",omOtherOutOrder.getOtherOutOrderId());
            omOtherOutOrderDetMapper.deleteByExample(example);
            this.addHt(omOtherOutOrder,null);
        }
        return omOtherOutOrderMapper.deleteByIds(ids);
    }


    /**
     * 添加历史记录
     * @return
     */
    private int addHt(OmOtherOutOrder omOtherOutOrder,List<OmOtherOutOrderDetDto> omOtherOutOrderDets){
        int num = 0;
        if(StringUtils.isNotEmpty(omOtherOutOrder)){
            OmHtOtherOutOrder omHtOtherOutOrder = new OmHtOtherOutOrder();
            BeanUtil.copyProperties(omOtherOutOrder,omHtOtherOutOrder);
            num+=omHtOtherOutOrderMapper.insertSelective(omHtOtherOutOrder);
        }
        if(StringUtils.isNotEmpty(omOtherOutOrderDets)){
            for (OmOtherOutOrderDet omOtherOutOrderDet : omOtherOutOrderDets) {
                OmHtOtherOutOrderDet omHtOtherOutOrderDet = new OmHtOtherOutOrderDet();
                BeanUtil.copyProperties(omOtherOutOrderDet,omHtOtherOutOrderDet);
                num+=omHtOtherOutOrderDetMapper.insertSelective(omHtOtherOutOrderDet);
            }
        }
        return num;
    }
}
