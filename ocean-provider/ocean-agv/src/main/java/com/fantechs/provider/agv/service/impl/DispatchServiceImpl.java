package com.fantechs.provider.agv.service.impl;

import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.agv.service.DispatchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RefreshScope
public class DispatchServiceImpl implements DispatchService {

    @Value("${rcsUrl}")
    private String url;

    @Override
    public String genAgvSchedulingTask(Map<String, Object> data) {

        data.put("reqCode", UUIDUtils.getUUID());
        String s = RestTemplateUtil.postForString(url + "/genAgvSchedulingTask", data);
        return s;
    }

    @Override
    public String continueTask(Map<String, Object> data) {

        data.put("reqCode", UUIDUtils.getUUID());
        String s = RestTemplateUtil.postForString(url + "/continueTask", data);
        return s;
    }

}
