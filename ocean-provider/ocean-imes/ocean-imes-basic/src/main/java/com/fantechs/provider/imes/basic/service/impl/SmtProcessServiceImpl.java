package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.history.SmtHtProcess;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcess;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProcessMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProcessMapper;
import com.fantechs.provider.imes.basic.service.SmtProcessService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/25.
 */
@Service
public class SmtProcessServiceImpl  extends BaseService<SmtProcess> implements SmtProcessService {

    @Resource
    private SmtProcessMapper smtProcessMapper;

    @Resource
    private SmtHtProcessMapper smtHtProcessMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtProcess smtProcess) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCode",smtProcess.getProcessCode());
        List<SmtProcess> smtProcesses = smtProcessMapper.selectByExample(example);
        if(null!=smtProcesses&&smtProcesses.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProcess.setCreateUserId(currentUser.getUserId());
        smtProcess.setCreateTime(new Date());
        smtProcessMapper.insertUseGeneratedKeys(smtProcess);

        //新增工序历史信息
        SmtHtProcess smtHtProcess=new SmtHtProcess();
        BeanUtils.copyProperties(smtProcess,smtHtProcess);
        int i = smtHtProcessMapper.insertSelective(smtHtProcess);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDel(String ids) {
        int i=0;
        List<SmtHtProcess> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] processIds = ids.split(",");
        for (String processId : processIds) {
            SmtProcess smtProcess = smtProcessMapper.selectByPrimaryKey(Long.parseLong(processId));
            if(StringUtils.isEmpty(smtProcess)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增工序历史信息
            SmtHtProcess smtHtProcess=new SmtHtProcess();
            BeanUtils.copyProperties(smtProcess,smtHtProcess);
            smtHtProcess.setModifiedUserId(currentUser.getUserId());
            smtHtProcess.setModifiedTime(new Date());
            list.add(smtHtProcess);
        }
        smtHtProcessMapper.insertList(list);

        return smtProcessMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtProcess smtProcess) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCode",smtProcess.getProcessCode());

        SmtProcess process = smtProcessMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(process)&&!process.getProcessId().equals(smtProcess.getProcessId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProcess.setModifiedUserId(currentUser.getUserId());
        smtProcess.setModifiedTime(new Date());
        int i= smtProcessMapper.updateByPrimaryKeySelective(smtProcess);

        //新增工序历史信息
        SmtHtProcess smtHtProcess=new SmtHtProcess();
        BeanUtils.copyProperties(smtProcess,smtHtProcess);
        smtHtProcess.setCreateUserId(smtProcess.getCreateUserId());
        smtHtProcess.setCreateTime(smtProcess.getCreateTime());
        smtHtProcess.setModifiedUserId(currentUser.getUserId());
        smtHtProcess.setModifiedTime(new Date());
        smtHtProcessMapper.insertSelective(smtHtProcess);
        return i;
    }

    @Override
    public SmtProcess selectById(Long id) {
        return smtProcessMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SmtProcess> findList(SearchSmtProcess searchSmtProcess) {
        return smtProcessMapper.findList(searchSmtProcess);
    }
}
