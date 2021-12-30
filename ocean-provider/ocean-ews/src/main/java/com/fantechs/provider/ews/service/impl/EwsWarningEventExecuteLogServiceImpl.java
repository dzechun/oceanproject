package com.fantechs.provider.ews.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.ews.*;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecuteLog;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecutePushLog;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfig;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.ews.mapper.EwsWarningEventExecuteLogMapper;
import com.fantechs.provider.ews.mapper.EwsWarningEventExecutePushLogMapper;
import com.fantechs.provider.ews.mapper.EwsWarningPushConfigMapper;
import com.fantechs.provider.ews.mapper.EwsWarningPushConfigReWuiMapper;
import com.fantechs.provider.ews.service.EwsWarningEventExecuteLogService;
import com.fantechs.provider.ews.utils.DingService;
import com.fantechs.provider.ews.utils.MailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by mr.lei on 2021/12/28.
 */
@Service
public class EwsWarningEventExecuteLogServiceImpl extends BaseService<EwsWarningEventExecuteLog> implements EwsWarningEventExecuteLogService {

    @Resource
    private EwsWarningEventExecuteLogMapper ewsWarningEventExecuteLogMapper;
    @Resource
    private EwsWarningPushConfigMapper ewsWarningPushConfigMapper;
    @Resource
    private EwsWarningPushConfigReWuiMapper ewsWarningPushConfigReWuiMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private EwsWarningEventExecutePushLogMapper ewsWarningEventExecutePushLogMapper;
    @Resource
    private MailService mailService;
    @Resource
    private DingService dingService;

