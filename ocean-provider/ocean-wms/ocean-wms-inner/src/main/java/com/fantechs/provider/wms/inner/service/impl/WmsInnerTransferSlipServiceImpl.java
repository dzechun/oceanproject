package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.*;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventoryDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlipDet;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtTransferSlip;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerTransferSlipDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtTransferSlipMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerTransferSlipDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerTransferSlipMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerTransferSlipService;
import io.micrometer.core.instrument.search.Search;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    private StorageInventoryFeignApi storageInventoryFeignApi;
    @Resource
    private BasicFeignApi basicFeignApi;

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
    public int save(WmsInnerTransferSlip wmsInnerTransferSlip) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsInnerTransferSlip.setTransferSlipCode(CodeUtils.getId("DB—"));
        wmsInnerTransferSlip.setCreateTime(new Date());
        wmsInnerTransferSlip.setCreateUserId(user.getUserId());
        wmsInnerTransferSlip.setModifiedTime(new Date());
        wmsInnerTransferSlip.setModifiedUserId(user.getUserId());
        wmsInnerTransferSlip.setTransferSlipTime(new Date());
        if (StringUtils.isEmpty(wmsInnerTransferSlip.getTransferSlipStatus())){
            wmsInnerTransferSlip.setTransferSlipStatus((byte) 0);
        }
        wmsInnerTransferSlip.setOrganizationId(user.getOrganizationId());
        if (wmsInnerTransferSlip.getOrderType() == 0){
            //库内调拨的操作人和处理人相同
            wmsInnerTransferSlip.setProcessorUserId(user.getUserId());
        }

        //新增调拨单
        int i = wmsInnerTransferSlipMapper.insertUseGeneratedKeys(wmsInnerTransferSlip);

        WmsInnerHtTransferSlip wmsInnerHtTransferSlip = new WmsInnerHtTransferSlip();
        BeanUtils.copyProperties(wmsInnerTransferSlip,wmsInnerHtTransferSlip);
        wmsInnerHtTransferSlipMapper.insertSelective(wmsInnerHtTransferSlip);

        List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos = wmsInnerTransferSlip.getWmsInnerTransferSlipDetDtos();
        if (StringUtils.isNotEmpty(wmsInnerTransferSlipDetDtos)){
            ArrayList<WmsInnerTransferSlipDet> wmsInnerTransferSlipDets = new ArrayList<>();
            for (WmsInnerTransferSlipDet wmsInnerTransferSlipDet : wmsInnerTransferSlipDetDtos) {
                if (StringUtils.isEmpty(wmsInnerTransferSlipDet.getPlanCartonQty())){
                    throw new BizErrorException("计划调拨箱数必须大于0");
                }

                wmsInnerTransferSlipDet.setCreateTime(new Date());
                wmsInnerTransferSlipDet.setCreateUserId(user.getUserId());
                wmsInnerTransferSlipDet.setModifiedUserId(user.getUserId());
                wmsInnerTransferSlipDet.setModifiedTime(new Date());
                if (StringUtils.isEmpty(wmsInnerTransferSlipDet.getTransferSlipStatus())){
                    wmsInnerTransferSlipDet.setTransferSlipStatus((byte) 0);
                }
                wmsInnerTransferSlipDet.setTransferSlipId(wmsInnerTransferSlip.getTransferSlipId());
                wmsInnerTransferSlipDets.add(wmsInnerTransferSlipDet);
            }
            if (StringUtils.isNotEmpty(wmsInnerTransferSlipDets)){
                wmsInnerTransferSlipDetMapper.insertList(wmsInnerTransferSlipDets);
            }
        }

        return i;
    }

    @Override
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
        boolean waitForTransfer = false;
        int transferFinish = 0;
        //判断调入的储位是否存在其他物料
        SearchSmtStorageMaterial searchSmtStorageMaterial = new SearchSmtStorageMaterial();
        for (WmsInnerTransferSlipDetDto wmsInnerTransferSlipDetDto : wmsInnerTransferSlipDetDtos) {
            //如果调拨明细中存在调拨中的单据，则修改调拨单状态为待调拨
            if (wmsInnerTransferSlipDetDto.getTransferSlipStatus() == 1){
                waitForTransfer = true;
                continue;
            }
            if (wmsInnerTransferSlipDetDto.getTransferSlipStatus() == 2){
                transferFinish++;
            }


            searchSmtStorageMaterial.setStorageId(wmsInnerTransferSlipDetDto.getInStorageId());
            List<SmtStorageMaterial> smtStorageMaterials = basicFeignApi.findStorageMaterialList(searchSmtStorageMaterial).getData();
            if (StringUtils.isNotEmpty(smtStorageMaterials)){
                if (smtStorageMaterials.get(0).getMaterialId() != wmsInnerTransferSlipDetDto.getMaterialId()){
                    throw new BizErrorException("调入储位已存在其他物料");
                }
            }

            //移除调出储位的库存明细信息
            SearchSmtStorageInventoryDet searchSmtStorageInventoryDet = new SearchSmtStorageInventoryDet();
            searchSmtStorageInventoryDet.setMaterialBarcodeCode(wmsInnerTransferSlipDetDto.getPalletCode());
            List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtos = storageInventoryFeignApi.findStorageInventoryDetList(searchSmtStorageInventoryDet).getData();
            SmtStorageInventoryDetDto smtStorageInventoryDetDto = smtStorageInventoryDetDtos.get(0);
            storageInventoryFeignApi.deleteStorageInventoryDet(String.valueOf(smtStorageInventoryDetDto.getStorageInventoryDetId()));

            //修改储位库存数据
            SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
            searchSmtStorageInventory.setStorageInventoryId(smtStorageInventoryDetDto.getStorageInventoryId());
            List<SmtStorageInventoryDto> smtStorageInventoryDtos = storageInventoryFeignApi.findList(searchSmtStorageInventory).getData();
            if (StringUtils.isEmpty(smtStorageInventoryDetDtos)){
                throw new BizErrorException("获取储位库存数失败");
            }
            SmtStorageInventoryDto smtStorageInventoryDto = smtStorageInventoryDtos.get(0);
            smtStorageInventoryDto.setQuantity(wmsInnerTransferSlipDetDto.getRealityTotalQty());

            //删除储位栈板关系
            SearchSmtStoragePallet searchSmtStoragePallet = new SearchSmtStoragePallet();
            searchSmtStoragePallet.setPalletCode(wmsInnerTransferSlipDetDto.getPalletCode());
            List<SmtStoragePalletDto> smtStoragePalletDtos = storageInventoryFeignApi.findList(searchSmtStoragePallet).getData();
            if (StringUtils.isEmpty(smtStoragePalletDtos)){
                throw new BizErrorException("无法获取到储位栈板关系");
            }
            storageInventoryFeignApi.deleteSmtStoragePallet(String.valueOf(smtStoragePalletDtos.get(0).getStoragePalletId()));

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
            storageInventoryFeignApi.add(smtStoragePallet);
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

        //对调拨完成的调拨单进行操作
        if (wmsInnerTransferSlip.getTransferSlipStatus() == 2){

        }

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
                if (StringUtils.isEmpty(wmsInnerTransferSlipDet.getPlanCartonQty())){
                    throw new BizErrorException("计划调拨箱数必须大于0");
                }

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
