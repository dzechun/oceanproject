package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
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
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmTransferOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderMapper;
import com.fantechs.provider.om.service.OmOtherInOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    @Resource
    private OmTransferOrderMapper omTransferOrderMapper;
    @Resource
    private OmSalesReturnOrderDetMapper omSalesReturnOrderDetMapper;

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
    @LcnTransaction
    public int packageAutoOutOrder(OmOtherInOrder omOtherInOrder) {
        SysUser sysUser = currentUser();
        if(omOtherInOrder.getOrderStatus()>=3){
            throw new BizErrorException("订单已下发完成");
        }
        if(omOtherInOrder.getOmOtherInOrderDets().size()<1){
            throw new BizErrorException("请输入下发数量");
        }
        int num = 0;
        List<WmsInAsnOrderDet> wmsInAsnOrderDets = new ArrayList<>();
        int i = 0;

        for (OmOtherInOrderDet omOtherInOrderDet : omOtherInOrder.getOmOtherInOrderDets()) {
            i++;
            if(StringUtils.isEmpty(omOtherInOrderDet.getIssueQty())){
                omOtherInOrderDet.setIssueQty(BigDecimal.ZERO);
            }
            BigDecimal total = omOtherInOrderDet.getIssueQty().add(omOtherInOrderDet.getQty());
            if(total.compareTo(omOtherInOrderDet.getOrderQty())==1){
                throw new BizErrorException("下发数量不能大于工单数量");
            }

            //获取物料单位名称
            String unitName =omSalesReturnOrderDetMapper.findUnitName(omOtherInOrderDet.getMaterialId());

            //获取发货库位
            Map<String,Object> map = new HashMap<>();
            map.put("orgId",sysUser.getOrganizationId());
            map.put("warehouseId",omOtherInOrderDet.getWarehouseId());
            map.put("storageType",2);
            Long storageId = omTransferOrderMapper.findStorage(map);
            if(StringUtils.isEmpty(storageId)){
                throw new BizErrorException("未获取到该仓库的发货库位");
            }
            WmsInAsnOrderDet wmsInAsnOrderDet = WmsInAsnOrderDet.builder()
                    .sourceOrderId(omOtherInOrderDet.getOtherInOrderId())
                    .orderDetId(omOtherInOrderDet.getOtherInOrderDetId())
                    .warehouseId(omOtherInOrderDet.getWarehouseId())
                    .storageId(storageId)
                    .materialId(omOtherInOrderDet.getMaterialId())
                    .packingUnitName(unitName)
                    .batchCode(omOtherInOrderDet.getBatchCode())
                    .packingQty(omOtherInOrderDet.getQty())
                    .productionDate(omOtherInOrderDet.getProductionDate())
                    .expiredDate(omOtherInOrderDet.getExpiredDate())
                    .lineNumber(i)
                    .build();
            wmsInAsnOrderDets.add(wmsInAsnOrderDet);
        }
        WmsInAsnOrder wmsInAsnOrder = WmsInAsnOrder.builder()
                .sourceOrderId(omOtherInOrder.getOtherInOrderId())
                .materialOwnerId(omOtherInOrder.getMaterialOwnerId())
                //其他入库单
                .orderTypeId(Long.parseLong("6"))
                .relatedOrderCode1(omOtherInOrder.getOtherInOrderCode())
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
        if(total.compareTo(omOtherInOrder.getTotalQty())==0){
            omOtherInOrder.setOrderStatus((byte)3);
        }else{
            omOtherInOrder.setOrderStatus((byte)2);
        }
        num+=omOtherInOrderMapper.updateByPrimaryKeySelective(omOtherInOrder);
        return num;
    }

    /**
     * 收货数量反写
     * @param omOtherInOrderDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int writeQty(OmOtherInOrderDet omOtherInOrderDet){
        OmOtherInOrder omOtherInOrder = omOtherInOrderMapper.selectByPrimaryKey(omOtherInOrderDet.getOtherInOrderId());
        if(StringUtils.isEmpty(omOtherInOrder)){
            throw new BizErrorException("查询订单失败");
        }
        OmOtherInOrderDet omOtherInOrderDet1 = omOtherInOrderDetMapper.selectByPrimaryKey(omOtherInOrderDet.getOtherInOrderDetId());
        if(StringUtils.isEmpty(omOtherInOrderDet1)){
            throw new BizErrorException("未获取到订单信息");
        }
        if(StringUtils.isEmpty(omOtherInOrderDet1.getReceivingQty())){
            omOtherInOrderDet1.setReceivingQty(BigDecimal.ZERO);
        }
        omOtherInOrderDet.setReceivingQty(omOtherInOrderDet1.getReceivingQty().add(omOtherInOrderDet.getReceivingQty()));
        if(omOtherInOrderDet.getReceivingQty().compareTo(omOtherInOrderDet1.getIssueQty())==0){
            omOtherInOrder.setOrderStatus((byte)4);
        }
        int num = omOtherInOrderDetMapper.updateByPrimaryKeySelective(omOtherInOrderDet);
        num+=omOtherInOrderMapper.updateByPrimaryKeySelective(omOtherInOrder);
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
        if(record.getOmOtherInOrderDets().size()>0){
            num+=omOtherInOrderDetMapper.insertList(record.getOmOtherInOrderDets());
        }
        num+=this.addHt(record, record.getOmOtherInOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmOtherInOrder entity) {
        SysUser sysUser = currentUser();
        if(entity.getOrderStatus()>1){
            throw new BizErrorException("单据已被操作，无法修改");
        }
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
        int num = 0;
        if(StringUtils.isNotEmpty(entity.getOmOtherInOrderDets())){
            num=omOtherInOrderDetMapper.insertList(entity.getOmOtherInOrderDets());
        }
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
