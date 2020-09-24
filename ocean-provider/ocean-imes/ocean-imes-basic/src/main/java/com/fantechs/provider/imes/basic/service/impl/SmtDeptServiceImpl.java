package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.history.SmtHtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtDeptMapper;
import com.fantechs.provider.imes.basic.mapper.SmtHtDeptMapper;
import com.fantechs.provider.imes.basic.service.SmtDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SmtDeptServiceImpl extends BaseService<SmtDept> implements SmtDeptService {

    @Resource
    private SmtDeptMapper smtDeptMapper;

    @Resource
    private SmtHtDeptMapper smtHtDeptMapper;

    @Override
    public List<SmtDept> selectDepts(SearchSmtDept searchSmtDept) {
        return smtDeptMapper.selectDepts(searchSmtDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtDept smtDept) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deptCode",smtDept.getDeptCode());
        List<SmtDept> smtDepts = smtDeptMapper.selectByExample(example);
        if(null!=smtDepts&&smtDepts.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        Example example1 = new Example(SmtDept.class);
        Example.Criteria criteria1= example1.createCriteria();
        criteria1.andEqualTo("factoryId",smtDept.getFactoryId());
        criteria1.andEqualTo("deptName",smtDept.getDeptName());
        SmtDept dept = smtDeptMapper.selectOneByExample(example1);
        if(StringUtils.isNotEmpty(dept)){
            throw new BizErrorException("该工厂里面部门名称已存在");
        }

        smtDept.setCreateUserId(currentUser.getUserId());
        smtDept.setCreateTime(new Date());
        int i = smtDeptMapper.insertSelective(smtDept);

        //新增部门历史信息
        SmtHtDept smtHtDept=new SmtHtDept();
        BeanUtils.copyProperties(smtDept,smtHtDept);
        smtHtDeptMapper.insertSelective(smtHtDept);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtDept smtDept) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deptCode",smtDept.getDeptCode());
        SmtDept dept = smtDeptMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(dept)&&!dept.getDeptId().equals(smtDept.getDeptId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtDept.setModifiedUserId(currentUser.getUserId());
        smtDept.setModifiedTime(new Date());
        int i= smtDeptMapper.updateByPrimaryKeySelective(smtDept);

        //新增部门历史信息
        SmtHtDept smtHtDept=new SmtHtDept();
        BeanUtils.copyProperties(smtDept,smtHtDept);
        smtHtDept.setModifiedUserId(currentUser.getUserId());
        smtHtDept.setModifiedTime(new Date());
        smtHtDeptMapper.insertSelective(smtHtDept);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<Long> deptIds) {
        int i=0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<SmtHtDept> list=new ArrayList<>();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        for (Long deptId : deptIds) {
            SmtDept smtDept = smtDeptMapper.selectByPrimaryKey(deptId);
            if(StringUtils.isEmpty(smtDept)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增部门历史信息
            SmtHtDept smtHtDept=new SmtHtDept();
            BeanUtils.copyProperties(smtDept,smtHtDept);
            smtHtDept.setModifiedUserId(currentUser.getUserId());
            smtHtDept.setModifiedTime(new Date());
            list.add(smtHtDept);

            smtDeptMapper.deleteByPrimaryKey(deptId);
        }

        smtHtDeptMapper.insertList(list);
        return i;
    }

    @Override
    public List<SmtDept> exportDepts(SearchSmtDept searchSmtDept) {
        List<SmtDept> smtDepts = this.selectDepts(searchSmtDept);
        return smtDepts;
    }

    @Override
    public List<SmtDept> selectDeptByFactoryId(String factoryId) {
        Example example = new Example(SmtDept.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("factoryId",factoryId);
        List<SmtDept> smtDepts = smtDeptMapper.selectByExample(example);
        return smtDepts;
    }

    @Override
    public SmtDept selectByKey(Long id) {
        return smtDeptMapper.selectByPrimaryKey(id);
    }
}
