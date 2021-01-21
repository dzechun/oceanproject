package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseStaffDto;
import com.fantechs.common.base.general.entity.basic.BaseStaff;
import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStaff;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtStaffMapper;
import com.fantechs.provider.base.mapper.BaseStaffMapper;
import com.fantechs.provider.base.service.BaseStaffProcessService;
import com.fantechs.provider.base.service.BaseStaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/16.
 */
@Service
public class BaseStaffServiceImpl extends BaseService<BaseStaff> implements BaseStaffService {

    @Resource
    private BaseStaffMapper baseStaffMapper;
    @Resource
    private BaseHtStaffMapper baseHtStaffMapper;
    @Resource
    private BaseStaffProcessService baseStaffProcessService;

    @Override
    public List<BaseStaffDto> findList(Map<String, Object> map) {
        return baseStaffMapper.findList(map);
    }

    @Override
    public int save(BaseStaff baseStaff) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(BaseStaff.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("staffCode", baseStaff.getStaffCode());
        BaseStaff baseStaff1 = baseStaffMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStaff1)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增人员信息
        baseStaff.setCreateTime(new Date());
        baseStaff.setCreateUserId(user.getUserId());
        baseStaff.setModifiedTime(new Date());
        baseStaff.setModifiedUserId(user.getUserId());
        int i = baseStaffMapper.insertUseGeneratedKeys(baseStaff);

        //新增人员信息和工种关系
        List<BaseStaffProcess> baseStaffProcessList = baseStaff.getBaseStaffProcessList();
        if (StringUtils.isNotEmpty(baseStaffProcessList)){
            for (BaseStaffProcess baseStaffProcess : baseStaffProcessList) {
                baseStaffProcess.setStaffId(baseStaff.getStaffId());
            }
            baseStaffProcessService.batchSave(baseStaff.getBaseStaffProcessList());
        }

        //新增人员信息履历
        BaseHtStaff baseHtStaff = new BaseHtStaff();
        BeanUtils.copyProperties(baseStaff, baseHtStaff);
        baseHtStaffMapper.insertSelective(baseHtStaff);

        return i;
    }

    @Override
    public int update(BaseStaff baseStaff) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(BaseStaff.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("staffCode", baseStaff.getStaffCode())
                .andNotEqualTo("staffId", baseStaff.getStaffId());
        BaseStaff baseStaff1 = baseStaffMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStaff1)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //更新人员信息
        baseStaff.setModifiedUserId(user.getUserId());
        baseStaff.setModifiedTime(new Date());
        int i = baseStaffMapper.updateByPrimaryKeySelective(baseStaff);

        //更新人员工种关系
        List<BaseStaffProcess> baseStaffProcessList = baseStaff.getBaseStaffProcessList();
        if (StringUtils.isNotEmpty(baseStaffProcessList)) {
            Long processId = baseStaffProcessList.get(0).getProcessId();
            Example example1 = new Example(BaseStaffProcess.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("processId", processId);
            baseStaffProcessService.deleteByExample(example1);

            for (BaseStaffProcess baseStaffProcess : baseStaffProcessList) {
                baseStaffProcess.setStaffId(baseStaff.getStaffId());
                baseStaffProcessService.batchSave(baseStaff.getBaseStaffProcessList());
            }
        }


        //新增人员信息履历
        BaseHtStaff baseHtStaff = new BaseHtStaff();
        BeanUtils.copyProperties(baseStaff, baseHtStaff);
        baseHtStaffMapper.insertSelective(baseHtStaff);

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseStaff baseStaff = baseStaffMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseStaff)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //删除该员工绑定的工种
            Example example = new Example(BaseStaffProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("staffId", id);
            baseStaffProcessService.deleteByExample(example);
        }
        return baseStaffMapper.deleteByIds(ids);
    }
}
