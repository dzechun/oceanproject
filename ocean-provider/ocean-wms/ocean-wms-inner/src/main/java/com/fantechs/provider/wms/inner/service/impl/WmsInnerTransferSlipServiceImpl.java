package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDto;
import com.fantechs.common.base.general.entity.basic.BaseStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlipDet;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStorageInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStorageInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerTransferSlipDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtTransferSlipMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerTransferSlipDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerTransferSlipMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerTransferSlipService;
import org.springframework.beans.BeanUtils;
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
 * Created by leifengzhi on 2021/03/05.
 */
@Service
public class WmsInnerTransferSlipServiceImpl extends BaseService<WmsInnerTransferSlip> implements WmsInnerTransferSlipService {

    @Resource
    private WmsInnerTransferSlipMapper wmsInnerTransferSlipMapper;
    @Resource
    private WmsInnerTransferSlipDetMapper wmsInnerTransferSlipDetMapper;
    @Resource
    private WmsInnerHtTransferSlipMapper wmsInnerHtTransferSlipMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<WmsInnerTransferSlipDto> findList(Map<String, Object> map) {
        List<WmsInnerTransferSlipDto> wmsInnerTransferSlipDtos = wmsInnerTransferSlipMapper.findList(map);
        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDtos)){
            for (WmsInnerTransferSlipDto wmsInnerTransferSlipDto : wmsInnerTransferSlipDtos) {
                SearchWmsInnerTransferSlipDet searchWmsInnerTransferSlipDet = new SearchWmsInnerTransferSlipDet();
                searchWmsInnerTransferSlipDet.setTransferSlipId(wmsInnerTransferSlipDto.getTransferSlipId());
                List<WmsInnerTransferSlipDetDto> list = wmsInnerTransferSlipDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerTransferSlipDet));
                if (StringUtils.isNotEmpty(list)){
                    wmsInnerTransferSlipDto.setWmsInnerTransferSlipDetDtos(list);
                }
            }
        }
        return wmsInnerTransferSlipDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsInnerTransferSlip wmsInnerTransferSlip) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsInnerTransferSlip.setTransferSlipCode(CodeUtils.getId("DB"));
        wmsInnerTransferSlip.setCreateTime(new Date());
        wmsInnerTransferSlip.setCreateUserId(user.getUserId());
        wmsInnerTransferSlip.setModifiedTime(new Date());
        wmsInnerTransferSlip.setModifiedUserId(user.getUserId());
        wmsInnerTransferSlip.setTransferSlipTime(new Date());
        wmsInnerTransferSlip.setTransferSlipStatus((byte) 0);
        wmsInnerTransferSlip.setOrganizationId(user.getOrganizationId());
        if (wmsInnerTransferSlip.getOrderType() == 0){
            //库内调拨的操作人和处理人相同
            wmsInnerTransferSlip.setProcessorUserId(user.getUserId());
        }

        boolean waitForTransfer = false;
        int transferFinish = 0;
        //判断调拨单明细的调拨状态
        List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos1 = wmsInnerTransferSlip.getWmsInnerTransferSlipDetDtos();
        for (WmsInnerTransferSlipDetDto wmsInnerTransferSlipDetDto : wmsInnerTransferSlipDetDtos1) {
            //如果调拨明细中存在调拨中的单据，则修改调拨单状态为调拨中
            if (wmsInnerTransferSlipDetDto.getTransferSlipStatus() == 1){
                waitForTransfer = true;
                continue;
            }

            //调拨单明细为调拨完成，则修改库存信息
            if (wmsInnerTransferSlipDetDto.getTransferSlipStatus() == 2){
                transferFinish++;
            }
        }
        //存在调拨中的单据
        if (waitForTransfer){
            wmsInnerTransferSlip.setTransferSlipStatus((byte) 1);
        }
        if (transferFinish == wmsInnerTransferSlipDetDtos1.size()){
            //若所有调拨单都属于调拨完成状态，则修改调拨单状态为调拨完成
            wmsInnerTransferSlip.setTransferSlipStatus((byte) 2);
        }
        //新增调拨单
        int i = wmsInnerTransferSlipMapper.insertUseGeneratedKeys(wmsInnerTransferSlip);

        WmsInnerHtTransferSlip wmsInnerHtTransferSlip = new WmsInnerHtTransferSlip();
        BeanUtils.copyProperties(wmsInnerTransferSlip,wmsInnerHtTransferSlip);
        wmsInnerHtTransferSlipMapper.insertSelective(wmsInnerHtTransferSlip);

        //删除原有调拨单明细
        Example example2 = new Example(WmsInnerTransferSlipDet.class);
        Example.Criteria criteria4 = example2.createCriteria();
        criteria4.andEqualTo("transferSlipId",wmsInnerTransferSlip.getTransferSlipId());
        wmsInnerTransferSlipDetMapper.deleteByExample(example2);

        List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos = wmsInnerTransferSlip.getWmsInnerTransferSlipDetDtos();
        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDetDtos)){
            ArrayList<WmsInnerTransferSlipDet> wmsInnerTransferSlipDets = new ArrayList<>();
            for (WmsInnerTransferSlipDetDto wmsInnerTransferSlipDetDto : wmsInnerTransferSlipDetDtos) {

                //判断是否已经存在这个栈板的调拨计划
                Example example = new Example(WmsInnerTransferSlipDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("palletCode",wmsInnerTransferSlipDetDto.getPalletCode());
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("transferSlipStatus",0)
                        .orEqualTo("transferSlipStatus",1);
                example.and(criteria1);
                List<WmsInnerTransferSlipDet> wmsInnerTransferSlipDets1 = wmsInnerTransferSlipDetMapper.selectByExample(example);
                if (StringUtils.isNotEmpty(wmsInnerTransferSlipDets1)){
                    throw new BizErrorException("该栈板的调拨计划已经存在");
                }

                //如果是调拨完成的单据则修改库存信息
                if (wmsInnerTransferSlipDetDto.getTransferSlipStatus() == 2){
                    SearchBaseStorageMaterial searchBaseStorageMaterial = new SearchBaseStorageMaterial();
                    searchBaseStorageMaterial.setStorageId(wmsInnerTransferSlipDetDto.getInStorageId());
                    List<BaseStorageMaterial> baseStorageMaterials = baseFeignApi.findStorageMaterialList(searchBaseStorageMaterial).getData();
                    if (StringUtils.isNotEmpty(baseStorageMaterials)){
                        if (baseStorageMaterials.get(0).getMaterialId() != wmsInnerTransferSlipDetDto.getMaterialId()){
                            throw new BizErrorException("调入储位已存在其他物料");
                        }
                    }

                    //移除调出储位的库存明细信息
                    SearchWmsInnerStorageInventoryDet searchWmsInnerStorageInventoryDet = new SearchWmsInnerStorageInventoryDet();
                    searchWmsInnerStorageInventoryDet.setMaterialBarcodeCode(wmsInnerTransferSlipDetDto.getPalletCode());
                    List<WmsInnerStorageInventoryDetDto> wmsInnerStorageInventoryDetDtos = baseFeignApi.findStorageInventoryDetList(searchWmsInnerStorageInventoryDet).getData();
                    if (StringUtils.isEmpty(wmsInnerStorageInventoryDetDtos)){
                        throw new BizErrorException("无法获取到储位的库存信息");
                    }
                    WmsInnerStorageInventoryDetDto wmsInnerStorageInventoryDetDto = wmsInnerStorageInventoryDetDtos.get(0);
                    baseFeignApi.deleteStorageInventoryDet(String.valueOf(wmsInnerStorageInventoryDetDto.getStorageInventoryDetId()));

                    //修改储位库存数据
                    SearchWmsInnerStorageInventory searchWmsInnerStorageInventory = new SearchWmsInnerStorageInventory();
                    searchWmsInnerStorageInventory.setStorageInventoryId(wmsInnerStorageInventoryDetDto.getStorageInventoryId());
                    List<WmsInnerStorageInventoryDto> smtStorageInventoryDtos = baseFeignApi.findList(searchWmsInnerStorageInventory).getData();
                    if (StringUtils.isEmpty(wmsInnerStorageInventoryDetDtos)){
                        throw new BizErrorException("获取储位库存数失败");
                    }
                    WmsInnerStorageInventoryDto smtStorageInventoryDto = smtStorageInventoryDtos.get(0);
                    smtStorageInventoryDto.setQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());
                    baseFeignApi.update(smtStorageInventoryDto);

                    //删除储位栈板关系
                    SearchSmtStoragePallet searchSmtStoragePallet = new SearchSmtStoragePallet();
                    searchSmtStoragePallet.setPalletCode(wmsInnerTransferSlipDetDto.getPalletCode());
                    List<SmtStoragePalletDto> smtStoragePalletDtos = baseFeignApi.findList(searchSmtStoragePallet).getData();
                    if (StringUtils.isEmpty(smtStoragePalletDtos)){
                        throw new BizErrorException("无法获取到储位栈板关系");
                    }
                    baseFeignApi.deleteSmtStoragePallet(String.valueOf(smtStoragePalletDtos.get(0).getStoragePalletId()));

                    if (wmsInnerTransferSlip.getOrderType() == 0){
                        //新增储位栈板关系
                        SmtStoragePallet smtStoragePallet = new SmtStoragePallet();
                        smtStoragePallet.setPalletCode(wmsInnerTransferSlipDetDto.getPalletCode());
                        smtStoragePallet.setStorageId(wmsInnerTransferSlipDetDto.getInStorageId());
                        smtStoragePallet.setPalletType((byte) 0);
                        smtStoragePallet.setIsBinding((byte) 1);
                        smtStoragePallet.setStatus((byte) 1);
                        smtStoragePallet.setOrganizationId(user.getOrganizationId());
                        smtStoragePallet.setCreateTime(new Date());
                        smtStoragePallet.setCreateUserId(user.getUserId());
                        smtStoragePallet.setModifiedTime(new Date());
                        smtStoragePallet.setModifiedUserId(user.getUserId());
                        baseFeignApi.add(smtStoragePallet);

                        //获取调入储位的储位库存数据
                        SearchWmsInnerStorageInventory searchWmsInnerStorageInventory1 = new SearchWmsInnerStorageInventory();
                        searchWmsInnerStorageInventory1.setStorageId(wmsInnerTransferSlipDetDto.getInStorageId());
                        List<WmsInnerStorageInventoryDto> smtStorageInventoryDtos1 = baseFeignApi.findList(searchWmsInnerStorageInventory1).getData();
                        if (StringUtils.isNotEmpty(searchWmsInnerStorageInventory1)){
                            //不为空则更新库存数量
                            WmsInnerStorageInventoryDto smtStorageInventoryDto1 = smtStorageInventoryDtos1.get(0);
                            smtStorageInventoryDto1.setQuantity(smtStorageInventoryDto1.getQuantity().subtract(wmsInnerTransferSlipDetDto.getRealityTotalQty()));
                            WmsInnerStorageInventory wmsInnerStorageInventory = new WmsInnerStorageInventory();
                            BeanUtils.copyProperties(smtStorageInventoryDto1, wmsInnerStorageInventory);
                            wmsInnerStorageInventory.setModifiedTime(new Date());
                            wmsInnerStorageInventory.setModifiedUserId(user.getUserId());
                            baseFeignApi.update(wmsInnerStorageInventory);

                            //新增储位库存明细
                            WmsInnerStorageInventoryDet smtStorageInventoryDet = new WmsInnerStorageInventoryDet();
                            smtStorageInventoryDet.setStorageInventoryId(wmsInnerStorageInventory.getStorageInventoryId());
                            smtStorageInventoryDet.setMaterialBarcodeCode(wmsInnerTransferSlipDetDto.getPalletCode());
                            smtStorageInventoryDet.setGodownEntry(wmsInnerStorageInventoryDetDto.getGodownEntry());
                            smtStorageInventoryDet.setProductionCode(wmsInnerStorageInventoryDetDto.getProductionCode());
                            smtStorageInventoryDet.setProductionDate(wmsInnerStorageInventoryDetDto.getProductionDate());
                            smtStorageInventoryDet.setSupplierId(wmsInnerStorageInventoryDetDto.getSupplierId());
                            smtStorageInventoryDet.setCreateTime(new Date());
                            smtStorageInventoryDet.setCreateUserId(user.getUserId());
                            smtStorageInventoryDet.setModifiedTime(new Date());
                            smtStorageInventoryDet.setModifiedUserId(user.getUserId());
                            smtStorageInventoryDet.setMaterialQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());
                            baseFeignApi.add(smtStorageInventoryDet);
                        }else {
                            //新增储位库存数据
                            WmsInnerStorageInventory wmsInnerStorageInventory = new WmsInnerStorageInventory();
                            wmsInnerStorageInventory.setStorageId(wmsInnerTransferSlipDetDto.getInStorageId());
                            wmsInnerStorageInventory.setMaterialId(wmsInnerTransferSlipDetDto.getMaterialId());
                            wmsInnerStorageInventory.setQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());
                            wmsInnerStorageInventory.setCreateTime(new Date());
                            wmsInnerStorageInventory.setCreateUserId(user.getUserId());
                            wmsInnerStorageInventory.setModifiedTime(new Date());
                            wmsInnerStorageInventory.setModifiedUserId(user.getUserId());
                            wmsInnerStorageInventory = baseFeignApi.add(wmsInnerStorageInventory).getData();

                            //新增储位库存明细
                            WmsInnerStorageInventoryDet smtStorageInventoryDet = new WmsInnerStorageInventoryDet();
                            smtStorageInventoryDet.setStorageInventoryId(wmsInnerStorageInventory.getStorageInventoryId());
                            smtStorageInventoryDet.setMaterialBarcodeCode(wmsInnerTransferSlipDetDto.getPalletCode());
                            smtStorageInventoryDet.setGodownEntry(wmsInnerStorageInventoryDetDto.getGodownEntry());
                            smtStorageInventoryDet.setProductionCode(wmsInnerStorageInventoryDetDto.getProductionCode());
                            smtStorageInventoryDet.setProductionDate(wmsInnerStorageInventoryDetDto.getProductionDate());
                            smtStorageInventoryDet.setSupplierId(wmsInnerStorageInventoryDetDto.getSupplierId());
                            smtStorageInventoryDet.setCreateTime(new Date());
                            smtStorageInventoryDet.setCreateUserId(user.getUserId());
                            smtStorageInventoryDet.setModifiedTime(new Date());
                            smtStorageInventoryDet.setModifiedUserId(user.getUserId());
                            smtStorageInventoryDet.setMaterialQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());
                            baseFeignApi.add(smtStorageInventoryDet);
                        }

                    }


                }

                wmsInnerTransferSlipDetDto.setCreateTime(new Date());
                wmsInnerTransferSlipDetDto.setCreateUserId(user.getUserId());
                wmsInnerTransferSlipDetDto.setModifiedUserId(user.getUserId());
                wmsInnerTransferSlipDetDto.setModifiedTime(new Date());
                if (StringUtils.isEmpty(wmsInnerTransferSlipDetDto.getTransferSlipStatus())){
                    wmsInnerTransferSlipDetDto.setTransferSlipStatus((byte) 0);
                }
                wmsInnerTransferSlipDetDto.setTransferSlipId(wmsInnerTransferSlip.getTransferSlipId());
                wmsInnerTransferSlipDets.add(wmsInnerTransferSlipDetDto);
            }
            if (StringUtils.isNotEmpty(wmsInnerTransferSlipDets)){
                wmsInnerTransferSlipDetMapper.insertList(wmsInnerTransferSlipDets);
            }
        }else {
            throw new BizErrorException("调拨内容不能为空");
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsInnerTransferSlip wmsInnerTransferSlip) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsInnerTransferSlip.setTransferSlipTime(new Date());
        wmsInnerTransferSlip.setOrganizationId(user.getOrganizationId());
        if (wmsInnerTransferSlip.getOrderType() == 0){
            //库内调拨的操作人和处理人相同
            wmsInnerTransferSlip.setProcessorUserId(user.getUserId());
        }

        //判断调拨单明细的状态
        List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos = wmsInnerTransferSlip.getWmsInnerTransferSlipDetDtos();
        if (StringUtils.isEmpty(wmsInnerTransferSlipDetDtos)){
            throw new BizErrorException("调拨内容不能为空");
        }

        //删除原有调拨单明细
        Example example2 = new Example(WmsInnerTransferSlipDet.class);
        Example.Criteria criteria4 = example2.createCriteria();
        criteria4.andEqualTo("transferSlipId",wmsInnerTransferSlip.getTransferSlipId());
        wmsInnerTransferSlipDetMapper.deleteByExample(example2);

        boolean waitForTransfer = false;
        int transferFinish = 0;
        for (WmsInnerTransferSlipDetDto wmsInnerTransferSlipDetDto : wmsInnerTransferSlipDetDtos) {
            //如果调拨明细中存在调拨中的单据，则修改调拨单状态为调拨中
            if (wmsInnerTransferSlipDetDto.getTransferSlipStatus() == 1){
                waitForTransfer = true;
                continue;
            }

            //调拨单明细为调拨完成，则修改库存信息
            if (wmsInnerTransferSlipDetDto.getTransferSlipStatus() == 2){
                transferFinish++;

                //判断是否已经存在这个栈板的调拨计划
                Example example = new Example(WmsInnerTransferSlipDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("palletCode",wmsInnerTransferSlipDetDto.getPalletCode());
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("transferSlipStatus",0)
                        .orEqualTo("transferSlipStatus",1);
                example.and(criteria1);
                WmsInnerTransferSlipDet wmsInnerTransferSlipDet1 = wmsInnerTransferSlipDetMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(wmsInnerTransferSlipDet1)){
                    throw new BizErrorException("该栈板的调拨计划已经存在");
                }

                //移除调出储位的库存明细信息
                SearchWmsInnerStorageInventoryDet searchWmsInnerStorageInventoryDet = new SearchWmsInnerStorageInventoryDet();
                searchWmsInnerStorageInventoryDet.setMaterialBarcodeCode(wmsInnerTransferSlipDetDto.getPalletCode());
                List<WmsInnerStorageInventoryDetDto> wmsInnerStorageInventoryDetDtos = baseFeignApi.findStorageInventoryDetList(searchWmsInnerStorageInventoryDet).getData();
                if (StringUtils.isNotEmpty(wmsInnerStorageInventoryDetDtos)){
                    WmsInnerStorageInventoryDetDto wmsInnerStorageInventoryDetDto = wmsInnerStorageInventoryDetDtos.get(0);
                    baseFeignApi.deleteStorageInventoryDet(String.valueOf(wmsInnerStorageInventoryDetDto.getStorageInventoryDetId()));

                    //修改储位库存数据
                    SearchWmsInnerStorageInventory searchWmsInnerStorageInventory = new SearchWmsInnerStorageInventory();
                    searchWmsInnerStorageInventory.setStorageInventoryId(wmsInnerStorageInventoryDetDto.getStorageInventoryId());
                    List<WmsInnerStorageInventoryDto> smtStorageInventoryDtos = baseFeignApi.findList(searchWmsInnerStorageInventory).getData();
                    if (StringUtils.isEmpty(wmsInnerStorageInventoryDetDtos)){
                        throw new BizErrorException("获取储位库存数失败");
                    }
                    WmsInnerStorageInventoryDto smtStorageInventoryDto = smtStorageInventoryDtos.get(0);
                    smtStorageInventoryDto.setQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());

                    //删除储位栈板关系
                    SearchSmtStoragePallet searchSmtStoragePallet = new SearchSmtStoragePallet();
                    searchSmtStoragePallet.setPalletCode(wmsInnerTransferSlipDetDto.getPalletCode());
                    List<SmtStoragePalletDto> smtStoragePalletDtos = baseFeignApi.findList(searchSmtStoragePallet).getData();
                    if (StringUtils.isEmpty(smtStoragePalletDtos)){
                        throw new BizErrorException("无法获取到储位栈板关系");
                    }
                    baseFeignApi.deleteSmtStoragePallet(String.valueOf(smtStoragePalletDtos.get(0).getStoragePalletId()));

                    if (wmsInnerTransferSlip.getOrderType() == 0){
                        //新增储位栈板关系
                        SmtStoragePallet smtStoragePallet = new SmtStoragePallet();
                        smtStoragePallet.setPalletCode(wmsInnerTransferSlipDetDto.getPalletCode());
                        smtStoragePallet.setStorageId(wmsInnerTransferSlipDetDto.getInStorageId());
                        smtStoragePallet.setPalletType((byte) 0);
                        smtStoragePallet.setIsBinding((byte) 1);
                        smtStoragePallet.setStatus((byte) 1);
                        smtStoragePallet.setOrganizationId(user.getOrganizationId());
                        smtStoragePallet.setCreateTime(new Date());
                        smtStoragePallet.setCreateUserId(user.getUserId());
                        smtStoragePallet.setModifiedTime(new Date());
                        smtStoragePallet.setModifiedUserId(user.getUserId());
                        baseFeignApi.add(smtStoragePallet);

                        //获取调入储位的储位库存数据
                        SearchWmsInnerStorageInventory searchWmsInnerStorageInventory1 = new SearchWmsInnerStorageInventory();
                        searchWmsInnerStorageInventory1.setStorageId(wmsInnerTransferSlipDetDto.getInStorageId());
                        List<WmsInnerStorageInventoryDto> smtStorageInventoryDtos1 = baseFeignApi.findList(searchWmsInnerStorageInventory1).getData();
                        if (StringUtils.isNotEmpty(smtStorageInventoryDtos1)){
                            //不为空则更新库存数量
                            WmsInnerStorageInventoryDto smtStorageInventoryDto1 = smtStorageInventoryDtos1.get(0);
                            smtStorageInventoryDto1.setQuantity(smtStorageInventoryDto1.getQuantity().subtract(wmsInnerTransferSlipDetDto.getRealityTotalQty()));
                            WmsInnerStorageInventory wmsInnerStorageInventory = new WmsInnerStorageInventory();
                            BeanUtils.copyProperties(smtStorageInventoryDto1, wmsInnerStorageInventory);
                            wmsInnerStorageInventory.setModifiedTime(new Date());
                            wmsInnerStorageInventory.setModifiedUserId(user.getUserId());
                            baseFeignApi.update(wmsInnerStorageInventory);

                            //新增储位库存明细
                            WmsInnerStorageInventoryDet smtStorageInventoryDet = new WmsInnerStorageInventoryDet();
                            smtStorageInventoryDet.setStorageInventoryId(wmsInnerStorageInventory.getStorageInventoryId());
                            smtStorageInventoryDet.setMaterialBarcodeCode(wmsInnerTransferSlipDetDto.getPalletCode());
                            smtStorageInventoryDet.setGodownEntry(wmsInnerStorageInventoryDetDto.getGodownEntry());
                            smtStorageInventoryDet.setProductionCode(wmsInnerStorageInventoryDetDto.getProductionCode());
                            smtStorageInventoryDet.setProductionDate(wmsInnerStorageInventoryDetDto.getProductionDate());
                            smtStorageInventoryDet.setSupplierId(wmsInnerStorageInventoryDetDto.getSupplierId());
                            smtStorageInventoryDet.setCreateTime(new Date());
                            smtStorageInventoryDet.setCreateUserId(user.getUserId());
                            smtStorageInventoryDet.setModifiedTime(new Date());
                            smtStorageInventoryDet.setModifiedUserId(user.getUserId());
                            smtStorageInventoryDet.setMaterialQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());
                            baseFeignApi.add(smtStorageInventoryDet);
                        }else {
                            //新增储位库存数据
                            WmsInnerStorageInventory wmsInnerStorageInventory = new WmsInnerStorageInventory();
                            wmsInnerStorageInventory.setStorageId(wmsInnerTransferSlipDetDto.getInStorageId());
                            wmsInnerStorageInventory.setMaterialId(wmsInnerTransferSlipDetDto.getMaterialId());
                            wmsInnerStorageInventory.setQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());
                            wmsInnerStorageInventory.setCreateTime(new Date());
                            wmsInnerStorageInventory.setCreateUserId(user.getUserId());
                            wmsInnerStorageInventory.setModifiedTime(new Date());
                            wmsInnerStorageInventory.setModifiedUserId(user.getUserId());
                            wmsInnerStorageInventory = baseFeignApi.add(wmsInnerStorageInventory).getData();

                            //新增储位库存明细
                            WmsInnerStorageInventoryDet smtStorageInventoryDet = new WmsInnerStorageInventoryDet();
                            smtStorageInventoryDet.setStorageInventoryId(wmsInnerStorageInventory.getStorageInventoryId());
                            smtStorageInventoryDet.setMaterialBarcodeCode(wmsInnerTransferSlipDetDto.getPalletCode());
                            smtStorageInventoryDet.setGodownEntry(wmsInnerStorageInventoryDetDto.getGodownEntry());
                            smtStorageInventoryDet.setProductionCode(wmsInnerStorageInventoryDetDto.getProductionCode());
                            smtStorageInventoryDet.setProductionDate(wmsInnerStorageInventoryDetDto.getProductionDate());
                            smtStorageInventoryDet.setSupplierId(wmsInnerStorageInventoryDetDto.getSupplierId());
                            smtStorageInventoryDet.setCreateTime(new Date());
                            smtStorageInventoryDet.setCreateUserId(user.getUserId());
                            smtStorageInventoryDet.setModifiedTime(new Date());
                            smtStorageInventoryDet.setModifiedUserId(user.getUserId());
                            smtStorageInventoryDet.setMaterialQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());
                            baseFeignApi.add(smtStorageInventoryDet);
                        }
                    }
                }
            }

        }
        //存在调拨中的单据
        if (waitForTransfer){
            wmsInnerTransferSlip.setTransferSlipStatus((byte) 1);
        }
        if (transferFinish == wmsInnerTransferSlipDetDtos.size()){
            //若所有调拨单都属于调拨完成状态，则修改调拨单状态为调拨完成
            wmsInnerTransferSlip.setTransferSlipStatus((byte) 2);
        }

        //更新调拨单
        int i = wmsInnerTransferSlipMapper.updateByPrimaryKeySelective(wmsInnerTransferSlip);

        WmsInnerHtTransferSlip wmsInnerHtTransferSlip = new WmsInnerHtTransferSlip();
        BeanUtils.copyProperties(wmsInnerTransferSlip,wmsInnerHtTransferSlip);
        wmsInnerHtTransferSlipMapper.insertSelective(wmsInnerHtTransferSlip);

        //删除原调拨单明细
        Example example = new Example(WmsInnerTransferSlipDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("transferSlipId",wmsInnerTransferSlip.getTransferSlipId());
        wmsInnerTransferSlipDetMapper.deleteByExample(example);

        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDetDtos)){
            ArrayList<WmsInnerTransferSlipDet> wmsInnerTransferSlipDets = new ArrayList<>();
            for (WmsInnerTransferSlipDet wmsInnerTransferSlipDet : wmsInnerTransferSlipDetDtos) {

                wmsInnerTransferSlipDet.setTransferSlipId(wmsInnerTransferSlip.getTransferSlipId());
                wmsInnerTransferSlipDet.setModifiedTime(new Date());
                wmsInnerTransferSlipDet.setModifiedUserId(user.getUserId());
                wmsInnerTransferSlipDets.add(wmsInnerTransferSlipDet);
            }
            if (StringUtils.isNotEmpty(wmsInnerTransferSlipDets)){
                wmsInnerTransferSlipDetMapper.insertList(wmsInnerTransferSlipDets);
            }

        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(WmsInnerTransferSlipDet.class);
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            example.clear();
            WmsInnerTransferSlip wmsInnerTransferSlip = wmsInnerTransferSlipMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerTransferSlip)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsInnerHtTransferSlip wmsInnerHtTransferSlip = new WmsInnerHtTransferSlip();
            BeanUtils.copyProperties(wmsInnerTransferSlip,wmsInnerHtTransferSlip);
            wmsInnerHtTransferSlipMapper.insertSelective(wmsInnerHtTransferSlip);

            //删除调拨单明细
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("transferSlipId",wmsInnerTransferSlip.getTransferSlipId());
            wmsInnerTransferSlipDetMapper.deleteByExample(example);
        }

        //删除调拨单
        return wmsInnerTransferSlipMapper.deleteByIds(ids);

    }
}
