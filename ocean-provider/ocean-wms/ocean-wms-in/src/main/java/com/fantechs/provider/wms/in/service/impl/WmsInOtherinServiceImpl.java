package com.fantechs.provider.wms.in.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDto;
import com.fantechs.common.base.general.entity.wms.in.*;
import com.fantechs.common.base.general.entity.wms.in.history.WmsInHtFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.history.WmsInHtOtherin;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.in.mapper.WmsInHtOtherinMapper;
import com.fantechs.provider.wms.in.mapper.WmsInOtherinDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInOtherinMapper;
import com.fantechs.provider.wms.in.mapper.WmsInPalletCartonMapper;
import com.fantechs.provider.wms.in.service.WmsInOtherinService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/12.
 */
@Service
public class WmsInOtherinServiceImpl  extends BaseService<WmsInOtherin> implements WmsInOtherinService {

    @Resource
    private WmsInOtherinMapper wmsInOtherinMapper;
    @Resource
    private WmsInHtOtherinMapper wmsInHtOtherinMapper;
    @Resource
    private WmsInOtherinDetMapper wmsInOtherinDetMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;
    @Resource
    private WmsInPalletCartonMapper wmsInPalletCartonMapper;

    @Override
    public List<WmsInOtherinDto> findList(Map<String, Object> map) {
        return wmsInOtherinMapper.findList(map);
    }

    @Override
    public List<WmsInOtherinDto> findHtList(Map<String, Object> map) {
        return wmsInHtOtherinMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsInOtherin wmsInOtherin) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(wmsInOtherin.getWmsInOtherinDetList())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsInOtherin.setOtherinCode(CodeUtils.getId("QTRK-"));
        wmsInOtherin.setInType(StringUtils.isEmpty(wmsInOtherin.getInType()) ? 0 :wmsInOtherin.getInType());
        wmsInOtherin.setInStatus(StringUtils.isEmpty(wmsInOtherin.getInStatus()) ? 0 :wmsInOtherin.getInStatus());
        wmsInOtherin.setStatus((byte)1);
        wmsInOtherin.setIsDelete((byte)1);
        wmsInOtherin.setCreateTime(new Date());
        wmsInOtherin.setCreateUserId(user.getUserId());

        int result = wmsInOtherinMapper.insertUseGeneratedKeys(wmsInOtherin);

        //履历
        WmsInHtOtherin wmsInHtOtherin = new WmsInHtOtherin();
        BeanUtils.copyProperties(wmsInOtherin,wmsInHtOtherin);
        wmsInHtOtherinMapper.insertSelective(wmsInHtOtherin);

        //查询工单信息
//        SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(wmsInFinishedProduct.getWorkOrderId());

        for (WmsInOtherinDet wmsInOtherinDet : wmsInOtherin.getWmsInOtherinDetList()) {

            wmsInOtherinDet.setOtherinId(String.valueOf(result));
            wmsInOtherinDet.setOrganizationId(user.getOrganizationId());
            wmsInOtherinDet.setCreateTime(new Date());
            wmsInOtherinDet.setCreateUserId(user.getCreateUserId());
            wmsInOtherinDetMapper.insertSelective(wmsInOtherinDet);

            //存入库时栈板与包箱关系
            /*for(){
                //循环栈板上的包装数，等栈板打标模块出来后才能写
            }*/
            WmsInPalletCarton wmsInPalletCarton = new WmsInPalletCarton();
//            wmsInPalletCarton.setCartonCode();
            wmsInPalletCarton.setPalletCode(wmsInOtherinDet.getPalletCode());
            wmsInPalletCarton.setStatus((byte)1);
            wmsInPalletCarton.setIsDelete((byte)1);
            wmsInPalletCarton.setCreateTime(new Date());
            wmsInPalletCarton.setCreateUserId(user.getCreateUserId());
            //栈板与包箱关系表
            wmsInPalletCartonMapper.insertSelective(wmsInPalletCarton);

            SmtStoragePallet smtStoragePallet = new SmtStoragePallet();
            smtStoragePallet.setPalletCode(wmsInOtherinDet.getPalletCode());
            smtStoragePallet.setStorageId(wmsInOtherinDet.getStorageId());
            //存储位与栈板关系表smt
            storageInventoryFeignApi.add(smtStoragePallet);

            //查询储位库存表，有库存累加，无库存新增
            SearchSmtStorageInventory searchSmtStorageInventory = new SearchSmtStorageInventory();
            searchSmtStorageInventory.setStorageId(wmsInOtherinDet.getStorageId().toString());
            searchSmtStorageInventory.setMaterialId(wmsInOtherinDet.getProductModelId().toString());
            ResponseEntity<List<SmtStorageInventoryDto>> storageInventoryFeignApiList = storageInventoryFeignApi.findList(searchSmtStorageInventory);
            if(storageInventoryFeignApiList.getCode() == 0){
                List<SmtStorageInventoryDto> smtStorageInventoryDtos = storageInventoryFeignApiList.getData();
                long storageInventory = 0;
                if(smtStorageInventoryDtos.size() > 0){
                    SmtStorageInventoryDto smtStorageInventoryDto = smtStorageInventoryDtos.get(0);
                    storageInventory = smtStorageInventoryDto.getStoringInventoryId();
                    //累加库存
                    smtStorageInventoryDto.setQuantity(smtStorageInventoryDto.getQuantity().add(wmsInOtherinDet.getInQuantity()));
                    smtStorageInventoryDto.setModifiedTime(new Date());
                    smtStorageInventoryDto.setModifiedUserId(user.getUserId());
                    storageInventoryFeignApi.update(smtStorageInventoryDto);
                } else {
                    //新增库存
                    SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
                    smtStorageInventory.setStorageId(wmsInOtherinDet.getStorageId());
                    smtStorageInventory.setMaterialId(wmsInOtherinDet.getProductModelId());
                    smtStorageInventory.setQuantity(wmsInOtherinDet.getInQuantity());
                    smtStorageInventory.setOrganizationId(user.getOrganizationId());
                    smtStorageInventory.setCreateTime(new Date());
                    smtStorageInventory.setCreateUserId(user.getCreateUserId());
                    smtStorageInventory = storageInventoryFeignApi.add(smtStorageInventory).getData();
                    storageInventory = smtStorageInventory.getStoringInventoryId();
                }

                //增加库位库存明细
                SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
                smtStorageInventoryDet.setStoringInventoryId(storageInventory);
                smtStorageInventoryDet.setMaterialBarcodeCode(wmsInOtherinDet.getPalletCode());
                smtStorageInventoryDet.setGodownEntry(wmsInOtherin.getOtherinCode());
                smtStorageInventoryDet.setMaterialQuantity(wmsInOtherinDet.getInQuantity());
                //生产批号，生产日期，供应商ID无
                storageInventoryFeignApi.add(smtStorageInventoryDet);
            }else{
                //查询失败
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
        }
        return result;
    }


}