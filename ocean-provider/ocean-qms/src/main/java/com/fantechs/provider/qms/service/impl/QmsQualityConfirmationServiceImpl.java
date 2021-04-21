package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.BaseStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.*;
import com.fantechs.common.base.general.dto.qms.QmsBadItemDto;
import com.fantechs.common.base.general.dto.qms.QmsPoorQualityDto;
import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutProductionMaterialDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlatePartsDet;
import com.fantechs.common.base.general.entity.qms.QmsPoorQuality;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterialdDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutProductionMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.BcmFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.qms.mapper.QmsBadItemMapper;
import com.fantechs.provider.qms.mapper.QmsPoorQualityMapper;
import com.fantechs.provider.qms.mapper.QmsQualityConfirmationMapper;
import com.fantechs.provider.qms.service.QmsQualityConfirmationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by leifengzhi on 2021/01/19.
 */
@Service
public class QmsQualityConfirmationServiceImpl extends BaseService<QmsQualityConfirmation> implements QmsQualityConfirmationService {

    @Resource
    private QmsQualityConfirmationMapper qmsQualityConfirmationMapper;
    @Resource
    private QmsPoorQualityMapper qmsPoorQualityMapper;
    @Resource
    private QmsBadItemMapper qmsBadItemMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private OutFeignApi outFeignApi;

    @Override
    public List<QmsQualityConfirmationDto> findList(Map<String, Object> map) {
        List<QmsQualityConfirmationDto> list = qmsQualityConfirmationMapper.findList(map);

        for (QmsQualityConfirmationDto qmsQualityConfirmationDto : list) {
            SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(qmsQualityConfirmationDto.getWorkOrderCardPoolId());
            List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
            if (StringUtils.isNotEmpty(workOrderCardPoolList) && (workOrderCardPoolList.get(0).getParentId() == 0 || workOrderCardPoolList.get(0).getParentId() == null)) {
                Integer quantity = this.parentUpdateQuantity(qmsQualityConfirmationDto.getWorkOrderCardPoolId(), qmsQualityConfirmationDto.getProcessId());
                qmsQualityConfirmationDto.setQuantity(new BigDecimal(quantity));
            } else {
                map.put("workOrderCardPoolId", qmsQualityConfirmationDto.getWorkOrderCardPoolId());
                map.put("processId", qmsQualityConfirmationDto.getProcessId());
                map.put("qualityType", qmsQualityConfirmationDto.getQualityType());

                Integer quantity = this.updateQuantity(map);
                qmsQualityConfirmationDto.setQuantity(new BigDecimal(quantity == null ? 0 : quantity));
            }

            if (qmsQualityConfirmationDto.getRouteId() == null) {
                continue;
            }
            ResponseEntity<List<BaseRouteProcess>> routeProcessResponse = baseFeignApi.findConfigureRout(qmsQualityConfirmationDto.getRouteId());
            List<BaseRouteProcess> routeProcesses = routeProcessResponse.getData();
            qmsQualityConfirmationDto.getSectionList().addAll(getBad(routeProcesses));
        }
        return list;
    }

