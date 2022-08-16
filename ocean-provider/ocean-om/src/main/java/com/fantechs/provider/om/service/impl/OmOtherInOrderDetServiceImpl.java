package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmOtherInOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmOtherInOrder;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmOtherInOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderDetMapper;
import com.fantechs.provider.om.service.OmOtherInOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/06/21.
 */
@Service
public class OmOtherInOrderDetServiceImpl extends BaseService<OmOtherInOrderDet> implements OmOtherInOrderDetService {

    @Resource
    private OmOtherInOrderDetMapper omOtherInOrderDetMapper;
    @Resource
    private OmHtOtherInOrderDetMapper omHtOtherInOrderDetMapper;

    @Override
    public List<OmOtherInOrderDetDto> findList(Map<String, Object> map) {
        return omOtherInOrderDetMapper.findList(map);
    }

    @Override
    public List<OmOtherInOrderDetDto> findHtList(Map<String, Object> map) {
        return omHtOtherInOrderDetMapper.findList(map);
    }
}
