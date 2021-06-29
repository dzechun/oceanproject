package com.fantechs.provider.ews.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.ews.EwsProcessSchedulingDto;
import com.fantechs.common.base.general.entity.ews.EwsProcessScheduling;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsProcessScheduling;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.config.QuartzDoInterface;
import com.fantechs.provider.ews.mapper.EwsProcessSchedulingMapper;
import com.fantechs.provider.ews.service.EwsProcessSchedulingService;
import com.fantechs.provider.ews.service.QuartzManagerService;
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
public class EwsProcessSchedulingServiceImpl extends BaseService<EwsProcessScheduling> implements EwsProcessSchedulingService {

    @Resource
    private EwsProcessSchedulingMapper ewsProcessSchedulingMapper;
    @Resource
    private QuartzManagerService quartzManager;

    private String DEFAULT_GROUP = "DEFAULT";

    @Override
    public List<EwsProcessSchedulingDto> findList(SearchEwsProcessScheduling searchEwsProcessScheduling) {
        return ewsProcessSchedulingMapper.findList(searchEwsProcessScheduling);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int start(Long Id) {
        SysUser sysUser = currentUser();
        EwsProcessScheduling ewsProcessScheduling = ewsProcessSchedulingMapper.selectByPrimaryKey(Id);
        ewsProcessScheduling.setProcessSchedulingId(Id);
        ewsProcessScheduling.setModifiedUserId(sysUser.getUserId());
        ewsProcessScheduling.setModifiedTime(new Date());
        ewsProcessScheduling.setExecuteStatus((byte)1);
        int num = ewsProcessSchedulingMapper.updateByPrimaryKeySelective(ewsProcessScheduling);

        //开始任务
        try {
            if(ewsProcessScheduling.getExecuteObjectType()==(byte)0){
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
        EwsProcessScheduling ewsProcessScheduling = ewsProcessSchedulingMapper.selectByPrimaryKey(Id);
        ewsProcessScheduling.setProcessSchedulingId(Id);
        ewsProcessScheduling.setModifiedUserId(sysUser.getUserId());
        ewsProcessScheduling.setModifiedTime(new Date());
        ewsProcessScheduling.setExecuteStatus((byte)0);
        int num = ewsProcessSchedulingMapper.updateByPrimaryKeySelective(ewsProcessScheduling);

        //开始任务
        try {
            if(ewsProcessScheduling.getExecuteObjectType()==(byte)0) {
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
            EwsProcessScheduling ewsProcessScheduling = ewsProcessSchedulingMapper.selectByPrimaryKey(Id);
            List<Map<String,Object>> list = new ArrayList<>();
            if(ewsProcessScheduling.getExecuteObjectType()==(byte)0){
                list= quartzManager.getJob(Id.toString(),DEFAULT_GROUP);
            }
            return list;
        }catch (Exception e){
            throw new BizErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EwsProcessScheduling record) {
  //      SysUser sysUser = currentUser();
        record.setCreateTime(new Date());
  //      record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
  //      record.setModifiedUserId(sysUser.getUserId());
        int num = ewsProcessSchedulingMapper.insertUseGeneratedKeys(record);
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
    public int update(EwsProcessScheduling entity) {
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
            num = ewsProcessSchedulingMapper.updateByPrimaryKeySelective(entity);
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
                EwsProcessScheduling ewsProcessScheduling = ewsProcessSchedulingMapper.selectByPrimaryKey(s);
                if(StringUtils.isEmpty(ewsProcessScheduling)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                if(ewsProcessScheduling.getExecuteObjectType()==(byte)0){
                    quartzManager.deleteJob(ewsProcessScheduling.getProcessSchedulingId().toString(),DEFAULT_GROUP);
                }
            }
        }catch (Exception e){
            throw new BizErrorException("删除失败");
        }
        return ewsProcessSchedulingMapper.deleteByIds(ids);
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
