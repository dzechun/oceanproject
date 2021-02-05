package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.*;
import com.fantechs.common.base.general.dto.qms.QmsBadItemDto;
import com.fantechs.common.base.general.dto.qms.QmsPoorQualityDto;
import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlatePartsDet;
import com.fantechs.common.base.general.entity.qms.QmsPoorQuality;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterialdDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
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
    private QmsBadItemMapper qmsBadItemMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BasicFeignApi basicFeignApi;
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
            if (StringUtils.isNotEmpty(workOrderCardPoolList) && (workOrderCardPoolList.get(0).getParentId() ==0 ||workOrderCardPoolList.get(0).getParentId() == null)){
                Integer quantity = this.parentUpdateQuantity(qmsQualityConfirmationDto.getWorkOrderCardPoolId(), qmsQualityConfirmationDto.getProcessId());
                qmsQualityConfirmationDto.setQuantity(new BigDecimal(quantity));
            }else {
                map.put("workOrderCardPoolId",qmsQualityConfirmationDto.getWorkOrderCardPoolId());
                map.put("processId",qmsQualityConfirmationDto.getProcessId());
                map.put("qualityType",qmsQualityConfirmationDto.getQualityType());
                qmsQualityConfirmationDto.setQuantity(new BigDecimal(this.updateQuantity(map)));
            }

            if (qmsQualityConfirmationDto.getRouteId() == null){
                continue;
            }
            ResponseEntity<List<SmtRouteProcess>> routeProcessResponse = basicFeignApi.findConfigureRout(qmsQualityConfirmationDto.getRouteId());
            List<SmtRouteProcess> routeProcesses = routeProcessResponse.getData();
            qmsQualityConfirmationDto.getSectionList().addAll(getBad(routeProcesses));
        }
        return list;
    }

    @Override
    public QmsQualityConfirmationDto analysis(String code,Byte type) {
        QmsQualityConfirmationDto qmsQualityConfirmationDto = new QmsQualityConfirmationDto();

        //获取工单任务池对象
        ProcessListWorkOrderDTO data = pmFeignApi.selectWorkOrderDtoByWorkOrderCardId(code).getData();
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setWorkOrderCardId(code);
        List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findSmtWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
        if (StringUtils.isEmpty(workOrderCardPoolList)){
            throw new BizErrorException("流程单号不正确");
        }
        //当前流程单的对象
        if (type ==1 && (workOrderCardPoolList.get(0).getParentId() == null || workOrderCardPoolList.get(0).getParentId() == 0)){
            throw new BizErrorException("当前为父级流程单");
        }else if (type ==2 && (workOrderCardPoolList.get(0).getParentId() == null || workOrderCardPoolList.get(0).getParentId() == 0)){
            qmsQualityConfirmationDto.setParentWorkOrderCardPoolId(workOrderCardPoolList.get(0).getWorkOrderCardPoolId());
        }

        //获取工艺路线
        ResponseEntity<List<SmtRouteProcess>> routeProcessResponse = basicFeignApi.findConfigureRout(data.getRouteId());
        List<SmtRouteProcess> routeProcesses = routeProcessResponse.getData();

        qmsQualityConfirmationDto.getSectionList().addAll(getBad(routeProcesses));

        qmsQualityConfirmationDto.setProductionQuantity(data.getWorkOrderQuantity());
        qmsQualityConfirmationDto.setWorkOrderCardPoolId(data.getWorkOrderCardPoolId());
        qmsQualityConfirmationDto.setWorkOrderCode(data.getWorkOrderCode());
        qmsQualityConfirmationDto.setMaterialDesc(data.getMaterialDesc());
        qmsQualityConfirmationDto.setMaterialCode(data.getMaterialCode());
        qmsQualityConfirmationDto.setProductModelName(data.getProductModuleName());
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
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if (qmsQualityConfirmation.getQualityType() == 1){

            SearchBasePlatePartsDet searchBasePlatePartsDet = new SearchBasePlatePartsDet();
            searchBasePlatePartsDet.setPlatePartsDetId(qmsQualityConfirmation.getPartsInformationId() == 0?qmsQualityConfirmation.getMaterialId():qmsQualityConfirmation.getPartsInformationId());
            List<BasePlatePartsDetDto> platePartsDetList = baseFeignApi.findPlatePartsDetList(searchBasePlatePartsDet).getData();
            if (StringUtils.isEmpty(platePartsDetList)){
                throw new BizErrorException("未找到部件信息");
            }
            BasePlatePartsDetDto basePlatePartsDet = platePartsDetList.get(0);

            List<SmtRouteProcess> routeProcesses = basicFeignApi.findConfigureRout(basePlatePartsDet.getRouteId()).getData();

            //获取当前报工工序信息
            ResponseEntity<SmtProcess> processResponse = basicFeignApi.processDetail(qmsQualityConfirmation.getProcessId());
            SmtProcess smtProcess = processResponse.getData();
            List<SmtRouteProcess> routeProcessList = new ArrayList<>();
            //筛选出当前报工工序的工段对应工艺路线里面的所有工序
            for (SmtRouteProcess smtRouteProcess : routeProcesses) {
                if (smtRouteProcess.getSectionId() == smtProcess.getSectionId()){
                    routeProcessList.add(smtRouteProcess);
                }
            }
            if (StringUtils.isEmpty(routeProcessList)){
                throw new BizErrorException("报工工序不属于当前工艺路线");
            }
            //获取当前报工工序所属工段的最后工序
            SmtRouteProcess smtRouteProcess = routeProcessList.get(routeProcessList.size() - 1);
            Byte isQuality = smtProcess.getIsQuality();
            if (qmsQualityConfirmation.getQualityType() == 1 && (isQuality == null || isQuality ==0)){
                throw new BizErrorException("当前工序不是品质确认工序");
            }
            if (smtRouteProcess.getProcessId() != qmsQualityConfirmation.getProcessId() && qmsQualityConfirmation.getQualityType()  == 1){
                throw new BizErrorException("当前工序不是最后一道工序");
            }
        }

        int i = 0;

        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(qmsQualityConfirmation.getWorkOrderCardPoolId());
        List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
        if (StringUtils.isEmpty(smtWorkOrderCardPoolList)){
            throw new BizErrorException("未找到流程单信息");
        }

        SmtWorkOrderCardPoolDto smtWorkOrderCardPool = smtWorkOrderCardPoolList.get(0);
        Byte type = smtWorkOrderCardPool.getType();
        List<QmsQualityConfirmationDto> qualityConfirmationDtos = null;
        Map map = new HashMap();
        map.put("workOrderCardPoolId",qmsQualityConfirmation.getWorkOrderCardPoolId());
        map.put("processId",qmsQualityConfirmation.getProcessId());
        //如果是成品抽检就打印条码
        if (StringUtils.isEmpty(smtWorkOrderCardPool.getParentId()) && (type == null || type != 3)){
            SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
            searchSmtWorkOrder.setWorkOrderId(smtWorkOrderCardPool.getWorkOrderId());
            List<SmtWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchSmtWorkOrder).getData();
            if(StringUtils.isNotEmpty(workOrderList)){
                //获取成品工艺路线
                List<SmtRouteProcess> routeProcesses = basicFeignApi.findConfigureRout(workOrderList.get(0).getRouteId()).getData();
                //判断成品抽检的工序是否是最后一道工序
                if (StringUtils.isNotEmpty(routeProcesses) && routeProcesses.get(routeProcesses.size()-1).getProcessId() == qmsQualityConfirmation.getProcessId()){
                    //打印成品条码
                }
            }
        }

        if (type != null && type == 3){
            if (qmsQualityConfirmation.getQualityType() == 1){
                qualityConfirmationDtos = qmsQualityConfirmationMapper.findList(map);
                qmsQualityConfirmation.setTotal(qmsQualityConfirmation.getQualifiedQuantity().add(qmsQualityConfirmation.getUnqualifiedQuantity()));
                qmsQualityConfirmation.setTotalQualified(qmsQualityConfirmation.getQualifiedQuantity());
                if (StringUtils.isNotEmpty(qualityConfirmationDtos)){
                    QmsQualityConfirmationDto qmsQualityConfirmationDto = qualityConfirmationDtos.get(0);
                    qmsQualityConfirmation.setTotal(qmsQualityConfirmation.getTotal().add(qmsQualityConfirmationDto.getTotal()==null?new BigDecimal(0):qmsQualityConfirmationDto.getTotal()));
                    qmsQualityConfirmation.setTotalQualified(qmsQualityConfirmation.getTotalQualified().add(qmsQualityConfirmationDto.getTotalQualified()==null?new BigDecimal(0):qmsQualityConfirmationDto.getTotalQualified()));
                }
            }

            map.put("workOrderCardPoolId",smtWorkOrderCardPool.getParentId());
            qualityConfirmationDtos = qmsQualityConfirmationMapper.findList(map);

            QmsQualityConfirmationDto qmsQualityConfirmationDto = qualityConfirmationDtos.get(0);
            qmsQualityConfirmationDto.setQualifiedQuantity(qmsQualityConfirmationDto.getQualifiedQuantity().add(qmsQualityConfirmation.getQualifiedQuantity()));
            qmsQualityConfirmationDto.setUnqualifiedQuantity(qmsQualityConfirmationDto.getUnqualifiedQuantity().add(qmsQualityConfirmation.getUnqualifiedQuantity()));
            qmsQualityConfirmationDto.setQualityType((byte) 1);

            qmsQualityConfirmationMapper.updateByPrimaryKey(qmsQualityConfirmationDto);
            qmsQualityConfirmation.setCreateTime(new Date());
            qmsQualityConfirmation.setCreateUserId(user.getUserId());
            qmsQualityConfirmation.setModifiedTime(new Date());
            qmsQualityConfirmation.setModifiedUserId(user.getUserId());
            qmsQualityConfirmation.setStatus(StringUtils.isEmpty(qmsQualityConfirmation.getStatus())?1:qmsQualityConfirmation.getStatus());
            qmsQualityConfirmation.setOrganizationId(user.getOrganizationId());
            qmsQualityConfirmation.setQualityConfirmationCode(CodeUtils.getId("PZQR"));
            i = qmsQualityConfirmationMapper.insertUseGeneratedKeys(qmsQualityConfirmation);
        } else if (qmsQualityConfirmation.getQualityConfirmationId() == null ||qmsQualityConfirmation.getQualityConfirmationId() == 0){
            if (qmsQualityConfirmation.getQualityType() == 1){
                qualityConfirmationDtos = qmsQualityConfirmationMapper.findList(map);
                qmsQualityConfirmation.setTotal(qmsQualityConfirmation.getQualifiedQuantity().add(qmsQualityConfirmation.getUnqualifiedQuantity()));
                qmsQualityConfirmation.setTotalQualified(qmsQualityConfirmation.getQualifiedQuantity());
                if (StringUtils.isNotEmpty(qualityConfirmationDtos)){
                    QmsQualityConfirmationDto qmsQualityConfirmationDto = qualityConfirmationDtos.get(0);
                    qmsQualityConfirmation.setTotal(qmsQualityConfirmation.getTotal().add(qmsQualityConfirmationDto.getTotal() ==null?new BigDecimal(0):qmsQualityConfirmationDto.getTotal()));
                    qmsQualityConfirmation.setTotalQualified(qmsQualityConfirmation.getTotalQualified().add(qmsQualityConfirmationDto.getTotalQualified()==null?new BigDecimal(0):qmsQualityConfirmationDto.getTotalQualified()));
                }
            }

            qmsQualityConfirmation.setCreateTime(new Date());
            qmsQualityConfirmation.setCreateUserId(user.getUserId());
            qmsQualityConfirmation.setModifiedTime(new Date());
            qmsQualityConfirmation.setModifiedUserId(user.getUserId());
            qmsQualityConfirmation.setStatus(StringUtils.isEmpty(qmsQualityConfirmation.getStatus())?1:qmsQualityConfirmation.getStatus());
            qmsQualityConfirmation.setOrganizationId(user.getOrganizationId());
            qmsQualityConfirmation.setQualityConfirmationCode(CodeUtils.getId("PZQR"));

            i = qmsQualityConfirmationMapper.insertUseGeneratedKeys(qmsQualityConfirmation);
        }else{
            qmsQualityConfirmation.setModifiedTime(new Date());
            qmsQualityConfirmation.setModifiedUserId(user.getUserId());

            Example example = new Example(QmsPoorQuality.class);
            example.createCriteria().andEqualTo("qualityId",qmsQualityConfirmation.getQualityConfirmationId());
            qmsPoorQualityMapper.deleteByExample(example);

            i = qmsQualityConfirmationMapper.updateByPrimaryKeySelective(qmsQualityConfirmation);
        }

        List<QmsPoorQualityDto> list = qmsQualityConfirmation.getSeledBadItemList();
        if (StringUtils.isNotEmpty(list)){
            List<QmsPoorQualityDto> qualityDtoList = new ArrayList<>();
            for (int j = 0 ; j < list.size() ; j++) {
                list.get(j).setQualityId(qmsQualityConfirmation.getQualityConfirmationId());
                boolean b = true;
                for (int k = j+1 ; k < list.size() ; k++) {
                    if (list.get(j).getSectionId() == list.get(k).getSectionId() && list.get(j).getBadItemDetId() == list.get(k).getBadItemDetId()){
                        list.get(j).setBadQuantity( list.get(j).getBadQuantity().add(list.get(k).getBadQuantity()));
                    }
                }
                if (b){
                    for (QmsPoorQualityDto qmsPoorQualityDto : qualityDtoList) {
                        if (list.get(j).getBadItemDetId() == qmsPoorQualityDto.getBadItemDetId() && list.get(j).getSectionId() == qmsPoorQualityDto.getSectionId()){
                            b=false;
                            break;
                        }
                    }
                }
                if (b)
                    qualityDtoList.add(list.get(j));
            }
            qmsPoorQualityMapper.insertList(qualityDtoList);
        }

        if (qmsQualityConfirmation.getQualityType() == 2 || !(qmsQualityConfirmation.getAffirmStatus() == 2)){
            return i;
        }

        SmtWorkOrderCardPoolDto workOrderCardPoolDto = null;
        searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(smtWorkOrderCardPool.getParentId());
        List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
        if (StringUtils.isEmpty(workOrderCardPoolList)){
            throw new BizErrorException("未找到回退流转卡父级信息");
        }
        workOrderCardPoolDto = workOrderCardPoolList.get(0);
        BigDecimal minMatchingQuantity = new BigDecimal(0);
        BigDecimal alreadyMatchingQuantity = new BigDecimal(0);
        if (type != null && type == 3){
            searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(workOrderCardPoolList.get(0).getParentId());
            List<SmtWorkOrderCardPoolDto> parentWorkOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
            if (StringUtils.isEmpty(parentWorkOrderCardPoolList)){
                throw new BizErrorException("未找到产品流转卡信息");
            }
            workOrderCardPoolDto = parentWorkOrderCardPoolList.get(0);
        }else if (qmsQualityConfirmation.getQualityType() != null && qmsQualityConfirmation.getQualityType() == 1) {

            if (workOrderCardPoolDto.getParentId() == null || workOrderCardPoolDto.getParentId() == 0 ){
                SmtProcess process = basicFeignApi.processDetail(qmsQualityConfirmation.getProcessId()).getData();
                if (StringUtils.isNotEmpty(process)){
                    MesPmMatchingDto matchingDto = pmFeignApi.findMinMatchingQuantity(workOrderCardPoolDto.getWorkOrderCardId(),process.getSectionId()).getData();
                    if (matchingDto != null){
                        minMatchingQuantity = matchingDto.getMinMatchingQuantity();
                        alreadyMatchingQuantity = matchingDto.getAlreadyMatchingQuantity();
                    }
                }

            }
        }

        if (minMatchingQuantity != null && minMatchingQuantity.compareTo(new BigDecimal(0)) == 1) {
            System.out.println("生成生产领料计划");
            SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
            searchSmtWorkOrder.setWorkOrderId(workOrderCardPoolDto.getWorkOrderId());
            List<SmtWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchSmtWorkOrder).getData();
            if (StringUtils.isEmpty(workOrderList)){
                throw new BizErrorException("未找到产品工单信息");
            }

            SearchSmtStorageMaterial searchSmtStorageMaterial = new SearchSmtStorageMaterial();
            searchSmtStorageMaterial.setMaterialId(workOrderList.get(0).getMaterialId());
            ResponseEntity<List<SmtStorageMaterial>> storageMaterialList = basicFeignApi.findStorageMaterialList(searchSmtStorageMaterial);
            List<SmtStorageMaterial> data = storageMaterialList.getData();
            if (StringUtils.isEmpty(data)){
                throw new BizErrorException("未找到该物料的储位");
            }

            SearchSmtWorkOrderBom searchSmtWorkOrderBom = new SearchSmtWorkOrderBom();
            searchSmtWorkOrderBom.setWorkOrderId(workOrderList.get(0).getWorkOrderId());
            List<SmtWorkOrderBomDto> workOrderBomList = pmFeignApi.findWordOrderBomList(searchSmtWorkOrderBom).getData();
            SmtWorkOrderBomDto workOrderBomDto = null;
            for (SmtWorkOrderBomDto smtWorkOrderBomDto : workOrderBomList) {
                if (smtWorkOrderBomDto.getProcessId() == qmsQualityConfirmation.getProcessId()){
                    workOrderBomDto = smtWorkOrderBomDto;
                    break;
                }
            }
            if (StringUtils.isEmpty(workOrderBomDto)){
                throw new BizErrorException("没有找到当前工序对应的工单BOM信息");
            }

            SearchMesPmMasterPlanListDTO searchMesPmMasterPlanListDTO = new SearchMesPmMasterPlanListDTO();
            searchMesPmMasterPlanListDTO.setWorkOrderId(workOrderList.get(0).getWorkOrderId());
            List<MesPmMasterPlanDTO> pmMasterPlanList = pmFeignApi.findPmMasterPlanlist(searchMesPmMasterPlanListDTO).getData();

            if (StringUtils.isEmpty(pmMasterPlanList)){
                throw new BizErrorException("未找到产品工单的月计划");
            }
            alreadyMatchingQuantity = alreadyMatchingQuantity == null ? new BigDecimal(0):alreadyMatchingQuantity;
            BigDecimal quantity = minMatchingQuantity.subtract(alreadyMatchingQuantity);
            if (quantity.compareTo(new BigDecimal(0)) < 1){
                return i;
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
            if (StringUtils.isEmpty(inFinishedProduct)){
                throw new BizErrorException("生成半成品入库单失败");
            }

            //生成领料计划
            WmsOutProductionMaterial wmsOutProductionMaterial = new WmsOutProductionMaterial();
            wmsOutProductionMaterial.setFinishedProductCode(inFinishedProduct.getFinishedProductCode());
            wmsOutProductionMaterial.setWorkOrderId(workOrderCardPoolDto.getWorkOrderId());
            wmsOutProductionMaterial.setMaterialId(workOrderBomDto.getPartMaterialId());
            wmsOutProductionMaterial.setPlanQty(quantity);
            wmsOutProductionMaterial.setRealityQty(new BigDecimal(0));
            wmsOutProductionMaterial.setOutTime(new Date());
            wmsOutProductionMaterial.setOutStatus(Byte.parseByte("0"));
            wmsOutProductionMaterial.setStorageId(data.get(0).getStorageId());
            wmsOutProductionMaterial.setProLineId(pmMasterPlanList.get(0).getProLineId());
            wmsOutProductionMaterial = outFeignApi.outProductionMaterialAdd(wmsOutProductionMaterial).getData();

            if (wmsOutProductionMaterial != null){
                searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
                searchSmtWorkOrderCardPool.setParentId(smtWorkOrderCardPool.getParentId());
                List<SmtWorkOrderCardPoolDto> workOrderCardPoolDtoList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
                if (StringUtils.isNotEmpty(workOrderCardPoolDtoList)){
                    for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : workOrderCardPoolDtoList) {
                        WmsOutProductionMaterialdDet wmsOutProductionMaterialdDet = new WmsOutProductionMaterialdDet();
                        wmsOutProductionMaterialdDet.setProductionMaterialId(wmsOutProductionMaterial.getProductionMaterialId());
                        wmsOutProductionMaterialdDet.setMaterialId(smtWorkOrderCardPoolDto.getMaterialId());
                        wmsOutProductionMaterialdDet.setWorkOrderId(smtWorkOrderCardPoolDto.getWorkOrderId());
                        wmsOutProductionMaterialdDet.setRealityQty(minMatchingQuantity);
                        outFeignApi.add(wmsOutProductionMaterialdDet);
                    }
                }
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsQualityConfirmation qmsQualityConfirmation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
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
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsQualityConfirmation qmsQualityConfirmation = qmsQualityConfirmationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsQualityConfirmation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }

        Example example = new Example(QmsPoorQuality.class);
        example.createCriteria().andEqualTo("qualityId", Arrays.asList(ids.split(",")));
        qmsPoorQualityMapper.deleteByExample(example);

        return qmsQualityConfirmationMapper.deleteByIds(ids);
    }

    public List<QmsBadItemDto> getBad(List<SmtRouteProcess> routeProcesses){
        Map<String,Object> search = new HashMap();
        List<Long> sections = new ArrayList<>();
        List<QmsBadItemDto> sectionList = new ArrayList<>();
        Map<Long,QmsBadItemDto> map = new HashMap<>();

        if (StringUtils.isNotEmpty(routeProcesses)){
            for (SmtRouteProcess routeProcess : routeProcesses) {
                int is = 0;
                for (Long section : sections) {
                    if (section.equals(routeProcess.getSectionId())){
                        is++;
                        break;
                    }
                }
                if (is == 0){
                    search.put("sectionId",routeProcess.getSectionId());
                    List<QmsBadItemDto> list = qmsBadItemMapper.findList(search);

                    QmsBadItemDto badItemDto = new QmsBadItemDto();
                    badItemDto.setSectionId(routeProcess.getSectionId());
                    badItemDto.setSectionName(routeProcess.getSectionName());
                    badItemDto.setProcessId(routeProcess.getProcessId());
                    badItemDto.setProcessName(routeProcess.getProcessName());
                    for (QmsBadItemDto qmsBadItemDto : list) {
                        badItemDto.getList().addAll(qmsBadItemDto.getList());
                    }
                    map.put(routeProcess.getSectionId(),badItemDto);
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
    public Integer parentUpdateQuantity(Long workOrderCardPoolId,Long processId) {
        BigDecimal quantity = null ;
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setParentId(workOrderCardPoolId);
        List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
        if (StringUtils.isEmpty(workOrderCardPoolList)){
            if (quantity == null){
                quantity = new BigDecimal(0);
            }
            return quantity.intValue();
        }
        //部件流转卡
        SearchBasePlatePartsDet searchBasePlatePartsDet = new SearchBasePlatePartsDet();
        SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
        for (SmtWorkOrderCardPoolDto workOrderCardPoolDto : workOrderCardPoolList) {
            //获取部件流程卡的部件明细数据
            searchBasePlatePartsDet.setPlatePartsDetId(workOrderCardPoolDto.getMaterialId());
            List<BasePlatePartsDetDto> platePartsDetList = baseFeignApi.findPlatePartsDetList(searchBasePlatePartsDet).getData();
            if (StringUtils.isEmpty(platePartsDetList)){
                continue;
            }
            BasePlatePartsDetDto basePlatePartsDetDto = platePartsDetList.get(0);
            //获取部件流程卡的工序报工数据
            searchSmtProcessListProcess.setWorkOrderCardPoolId(workOrderCardPoolDto.getWorkOrderCardPoolId());
            searchSmtProcessListProcess.setProcessId(processId);
            List<SmtProcessListProcessDto> processListProcessList = pmFeignApi.findSmtProcessListProcessList(searchSmtProcessListProcess).getData();
            if (StringUtils.isEmpty(processListProcessList)){
                continue;
            }
            SmtProcessListProcessDto processListProcess = processListProcessList.get(0);
            BigDecimal divide = processListProcess.getOutputQuantity().divide(basePlatePartsDetDto.getQuantity(),0);
            if (quantity == null){
                quantity = divide;
                continue;
            }
            if(quantity.compareTo(divide) > -1){
                quantity=divide;
            }
        }
        if (quantity == null){
            quantity = new BigDecimal(0);
        }else {
            Map<String,Object> map = new HashMap();
            map.put("workOrderCardPoolId",workOrderCardPoolId);
            map.put("processId",processId);
            List<QmsQualityConfirmationDto> list = qmsQualityConfirmationMapper.findList(map);
            if (StringUtils.isNotEmpty(list)){
                BigDecimal add = list.get(0).getQualifiedQuantity().add(list.get(0).getUnqualifiedQuantity());
                quantity = quantity.subtract(add);
            }
            if(quantity.compareTo(new BigDecimal(0)) < 1){
                quantity=new BigDecimal(0);
            }
        }
        return quantity.intValue();
    }

    @Override
    public QmsQualityConfirmation getQualityQuantity(Long workOrderCardPoolId,Long processId) {
        Map<String,Object> map = new HashMap();
        map.put("workOrderCardPoolId",workOrderCardPoolId);
        map.put("processId",processId);
        if (processId == null || processId == 0){
            SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setWorkOrderCardPoolId(workOrderCardPoolId);
            List<SmtWorkOrderCardPoolDto> workOrderCardPoolList = pmFeignApi.findWorkOrderCardPoolList(searchSmtWorkOrderCardPool).getData();
            if (StringUtils.isEmpty(workOrderCardPoolList)){
                throw new BizErrorException("未找到流程单信息");
            }

            SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
            searchSmtWorkOrder.setWorkOrderId(workOrderCardPoolList.get(0).getWorkOrderId());
            List<SmtWorkOrderDto> workOrderList = pmFeignApi.findWorkOrderList(searchSmtWorkOrder).getData();
            if (StringUtils.isEmpty(workOrderList)){
                throw new BizErrorException("未找到流程单的工单信息");
            }

            List<SmtRouteProcess> routeProcessList = basicFeignApi.findConfigureRout(workOrderList.get(0).getRouteId()).getData();
            if(StringUtils.isEmpty(routeProcessList)){
                throw new BizErrorException("未找到工艺路线信息");
            }
            for (int i =routeProcessList.size() -1 ; i >= 0 ; i--) {
                SmtProcess process = basicFeignApi.processDetail(routeProcessList.get(i).getProcessId()).getData();
                if(StringUtils.isNotEmpty(process) && process.getIsQuality() == 1){
                    map.put("processId",process.getProcessId());
                    break;
                }
            }
        }
        return qmsQualityConfirmationMapper.getQualityQuantity(map);
    }
}
