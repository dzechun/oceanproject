package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.qms.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetSampleService;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetService;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */
@Service
public class QmsIpqcInspectionOrderDetSampleServiceImpl extends BaseService<QmsIpqcInspectionOrderDetSample> implements QmsIpqcInspectionOrderDetSampleService {

    @Resource
    private QmsIpqcInspectionOrderDetSampleMapper qmsIpqcInspectionOrderDetSampleMapper;
    @Resource
    private QmsIpqcInspectionOrderDetMapper qmsIpqcInspectionOrderDetMapper;
    @Resource
    private QmsIpqcInspectionOrderMapper qmsIpqcInspectionOrderMapper;
    @Resource
    private QmsIpqcInspectionOrderService qmsIpqcInspectionOrderService;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;

    @Override
    public List<QmsIpqcInspectionOrderDetSample> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",user.getOrganizationId());
        return qmsIpqcInspectionOrderDetSampleMapper.findList(map);
    }

    @Override
    public Boolean checkBarcode(String barcode, Long qmsIpqcInspectionOrderDetId) {
        Boolean bool = true;
        QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet = qmsIpqcInspectionOrderDetMapper.selectByPrimaryKey(qmsIpqcInspectionOrderDetId);
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.selectByPrimaryKey(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderId());

        MesSfcWorkOrderBarcode workOrderBarcode = sfcFeignApi.findBarcode(barcode).getData();
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrder.getWorkOrderId())) {
            if (StringUtils.isEmpty(workOrderBarcode)) {
                throw new BizErrorException("无该条码对应工单");
            }
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(workOrderBarcode.getWorkOrderId());
            List<MesPmWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (StringUtils.isEmpty(workOrderList)) {
                throw new BizErrorException("无该条码对应工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = workOrderList.get(0);
            if (!qmsIpqcInspectionOrder.getWorkOrderId().equals(mesPmWorkOrderDto.getWorkOrderId())) {
                throw new BizErrorException("该条码对应的工单号与检验单的工单号不一致");
            }
        }else if(StringUtils.isNotEmpty(qmsIpqcInspectionOrder.getMaterialId())) {
            if (StringUtils.isEmpty(workOrderBarcode)) {
                throw new BizErrorException("无该条码对应工单");
            }
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(workOrderBarcode.getWorkOrderId());
            List<MesPmWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (StringUtils.isEmpty(workOrderList)) {
                throw new BizErrorException("无该条码对应工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = workOrderList.get(0);
            if (!qmsIpqcInspectionOrder.getMaterialId().equals(mesPmWorkOrderDto.getMaterialId())) {
                throw new BizErrorException("该条码对应的产品料号与检验单的产品料号不一致");
            }
        }

        return bool;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchAdd(List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSampleList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if(StringUtils.isEmpty(qmsIpqcInspectionOrderDetSampleList)){
            throw new BizErrorException("样本信息不能为空");
        }

        //原数据删除
        Long ipqcInspectionOrderDetId = qmsIpqcInspectionOrderDetSampleList.get(0).getIpqcInspectionOrderDetId();
        Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ipqcInspectionOrderDetId",ipqcInspectionOrderDetId);
        qmsIpqcInspectionOrderDetSampleMapper.deleteByExample(example);

        int i = 0;
        int badnessQty = 0;//不良数量
        for (QmsIpqcInspectionOrderDetSample qmsIpqcInspectionOrderDetSample : qmsIpqcInspectionOrderDetSampleList){
            i = i + this.save(qmsIpqcInspectionOrderDetSample);
            if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSample.getBadnessPhenotypeId())){
                badnessQty++;
            }
        }

        Map<String,Object> map = new HashMap();
        map.put("ipqcInspectionOrderDetId",ipqcInspectionOrderDetId);
        QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet = qmsIpqcInspectionOrderDetMapper.findList(map).get(0);
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderService.selectByKey(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderId());

        //赋值Qty、AC、RE
        for (QmsIpqcInspectionOrderDet ipqcInspectionOrderDet:qmsIpqcInspectionOrder.getQmsIpqcInspectionOrderDets()){
            if(ipqcInspectionOrderDetId.equals(ipqcInspectionOrderDet.getIpqcInspectionOrderDetId())){
                qmsIpqcInspectionOrderDet.setSampleQty(ipqcInspectionOrderDet.getSampleQty());
                qmsIpqcInspectionOrderDet.setAcValue(ipqcInspectionOrderDet.getAcValue());
                qmsIpqcInspectionOrderDet.setReValue(ipqcInspectionOrderDet.getReValue());
            }
        }

        //当已检验样本数等于样本数时，才计算不良数量、检验结果
        if(qmsIpqcInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(qmsIpqcInspectionOrderDetSampleList.size()))==0) {
            qmsIpqcInspectionOrderDet.setBadnessQty((long)badnessQty);
            if (qmsIpqcInspectionOrderDet.getBadnessQty().longValue() <= qmsIpqcInspectionOrderDet.getAcValue().longValue()) {
                qmsIpqcInspectionOrderDet.setInspectionResult((byte) 1);
            } else if (qmsIpqcInspectionOrderDet.getBadnessQty().longValue() >= qmsIpqcInspectionOrderDet.getReValue()) {
                qmsIpqcInspectionOrderDet.setInspectionResult((byte) 0);
            }
            qmsIpqcInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrderDet);

            //更新检验状态
            if(qmsIpqcInspectionOrder.getInspectionStatus()==(byte)1){
                qmsIpqcInspectionOrder.setInspectionStatus((byte)2);
                qmsIpqcInspectionOrder.setIpqcInspectionOrderId(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderId());
                qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(QmsIpqcInspectionOrderDetSample record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if(StringUtils.isNotEmpty(record.getBarcode())) {
            Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcode", record.getBarcode());
            List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSamples)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }

        //新增IPQC检验单明细样本
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1:record.getStatus());
        record.setOrgId(user.getOrganizationId());
        return qmsIpqcInspectionOrderDetSampleMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(QmsIpqcInspectionOrderDetSample entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode", entity.getBarcode())
                .andNotEqualTo("inspectionOrderDetSampleId",entity.getInspectionOrderDetSampleId());
        List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSamples)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //修改IPQC检验单明细样本
        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        return qmsIpqcInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(entity);
    }
}