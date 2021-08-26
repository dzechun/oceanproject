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
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private EamFeignApi eamFeignApi;
    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;

    /*
     * 获取组织ID
     * */
    public ResponseEntity<List<BaseOrganizationDto>> getOrId() {
        ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=null;
        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationName("雷赛");
        baseOrganizationDtoList = baseFeignApi.findOrganizationList(searchBaseOrganization);
        return baseOrganizationDtoList;
    }
    /*
     * 获取产线信息
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
     * 获取工段信息
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
     * 获取工序信息
     * */
    public ResponseEntity<List<BaseProcess>> getProcess(String ProcessCode, Long orgId){
        ResponseEntity<List<BaseProcess>> baseProcessList=null;
        SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
        searchBaseProcess.setProcessCode(ProcessCode);
        searchBaseProcess.setOrgId(orgId);
        searchBaseProcess.setCodeQueryMark(1);//编码查询标记(设为1做等值查询)
        baseProcessList = baseFeignApi.findProcessList(searchBaseProcess);
        return baseProcessList;
    }
    /*
     * 获取工位信息
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
     * 获取工艺路线明细信息
     * */
    public ResponseEntity<List<BaseRouteProcess>> getBaseRouteProcess(Long routeId){
        ResponseEntity<List<BaseRouteProcess>> baseRouteProcessList=null;
        baseRouteProcessList = baseFeignApi.findConfigureRout(routeId);
        return baseRouteProcessList;
    }
    /*
     * 获取用户信息
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
     * 获取用户对应角色信息
     * */
    public ResponseEntity<List<SysUserRole>> findUserRoleList(Long userId){
        return securityFeignApi.findUserRoleList(userId);
    }

    /*
     * 获取授权信息
     * */
    public ResponseEntity<SysAuthRole> getSysAuthRole(Long roleId,long menuId){
        return securityFeignApi.getSysAuthRole(roleId,menuId);
    }

    /*
     * 获取工单条码信息
     * */
    public List<MesSfcWorkOrderBarcodeDto> getWorkOrderBarcode(String barcodeCode){
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList=null;
        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(barcodeCode);
        mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        return mesSfcWorkOrderBarcodeDtoList;
    }
    /*
     * 获取工单信息
     * */
    public ResponseEntity<List<MesPmWorkOrderDto>> getWorkOrder(Long workOrderId){
        ResponseEntity<List<MesPmWorkOrderDto>> mesPmWorkOrderDtoList=null;
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderId(workOrderId);
        mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
        return mesPmWorkOrderDtoList;
    }
    /*
    *获取工单信息
    */
    public ResponseEntity<List<MesPmWorkOrderDto>> getWorkOrder(String workOrderCode){
        ResponseEntity<List<MesPmWorkOrderDto>> mesPmWorkOrderDtoList=null;
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
        searchMesPmWorkOrder.setCodeQueryMark(1);
        mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
        return mesPmWorkOrderDtoList;
    }
    /*
     *获取工单BOM信息
     */
    public ResponseEntity<List<MesPmWorkOrderBomDto>> getWorkOrderBomList(SearchMesPmWorkOrderBom searchMesPmWorkOrderBom){
        ResponseEntity<List<MesPmWorkOrderBomDto>> mesPmWorkOrderBomDtoList=null;
        mesPmWorkOrderBomDtoList = pmFeignApi.findList(searchMesPmWorkOrderBom);
        return mesPmWorkOrderBomDtoList;
    }
    /*
     *获取产品BOM信息
     */
    public ResponseEntity<List<BaseProductBomDto>> getProductBomList(SearchBaseProductBom searchBaseProductBom){
        ResponseEntity<List<BaseProductBomDto>> baseProductBomDtoList=null;
        baseProductBomDtoList = baseFeignApi.findProductBomList(searchBaseProductBom);
        return baseProductBomDtoList;
    }

    /*
     *获取设备绑定产品信息
     */
    public ResponseEntity<List<EamEquipmentMaterialDto>> getEquipmentMaterialList(SearchEamEquipmentMaterial searchEamEquipmentMaterial){
        ResponseEntity<List<EamEquipmentMaterialDto>> eamEquipmentMaterialDtoList=null;
        eamEquipmentMaterialDtoList = eamFeignApi.findEquipmentMaterialDtoList(searchEamEquipmentMaterial);
        return eamEquipmentMaterialDtoList;
    }

    /*
     *获取设备条码信息
     */
    public ResponseEntity<List<EamEquipmentBarcode>> findEamEquipmentBarCodeList(SearchEamEquipmentBarcode searchEamEquipmentBarcode){
        ResponseEntity<List<EamEquipmentBarcode>> eamEquipmentBarcodeList=null;
        eamEquipmentBarcodeList = eamFeignApi.findEamEquipmentBarCodeList(searchEamEquipmentBarcode);
        return eamEquipmentBarcodeList;
    }

    /*
     *获取治具绑定产品信息
     */
    public ResponseEntity<List<EamJigMaterialDto>> getJigMaterialDtoList(SearchEamJigMaterial searchEamJigMaterial){
        ResponseEntity<List<EamJigMaterialDto>> eamJigMaterialDtoList=null;
        eamJigMaterialDtoList = eamFeignApi.findList(searchEamJigMaterial);
        return eamJigMaterialDtoList;
    }

    /*
     * 获取治具条码信息
     * */
    public ResponseEntity<List<EamJigBarcodeDto>> getJigBarCode(String jigBarCode){
        ResponseEntity<List<EamJigBarcodeDto>> eamJigBarcodeDtoList=null;
        SearchEamJigBarcode searchEamJigBarcode = new SearchEamJigBarcode();
        searchEamJigBarcode.setJigBarCode(jigBarCode);
        eamJigBarcodeDtoList = eamFeignApi.findList(searchEamJigBarcode);
        return eamJigBarcodeDtoList;
    }
    /*
     * 获取设备信息
     * */
    public ResponseEntity<List<EamEquipmentDto>> getEamEquipment(String equipmentCode){
        ResponseEntity<List<EamEquipmentDto>> eamEquipmentDtoList=null;
        SearchEamEquipment searchEamEquipment = new SearchEamEquipment();
        searchEamEquipment.setEquipmentCode(equipmentCode);
        eamEquipmentDtoList = eamFeignApi.findList(searchEamEquipment);
        return eamEquipmentDtoList;
    }
    /*
     * 获取系统配置项信息
     * */
    public ResponseEntity<List<SysSpecItem>> getSysSpecItem(String specCode){
        ResponseEntity<List<SysSpecItem>> sysSpecItemList=null;
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode(specCode);
        sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        return sysSpecItemList;
    }
    /*
     * 获取工单产前关键事项
     * */
    public ResponseEntity<List<MesPmProductionKeyIssuesOrder>> getPmPKIOList(String WorkOrderCode){
        ResponseEntity<List<MesPmProductionKeyIssuesOrder>> mesPmProductionKeyIssuesOrderList=null;
        SearchMesPmProductionKeyIssuesOrder searchMesPmProductionKeyIssuesOrder = new SearchMesPmProductionKeyIssuesOrder();
        searchMesPmProductionKeyIssuesOrder.setWorkOrderCode(WorkOrderCode);
        mesPmProductionKeyIssuesOrderList = pmFeignApi.findPmPKIOList(searchMesPmProductionKeyIssuesOrder);
        return mesPmProductionKeyIssuesOrderList;
    }
    /*
     * 获取不良现象信息
     * */
    public ResponseEntity<List<BaseBadnessPhenotypeDto>> getBadnessPhenotype(String badnessPhenotypeCode, Long orgId){
        ResponseEntity<List<BaseBadnessPhenotypeDto>> baseBadnessPhenotypeDtoList=null;
        SearchBaseBadnessPhenotype searchBaseBadnessPhenotype = new SearchBaseBadnessPhenotype();
        searchBaseBadnessPhenotype.setBadnessPhenotypeCode(badnessPhenotypeCode);
        searchBaseBadnessPhenotype.setOrgId(orgId);
        baseBadnessPhenotypeDtoList = baseFeignApi.findBadnessPhenotypeDtoList(searchBaseBadnessPhenotype);
        return baseBadnessPhenotypeDtoList;
    }

    public void  addLog(Byte result,Byte type,Long orgId,String responseData,String requestParameter) throws Exception {
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("雷赛设备过站接口");
        sysApiLog.setCallResult(result);
        sysApiLog.setCallType(type);
        sysApiLog.setApiModule("ocean-materialapi");
        sysApiLog.setOrgId(orgId);
        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);
        securityFeignApi.add(sysApiLog);
    }

}