    @Override
    public List<EwsWarningEventExecuteLogDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return ewsWarningEventExecuteLogMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void push() {
        List<UntreatedLogDto> list = ewsWarningEventExecuteLogMapper.findUntreatedLog();
        for (UntreatedLogDto untreatedLogDto : list) {
            //判断是否推送
            if(untreatedLogDto.getMessagePushResult()==2){
                //判断是否已经超时未处理
                if(untreatedLogDto.getUntreatedTime()>untreatedLogDto.getAging()){
                    //超时消息推送至下一级人员 没有绑定下一级人员则直接重复推送
                    Example example = new Example(EwsWarningPushConfig.class);
                    Byte personnelLevel =1;
                    if(untreatedLogDto.getPersonnelLevel()==1){
                        personnelLevel = 2;
                    }else if(untreatedLogDto.getPersonnelLevel()==2){
                        personnelLevel = 3;
                    }else {
                        personnelLevel=4;
                    }
                    example.createCriteria().andEqualTo("warningEventConfigId",untreatedLogDto.getWarningEventConfigId()).andEqualTo("personnelLevel",personnelLevel);
                    EwsWarningPushConfig ewsWarningPushConfig = ewsWarningPushConfigMapper.selectOneByExample(example);
                    if(StringUtils.isNotEmpty(ewsWarningPushConfig)){
                        untreatedLogDto.setWarningPushConfigId(ewsWarningPushConfig.getWarningPushConfigId());
                    }
                    untreatedLogDto.setPersonnelLevel(personnelLevel);
                    untreatedLogDto.setExecuteTime(new Date());
                }
            }
            //获取事件推送人员信息
            List<EwsWarningPushConfigReWuiDto> ewsWarningPushConfigReWuiDtos = ewsWarningPushConfigReWuiMapper.findList(ControllerUtil.dynamicCondition("warningPushConfigId",untreatedLogDto.getWarningPushConfigId()));
            //List<PushMessageDto> pushMessageDtos = new ArrayList<>();
            PushMessageDto pushMessageDto = new PushMessageDto();
            BeanUtils.copyProperties(ewsWarningPushConfigReWuiDtos.get(0),pushMessageDto);
            pushMessageDto.setNotificationMethod(untreatedLogDto.getNotificationMethod());
            pushMessageDto.setExecuteParameter(untreatedLogDto.getExecuteParameter());
            Boolean isOk = false;
            String resMessage = null;
            String message = null;
                switch (pushMessageDto.getNotificationMethod()){
                    case "0":
                        //微信
                        String[] weChat = ewsWarningPushConfigReWuiDtos.stream().map(EwsWarningPushConfigReWuiDto::getWechat).toArray(String[]::new);
                        break;
                    case "1":
                        String[] phone = ewsWarningPushConfigReWuiDtos.stream().map(EwsWarningPushConfigReWuiDto::getMobilePhone).toArray(String[]::new);
                        //短信
                        break;
                    case "2":
                        //钉钉
                        List<String> dingTalk = ewsWarningPushConfigReWuiDtos.stream().map(EwsWarningPushConfigReWuiDto::getDingTalk).collect(Collectors.toList());
                        //获取配置项
                        SearchSysSpecItem searchSysSpecItemDingTalk = new SearchSysSpecItem();
                        searchSysSpecItemDingTalk.setSpecCode("dingTalk_config");
                        List<SysSpecItem> itemListDingTalk = securityFeignApi.findSpecItemList(searchSysSpecItemDingTalk).getData();
                        if(itemListDingTalk.size()<1){
                            throw new BizErrorException("配置项 dingTalk_config 获取失败");
                        }
                        SysSpecItem sysSpecItemDingTalk = itemListDingTalk.get(0);
                        Map<String,Object> dingTalkMap = JSONObject.parseObject(sysSpecItemDingTalk.getParaValue());
                        dingService.Webhook = dingTalkMap.get("Webhook").toString();
                        dingService.secret = dingTalkMap.get("secret").toString();
                        String req = dingService.buildReqStr(false,dingTalk,pushMessageDto.getExecuteParameter());
                        String response = dingService.dingRequest(req);
                        resMessage = response;
                        isOk = true;
                        break;
                    case "3":
                        String[] mail = ewsWarningPushConfigReWuiDtos.stream().map(EwsWarningPushConfigReWuiDto::getEMailAddress).toArray(String[]::new);
                        //邮件
                        SearchSysSpecItem searchSysSpecItemMail = new SearchSysSpecItem();
                        searchSysSpecItemMail.setSpecCode("mail_config");
                        List<SysSpecItem> itemListMail = securityFeignApi.findSpecItemList(searchSysSpecItemMail).getData();
                        if(itemListMail.size()<1){
                            throw new BizErrorException("配置项 mail_config 获取失败");
                        }
                        SysSpecItem sysSpecItemMail = itemListMail.get(0);
                        Map<String ,Object> map = JSONObject.parseObject(sysSpecItemMail.getParaValue());
                        mailService.username = map.get("username").toString();
                        mailService.password = map.get("password").toString();
                        mailService.host = map.get("host").toString();
                        mailService.port = Integer.parseInt(map.get("port").toString());
                        mailService.protocol = map.get("protocol").toString();
                        mailService.sendSimpleMail(mail,"推送",pushMessageDto.getExecuteParameter());
                        isOk = true;
                        break;
                    default:
                }
            //更新执行时间 推送状态
            EwsWarningEventExecutePushLog ewsWarningEventExecutePushLog = new EwsWarningEventExecutePushLog();
            ewsWarningEventExecutePushLog.setWarningEventExecutePushLogId(untreatedLogDto.getWarningEventExecutePushLogId());
            ewsWarningEventExecutePushLog.setPersonnelLevel(untreatedLogDto.getPersonnelLevel());
            ewsWarningEventExecutePushLog.setMessagePushResult(!isOk?(byte)1:(byte)2);
            ewsWarningEventExecutePushLog.setModifiedTime(new Date());
            ewsWarningEventExecutePushLogMapper.updateByPrimaryKeySelective(ewsWarningEventExecutePushLog);

            EwsWarningEventExecuteLog ewsWarningEventExecuteLog = new EwsWarningEventExecuteLog();
            ewsWarningEventExecuteLog.setWarningEventExecuteLogId(untreatedLogDto.getWarningEventExecuteLogId());
            ewsWarningEventExecuteLog.setExecuteTime(untreatedLogDto.getExecuteTime());
            ewsWarningEventExecuteLog.setExecuteResult(!isOk?(byte)2:(byte)1);
            ewsWarningEventExecuteLog.setExecuteResultMessage(resMessage);
            ewsWarningEventExecuteLog.setModifiedTime(new Date());
            ewsWarningEventExecuteLogMapper.updateByPrimaryKeySelective(ewsWarningEventExecuteLog);
        }
    }

    @Override
    public List<LogUreportDto> findLogUreport(Map<String, Object> map) {
        return ewsWarningEventExecuteLogMapper.findLogUreport(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int affirm(Long warningEventExecutePushLogId) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        EwsWarningEventExecutePushLog ewsWarningEventExecutePushLog = ewsWarningEventExecutePushLogMapper.selectByPrimaryKey(warningEventExecutePushLogId);
        if(StringUtils.isEmpty(ewsWarningEventExecutePushLog)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        EwsWarningEventExecuteLog ewsWarningEventExecuteLog = ewsWarningEventExecuteLogMapper.selectByPrimaryKey(ewsWarningEventExecutePushLog.getWarningEventExecuteLogId());
        if(StringUtils.isEmpty(ewsWarningEventExecuteLog)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        ewsWarningEventExecuteLog.setHandleResult((byte)2);
        ewsWarningEventExecuteLog.setModifiedTime(new Date());
        ewsWarningEventExecuteLog.setModifiedUserId(sysUser.getUserId());
        int num = ewsWarningEventExecuteLogMapper.updateByPrimaryKeySelective(ewsWarningEventExecuteLog);

        ewsWarningEventExecutePushLog.setHandleTime(new Date());
        ewsWarningEventExecutePushLog.setModifiedTime(new Date());
        ewsWarningEventExecutePushLog.setModifiedUserId(sysUser.getUserId());
        num+=ewsWarningEventExecutePushLogMapper.updateByPrimaryKeySelective(ewsWarningEventExecutePushLog);

        return num;
    }
}
