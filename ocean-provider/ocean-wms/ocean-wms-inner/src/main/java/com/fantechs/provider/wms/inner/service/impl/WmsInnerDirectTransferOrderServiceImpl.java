package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerDirectTransferOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerDirectTransferOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerDirectTransferOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerDirectTransferOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerDirectTransferOrderMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerDirectTransferOrderService;
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
 * Created by leifengzhi on 2021/12/21.
 */
@Service
public class WmsInnerDirectTransferOrderServiceImpl extends BaseService<WmsInnerDirectTransferOrder> implements WmsInnerDirectTransferOrderService {

    @Resource
    private WmsInnerDirectTransferOrderMapper wmsInnerDirectTransferOrderMapper;
    @Resource
    private WmsInnerDirectTransferOrderDetMapper wmsInnerDirectTransferOrderDetMapper;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;

    @Override
    public List<WmsInnerDirectTransferOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsInnerDirectTransferOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(List<PDAWmsInnerDirectTransferOrderDto> pdaWmsInnerDirectTransferOrderDtos) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(pdaWmsInnerDirectTransferOrderDtos)) throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"提交参数不能为空");
        List<WmsInnerDirectTransferOrderDet> list = new ArrayList<>();
        List<WmsInnerInventory> inventoryList = new ArrayList<>();
        List<WmsInnerInventoryDet> inventoryDetList = new ArrayList<>();
        int i = 0;
        for(PDAWmsInnerDirectTransferOrderDto dto : pdaWmsInnerDirectTransferOrderDtos){
            if(StringUtils.isEmpty(dto.getPdaWmsInnerDirectTransferOrderDetDtos(),dto.getMaterialId(),dto.getInStorageId(),dto.getOutStorageId(),dto.getWorkerUserId()))
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"相关参数不能为空");
            WmsInnerDirectTransferOrder order = new WmsInnerDirectTransferOrder();
            order.setDirectTransferOrderCode(CodeUtils.getId("INNER-DTO"));
            order.setWorkerUserId(dto.getWorkerUserId());
            order.setOrderStatus((byte)3);
            order.setStatus((byte)1);
            order.setOrgId(user.getOrganizationId());
            order.setCreateUserId(user.getUserId());
            order.setCreateTime(new Date());
            order.setModifiedUserId(user.getUserId());
            order.setModifiedTime(new Date());
            wmsInnerDirectTransferOrderMapper.insertUseGeneratedKeys(order);
            for(PDAWmsInnerDirectTransferOrderDetDto det : dto.getPdaWmsInnerDirectTransferOrderDetDtos()){

                Example example = new Example(WmsInnerInventoryDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("outStorageId", dto.getOutStorageId());
                criteria.andEqualTo("barcode", det.getBarcode());
                criteria.andEqualTo("materialId", dto.getMaterialId());
                List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example);
                if(StringUtils.isNotEmpty(wmsInnerInventoryDets)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"为查询到已出库位的物料条码，条码为："+det.getBarcode());
                }
                if(wmsInnerInventoryDets.get(0).getMaterialQty().compareTo(det.getQty())== -1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"扫描的条码数量不能大于库存数量，条码为："+det.getBarcode());
                }
                wmsInnerInventoryDets.get(0).setStorageId(dto.getInStorageId());
                inventoryDetList.add(wmsInnerInventoryDets.get(0));

                //TODO 需要修改移入、移除库位数量
/*                Example example1 = new Example(WmsInnerInventory.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("storageId", dto.getOutStorageId());
                List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(example);
                if(StringUtils.isNotEmpty(wmsInnerInventoryDets)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"为查询到已出库位的物料条码，条码为："+det.getBarcode());
                }*/

                WmsInnerDirectTransferOrderDet orderDet = new WmsInnerDirectTransferOrderDet();
                orderDet.setDirectTransferOrderId(order.getDirectTransferOrderId());
                orderDet.setInStorageId(dto.getInStorageId());
                orderDet.setOutStorageId(dto.getOutStorageId());
                orderDet.setMaterialId(dto.getMaterialId());
                orderDet.setActualQty(det.getQty());
                orderDet.setStatus((byte)1);
                orderDet.setOrgId(user.getOrganizationId());
                orderDet.setCreateUserId(user.getUserId());
                orderDet.setCreateTime(new Date());
                orderDet.setModifiedUserId(user.getUserId());
                orderDet.setModifiedTime(new Date());
                list.add(orderDet);
            }
        }
        if(StringUtils.isNotEmpty(list)){
            wmsInnerDirectTransferOrderDetMapper.insertList(list);
        }
        if(StringUtils.isNotEmpty(inventoryDetList)){
            wmsInnerInventoryDetMapper.insertList(inventoryDetList);
        }


        return i;
    }

}
