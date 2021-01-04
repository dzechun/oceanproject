package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutFinishedProductDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutFinishedProductDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtFinishedProductDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutFinishedProductDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
@Service
public class WmsOutFinishedProductDetServiceImpl extends BaseService<WmsOutFinishedProductDet> implements WmsOutFinishedProductDetService {

    @Resource
    private WmsOutFinishedProductDetMapper wmsOutFinishedProductDetMapper;
    @Resource
    private WmsOutHtFinishedProductDetMapper wmsOutHtFinishedProductDetMapper;

    @Override
    public int save(WmsOutFinishedProductDet wmsOutFinishedProductDet) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutFinishedProductDet.setCreateTime(new Date());
        wmsOutFinishedProductDet.setCreateUserId(user.getUserId());

        return wmsOutFinishedProductDetMapper.insertSelective(wmsOutFinishedProductDet);
    }

    @Override
    public int update(WmsOutFinishedProductDet wmsOutFinishedProductDet) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutFinishedProductDet.setModifiedUserId(user.getUserId());
        wmsOutFinishedProductDet.setModifiedTime(new Date());

        return wmsOutFinishedProductDetMapper.updateByPrimaryKeySelective(wmsOutFinishedProductDet);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsOutFinishedProductDet wmsOutFinishedProductDet = wmsOutFinishedProductDetMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutFinishedProductDet)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return wmsOutFinishedProductDetMapper.deleteByIds(ids);
    }

    @Override
    public List<WmsOutFinishedProductDetDto> findList(Map<String, Object> map) {
        return wmsOutFinishedProductDetMapper.findList(map);
    }

    @Override
    public List<WmsOutHtFinishedProductDet> findHTList(Map<String, Object> map) {
        return wmsOutHtFinishedProductDetMapper.findHTList(map);
    }
}