    @Override
    public QmsQualityConfirmationDto analysis(String code, Byte type) {
        QmsQualityConfirmationDto qmsQualityConfirmationDto = new QmsQualityConfirmationDto();

        //获取工单任务池对象
        ProcessListWorkOrderDTO data = pmFeignApi.selectWorkOrderDtoByWorkOrderCardId(code).getData();
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setWorkOrderCardId(code);
        List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findSmtWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
        if (StringUtils.isEmpty(workOrderCardPoolList)) {
            throw new BizErrorException("流程单号不正确");
        }
        //当前流程单的对象
        if (type == 1 && (workOrderCardPoolList.get(0).getParentId() == null || workOrderCardPoolList.get(0).getParentId() == 0)) {
            throw new BizErrorException("当前为父级流程单");
        } else if (type == 2 && (workOrderCardPoolList.get(0).getParentId() == null || workOrderCardPoolList.get(0).getParentId() == 0)) {
            qmsQualityConfirmationDto.setParentWorkOrderCardPoolId(workOrderCardPoolList.get(0).getWorkOrderCardPoolId());
        }

        //获取工艺路线
        ResponseEntity<List<BaseRouteProcess>> routeProcessResponse = baseFeignApi.findConfigureRout(data.getRouteId());
        List<BaseRouteProcess> routeProcesses = routeProcessResponse.getData();

        qmsQualityConfirmationDto.getSectionList().addAll(getBad(routeProcesses));

        qmsQualityConfirmationDto.setProductionQuantity(data.getWorkOrderQuantity());
        qmsQualityConfirmationDto.setWorkOrderCardPoolId(data.getWorkOrderCardPoolId());
        qmsQualityConfirmationDto.setWorkOrderCode(data.getWorkOrderCode());
        qmsQualityConfirmationDto.setMaterialDesc(data.getMaterialDesc());
        qmsQualityConfirmationDto.setMaterialCode(data.getMaterialCode());
        qmsQualityConfirmationDto.setProductModelName(data.getProductModelName());
        qmsQualityConfirmationDto.setUnit(data.getMainUnit());
        qmsQualityConfirmationDto.setRouteId(data.getRouteId());
        qmsQualityConfirmationDto.setWorkOrderId(data.getWorkOrderId());
        qmsQualityConfirmationDto.setMaterialId(data.getMaterialId());

        return qmsQualityConfirmationDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsQualityConfirmationDto qmsQualityConfirmation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //获取当前报工工序信息
        ResponseEntity<BaseProcess> processResponse = baseFeignApi.processDetail(qmsQualityConfirmation.getProcessId());
        BaseProcess baseProcess = processResponse.getData();
        if (StringUtils.isEmpty(baseProcess)){
            throw new BizErrorException("当前报工工序不存在");
        }

        if (qmsQualityConfirmation.getQualityType() == 1) {

            SearchBasePlatePartsDet searchBasePlatePartsDet = new SearchBasePlatePartsDet();
            searchBasePlatePartsDet.setPlatePartsDetId(qmsQualityConfirmation.getPartsInformationId() == 0 ? qmsQualityConfirmation.getMaterialId() : qmsQualityConfirmation.getPartsInformationId());
            List<BasePlatePartsDetDto> platePartsDetList = baseFeignApi.findPlatePartsDetList(searchBasePlatePartsDet).getData();
            if (StringUtils.isEmpty(platePartsDetList)) {
                throw new BizErrorException("未找到部件信息");
            }
            BasePlatePartsDetDto basePlatePartsDet = platePartsDetList.get(0);

            List<BaseRouteProcess> routeProcesses = baseFeignApi.findConfigureRout(basePlatePartsDet.getRouteId()).getData();


            List<BaseRouteProcess> routeProcessList = new ArrayList<>();
            //筛选出当前报工工序的工段对应工艺路线里面的所有工序
            for (BaseRouteProcess baseRouteProcess : routeProcesses) {
                if (baseRouteProcess.getSectionId().equals(baseProcess.getSectionId())) {
                    routeProcessList.add(baseRouteProcess);
                }
            }
            if (StringUtils.isEmpty(routeProcessList)) {
                throw new BizErrorException("报工工序不属于当前工艺路线");
            }
            //获取当前报工工序所属工段的最后工序
            BaseRouteProcess baseRouteProcess = routeProcessList.get(routeProcessList.size() - 1);
            Byte isQuality = baseProcess.getIsQuality();
            if (qmsQualityConfirmation.getQualityType() == 1 && (isQuality == null || isQuality == 0)) {
                throw new BizErrorException("当前工序不是品质确认工序");
            }
            if (!(baseRouteProcess.getProcessId().equals(qmsQualityConfirmation.getProcessId())) && qmsQualityConfirmation.getQualityType() == 1) {
                throw new BizErrorException("当前工序不是最后一道工序");
            }
        }

        int i = 0;

        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(qmsQualityConfirmation.getWorkOrderCardPoolId());

        List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
        if (StringUtils.isEmpty(smtWorkOrderCardPoolList)) {
            throw new BizErrorException("未找到流程单信息");
        }

        SmtWorkOrderCardPoolDto smtWorkOrderCardPool = smtWorkOrderCardPoolList.get(0);
        Byte type = smtWorkOrderCardPool.getType();
        List<QmsQualityConfirmationDto> qualityConfirmationDtos = null;
        Map map = new HashMap();
        map.put("workOrderCardPoolId", qmsQualityConfirmation.getWorkOrderCardPoolId());
        map.put("processId", qmsQualityConfirmation.getProcessId());
        //如果是成品抽检就打印条码
        if (StringUtils.isEmpty(smtWorkOrderCardPool.getParentId()) && (type == null || type != 3)) {
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(smtWorkOrderCardPool.getWorkOrderId());
            List<MesPmWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (StringUtils.isNotEmpty(workOrderList)) {
                //获取成品工艺路线
                List<BaseRouteProcess> routeProcesses = baseFeignApi.findConfigureRout(workOrderList.get(0).getRouteId()).getData();
                //判断成品抽检的工序是否是最后一道工序
                if (StringUtils.isNotEmpty(routeProcesses) && routeProcesses.get(routeProcesses.size() - 1).getProcessId() == qmsQualityConfirmation.getProcessId()) {
                    //打印成品条码
                }
                //修改工单状态
                if (workOrderList.get(0).getWorkOrderQty().compareTo(qmsQualityConfirmation.getQualifiedQuantity()) == 0) {
                    pmFeignApi.updateStatus(workOrderList.get(0).getWorkOrderId(), 4);
                }
            }

        }

        if (type != null && type == 3) {
//            searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(null);
//            searchSmtWorkOrderCardPool.setWorkOrderId(qmsQualityConfirmation.getWorkOrderId());
//            searchSmtWorkOrderCardPool.setType((byte) 2);
//            List<SmtWorkOrderCardPoolDto> workOrderCardPoolDtoList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
            SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto = this.getSubbatchPart(smtWorkOrderCardPool.getParentId());
            if (smtWorkOrderCardPoolDto == null) {
                throw new BizErrorException("未找到该子批的部件流转卡信息");
            }
            if (StringUtils.isNotEmpty(smtWorkOrderCardPoolDto)) {
                map.put("workOrderCardPoolId", smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
            }


            if (qmsQualityConfirmation.getQualityType() == 1) {
                qualityConfirmationDtos = qmsQualityConfirmationMapper.findList(map);
                qmsQualityConfirmation.setTotal(qmsQualityConfirmation.getQualifiedQuantity().add(qmsQualityConfirmation.getUnqualifiedQuantity()));
                qmsQualityConfirmation.setTotalQualified(qmsQualityConfirmation.getQualifiedQuantity());
                if (StringUtils.isNotEmpty(qualityConfirmationDtos)) {
                    QmsQualityConfirmationDto qmsQualityConfirmationDto = qualityConfirmationDtos.get(0);
                    qmsQualityConfirmation.setTotal(qmsQualityConfirmation.getTotal().add(qmsQualityConfirmationDto.getTotal() == null ? new BigDecimal(0) : qmsQualityConfirmationDto.getTotal()));
                    qmsQualityConfirmation.setTotalQualified(qmsQualityConfirmation.getTotalQualified().add(qmsQualityConfirmationDto.getTotalQualified() == null ? new BigDecimal(0) : qmsQualityConfirmationDto.getTotalQualified()));
                }
            }

            map.put("workOrderCardPoolId", smtWorkOrderCardPool.getParentId());
            qualityConfirmationDtos = qmsQualityConfirmationMapper.findList(map);

            QmsQualityConfirmationDto qmsQualityConfirmationDto = qualityConfirmationDtos.get(0);
            qmsQualityConfirmationDto.setTotalQualified(qmsQualityConfirmationDto.getQualifiedQuantity().add(qmsQualityConfirmation.getQualifiedQuantity()));
            //qmsQualityConfirmationDto.setUnqualifiedQuantity(qmsQualityConfirmationDto.getUnqualifiedQuantity().add(qmsQualityConfirmation.getUnqualifiedQuantity()));
            qmsQualityConfirmationDto.setQualityType((byte) 1);

            qmsQualityConfirmationMapper.updateByPrimaryKey(qmsQualityConfirmationDto);
            qmsQualityConfirmation.setCreateTime(new Date());
            qmsQualityConfirmation.setCreateUserId(user.getUserId());
            qmsQualityConfirmation.setModifiedTime(new Date());
            qmsQualityConfirmation.setModifiedUserId(user.getUserId());
            qmsQualityConfirmation.setStatus(StringUtils.isEmpty(qmsQualityConfirmation.getStatus()) ? 1 : qmsQualityConfirmation.getStatus());
            qmsQualityConfirmation.setOrganizationId(user.getOrganizationId());
            qmsQualityConfirmation.setQualityConfirmationCode(CodeUtils.getId("PZQR"));
            i = qmsQualityConfirmationMapper.insertUseGeneratedKeys(qmsQualityConfirmation);
        } else if (qmsQualityConfirmation.getQualityConfirmationId() == null || qmsQualityConfirmation.getQualityConfirmationId() == 0) {
            if (qmsQualityConfirmation.getQualityType() == 1) {
                qualityConfirmationDtos = qmsQualityConfirmationMapper.findList(map);
                qmsQualityConfirmation.setTotal(qmsQualityConfirmation.getQualifiedQuantity().add(qmsQualityConfirmation.getUnqualifiedQuantity()));
                qmsQualityConfirmation.setTotalQualified(qmsQualityConfirmation.getQualifiedQuantity());
                if (StringUtils.isNotEmpty(qualityConfirmationDtos)) {
                    QmsQualityConfirmationDto qmsQualityConfirmationDto = qualityConfirmationDtos.get(0);
                    qmsQualityConfirmation.setTotal(qmsQualityConfirmation.getTotal().add(qmsQualityConfirmationDto.getTotal() == null ? new BigDecimal(0) : qmsQualityConfirmationDto.getTotal()));
                    qmsQualityConfirmation.setTotalQualified(qmsQualityConfirmation.getTotalQualified().add(qmsQualityConfirmationDto.getTotalQualified() == null ? new BigDecimal(0) : qmsQualityConfirmationDto.getTotalQualified()));
                }
            }

            qmsQualityConfirmation.setCreateTime(new Date());
            qmsQualityConfirmation.setCreateUserId(user.getUserId());
            qmsQualityConfirmation.setModifiedTime(new Date());
            qmsQualityConfirmation.setModifiedUserId(user.getUserId());
            qmsQualityConfirmation.setStatus(StringUtils.isEmpty(qmsQualityConfirmation.getStatus()) ? 1 : qmsQualityConfirmation.getStatus());
            qmsQualityConfirmation.setOrganizationId(user.getOrganizationId());
            qmsQualityConfirmation.setQualityConfirmationCode(CodeUtils.getId("PZQR"));

            i = qmsQualityConfirmationMapper.insertUseGeneratedKeys(qmsQualityConfirmation);
        } else {
            qmsQualityConfirmation.setModifiedTime(new Date());
            qmsQualityConfirmation.setModifiedUserId(user.getUserId());
            qmsQualityConfirmation.setOrganizationId(user.getOrganizationId());
            Example example = new Example(QmsPoorQuality.class);
            example.createCriteria().andEqualTo("qualityId", qmsQualityConfirmation.getQualityConfirmationId());
            qmsPoorQualityMapper.deleteByExample(example);

            i = qmsQualityConfirmationMapper.updateByPrimaryKeySelective(qmsQualityConfirmation);
        }

        List<QmsPoorQualityDto> list = qmsQualityConfirmation.getSeledBadItemList();
        if (StringUtils.isNotEmpty(list)) {
            List<QmsPoorQualityDto> qualityDtoList = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                list.get(j).setQualityId(qmsQualityConfirmation.getQualityConfirmationId());
                boolean b = true;
                for (int k = j + 1; k < list.size(); k++) {
                    if (list.get(j).getSectionId().equals(list.get(k).getSectionId())  && list.get(j).getBadItemDetId() == list.get(k).getBadItemDetId()) {
                        list.get(j).setBadQuantity(list.get(j).getBadQuantity().add(list.get(k).getBadQuantity()));
                    }
                }
                if (b) {
                    for (QmsPoorQualityDto qmsPoorQualityDto : qualityDtoList) {
                        if (list.get(j).getBadItemDetId().equals(qmsPoorQualityDto.getBadItemDetId()) && list.get(j).getSectionId() == qmsPoorQualityDto.getSectionId()) {
                            b = false;
                            break;
                        }
                    }
                }
                if (b) {
                    qualityDtoList.add(list.get(j));
                }
            }
            qmsPoorQualityMapper.insertList(qualityDtoList);
        }

        if (qmsQualityConfirmation.getQualityType() == 2 || !(qmsQualityConfirmation.getAffirmStatus() == 2)) {
            return i;
        }

        SmtWorkOrderCardPoolDto workOrderCardPoolDto = null;
        BigDecimal minMatchingQuantity = new BigDecimal(0);
        BigDecimal alreadyMatchingQuantity = new BigDecimal(0);
        if (type != null && type == 3) {
//            searchSmtWorkOrderCardPool.setWorkOrderId(smtWorkOrderCardPool.getWorkOrderId());
//            searchSmtWorkOrderCardPool.setType((byte) 2);
//            List<SmtWorkOrderCardPoolDto> parentWorkOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
//            if (StringUtils.isEmpty(parentWorkOrderCardPoolList)){
//                throw new BizErrorException("未找到该子批的部件流转卡信息");
//            }
//
            SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto = this.getSubbatchPart(smtWorkOrderCardPool.getParentId());
            if (smtWorkOrderCardPoolDto == null) {
                throw new BizErrorException("未找到该子批的部件流转卡信息");
            }
            searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getParentId());
            List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtos = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
            if (StringUtils.isEmpty(smtWorkOrderCardPoolDtos)) {
                throw new BizErrorException("未找到产品流转卡信息");
            }

            workOrderCardPoolDto = smtWorkOrderCardPoolDtos.get(0);
        } else {
            searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(smtWorkOrderCardPool.getParentId());
            List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
            if (StringUtils.isEmpty(workOrderCardPoolList)) {
                throw new BizErrorException("未找到产品流转卡信息");
            }
            workOrderCardPoolDto = workOrderCardPoolList.get(0);
        }

