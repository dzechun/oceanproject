package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
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
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",user.getOrganizationId());

        //传检验单id，查对应的检验单明细
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.findList(map).get(0);
        SearchQmsInspectionOrderDet searchQmsInspectionOrderDet = new SearchQmsInspectionOrderDet();
        searchQmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDet));
        QmsInspectionOrder inspectionOrder = this.getAcReQty(qmsInspectionOrder, qmsInspectionOrderDets);

        return inspectionOrder.getQmsInspectionOrderDets();
    }

    public QmsInspectionOrder getAcReQty(QmsInspectionOrder qmsInspectionOrder, List<QmsInspectionOrderDet> qmsInspectionOrderDets){
        if(StringUtils.isNotEmpty(qmsInspectionOrderDets)){
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
                //抽样类型为抽样方案时，去抽样方案取AC、RE、样本数
                if(qmsInspectionOrderDet.getSampleProcessType()!=null&&qmsInspectionOrderDet.getSampleProcessType()==(byte)4){
                    BaseSampleProcess baseSampleProcess = baseFeignApi.getAcReQty(qmsInspectionOrderDet.getSampleProcessId(), qmsInspectionOrder.getOrderQty()).getData();
                    if(StringUtils.isNotEmpty(baseSampleProcess)) {
                        qmsInspectionOrderDet.setSampleQty(baseSampleProcess.getSampleQty());
                        qmsInspectionOrderDet.setAcValue(baseSampleProcess.getAcValue());
                        qmsInspectionOrderDet.setReValue(baseSampleProcess.getReValue());
                    }
                }
            }
            qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
        }
        return qmsInspectionOrder;
    }
}
