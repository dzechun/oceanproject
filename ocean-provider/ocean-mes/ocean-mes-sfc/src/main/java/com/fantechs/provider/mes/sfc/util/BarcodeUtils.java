package com.fantechs.provider.mes.sfc.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.SysUserRole;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterialList;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


/**
 * 条码工具类
 *
 * @author hyc
 * @version 1.0
 * @date 2021/04/09 17:23
 **/
@Component
public class BarcodeUtils {

    // region 接口注入

    @Resource
    private MesSfcWorkOrderBarcodeMapper mesSfcWorkOrderBarcodeMapper;
    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcBarcodeProcessRecordService mesSfcBarcodeProcessRecordService;
    @Resource
    private MesSfcReworkOrderService mesSfcReworkOrderService;
    @Resource
    private MesSfcReworkOrderBarcodeService mesSfcReworkOrderBarcodeService;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;
    @Resource
    private EamFeignApi eamFeignApi;

    // endregion

    private static BarcodeUtils barcodeUtils;

    @PostConstruct
    public void init() {
        barcodeUtils = this;
        barcodeUtils.mesSfcWorkOrderBarcodeMapper = this.mesSfcWorkOrderBarcodeMapper;
        barcodeUtils.mesSfcWorkOrderBarcodeService = this.mesSfcWorkOrderBarcodeService;
        barcodeUtils.mesSfcBarcodeProcessService = this.mesSfcBarcodeProcessService;
        barcodeUtils.mesSfcBarcodeProcessRecordService = this.mesSfcBarcodeProcessRecordService;
        barcodeUtils.mesSfcReworkOrderService = this.mesSfcReworkOrderService;
        barcodeUtils.mesSfcReworkOrderService = this.mesSfcReworkOrderService;
        barcodeUtils.pmFeignApi = this.pmFeignApi;
        barcodeUtils.baseFeignApi = this.baseFeignApi;
        barcodeUtils.rabbitProducer = this.rabbitProducer;
        barcodeUtils.redisUtil = this.redisUtil;
        barcodeUtils.deviceInterFaceUtils=this.deviceInterFaceUtils;
        barcodeUtils.eamFeignApi=this.eamFeignApi;
    }


    /**
     * 校验条码以及工单
     *
     * @return
     */
    public static Boolean checkSN(CheckProductionDto record) throws Exception {

        // 1、判断条码是否正确（是否存在）
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = checkBarcodeStatus(record.getBarCode(), record.getWorkOrderId());

        // 2、判断条码流程是否正确（流程表）
        if (record.getProcessId() != null) {
            checkBarcodeProcess(mesSfcWorkOrderBarcodeDto, record.getProcessId(), record.getStationId());
        }

        // 3、系统检查条码工单状态是否正确（工单表）
        if (mesSfcWorkOrderBarcodeDto.getWorkOrderId() != null) {
            checkOrder(mesSfcWorkOrderBarcodeDto);
        }

        // 4、是否检查排程
        if (record.getCheckOrNot()) {

        }
        return true;
    }

    /**
     * 1．传【产品条码、当前扫码工序ID、不良现象代码、作业人员ID、线别ID、设备ID、工艺路线ID】参数，检验结果是OK时不良现象代码写N/A,设备ID为空值时写N/A；
     * 2．更新产品条码当前工序、下一工序；
     * 3．写过站记录；
     *
     * @return
     * @throws Exception
     */
    public static int updateProcess(UpdateProcessDto dto) throws Exception {
        // 获取条码
        MesSfcBarcodeProcess mesSfcBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .barcode(dto.getBarCode())
                .build());
        ResponseEntity<List<BaseRouteProcess>> responseEntity = barcodeUtils.baseFeignApi.findConfigureRout(dto.getRouteId());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012008);
        }
        List<BaseRouteProcess> routeProcessList = responseEntity.getData();
        // 获取当前工单信息
        MesPmWorkOrder mesPmWorkOrder = barcodeUtils.pmFeignApi.workOrderDetail(dto.getWorkOrderId()).getData();
        // 判断当前工序是否为产出工序,若是产出工序则不用判断下一工序
        if (!mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
            // 若入参当前扫码工序ID跟过站表下一工序ID不一致
            // 则判断过站表下一工序是否必过工序
            if (!dto.getNowProcessId().equals(mesSfcBarcodeProcess.getNextProcessId())) {
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> mesSfcBarcodeProcess.getNextProcessId().equals(i.getProcessId()))
                        .findFirst();
                if (!routeProcessOptional.isPresent()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012009, mesSfcBarcodeProcess.getNextProcessId());
                }
                BaseRouteProcess routeProcess = routeProcessOptional.get();
                if (routeProcess.getIsMustPass() == 1) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012010, mesSfcBarcodeProcess.getNextProcessId());
                }
            }
        }
        ResponseEntity<BaseProcess> processResponseEntity = barcodeUtils.baseFeignApi.processDetail(dto.getNowProcessId());
        if (processResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012012, dto.getNowProcessId());
        }

        if (mesPmWorkOrder.getWorkOrderStatus() > 3) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012032);
        }

        BaseProcess baseProcess = processResponseEntity.getData();
        // 更新当前工序
        mesSfcBarcodeProcess.setProcessId(dto.getNowProcessId());
        mesSfcBarcodeProcess.setProcessCode(baseProcess.getProcessCode());
        mesSfcBarcodeProcess.setProcessName(baseProcess.getProcessName());
        // 更新工位、工段、产线
        BaseStation baseStation = barcodeUtils.baseFeignApi.findStationDetail(dto.getNowStationId()).getData();
        if (baseStation == null) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003, "没有找到对对应的工位");
        }

        //从工段表 base_workshop_section 取出工段编码和工段名称 2021-08-02 huangshuijun
        ResponseEntity<BaseWorkshopSection> bwssResponseEntity = barcodeUtils.baseFeignApi.sectionDetail(baseProcess.getSectionId());
        BaseWorkshopSection baseWorkshopSection = bwssResponseEntity.getData();
        if(baseWorkshopSection == null)
            throw new BizErrorException(ErrorCodeEnum.PDA40012035);

        mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
        mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
        mesSfcBarcodeProcess.setStationName(baseStation.getStationName());
        mesSfcBarcodeProcess.setSectionId(baseProcess.getSectionId());//工段id
