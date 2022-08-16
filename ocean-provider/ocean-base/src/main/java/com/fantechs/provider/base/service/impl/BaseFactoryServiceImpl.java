package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseFactoryImport;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.basic.history.BaseHtFactory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseDeptMapper;
import com.fantechs.provider.base.mapper.BaseFactoryMapper;
import com.fantechs.provider.base.mapper.BaseHtFactoryMapper;
import com.fantechs.provider.base.mapper.BaseWorkShopMapper;
import com.fantechs.provider.base.service.BaseFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by lfz on 2020/9/1.
 */
@Service
@Slf4j
public class BaseFactoryServiceImpl extends BaseService<BaseFactory> implements BaseFactoryService {
    @Resource
    private BaseFactoryMapper baseFactoryMapper;
    @Resource
    private BaseHtFactoryMapper baseHtFactoryMapper;
    @Resource
    private BaseDeptMapper baseDeptMapper;
    @Resource
    private BaseWorkShopMapper baseWorkShopMapper;


    @Override
    public List<BaseFactoryDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return baseFactoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseFactory baseFactory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseFactory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("factoryCode", baseFactory.getFactoryCode());

        BaseFactory odlsmtFactory = baseFactoryMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtFactory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseFactory.setCreateUserId(user.getUserId());
        baseFactory.setCreateTime(new Date());
        baseFactory.setModifiedUserId(user.getUserId());
        baseFactory.setModifiedTime(new Date());
        baseFactory.setStatus(StringUtils.isEmpty(baseFactory.getStatus())?1: baseFactory.getStatus());
        baseFactory.setOrganizationId(user.getOrganizationId());
        int i = baseFactoryMapper.insertUseGeneratedKeys(baseFactory);

        BaseHtFactory baseHtFactory = new BaseHtFactory();
        BeanUtils.copyProperties(baseFactory, baseHtFactory);
        baseHtFactoryMapper.insertSelective(baseHtFactory);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete (String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtFactory> baseHtFactories = new LinkedList<>();
        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            BaseFactory baseFactory = baseFactoryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseFactory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被部门应用
            Long factoryId = baseFactory.getFactoryId();
            Example example = new Example(BaseDept.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("factoryId",factoryId);
            List<BaseDept> baseDepts = baseDeptMapper.selectByExample(example);

            //被车间引用
            Example example1 = new Example(BaseWorkShop.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("factoryId",factoryId);
            List<BaseWorkShop> baseWorkShops = baseWorkShopMapper.selectByExample(example1);
            if(StringUtils.isNotEmpty(baseDepts)||StringUtils.isNotEmpty(baseWorkShops)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            BaseHtFactory baseHtFactory = new BaseHtFactory();
            BeanUtils.copyProperties(baseFactory, baseHtFactory);

            baseHtFactory.setModifiedTime(new Date());
            baseHtFactory.setModifiedUserId(user.getUserId());
            baseHtFactory.setCreateTime(new Date());
            baseHtFactory.setCreateUserId(user.getUserId());
            baseHtFactories.add(baseHtFactory);

        }
        baseHtFactoryMapper.insertList(baseHtFactories);
        return baseFactoryMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseFactory baseFactory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseFactory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("factoryCode", baseFactory.getFactoryCode());

        BaseFactory odlsmtFactory = baseFactoryMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtFactory)&&!odlsmtFactory.getFactoryId().equals(baseFactory.getFactoryId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseFactory.setModifiedTime(new Date());
        baseFactory.setModifiedUserId(user.getUserId());
        baseFactory.setOrganizationId(user.getOrganizationId());

        BaseHtFactory baseHtFactory = new BaseHtFactory();
        BeanUtils.copyProperties(baseFactory, baseHtFactory);

        baseHtFactoryMapper.insertSelective(baseHtFactory);

        return baseFactoryMapper.updateByPrimaryKeySelective(baseFactory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseFactoryImport> baseFactoryImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseFactory> list = new LinkedList<>();
        LinkedList<BaseHtFactory> htList = new LinkedList<>();
        for (int i = 0; i < baseFactoryImports.size(); i++) {
            BaseFactoryImport baseFactoryImport = baseFactoryImports.get(i);

            String factoryCode = baseFactoryImport.getFactoryCode();
            String factoryName = baseFactoryImport.getFactoryName();
            if (StringUtils.isEmpty(
                    factoryCode,factoryName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("factoryCode",factoryCode);
            if (StringUtils.isNotEmpty(baseFactoryMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (BaseFactory baseFactory : list) {
                    if (baseFactory.getFactoryCode().equals(factoryCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }


            BaseFactory baseFactory = new BaseFactory();
            BeanUtils.copyProperties(baseFactoryImport, baseFactory);
            baseFactory.setCreateTime(new Date());
            baseFactory.setCreateUserId(currentUser.getUserId());
            baseFactory.setModifiedTime(new Date());
            baseFactory.setModifiedUserId(currentUser.getUserId());
            baseFactory.setStatus(1);
            baseFactory.setOrganizationId(currentUser.getOrganizationId());
            list.add(baseFactory);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseFactoryMapper.insertList(list);
        }

        for (BaseFactory baseFactory : list) {
            BaseHtFactory baseHtFactory = new BaseHtFactory();
            BeanUtils.copyProperties(baseFactory, baseHtFactory);
            htList.add(baseHtFactory);
        }
        if (StringUtils.isNotEmpty(htList)){
            baseHtFactoryMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
