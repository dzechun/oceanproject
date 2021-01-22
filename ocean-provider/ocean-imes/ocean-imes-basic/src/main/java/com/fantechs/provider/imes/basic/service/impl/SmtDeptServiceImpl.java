package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtMaterialCategory;
import com.fantechs.common.base.entity.basic.history.SmtHtDept;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtDeptMapper;
import com.fantechs.provider.imes.basic.mapper.SmtHtDeptMapper;
import com.fantechs.provider.imes.basic.service.SmtDeptService;
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
    public Map<String, Object> importExcel(List<SmtDept> smtDepts) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtDept> list = new LinkedList<>();
        LinkedList<SmtHtDept> htList = new LinkedList<>();
        for (int i = 0; i < smtDepts.size(); i++) {
            SmtDept smtDept = smtDepts.get(i);
            String deptCode = smtDept.getDeptCode();
            String deptName = smtDept.getDeptName();
            if (StringUtils.isEmpty(
                    deptCode,deptName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtDept.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("deptCode",smtDept.getDeptCode());
            if (StringUtils.isNotEmpty(smtDeptMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            smtDept.setCreateTime(new Date());
            smtDept.setCreateUserId(currentUser.getUserId());
            smtDept.setModifiedTime(new Date());
            smtDept.setModifiedUserId(currentUser.getUserId());
            list.add(smtDept);
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
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
