package com.fantechs.provider.ews.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.ews.EwsWarningEventConfigDto;
import com.fantechs.common.base.general.entity.basic.search.QuartzSearch;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventConfig;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.config.QuartzDoInterface;
import com.fantechs.provider.ews.mapper.EwsWarningEventConfigMapper;
import com.fantechs.provider.ews.service.EwsWarningEventConfigService;
import com.fantechs.provider.ews.service.QuartzManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/27.
 */
@Service
public class EwsWarningEventConfigServiceImpl extends BaseService<EwsWarningEventConfig> implements EwsWarningEventConfigService {

    @Resource
    private EwsWarningEventConfigMapper ewsWarningEventConfigMapper;
    @Resource
    private QuartzManagerService quartzManager;

    private String DEFAULT_GROUP = "DEFAULT";

    @Override
    public List<EwsWarningEventConfigDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return ewsWarningEventConfigMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EwsWarningEventConfig record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setWarningEventIdCode(CodeUtils.getId("SD-"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = ewsWarningEventConfigMapper.insertUseGeneratedKeys(record);
        try {
            if(record.getExecuteStatus()==(byte)1){
                QuartzSearch quartzSearch = new QuartzSearch();
                quartzSearch.setUrl(record.getUrl());
                quartzSearch.setMethod(record.getCallingMethod().toString());
                Map<String,Object> param = JsonUtils.jsonToMap(record.getSendParameter());
                quartzSearch.setMap(param);
                quartzManager.addJob(QuartzDoInterface.class, record.getWarningEventConfigId().toString(), DEFAULT_GROUP,record.getIntervalTime(), ControllerUtil.dynamicConditionByEntity(quartzSearch));
            }
        }catch (Exception e){
            throw new BizErrorException("排程任务添加失败");
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int start(Long Id) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        EwsWarningEventConfig ewsWarningEventConfig = ewsWarningEventConfigMapper.selectByPrimaryKey(Id);
        ewsWarningEventConfig.setModifiedUserId(sysUser.getUserId());
        ewsWarningEventConfig.setModifiedTime(new Date());
        ewsWarningEventConfig.setExecuteStatus((byte)1);
        int num = ewsWarningEventConfigMapper.updateByPrimaryKeySelective(ewsWarningEventConfig);

        //开始任务
        try {
            quartzManager.resumeJob(Id.toString(),DEFAULT_GROUP);
        }catch (Exception e){
            throw new BizErrorException("任务开始失败");
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int stop(Long Id) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        EwsWarningEventConfig ewsWarningEventConfig = ewsWarningEventConfigMapper.selectByPrimaryKey(Id);
        ewsWarningEventConfig.setModifiedUserId(sysUser.getUserId());
        ewsWarningEventConfig.setModifiedTime(new Date());
        ewsWarningEventConfig.setExecuteStatus((byte)2);
        int num = ewsWarningEventConfigMapper.updateByPrimaryKeySelective(ewsWarningEventConfig);

        //开始任务
        try {
            quartzManager.stopJob(Id.toString(), DEFAULT_GROUP);
        }catch (Exception e){
            throw new BizErrorException("任务暂停失败");
        }
        return num;
    }

    @Override
    public int immediately(Long Id) {
        //开始任务
        try {
            quartzManager.immediately(Id.toString(),DEFAULT_GROUP);
        }catch (Exception e){
            throw new BizErrorException("任务开始失败");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EwsWarningEventConfig entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        //更新Job
        try {
            QuartzSearch quartzSearch = new QuartzSearch();
            quartzSearch.setUrl(entity.getUrl());
            quartzSearch.setMethod(entity.getCallingMethod().toString());
            Map<String,Object> param = JsonUtils.jsonToMap(entity.getSendParameter());
            quartzSearch.setMap(param);
            quartzManager.updateJob(entity.getWarningEventConfigId().toString(),DEFAULT_GROUP,entity.getIntervalTime(),ControllerUtil.dynamicConditionByEntity(quartzSearch));

            if(entity.getExecuteStatus()==(byte)1){
                //开始job
                quartzManager.resumeJob(entity.getWarningEventConfigId().toString(),DEFAULT_GROUP);
            }else {
                //暂停job
                quartzManager.stopJob(entity.getWarningEventConfigId().toString(),DEFAULT_GROUP);
            }
        }catch (Exception e){
            throw new BizErrorException("排程修改失败");
        }
        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            EwsWarningEventConfig ewsWarningEventConfig = ewsWarningEventConfigMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(ewsWarningEventConfig)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //删除job
            try {
                quartzManager.deleteJob(ewsWarningEventConfig.getWarningEventConfigId().toString(),DEFAULT_GROUP);
            }catch (Exception e){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(),"任务删除失败");
            }
        }
        return super.batchDelete(ids);
    }
}