//        mesSfcBarcodeProcess.setSectionCode(baseProcess.getSectionCode());//工段code
//        mesSfcBarcodeProcess.setSectionName(baseProcess.getSectionName());//工段名称
        mesSfcBarcodeProcess.setSectionCode(baseWorkshopSection.getSectionCode());//工段code
        mesSfcBarcodeProcess.setSectionName(baseWorkshopSection.getSectionName());//工段名称
        BaseProLine baseProLine = barcodeUtils.baseFeignApi.getProLineDetail(dto.getProLineId()).getData();
        if (baseProLine == null) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003, "该产线不存在或已被删除");
        }
        mesSfcBarcodeProcess.setProLineId(baseProLine.getProLineId());
        mesSfcBarcodeProcess.setProCode(baseProLine.getProCode());
        mesSfcBarcodeProcess.setProName(baseProLine.getProName());
        mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcess.getPassStationCount() != null ? mesSfcBarcodeProcess.getPassStationCount() + 1 : 1);
        if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getNowProcessId())) {
            mesSfcBarcodeProcess.setDevoteTime(new Date());
        }
        if (mesSfcBarcodeProcess.getReworkOrderId() != null){
            // 返工单的工艺路线
            List<BaseRouteProcess> routeProcesses = barcodeUtils.baseFeignApi.findConfigureRout(mesSfcBarcodeProcess.getRouteId()).getData();
            Optional<BaseRouteProcess> lastRouteProcessOptional = routeProcesses.stream()
                    .filter(item -> item.getIsMustPass().equals(1))
                    .sorted(Comparator.comparing(BaseRouteProcess::getOrderNum).reversed())
                    .findFirst();
            if(lastRouteProcessOptional.isPresent()){
                BaseRouteProcess lastRouteProcess = lastRouteProcessOptional.get();
                if (mesSfcBarcodeProcess.getNextProcessId().equals(lastRouteProcess.getProcessId())) {
                    // 产出工序置空下一道工序关信息
                    mesSfcBarcodeProcess.setProductionTime(new Date());
                    mesSfcBarcodeProcess.setNextProcessId(0L);
                    mesSfcBarcodeProcess.setNextProcessName("");
                    mesSfcBarcodeProcess.setNextProcessCode("");

                    Map<String, Object> map = new HashMap<>();
                    map.put("reworkOrderId", mesSfcBarcodeProcess.getReworkOrderId());
                    List<MesSfcReworkOrderBarcodeDto> reworkOrderBarcodeDtos = barcodeUtils.mesSfcReworkOrderBarcodeService.findList(map);
                    List<Long> workOrderBarcodeIds = new ArrayList<>();
                    for (MesSfcReworkOrderBarcodeDto reworkOrderBarcodeDto : reworkOrderBarcodeDtos){
                        workOrderBarcodeIds.add(reworkOrderBarcodeDto.getWorkOrderBarcodeId());
                    }
                    Example example = new Example(MesSfcBarcodeProcess.class);
                    example.createCriteria().andIn("workOrderBarcodeId", workOrderBarcodeIds);
                    List<MesSfcBarcodeProcess> barcodeProcessList = barcodeUtils.mesSfcBarcodeProcessService.selectByExample(example);
                    boolean flag = true;
                    for (MesSfcBarcodeProcess item : barcodeProcessList){
                        if(!item.getBarcodeProcessId().equals(mesSfcBarcodeProcess.getBarcodeProcessId())){
                            if(item.getProductionTime() == null || item.getNextProcessId().equals(0)){
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (flag){
                        MesSfcReworkOrder mesSfcReworkOrder = barcodeUtils.mesSfcReworkOrderService.selectByKey(mesSfcBarcodeProcess.getReworkOrderId());
                        mesSfcReworkOrder.setReworkStatus((byte) 3);
                        barcodeUtils.mesSfcReworkOrderService.update(mesSfcReworkOrder);
                    }
                }else {
                    Optional<BaseRouteProcess> routeProcessOptional = routeProcesses.stream()
                            .filter(i -> dto.getNowProcessId().equals(i.getProcessId()))
                            .findFirst();
                    if (!routeProcessOptional.isPresent()) {
                        throw new BizErrorException(ErrorCodeEnum.PDA40012011, mesSfcBarcodeProcess.getNextProcessId());
                    }
                    BaseRouteProcess routeProcess = routeProcessOptional.get();
                    processResponseEntity = barcodeUtils.baseFeignApi.processDetail(routeProcess.getNextProcessId());
                    if (processResponseEntity.getCode() != 0) {
                        throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcess.getNextProcessId());
                    }
                    baseProcess = processResponseEntity.getData();
                    // 设置下一工序
                    mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                    mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                    mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
                    MesSfcReworkOrder mesSfcReworkOrder = barcodeUtils.mesSfcReworkOrderService.selectByKey(mesSfcBarcodeProcess.getReworkOrderId());
                    if(!mesSfcReworkOrder.getReworkStatus().equals("2")){
                        mesSfcReworkOrder.setReworkStatus((byte) 2);
                        barcodeUtils.mesSfcReworkOrderService.update(mesSfcReworkOrder);
                    }
                }

            }else {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "返工条码工艺路线的产出工序不存在或已被删除");
            }
        }else {
            if (mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
                // 产出工序置空下一道工序关信息
                mesSfcBarcodeProcess.setProductionTime(new Date());
                mesSfcBarcodeProcess.setNextProcessId(0L);
                mesSfcBarcodeProcess.setNextProcessName("");
                mesSfcBarcodeProcess.setNextProcessCode("");
            }else {
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> dto.getNowProcessId().equals(i.getProcessId()))
                        .findFirst();
                if (!routeProcessOptional.isPresent()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012011, mesSfcBarcodeProcess.getNextProcessId());
                }
                BaseRouteProcess routeProcess = routeProcessOptional.get();
                processResponseEntity = barcodeUtils.baseFeignApi.processDetail(routeProcess.getNextProcessId());
                if (processResponseEntity.getCode() != 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcess.getNextProcessId());
                }
                baseProcess = processResponseEntity.getData();
                // 设置下一工序
                mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
            }
        }

        mesSfcBarcodeProcess.setInProcessTime(new Date());
        mesSfcBarcodeProcess.setOutProcessTime(new Date());
        mesSfcBarcodeProcess.setOperatorUserId(dto.getOperatorUserId());
        mesSfcBarcodeProcess.setModifiedUserId(dto.getOperatorUserId());
        mesSfcBarcodeProcess.setModifiedTime(new Date());
        if(dto.getPassCodeType() == 1){
            mesSfcBarcodeProcess.setCartonCode(dto.getPassCode());
        }else if (dto.getPassCodeType() == 2){
            mesSfcBarcodeProcess.setPalletCode(dto.getPassCode());
        }
        int update = barcodeUtils.mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);
        if (update < 1) {
            throw new RuntimeException("更新过站表下一工序失败！");
        }
        // 增加过站记录
        MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = new MesSfcBarcodeProcessRecord();
        BeanUtils.copyProperties(mesSfcBarcodeProcess, mesSfcBarcodeProcessRecord);
        mesSfcBarcodeProcessRecord.setOperatorUserId(dto.getOperatorUserId());
        mesSfcBarcodeProcessRecord.setModifiedTime(new Date());
        mesSfcBarcodeProcessRecord.setModifiedUserId(dto.getOperatorUserId());
        barcodeUtils.mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);

        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeId", dto.getBarCode());
        map.put("stationId", dto.getNowStationId());
        map.put("processId", dto.getNowProcessId());
        List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(map);
        // 是否投产工序且是该条码在工单工序第一次过站，工单投产数量 +1
        if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getNowProcessId()) && mesSfcBarcodeProcessRecordDtoList.isEmpty()) {
            mesPmWorkOrder.setProductionQty(mesPmWorkOrder.getProductionQty().add(BigDecimal.ONE));
            // 若是投产工序，则判断是否首条码，若是则更新工单状态为生产中
            if (mesPmWorkOrder.getWorkOrderStatus() == (byte) 1) {
                mesPmWorkOrder.setWorkOrderStatus((byte) 3);
            }
            barcodeUtils.pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
        }
        // 判断当前工序是否为产出工序，且是该条码在工单工序第一次过站，工单产出 +1
        if (dto.getNowProcessId().equals(mesPmWorkOrder.getOutputProcessId()) && mesSfcBarcodeProcessRecordDtoList.isEmpty()) {
            mesPmWorkOrder.setOutputQty(BigDecimal.ONE.add(mesPmWorkOrder.getOutputQty()));
            if (mesPmWorkOrder.getOutputQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 0) {
                // 产出数量等于工单数量，工单完工
                mesPmWorkOrder.setWorkOrderStatus((byte) 6);
                mesPmWorkOrder.setActualEndTime(new Date());
                mesPmWorkOrder.setModifiedTime(new Date());
                mesPmWorkOrder.setModifiedUserId(dto.getOperatorUserId());
            }
            barcodeUtils.pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
        }
        return 1;
    }


    /**
     * 生成包箱码
     *
     * @param materialId       产品物料ID
     * @param processId        工序ID
     * @param materialCode     产品物料编码
     * @param workOrderId      工单ID
     * @param categoryCode 条码规则类别编号
     * @return
     * @throws Exception
     */
    public static String generatorCartonCode(Long materialId, Long processId, String materialCode, Long workOrderId, String categoryCode) throws Exception {
        BaseBarcodeRule barcodeRule = getBarcodeRule(materialId, processId);
        BaseLabelCategory labelCategory = barcodeUtils.baseFeignApi.findLabelCategoryDetail(barcodeRule.getLabelCategoryId()).getData();
        if(!labelCategory.getLabelCategoryCode().equals(categoryCode)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012021);
        }
        // 获取规则配置列表
        SearchBaseBarcodeRuleSpec baseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
        baseBarcodeRuleSpec.setBarcodeRuleId(barcodeRule.getBarcodeRuleId());
        ResponseEntity<List<BaseBarcodeRuleSpec>> barcodeRuleSpecResponseEntity = barcodeUtils.baseFeignApi.findSpec(baseBarcodeRuleSpec);
        if (barcodeRuleSpecResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012022);
        }
        List<BaseBarcodeRuleSpec> barcodeRuleSpecList = barcodeRuleSpecResponseEntity.getData();
        if (barcodeRuleSpecList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012023);
        }

        String lastBarCode = null;
        boolean hasKey = barcodeUtils.redisUtil.hasKey(barcodeRule.getBarcodeRule());
        if (hasKey) {
            // 从redis获取上次生成条码
            Object redisRuleData = barcodeUtils.redisUtil.get(barcodeRule.getBarcodeRule());
            lastBarCode = String.valueOf(redisRuleData);
        }
        //获取最大流水号
        String maxCode = barcodeUtils.baseFeignApi.generateMaxCode(barcodeRuleSpecList, lastBarCode).getData();
        //生成条码
        ResponseEntity<String> rs = barcodeUtils.baseFeignApi.generateCode(barcodeRuleSpecList, maxCode, materialCode, workOrderId.toString());
        if (rs.getCode() != 0) {
            throw new BizErrorException(rs.getMessage());
        }
        // 更新redis最新包箱号
        barcodeUtils.redisUtil.set(barcodeRule.getBarcodeRule(), rs.getData());
        return rs.getData();
    }

    /**
     * 打印包箱/栈板条码
     *
     * @param dto
     */
    public static void printBarCode(PrintCarCodeDto dto) {
        LabelRuteDto labelRuteDto = barcodeUtils.mesSfcWorkOrderBarcodeMapper.findRule(dto.getLabelTypeCode(), dto.getWorkOrderId());
//        PrintModel printModel = barcodeUtils.mesSfcWorkOrderBarcodeMapper.findPrintModel(dto.getBarcodeType(), dto.getWorkOrderId());
        PrintModel printModel = new PrintModel();
        printModel.setQrCode(dto.getBarcode());
        PrintDto printDto = new PrintDto();
        printDto.setLabelName(labelRuteDto.getLabelName());
        printDto.setLabelVersion(labelRuteDto.getLabelVersion());
        printDto.setPrintName(dto.getPrintName());
        List<PrintModel> printModelList = new ArrayList<>();
        printModelList.add(printModel);
        printDto.setPrintModelList(printModelList);
        barcodeUtils.rabbitProducer.sendPrint(printDto);
    }


    // region 校验工单条码

    /**
     * 检查条码
     *
     * @param barCode 条码
     * @return MesSfcWorkOrderBarcodeDto 条码DTO
     */
    private static MesSfcWorkOrderBarcodeDto checkBarcodeStatus(String barCode, Long workOrderId) {
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = barcodeUtils.mesSfcWorkOrderBarcodeService
                .findList(SearchMesSfcWorkOrderBarcode.builder()
                        .barcode(barCode)
                        .workOrderId(workOrderId)
                        .build());
        if (mesSfcWorkOrderBarcodeDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012000);
        }
        if (mesSfcWorkOrderBarcodeDtos.size() > 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012001);
        }
        /*
        * 流转卡状态(0-待投产 1-投产中 2-已完成 3-待打印)
        *
        */
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        if (mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 || mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 3) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012004, mesSfcWorkOrderBarcodeDto.getBarcodeStatus());
        }
        return mesSfcWorkOrderBarcodeDto;
    }

    /**
     * 判断条码流程
     *
     * @param mesSfcWorkOrderBarcodeDto 条码DTO
     * @param processId                 流程ID
     * @param processId                 工位ID
     */
    private static void checkBarcodeProcess(MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto, Long processId, Long stationId) {
        MesSfcBarcodeProcess mesSfcBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .workOrderBarcodeId(mesSfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())
                .build());
        if (mesSfcBarcodeProcess != null) {
            if (!processId.equals(mesSfcBarcodeProcess.getNextProcessId())) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012003, processId, mesSfcBarcodeProcess.getNextProcessId());
            }
        }else {
            throw new BizErrorException(ErrorCodeEnum.PDA40012002, mesSfcWorkOrderBarcodeDto.getBarcode());
        }
    }

    /**
     * 检查工单
     *
     * @param mesSfcWorkOrderBarcodeDto 条码DTO
     */
    private static void checkOrder(MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto) {
        //判断工单是否存在
        ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntity = barcodeUtils.pmFeignApi.workOrderDetail(mesSfcWorkOrderBarcodeDto.getWorkOrderId());
        if (pmWorkOrderResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012005, mesSfcWorkOrderBarcodeDto.getWorkOrderCode());
        }
        /*
        * 工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)
        *
        */
        MesPmWorkOrder mesPmWorkOrder = pmWorkOrderResponseEntity.getData();
        if (4 == mesPmWorkOrder.getWorkOrderStatus() || 5 == mesPmWorkOrder.getWorkOrderStatus()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012006);
        }
        /*
        * productionQty 投产数量
        * workOrderQty 工单数量
        * 投产数量可以=工单数量 此判断是否有问题?
        */
        if (mesPmWorkOrder.getProductionQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) > -1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012007, mesPmWorkOrder.getWorkOrderCode());
        }
    }

    // endregion

    // region 获取当前工序条码生成规则

    /**
     * 获取当前工序条码生成规则
     * 通过物料工序包装规格获取当前包装规格的条码规则
     *
     * @param materialId       产品物料ID
     * @param processId        工序ID
     * @return
     * @throws Exception
     */
    private static BaseBarcodeRule getBarcodeRule(Long materialId, Long processId) throws Exception {
        // 查询包装规格
        SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
        searchBasePackageSpecification.setMaterialId(materialId);
        searchBasePackageSpecification.setProcessId(processId);
        List<BasePackageSpecificationDto> packageSpecificationDtos = barcodeUtils.baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification).getData();
        if (packageSpecificationDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "物料匹配的包装规格数据不存在");
        }
        List<BaseMaterialPackageDto> baseMaterialPackageDtos = packageSpecificationDtos.get(0).getBaseMaterialPackages();
        if(baseMaterialPackageDtos.size() <= 0){
            throw new BizErrorException();
        }

        //根据materialId和processId 找唯一的条码规则 2021-08-09 by huangshuijun
        BaseMaterialPackageDto bMaterialPackageDto=null;
        for (BaseMaterialPackageDto baseMaterialPackageDto : baseMaterialPackageDtos) {
            if(baseMaterialPackageDto.getMaterialId().equals(materialId) && baseMaterialPackageDto.getProcessId().equals(processId)) {
                bMaterialPackageDto=baseMaterialPackageDto;
                break;
            }
        }

        //BaseBarcodeRule baseBarcodeRule = barcodeUtils.baseFeignApi.baseBarcodeRuleDetail(baseMaterialPackageDtos.get(0).getBarcodeRuleId()).getData();
        //根据materialId和processId 找唯一的条码规则 2021-08-09 by huangshuijun
        if(bMaterialPackageDto==null)
            throw new BizErrorException(ErrorCodeEnum.PDA40012036,materialId,processId);

        BaseBarcodeRule baseBarcodeRule = barcodeUtils.baseFeignApi.baseBarcodeRuleDetail(bMaterialPackageDto.getBarcodeRuleId()).getData();
        return baseBarcodeRule;
    }

    // endregion

    /*
    * 检查用户登录信息
    *
    */
    public static BaseExecuteResultDto ChkLogUserInfo(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws Exception {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            String pass = "Pass";
            String fail = "Fail";
            Long orgId = null;
            String proName = "";
            String processName = "";
            String userName = "";
            ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList = barcodeUtils.deviceInterFaceUtils.getOrId();
            if (StringUtils.isEmpty(baseOrganizationDtoList.getData())) {
                throw new Exception(fail + " 请求失败,未查询到对应组织");
            }
            //获取组织ID
            orgId = baseOrganizationDtoList.getData().get(0).getOrganizationId();

            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto)) {
                throw new Exception(fail + " 请求失败,参数为空");
            }

            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getProCode())) {
                throw new Exception(fail + " 请求失败,产线编码不能为空");
            } else {
                ResponseEntity<List<BaseProLine>> baseProLinelist = barcodeUtils.deviceInterFaceUtils.getProLine(restapiChkLogUserInfoApiDto.getProCode(), orgId);
                if (StringUtils.isEmpty(baseProLinelist.getData())) {
                    throw new Exception(fail + " 请求失败,产线编码不存在");
                }
                proName = baseProLinelist.getData().get(0).getProName();

            }
            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getProcessCode())) {
                throw new Exception(fail + " 请求失败,工序编码不能为空");
            } else {
                ResponseEntity<List<BaseProcess>> baseProcesslist = barcodeUtils.deviceInterFaceUtils.getProcess(restapiChkLogUserInfoApiDto.getProcessCode(), orgId);
                if (StringUtils.isEmpty(baseProcesslist.getData())) {
                    throw new Exception(fail + " 请求失败,工序编码不存在");
                }
                processName = baseProcesslist.getData().get(0).getProcessName();
            }
            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getUserCode())) {
                throw new Exception(fail + " 请求失败,登录用户帐号不能为空");
            }

            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getPassword())) {
                throw new Exception(fail + " 请求失败,登录用户密码不能为空");
            }

            //验证用户账号和密码是否正确
            ResponseEntity<List<SysUser>> sysUserlist = barcodeUtils.deviceInterFaceUtils.getSysUser(restapiChkLogUserInfoApiDto.getUserCode(), orgId);
            if (StringUtils.isEmpty(sysUserlist.getData())) {
                throw new Exception(fail + " 请求失败,登录用户帐号不存在");
            } else {
                SysUser sysUser = sysUserlist.getData().get(0);
                Boolean isOK = new BCryptPasswordEncoder().matches(restapiChkLogUserInfoApiDto.getPassword(), sysUser.getPassword());
                if (!isOK) {
                    throw new Exception(fail + " 请求失败,登录用户密码不正确");
                }

                //用户权限判断 判断是否有工序权限 只到菜单权限
                boolean haveAuth=false;
                ResponseEntity<List<SysUserRole>> reSysUserRoleList=barcodeUtils.deviceInterFaceUtils.findUserRoleList(sysUser.getUserId());
                if(StringUtils.isEmpty(reSysUserRoleList)){
                    throw new Exception(fail + " 请求失败,登录用户没有设置权限");
                }
                else {
                    List<SysUserRole> sysUserRoleList=reSysUserRoleList.getData();
                    for (SysUserRole sysUserRole : sysUserRoleList) {
                        Long menuId=478L;//工序菜单id
                        Long roleId=sysUserRole.getRoleId();
                        ResponseEntity<SysAuthRole> reSysAuthRole=barcodeUtils.deviceInterFaceUtils.getSysAuthRole(roleId,menuId);
                        if(StringUtils.isNotEmpty(reSysAuthRole)){
                            haveAuth=true;
                            break;
                        }
                    }
                }
                if(haveAuth==false){
                    throw new Exception(fail + " 请求失败,登录用户没有工序权限");
                }
                userName = sysUser.getUserName();
            }

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg(pass + " 验证通过 产线名称 " + proName + " 工序名称 " + processName + " 用户名称 " + userName);
        }
        catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录请求参数及结果
        barcodeUtils.deviceInterFaceUtils.addLog((byte) 0, (byte) 2, (long) 1002, baseExecuteResultDto.getIsSuccess()?baseExecuteResultDto.getSuccessMsg():baseExecuteResultDto.getFailMsg(), restapiChkLogUserInfoApiDto.toString());

        return baseExecuteResultDto;
    }
    /*
     * 1 验证传参基础信息是否正确
     * 2 检查成品SN、半成品SN状态、流程是否正确
     * 3 检查产品物料与半成品物料绑定关系
     * 4 检查设备、治具状态及绑定关系是否正确
     * 5 检查设备、治具是否可以在该产品生产
     * 6 检查产前、关键事项是否完成
     */
    public static BaseExecuteResultDto ChkSnRouting(RestapiChkSNRoutingApiDto restapiChkSNRoutingApiDto) throws Exception {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        UpdateProcessDto updateProcessDto=new UpdateProcessDto();
        try {
                Long orgId=0L;
                //参数基础信息判断
                baseExecuteResultDto=checkParameter(restapiChkSNRoutingApiDto.getProCode(),restapiChkSNRoutingApiDto.getProcessCode(),
                        restapiChkSNRoutingApiDto.getBarcodeCode(),restapiChkSNRoutingApiDto.getPartBarcode(),
                        restapiChkSNRoutingApiDto.getEamJigBarCode(),restapiChkSNRoutingApiDto.getEquipmentCode(),
                        "","","");
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());

                //接收返回结果集
                updateProcessDto=(UpdateProcessDto)baseExecuteResultDto.getExecuteResult();
                orgId=updateProcessDto.getOrgId();

                //检查产品条码与半成品条码关系
                baseExecuteResultDto=checkProHalfProRelation(restapiChkSNRoutingApiDto.getBarcodeCode(),
                        restapiChkSNRoutingApiDto.getPartBarcode(),restapiChkSNRoutingApiDto.getProcessCode(),orgId);
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());

                //检查设备与产品绑定关系
                baseExecuteResultDto=checkEquiProRelation(restapiChkSNRoutingApiDto.getBarcodeCode(),restapiChkSNRoutingApiDto.getEquipmentCode(),orgId);
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());

                //检查治具与产品绑定关系
                baseExecuteResultDto=checkJigProRelation(restapiChkSNRoutingApiDto.getBarcodeCode(),restapiChkSNRoutingApiDto.getEamJigBarCode(),orgId);
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());

                //产前关键事项是否已完成
                baseExecuteResultDto=checkPmProKeyIssues(updateProcessDto.getWorkOrderCode());
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());

                //标准条码流程检查
                CheckProductionDto checkProductionDto=new CheckProductionDto();
                checkProductionDto.setBarCode(restapiChkSNRoutingApiDto.getBarcodeCode());
                checkProductionDto.setWorkOrderId(updateProcessDto.getWorkOrderId());
                checkProductionDto.setProcessId(updateProcessDto.getNowProcessId());

                checkSN(checkProductionDto);

                baseExecuteResultDto.setIsSuccess(true);
                baseExecuteResultDto.setSuccessMsg(" 验证通过 ");
        }catch (Exception ex) {
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录请求参数及结果
        barcodeUtils.deviceInterFaceUtils.addLog((byte) 0, (byte) 2, (long) 1002, baseExecuteResultDto.getIsSuccess()?baseExecuteResultDto.getSuccessMsg():baseExecuteResultDto.getFailMsg(), restapiChkSNRoutingApiDto.toString());

        return baseExecuteResultDto;
    }

    /*
     * 1 验证传参基础信息是否正确
     * 2 检查成品SN、半成品SN状态、流程是否正确
     * 3 检查设备、治具状态及绑定关系是否正确
     * 4 检查设备、治具是否可以在该产品生产
     * 5 检查产前、关键事项是否完成
     * 前5项判断在checkParameter方法中完成
     * 6 返写治具编号使用次数
     * 7 记录条码过站时间、结果
     */
    public static BaseExecuteResultDto SnDataTransfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) throws Exception {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        UpdateProcessDto updateProcessDto=new UpdateProcessDto();
        try {
            Long orgId=0L;
            //参数基础信息判断
            baseExecuteResultDto=checkParameter(restapiSNDataTransferApiDto.getProCode(),restapiSNDataTransferApiDto.getProcessCode(),
                    restapiSNDataTransferApiDto.getBarCode(),restapiSNDataTransferApiDto.getPartBarcode(),
                    restapiSNDataTransferApiDto.getEamJigBarCode(),restapiSNDataTransferApiDto.getEquipmentCode(),
                    "","","");
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //接收返回结果集
            updateProcessDto=(UpdateProcessDto)baseExecuteResultDto.getExecuteResult();
            orgId=updateProcessDto.getOrgId();

            //检查产品条码与半成品条码关系
            baseExecuteResultDto=checkProHalfProRelation(restapiSNDataTransferApiDto.getBarCode(),
                    restapiSNDataTransferApiDto.getPartBarcode(),restapiSNDataTransferApiDto.getProcessCode(),orgId);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //获取半成品物料ID
            UpdateProcessDto updateProcessDto1=(UpdateProcessDto)baseExecuteResultDto.getExecuteResult();
            Long partMaterialId=updateProcessDto1.getPartMaterialId();

            //检查设备与产品绑定关系
            baseExecuteResultDto=checkEquiProRelation(restapiSNDataTransferApiDto.getBarCode(),restapiSNDataTransferApiDto.getEquipmentCode(),orgId);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //检查治具与产品绑定关系
            baseExecuteResultDto=checkJigProRelation(restapiSNDataTransferApiDto.getBarCode(),restapiSNDataTransferApiDto.getEamJigBarCode(),orgId);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //产前关键事项是否已完成
            baseExecuteResultDto=checkPmProKeyIssues(updateProcessDto.getWorkOrderCode());
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //标准条码流程检查
            CheckProductionDto checkProductionDto=new CheckProductionDto();
            checkProductionDto.setBarCode(restapiSNDataTransferApiDto.getBarCode());
            checkProductionDto.setWorkOrderId(updateProcessDto.getWorkOrderId());
            checkProductionDto.setProcessId(updateProcessDto.getNowProcessId());
            checkSN(checkProductionDto);

            //过站
            barcodeUtils.updateProcess(updateProcessDto);

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg(" 验证通过 ");
        }catch (Exception ex) {
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录请求参数及结果
        barcodeUtils.deviceInterFaceUtils.addLog((byte) 0, (byte) 2, (long) 1002, baseExecuteResultDto.getIsSuccess()?baseExecuteResultDto.getSuccessMsg():baseExecuteResultDto.getFailMsg(), restapiSNDataTransferApiDto.toString());

        return baseExecuteResultDto;
    }

    /*
     * 验证信息是否正确
     * String proCode;  产线编码
       String processCode;   工序编码
       String barcodeCode;  成品SN
       String partBarcode;  半成品SN 部件条码
       String eamJigBarCode;  治具SN
       String equipmentCode; 设备编码
       String sectionCode 工段
       String userCode 员工编号
       String badnessPhenotypeCode 不良现象代码
     */
    public static BaseExecuteResultDto checkParameter(String proCode,String processCode,String barcodeCode,String partBarcode,
                String eamJigBarCode,String equipmentCode, String sectionCode,String userCode,String badnessPhenotypeCode) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        UpdateProcessDto updateProcessDto=new UpdateProcessDto();
        try{
            String check = "1";
            String fail="Fail";
            Long orgId=null;
            Long MaterialId=null;

            ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=barcodeUtils.deviceInterFaceUtils.getOrId();
            if(StringUtils.isEmpty(baseOrganizationDtoList.getData())){
                throw new Exception("未查询到对应组织");
            }
            //获取组织ID
            orgId=baseOrganizationDtoList.getData().get(0).getOrganizationId();

            //设置组织ID
            updateProcessDto.setOrgId(orgId);

            //设备编码判断
            if(StringUtils.isNotEmpty(equipmentCode)){
                ResponseEntity<List<EamEquipmentDto>> eamEquipmentDtoList = barcodeUtils.deviceInterFaceUtils.getEamEquipment(equipmentCode);
                if (StringUtils.isEmpty(eamEquipmentDtoList.getData())) {
                    throw new Exception("设备编码不存在");
                }

                //判断设备使用次数
                EamEquipmentDto eamEquipmentDto=eamEquipmentDtoList.getData().get(0);
                if(eamEquipmentDto.getCurrentUsageTimes()>=eamEquipmentDto.getMaxUsageTimes())
                    throw new Exception("该设备已达到最大使用次数");

                //设置设备ID
                updateProcessDto.setEquipmentId(eamEquipmentDto.getEquipmentId().toString());
            }

            if(StringUtils.isEmpty(proCode)){
                throw new Exception("产线编码不能为空");
            }
            else{
                ResponseEntity<List<BaseProLine>> baseProLinelist=barcodeUtils.deviceInterFaceUtils.getProLine(proCode,orgId);
                if(StringUtils.isEmpty(baseProLinelist.getData())){
                    throw new Exception("产线编码不存在");
                }
                //设置产线ID
                updateProcessDto.setProLineId(baseProLinelist.getData().get(0).getProLineId());
            }
            if(StringUtils.isEmpty(processCode)){
                throw new Exception("工序编码不能为空");
            }
            else {
                ResponseEntity<List<BaseProcess>> baseProcesslist=barcodeUtils.deviceInterFaceUtils.getProcess(processCode,orgId);
                if(StringUtils.isEmpty(baseProcesslist.getData())){
                    throw new Exception("工序编码不存在");

                }
                //设置工序ID
                updateProcessDto.setNowProcessId(baseProcesslist.getData().get(0).getProcessId());
            }

            if(StringUtils.isEmpty(barcodeCode)){
                throw new Exception("成品SN不能为空");
            }
            else{
                List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList= barcodeUtils.deviceInterFaceUtils.getWorkOrderBarcode(barcodeCode);
                if(StringUtils.isEmpty(mesSfcWorkOrderBarcodeDtoList)){
                    throw new Exception("成品SN不存在");
                }

                //设置工单ID
                updateProcessDto.setWorkOrderId(mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId());
                //设置工单编号
                updateProcessDto.setWorkOrderCode(mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderCode());

                //获取工单
                ResponseEntity<MesPmWorkOrder> responseEntityWorkOrder=barcodeUtils.pmFeignApi.workOrderDetail(mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId());
                if(StringUtils.isEmpty(responseEntityWorkOrder.getData()))
                    throw new Exception("工单信息不存在");

                //设置流程ID
                updateProcessDto.setRouteId(responseEntityWorkOrder.getData().getRouteId());
            }

            //设置过站条码
            updateProcessDto.setBarCode(barcodeCode);

            //检查工段
            if (StringUtils.isNotEmpty(sectionCode)) {
                ResponseEntity<List<BaseWorkshopSection>> baseWorkshopSectionList = barcodeUtils.deviceInterFaceUtils.getWorkshopSection(sectionCode, orgId);
                if (StringUtils.isEmpty(baseWorkshopSectionList.getData())) {
                    throw new Exception("工段编码信息不存在");
                }
            }

            //检查用户
            if (StringUtils.isNotEmpty(userCode)) {
                ResponseEntity<List<SysUser>> sysUserList = barcodeUtils.deviceInterFaceUtils.getSysUser(userCode, orgId);
                if (StringUtils.isEmpty(sysUserList.getData())) {
                    throw new Exception("用户信息不存在");
                }

                //设置操作人员ID
                updateProcessDto.setOperatorUserId(sysUserList.getData().get(0).getUserId());
            }
            //检查不良现象
            if (StringUtils.isNotEmpty(badnessPhenotypeCode)) {
                ResponseEntity<List<BaseBadnessPhenotypeDto>> baseBadnessPhenotypeDtoList = barcodeUtils.deviceInterFaceUtils.getBadnessPhenotype(badnessPhenotypeCode, orgId);
                if (StringUtils.isEmpty(baseBadnessPhenotypeDtoList.getData())) {
                    throw new Exception("不良现象信息不存在");
                }
            }

            //设置不良现象代码
            updateProcessDto.setBadnessPhenotypeCode(badnessPhenotypeCode);

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setExecuteResult(updateProcessDto);

            }catch (Exception ex){
                baseExecuteResultDto.setIsSuccess(false);
                baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    /*
    * 检查产品条码与半成品条码关系
    * productionSn 产品条码
    * halfProductionSn 半成品条码
    * processCode 工序编码
    * orgId 组织ID
    */

    public static BaseExecuteResultDto checkProHalfProRelation(String productionSn,String halfProductionSn,String processCode,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            //获取配置项检查成品条码与半成品条码关系  ProductIfCheckHalfProductionRelation
            String paraValue = getSysSpecItemValue("ProductIfCheckHalfProductionRelation");
            if ("1".equals(paraValue)) {
                baseExecuteResultDto = checkProductionHalfProductionRelation(productionSn, halfProductionSn, processCode, orgId);
                if (baseExecuteResultDto.getIsSuccess() == false) {
                    throw new Exception(baseExecuteResultDto.getFailMsg());
                }
            }
            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    public static BaseExecuteResultDto checkProductionHalfProductionRelation(String productionSn,String halfProductionSn,String processCode,Long orgId) throws Exception{

        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        UpdateProcessDto updateProcessDto=new UpdateProcessDto();
        try{
            /*
            * 1 判断产品条码是否存在
            * 2 判断半成品条码是否存在
            * 2.1 存在 由条码信息获取半成品料号
            * 3 半产品条码不存在 在工单在条码中的位置配置项(WorkOrderPositionOnBarcode)设定中获取工单编号
            * 3.1 根据工单编号获取工单信息-->半成品料号
            * 4 判断产品工单bom中是否存在当前工序对应的半成品料号
            * 4.1 如果不存在 判断产品BOM中是否存在对应的半成品料号
            * 5 不存在半成品料号 报错
             */
            String workOrderCode=null;
            Long worOrderId=0L;
            Long materialId=0L;//产品物料ID
            Long partMaterialId=0L;//半成品物料ID
            Long processId=0L;//工序ID

            ResponseEntity<List<BaseProcess>> responseEntityPro=barcodeUtils.deviceInterFaceUtils.getProcess(processCode,orgId);
            if(StringUtils.isEmpty(responseEntityPro))
                throw new Exception("工序编码无效");
            processId=responseEntityPro.getData().get(0).getProcessId();

            baseExecuteResultDto=checkBarcodeStatus(productionSn);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //产品工单ID
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto=(MesSfcWorkOrderBarcodeDto)baseExecuteResultDto.getExecuteResult();
            worOrderId=mesSfcWorkOrderBarcodeDto.getWorkOrderId();
            materialId=mesSfcWorkOrderBarcodeDto.getMaterialId();

            baseExecuteResultDto=checkBarcodeStatus(halfProductionSn);
            if(baseExecuteResultDto.getIsSuccess()==false) {
                //获取配置项 工单在条码中的位置 WorkOrderPositionOnBarcode
                String paraValue = getSysSpecItemValue("WorkOrderPositionOnBarcode");
                int beginIndex = 0;
                int endIndex = 0;
                if (StringUtils.isNotEmpty(paraValue)) {
                    String[] arry = paraValue.split("-");
                    if (arry.length == 2) {
                        beginIndex = Integer.parseInt(arry[0]);
                        endIndex = Integer.parseInt(arry[1]);
                    }
                }
                workOrderCode = halfProductionSn.substring(beginIndex, endIndex);
            }
            else {
                workOrderCode=baseExecuteResultDto.getExecuteResult().toString();
            }
            if(StringUtils.isNotEmpty(workOrderCode)) {
                ResponseEntity<List<MesPmWorkOrderDto>> responseEntity = barcodeUtils.deviceInterFaceUtils.getWorkOrder(workOrderCode);
                if (StringUtils.isEmpty(responseEntity))
                    throw new Exception("找不到半成品工单信息");

                MesPmWorkOrderDto mesPmWorkOrderDto = responseEntity.getData().get(0);
                partMaterialId = mesPmWorkOrderDto.getMaterialId();

                //设置半成品物料ID
                updateProcessDto.setPartMaterialId(partMaterialId);

                SearchMesPmWorkOrderBom searchMesPmWorkOrderBom = new SearchMesPmWorkOrderBom();
                searchMesPmWorkOrderBom.setWorkOrderId(worOrderId);
                searchMesPmWorkOrderBom.setPartMaterialId(partMaterialId);
                searchMesPmWorkOrderBom.setProcessId(processId);
                ResponseEntity<List<MesPmWorkOrderBomDto>> responseEntityBom = barcodeUtils.deviceInterFaceUtils.getWorkOrderBomList(searchMesPmWorkOrderBom);
                if (StringUtils.isEmpty(responseEntityBom)) {
                    //工单BOM找不到半成品信息
                    //通过配置项是否找产品BOM ProductBomCheckRelation
                    String paraValue = getSysSpecItemValue("ProductBomCheckRelation");
                    if ("1".equals(paraValue)) {
                        //产品BOM中是否存在物料
                        SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
                        searchBaseProductBom.setMaterialId(materialId);
                        ResponseEntity<List<BaseProductBomDto>> responseEntityPB = barcodeUtils.deviceInterFaceUtils.getProductBomList(searchBaseProductBom);
                        if (StringUtils.isNotEmpty(responseEntityPB)) {
                            List<BaseProductBomDetDto> baseProductBomDetDtos = responseEntityPB.getData().get(0).getBaseProductBomDetDtos();
                            Long finalProcessId = processId;
                            Long finalMaterialId = partMaterialId;
                            Optional<BaseProductBomDetDto> productBomDetOptional = baseProductBomDetDtos.stream()
                                    .filter(i -> finalProcessId.equals(i.getProcessId()) && finalMaterialId.equals(i.getMaterialId()))
                                    .findFirst();
                            if (!productBomDetOptional.isPresent()) {
                                throw new Exception("找不到成品条码与半成品条码的关系");
                            }
                        }

                    }
                }
            }

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setExecuteResult(updateProcessDto);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }
    /*
    * 重载检查条码状态
    * 只传入条码
    */
    private static BaseExecuteResultDto checkBarcodeStatus(String barCode) throws Exception {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = barcodeUtils.mesSfcWorkOrderBarcodeService
                    .findList(SearchMesSfcWorkOrderBarcode.builder()
                            .barcode(barCode)
                            .build());
            if (mesSfcWorkOrderBarcodeDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012000);
            }
            if (mesSfcWorkOrderBarcodeDtos.size() > 1) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012001);
            }
            /*
             * 流转卡状态(0-待投产 1-投产中 2-已完成 3-待打印)
             */
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
            if (mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == (byte)2 || mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == (byte)3) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012004, mesSfcWorkOrderBarcodeDto.getBarcodeStatus());
            }

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");
            baseExecuteResultDto.setExecuteResult(mesSfcWorkOrderBarcodeDto);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }
        return baseExecuteResultDto;
    }

    /*
    * 检查设备与产品绑定关系
    * productionSn 产品条码
    * equipmentCode 设备编码
    * orgId 组织ID
    */
    public static BaseExecuteResultDto checkEquiProRelation(String productionSn,String equipmentCode,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            //获取配置项检查成品条码与半成品条码关系  EquipmentIfCheckProductionRelation
            String paraValue = getSysSpecItemValue("EquipmentIfCheckProductionRelation");
            if ("1".equals(paraValue)) {
                baseExecuteResultDto = checkEquipmentProductionRelation(productionSn, equipmentCode, orgId);
                if (baseExecuteResultDto.getIsSuccess() == false) {
                    throw new Exception(baseExecuteResultDto.getFailMsg());
                }
            }
            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    public static BaseExecuteResultDto checkEquipmentProductionRelation(String productionSn,String equipmentCode,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            Long materialId=0L;//产品物料ID

            baseExecuteResultDto=checkBarcodeStatus(productionSn);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //产品物料ID
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto=(MesSfcWorkOrderBarcodeDto)baseExecuteResultDto.getExecuteResult();
            materialId=mesSfcWorkOrderBarcodeDto.getMaterialId();

            //获取设备绑定产品信息
            SearchEamEquipmentMaterial searchEamEquipmentMaterial=new SearchEamEquipmentMaterial();
            searchEamEquipmentMaterial.setEquipmentCode(equipmentCode);
            ResponseEntity<List<EamEquipmentMaterialDto>> responseEntityDto=barcodeUtils.deviceInterFaceUtils.getEquipmentMaterialList(searchEamEquipmentMaterial);
            if(StringUtils.isEmpty(responseEntityDto.getData()))
                throw new Exception("找不到设备与产品的绑定信息");

            List<EamEquipmentMaterialList> eamEquipmentMaterialLists=responseEntityDto.getData().get(0).getList();

            Long finalMaterialId=materialId;
            Optional<EamEquipmentMaterialList> equipmentMaterialDetOptional = eamEquipmentMaterialLists.stream()
                    .filter(i -> finalMaterialId.equals(i.getMaterialId()))
                    .findFirst();
            if (!equipmentMaterialDetOptional.isPresent()) {
                throw new Exception("找不到设备与产品的绑定关系");
            }

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    //////////////////////////////////////////
    /*
     * 检查治具与产品绑定关系
     * productionSn 产品条码
     * eamJigBarCode 治具条码
     * orgId 组织ID
     */
    public static BaseExecuteResultDto checkJigProRelation(String productionSn,String eamJigBarCode,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            //获取配置项检查治具与产品条码关系  JigsIfCheckProductionRelation
            String paraValue = getSysSpecItemValue("JigsIfCheckProductionRelation");
            if ("1".equals(paraValue)) {
                baseExecuteResultDto = checkJigProductRelation(productionSn, eamJigBarCode, orgId);
                if (baseExecuteResultDto.getIsSuccess() == false) {
                    throw new Exception(baseExecuteResultDto.getFailMsg());
                }
            }
            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    public static BaseExecuteResultDto checkJigProductRelation(String productionSn,String eamJigBarCode,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            Long materialId=0L;//产品物料ID
            baseExecuteResultDto=checkBarcodeStatus(productionSn);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //产品物料ID
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto=(MesSfcWorkOrderBarcodeDto)baseExecuteResultDto.getExecuteResult();
            materialId=mesSfcWorkOrderBarcodeDto.getMaterialId();

            if(StringUtils.isNotEmpty(eamJigBarCode)){
                String[] jigBarCodeA=eamJigBarCode.split(",");
                for (String item : jigBarCodeA) {
                    if(StringUtils.isNotEmpty(item)) {
                        ResponseEntity<List<EamJigBarcodeDto>> eamJigBarcodeDtoList = barcodeUtils.deviceInterFaceUtils.getJigBarCode(item);
                        if (StringUtils.isEmpty(eamJigBarcodeDtoList.getData())) {
                            throw new Exception("治具条码信息不存在-->"+item);
                        }
                        else {
                            //判断治具状态
                            EamJigBarcodeDto eamJigBarcodeDto = eamJigBarcodeDtoList.getData().get(0);
                            if ((byte)3 == eamJigBarcodeDto.getUsageStatus()) {
                                throw new Exception("治具条码已停用-->" + item);
                            }
                            //判断治具编码与产品绑定关系
                            Long jigID = eamJigBarcodeDto.getJigId();

                            SearchEamJigMaterial searchEamJigMaterial = new SearchEamJigMaterial();
                            searchEamJigMaterial.setJigId(jigID);
                            searchEamJigMaterial.setMaterialId(materialId);
                            ResponseEntity<List<EamJigMaterialDto>> eamJigReMaterialDtoList = barcodeUtils.deviceInterFaceUtils.getJigMaterialDtoList(searchEamJigMaterial);
                            if (StringUtils.isEmpty(eamJigReMaterialDtoList.getData())) {
                                throw new Exception("找不到治具条码-->" + item+" 与产品条码的绑定关系");
                            }

                            //判断治具使用次数和天数
                            //治具条码表 currentUsageTime 当前使用次数
                            //治具条码表 currentUsageDays 当前使用天数
                            //治具表 maxUsageTime 最大使用次数
                            //治具表 maxUsageDays 最大使用天数
                            ResponseEntity<EamJig> responseEntityJig=barcodeUtils.eamFeignApi.findEamJigDetail(jigID);
                            if(StringUtils.isEmpty(responseEntityJig.getData())){
                                throw new Exception("找不到治具条码-->"+item+" 相应的治具信息");
                            }
                            EamJig eamJig=responseEntityJig.getData();
                            //最大使用次数判断
                            if(eamJigBarcodeDto.getCurrentUsageTime()+1>eamJig.getMaxUsageTime()){
                                throw new Exception("治具条码-->"+item+" 已达到最大使用次数");
                            }
                            //最大使用天数判断
                            if(eamJigBarcodeDto.getCurrentUsageDays()+1>eamJig.getMaxUsageDays()){
                                throw new Exception("治具条码-->"+item+" 已达到最大使用天数");
                            }
                        }
                    }
                }
            }

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    /////////////////////////////////////////
    /*
     * 产前关键事项是否已完成
     * workOrderCode 工单编码
     */
    public static BaseExecuteResultDto checkPmProKeyIssues(String workOrderCode) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            //获取配置项产前关键事项是否已完成  WorkOrderIfNeedProductionKeyIssues
            String paraValue = getSysSpecItemValue("WorkOrderIfNeedProductionKeyIssues");
            if ("1".equals(paraValue)) {
                baseExecuteResultDto = checkPmProductionKeyIssues(workOrderCode);
                if (baseExecuteResultDto.getIsSuccess() == false) {
                    throw new Exception(baseExecuteResultDto.getFailMsg());
                }

            }

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    public static BaseExecuteResultDto checkPmProductionKeyIssues(String workOrderCode) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            ResponseEntity<List<MesPmProductionKeyIssuesOrder>> PmPKIOList = barcodeUtils.deviceInterFaceUtils.getPmPKIOList(workOrderCode);
            if (StringUtils.isEmpty(PmPKIOList.getData())) {
                throw new Exception("工单产前关键事项未完成");
            }
            else {
                MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder = PmPKIOList.getData().get(0);
                if (!"2".equals(mesPmProductionKeyIssuesOrder.getOrderStatus())) {
                    throw new Exception("工单产前关键事项未完成");
                }
            }

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }
    /*
    * 查询系统配置项值
    * specCode 配置项编码
    */
    public static String getSysSpecItemValue(String specCode){
        String paraValue="";
        ResponseEntity<List<SysSpecItem>> sysSpecItemList= barcodeUtils.deviceInterFaceUtils.getSysSpecItem(specCode);
        if(StringUtils.isNotEmpty(sysSpecItemList)) {
            SysSpecItem sysSpecItem = sysSpecItemList.getData().get(0);
            paraValue = sysSpecItem.getParaValue();
        }

        return paraValue;
    }

    /*
     * 设备投料
     * workOrderId 工单ID
     * productionSn 产品条码
     * halfProductionSn 半成品条码或物料条码
     * proLineId 产线ID
     * processId 工序ID
     * stationId 工位ID
     * materialId 产品物料ID
     * userId 操作用户ID
     * orgId 组织ID
     */
    public static BaseExecuteResultDto bandingWorkOrderBarcode(Long workOrderId,String productionSn,String halfProductionSn,
                           Long proLineId,Long processId,Long stationId,Long materialId,Long userId,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            if(StringUtils.isNotEmpty(halfProductionSn)){
                /*
                * 1 获取工单BOM明细
                * 2 比较使用量与当前已绑定量
                * 3 绑定到mes_sfc_key_part_relevance 生产管理-关键部件关联表
                */

            }


            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

}
