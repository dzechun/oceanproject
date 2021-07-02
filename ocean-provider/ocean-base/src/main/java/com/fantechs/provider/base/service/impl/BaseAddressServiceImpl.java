package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.general.entity.basic.BaseAddress;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseAddressMapper;
import com.fantechs.provider.base.service.BaseAddressService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/13.
 */
@Service
public class BaseAddressServiceImpl extends BaseService<BaseAddress> implements BaseAddressService {

    @Resource
    private BaseAddressMapper baseAddressMapper;

    @Override
    public int save(BaseAddress baseAddress) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("addressDetail", baseAddress.getAddressDetail());
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        List<BaseAddress> baseAddresses = baseAddressMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseAddresses)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseAddress.setCreateUserId(currentUser.getUserId());
        baseAddress.setCreateTime(new Date());
        baseAddress.setModifiedTime(new Date());
        baseAddress.setModifiedUserId(currentUser.getUserId());
        baseAddress.setStatus(StringUtils.isEmpty(baseAddress.getStatus())?1: baseAddress.getStatus());
        baseAddress.setOrganizationId(currentUser.getOrganizationId());
        return baseAddressMapper.insertUseGeneratedKeys(baseAddress);
    }

    @Override
    public int update(BaseAddress baseAddress) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("addressDetail", baseAddress.getAddressDetail())
        .andNotEqualTo("addressId", baseAddress.getAddressId());
        List<BaseAddress> baseAddresses = baseAddressMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseAddresses)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseAddress.setModifiedUserId(currentUser.getUserId());
        baseAddress.setModifiedTime(new Date());
        baseAddress.setOrganizationId(currentUser.getOrganizationId());
        return baseAddressMapper.updateByPrimaryKeySelective(baseAddress);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseAddress baseAddress = baseAddressMapper.selectByPrimaryKey(Long.valueOf(id));
            if (StringUtils.isEmpty(baseAddress)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return baseAddressMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseAddressDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",user.getOrganizationId());
        return baseAddressMapper.findList(map);
    }
}
