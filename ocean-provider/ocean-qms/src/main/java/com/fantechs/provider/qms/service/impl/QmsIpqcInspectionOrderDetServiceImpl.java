package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIpqcInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandardDet;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionStandard;
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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private BaseFeignApi baseFeignApi;

    @Override
    public List<QmsIpqcInspectionOrderDet> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());

        return qmsIpqcInspectionOrderDetMapper.findDetList(map);
    }


    public List<QmsIpqcInspectionOrderDetDto> showOrderDet(Long inspectionStandardId, BigDecimal qty){
        List<QmsIpqcInspectionOrderDetDto> qmsIpqcInspectionOrderDetDtos = new ArrayList<>();

        SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
        searchBaseInspectionStandard.setInspectionStandardId(inspectionStandardId);
        List<BaseInspectionStandard> baseInspectionStandards = baseFeignApi.findList(searchBaseInspectionStandard).getData();
        List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandards.get(0).getBaseInspectionStandardDets();

        if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDets){
                QmsIpqcInspectionOrderDetDto qmsIpqcInspectionOrderDetDto = new QmsIpqcInspectionOrderDetDto();
                BeanUtils.copyProperties(baseInspectionStandardDet, qmsIpqcInspectionOrderDetDto);

                //抽样类型为抽样方案时，去抽样方案取AC、RE、样本数
                if(baseInspectionStandardDet.getSampleProcessType()!=null&&baseInspectionStandardDet.getSampleProcessType()==(byte)4){
                    BaseSampleProcess baseSampleProcess = baseFeignApi.getAcReQty(baseInspectionStandardDet.getSampleProcessId(), qty).getData();
                    if(StringUtils.isNotEmpty(baseSampleProcess.getSampleQty())) {
                        //总数量<样本数时,样本数=总数量
                        qmsIpqcInspectionOrderDetDto.setSampleQty(qty.compareTo(baseSampleProcess.getSampleQty())==-1 ? qty : baseSampleProcess.getSampleQty());
                    }
                    qmsIpqcInspectionOrderDetDto.setAcValue(baseSampleProcess.getAcValue());
                    qmsIpqcInspectionOrderDetDto.setReValue(baseSampleProcess.getReValue());

                }else if(baseInspectionStandardDet.getSampleProcessType()!=null&&baseInspectionStandardDet.getSampleProcessType()!=(byte)4){
                    if(StringUtils.isNotEmpty(baseInspectionStandardDet.getSampleQty())) {
                        //总数量<样本数时,样本数=总数量
                        qmsIpqcInspectionOrderDetDto.setSampleQty(qty.compareTo(baseInspectionStandardDet.getSampleQty()) == -1 ? qty : baseInspectionStandardDet.getSampleQty());
                    }
                    qmsIpqcInspectionOrderDetDto.setAcValue(baseInspectionStandardDet.getAcValue());
                    qmsIpqcInspectionOrderDetDto.setReValue(baseInspectionStandardDet.getReValue());
                }

                qmsIpqcInspectionOrderDetDtos.add(qmsIpqcInspectionOrderDetDto);
            }
        }

        return qmsIpqcInspectionOrderDetDtos;
    }

}
