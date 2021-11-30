package com.fantechs.provider.guest.jinan.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.jinan.RfidBaseStationReAsset;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.mapper.RfidBaseStationReAssetMapper;
import com.fantechs.provider.guest.jinan.service.RfidBaseStationReAssetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/30.
 */
@Service
public class RfidBaseStationReAssetServiceImpl extends BaseService<RfidBaseStationReAsset> implements RfidBaseStationReAssetService {

    @Resource
    private RfidBaseStationReAssetMapper rfidBaseStationReAssetMapper;

    @Override
    public List<RfidBaseStationReAsset> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return rfidBaseStationReAssetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(RfidBaseStationReAsset record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(RfidBaseStationReAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", record.getAssetId())
                .andEqualTo("orgId", user.getOrganizationId());
        RfidBaseStationReAsset rfidBaseStationReAsset1 = rfidBaseStationReAssetMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(rfidBaseStationReAsset1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"该资产已绑定其他基站");
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        return rfidBaseStationReAssetMapper.insertSelective(record);
    }
}
