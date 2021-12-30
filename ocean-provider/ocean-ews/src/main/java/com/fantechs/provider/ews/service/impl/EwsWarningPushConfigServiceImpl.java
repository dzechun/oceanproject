package com.fantechs.provider.ews.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.ews.EwsHtWarningPushConfigDto;
import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigDto;
import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigReWuiDto;
import com.fantechs.common.base.general.entity.ews.EwsHtWarningPushConfig;
import com.fantechs.common.base.general.entity.ews.EwsHtWarningPushConfigReWui;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfig;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfigReWui;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.mapper.EwsHtWarningPushConfigMapper;
import com.fantechs.provider.ews.mapper.EwsHtWarningPushConfigReWuiMapper;
import com.fantechs.provider.ews.mapper.EwsWarningPushConfigMapper;
import com.fantechs.provider.ews.mapper.EwsWarningPushConfigReWuiMapper;
import com.fantechs.provider.ews.service.EwsWarningPushConfigService;
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
 * Created by mr.lei on 2021/12/27.
 */
@Service
public class EwsWarningPushConfigServiceImpl extends BaseService<EwsWarningPushConfig> implements EwsWarningPushConfigService {

    @Resource
    private EwsWarningPushConfigMapper ewsWarningPushConfigMapper;
    @Resource
    private EwsWarningPushConfigReWuiMapper ewsWarningPushConfigReWuiMapper;
    @Resource
    private EwsHtWarningPushConfigMapper ewsHtWarningPushConfigMapper;
    @Resource
    private EwsHtWarningPushConfigReWuiMapper ewsHtWarningPushConfigReWuiMapper;