        if (qmsQualityConfirmation.getQualityType() != null && qmsQualityConfirmation.getQualityType() == 1) {

            if (workOrderCardPoolDto.getParentId() == null || workOrderCardPoolDto.getParentId() == 0) {
                BaseProcess process = baseFeignApi.processDetail(qmsQualityConfirmation.getProcessId()).getData();
                if (StringUtils.isNotEmpty(process)) {
                    MesPmMatchingDto matchingDto = pmFeignApi.findMinMatchingQuantity(workOrderCardPoolDto.getWorkOrderCardId(), process.getSectionId(), qmsQualityConfirmation.getTotalQualified() == null ? new BigDecimal(0) : qmsQualityConfirmation.getTotalQualified(), qmsQualityConfirmation.getWorkOrderCardPoolId()).getData();
                    if (matchingDto != null) {
                        minMatchingQuantity = matchingDto.getMinMatchingQuantity();
                        SearchWmsOutProductionMaterial searchWmsOutProductionMaterial = new SearchWmsOutProductionMaterial();
                        searchWmsOutProductionMaterial.setWorkOrderId(workOrderCardPoolDto.getWorkOrderId());
                        searchWmsOutProductionMaterial.setProcessId(qmsQualityConfirmation.getProcessId());
                        List<WmsOutProductionMaterialDto> outProductionMaterialList = outFeignApi.findList(searchWmsOutProductionMaterial).getData();
                        for (WmsOutProductionMaterialDto wmsOutProductionMaterialDto : outProductionMaterialList) {
                            alreadyMatchingQuantity = alreadyMatchingQuantity.add(wmsOutProductionMaterialDto.getPlanQty());
                        }
                    }
                }

            }
        }

