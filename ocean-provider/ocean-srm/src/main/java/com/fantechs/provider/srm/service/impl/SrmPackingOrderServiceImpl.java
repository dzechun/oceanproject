package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrder;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmHtPackingOrderMapper;
import com.fantechs.provider.srm.mapper.SrmPackingOrderMapper;
import com.fantechs.provider.srm.service.SrmPackingOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class SrmPackingOrderServiceImpl extends BaseService<SrmPackingOrder> implements SrmPackingOrderService {

    @Resource
    private SrmPackingOrderMapper srmPackingOrderMapper;
    @Resource
    private SrmHtPackingOrderMapper srmHtPackingOrderMapper;

    @Override
    public List<SrmPackingOrderDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId",user.getOrganizationId());
        return srmPackingOrderMapper.findList(map);
    }



    public int save(SrmPackingOrder srmPackingOrder) {
        SysUser user = getUser();
        srmPackingOrder.setCreateTime(new Date());
        srmPackingOrder.setCreateUserId(user.getUserId());
        srmPackingOrder.setModifiedTime(new Date());
        srmPackingOrder.setModifiedUserId(user.getUserId());
        srmPackingOrder.setStatus((byte)1);
        srmPackingOrder.setOrgId(user.getOrganizationId());
        int i = srmPackingOrderMapper.insertUseGeneratedKeys(srmPackingOrder);

        SrmHtPackingOrder srmHtPackingOrder =new SrmHtPackingOrder();
        BeanUtils.copyProperties(srmPackingOrder, srmHtPackingOrder);
        srmHtPackingOrderMapper.insertSelective(srmHtPackingOrder);
        return i;
    }


    public SysUser getUser(){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return currentUser;
    }
}
