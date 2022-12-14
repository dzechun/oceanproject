package com.fantechs.provider.mes.sfc.util;

import com.fantechs.common.base.entity.security.*;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.general.dto.basic.BaseBadnessPhenotypeDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.general.entity.eam.search.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Huangshuijun
 * @create 2021/08/10
 */
@Component
public class DeviceInterFaceUtils {

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;

    /*
     * ????????????ID
     * */
    public ResponseEntity<List<BaseOrganizationDto>> getOrId() {
        ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=null;
        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationName("??????");
        baseOrganizationDtoList = baseFeignApi.findOrganizationList(searchBaseOrganization);
        return baseOrganizationDtoList;
    }
    /*
     * ??????????????????
     *
     * */
    public ResponseEntity<List<BaseProLine>> getProLine(String proLineCode, Long orgId){
        ResponseEntity<List<BaseProLine>> baseProLineList=null;
        SearchBaseProLine searchBaseProLine = new SearchBaseProLine();
        searchBaseProLine.setProCode(proLineCode);
        searchBaseProLine.setOrgId(orgId);
        baseProLineList = baseFeignApi.findList(searchBaseProLine);
        return baseProLineList;
    }
    /*
     * ??????????????????
     * */
    public ResponseEntity<List<BaseWorkshopSection>> getWorkshopSection(String sectionCode, Long orgId){
        ResponseEntity<List<BaseWorkshopSection>> baseWorkshopSectionList=null;
        SearchBaseWorkshopSection searchBaseWorkshopSection= new SearchBaseWorkshopSection();
        searchBaseWorkshopSection.setSectionCode(sectionCode);
        searchBaseWorkshopSection.setOrgId(orgId);
        baseWorkshopSectionList = baseFeignApi.findWorkshopSectionList(searchBaseWorkshopSection);
        return baseWorkshopSectionList;
    }
    /*
     * ??????????????????
     * */
    public ResponseEntity<List<BaseProcess>> getProcess(String ProcessCode, Long orgId){
        ResponseEntity<List<BaseProcess>> baseProcessList=null;
        SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
        searchBaseProcess.setProcessCode(ProcessCode);
        searchBaseProcess.setOrgId(orgId);
        searchBaseProcess.setCodeQueryMark(1);//??????????????????(??????1???????????????)
        baseProcessList = baseFeignApi.findProcessList(searchBaseProcess);
        return baseProcessList;
    }
    /*
     * ??????????????????
     * */
    public ResponseEntity<List<BaseStation>> getStation(String stationCode, Long orgId){
        ResponseEntity<List<BaseStation>> baseStationList=null;
        SearchBaseStation searchBaseStation = new SearchBaseStation();
        searchBaseStation.setStationCode(stationCode);
        searchBaseStation.setOrgId(orgId);
        baseStationList = baseFeignApi.findBaseStationList(searchBaseStation);
        return baseStationList;
    }
    /*
     * ??????????????????????????????
     * */
    public ResponseEntity<List<BaseRouteProcess>> getBaseRouteProcess(Long routeId){
        ResponseEntity<List<BaseRouteProcess>> baseRouteProcessList=null;
        baseRouteProcessList = baseFeignApi.findConfigureRout(routeId);
        return baseRouteProcessList;
    }
    /*
     * ??????????????????
     * */
    public ResponseEntity<List<SysUser>> getSysUser(String UserCode, Long orgId){
        ResponseEntity<List<SysUser>> sysUserList=null;
        SearchSysUser searchSysUser = new SearchSysUser();
        searchSysUser.setUserCode(UserCode);
        searchSysUser.setOrgId(orgId);
        sysUserList = securityFeignApi.selectUsers(searchSysUser);
        return sysUserList;
    }

    /*
     * ????????????
     * */
    public ResponseEntity checkLogin(String UserCode, String Password, Long orgId){
        return securityFeignApi.login(UserCode,Password,orgId,null,"");
    }

    /*
     * ??????????????????????????????
     * */
    public ResponseEntity<List<SysUserRole>> findUserRoleList(Long userId){
        return securityFeignApi.findUserRoleList(userId);
    }

    /*
     * ??????????????????
     * */
    public ResponseEntity<SysAuthRole> getSysAuthRole(Long roleId,long menuId){
        return securityFeignApi.getSysAuthRole(roleId,menuId);
    }

    /*
     * ????????????????????????
     * */
    public List<MesSfcWorkOrderBarcodeDto> getWorkOrderBarcode(String barcodeCode,Long orgId){
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList=null;
        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(barcodeCode);
        searchMesSfcWorkOrderBarcode.setOrgId(orgId);
        mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        return mesSfcWorkOrderBarcodeDtoList;
    }

