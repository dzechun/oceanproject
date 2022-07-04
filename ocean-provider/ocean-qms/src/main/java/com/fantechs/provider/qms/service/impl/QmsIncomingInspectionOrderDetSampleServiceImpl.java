package com.fantechs.provider.qms.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.PdaIncomingCheckBarcodeDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSampleSubmitDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetSampleDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.qms.mapper.*;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderDetSampleService;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderService;
import io.seata.spring.annotation.GlobalTransactional;
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
    private QmsIncomingInspectionOrderService qmsIncomingInspectionOrderService;

    @Resource
    private InnerFeignApi innerFeignApi;


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
        if(StringUtils.isEmpty(qmsIncomingInspectionOrderDetSampleList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"样本信息不能为空");
        }
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

        //查出所有条码
        SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
        searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode("QMS-MIIO");
        searchWmsInnerMaterialBarcodeReOrder.setOrderId(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderId());
        List<WmsInnerMaterialBarcodeReOrderDto> materialBarcodeReOrderDtos = innerFeignApi.findAll(searchWmsInnerMaterialBarcodeReOrder).getData();
        if(StringUtils.isEmpty(materialBarcodeReOrderDtos)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到当前单据对应的条码");
        }

        int badnessQty = 0;//不良数量
        for(QmsIncomingInspectionOrderDetSample qmsIncomingInspectionOrderDetSample : qmsIncomingInspectionOrderDetSampleList){
            if (StringUtils.isEmpty(qmsIncomingInspectionOrderDetSample.getBarcode())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"条码不能为空");
            }

            Long materialBarcodeId = 0L;
            //条码校验
            boolean tag = false;
            for (WmsInnerMaterialBarcodeReOrderDto materialBarcodeReOrderDto : materialBarcodeReOrderDtos) {
                if (qmsIncomingInspectionOrderDetSample.getBarcode().equals(materialBarcodeReOrderDto.getBarcode())) {
                    tag = true;
                    materialBarcodeId = materialBarcodeReOrderDto.getMaterialBarcodeId();
                    break;
                }
            }
            if (!tag) {
                throw new BizErrorException("条码"+qmsIncomingInspectionOrderDetSample.getBarcode()+"不属于当前单据");
            }

            //是否重复
            Example example1 = new Example(QmsIncomingInspectionOrderDetSample.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialBarcodeId", materialBarcodeId)
                    .andEqualTo("incomingInspectionOrderDetId", qmsIncomingInspectionOrderDetSample.getIncomingInspectionOrderDetId());
            QmsIncomingInspectionOrderDetSample incomingInspectionOrderDetSample = qmsIncomingInspectionOrderDetSampleMapper.selectOneByExample(example1);
            if (StringUtils.isNotEmpty(incomingInspectionOrderDetSample)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "条码" + incomingInspectionOrderDetSample.getBarcode() + "已存在");
            }

            qmsIncomingInspectionOrderDetSample.setMaterialBarcodeId(materialBarcodeId);
            qmsIncomingInspectionOrderDetSample.setCreateUserId(user.getUserId());
            qmsIncomingInspectionOrderDetSample.setCreateTime(new Date());
            qmsIncomingInspectionOrderDetSample.setModifiedUserId(user.getUserId());
            qmsIncomingInspectionOrderDetSample.setModifiedTime(new Date());
            qmsIncomingInspectionOrderDetSample.setStatus((byte) 1);
            qmsIncomingInspectionOrderDetSample.setOrgId(user.getOrganizationId());

            //计算不良数量
            if (StringUtils.isNotEmpty(qmsIncomingInspectionOrderDetSample.getBadnessPhenotypeId())) {
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

        //修改检验单状态
        QmsIncomingInspectionOrder qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderId());
        if (qmsIncomingInspectionOrder.getInspectionStatus() == (byte) 1) {
            qmsIncomingInspectionOrder.setInspectionStatus((byte) 2);
            qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrder);
        }

        return i;
    }

    @Override
    public Long checkBarcode(PdaIncomingCheckBarcodeDto pdaIncomingCheckBarcodeDto){
        List<Long> detIdList = pdaIncomingCheckBarcodeDto.getIncomingInspectionOrderDetIdList();
        String barcode = pdaIncomingCheckBarcodeDto.getBarcode();
        Long materialBarcodeId = 0L;
        QmsIncomingInspectionOrderDet orderDet = qmsIncomingInspectionOrderDetMapper.selectByPrimaryKey(detIdList.get(0));
        QmsIncomingInspectionOrder qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(orderDet.getIncomingInspectionOrderId());

        //条码校验
        SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
        searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode(qmsIncomingInspectionOrder.getSysOrderTypeCode());
        searchWmsInnerMaterialBarcodeReOrder.setOrderId(qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
        List<WmsInnerMaterialBarcodeReOrderDto> materialBarcodeReOrderDtos = innerFeignApi.findAll(searchWmsInnerMaterialBarcodeReOrder).getData();
        if(StringUtils.isEmpty(materialBarcodeReOrderDtos)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到当前单据对应的条码");
        }
        boolean tag = false;
        for (WmsInnerMaterialBarcodeReOrderDto materialBarcodeReOrderDto : materialBarcodeReOrderDtos){
            if(barcode.equals(materialBarcodeReOrderDto.getBarcode())){
                tag = true;
                materialBarcodeId = materialBarcodeReOrderDto.getMaterialBarcodeId();
                break;
            }
        }
        if(!tag){
            throw new BizErrorException("该条码不属于当前单据");
        }


        for (Long detId : detIdList){
            Example example = new Example(QmsIncomingInspectionOrderDetSample.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialBarcodeId",materialBarcodeId)
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
        }

        return materialBarcodeId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
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
                criteria1.andEqualTo("incomingInspectionOrderDetId",detId);
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
                qmsIncomingInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrderDet);

                QmsIncomingInspectionOrderDetSample qmsIncomingInspectionOrderDetSample = new QmsIncomingInspectionOrderDetSample();
                qmsIncomingInspectionOrderDetSample.setIncomingInspectionOrderDetId(detId);
                qmsIncomingInspectionOrderDetSample.setBarcode(pdaIncomingSampleSubmitDto.getBarcode());
                qmsIncomingInspectionOrderDetSample.setMaterialBarcodeId(pdaIncomingSampleSubmitDto.getMaterialBarcodeId());
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
                if(qmsIncomingInspectionOrder == null) {
                    qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderId());
                }
            }
            //返写检验单结果
            Example example = new Example(QmsIncomingInspectionOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("incomingInspectionOrderId",qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
            List<QmsIncomingInspectionOrderDet> qmsIncomingInspectionOrderDets = qmsIncomingInspectionOrderDetMapper.selectByExample(example);
            qmsIncomingInspectionOrderService.checkInspectionResult(qmsIncomingInspectionOrderDets);
        }

        return i;
    }
}
