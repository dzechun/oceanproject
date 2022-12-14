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
 * ???????????????
 *
 * @author hyc
 * @version 1.0
 * @date 2021/04/09 17:23
 **/
@Component
@Slf4j
public class BarcodeUtils {

    // region ????????????

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
     * ????????????????????????
     *
     * @return
     */
    public static Boolean checkSN(CheckProductionDto record) throws Exception {

        // 1?????????????????????????????????????????????
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = checkBarcodeStatus(record.getBarCode(), record.getWorkOrderId());

        MesPmWorkOrder mesPmWorkOrder = barcodeUtils.pmFeignApi.workOrderDetail(mesSfcWorkOrderBarcodeDto.getWorkOrderId()).getData();

        // 2????????????????????????????????????????????????
        if (record.getProcessId() != null) {
            checkBarcodeProcess(mesSfcWorkOrderBarcodeDto, record.getProcessId(), mesPmWorkOrder.getRouteId());
        }

        // 3????????????????????????????????????????????????????????????
        if (mesSfcWorkOrderBarcodeDto.getWorkOrderId() != null) {
            checkOrder(mesSfcWorkOrderBarcodeDto, record.getProLineId());
        }

        // 4?????????????????????
        if (record.getCheckOrNot()) {

        }
        return true;
    }

    public static Boolean checkSN(CheckProductionDto record,Long workOrderBarcodeId,Long routeId) throws Exception {

        // 2????????????????????????????????????????????????
        if (record.getProcessId() != null) {
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto=new MesSfcWorkOrderBarcodeDto();
            mesSfcWorkOrderBarcodeDto.setWorkOrderBarcodeId(workOrderBarcodeId);
            mesSfcWorkOrderBarcodeDto.setBarcode(record.getBarCode());
            checkBarcodeProcess(mesSfcWorkOrderBarcodeDto, record.getProcessId(), routeId);
        }
        return true;
    }

