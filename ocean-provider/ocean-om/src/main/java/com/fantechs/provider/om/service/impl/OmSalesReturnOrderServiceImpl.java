package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderMapper;
import com.fantechs.provider.om.service.OmSalesReturnOrderService;
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
    @Resource
    private InFeignApi inFeignApi;

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

    /**
     * 下发
     * @param omSalesReturnOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int packageAutoOutOrder(OmSalesReturnOrder omSalesReturnOrder) {
        SysUser sysUser = currentUser();
        int num = 0;
            List<WmsInAsnOrderDet> wmsInAsnOrderDets = new ArrayList<>();
            int i = 0;
            for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrder.getOmSalesReturnOrderDets()) {
                if(StringUtils.isEmpty(omSalesReturnOrderDet.getIssueQty())){
                    omSalesReturnOrderDet.setIssueQty(BigDecimal.ZERO);
                }
                BigDecimal total = omSalesReturnOrderDet.getIssueQty().add(omSalesReturnOrderDet.getQty());
                if(total.compareTo(omSalesReturnOrderDet.getOrderQty())==1){
                    throw new BizErrorException("下发数量不能大于工单数量");
                }
                WmsInAsnOrderDet wmsInAsnOrderDet = WmsInAsnOrderDet.builder()
                        .sourceOrderId(omSalesReturnOrderDet.getSalesReturnOrderId())
                        .orderDetId(omSalesReturnOrderDet.getSalesReturnOrderDetId())
                        .warehouseId(omSalesReturnOrderDet.getWarehouseId())
                        .materialId(omSalesReturnOrderDet.getMaterialId())
                        .packingUnitName(omSalesReturnOrderDet.getUnitName())
                        .batchCode(omSalesReturnOrderDet.getBatchCode())
                        .packingQty(omSalesReturnOrderDet.getQty())
                        .productionDate(omSalesReturnOrderDet.getProductionDate())
                        .expiredDate(omSalesReturnOrderDet.getExpiredDate())
                        .lineNumber(i++)
                        .build();
                wmsInAsnOrderDets.add(wmsInAsnOrderDet);
            }
            WmsInAsnOrder wmsInAsnOrder = WmsInAsnOrder.builder()
                    .sourceOrderId(omSalesReturnOrder.getSalesReturnOrderId())
                    .materialOwnerId(omSalesReturnOrder.getMaterialOwnerId())
                    //销退入库单
                    .orderTypeId(Long.parseLong("5"))
                    .relatedOrderCode1(omSalesReturnOrder.getRelatedOrderCode())
                    .orderDate(new Date())
                    .wmsInAsnOrderDetList(wmsInAsnOrderDets)
                    .build();
            ResponseEntity responseEntity = inFeignApi.add(wmsInAsnOrder);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getMessage());
            }
            num+=this.updateStatus(omSalesReturnOrder);
            //更新订单状态
        return num;
    }

    /**
     * 数量累加更新状态
     * @param omSalesReturnOrder
     * @return
     */
    private int updateStatus(OmSalesReturnOrder omSalesReturnOrder){
        int num = 0;
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrder.getOmSalesReturnOrderDets()) {
            OmSalesReturnOrderDet omSalesReturnOrderDet1 = omSalesReturnOrderDetMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesReturnOrderDetId());
            if(StringUtils.isEmpty(omSalesReturnOrderDet1.getIssueQty()) || StringUtils.isEmpty(omSalesReturnOrderDet.getIssueQty())){
                omSalesReturnOrderDet1.setIssueQty(BigDecimal.ZERO);
                omSalesReturnOrderDet.setIssueQty(BigDecimal.ZERO);
            }
            omSalesReturnOrderDet.setIssueQty(omSalesReturnOrderDet.getQty().add(omSalesReturnOrderDet1.getIssueQty()));
            num+=omSalesReturnOrderDetMapper.updateByPrimaryKeySelective(omSalesReturnOrderDet);
        }
        //统计总订单数量
        Example example = new Example(OmSalesReturnOrderDet.class);
        example.createCriteria().andEqualTo("salesReturnOrderId",omSalesReturnOrder.getSalesReturnOrderId());
        List<OmSalesReturnOrderDet> list = omSalesReturnOrderDetMapper.selectByExample(example);
        BigDecimal totalQty = list.stream()
                .map(OmSalesReturnOrderDet::getOrderQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal totalIssuseQty  = list.stream()
                .map(OmSalesReturnOrderDet::getIssueQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        if(totalQty.compareTo(totalIssuseQty)==0){
            omSalesReturnOrder.setOrderStatus((byte)3);
        }else{
            omSalesReturnOrder.setOrderStatus((byte)2);
        }
        num+=omSalesReturnOrderMapper.updateByPrimaryKeySelective(omSalesReturnOrder);
        return num;
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
        record.setOrderStatus((byte)1);
        record.setOrgId(sysUser.getOrganizationId());
        //计算重量
        record = this.calculateWeight(record);
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

    /**
     * 计算重量
     * @param omSalesReturnOrder
     * @return
     */
    private OmSalesReturnOrder calculateWeight(OmSalesReturnOrder omSalesReturnOrder){
        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalNetWeight = BigDecimal.ZERO;
        BigDecimal totalGrossWeight = BigDecimal.ZERO;
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrder.getOmSalesReturnOrderDets()) {
            if(StringUtils.isEmpty(omSalesReturnOrderDet.getMaterialId())){
                throw new BizErrorException("物料错误");
            }
            OmSalesReturnOrder om = omSalesReturnOrderMapper.findMaterial(omSalesReturnOrderDet.getMaterialId());
            totalVolume.add(om.getTotalVolume());
            totalNetWeight.add(om.getTotalNetWeight());
            totalGrossWeight.add(om.getTotalGrossWeight());
        }
       omSalesReturnOrder.setTotalVolume(totalVolume);
        omSalesReturnOrder.setTotalNetWeight(totalNetWeight);
        omSalesReturnOrder.setTotalGrossWeight(totalGrossWeight);
        return omSalesReturnOrder;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmSalesReturnOrder entity) {
        SysUser sysUser = currentUser();
        if(entity.getOrderStatus()>1){
            throw new BizErrorException("单据已被操作，无法修改");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        entity = this.calculateWeight(entity);
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

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int writeQty(OmSalesReturnOrderDet omSalesReturnOrderDet){
        OmSalesReturnOrder omSalesReturnOrder = omSalesReturnOrderMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesOrderId());
        if(StringUtils.isEmpty(omSalesReturnOrder)){
            throw new BizErrorException("查询订单失败");
        }
        OmSalesReturnOrderDet omSalesReturnOrderDet1 = omSalesReturnOrderDetMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesReturnOrderDetId());
        if(StringUtils.isEmpty(omSalesReturnOrderDet1)){
            throw new BizErrorException("未获取到订单信息");
        }
        if(StringUtils.isEmpty(omSalesReturnOrderDet1.getReceivingQty())){
            omSalesReturnOrderDet1.setReceivingQty(BigDecimal.ZERO);
        }
        omSalesReturnOrderDet.setReceivingQty(omSalesReturnOrderDet1.getReceivingQty().add(omSalesReturnOrderDet.getReceivingQty()));
        if(omSalesReturnOrderDet.getReceivingQty().compareTo(omSalesReturnOrderDet1.getIssueQty())==0){
            omSalesReturnOrder.setOrderStatus((byte)4);
        }
        int num = omSalesReturnOrderDetMapper.updateByPrimaryKeySelective(omSalesReturnOrderDet);
        num+=omSalesReturnOrderMapper.updateByPrimaryKeySelective(omSalesReturnOrder);
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
