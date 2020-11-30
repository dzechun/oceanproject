package com.fantechs.provider.client.controller;

import com.fantechs.common.base.electronic.entity.SmtElectronicTagStorage;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.server.impl.FanoutSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by lfz on 2020/11/27.
 */
@RestController
public class ElectronicTagController {
    @Autowired
    FanoutSender fanoutSender;

    /**
     * 发送需要亮灯的储位、标签信息
     * @param list
     * @throws Exception
     */
    @PostMapping(value="/sendElectronicTagStorage")
    public void sendElectronicTagStorage(@ModelAttribute("list") List<SmtElectronicTagStorage> list )  throws Exception{
        MQResponseEntity mQResponseEntity =  new  MQResponseEntity();
        if(StringUtils.isNotEmpty(list)){
            mQResponseEntity.setCode(2);
            mQResponseEntity.setData(list);
            mQResponseEntity.setSnedTime(new Date());
            mQResponseEntity.setCount(list.size());
        }
        fanoutSender.send(RabbitConfig.TOPIC_EXCHANGE,RabbitConfig.TOPIC_QUEUE1,mQResponseEntity);
    }
}
