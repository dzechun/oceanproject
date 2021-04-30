package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderService;
import org.springframework.stereotype.Service;
import com.fantechs.common.base.support.BaseService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/04/29.
 */
@Service
public class WmsInAsnOrderServiceImpl extends BaseService<WmsInAsnOrder> implements WmsInAsnOrderService {

    @Resource
    private WmsInAsnOrderMapper wmsInAsnOrderMapper;
    @Resource
    private WmsInAsnOrderDetMapper wmsInAsnOrderDetMapper;

    @Override
    public List<WmsInAsnOrderDto> findList(SearchWmsInAsnOrder searchWmsInAsnOrder) {
        return wmsInAsnOrderMapper.findList(searchWmsInAsnOrder);
    }

    /**
     * 整单收货
     * @param ids
     * @param inventoryStatusId
     * @return
     */
    @Override
    public int allReceiving(String ids,Long inventoryStatusId) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInAsnOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            List<WmsInAsnOrderDet> list = wmsInAsnOrderDetMapper.select(WmsInAsnOrderDet.builder()
                    .asnOrderId(Long.parseLong(s))
                    .build());
            for (WmsInAsnOrderDet wmsInAsnOrderDet : list) {
                wmsInAsnOrderDet.setInventoryStatusId(inventoryStatusId);
                wmsInAsnOrderDet.setActualQty(wmsInAsnOrderDet.getPackingQty());

                wmsInAsnOrderDet.setModifiedTime(new Date());
                wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wmsInAsnOrderDet);
            }
            wmsInAsnOrder.setModifiedTime(new Date());
            wmsInAsnOrder.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrder.setOrderStatus((byte)2);
            wmsInAsnOrderMapper.updateByPrimaryKeySelective(wmsInAsnOrder);
        }

        //添加库存
        return 1;
    }

    /**
     * 单一收货
     * @param wmsInAsnOrderDet
     * @return
     */
    @Override
    public int singleReceiving(WmsInAsnOrderDet wmsInAsnOrderDet) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(wmsInAsnOrderDet.getAsnOrderDetId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"参数错误");
        }
        wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
        wmsInAsnOrderDet.setModifiedTime(new Date());
        //收货数量达到总数更改单据完成状态
        if(wmsInAsnOrderDet.getActualQty().compareTo(wmsInAsnOrderDet.getPackingQty())==1){
            wmsInAsnOrderMapper.updateByPrimaryKeySelective(WmsInAsnOrder.builder()
                    .orderStatus((byte)2)
                    .modifiedTime(new Date())
                    .modifiedUserId(sysUser.getUserId())
                    .build());
        }
        return wmsInAsnOrderDetMapper.updateByPrimaryKeySelective(wmsInAsnOrderDet);
    }

    @Override
    public int save(WmsInAsnOrder record) {
        SysUser sysUser = currentUser();
        record.setAsnCode(CodeUtils.getId("ASN-"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(record);
        for (WmsInAsnOrderDet wmsInAsnOrderDet : record.getWmsInAsnOrderDetList()) {
            wmsInAsnOrderDet.setAsnOrderId(record.getAsnOrderId());
            wmsInAsnOrderDet.setCreateTime(new Date());
            wmsInAsnOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDetMapper.insert(wmsInAsnOrderDet);
        }
        return num;
    }

    @Override
    public int update(WmsInAsnOrder entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        int num = wmsInAsnOrderMapper.insertUseGeneratedKeys(entity);

        //删除原有明细
        wmsInAsnOrderDetMapper.delete(WmsInAsnOrderDet.builder()
                .asnOrderId(entity.getAsnOrderId())
                .build());
        for (WmsInAsnOrderDet wmsInAsnOrderDet : entity.getWmsInAsnOrderDetList()) {
            wmsInAsnOrderDet.setAsnOrderId(entity.getAsnOrderId());
            wmsInAsnOrderDet.setModifiedTime(new Date());
            wmsInAsnOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInAsnOrderDetMapper.insert(wmsInAsnOrderDet);
        }
        return num;
    }

    @Override
    public int batchDelete(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInAsnOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            wmsInAsnOrderDetMapper.delete(WmsInAsnOrderDet.builder()
                    .asnOrderId(Long.parseLong(s))
                    .build());
        }
        return wmsInAsnOrderMapper.deleteByIds(ids);
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
