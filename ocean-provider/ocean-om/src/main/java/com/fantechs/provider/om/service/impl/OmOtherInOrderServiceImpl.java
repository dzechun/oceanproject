package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmOtherInOrderDto;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.om.mapper.OmOtherInOrderDetMapper;
import com.fantechs.provider.om.mapper.OmOtherInOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderMapper;
import com.fantechs.provider.om.service.OmOtherInOrderService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    @Resource
    private InFeignApi inFeignApi;

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
    public int packageAutoOutOrder(OmOtherInOrder omOtherInOrder) {
        SysUser sysUser = currentUser();
        if(omOtherInOrder.getOrderStatus()>=3){
            throw new BizErrorException("订单已下发完成");
        }
        int num = 0;
        List<WmsInAsnOrderDet> wmsInAsnOrderDets = new ArrayList<>();
        int i = 0;
        for (OmOtherInOrderDet omOtherInOrderDet : omOtherInOrder.getOmOtherInOrderDets()) {
            BigDecimal total = omOtherInOrderDet.getIssueQty().add(omOtherInOrderDet.getQty());
            if(total.compareTo(omOtherInOrderDet.getOrderQty())==1){
                throw new BizErrorException("下发数量不能大于工单数量");
            }
            WmsInAsnOrderDet wmsInAsnOrderDet = WmsInAsnOrderDet.builder()
                    .sourceOrderId(omOtherInOrderDet.getOtherInOrderId())
                    .asnOrderDetId(omOtherInOrderDet.getOtherInOrderDetId())
                    .warehouseId(omOtherInOrderDet.getWarehouseId())
                    .materialId(omOtherInOrderDet.getMaterialId())
                    .packingUnitName(omOtherInOrderDet.getUnitName())
                    .batchCode(omOtherInOrderDet.getBatchCode())
                    .packingQty(omOtherInOrderDet.getOrderQty())
                    .productionDate(omOtherInOrderDet.getProductionDate())
                    .expiredDate(omOtherInOrderDet.getExpiredDate())
                    .lineNumber(i++)
                    .build();
            wmsInAsnOrderDets.add(wmsInAsnOrderDet);
        }
        WmsInAsnOrder wmsInAsnOrder = WmsInAsnOrder.builder()
                .sourceOrderId(omOtherInOrder.getOtherInOrderId())
                .materialOwnerId(omOtherInOrder.getMaterialOwnerId())
                //销退入库单
                .orderTypeId(Long.parseLong("5"))
                .relatedOrderCode1(omOtherInOrder.getRelatedOrderCode())
                .orderDate(new Date())
                .wmsInAsnOrderDetList(wmsInAsnOrderDets)
                .build();
        ResponseEntity responseEntity = inFeignApi.add(wmsInAsnOrder);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getMessage());
        }
        num+=this.updateStatus(omOtherInOrder);
        this.addHt(omOtherInOrder, omOtherInOrder.getOmOtherInOrderDets());
        //更新订单状态
        return num;
    }

    private int updateStatus(OmOtherInOrder omOtherInOrder){
        int num = 0;
        for (OmOtherInOrderDet omOtherInOrderDet : omOtherInOrder.getOmOtherInOrderDets()) {
            OmOtherInOrderDet omOtherInOrderDet1 = omOtherInOrderDetMapper.selectByPrimaryKey(omOtherInOrderDet.getOtherInOrderDetId());
            if(StringUtils.isEmpty(omOtherInOrderDet1.getIssueQty())){
                omOtherInOrderDet1.setIssueQty(BigDecimal.ZERO);
                omOtherInOrderDet.setIssueQty(BigDecimal.ZERO);
            }
            omOtherInOrderDet.setIssueQty(omOtherInOrderDet.getQty().add(omOtherInOrderDet1.getIssueQty()));
            num+=omOtherInOrderDetMapper.updateByPrimaryKeySelective(omOtherInOrderDet);
        }
        BigDecimal total = omOtherInOrder.getOmOtherInOrderDets().stream()
                .map(OmOtherInOrderDet::getIssueQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        if(total.compareTo(omOtherInOrder.getTotalIssueQty())==0){
            omOtherInOrder.setOrderStatus((byte)3);
        }else{
            omOtherInOrder.setOrderStatus((byte)2);
        }
        num+=omOtherInOrderMapper.updateByPrimaryKeySelective(omOtherInOrder);
        return num;
    }

    /**
     * 收货数量反写
     * @param omOtherInOrder
     * @return
     */
    @Override
    public int writeQty(OmOtherInOrder omOtherInOrder){
        OmOtherInOrder om = omOtherInOrderMapper.selectByPrimaryKey(omOtherInOrder.getOtherInOrderId());
        int num = 0;
        for (OmOtherInOrderDet omOtherInOrderDet : omOtherInOrder.getOmOtherInOrderDets()) {
            OmOtherInOrderDet oms = omOtherInOrderDetMapper.selectByPrimaryKey(omOtherInOrderDet.getOtherInOrderDetId());
            omOtherInOrderDet.setReceivingQty(omOtherInOrderDet.getReceivingQty().add(oms.getReceivingQty()));
            num+=omOtherInOrderDetMapper.updateByPrimaryKeySelective(omOtherInOrderDet);
        }
        om.setTotalReceivingQty(omOtherInOrder.getTotalReceivingQty().add(om.getTotalReceivingQty()));
        num+=omOtherInOrderMapper.updateByPrimaryKeySelective(om);
        return num;
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
        record.setOrderStatus((byte)1);
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
