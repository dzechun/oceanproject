package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanStockList;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;

import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderDetBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/12/28.
 */
@Service
public class WmsInnerStockOrderDetBarcodeServiceImpl extends BaseService<WmsInnerStockOrderDetBarcode> implements WmsInnerStockOrderDetBarcodeService {

    @Resource
    private WmsInnerStockOrderDetBarcodeMapper wmsInnerStockOrderDetBarcodeMapper;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;


    @Override
    public List<WmsInnerStockOrderDetBarcodeDto> findList(Map<String, Object> map) {
        return wmsInnerStockOrderDetBarcodeMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInnerStockOrderDetBarcodeDto record) {
        int num=0;
        SysUser sysUser=CurrentUserInfoUtils.getCurrentUserInfo();
        Example exampleDet = new Example(WmsInnerMaterialBarcode.class);
        Example.Criteria criteriaDet = exampleDet.createCriteria();
        criteriaDet.andEqualTo("barcode", record.getBarcode());
        List<WmsInnerMaterialBarcode> materialBarcodes=wmsInnerMaterialBarcodeMapper.selectByExample(exampleDet);
        if(materialBarcodes.size()>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已存在不能重复新增");
        }
        WmsInnerMaterialBarcode materialBarcode=new WmsInnerMaterialBarcode();
        materialBarcode.setBarcode(record.getBarcode());
        materialBarcode.setMaterialId(record.getMaterialId());
        materialBarcode.setMaterialQty(record.getMaterialQty());
        materialBarcode.setIfSysBarcode((byte)0);
        materialBarcode.setBatchCode(record.getBatchCode());
        materialBarcode.setProductionTime(record.getProductionTime());
        materialBarcode.setCreateUserId(sysUser.getUserId());
        materialBarcode.setCreateTime(new Date());
        materialBarcode.setOrgId(sysUser.getOrganizationId());
        num+=wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(materialBarcode);

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        int num=1;
        String[] arrId = ids.split(",");
        for (String item : arrId) {
            WmsInnerStockOrderDetBarcode detBarcode=wmsInnerStockOrderDetBarcodeMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(detBarcode)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到要删除的盘点条码信息");
            }

            Example exampleDet = new Example(WmsInnerMaterialBarcode.class);
            Example.Criteria criteriaDet = exampleDet.createCriteria();
            criteriaDet.andEqualTo("materialBarcodeId", detBarcode.getMaterialBarcodeId());
            List<WmsInnerMaterialBarcode> materialBarcodes=wmsInnerMaterialBarcodeMapper.selectByExample(exampleDet);
            if(materialBarcodes.size()>0){
                if(materialBarcodes.get(0).getIfSysBarcode()==(byte)1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"系统条码不能删除");
                }
            }

            num+=wmsInnerStockOrderDetBarcodeMapper.deleteByPrimaryKey(detBarcode);

        }
        return num;
    }


}
