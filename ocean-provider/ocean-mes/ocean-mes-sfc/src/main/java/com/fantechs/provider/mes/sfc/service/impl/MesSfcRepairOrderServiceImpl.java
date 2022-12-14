package com.fantechs.provider.mes.sfc.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDetDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcKeyPartRelevance;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;
import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFile;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrderBadPhenotype;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrderBadPhenotypeRepair;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrderSemiProduct;
import com.fantechs.common.base.general.entity.mes.sfc.history.MesSfcHtRepairOrder;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.mes.sfc.mapper.*;
import com.fantechs.provider.mes.sfc.service.MesSfcRepairOrderService;
import com.fantechs.provider.mes.sfc.service.MesSfcScanBarcodeService;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import com.fantechs.provider.mes.sfc.util.DeviceInterFaceUtils;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/09/10.
 */
@Service
public class MesSfcRepairOrderServiceImpl extends BaseService<MesSfcRepairOrder> implements MesSfcRepairOrderService {

    @Resource
    private MesSfcRepairOrderMapper mesSfcRepairOrderMapper;
    @Resource
    private MesSfcHtRepairOrderMapper mesSfcHtRepairOrderMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private MesSfcRepairOrderBadPhenotypeMapper mesSfcRepairOrderBadPhenotypeMapper;
    @Resource
    private MesSfcRepairOrderBadPhenotypeRepairMapper mesSfcRepairOrderBadPhenotypeRepairMapper;
    @Resource
    private MesSfcRepairOrderSemiProductMapper mesSfcRepairOrderSemiProductMapper;
    @Resource
    private MesSfcKeyPartRelevanceMapper mesSfcKeyPartRelevanceMapper;
    @Resource
    private RabbitProducer rabbitProducer;
    @Resource
    private BarcodeUtils barcodeUtils;
    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;
    @Resource
    private MesSfcScanBarcodeService mesSfcScanBarcodeService;

    @Override
    public List<MesSfcRepairOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        List<MesSfcRepairOrderDto> list = mesSfcRepairOrderMapper.findList(map);

        SearchBaseFile searchBaseFile = new SearchBaseFile();
        searchBaseFile.setRelevanceTableName("mes_sfc_repair_order");
        for(MesSfcRepairOrderDto mesSfcRepairOrderDto : list){
            searchBaseFile.setRelevanceId(mesSfcRepairOrderDto.getRepairOrderId());
            List<BaseFile> fileList = baseFeignApi.findList(searchBaseFile).getData();
            mesSfcRepairOrderDto.setBaseFileList(fileList);
        }

