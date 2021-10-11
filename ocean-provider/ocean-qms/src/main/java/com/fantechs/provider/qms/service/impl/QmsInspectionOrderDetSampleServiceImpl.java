package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionWay;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsInspectionOrderDetSampleService;
import com.fantechs.provider.qms.service.QmsInspectionOrderDetService;
import com.fantechs.provider.qms.service.QmsInspectionOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */
@Service
public class QmsInspectionOrderDetSampleServiceImpl extends BaseService<QmsInspectionOrderDetSample> implements QmsInspectionOrderDetSampleService {

    @Resource
    private QmsInspectionOrderDetSampleMapper qmsInspectionOrderDetSampleMapper;
    @Resource
    private QmsInspectionOrderDetMapper qmsInspectionOrderDetMapper;
    @Resource
    private QmsInspectionOrderMapper qmsInspectionOrderMapper;
    @Resource
    private InnerFeignApi innerFeignApi;

    @Override
    public List<QmsInspectionOrderDetSample> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",user.getOrganizationId());
        return qmsInspectionOrderDetSampleMapper.findList(map);
    }

    @Override
    public Boolean checkBarcode(String barcode, Long qmsInspectionOrderDetId) {
        Boolean bool = true;
        QmsInspectionOrderDet qmsInspectionOrderDet = qmsInspectionOrderDetMapper.selectByPrimaryKey(qmsInspectionOrderDetId);
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(qmsInspectionOrderDet.getInspectionOrderId());

        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setBarcode(barcode);
        searchWmsInnerInventoryDet.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if(StringUtils.isEmpty(inventoryDetDtos)){
            throw new BizErrorException("该条码未与该检验单绑定");
        }

        return bool;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchAdd(List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSampleList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if(StringUtils.isEmpty(qmsInspectionOrderDetSampleList)){
            throw new BizErrorException("样本信息不能为空");
        }

        //原数据删除
        Long inspectionOrderDetId = qmsInspectionOrderDetSampleList.get(0).getInspectionOrderDetId();
        Example example = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderDetId",inspectionOrderDetId);
        qmsInspectionOrderDetSampleMapper.deleteByExample(example);

        int i = 0;
        int badnessQty = 0;//不良数量
        for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSampleList){
            i = i + this.save(qmsInspectionOrderDetSample);
            if(StringUtils.isNotEmpty(qmsInspectionOrderDetSample.getBadnessPhenotypeId())){
                badnessQty++;
            }
        }

        Map<String,Object> map = new HashMap();
        map.put("inspectionOrderDetId",inspectionOrderDetId);
        QmsInspectionOrderDet qmsInspectionOrderDet = qmsInspectionOrderDetMapper.findDetList(map).get(0);

        //返写不良数量、检验结果
        qmsInspectionOrderDet.setBadnessQty(new BigDecimal(badnessQty));
        if (qmsInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsInspectionOrderDet.getAcValue())) == -1
                || qmsInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsInspectionOrderDet.getAcValue())) == 0) {
            qmsInspectionOrderDet.setInspectionResult((byte) 1);
        } else if (qmsInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsInspectionOrderDet.getReValue())) == 0
                || qmsInspectionOrderDet.getBadnessQty().compareTo(new BigDecimal(qmsInspectionOrderDet.getReValue())) == 1) {
            qmsInspectionOrderDet.setInspectionResult((byte) 0);
        }
        qmsInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsInspectionOrderDet);

        //更新检验状态
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(qmsInspectionOrderDet.getInspectionOrderId());
        if (qmsInspectionOrder.getInspectionStatus() == (byte) 1) {
            qmsInspectionOrder.setInspectionStatus((byte) 2);
            qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(QmsInspectionOrderDetSample qmsInspectionOrderDetSample) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionOrderDetSample.setCreateUserId(user.getUserId());
        qmsInspectionOrderDetSample.setCreateTime(new Date());
        qmsInspectionOrderDetSample.setModifiedUserId(user.getUserId());
        qmsInspectionOrderDetSample.setModifiedTime(new Date());
        qmsInspectionOrderDetSample.setStatus(StringUtils.isEmpty(qmsInspectionOrderDetSample.getStatus())?1: qmsInspectionOrderDetSample.getStatus());
        qmsInspectionOrderDetSample.setOrgId(user.getOrganizationId());

        return qmsInspectionOrderDetSampleMapper.insertUseGeneratedKeys(qmsInspectionOrderDetSample);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(QmsInspectionOrderDetSample qmsInspectionOrderDetSample) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionOrderDetSample.setModifiedUserId(user.getUserId());
        qmsInspectionOrderDetSample.setModifiedTime(new Date());
        qmsInspectionOrderDetSample.setOrgId(user.getOrganizationId());

        return qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(qmsInspectionOrderDetSample);
    }

    @Override
    public List<QmsInspectionOrderDetSample> findBarcodes(Long inspectionOrderId){
        HashMap<String, Object> map = new HashMap<>();
        List<QmsInspectionOrderDetSample> detSamples = qmsInspectionOrderDetSampleMapper.findList(map);

        map.put("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDetSample> list = qmsInspectionOrderDetSampleMapper.findList(map);

        for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : list){
            for (QmsInspectionOrderDetSample detSample : detSamples){
                if (qmsInspectionOrderDetSample.getBarcode().equals(detSample.getBarcode()) && StringUtils.isNotEmpty(detSample.getBadnessPhenotypeId())) {
                    qmsInspectionOrderDetSample.setBarcodeStatus((byte) 0);
                }
            }
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public QmsInspectionOrderDetSample checkAndSaveBarcode(QmsInspectionOrderDetSample qmsInspectionOrderDetSample) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(qmsInspectionOrderDetSample.getInspectionOrderId());

        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setBarcode(qmsInspectionOrderDetSample.getBarcode());
        searchWmsInnerInventoryDet.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if(StringUtils.isEmpty(inventoryDetDtos)){
            throw new BizErrorException("该条码未与该检验单绑定");
        }

        Example example = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode",qmsInspectionOrderDetSample.getBarcode())
                .andEqualTo("inspectionOrderId",qmsInspectionOrderDetSample.getInspectionOrderId());
        QmsInspectionOrderDetSample inspectionOrderDetSample = qmsInspectionOrderDetSampleMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(inspectionOrderDetSample)){
            throw new BizErrorException("已存在该条码，请勿重复扫描");
        }

        qmsInspectionOrderDetSample.setCreateUserId(user.getUserId());
        qmsInspectionOrderDetSample.setCreateTime(new Date());
        qmsInspectionOrderDetSample.setModifiedUserId(user.getUserId());
        qmsInspectionOrderDetSample.setModifiedTime(new Date());
        qmsInspectionOrderDetSample.setStatus(StringUtils.isEmpty(qmsInspectionOrderDetSample.getStatus()) ? 1 : qmsInspectionOrderDetSample.getStatus());
        qmsInspectionOrderDetSample.setOrgId(user.getOrganizationId());
        qmsInspectionOrderDetSampleMapper.insertUseGeneratedKeys(qmsInspectionOrderDetSample);

        return qmsInspectionOrderDetSample;
    }


}
