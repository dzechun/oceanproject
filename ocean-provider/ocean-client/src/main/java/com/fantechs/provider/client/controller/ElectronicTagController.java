package com.fantechs.provider.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagStorage;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.server.impl.FanoutSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by lfz on 2020/11/27.
 */
@RestController
@Api(tags = "电子标签控制器")
public class ElectronicTagController {
    @Autowired
    FanoutSender fanoutSender;

    /**
     * 发送需要亮灯的储位、标签信息
     * @param list
     * @throws Exception
     */
    @PostMapping(value="/sendElectronicTagStorage")
    @ApiOperation(value = "发送需要亮灯",notes = "发送需要亮灯")
    public void sendElectronicTagStorage(@RequestBody List<SmtElectronicTagStorageDto> list )  throws Exception{
        MQResponseEntity mQResponseEntity =  new  MQResponseEntity();
        if(StringUtils.isNotEmpty(list)){
            mQResponseEntity.setCode(1001);
            mQResponseEntity.setData(list);
            mQResponseEntity.setCount(list.size());
        }
        fanoutSender.send("ocean.ablepick6",
                JSONObject.toJSONString(mQResponseEntity));
    }

}
