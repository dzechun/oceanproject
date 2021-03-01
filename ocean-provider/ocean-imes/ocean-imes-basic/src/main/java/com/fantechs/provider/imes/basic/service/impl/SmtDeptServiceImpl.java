package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.imports.SmtDeptImport;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtMaterialCategory;
import com.fantechs.common.base.entity.basic.history.SmtHtDept;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.SmtDeptMapper;
import com.fantechs.provider.imes.basic.mapper.SmtFactoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtHtDeptMapper;
import com.fantechs.provider.imes.basic.service.SmtDeptService;
import com.fantechs.provider.imes.basic.service.SmtFactoryService;
import javafx.scene.layout.BackgroundImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Service
@Slf4j
public class SmtDeptServiceImpl extends BaseService<SmtDept> implements SmtDeptService {

    @Resource
    private SmtDeptMapper smtDeptMapper;
    @Resource
    private SmtHtDeptMapper smtHtDeptMapper;
    @Resource
    private SmtFactoryMapper smtFactoryMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<SmtDept> findList(SearchSmtDept searchSmtDept) {
        return smtDeptMapper.findList(searchSmtDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtDept smtDept) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deptCode",smtDept.getDeptCode());
        SmtDept smtDept1 = smtDeptMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(smtDept1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("factoryId",smtDept.getFactoryId())
                .andEqualTo("deptName",smtDept.getDeptName());
        SmtDept smtDept2 = smtDeptMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(smtDept2)){
            throw new BizErrorException("该工厂下的部门名称已存在");
        }

        smtDept.setCreateUserId(currentUser.getUserId());
        smtDept.setCreateTime(new Date());
        smtDept.setModifiedUserId(currentUser.getUserId());
        smtDept.setModifiedTime(new Date());
        int i = smtDeptMapper.insertSelective(smtDept);

        //新增部门历史信息
        SmtHtDept smtHtDept=new SmtHtDept();
        BeanUtils.copyProperties(smtDept,smtHtDept);
        smtHtDeptMapper.insertSelective(smtHtDept);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtDept smtDept) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }


        Example example = new Example(SmtDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deptCode",smtDept.getDeptCode())
                .andNotEqualTo("deptId",smtDept.getDeptId());
        SmtDept smtDept1 = smtDeptMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(smtDept1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("factoryId",smtDept.getFactoryId())
                .andEqualTo("deptName",smtDept.getDeptName())
                .andNotEqualTo("deptId",smtDept.getDeptId());
        SmtDept smtDept2 = smtDeptMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(smtDept2)){
            throw new BizErrorException("该工厂下的部门名称已存在");
        }

        smtDept.setModifiedUserId(currentUser.getUserId());
        smtDept.setModifiedTime(new Date());
        int i= smtDeptMapper.updateByPrimaryKeySelective(smtDept);

        //新增部门历史信息
        SmtHtDept smtHtDept=new SmtHtDept();
        BeanUtils.copyProperties(smtDept,smtHtDept);
        smtHtDeptMapper.insertSelective(smtHtDept);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<SmtHtDept> list=new ArrayList<>();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr =  ids.split(",");
        for (String deptId : idsArr) {
            SmtDept smtDept = smtDeptMapper.selectByPrimaryKey(deptId);
            if(StringUtils.isEmpty(smtDept)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            Example example = new Example(SmtDept.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId",deptId);
            List<SmtDept> smtMaterialCategories = smtDeptMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtMaterialCategories)){
                throw new BizErrorException("删除失败，该部门有子部门");
            }

            //新增部门历史信息
            SmtHtDept smtHtDept=new SmtHtDept();
            BeanUtils.copyProperties(smtDept,smtHtDept);
            smtHtDept.setModifiedUserId(currentUser.getUserId());
            smtHtDept.setModifiedTime(new Date());
            list.add(smtHtDept);


        }
        smtHtDeptMapper.insertList(list);
        i= smtDeptMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtDeptImport> smtDeptImports) {
        /*SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }*/

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtDept> list = new LinkedList<>();
        LinkedList<SmtHtDept> htList = new LinkedList<>();
        ArrayList<SmtDeptImport> smtDeptImportList = new ArrayList<>();

        for (int i = 0; i < smtDeptImports.size(); i++) {
            SmtDeptImport smtDeptImport = smtDeptImports.get(i);
            String deptCode = smtDeptImport.getDeptCode();
            String deptName = smtDeptImport.getDeptName();
            String factoryCode = smtDeptImport.getFactoryCode();
            String organizationCode = smtDeptImport.getOrganizationCode();
            String parentCode = smtDeptImport.getParentCode();
            if (StringUtils.isEmpty(
                    deptCode,deptName,factoryCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtDept.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("deptCode",deptCode);
            if (StringUtils.isNotEmpty(smtDeptMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断工厂是否存在
            Example example1 = new Example(SmtFactory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("factoryCode",factoryCode);
            SmtFactory smtFactory = smtFactoryMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(smtFactory)){
                fail.add(i+4);
                continue;
            }
            smtDeptImport.setFactoryId(smtFactory.getFactoryId());

            //判断集合中是否存在同样得数据
            boolean tag1 = false;
            if (StringUtils.isNotEmpty(smtDeptImportList)){
                for (SmtDeptImport deptImport : smtDeptImportList) {
                    if (!deptImport.getDeptCode().equals(deptCode)){
                        tag1 = true;
                    }
                }
            }
            if (tag1){
                fail.add(i+4);
                continue;
            }

            //组织编码不为空则组织的数据必须存在
            if (StringUtils.isNotEmpty(organizationCode)){
                SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
                searchBaseOrganization.setCodeQueryMark(1);
                searchBaseOrganization.setOrganizationCode(organizationCode);
                List<BaseOrganizationDto> baseOrganizationDtos = baseFeignApi.findOrganizationList(searchBaseOrganization).getData();
                if (StringUtils.isEmpty(baseOrganizationDtos)){
                    fail.add(i+4);
                    continue;
                }
                smtDeptImport.setOrganizationId(baseOrganizationDtos.get(0).getOrganizationId());
            }

            //若父级编码不为空，判断父级是否维护
            if (StringUtils.isNotEmpty(parentCode)){

                //判断数据库中是否存在
                example.clear();
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.andEqualTo("deptCode",parentCode);
                SmtDept smtDept = smtDeptMapper.selectOneByExample(example);

                //判断是否在excel中提前维护
                boolean tag2 = false;
                if (StringUtils.isNotEmpty(smtDeptImportList)){

                    for (SmtDeptImport deptImport : smtDeptImportList) {
                        if (deptImport.getDeptCode().equals(parentCode)){
                            tag2 = true;
                        }
                    }
                }
                if (!tag2 && StringUtils.isEmpty(smtDept)){
                    fail.add(i+4);
                    continue;
                }
            }else {
                smtDeptImport.setParentId((long) -1);
            }

            smtDeptImportList.add(smtDeptImport);
        }

        if (StringUtils.isNotEmpty(smtDeptImportList)){
            for (SmtDeptImport smtDeptImport : smtDeptImportList) {
                SmtDept smtDept = new SmtDept();
                BeanUtils.copyProperties(smtDeptImport,smtDept);
                smtDept.setCreateTime(new Date());
                //smtDept.setCreateUserId(currentUser.getUserId());
                smtDept.setModifiedTime(new Date());
                //smtDept.setModifiedUserId(currentUser.getUserId());
                list.add(smtDept);
            }
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtDeptMapper.insertList(list);
        }

        for (SmtDept smtDept : list) {
            SmtHtDept smtHtDept = new SmtHtDept();
            BeanUtils.copyProperties(smtDept,smtHtDept);
            htList.add(smtHtDept);
        }

        if (StringUtils.isNotEmpty(htList)){
            smtHtDeptMapper.insertList(htList);
        }

        //更新部门的父级ID
        for (SmtDept smtDept : list) {
            String parentCode = smtDept.getParentCode();
            Example example = new Example(SmtDept.class);
            if (StringUtils.isNotEmpty(parentCode)){
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("deptCode",parentCode);
                SmtDept smtDept1 = smtDeptMapper.selectOneByExample(example);
                smtDept.setParentId(smtDept1.getDeptId());
                smtDeptMapper.updateByPrimaryKeySelective(smtDept1);
            }
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
