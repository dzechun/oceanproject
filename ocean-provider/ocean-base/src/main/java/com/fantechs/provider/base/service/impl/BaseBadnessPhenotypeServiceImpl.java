package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessPhenotypeDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.BaseBadnessPhenotype;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessPhenotype;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBadnessPhenotypeMapper;
import com.fantechs.provider.base.mapper.BaseHtBadnessPhenotypeMapper;
import com.fantechs.provider.base.service.BaseBadnessPhenotypeService;
import com.fantechs.provider.base.service.BaseHtBadnessPhenotypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/07.
 */
@Service
public class BaseBadnessPhenotypeServiceImpl extends BaseService<BaseBadnessPhenotype> implements BaseBadnessPhenotypeService {

    @Resource
    private BaseBadnessPhenotypeMapper baseBadnessPhenotypeMapper;

    @Resource
    private BaseHtBadnessPhenotypeMapper baseHtBadnessPhenotypeMapper;

    @Override
    public List<BaseBadnessPhenotypeDto> findList(Map<String, Object> map) {
        return baseBadnessPhenotypeMapper.findList(map);
    }

    @Override
    public int save(BaseBadnessPhenotype record) {
//        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
//        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
//        record.setModifiedUserId(user.getUserId());
//        record.setOrgId(user.getOrganizationId());

        int i = baseBadnessPhenotypeMapper.insertUseGeneratedKeys(record);

        BaseHtBadnessPhenotype baseHtBadnessPhenotype = new BaseHtBadnessPhenotype();
        BeanUtils.copyProperties(record,baseHtBadnessPhenotype);
        baseHtBadnessPhenotypeMapper.insert(baseHtBadnessPhenotype);

        return i;
    }

    @Override
    public int update(BaseBadnessPhenotype entity) {
//        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        this.codeIfRepeat(entity);

//        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());

        BaseHtBadnessPhenotype baseHtBadnessPhenotype = new BaseHtBadnessPhenotype();
        BeanUtils.copyProperties(entity,baseHtBadnessPhenotype);
        baseHtBadnessPhenotypeMapper.insert(baseHtBadnessPhenotype);

        return baseBadnessPhenotypeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
//        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        List<BaseHtBadnessPhenotype> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseBadnessPhenotype baseBadnessPhenotype = baseBadnessPhenotypeMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseBadnessPhenotype)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtBadnessPhenotype baseHtBadnessPhenotype = new BaseHtBadnessPhenotype();
            BeanUtils.copyProperties(baseBadnessPhenotype,baseHtBadnessPhenotype);
            list.add(baseHtBadnessPhenotype);
        }

        baseHtBadnessPhenotypeMapper.insertList(list);
        return baseBadnessPhenotypeMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseBadnessPhenotype entity){
        Example example = new Example(BaseBadnessPhenotype.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("badnessPhenotypeCode",entity.getBadnessPhenotypeCode());
        BaseBadnessPhenotype baseBadnessPhenotype = baseBadnessPhenotypeMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessPhenotype)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

}
