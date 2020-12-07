package com.fantechs.provider.imes.storage.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryDetMapper;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryMapper;
import com.fantechs.provider.imes.storage.service.SmtStorageInventoryDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/04.
 */
@Service
public class SmtStorageInventoryDetServiceImpl extends BaseService<SmtStorageInventoryDet> implements SmtStorageInventoryDetService {

    @Resource
    private SmtStorageInventoryDetMapper smtStorageInventoryDetMapper;
    @Resource
    private SmtStorageInventoryMapper smtStorageInventoryMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;

    @Override
    public List<SmtStorageInventoryDetDto> findList(Map<String, Object> map) {
        return smtStorageInventoryDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtStorageInventoryDet smtStorageInventoryDet) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        ResponseEntity<SmtStorageMaterial> detail = storageInventoryFeignApi.detail(smtStorageInventoryDet.getStorageId());
        if (StringUtils.isEmpty(detail.getData())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }

        Example storageInventoryExample = new Example(SmtStorageInventory.class);
        Example.Criteria storageInventoryCriteria = storageInventoryExample.createCriteria();
        storageInventoryCriteria.andEqualTo("storageId", smtStorageInventoryDet.getStorageId());

        List<SmtStorageInventory> smtStorageInventories = smtStorageInventoryMapper.selectByExample(storageInventoryExample);
        int i = 0;
        if (StringUtils.isEmpty(smtStorageInventories)) {
            SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
            smtStorageInventory.setStorageId(smtStorageInventoryDet.getStorageId());
            smtStorageInventory.setMaterialId(detail.getData().getMaterialId());
            smtStorageInventory.setLevel(null);
            smtStorageInventory.setQuantity(smtStorageInventoryDet.getMaterialQuantity());
            smtStorageInventory.setCreateUserId(currentUser.getUserId());
            smtStorageInventory.setCreateTime(new Date());
            smtStorageInventory.setModifiedUserId(currentUser.getUserId());
            smtStorageInventory.setModifiedTime(new Date());
            smtStorageInventoryMapper.insert(smtStorageInventory);
        } else {
            //刷新储位库存的数量
            Map<String,Object> map = new HashMap();
            map.put("quantity",smtStorageInventoryDet.getMaterialQuantity());
            map.put("storageId",smtStorageInventoryDet.getStorageId());
            smtStorageInventoryMapper.refreshQuantity(map);
        }

        i = super.save(smtStorageInventoryDet);

        return i;
    }

}