    /*
    *??????????????????
    */
    public ResponseEntity<List<MesPmWorkOrderDto>> getWorkOrderList(SearchMesPmWorkOrder searchMesPmWorkOrder){
        ResponseEntity<List<MesPmWorkOrderDto>> mesPmWorkOrderDtoList=null;
        mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
        return mesPmWorkOrderDtoList;
    }
    /*
     *????????????BOM??????
     */
    public ResponseEntity<List<MesPmWorkOrderBomDto>> getWorkOrderBomList(SearchMesPmWorkOrderBom searchMesPmWorkOrderBom){
        ResponseEntity<List<MesPmWorkOrderBomDto>> mesPmWorkOrderBomDtoList=null;
        mesPmWorkOrderBomDtoList = pmFeignApi.findList(searchMesPmWorkOrderBom);
        return mesPmWorkOrderBomDtoList;
    }
    /*
     *????????????BOM??????
     */
    public ResponseEntity<List<BaseProductBomDto>> getProductBomList(SearchBaseProductBom searchBaseProductBom){
        ResponseEntity<List<BaseProductBomDto>> baseProductBomDtoList=null;
        baseProductBomDtoList = baseFeignApi.findProductBomList(searchBaseProductBom);
        return baseProductBomDtoList;
    }

    /*
     * ???????????????????????????
     * */
    public ResponseEntity<List<SysSpecItem>> getSysSpecItem(String specCode){
        ResponseEntity<List<SysSpecItem>> sysSpecItemList=null;
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode(specCode);
        sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        return sysSpecItemList;
    }
    /*
     * ??????????????????????????????
     * */
    public ResponseEntity<List<MesPmProductionKeyIssuesOrder>> getPmPKIOList(SearchMesPmProductionKeyIssuesOrder searchMesPmProductionKeyIssuesOrder){
        ResponseEntity<List<MesPmProductionKeyIssuesOrder>> mesPmProductionKeyIssuesOrderList=null;
        mesPmProductionKeyIssuesOrderList = pmFeignApi.findPmPKIOList(searchMesPmProductionKeyIssuesOrder);
        return mesPmProductionKeyIssuesOrderList;
    }
    /*
     * ????????????????????????
     * */
    public ResponseEntity<List<BaseBadnessPhenotypeDto>> getBadnessPhenotype(String badnessPhenotypeCode, Long orgId){
        ResponseEntity<List<BaseBadnessPhenotypeDto>> baseBadnessPhenotypeDtoList=null;
        SearchBaseBadnessPhenotype searchBaseBadnessPhenotype = new SearchBaseBadnessPhenotype();
        searchBaseBadnessPhenotype.setBadnessPhenotypeCode(badnessPhenotypeCode);
        searchBaseBadnessPhenotype.setOrgId(orgId);
        baseBadnessPhenotypeDtoList = baseFeignApi.findBadnessPhenotypeDtoList(searchBaseBadnessPhenotype);
        return baseBadnessPhenotypeDtoList;
    }

    /*
     * ???????????????????????????ID
     * */
    public ResponseEntity<String> findPurchaseMaterial(String purchaseOrderCode){
         return omFeignApi.findPurchaseMaterial(purchaseOrderCode);
    }
    /*
     * ??????BOM????????????
     * */
    public ResponseEntity<BaseProductBomDto> findNextLevelProductBomDet(SearchBaseProductBom searchBaseProductBom){
        ResponseEntity<BaseProductBomDto> productBomDto=null;
        productBomDto = baseFeignApi.findNextLevelProductBomDet(searchBaseProductBom);
        return productBomDto;
    }

    public void  addLog(Byte result, Byte type, Long orgId, String responseData, String requestParameter, BigDecimal consumeTime, String requestTimeS, String responseTimeS) {
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("????????????????????????");
        sysApiLog.setCallResult(result);
        sysApiLog.setCallType(type);
        sysApiLog.setApiModule("ocean-materialapi");
        sysApiLog.setOrgId(orgId);

        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());

//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//            sysApiLog.setRequestTime(sdf.parse(requestTimeS));
//            sysApiLog.setResponseTime(sdf.parse(responseTimeS));
//
//            if (StringUtils.isNotEmpty(requestTimeS))
//                sysApiLog.setRequestTime(sdf.parse(requestTimeS));
//            else
//                sysApiLog.setRequestTime(new Date());
//
//            if (StringUtils.isNotEmpty(responseTimeS))
//                sysApiLog.setResponseTime(sdf.parse(responseTimeS));
//            else
//                sysApiLog.setResponseTime(new Date());
//        }
//        catch (Exception ex){
//
//        }

        sysApiLog.setConsumeTime(consumeTime);
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);

        securityFeignApi.add(sysApiLog);
    }

}
