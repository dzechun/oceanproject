package com.fantechs.provider.electronic.service.Impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.PtlJobOrderMapper;
import com.fantechs.provider.electronic.service.PtlJobOrderService;
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
public class PtlJobOrderServiceImpl extends BaseService<PtlJobOrder> implements PtlJobOrderService {

    @Resource
    private PtlJobOrderMapper ptlJobOrderMapper;

    @Override
    public List<PtlJobOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return ptlJobOrderMapper.findList(map);
    }

    @Override
    @Transactional
    @LcnTransaction
    public int updateByRelatedOrderCode(PtlJobOrder ptlJobOrder) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(PtlJobOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relatedOrderCode", ptlJobOrder.getRelatedOrderCode()).andEqualTo("orgId", user.getOrganizationId());

        return ptlJobOrderMapper.updateByExampleSelective(ptlJobOrder, example);
    }
}
