package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.PdaIncomingCheckBarcodeDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSampleSubmitDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetSampleDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDetSample;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.qms.mapper.*;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderDetSampleService;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderService;
import org.springframework.beans.BeanUtils;
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

    @Resource
    private QmsHtIncomingInspectionOrderMapper qmsHtIncomingInspectionOrderMapper;

    @Resource
    private QmsIncomingInspectionOrderService qmsIncomingInspectionOrderService;

    @Resource
    private BaseFeignApi baseFeignApi;

    /*@Resource
    private SrmFeignApi srmFeignApi;*/

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
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchAdd(List<QmsIncomingInspectionOrderDetSample> qmsIncomingInspectionOrderDetSampleList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //原数据删除
        Long incomingInspectionOrderDetId = qmsIncomingInspectionOrderDetSampleList.get(0).getIncomingInspectionOrderDetId();
        Example example = new Example(QmsIncomingInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("incomingInspectionOrderDetId", incomingInspectionOrderDetId);
        qmsIncomingInspectionOrderDetSampleMapper.deleteByExample(example);

        QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet = qmsIncomingInspectionOrderDetMapper.selectByPrimaryKey(incomingInspectionOrderDetId);
        if(qmsIncomingInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDetSampleList.size())) != 0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"样本的个数与样本数的值不一致");
        }

        int badnessQty = 0;//不良数量
        for(QmsIncomingInspectionOrderDetSample qmsIncomingInspectionOrderDetSample : qmsIncomingInspectionOrderDetSampleList){
            if(StringUtils.isNotEmpty(qmsIncomingInspectionOrderDetSample.getBarcode())) {
                Example example1 = new Example(QmsIncomingInspectionOrderDetSample.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("barcode", qmsIncomingInspectionOrderDetSample.getBarcode())
                        .andEqualTo("incomingInspectionOrderDetId",qmsIncomingInspectionOrderDetSample.getIncomingInspectionOrderDetId());
                QmsIncomingInspectionOrderDetSample incomingInspectionOrderDetSample = qmsIncomingInspectionOrderDetSampleMapper.selectOneByExample(example1);
                if (StringUtils.isNotEmpty(incomingInspectionOrderDetSample)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"条码"+incomingInspectionOrderDetSample.getBarcode()+"已存在");
                }
            }
            qmsIncomingInspectionOrderDetSample.setCreateUserId(user.getUserId());
            qmsIncomingInspectionOrderDetSample.setCreateTime(new Date());
            qmsIncomingInspectionOrderDetSample.setModifiedUserId(user.getUserId());
            qmsIncomingInspectionOrderDetSample.setModifiedTime(new Date());
            qmsIncomingInspectionOrderDetSample.setStatus((byte)1);
            qmsIncomingInspectionOrderDetSample.setOrgId(user.getOrganizationId());

            //计算不良数量
            if(StringUtils.isNotEmpty(qmsIncomingInspectionOrderDetSample.getBadnessPhenotypeId())){
                badnessQty++;
            }
        }
        int i = qmsIncomingInspectionOrderDetSampleMapper.insertList(qmsIncomingInspectionOrderDetSampleList);

        //不良数量
        qmsIncomingInspectionOrderDet.setBadnessQty(new BigDecimal(badnessQty));
        //计算明细检验结果
        if(StringUtils.isEmpty(qmsIncomingInspectionOrderDet.getAcValue(),qmsIncomingInspectionOrderDet.getReValue())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"该检验单明细的AC或RE值为空，无法计算检验结果");
        }
        if (qmsIncomingInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDet.getAcValue())) == 0
                ||qmsIncomingInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDet.getAcValue())) == -1) {
            qmsIncomingInspectionOrderDet.setInspectionResult((byte) 1);
        } else if (qmsIncomingInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDet.getReValue())) == 0
                ||qmsIncomingInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsIncomingInspectionOrderDet.getReValue())) == 1) {
            qmsIncomingInspectionOrderDet.setInspectionResult((byte) 0);
        }
        qmsIncomingInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrderDet);

        return i;
    }

    @Override
    public String checkBarcode(PdaIncomingCheckBarcodeDto pdaIncomingCheckBarcodeDto){
        List<Long> detIdList = pdaIncomingCheckBarcodeDto.getIncomingInspectionOrderDetIdList();
        String barcode = pdaIncomingCheckBarcodeDto.getBarcode();
        QmsIncomingInspectionOrder qmsIncomingInspectionOrder = null;

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
            if(StringUtils.isEmpty(qmsIncomingInspectionOrderDet.getSampleQty())){
                throw new BizErrorException("样本数为空");
            }
            if(qmsIncomingInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(detSamples.size())) == 0){
                throw new BizErrorException("检验样本数已达上限");
            }

            if(qmsIncomingInspectionOrder == null){
                qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderId());
            }
        }

        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setBusinessType((byte)1);
        searchBaseOrderFlow.setOrderNode((byte)4);
        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
        if(StringUtils.isEmpty(baseOrderFlow)){
            throw new BizErrorException("未找到当前单据配置的下游单据");
        }
        //条码校验
        if("SRM-ASN".equals(baseOrderFlow.getSourceOrderTypeCode())){
            //ASN单
            /*SearchSrmInAsnOrderDetBarcode searchSrmInAsnOrderDetBarcode = new SearchSrmInAsnOrderDetBarcode();
            searchSrmInAsnOrderDetBarcode.setAsnOrderDetId(qmsIncomingInspectionOrder.getSourceId());
            List<SrmInAsnOrderDetBarcodeDto> detBarcodeDtos = srmFeignApi.findList(searchSrmInAsnOrderDetBarcode).getData();
*/
        }else if("IN-SPO".equals(baseOrderFlow.getSourceOrderTypeCode())){
            //收货计划

        }else if("IN-SWK".equals(baseOrderFlow.getSourceOrderTypeCode())){
            //收货作业

        }else if("IN-PO".equals(baseOrderFlow.getSourceOrderTypeCode())){
            //采购订单

        }

        return barcode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sampleSubmit(List<PdaIncomingSampleSubmitDto> list){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<QmsIncomingInspectionOrderDetSample> sampleList = new LinkedList<>();
        QmsIncomingInspectionOrder qmsIncomingInspectionOrder = null;
        int i = 0;

        //条码不为空则提交样本值，条码为空则提交明细
        if(StringUtils.isNotEmpty(list.get(0).getBarcode())) {
            for (PdaIncomingSampleSubmitDto pdaIncomingSampleSubmitDto : list) {
                Long detId = pdaIncomingSampleSubmitDto.getIncomingInspectionOrderDetId();
                Example example1 = new Example(QmsIncomingInspectionOrderDetSample.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("incomingInspectionOrderDetId",pdaIncomingSampleSubmitDto.getIncomingInspectionOrderDetId());
                List<QmsIncomingInspectionOrderDetSample> detSampleList = qmsIncomingInspectionOrderDetSampleMapper.selectByExample(example1);
                QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet = qmsIncomingInspectionOrderDetMapper.selectByPrimaryKey(detId);

                qmsIncomingInspectionOrderDet.setBadnessQty(qmsIncomingInspectionOrderDet.getBadnessQty() == null ? BigDecimal.ZERO : qmsIncomingInspectionOrderDet.getBadnessQty());
                if (StringUtils.isNotEmpty(pdaIncomingSampleSubmitDto.getBadnessPhenotypeId())) {
                    qmsIncomingInspectionOrderDet.setBadnessQty(qmsIncomingInspectionOrderDet.getBadnessQty().add(BigDecimal.ONE));
                }
                //检验数与样本数相等时，计算检验结果
                if (qmsIncomingInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(detSampleList.size() + 1)) == 0) {
                    if(StringUtils.isEmpty(qmsIncomingInspectionOrderDet.getAcValue(),qmsIncomingInspectionOrderDet.getReValue())){
                        throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"检验单明细的AC或RE值为空，无法计算检验结果");
                    }
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

            //返写检验单结果
            Example example = new Example(QmsIncomingInspectionOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("incomingInspectionOrderId",qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
            List<QmsIncomingInspectionOrderDet> qmsIncomingInspectionOrderDets = qmsIncomingInspectionOrderDetMapper.selectByExample(example);
            qmsIncomingInspectionOrderService.checkInspectionResult(qmsIncomingInspectionOrderDets);
        }else {
            for (PdaIncomingSampleSubmitDto pdaIncomingSampleSubmitDto : list) {
                QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet = qmsIncomingInspectionOrderDetMapper.selectByPrimaryKey(pdaIncomingSampleSubmitDto.getIncomingInspectionOrderDetId());
                qmsIncomingInspectionOrderDet.setBadnessCategoryId(pdaIncomingSampleSubmitDto.getBadnessCategoryId());
                i += qmsIncomingInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrderDet);
            }

        }

        //履历
        QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
        BeanUtils.copyProperties(qmsIncomingInspectionOrder, qmsHtIncomingInspectionOrder);
        qmsHtIncomingInspectionOrderMapper.insertSelective(qmsHtIncomingInspectionOrder);

        return i;
    }
}