    /**
     * 1??????????????????????????????????????????ID????????????????????????????????????ID?????????ID?????????ID???????????????ID???????????????????????????OK????????????????????????N/A,??????ID???????????????N/A???
     * 2???????????????????????????????????????????????????
     * 3?????????????????????
     *
     * @return
     * @throws Exception
     */
    public static int updateProcess(UpdateProcessDto dto) throws Exception {
        log.info("================== ?????? ==================");
        long start = System.currentTimeMillis();
        // ????????????
        MesSfcBarcodeProcess mesSfcBarcodeProcess=null;
        if(StringUtils.isNotEmpty(dto.getWorkOrderBarcodeId())){
            mesSfcBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .workOrderBarcodeId(dto.getWorkOrderBarcodeId())
                    .build());
        }
        else if(StringUtils.isEmpty(dto.getWorkOrderBarcodeId()) && StringUtils.isNotEmpty(dto.getBarCode())) {
            mesSfcBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .barcode(dto.getBarCode())
                    .build());
        }
        ResponseEntity<List<BaseRouteProcess>> responseEntity = barcodeUtils.baseFeignApi.findConfigureRout(dto.getRouteId());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012008);
        }
        List<BaseRouteProcess> routeProcessList = responseEntity.getData();
        // ????????????????????????
        MesPmWorkOrder mesPmWorkOrder = barcodeUtils.pmFeignApi.workOrderDetail(dto.getWorkOrderId()).getData();
        long one1 = System.currentTimeMillis();
        log.info("============== ??????????????????one1:"+ (one1 - start));
        // ???????????????????????????????????????,?????????????????????????????????????????????
        if (!mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
            // ???????????????????????????ID????????????????????????ID?????????
            // ????????????????????????????????????????????????
            if (!dto.getNowProcessId().equals(mesSfcBarcodeProcess.getNextProcessId())) {
                MesSfcBarcodeProcess mesSfcBarcodeProcessOptional=mesSfcBarcodeProcess;
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> mesSfcBarcodeProcessOptional.getNextProcessId().equals(i.getProcessId()))
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
        log.info("============== ??????????????????one:"+ (one - one1));
        log.info("============== ??????????????????:"+ (one - start));

        BaseProcess baseProcess = processResponseEntity.getData();
        // ??????????????????
        mesSfcBarcodeProcess.setProcessId(dto.getNowProcessId());
        mesSfcBarcodeProcess.setProcessCode(baseProcess.getProcessCode());
        mesSfcBarcodeProcess.setProcessName(baseProcess.getProcessName());
        // ??????????????????????????????
        if(StringUtils.isNotEmpty(dto.getNowStationId())) {
            BaseStation baseStation = barcodeUtils.baseFeignApi.findStationDetail(dto.getNowStationId()).getData();
            if (baseStation == null) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "???????????????????????????");
            }
            mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
            mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
            mesSfcBarcodeProcess.setStationName(baseStation.getStationName());
        }
        long two = System.currentTimeMillis();
        log.info("============== ????????????:"+ (two - one));
        //???????????? base_workshop_section ????????????????????????????????? 2021-08-02 huangshuijun
        ResponseEntity<BaseWorkshopSection> bwssResponseEntity = barcodeUtils.baseFeignApi.sectionDetail(baseProcess.getSectionId());
        BaseWorkshopSection baseWorkshopSection = bwssResponseEntity.getData();
        if(baseWorkshopSection == null)
            throw new BizErrorException(ErrorCodeEnum.PDA40012035);

        mesSfcBarcodeProcess.setSectionId(baseProcess.getSectionId());//??????id
        mesSfcBarcodeProcess.setSectionCode(baseWorkshopSection.getSectionCode());//??????code
        mesSfcBarcodeProcess.setSectionName(baseWorkshopSection.getSectionName());//????????????
        long two1 = System.currentTimeMillis();
        log.info("============== ?????????????????????????????????:"+ (two1 - two));
        BaseProLine baseProLine = barcodeUtils.baseFeignApi.getProLineDetail(dto.getProLineId()).getData();
        if (baseProLine == null) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003, "?????????????????????????????????");
        }
        mesSfcBarcodeProcess.setProLineId(baseProLine.getProLineId());
        mesSfcBarcodeProcess.setProCode(baseProLine.getProCode());
        mesSfcBarcodeProcess.setProName(baseProLine.getProName());

        long two2 = System.currentTimeMillis();
        log.info("============== ??????:"+ (two2 - two1));


        //????????????????????????
        //?????????????????????????????????????????? ?????????????????????????????? 2021-10-18
        Map<String, Object> mapExist = new HashMap<>();
        mapExist.put("barcode", dto.getBarCode());
        mapExist.put("stationId", dto.getNowStationId());
        mapExist.put("processId", dto.getNowProcessId());
        List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(mapExist);
        mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcessRecordDtoList.size()+1);
        //?????????????????????????????????????????? ?????????????????????????????? 2021-10-18

        if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getNowProcessId())) {
            mesSfcBarcodeProcess.setDevoteTime(new Date());
        }
        if (mesSfcBarcodeProcess.getReworkOrderId() != null){
            // ????????????????????????
            List<BaseRouteProcess> routeProcesses = barcodeUtils.baseFeignApi.findConfigureRout(mesSfcBarcodeProcess.getRouteId()).getData();
            Optional<BaseRouteProcess> lastRouteProcessOptional = routeProcesses.stream()
                    .filter(item -> item.getIsMustPass().equals(1))
                    .sorted(Comparator.comparing(BaseRouteProcess::getOrderNum).reversed())
                    .findFirst();
            if(lastRouteProcessOptional.isPresent()){
                BaseRouteProcess lastRouteProcess = lastRouteProcessOptional.get();
                if (mesSfcBarcodeProcess.getNextProcessId().equals(lastRouteProcess.getProcessId())) {
                    // ??????????????????????????????????????????
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
                    // ??????????????????
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
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "???????????????????????????????????????????????????????????????");
            }

            long three = System.currentTimeMillis();
            log.info("============== ?????????:"+ (three - two));
        }else {
            if (mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
                // ??????????????????????????????????????????
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

                // ??????????????????
                mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
            }
        }

        long three = System.currentTimeMillis();
        log.info("============== ??????next??????:"+ (three - two));

        //?????????????????? ??????????????????(0-NG 1-OK)
        if(StringUtils.isNotEmpty(dto.getOpResult()) && "NG".equals(dto.getOpResult()))
            mesSfcBarcodeProcess.setBarcodeStatus((byte)0);
        else if(StringUtils.isNotEmpty(dto.getOpResult()) && "OK".equals(dto.getOpResult()))
            mesSfcBarcodeProcess.setBarcodeStatus((byte)1);

        //????????????
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
            throw new RuntimeException("????????????????????????????????????");
        }
        // ??????????????????
        MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = new MesSfcBarcodeProcessRecord();
        BeanUtils.copyProperties(mesSfcBarcodeProcess, mesSfcBarcodeProcessRecord);
        mesSfcBarcodeProcessRecord.setOperatorUserId(dto.getOperatorUserId());
        mesSfcBarcodeProcessRecord.setModifiedTime(new Date());
        mesSfcBarcodeProcessRecord.setModifiedUserId(dto.getOperatorUserId());
        //?????????????????????????????? ??????????????? option1 ??????
        mesSfcBarcodeProcessRecord.setOption1(dto.getPassTime());
        barcodeUtils.mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);

        long four = System.currentTimeMillis();
        log.info("============== ??????????????????:"+ (four - three));

        /**
         * ?????????20211109
         * ????????????????????????
         */
        MesSfcWorkOrderBarcode sfcWorkOrderBarcode = barcodeUtils.mesSfcWorkOrderBarcodeService.selectByKey(mesSfcBarcodeProcess.getWorkOrderBarcodeId());

        // ???????????????????????????????????????????????????????????????????????????????????? +1 mesSfcBarcodeProcessRecordDtoList.isEmpty()
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getPutIntoProcessId())) {
            if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getNowProcessId()) && mesSfcBarcodeProcessRecordDtoList.size()==0) {

                /**
                 * 20211215 bgkun
                 * ???????????????????????????????????????????????????
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
                // ???????????????????????????????????????????????????????????????????????????????????????
                if (mesPmWorkOrder.getWorkOrderStatus() == (byte) 1) {
                    mesPmWorkOrder.setWorkOrderStatus((byte) 3);
                    mesPmWorkOrder.setActualStartTime(new Date());
                }
                barcodeUtils.pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);

                /**
                 * ?????????20211109
                 * ??????????????????  ????????????(0-????????? 1-????????? 2-????????? 3-?????????)
                 */
                sfcWorkOrderBarcode.setBarcodeStatus((byte) 1);
                barcodeUtils.mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
            }
        }

        long five = System.currentTimeMillis();
        log.info("============== ????????????:"+ (five - four));

        // ?????????????????????????????????????????????????????????????????????????????????????????????????????? +1
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getOutputProcessId())) {
            if (dto.getNowProcessId().equals(mesPmWorkOrder.getOutputProcessId()) && mesSfcBarcodeProcessRecordDtoList.size()==0) {
                mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty() != null ? BigDecimal.ONE.add(mesPmWorkOrder.getOutputQty()) : BigDecimal.ONE);
                if (mesPmWorkOrder.getOutputQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 0) {
                    // ?????????????????????????????????????????????
                    mesPmWorkOrder.setWorkOrderStatus((byte) 6);
                    mesPmWorkOrder.setActualEndTime(new Date());
                    mesPmWorkOrder.setModifiedTime(new Date());
                    mesPmWorkOrder.setModifiedUserId(dto.getOperatorUserId());
                }
                barcodeUtils.pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);
                long six = System.currentTimeMillis();
                log.info("============== ????????????:"+ (six - five));
                /**
                 * ?????????20211109
                 * ??????????????????  ????????????(0-????????? 1-????????? 2-????????? 3-?????????)
                 */
                sfcWorkOrderBarcode.setBarcodeStatus((byte) 2);
                barcodeUtils.mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
                long six1 = System.currentTimeMillis();
                log.info("============== ????????????:"+ (six1 - six));

                /**
                 * 20211215 bgkun
                 * ???????????????????????????????????????????????????
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
                log.info("============== ????????????:"+ (six2 - six1));
            }
        }

        long six = System.currentTimeMillis();
        log.info("============== ????????????:"+ (six - five));
        log.info("============== ?????????:"+ (six - start));
        return 1;
    }


    /**
     * ???????????????
     *
     * @param materialId       ????????????ID
     * @param processId        ??????ID
     * @param materialCode     ??????????????????
     * @param workOrderId      ??????ID
     * @param categoryCode ????????????????????????
     * @return
     * @throws Exception
     */
    public static String generatorCartonCode(Long materialId, Long processId, String materialCode, Long workOrderId, String categoryCode) throws Exception {
        BaseBarcodeRule barcodeRule = getBarcodeRule(materialId, processId);
        BaseLabelCategory labelCategory = barcodeUtils.baseFeignApi.findLabelCategoryDetail(barcodeRule.getLabelCategoryId()).getData();
        if(!labelCategory.getLabelCategoryCode().equals(categoryCode)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012021);
        }
        // ????????????????????????
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
            // ???redis????????????????????????
            Object redisRuleData = barcodeUtils.redisUtil.get(barcodeRule.getBarcodeRule());
            lastBarCode = String.valueOf(redisRuleData);
        }
        //?????????????????????
        String maxCode = barcodeUtils.baseFeignApi.generateMaxCode(barcodeRuleSpecList, lastBarCode).getData();
        //????????????
        ResponseEntity<String> rs = barcodeUtils.baseFeignApi.generateCode(barcodeRuleSpecList, maxCode, materialCode, workOrderId.toString());
        if (rs.getCode() != 0) {
            throw new BizErrorException(rs.getMessage());
        }
        // ??????redis???????????????
        barcodeUtils.redisUtil.set(barcodeRule.getBarcodeRule(), rs.getData());
        return rs.getData();
    }

    /**
     * ????????????/????????????
     *
     * @param dto
     */
    public static void printBarCode(PrintCarCodeDto dto) {
        LabelRuteDto labelRuteDto = barcodeUtils.mesSfcWorkOrderBarcodeMapper.findRule(dto.getLabelTypeCode(), dto.getWorkOrderId());
        if(StringUtils.isEmpty(labelRuteDto)) {
            throw new BizErrorException("???????????????????????????????????????????????????????????????");
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


    // region ??????????????????

    /**
     * ????????????
     *
     * @param barCode ??????
     * @return MesSfcWorkOrderBarcodeDto ??????DTO
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
        * ???????????????(0-????????? 1-????????? 2-????????? 3-?????????)
        *
        */
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        if (mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 || mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 3) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012004, mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 ? "?????????" : "?????????");
        }
        return mesSfcWorkOrderBarcodeDto;
    }

    /**
     * ??????????????????
     *
     * @param mesSfcWorkOrderBarcodeDto ??????DTO
     * @param processId                 ??????ID
     * @param routeId                   ????????????ID
     */
    private static void checkBarcodeProcess(MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto, Long processId, Long routeId) {
        if(StringUtils.isNotEmpty(mesSfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())) {
            MesSfcBarcodeProcess mesSfcBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .workOrderBarcodeId(mesSfcWorkOrderBarcodeDto.getWorkOrderBarcodeId())
                    .build());
            // ???????????????????????????????????? ??????

            //??????????????? ???????????????????????????????????????  PackingIfCheckBarcodeStatus
            String paraValue = getSysSpecItemValue("PackingIfCheckBarcodeStatus");
            boolean isPackingProcess = false;
            if ("1".equals(paraValue)) {
                //???????????????????????????????????????
                BaseProcess bProcss = null;
                ResponseEntity<BaseProcess> reEntityBP = barcodeUtils.baseFeignApi.processDetail(processId);
                if (StringUtils.isNotEmpty(reEntityBP)) {
                    bProcss = reEntityBP.getData();
                }
                if (StringUtils.isNotEmpty(bProcss)) {
                    long processCategoryId = bProcss.getProcessCategoryId();
                    ResponseEntity<BaseProcessCategory> processCategoryEntity = barcodeUtils.baseFeignApi.processCategoryDetail(processCategoryId);
                    if (processCategoryEntity.getCode() != 0) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "???????????????????????????");
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
                    throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "???????????????????????????????????????-->" + " ????????????:" + nowProcess.getData().getProcessCode() + " ????????????:" + nowProcess.getData().getProcessName());
                }

                BaseRouteProcess routeProcess = routeProcessOptional.get();
                //??????????????????????????????????????????
                if (routeProcess.getIsMustPass() == 0) {
                    return;
                }

                int num = routeProcess.getOrderNum();//??????????????????
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
                        throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "?????????????????????-->" + baseProcess.getProcessName());
                    }
                }
            }

            // ???????????????????????????????????? ??????

            if (mesSfcBarcodeProcess != null) {
                Long existID = processId;
                if (!processId.equals(mesSfcBarcodeProcess.getNextProcessId()) && StringUtils.isNotEmpty(mesSfcBarcodeProcess.getNextProcessCode())) {
//                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
////                        .filter(i -> existID.equals(i.getProcessId()))
////                        .findFirst();
////                if (!routeProcessOptional.isPresent()) {
////                    throw new BizErrorException(ErrorCodeEnum.PDA40012009.getCode(), "??????????????????????????????");
////                }
////                BaseRouteProcess routeProcess = routeProcessOptional.get();
////                if (routeProcess.getIsMustPass() == 0) {
////                    throw new BizErrorException(ErrorCodeEnum.PDA40012010.getCode(), "???????????????????????????");
////                }
////                else {
////                    BaseProcess baseProcess = barcodeUtils.baseFeignApi.processDetail(processId).getData();
////                    throw new BizErrorException(ErrorCodeEnum.PDA40012003, baseProcess.getProcessName(), mesSfcBarcodeProcess.getNextProcessName());
////                }

                    BaseProcess baseProcess = barcodeUtils.baseFeignApi.processDetail(processId).getData();
                    throw new BizErrorException(ErrorCodeEnum.PDA40012003, baseProcess.getProcessName(), mesSfcBarcodeProcess.getNextProcessName());

                }
//            if (!mesSfcBarcodeProcess.getProLineId().equals(proLineId)){
//                throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "????????????????????????????????????????????????");
//            }
                // ???????????????????????????
                if (mesSfcBarcodeProcess.getNextProcessId().equals(0L)) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "??????????????????????????????????????????");
                }
                //???????????????
