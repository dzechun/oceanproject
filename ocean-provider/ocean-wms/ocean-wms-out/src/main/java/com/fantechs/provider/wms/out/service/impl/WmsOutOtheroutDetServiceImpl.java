package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtheroutDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtheroutDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutHtOtheroutDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutOtheroutDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutOtheroutDetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class WmsOutOtheroutDetServiceImpl extends BaseService<WmsOutOtheroutDet> implements WmsOutOtheroutDetService {

    @Resource
    private WmsOutOtheroutDetMapper wmsOutOtheroutDetMapper;
    @Resource
    private WmsOutHtOtheroutDetMapper wmsOutHtOtheroutDetMapper;

    @Override
    public int save(WmsOutOtheroutDet wmsOutOtheroutDet) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutOtheroutDet.setCreateUserId(user.getUserId());
        wmsOutOtheroutDet.setCreateTime(new Date());
        wmsOutOtheroutDet.setModifiedUserId(user.getUserId());
        wmsOutOtheroutDet.setModifiedTime(new Date());
        wmsOutOtheroutDet.setStatus(StringUtils.isEmpty(wmsOutOtheroutDet.getStatus())?1:wmsOutOtheroutDet.getStatus());
        int i = wmsOutOtheroutDetMapper.insertUseGeneratedKeys(wmsOutOtheroutDet);

        WmsOutHtOtheroutDet wmsOutHtOtheroutDet = new WmsOutHtOtheroutDet();
        BeanUtils.copyProperties(wmsOutOtheroutDet,wmsOutHtOtheroutDet);
        wmsOutHtOtheroutDetMapper.insertSelective(wmsOutHtOtheroutDet);

        return i;
    }

    @Override
    public int batchSave(List<WmsOutOtheroutDet> wmsOutOtheroutDets) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        for (WmsOutOtheroutDet wmsOutOtheroutDet : wmsOutOtheroutDets) {
            wmsOutOtheroutDet.setCreateUserId(user.getUserId());
            wmsOutOtheroutDet.setCreateTime(new Date());
            wmsOutOtheroutDet.setModifiedUserId(user.getUserId());
            wmsOutOtheroutDet.setModifiedTime(new Date());
            wmsOutOtheroutDet.setStatus(StringUtils.isEmpty(wmsOutOtheroutDet.getStatus())?1:wmsOutOtheroutDet.getStatus());
        }
        return wmsOutOtheroutDetMapper.insertList(wmsOutOtheroutDets);
    }

    @Override
    public int update(WmsOutOtheroutDet wmsOutOtheroutDet) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutOtheroutDet.setMaterialId(user.getUserId());
        wmsOutOtheroutDet.setModifiedTime(new Date());

        WmsOutHtOtheroutDet wmsOutHtOtheroutDet = new WmsOutHtOtheroutDet();
        BeanUtils.copyProperties(wmsOutOtheroutDet,wmsOutHtOtheroutDet);
        wmsOutHtOtheroutDetMapper.insert(wmsOutHtOtheroutDet);

        return wmsOutOtheroutDetMapper.updateByPrimaryKeySelective(wmsOutOtheroutDet);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            WmsOutOtheroutDet wmsOutOtheroutDet = wmsOutOtheroutDetMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutOtheroutDet)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //新增其他出库单明细履历
            WmsOutHtOtheroutDet wmsOutHtOtheroutDet = new WmsOutHtOtheroutDet();
            BeanUtils.copyProperties(wmsOutOtheroutDet,wmsOutHtOtheroutDet);
            wmsOutHtOtheroutDetMapper.insertSelective(wmsOutHtOtheroutDet);
        }

        return wmsOutOtheroutDetMapper.deleteByIds(ids);
    }

    @Override
    public List<WmsOutOtheroutDetDto> findList(Map<String, Object> map) {
        return wmsOutOtheroutDetMapper.findList(map);
    }

    @Override
    public int batchUpdate(List<WmsOutOtheroutDet> wmsOutOtheroutDets) {
        return wmsOutOtheroutDetMapper.batchUpdate(wmsOutOtheroutDets);
    }

    @Override
    public List<WmsOutHtOtheroutDet> findHTList(Map<String, Object> map) {
        return wmsOutHtOtheroutDetMapper.findHTList(map);
    }
}
