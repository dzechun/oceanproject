package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
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
import com.fantechs.provider.wms.in.mapper.WmsInFinishedProductDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInFinishedProductMapper;
import com.fantechs.provider.wms.in.mapper.WmsInHtFinishedProductMapper;
import com.fantechs.provider.wms.in.mapper.WmsInPalletCartonMapper;
import com.fantechs.provider.wms.in.service.WmsInFinishedProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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


    @Override
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
            /*for(){
                //循环栈板上的包装数，等栈板打标模块出来后才能写
            }*/
            WmsInPalletCarton wmsInPalletCarton = new WmsInPalletCarton();
//            wmsInPalletCarton.setCartonCode();
            wmsInPalletCarton.setPalletCode(wmsInFinishedProductDet.getPalletCode());
            wmsInFinishedProduct.setStatus((byte)1);
            wmsInPalletCarton.setIsDelete((byte)1);
            wmsInPalletCarton.setCreateTime(new Date());
            wmsInPalletCarton.setCreateUserId(user.getCreateUserId());
            //栈板与包箱关系表
            wmsInPalletCartonMapper.insertSelective(wmsInPalletCarton);

            SmtStoragePallet smtStoragePallet = new SmtStoragePallet();
            smtStoragePallet.setPalletCode(wmsInFinishedProductDet.getPalletCode());
            smtStoragePallet.setStorageId(wmsInFinishedProductDet.getStorageId());
            //存储位与栈板关系表smt
            storageInventoryFeignApi.add(smtStoragePallet);

            //查询储位库存表，有库存累加，无库存新增
            SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
            searchSmtStorageInventory.setStorageId(wmsInFinishedProductDet.getStorageId().toString());
            searchSmtStorageInventory.setMaterialId(wmsInFinishedProductDet.getProductModelId().toString());
            ResponseEntity<List<SmtStorageInventoryDto>> storageInventoryFeignApiList = storageInventoryFeignApi.findList(searchSmtStorageInventory);
            if(storageInventoryFeignApiList.getCode() == 0){
                List<SmtStorageInventoryDto> smtStorageInventoryDtos = storageInventoryFeignApiList.getData();
                SmtStorageInventoryDto smtStorageInventoryDto = smtStorageInventoryDtos.get(0);
                long storageInventory = 0;
                if(smtStorageInventoryDto != null){
                    storageInventory = smtStorageInventoryDto.getStoringInventoryId();
                    //累加库存
                    smtStorageInventoryDto.setQuantity(smtStorageInventoryDto.getQuantity().add(wmsInFinishedProductDet.getInQuantity()));
                    smtStorageInventoryDto.setModifiedTime(new Date());
                    smtStorageInventoryDto.setModifiedUserId(user.getUserId());
                    storageInventoryFeignApi.update(smtStorageInventoryDto);
                } else {
                    //新增库存
                    SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
                    smtStorageInventory.setStorageId(wmsInFinishedProductDet.getStorageId());
                    smtStorageInventory.setMaterialId(wmsInFinishedProductDet.getProductModelId());
                    smtStorageInventory.setQuantity(wmsInFinishedProductDet.getInQuantity());
                    smtStorageInventory.setOrganizationId(user.getOrganizationId());
                    smtStorageInventory.setCreateTime(new Date());
                    smtStorageInventory.setCreateUserId(user.getCreateUserId());
                    storageInventoryFeignApi.add(smtStorageInventory);
                    storageInventory = smtStorageInventory.getStoringInventoryId();
                }

                //增加库位库存明细
                SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
                smtStorageInventoryDet.setStoringInventoryId(storageInventory);
                smtStorageInventoryDet.setMaterialBarcodeCode(wmsInFinishedProductDet.getPalletCode());
                smtStorageInventoryDet.setGodownEntry(wmsInFinishedProduct.getFinishedProductCode());
                smtStorageInventoryDet.setMaterialQuantity(wmsInFinishedProductDet.getInQuantity());
                //生产批号，生产日期，供应商ID无
                storageInventoryFeignApi.add(smtStorageInventoryDet);
            }else{
                //查询失败
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
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
