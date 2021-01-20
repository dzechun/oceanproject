package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardPool;
import com.fantechs.common.base.general.entity.qms.QmsPoorQuality;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.general.entity.qms.QmsRejectsMrbReview;
import com.fantechs.common.base.general.entity.qms.history.QmsHtRejectsMrbReview;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.qms.mapper.QmsPoorQualityMapper;
import com.fantechs.provider.qms.mapper.QmsQualityConfirmationMapper;
import com.fantechs.provider.qms.service.QmsQualityConfirmationService;
import org.assertj.core.internal.BigDecimals;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/01/19.
 */
@Service
public class QmsQualityConfirmationServiceImpl extends BaseService<QmsQualityConfirmation> implements QmsQualityConfirmationService {

    @Resource
    private QmsQualityConfirmationMapper qmsQualityConfirmationMapper;
    @Resource
    private QmsPoorQualityMapper qmsPoorQualityMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BasicFeignApi basicFeignApi;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private OutFeignApi outFeignApi;

    @Override
    public List<QmsQualityConfirmationDto> findList(Map<String, Object> map) {
        return qmsQualityConfirmationMapper.findList(map);
    }

    @Override
    public QmsQualityConfirmationDto analysis(String code,Byte type) {
        QmsQualityConfirmationDto qmsQualityConfirmationDto = new QmsQualityConfirmationDto();

        //获取工单任务池对象
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setWorkOrderCardId(code);
        ResponseEntity<List<SmtWorkOrderCardPoolDto>> workOrderCardPoolResponse =
                pmFeignApi.findSmtWorkOrderCardPoolList(searchSmtWorkOrderCardPool);
        List<SmtWorkOrderCardPoolDto> poolList = workOrderCardPoolResponse.getData();
        if (StringUtils.isEmpty(poolList) || poolList.size() ==0){
            throw new BizErrorException("流程单号不正确");
        }
        SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto = poolList.get(0);
        ResponseEntity<SmtWorkOrderCardPool> workOrderCardPoolDetail = pmFeignApi.findSmtWorkOrderCardPoolDetail(smtWorkOrderCardPoolDto.getParentId());
        SmtWorkOrderCardPool parentWorkOrderCardPool = workOrderCardPoolDetail.getData();


        //查询过站信息（报工数据）
        SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
        ResponseEntity<List<SmtProcessListProcessDto>> processListProcessResponse = pmFeignApi.findSmtProcessListProcessList(searchSmtProcessListProcess);
        List<SmtProcessListProcessDto> processListProcessList = processListProcessResponse.getData();
        if (StringUtils.isEmpty(processListProcessList) || processListProcessList.size() == 0){
            throw new BizErrorException("暂无过站信息");
        }
        //获取最后报工数据
        SmtProcessListProcessDto smtProcessListProcessDto = processListProcessList.get(processListProcessList.size()-1);
        Long processId = smtProcessListProcessDto.getProcessId();

        ResponseEntity<SmtProcess> processResponse = basicFeignApi.processDetail(processId);
        SmtProcess smtProcess = processResponse.getData();
        Byte isQuality = smtProcess.getIsQuality();
        if (type.equals(0) && isQuality.equals(0)){
            throw new BizErrorException("当前工序不是最后一道工序");
        }
        ResponseEntity<SmtWorkshopSection> workshopSectionResponse = basicFeignApi.sectionDetail(smtProcess.getSectionId());
        SmtWorkshopSection workshopSection = workshopSectionResponse.getData();

        //获取物料信息
        ResponseEntity<SmtWorkOrder> workOrderResponse = pmFeignApi.workOrderDetail(parentWorkOrderCardPool.getWorkOrderId());
        SmtWorkOrder smtWorkOrder = workOrderResponse.getData();
        ResponseEntity<SmtMaterial> materialResponse = basicFeignApi.materialDetail(smtWorkOrder.getMaterialId());
        SmtMaterial material = materialResponse.getData();
        if (StringUtils.isEmpty(material)){
            throw new BizErrorException("物料不存在");
        }
        BaseTab baseTab = material.getBaseTab();
        ResponseEntity<SmtProductModel> productModelResponse = basicFeignApi.productModelDetail(baseTab.getProductModelId());
        SmtProductModel productModel = productModelResponse.getData();

        qmsQualityConfirmationDto.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
        qmsQualityConfirmationDto.setWorkOrderCode(smtWorkOrderCardPoolDto.getWorkOrderCode());
        qmsQualityConfirmationDto.setMaterialDesc(material.getMaterialDesc());
        qmsQualityConfirmationDto.setMaterialCode(material.getMaterialCode());
        qmsQualityConfirmationDto.setQuantity(smtProcessListProcessDto.getOutputQuantity());
        qmsQualityConfirmationDto.setProductModelName(productModel.getProductModelName());
        qmsQualityConfirmationDto.setUnit(baseTab.getMainUnit());
        qmsQualityConfirmationDto.setProcessName(smtProcess.getProcessName());
        qmsQualityConfirmationDto.setProcessId(smtProcess.getProcessId());
        qmsQualityConfirmationDto.setSectionName(workshopSection.getSectionName());
        qmsQualityConfirmationDto.setSectionId(workshopSection.getSectionId());
        qmsQualityConfirmationDto.setWorkOrderId(smtWorkOrderCardPoolDto.getWorkOrderId());
        qmsQualityConfirmationDto.setMaterialId(material.getMaterialId());

        return qmsQualityConfirmationDto;
    }

