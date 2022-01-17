package com.fantechs.provider.wms.inner.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by Mr.Lei on 2021/06/02.
 */
@Service
public class WmsInnerInventoryDetServiceImpl extends BaseService<WmsInnerInventoryDet> implements WmsInnerInventoryDetService {

    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private WmsInnerMaterialBarcodeService wmsInnerMaterialBarcodeService;

    @Override
    public List<WmsInnerInventoryDetDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
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
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        for (WmsInnerInventoryDet wmsInnerInventoryDet : wmsInnerInventoryDets) {
            wmsInnerInventoryDet.setCreateTime(new Date());
            wmsInnerInventoryDet.setCreateUserId(sysUser.getUserId());
            wmsInnerInventoryDet.setModifiedTime(new Date());
            wmsInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerInventoryDet.setOrgId(sysUser.getOrganizationId());
        }
        return wmsInnerInventoryDetMapper.insertList(wmsInnerInventoryDets);
    }

    /**
     * 减库存明细
     * @param wmsInnerInventoryDetDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int subtract(WmsInnerInventoryDetDto wmsInnerInventoryDetDto) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isEmpty(wmsInnerInventoryDetDto.getMaterialQty()) || wmsInnerInventoryDetDto.getMaterialQty().compareTo(BigDecimal.ZERO)<1){
            throw new BizErrorException("出库数量错误");
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getMaterialBarcodeId())){
            criteria.andEqualTo("materialBarcodeId",wmsInnerInventoryDetDto.getMaterialBarcodeId());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getStorageId())){
            criteria.andEqualTo("storageId",wmsInnerInventoryDetDto.getStorageId());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getMaterialId())){
            criteria.andEqualTo("materialId",wmsInnerInventoryDetDto.getMaterialId());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getAsnCode())){
            criteria.andEqualTo("asnCode",wmsInnerInventoryDetDto.getAsnCode());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getDeliveryOrderCode())){
            criteria.andEqualTo("deliveryOrderCode",wmsInnerInventoryDetDto.getDeliveryOrderCode());
        }
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        List<WmsInnerInventoryDet> wms = wmsInnerInventoryDetMapper.selectByExample(example);
        BigDecimal qty = wmsInnerInventoryDetDto.getMaterialQty();
        int num=0;
        for (WmsInnerInventoryDet wm : wms) {
            if(qty.compareTo(BigDecimal.ZERO)==0){
                break;
            }
                if(qty.compareTo(wmsInnerInventoryDetDto.getMaterialQty())==1){
                    if(qty.compareTo(wmsInnerInventoryDetDto.getMaterialQty())==1){
                        //删除记录
                        wmsInnerInventoryDetMapper.deleteByPrimaryKey(wmsInnerInventoryDetDto.getInventoryDetId());
                    }else{
                        wmsInnerInventoryDetDto.setMaterialQty(wmsInnerInventoryDetDto.getMaterialQty().subtract(qty));
                    }
                    qty.subtract(wmsInnerInventoryDetDto.getMaterialQty());
                }
                num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wm);
        }
        return num;
    }

    @Override
    public WmsInnerInventoryDet findByOne(String barCode){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(WmsInnerInventoryDet.class);
        example.createCriteria().andEqualTo("barcode",barCode).andEqualTo("orgId",sysUser.getOrganizationId());
        List<WmsInnerInventoryDet> wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectByExample(example);
        if(wmsInnerInventoryDet.size()>0){
            return wmsInnerInventoryDet.get(0);
        }
        return null;
    }

    /**
     * 查询对应条码的库存(箱码只查询到箱码，栈板码只查询到栈板码，不查询下一级)
     * @param codes
     * @return
     */
    @Override
    public List<WmsInnerInventoryDetDto> findListByBarCode(List<String> codes) {
        List<WmsInnerMaterialBarcodeDto> list = wmsInnerMaterialBarcodeService.findListByCode(codes);
        List<Long> barCodes = new ArrayList<>();
        if(StringUtils.isNotEmpty(list)){
            for(String code : codes){
                for(WmsInnerMaterialBarcodeDto dto : list) {
                    if (code.equals(dto.getBarcode())) {
                        barCodes.add(dto.getMaterialBarcodeId());
                    }else if(code.equals(dto.getColorBoxCode())){
                        //彩盒码,箱码为空
                        if(StringUtils.isEmpty(dto.getBarcode()))
                            barCodes.add(dto.getMaterialBarcodeId());
                    }else if(code.equals(dto.getCartonCode())){
                        //箱码,箱码为空
                        if(StringUtils.isEmpty(dto.getBarcode()) ||  StringUtils.isEmpty(dto.getColorBoxCode()))
                            barCodes.add(dto.getMaterialBarcodeId());
                    }else if(code.equals(dto.getPalletCode())){
                        //栈板码,箱码为空
                        if(StringUtils.isEmpty(dto.getBarcode()) ||  StringUtils.isEmpty(dto.getColorBoxCode()) || StringUtils.isEmpty(dto.getCartonCode()))
                            barCodes.add(dto.getMaterialBarcodeId());
                    }
                }
            }
        }
        if(StringUtils.isEmpty(barCodes))
            throw new BizErrorException("未在条码表中查询到相应的条码信息");
        Map map = new HashMap();
        map.put("materialBarcodeIdList",barCodes);
        return wmsInnerInventoryDetMapper.findList(map);
    }
}
