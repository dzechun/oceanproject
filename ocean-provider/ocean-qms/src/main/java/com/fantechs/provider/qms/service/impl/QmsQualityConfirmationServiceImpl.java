package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.SmtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
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
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.qms.mapper.QmsPoorQualityMapper;
import com.fantechs.provider.qms.mapper.QmsQualityConfirmationMapper;
import com.fantechs.provider.qms.service.QmsQualityConfirmationService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<QmsQualityConfirmationDto> findList(Map<String, Object> map) {
        return qmsQualityConfirmationMapper.findList(map);
    }

    @Override
    public QmsQualityConfirmationDto analysis(String code,Byte type) {
        QmsQualityConfirmationDto qmsQualityConfirmationDto = new QmsQualityConfirmationDto();

        //获取工单任务池对象
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
//        searchSmtWorkOrderCardPool.
        ResponseEntity<List<SmtWorkOrderCardPoolDto>> workOrderCardPoolResponse =
                pmFeignApi.findSmtWorkOrderCardPoolList(searchSmtWorkOrderCardPool);
        List<SmtWorkOrderCardPoolDto> poolList = workOrderCardPoolResponse.getData();
        if (StringUtils.isEmpty(poolList) || poolList.size() ==0){
            throw new BizErrorException("流程单号不正确");
        }
        SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto = poolList.get(0);

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
        ResponseEntity<SmtWorkOrder> workOrderResponse = pmFeignApi.workOrderDetail(smtWorkOrderCardPoolDto.getWorkOrderId());
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
//        qmsQualityConfirmationDto.setQuantity(smtProcessListProcessDto.getOutputQuantity());
        qmsQualityConfirmationDto.setProductModelName(productModel.getProductModelName());
        qmsQualityConfirmationDto.setUnit(baseTab.getMainUnit());
        qmsQualityConfirmationDto.setProcessName(smtProcess.getProcessName());
        qmsQualityConfirmationDto.setProcessId(smtProcess.getProcessId());
        qmsQualityConfirmationDto.setSectionName(workshopSection.getSectionName());
        qmsQualityConfirmationDto.setSectionId(workshopSection.getSectionId());

        return qmsQualityConfirmationDto;
    }

    @Override
    public int save(QmsQualityConfirmation qmsQualityConfirmation) {
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
        qmsPoorQualityMapper.insertList(list);



        return i;
    }

    @Override
    public int update(QmsQualityConfirmation entity) {
        return super.update(entity);
    }

    @Override
    public int batchDelete(String ids) {
        return super.batchDelete(ids);
    }
}