    @Override
    public int save(QmsQualityConfirmationDto qmsQualityConfirmation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsQualityConfirmation.setCreateTime(new Date());
        qmsQualityConfirmation.setCreateUserId(user.getUserId());
        qmsQualityConfirmation.setModifiedTime(new Date());
        qmsQualityConfirmation.setModifiedUserId(user.getUserId());
        qmsQualityConfirmation.setStatus(StringUtils.isEmpty(qmsQualityConfirmation.getStatus())?1:qmsQualityConfirmation.getStatus());
        qmsQualityConfirmation.setOrganizationId(user.getOrganizationId());
        qmsQualityConfirmation.setQualityConfirmationCode(CodeUtils.getId("PZQR"));

        int i = qmsQualityConfirmationMapper.insertUseGeneratedKeys(qmsQualityConfirmation);

        List<QmsPoorQuality> list = qmsQualityConfirmation.getList();
        for (QmsPoorQuality qmsPoorQuality : list) {
            qmsPoorQuality.setQualityId(qmsQualityConfirmation.getQualityConfirmationId());
        }
        qmsPoorQualityMapper.insertList(list);

        if (qmsQualityConfirmation.getQualityType().equals(1)){
            return i;
        }

        SearchSmtStorageMaterial searchSmtStorageMaterial = new SearchSmtStorageMaterial();
        searchSmtStorageMaterial.setMaterialId(qmsQualityConfirmation.getMaterialId());
        ResponseEntity<List<SmtStorageMaterial>> storageMaterialList = basicFeignApi.findStorageMaterialList(searchSmtStorageMaterial);
        List<SmtStorageMaterial> data = storageMaterialList.getData();
        if (StringUtils.isEmpty(data)){
            throw new BizErrorException("未找到该物料的储位");
        }

        //完工入库
        WmsInFinishedProduct wmsInFinishedProduct = new WmsInFinishedProduct();
        wmsInFinishedProduct.setWorkOrderId(qmsQualityConfirmation.getWorkOrderId());
        wmsInFinishedProduct.setOperatorUserId(user.getUserId());
        wmsInFinishedProduct.setInTime(new Date());
        wmsInFinishedProduct.setInType(Byte.parseByte("1"));
        wmsInFinishedProduct.setProjectType("dp");
        wmsInFinishedProduct.setInStatus(Byte.parseByte("2"));
        List<WmsInFinishedProductDet> wmsInFinishedProductDetList = new ArrayList<>();
        WmsInFinishedProductDet wmsInFinishedProductDet = new WmsInFinishedProductDet();

        wmsInFinishedProductDet.setMaterialId(qmsQualityConfirmation.getMaterialId());
        wmsInFinishedProductDet.setStorageId(data.get(0).getStorageId());
        wmsInFinishedProductDet.setPlanInQuantity(qmsQualityConfirmation.getQualifiedQuantity());
        wmsInFinishedProductDet.setInQuantity(qmsQualityConfirmation.getQualifiedQuantity());
        wmsInFinishedProductDet.setInTime(new Date());
        wmsInFinishedProductDet.setDeptId(user.getDeptId());
        wmsInFinishedProductDet.setInStatus(Byte.parseByte("2"));

        wmsInFinishedProductDetList.add(wmsInFinishedProductDet);
        wmsInFinishedProduct.setWmsInFinishedProductDetList(wmsInFinishedProductDetList);
        inFeignApi.inFinishedProductAdd(wmsInFinishedProduct);

        //生成领料计划
        WmsOutProductionMaterial wmsOutProductionMaterial = new WmsOutProductionMaterial();
        wmsOutProductionMaterial.setWorkOrderId(qmsQualityConfirmation.getWorkOrderId());
        wmsOutProductionMaterial.setMaterialId(qmsQualityConfirmation.getMaterialId());
        wmsOutProductionMaterial.setPlanQty(qmsQualityConfirmation.getQualifiedQuantity());
        wmsOutProductionMaterial.setOutTime(new Date());
        wmsOutProductionMaterial.setOutStatus(Byte.parseByte("0"));
        wmsOutProductionMaterial.setStorageId(data.get(0).getStorageId());
        wmsOutProductionMaterial.setProLineId(qmsQualityConfirmation.getProLineId());
        outFeignApi.outProductionMaterialAdd(wmsOutProductionMaterial);

        return i;
    }

    @Override
    public int update(QmsQualityConfirmation qmsQualityConfirmation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsQualityConfirmation.setModifiedTime(new Date());
        qmsQualityConfirmation.setModifiedUserId(user.getUserId());

//        QmsHtRejectsMrbReview qmsHtRejectsMrbReview = new QmsHtRejectsMrbReview();
//        BeanUtils.copyProperties(qmsQualityConfirmation,qmsHtRejectsMrbReview);
//        qmsHtRejectsMrbReviewMapper.insert(qmsHtRejectsMrbReview);
        return qmsQualityConfirmationMapper.updateByPrimaryKeySelective(qmsQualityConfirmation);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
//        List<QmsHtRejectsMrbReview> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsQualityConfirmation qmsQualityConfirmation = qmsQualityConfirmationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsQualityConfirmation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

//            QmsHtRejectsMrbReview qmsHtRejectsMrbReview = new QmsHtRejectsMrbReview();
//            BeanUtils.copyProperties(qmsQualityConfirmation,qmsHtRejectsMrbReview);
//            qmsHtQualityInspections.add(qmsHtRejectsMrbReview);
        }

//        qmsHtRejectsMrbReviewMapper.insertList(qmsHtQualityInspections);
        Example example = new Example(QmsPoorQuality.class);
        example.createCriteria().andEqualTo("qualityId", Arrays.asList(ids.split(",")));
        qmsPoorQualityMapper.deleteByExample(example);

        return qmsQualityConfirmationMapper.deleteByIds(ids);
    }
}