        alreadyMatchingQuantity = alreadyMatchingQuantity == null ? new BigDecimal(0) : alreadyMatchingQuantity;
        minMatchingQuantity = minMatchingQuantity == null ? new BigDecimal(0) : minMatchingQuantity;

        BigDecimal quantity = minMatchingQuantity.subtract(alreadyMatchingQuantity);
        if (quantity.compareTo(new BigDecimal(0)) == 1) {
            System.out.println("生成生产领料计划");
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(workOrderCardPoolDto.getWorkOrderId());
            List<MesPmWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (StringUtils.isEmpty(workOrderList)) {
                throw new BizErrorException("未找到产品工单信息");
            }

            SearchBaseStorageMaterial searchBaseStorageMaterial = new SearchBaseStorageMaterial();
            searchBaseStorageMaterial.setMaterialId(workOrderList.get(0).getMaterialId());
            ResponseEntity<List<BaseStorageMaterial>> storageMaterialList = baseFeignApi.findStorageMaterialList(searchBaseStorageMaterial);
            List<BaseStorageMaterial> data = storageMaterialList.getData();
            if (StringUtils.isEmpty(data)) {
                throw new BizErrorException("未找到该物料的储位");
            }

            SearchSmtWorkOrderBom searchSmtWorkOrderBom = new SearchSmtWorkOrderBom();
            searchSmtWorkOrderBom.setWorkOrderId(workOrderList.get(0).getWorkOrderId());
            List<SmtWorkOrderBomDto> workOrderBomList = pmFeignApi.findWordOrderBomList(searchSmtWorkOrderBom).getData();
            SmtWorkOrderBomDto workOrderBomDto = null;
            for (SmtWorkOrderBomDto smtWorkOrderBomDto : workOrderBomList) {
                //获取当前报工工序信息
                processResponse = baseFeignApi.processDetail(smtWorkOrderBomDto.getProcessId());
                BaseProcess process = processResponse.getData();
                if (StringUtils.isEmpty(process)){
                    throw new BizErrorException("当前报工工序不存在");
                }
                if (StringUtils.isNotEmpty(baseProcess) && baseProcess.getSectionId().equals(process.getSectionId())) {
                    workOrderBomDto = smtWorkOrderBomDto;
                    break;
                }
            }
            if (StringUtils.isEmpty(workOrderBomDto)) {
                throw new BizErrorException("没有找到当前工序对应的工单BOM信息");
            }

            SearchMesPmMasterPlanListDTO searchMesPmMasterPlanListDTO = new SearchMesPmMasterPlanListDTO();
            searchMesPmMasterPlanListDTO.setWorkOrderId(workOrderList.get(0).getWorkOrderId());
            List<MesPmMasterPlanDTO> pmMasterPlanList = pmFeignApi.findPmMasterPlanlist(searchMesPmMasterPlanListDTO).getData();

            if (StringUtils.isEmpty(pmMasterPlanList)) {
                throw new BizErrorException("未找到产品工单的月计划");
            }

            //半成品完工入库
            WmsInFinishedProduct wmsInFinishedProduct = new WmsInFinishedProduct();
            wmsInFinishedProduct.setWorkOrderId(workOrderCardPoolDto.getWorkOrderId());
            wmsInFinishedProduct.setOperatorUserId(user.getUserId());
            wmsInFinishedProduct.setInTime(new Date());
            wmsInFinishedProduct.setInType(Byte.parseByte("1"));
            wmsInFinishedProduct.setProjectType("dp");
            wmsInFinishedProduct.setInStatus(Byte.parseByte("2"));
            //半成品完工入库明细
            List<WmsInFinishedProductDet> wmsInFinishedProductDetList = new ArrayList<>();
            WmsInFinishedProductDet wmsInFinishedProductDet = new WmsInFinishedProductDet();
            wmsInFinishedProductDet.setMaterialId(workOrderBomDto.getPartMaterialId());
            wmsInFinishedProductDet.setStorageId(data.get(0).getStorageId());
            wmsInFinishedProductDet.setPlanInQuantity(quantity);
            wmsInFinishedProductDet.setInQuantity(quantity);
            wmsInFinishedProductDet.setInTime(new Date());
            wmsInFinishedProductDet.setDeptId(user.getDeptId());
            wmsInFinishedProductDet.setInStatus(Byte.parseByte("2"));

            wmsInFinishedProductDetList.add(wmsInFinishedProductDet);
            wmsInFinishedProduct.setWmsInFinishedProductDetList(wmsInFinishedProductDetList);

            ResponseEntity<WmsInFinishedProduct> wmsInFinishedProductResponse = inFeignApi.inFinishedProductAdd(wmsInFinishedProduct);
            WmsInFinishedProduct inFinishedProduct = wmsInFinishedProductResponse.getData();
            if (StringUtils.isEmpty(inFinishedProduct)) {
                throw new BizErrorException("生成半成品入库单失败");
            }

            //生成领料计划
            WmsOutProductionMaterial wmsOutProductionMaterial = new WmsOutProductionMaterial();
            wmsOutProductionMaterial.setFinishedProductCode(inFinishedProduct.getFinishedProductCode());
            wmsOutProductionMaterial.setWorkOrderId(workOrderCardPoolDto.getWorkOrderId());
            wmsOutProductionMaterial.setMaterialId(workOrderBomDto.getPartMaterialId());
            wmsOutProductionMaterial.setPlanQty(quantity);
            wmsOutProductionMaterial.setRealityQty(new BigDecimal(0));
            wmsOutProductionMaterial.setProcessId(qmsQualityConfirmation.getProcessId());
            wmsOutProductionMaterial.setOutTime(new Date());
            wmsOutProductionMaterial.setOutStatus(Byte.parseByte("0"));
            wmsOutProductionMaterial.setStorageId(data.get(0).getStorageId());
            wmsOutProductionMaterial.setProLineId(pmMasterPlanList.get(0).getProLineId());
            wmsOutProductionMaterial = outFeignApi.outProductionMaterialAdd(wmsOutProductionMaterial).getData();

            if (wmsOutProductionMaterial != null) {
                searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
                searchSmtWorkOrderCardPool.setParentId(smtWorkOrderCardPool.getParentId());
                List<SmtWorkOrderCardPoolDto> workOrderCardPoolDtoList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
                if (StringUtils.isNotEmpty(workOrderCardPoolDtoList)) {
                    Map<String,Object> qualityConditions = new HashMap<>();
                    qualityConditions.put("sectionId", baseProcess.getSectionId());
                    for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : workOrderCardPoolDtoList) {
                        boolean ifProcess = qmsQualityConfirmation.getWorkOrderCardPoolId().equals(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                        qualityConditions.put("workOrderCardPoolId",smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                        List<QmsQualityConfirmationDto> qmsQualityConfirmationList = qmsQualityConfirmationMapper.findList(qualityConditions);
                        if (StringUtils.isEmpty(qmsQualityConfirmationList)){
                            continue;
                        }
                        SearchBasePlatePartsDet searchBasePlatePartsDet = new SearchBasePlatePartsDet();
                        searchBasePlatePartsDet.setPlatePartsDetId(smtWorkOrderCardPoolDto.getMaterialId());
                        List<BasePlatePartsDetDto> basePlatePartsDetDtos = baseFeignApi.findPlatePartsDetList(searchBasePlatePartsDet).getData();
                        WmsOutProductionMaterialdDet wmsOutProductionMaterialdDet = new WmsOutProductionMaterialdDet();
                        wmsOutProductionMaterialdDet.setQuantity(basePlatePartsDetDtos.size() != 0 ? basePlatePartsDetDtos.get(0).getUseQty() : new BigDecimal(1));
                        wmsOutProductionMaterialdDet.setProductionMaterialId(wmsOutProductionMaterial.getProductionMaterialId());
                        wmsOutProductionMaterialdDet.setProcessId(ifProcess ?qmsQualityConfirmation.getProcessId():qmsQualityConfirmationList.get(0).getProcessId());
                        wmsOutProductionMaterialdDet.setMaterialId(smtWorkOrderCardPoolDto.getMaterialId());
                        wmsOutProductionMaterialdDet.setWorkOrderId(smtWorkOrderCardPoolDto.getWorkOrderId());
                        wmsOutProductionMaterialdDet.setRealityQty(quantity);
                        wmsOutProductionMaterialdDet.setScanQty(new BigDecimal(0));
                        wmsOutProductionMaterialdDet.setUseQty(new BigDecimal(0));
                        outFeignApi.add(wmsOutProductionMaterialdDet);
                    }
                }
            }
            //发送邮箱
//            bcmFeignApi.sendHtmlMail()
        }

        return i;
    }

