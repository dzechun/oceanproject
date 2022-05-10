package com.fantechs.provider.mes.sfc.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterialList;
import com.fantechs.common.base.general.entity.eam.search.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 条码工具类
 *
 * @author hyc
 * @version 1.0
 * @date 2021/04/09 17:23
 **/
@Component
@Slf4j
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
    private MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;
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
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;

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
        barcodeUtils.mesSfcKeyPartRelevanceService=this.mesSfcKeyPartRelevanceService;
        barcodeUtils.mesSfcBarcodeProcessMapper=this.mesSfcBarcodeProcessMapper;
    }


    /**
     * 校验条码以及工单
     *
     * @return
     */
    public static Boolean checkSN(CheckProductionDto record) throws Exception {

        // 1、判断条码是否正确（是否存在）
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = checkBarcodeStatus(record.getBarCode(), record.getWorkOrderId());

        MesPmWorkOrder mesPmWorkOrder = barcodeUtils.pmFeignApi.workOrderDetail(mesSfcWorkOrderBarcodeDto.getWorkOrderId()).getData();

        // 2、判断条码流程是否正确（流程表）
        if (record.getProcessId() != null) {
            checkBarcodeProcess(mesSfcWorkOrderBarcodeDto, record.getProcessId(), mesPmWorkOrder.getRouteId());
        }

        // 3、系统检查条码工单状态是否正确（工单表）
        if (mesSfcWorkOrderBarcodeDto.getWorkOrderId() != null) {
            checkOrder(mesSfcWorkOrderBarcodeDto, record.getProLineId());
        }

        // 4、是否检查排程
        if (record.getCheckOrNot()) {

        }
        return true;
    }

    public static Boolean checkSN(CheckProductionDto record,Long workOrderBarcodeId,Long routeId) throws Exception {

        // 2、判断条码流程是否正确（流程表）
        if (record.getProcessId() != null) {
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto=new MesSfcWorkOrderBarcodeDto();
            mesSfcWorkOrderBarcodeDto.setWorkOrderBarcodeId(workOrderBarcodeId);
            mesSfcWorkOrderBarcodeDto.setBarcode(record.getBarCode());
            checkBarcodeProcess(mesSfcWorkOrderBarcodeDto, record.getProcessId(), routeId);
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
        log.info("================== 开始 ==================");
        long start = System.currentTimeMillis();
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
        long one1 = System.currentTimeMillis();
        log.info("============== 判断当前工序one1:"+ (one1 - start));
        // 判断当前工序是否为产出工序,若是产出工序则不用判断下一工序
        if (!mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
            // 若入参当前扫码工序ID跟过站表下一工序ID不一致
            // 则判断过站表下一工序是否必过工序
            if (!dto.getNowProcessId().equals(mesSfcBarcodeProcess.getNextProcessId())) {
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> mesSfcBarcodeProcess.getNextProcessId().equals(i.getProcessId()))
                        .findFirst();
                if (!routeProcessOptional.isPresent()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012009, mesSfcBarcodeProcess.getNextProcessName());
                }
                BaseRouteProcess routeProcess = routeProcessOptional.get();
                if (routeProcess.getIsMustPass() == 1) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012010, mesSfcBarcodeProcess.getNextProcessName());
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

        long one = System.currentTimeMillis();
        log.info("============== 判断当前工序one:"+ (one - one1));
        log.info("============== 判断当前工序:"+ (one - start));

        BaseProcess baseProcess = processResponseEntity.getData();
        // 更新当前工序
        mesSfcBarcodeProcess.setProcessId(dto.getNowProcessId());
        mesSfcBarcodeProcess.setProcessCode(baseProcess.getProcessCode());
        mesSfcBarcodeProcess.setProcessName(baseProcess.getProcessName());
        // 更新工位、工段、产线
        if(StringUtils.isNotEmpty(dto.getNowStationId())) {
            BaseStation baseStation = barcodeUtils.baseFeignApi.findStationDetail(dto.getNowStationId()).getData();
            if (baseStation == null) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "没有找到对应的工位");
            }
            mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
            mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
            mesSfcBarcodeProcess.setStationName(baseStation.getStationName());
        }
        long two = System.currentTimeMillis();
        log.info("============== 更新工位:"+ (two - one));
        //从工段表 base_workshop_section 取出工段编码和工段名称 2021-08-02 huangshuijun
        ResponseEntity<BaseWorkshopSection> bwssResponseEntity = barcodeUtils.baseFeignApi.sectionDetail(baseProcess.getSectionId());
        BaseWorkshopSection baseWorkshopSection = bwssResponseEntity.getData();
        if(baseWorkshopSection == null)
            throw new BizErrorException(ErrorCodeEnum.PDA40012035);

        mesSfcBarcodeProcess.setSectionId(baseProcess.getSectionId());//工段id
        mesSfcBarcodeProcess.setSectionCode(baseWorkshopSection.getSectionCode());//工段code
        mesSfcBarcodeProcess.setSectionName(baseWorkshopSection.getSectionName());//工段名称
        long two1 = System.currentTimeMillis();
        log.info("============== 取出工段编码和工段名称:"+ (two1 - two));
        BaseProLine baseProLine = barcodeUtils.baseFeignApi.getProLineDetail(dto.getProLineId()).getData();
        if (baseProLine == null) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003, "该产线不存在或已被删除");
        }
        mesSfcBarcodeProcess.setProLineId(baseProLine.getProLineId());
        mesSfcBarcodeProcess.setProCode(baseProLine.getProCode());
        mesSfcBarcodeProcess.setProName(baseProLine.getProName());

        long two2 = System.currentTimeMillis();
        log.info("============== 产线:"+ (two2 - two1));


        //过站次数累加注释
        //条码在当前工序有几条过站记录 记录工序过站次数开始 2021-10-18
        Map<String, Object> mapExist = new HashMap<>();
        mapExist.put("barcode", dto.getBarCode());
        mapExist.put("stationId", dto.getNowStationId());
        mapExist.put("processId", dto.getNowProcessId());
        List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(mapExist);
        mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcessRecordDtoList.size()+1);
        //条码在当前工序有几条过站记录 记录工序过站次数结束 2021-10-18

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

            long three = System.currentTimeMillis();
            log.info("============== 返工单:"+ (three - two));
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

        long three = System.currentTimeMillis();
        log.info("============== 设置next工序:"+ (three - two));

        //更新条码状态 产品条码状态(0-NG 1-OK)
        if(StringUtils.isNotEmpty(dto.getOpResult()) && "NG".equals(dto.getOpResult()))
            mesSfcBarcodeProcess.setBarcodeStatus((byte)0);
        else if(StringUtils.isNotEmpty(dto.getOpResult()) && "OK".equals(dto.getOpResult()))
            mesSfcBarcodeProcess.setBarcodeStatus((byte)1);

        //客户条码
        if (StringUtils.isNotEmpty(dto.getCustomerBarcode())){
            mesSfcBarcodeProcess.setCustomerBarcode(dto.getCustomerBarcode());
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
        //雷赛增加过站耗时记录 用预留栏位 option1 记录
        mesSfcBarcodeProcessRecord.setOption1(dto.getPassTime());
        barcodeUtils.mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);

        long four = System.currentTimeMillis();
        log.info("============== 增加过站记录:"+ (four - three));

        /**
         * 日期：20211109
         * 条码状态过站变更
         */
        MesSfcWorkOrderBarcode sfcWorkOrderBarcode = barcodeUtils.mesSfcWorkOrderBarcodeService.selectByKey(mesSfcBarcodeProcess.getWorkOrderBarcodeId());

        // 是否投产工序且是该条码在工单工序第一次过站，工单投产数量 +1 mesSfcBarcodeProcessRecordDtoList.isEmpty()
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getPutIntoProcessId())) {
            if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getNowProcessId()) && mesSfcBarcodeProcessRecordDtoList.size()==0) {

                /**
                 * 20211215 bgkun
                 * 如果有附件码，变更销售订单条码状态
                 */
                Map<String, Object> map = new HashMap<>();
                map.put("workOrderBarcodeId", sfcWorkOrderBarcode.getWorkOrderBarcodeId());
                List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = barcodeUtils.mesSfcKeyPartRelevanceService.findList(map);
                if (!keyPartRelevanceDtos.isEmpty() && keyPartRelevanceDtos.size() >0){
                    List<MesSfcWorkOrderBarcode> barcodes = new ArrayList<>();
                    for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos){
                        if (keyPartRelevanceDto.getPartBarcode() != null && mesSfcBarcodeProcess.getCustomerBarcode() == null){
                            SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
                            searchMesSfcWorkOrderBarcode.setBarcode(keyPartRelevanceDto.getPartBarcode());
                            List<MesSfcWorkOrderBarcodeDto> barcodeDtos = barcodeUtils.mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
                            if (!barcodeDtos.isEmpty()){
                                MesSfcWorkOrderBarcode barcode = new MesSfcWorkOrderBarcode();
                                barcode.setWorkOrderBarcodeId(barcodeDtos.get(0).getWorkOrderBarcodeId());
                                barcode.setBarcodeStatus((byte) 1);
                                barcodes.add(barcode);
                            }
                        }
                    }
                    if (barcodes.size() > 0){
                        barcodeUtils.mesSfcWorkOrderBarcodeService.batchUpdate(barcodes);
                    }
                }

                mesPmWorkOrder.setProductionQty(mesPmWorkOrder.getProductionQty() != null ? mesPmWorkOrder.getProductionQty().add(BigDecimal.ONE) : BigDecimal.ONE);
                // 若是投产工序，则判断是否首条码，若是则更新工单状态为生产中
                if (mesPmWorkOrder.getWorkOrderStatus() == (byte) 1) {
                    mesPmWorkOrder.setWorkOrderStatus((byte) 3);
                    mesPmWorkOrder.setActualStartTime(new Date());
                }
                barcodeUtils.pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);

                /**
                 * 日期：20211109
                 * 更新条码状态  条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
                 */
                sfcWorkOrderBarcode.setBarcodeStatus((byte) 1);
                barcodeUtils.mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
            }
        }

        long five = System.currentTimeMillis();
        log.info("============== 投产工序:"+ (five - four));

        // 判断当前工序是否为产出工序，且是该条码在工单工序第一次过站，工单产出 +1
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getOutputProcessId())) {
            if (dto.getNowProcessId().equals(mesPmWorkOrder.getOutputProcessId()) && mesSfcBarcodeProcessRecordDtoList.size()==0) {
                mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty() != null ? BigDecimal.ONE.add(mesPmWorkOrder.getOutputQty()) : BigDecimal.ONE);
                if (mesPmWorkOrder.getOutputQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 0) {
                    // 产出数量等于工单数量，工单完工
                    mesPmWorkOrder.setWorkOrderStatus((byte) 6);
                    mesPmWorkOrder.setActualEndTime(new Date());
                    mesPmWorkOrder.setModifiedTime(new Date());
                    mesPmWorkOrder.setModifiedUserId(dto.getOperatorUserId());
                }
                barcodeUtils.pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);
                long six = System.currentTimeMillis();
                log.info("============== 变更工单:"+ (six - five));
                /**
                 * 日期：20211109
                 * 更新条码状态  条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
                 */
                sfcWorkOrderBarcode.setBarcodeStatus((byte) 2);
                barcodeUtils.mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
                long six1 = System.currentTimeMillis();
                log.info("============== 变更条码:"+ (six1 - six));

                /**
                 * 20211215 bgkun
                 * 如果有附件码，变更销售订单条码状态
                 */
                Map<String, Object> map = new HashMap<>();
                map.put("workOrderBarcodeId", sfcWorkOrderBarcode.getWorkOrderBarcodeId());
                List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = barcodeUtils.mesSfcKeyPartRelevanceService.findList(map);
                if (!keyPartRelevanceDtos.isEmpty() && keyPartRelevanceDtos.size() >0){
                    List<MesSfcWorkOrderBarcode> barcodes = new ArrayList<>();
                    for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos){
                        if (keyPartRelevanceDto.getPartBarcode() != null){
                            SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
                            searchMesSfcWorkOrderBarcode.setBarcode(keyPartRelevanceDto.getPartBarcode());
                            List<MesSfcWorkOrderBarcodeDto> barcodeDtos = barcodeUtils.mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
                            if (!barcodeDtos.isEmpty()){
                                MesSfcWorkOrderBarcode barcode = new MesSfcWorkOrderBarcode();
                                barcode.setWorkOrderBarcodeId(barcodeDtos.get(0).getWorkOrderBarcodeId());
                                barcode.setBarcodeStatus((byte) 2);
                                barcodes.add(barcode);
                            }
                        }
                    }
                    if (barcodes.size() > 0){
                        barcodeUtils.mesSfcWorkOrderBarcodeService.batchUpdate(barcodes);
                    }
                }
                long six2 = System.currentTimeMillis();
                log.info("============== 变更条码:"+ (six2 - six1));
            }
        }

        long six = System.currentTimeMillis();
        log.info("============== 产出工序:"+ (six - five));
        log.info("============== 总耗时:"+ (six - start));
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
        if(StringUtils.isEmpty(labelRuteDto)) {
            throw new BizErrorException("未找到物料标签信息或工单未设置条码规则集合");
        }

        PrintModel printModel = barcodeUtils.mesSfcWorkOrderBarcodeMapper.findPrintModel(ControllerUtil.dynamicCondition("labelCode",labelRuteDto.getLabelCode(),"id",dto.getWorkOrderId()));
        if(StringUtils.isEmpty(printModel)) {
            printModel = new PrintModel();
        }

        printModel.setPackingQty(dto.getPackingQty());
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
            throw new BizErrorException(ErrorCodeEnum.PDA40012004, mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 ? "已完成" : "待打印");
        }
        return mesSfcWorkOrderBarcodeDto;
    }

    /**
     * 判断条码流程
     *
     * @param mesSfcWorkOrderBarcodeDto 条码DTO
     * @param processId                 流程ID
     * @param routeId                   工艺路线ID
     */
    private static void checkBarcodeProcess(MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto, Long processId, Long routeId) {
        if(StringUtils.isNotEmpty(mesSfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())) {
            MesSfcBarcodeProcess mesSfcBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .workOrderBarcodeId(mesSfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())
                    .build());
            // 判断是否有必过工序未过站 开始

            //获取配置项 雷赛包装工序不判断条码状态  PackingIfCheckBarcodeStatus
            String paraValue = getSysSpecItemValue("PackingIfCheckBarcodeStatus");
            boolean isPackingProcess = false;
            if ("1".equals(paraValue)) {
                //判断当前工序是否是包装工序
                BaseProcess bProcss = null;
                ResponseEntity<BaseProcess> reEntityBP = barcodeUtils.baseFeignApi.processDetail(processId);
                if (StringUtils.isNotEmpty(reEntityBP)) {
                    bProcss = reEntityBP.getData();
                }
                if (StringUtils.isNotEmpty(bProcss)) {
                    long processCategoryId = bProcss.getProcessCategoryId();
                    ResponseEntity<BaseProcessCategory> processCategoryEntity = barcodeUtils.baseFeignApi.processCategoryDetail(processCategoryId);
                    if (processCategoryEntity.getCode() != 0) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "工序类别信息不存在");
                    }
                    BaseProcessCategory baseProcessCategory = processCategoryEntity.getData();
                    if ("PACKING".equals(baseProcessCategory.getProcessCategoryCode()) == true) {
                        isPackingProcess = true;
                    }
                }
            }
            if (isPackingProcess == false) {
                ResponseEntity<List<BaseRouteProcess>> responseEntity = barcodeUtils.baseFeignApi.findConfigureRout(routeId);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012008);
                }
                List<BaseRouteProcess> routeProcessList = responseEntity.getData();
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> processId.equals(i.getProcessId()))
                        .findFirst();
                if (!routeProcessOptional.isPresent()) {
                    ResponseEntity<BaseProcess> nowProcess = barcodeUtils.baseFeignApi.processDetail(processId);
                    if (StringUtils.isEmpty(nowProcess)) {
                        throw new BizErrorException(ErrorCodeEnum.PDA40012012, processId);
                    }
                    throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "工单工艺路线中不存在此工序-->" + " 工序编码:" + nowProcess.getData().getProcessCode() + " 工序名称:" + nowProcess.getData().getProcessName());
                }

                BaseRouteProcess routeProcess = routeProcessOptional.get();
                //判断传参工序是否为非必过工序
                if (routeProcess.getIsMustPass() == 0) {
                    return;
                }

                int num = routeProcess.getOrderNum();//当前工序序号
                Optional<BaseRouteProcess> routeProcessOptionalLast = routeProcessList.stream()
                        .filter(i -> i.getOrderNum() < num && i.getIsPass() == 1 && i.getIsMustPass() == 1)
                        .sorted(Comparator.comparing(BaseRouteProcess::getOrderNum).reversed())
                        .findFirst();
                if (routeProcessOptionalLast.isPresent()) {
                    BaseRouteProcess routeProcessLast = routeProcessOptionalLast.get();
                    Long lastProcessId = routeProcessLast.getProcessId();
                    Map<String, Object> mapExist = new HashMap<>();
                    mapExist.put("barcode", mesSfcBarcodeProcess.getBarcode());
                    mapExist.put("processId", lastProcessId);
                    List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(mapExist);

                    Map<String, Object> mapExistPart = new HashMap<>();
                    mapExistPart.put("barcode", mesSfcBarcodeProcess.getCustomerBarcode());
                    mapExistPart.put("processId", lastProcessId);
                    List<MesSfcBarcodeProcessRecordDto> partRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(mapExistPart);

                    if (mesSfcBarcodeProcessRecordDtoList.size() <= 0 && partRecordDtoList.size() <= 0) {
                        BaseProcess baseProcess = barcodeUtils.baseFeignApi.processDetail(lastProcessId).getData();
                        throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "必过工序未过站-->" + baseProcess.getProcessName());
                    }
                }
            }

            // 判断是否有必过工序未过站 结束

            if (mesSfcBarcodeProcess != null) {
                Long existID = processId;
                if (!processId.equals(mesSfcBarcodeProcess.getNextProcessId()) && StringUtils.isNotEmpty(mesSfcBarcodeProcess.getNextProcessCode())) {
//                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
////                        .filter(i -> existID.equals(i.getProcessId()))
////                        .findFirst();
////                if (!routeProcessOptional.isPresent()) {
////                    throw new BizErrorException(ErrorCodeEnum.PDA40012009.getCode(), "工艺路线无此过站工序");
////                }
////                BaseRouteProcess routeProcess = routeProcessOptional.get();
////                if (routeProcess.getIsMustPass() == 0) {
////                    throw new BizErrorException(ErrorCodeEnum.PDA40012010.getCode(), "此工序为非必过工序");
////                }
////                else {
////                    BaseProcess baseProcess = barcodeUtils.baseFeignApi.processDetail(processId).getData();
////                    throw new BizErrorException(ErrorCodeEnum.PDA40012003, baseProcess.getProcessName(), mesSfcBarcodeProcess.getNextProcessName());
////                }

                    BaseProcess baseProcess = barcodeUtils.baseFeignApi.processDetail(processId).getData();
                    throw new BizErrorException(ErrorCodeEnum.PDA40012003, baseProcess.getProcessName(), mesSfcBarcodeProcess.getNextProcessName());

                }
//            if (!mesSfcBarcodeProcess.getProLineId().equals(proLineId)){
//                throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "该产品条码产线跟该工位产线不匹配");
//            }
                // 已完成所有过站工序
                if (mesSfcBarcodeProcess.getNextProcessId().equals(0L)) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "该产品条码已完成所有工序过站");
                }
                //是否已不良
