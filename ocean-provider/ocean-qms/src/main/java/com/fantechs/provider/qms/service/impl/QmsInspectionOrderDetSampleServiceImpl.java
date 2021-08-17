package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionWay;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
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
    private QmsInspectionOrderService qmsInspectionOrderService;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private RedisUtil redisUtil;

    //存入redis前缀
    private static String DEFAULT_NAME="INSPECTION:";

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

        if(redisUtil.hasKey(DEFAULT_NAME+qmsInspectionOrderDetId.toString())){
            List<Object> list = redisUtil.lGet(DEFAULT_NAME + qmsInspectionOrderDetId.toString(), 0, -1);
            if(list.contains(barcode)){
                throw new BizErrorException("同一检验项目不可重复扫描同一条码");
            }else {
                list.add(barcode);
                redisUtil.lSet(DEFAULT_NAME+qmsInspectionOrderDetId.toString(),list,(long)600);
            }
        }else {
            //将扫描的条码存入redis
            List<String> barcodeList = new ArrayList<>();
            barcodeList.add(barcode);
            redisUtil.lSet(DEFAULT_NAME+qmsInspectionOrderDetId.toString(),barcodeList,(long)600);
        }


        Boolean bool = true;
        QmsInspectionOrderDet qmsInspectionOrderDet = qmsInspectionOrderDetMapper.selectByPrimaryKey(qmsInspectionOrderDetId);
        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(qmsInspectionOrderDet.getInspectionOrderId());

        MesSfcWorkOrderBarcode workOrderBarcode = sfcFeignApi.findBarcode(barcode).getData();
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
        if (!qmsInspectionOrder.getMaterialId().equals(mesPmWorkOrderDto.getMaterialId())) {
            throw new BizErrorException("该条码对应的产品料号与检验单的产品料号不一致");
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

        //删除条码缓存
        redisUtil.del(DEFAULT_NAME+qmsInspectionOrderDetSampleList.get(0).getInspectionOrderDetId().toString());


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
        QmsInspectionOrderDet qmsInspectionOrderDet = qmsInspectionOrderDetMapper.findList(map).get(0);
        QmsInspectionOrder inspectionOrder = qmsInspectionOrderService.selectByKey(qmsInspectionOrderDet.getInspectionOrderId());

        //赋值Qty、AC、RE
        for (QmsInspectionOrderDet inspectionOrderDet:inspectionOrder.getQmsInspectionOrderDets()){
            if(inspectionOrderDetId.equals(inspectionOrderDet.getInspectionOrderDetId())){
                qmsInspectionOrderDet.setSampleQty(inspectionOrderDet.getSampleQty());
                qmsInspectionOrderDet.setAcValue(inspectionOrderDet.getAcValue());
                qmsInspectionOrderDet.setReValue(inspectionOrderDet.getReValue());
            }
        }

        //当已检验样本数等于样本数时，才计算不良数量、检验结果
        if(qmsInspectionOrderDet.getSampleQty().compareTo(new BigDecimal(qmsInspectionOrderDetSampleList.size()))==0) {
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
            if(qmsInspectionOrder.getInspectionStatus()==(byte)1){
                qmsInspectionOrder.setInspectionStatus((byte)2);
                qmsInspectionOrder.setInspectionOrderId(qmsInspectionOrderDet.getInspectionOrderId());
                qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);
            }
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

        if(StringUtils.isNotEmpty(qmsInspectionOrderDetSample.getBarcode())) {
            Example example = new Example(QmsInspectionOrderDetSample.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcode", qmsInspectionOrderDetSample.getBarcode())
                    .andEqualTo("inspectionOrderDetId",qmsInspectionOrderDetSample.getInspectionOrderDetId());
            List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(qmsInspectionOrderDetSamples)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
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

}
