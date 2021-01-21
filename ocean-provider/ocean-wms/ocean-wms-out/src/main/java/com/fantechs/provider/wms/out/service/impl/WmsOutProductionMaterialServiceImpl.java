package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutProductionMaterialDto;

import com.fantechs.common.base.general.entity.wms.out.WmsOutOtherout;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtheroutDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtherout;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtProductionMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutHtProductionMaterialMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutProductionMaterialMapper;
import com.fantechs.provider.wms.out.service.WmsOutProductionMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/18.
 */
@Service
public class WmsOutProductionMaterialServiceImpl extends BaseService<WmsOutProductionMaterial> implements WmsOutProductionMaterialService {

    @Resource
    private WmsOutProductionMaterialMapper wmsOutProductionMaterialMapper;
    @Resource
    private WmsOutHtProductionMaterialMapper wmsOutHtProductionMaterialMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;

    @Override
    public List<WmsOutProductionMaterialDto> findList(Map<String, Object> map) {
        return wmsOutProductionMaterialMapper.findList(map);
    }

    @Override
    public List<WmsOutProductionMaterial> findHtList(Map<String, Object> map) {
        return wmsOutHtProductionMaterialMapper.findHtList(map);
    }

    @Override
    public int save(WmsOutProductionMaterial wmsOutProductionMaterial) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //库存数据
        SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
        smtStorageInventory.setQuantity(wmsOutProductionMaterial.getRealityQty());
        smtStorageInventory.setStorageId(wmsOutProductionMaterial.getStorageId());
        smtStorageInventory.setMaterialId(wmsOutProductionMaterial.getMaterialId());

        SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
        smtStorageInventoryDet.setMaterialQuantity(wmsOutProductionMaterial.getRealityQty());
        smtStorageInventoryDet.setGodownEntry(wmsOutProductionMaterial.getFinishedProductCode());
        List<SmtStorageInventoryDet> smtStorageInventoryDets = new ArrayList<>();
        smtStorageInventoryDets.add(smtStorageInventoryDet);
        smtStorageInventory.setSmtStorageInventoryDets(smtStorageInventoryDets);
        //扣库存
        storageInventoryFeignApi.out(smtStorageInventory);

        wmsOutProductionMaterial.setProductionMaterialCode(CodeUtils.getId("FL"));
        wmsOutProductionMaterial.setOrganizationId(user.getOrganizationId());
        wmsOutProductionMaterial.setCreateUserId(user.getCreateUserId());
        wmsOutProductionMaterial.setCreateTime(new Date());
        int i = wmsOutProductionMaterialMapper.insertSelective(wmsOutProductionMaterial);

        WmsOutHtProductionMaterial wmsOutHtProductionMaterial = new WmsOutHtProductionMaterial();
        BeanUtils.copyProperties(wmsOutProductionMaterial,wmsOutHtProductionMaterial);
        wmsOutHtProductionMaterialMapper.insertSelective(wmsOutHtProductionMaterial);

        return i;
    }

    @Override
    public int update(WmsOutProductionMaterial wmsOutProductionMaterial) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutProductionMaterial.setModifiedUserId(user.getCreateUserId());
        wmsOutProductionMaterial.setModifiedTime(new Date());

        WmsOutHtProductionMaterial wmsOutHtProductionMaterial = new WmsOutHtProductionMaterial();
        BeanUtils.copyProperties(wmsOutProductionMaterial,wmsOutHtProductionMaterial);
        wmsOutHtProductionMaterialMapper.insertSelective(wmsOutHtProductionMaterial);

        return wmsOutProductionMaterialMapper.updateByPrimaryKeySelective(wmsOutProductionMaterial);
    }

    @Override
    public int batchDelete(String ids) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            WmsOutProductionMaterial wmsOutProductionMaterial = wmsOutProductionMaterialMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutProductionMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsOutHtProductionMaterial wmsOutHtProductionMaterial = new WmsOutHtProductionMaterial();
            BeanUtils.copyProperties(wmsOutProductionMaterial,wmsOutHtProductionMaterial);
            wmsOutHtProductionMaterialMapper.insertSelective(wmsOutHtProductionMaterial);
        }
        return wmsOutProductionMaterialMapper.deleteByIds(ids);
    }
}
