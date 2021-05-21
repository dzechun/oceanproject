package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionWay;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleProcess;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSampleProcessMapper;
import com.fantechs.provider.base.mapper.BaseSampleProcessMapper;
import com.fantechs.provider.base.service.BaseSampleProcessService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */
@Service
public class BaseSampleProcessServiceImpl extends BaseService<BaseSampleProcess> implements BaseSampleProcessService {

    @Resource
    private BaseSampleProcessMapper baseSampleProcessMapper;
    @Resource
    private BaseHtSampleProcessMapper baseHtSampleProcessMapper;

    @Override
    public List<BaseSampleProcess> findList(Map<String, Object> map) {
        return baseSampleProcessMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseSampleProcess baseSampleProcess) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseSampleProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sampleProcessCode", baseSampleProcess.getSampleProcessCode());
        BaseSampleProcess baseSampleProcess1 = baseSampleProcessMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSampleProcess1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseSampleProcess.setCreateUserId(user.getUserId());
        baseSampleProcess.setCreateTime(new Date());
        baseSampleProcess.setModifiedUserId(user.getUserId());
        baseSampleProcess.setModifiedTime(new Date());
        baseSampleProcess.setStatus(StringUtils.isEmpty(baseSampleProcess.getStatus())?1: baseSampleProcess.getStatus());
        baseSampleProcess.setOrgId(user.getOrganizationId());
        int i = baseSampleProcessMapper.insertUseGeneratedKeys(baseSampleProcess);

        BaseHtSampleProcess baseHtSampleProcess = new BaseHtSampleProcess();
        BeanUtils.copyProperties(baseSampleProcess, baseHtSampleProcess);
        baseHtSampleProcessMapper.insert(baseHtSampleProcess);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseSampleProcess baseSampleProcess) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseSampleProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sampleProcessCode", baseSampleProcess.getSampleProcessCode())
                .andNotEqualTo("sampleProcessId",baseSampleProcess.getSampleProcessId());
        BaseSampleProcess baseSampleProcess1 = baseSampleProcessMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSampleProcess1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseSampleProcess.setModifiedTime(new Date());
        baseSampleProcess.setModifiedUserId(user.getUserId());
        baseSampleProcess.setOrgId(user.getOrganizationId());
        int i = baseSampleProcessMapper.updateByPrimaryKey(baseSampleProcess);

        BaseHtSampleProcess baseHtSampleProcess = new BaseHtSampleProcess();
        BeanUtils.copyProperties(baseSampleProcess, baseHtSampleProcess);
        baseHtSampleProcessMapper.insert(baseHtSampleProcess);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtSampleProcess> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseSampleProcess baseSampleProcess = baseSampleProcessMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseSampleProcess)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtSampleProcess baseHtSampleProcess = new BaseHtSampleProcess();
            BeanUtils.copyProperties(baseSampleProcess, baseHtSampleProcess);
            list.add(baseHtSampleProcess);
        }

        baseHtSampleProcessMapper.insertList(list);

        return baseSampleProcessMapper.deleteByIds(ids);
    }
}