//            if (StringUtils.isNotEmpty(mesSfcBarcodeProcess.getBarcodeStatus()) && mesSfcBarcodeProcess.getBarcodeStatus().equals((byte)0)){
//                throw new BizErrorException("该产品条码已不良 不可继续");
//            }
            } else {
                throw new BizErrorException(ErrorCodeEnum.PDA40012002, mesSfcWorkOrderBarcodeDto.getBarcode());
            }
        }
    }

    public static int updateBarcodeProcess(UpdateProcessDto dto) throws Exception {
        String passTimeIsOK="0";
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
//            if (!dto.getNowProcessId().equals(mesSfcBarcodeProcess.getNextProcessId())) {
//                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
//                        .filter(i -> mesSfcBarcodeProcess.getNextProcessId().equals(i.getProcessId()))
//                        .findFirst();
//                if (!routeProcessOptional.isPresent()) {
//                    throw new BizErrorException(ErrorCodeEnum.PDA40012009, mesSfcBarcodeProcess.getNextProcessName());
//                }
//                BaseRouteProcess routeProcess = routeProcessOptional.get();
//                if (routeProcess.getIsMustPass() == 1) {
//                    throw new BizErrorException(ErrorCodeEnum.PDA40012010, mesSfcBarcodeProcess.getNextProcessName());
//                }
//            }
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
        if(StringUtils.isNotEmpty(dto.getNowStationId())) {
            BaseStation baseStation = barcodeUtils.baseFeignApi.findStationDetail(dto.getNowStationId()).getData();
            if (baseStation == null) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "没有找到对应的工位");
            }
            mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
            mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
            mesSfcBarcodeProcess.setStationName(baseStation.getStationName());
        }
        //从工段表 base_workshop_section 取出工段编码和工段名称 2021-08-02 huangshuijun
        ResponseEntity<BaseWorkshopSection> bwssResponseEntity = barcodeUtils.baseFeignApi.sectionDetail(baseProcess.getSectionId());
        BaseWorkshopSection baseWorkshopSection = bwssResponseEntity.getData();
        if(baseWorkshopSection == null)
            throw new BizErrorException(ErrorCodeEnum.PDA40012035);

