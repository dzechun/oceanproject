package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.search.*;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInPalletCarton;
import com.fantechs.common.base.general.entity.wms.in.history.WmsInHtFinishedProduct;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;

import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.in.controller.pda.PDAMesPackageManagerController;
import com.fantechs.provider.wms.in.controller.pda.PDAWmsInStorageBillsController;
import com.fantechs.provider.wms.in.mapper.WmsInFinishedProductDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInFinishedProductMapper;
import com.fantechs.provider.wms.in.mapper.WmsInHtFinishedProductMapper;
import com.fantechs.provider.wms.in.mapper.WmsInPalletCartonMapper;
import com.fantechs.provider.wms.in.service.WmsInFinishedProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class WmsInFinishedProductServiceImpl  extends BaseService<WmsInFinishedProduct> implements WmsInFinishedProductService {

    @Resource
    private WmsInFinishedProductMapper wmsInFinishedProductMapper;
    @Resource
    private WmsInHtFinishedProductMapper wmsInHtFinishedProductMapper;
    @Resource
    private WmsInPalletCartonMapper wmsInPalletCartonMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;
    @Resource
    private WmsInFinishedProductDetMapper wmsInFinishedProductDetMapper;
    @Resource
    private PDAMesPackageManagerController pdaMesPackageManagerController;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsInFinishedProduct wmsInFinishedProduct) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(wmsInFinishedProduct.getWmsInFinishedProductDetList())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsInFinishedProduct.setFinishedProductCode(CodeUtils.getId("CPRK-"));
        wmsInFinishedProduct.setInType(StringUtils.isEmpty(wmsInFinishedProduct.getInType()) ? 0 :wmsInFinishedProduct.getInType());
        wmsInFinishedProduct.setInStatus(StringUtils.isEmpty(wmsInFinishedProduct.getInStatus()) ? 0 :wmsInFinishedProduct.getInStatus());
        wmsInFinishedProduct.setStatus((byte)1);
        wmsInFinishedProduct.setIsDelete((byte)1);
        wmsInFinishedProduct.setCreateTime(new Date());
        wmsInFinishedProduct.setCreateUserId(user.getUserId());

        int result = wmsInFinishedProductMapper.insertUseGeneratedKeys(wmsInFinishedProduct);

        //履历
        WmsInHtFinishedProduct wmsInHtFinishedProduct = new WmsInHtFinishedProduct();
        BeanUtils.copyProperties(wmsInFinishedProduct,wmsInHtFinishedProduct);
        wmsInHtFinishedProductMapper.insertSelective(wmsInHtFinishedProduct);

        //查询工单信息
//        SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(wmsInFinishedProduct.getWorkOrderId());

        for (WmsInFinishedProductDet wmsInFinishedProductDet : wmsInFinishedProduct.getWmsInFinishedProductDetList()) {

            wmsInFinishedProductDet.setFinishedProductId(String.valueOf(result));
            wmsInFinishedProductDet.setOrganizationId(user.getOrganizationId());
            wmsInFinishedProductDet.setCreateTime(new Date());
            wmsInFinishedProductDet.setCreateUserId(user.getCreateUserId());
            wmsInFinishedProductDetMapper.insertSelective(wmsInFinishedProductDet);

            //存入库时栈板与包箱关系
            SearchMesPackageManagerListDTO searchMesPackageManagerListDTO = new SearchMesPackageManagerListDTO();
            searchMesPackageManagerListDTO.setIsFindChildren(true);
            searchMesPackageManagerListDTO.setBarcode(wmsInFinishedProductDet.getPalletCode());
            List<MesPackageManagerDTO> mesPackageManagerDTOS = pdaMesPackageManagerController.list(searchMesPackageManagerListDTO).getData();
            if(StringUtils.isEmpty(mesPackageManagerDTOS)){
                throw new BizErrorException(ErrorCodeEnum.GL99990100);
            }
            for (MesPackageManagerDTO mesPackageManagerDTO : mesPackageManagerDTOS) {
                WmsInPalletCarton wmsInPalletCarton = new WmsInPalletCarton();
                wmsInPalletCarton.setCartonCode(mesPackageManagerDTO.getBarCode());
                wmsInPalletCarton.setPalletCode(wmsInFinishedProductDet.getPalletCode());
                wmsInPalletCarton.setStatus((byte)1);
                wmsInPalletCarton.setIsDelete((byte)1);
                wmsInPalletCarton.setCreateTime(new Date());
                wmsInPalletCarton.setCreateUserId(user.getCreateUserId());
                //栈板与包箱关系表
                wmsInPalletCartonMapper.insertSelective(wmsInPalletCarton);

            }

            SmtStoragePallet smtStoragePallet = new SmtStoragePallet();
            smtStoragePallet.setPalletCode(wmsInFinishedProductDet.getPalletCode());
            smtStoragePallet.setStorageId(wmsInFinishedProductDet.getStorageId());
            //存储位与栈板关系表smt
            storageInventoryFeignApi.add(smtStoragePallet);

            //新增库存
            SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
            smtStorageInventory.setStorageId(wmsInFinishedProductDet.getStorageId());
            smtStorageInventory.setMaterialId(wmsInFinishedProductDet.getProductModelId());
            smtStorageInventory.setQuantity(wmsInFinishedProductDet.getInQuantity());

            smtStorageInventory = storageInventoryFeignApi.add(smtStorageInventory).getData();

            //增加库位库存明细
            SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
            smtStorageInventoryDet.setStorageInventoryId(smtStorageInventory.getStorageInventoryId());
            smtStorageInventoryDet.setMaterialBarcodeCode(wmsInFinishedProductDet.getPalletCode());
            smtStorageInventoryDet.setGodownEntry(wmsInFinishedProduct.getFinishedProductCode());
            smtStorageInventoryDet.setMaterialQuantity(wmsInFinishedProductDet.getInQuantity());
            //生产批号，生产日期，供应商ID无
            storageInventoryFeignApi.add(smtStorageInventoryDet);

        }
        return result;
    }

    @Override
    public List<WmsInFinishedProductDto> findList(Map<String, Object> map) {
        return wmsInFinishedProductMapper.findList(map);
    }

    @Override
    public List<WmsInHtFinishedProduct> findHtList(Map<String, Object> map) {
        return wmsInHtFinishedProductMapper.findHtList(map);
    }
}