        return list;
    }

    @Override
    public List<MesSfcHtRepairOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return mesSfcHtRepairOrderMapper.findHtList(map);
    }

    @Override
    public List<BaseProductBomDetDto> findSemiProductBom(String semiProductBarcode) {
        //??????????????????
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("WorkOrderPositionOnBarcode");
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
        if(semiProductBarcode.length() < endIndex){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"????????????????????????");
        }
        String partPurchaseOrderCode = semiProductBarcode.substring(beginIndex, endIndex);
        StringBuilder stringBuilder = new StringBuilder(partPurchaseOrderCode);
        stringBuilder.insert(0,"4");
        stringBuilder.insert(2,"000");
        String purchaseOrderCode = stringBuilder.toString();

        SearchOmPurchaseOrder searchOmPurchaseOrder = new SearchOmPurchaseOrder();
        searchOmPurchaseOrder.setPurchaseOrderCode(purchaseOrderCode);
        List<OmPurchaseOrderDto> purchaseOrderDtos = omFeignApi.findList(searchOmPurchaseOrder).getData();
        if(StringUtils.isEmpty(purchaseOrderDtos)){
            throw new BizErrorException("??????????????????");
        }
        OmPurchaseOrderDto omPurchaseOrderDto = purchaseOrderDtos.get(0);

        SearchOmPurchaseOrderDet searchOmPurchaseOrderDet = new SearchOmPurchaseOrderDet();
        searchOmPurchaseOrderDet.setPurchaseOrderId(omPurchaseOrderDto.getPurchaseOrderId());
        List<OmPurchaseOrderDetDto> purchaseOrderDetDtos = omFeignApi.findList(searchOmPurchaseOrderDet).getData();
        if(StringUtils.isEmpty(purchaseOrderDetDtos)){
            throw new BizErrorException("????????????????????????");
        }
        Long partMaterialId = purchaseOrderDetDtos.get(0).getMaterialId();

        SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
        searchBaseProductBom.setMaterialId(partMaterialId);
        List<BaseProductBomDto> productBomDtos = baseFeignApi.findProductBomList(searchBaseProductBom).getData();
        if(StringUtils.isEmpty(productBomDtos)){
            throw new BizErrorException("?????????????????????");
        }
        BaseProductBomDto baseProductBomDto = productBomDtos.get(0);

        SearchBaseProductBom searchBaseProductBom1 = new SearchBaseProductBom();
        searchBaseProductBom1.setProductBomId(baseProductBomDto.getProductBomId());
        BaseProductBomDto productBomDto = baseFeignApi.findNextLevelProductBomDet(searchBaseProductBom1).getData();
        if(StringUtils.isEmpty(productBomDto)){
            throw new BizErrorException("?????????????????????");
        }

        List<BaseProductBomDetDto> baseProductBomDetDtos = productBomDto.getBaseProductBomDetDtos();
        if(StringUtils.isEmpty(baseProductBomDetDtos)){
            throw new BizErrorException("???????????????Bom??????");
        }

        return baseProductBomDetDtos;
    }

    @Override
    public MesSfcRepairOrderDto getWorkOrder(String SNCode,String workOrderCode,Integer SNCodeType){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        MesSfcRepairOrderDto mesSfcRepairOrderDto = new MesSfcRepairOrderDto();
        String purchaseOrderCode = "";

        if(StringUtils.isNotEmpty(SNCode)) {
            if(SNCodeType == 1) {
                //???????????????
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
                if(SNCode.length() < endIndex){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"?????????????????????");
                }
                workOrderCode = SNCode.substring(beginIndex, endIndex);
            }else if(SNCodeType == 2) {
                //??????????????????
                SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                searchSysSpecItem.setSpecCode("WorkOrderPositionOnBarcode");
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
                if(SNCode.length() < endIndex){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"????????????????????????");
                }
                String partPurchaseOrderCode = SNCode.substring(beginIndex, endIndex);
                StringBuilder stringBuilder = new StringBuilder(partPurchaseOrderCode);
                stringBuilder.insert(0,"4");
                stringBuilder.insert(2,"000");
                purchaseOrderCode = stringBuilder.toString();
            }
        }

        //??????????????????
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
        searchMesPmWorkOrder.setCodeQueryMark(1);
        List<MesPmWorkOrderDto> pmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if (StringUtils.isEmpty(pmWorkOrderDtos)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"???????????????");
        }
        MesPmWorkOrderDto mesPmWorkOrderDto = pmWorkOrderDtos.get(0);

        //???????????????????????????
        List<MesSfcRepairOrderSemiProduct> mesSfcRepairOrderSemiProducts = new ArrayList<>();
        if(SNCodeType == 1) {
            SearchMesSfcKeyPartRelevance searchMesSfcKeyPartRelevance = new SearchMesSfcKeyPartRelevance();
            searchMesSfcKeyPartRelevance.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
            searchMesSfcKeyPartRelevance.setBarcodeCode(SNCode);
            List<MesSfcKeyPartRelevanceDto> keyPartRelevanceList = mesSfcKeyPartRelevanceMapper.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcKeyPartRelevance));
            if (StringUtils.isNotEmpty(keyPartRelevanceList)) {
                for (MesSfcKeyPartRelevanceDto mesSfcKeyPartRelevanceDto : keyPartRelevanceList) {
                    MesSfcRepairOrderSemiProduct mesSfcRepairOrderSemiProduct = new MesSfcRepairOrderSemiProduct();
                    mesSfcRepairOrderSemiProduct.setBarcode(mesSfcKeyPartRelevanceDto.getPartBarcode());
                    mesSfcRepairOrderSemiProduct.setMaterialId(mesSfcKeyPartRelevanceDto.getMaterialId());
                    mesSfcRepairOrderSemiProduct.setMaterialCode(mesSfcKeyPartRelevanceDto.getMaterialCode());
                    mesSfcRepairOrderSemiProducts.add(mesSfcRepairOrderSemiProduct);
                }
            }
        }else if(SNCodeType == 2 && StringUtils.isNotEmpty(SNCode)){
            SearchOmPurchaseOrder searchOmPurchaseOrder = new SearchOmPurchaseOrder();
            searchOmPurchaseOrder.setPurchaseOrderCode(purchaseOrderCode);
            List<OmPurchaseOrderDto> purchaseOrderDtos = omFeignApi.findList(searchOmPurchaseOrder).getData();
            if(StringUtils.isEmpty(purchaseOrderDtos)){
                throw new BizErrorException("??????????????????");
            }
            OmPurchaseOrderDto omPurchaseOrderDto = purchaseOrderDtos.get(0);

            SearchOmPurchaseOrderDet searchOmPurchaseOrderDet = new SearchOmPurchaseOrderDet();
            searchOmPurchaseOrderDet.setPurchaseOrderId(omPurchaseOrderDto.getPurchaseOrderId());
            List<OmPurchaseOrderDetDto> purchaseOrderDetDtos = omFeignApi.findList(searchOmPurchaseOrderDet).getData();
            if(StringUtils.isEmpty(purchaseOrderDetDtos)){
                throw new BizErrorException("????????????????????????");
            }
            Long partMaterialId = purchaseOrderDetDtos.get(0).getMaterialId();
            if(StringUtils.isEmpty(partMaterialId)){
                throw new BizErrorException("????????????????????????????????????ID-->"+purchaseOrderCode);
            }
            Boolean isExist = barcodeUtils.findBomExistMaterialId(mesPmWorkOrderDto.getMaterialId(),partMaterialId,user.getOrganizationId());

            if(isExist==false) {
                SearchMesPmWorkOrderBom searchMesPmWorkOrderBom = new SearchMesPmWorkOrderBom();
                searchMesPmWorkOrderBom.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                searchMesPmWorkOrderBom.setPartMaterialId(partMaterialId);
                searchMesPmWorkOrderBom.setProcessId(mesPmWorkOrderDto.getPutIntoProcessId());
                ResponseEntity<List<MesPmWorkOrderBomDto>> responseEntityBom = deviceInterFaceUtils.getWorkOrderBomList(searchMesPmWorkOrderBom);
                if (StringUtils.isEmpty(responseEntityBom.getData())) {
                    throw new BizErrorException("??????????????????????????????????????????");
                }
            }
        }

        //??????????????????
        SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
        searchBaseProcess.setProcessCategoryCode("repair");
        List<BaseProcess> baseProcesses = baseFeignApi.findProcessList(searchBaseProcess).getData();
        if(StringUtils.isEmpty(baseProcesses)){
            throw new BizErrorException("?????????????????????");
        }

        //????????????
        mesSfcRepairOrderDto.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
        mesSfcRepairOrderDto.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
        if(SNCodeType == 1){
            mesSfcRepairOrderDto.setBarcode(SNCode);
        }else if(SNCodeType == 2){
            mesSfcRepairOrderDto.setSemiProductBarcode(SNCode);
        }
        mesSfcRepairOrderDto.setCurrentProcessId(baseProcesses.get(0).getProcessId());
        mesSfcRepairOrderDto.setCurrentProcessName(baseProcesses.get(0).getProcessName());
        mesSfcRepairOrderDto.setMaterialId(mesPmWorkOrderDto.getMaterialId());
        mesSfcRepairOrderDto.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
        mesSfcRepairOrderDto.setMaterialDesc(mesPmWorkOrderDto.getMaterialDesc());
        mesSfcRepairOrderDto.setProLineId(mesPmWorkOrderDto.getProLineId());
        mesSfcRepairOrderDto.setProName(mesPmWorkOrderDto.getProName());
        mesSfcRepairOrderDto.setRouteId(mesPmWorkOrderDto.getRouteId());
        mesSfcRepairOrderDto.setRouteName(mesPmWorkOrderDto.getRouteName());
        mesSfcRepairOrderDto.setMesSfcRepairOrderSemiProductList(mesSfcRepairOrderSemiProducts);

        return mesSfcRepairOrderDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(MesSfcRepairOrderPrintParam mesSfcRepairOrderPrintParam) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("repairOrderId",mesSfcRepairOrderPrintParam.getRepairOrderId());
        List<MesSfcRepairOrderDto> list = mesSfcRepairOrderMapper.findList(map);
        MesSfcRepairOrderDto mesSfcRepairOrderDto = list.get(0);

        //??????
        SearchBaseStation searchBaseStation = new SearchBaseStation();
        searchBaseStation.setProcessId(mesSfcRepairOrderDto.getBadProcessId());
        List<BaseStation> baseStations = baseFeignApi.findBaseStationList(searchBaseStation).getData();
        BaseStation baseStation = new BaseStation();
        if(StringUtils.isNotEmpty(baseStations)) {
            baseStation = baseStations.get(0);
        }

        //????????????
        List<MesSfcRepairOrderBadPhenotype> mesSfcRepairOrderBadPhenotypeList = mesSfcRepairOrderDto.getMesSfcRepairOrderBadPhenotypeList();
        if(StringUtils.isEmpty(mesSfcRepairOrderBadPhenotypeList)){
            throw new BizErrorException("??????????????????????????????????????????");
        }
        String badnessPhenotypeCode = mesSfcRepairOrderBadPhenotypeList.get(0).getBadnessPhenotypeCode();
        String badnessPhenotypeDesc = mesSfcRepairOrderBadPhenotypeList.get(0).getBadnessPhenotypeDesc();

        //?????????????????????
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("repairOrderPrintName");
        List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        String printName = sysSpecItemList.get(0).getParaValue();


        PrintModel printModel = new PrintModel();
        List<PrintModel> printModelList = new ArrayList<>();
        printModel.setId(mesSfcRepairOrderPrintParam.getRepairOrderId());
        printModel.setSize(StringUtils.isEmpty(mesSfcRepairOrderPrintParam.getSize())?1:mesSfcRepairOrderPrintParam.getSize());
        printModel.setOption1(StringUtils.isEmpty(badnessPhenotypeCode)?"":badnessPhenotypeCode);
        printModel.setOption2(StringUtils.isEmpty(badnessPhenotypeDesc)?"":badnessPhenotypeDesc);
        printModel.setOption3(StringUtils.isEmpty(baseStation.getStationCode())?"":baseStation.getStationCode());
        printModelList.add(printModel);
        PrintDto printDto = new PrintDto();
        printDto.setPrintName(printName);
        printDto.setLabelName("????????????.prn");
        printDto.setLabelVersion("0.0.1");
        printDto.setPrintModelList(printModelList);

        rabbitProducer.sendPrint(printDto);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public MesSfcRepairOrder add(MesSfcRepairOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //ifCodeRepeat(record,user);

        record.setRepairOrderCode(CodeUtils.getId("WX-"));
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrderStatus(StringUtils.isEmpty(record.getOrderStatus())?1: record.getOrderStatus());
        record.setOrgId(user.getOrganizationId());
        mesSfcRepairOrderMapper.insertUseGeneratedKeys(record);

        MesSfcHtRepairOrder mesSfcHtRepairOrder = new MesSfcHtRepairOrder();
        BeanUtils.copyProperties(record, mesSfcHtRepairOrder);
        int i = mesSfcHtRepairOrderMapper.insertSelective(mesSfcHtRepairOrder);

        //????????????
        List<BaseFile> baseFileList = record.getBaseFileList();
        if(StringUtils.isNotEmpty(baseFileList)){
            for (BaseFile baseFile : baseFileList){
                baseFile.setRelevanceTableName("mes_sfc_repair_order");
                baseFile.setRelevanceId(record.getRepairOrderId());
            }
            baseFeignApi.batchAddFile(baseFileList);
        }

        //????????????
        List<MesSfcRepairOrderBadPhenotype> mesSfcRepairOrderBadPhenotypeList = record.getMesSfcRepairOrderBadPhenotypeList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderBadPhenotypeList)){
            for (MesSfcRepairOrderBadPhenotype mesSfcRepairOrderBadPhenotype : mesSfcRepairOrderBadPhenotypeList){
                mesSfcRepairOrderBadPhenotype.setRepairOrderId(record.getRepairOrderId());
                mesSfcRepairOrderBadPhenotype.setCreateUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotype.setCreateTime(new Date());
                mesSfcRepairOrderBadPhenotype.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotype.setModifiedTime(new Date());
                mesSfcRepairOrderBadPhenotype.setStatus(StringUtils.isEmpty(mesSfcRepairOrderBadPhenotype.getStatus())?1: mesSfcRepairOrderBadPhenotype.getStatus());
                mesSfcRepairOrderBadPhenotype.setOrgId(user.getOrganizationId());
            }
            mesSfcRepairOrderBadPhenotypeMapper.insertList(mesSfcRepairOrderBadPhenotypeList);
        }

        //??????????????????
        List<MesSfcRepairOrderBadPhenotypeRepair> mesSfcRepairOrderBadPhenotypeRepairList = record.getMesSfcRepairOrderBadPhenotypeRepairList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderBadPhenotypeRepairList)) {
            for (MesSfcRepairOrderBadPhenotypeRepair mesSfcRepairOrderBadPhenotypeRepair : mesSfcRepairOrderBadPhenotypeRepairList) {
                mesSfcRepairOrderBadPhenotypeRepair.setRepairOrderId(record.getRepairOrderId());
                mesSfcRepairOrderBadPhenotypeRepair.setCreateUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotypeRepair.setCreateTime(new Date());
                mesSfcRepairOrderBadPhenotypeRepair.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotypeRepair.setModifiedTime(new Date());
                mesSfcRepairOrderBadPhenotypeRepair.setStatus(StringUtils.isEmpty(mesSfcRepairOrderBadPhenotypeRepair.getStatus()) ? 1 : mesSfcRepairOrderBadPhenotypeRepair.getStatus());
                mesSfcRepairOrderBadPhenotypeRepair.setOrgId(user.getOrganizationId());
            }
            mesSfcRepairOrderBadPhenotypeRepairMapper.insertList(mesSfcRepairOrderBadPhenotypeRepairList);
        }

        //?????????
        List<MesSfcRepairOrderSemiProduct> mesSfcRepairOrderSemiProductList = record.getMesSfcRepairOrderSemiProductList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderSemiProductList)){
            for (MesSfcRepairOrderSemiProduct mesSfcRepairOrderSemiProduct : mesSfcRepairOrderSemiProductList){
                mesSfcRepairOrderSemiProduct.setRepairOrderId(record.getRepairOrderId());
                mesSfcRepairOrderSemiProduct.setCreateUserId(user.getUserId());
                mesSfcRepairOrderSemiProduct.setCreateTime(new Date());
                mesSfcRepairOrderSemiProduct.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderSemiProduct.setModifiedTime(new Date());
                mesSfcRepairOrderSemiProduct.setStatus(StringUtils.isEmpty(mesSfcRepairOrderSemiProduct.getStatus())?1: mesSfcRepairOrderSemiProduct.getStatus());
                mesSfcRepairOrderSemiProduct.setOrgId(user.getOrganizationId());
            }
            mesSfcRepairOrderSemiProductMapper.insertList(mesSfcRepairOrderSemiProductList);
        }

        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(MesSfcRepairOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //ifCodeRepeat(entity,user);

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = mesSfcRepairOrderMapper.updateByPrimaryKeySelective(entity);

        //???????????????
        MesSfcHtRepairOrder mesSfcHtRepairOrder = new MesSfcHtRepairOrder();
        BeanUtils.copyProperties(entity, mesSfcHtRepairOrder);
        mesSfcHtRepairOrderMapper.insertSelective(mesSfcHtRepairOrder);

        //????????????
        List<BaseFile> baseFileList = entity.getBaseFileList();
        if(StringUtils.isNotEmpty(baseFileList)){
            for (BaseFile baseFile : baseFileList){
                baseFile.setRelevanceTableName("mes_sfc_repair_order");
                baseFile.setRelevanceId(entity.getRepairOrderId());
            }
            baseFeignApi.batchAddFile(baseFileList);
        }

        //?????????????????????
        Example example1 = new Example(MesSfcRepairOrderBadPhenotype.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("repairOrderId", entity.getRepairOrderId());
        mesSfcRepairOrderBadPhenotypeMapper.deleteByExample(example1);

        //???????????????????????????
        Example example2 = new Example(MesSfcRepairOrderBadPhenotypeRepair.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("repairOrderId",entity.getRepairOrderId());
        mesSfcRepairOrderBadPhenotypeRepairMapper.deleteByExample(example2);

        //????????????
        List<MesSfcRepairOrderBadPhenotype> mesSfcRepairOrderBadPhenotypeList = entity.getMesSfcRepairOrderBadPhenotypeList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderBadPhenotypeList)){
            for (MesSfcRepairOrderBadPhenotype mesSfcRepairOrderBadPhenotype : mesSfcRepairOrderBadPhenotypeList){
                mesSfcRepairOrderBadPhenotype.setRepairOrderId(entity.getRepairOrderId());
                mesSfcRepairOrderBadPhenotype.setCreateUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotype.setCreateTime(new Date());
                mesSfcRepairOrderBadPhenotype.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotype.setModifiedTime(new Date());
                mesSfcRepairOrderBadPhenotype.setStatus(StringUtils.isEmpty(mesSfcRepairOrderBadPhenotype.getStatus())?1: mesSfcRepairOrderBadPhenotype.getStatus());
                mesSfcRepairOrderBadPhenotype.setOrgId(user.getOrganizationId());
            }
            mesSfcRepairOrderBadPhenotypeMapper.insertList(mesSfcRepairOrderBadPhenotypeList);
        }

        //??????????????????
        List<MesSfcRepairOrderBadPhenotypeRepair> mesSfcRepairOrderBadPhenotypeRepairList = entity.getMesSfcRepairOrderBadPhenotypeRepairList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderBadPhenotypeRepairList)) {
            for (MesSfcRepairOrderBadPhenotypeRepair mesSfcRepairOrderBadPhenotypeRepair : mesSfcRepairOrderBadPhenotypeRepairList) {
                mesSfcRepairOrderBadPhenotypeRepair.setRepairOrderId(entity.getRepairOrderId());
                mesSfcRepairOrderBadPhenotypeRepair.setCreateUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotypeRepair.setCreateTime(new Date());
                mesSfcRepairOrderBadPhenotypeRepair.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotypeRepair.setModifiedTime(new Date());
                mesSfcRepairOrderBadPhenotypeRepair.setStatus(StringUtils.isEmpty(mesSfcRepairOrderBadPhenotypeRepair.getStatus()) ? 1 : mesSfcRepairOrderBadPhenotypeRepair.getStatus());
                mesSfcRepairOrderBadPhenotypeRepair.setOrgId(user.getOrganizationId());
            }
            mesSfcRepairOrderBadPhenotypeRepairMapper.insertList(mesSfcRepairOrderBadPhenotypeRepairList);
        }

        //?????????????????? mesSfcScanBarcodeService
        if(entity.getOrderStatus()==(byte)2) {
            RestapiSNDataTransferApiDto para = new RestapiSNDataTransferApiDto();
            para.setBarCode(entity.getBarcode());//????????????
            para.setPartBarcode(entity.getSemiProductBarcode());//???????????????

            Long proLineId = entity.getProLineId();
            ResponseEntity<BaseProLine> entityLine = baseFeignApi.getProLineDetail(proLineId);
            if (StringUtils.isNotEmpty(entityLine.getData())) {
                para.setProCode(entityLine.getData().getProCode());//????????????
            }

            Long currentProcessId = entity.getCurrentProcessId();//????????????ID
            ResponseEntity<BaseProcess> entityProcess = baseFeignApi.processDetail(currentProcessId);
            if (StringUtils.isNotEmpty(entityProcess.getData())) {
                para.setProcessCode(entityProcess.getData().getProcessCode());//????????????
            }

            Long workOrderId = entity.getWorkOrderId();
            ResponseEntity<MesPmWorkOrder> entityWorkOrder = pmFeignApi.workOrderDetail(workOrderId);
            if (StringUtils.isNotEmpty(entityWorkOrder.getData())) {
                para.setWorkOrderCode(entityWorkOrder.getData().getWorkOrderCode());//?????????
            }

            para.setOpResult("OK");

            barcodeUtils.RepairDataTransfer(para);
        }

        return i;
    }


    public void ifCodeRepeat(MesSfcRepairOrder mesSfcRepairOrder, SysUser user){
        Example example = new Example(MesSfcRepairOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("repairOrderCode",mesSfcRepairOrder.getRepairOrderCode())
                .andEqualTo("orgId",user.getOrganizationId());
        if(StringUtils.isNotEmpty(mesSfcRepairOrder.getRepairOrderId())){
            criteria.andNotEqualTo("repairOrderId",mesSfcRepairOrder.getRepairOrderId());
        }
        MesSfcRepairOrder repairOrder = mesSfcRepairOrderMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(repairOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<MesSfcHtRepairOrder> htList = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            MesSfcRepairOrder mesSfcRepairOrder = mesSfcRepairOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(mesSfcRepairOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            MesSfcHtRepairOrder mesSfcHtRepairOrder = new MesSfcHtRepairOrder();
            BeanUtils.copyProperties(mesSfcRepairOrder, mesSfcHtRepairOrder);
            htList.add(mesSfcHtRepairOrder);

            //??????????????????
            Example example1 = new Example(MesSfcRepairOrderBadPhenotype.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("repairOrderId", id);
            mesSfcRepairOrderBadPhenotypeMapper.deleteByExample(example1);

            //????????????????????????
            Example example2 = new Example(MesSfcRepairOrderBadPhenotypeRepair.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("repairOrderId",id);
            mesSfcRepairOrderBadPhenotypeRepairMapper.deleteByExample(example2);

            //???????????????
            Example example3 = new Example(MesSfcRepairOrderSemiProduct.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("repairOrderId",id);
            mesSfcRepairOrderSemiProductMapper.deleteByExample(example3);
        }

        mesSfcHtRepairOrderMapper.insertList(htList);

        return mesSfcRepairOrderMapper.deleteByIds(ids);
    }
}
