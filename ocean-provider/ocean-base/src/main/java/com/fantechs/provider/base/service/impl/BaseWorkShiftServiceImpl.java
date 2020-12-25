package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.general.entity.basic.BaseWorkShift;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShift;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkShiftMapper;
import com.fantechs.provider.base.mapper.BaseWorkShiftMapper;
import com.fantechs.provider.base.service.BaseWorkShiftService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/21.
 */
@Service
public class BaseWorkShiftServiceImpl extends BaseService<BaseWorkShift> implements BaseWorkShiftService {

    @Resource
    private BaseWorkShiftMapper baseWorkShiftMapper;
    @Resource
    private BaseHtWorkShiftMapper baseHtWorkShiftMapper;

    @Override
    public int save(BaseWorkShift baseWorkShift) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseWorkShift.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("workShiftCode",baseWorkShift.getWorkShiftCode());
        BaseWorkShift baseWorkShift1 = baseWorkShiftMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseWorkShift1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增班次
        baseWorkShift.setCreateTime(new Date());
        baseWorkShift.setCreateUserId(user.getUserId());
        baseWorkShift.setModifiedTime(new Date());
        baseWorkShift.setModifiedUserId(user.getUserId());
        baseWorkShift.setStatus(StringUtils.isEmpty(baseWorkShift.getStatus())?1:baseWorkShift.getStatus());
        int i = baseWorkShiftMapper.insertUseGeneratedKeys(baseWorkShift);

        //新增班次履历
        BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
        BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
        baseHtWorkShiftMapper.insert(baseHtWorkShift);

        return i;
    }

    @Override
    public int update(BaseWorkShift baseWorkShift) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseWorkShift.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("workShiftCode",baseWorkShift.getWorkShiftCode())
                .andNotEqualTo("workShiftId",baseWorkShift.getWorkShiftId());
        BaseWorkShift baseWorkShift1 = baseWorkShiftMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseWorkShift1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }


        baseWorkShift.setModifiedUserId(user.getUserId());
        baseWorkShift.setModifiedTime(new Date());

        BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
        BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
        baseHtWorkShiftMapper.insert(baseHtWorkShift);

        return baseWorkShiftMapper.updateByPrimaryKeySelective(baseWorkShift);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtWorkShift> baseHtWorkShifts = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseWorkShift baseWorkShift = baseWorkShiftMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseWorkShift)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
            BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
            baseHtWorkShifts.add(baseHtWorkShift);
        }

        baseHtWorkShiftMapper.insertList(baseHtWorkShifts);
        return baseWorkShiftMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseWorkShiftDto> findList(Map<String, Object> map) {
        return baseWorkShiftMapper.findList(map);
    }
}
