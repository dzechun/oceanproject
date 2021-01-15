package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPartsInformation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPlatePartsMapper;
import com.fantechs.provider.base.mapper.BasePlatePartsDetMapper;
import com.fantechs.provider.base.mapper.BasePlatePartsMapper;
import com.fantechs.provider.base.service.BasePlatePartsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@Service
public class BasePlatePartsServiceImpl  extends BaseService<BasePlateParts> implements BasePlatePartsService {

    @Resource
    private BasePlatePartsMapper basePlatePartsMapper;
    @Resource
    private BaseHtPlatePartsMapper baseHtPlatePartsMapper;
    @Resource
    private BasePlatePartsDetMapper basePlatePartsDetMapper;

    @Override
    public List<BasePlatePartsDto> findList(Map<String, Object> map) {
        return basePlatePartsMapper.findList(map);
    }

    @Override
    public int save(BasePlateParts basePlateParts) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        basePlateParts.setCreateTime(new Date());
        basePlateParts.setCreateUserId(user.getUserId());
        basePlateParts.setModifiedTime(new Date());
        basePlateParts.setModifiedUserId(user.getUserId());
        basePlateParts.setStatus(StringUtils.isEmpty(basePlateParts.getStatus())?1:basePlateParts.getStatus());
        basePlateParts.setOrganizationId(user.getOrganizationId());

        int i = basePlatePartsMapper.insertUseGeneratedKeys(basePlateParts);

        BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
        BeanUtils.copyProperties(basePlateParts,baseHtPlateParts);
        baseHtPlatePartsMapper.insert(baseHtPlateParts);

        return i;
    }

    @Override
    public int update(BasePlateParts basePlateParts) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        basePlateParts.setModifiedTime(new Date());
        basePlateParts.setModifiedUserId(user.getUserId());

        BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
        BeanUtils.copyProperties(basePlateParts,baseHtPlateParts);
        baseHtPlatePartsMapper.insert(baseHtPlateParts);

        return basePlatePartsMapper.updateByPrimaryKeySelective(basePlateParts);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BaseHtPlateParts> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BasePlateParts basePlateParts = basePlatePartsMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(basePlateParts)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
            BeanUtils.copyProperties(basePlateParts,baseHtPlateParts);
            list.add(baseHtPlateParts);
        }

        baseHtPlatePartsMapper.insertList(list);

        Example example = new Example(BasePlatePartsDet.class);
        example.createCriteria().andIn("platePartsId", Arrays.asList(ids.split(",")));
        basePlatePartsDetMapper.deleteByExample(example);

        return basePlatePartsMapper.deleteByIds(ids);
    }
}
