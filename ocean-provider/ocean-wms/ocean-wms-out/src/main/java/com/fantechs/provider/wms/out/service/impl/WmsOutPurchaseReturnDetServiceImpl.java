package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPurchaseReturnDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturn;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturnDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutPurchaseReturnDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutPurchaseReturnDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/24.
 */
@Service
public class WmsOutPurchaseReturnDetServiceImpl  extends BaseService<WmsOutPurchaseReturnDet> implements WmsOutPurchaseReturnDetService {

         @Resource
         private WmsOutPurchaseReturnDetMapper wmsOutPurchaseReturnDetMapper;

    @Override
    public List<WmsOutPurchaseReturnDetDto> findList(Map<String, Object> dynamicConditionByEntity) {
        return wmsOutPurchaseReturnDetMapper.findList(dynamicConditionByEntity);
    }

    @Override
    public int save(WmsOutPurchaseReturnDet wmsOutPurchaseReturnDet) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutPurchaseReturnDet.setCreateTime(new Date());
        wmsOutPurchaseReturnDet.setCreateUserId(user.getUserId());

        return wmsOutPurchaseReturnDetMapper.insertSelective(wmsOutPurchaseReturnDet);
    }

    @Override
    public int update(WmsOutPurchaseReturnDet wmsOutPurchaseReturnDet) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutPurchaseReturnDet.setModifiedUserId(user.getUserId());
        wmsOutPurchaseReturnDet.setModifiedTime(new Date());

        return wmsOutPurchaseReturnDetMapper.updateByPrimaryKeySelective(wmsOutPurchaseReturnDet);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsOutPurchaseReturnDet wmsOutPurchaseReturnDet = wmsOutPurchaseReturnDetMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutPurchaseReturnDet)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }

        return wmsOutPurchaseReturnDetMapper.deleteByIds(ids);
    }
}