//            if (StringUtils.isNotEmpty(mesSfcBarcodeProcess.getBarcodeStatus()) && mesSfcBarcodeProcess.getBarcodeStatus().equals((byte)0)){
//                throw new BizErrorException("???????????????????????? ????????????");
//            }
            } else {
                throw new BizErrorException(ErrorCodeEnum.PDA40012002, mesSfcWorkOrderBarcodeDto.getBarcode());
            }
        }
    }

    public static int updateBarcodeProcess(UpdateProcessDto dto) throws Exception {
        String passTimeIsOK="0";
        // ????????????
        MesSfcBarcodeProcess mesSfcBarcodeProcess = barcodeUtils.mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                .barcode(dto.getBarCode())
                .build());
        ResponseEntity<List<BaseRouteProcess>> responseEntity = barcodeUtils.baseFeignApi.findConfigureRout(dto.getRouteId());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012008);
        }
        List<BaseRouteProcess> routeProcessList = responseEntity.getData();
        // ????????????????????????
        MesPmWorkOrder mesPmWorkOrder = barcodeUtils.pmFeignApi.workOrderDetail(dto.getWorkOrderId()).getData();
        // ???????????????????????????????????????,?????????????????????????????????????????????
        if (!mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
            // ???????????????????????????ID????????????????????????ID?????????
            // ????????????????????????????????????????????????
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
        // ??????????????????
        mesSfcBarcodeProcess.setProcessId(dto.getNowProcessId());
        mesSfcBarcodeProcess.setProcessCode(baseProcess.getProcessCode());
        mesSfcBarcodeProcess.setProcessName(baseProcess.getProcessName());
        // ??????????????????????????????
        if(StringUtils.isNotEmpty(dto.getNowStationId())) {
            BaseStation baseStation = barcodeUtils.baseFeignApi.findStationDetail(dto.getNowStationId()).getData();
            if (baseStation == null) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "???????????????????????????");
            }
            mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
            mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
            mesSfcBarcodeProcess.setStationName(baseStation.getStationName());
        }
        //???????????? base_workshop_section ????????????????????????????????? 2021-08-02 huangshuijun
        ResponseEntity<BaseWorkshopSection> bwssResponseEntity = barcodeUtils.baseFeignApi.sectionDetail(baseProcess.getSectionId());
        BaseWorkshopSection baseWorkshopSection = bwssResponseEntity.getData();
        if(baseWorkshopSection == null)
            throw new BizErrorException(ErrorCodeEnum.PDA40012035);

//        mesSfcBarcodeProcess.setStationId(baseStation.getStationId());
//        mesSfcBarcodeProcess.setStationCode(baseStation.getStationCode());
//        mesSfcBarcodeProcess.setStationName(baseStation.getStationName());

        mesSfcBarcodeProcess.setSectionId(baseProcess.getSectionId());//??????id
