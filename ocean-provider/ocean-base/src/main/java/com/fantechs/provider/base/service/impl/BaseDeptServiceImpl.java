package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseDeptImport;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TreeTableUtil;
import com.fantechs.provider.base.mapper.BaseDeptMapper;
import com.fantechs.provider.base.mapper.BaseFactoryMapper;
import com.fantechs.provider.base.mapper.BaseHtDeptMapper;
import com.fantechs.provider.base.service.BaseDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class BaseDeptServiceImpl extends BaseService<BaseDept> implements BaseDeptService {

    @Resource
    private BaseDeptMapper baseDeptMapper;
    @Resource
    private BaseHtDeptMapper baseHtDeptMapper;
    @Resource
    private BaseFactoryMapper baseFactoryMapper;

    @Override
    public List<BaseDept> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BaseDept> list = baseDeptMapper.findList(map);
        list = TreeTableUtil.list2TreeList(list, "deptId", "parentId", "depts");
        return list;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseDept baseDept) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("deptCode", baseDept.getDeptCode());
        BaseDept baseDept1 = baseDeptMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseDept1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria1.andEqualTo("factoryId", baseDept.getFactoryId())
                .andEqualTo("deptName", baseDept.getDeptName());
        BaseDept baseDept2 = baseDeptMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseDept2)){
            throw new BizErrorException("????????????????????????????????????");
        }

        baseDept.setCreateUserId(currentUser.getUserId());
        baseDept.setCreateTime(new Date());
        baseDept.setModifiedUserId(currentUser.getUserId());
        baseDept.setModifiedTime(new Date());
        baseDept.setOrganizationId(currentUser.getOrganizationId());
        int i = baseDeptMapper.insertSelective(baseDept);

        //????????????????????????
        BaseHtDept baseHtDept =new BaseHtDept();
        BeanUtils.copyProperties(baseDept, baseHtDept);
        baseHtDeptMapper.insertSelective(baseHtDept);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseDept baseDept) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();


        Example example = new Example(BaseDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("deptCode", baseDept.getDeptCode())
                .andNotEqualTo("deptId", baseDept.getDeptId());
        BaseDept baseDept1 = baseDeptMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseDept1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria1.andEqualTo("factoryId", baseDept.getFactoryId())
                .andEqualTo("deptName", baseDept.getDeptName())
                .andNotEqualTo("deptId", baseDept.getDeptId());
        BaseDept baseDept2 = baseDeptMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseDept2)){
            throw new BizErrorException("????????????????????????????????????");
        }

        baseDept.setModifiedUserId(currentUser.getUserId());
        baseDept.setModifiedTime(new Date());
        baseDept.setOrganizationId(currentUser.getOrganizationId());
        int i= baseDeptMapper.updateByPrimaryKeySelective(baseDept);

        //????????????????????????
        BaseHtDept baseHtDept =new BaseHtDept();
        BeanUtils.copyProperties(baseDept, baseHtDept);
        baseHtDeptMapper.insertSelective(baseHtDept);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<BaseHtDept> list=new ArrayList<>();

        String[] idsArr =  ids.split(",");
        for (String deptId : idsArr) {
            BaseDept baseDept = baseDeptMapper.selectByPrimaryKey(deptId);
            if(StringUtils.isEmpty(baseDept)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            Example example = new Example(BaseDept.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId",deptId);
            List<BaseDept> smtMaterialCategories = baseDeptMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtMaterialCategories)){
                throw new BizErrorException("????????????????????????????????????");
            }

            //????????????????????????
            BaseHtDept baseHtDept =new BaseHtDept();
            BeanUtils.copyProperties(baseDept, baseHtDept);
            baseHtDept.setModifiedUserId(currentUser.getUserId());
            baseHtDept.setModifiedTime(new Date());
            list.add(baseHtDept);


        }
        baseHtDeptMapper.insertList(list);
        i= baseDeptMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseDeptImport> baseDeptImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????
        LinkedList<BaseDept> list = new LinkedList<>();
        LinkedList<BaseHtDept> htList = new LinkedList<>();
        ArrayList<BaseDeptImport> baseDeptImportList = new ArrayList<>();

        for (int i = 0; i < baseDeptImports.size(); i++) {
            BaseDeptImport baseDeptImport = baseDeptImports.get(i);
            String deptCode = baseDeptImport.getDeptCode();
            String deptName = baseDeptImport.getDeptName();
            String factoryCode = baseDeptImport.getFactoryCode();
            String parentCode = baseDeptImport.getParentCode();
            if (StringUtils.isEmpty(
                    deptCode,deptName,factoryCode
            )){
                fail.add(i+4);
                continue;
            }

            //????????????????????????
            Example example = new Example(BaseDept.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("deptCode",deptCode);
            if (StringUtils.isNotEmpty(baseDeptMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //????????????????????????
            Example example1 = new Example(BaseFactory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("factoryCode",factoryCode);
            BaseFactory baseFactory = baseFactoryMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseFactory)){
                fail.add(i+4);
                continue;
            }
            baseDeptImport.setFactoryId(baseFactory.getFactoryId());

            //??????????????????????????????????????????
            boolean tag1 = false;
            if (StringUtils.isNotEmpty(baseDeptImportList)){
                for (BaseDeptImport deptImport : baseDeptImportList) {
                    if (deptImport.getDeptCode().equals(deptCode)){
                        tag1 = true;
                    }
                }
            }
            if (tag1){
                fail.add(i+4);
                continue;
            }

            //???????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(parentCode)){

                //??????????????????????????????
                example.clear();
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria2.andEqualTo("deptCode",parentCode);
                BaseDept baseDept = baseDeptMapper.selectOneByExample(example);

                //???????????????excel???????????????
                boolean tag2 = false;
                if (StringUtils.isNotEmpty(baseDeptImportList)){

                    for (BaseDeptImport deptImport : baseDeptImportList) {
                        if (deptImport.getDeptCode().equals(parentCode)){
                            tag2 = true;
                        }
                    }
                }
                if (!tag2 && StringUtils.isEmpty(baseDept)){
                    fail.add(i+4);
                    continue;
                }
            }else {
                baseDeptImport.setParentId((long) -1);
            }

            baseDeptImportList.add(baseDeptImport);
        }

        if (StringUtils.isNotEmpty(baseDeptImportList)){
            for (BaseDeptImport baseDeptImport : baseDeptImportList) {
                BaseDept baseDept = new BaseDept();
                BeanUtils.copyProperties(baseDeptImport, baseDept);
                baseDept.setCreateTime(new Date());
                baseDept.setCreateUserId(currentUser.getUserId());
                baseDept.setModifiedTime(new Date());
                baseDept.setModifiedUserId(currentUser.getUserId());
                baseDept.setStatus(1);
                baseDept.setOrganizationId(currentUser.getOrganizationId());
                list.add(baseDept);
            }
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseDeptMapper.insertList(list);
        }

        for (BaseDept baseDept : list) {
            BaseHtDept baseHtDept = new BaseHtDept();
            BeanUtils.copyProperties(baseDept, baseHtDept);
            htList.add(baseHtDept);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtDeptMapper.insertList(htList);
        }

        //?????????????????????ID
        for (BaseDept baseDept : list) {
            String parentCode = baseDept.getParentCode();
            Example example = new Example(BaseDept.class);
            if (StringUtils.isNotEmpty(parentCode)){
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("deptCode",parentCode);
                criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
                BaseDept baseDept1 = baseDeptMapper.selectOneByExample(example);
                baseDept.setParentId(baseDept1.getDeptId());
                baseDeptMapper.updateByPrimaryKeySelective(baseDept);
            }
        }

        resutlMap.put("??????????????????",success);
        resutlMap.put("??????????????????",fail);
        return resutlMap;
    }

    @Override
    public List<BaseDept> batchAdd(List<BaseDept> baseDepts ) {
        List<BaseDept> ins = new ArrayList<BaseDept>();
        List<BaseHtDept> baseHtDepts = new ArrayList<BaseHtDept>();

        for(BaseDept baseDept : baseDepts) {
            Example example = new Example(BaseDept.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", baseDept.getOrganizationId());
            criteria.andEqualTo("deptCode", baseDept.getDeptCode());
            BaseDept oldBaseDept = baseDeptMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(oldBaseDept)) {
                baseDept.setDeptId(oldBaseDept.getDeptId());
                baseDeptMapper.updateByPrimaryKey(baseDept);
            }else{
                ins.add(baseDept);
                BaseHtDept baseHtDept =new BaseHtDept();
                BeanUtils.copyProperties(baseDept, baseHtDept);
                baseHtDepts.add(baseHtDept);
            }

        }
        if(StringUtils.isNotEmpty(ins)) {
            baseDeptMapper.insertList(ins);
        }
        //????????????????????????
        if(StringUtils.isNotEmpty(baseHtDepts))
            baseHtDeptMapper.insertList(baseHtDepts);
        return ins;
    }
}
