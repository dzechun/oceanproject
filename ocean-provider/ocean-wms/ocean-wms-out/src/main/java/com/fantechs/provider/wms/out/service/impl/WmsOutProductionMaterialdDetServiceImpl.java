package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterialdDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutProductionMaterialdDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutProductionMaterialdDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 * Created by leifengzhi on 2021/02/04.
 */
@Service
public class WmsOutProductionMaterialdDetServiceImpl extends BaseService<WmsOutProductionMaterialdDet> implements WmsOutProductionMaterialdDetService {

    @Resource
    private WmsOutProductionMaterialdDetMapper wmsOutProductionMaterialdDetMapper;

//    @Override
//    public List<WmsOutProductionMaterialdDetDto> findList(Map<String, Object> map) {
//        return wmsOutProductionMaterialdDetMapper.findList(map);
//    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutProductionMaterialdDet wmsOutProductionMaterialdDet) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutProductionMaterialdDet.setOrganizationId(user.getOrganizationId());
        wmsOutProductionMaterialdDet.setCreateUserId(user.getCreateUserId());
        wmsOutProductionMaterialdDet.setCreateTime(new Date());
        return wmsOutProductionMaterialdDetMapper.insertSelective(wmsOutProductionMaterialdDet);
    }
}
