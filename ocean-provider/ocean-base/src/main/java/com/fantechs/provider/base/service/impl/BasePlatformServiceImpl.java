package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatformImport;
import com.fantechs.common.base.general.entity.basic.BasePlatform;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlatform;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPlatformMapper;
import com.fantechs.provider.base.mapper.BasePlatformMapper;
import com.fantechs.provider.base.service.BasePlatformService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/10/14.
 */
@Service
public class BasePlatformServiceImpl extends BaseService<BasePlatform> implements BasePlatformService {

    @Resource
    private BasePlatformMapper basePlatformMapper;
    @Resource
    private BaseHtPlatformMapper baseHtPlatformMapper;

    @Override
    public List<BasePlatform> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return basePlatformMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BasePlatform record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BasePlatform.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("platformCode",record.getPlatformCode())
                .andEqualTo("orgId",user.getOrganizationId());
        BasePlatform basePlatform = basePlatformMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(basePlatform)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }


        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        basePlatformMapper.insertUseGeneratedKeys(record);

        BaseHtPlatform baseHtPlatform = new BaseHtPlatform();
        BeanUtils.copyProperties(record, baseHtPlatform);
        int i = baseHtPlatformMapper.insertSelective(baseHtPlatform);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BasePlatform entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BasePlatform.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("platformCode",entity.getPlatformCode())
                .andEqualTo("orgId",user.getOrganizationId())
                .andNotEqualTo("platformId",entity.getPlatformId());
        BasePlatform basePlatform = basePlatformMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(basePlatform)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        BaseHtPlatform baseHtPlatform = new BaseHtPlatform();
        BeanUtils.copyProperties(entity, baseHtPlatform);
        baseHtPlatformMapper.insertSelective(baseHtPlatform);

        return basePlatformMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        List<BaseHtPlatform> htList = new ArrayList<>();
        List<BasePlatform> basePlatforms = basePlatformMapper.selectByIds(ids);
        for (BasePlatform basePlatform : basePlatforms){
            BaseHtPlatform baseHtPlatform = new BaseHtPlatform();
            BeanUtils.copyProperties(basePlatform, baseHtPlatform);
            htList.add(baseHtPlatform);
        }
        baseHtPlatformMapper.insertList(htList);

        return basePlatformMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BasePlatformImport> basePlatformImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BasePlatform> list = new LinkedList<>();
        LinkedList<BaseHtPlatform> htList = new LinkedList<>();
        for (int i = 0; i < basePlatformImports.size(); i++) {
            BasePlatformImport basePlatformImport = basePlatformImports.get(i);

            String platformCode = basePlatformImport.getPlatformCode();
            String platformName = basePlatformImport.getPlatformName();
            if (StringUtils.isEmpty(
                    platformCode,platformName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BasePlatform.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId())
                    .andEqualTo("platformCode",platformCode);
            if (StringUtils.isNotEmpty(basePlatformMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (BasePlatform basePlatform : list) {
                    if (basePlatform.getPlatformCode().equals(platformCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }


            BasePlatform basePlatform = new BasePlatform();
            BeanUtils.copyProperties(basePlatformImport, basePlatform);
            basePlatform.setCreateTime(new Date());
            basePlatform.setCreateUserId(currentUser.getUserId());
            basePlatform.setModifiedTime(new Date());
            basePlatform.setModifiedUserId(currentUser.getUserId());
            basePlatform.setStatus((byte)1);
            basePlatform.setOrgId(currentUser.getOrganizationId());
            list.add(basePlatform);
        }

        if (StringUtils.isNotEmpty(list)){
            success = basePlatformMapper.insertList(list);
        }

        for (BasePlatform basePlatform : list) {
            BaseHtPlatform baseHtPlatform = new BaseHtPlatform();
            BeanUtils.copyProperties(basePlatform, baseHtPlatform);
            htList.add(baseHtPlatform);
        }
        if (StringUtils.isNotEmpty(htList)){
            baseHtPlatformMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