//        mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
//        mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
//        mesSfcBarcodeProcess.setStationName(baseStation.getStationName());

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

        //过站次数累加注释
        //mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcess.getPassStationCount() != null ? mesSfcBarcodeProcess.getPassStationCount() + 1 : 1);
        //条码在当前工序有几条过站记录 记录工序过站次数开始 2021-10-18
        Map<String, Object> mapExist = new HashMap<>();
        mapExist.put("barcode", dto.getBarCode());
        //mapExist.put("stationId", dto.getNowStationId());
        mapExist.put("processId", dto.getNowProcessId());
        List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(mapExist);
        mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcessRecordDtoList.size()+1);
        //条码在当前工序有几条过站记录 记录工序过站次数结束 2021-10-18

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
            // mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())
            if (dto.getNowProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
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
                    ResponseEntity<BaseProcess> nowProcess=barcodeUtils.baseFeignApi.processDetail(dto.getNowProcessId());
                    if(StringUtils.isEmpty(nowProcess)){
                        throw new BizErrorException(ErrorCodeEnum.PDA40012012, dto.getNowProcessId());
                    }
                    throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "工单工艺路线中不存在此工序-->"+" 工序编码:"+nowProcess.getData().getProcessCode()+" 工序名称:"+nowProcess.getData().getProcessName());
                }

                BaseRouteProcess routeProcess = routeProcessOptional.get();
                //String standardTimeS=routeProcess.getStandardTime().toString();
                Long materialId=mesPmWorkOrder.getMaterialId();
                SearchBaseTab searchBaseTab=new SearchBaseTab();
                searchBaseTab.setMaterialId(materialId);
                searchBaseTab.setOrgId(mesPmWorkOrder.getOrgId());
                ResponseEntity<List<BaseTabDto>> listResponseEntity=barcodeUtils.baseFeignApi.findTabList(searchBaseTab);
                String takt="0";
                if(StringUtils.isNotEmpty(listResponseEntity.getData())){
                    takt=listResponseEntity.getData().get(0).getTakt().toString();
                }
                if(StringUtils.isEmpty(takt))
                    takt="0";
                Double standardTimeD=Double.parseDouble(takt);
                try{
                    Double passTimeD=Double.parseDouble(StringUtils.isEmpty(dto.getPassTime())?"0":dto.getPassTime());
                    if(passTimeD>standardTimeD)
                        passTimeIsOK="1";

                }catch (Exception ex){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"耗时转为数字异常");
                }
                processResponseEntity = barcodeUtils.baseFeignApi.processDetail(routeProcess.getNextProcessId());
                if (processResponseEntity.getCode() != 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcess.getNextProcessId());
                }
                baseProcess = processResponseEntity.getData();

                //判断当前工序是否是维修工序
                boolean isRepairProcess=false;
                BaseProcess bProcss=null;
                ResponseEntity<BaseProcess> reEntityBP = barcodeUtils.baseFeignApi.processDetail(dto.getNowProcessId());
                if(StringUtils.isNotEmpty(reEntityBP)){
                    bProcss=reEntityBP.getData();
                }
                if(StringUtils.isNotEmpty(bProcss)) {
                    long processCategoryId = bProcss.getProcessCategoryId();
                    ResponseEntity<BaseProcessCategory> processCategoryEntity = barcodeUtils.baseFeignApi.processCategoryDetail(processCategoryId);
                    if (processCategoryEntity.getCode() != 0) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "工序类别信息不存在");
                    }
                    BaseProcessCategory baseProcessCategory = processCategoryEntity.getData();
                    if ("repair".equals(baseProcessCategory.getProcessCategoryCode()) == true) {
                        isRepairProcess=true;
                        if(StringUtils.isNotEmpty(dto.getOpResult()) && "NG".equals(dto.getOpResult())){
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "维修NG 请走报废流程");
                        }
                        else {
                            //设置返修工序
                            mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                            mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                            mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
                        }
                    }
                }

                //判断作业结果 1 如果是NG 则判断NG工序是否已设置维修工序 维修工序由工序类别编码 repair 判断 没有设置报错
                // 2 如果是OK 则先找当前工序序号  再找比当前工序序号大的下一个工序
                if(isRepairProcess==false) {
                    if (StringUtils.isNotEmpty(dto.getOpResult()) && "NG".equals(dto.getOpResult())) {
                        /*long processCategoryId = baseProcess.getProcessCategoryId();
                        ResponseEntity<BaseProcessCategory> processCategoryEntity = barcodeUtils.baseFeignApi.processCategoryDetail(processCategoryId);
                        if (processCategoryEntity.getCode() != 0) {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "工序类别信息不存在");
                        }
                        BaseProcessCategory baseProcessCategory = processCategoryEntity.getData();
                        if ("repair".equals(baseProcessCategory.getProcessCategoryCode()) == false) {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "工序不良时 未设置工序的维修工序");
                        }

                        mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                        mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                        mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());*/

                        int num = routeProcess.getOrderNum();
                        Optional<BaseRouteProcess> routeProcessOptionalNext = routeProcessList.stream()
                                .filter(i -> i.getOrderNum() > num && i.getIsPass() == 1 && i.getIsMustPass()==1)
                                .findFirst();
                        if (!routeProcessOptionalNext.isPresent()) {
                            throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "数据错误 未设置下一工序");
                        }

                        BaseRouteProcess routeProcessNext = routeProcessOptionalNext.get();
                        processResponseEntity = barcodeUtils.baseFeignApi.processDetail(routeProcessNext.getProcessId());
                        if (processResponseEntity.getCode() != 0) {
                            throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcessNext.getProcessId());
                        }
                        baseProcess = processResponseEntity.getData();
                        mesSfcBarcodeProcess.setNextProcessId(routeProcessNext.getProcessId());
                        mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                        mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());


                    } else if (StringUtils.isNotEmpty(dto.getOpResult()) && "OK".equals(dto.getOpResult())) {
                        int num = routeProcess.getOrderNum();
                        Optional<BaseRouteProcess> routeProcessOptionalNext = routeProcessList.stream()
                                .filter(i -> i.getOrderNum() > num && i.getIsPass() == 1 && i.getIsMustPass()==1)
                                .findFirst();
                        if (!routeProcessOptionalNext.isPresent()) {
                            throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "数据错误 未设置下一工序");
                        }

                        BaseRouteProcess routeProcessNext = routeProcessOptionalNext.get();
                        processResponseEntity = barcodeUtils.baseFeignApi.processDetail(routeProcessNext.getProcessId());
                        if (processResponseEntity.getCode() != 0) {
                            throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcessNext.getProcessId());
                        }
                        baseProcess = processResponseEntity.getData();
                        mesSfcBarcodeProcess.setNextProcessId(routeProcessNext.getProcessId());
                        mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                        mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
                    }
                }

                // 设置下一工序
                //mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                //mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                //mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
            }
        }

        //更新条码状态 产品条码状态(0-NG 1-OK)
        if(StringUtils.isNotEmpty(dto.getOpResult()) && "NG".equals(dto.getOpResult()))
            mesSfcBarcodeProcess.setBarcodeStatus((byte)0);
        else if(StringUtils.isNotEmpty(dto.getOpResult()) && "OK".equals(dto.getOpResult()))
            mesSfcBarcodeProcess.setBarcodeStatus((byte)1);

        //客户条码
        if (StringUtils.isNotEmpty(dto.getCustomerBarcode())){
            mesSfcBarcodeProcess.setCustomerBarcode(dto.getCustomerBarcode());
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
        //雷赛增加过站耗时记录 用预留栏位 option1 记录
        mesSfcBarcodeProcessRecord.setOption1(dto.getPassTime());

        //雷赛增加工序标准时间与设备耗时比较 option2 记录 passTimeIsOK 0 正常 1 超时
        mesSfcBarcodeProcessRecord.setOption2(passTimeIsOK);

        //增加保存设备条码 用预留栏位 option3 记录
        if(StringUtils.isNotEmpty(dto.getEquipmentCode())){
            mesSfcBarcodeProcessRecord.setOption3(dto.getEquipmentCode());
        }

        barcodeUtils.mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);

        /**
         * 日期：20211109
         * 条码状态过站变更
         */
        MesSfcWorkOrderBarcode sfcWorkOrderBarcode = barcodeUtils.mesSfcWorkOrderBarcodeService.selectByKey(mesSfcBarcodeProcess.getWorkOrderBarcodeId());


