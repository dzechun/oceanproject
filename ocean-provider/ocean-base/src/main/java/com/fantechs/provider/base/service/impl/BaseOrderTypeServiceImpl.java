package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderTypeDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseOrderTypeMapper;
import com.fantechs.provider.base.service.BaseOrderTypeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/23.
 */
@Service
public class BaseOrderTypeServiceImpl extends BaseService<BaseOrderType> implements BaseOrderTypeService {

    @Resource
    private BaseOrderTypeMapper baseOrderTypeMapper;

    @Override
    public List<BaseOrderTypeDto> findList(Map<String, Object> map) {
        return baseOrderTypeMapper.findList(map);
    }

    @Override
    public int save(BaseOrderType baseOrderType) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseOrderType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderTypeCode",baseOrderType.getOrderTypeCode());
        List<BaseOrderType> baseOrderTypes = baseOrderTypeMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseOrderTypes)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseOrderType.setCreateTime(new Date());
        baseOrderType.setCreateUserId(currentUser.getUserId());
        baseOrderType.setModifiedTime(new Date());
        baseOrderType.setModifiedUserId(currentUser.getUserId());
        int i = baseOrderTypeMapper.insertSelective(baseOrderType);

        return i;
    }

    @Override
    public int update(BaseOrderType baseOrderType) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseOrderType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderTypeCode",baseOrderType.getOrderTypeCode())
                .andNotEqualTo("orderTypeId",baseOrderType.getOrderTypeId());
        List<BaseOrderType> baseOrderTypes = baseOrderTypeMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseOrderTypes)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseOrderType.setModifiedTime(new Date());
        baseOrderType.setModifiedUserId(currentUser.getUserId());
        int i = baseOrderTypeMapper.updateByPrimaryKeySelective(baseOrderType);
        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");

        for (String id : idArray) {
            BaseOrderType baseOrderType = baseOrderTypeMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseOrderType)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }

        return baseOrderTypeMapper.deleteByIds(ids);
    }


}