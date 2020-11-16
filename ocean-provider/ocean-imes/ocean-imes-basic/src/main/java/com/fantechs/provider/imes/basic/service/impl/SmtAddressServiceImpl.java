package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtAddressDto;
import com.fantechs.common.base.dto.basic.SmtCurrencyDto;
import com.fantechs.common.base.entity.basic.SmtAddress;
import com.fantechs.common.base.entity.basic.SmtCurrency;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtAddressMapper;
import com.fantechs.provider.imes.basic.service.SmtAddressService;
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
public class SmtAddressServiceImpl extends BaseService<SmtAddress> implements SmtAddressService {

    @Resource
    private SmtAddressMapper smtAddressMapper;

    @Override
    public int save(SmtAddress smtAddress) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("addressDetail", smtAddress.getAddressDetail());
        List<SmtAddress> smtAddresses = smtAddressMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtAddresses)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtAddress.setCreateUserId(currentUser.getUserId());
        smtAddress.setCreateTime(new Date());
        smtAddress.setModifiedTime(new Date());
        smtAddress.setModifiedUserId(currentUser.getUserId());
        smtAddress.setStatus(StringUtils.isEmpty(smtAddress.getStatus())?1:smtAddress.getStatus());
        return smtAddressMapper.insertUseGeneratedKeys(smtAddress);
    }

    @Override
    public int update(SmtAddress smtAddress) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtCurrency.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("addressDetail", smtAddress.getAddressDetail())
        .andNotEqualTo("addressId", smtAddress.getAddressId());
        List<SmtAddress> smtAddresses = smtAddressMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtAddresses)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtAddress.setModifiedUserId(currentUser.getUserId());
        smtAddress.setModifiedTime(new Date());
        return smtAddressMapper.updateByPrimaryKeySelective(smtAddress);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            SmtAddress smtAddress = smtAddressMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtAddress)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return smtAddressMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtAddressDto> findList(Map<String, Object> map) {
        return smtAddressMapper.findList(map);
    }
}
