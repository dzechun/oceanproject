package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtTransferOrderDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.om.*;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmTransferOrderDetMapper;
import com.fantechs.provider.om.mapper.OmTransferOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherOutOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtTransferOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtTransferOrderMapper;
import com.fantechs.provider.om.service.OmTransferOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by Mr.Lei on 2021/06/15.
 */
@Service
public class OmTransferOrderServiceImpl extends BaseService<OmTransferOrder> implements OmTransferOrderService {

    @Resource
    private OmTransferOrderMapper omTransferOrderMapper;
    @Resource
    private OmTransferOrderDetMapper omTransferOrderDetMapper;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private OmSalesReturnOrderDetMapper omSalesReturnOrderDetMapper;
    @Resource
    private OmHtTransferOrderMapper omHtTransferOrderMapper;
    @Resource
    private OmHtTransferOrderDetMapper omHtTransferOrderDetMapper;

    @Override
    public List<OmTransferOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        List<OmTransferOrderDto> list = omTransferOrderMapper.findList(map);
        for (OmTransferOrderDto omTransferOrderDto : list) {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("transferOrderId",omTransferOrderDto.getTransferOrderId());
            List<OmTransferOrderDetDto> omTransferOrderDetDtos = omTransferOrderDetMapper.findList(map1);
            BigDecimal countQty = BigDecimal.ZERO;
            BigDecimal countVolume = BigDecimal.ZERO;
            BigDecimal countNetWeight = BigDecimal.ZERO;
            BigDecimal countGrossWeight = BigDecimal.ZERO;

            for (OmTransferOrderDetDto omTransferOrderDetDto : omTransferOrderDetDtos) {
                countQty.add(omTransferOrderDetDto.getOrderQty());
                countVolume.add(omTransferOrderDetDto.getVolume());
                countNetWeight.add(omTransferOrderDetDto.getNetWeight());
                countGrossWeight.add(omTransferOrderDetDto.getGrossWeight());
            }
//            BigDecimal countQty = omTransferOrderDetDtos.stream()
//                    .map(OmTransferOrderDet::getOrderQty)
//                    .reduce(BigDecimal.ZERO,BigDecimal::add);
//            BigDecimal countVolume = omTransferOrderDetDtos.stream()
//                    .map(OmTransferOrderDetDto::getVolume)
//                    .reduce(BigDecimal.ZERO,BigDecimal::add);
//            BigDecimal countNetWeight = omTransferOrderDetDtos.stream()
//                    .map(OmTransferOrderDetDto::getNetWeight)
//                    .reduce(BigDecimal.ZERO,BigDecimal::add);
//            BigDecimal countGrossWeight = omTransferOrderDetDtos.stream()
//                    .map(OmTransferOrderDetDto::getGrossWeight)
//                    .reduce(BigDecimal.ZERO,BigDecimal::add);
            omTransferOrderDto.setCountOrderQty(countQty);
            omTransferOrderDto.setCountVolume(countVolume);
            omTransferOrderDto.setCountNetWeight(countNetWeight);
            omTransferOrderDto.setCountGrossWeight(countGrossWeight);
        }
        return list;
    }

    /**
     * 下发生成调拨出库单
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int packageAutoOutOrder(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            OmTransferOrder omTransferOrder = omTransferOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omTransferOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(omTransferOrder.getOrderStatus()>1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),omTransferOrder.getOrderStatus()==2?"订单已下发，无法修改":"订单已完成");
            }
            Example example = new Example(OmTransferOrderDet.class);
            example.createCriteria().andEqualTo("transferOrderId",omTransferOrder.getTransferOrderId());
            List<OmTransferOrderDet> list = omTransferOrderDetMapper.selectByExample(example);
            if(list.size()<1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"下发失败");
            }
            //获取仓库联系人
            BaseWarehouse baseWarehouse = baseFeignApi.getWarehouseDetail(omTransferOrder.getOutWarehouseId()).getData();
            //获取发货库位
            Map<String,Object> map = new HashMap<>();
            map.put("orgId",sysUser.getOrganizationId());
            map.put("warehouseId",omTransferOrder.getOutWarehouseId());
            map.put("storageType",3);
            Long storageId = omTransferOrderMapper.findStorage(map);
            if(StringUtils.isEmpty(storageId)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未获取到该仓库的发货库位");
            }
            //出库单表头
            WmsOutDeliveryOrder wmsOutDeliveryOrder = new WmsOutDeliveryOrder();
            wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("DBCK-"));
            wmsOutDeliveryOrder.setMaterialOwnerId(omTransferOrder.getMaterialOwnerId());
            wmsOutDeliveryOrder.setSourceOrderId(omTransferOrder.getTransferOrderId());
            wmsOutDeliveryOrder.setRelatedOrderCode1(omTransferOrder.getTransferOrderCode());
            wmsOutDeliveryOrder.setWarehouseId(omTransferOrder.getOutWarehouseId());
            wmsOutDeliveryOrder.setStorageId(storageId);
            wmsOutDeliveryOrder.setLinkManName(baseWarehouse.getLinkManName());
            wmsOutDeliveryOrder.setLinkManPhone(baseWarehouse.getLinkManPhone());
            wmsOutDeliveryOrder.setOrderTypeId((long)2);
            wmsOutDeliveryOrder.setOrderStatus((byte)1);
            wmsOutDeliveryOrder.setOrderDate(new Date());
            wmsOutDeliveryOrder.setOrgId(sysUser.getOrganizationId());
            List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetDtos = new ArrayList<>();
            int i = 1;
            for (OmTransferOrderDet omTransferOrderDet : list) {

                //获取物料单位名称
                String unitName =omSalesReturnOrderDetMapper.findUnitName(omTransferOrderDet.getMaterialId());

                WmsOutDeliveryOrderDetDto wmsOutDeliveryOrderDetDto = new WmsOutDeliveryOrderDetDto();
                wmsOutDeliveryOrderDetDto.setWarehouseId(omTransferOrder.getOutWarehouseId());
                wmsOutDeliveryOrderDetDto.setSourceOrderId(omTransferOrder.getTransferOrderId());
                wmsOutDeliveryOrderDetDto.setOrderDetId(omTransferOrderDet.getTransferOrderDetId());
                wmsOutDeliveryOrderDetDto.setStorageId(storageId);
                wmsOutDeliveryOrderDetDto.setMaterialId(omTransferOrderDet.getMaterialId());
                wmsOutDeliveryOrderDetDto.setLineNumber(i);
                wmsOutDeliveryOrderDetDto.setPackingUnitName(unitName);
                wmsOutDeliveryOrderDetDto.setPackingQty(omTransferOrderDet.getOrderQty());
                wmsOutDeliveryOrderDetDto.setBatchCode(omTransferOrderDet.getBatchCode());
                wmsOutDeliveryOrderDetDtos.add(wmsOutDeliveryOrderDetDto);
                i++;
            }
            if(wmsOutDeliveryOrderDetDtos.size()<1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"暂无可下发的调拨货品");
            }
            wmsOutDeliveryOrder.setWmsOutDeliveryOrderDetList(wmsOutDeliveryOrderDetDtos);
            ResponseEntity responseEntity = outFeignApi.add(wmsOutDeliveryOrder);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"生成出库单失败");
            }
            omTransferOrder.setOrderStatus((byte)2);
            omTransferOrderMapper.updateByPrimaryKeySelective(omTransferOrder);
            num++;
        }
        return num;
    }
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmTransferOrder record) {
        SysUser sysUser = currentUser();
        record.setTransferOrderCode(CodeUtils.getId("DBOD-"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setOrderStatus((byte)1);
        int num = omTransferOrderMapper.insertUseGeneratedKeys(record);
        for (OmTransferOrderDet omTransferOrderDet : record.getOmTransferOrderDets()) {
            omTransferOrderDet.setTransferOrderId(record.getTransferOrderId());
            omTransferOrderDet.setCreateTime(new Date());
            omTransferOrderDet.setCreateUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
            omTransferOrderDet.setModifiedTime(new Date());
            omTransferOrderDet.setModifiedUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
            num+=omTransferOrderDetMapper.insertSelective(omTransferOrderDet);
        }
        this.addHt(record,record.getOmTransferOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmTransferOrder entity) {
        SysUser sysUser = currentUser();
        if(entity.getOrderStatus()>1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),entity.getOrderStatus()==2?"订单已下发，无法修改":"订单已完成");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        //删除原有明细
        Example example = new Example(OmTransferOrderDet.class);
        example.createCriteria().andEqualTo("transferOrderId",entity.getTransferOrderId());
        omTransferOrderDetMapper.deleteByExample(example);
        for (OmTransferOrderDet omTransferOrderDet : entity.getOmTransferOrderDets()) {
            omTransferOrderDet.setTransferOrderDetId(null);
            omTransferOrderDet.setTransferOrderId(entity.getTransferOrderId());
            omTransferOrderDet.setCreateTime(new Date());
            omTransferOrderDet.setCreateUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
            omTransferOrderDet.setModifiedTime(new Date());
            omTransferOrderDet.setModifiedUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        int num = 0;
        if(StringUtils.isNotEmpty(entity.getOmTransferOrderDets())){
            num= omTransferOrderDetMapper.insertList(entity.getOmTransferOrderDets());
        }

        num+=omTransferOrderMapper.updateByPrimaryKeySelective(entity);

        this.addHt(entity,entity.getOmTransferOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmTransferOrder omTransferOrder = omTransferOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omTransferOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //删除明细
            Example example = new Example(OmTransferOrderDet.class);
            example.createCriteria().andEqualTo("transferOrderId",omTransferOrder.getTransferOrderId());
            omTransferOrderDetMapper.deleteByExample(example);

            this.addHt(omTransferOrder,null);
        }
        return omTransferOrderMapper.deleteByIds(ids);
    }

    /**
     * 更改单据状态
     * @param omTransferOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateStatus(OmTransferOrder omTransferOrder) {
        SysUser sysUser = currentUser();
        omTransferOrder.setModifiedUserId(sysUser.getUserId());
        omTransferOrder.setModifiedTime(new Date());
        return omTransferOrderMapper.updateByPrimaryKeySelective(omTransferOrder);
    }

    @Override
    public List<OmHtTransferOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtTransferOrderMapper.findHtList(map);
    }

    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }


    /**
     * 添加历史记录
     * @return
     */
    private int addHt(OmTransferOrder omTransferOrder, List<OmTransferOrderDet> omTransferOrderDets){
        int num = 0;
        if(StringUtils.isNotEmpty(omTransferOrder)){
            OmHtTransferOrder omHtTransferOrder = new OmHtTransferOrder();
            BeanUtil.copyProperties(omTransferOrder,omHtTransferOrder);
            num+=omHtTransferOrderMapper.insertSelective(omHtTransferOrder);
        }
        if(StringUtils.isNotEmpty(omTransferOrderDets)){
            for (OmTransferOrderDet omTransferOrderDet : omTransferOrderDets) {
                OmHtTransferOrderDet omHtTransferOrderDet = new OmHtTransferOrderDet();
                BeanUtil.copyProperties(omTransferOrderDet,omHtTransferOrderDet);
                num+=omHtTransferOrderDetMapper.insertSelective(omHtTransferOrderDet);
            }
        }
        return num;
    }
}
