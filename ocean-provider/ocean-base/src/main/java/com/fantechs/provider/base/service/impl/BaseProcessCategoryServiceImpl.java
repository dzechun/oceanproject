package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseProcessCategoryDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProcessCategoryImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseTeamImport;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseProcessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessCategory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProcessCategoryMapper;
import com.fantechs.provider.base.mapper.BaseProcessCategoryMapper;
import com.fantechs.provider.base.mapper.BaseProcessMapper;
import com.fantechs.provider.base.service.BaseProcessCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/10/15.
 */
@Service
public class BaseProcessCategoryServiceImpl extends BaseService<BaseProcessCategory> implements BaseProcessCategoryService {

    @Resource
    private BaseProcessCategoryMapper baseProcessCategoryMapper;

    @Resource
    private BaseHtProcessCategoryMapper baseHtProcessCategoryMapper;

    @Resource
    private BaseProcessMapper baseProcessMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProcessCategory baseProcessCategory) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProcessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUserInfo.getOrganizationId());
        criteria.andEqualTo("processCategoryCode", baseProcessCategory.getProcessCategoryCode());
        List<BaseProcessCategory> smtProcessCategories = baseProcessCategoryMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtProcessCategories)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseProcessCategory.setCreateUserId(currentUserInfo.getUserId());
        baseProcessCategory.setCreateTime(new Date());
        baseProcessCategory.setModifiedTime(new Date());
        baseProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
        baseProcessCategory.setOrganizationId(currentUserInfo.getOrganizationId());
        baseProcessCategoryMapper.insertUseGeneratedKeys(baseProcessCategory);

        //新增工序类别历史信息
        BaseHtProcessCategory baseHtProcessCategory = new BaseHtProcessCategory();
        BeanUtils.copyProperties(baseProcessCategory, baseHtProcessCategory);
        return baseHtProcessCategoryMapper.insertSelective(baseHtProcessCategory);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProcessCategory baseProcessCategory) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProcessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUserInfo.getOrganizationId());
        criteria.andEqualTo("processCategoryCode", baseProcessCategory.getProcessCategoryCode());
        BaseProcessCategory processCategory = baseProcessCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(processCategory)&&!processCategory.getProcessCategoryId().equals(baseProcessCategory.getProcessCategoryId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
        baseProcessCategory.setModifiedTime(new Date());
        baseProcessCategory.setOrganizationId(currentUserInfo.getOrganizationId());
        baseProcessCategoryMapper.updateByPrimaryKeySelective(baseProcessCategory);

        //新增工序列表历史信息
        BaseHtProcessCategory baseHtProcessCategory = new BaseHtProcessCategory();
        BeanUtils.copyProperties(baseProcessCategory, baseHtProcessCategory);
        return baseHtProcessCategoryMapper.insertSelective(baseHtProcessCategory);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        ArrayList<BaseHtProcessCategory> list = new ArrayList<>();

        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] processCategoryIds = ids.split(",");
        for (String processCategoryId : processCategoryIds) {
            BaseProcessCategory baseProcessCategory = baseProcessCategoryMapper.selectByPrimaryKey(processCategoryId);
            if (StringUtils.isEmpty(baseProcessCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被工序以用
            Example example = new Example(BaseProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("processCategoryId", baseProcessCategory.getProcessCategoryId());
            List<BaseProcess> baseProcesses = baseProcessMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProcesses)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增工序历史信息
            BaseHtProcessCategory baseHtProcessCategory = new BaseHtProcessCategory();
            BeanUtils.copyProperties(baseProcessCategory, baseHtProcessCategory);
            baseHtProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
            baseHtProcessCategory.setModifiedTime(new Date());
            list.add(baseHtProcessCategory);
        }
        baseHtProcessCategoryMapper.insertList(list);

        return baseProcessCategoryMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseProcessCategoryDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseProcessCategoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseProcessCategoryImport> baseProcessCategoryImportList) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseProcessCategory> list = new LinkedList<>();
        LinkedList<BaseHtProcessCategory> htList = new LinkedList<>();
        for (int i = 0; i < baseProcessCategoryImportList.size(); i++) {
            BaseProcessCategoryImport baseProcessCategoryImport = baseProcessCategoryImportList.get(i);
            String processCategoryCode = baseProcessCategoryImport.getProcessCategoryCode();
            String processCategoryName = baseProcessCategoryImport.getProcessCategoryName();
            if (StringUtils.isEmpty(
                    processCategoryCode,processCategoryName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseProcessCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("processCategoryCode",processCategoryCode);
            if (StringUtils.isNotEmpty(baseProcessCategoryMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (BaseProcessCategory baseProcessCategory : list) {
                    if (baseProcessCategory.getProcessCategoryCode().equals(processCategoryCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            BaseProcessCategory baseProcessCategory = new BaseProcessCategory();
            BeanUtils.copyProperties(baseProcessCategoryImport, baseProcessCategory);
            baseProcessCategory.setCreateTime(new Date());
            baseProcessCategory.setCreateUserId(currentUser.getUserId());
            baseProcessCategory.setModifiedTime(new Date());
            baseProcessCategory.setModifiedUserId(currentUser.getUserId());
            baseProcessCategory.setStatus((byte) 1);
            baseProcessCategory.setOrganizationId(currentUser.getOrganizationId());
            list.add(baseProcessCategory);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseProcessCategoryMapper.insertList(list);
        }

        for (BaseProcessCategory baseProcessCategory : list) {
            BaseHtProcessCategory baseHtProcessCategory = new BaseHtProcessCategory();
            BeanUtils.copyProperties(baseProcessCategory, baseHtProcessCategory);
            htList.add(baseHtProcessCategory);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtProcessCategoryMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    @Override
    public BaseProcessCategory addOrUpdate (BaseProcessCategory baseProcessCategory) {

        Example example = new Example(BaseProcessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCategoryCode", baseProcessCategory.getProcessCategoryCode());
        criteria.andEqualTo("organizationId", baseProcessCategory.getOrganizationId());
        List<BaseProcessCategory> baseProcessCategoryOld = baseProcessCategoryMapper.selectByExample(example);

        baseProcessCategory.setModifiedTime(new Date());
        if (StringUtils.isNotEmpty(baseProcessCategoryOld)){
            baseProcessCategory.setProcessCategoryId(baseProcessCategoryOld.get(0).getProcessCategoryId());
            baseProcessCategoryMapper.updateByPrimaryKey(baseProcessCategory);
        }else{
            baseProcessCategory.setCreateTime(new Date());
            baseProcessCategoryMapper.insertUseGeneratedKeys(baseProcessCategory);
        }
        return baseProcessCategory;
    }
}
