package com.fantechs.provider.om.service.ht.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.ht.OmHtSalesOrderMapper;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class OmHtSalesOrderServiceImpl extends BaseService<OmHtSalesOrder> implements OmHtSalesOrderService {

    @Resource
    private OmHtSalesOrderMapper omHtSalesOrderMapper;

    @Override
    public int save(OmHtSalesOrder omHtSalesOrder) {
        return omHtSalesOrderMapper.insertSelective(omHtSalesOrder);
    }

    @Override
    public List<OmHtSalesOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orgId",user.getOrganizationId());
        return omHtSalesOrderMapper.findList(map);
    }
}
