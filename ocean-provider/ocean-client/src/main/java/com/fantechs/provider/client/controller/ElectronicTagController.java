package com.fantechs.provider.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.server.impl.FanoutSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    public void sendElectronicTagStorage(@RequestBody List<SmtElectronicTagStorageDto> list ) {
        for(SmtElectronicTagStorageDto smtElectronicTagStorageDto :list){
            MQResponseEntity mQResponseEntity =  new  MQResponseEntity();
            mQResponseEntity.setCode(1001);
            mQResponseEntity.setData(smtElectronicTagStorageDto);
            fanoutSender.send(smtElectronicTagStorageDto.getQueueName(),
                    JSONObject.toJSONString(mQResponseEntity));
        }


    }

    public static void main(String[] args) {


                List<String> list = new ArrayList<>();
                list.add("aa");
                list.add("bb");
                list.add("cc");
                CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<String>(list);
                for (String str : cowList) {
                    if ("aa".equals(str)) {
                        cowList.remove(str);
                    }
                }
                System.out.println(cowList.size());

    }

}