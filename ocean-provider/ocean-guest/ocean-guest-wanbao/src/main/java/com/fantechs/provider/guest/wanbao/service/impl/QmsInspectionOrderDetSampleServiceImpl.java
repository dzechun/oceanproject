package com.fantechs.provider.guest.wanbao.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcKeyPartRelevanceDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcKeyPartRelevance;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSampleBeforeRecheck;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.guest.wanbao.mapper.QmsInspectionOrderDetMapper;
import com.fantechs.provider.guest.wanbao.mapper.QmsInspectionOrderDetSampleBeforeRecheckMapper;
import com.fantechs.provider.guest.wanbao.mapper.QmsInspectionOrderDetSampleMapper;
import com.fantechs.provider.guest.wanbao.mapper.QmsInspectionOrderMapper;
import com.fantechs.provider.guest.wanbao.service.QmsInspectionOrderDetSampleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private QmsInspectionOrderDetSampleBeforeRecheckMapper qmsInspectionOrderDetSampleBeforeRecheckMapper;

    @Override
    public List<QmsInspectionOrderDetSample> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
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

        //原数据删除
        Long inspectionOrderDetId = qmsInspectionOrderDetSampleList.get(0).getInspectionOrderDetId();
        Example example = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionOrderDetId",inspectionOrderDetId);
        qmsInspectionOrderDetSampleMapper.deleteByExample(example);

        Map<String,Object> map = new HashMap();
        map.put("inspectionOrderDetId",inspectionOrderDetId);
        QmsInspectionOrderDet qmsInspectionOrderDet = qmsInspectionOrderDetMapper.findDetList(map).get(0);

        //未检验的条码默认ok
        //属于这个单据的条码
        Example example1 = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId",qmsInspectionOrderDet.getInspectionOrderId());
        List<QmsInspectionOrderDetSample> inspectionOrderDetSamples = qmsInspectionOrderDetSampleMapper.selectByExample(example1);

        if (inspectionOrderDetSamples.size() >= qmsInspectionOrderDetSampleList.size()) {
            List<String> barcodes = new LinkedList<>();
            //提交过来的条码
            for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSampleList) {
                barcodes.add(qmsInspectionOrderDetSample.getBarcode());
                QmsInspectionOrderDetSample detSample = inspectionOrderDetSamples.stream()
                        .filter(item -> item.getBarcode().equals(qmsInspectionOrderDetSample.getBarcode()))
                        .findFirst()
                        .get();
                qmsInspectionOrderDetSample.setFactoryBarcode(detSample.getFactoryBarcode());
                if (StringUtils.isNotEmpty(detSample.getBadnessPhenotypeId())){
                    detSample.setBarcodeStatus((byte)0);
                    qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(detSample);
                }
            }

            for (QmsInspectionOrderDetSample inspectionOrderDetSample : inspectionOrderDetSamples){
                //若属于这个单据的条码没有被提交，则默认set为ok；若提交了则判断是否合格
                if(!barcodes.contains(inspectionOrderDetSample.getBarcode())){
                    QmsInspectionOrderDetSample qmsInspectionOrderDetSample = new QmsInspectionOrderDetSample();
                    qmsInspectionOrderDetSample.setBarcode(inspectionOrderDetSample.getBarcode());
                    qmsInspectionOrderDetSample.setFactoryBarcode(inspectionOrderDetSample.getFactoryBarcode());
                    qmsInspectionOrderDetSample.setInspectionOrderDetId(inspectionOrderDetId);
                    qmsInspectionOrderDetSample.setSampleValue("OK");
                    qmsInspectionOrderDetSample.setBarcodeStatus((byte) 1);
                    qmsInspectionOrderDetSample.setOrgId(user.getOrganizationId());
                    qmsInspectionOrderDetSampleList.add(qmsInspectionOrderDetSample);
                }
            }
        }

        int i = 0;
        int badnessQty = 0;//不良数量
        List<QmsInspectionOrderDetSampleBeforeRecheck> sampleBeforeRechecks = new LinkedList<>();
        for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : qmsInspectionOrderDetSampleList){
            qmsInspectionOrderDetSample.setCreateUserId(user.getUserId());
            qmsInspectionOrderDetSample.setCreateTime(new Date());
            qmsInspectionOrderDetSample.setModifiedUserId(user.getUserId());
            qmsInspectionOrderDetSample.setModifiedTime(new Date());
            qmsInspectionOrderDetSample.setStatus(StringUtils.isEmpty(qmsInspectionOrderDetSample.getStatus())?1: qmsInspectionOrderDetSample.getStatus());
            qmsInspectionOrderDetSample.setOrgId(user.getOrganizationId());
            if(StringUtils.isNotEmpty(qmsInspectionOrderDetSample.getBadnessPhenotypeId())){
                badnessQty++;

                //把不良的条码信息保存起来
                QmsInspectionOrderDetSampleBeforeRecheck sampleBeforeRecheck = new QmsInspectionOrderDetSampleBeforeRecheck();
                BeanUtils.copyProperties(qmsInspectionOrderDetSample, sampleBeforeRecheck);
                sampleBeforeRecheck.setInspectionOrderId(qmsInspectionOrderDet.getInspectionOrderId());
                sampleBeforeRechecks.add(sampleBeforeRecheck);
            }
        }
        i = qmsInspectionOrderDetSampleMapper.insertList(qmsInspectionOrderDetSampleList);
        if(StringUtils.isNotEmpty(sampleBeforeRechecks)){
            qmsInspectionOrderDetSampleBeforeRecheckMapper.insertList(sampleBeforeRechecks);
        }

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

        qmsInspectionOrderDetSample.setModifiedUserId(user.getUserId());
        qmsInspectionOrderDetSample.setModifiedTime(new Date());
        qmsInspectionOrderDetSample.setOrgId(user.getOrganizationId());

        return qmsInspectionOrderDetSampleMapper.updateByPrimaryKeySelective(qmsInspectionOrderDetSample);
    }

    @Override
    public List<QmsInspectionOrderDetSample> findBarcodes(Long inspectionOrderId){
        //增加是否产生质检移位单判断 开始
        QmsInspectionOrder qmsInspectionOrder=qmsInspectionOrderMapper.selectByPrimaryKey(inspectionOrderId);
        if(StringUtils.isEmpty(qmsInspectionOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005);
        }
        String inspectionOrderCode=qmsInspectionOrder.getInspectionOrderCode();
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setRelatedOrderCode(inspectionOrderCode);
        searchWmsInnerJobOrder.setOption1("qmsToInnerJobShift");
        List<WmsInnerJobOrderDto> jobOrderDtoList = innerFeignApi.findList(searchWmsInnerJobOrder).getData();
        if(StringUtils.isEmpty(jobOrderDtoList) || jobOrderDtoList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"成品检验单【"+inspectionOrderCode+"】未生成质检移位单 不能操作");
        }
        //增加是否产生质检移位单判断 结束

        HashMap<String, Object> map = new HashMap<>();
        //List<QmsInspectionOrderDetSample> detSamples = qmsInspectionOrderDetSampleMapper.findList(map);

        map.put("inspectionOrderId",inspectionOrderId);
        List<QmsInspectionOrderDetSample> list = qmsInspectionOrderDetSampleMapper.findList(map);

        /*for (QmsInspectionOrderDetSample qmsInspectionOrderDetSample : list){
            for (QmsInspectionOrderDetSample detSample : detSamples){
                if (qmsInspectionOrderDetSample.getBarcode().equals(detSample.getBarcode()) && StringUtils.isNotEmpty(detSample.getBadnessPhenotypeId())) {
                    qmsInspectionOrderDetSample.setBarcodeStatus((byte) 0);
                }
            }
        }*/

        return list;
    }

    public String getFactoryBarcode(String barcode){
        String factoryBarcode = null;
        if (barcode.length() != 23){
            // 判断是否三星客户条码
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoCheckBarcode");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (!specItems.isEmpty()){
                SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = new SearchMesSfcBarcodeProcess();
                searchMesSfcBarcodeProcess.setIsCustomerBarcode(barcode);
                List<MesSfcBarcodeProcessDto> mesSfcBarcodeProcessDtos = sfcFeignApi.findList(searchMesSfcBarcodeProcess).getData();
                if (!mesSfcBarcodeProcessDtos.isEmpty()){
                    factoryBarcode = mesSfcBarcodeProcessDtos.get(0).getBarcode();
                }
            }
        }

        if (factoryBarcode == null) {
            SearchMesSfcKeyPartRelevance searchMesSfcKeyPartRelevance = new SearchMesSfcKeyPartRelevance();
            searchMesSfcKeyPartRelevance.setPartBarcode(barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtos = sfcFeignApi.findList(searchMesSfcKeyPartRelevance).getData();
            if (!mesSfcKeyPartRelevanceDtos.isEmpty()) {
                factoryBarcode = mesSfcKeyPartRelevanceDtos.get(0).getBarcodeCode();
            }else{
                factoryBarcode = barcode;
            }
        }
        return factoryBarcode;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public QmsInspectionOrderDetSample checkAndSaveBarcode(QmsInspectionOrderDetSample qmsInspectionOrderDetSample) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //获取厂内码
        String factoryBarcode = getFactoryBarcode(qmsInspectionOrderDetSample.getBarcode());

        QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(qmsInspectionOrderDetSample.getInspectionOrderId());

        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setBarcode(factoryBarcode);
        searchWmsInnerInventoryDet.setInspectionOrderCode(qmsInspectionOrder.getInspectionOrderCode());
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if(StringUtils.isEmpty(inventoryDetDtos)){
            throw new BizErrorException("该条码未与该检验单绑定");
        }
        /*WmsInnerInventoryDetDto wmsInnerInventoryDetDto = inventoryDetDtos.get(0);
        if("Z-QC".equals(wmsInnerInventoryDetDto.getStorageCode())){
            throw new BizErrorException("该条码不在质检库位上");
        }*/

        //已扫条码数
        Example example = new Example(QmsInspectionOrderDetSample.class);
        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andEqualTo("inspectionOrderId",qmsInspectionOrderDetSample.getInspectionOrderId());
        int size = qmsInspectionOrderDetSampleMapper.selectCountByExample(example);

        //样本数
        Example example1 = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId",qmsInspectionOrder.getInspectionOrderId());
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example1);
        BigDecimal sampleQty = qmsInspectionOrderDets.get(0).getSampleQty();

        if(StringUtils.isNotEmpty(sampleQty)) {
            if (sampleQty.compareTo(new BigDecimal(size)) == 0) {
                throw new BizErrorException("扫描条码数已达到上限");
            }
        }

        //优化速度 ：PDA端检验
        /*example.clear();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode",qmsInspectionOrderDetSample.getBarcode())
                .andEqualTo("inspectionOrderId",qmsInspectionOrderDetSample.getInspectionOrderId());
        QmsInspectionOrderDetSample inspectionOrderDetSample = qmsInspectionOrderDetSampleMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(inspectionOrderDetSample)){
            throw new BizErrorException("已存在该条码，请勿重复扫描");
        }*/

        qmsInspectionOrderDetSample.setFactoryBarcode(factoryBarcode);
        qmsInspectionOrderDetSample.setBarcodeStatus((byte) 1);
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
