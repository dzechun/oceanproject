package com.fantechs.provider.client.listener;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagController;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.provider.api.basic.ClientManageFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.imes.basic.mapper.SmtClientManageMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Action;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class HeartBeatReceiver {

    @Autowired
    private ClientManageFeignApi clientManageFeignApi;

    // 设备监听队列
    @RabbitListener(queues = RabbitConfig.TOPIC_HEARTBEAT_QUEUE)
    public void heartBeatStatus(SmtClientManage smtClientManage) {
        smtClientManage.setMonitoringTime(new Date());
        smtClientManage.setLoginTag((byte) 1);
        clientManageFeignApi.update(smtClientManage);
        //fanoutSender.send("testtopic",messageStr);
    }


}