    //使用递归获取子批的部件流转卡
    public SmtWorkOrderCardPoolDto getSubbatchPart(long parentId) {
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(parentId);
        List<SmtWorkOrderCardPoolDto> parentWorkOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
        if (StringUtils.isEmpty(parentWorkOrderCardPoolList)) {
            return null;
        }
        if (parentWorkOrderCardPoolList.get(0).getType() != null && parentWorkOrderCardPoolList.get(0).getType() == 2) {
            return parentWorkOrderCardPoolList.get(0);
        }
        if (parentWorkOrderCardPoolList.get(0).getParentId() == null || parentWorkOrderCardPoolList.get(0).getParentId() == 0) {
            throw new BizErrorException("流程卡类型数据不完整");
        }
        return this.getSubbatchPart(parentWorkOrderCardPoolList.get(0).getParentId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsQualityConfirmation qmsQualityConfirmation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsQualityConfirmation.setModifiedTime(new Date());
        qmsQualityConfirmation.setModifiedUserId(user.getUserId());

        return qmsQualityConfirmationMapper.updateByPrimaryKeySelective(qmsQualityConfirmation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            QmsQualityConfirmation qmsQualityConfirmation = qmsQualityConfirmationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsQualityConfirmation)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }

        Example example = new Example(QmsPoorQuality.class);
        example.createCriteria().andEqualTo("qualityId", Arrays.asList(ids.split(",")));
        qmsPoorQualityMapper.deleteByExample(example);

        return qmsQualityConfirmationMapper.deleteByIds(ids);
    }

