package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandardDet;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionStandard;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.qms.mapper.QmsHtIncomingInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderDetMapper;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderDetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */
@Service
public class QmsIncomingInspectionOrderDetServiceImpl extends BaseService<QmsIncomingInspectionOrderDet> implements QmsIncomingInspectionOrderDetService {

    @Resource
    private QmsIncomingInspectionOrderDetMapper qmsIncomingInspectionOrderDetMapper;

    @Resource
    private QmsHtIncomingInspectionOrderDetMapper qmsHtIncomingInspectionOrderDetMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<QmsHtIncomingInspectionOrderDet> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsHtIncomingInspectionOrderDetMapper.findHtList(map);
    }

    @Override
    public List<QmsIncomingInspectionOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsIncomingInspectionOrderDetMapper.findList(map);
    }

    @Override
    public List<QmsIncomingInspectionOrderDetDto> showOrderDet(Long inspectionStandardId, BigDecimal qty){
        List<QmsIncomingInspectionOrderDetDto> qmsIncomingInspectionOrderDetDtos = new ArrayList<>();

        SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
        searchBaseInspectionStandard.setInspectionStandardId(inspectionStandardId);
        List<BaseInspectionStandard> baseInspectionStandards = baseFeignApi.findList(searchBaseInspectionStandard).getData();
        if(StringUtils.isEmpty(baseInspectionStandards)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"找不到该检验标准");
        }
        List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandards.get(0).getBaseInspectionStandardDets();

        if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDets){
                QmsIncomingInspectionOrderDetDto qmsIncomingInspectionOrderDetDto = new QmsIncomingInspectionOrderDetDto();
                BeanUtils.copyProperties(baseInspectionStandardDet, qmsIncomingInspectionOrderDetDto);
                qmsIncomingInspectionOrderDetDto.setBigInspectionItemDesc(baseInspectionStandardDet.getInspectionItemDescBig());
                qmsIncomingInspectionOrderDetDto.setSmallInspectionItemDesc(baseInspectionStandardDet.getInspectionItemDescSmall());
                qmsIncomingInspectionOrderDetDto.setInspectionStandardName(baseInspectionStandardDet.getInspectionItemStandard());

                //抽样类型为抽样方案时，去抽样方案取AC、RE、样本数
                if(baseInspectionStandardDet.getSampleProcessType()!=null&&baseInspectionStandardDet.getSampleProcessType()==(byte)4){
                    BaseSampleProcess baseSampleProcess = baseFeignApi.getAcReQty(baseInspectionStandardDet.getSampleProcessId(), qty).getData();
                    if(StringUtils.isNotEmpty(baseSampleProcess.getSampleQty())) {
                        //总数量<样本数时,样本数=总数量
                        qmsIncomingInspectionOrderDetDto.setSampleQty(qty.compareTo(baseSampleProcess.getSampleQty())==-1 ? qty : baseSampleProcess.getSampleQty());
                    }
                    qmsIncomingInspectionOrderDetDto.setAcValue(baseSampleProcess.getAcValue());
                    qmsIncomingInspectionOrderDetDto.setReValue(baseSampleProcess.getReValue());

                }else if(baseInspectionStandardDet.getSampleProcessType()!=null&&baseInspectionStandardDet.getSampleProcessType()!=(byte)4){
                    if(StringUtils.isNotEmpty(baseInspectionStandardDet.getSampleQty())) {
                        //总数量<样本数时,样本数=总数量
                        qmsIncomingInspectionOrderDetDto.setSampleQty(qty.compareTo(baseInspectionStandardDet.getSampleQty()) == -1 ? qty : baseInspectionStandardDet.getSampleQty());
                    }
                    qmsIncomingInspectionOrderDetDto.setAcValue(baseInspectionStandardDet.getAcValue());
                    qmsIncomingInspectionOrderDetDto.setReValue(baseInspectionStandardDet.getReValue());
                }

                qmsIncomingInspectionOrderDetDtos.add(qmsIncomingInspectionOrderDetDto);
            }
        }

        return qmsIncomingInspectionOrderDetDtos;
    }

}
