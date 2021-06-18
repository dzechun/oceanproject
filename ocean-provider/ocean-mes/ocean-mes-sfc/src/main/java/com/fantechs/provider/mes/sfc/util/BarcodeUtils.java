package com.fantechs.provider.mes.sfc.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessRecordService;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

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
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;
    @Resource
    private RedisUtil redisUtil;

    // endregion

    private static BarcodeUtils barcodeUtils;

    @PostConstruct
    public void init() {
        barcodeUtils = this;
        barcodeUtils.mesSfcWorkOrderBarcodeMapper = this.mesSfcWorkOrderBarcodeMapper;
        barcodeUtils.mesSfcWorkOrderBarcodeService = this.mesSfcWorkOrderBarcodeService;
        barcodeUtils.mesSfcBarcodeProcessService = this.mesSfcBarcodeProcessService;
        barcodeUtils.mesSfcBarcodeProcessRecordService = this.mesSfcBarcodeProcessRecordService;
        barcodeUtils.pmFeignApi = this.pmFeignApi;
        barcodeUtils.baseFeignApi = this.baseFeignApi;
        barcodeUtils.rabbitProducer = this.rabbitProducer;
        barcodeUtils.redisUtil = this.redisUtil;
    }


    /**
     * 校验条码以及工单
     *
     * @return
     */
    public static Boolean checkSN(CheckProductionDto record) throws Exception {

        // 1、判断条码是否正确（是否存在）
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = checkBarcodeStatus(record.getBarCode());

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
        mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
        mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
        mesSfcBarcodeProcess.setStationName(baseStation.getStationName());
        mesSfcBarcodeProcess.setSectionId(baseProcess.getSectionId());
        mesSfcBarcodeProcess.setSectionCode(baseProcess.getSectionCode());
        mesSfcBarcodeProcess.setSectionName(baseProcess.getSectionName());
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
        if (mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
            mesSfcBarcodeProcess.setProductionTime(new Date());
            mesSfcBarcodeProcess.setNextProcessId(0L);
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
            if (mesPmWorkOrder.getWorkOrderStatus().equals("2")) {
                mesPmWorkOrder.setWorkOrderStatus(Byte.valueOf("3"));
            }
            barcodeUtils.pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
        }
        // 判断当前工序是否为产出工序，且是该条码在工单工序第一次过站，工单产出 +1
        if (dto.getNowProcessId().equals(mesPmWorkOrder.getOutputProcessId()) && mesSfcBarcodeProcessRecordDtoList.isEmpty()) {
            mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty().add(BigDecimal.ONE));
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
    private static MesSfcWorkOrderBarcodeDto checkBarcodeStatus(String barCode) {
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
                throw new BizErrorException(ErrorCodeEnum.PDA40012003, mesSfcBarcodeProcess.getBarcode(), mesSfcBarcodeProcess.getNextProcessCode());
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
        ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntity = barcodeUtils.pmFeignApi.workOrderDetail(mesSfcWorkOrderBarcodeDto.getWorkOrderId());
        if (pmWorkOrderResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012005, mesSfcWorkOrderBarcodeDto.getWorkOrderCode());
        }
        MesPmWorkOrder mesPmWorkOrder = pmWorkOrderResponseEntity.getData();
        if (4 == mesPmWorkOrder.getWorkOrderStatus() || 5 == mesPmWorkOrder.getWorkOrderStatus()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012006);
        }
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
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        List<BaseMaterialPackageDto> baseMaterialPackageDtos = packageSpecificationDtos.get(0).getBaseMaterialPackages();
        if(baseMaterialPackageDtos.size() <= 0){
            throw new BizErrorException();
        }
        BaseBarcodeRule baseBarcodeRule = barcodeUtils.baseFeignApi.baseBarcodeRuleDetail(baseMaterialPackageDtos.get(0).getBarcodeRuleId()).getData();

        return baseBarcodeRule;
    }

    // endregion

}