//        mesSfcBarcodeProcess.setSectionCode(baseProcess.getSectionCode());//??????code
//        mesSfcBarcodeProcess.setSectionName(baseProcess.getSectionName());//????????????
        mesSfcBarcodeProcess.setSectionCode(baseWorkshopSection.getSectionCode());//??????code
        mesSfcBarcodeProcess.setSectionName(baseWorkshopSection.getSectionName());//????????????
        BaseProLine baseProLine = barcodeUtils.baseFeignApi.getProLineDetail(dto.getProLineId()).getData();
        if (baseProLine == null) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003, "?????????????????????????????????");
        }
        mesSfcBarcodeProcess.setProLineId(baseProLine.getProLineId());
        mesSfcBarcodeProcess.setProCode(baseProLine.getProCode());
        mesSfcBarcodeProcess.setProName(baseProLine.getProName());

        //????????????????????????
        //mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcess.getPassStationCount() != null ? mesSfcBarcodeProcess.getPassStationCount() + 1 : 1);
        //?????????????????????????????????????????? ?????????????????????????????? 2021-10-18
        Map<String, Object> mapExist = new HashMap<>();
        mapExist.put("barcode", dto.getBarCode());
        //mapExist.put("stationId", dto.getNowStationId());
        mapExist.put("processId", dto.getNowProcessId());
        List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(mapExist);
        mesSfcBarcodeProcess.setPassStationCount(mesSfcBarcodeProcessRecordDtoList.size()+1);
        //?????????????????????????????????????????? ?????????????????????????????? 2021-10-18

        if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getNowProcessId())) {
            mesSfcBarcodeProcess.setDevoteTime(new Date());
        }
        if (mesSfcBarcodeProcess.getReworkOrderId() != null){
            // ????????????????????????
            List<BaseRouteProcess> routeProcesses = barcodeUtils.baseFeignApi.findConfigureRout(mesSfcBarcodeProcess.getRouteId()).getData();
            Optional<BaseRouteProcess> lastRouteProcessOptional = routeProcesses.stream()
                    .filter(item -> item.getIsMustPass().equals(1))
                    .sorted(Comparator.comparing(BaseRouteProcess::getOrderNum).reversed())
                    .findFirst();
            if(lastRouteProcessOptional.isPresent()){
                BaseRouteProcess lastRouteProcess = lastRouteProcessOptional.get();
                if (mesSfcBarcodeProcess.getNextProcessId().equals(lastRouteProcess.getProcessId())) {
                    // ??????????????????????????????????????????
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
                    // ??????????????????
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
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "???????????????????????????????????????????????????????????????");
            }
        }else {
            // mesSfcBarcodeProcess.getNextProcessId().equals(mesPmWorkOrder.getOutputProcessId())
            if (dto.getNowProcessId().equals(mesPmWorkOrder.getOutputProcessId())) {
                // ??????????????????????????????????????????
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
                    throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "???????????????????????????????????????-->"+" ????????????:"+nowProcess.getData().getProcessCode()+" ????????????:"+nowProcess.getData().getProcessName());
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
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"????????????????????????");
                }
                processResponseEntity = barcodeUtils.baseFeignApi.processDetail(routeProcess.getNextProcessId());
                if (processResponseEntity.getCode() != 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012012, routeProcess.getNextProcessId());
                }
                baseProcess = processResponseEntity.getData();

                //???????????????????????????????????????
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
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "???????????????????????????");
                    }
                    BaseProcessCategory baseProcessCategory = processCategoryEntity.getData();
                    if ("repair".equals(baseProcessCategory.getProcessCategoryCode()) == true) {
                        isRepairProcess=true;
                        if(StringUtils.isNotEmpty(dto.getOpResult()) && "NG".equals(dto.getOpResult())){
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "??????NG ??????????????????");
                        }
                        else {
                            //??????????????????
                            mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                            mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                            mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
                        }
                    }
                }

                //?????????????????? 1 ?????????NG ?????????NG????????????????????????????????? ????????????????????????????????? repair ?????? ??????????????????
                // 2 ?????????OK ???????????????????????????  ????????????????????????????????????????????????
                if(isRepairProcess==false) {
                    if (StringUtils.isNotEmpty(dto.getOpResult()) && "NG".equals(dto.getOpResult())) {
                        /*long processCategoryId = baseProcess.getProcessCategoryId();
                        ResponseEntity<BaseProcessCategory> processCategoryEntity = barcodeUtils.baseFeignApi.processCategoryDetail(processCategoryId);
                        if (processCategoryEntity.getCode() != 0) {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "???????????????????????????");
                        }
                        BaseProcessCategory baseProcessCategory = processCategoryEntity.getData();
                        if ("repair".equals(baseProcessCategory.getProcessCategoryCode()) == false) {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "??????????????? ??????????????????????????????");
                        }

                        mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                        mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                        mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());*/

                        int num = routeProcess.getOrderNum();
                        Optional<BaseRouteProcess> routeProcessOptionalNext = routeProcessList.stream()
                                .filter(i -> i.getOrderNum() > num && i.getIsPass() == 1 && i.getIsMustPass()==1)
                                .findFirst();
                        if (!routeProcessOptionalNext.isPresent()) {
                            throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "???????????? ?????????????????????");
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
                            throw new BizErrorException(ErrorCodeEnum.PDA40012011.getCode(), "???????????? ?????????????????????");
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

                // ??????????????????
                //mesSfcBarcodeProcess.setNextProcessId(routeProcess.getNextProcessId());
                //mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                //mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
            }
        }

        //?????????????????? ??????????????????(0-NG 1-OK)
        if(StringUtils.isNotEmpty(dto.getOpResult()) && "NG".equals(dto.getOpResult()))
            mesSfcBarcodeProcess.setBarcodeStatus((byte)0);
        else if(StringUtils.isNotEmpty(dto.getOpResult()) && "OK".equals(dto.getOpResult()))
            mesSfcBarcodeProcess.setBarcodeStatus((byte)1);

        //????????????
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
            throw new RuntimeException("????????????????????????????????????");
        }
        // ??????????????????
        MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = new MesSfcBarcodeProcessRecord();
        BeanUtils.copyProperties(mesSfcBarcodeProcess, mesSfcBarcodeProcessRecord);
        mesSfcBarcodeProcessRecord.setOperatorUserId(dto.getOperatorUserId());
        mesSfcBarcodeProcessRecord.setModifiedTime(new Date());
        mesSfcBarcodeProcessRecord.setModifiedUserId(dto.getOperatorUserId());
        //?????????????????????????????? ??????????????? option1 ??????
        mesSfcBarcodeProcessRecord.setOption1(dto.getPassTime());

        //??????????????????????????????????????????????????? option2 ?????? passTimeIsOK 0 ?????? 1 ??????
        mesSfcBarcodeProcessRecord.setOption2(passTimeIsOK);

        //???????????????????????? ??????????????? option3 ??????
        if(StringUtils.isNotEmpty(dto.getEquipmentCode())){
            mesSfcBarcodeProcessRecord.setOption3(dto.getEquipmentCode());
        }

        barcodeUtils.mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);

        /**
         * ?????????20211109
         * ????????????????????????
         */
        MesSfcWorkOrderBarcode sfcWorkOrderBarcode = barcodeUtils.mesSfcWorkOrderBarcodeService.selectByKey(mesSfcBarcodeProcess.getWorkOrderBarcodeId());


