package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDetDto;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStorageInventoryDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStorageInventoryDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/04.
 */
@Service
public class WmsInnerStorageInventoryDetServiceImpl extends BaseService<WmsInnerStorageInventoryDet> implements WmsInnerStorageInventoryDetService {

    @Resource
    private WmsInnerStorageInventoryDetMapper wmsInnerStorageInventoryDetMapper;

    @Override
    public List<WmsInnerStorageInventoryDetDto> findList(Map<String, Object> map) {
        return wmsInnerStorageInventoryDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsInnerStorageInventoryDet smtStorageInventoryDet) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUser)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        smtStorageInventoryDet.setCreateTime(new Date());
        smtStorageInventoryDet.setCreateUserId(currentUser == null?0:currentUser.getUserId());
        smtStorageInventoryDet.setModifiedTime(new Date());
        smtStorageInventoryDet.setModifiedUserId(currentUser == null?0:currentUser.getUserId());
        smtStorageInventoryDet.setStatus(StringUtils.isEmpty(smtStorageInventoryDet.getStatus())?1:smtStorageInventoryDet.getStatus());

        return wmsInnerStorageInventoryDetMapper.insertUseGeneratedKeys(smtStorageInventoryDet);
    }

    @Override
    public int update(WmsInnerStorageInventoryDet smtStorageInventoryDet) {
        return wmsInnerStorageInventoryDetMapper.updateByPrimaryKeySelective(smtStorageInventoryDet);
    }
}
