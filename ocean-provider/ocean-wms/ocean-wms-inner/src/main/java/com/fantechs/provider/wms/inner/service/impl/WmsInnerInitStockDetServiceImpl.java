package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStock;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInitStockBarcodeMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInitStockDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInitStockMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockDetService;
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
 * Created by leifengzhi on 2021/12/01.
 */
@Service
public class WmsInnerInitStockDetServiceImpl extends BaseService<WmsInnerInitStockDet> implements WmsInnerInitStockDetService {

    @Resource
    private WmsInnerInitStockMapper wmsInnerInitStockMapper;
    @Resource
    private WmsInnerInitStockDetMapper wmsInnerInitStockDetMapper;
    @Resource
    private WmsInnerInitStockBarcodeMapper wmsInnerInitStockBarcodeMapper;

    @Override
    public List<WmsInnerInitStockDetDto> findList(Map<String, Object> map) {
        return wmsInnerInitStockDetMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInnerInitStockDet commit(WmsInnerInitStockDet record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(record.getInitStockId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        WmsInnerInitStock wmsInnerInitStock = wmsInnerInitStockMapper.selectByPrimaryKey(record.getInitStockId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        if(wmsInnerInitStock.getInitStockType()==1){
            //初始化盘点
            //查询是否有改物料的明细
            Example example = new Example(WmsInnerInitStockDet.class);
            example.createCriteria().andEqualTo("initStockId",wmsInnerInitStock.getInitStockId()).andEqualTo("materialId",record.getMaterialId());
            List<WmsInnerInitStockDet> wmsInnerInitStockDets = wmsInnerInitStockDetMapper.selectByExample(example);
            if(!wmsInnerInitStockDets.isEmpty()){
                WmsInnerInitStockDet wmsInnerInitStockDet = wmsInnerInitStockDets.get(0);

                record.setInitStockDetId(wmsInnerInitStockDets.get(0).getInitStockDetId());
                if(StringUtils.isEmpty(wmsInnerInitStockDet.getStockQty())){
                    wmsInnerInitStockDet.setStockQty(BigDecimal.ZERO);
                }
                if(StringUtils.isEmpty(wmsInnerInitStockDet.getPlanQty())){
                    wmsInnerInitStockDet.setPlanQty(BigDecimal.ZERO);
                }
                record.setStockQty(record.getStockQty().add(wmsInnerInitStockDet.getStockQty()));
                //差异量
                BigDecimal varQty = record.getStockQty().subtract(wmsInnerInitStockDet.getPlanQty());
                int qty = varQty.intValue();
                if(varQty.signum()==-1){
                    qty =  ~(varQty.intValue() - 1);
                }
                record.setVarianceQty(new BigDecimal(qty));
                //wmsInnerInitStockDetMapper.updateByPrimaryKeySelective(record);
            }else {
                record.setPlanQty(BigDecimal.ZERO);
                record.setCreateTime(new Date());
                record.setCreateUserId(sysUser.getUserId());
                record.setOrgId(sysUser.getOrganizationId());

                //差异量
                BigDecimal varQty = record.getStockQty().subtract(record.getPlanQty());
                int qty = varQty.intValue();
                if(varQty.signum()==-1){
                   qty =  ~(varQty.intValue() - 1);
                }
                record.setVarianceQty(new BigDecimal(qty));

                wmsInnerInitStockDetMapper.insertUseGeneratedKeys(record);
            }
        }else {
            WmsInnerInitStockDet wmsInnerInitStockDet = wmsInnerInitStockDetMapper.selectByPrimaryKey(record.getInitStockDetId());
            if(StringUtils.isEmpty(wmsInnerInitStockDet.getPlanQty())){
                wmsInnerInitStockDet.setPlanQty(BigDecimal.ZERO);
            }
            if(StringUtils.isEmpty(wmsInnerInitStockDet.getStockQty())){
                wmsInnerInitStockDet.setStockQty(BigDecimal.ZERO);
            }
            record.setStockQty(wmsInnerInitStockDet.getStockQty().add(record.getStockQty()));
            //差异量
            BigDecimal varQty = record.getStockQty().subtract(wmsInnerInitStockDet.getPlanQty());
            int qty = varQty.intValue();
            if(varQty.signum()==-1){
                qty =  ~(varQty.intValue() - 1);
            }
            record.setVarianceQty(new BigDecimal(qty));
        }
        wmsInnerInitStockDetMapper.updateByPrimaryKeySelective(record);
        for (WmsInnerInitStockBarcode wmsInnerInitStockBarcode : record.getWmsInnerInitStockBarcodes()) {
            wmsInnerInitStockBarcode.setInitStockDetId(record.getInitStockDetId());
            wmsInnerInitStockBarcode.setInitStockId(wmsInnerInitStock.getInitStockId());
        }
        if(!record.getWmsInnerInitStockBarcodes().isEmpty()){
            wmsInnerInitStockBarcodeMapper.insertList(record.getWmsInnerInitStockBarcodes());
        }
        wmsInnerInitStock.setModifiedTime(new Date());
        wmsInnerInitStock.setModifiedUserId(sysUser.getUserId());
        wmsInnerInitStock.setOrderStatus((byte)2);
        wmsInnerInitStockMapper.updateByPrimaryKeySelective(wmsInnerInitStock);
        return record;
    }
}
