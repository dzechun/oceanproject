package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmPlanDeliveryOrderDetMapper;
import com.fantechs.provider.srm.service.SrmPlanDeliveryOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class SrmPlanDeliveryOrderDetServiceImpl extends BaseService<SrmPlanDeliveryOrderDet> implements SrmPlanDeliveryOrderDetService {

    @Resource
    private SrmPlanDeliveryOrderDetMapper srmPlanDeliveryOrderDetMapper;

    @Override
    public List<SrmPlanDeliveryOrderDetDto> findList(Map<String, Object> map) {
        return srmPlanDeliveryOrderDetMapper.findList(map);
    }

    @Override
    public int asn(List<SrmPlanDeliveryOrderDetDto> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isNotEmpty(list)) {
            Example example = new Example(SrmPlanDeliveryOrderDet.class);
            example.createCriteria().andEqualTo("planDeliveryOrderId",list.get(0).getPlanDeliveryOrderId());
            srmPlanDeliveryOrderDetMapper.deleteByExample(example);
            for (SrmPlanDeliveryOrderDetDto srmPlanDeliveryOrderDetDto : list) {
                if (srmPlanDeliveryOrderDetDto.getIfCreateAsn() == 1) {
                    continue;
                }
                srmPlanDeliveryOrderDetDto.setIfCreateAsn((byte) 1);
                srmPlanDeliveryOrderDetDto.setModifiedUserId(user.getUserId());
                srmPlanDeliveryOrderDetDto.setModifiedTime(new Date());
            }
            srmPlanDeliveryOrderDetMapper.insertList(list);
        }

        return 1;
    }
}
