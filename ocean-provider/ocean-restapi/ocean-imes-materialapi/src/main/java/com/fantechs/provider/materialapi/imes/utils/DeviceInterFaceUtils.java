package com.fantechs.provider.materialapi.imes.utils;

import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.general.dto.basic.BaseBadnessPhenotypeDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    private AuthFeignApi securityFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;

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

    /*
    * 验证信息是否正确
    *
    */
    public String checkParameter(String proCode,String processCode,String barcodeCode,String partBarcode,
                        String eamJigBarCode,String equipmentCode, String sectionCode,String userCode,String badnessPhenotypeCode) {
        String check = "1";
        String fail="Fail";
        Long orgId=null;
        Long MaterialId=null;
        /* String proCode;  产线编码
           String processCode;   工序编码
           String barcodeCode;  成品SN
           String partBarcode;  半成品SN 部件条码
           String eamJigBarCode;  治具SN
           String equipmentCode; 设备编码
           String sectionCode 工段
           String userCode 员工编号
           String badnessPhenotypeCode 不良现象代码
         */

        ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=this.getOrId();
        if(StringUtils.isEmpty(baseOrganizationDtoList.getData())){
            check = fail+" 请求失败,未查询到对应组织";
            return check;
        }
        //获取组织ID
        orgId=baseOrganizationDtoList.getData().get(0).getOrganizationId();

        if(StringUtils.isEmpty(proCode)){
            check = fail+" 请求失败,产线编码不能为空";
            return check;
        }
        else{
            ResponseEntity<List<BaseProLine>> baseProLinelist=this.getProLine(proCode,orgId);
            if(StringUtils.isEmpty(baseProLinelist.getData())){
                check = fail+" 请求失败,产线编码不存在";
                return check;
            }
        }
        if(StringUtils.isEmpty(processCode)){
            check = fail+" 请求失败,工序编码不能为空";
            return check;
        }
        else {
            ResponseEntity<List<BaseProcess>> baseProcesslist=this.getProcess(processCode,orgId);
            if(StringUtils.isEmpty(baseProcesslist.getData())){
                check = fail+" 请求失败,工序编码不存在";
                return check;
            }

        }
        if(StringUtils.isEmpty(barcodeCode)){
            check = fail+" 请求失败,成品SN不能为空";
            return check;
        }
        else{
            ResponseEntity<List<MesSfcWorkOrderBarcodeDto>> mesSfcWorkOrderBarcodeDtoList= this.getWorkOrderBarcode(barcodeCode);
            if(StringUtils.isEmpty(mesSfcWorkOrderBarcodeDtoList.getData())){
                check = fail+" 请求失败,成品SN不存在";
                return check;
            }

            //检查条码状态
            if (mesSfcWorkOrderBarcodeDtoList.getData().size() > 1) {
                check = fail+" 请求失败,成品SN重复";
                return check;
            }
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtoList.getData().get(0);
            if (mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 || mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 3) {
                check = fail+" 请求失败,成品SN条码状态不正确";
                return check;
            }
            //检查工段
            if(StringUtils.isNotEmpty(sectionCode)){
                ResponseEntity<List<BaseWorkshopSection>> baseWorkshopSectionList=this.getWorkshopSection(sectionCode,orgId);
                if(StringUtils.isEmpty(baseWorkshopSectionList.getData())){
                    check = fail+" 请求失败,工段编码信息不存在";
                    return check;
                }
            }
            //检查用户
            if(StringUtils.isNotEmpty(userCode)){
                ResponseEntity<List<SysUser>> sysUserList=this.getSysUser(userCode,orgId);
                if(StringUtils.isEmpty(sysUserList.getData())){
                    check = fail+" 请求失败,用户信息不存在";
                    return check;
                }
            }
            //检查不良现象
            if(StringUtils.isNotEmpty(badnessPhenotypeCode)){
                ResponseEntity<List<BaseBadnessPhenotypeDto>> baseBadnessPhenotypeDtoList=this.getBadnessPhenotype(badnessPhenotypeCode,orgId);
                if(StringUtils.isEmpty(baseBadnessPhenotypeDtoList.getData())){
                    check = fail+" 请求失败,不良现象信息不存在";
                    return check;
                }
            }

            //检查工单
            Long workOrderId=mesSfcWorkOrderBarcodeDto.getWorkOrderId();
            ResponseEntity<List<MesPmWorkOrderDto>> mesPmWorkOrderDtoList= this.getWorkOrder(workOrderId);
            if(StringUtils.isEmpty(mesPmWorkOrderDtoList.getData())){
                check = fail+" 请求失败,生产工单不存在";
                return check;
            }
            MesPmWorkOrderDto mesPmWorkOrderDto=mesPmWorkOrderDtoList.getData().get(0);

            if(StringUtils.isEmpty(mesPmWorkOrderDto.getRouteId())){
                check = fail+" 请求失败,工单未设置工艺流程";
                return check;
            }
            if (4 == mesPmWorkOrderDto.getWorkOrderStatus() || 5 == mesPmWorkOrderDto.getWorkOrderStatus()) {
                check = fail+" 请求失败,工单状态已完成或已挂起";
                return check;
            }
            if (mesPmWorkOrderDto.getProductionQty().compareTo(mesPmWorkOrderDto.getWorkOrderQty()) > -1) {
                check = fail+" 请求失败,工单投产数量大于等于工单数";
                return check;
            }
            /*
             * 产前关键事项是否完成判断
             * 1 获取配置项设置
             * 2 根据配置项值判断
             */
            ResponseEntity<List<SysSpecItem>> sysSpecItemList= this.getSysSpecItem("WorkOrderIfNeedProductionKeyIssues");
            if(StringUtils.isNotEmpty(sysSpecItemList.getData().get(0))){
                SysSpecItem sysSpecItem=sysSpecItemList.getData().get(0);
                if("1".equals(sysSpecItem.getParaValue())){
                    ResponseEntity<List<MesPmProductionKeyIssuesOrder>> PmPKIOList= this.getPmPKIOList(mesPmWorkOrderDto.getWorkOrderCode());
                    if(StringUtils.isEmpty(PmPKIOList.getData())){
                        check = fail+" 请求失败,工单产前关键事项未完成";
                        return check;
                    }
                    else{
                        MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder=PmPKIOList.getData().get(0);
                        if(!"2".equals(mesPmProductionKeyIssuesOrder.getOrderStatus())){
                            //产前关键事项确认状态 1 待确认 2 已确认
                            check = fail+" 请求失败,工单产前关键事项未完成";
                            return check;
                        }
                    }

                }
            }
            //检查工序是否在工单的工艺路线工序中
            Long routeId=mesPmWorkOrderDto.getRouteId();
            ResponseEntity<List<BaseRouteProcess>> responseEntity=this.getBaseRouteProcess(routeId);
            if(StringUtils.isEmpty(responseEntity.getData())){
                check = fail+" 请求失败,生产工单工艺路线不存在";
                return check;
            }
            List<BaseRouteProcess> routeProcessList=responseEntity.getData();
            ResponseEntity<List<BaseProcess>> baseProcesslist=this.getProcess(processCode,orgId);
            BaseProcess baseProcess=baseProcesslist.getData().get(0);
            if(StringUtils.isNotEmpty(baseProcess.getProcessId())) {
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> baseProcess.getProcessId().equals(i.getProcessId()))
                        .findFirst();
                if (!routeProcessOptional.isPresent()) {
                    check = fail+" 请求失败,当前工序在生产工单工艺路线工序中不存在";
                    return check;
                }
            }

            MaterialId=mesPmWorkOrderDto.getMaterialId();

        }

        return check;
    }
}
