package com.fantechs.provider.materialapi.imes.utils;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipment;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigBarcode;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigReMaterial;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    private SFCFeignApi sfcFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private EamFeignApi eamFeignApi;

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
     * 获取工序信息
     * */
    public ResponseEntity<List<BaseProcess>> getProcess(String ProcessCode, Long orgId){
        ResponseEntity<List<BaseProcess>> baseProcessList=null;
        SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
        searchBaseProcess.setProcessCode(ProcessCode);
        searchBaseProcess.setOrgId(orgId);
        baseProcessList = baseFeignApi.findProcessList(searchBaseProcess);
        return baseProcessList;
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
     * 获取工单条码信息
     * */
    public ResponseEntity<List<MesSfcWorkOrderBarcodeDto>> getWorkOrderBarcode(String barcodeCode){
        ResponseEntity<List<MesSfcWorkOrderBarcodeDto>> mesSfcWorkOrderBarcodeDtoList=null;
        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(barcodeCode);
        mesSfcWorkOrderBarcodeDtoList = sfcFeignApi.findList(searchMesSfcWorkOrderBarcode);
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
     * 获取治具绑定产品信息
     * */
    public ResponseEntity<List<EamJigReMaterialDto>> getEamJigReMaterial(Long MaterialId,Long JigID){
        ResponseEntity<List<EamJigReMaterialDto>> eamJigReMaterialDtoList=null;
        SearchEamJigReMaterial searchEamJigReMaterial = new SearchEamJigReMaterial();
        searchEamJigReMaterial.setMaterialId(MaterialId);
        searchEamJigReMaterial.setJigId(JigID);
        eamJigReMaterialDtoList = eamFeignApi.findList(searchEamJigReMaterial);
        return eamJigReMaterialDtoList;
    }
}
