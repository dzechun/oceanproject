package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDto;
import com.fantechs.common.base.general.entity.bcm.BcmBarCodeDet;
import com.fantechs.common.base.general.entity.basic.BaseBarCodeDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsPdaInspection;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInPalletCarton;
import com.fantechs.common.base.general.entity.wms.in.history.WmsInHtFinishedProduct;
import com.fantechs.common.base.support.BaseService;

import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.fileserver.service.BcmFeignApi;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.wms.in.controller.pda.PDAMesPackageManagerController;
import com.fantechs.provider.wms.in.mapper.WmsInFinishedProductDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInFinishedProductMapper;
import com.fantechs.provider.wms.in.mapper.WmsInHtFinishedProductMapper;
import com.fantechs.provider.wms.in.mapper.WmsInPalletCartonMapper;
import com.fantechs.provider.wms.in.service.WmsInFinishedProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class WmsInFinishedProductServiceImpl extends BaseService<WmsInFinishedProduct> implements WmsInFinishedProductService {

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
    @Resource
    private BcmFeignApi bcmFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsInFinishedProduct wmsInFinishedProduct) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(wmsInFinishedProduct.getWmsInFinishedProductDetList())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        if (wmsInFinishedProduct.getProjectType().equals("dp")) {
            wmsInFinishedProduct.setFinishedProductCode(CodeUtils.getId("R-"));
            wmsInFinishedProduct.setInStatus(StringUtils.isEmpty(wmsInFinishedProduct.getInStatus()) ? 0 : wmsInFinishedProduct.getInStatus());
        }
        if (wmsInFinishedProduct.getProjectType().equals("hf")) {
            wmsInFinishedProduct.setFinishedProductCode(CodeUtils.getId("CPRK-"));
            wmsInFinishedProduct.setInStatus((byte) 2);
        }
        wmsInFinishedProduct.setInType(StringUtils.isEmpty(wmsInFinishedProduct.getInType()) ? 0 : wmsInFinishedProduct.getInType());
        wmsInFinishedProduct.setStatus((byte) 1);
        wmsInFinishedProduct.setIsDelete((byte) 1);
        wmsInFinishedProduct.setCreateTime(new Date());
        wmsInFinishedProduct.setCreateUserId(user.getUserId());
        wmsInFinishedProduct.setOrganizationId(user.getOrganizationId());

        int result = wmsInFinishedProductMapper.insertUseGeneratedKeys(wmsInFinishedProduct);

        //履历
        WmsInHtFinishedProduct wmsInHtFinishedProduct = new WmsInHtFinishedProduct();
        BeanUtils.copyProperties(wmsInFinishedProduct, wmsInHtFinishedProduct);
        wmsInHtFinishedProductMapper.insertSelective(wmsInHtFinishedProduct);

        for (WmsInFinishedProductDet wmsInFinishedProductDet : wmsInFinishedProduct.getWmsInFinishedProductDetList()) {



            wmsInFinishedProductDet.setFinishedProductId(String.valueOf(wmsInFinishedProduct.getFinishedProductId()));
            wmsInFinishedProductDet.setOrganizationId(user.getOrganizationId());
            wmsInFinishedProductDet.setCreateTime(new Date());
            wmsInFinishedProductDet.setCreateUserId(user.getCreateUserId());
            wmsInFinishedProductDetMapper.insertSelective(wmsInFinishedProductDet);

            if (wmsInFinishedProduct.getProjectType().equals("dp")) {
                if (wmsInFinishedProduct.getInType() == 1) {
                    //半成品入库 直接计算库存。
                    //新增库存
                    SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
                    smtStorageInventory.setStorageId(wmsInFinishedProductDet.getStorageId());
                    smtStorageInventory.setMaterialId(wmsInFinishedProductDet.getMaterialId());
                    smtStorageInventory.setQuantity(wmsInFinishedProductDet.getInQuantity());

                    smtStorageInventory = storageInventoryFeignApi.add(smtStorageInventory).getData();

                    //增加库位库存明细
                    SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
                    smtStorageInventoryDet.setStorageInventoryId(smtStorageInventory.getStorageInventoryId());
                    smtStorageInventoryDet.setGodownEntry(wmsInFinishedProduct.getFinishedProductCode());
                    smtStorageInventoryDet.setMaterialQuantity(wmsInFinishedProductDet.getInQuantity());
                    storageInventoryFeignApi.add(smtStorageInventoryDet);
                }
            }
            if (wmsInFinishedProduct.getProjectType().equals("hf")) {
                //华峰内容

                //判断该栈板是否质检合格
                SearchQmsPdaInspection searchQmsPdaInspection = new SearchQmsPdaInspection();
                searchQmsPdaInspection.setPalletCode(wmsInFinishedProductDet.getPalletCode());
                List<QmsPdaInspectionDto> qmsPdaInspectionDtos = qmsFeignApi.findList(searchQmsPdaInspection).getData();
                if(StringUtils.isEmpty(qmsPdaInspectionDtos)){
                    throw new BizErrorException(wmsInFinishedProductDet.getPalletCode() + "未质检,不允许入库");
                }
                if(qmsPdaInspectionDtos.size() > 0){
                    if(qmsPdaInspectionDtos.get(0).getQualifiedState().equals("1")){
                        throw new BizErrorException(wmsInFinishedProductDet.getPalletCode() + "有不合格箱码");
                    }
                }
                // PDA直接提交 增加库存
                //存入库时栈板与包箱关系
                SearchMesPackageManagerListDTO searchMesPackageManagerListDTO = new SearchMesPackageManagerListDTO();
                searchMesPackageManagerListDTO.setIsFindChildren(true);
                searchMesPackageManagerListDTO.setBarcode(wmsInFinishedProductDet.getPalletCode());
                List<MesPackageManagerDTO> mesPackageManagerDTOS = pdaMesPackageManagerController.list(searchMesPackageManagerListDTO).getData();
                if (StringUtils.isEmpty(mesPackageManagerDTOS)) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100);
                }
                for (MesPackageManagerDTO mesPackageManagerDTO : mesPackageManagerDTOS) {
                    WmsInPalletCarton wmsInPalletCarton = new WmsInPalletCarton();
                    wmsInPalletCarton.setCartonCode(mesPackageManagerDTO.getBarCode());
                    wmsInPalletCarton.setPalletCode(wmsInFinishedProductDet.getPalletCode());
                    wmsInPalletCarton.setStatus((byte) 1);
                    wmsInPalletCarton.setIsDelete((byte) 1);
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
                smtStorageInventory.setMaterialId(wmsInFinishedProductDet.getMaterialId());
                smtStorageInventory.setQuantity(wmsInFinishedProductDet.getInQuantity());

                smtStorageInventory = storageInventoryFeignApi.add(smtStorageInventory).getData();

                //增加库位库存明细
                SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
                smtStorageInventoryDet.setStorageInventoryId(smtStorageInventory.getStorageInventoryId());
                smtStorageInventoryDet.setMaterialBarcodeCode(wmsInFinishedProductDet.getPalletCode());
                smtStorageInventoryDet.setGodownEntry(wmsInFinishedProduct.getFinishedProductCode());
                smtStorageInventoryDet.setMaterialQuantity(wmsInFinishedProductDet.getInQuantity());
                storageInventoryFeignApi.add(smtStorageInventoryDet);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int PDASubmit(WmsInFinishedProduct wmsInFinishedProduct) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(wmsInFinishedProduct.getWmsInFinishedProductDetList())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }


        for (WmsInFinishedProductDet wmsInFinishedProductDet : wmsInFinishedProduct.getWmsInFinishedProductDetList()) {

            if(wmsInFinishedProductDet.getInQuantity().add(wmsInFinishedProductDet.getCount()).compareTo(wmsInFinishedProductDet.getPlanInQuantity()) > 0){
                throw new BizErrorException("完工数量不能大于计划入库数量");
            }

            wmsInFinishedProductDet.setModifiedTime(new Date());
            wmsInFinishedProductDet.setModifiedUserId(user.getUserId());
            wmsInFinishedProductDet.setInTime(new Date());
            wmsInFinishedProductDet.setDeptId(user.getDeptId());
            if (!StringUtils.isEmpty(wmsInFinishedProductDet.getCount())) {
                Boolean flag2 = true;
                if (StringUtils.isEmpty(wmsInFinishedProductDet.getInQuantity())) {
                    if (wmsInFinishedProductDet.getPlanInQuantity().compareTo(wmsInFinishedProductDet.getCount()) == 0) {
                    } else {
                        flag2 = false;
                    }
                } else {
                    if (wmsInFinishedProductDet.getPlanInQuantity().compareTo(wmsInFinishedProductDet.getInQuantity().add(wmsInFinishedProductDet.getCount())) == 0) {
                    } else {
                        flag2 = false;
                    }
                }

                //新增库存
                SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
                smtStorageInventory.setStorageId(wmsInFinishedProductDet.getStorageId());
                smtStorageInventory.setMaterialId(wmsInFinishedProductDet.getMaterialId());
                smtStorageInventory.setQuantity(wmsInFinishedProductDet.getCount());
                smtStorageInventory = storageInventoryFeignApi.add(smtStorageInventory).getData();

                //增加库位库存明细
                SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
                smtStorageInventoryDet.setStorageInventoryId(smtStorageInventory.getStorageInventoryId());
//                smtStorageInventoryDet.setMaterialBarcodeCode(wmsInFinishedProductDet.getPalletCode());
                smtStorageInventoryDet.setGodownEntry(wmsInFinishedProduct.getFinishedProductCode());
                smtStorageInventoryDet.setMaterialQuantity(wmsInFinishedProductDet.getCount());
                storageInventoryFeignApi.add(smtStorageInventoryDet);

                wmsInFinishedProductDet.setInQuantity((StringUtils.isEmpty(wmsInFinishedProductDet.getInQuantity()) ? new BigDecimal(0) : wmsInFinishedProductDet.getInQuantity()).add(wmsInFinishedProductDet.getCount()));
                if (flag2) {//子表状态
                    wmsInFinishedProductDet.setInStatus((byte) 2);
                } else {
                    wmsInFinishedProductDet.setInStatus((byte) 1);
                }
                wmsInFinishedProductDetMapper.updateByPrimaryKeySelective(wmsInFinishedProductDet);

            }

            //更改条码状态
            List<BcmBarCodeDet> bcmBarCodeDets = new ArrayList<>();
            for (Integer id : wmsInFinishedProductDet.getBarCodeIdList()) {
                BcmBarCodeDet bcmBarCodeDet = new BcmBarCodeDet();
                bcmBarCodeDet.setBarCodeDetId(id.longValue());
                bcmBarCodeDets.add(bcmBarCodeDet);
            }
            bcmFeignApi.updateByContent(bcmBarCodeDets);

        }

        //获取最新子表数据判断主表状态
        Map<String, Object> map = new HashMap();
        map.put("finishedProductId", wmsInFinishedProduct.getFinishedProductId());
        List<WmsInFinishedProductDetDto> wmsInFinishedProductDets = wmsInFinishedProductDetMapper.findList(map);

        Boolean flag = true;
        for (WmsInFinishedProductDet wmsInFinishedProductDet : wmsInFinishedProductDets) {
            if (wmsInFinishedProductDet.getInStatus() != 2) {
                flag = false;
                break;
            }
        }

        if (flag) {//主表状态
            wmsInFinishedProduct.setInStatus((byte) 2);
        } else {
            wmsInFinishedProduct.setInStatus((byte) 1);
        }
        wmsInFinishedProduct.setModifiedTime(new Date());
        wmsInFinishedProduct.setModifiedUserId(user.getUserId());

        return wmsInFinishedProductMapper.updateByPrimaryKeySelective(wmsInFinishedProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsInFinishedProduct wmsInFinishedProduct) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsInFinishedProduct.setModifiedUserId(user.getUserId());
        wmsInFinishedProduct.setModifiedTime(new Date());

        //履历
        WmsInHtFinishedProduct wmsInHtFinishedProduct = new WmsInHtFinishedProduct();
        BeanUtils.copyProperties(wmsInFinishedProduct, wmsInHtFinishedProduct);
        wmsInHtFinishedProductMapper.insertSelective(wmsInHtFinishedProduct);

        if (wmsInFinishedProduct.getWmsInFinishedProductDetList() != null) {
            for (WmsInFinishedProductDet wmsInFinishedProductDet : wmsInFinishedProduct.getWmsInFinishedProductDetList()) {
                wmsInFinishedProductDet.setModifiedUserId(user.getUserId());
                wmsInFinishedProductDet.setModifiedTime(new Date());
                wmsInFinishedProductDetMapper.updateByPrimaryKeySelective(wmsInFinishedProductDet);
            }
        }
        return wmsInFinishedProductMapper.updateByPrimaryKeySelective(wmsInFinishedProduct);
    }
}