//        Map<String, Object> map = new HashMap<>();
//        map.put("workOrderBarcodeId", dto.getBarCode());
//        map.put("stationId", dto.getNowStationId());
//        map.put("processId", dto.getNowProcessId());
//        List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(map);
        // 是否投产工序且是该条码在工单工序第一次过站，工单投产数量 +1 mesSfcBarcodeProcessRecordDtoList.isEmpty()
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getPutIntoProcessId())) {
            if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getNowProcessId()) && mesSfcBarcodeProcessRecordDtoList.size()==0) {
                //mesPmWorkOrder.setProductionQty(mesPmWorkOrder.getProductionQty().add(BigDecimal.ONE));
                mesPmWorkOrder.setProductionQty(mesPmWorkOrder.getProductionQty() != null ? mesPmWorkOrder.getProductionQty().add(BigDecimal.ONE) : BigDecimal.ONE);
                // 若是投产工序，则判断是否首条码，若是则更新工单状态为生产中
                if (mesPmWorkOrder.getWorkOrderStatus() == (byte) 1) {
                    mesPmWorkOrder.setWorkOrderStatus((byte) 3);
                }
                // huangshuijun 注释此 updateSmtWorkOrder 方法原因：1 过站方式如果是设备调用 获取不了当前用户
                // 2 调用此方法会把工单BOM删除 大忌
                //barcodeUtils.pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
                barcodeUtils.pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);

                /**
                 * 日期：20211109
                 * 更新条码状态  条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
                 */
                sfcWorkOrderBarcode.setBarcodeStatus((byte) 1);
                barcodeUtils.mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
            }
        }
        // 判断当前工序是否为产出工序，且是该条码在工单工序第一次过站，工单产出 +1
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getOutputProcessId())) {
            if (dto.getNowProcessId().equals(mesPmWorkOrder.getOutputProcessId()) && mesSfcBarcodeProcessRecordDtoList.size()==0) {
                //mesPmWorkOrder.setOutputQty(BigDecimal.ONE.add(mesPmWorkOrder.getOutputQty()));
                mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty() != null ? BigDecimal.ONE.add(mesPmWorkOrder.getOutputQty()) : BigDecimal.ONE);
                if (mesPmWorkOrder.getOutputQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 0) {
                    // 产出数量等于工单数量，工单完工
                    mesPmWorkOrder.setWorkOrderStatus((byte) 6);
                    mesPmWorkOrder.setActualEndTime(new Date());
                    mesPmWorkOrder.setModifiedTime(new Date());
                    mesPmWorkOrder.setModifiedUserId(dto.getOperatorUserId());
                }
                // huangshuijun 注释此 updateSmtWorkOrder 方法原因：1 过站方式如果是设备调用 获取不了当前用户
                // 2 调用此方法会把工单BOM删除 大忌
                //barcodeUtils.pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
                barcodeUtils.pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);

                /**
                 * 日期：20211109
                 * 更新条码状态  条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
                 */
                sfcWorkOrderBarcode.setBarcodeStatus((byte) 2);
                barcodeUtils.mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
            }
        }
        return 1;
    }

    /**
     * 检查工单
     *
     * @param mesSfcWorkOrderBarcodeDto 条码DTO
     */
    private static void checkOrder(MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto, Long proLineId) {
        //判断工单是否存在
        ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntity = barcodeUtils.pmFeignApi.workOrderDetail(mesSfcWorkOrderBarcodeDto.getWorkOrderId());
        if (pmWorkOrderResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012005, mesSfcWorkOrderBarcodeDto.getWorkOrderCode());
        }
        /*
        * 工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)
        */
        MesPmWorkOrder mesPmWorkOrder = pmWorkOrderResponseEntity.getData();
        if (4 == mesPmWorkOrder.getWorkOrderStatus() || 5 == mesPmWorkOrder.getWorkOrderStatus()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012006);
        }
        if (mesPmWorkOrder.getProductionQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012007, mesPmWorkOrder.getWorkOrderCode());
        }
        if (!mesPmWorkOrder.getProLineId().equals(proLineId)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012007.getCode(), "作业配置的产线与工单上的产线不符，不可操作");
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
        byte result=1;//调用结果(0-失败 1-成功)
        Long orgId = null;
        long start=0;
        long end=0;
        String requestTimeS="";
        String responseTimeS="";
        try {
            start = System.currentTimeMillis();//获取毫秒数
            requestTimeS=DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN);

            String pass = "Pass";
            String fail = "Fail";
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
            ResponseEntity responseEntityResult=barcodeUtils.deviceInterFaceUtils.checkLogin(restapiChkLogUserInfoApiDto.getUserCode(), restapiChkLogUserInfoApiDto.getPassword(),orgId);
            if(responseEntityResult.getCode()>0)
                throw new Exception(responseEntityResult.getMessage());
            Long userId=0L;
            Map<String,Object> map=(Map<String,Object>)responseEntityResult.getData();
            if(map.containsKey("userName"))
                userName=map.get("userName").toString();
            if(map.containsKey("userId"))
                userId=Long.valueOf(map.get("userId").toString());

            //String result=responseEntityResult.getData().toString();


//            String strName="";
//            String[] str=result.split(",");
//            for (String s : str) {
//                if(s.indexOf("userName")>0){
//                    strName=s;
//                    break;
//                }
//            }
//
//            userName=strName.substring(strName.indexOf("=")+1);

