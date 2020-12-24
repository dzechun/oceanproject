package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutFinishedProduct;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutFinishedProductMapper;
import com.fantechs.provider.wms.out.service.WmsOutFinishedProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/22.
 */
@Service
public class WmsOutFinishedProductServiceImpl extends BaseService<WmsOutFinishedProduct> implements WmsOutFinishedProductService {

     @Resource
     private WmsOutFinishedProductMapper wmsOutFinishedProductMapper;

    @Override
    public List<WmsOutFinishedProductDto> findList(Map<String, Object> map) {
        return wmsOutFinishedProductMapper.findList(map);
    }

    @Override
    public int save(WmsOutFinishedProduct wmsOutFinishedProduct) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutFinishedProduct.setFinishedProductCode(CodeUtils.getId("CPCK-"));
        wmsOutFinishedProduct.setCreateTime(new Date());
        wmsOutFinishedProduct.setCreateUserId(user.getUserId());

        return wmsOutFinishedProductMapper.insertSelective(wmsOutFinishedProduct);
    }

    @Override
    public int update(WmsOutFinishedProduct wmsOutFinishedProduct) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutFinishedProduct.setModifiedUserId(user.getUserId());
        wmsOutFinishedProduct.setModifiedTime(new Date());

        return wmsOutFinishedProductMapper.updateByPrimaryKeySelective(wmsOutFinishedProduct);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsOutFinishedProduct wmsOutFinishedProduct = wmsOutFinishedProductMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutFinishedProduct)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }

        return wmsOutFinishedProductMapper.deleteByIds(ids);
    }
}