    public List<QmsBadItemDto> getBad(List<BaseRouteProcess> routeProcesses) {
        Map<String, Object> search = new HashMap();
        List<Long> sections = new ArrayList<>();
        List<QmsBadItemDto> sectionList = new ArrayList<>();
        Map<Long, QmsBadItemDto> map = new HashMap<>();

        if (StringUtils.isNotEmpty(routeProcesses)) {
            for (BaseRouteProcess routeProcess : routeProcesses) {
                int is = 0;
                for (Long section : sections) {
                    if (section.equals(routeProcess.getSectionId())) {
                        is++;
                        break;
                    }
                }
                if (is == 0) {
                    search.put("sectionId", routeProcess.getSectionId());
                    List<QmsBadItemDto> list = qmsBadItemMapper.findList(search);

                    QmsBadItemDto badItemDto = new QmsBadItemDto();
                    badItemDto.setSectionId(routeProcess.getSectionId());
                    badItemDto.setSectionName(routeProcess.getSectionName());
                    badItemDto.setProcessId(routeProcess.getProcessId());
                    badItemDto.setProcessName(routeProcess.getProcessName());
                    for (QmsBadItemDto qmsBadItemDto : list) {
                        badItemDto.getList().addAll(qmsBadItemDto.getList());
                    }
                    map.put(routeProcess.getSectionId(), badItemDto);
                    sections.add(routeProcess.getSectionId());
                }
            }
            Set<Long> key = map.keySet();
            for (Long id : key) {
                sectionList.add(map.get(id));
            }
        }
        return sectionList;
    }

