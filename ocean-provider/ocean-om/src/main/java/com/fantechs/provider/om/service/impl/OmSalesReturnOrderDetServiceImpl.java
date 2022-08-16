package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrder;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderDetMapper;
import com.fantechs.provider.om.service.OmSalesReturnOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/21.
 */
@Service
public class OmSalesReturnOrderDetServiceImpl extends BaseService<OmSalesReturnOrderDet> implements OmSalesReturnOrderDetService {

    @Resource
    private OmSalesReturnOrderDetMapper omSalesReturnOrderDetMapper;
    @Resource
    private OmHtSalesReturnOrderDetMapper omHtSalesReturnOrderDetMapper;

    @Override
    public List<OmSalesReturnOrderDetDto> findList(Map<String, Object> map) {
        return omSalesReturnOrderDetMapper.findList(map);
    }

    @Override
    public List<OmHtSalesReturnOrderDetDto> findHtList(Map<String, Object> map) {
        return omHtSalesReturnOrderDetMapper.findList(map);
    }
}
