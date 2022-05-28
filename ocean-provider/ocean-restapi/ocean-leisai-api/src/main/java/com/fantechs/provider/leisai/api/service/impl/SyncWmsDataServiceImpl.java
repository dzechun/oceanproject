package com.fantechs.provider.leisai.api.service.impl;

import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCarton;
import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCartonDet;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.leisai.api.mapper.WmsCartonDetMapper;
import com.fantechs.provider.leisai.api.mapper.WmsCartonMapper;
import com.fantechs.provider.leisai.api.service.SyncWmsDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SyncWmsDataServiceImpl implements SyncWmsDataService {

    @Resource
    private WmsCartonMapper wmsCartonMapper;

    @Resource
    private WmsCartonDetMapper wmsCartonDetMapper;

    @Resource
    private AuthFeignApi securityFeignApi;

    @Override
    public void syncCartonData(LeisaiWmsCarton leisaiWmsCarton) {
        long start = System.currentTimeMillis();

        // 保存WMS库
        //DynamicDataSourceHolder.putDataSouce("thirdary");
        wmsCartonMapper.save(leisaiWmsCarton);

        //保存WMS库 明细
        List<LeisaiWmsCartonDet> detList=leisaiWmsCarton.getLeisaiWmsCartonDetList();
        if(detList.size()>0){
            for (LeisaiWmsCartonDet leisaiWmsCartonDet : detList) {
                wmsCartonDetMapper.save(leisaiWmsCartonDet);
            }
        }
        //DynamicDataSourceHolder.removeDataSource();

        // 记录日志
        SysApiLog apiLog = new SysApiLog();
        apiLog.setCreateTime(new Date());
        apiLog.setThirdpartySysName("雷赛数据库对接");
        apiLog.setCallType((byte) 1);
        apiLog.setCallResult((byte) 1);
        apiLog.setApiModule("ocean-leisai-api");
        apiLog.setApiUrl("同步包箱条码及明细到WMS数据库表");
        apiLog.setRequestParameter(leisaiWmsCarton.toString());
        apiLog.setRequestTime(new Date());
        apiLog.setConsumeTime(new BigDecimal(System.currentTimeMillis() - start));

        securityFeignApi.add(apiLog);

    }

    @Override
    public void syncCartonDetData() {

    }
}
