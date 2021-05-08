package com.fantechs.provider.mes.sfc.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSetDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.RedisUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * 1．传【产品条码、工序ID】参数；
     * 2．系统检查产品条码是否正确，不正确返回错误信息结束；
     * 3．系统检查条码流程是否正确，不正确返回错误信息结束；
     * 4．系统检查条码状态是否正确，如是否挂起、维修、完工等状态返回错误信息结束；
     * 5．系统检查条码工单状态是否正确，如是否挂起、完工、投产数量>=工单数等状态返回错误信息结束；
     * 6．系统条码检查OK；
     *
     * @return
     */
    public static Boolean checkSN(CheckProductionVo record) throws Exception {

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
     * 1．传【产品条码、下一工序ID、不良现象代码、作业人员ID、线别ID、设备ID、工艺路线ID】参数，检验结果是OK时不良现象代码写N/A,设备ID为空值时写N/A；
     * 2．更新产品条码当前工序、下一工序；
     * 3．写过站记录；
     *
     * @return
     * @throws Exception
     */
    public static int updateProcess(UpdateProcessDto dto) throws Exception {
        // 获取条码
        MesSfcBarcodeProcess mesSfcBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .workOrderCode(dto.getBarCode())
                .build());
        ResponseEntity<List<BaseRouteProcess>> responseEntity = barcodeUtils.baseFeignApi.findConfigureRout(dto.getRouteId());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012008);
        }
        List<BaseRouteProcess> routeProcessList = responseEntity.getData();
        // 若入参下一工序ID跟过站表下一工序ID不一致
        // 则判断过站表下一工序是否必过工序
        if (dto.getNextProcessId().equals(mesSfcBarcodeProcess.getNextProcessId())) {
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
        // 更新当前工序
        mesSfcBarcodeProcess.setProcessId(mesSfcBarcodeProcess.getNextProcessId());
        mesSfcBarcodeProcess.setProcessCode(mesSfcBarcodeProcess.getProcessCode());
        mesSfcBarcodeProcess.setProcessName(mesSfcBarcodeProcess.getProcessName());
        Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                .filter(i -> dto.getNextProcessId().equals(i.getProcessId()))
                .findFirst();
        if (!routeProcessOptional.isPresent()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012011, mesSfcBarcodeProcess.getNextProcessId());
        }
        BaseRouteProcess routeProcess = routeProcessOptional.get();
        ResponseEntity<BaseProcess> processResponseEntity = barcodeUtils.baseFeignApi.processDetail(routeProcess.getNextProcessId());
        if (processResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcess.getNextProcessId());
        }
        BaseProcess baseProcess = processResponseEntity.getData();
        // 设置下一工序
        mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
        mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
        mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
        mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcess.getPassStationCount() + 1);
        int update = barcodeUtils.mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);
        if (update < 1) {
            throw new RuntimeException("更新过站表下一工序失败！");
        }
        // 增加过站记录
        MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = new MesSfcBarcodeProcessRecord();
        BeanUtils.copyProperties(mesSfcBarcodeProcessRecord, mesSfcBarcodeProcess);
        mesSfcBarcodeProcessRecord.setOperatorUserId(dto.getOperatorUserId());
        mesSfcBarcodeProcessRecord.setModifiedTime(new Date());
        mesSfcBarcodeProcessRecord.setModifiedUserId(dto.getOperatorUserId());
        return barcodeUtils.mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);
    }


    /**
     * 生成包箱码
     * @param materialId 产品物料ID
     * @param barcodeRuleSetId 产品规则集合ID
     * @param processId 工序ID
     * @param materialCode 产品物料编码
     * @param workOrderId 工单ID
     * @param categoryCode 条码规则类别编号
     * @return
     * @throws Exception
     */
    public static String generatorCartonCode(String materialId, Long barcodeRuleSetId, String processId, String materialCode, String workOrderId, String categoryCode) throws Exception{
        List<BaseBarcodeRuleDto> ruleList = getBarcodeRuleList(materialId, barcodeRuleSetId, processId);
        SearchBaseLabelCategory category = new SearchBaseLabelCategory();
        category.setLabelCategoryCode(categoryCode);
        ResponseEntity<List<BaseLabelCategoryDto>> categoryListResponseEntity = barcodeUtils.baseFeignApi.findLabelCategoryList(category);
        if (categoryListResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012021);
        }
        // 包箱条码规则类别有且只有一条
        List<BaseLabelCategoryDto> categoryDtoList = categoryListResponseEntity.getData();
        if (categoryDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012021);
        }
        BaseLabelCategoryDto labelCategoryDto = categoryDtoList.get(0);
        BaseBarcodeRuleDto barcodeRuleDto = ruleList.stream()
                .filter(item -> item.getBarcodeRuleCategoryId().equals(labelCategoryDto.getLabelCategoryId()))
                .findFirst()
                .orElseThrow(() -> new BizErrorException(ErrorCodeEnum.PDA40012020));
        // 获取规则配置列表
        SearchBaseBarcodeRuleSpec baseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
        baseBarcodeRuleSpec.setBarcodeRuleId(barcodeRuleDto.getBarcodeRuleId());
        ResponseEntity<List<BaseBarcodeRuleSpec>> barcodeRuleSpecResponseEntity = barcodeUtils.baseFeignApi.findSpec(baseBarcodeRuleSpec);
        if (barcodeRuleSpecResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012022);
        }
        List<BaseBarcodeRuleSpec> barcodeRuleSpecList = barcodeRuleSpecResponseEntity.getData();
        if (barcodeRuleSpecList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012023);
        }

        String lastBarCode = null;
        boolean hasKey = barcodeUtils.redisUtil.hasKey(barcodeRuleDto.getBarcodeRuleId().toString());
        if(hasKey){
            // 从redis获取上次生成条码
            Object redisRuleData = barcodeUtils.redisUtil.get(barcodeRuleDto.getBarcodeRuleId().toString());
            lastBarCode = String.valueOf(redisRuleData);
        }
        //获取最大流水号
        String maxCode = barcodeUtils.baseFeignApi.generateMaxCode(barcodeRuleSpecList, lastBarCode).getData();
        //生成条码
        ResponseEntity<String> rs = barcodeUtils.baseFeignApi.generateCode(barcodeRuleSpecList, maxCode, materialCode, workOrderId);
        if (rs.getCode() != 0) {
            throw new BizErrorException(rs.getMessage());
        }
        // 更新redis最新包箱号
        barcodeUtils.redisUtil.set(barcodeRuleDto.getBarcodeRuleId().toString(), rs.getData());
        return rs.getData();
    }

    /**
     * 打印条码
     * @param dto
     */
    public static void printBarCode(MesSfcWorkOrderBarcodeDto dto) {
        LabelRuteDto labelRuteDto = barcodeUtils.mesSfcWorkOrderBarcodeMapper.findRule("01", dto.getWorkOrderId());
        PrintModel printModel = barcodeUtils.mesSfcWorkOrderBarcodeMapper.findPrintModel(dto.getBarcodeType(), dto.getWorkOrderId());
        printModel.setQrCode(dto.getBarcode());
        PrintDto printDto = new PrintDto();
        printDto.setLabelName(labelRuteDto.getLabelName());
        printDto.setLabelVersion(labelRuteDto.getLabelVersion());
        printDto.setPrintName("测试");
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
            if (!processId.equals(mesSfcBarcodeProcess.getProcessId())) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012003, mesSfcBarcodeProcess.getBarcode(), mesSfcBarcodeProcess.getNextProcessCode());
            }
            if (!stationId.equals(mesSfcBarcodeProcess.getStationId())) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012013, mesSfcBarcodeProcess.getProcessCode(), mesSfcBarcodeProcess.getStationId(), stationId);
            }
        }
        throw new BizErrorException(ErrorCodeEnum.PDA40012002, mesSfcWorkOrderBarcodeDto.getBarcode());
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
     * 一般情况下每个工序、工位下需要生成条码只有一个，现保留获取多个条码规则情况
     * 在调用方法处做处理，需要哪个规则用哪个
     *
     * @param materialId       产品物料ID
     * @param barcodeRuleSetId 工单规则集合ID
     * @param processId        工序ID
     * @return
     * @throws Exception
     */
    private static List<BaseBarcodeRuleDto> getBarcodeRuleList(String materialId, Long barcodeRuleSetId, String processId) throws Exception {
        SearchBaseLabelMaterial searchBaseLabelMaterial = new SearchBaseLabelMaterial();
        searchBaseLabelMaterial.setMaterialId(materialId);
        searchBaseLabelMaterial.setProcessId(processId);
        ResponseEntity<List<BaseLabelMaterialDto>> materialResult = barcodeUtils.baseFeignApi.findLabelMaterialList(searchBaseLabelMaterial);
        if (materialResult.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012014, materialId, processId);
        }
        // 1、通过物料以及工序查询标签
        List<BaseLabelMaterialDto> labelMaterialDtos = materialResult.getData();
        // 2、通过标签获取标签类型
        ResponseEntity<List<BaseLabel>> labelResult = barcodeUtils.baseFeignApi.findListByIDs(labelMaterialDtos.stream()
                .map(BaseLabelMaterial::getLabelId)
                .collect(Collectors.toList()));
        if (labelResult.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012015, materialId, processId);
        }
        List<BaseLabel> baseLabels = labelResult.getData();
        // 3、通过标签类型获取所有条码规则
        ResponseEntity<List<BaseBarcodeRuleDto>> barcodeRuleResult = barcodeUtils.baseFeignApi.findListByBarcodeRuleCategoryIds(baseLabels.stream()
                .map(BaseLabel::getLabelCategoryId)
                .collect(Collectors.toList()));
        if (barcodeRuleResult.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012016);
        }
        List<BaseBarcodeRuleDto> barcodeRuleDtos = barcodeRuleResult.getData();
        // 4、通过工单规则集合获取所有条码规则
        SearchBaseBarcodeRuleSetDet barcodeRuleSetDet = new SearchBaseBarcodeRuleSetDet();
        barcodeRuleSetDet.setBarcodeRuleSetId(barcodeRuleSetId);
        ResponseEntity<List<BaseBarcodeRuleSetDetDto>> ruleSetDetResult = barcodeUtils.baseFeignApi.findBarcodeRuleSetDetList(barcodeRuleSetDet);
        if (ruleSetDetResult.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012017, barcodeRuleSetId);
        }
        List<BaseBarcodeRuleSetDetDto> ruleSetDetDtos = ruleSetDetResult.getData();
        // 5、取34交集
        List<Long> ruleCategoryIDs1 = barcodeRuleDtos.stream().map(BaseBarcodeRule::getBarcodeRuleCategoryId).collect(Collectors.toList());
        List<Long> ruleCategoryIDs2 = ruleSetDetDtos.stream().map(BaseBarcodeRuleSetDetDto::getBarcodeRuleCategoryId).collect(Collectors.toList());
        // 取两个分类集合交集
        ruleCategoryIDs1.retainAll(ruleCategoryIDs2);
        // 获取交集中的条码规则
        return barcodeRuleDtos.stream().filter(item -> ruleCategoryIDs1.contains(item.getBarcodeRuleCategoryId())).collect(Collectors.toList());
    }

    // endregion

}
