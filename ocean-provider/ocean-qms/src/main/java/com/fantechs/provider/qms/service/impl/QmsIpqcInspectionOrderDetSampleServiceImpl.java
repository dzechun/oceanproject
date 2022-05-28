package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetSampleService;
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
    private AuthFeignApi securityFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private QmsIpqcInspectionOrderService qmsIpqcInspectionOrderService;

    @Override
    public List<QmsIpqcInspectionOrderDetSample> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsIpqcInspectionOrderDetSampleMapper.findList(map);
    }

    @Override
    public Boolean checkBarcode(String barcode, Long qmsIpqcInspectionOrderDetId) {
        Boolean bool = true;

        Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode",barcode)
                .andEqualTo("ipqcInspectionOrderDetId",qmsIpqcInspectionOrderDetId);
        List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSamples)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"条码重复");
        }

        Example example1 = new Example(QmsIpqcInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("ipqcInspectionOrderDetId", qmsIpqcInspectionOrderDetId);
        List<QmsIpqcInspectionOrderDetSample> DetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example1);
        QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet = qmsIpqcInspectionOrderDetMapper.selectByPrimaryKey(qmsIpqcInspectionOrderDetId);
        if(qmsIpqcInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(DetSamples.size())) == 0){
            throw new BizErrorException("检验样本数已达上限");
        }

        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.selectByPrimaryKey(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderId());

       /* MesSfcWorkOrderBarcode workOrderBarcode = sfcFeignApi.findBarcode(barcode).getData();
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
            bool = true;
        }*/

        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrder.getWorkOrderId())) {
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("WorkOrderPositionOnBarcodeProduct");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            String paraValue = sysSpecItemList.get(0).getParaValue();
            int beginIndex = 0;
            int endIndex = 0;
            if (StringUtils.isNotEmpty(paraValue)) {
                String[] arry = paraValue.split("-");
                if (arry.length == 2) {
                    beginIndex = Integer.parseInt(arry[0]);
                    endIndex = Integer.parseInt(arry[1]);
                }
            }
            if(barcode.length() < endIndex){
                throw new BizErrorException("条码错误");
            }
            String workOrderCode = barcode.substring(beginIndex, endIndex);

            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
            searchMesPmWorkOrder.setCodeQueryMark(1);
            List<MesPmWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (StringUtils.isEmpty(workOrderList)) {
                throw new BizErrorException("查无此条码对应工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = workOrderList.get(0);
            if (!qmsIpqcInspectionOrder.getWorkOrderId().equals(mesPmWorkOrderDto.getWorkOrderId())) {
                throw new BizErrorException("该条码对应的工单号与检验单的工单号不一致");
            }
        }

        return bool;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchAdd(List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSampleList) {

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
        QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet = qmsIpqcInspectionOrderDetMapper.findDetList(map).get(0);
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.selectByPrimaryKey(qmsIpqcInspectionOrderDet.getIpqcInspectionOrderId());

        //当已检验样本数等于样本数时，才计算不良数量、检验结果
        if(qmsIpqcInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(qmsIpqcInspectionOrderDetSampleList.size()))==0) {
            qmsIpqcInspectionOrderDet.setBadnessQty((long)badnessQty);
            if (qmsIpqcInspectionOrderDet.getBadnessQty().longValue() <= qmsIpqcInspectionOrderDet.getAcValue().longValue()) {
                qmsIpqcInspectionOrderDet.setInspectionResult((byte) 1);
            } else if (qmsIpqcInspectionOrderDet.getBadnessQty().longValue() >= qmsIpqcInspectionOrderDet.getReValue().longValue()) {
                qmsIpqcInspectionOrderDet.setInspectionResult((byte) 0);
            }
            qmsIpqcInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrderDet);

            //更新检验状态
            if(qmsIpqcInspectionOrder.getInspectionStatus()==(byte)1){
                qmsIpqcInspectionOrder.setInspectionStatus((byte)2);
                qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(QmsIpqcInspectionOrderDetSample record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if(StringUtils.isNotEmpty(record.getBarcode())) {
            Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcode", record.getBarcode())
                    .andEqualTo("ipqcInspectionOrderDetId",record.getIpqcInspectionOrderDetId());
            List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSamples)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"条码重复");
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

        Example example = new Example(QmsIpqcInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode", entity.getBarcode())
                .andEqualTo("ipqcInspectionOrderDetId",entity.getIpqcInspectionOrderDetId())
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

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int barcodeBatchAdd(List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSampleList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Long ipqcInspectionOrderId = 0L;
        int i = 0;

        //不扫条码直接提交则为提交明细
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSampleList.get(0).getBarcode())) {
            for (QmsIpqcInspectionOrderDetSample qmsIpqcInspectionOrderDetSample : qmsIpqcInspectionOrderDetSampleList) {
                Example example1 = new Example(QmsIpqcInspectionOrderDetSample.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("ipqcInspectionOrderDetId", qmsIpqcInspectionOrderDetSample.getIpqcInspectionOrderDetId());
                List<QmsIpqcInspectionOrderDetSample> DetSamples = qmsIpqcInspectionOrderDetSampleMapper.selectByExample(example1);
                QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet = qmsIpqcInspectionOrderDetMapper.selectByPrimaryKey(qmsIpqcInspectionOrderDetSample.getIpqcInspectionOrderDetId());

                qmsIpqcInspectionOrderDet.setBadnessQty(qmsIpqcInspectionOrderDet.getBadnessQty() == null ? 0 : qmsIpqcInspectionOrderDet.getBadnessQty());
                if (StringUtils.isNotEmpty(qmsIpqcInspectionOrderDetSample.getBadnessPhenotypeId())) {
                    qmsIpqcInspectionOrderDet.setBadnessQty(qmsIpqcInspectionOrderDet.getBadnessQty() + 1);
                }
                //检验数与样本数相等时，计算检验结果
                if (qmsIpqcInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(DetSamples.size() + 1)) == 0) {
                    if (qmsIpqcInspectionOrderDet.getBadnessQty().longValue() <= qmsIpqcInspectionOrderDet.getAcValue().longValue()) {
                        qmsIpqcInspectionOrderDet.setInspectionResult((byte) 1);
                    } else if (qmsIpqcInspectionOrderDet.getBadnessQty().longValue() >= qmsIpqcInspectionOrderDet.getReValue().longValue()) {
                        qmsIpqcInspectionOrderDet.setInspectionResult((byte) 0);
                    }
                }
                qmsIpqcInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrderDet);
                ipqcInspectionOrderId = qmsIpqcInspectionOrderDet.getIpqcInspectionOrderId();

                //默认值
                qmsIpqcInspectionOrderDetSample.setCreateUserId(user.getUserId());
                qmsIpqcInspectionOrderDetSample.setCreateTime(new Date());
                qmsIpqcInspectionOrderDetSample.setModifiedUserId(user.getUserId());
                qmsIpqcInspectionOrderDetSample.setModifiedTime(new Date());
                qmsIpqcInspectionOrderDetSample.setStatus(StringUtils.isEmpty(qmsIpqcInspectionOrderDetSample.getStatus()) ? 1 : qmsIpqcInspectionOrderDetSample.getStatus());
                qmsIpqcInspectionOrderDetSample.setOrgId(user.getOrganizationId());
            }
            i = qmsIpqcInspectionOrderDetSampleMapper.insertList(qmsIpqcInspectionOrderDetSampleList);

            QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.selectByPrimaryKey(ipqcInspectionOrderId);
            //更新检验状态
            if(qmsIpqcInspectionOrder.getInspectionStatus()==(byte)1){
                qmsIpqcInspectionOrder.setInspectionStatus((byte)2);
                qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);
            }
            //返写单据检验结果
            qmsIpqcInspectionOrderService.writeBack(ipqcInspectionOrderId);
        }else {
            for (QmsIpqcInspectionOrderDetSample qmsIpqcInspectionOrderDetSample : qmsIpqcInspectionOrderDetSampleList) {
                QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet = qmsIpqcInspectionOrderDetMapper.selectByPrimaryKey(qmsIpqcInspectionOrderDetSample.getIpqcInspectionOrderDetId());
                qmsIpqcInspectionOrderDet.setBadnessCategoryId(qmsIpqcInspectionOrderDetSample.getBadnessCategoryId());
                i += qmsIpqcInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrderDet);
            }
        }

        return i;
    }
}
