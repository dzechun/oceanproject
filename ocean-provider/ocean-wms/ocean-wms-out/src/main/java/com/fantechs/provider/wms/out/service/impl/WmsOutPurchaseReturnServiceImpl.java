package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPurchaseReturnDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturn;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPurchaseReturn;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutHtPurchaseReturnMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPurchaseReturnMapper;
import com.fantechs.provider.wms.out.service.WmsOutPurchaseReturnService;
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
public class WmsOutPurchaseReturnServiceImpl  extends BaseService<WmsOutPurchaseReturn> implements WmsOutPurchaseReturnService {

    @Resource
    private WmsOutPurchaseReturnMapper wmsOutPurchaseReturnMapper;
    @Resource
    private WmsOutHtPurchaseReturnMapper wmsOutHtPurchaseReturnMapper;

    @Override
    public int save(WmsOutPurchaseReturn wmsOutPurchaseReturn) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutPurchaseReturn.setPurchaseReturnCode(CodeUtils.getId("CGTH-"));
        wmsOutPurchaseReturn.setCreateTime(new Date());
        wmsOutPurchaseReturn.setCreateUserId(user.getUserId());

        return wmsOutPurchaseReturnMapper.insertSelective(wmsOutPurchaseReturn);
    }

    @Override
    public int update(WmsOutPurchaseReturn wmsOutPurchaseReturn) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutPurchaseReturn.setModifiedUserId(user.getUserId());
        wmsOutPurchaseReturn.setModifiedTime(new Date());

        return wmsOutPurchaseReturnMapper.updateByPrimaryKeySelective(wmsOutPurchaseReturn);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsOutPurchaseReturn wmsOutPurchaseReturn = wmsOutPurchaseReturnMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutPurchaseReturn)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }

        return wmsOutPurchaseReturnMapper.deleteByIds(ids);
    }

    @Override
    public List<WmsOutPurchaseReturnDto> findList(Map<String, Object> dynamicConditionByEntity) {
        return wmsOutPurchaseReturnMapper.findList(dynamicConditionByEntity);
    }

    @Override
    public List<WmsOutHtPurchaseReturn> findHTList(Map<String, Object> dynamicConditionByEntity) {
        return wmsOutHtPurchaseReturnMapper.findHTList(dynamicConditionByEntity);
    }
}
