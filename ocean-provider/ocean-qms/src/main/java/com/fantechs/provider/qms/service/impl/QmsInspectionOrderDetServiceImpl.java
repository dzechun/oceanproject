package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsInspectionOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */
@Service
public class QmsInspectionOrderDetServiceImpl extends BaseService<QmsInspectionOrderDet> implements QmsInspectionOrderDetService {

    @Resource
    private QmsInspectionOrderDetMapper qmsInspectionOrderDetMapper;
    @Resource
    private QmsInspectionOrderMapper qmsInspectionOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<QmsInspectionOrderDet> findList(Map<String, Object> map) {
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(map.get("inspectionOrderId"));

        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.findList(map);
        if(StringUtils.isNotEmpty(qmsInspectionOrderDets)){
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
                //抽样类型为抽样方案时，去抽样方案取AC、RE、样本数
                if(qmsInspectionOrderDet.getSampleProcessType()==(byte)4){
                    BaseSampleProcess baseSampleProcess = baseFeignApi.getAcReQty(qmsInspectionOrderDet.getSampleProcessId(), qmsInspectionOrder.getOrderQty()).getData();
                    if(StringUtils.isNotEmpty(baseSampleProcess)) {
                        qmsInspectionOrderDet.setSampleQty(baseSampleProcess.getSampleQty());
                        qmsInspectionOrderDet.setAcValue(baseSampleProcess.getAcValue());
                        qmsInspectionOrderDet.setReValue(baseSampleProcess.getReValue());
                    }
                }
            }
        }
        return qmsInspectionOrderDets;
    }
}
