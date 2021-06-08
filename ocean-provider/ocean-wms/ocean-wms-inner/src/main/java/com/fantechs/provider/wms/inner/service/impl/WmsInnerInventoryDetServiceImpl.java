package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
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
 * Created by Mr.Lei on 2021/06/02.
 */
@Service
public class WmsInnerInventoryDetServiceImpl extends BaseService<WmsInnerInventoryDet> implements WmsInnerInventoryDetService {

    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;

    @Override
    public List<WmsInnerInventoryDetDto> findList(Map<String, Object> map) {
        return wmsInnerInventoryDetMapper.findList(map);
    }

    /**
     * 加库存明细
     * @param wmsInnerInventoryDets
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int add(List<WmsInnerInventoryDet> wmsInnerInventoryDets) {
        SysUser sysUser = currentUser();
        for (WmsInnerInventoryDet wmsInnerInventoryDet : wmsInnerInventoryDets) {
            wmsInnerInventoryDet.setCreateTime(new Date());
            wmsInnerInventoryDet.setCreateUserId(sysUser.getUserId());
            wmsInnerInventoryDet.setModifiedTime(new Date());
            wmsInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
        }
        return wmsInnerInventoryDetMapper.insertList(wmsInnerInventoryDets);
    }

    /**
     * 减库存明细
     * @param wmsInnerInventoryDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int subtract(WmsInnerInventoryDet wmsInnerInventoryDet) {
        Example example = new Example(WmsInnerJobOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isEmpty(wmsInnerInventoryDet.getMaterialQty()) || wmsInnerInventoryDet.getMaterialQty().compareTo(BigDecimal.ZERO)<1){
            throw new BizErrorException("出库数量错误");
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDet.getBarcode())){
            criteria.andEqualTo("barcode",wmsInnerInventoryDet.getBarcode());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDet.getStorageId())){
            criteria.andEqualTo("storageId",wmsInnerInventoryDet.getStorageId());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDet.getMaterialId())){
            criteria.andEqualTo("materialId",wmsInnerInventoryDet.getMaterialId());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDet.getRelatedOrderCode())){
            criteria.andEqualTo("relatedOrderCode",wmsInnerInventoryDet.getRelatedOrderCode());
        }
        List<WmsInnerInventoryDet> wms = wmsInnerInventoryDetMapper.selectByExample(example);
        BigDecimal qty = wmsInnerInventoryDet.getMaterialQty();
        int num=0;
        for (WmsInnerInventoryDet wm : wms) {
            if(qty.compareTo(BigDecimal.ZERO)==0){
                break;
            }
                if(qty.compareTo(wmsInnerInventoryDet.getMaterialQty())==1){
                    if(qty.compareTo(wmsInnerInventoryDet.getMaterialQty())==1){
                        wmsInnerInventoryDet.setMaterialQty(BigDecimal.ZERO);
                    }else{
                        wmsInnerInventoryDet.setMaterialQty(wmsInnerInventoryDet.getMaterialQty().subtract(qty));
                    }
                    qty.subtract(wmsInnerInventoryDet.getMaterialQty());
                }
                num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wm);
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

    @Override
    public WmsInnerInventoryDet findByOne(String barCode){
        Example example = new Example(WmsInnerInventoryDet.class);
        example.createCriteria().andEqualTo("barcode",barCode);
        List<WmsInnerInventoryDet> wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectByExample(example);
        if(wmsInnerInventoryDet.size()>0){
            return wmsInnerInventoryDet.get(0);
        }
        return null;
    }

}