    @Override
    public Integer updateQuantity(Map<String, Object> map) {
        return qmsQualityConfirmationMapper.updateQuantity(map);
    }

    @Override
    public Integer parentUpdateQuantity(Long workOrderCardPoolId, Long processId) {
        BigDecimal quantity = null;
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setParentId(workOrderCardPoolId);
        List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
        if (StringUtils.isEmpty(workOrderCardPoolList)) {
            if (quantity == null) {
                quantity = new BigDecimal(0);
            }
            return quantity.intValue();
        }
        //部件流转卡
        SearchBasePlatePartsDet searchBasePlatePartsDet = new SearchBasePlatePartsDet();
        SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
        List dj = new ArrayList();
        for (SmtWorkOrderCardPoolDto workOrderCardPoolDto : workOrderCardPoolList) {
            //获取部件流程卡的部件明细数据
            searchBasePlatePartsDet.setPlatePartsDetId(workOrderCardPoolDto.getMaterialId());
            List<BasePlatePartsDetDto> platePartsDetList = baseFeignApi.findPlatePartsDetList(searchBasePlatePartsDet).getData();
            if (StringUtils.isEmpty(platePartsDetList)) {
                continue;
            }
            BasePlatePartsDetDto basePlatePartsDetDto = platePartsDetList.get(0);
            //获取部件流程卡的工序报工数据
            searchSmtProcessListProcess.setWorkOrderCardPoolId(workOrderCardPoolDto.getWorkOrderCardPoolId());
            searchSmtProcessListProcess.setProcessId(processId);
            searchSmtProcessListProcess.setProcessType((byte) 2);
            List<SmtProcessListProcessDto> processListProcessList = pmFeignApi.findSmtProcessListProcessList(searchSmtProcessListProcess).getData();
            if (StringUtils.isEmpty(processListProcessList)) {
                continue;
            }
            SmtProcessListProcessDto processListProcess = processListProcessList.get(0);
            if (dj.contains(workOrderCardPoolDto.getWorkOrderCardPoolId())) {
                continue;
            }
            for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : workOrderCardPoolList) {
                if (!workOrderCardPoolDto.getWorkOrderCardPoolId().equals(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId()) && workOrderCardPoolDto.getWorkOrderId().equals(smtWorkOrderCardPoolDto.getWorkOrderId())) {
                    searchSmtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                    processListProcessList = pmFeignApi.findSmtProcessListProcessList(searchSmtProcessListProcess).getData();
                    if (StringUtils.isEmpty(processListProcessList)) {
                        continue;
                    }
                    BigDecimal all = processListProcess.getOutputQuantity().add(processListProcessList.get(0).getOutputQuantity());
                    processListProcess.setOutputQuantity(all);
                    dj.add(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                }
            }

            BigDecimal divide = processListProcess.getOutputQuantity().divide(basePlatePartsDetDto.getUseQty(), 0);
            if (quantity == null) {
                quantity = divide;
                continue;
            }
            if (quantity.compareTo(divide) > -1) {
                quantity = divide;
            }
        }


        if (quantity == null) {
            quantity = new BigDecimal(0);
        } else {
            Map<String, Object> map = new HashMap();
            map.put("workOrderCardPoolId", workOrderCardPoolId);
            map.put("processId", processId);
            List<QmsQualityConfirmationDto> list = qmsQualityConfirmationMapper.findList(map);
            if (StringUtils.isNotEmpty(list)) {
                BigDecimal add = list.get(0).getQualifiedQuantity().add(list.get(0).getUnqualifiedQuantity());
                quantity = quantity.subtract(add);
            }
            if (quantity.compareTo(new BigDecimal(0)) < 1) {
                quantity = new BigDecimal(0);
            }
        }
        return quantity.intValue();
    }

