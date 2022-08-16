package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkshopSectionImport;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkshopSection;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkshopSectionMapper;
import com.fantechs.provider.base.mapper.BaseProcessMapper;
import com.fantechs.provider.base.mapper.BaseWorkshopSectionMapper;
import com.fantechs.provider.base.service.BaseWorkshopSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2020/09/25.
 */
@Service
public class BaseWorkshopSectionServiceImpl extends BaseService<BaseWorkshopSection> implements BaseWorkshopSectionService {

    @Resource
    private BaseWorkshopSectionMapper workshopSectionMapper;
    @Resource
    private BaseHtWorkshopSectionMapper baseHtWorkshopSectionMapper;
    @Resource
    private BaseProcessMapper baseProcessMapper;

    @Override
    public List<BaseWorkshopSection> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return workshopSectionMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseWorkshopSection baseWorkshopSection) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseWorkshopSection.class);
        example.createCriteria().andEqualTo("sectionCode", baseWorkshopSection.getSectionCode())
                                .andEqualTo("organizationId", currentUser.getOrganizationId());
        List<BaseWorkshopSection> list = workshopSectionMapper.selectByExample(example);
        if(list !=null && list.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseWorkshopSection.setCreateUserId(currentUser.getUserId());
        baseWorkshopSection.setCreateTime(new Date());
        baseWorkshopSection.setModifiedTime(new Date());
        baseWorkshopSection.setModifiedUserId(currentUser.getUserId());
        baseWorkshopSection.setOrganizationId(currentUser.getOrganizationId());
        workshopSectionMapper.insertUseGeneratedKeys(baseWorkshopSection);

        //添加工段历史信息
        BaseHtWorkshopSection baseHtWorkshopSection = new BaseHtWorkshopSection();
        BeanUtils.copyProperties(baseWorkshopSection, baseHtWorkshopSection);
        int i=  baseHtWorkshopSectionMapper.insertSelective(baseHtWorkshopSection);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseWorkshopSection baseWorkshopSection) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseWorkshopSection.class);
        example.createCriteria().andEqualTo("sectionCode", baseWorkshopSection.getSectionCode())
                                .andEqualTo("organizationId", currentUser.getOrganizationId());
        BaseWorkshopSection workshopSection = workshopSectionMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(workshopSection)&&!workshopSection.getSectionId().equals(baseWorkshopSection.getSectionId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseWorkshopSection.setModifiedUserId(currentUser.getUserId());
        baseWorkshopSection.setModifiedTime(new Date());
        baseWorkshopSection.setOrganizationId(currentUser.getOrganizationId());
        int i = workshopSectionMapper.updateByPrimaryKeySelective(baseWorkshopSection);

        //添加工段历史信息

        BaseHtWorkshopSection baseHtWorkshopSection = new BaseHtWorkshopSection();
        BeanUtils.copyProperties(baseWorkshopSection, baseHtWorkshopSection);
        baseHtWorkshopSectionMapper.insertSelective(baseHtWorkshopSection);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idsArr = ids.split(",");
        List<BaseHtWorkshopSection> list = new LinkedList<>();
        for (String item:idsArr) {
            BaseWorkshopSection baseWorkshopSection = workshopSectionMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(baseWorkshopSection)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被工序引用
            Example example = new Example(BaseProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sectionId", baseWorkshopSection.getSectionId());
            List<BaseProcess> baseProcesses = baseProcessMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProcesses)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            BaseHtWorkshopSection baseHtWorkshopSection = new BaseHtWorkshopSection();
            BeanUtils.copyProperties(baseWorkshopSection, baseHtWorkshopSection);
            baseHtWorkshopSection.setModifiedTime(new Date());
            baseHtWorkshopSection.setModifiedUserId(currentUser.getUserId());
            list.add(baseHtWorkshopSection);
        }
        baseHtWorkshopSectionMapper.insertList(list);
        return workshopSectionMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseWorkshopSectionImport> baseWorkshopSectionImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseWorkshopSection> list = new LinkedList<>();
        LinkedList<BaseHtWorkshopSection> htList = new LinkedList<>();
        LinkedList<BaseWorkshopSectionImport> workshopSectionImports = new LinkedList<>();
        for (int i = 0; i < baseWorkshopSectionImports.size(); i++) {
            BaseWorkshopSectionImport baseWorkshopSectionImport = baseWorkshopSectionImports.get(i);
            String sectionCode = baseWorkshopSectionImport.getSectionCode();
            String sectionName = baseWorkshopSectionImport.getSectionName();
            if (StringUtils.isEmpty(
                    sectionCode,sectionName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseWorkshopSection.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sectionCode",sectionCode);
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            if (StringUtils.isNotEmpty(workshopSectionMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(workshopSectionImports)){
                for (BaseWorkshopSectionImport workshopSectionImport : workshopSectionImports) {
                    if (workshopSectionImport.getSectionCode().equals(sectionCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            workshopSectionImports.add(baseWorkshopSectionImport);
        }

        if (StringUtils.isNotEmpty(workshopSectionImports)){
            for (BaseWorkshopSectionImport workshopSectionImport : workshopSectionImports) {
                BaseWorkshopSection baseWorkshopSection = new BaseWorkshopSection();
                BeanUtils.copyProperties(workshopSectionImport, baseWorkshopSection);
                baseWorkshopSection.setCreateTime(new Date());
                baseWorkshopSection.setCreateUserId(currentUser.getUserId());
                baseWorkshopSection.setModifiedTime(new Date());
                baseWorkshopSection.setModifiedUserId(currentUser.getUserId());
                baseWorkshopSection.setOrganizationId(currentUser.getOrganizationId());
                baseWorkshopSection.setStatus(workshopSectionImport.getStatus().byteValue());
                list.add(baseWorkshopSection);
            }

            success = workshopSectionMapper.insertList(list);
            for (BaseWorkshopSection baseWorkshopSection : list) {
                BaseHtWorkshopSection baseHtWorkshopSection = new BaseHtWorkshopSection();
                BeanUtils.copyProperties(baseWorkshopSection, baseHtWorkshopSection);
                htList.add(baseHtWorkshopSection);
            }
            baseHtWorkshopSectionMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    @Override
    public BaseWorkshopSection addOrUpdate(BaseWorkshopSection baseWorkshopSection) {

        Example example = new Example(BaseWorkshopSection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sectionCode", baseWorkshopSection.getSectionCode());
        if(StringUtils.isEmpty(baseWorkshopSection.getOrganizationId())){
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        }else{
            criteria.andEqualTo("organizationId", baseWorkshopSection.getOrganizationId());
        }
        List<BaseWorkshopSection> baseWorkshopSections = workshopSectionMapper.selectByExample(example);

        baseWorkshopSection.setModifiedTime(new Date());
        if (StringUtils.isNotEmpty(baseWorkshopSections)){
            baseWorkshopSection.setSectionId(baseWorkshopSections.get(0).getSectionId());
            workshopSectionMapper.updateByPrimaryKey(baseWorkshopSection);
        }else{
            baseWorkshopSection.setCreateTime(new Date());
            workshopSectionMapper.insertUseGeneratedKeys(baseWorkshopSection);
        }
        return baseWorkshopSection;
    }

}
