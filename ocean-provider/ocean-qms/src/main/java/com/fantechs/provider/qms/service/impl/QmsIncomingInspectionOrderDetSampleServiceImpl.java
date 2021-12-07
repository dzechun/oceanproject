package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.PdaIncomingCheckBarcodeDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingDetSubmitDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSampleSubmitDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetSampleDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDetSample;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsHtIncomingInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderDetSampleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */
@Service
public class QmsIncomingInspectionOrderDetSampleServiceImpl extends BaseService<QmsIncomingInspectionOrderDetSample> implements QmsIncomingInspectionOrderDetSampleService {

    @Resource
    private QmsIncomingInspectionOrderDetSampleMapper qmsIncomingInspectionOrderDetSampleMapper;

    @Resource
    private QmsIncomingInspectionOrderDetMapper qmsIncomingInspectionOrderDetMapper;

    @Resource
    private QmsIncomingInspectionOrderMapper qmsIncomingInspectionOrderMapper;

    @Resource
    private QmsHtIncomingInspectionOrderDetSampleMapper qmsHtIncomingInspectionOrderDetSampleMapper;

    @Override
    public List<QmsHtIncomingInspectionOrderDetSample> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsHtIncomingInspectionOrderDetSampleMapper.findHtList(map);
    }

    @Override
    public List<QmsIncomingInspectionOrderDetSampleDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsIncomingInspectionOrderDetSampleMapper.findList(map);
    }

    @Override
    public String checkBarcode(PdaIncomingCheckBarcodeDto pdaIncomingCheckBarcodeDto){
        List<Long> detIdList = pdaIncomingCheckBarcodeDto.getIncomingInspectionOrderDetIdList();
        String barcode = pdaIncomingCheckBarcodeDto.getBarcode();

        for (Long detId : detIdList){
            Example example = new Example(QmsIncomingInspectionOrderDetSample.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcode",barcode)
                    .andEqualTo("incomingInspectionOrderDetId",detId);
            List<QmsIncomingInspectionOrderDetSample> qmsIncomingInspectionOrderDetSamples = qmsIncomingInspectionOrderDetSampleMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(qmsIncomingInspectionOrderDetSamples)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"条码重复");
            }

            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("incomingInspectionOrderDetId", detId);
            List<QmsIncomingInspectionOrderDetSample> detSamples = qmsIncomingInspectionOrderDetSampleMapper.selectByExample(example);
            QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet = qmsIncomingInspectionOrderDetMapper.selectByPrimaryKey(detId);
            if(qmsIncomingInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(detSamples.size())) == 0){
                throw new BizErrorException("检验样本数已达上限");
            }
        }

        return barcode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sampleSubmit(PdaIncomingSampleSubmitDto pdaIncomingSampleSubmitDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<PdaIncomingDetSubmitDto> pdaIncomingDetSubmitDtoList = pdaIncomingSampleSubmitDto.getPdaIncomingDetSubmitDtoList();
        String barcode = pdaIncomingSampleSubmitDto.getBarcode();
        List<QmsIncomingInspectionOrderDetSample> sampleList = new LinkedList<>();
        int i = 0;

        //条码不为空则提交样本值，条码为空则提交明细
        if(StringUtils.isNotEmpty(barcode)) {
            QmsIncomingInspectionOrder qmsIncomingInspectionOrder = null;
            for (PdaIncomingDetSubmitDto pdaIncomingDetSubmitDto : pdaIncomingDetSubmitDtoList) {
                Long detId = pdaIncomingDetSubmitDto.getIncomingInspectionOrderDetId();
                Example example1 = new Example(QmsIncomingInspectionOrderDetSample.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("incomingInspectionOrderDetId",pdaIncomingDetSubmitDto.getIncomingInspectionOrderDetId());
                List<QmsIncomingInspectionOrderDetSample> detSampleList = qmsIncomingInspectionOrderDetSampleMapper.selectByExample(example1);
                QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet = qmsIncomingInspectionOrderDetMapper.selectByPrimaryKey(detId);

                qmsIncomingInspectionOrderDet.setBadnessQty(qmsIncomingInspectionOrderDet.getBadnessQty() == null ? BigDecimal.ZERO : qmsIncomingInspectionOrderDet.getBadnessQty());
                if (StringUtils.isNotEmpty(pdaIncomingSampleSubmitDto.getBadnessPhenotypeId())) {
                    qmsIncomingInspectionOrderDet.setBadnessQty(qmsIncomingInspectionOrderDet.getBadnessQty().add(BigDecimal.ONE));
                }
                //检验数与样本数相等时，计算检验结果
                if (qmsIncomingInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(detSampleList.size() + 1)) == 0) {
                    if (qmsIncomingInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDet.getAcValue())) == 0
                    ||qmsIncomingInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDet.getAcValue())) == -1) {
                        qmsIncomingInspectionOrderDet.setInspectionResult((byte) 1);
                    } else if (qmsIncomingInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDet.getReValue())) == 0
                            ||qmsIncomingInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDet.getReValue())) == 1) {
                        qmsIncomingInspectionOrderDet.setInspectionResult((byte) 0);
                    }
                }
                i += qmsIncomingInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrderDet);

                QmsIncomingInspectionOrderDetSample qmsIncomingInspectionOrderDetSample = new QmsIncomingInspectionOrderDetSample();
                qmsIncomingInspectionOrderDetSample.setIncomingInspectionOrderDetId(detId);
                qmsIncomingInspectionOrderDetSample.setBarcode(pdaIncomingSampleSubmitDto.getBarcode());
                qmsIncomingInspectionOrderDetSample.setSampleValue(pdaIncomingSampleSubmitDto.getSampleValue());
                qmsIncomingInspectionOrderDetSample.setBadnessPhenotypeId(pdaIncomingSampleSubmitDto.getBadnessPhenotypeId());
                qmsIncomingInspectionOrderDetSample.setStatus((byte) 1);
                qmsIncomingInspectionOrderDetSample.setOrgId(user.getOrganizationId());
                qmsIncomingInspectionOrderDetSample.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrderDetSample.setCreateTime(new Date());
                qmsIncomingInspectionOrderDetSample.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrderDetSample.setModifiedTime(new Date());
                sampleList.add(qmsIncomingInspectionOrderDetSample);

                //修改检验单状态
                if(qmsIncomingInspectionOrder == null) {
                    qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderId());
                    if (qmsIncomingInspectionOrder.getInspectionStatus() == (byte) 1) {
                        qmsIncomingInspectionOrder.setInspectionStatus((byte) 2);
                        qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrder);
                    }
                }
            }
            i += qmsIncomingInspectionOrderDetSampleMapper.insertList(sampleList);
        }else {
            Byte inspectionResult = 1;
            QmsIncomingInspectionOrder qmsIncomingInspectionOrder = null;
            for (PdaIncomingDetSubmitDto pdaIncomingDetSubmitDto : pdaIncomingDetSubmitDtoList) {
                QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet = qmsIncomingInspectionOrderDetMapper.selectByPrimaryKey(pdaIncomingDetSubmitDto.getIncomingInspectionOrderDetId());
                qmsIncomingInspectionOrderDet.setBadnessCategoryId(pdaIncomingDetSubmitDto.getBadnessCategoryId());
                i += qmsIncomingInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrderDet);

                if(qmsIncomingInspectionOrder == null){
                    qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderId());
                }
                if(qmsIncomingInspectionOrderDet.getInspectionResult() == (byte)0){
                    inspectionResult = 0;
                }
            }
            //返写检验单结果
            qmsIncomingInspectionOrder.setInspectionResult(inspectionResult);
            qmsIncomingInspectionOrder.setInspectionStatus((byte)3);
            qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrder);
        }

        return i;
    }
}
