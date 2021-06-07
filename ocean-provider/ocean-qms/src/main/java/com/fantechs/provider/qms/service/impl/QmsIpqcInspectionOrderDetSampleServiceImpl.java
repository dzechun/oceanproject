package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.qms.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetSampleService;
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
    private BaseFeignApi baseFeignApi;

    @Override
    public List<QmsIpqcInspectionOrderDetSample> findList(Map<String, Object> map) {
        return qmsIpqcInspectionOrderDetSampleMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchAdd(List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSampleList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
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
        map.put("inspectionOrderDetId",ipqcInspectionOrderDetId);
        QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet = qmsIpqcInspectionOrderDetMapper.findList(map).get(0);
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.selectByPrimaryKey(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderId());
        //抽样类型为抽样方案时，去抽样方案取AC、RE、样本数
        if(qmsIpqcInspectionOrderDet.getSampleProcessType()==(byte)4){
            BaseSampleProcess baseSampleProcess = baseFeignApi.getAcReQty(qmsIpqcInspectionOrderDet.getSampleProcessId(), qmsIpqcInspectionOrder.getQty()).getData();
            if(StringUtils.isNotEmpty(baseSampleProcess)) {
                qmsIpqcInspectionOrderDet.setSampleQty(baseSampleProcess.getSampleQty());
                qmsIpqcInspectionOrderDet.setAcValue(baseSampleProcess.getAcValue());
                qmsIpqcInspectionOrderDet.setReValue(baseSampleProcess.getReValue());
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

        Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode", record.getBarcode());
        List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSamples)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
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
