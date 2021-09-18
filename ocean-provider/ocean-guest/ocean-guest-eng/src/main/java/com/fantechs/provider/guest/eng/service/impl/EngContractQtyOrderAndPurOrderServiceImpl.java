package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngContractQtyOrderAndPurOrderMapper;
import com.fantechs.provider.guest.eng.service.EngContractQtyOrderAndPurOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@Service
public class EngContractQtyOrderAndPurOrderServiceImpl extends BaseService<EngContractQtyOrderAndPurOrderDto> implements EngContractQtyOrderAndPurOrderService {

    @Resource
    private EngContractQtyOrderAndPurOrderMapper engContractQtyOrderAndPurOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<EngContractQtyOrderAndPurOrderDto> findList(Map<String, Object> map) {
        SysUser user= getUser();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",user.getOrganizationId());
        }

        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(user.getUserId());
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        if(StringUtils.isNotEmpty(list.getData()))
            map.put("supplierId",list.getData().get(0).getSupplierId());
        return engContractQtyOrderAndPurOrderMapper.findList(map);
    }

    public SysUser getUser(){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return currentUser;
    }
}
