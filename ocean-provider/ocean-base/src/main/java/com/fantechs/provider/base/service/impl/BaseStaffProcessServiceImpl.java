package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseStaffProcessMapper;
import com.fantechs.provider.base.service.BaseStaffProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/16.
 */
@Service
public class BaseStaffProcessServiceImpl extends BaseService<BaseStaffProcess> implements BaseStaffProcessService {

    @Resource
    private BaseStaffProcessMapper baseStaffProcessMapper;

    @Override
    public List<BaseStaffProcess> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseStaffProcessMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStaffProcess baseStaffProcess) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //判断员工和工作的关系是否存在
        Example example = new Example(BaseStaffProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("staffId",baseStaffProcess.getStaffId())
                .andEqualTo("processId",baseStaffProcess.getProcessId());
        BaseStaffProcess baseStaffProcess1 = baseStaffProcessMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStaffProcess1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        return baseStaffProcessMapper.insertUseGeneratedKeys(baseStaffProcess);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseStaffProcess baseStaffProcess) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //判断员工和工作的关系是否存在
        Example example = new Example(BaseStaffProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("staffId",baseStaffProcess.getStaffId())
                .andEqualTo("processId",baseStaffProcess.getProcessId())
                .andNotEqualTo("staffProcessId",baseStaffProcess.getStaffProcessId());
        BaseStaffProcess baseStaffProcess1 = baseStaffProcessMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStaffProcess1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        return baseStaffProcessMapper.updateByPrimaryKeySelective(baseStaffProcess);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseStaffProcess baseStaffProcess = baseStaffProcessMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseStaffProcess)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }

        return baseStaffProcessMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<BaseStaffProcess> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        for (BaseStaffProcess baseStaffProcess : list) {

            //判断员工和工作的关系是否存在
            Example example = new Example(BaseStaffProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("staffId",baseStaffProcess.getStaffId())
                    .andEqualTo("processId",baseStaffProcess.getProcessId());
            BaseStaffProcess baseStaffProcess1 = baseStaffProcessMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseStaffProcess1)){
                throw new BizErrorException("绑定关系已存在");
            }

            baseStaffProcess.setCreateTime(new Date());
            baseStaffProcess.setModifiedTime(new Date());
            baseStaffProcess.setCreateUserId(user.getUserId());
            baseStaffProcess.setModifiedUserId(user.getUserId());
        }

        return baseStaffProcessMapper.insertList(list);
    }
}