    @Override
    public QmsQualityConfirmation getQualityQuantity(Long workOrderCardPoolId, Long processId) {
        Map<String, Object> map = new HashMap();
        map.put("workOrderCardPoolId", workOrderCardPoolId);
        map.put("processId", processId);
        if (processId == null || processId == 0) {
            SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(workOrderCardPoolId);
            List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
            if (StringUtils.isEmpty(workOrderCardPoolList)) {
                throw new BizErrorException("未找到流程单信息");
            }

            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(workOrderCardPoolList.get(0).getWorkOrderId());
            List<MesPmWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (StringUtils.isEmpty(workOrderList)) {
                throw new BizErrorException("未找到流程单的工单信息");
            }

            List<BaseRouteProcess> routeProcessList = baseFeignApi.findConfigureRout(workOrderList.get(0).getRouteId()).getData();
            if (StringUtils.isEmpty(routeProcessList)) {
                throw new BizErrorException("未找到工艺路线信息");
            }
            for (int i = routeProcessList.size() - 1; i >= 0; i--) {
                BaseProcess process = baseFeignApi.processDetail(routeProcessList.get(i).getProcessId()).getData();
                if (StringUtils.isNotEmpty(process) && process.getIsQuality() == 1) {
                    map.put("processId", process.getProcessId());
                    break;
                }
            }
        }
        return qmsQualityConfirmationMapper.getQualityQuantity(map);
    }
}
