package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrap;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJo;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.out.mapper.*;
import com.fantechs.provider.wms.out.service.WmsOutDespatchOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */
@Service
public class WmsOutDespatchOrderServiceImpl extends BaseService<WmsOutDespatchOrder> implements WmsOutDespatchOrderService {

    @Resource
    private WmsOutDespatchOrderMapper wmsOutDespatchOrderMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private WmsOutDespatchOrderReJoMapper wmsOutDespatchOrderReJoMapper;
    @Resource
    private WmsOutDespatchOrderReJoReDetMapper wmsOutDespatchOrderReJoReDetMapper;
    @Resource
    private WmsOutDeliveryOrderDetMapper wmsOutDeliveryOrderDetMapper;

    @Override
    public List<WmsOutDespatchOrderDto> findList(SearchWmsOutDespatchOrder searchWmsOutDespatchOrder) {
        return wmsOutDespatchOrderMapper.findList(searchWmsOutDespatchOrder);
    }

    /**
     * 发运
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int forwarding(String ids) {
        SysUser sysUser = currentUser();
        String[] arrIds = ids.split(",");
        int num = 0;
        for (String arrId : arrIds) {
            WmsOutDespatchOrder wmsOutDespatchOrder = wmsOutDespatchOrderMapper.selectByPrimaryKey(arrId);
            Example example = new Example(WmsOutDespatchOrderReJo.class);
            example.createCriteria().andEqualTo("despatchOrderId",arrId);
            List<WmsOutDespatchOrderReJo> list = wmsOutDespatchOrderReJoMapper.selectByExample(example);
            for (WmsOutDespatchOrderReJo wms : list) {
                SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
                searchWmsInnerJobOrder.setJobOrderId(wms.getJobOrderId());
                WmsInnerJobOrderDto wmsInnerJobOrder = innerFeignApi.findList(searchWmsInnerJobOrder).getData().get(0);

                Example example1 = new Example(WmsOutDespatchOrderReJoReDet.class);
                example1.createCriteria().andEqualTo("despatchOrderReJoId",wms.getDespatchOrderReJoId());
                List<WmsOutDespatchOrderReJoReDet> wmsOutDespatchOrderReJoReDetDtoList = wmsOutDespatchOrderReJoReDetMapper.selectByExample(example1);
                for (WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet : wmsOutDespatchOrderReJoReDetDtoList) {
                    //明细
                    SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsOutDespatchOrderReJoReDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = innerFeignApi.findList(searchWmsInnerJobOrderDet).getData().get(0);
                    Map<String,Object> map = new HashMap<>();
                    map.put("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode());
                    map.put("warehouseName",wmsInnerJobOrder.getWarehouseName());
                    map.put("storageName",wmsInnerJobOrderDetDto.getInStorageName());
                    map.put("materialId",wmsInnerJobOrderDetDto.getMaterialId());
                    map.put("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
                    ResponseEntity<WmsInnerInventory> responseEntity = innerFeignApi.selectOneByExample(map);
                    if(responseEntity.getCode()!=0){
                        throw new BizErrorException(ErrorCodeEnum.GL9999404);
                    }
                    WmsInnerInventory wmsInnerInventory = responseEntity.getData();
                    if(StringUtils.isEmpty(wmsInnerInventory)){
                        throw new BizErrorException("未匹配到库存");
                    }
                    if(wmsInnerJobOrderDetDto.getActualQty().compareTo(wmsInnerInventory.getPackingQty())==1){
                        throw new BizErrorException("库存不足");
                    }
                    wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInnerJobOrderDetDto.getActualQty()));
                    ResponseEntity rs = innerFeignApi.updateByPrimaryKeySelective(wmsInnerInventory);
                    if(responseEntity.getCode()!=0){
                        throw new BizErrorException(rs.getMessage());
                    }
                    //数量反写销售订单
                    int i = this.writeDeliveryOrderQty(wmsInnerJobOrder,wmsInnerJobOrderDetDto);
                    //反写拣货单状态
                    i = this.retrographyStatus(wmsInnerJobOrderDetDto);
                }
            }
            wmsOutDespatchOrder.setOrderStatus((byte)4);
            num = wmsOutDespatchOrderMapper.updateByPrimaryKeySelective(wmsOutDespatchOrder);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int finishTruckloading(String ids) {
        String[] arrIds = ids.split(",");
        int num =0;
        for (String arrId : arrIds) {
            WmsOutDespatchOrder wmsOutDespatchOrder = wmsOutDespatchOrderMapper.selectByPrimaryKey(arrId);
            if(StringUtils.isEmpty(wmsOutDespatchOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            wmsOutDespatchOrder.setOrderStatus((byte)3);
            num +=wmsOutDespatchOrderMapper.updateByPrimaryKeySelective(wmsOutDespatchOrder);
        }
        return num;
    }

    @Override
    public List<WmsOutDespatchOrderReJoReDetDto> findDetList(SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet) {
        return wmsOutDespatchOrderReJoReDetMapper.findList(searchWmsOutDespatchOrderReJoReDet);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsOutDespatchOrder record) {
        SysUser sysUser = currentUser();
        record.setDespatchOrderCode(CodeUtils.getId("TRUCK"));
        record.setOrderStatus((byte)1);
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        int num = wmsOutDespatchOrderMapper.insertUseGeneratedKeys(record);
        for (WmsOutDespatchOrderReJo wms : record.getWmsOutDespatchOrderReJo()) {
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wms.getJobOrderId());
            ResponseEntity<List<WmsInnerJobOrderDetDto>> responseEntity = innerFeignApi.findList(searchWmsInnerJobOrderDet);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
            List<WmsInnerJobOrderDetDto> list = responseEntity.getData();
            wms.setDespatchOrderId(record.getDespatchOrderId());
            wms.setCreateTime(new Date());
            wms.setCreateUserId(sysUser.getUserId());
            wms.setModifiedTime(new Date());
            wms.setModifiedUserId(sysUser.getUserId());
            wms.setOrgId(sysUser.getOrganizationId());
            wmsOutDespatchOrderReJoMapper.insertUseGeneratedKeys(wms);
            for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : list) {
                WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet = new WmsOutDespatchOrderReJoReDetDto();
                wmsOutDespatchOrderReJoReDet.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
                wmsOutDespatchOrderReJoReDet.setDespatchOrderReJoId(wms.getDespatchOrderReJoId());
                wmsOutDespatchOrderReJoReDet.setCreateTime(new Date());
                wmsOutDespatchOrderReJoReDet.setCreateUserId(sysUser.getUserId());
                wmsOutDespatchOrderReJoReDet.setModifiedTime(new Date());
                wmsOutDespatchOrderReJoReDet.setModifiedUserId(sysUser.getUserId());
                wmsOutDespatchOrderReJoReDet.setOrgId(sysUser.getOrganizationId());
                num +=wmsOutDespatchOrderReJoReDetMapper.insertSelective(wmsOutDespatchOrderReJoReDet);
            }
        }

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsOutDespatchOrder entity) {
        SysUser sysUser = currentUser();

        int num = 0;

        Example example = new Example(WmsOutDespatchOrderReJo.class);
        example.createCriteria().andEqualTo("despatchOrderId",entity.getDespatchOrderId());
        wmsOutDespatchOrderReJoMapper.deleteByExample(example);
        for (WmsOutDespatchOrderReJo wms : entity.getWmsOutDespatchOrderReJo()) {
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wms.getJobOrderId());
            ResponseEntity<List<WmsInnerJobOrderDetDto>> responseEntity = innerFeignApi.findList(searchWmsInnerJobOrderDet);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
            List<WmsInnerJobOrderDetDto> list = responseEntity.getData();
            wms.setDespatchOrderId(entity.getDespatchOrderId());
            wms.setCreateTime(new Date());
            wms.setCreateUserId(sysUser.getUserId());
            wms.setModifiedTime(new Date());
            wms.setModifiedUserId(sysUser.getUserId());
            wms.setOrgId(sysUser.getOrganizationId());
            wmsOutDespatchOrderReJoMapper.insertUseGeneratedKeys(wms);
            for (WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto : list) {
                WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet = new WmsOutDespatchOrderReJoReDetDto();
                wmsOutDespatchOrderReJoReDet.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
                wmsOutDespatchOrderReJoReDet.setDespatchOrderReJoId(wms.getDespatchOrderReJoId());
                wmsOutDespatchOrderReJoReDet.setCreateTime(new Date());
                wmsOutDespatchOrderReJoReDet.setCreateUserId(sysUser.getUserId());
                wmsOutDespatchOrderReJoReDet.setModifiedTime(new Date());
                wmsOutDespatchOrderReJoReDet.setModifiedUserId(sysUser.getUserId());
                wmsOutDespatchOrderReJoReDet.setOrgId(sysUser.getOrganizationId());
                num +=wmsOutDespatchOrderReJoReDetMapper.insertSelective(wmsOutDespatchOrderReJoReDet);
            }
        }
        entity.setOrderStatus((byte)2);
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        num+=wmsOutDespatchOrderMapper.updateByPrimaryKeySelective(entity);
        return num;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrIds = ids.split(",");
        for (String id : arrIds) {
            WmsOutDespatchOrder wmsOutDespatchOrder = wmsOutDespatchOrderMapper.selectByPrimaryKey(id);
            if(wmsOutDespatchOrder.getOrderStatus()==(4)){
                throw new BizErrorException("单据已完成，无法删除");
            }
            if(StringUtils.isEmpty(wmsOutDespatchOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return super.batchDelete(ids);
    }

    /**
     * 反写销售出库单拣货数量
     * @param wmsInnerJobOrderDto
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int writeDeliveryOrderQty(WmsInnerJobOrderDto wmsInnerJobOrderDto,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        Example example = new Example(WmsOutDeliveryOrderDet.class);
        example.createCriteria().andEqualTo("deliveryOrderId",wmsInnerJobOrderDto.getSourceOrderId())
                .andEqualTo("materialId",wmsInnerJobOrderDetDto.getMaterialId())
                .andEqualTo("storageId",wmsInnerJobOrderDetDto.getInStorageId())
                .andEqualTo("warehouseId",wmsInnerJobOrderDto.getWarehouseId())
                .andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
        WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = wmsOutDeliveryOrderDetMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsOutDeliveryOrderDet)){
            throw new BizErrorException("未匹配到关联的出库单");
        }
        wmsOutDeliveryOrderDet.setDispatchQty(wmsInnerJobOrderDetDto.getActualQty());

        //反写销售订单出库数量
        return wmsOutDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrderDet);
    }

    /**
     * 更新拣货单状态
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int retrographyStatus(WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        ResponseEntity responseEntity = innerFeignApi.retrographyStatus(WmsInnerJobOrderDet.builder()
                .jobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId())
                .jobOrderId(wmsInnerJobOrderDetDto.getJobOrderId())
                .orderStatus((byte)6)
                .build());
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        return 1;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
