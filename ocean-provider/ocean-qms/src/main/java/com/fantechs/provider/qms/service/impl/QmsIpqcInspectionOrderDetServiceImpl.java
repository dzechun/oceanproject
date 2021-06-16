package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */
@Service
public class QmsIpqcInspectionOrderDetServiceImpl extends BaseService<QmsIpqcInspectionOrderDet> implements QmsIpqcInspectionOrderDetService {

    @Resource
    private QmsIpqcInspectionOrderDetMapper qmsIpqcInspectionOrderDetMapper;
    @Resource
    private QmsIpqcInspectionOrderMapper qmsIpqcInspectionOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<QmsIpqcInspectionOrderDet> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",user.getOrganizationId());

        //传IPQC检验单id，查对应的IPQC检验单明细
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.findList(map).get(0);
        SearchQmsIpqcInspectionOrderDet searchQmsIpqcInspectionOrderDet = new SearchQmsIpqcInspectionOrderDet();
        searchQmsIpqcInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDet));
        QmsIpqcInspectionOrder ipqcInspectionOrder = this.getAcReQty(qmsIpqcInspectionOrder, qmsIpqcInspectionOrderDets);

        return ipqcInspectionOrder.getQmsIpqcInspectionOrderDets();
    }


    public QmsIpqcInspectionOrder getAcReQty(QmsIpqcInspectionOrder qmsIpqcInspectionOrder, List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets){
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)){
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet : qmsIpqcInspectionOrderDets){
                //抽样类型为抽样方案时，去抽样方案取AC、RE、样本数
                if(qmsIpqcInspectionOrderDet.getSampleProcessType()!=null&&qmsIpqcInspectionOrderDet.getSampleProcessType()==(byte)4){
                    BaseSampleProcess baseSampleProcess = baseFeignApi.getAcReQty(qmsIpqcInspectionOrderDet.getSampleProcessId(), qmsIpqcInspectionOrder.getQty()).getData();
                    if(StringUtils.isNotEmpty(baseSampleProcess)) {
                        qmsIpqcInspectionOrderDet.setSampleQty(baseSampleProcess.getSampleQty());
                        qmsIpqcInspectionOrderDet.setAcValue(baseSampleProcess.getAcValue());
                        qmsIpqcInspectionOrderDet.setReValue(baseSampleProcess.getReValue());
                    }
                }
            }
            qmsIpqcInspectionOrder.setQmsIpqcInspectionOrderDets(qmsIpqcInspectionOrderDets);
        }
        return qmsIpqcInspectionOrder;
    }

}