    @Override
    public List<EwsWarningPushConfigDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return ewsWarningPushConfigMapper.findList(map);
    }

    @Override
    public List<EwsHtWarningPushConfigDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return ewsHtWarningPushConfigMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EwsWarningPushConfig record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(record.getWarningEventConfigId(),record.getPersonnelLevel(),record.getNotificationMethod(),record.getHandleTimeUnit())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        //查询是否存在同等级
        Example example = new Example(EwsWarningPushConfig.class);
        example.createCriteria().andEqualTo("warningEventConfigId",record.getWarningEventConfigId()).andEqualTo("personnelLevel",record.getPersonnelLevel());
        List<EwsWarningPushConfig> ewsWarningPushConfigs = ewsWarningPushConfigMapper.selectByExample(example);
        if(ewsWarningPushConfigs.size()>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"事件已配置当前级别人员");
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = ewsWarningPushConfigMapper.insertUseGeneratedKeys(record);

        EwsHtWarningPushConfig ewsHtWarningPushConfig = new EwsHtWarningPushConfig();
        BeanUtils.copyProperties(record,ewsHtWarningPushConfig);
        ewsHtWarningPushConfigMapper.insertSelective(ewsHtWarningPushConfig);

        List<EwsWarningPushConfigReWui> list = new ArrayList<>();
        List<EwsHtWarningPushConfigReWui> ewsHtWarningPushConfigReWuis = new ArrayList<>();
        for (EwsWarningPushConfigReWuiDto ewsWarningPushConfigReWuiDto : record.getEwsWarningPushConfigReWuiDtos()) {
            EwsWarningPushConfigReWui ewsWarningPushConfigReWui = new EwsWarningPushConfigReWui();
            BeanUtils.copyProperties(ewsWarningPushConfigReWuiDto,ewsWarningPushConfigReWui);
            ewsWarningPushConfigReWui.setWarningPushConfigId(record.getWarningPushConfigId());
            ewsWarningPushConfigReWui.setCreateTime(new Date());
            ewsWarningPushConfigReWui.setCreateUserId(sysUser.getUserId());
            ewsWarningPushConfigReWui.setModifiedUserId(sysUser.getUserId());
            ewsWarningPushConfigReWui.setModifiedTime(new Date());
            ewsWarningPushConfigReWui.setOrgId(sysUser.getOrganizationId());
            list.add(ewsWarningPushConfigReWui);

            EwsHtWarningPushConfigReWui ewsHtWarningPushConfigReWui = new EwsHtWarningPushConfigReWui();
            BeanUtils.copyProperties(ewsWarningPushConfigReWui,ewsHtWarningPushConfigReWui);
            ewsHtWarningPushConfigReWuis.add(ewsHtWarningPushConfigReWui);
        }
        if(list.size()>0){
            ewsWarningPushConfigReWuiMapper.insertList(list);
            ewsHtWarningPushConfigReWuiMapper.insertList(ewsHtWarningPushConfigReWuis);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EwsWarningPushConfig entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(entity.getWarningEventConfigId(),entity.getPersonnelLevel(),entity.getNotificationMethod(),entity.getHandleTimeUnit())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        //查询是否存在同等级
        Example example = new Example(EwsWarningPushConfig.class);
        example.createCriteria().andEqualTo("warningEventConfigId",entity.getWarningEventConfigId()).andEqualTo("personnelLevel",entity.getPersonnelLevel()).andNotEqualTo("warningPushConfigId",entity.getWarningPushConfigId());
        List<EwsWarningPushConfig> ewsWarningPushConfigs = ewsWarningPushConfigMapper.selectByExample(example);
        if(ewsWarningPushConfigs.size()>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"事件已配置当前级别人员");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());

        EwsHtWarningPushConfig ewsHtWarningPushConfig = new EwsHtWarningPushConfig();
        BeanUtils.copyProperties(entity,ewsHtWarningPushConfig);
        ewsHtWarningPushConfigMapper.insertSelective(ewsHtWarningPushConfig);

        //删除原有数据
        example = new Example(EwsWarningPushConfigReWui.class);
        example.createCriteria().andEqualTo("warningPushConfigId",entity.getWarningPushConfigId());
        ewsWarningPushConfigReWuiMapper.deleteByExample(example);

        List<EwsWarningPushConfigReWui> list = new ArrayList<>();
        List<EwsHtWarningPushConfigReWui> ewsHtWarningPushConfigReWuis = new ArrayList<>();
        for (EwsWarningPushConfigReWuiDto ewsWarningPushConfigReWuiDto : entity.getEwsWarningPushConfigReWuiDtos()) {
            EwsWarningPushConfigReWui ewsWarningPushConfigReWui = new EwsWarningPushConfigReWui();
            BeanUtils.copyProperties(ewsWarningPushConfigReWuiDto,ewsWarningPushConfigReWui);
            ewsWarningPushConfigReWui.setWarningPushConfigId(entity.getWarningPushConfigId());
            ewsWarningPushConfigReWui.setCreateTime(new Date());
            ewsWarningPushConfigReWui.setCreateUserId(sysUser.getUserId());
            ewsWarningPushConfigReWui.setModifiedUserId(sysUser.getUserId());
            ewsWarningPushConfigReWui.setModifiedTime(new Date());
            ewsWarningPushConfigReWui.setOrgId(sysUser.getOrganizationId());
            list.add(ewsWarningPushConfigReWui);

            EwsHtWarningPushConfigReWui ewsHtWarningPushConfigReWui = new EwsHtWarningPushConfigReWui();
            BeanUtils.copyProperties(ewsWarningPushConfigReWui,ewsHtWarningPushConfigReWui);
            ewsHtWarningPushConfigReWuis.add(ewsHtWarningPushConfigReWui);
        }
        if(list.size()>0){
            ewsWarningPushConfigReWuiMapper.insertList(list);
            ewsHtWarningPushConfigReWuiMapper.insertList(ewsHtWarningPushConfigReWuis);
        }
        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            EwsWarningPushConfig ewsWarningPushConfig = ewsWarningPushConfigMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(ewsWarningPushConfig)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //删除原有数据
            Example example = new Example(EwsWarningPushConfigReWui.class);
            example.createCriteria().andEqualTo("warningPushConfigId",ewsWarningPushConfig.getWarningPushConfigId());
            ewsWarningPushConfigReWuiMapper.deleteByExample(example);
        }
        return super.batchDelete(ids);
    }
}
