package com.fantechs.provider.quartz.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.SmtProcessSchedulingDto;
import com.fantechs.common.base.general.entity.bcm.SmtProcessScheduling;
import com.fantechs.common.base.general.entity.bcm.search.SearchSmtProcessScheduling;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.quartz.mapper.SmtProcessSchedulingMapper;
import com.fantechs.provider.quartz.service.SmtProcessSchedulingService;
import com.fantechs.provider.quartz.config.QuartzDoInterface;
import com.fantechs.provider.quartz.service.QuartzManagerService;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/03/08.
 */
@Service
public class SmtProcessSchedulingServiceImpl extends BaseService<SmtProcessScheduling> implements SmtProcessSchedulingService {

    @Resource
    private SmtProcessSchedulingMapper smtProcessSchedulingMapper;
    @Resource
    private QuartzManagerService quartzManager;

    private String DEFAULT_GROUP = "DEFAULT";

    @Override
    public List<SmtProcessSchedulingDto> findList(SearchSmtProcessScheduling searchSmtProcessScheduling) {
        return smtProcessSchedulingMapper.findList(searchSmtProcessScheduling);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int start(Long Id) {
        SysUser sysUser = currentUser();
        SmtProcessScheduling smtProcessScheduling = smtProcessSchedulingMapper.selectByPrimaryKey(Id);
        smtProcessScheduling.setProcessSchedulingId(Id);
        smtProcessScheduling.setModifiedUserId(sysUser.getUserId());
        smtProcessScheduling.setModifiedTime(new Date());
        smtProcessScheduling.setExecuteStatus((byte)1);
        int num = smtProcessSchedulingMapper.updateByPrimaryKeySelective(smtProcessScheduling);

        //开始任务
        try {
            if(smtProcessScheduling.getExecuteObjectType()==(byte)0){
                quartzManager.resumeJob(Id.toString(),DEFAULT_GROUP);
            }
        }catch (Exception e){
            throw new BizErrorException("任务开始失败");
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int stop(Long Id) {
        SysUser sysUser = currentUser();
        SmtProcessScheduling smtProcessScheduling = smtProcessSchedulingMapper.selectByPrimaryKey(Id);
        smtProcessScheduling.setProcessSchedulingId(Id);
        smtProcessScheduling.setModifiedUserId(sysUser.getUserId());
        smtProcessScheduling.setModifiedTime(new Date());
        smtProcessScheduling.setExecuteStatus((byte)0);
        int num = smtProcessSchedulingMapper.updateByPrimaryKeySelective(smtProcessScheduling);

        //开始任务
        try {
            if(smtProcessScheduling.getExecuteObjectType()==(byte)0) {
                quartzManager.stopJob(Id.toString(), DEFAULT_GROUP);
            }
        }catch (Exception e){
            throw new BizErrorException("任务暂停失败");
        }
        return num;
    }

    @Override
    public List<Map<String, Object>> detail(Long Id) {
        try {
            SmtProcessScheduling smtProcessScheduling = smtProcessSchedulingMapper.selectByPrimaryKey(Id);
            List<Map<String,Object>> list = new ArrayList<>();
            if(smtProcessScheduling.getExecuteObjectType()==(byte)0){
                list= quartzManager.getJob(Id.toString(),DEFAULT_GROUP);
            }
            return list;
        }catch (Exception e){
            throw new BizErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SmtProcessScheduling record) {
        SysUser sysUser = currentUser();
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        int num = smtProcessSchedulingMapper.insertUseGeneratedKeys(record);
        try {
            if(record.getExecuteObjectType()==(byte)0){
                record.setExecuteStatus((byte)1);
                quartzManager.addJob(QuartzDoInterface.class, record.getProcessSchedulingId().toString(), DEFAULT_GROUP,record.getCron(), ControllerUtil.dynamicConditionByEntity(record.getQuartzSearch()));

            }
        }catch (Exception e){
            throw new BizErrorException("排程任务添加失败");
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(SmtProcessScheduling entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        int num;
        //更新Job
        try {
            if(entity.getExecuteStatus()==(byte)1){
                if(entity.getExecuteObjectType()==(byte)0){
                    quartzManager.updateJob(entity.getProcessSchedulingId().toString(),DEFAULT_GROUP,entity.getCron(),null);
                }
            }
            num = smtProcessSchedulingMapper.updateByPrimaryKeySelective(entity);
        }catch (Exception e){
            throw new BizErrorException("排程修改失败");
        }
       return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        try {
            String[] arrayId = ids.split(",");
            for (String s : arrayId) {
                SmtProcessScheduling smtProcessScheduling = smtProcessSchedulingMapper.selectByPrimaryKey(s);
                if(StringUtils.isEmpty(smtProcessScheduling)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                if(smtProcessScheduling.getExecuteObjectType()==(byte)0){
                    quartzManager.deleteJob(smtProcessScheduling.getProcessSchedulingId().toString(),DEFAULT_GROUP);
                }
            }
        }catch (Exception e){
            throw new BizErrorException("删除失败");
        }
        return smtProcessSchedulingMapper.deleteByIds(ids);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

}
