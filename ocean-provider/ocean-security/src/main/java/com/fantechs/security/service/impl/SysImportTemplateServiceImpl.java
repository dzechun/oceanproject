package com.fantechs.security.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.entity.security.SysImportTemplate;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysImportTemplateMapper;
import com.fantechs.security.service.SysImportTemplateService;
import com.fantechs.security.service.SysMenuInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/13.
 */
@Service
public class SysImportTemplateServiceImpl extends BaseService<SysImportTemplate> implements SysImportTemplateService {

    @Resource
    private SysImportTemplateMapper sysImportTemplateMapper;


    @Override
    public List<SysImportTemplate> findList(Map<String, Object> map) {
        return sysImportTemplateMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysImportTemplate record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SysImportTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("importTemplateCode",record.getImportTemplateCode());
        SysImportTemplate sysImportTemplate = sysImportTemplateMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(sysImportTemplate)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(currentUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUser.getUserId());
        int i = sysImportTemplateMapper.insertSelective(record);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysImportTemplate entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SysImportTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("importTemplateCode",entity.getImportTemplateCode())
                .andNotEqualTo("importTemplateId",entity.getImportTemplateId());
        SysImportTemplate sysImportTemplate = sysImportTemplateMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(sysImportTemplate)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUser.getUserId());
        int i = sysImportTemplateMapper.updateByPrimaryKeySelective(entity);

        return i;
    }

}