//        Map<String, Object> map = new HashMap<>();
//        map.put("workOrderBarcodeId", dto.getBarCode());
//        map.put("stationId", dto.getNowStationId());
//        map.put("processId", dto.getNowProcessId());
//        List<MesSfcBarcodeProcessRecordDto> mesSfcBarcodeProcessRecordDtoList = barcodeUtils.mesSfcBarcodeProcessRecordService.findList(map);
        // ???????????????????????????????????????????????????????????????????????????????????? +1 mesSfcBarcodeProcessRecordDtoList.isEmpty()
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getPutIntoProcessId())) {
            if (mesPmWorkOrder.getPutIntoProcessId().equals(dto.getNowProcessId()) && mesSfcBarcodeProcessRecordDtoList.size()==0) {
                //mesPmWorkOrder.setProductionQty(mesPmWorkOrder.getProductionQty().add(BigDecimal.ONE));
                mesPmWorkOrder.setProductionQty(mesPmWorkOrder.getProductionQty() != null ? mesPmWorkOrder.getProductionQty().add(BigDecimal.ONE) : BigDecimal.ONE);
                // ???????????????????????????????????????????????????????????????????????????????????????
                if (mesPmWorkOrder.getWorkOrderStatus() == (byte) 1) {
                    mesPmWorkOrder.setWorkOrderStatus((byte) 3);
                }
                // huangshuijun ????????? updateSmtWorkOrder ???????????????1 ????????????????????????????????? ????????????????????????
                // 2 ???????????????????????????BOM?????? ??????
                //barcodeUtils.pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
                barcodeUtils.pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);

                /**
                 * ?????????20211109
                 * ??????????????????  ????????????(0-????????? 1-????????? 2-????????? 3-?????????)
                 */
                sfcWorkOrderBarcode.setBarcodeStatus((byte) 1);
                barcodeUtils.mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
            }
        }
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????? +1
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getOutputProcessId())) {
            if (dto.getNowProcessId().equals(mesPmWorkOrder.getOutputProcessId()) && mesSfcBarcodeProcessRecordDtoList.size()==0) {
                //mesPmWorkOrder.setOutputQty(BigDecimal.ONE.add(mesPmWorkOrder.getOutputQty()));
                mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty() != null ? BigDecimal.ONE.add(mesPmWorkOrder.getOutputQty()) : BigDecimal.ONE);
                if (mesPmWorkOrder.getOutputQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 0) {
                    // ?????????????????????????????????????????????
                    mesPmWorkOrder.setWorkOrderStatus((byte) 6);
                    mesPmWorkOrder.setActualEndTime(new Date());
                    mesPmWorkOrder.setModifiedTime(new Date());
                    mesPmWorkOrder.setModifiedUserId(dto.getOperatorUserId());
                }
                // huangshuijun ????????? updateSmtWorkOrder ???????????????1 ????????????????????????????????? ????????????????????????
                // 2 ???????????????????????????BOM?????? ??????
                //barcodeUtils.pmFeignApi.updateSmtWorkOrder(mesPmWorkOrder);
                barcodeUtils.pmFeignApi.updatePmWorkOrder(mesPmWorkOrder);

                /**
                 * ?????????20211109
                 * ??????????????????  ????????????(0-????????? 1-????????? 2-????????? 3-?????????)
                 */
                sfcWorkOrderBarcode.setBarcodeStatus((byte) 2);
                barcodeUtils.mesSfcWorkOrderBarcodeService.update(sfcWorkOrderBarcode);
            }
        }
        return 1;
    }

    /**
     * ????????????
     *
     * @param mesSfcWorkOrderBarcodeDto ??????DTO
     */
    private static void checkOrder(MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto, Long proLineId) {
        //????????????????????????
        ResponseEntity<MesPmWorkOrder> pmWorkOrderResponseEntity = barcodeUtils.pmFeignApi.workOrderDetail(mesSfcWorkOrderBarcodeDto.getWorkOrderId());
        if (pmWorkOrderResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012005, mesSfcWorkOrderBarcodeDto.getWorkOrderCode());
        }
        /*
        * ????????????(1:Initial???????????????????????????2:Release?????????????????????;3:WIP:????????????4:Hold???????????????5:Cancel?????????6:Complete?????????7:Delete?????????)
        */
        MesPmWorkOrder mesPmWorkOrder = pmWorkOrderResponseEntity.getData();
        if (4 == mesPmWorkOrder.getWorkOrderStatus() || 5 == mesPmWorkOrder.getWorkOrderStatus()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012006);
        }
        if (mesPmWorkOrder.getProductionQty().compareTo(mesPmWorkOrder.getWorkOrderQty()) == 1) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012007, mesPmWorkOrder.getWorkOrderCode());
        }
        if (!mesPmWorkOrder.getProLineId().equals(proLineId)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012007.getCode(), "???????????????????????????????????????????????????????????????");
        }
    }

    // endregion

    // region ????????????????????????????????????

    /**
     * ????????????????????????????????????
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param materialId       ????????????ID
     * @param processId        ??????ID
     * @return
     * @throws Exception
     */
    private static BaseBarcodeRule getBarcodeRule(Long materialId, Long processId) throws Exception {
        // ??????????????????
        SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
        searchBasePackageSpecification.setMaterialId(materialId);
        searchBasePackageSpecification.setProcessId(processId);
        List<BasePackageSpecificationDto> packageSpecificationDtos = barcodeUtils.baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification).getData();
        if (packageSpecificationDtos.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "??????????????????????????????????????????");
        }
        List<BaseMaterialPackageDto> baseMaterialPackageDtos = packageSpecificationDtos.get(0).getBaseMaterialPackages();
        if(baseMaterialPackageDtos.size() <= 0){
            throw new BizErrorException();
        }

        //??????materialId???processId ???????????????????????? 2021-08-09 by huangshuijun
        BaseMaterialPackageDto bMaterialPackageDto=null;
        for (BaseMaterialPackageDto baseMaterialPackageDto : baseMaterialPackageDtos) {
            if(baseMaterialPackageDto.getMaterialId().equals(materialId) && baseMaterialPackageDto.getProcessId().equals(processId)) {
                bMaterialPackageDto=baseMaterialPackageDto;
                break;
            }
        }

        //BaseBarcodeRule baseBarcodeRule = barcodeUtils.baseFeignApi.baseBarcodeRuleDetail(baseMaterialPackageDtos.get(0).getBarcodeRuleId()).getData();
        //??????materialId???processId ???????????????????????? 2021-08-09 by huangshuijun
        if(bMaterialPackageDto==null)
            throw new BizErrorException(ErrorCodeEnum.PDA40012036,materialId,processId);

        BaseBarcodeRule baseBarcodeRule = barcodeUtils.baseFeignApi.baseBarcodeRuleDetail(bMaterialPackageDto.getBarcodeRuleId()).getData();
        return baseBarcodeRule;
    }

    // endregion

    /*
    * ????????????????????????
    *
    */
    public static BaseExecuteResultDto ChkLogUserInfo(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws Exception {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        byte result=1;//????????????(0-?????? 1-??????)
        Long orgId = null;
        long start=0;
        long end=0;
        String requestTimeS="";
        String responseTimeS="";
        try {
            start = System.currentTimeMillis();//???????????????
            requestTimeS=DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN);

            String pass = "Pass";
            String fail = "Fail";
            String proName = "";
            String processName = "";
            String userName = "";
            ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList = barcodeUtils.deviceInterFaceUtils.getOrId();
            if (StringUtils.isEmpty(baseOrganizationDtoList.getData())) {
                throw new Exception(fail + " ????????????,????????????????????????");
            }
            //????????????ID
            orgId = baseOrganizationDtoList.getData().get(0).getOrganizationId();

            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto)) {
                throw new Exception(fail + " ????????????,????????????");
            }

            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getProCode())) {
                throw new Exception(fail + " ????????????,????????????????????????");
            } else {
                ResponseEntity<List<BaseProLine>> baseProLinelist = barcodeUtils.deviceInterFaceUtils.getProLine(restapiChkLogUserInfoApiDto.getProCode(), orgId);
                if (StringUtils.isEmpty(baseProLinelist.getData())) {
                    throw new Exception(fail + " ????????????,?????????????????????");
                }
                proName = baseProLinelist.getData().get(0).getProName();

            }
            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getProcessCode())) {
                throw new Exception(fail + " ????????????,????????????????????????");
            } else {
                ResponseEntity<List<BaseProcess>> baseProcesslist = barcodeUtils.deviceInterFaceUtils.getProcess(restapiChkLogUserInfoApiDto.getProcessCode(), orgId);
                if (StringUtils.isEmpty(baseProcesslist.getData())) {
                    throw new Exception(fail + " ????????????,?????????????????????");
                }
                processName = baseProcesslist.getData().get(0).getProcessName();
            }
            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getUserCode())) {
                throw new Exception(fail + " ????????????,??????????????????????????????");
            }

            if (StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getPassword())) {
                throw new Exception(fail + " ????????????,??????????????????????????????");
            }

            //???????????????????????????????????????
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
//                throw new Exception(fail + " ????????????,???????????????????????????");
//            } else {
//                SysUser sysUser = sysUserlist.getData().get(0);
//                Boolean isOK = new BCryptPasswordEncoder().matches(restapiChkLogUserInfoApiDto.getPassword(), sysUser.getPassword());
//                if (!isOK) {
//                    throw new Exception(fail + " ????????????,???????????????????????????");
//                }
//
//                //?????????????????? ??????????????????????????? ??????????????????
//                boolean haveAuth=false;
//                ResponseEntity<List<SysUserRole>> reSysUserRoleList=barcodeUtils.deviceInterFaceUtils.findUserRoleList(userId);
//                if(StringUtils.isEmpty(reSysUserRoleList)){
//                    throw new Exception(fail + " ????????????,??????????????????????????????");
//                }
//                else {
//                    List<SysUserRole> sysUserRoleList=reSysUserRoleList.getData();
//                    for (SysUserRole sysUserRole : sysUserRoleList) {
//                        Long menuId=478L;//????????????id
//                        Long roleId=sysUserRole.getRoleId();
//                        ResponseEntity<SysAuthRole> reSysAuthRole=barcodeUtils.deviceInterFaceUtils.getSysAuthRole(roleId,menuId);
//                        if(StringUtils.isNotEmpty(reSysAuthRole)){
//                            haveAuth=true;
//                            break;
//                        }
//                    }
//                }
//                if(haveAuth==false){
//                    throw new Exception(fail + " ????????????,??????????????????????????????");
//                }
//                userName = sysUser.getUserName();
//            }

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg(pass + ",????????????,????????????:" + proName + ",????????????:" + processName + ",????????????:" + userName);
        }
        catch (Exception ex){
            result=0;
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //???????????????????????????
        end = System.currentTimeMillis();//???????????????
        BigDecimal consumeTime=new BigDecimal((end-start)/1000);//?????? ???
        responseTimeS=DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN);//????????????

        barcodeUtils.deviceInterFaceUtils.addLog(result, (byte) 2, orgId, baseExecuteResultDto.getIsSuccess()?baseExecuteResultDto.getSuccessMsg():baseExecuteResultDto.getFailMsg(),
                restapiChkLogUserInfoApiDto.toString(),consumeTime,requestTimeS,responseTimeS);

        return baseExecuteResultDto;
    }

    public static BaseExecuteResultDto RepairDataTransfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        UpdateProcessDto updateProcessDto=new UpdateProcessDto();
        byte result=1;//????????????(0-?????? 1-??????)
        Long orgId=0L;
        try {

            //?????????????????????
            ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=barcodeUtils.deviceInterFaceUtils.getOrId();
            if(StringUtils.isNotEmpty(baseOrganizationDtoList.getData())){
                //throw new Exception("????????????????????????");
                orgId=baseOrganizationDtoList.getData().get(0).getOrganizationId();
            }

            //????????????ID
            updateProcessDto.setOrgId(orgId);

            //????????????
            String proCode=restapiSNDataTransferApiDto.getProCode();
            if(StringUtils.isEmpty(proCode)){
                throw new Exception("????????????????????????");
            }
            else{
                ResponseEntity<List<BaseProLine>> baseProLinelist=barcodeUtils.deviceInterFaceUtils.getProLine(proCode,orgId);
                if(StringUtils.isEmpty(baseProLinelist.getData())){
                    throw new Exception("?????????????????????");
                }
                //????????????ID
                updateProcessDto.setProLineId(baseProLinelist.getData().get(0).getProLineId());
            }

            //????????????
            String processCode=restapiSNDataTransferApiDto.getProcessCode();
            if(StringUtils.isEmpty(processCode)){
                throw new Exception("????????????????????????");
            }
            else {
                ResponseEntity<List<BaseProcess>> baseProcesslist=barcodeUtils.deviceInterFaceUtils.getProcess(processCode,orgId);
                if(StringUtils.isEmpty(baseProcesslist.getData())){
                    throw new Exception("?????????????????????");

                }
                //????????????ID
                updateProcessDto.setNowProcessId(baseProcesslist.getData().get(0).getProcessId());
            }

            //?????????????????? ????????????
            String opResult=restapiSNDataTransferApiDto.getOpResult();
            if(StringUtils.isEmpty(opResult)){
                throw new Exception("????????????????????????");
            }
            else if(StringUtils.isNotEmpty(opResult)){
                if("OK".equals(opResult)==false && "NG".equals(opResult)==false){
                    throw new Exception("?????????????????? OK ??? NG");
                }
            }

            //????????????
            String partBarcode=restapiSNDataTransferApiDto.getPartBarcode();
            String barcodeCode=restapiSNDataTransferApiDto.getBarCode();
            if(StringUtils.isEmpty(partBarcode) && StringUtils.isEmpty(barcodeCode)){
                throw new Exception("?????????SN?????????SN??????????????????");
            }

            //???????????????
            String workOrderCode=restapiSNDataTransferApiDto.getWorkOrderCode();
            if(StringUtils.isNotEmpty(partBarcode) && StringUtils.isEmpty(workOrderCode)){
                throw new Exception("???????????????????????????");
            }

            MesPmWorkOrderDto mesPmWorkOrderDto=null;
            if(StringUtils.isNotEmpty(workOrderCode)){
                SearchMesPmWorkOrder searchMesPmWorkOrder=new SearchMesPmWorkOrder();
                searchMesPmWorkOrder.setOrgId(orgId);
                searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
                ResponseEntity<List<MesPmWorkOrderDto>> workOrderlist=barcodeUtils.pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
                if(StringUtils.isNotEmpty(workOrderlist.getData())){
                    mesPmWorkOrderDto=workOrderlist.getData().get(0);

                    //????????????ID
                    updateProcessDto.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                    //??????????????????
                    updateProcessDto.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
                    //??????????????????ID
                    //updateProcessDto.setWorkOrderBarcodeId(mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId());
                    //??????????????????ID
                    updateProcessDto.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                    //????????????ID
                    updateProcessDto.setRouteId(mesPmWorkOrderDto.getRouteId());
                    //??????????????????ID
                    updateProcessDto.setPutIntoProcessId(mesPmWorkOrderDto.getPutIntoProcessId());
                    //??????????????????ID
                    updateProcessDto.setOutputProcessId(mesPmWorkOrderDto.getOutputProcessId());
                }

            }

            //???????????????SN ????????? ?????????????????????????????? ?????????????????????????????????
            if(StringUtils.isNotEmpty(partBarcode) && StringUtils.isNotEmpty(mesPmWorkOrderDto)){
                baseExecuteResultDto=createWorkOrderBarcode(partBarcode,"",mesPmWorkOrderDto,orgId);
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());
                else {
                    if(StringUtils.isNotEmpty(partBarcode) && StringUtils.isEmpty(barcodeCode)){
                        //??????????????????ID
                        updateProcessDto.setWorkOrderBarcodeId((Long) baseExecuteResultDto.getExecuteResult());
                    }
                }
            }
            //??????SN,?????????SN???????????? ?????????????????????????????? ?????????????????????????????????
            if(StringUtils.isNotEmpty(barcodeCode) && StringUtils.isNotEmpty(partBarcode) && StringUtils.isNotEmpty(mesPmWorkOrderDto)){
                baseExecuteResultDto=createWorkOrderBarcode(barcodeCode,partBarcode,mesPmWorkOrderDto,orgId);
                if(baseExecuteResultDto.getIsSuccess()==false)
                    throw new Exception(baseExecuteResultDto.getFailMsg());
                else {
                    if(StringUtils.isNotEmpty(barcodeCode)){
                        //??????????????????ID
                        updateProcessDto.setWorkOrderBarcodeId((Long) baseExecuteResultDto.getExecuteResult());
                    }
                }
            }

            //????????????SN
            if(StringUtils.isNotEmpty(barcodeCode) && StringUtils.isEmpty(partBarcode)){
                List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtoList = barcodeUtils.deviceInterFaceUtils.getWorkOrderBarcode(barcodeCode, orgId);
                if(workOrderBarcodeDtoList.size()>0){
                    updateProcessDto.setWorkOrderBarcodeId(workOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId());
                }
            }

            //??????????????????
            String badnessPhenotypeCode=restapiSNDataTransferApiDto.getBadnessPhenotypeCode();
            if (StringUtils.isNotEmpty(badnessPhenotypeCode)) {
                ResponseEntity<List<BaseBadnessPhenotypeDto>> baseBadnessPhenotypeDtoList = barcodeUtils.deviceInterFaceUtils.getBadnessPhenotype(badnessPhenotypeCode, orgId);
                if (StringUtils.isEmpty(baseBadnessPhenotypeDtoList.getData())) {
                    //????????????????????????
                    updateProcessDto.setBadnessPhenotypeCode(badnessPhenotypeCode);
                }
            }

            //??????????????????
            updateProcessDto.setOperatorUserId(1L);

            //?????????????????????ID
            Long partMaterialId=updateProcessDto.getPartMaterialId();

            //??????????????????
            updateProcessDto.setOpResult(restapiSNDataTransferApiDto.getOpResult());

            //?????????????????????????????????
            String barCode=restapiSNDataTransferApiDto.getBarCode();//????????????
            //??????????????????
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

            //????????????????????????
            //?????????????????????????????????  EquipmentIfCheckBarcodeStatus
            String paraValue = getSysSpecItemValue("EquipmentIfCheckBarcodeStatus");
            if ("1".equals(paraValue)) {
                checkProductionDto.setWorkOrderId(updateProcessDto.getWorkOrderId());
                checkProductionDto.setProcessId(updateProcessDto.getNowProcessId());
                checkProductionDto.setProLineId(updateProcessDto.getProLineId());
                checkSN(checkProductionDto,updateProcessDto.getWorkOrderBarcodeId(),orgId);
            }

            //??????
            barcodeUtils.updateBarcodeProcess(updateProcessDto);

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg(" ???????????? ");
        }catch (Exception ex) {
            result=0;
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //???????????????????????????
        BigDecimal consum=new BigDecimal(0);
        barcodeUtils.deviceInterFaceUtils.addLog(result, (byte) 2, orgId, baseExecuteResultDto.getIsSuccess()?baseExecuteResultDto.getSuccessMsg():baseExecuteResultDto.getFailMsg(),
                restapiSNDataTransferApiDto.toString(),consum,"","");
        return baseExecuteResultDto;
    }

    /*
    * ??????????????????????????????????????????
    * productionSn ????????????
    * halfProductionSn ???????????????
    * processCode ????????????
    * orgId ??????ID
    */

    public static BaseExecuteResultDto checkProHalfProRelation(String productionSn,String halfProductionSn,Long processId,Long workOrderId,Long materialId,Long partMaterialId,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            //?????????????????????????????????????????????????????????  ProductIfCheckHalfProductionRelation
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
            * 1 ??????????????????????????????
            * 2 ?????????????????????????????????
            * 2.1 ?????? ????????????????????????????????????
            * 3 ???????????????????????? ???????????????????????????????????????(WorkOrderPositionOnBarcode)???????????????????????????
            * 3.1 ????????????????????????????????????-->???????????????
            * 4 ??????????????????bom???????????????????????????????????????????????????
            * 4.1 ??????????????? ????????????BOM???????????????????????????????????????
            * 5 ???????????????????????? ??????
             */

            //??????????????????
//            if(StringUtils.isNotEmpty(productionSn)) {
//                baseExecuteResultDto = checkBarcodeStatus(productionSn, orgId, "");
//                if (baseExecuteResultDto.getIsSuccess() == false)
//                    throw new Exception(baseExecuteResultDto.getFailMsg());
//            }

            //?????????????????????
//            if(StringUtils.isNotEmpty(halfProductionSn)) {
//                baseExecuteResultDto = checkBarcodeStatus(halfProductionSn, orgId, "");
//                if (baseExecuteResultDto.getIsSuccess() == false) {
//                    throw new Exception(baseExecuteResultDto.getFailMsg());
//                }
//            }

            //?????????BOM?????????????????????  ???????????? ?????????BOM??????
            Boolean isExist=findBomExistMaterialId(materialId,partMaterialId,orgId);

            if(isExist==false) {
                SearchMesPmWorkOrderBom searchMesPmWorkOrderBom = new SearchMesPmWorkOrderBom();
                searchMesPmWorkOrderBom.setWorkOrderId(workOrderId);
                searchMesPmWorkOrderBom.setPartMaterialId(partMaterialId);
                //searchMesPmWorkOrderBom.setProcessId(processId);
                ResponseEntity<List<MesPmWorkOrderBomDto>> responseEntityBom = barcodeUtils.deviceInterFaceUtils.getWorkOrderBomList(searchMesPmWorkOrderBom);
                if (StringUtils.isEmpty(responseEntityBom.getData())) {

                    //?????????????????????
                    String partMaterialCode="";
                    if(StringUtils.isNotEmpty(partMaterialId)) {
                        ResponseEntity<BaseMaterial> baseMaterialEntity = barcodeUtils.baseFeignApi.materialDetail(partMaterialId);
                        if(StringUtils.isEmpty(baseMaterialEntity.getData())){
                            throw new Exception("???????????????????????????ID?????????????????????-->"+partMaterialId.toString());
                        }
                        partMaterialCode=baseMaterialEntity.getData().getMaterialCode();
                    }

                    //??????????????????
                    String materialCode="";
                    if(StringUtils.isNotEmpty(materialId)) {
                        ResponseEntity<BaseMaterial> baseMaterialEntity = barcodeUtils.baseFeignApi.materialDetail(materialId);
                        if(StringUtils.isEmpty(baseMaterialEntity.getData())){
                            throw new Exception("????????????????????????ID?????????????????????-->"+materialId.toString());
                        }
                        materialCode=baseMaterialEntity.getData().getMaterialCode();
                    }

                    //??????BOM????????????????????????
                    //??????????????????????????????BOM ProductBomCheckRelation
//                    String paraValue = getSysSpecItemValue("ProductBomCheckRelation");
//                    if ("1".equals(paraValue)) {
                    //??????BOM?????????????????????
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
//                                throw new Exception("????????????????????????????????????????????????????????????");
//                            }
//                        }

//                    }

                    throw new Exception("???????????????????????????????????????????????????????????? ??????????????????-->"+materialCode+" ?????????????????????-->"+partMaterialCode);
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
    * ????????????????????????
    * ???????????????
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
             * ???????????????(0-????????? 1-????????? 2-????????? 3-?????????)
             */
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
            if (mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == (byte)2 || mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == (byte)3) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012004, mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 ? "?????????" : "?????????");
            }

            baseExecuteResultDto.setIsSuccess(true);
            //baseExecuteResultDto.setSuccessMsg("????????????");
            baseExecuteResultDto.setExecuteResult(mesSfcWorkOrderBarcodeDto);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }
        return baseExecuteResultDto;
    }

    /////////////////////////////////////////
    /*
     * ?????????????????????????????????
     * workOrderCode ????????????
     */
    public static BaseExecuteResultDto checkPmProKeyIssues(String workOrderCode,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            //????????????????????????????????????????????????  WorkOrderIfNeedProductionKeyIssues
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
                throw new Exception("?????????????????????????????????");
            }
            else {
                MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder = PmPKIOList.getData().get(0);
                if (mesPmProductionKeyIssuesOrder.getOrderStatus()!=(byte)2) {
                    throw new Exception("?????????????????????????????????");
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
    * ????????????????????????
    * specCode ???????????????
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
     * ????????????
     * workOrderId ??????ID
     * productionSn ????????????
     * halfProductionSn ??????????????????????????????
     * proLineId ??????ID
     * processId ??????ID
     * stationId ??????ID
     * materialId ????????????ID
     * userId ????????????ID
     * orgId ??????ID
     */
    public static BaseExecuteResultDto bandingWorkOrderBarcode(Long workOrderId,Long partMaterialId,String productionSn,String halfProductionSn,
            Long workOrderBarcodeId,Long proLineId,Long processId,Long stationId,Long materialId,Long userId,Long orgId) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            if(StringUtils.isNotEmpty(halfProductionSn)){

                /*
                * 1 ????????????BOM??????
                * 2 ???????????????
                * 2 ??????????????? usageQty ?????????????????????
                * 3 ?????????mes_sfc_key_part_relevance ????????????-?????????????????????
                */

//                SearchMesPmWorkOrderBom searchMesPmWorkOrderBom = new SearchMesPmWorkOrderBom();
//                searchMesPmWorkOrderBom.setWorkOrderId(workOrderId);
//                searchMesPmWorkOrderBom.setPartMaterialId(partMaterialId);
//                searchMesPmWorkOrderBom.setProcessId(processId);
//                ResponseEntity<List<MesPmWorkOrderBomDto>> responseEntityBom = barcodeUtils.deviceInterFaceUtils.getWorkOrderBomList(searchMesPmWorkOrderBom);
//                if(StringUtils.isEmpty(responseEntityBom.getData())){
//                    throw new Exception("????????????????????????????????????????????????");
//                }

//                MesPmWorkOrderBomDto mesPmWorkOrderBomDto=responseEntityBom.getData().get(0);
                // ??????????????????????????????
                Example example = new Example(MesSfcKeyPartRelevance.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("partBarcode", halfProductionSn);
                int countByExample = barcodeUtils.mesSfcKeyPartRelevanceService.selectCountByExample(example);
                if (countByExample > 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012028,"??????????????????????????????????????????????????????");
                }

                Map<String, Object> map = new HashMap<>();
                // ????????????????????????
                map.clear();
                map.put("workOrderId", workOrderId);
                map.put("processId", processId);
                //map.put("materialId", mesPmWorkOrder.getMaterialId());
                map.put("workOrderBarcodeId", workOrderBarcodeId);
                List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = barcodeUtils.mesSfcKeyPartRelevanceService.findList(map);
                // ?????????????????????????????????????????? mesPmWorkOrderBomDto.getUsageQty().intValue()
                if (keyPartRelevanceDtos.size() >= 1) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012020,"?????????????????????????????????");
                }

                BaseProLine proLine = barcodeUtils.baseFeignApi.selectProLinesDetail(proLineId).getData();
                BaseProcess baseProcess = barcodeUtils.baseFeignApi.processDetail(processId).getData();
                //BaseStation baseStation = barcodeUtils.baseFeignApi.findStationDetail(stationId).getData();

                BaseStation baseStation=new BaseStation();
                if(StringUtils.isNotEmpty(stationId)){
                    baseStation = barcodeUtils.baseFeignApi.findStationDetail(stationId).getData();
                }

                // ??????????????????????????????
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
     * ?????????????????????
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
                    mesSfcWorkOrderBarcode.setLabelCategoryId(barcodeUtils.mesSfcWorkOrderBarcodeMapper.finByTypeId("????????????"));
                    mesSfcWorkOrderBarcode.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                    mesSfcWorkOrderBarcode.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
                    mesSfcWorkOrderBarcode.setBarcodeStatus((byte) 1);//????????????(0-????????? 1-????????? 2-????????? 3-?????????)
                    mesSfcWorkOrderBarcode.setOrgId(orgId);
                    mesSfcWorkOrderBarcode.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                    mesSfcWorkOrderBarcode.setWorkOrderQty(mesPmWorkOrderDto.getWorkOrderQty());

                    mesSfcWorkOrderBarcode.setCreateTime(new Date());
                    mesSfcWorkOrderBarcode.setCreateUserId(1L);
                    mesSfcWorkOrderBarcode.setModifiedTime(new Date());
                    mesSfcWorkOrderBarcode.setModifiedUserId(1L);
                    mesSfcWorkOrderBarcode.setCreateBarcodeTime(new Date());
                    barcodeUtils.mesSfcWorkOrderBarcodeMapper.insertUseGeneratedKeys(mesSfcWorkOrderBarcode);

                    //????????????????????????
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

                        //??????????????????
                        ResponseEntity<List<BaseRouteProcess>> res = barcodeUtils.baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
                        if (res.getCode() != 0) {
                            throw new BizErrorException("????????????????????????");
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
                            throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(), "??????????????????-->" + barcode);
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
                    //??????????????????
                    MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = new MesSfcWorkOrderBarcode();
                    mesSfcWorkOrderBarcode.setBarcode(barcode);
                    mesSfcWorkOrderBarcode.setLabelCategoryId(barcodeUtils.mesSfcWorkOrderBarcodeMapper.finByTypeId("????????????"));
                    mesSfcWorkOrderBarcode.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                    mesSfcWorkOrderBarcode.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
                    mesSfcWorkOrderBarcode.setBarcodeStatus((byte) 1);//????????????(0-????????? 1-????????? 2-????????? 3-?????????)
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
                        //??????????????????????????????=???????????????????????????
                        if(StringUtils.isEmpty(mesSfcBarcodeProcessExist)) {
                            MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                            BeanUtils.copyProperties(mesSfcHalfBarcodeProcess, mesSfcBarcodeProcess);
                            mesSfcBarcodeProcess.setBarcode(barcode);
                            mesSfcBarcodeProcess.setCustomerBarcode(partBarcode);
                            mesSfcBarcodeProcess.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
                            mesSfcBarcodeProcess.setBarcodeProcessId(null);
                            if (barcodeUtils.mesSfcBarcodeProcessMapper.insertSelective(mesSfcBarcodeProcess) < 1) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(), "??????????????????-->" + barcode);
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
                    //??????????????????
                    MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = new MesSfcWorkOrderBarcode();
                    mesSfcWorkOrderBarcode.setBarcode(barcode);
                    mesSfcWorkOrderBarcode.setLabelCategoryId(barcodeUtils.mesSfcWorkOrderBarcodeMapper.finByTypeId("????????????"));
                    mesSfcWorkOrderBarcode.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                    mesSfcWorkOrderBarcode.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
                    mesSfcWorkOrderBarcode.setBarcodeStatus((byte) 1);//????????????(0-????????? 1-????????? 2-????????? 3-?????????)
                    mesSfcWorkOrderBarcode.setOrgId(orgId);
                    mesSfcWorkOrderBarcode.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                    mesSfcWorkOrderBarcode.setWorkOrderQty(mesPmWorkOrderDto.getWorkOrderQty());

                    mesSfcWorkOrderBarcode.setCreateTime(new Date());
                    mesSfcWorkOrderBarcode.setCreateUserId(1L);
                    mesSfcWorkOrderBarcode.setModifiedTime(new Date());
                    mesSfcWorkOrderBarcode.setModifiedUserId(1L);
                    mesSfcWorkOrderBarcode.setCreateBarcodeTime(new Date());
                    barcodeUtils.mesSfcWorkOrderBarcodeMapper.insertUseGeneratedKeys(mesSfcWorkOrderBarcode);

                    //????????????????????????
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

                        //??????????????????
                        ResponseEntity<List<BaseRouteProcess>> res = barcodeUtils.baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
                        if (res.getCode() != 0) {
                            throw new BizErrorException("????????????????????????");
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
                            throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(), "??????????????????-->" + barcode);
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
                    //throw new Exception("????????????????????????????????????????????????????????????");
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