//            ResponseEntity<List<SysUser>> sysUserlist = barcodeUtils.deviceInterFaceUtils.getSysUser(restapiChkLogUserInfoApiDto.getUserCode(), orgId);
//            if (StringUtils.isEmpty(sysUserlist.getData())) {
//                throw new Exception(fail + " 请求失败,登录用户帐号不存在");
//            } else {
//                SysUser sysUser = sysUserlist.getData().get(0);
//                Boolean isOK = new BCryptPasswordEncoder().matches(restapiChkLogUserInfoApiDto.getPassword(), sysUser.getPassword());
//                if (!isOK) {
//                    throw new Exception(fail + " 请求失败,登录用户密码不正确");
//                }
//
//                //用户权限判断 判断是否有工序权限 只到菜单权限
//                boolean haveAuth=false;
//                ResponseEntity<List<SysUserRole>> reSysUserRoleList=barcodeUtils.deviceInterFaceUtils.findUserRoleList(userId);
//                if(StringUtils.isEmpty(reSysUserRoleList)){
//                    throw new Exception(fail + " 请求失败,登录用户没有设置权限");
//                }
//                else {
//                    List<SysUserRole> sysUserRoleList=reSysUserRoleList.getData();
//                    for (SysUserRole sysUserRole : sysUserRoleList) {
//                        Long menuId=478L;//工序菜单id
//                        Long roleId=sysUserRole.getRoleId();
//                        ResponseEntity<SysAuthRole> reSysAuthRole=barcodeUtils.deviceInterFaceUtils.getSysAuthRole(roleId,menuId);
//                        if(StringUtils.isNotEmpty(reSysAuthRole)){
//                            haveAuth=true;
//                            break;
//                        }
//                    }
//                }
//                if(haveAuth==false){
//                    throw new Exception(fail + " 请求失败,登录用户没有工序权限");
//                }
//                userName = sysUser.getUserName();
//            }

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg(pass + ",验证通过,产线名称:" + proName + ",工序名称:" + processName + ",用户名称:" + userName);
        }
        catch (Exception ex){
            result=0;
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录请求参数及结果
        end = System.currentTimeMillis();//获取毫秒数
        BigDecimal consumeTime=new BigDecimal((end-start)/1000);//耗时 秒
        responseTimeS=DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN);//响应时间

        barcodeUtils.deviceInterFaceUtils.addLog(result, (byte) 2, orgId, baseExecuteResultDto.getIsSuccess()?baseExecuteResultDto.getSuccessMsg():baseExecuteResultDto.getFailMsg(),
                restapiChkLogUserInfoApiDto.toString(),consumeTime,requestTimeS,responseTimeS);

        return baseExecuteResultDto;
    }

    public static BaseExecuteResultDto RepairDataTransfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        UpdateProcessDto updateProcessDto=new UpdateProcessDto();
        byte result=1;//调用结果(0-失败 1-成功)
        Long orgId=0L;
        try {

            //参数基础信息判
            ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=barcodeUtils.deviceInterFaceUtils.getOrId();
            if(StringUtils.isNotEmpty(baseOrganizationDtoList.getData())){
                //throw new Exception("未查询到对应组织");
                orgId=baseOrganizationDtoList.getData().get(0).getOrganizationId();
            }

            //设置组织ID
            updateProcessDto.setOrgId(orgId);

            //产线处理
            String proCode=restapiSNDataTransferApiDto.getProCode();
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

            //工序处理
            String processCode=restapiSNDataTransferApiDto.getProcessCode();
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

            //作业结果处理 过站必传
            String opResult=restapiSNDataTransferApiDto.getOpResult();
            if(StringUtils.isEmpty(opResult)){
                throw new Exception("作业结果不能为空");
            }
            else if(StringUtils.isNotEmpty(opResult)){
                if("OK".equals(opResult)==false && "NG".equals(opResult)==false){
                    throw new Exception("作业结果请传 OK 或 NG");
                }
            }

            //条码处理
            String partBarcode=restapiSNDataTransferApiDto.getPartBarcode();
            String barcodeCode=restapiSNDataTransferApiDto.getBarCode();
            if(StringUtils.isEmpty(partBarcode) && StringUtils.isEmpty(barcodeCode)){
                throw new Exception("半成品SN和成品SN不能同时为空");
            }

            //工单号处理
            String workOrderCode=restapiSNDataTransferApiDto.getWorkOrderCode();
            if(StringUtils.isNotEmpty(partBarcode) && StringUtils.isEmpty(workOrderCode)){
                throw new Exception("成品工单号不能为空");
            }

            MesPmWorkOrderDto mesPmWorkOrderDto=null;
            if(StringUtils.isNotEmpty(workOrderCode)){
                SearchMesPmWorkOrder searchMesPmWorkOrder=new SearchMesPmWorkOrder();
                searchMesPmWorkOrder.setOrgId(orgId);
                searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
                ResponseEntity<List<MesPmWorkOrderDto>> workOrderlist=barcodeUtils.pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
                if(StringUtils.isNotEmpty(workOrderlist.getData())){
                    mesPmWorkOrderDto=workOrderlist.getData().get(0);

                    //设置工单ID
                    updateProcessDto.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                    //设置工单编号
                    updateProcessDto.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
                    //设置工单条码ID
                    //updateProcessDto.setWorkOrderBarcodeId(mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId());
                    //设置产品物料ID
                    updateProcessDto.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                    //设置流程ID
                    updateProcessDto.setRouteId(mesPmWorkOrderDto.getRouteId());
                    //设置投产工序ID
                    updateProcessDto.setPutIntoProcessId(mesPmWorkOrderDto.getPutIntoProcessId());
                    //设置产出工序ID
                    updateProcessDto.setOutputProcessId(mesPmWorkOrderDto.getOutputProcessId());
                }

            }

            //检查半成品SN 不为空 检查是否已在条码表中 不存在则新增到条码表中
            if(StringUtils.isNotEmpty(partBarcode) && StringUtils.isNotEmpty(mesPmWorkOrderDto)){
                baseExecuteResultDto=createWorkOrderBarcode(partBarcode,"",mesPmWorkOrderDto,orgId);
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());
                else {
                    if(StringUtils.isNotEmpty(partBarcode) && StringUtils.isEmpty(barcodeCode)){
                        //设置工单条码ID
                        updateProcessDto.setWorkOrderBarcodeId((Long) baseExecuteResultDto.getExecuteResult());
                    }
                }
            }
            //成品SN,半成品SN都不为空 检查是否已在条码表中 不存在则新增到条码表中
            if(StringUtils.isNotEmpty(barcodeCode) && StringUtils.isNotEmpty(partBarcode) && StringUtils.isNotEmpty(mesPmWorkOrderDto)){
                baseExecuteResultDto=createWorkOrderBarcode(barcodeCode,partBarcode,mesPmWorkOrderDto,orgId);
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());
                else {
                    if(StringUtils.isNotEmpty(barcodeCode)){
                        //设置工单条码ID
                        updateProcessDto.setWorkOrderBarcodeId((Long) baseExecuteResultDto.getExecuteResult());
                    }
                }
            }

            //只传成品SN
            if(StringUtils.isNotEmpty(barcodeCode) && StringUtils.isEmpty(partBarcode)){
                List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtoList = barcodeUtils.deviceInterFaceUtils.getWorkOrderBarcode(barcodeCode, orgId);
                if(workOrderBarcodeDtoList.size()>0){
                    updateProcessDto.setWorkOrderBarcodeId(workOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId());
                }
            }

            //不良现象处理
            String badnessPhenotypeCode=restapiSNDataTransferApiDto.getBadnessPhenotypeCode();
            if (StringUtils.isNotEmpty(badnessPhenotypeCode)) {
                ResponseEntity<List<BaseBadnessPhenotypeDto>> baseBadnessPhenotypeDtoList = barcodeUtils.deviceInterFaceUtils.getBadnessPhenotype(badnessPhenotypeCode, orgId);
                if (StringUtils.isEmpty(baseBadnessPhenotypeDtoList.getData())) {
                    //设置不良现象代码
                    updateProcessDto.setBadnessPhenotypeCode(badnessPhenotypeCode);
                }
            }

            //设置操作人员
            updateProcessDto.setOperatorUserId(1L);

            //获取半成品物料ID
            Long partMaterialId=updateProcessDto.getPartMaterialId();

            //设置作业结果
            updateProcessDto.setOpResult(restapiSNDataTransferApiDto.getOpResult());

            //设置过站条码及客户条码
            String barCode=restapiSNDataTransferApiDto.getBarCode();//成品条码
            //设置过站条码
            updateProcessDto.setBarCode(barCode);
            if(StringUtils.isNotEmpty(partBarcode) && StringUtils.isNotEmpty(workOrderCode) && StringUtils.isEmpty(barCode)){
                updateProcessDto.setBarCode(partBarcode);
                updateProcessDto.setCustomerBarcode(partBarcode);
            }
            else if(StringUtils.isNotEmpty(partBarcode) && StringUtils.isNotEmpty(workOrderCode) && StringUtils.isNotEmpty(barCode)){
                updateProcessDto.setBarCode(barCode);
                updateProcessDto.setCustomerBarcode(partBarcode);
            }

            CheckProductionDto checkProductionDto=new CheckProductionDto();
            if(StringUtils.isNotEmpty(restapiSNDataTransferApiDto.getBarCode())){
                checkProductionDto.setBarCode(restapiSNDataTransferApiDto.getBarCode());
            }
            else if(StringUtils.isNotEmpty(restapiSNDataTransferApiDto.getPartBarcode())){
                checkProductionDto.setBarCode(restapiSNDataTransferApiDto.getPartBarcode());
            }

            //标准条码流程检查
            //获取配置项检查条码状态  EquipmentIfCheckBarcodeStatus
            String paraValue = getSysSpecItemValue("EquipmentIfCheckBarcodeStatus");
            if ("1".equals(paraValue)) {
                checkProductionDto.setWorkOrderId(updateProcessDto.getWorkOrderId());
                checkProductionDto.setProcessId(updateProcessDto.getNowProcessId());
                checkProductionDto.setProLineId(updateProcessDto.getProLineId());
                checkSN(checkProductionDto,updateProcessDto.getWorkOrderBarcodeId(),orgId);
            }

            //过站
            barcodeUtils.updateBarcodeProcess(updateProcessDto);

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg(" 过站成功 ");
        }catch (Exception ex) {
            result=0;
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录请求参数及结果
        BigDecimal consum=new BigDecimal(0);
        barcodeUtils.deviceInterFaceUtils.addLog(result, (byte) 2, orgId, baseExecuteResultDto.getIsSuccess()?baseExecuteResultDto.getSuccessMsg():baseExecuteResultDto.getFailMsg(),
                restapiSNDataTransferApiDto.toString(),consum,"","");
        return baseExecuteResultDto;
    }

    /*
    * 检查产品条码与半成品条码关系
    * productionSn 产品条码
    * halfProductionSn 半成品条码
    * processCode 工序编码
    * orgId 组织ID
    */

    public static BaseExecuteResultDto checkProHalfProRelation(String productionSn,String halfProductionSn,Long processId,Long workOrderId,Long materialId,Long partMaterialId,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            //获取配置项检查成品条码与半成品条码关系  ProductIfCheckHalfProductionRelation
            String paraValue = getSysSpecItemValue("ProductIfCheckHalfProductionRelation");
            if ("1".equals(paraValue)) {
                baseExecuteResultDto = checkProductionHalfProductionRelation(productionSn, halfProductionSn, processId, workOrderId,materialId,partMaterialId,orgId);
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

    public static BaseExecuteResultDto checkProductionHalfProductionRelation(String productionSn,String halfProductionSn,Long processId,Long workOrderId,Long materialId,Long partMaterialId,Long orgId) throws Exception{

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

            //检查产品条码
//            if(StringUtils.isNotEmpty(productionSn)) {
//                baseExecuteResultDto = checkBarcodeStatus(productionSn, orgId, "");
//                if (baseExecuteResultDto.getIsSuccess() == false)
//                    throw new Exception(baseExecuteResultDto.getFailMsg());
//            }

            //检查半成品条码
//            if(StringUtils.isNotEmpty(halfProductionSn)) {
//                baseExecuteResultDto = checkBarcodeStatus(halfProductionSn, orgId, "");
//                if (baseExecuteResultDto.getIsSuccess() == false) {
//                    throw new Exception(baseExecuteResultDto.getFailMsg());
//                }
//            }

            //展产品BOM找是否存在物料  如不存在 在工单BOM中找
            Boolean isExist=findBomExistMaterialId(materialId,partMaterialId,orgId);

            if(isExist==false) {
                SearchMesPmWorkOrderBom searchMesPmWorkOrderBom = new SearchMesPmWorkOrderBom();
                searchMesPmWorkOrderBom.setWorkOrderId(workOrderId);
                searchMesPmWorkOrderBom.setPartMaterialId(partMaterialId);
                //searchMesPmWorkOrderBom.setProcessId(processId);
                ResponseEntity<List<MesPmWorkOrderBomDto>> responseEntityBom = barcodeUtils.deviceInterFaceUtils.getWorkOrderBomList(searchMesPmWorkOrderBom);
                if (StringUtils.isEmpty(responseEntityBom.getData())) {

                    //半成品物料编号
                    String partMaterialCode="";
                    if(StringUtils.isNotEmpty(partMaterialId)) {
                        ResponseEntity<BaseMaterial> baseMaterialEntity = barcodeUtils.baseFeignApi.materialDetail(partMaterialId);
                        if(StringUtils.isEmpty(baseMaterialEntity.getData())){
                            throw new Exception("半成品条码相应物料ID找不到物料信息-->"+partMaterialId.toString());
                        }
                        partMaterialCode=baseMaterialEntity.getData().getMaterialCode();
                    }

                    //成品物料编号
                    String materialCode="";
                    if(StringUtils.isNotEmpty(materialId)) {
                        ResponseEntity<BaseMaterial> baseMaterialEntity = barcodeUtils.baseFeignApi.materialDetail(materialId);
                        if(StringUtils.isEmpty(baseMaterialEntity.getData())){
                            throw new Exception("成品条码相应物料ID找不到物料信息-->"+materialId.toString());
                        }
                        materialCode=baseMaterialEntity.getData().getMaterialCode();
                    }

                    //工单BOM找不到半成品信息
                    //通过配置项是否找产品BOM ProductBomCheckRelation
//                    String paraValue = getSysSpecItemValue("ProductBomCheckRelation");
//                    if ("1".equals(paraValue)) {
                    //产品BOM中是否存在物料
//                        SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
//                        searchBaseProductBom.setMaterialId(materialId);
//                        ResponseEntity<List<BaseProductBomDto>> responseEntityPB = barcodeUtils.deviceInterFaceUtils.getProductBomList(searchBaseProductBom);
//                        if (StringUtils.isNotEmpty(responseEntityPB.getData())) {
//                            List<BaseProductBomDetDto> baseProductBomDetDtos = responseEntityPB.getData().get(0).getBaseProductBomDetDtos();
//                            Long finalProcessId = processId;
//                            Long finalMaterialId = partMaterialId;
//                            Optional<BaseProductBomDetDto> productBomDetOptional = baseProductBomDetDtos.stream()
//                                    .filter(i -> finalProcessId.equals(i.getProcessId()) && finalMaterialId.equals(i.getMaterialId()))
//                                    .findFirst();
//                            if (!productBomDetOptional.isPresent()) {
//                                throw new Exception("当前工序找不到成品条码与半成品条码的关系");
//                            }
//                        }

//                    }

                    throw new Exception("当前工序找不到成品条码与半成品条码的关系 产品物料编码-->"+materialCode+" 半成品物料编码-->"+partMaterialCode);
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
    private static BaseExecuteResultDto checkBarcodeStatus(String barCode,Long orgId,String type) throws Exception {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = barcodeUtils.mesSfcWorkOrderBarcodeService
                    .findList(SearchMesSfcWorkOrderBarcode.builder()
                            .barcode(barCode)
                            .orgId(orgId)
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
                throw new BizErrorException(ErrorCodeEnum.PDA40012004, mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 ? "已完成" : "待打印");
            }

            baseExecuteResultDto.setIsSuccess(true);
            //baseExecuteResultDto.setSuccessMsg("操作成功");
            baseExecuteResultDto.setExecuteResult(mesSfcWorkOrderBarcodeDto);

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
    public static BaseExecuteResultDto checkPmProKeyIssues(String workOrderCode,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            //获取配置项产前关键事项是否已完成  WorkOrderIfNeedProductionKeyIssues
            String paraValue = getSysSpecItemValue("WorkOrderIfNeedProductionKeyIssues");
            if ("1".equals(paraValue)) {
                baseExecuteResultDto = checkPmProductionKeyIssues(workOrderCode,orgId);
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

    public static BaseExecuteResultDto checkPmProductionKeyIssues(String workOrderCode,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            SearchMesPmProductionKeyIssuesOrder searchMesPmProductionKeyIssuesOrder=new SearchMesPmProductionKeyIssuesOrder();
            searchMesPmProductionKeyIssuesOrder.setWorkOrderCode(workOrderCode);
            searchMesPmProductionKeyIssuesOrder.setOrgId(orgId);
            ResponseEntity<List<MesPmProductionKeyIssuesOrder>> PmPKIOList = barcodeUtils.deviceInterFaceUtils.getPmPKIOList(searchMesPmProductionKeyIssuesOrder);
            if (StringUtils.isEmpty(PmPKIOList.getData())) {
                throw new Exception("工单产前关键事项未完成");
            }
            else {
                MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder = PmPKIOList.getData().get(0);
                if (mesPmProductionKeyIssuesOrder.getOrderStatus()!=(byte)2) {
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
        if(StringUtils.isNotEmpty(sysSpecItemList.getData())) {
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
    public static BaseExecuteResultDto bandingWorkOrderBarcode(Long workOrderId,Long partMaterialId,String productionSn,String halfProductionSn,
            Long workOrderBarcodeId,Long proLineId,Long processId,Long stationId,Long materialId,Long userId,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            if(StringUtils.isNotEmpty(halfProductionSn)){

                /*
                * 1 获取工单BOM明细
                * 2 是否已绑定
                * 2 比较使用量 usageQty 与当前已绑定量
                * 3 绑定到mes_sfc_key_part_relevance 生产管理-关键部件关联表
                */

//                SearchMesPmWorkOrderBom searchMesPmWorkOrderBom = new SearchMesPmWorkOrderBom();
//                searchMesPmWorkOrderBom.setWorkOrderId(workOrderId);
//                searchMesPmWorkOrderBom.setPartMaterialId(partMaterialId);
//                searchMesPmWorkOrderBom.setProcessId(processId);
//                ResponseEntity<List<MesPmWorkOrderBomDto>> responseEntityBom = barcodeUtils.deviceInterFaceUtils.getWorkOrderBomList(searchMesPmWorkOrderBom);
//                if(StringUtils.isEmpty(responseEntityBom.getData())){
//                    throw new Exception("找不到当前工单工序所需的物料信息");
//                }

//                MesPmWorkOrderBomDto mesPmWorkOrderBomDto=responseEntityBom.getData().get(0);
                // 半成品条码是否已绑定
                Example example = new Example(MesSfcKeyPartRelevance.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("partBarcode", halfProductionSn);
                int countByExample = barcodeUtils.mesSfcKeyPartRelevanceService.selectCountByExample(example);
                if (countByExample > 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012028,"该半成品条码已绑定条码，不可重复扫描");
                }

                Map<String, Object> map = new HashMap<>();
                // 关键部件物料清单
                map.clear();
                map.put("workOrderId", workOrderId);
                map.put("processId", processId);
                //map.put("materialId", mesPmWorkOrder.getMaterialId());
                map.put("workOrderBarcodeId", workOrderBarcodeId);
                List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = barcodeUtils.mesSfcKeyPartRelevanceService.findList(map);
                // 关键部件物料数量大于等于用量 mesPmWorkOrderBomDto.getUsageQty().intValue()
                if (keyPartRelevanceDtos.size() >= 1) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012020,"物料清单已满，不可扫码");
                }

                BaseProLine proLine = barcodeUtils.baseFeignApi.selectProLinesDetail(proLineId).getData();
                BaseProcess baseProcess = barcodeUtils.baseFeignApi.processDetail(processId).getData();
                //BaseStation baseStation = barcodeUtils.baseFeignApi.findStationDetail(stationId).getData();

                BaseStation baseStation=new BaseStation();
                if(StringUtils.isNotEmpty(stationId)){
                    baseStation = barcodeUtils.baseFeignApi.findStationDetail(stationId).getData();
                }

                // 绑定附件码跟条码关系
                barcodeUtils.mesSfcKeyPartRelevanceService.save(MesSfcKeyPartRelevance.builder()
                        .barcodeCode(productionSn)
                        .workOrderId(workOrderId)
                        .workOrderCode("")
                        .workOrderBarcodeId(workOrderBarcodeId)
                        .proLineId(proLineId)
                        .proCode(proLine.getProCode())
                        .proName(proLine.getProName())
                        .processId(processId)
                        .processCode(baseProcess.getProcessCode())
                        .processName(baseProcess.getProcessName())
                        .stationId(baseStation.getStationId())
                        .stationCode(baseStation.getStationCode())
                        .stationName(baseStation.getStationName())
                        .materialId(partMaterialId)
                        .partBarcode(halfProductionSn)
                        .materialCode(halfProductionSn)
                        .operatorUserId(userId)
                        .operatorTime(new Date())
                        .orgId(orgId)
                        .createTime(new Date())
                        .createUserId(userId)
                        .isDelete((byte) 1)
                        .build());
            }

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    /**
     * 生成条码表记录
     * create by: Dylan
     * description: TODO
     * create time:
     * @return
     */
    public static BaseExecuteResultDto createWorkOrderBarcode(String barcode,String partBarcode,MesPmWorkOrderDto mesPmWorkOrderDto,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            if(StringUtils.isNotEmpty(barcode) && StringUtils.isEmpty(partBarcode)) {
                List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtoList = barcodeUtils.deviceInterFaceUtils.getWorkOrderBarcode(barcode, orgId);
                if (workOrderBarcodeDtoList.size() <= 0) {
                    MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = new MesSfcWorkOrderBarcode();
                    mesSfcWorkOrderBarcode.setBarcode(barcode);
                    mesSfcWorkOrderBarcode.setLabelCategoryId(barcodeUtils.mesSfcWorkOrderBarcodeMapper.finByTypeId("产品条码"));
                    mesSfcWorkOrderBarcode.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                    mesSfcWorkOrderBarcode.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
                    mesSfcWorkOrderBarcode.setBarcodeStatus((byte) 1);//条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
                    mesSfcWorkOrderBarcode.setOrgId(orgId);
                    mesSfcWorkOrderBarcode.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                    mesSfcWorkOrderBarcode.setWorkOrderQty(mesPmWorkOrderDto.getWorkOrderQty());

                    mesSfcWorkOrderBarcode.setCreateTime(new Date());
                    mesSfcWorkOrderBarcode.setCreateUserId(1L);
                    mesSfcWorkOrderBarcode.setModifiedTime(new Date());
                    mesSfcWorkOrderBarcode.setModifiedUserId(1L);
                    mesSfcWorkOrderBarcode.setCreateBarcodeTime(new Date());
                    barcodeUtils.mesSfcWorkOrderBarcodeMapper.insertUseGeneratedKeys(mesSfcWorkOrderBarcode);

                    //生成条码过站记录
                    MesSfcBarcodeProcess mesSfcBarcodeProcessExist = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                            .barcode(barcode)
                            .build());

                    if(StringUtils.isEmpty(mesSfcBarcodeProcessExist)) {
                        MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                        mesSfcBarcodeProcess.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
                        mesSfcBarcodeProcess.setWorkOrderCode(mesSfcWorkOrderBarcode.getWorkOrderCode());
                        mesSfcBarcodeProcess.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
                        mesSfcBarcodeProcess.setBarcodeType((byte) 2);
                        mesSfcBarcodeProcess.setBarcode(mesSfcWorkOrderBarcode.getBarcode());

                        mesSfcBarcodeProcess.setProLineId(mesPmWorkOrderDto.getProLineId());

                        mesSfcBarcodeProcess.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                        mesSfcBarcodeProcess.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                        mesSfcBarcodeProcess.setMaterialName(mesPmWorkOrderDto.getMaterialName());
                        mesSfcBarcodeProcess.setMaterialVer(mesPmWorkOrderDto.getMaterialVersion());
                        mesSfcBarcodeProcess.setRouteId(mesPmWorkOrderDto.getRouteId());
                        mesSfcBarcodeProcess.setRouteCode(mesPmWorkOrderDto.getRouteCode());
                        mesSfcBarcodeProcess.setRouteName(mesPmWorkOrderDto.getRouteName());
                        mesSfcBarcodeProcess.setOrgId(orgId);

                        //查询工艺路线
                        ResponseEntity<List<BaseRouteProcess>> res = barcodeUtils.baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
                        if (res.getCode() != 0) {
                            throw new BizErrorException("工艺路线查询失败");
                        }
                        mesSfcBarcodeProcess.setProcessId(res.getData().get(0).getProcessId());
                        mesSfcBarcodeProcess.setProcessCode(res.getData().get(0).getProcessCode());
                        mesSfcBarcodeProcess.setProcessName(res.getData().get(0).getProcessName());
                        mesSfcBarcodeProcess.setNextProcessId(res.getData().get(0).getProcessId());
                        mesSfcBarcodeProcess.setNextProcessCode(res.getData().get(0).getProcessCode());
                        mesSfcBarcodeProcess.setNextProcessName(res.getData().get(0).getProcessName());
                        mesSfcBarcodeProcess.setSectionId(res.getData().get(0).getSectionId());
                        mesSfcBarcodeProcess.setSectionName(res.getData().get(0).getSectionName());
                        if (barcodeUtils.mesSfcBarcodeProcessMapper.insertSelective(mesSfcBarcodeProcess) < 1) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(), "条码过站失败-->" + barcode);
                        }
                    }

                    baseExecuteResultDto.setExecuteResult(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());

                } else {
                    baseExecuteResultDto.setExecuteResult(workOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId());
                }
            }
            else if(StringUtils.isNotEmpty(barcode) && StringUtils.isNotEmpty(partBarcode) && partBarcode.equals("onlySn")==false){
                List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtoList = barcodeUtils.deviceInterFaceUtils.getWorkOrderBarcode(barcode, orgId);
                if (workOrderBarcodeDtoList.size() <= 0) {
                    //生成产品条码
                    MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = new MesSfcWorkOrderBarcode();
                    mesSfcWorkOrderBarcode.setBarcode(barcode);
                    mesSfcWorkOrderBarcode.setLabelCategoryId(barcodeUtils.mesSfcWorkOrderBarcodeMapper.finByTypeId("产品条码"));
                    mesSfcWorkOrderBarcode.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                    mesSfcWorkOrderBarcode.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
                    mesSfcWorkOrderBarcode.setBarcodeStatus((byte) 1);//条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
                    mesSfcWorkOrderBarcode.setOrgId(orgId);
                    mesSfcWorkOrderBarcode.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                    mesSfcWorkOrderBarcode.setWorkOrderQty(mesPmWorkOrderDto.getWorkOrderQty());

                    mesSfcWorkOrderBarcode.setCreateTime(new Date());
                    mesSfcWorkOrderBarcode.setCreateUserId(1L);
                    mesSfcWorkOrderBarcode.setModifiedTime(new Date());
                    mesSfcWorkOrderBarcode.setModifiedUserId(1L);
                    mesSfcWorkOrderBarcode.setCreateBarcodeTime(new Date());
                    barcodeUtils.mesSfcWorkOrderBarcodeMapper.insertUseGeneratedKeys(mesSfcWorkOrderBarcode);

                    baseExecuteResultDto.setExecuteResult(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());

                    //List<MesSfcWorkOrderBarcodeDto> workOrderHalfBarcodeDtoList = barcodeUtils.deviceInterFaceUtils.getWorkOrderBarcode(partBarcode, orgId);
                    MesSfcBarcodeProcess mesSfcHalfBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                            .barcode(partBarcode)
                            .build());
                    if(StringUtils.isNotEmpty(mesSfcHalfBarcodeProcess)){
                        MesSfcBarcodeProcess mesSfcBarcodeProcessExist = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                                .barcode(barcode)
                                .build());
                        //产品条码过站信息主表=半成品条码过站信息
                        if(StringUtils.isEmpty(mesSfcBarcodeProcessExist)) {
                            MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                            BeanUtils.copyProperties(mesSfcHalfBarcodeProcess, mesSfcBarcodeProcess);
                            mesSfcBarcodeProcess.setBarcode(barcode);
                            mesSfcBarcodeProcess.setCustomerBarcode(partBarcode);
                            mesSfcBarcodeProcess.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
                            mesSfcBarcodeProcess.setBarcodeProcessId(null);
                            if (barcodeUtils.mesSfcBarcodeProcessMapper.insertSelective(mesSfcBarcodeProcess) < 1) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(), "条码过站失败-->" + barcode);
                            }
                        }
                    }


                } else {
                    baseExecuteResultDto.setExecuteResult(workOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId());
                }
            }
            else if(partBarcode.equals("onlySn")){
                List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtoList = barcodeUtils.deviceInterFaceUtils.getWorkOrderBarcode(barcode, orgId);
                if (workOrderBarcodeDtoList.size() <= 0) {
                    //生成产品条码
                    MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = new MesSfcWorkOrderBarcode();
                    mesSfcWorkOrderBarcode.setBarcode(barcode);
                    mesSfcWorkOrderBarcode.setLabelCategoryId(barcodeUtils.mesSfcWorkOrderBarcodeMapper.finByTypeId("产品条码"));
                    mesSfcWorkOrderBarcode.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                    mesSfcWorkOrderBarcode.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
                    mesSfcWorkOrderBarcode.setBarcodeStatus((byte) 1);//条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
                    mesSfcWorkOrderBarcode.setOrgId(orgId);
                    mesSfcWorkOrderBarcode.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                    mesSfcWorkOrderBarcode.setWorkOrderQty(mesPmWorkOrderDto.getWorkOrderQty());

                    mesSfcWorkOrderBarcode.setCreateTime(new Date());
                    mesSfcWorkOrderBarcode.setCreateUserId(1L);
                    mesSfcWorkOrderBarcode.setModifiedTime(new Date());
                    mesSfcWorkOrderBarcode.setModifiedUserId(1L);
                    mesSfcWorkOrderBarcode.setCreateBarcodeTime(new Date());
                    barcodeUtils.mesSfcWorkOrderBarcodeMapper.insertUseGeneratedKeys(mesSfcWorkOrderBarcode);

                    //生成条码过站记录
                    MesSfcBarcodeProcess mesSfcBarcodeProcessExist = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                            .barcode(barcode)
                            .build());
                    if(StringUtils.isEmpty(mesSfcBarcodeProcessExist)) {
                        MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                        mesSfcBarcodeProcess.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
                        mesSfcBarcodeProcess.setWorkOrderCode(mesSfcWorkOrderBarcode.getWorkOrderCode());
                        mesSfcBarcodeProcess.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
                        mesSfcBarcodeProcess.setBarcodeType((byte) 2);
                        mesSfcBarcodeProcess.setBarcode(mesSfcWorkOrderBarcode.getBarcode());

                        mesSfcBarcodeProcess.setProLineId(mesPmWorkOrderDto.getProLineId());

                        mesSfcBarcodeProcess.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                        mesSfcBarcodeProcess.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                        mesSfcBarcodeProcess.setMaterialName(mesPmWorkOrderDto.getMaterialName());
                        mesSfcBarcodeProcess.setMaterialVer(mesPmWorkOrderDto.getMaterialVersion());
                        mesSfcBarcodeProcess.setRouteId(mesPmWorkOrderDto.getRouteId());
                        mesSfcBarcodeProcess.setRouteCode(mesPmWorkOrderDto.getRouteCode());
                        mesSfcBarcodeProcess.setRouteName(mesPmWorkOrderDto.getRouteName());
                        mesSfcBarcodeProcess.setOrgId(orgId);

                        //查询工艺路线
                        ResponseEntity<List<BaseRouteProcess>> res = barcodeUtils.baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
                        if (res.getCode() != 0) {
                            throw new BizErrorException("工艺路线查询失败");
                        }
                        mesSfcBarcodeProcess.setProcessId(res.getData().get(0).getProcessId());
                        mesSfcBarcodeProcess.setProcessCode(res.getData().get(0).getProcessCode());
                        mesSfcBarcodeProcess.setProcessName(res.getData().get(0).getProcessName());
                        mesSfcBarcodeProcess.setNextProcessId(res.getData().get(0).getProcessId());
                        mesSfcBarcodeProcess.setNextProcessCode(res.getData().get(0).getProcessCode());
                        mesSfcBarcodeProcess.setNextProcessName(res.getData().get(0).getProcessName());
                        mesSfcBarcodeProcess.setSectionId(res.getData().get(0).getSectionId());
                        mesSfcBarcodeProcess.setSectionName(res.getData().get(0).getSectionName());
                        if (barcodeUtils.mesSfcBarcodeProcessMapper.insertSelective(mesSfcBarcodeProcess) < 1) {
                            throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(), "条码过站失败-->" + barcode);
                        }
                    }

                    baseExecuteResultDto.setExecuteResult(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());

                } else {
                    baseExecuteResultDto.setExecuteResult(workOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId());
                }
            }

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    public static Boolean findBomExistMaterialId(Long materialId,Long partMaterialId,Long orgId){
        SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
        searchBaseProductBom.setMaterialId(materialId);
        searchBaseProductBom.setOrgId(orgId);
        ResponseEntity<List<BaseProductBomDto>> responseEntityPB = barcodeUtils.deviceInterFaceUtils.getProductBomList(searchBaseProductBom);
        if (StringUtils.isNotEmpty(responseEntityPB.getData())) {
            BaseProductBomDto baseProductBomDto = responseEntityPB.getData().get(0);
            long productBomId=baseProductBomDto.getProductBomId();
            searchBaseProductBom.setProductBomId(productBomId);
            searchBaseProductBom.setPageSize(2000);
            ResponseEntity<BaseProductBomDto> responseEntityBom= barcodeUtils.deviceInterFaceUtils.findNextLevelProductBomDet(searchBaseProductBom);
            if(StringUtils.isNotEmpty(responseEntityBom.getData())){
                List<BaseProductBomDetDto> baseProductBomDetDtos = responseEntityBom.getData().getBaseProductBomDetDtos();
                Long finalMaterialId = partMaterialId;
                Optional<BaseProductBomDetDto> productBomDetOptional = baseProductBomDetDtos.stream()
                        .filter(i -> finalMaterialId.equals(i.getMaterialId()))
                        .findFirst();
                if (!productBomDetOptional.isPresent()) {
                    //throw new Exception("当前工序找不到成品条码与半成品条码的关系");
                    List<BaseProductBomDetDto> productBomDetDtos=baseProductBomDetDtos.stream().filter(item -> item.getIfHaveLowerLevel().equals((byte)1)).collect(Collectors.toList());
                    if(productBomDetDtos.size()>0){
                        for (BaseProductBomDetDto item : productBomDetDtos) {
                            Boolean isOK=findBomExistMaterialId(item.getMaterialId(),partMaterialId,orgId);
                            if(isOK)
                                return true;
                        }
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }

}
