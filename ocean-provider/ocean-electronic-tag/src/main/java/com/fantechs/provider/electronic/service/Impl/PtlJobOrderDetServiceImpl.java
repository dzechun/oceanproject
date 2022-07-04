package com.fantechs.provider.electronic.service.Impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDetDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrderDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.PtlJobOrderDetMapper;
import com.fantechs.provider.electronic.service.PtlJobOrderDetService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/01.
 */
@Service
public class PtlJobOrderDetServiceImpl extends BaseService<PtlJobOrderDet> implements PtlJobOrderDetService {

    @Resource
    private PtlJobOrderDetMapper ptlJobOrderDetMapper;

    @Override
    public List<PtlJobOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return ptlJobOrderDetMapper.findList(map);
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int batchUpdate(List<PtlJobOrderDet> ptlJobOrderDetList) {
        int i = 0;
        if (StringUtils.isNotEmpty(ptlJobOrderDetList)){
            i = ptlJobOrderDetMapper.batchUpdate(ptlJobOrderDetList);
        }
        return i;
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int updateByJobOrderId(PtlJobOrderDet ptlJobOrderDet) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(PtlJobOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobOrderId", ptlJobOrderDet.getJobOrderId()).andEqualTo("orgId", user.getOrganizationId());

        return ptlJobOrderDetMapper.updateByExampleSelective(ptlJobOrderDet, example);
    }
}
