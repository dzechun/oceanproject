package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingListDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.dto.wms.in.BarPODto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.wanbao.WanbaoFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MesSfcPalletWorkServiceImpl implements MesSfcPalletWorkService {

    // region Service ??????

    @Resource
    MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;
    @Resource
    MesSfcProductPalletService mesSfcProductPalletService;
    @Resource
    MesSfcProductPalletDetService mesSfcProductPalletDetService;
    @Resource
    MesSfcProductCartonService mesSfcProductCartonService;
    @Resource
    MesSfcProductCartonDetService mesSfcProductCartonDetService;
    @Resource
    MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcBarcodeProcessRecordService mesSfcBarcodeProcessRecordService;
    @Resource
    PMFeignApi pmFeignApi;
    @Resource
    BaseFeignApi baseFeignApi;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    InFeignApi inFeignApi;
    @Resource
    SecurityFeignApi securityFeignApi;
    @Resource
    OMFeignApi omFeignApi;
    @Resource
    WanbaoFeignApi wanbaoFeignApi;
    @Resource
    InnerFeignApi innerFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;

    // endregion

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PalletWorkScanDto palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception {
        log.info("===================   ??????  =================");
        long curretTime = System.currentTimeMillis();
        Long startTime = System.currentTimeMillis();
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        // ????????????
        Map<String, Object> cleanBarcodeMap = this.cleanBarcode(requestPalletWorkScanDto.getBarcode());
        String barcode = cleanBarcodeMap.get("barcode").toString();
        Long workOrderBarcodeId = Long.parseLong(cleanBarcodeMap.get("workOrderBarcodeId").toString());
        Long workOrderId = Long.parseLong(cleanBarcodeMap.get("workOrderId").toString());
        log.info("=============== ?????????????????????" + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        // 2022-03-08 ?????????????????????????????????????????????
        WmsInnerInventoryDet innerInventoryDet = innerFeignApi.findByDet(barcode).getData();
        if (StringUtils.isNotEmpty(innerInventoryDet)){
            return new PalletWorkScanDto();
        }
        log.info("=============== ??????????????????????????????????????????????????????" + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        // ?????????????????????
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeId", workOrderBarcodeId);
        List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
        if (processServiceList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????");
        }
        if (!processServiceList.get(0).getProLineId().equals(requestPalletWorkScanDto.getProLineId())) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????");
        }
        MesSfcBarcodeProcessDto barcodeProcessDto = processServiceList.get(0);
        if (barcodeProcessDto.getNextProcessId().equals(0L)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????????????????");
        }
        if (!barcodeProcessDto.getNextProcessId().equals(requestPalletWorkScanDto.getProcessId())) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "PDA????????????????????????????????????");
        }

        // ????????????
        MesPmWorkOrder pmWorkOrder = pmFeignApi.workOrderDetail(workOrderId).getData();
        if (StringUtils.isEmpty(pmWorkOrder)) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????????????????????????????????????????");
        }
        if (!pmWorkOrder.getProLineId().equals(requestPalletWorkScanDto.getProLineId())) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "????????????????????????????????????????????????");
        }
        if (pmWorkOrder.getWorkOrderStatus() > 3) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012032);
        }

        // ?????????
        MesSfcWorkOrderBarcode workOrderBarcode = mesSfcWorkOrderBarcodeService.selectByKey(workOrderBarcodeId);
        log.info("=============== ?????????????????????/????????????/??????????????????" + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        // ????????????/??????????????????
        long start = System.currentTimeMillis();

        // ?????????????????????
        ResponseEntity<List<BaseRouteProcess>> responseEntity = baseFeignApi.findConfigureRout(barcodeProcessDto.getRouteId());
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012008);
        }
        List<BaseRouteProcess> routeProcessList = responseEntity.getData();
        // ???????????????????????????????????????,?????????????????????????????????????????????
        if (!barcodeProcessDto.getNextProcessId().equals(pmWorkOrder.getOutputProcessId())) {
            // ???????????????????????????ID????????????????????????ID?????????
            // ????????????????????????????????????????????????
            if (!requestPalletWorkScanDto.getProcessId().equals(barcodeProcessDto.getNextProcessId())) {
                Optional<BaseRouteProcess> routeProcessOptional = routeProcessList.stream()
                        .filter(i -> barcodeProcessDto.getNextProcessId().equals(i.getProcessId()))
                        .findFirst();
                if (!routeProcessOptional.isPresent()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012009, barcodeProcessDto.getNextProcessName());
                }
                BaseRouteProcess routeProcess = routeProcessOptional.get();
                if (routeProcess.getIsMustPass() == 1) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012010, barcodeProcessDto.getNextProcessName());
                }
            }
        }
        ResponseEntity<BaseProcess> processResponseEntity = baseFeignApi.processDetail(requestPalletWorkScanDto.getProcessId());
        if (processResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012012, requestPalletWorkScanDto.getProcessId());
        }
        long one = System.currentTimeMillis();
        log.info("============== ??????????????????:" + (one - start));

        BaseProcess baseProcess = processResponseEntity.getData();
        // ??????????????????
        barcodeProcessDto.setProcessId(requestPalletWorkScanDto.getProcessId());
        barcodeProcessDto.setProcessCode(baseProcess.getProcessCode());
        barcodeProcessDto.setProcessName(baseProcess.getProcessName());
        // ??????????????????????????????
        if (StringUtils.isNotEmpty(requestPalletWorkScanDto.getStationId())) {
            BaseStation baseStation = baseFeignApi.findStationDetail(requestPalletWorkScanDto.getStationId()).getData();
            if (baseStation == null) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "???????????????????????????");
            }
            barcodeProcessDto.setStationId(baseStation.getStationId());
            barcodeProcessDto.setStationCode(baseStation.getStationCode());
            barcodeProcessDto.setStationName(baseStation.getStationName());
        }
        long two = System.currentTimeMillis();
        log.info("============== ????????????:" + (two - one));

        // ????????????
        ResponseEntity<BaseWorkshopSection> bwssResponseEntity = baseFeignApi.sectionDetail(baseProcess.getSectionId());
        BaseWorkshopSection baseWorkshopSection = bwssResponseEntity.getData();
        if (baseWorkshopSection == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012035);
        }
        barcodeProcessDto.setSectionId(baseProcess.getSectionId());//??????id
        barcodeProcessDto.setSectionCode(baseWorkshopSection.getSectionCode());//??????code
        barcodeProcessDto.setSectionName(baseWorkshopSection.getSectionName());//????????????
        long two1 = System.currentTimeMillis();
        log.info("============== ????????????:" + (two1 - two));

        if (barcodeProcessDto.getNextProcessId().equals(pmWorkOrder.getOutputProcessId())) {
            // ??????????????????????????????????????????
            barcodeProcessDto.setProductionTime(new Date());
            barcodeProcessDto.setNextProcessId(0L);
            barcodeProcessDto.setNextProcessName("");
            barcodeProcessDto.setNextProcessCode("");

            // ?????????????????????????????????????????????????????????????????????????????????????????????????????? +1
            pmWorkOrder.setOutputQty(pmWorkOrder.getOutputQty() != null ? BigDecimal.ONE.add(pmWorkOrder.getOutputQty()) : BigDecimal.ONE);
            if (pmWorkOrder.getOutputQty().compareTo(pmWorkOrder.getWorkOrderQty()) == 0) {
                // ?????????????????????????????????????????????
                pmWorkOrder.setWorkOrderStatus((byte) 6);
                pmWorkOrder.setActualEndTime(new Date());
                pmWorkOrder.setModifiedTime(new Date());
                pmWorkOrder.setModifiedUserId(user.getUserId());
            }
            pmFeignApi.updatePmWorkOrder(pmWorkOrder);
            long six = System.currentTimeMillis();
            log.info("============== ????????????:" + (six - two1));
            /**
             * ?????????20211109
             * ??????????????????  ????????????(0-????????? 1-????????? 2-????????? 3-?????????)
             */
            workOrderBarcode.setBarcodeStatus((byte) 2);
            mesSfcWorkOrderBarcodeService.update(workOrderBarcode);
            long six1 = System.currentTimeMillis();
            log.info("============== ??????????????????:" + (six1 - six));

            /**
             * 20211215 bgkun
             * ???????????????????????????????????????????????????
             */
            this.updateAttachmentCodeStatus(workOrderBarcodeId);

            long six2 = System.currentTimeMillis();
            log.info("============== ???????????????????????????:" + (six2 - six1));
        }
        startTime = System.currentTimeMillis();

        barcodeProcessDto.setOutProcessTime(new Date());
        barcodeProcessDto.setOperatorUserId(user.getUserId());
        barcodeProcessDto.setModifiedUserId(user.getUserId());
        barcodeProcessDto.setModifiedTime(new Date());
        int update = mesSfcBarcodeProcessService.update(barcodeProcessDto);
        if (update < 1) {
            throw new RuntimeException("????????????????????????????????????");
        }

        // ??????????????????
        /*MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = new MesSfcBarcodeProcessRecord();
        BeanUtils.copyProperties(barcodeProcessDto, mesSfcBarcodeProcessRecord);
        mesSfcBarcodeProcessRecord.setOperatorUserId(user.getUserId());
        mesSfcBarcodeProcessRecord.setModifiedTime(new Date());
        mesSfcBarcodeProcessRecord.setModifiedUserId(user.getUserId());
        mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord);*/

        long four = System.currentTimeMillis();
        log.info("============== ??????????????????:"+ (four - startTime));

        // ???????????????
        PalletWorkScanDto palletWorkScanDto = new PalletWorkScanDto();
//        palletWorkScanDto.setPalletCode(palletCode);
//        palletWorkScanDto.setProductPalletId(mesSfcProductPallet.getProductPalletId());
        palletWorkScanDto.setWorkOrderCode(pmWorkOrder.getWorkOrderCode());
        palletWorkScanDto.setMaterialCode(barcodeProcessDto.getMaterialCode());
        palletWorkScanDto.setMaterialDesc(barcodeProcessDto.getMaterialName());
        palletWorkScanDto.setWorkOrderQty(pmWorkOrder.getWorkOrderQty());
        palletWorkScanDto.setProductionQty(pmWorkOrder.getProductionQty());
        palletWorkScanDto.setClosePalletNum(BigDecimal.ONE);
        palletWorkScanDto.setNowPackageSpecQty(BigDecimal.ONE);
        palletWorkScanDto.setScanCartonNum(1);

        log.info("=============== ??????????????????" + (System.currentTimeMillis() - curretTime));

        //?????????????????????????????????mq??????????????????????????????
        if(ObjectUtil.isNull(requestPalletWorkScanDto.getIsReadHead()) || !requestPalletWorkScanDto.getIsReadHead()){
            log.info("============== ?????????????????????????????????mq??????????????????ID???{}, ??????ID:{}",requestPalletWorkScanDto.getProLineId(),requestPalletWorkScanDto.getStationId());
            String topic = requestPalletWorkScanDto.getProLineId() + "_" + requestPalletWorkScanDto.getStationId();
            String code = "{\"code\":\"0\"}";
            rabbitProducer.sendMakeUp(code,topic);
        }
        return palletWorkScanDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int workByManualOperation(PalletWorkByManualOperationDto dto) throws Exception {
        long startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCode", dto.getStackingCode());
        List<WanbaoStackingDto> stackingList = mesSfcProductPalletService.findStackingList(map);
        if (stackingList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????");
        }
        long stackingTime = System.currentTimeMillis();
        log.info("=========== ??????????????????:" + (stackingTime-startTime));

        try {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (dto.getWanbaoBarcodeDtos() == null || dto.getWanbaoBarcodeDtos().isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "??????????????????????????????");
            }
            List<String> barcodeList = dto.getWanbaoBarcodeDtos().stream().map(WanbaoBarcodeDto::getBarcode).collect(Collectors.toList());
            SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
            searchMesSfcWorkOrderBarcode.setBarcodeList(barcodeList);
            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
            if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????");
            }
            long WorkOrderBarcodeTime = System.currentTimeMillis();
            log.info("=========== ??????????????????????????????:" + (WorkOrderBarcodeTime-stackingTime));

            // ????????????????????????
            map.clear();
            map.put("barcodeList", barcodeList);
            byte palletType = dto.getPalletType();
            if (palletType == 2) {
                String count = mesSfcBarcodeProcessService.countBarcodeListForPOGroup(map);
                if (count != null && Integer.parseInt(count) != 1) {
                    palletType = (byte) 3;
                }
            }
            if (palletType == 3) {
                String count = mesSfcBarcodeProcessService.countBarcodeListForSalesOrder(map);
                if (count != null && Integer.parseInt(count) != 1) {
                    palletType = (byte) 1;
                }
            }
            if (palletType == 1) {
                // ?????????
                String count = mesSfcBarcodeProcessService.countBarcodeListForMaterial(map);
                if (count != null && Integer.parseInt(count) > 1) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????????????????");
                }
            }
            long checkbarcodeTime = System.currentTimeMillis();
            log.info("=========== ????????????????????????:" + (checkbarcodeTime-WorkOrderBarcodeTime));

            // ????????????????????????
            List<WanbaoStackingDet> stackingDets = new ArrayList<>();
            for (WanbaoBarcodeDto wanbaoBarcodeDto : dto.getWanbaoBarcodeDtos()) {
                WanbaoStackingDet stackingDet = new WanbaoStackingDet();
                BeanUtil.copyProperties(wanbaoBarcodeDto, stackingDet);
                stackingDet.setStackingId(stackingList.get(0).getStackingId());
                for (MesSfcWorkOrderBarcodeDto workOrderBarcodeDto : mesSfcWorkOrderBarcodeDtoList) {
                    if (workOrderBarcodeDto.getBarcode().equals(wanbaoBarcodeDto.getBarcode())) {
                        wanbaoBarcodeDto.setWorkOrderId(workOrderBarcodeDto.getWorkOrderId());
                        wanbaoBarcodeDto.setMaterialId(workOrderBarcodeDto.getMaterialId());
                        break;
                    }
                }
                stackingDets.add(stackingDet);
            }
            wanbaoFeignApi.batchAdd(stackingDets);
            long saveBarcodeStackingTime = System.currentTimeMillis();
            log.info("=========== ??????????????????????????????:" + (saveBarcodeStackingTime-checkbarcodeTime));

            // ??????????????????????????????
            this.beforePalletAutoAsnOrder(stackingList.get(0).getStackingId(), user.getOrganizationId(), dto.getWanbaoBarcodeDtos(), barcodeList, mesSfcWorkOrderBarcodeDtoList);
            long beforePalletAutoAsnOrderTime = System.currentTimeMillis();
            log.info("=========== ????????????????????????????????????:" + (beforePalletAutoAsnOrderTime-saveBarcodeStackingTime));

            // A??????????????????????????????MQ
            /*if (stackingList.get(0).getProCode().equals("A") && (dto.getIsReadHead() == null || !dto.getIsReadHead())) {
                WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
                wanbaoStackingMQDto.setCode(0);
                wanbaoStackingMQDto.setStackingCode(stackingList.get(0).getStackingCode());
                String stackingLine = stackingList.get(0).getStackingCode().substring(stackingList.get(0).getStackingCode().length() - 1);
                wanbaoStackingMQDto.setStackingLine(stackingLine);
                // ??????????????????mq
                rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            }*/
        } catch (Exception e) {
            if (!stackingList.isEmpty() && stackingList.get(0).getProCode().equals("A")) {
                WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
                wanbaoStackingMQDto.setCode(500);
                // ??????????????????mq
                rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            }
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), e.getMessage());
        }
        return 1;
    }


    @Override
    public ScanByManualOperationDto scanByManualOperation(String barcode, Long proLineId) {
        long startTime = System.currentTimeMillis();
        String customerBarcode = null;
        if (barcode.length() != 23) {
            // ??????????????????????????????
            Example example = new Example(MesSfcBarcodeProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("customerBarcode", barcode + "%");
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = mesSfcBarcodeProcessService.selectByExample(example);
            if (!mesSfcBarcodeProcesses.isEmpty()) {
                customerBarcode = barcode;
                barcode = mesSfcBarcodeProcesses.get(0).getBarcode();
            }
        }
        long custBarcode = System.currentTimeMillis();
        log.info("=========== ????????????????????????????????????????????????:" + (custBarcode-startTime));
        ScanByManualOperationDto dto = new ScanByManualOperationDto();

        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(barcode);
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????");
        }
        long WorkOrderBarcode = System.currentTimeMillis();
        log.info("=========== ????????????????????????????????? :" + (WorkOrderBarcode-custBarcode));
        SearchBaseLabelCategory baseLabelCategory = new SearchBaseLabelCategory();
        baseLabelCategory.setPageSize(9999);
        List<BaseLabelCategoryDto> labelCategoryDtoList = baseFeignApi.findLabelCategoryList(baseLabelCategory).getData();
        log.info("======== labelCategoryDtoList: " + JSON.toJSONString(labelCategoryDtoList));
        BaseLabelCategory labelCategory = new BaseLabelCategory();
        for (BaseLabelCategoryDto categoryDto : labelCategoryDtoList){
            if (mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId().equals(categoryDto.getLabelCategoryId())){
                BeanUtil.copyProperties(categoryDto, labelCategory);
                log.info("======== categoryDto: " + JSON.toJSONString(categoryDto));
                log.info("======== labelCategory11222: " + JSON.toJSONString(labelCategory));
                break;
            }
        }
        log.info("======== labelCategory: " + JSON.toJSONString(labelCategory));
        log.info("======== mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId(): " + JSON.toJSONString(mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId()));
        if (StringUtils.isEmpty(labelCategory) || StringUtils.isEmpty(labelCategory.getLabelCategoryId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????????????????");
        }

        long labelCategoryTime = System.currentTimeMillis();
        log.info("=========== ???????????????????????? :" + (labelCategoryTime-WorkOrderBarcode));
        if (labelCategory.getLabelCategoryCode().equals("01")) {
            // 2022-03-08 ?????????????????????????????????????????????
            WmsInnerInventoryDet innerInventoryDet = innerFeignApi.findByDet(barcode).getData();
            if (StringUtils.isNotEmpty(innerInventoryDet)){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????????????????????????????");
            }
            long InventoryDet = System.currentTimeMillis();
            log.info("=========== ?????????????????????????????????????????????01??????:" + (InventoryDet - labelCategoryTime));

            // ????????????
            Map<String, Object> map = new HashMap<>();
            map.put("barcodeCode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            long KeyPartTime = System.currentTimeMillis();
            log.info("=========== ??????keypart??????:" + (KeyPartTime - InventoryDet));

            map.clear();
            map.put("barcode", barcode);
            List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
            if (processServiceList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????");
            }
            if (!processServiceList.get(0).getProLineId().equals(proLineId)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????");
            }
            if (processServiceList.get(0).getNextProcessId() != 0L) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????");
            }
            long BarcodeProcessTime = System.currentTimeMillis();
            log.info("=========== ???????????????????????????:" + (BarcodeProcessTime - KeyPartTime));
            if (!mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                if (customerBarcode != null) {
                    dto.setCustomerBarcode(customerBarcode);
                } else {
                    for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : mesSfcKeyPartRelevanceDtoList) {
                        BaseLabelCategory keyPartLabelCategory = new BaseLabelCategory();
                        for (BaseLabelCategoryDto categoryDto : labelCategoryDtoList){
                            if (keyPartRelevanceDto.getLabelCategoryId().equals(categoryDto.getLabelCategoryId())){
                                BeanUtil.copyProperties(categoryDto, keyPartLabelCategory);
                                break;
                            }
                        }
                        if (keyPartLabelCategory.getLabelCategoryCode().equals("02")) {
                            // ??????????????????
                            dto.setSalesBarcode(keyPartRelevanceDto.getPartBarcode());
                        } else if (keyPartLabelCategory.getLabelCategoryCode().equals("03")) {
                            // ????????????
                            dto.setCustomerBarcode(keyPartRelevanceDto.getPartBarcode());
                        }
                    }
                }
                dto.setProName(mesSfcKeyPartRelevanceDtoList.get(0).getProName());
                long buildBarcodeTime = System.currentTimeMillis();
                log.info("=========== ???????????????????????????:" + (buildBarcodeTime - BarcodeProcessTime));
            } else {
                BaseProLine baseProLine = baseFeignApi.selectProLinesDetail(proLineId).getData();
                dto.setProName(baseProLine.getProName());
            }

            dto.setBarcode(barcode);
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId());
            List<MesPmWorkOrderDto> orderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (orderDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????????????????????????????????????????");
            }
            dto.setMaterialCode(orderDtos.get(0).getMaterialCode());
            dto.setMaterialDesc(orderDtos.get(0).getMaterialName());
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("partBarcode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            if (mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????");
            }
            if (labelCategory.getLabelCategoryCode().equals("02")) {
                // ??????????????????
                dto.setSalesBarcode(barcode);
            } else if (labelCategory.getLabelCategoryCode().equals("03")) {
                // ????????????
                dto.setCustomerBarcode(barcode);
            }
            long KeyPartTime = System.currentTimeMillis();
            log.info("=========== ??????KeyPart NOT01??????:" + (KeyPartTime - labelCategoryTime));

            // 2022-03-08 ?????????????????????????????????????????????
            WmsInnerInventoryDet innerInventoryDet = innerFeignApi.findByDet(mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode()).getData();
            if (StringUtils.isNotEmpty(innerInventoryDet)){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????????????????????????????");
            }
            long InventoryDet = System.currentTimeMillis();
            log.info("=========== ?????????????????????????????????????????????NOT01??????:" + (InventoryDet - KeyPartTime));

            map.clear();
            map.put("barcode", mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
            if (processServiceList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????");
            }
            if (!processServiceList.get(0).getProLineId().equals(proLineId)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????");
            }
            if (processServiceList.get(0).getNextProcessId() != 0L) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????");
            }
            dto.setBarcode(mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            dto.setProName(mesSfcKeyPartRelevanceDtoList.get(0).getProName());
            long BarcodeProcessTime = System.currentTimeMillis();
            log.info("=========== ??????????????????NOT01??????:" + (BarcodeProcessTime - InventoryDet));

            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId());
            List<MesPmWorkOrderDto> orderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (orderDtos.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????????????????????????????????????????");
            }
            long WorkOrderTime = System.currentTimeMillis();
            log.info("=========== ????????????NOT01??????:" + (WorkOrderTime - BarcodeProcessTime));
            dto.setMaterialCode(orderDtos.get(0).getMaterialCode());
            dto.setMaterialDesc(orderDtos.get(0).getMaterialName());
            map.clear();
            map.put("barcodeCode", dto.getBarcode());
            mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : mesSfcKeyPartRelevanceDtoList) {
                BaseLabelCategory keyPartLabelCategory = new BaseLabelCategory();
                for (BaseLabelCategoryDto categoryDto : labelCategoryDtoList){
                    if (keyPartRelevanceDto.getLabelCategoryId().equals(categoryDto.getLabelCategoryId())){
                        BeanUtil.copyProperties(categoryDto, keyPartLabelCategory);
                        break;
                    }
                }
                if (labelCategory.getLabelCategoryCode().equals("02") && keyPartLabelCategory.getLabelCategoryCode().equals("03")) {
                    // ????????????
                    dto.setCustomerBarcode(keyPartRelevanceDto.getPartBarcode());
                } else if (labelCategory.getLabelCategoryCode().equals("03") && keyPartLabelCategory.getLabelCategoryCode().equals("02")) {
                    // ??????????????????
                    dto.setSalesBarcode(keyPartRelevanceDto.getPartBarcode());
                }
            }
        }
        log.info("=========== ?????????????????????:" + (System.currentTimeMillis() - startTime));
        return dto;
    }

    @Override
    public List<PalletWorkScanDto> palletWorkScan(Long stationId) {

        List<PalletWorkScanDto> palletWorkScanDtoList = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("closeStatus", 0);
        List<MesSfcProductPalletDto> mesSfcProductPalletDtoList = mesSfcProductPalletService.findList(map);
        for (MesSfcProductPalletDto mesSfcProductPalletDto : mesSfcProductPalletDtoList) {
            PalletWorkScanDto palletWorkScanDto = new PalletWorkScanDto();
            BeanUtils.copyProperties(mesSfcProductPalletDto, palletWorkScanDto);
            map.clear();
            map.put("workOrderId", mesSfcProductPalletDto.getWorkOrderId());
            map.put("closeStatus", 1);
            List<MesSfcProductPalletDto> mesSfcProductPalletDtos = mesSfcProductPalletService.findList(map);
            palletWorkScanDto.setClosePalletNum(BigDecimal.valueOf(mesSfcProductPalletDtos.size()));
            int palletCartons = findPalletCarton(mesSfcProductPalletDto.getProductPalletId()).size();
            palletWorkScanDto.setScanCartonNum(palletCartons);
            // ??????????????????
            List<String> cartonCodeList = this.findPalletCarton(mesSfcProductPalletDto.getProductPalletId());
            palletWorkScanDto.setCartonCodeList(cartonCodeList);
            palletWorkScanDtoList.add(palletWorkScanDto);
        }

        return palletWorkScanDtoList;
    }

    @Override
    public List<String> findPalletCarton(Long productPalletId) {

        List<String> cartonCodeList = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("productPalletId", productPalletId);
        map.put("groupBy", "cartonCode");
        List<MesSfcProductPalletDetDto> mesSfcProductPalletDetDtoList = mesSfcProductPalletDetService.findList(map);
        for (MesSfcProductPalletDetDto mesSfcProductPalletDetDto : mesSfcProductPalletDetDtoList) {
            cartonCodeList.add(mesSfcProductPalletDetDto.getCartonCode());
        }

        return cartonCodeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int submitNoFullPallet(List<Long> palletIdList, byte printBarcode, String printName, Long processId) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(MesSfcProductPallet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("productPalletId", palletIdList);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductPallet> productPallets = mesSfcProductPalletService.selectByExample(example);
        List<Long> palletIds = new ArrayList<>();
        for (MesSfcProductPallet item : productPallets) {
            item.setCloseStatus((byte) 1);
            item.setModifiedUserId(user.getUserId());
            item.setModifiedTime(new Date());
            mesSfcProductPalletService.update(item);
            // ?????????????????????
            if (printBarcode == 1) {
                PrintCarCodeDto printCarCodeDto = new PrintCarCodeDto();
                printCarCodeDto.setWorkOrderId(item.getWorkOrderId());
                printCarCodeDto.setLabelTypeCode("10");
                printCarCodeDto.setBarcode(item.getPalletCode());
                printCarCodeDto.setPrintName(printName != null ? printName : "??????");
                BarcodeUtils.printBarCode(printCarCodeDto);
            }
            palletIds.add(item.getProductPalletId());
        }
        return productPallets.size();
    }

    @Override
    public Boolean updatePalletType(Long stationId) {
        List<MesSfcProductPalletDto> list = mesSfcProductPalletService.findList(ControllerUtil.dynamicConditionByEntity(SearchMesSfcProductPallet.builder()
                .stationId(stationId)
                .closeStatus((byte) 0)
                .build()));
        if (list.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updateNowPackageSpecQty(Long productPalletId, Double nowPackageSpecQty, Boolean print, String printName, Long processId) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        int palletCartons = findPalletCarton(productPalletId).size();
        if (nowPackageSpecQty < palletCartons) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "?????????????????????????????????????????????????????????????????????");
        }
        MesSfcProductPallet mesSfcProductPallet = new MesSfcProductPallet();
        mesSfcProductPallet.setProductPalletId(productPalletId);
        mesSfcProductPallet.setNowPackageSpecQty(BigDecimal.valueOf(nowPackageSpecQty));
        if (nowPackageSpecQty == palletCartons) {
            mesSfcProductPallet.setCloseStatus((byte) 1);
            mesSfcProductPallet.setClosePalletUserId(user.getUserId());
            mesSfcProductPallet.setClosePalletTime(new Date());

            // 2022-03-01 ?????????????????????????????????????????????????????????
            /*// ????????????????????????????????????
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcProductPallet.getWorkOrderId());
            List<MesPmWorkOrderDto> mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (mesPmWorkOrderDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtoList.get(0);
            if (mesPmWorkOrderDto.getOutputProcessId().equals(processId)){
                // ?????????????????????
                List<Long> palletIds = new ArrayList<>();
                palletIds.add(productPalletId);
                this.beforePalletAutoAsnOrder(palletIds, user.getOrganizationId(), null);
            }*/
        }
        mesSfcProductPallet.setModifiedTime(new Date());
        mesSfcProductPallet.setModifiedUserId(user.getUserId());
        int update = mesSfcProductPalletService.update(mesSfcProductPallet);
        if (print && update > 0) {
            // ???????????????????????????
            BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                    .barcode(mesSfcProductPallet.getPalletCode())
                    .labelTypeCode("09")
                    .workOrderId(mesSfcProductPallet.getWorkOrderId())
                    .printName(printName != null ? printName : "??????")
                    .build());
        }
        return update;
    }

    @Override
    public int updateMoveStatus(Long productPalletId) {
        MesSfcProductPallet sfcProductPallet = mesSfcProductPalletService.selectByKey(productPalletId);
        if (sfcProductPallet == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012030);
        }
        sfcProductPallet.setMoveStatus((byte) 1);
        return mesSfcProductPalletService.update(sfcProductPallet);
    }

    @Override
    public Boolean testStacking(String code) {
        try {
            if (code.equals("500")) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????");
            }
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(0);
            wanbaoStackingMQDto.setStackingCode("391-1066920304213");
            wanbaoStackingMQDto.setStackingLine("1");
            // ??????????????????mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            return true;
        } catch (Exception e) {
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(500);
            // ??????????????????mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), e.getMessage());
        }
    }

    /**
     * ??????????????????MQ
     *
     * @param stackCode
     */
    @Override
    public void sendMQByStacking(String stackCode) {
        try {
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(0);
            wanbaoStackingMQDto.setStackingCode(stackCode);
            String stackingLine = stackCode.substring(stackCode.length() - 1);
            wanbaoStackingMQDto.setStackingLine(stackingLine);
            // ??????????????????mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
        } catch (Exception e) {
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(500);
            // ??????????????????mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), e.getMessage());
        }
    }

    /**
     * ??????-???????????????A??????
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    @LcnTransaction
    @Transactional(rollbackFor = RuntimeException.class)
    public int workByAuto(WanbaoAutoStackingListDto dto) throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("======= ????????????????????? ========");
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (dto.getList() == null || dto.getList().isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "??????????????????????????????");
        }
        List<String> barcodeList = dto.getList().stream().map(WanbaoAutoStackingDto::getBarcode).collect(Collectors.toList());
        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcodeList(barcodeList);
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????");
        }

        List<WanbaoBarcodeDto> list = new ArrayList<>();
        for (WanbaoAutoStackingDto stackingDet : dto.getList()) {
            WanbaoBarcodeDto wanbaoBarcodeDto = new WanbaoBarcodeDto();
            BeanUtil.copyProperties(stackingDet, wanbaoBarcodeDto);
            for (MesSfcWorkOrderBarcodeDto workOrderBarcodeDto : mesSfcWorkOrderBarcodeDtoList) {
                if (workOrderBarcodeDto.getBarcode().equals(wanbaoBarcodeDto.getBarcode())) {
                    wanbaoBarcodeDto.setWorkOrderId(workOrderBarcodeDto.getWorkOrderId());
                    wanbaoBarcodeDto.setMaterialId(workOrderBarcodeDto.getMaterialId());
                    break;
                }
            }
            list.add(wanbaoBarcodeDto);
        }
        long barcodeListTime = System.currentTimeMillis();
        log.info("=========== ??????????????????????????????:" + (barcodeListTime - startTime));

        // ??????????????????????????????
        this.beforePalletAutoAsnOrder(dto.getStackingId(), user.getOrganizationId(), list, barcodeList, mesSfcWorkOrderBarcodeDtoList);
        long beforePalletAutoAsnOrderTime = System.currentTimeMillis();
        log.info("=========== ????????????????????????????????????:" + (beforePalletAutoAsnOrderTime - barcodeListTime));
        return 1;
    }

    /**
     * ???????????????PO/????????????/??????
     *
     * @param barcodeList
     * @return
     */
    @Override
    public boolean checkBarCode(List<String> barcodeList) {
        Map<String, Object> map = new HashMap<>();
        map.put("barcodeList", barcodeList);
        String count = mesSfcBarcodeProcessService.countBarcodeListForPOGroup(map);
        if (StringUtils.isNotEmpty(count) && Integer.parseInt(count) > 1) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "????????????????????????????????????????????????PO??????????????????");
        }
        log.info("======== count1:" + count);
        count = mesSfcBarcodeProcessService.countBarcodeListForSalesOrder(map);
        log.info("======== count2:" + count);
        if (StringUtils.isNotEmpty(count) && Integer.parseInt(count) > 1) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????????????????????????????????????????");
        }
        count = mesSfcBarcodeProcessService.countBarcodeListForMaterial(map);
        log.info("======== count3:" + count);
        if (StringUtils.isNotEmpty(count) && Integer.parseInt(count) > 1) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????????????????????????????????????????");
        }
        return true;
    }

    // region ????????????

    /**
     * ?????????????????????
     *
     * @param stackingId        ??????
     * @param orgId             ??????
     * @param wanbaoBarcodeDtos ??????
     */
    private void beforePalletAutoAsnOrder(Long stackingId, Long orgId, List<WanbaoBarcodeDto> wanbaoBarcodeDtos, List<String> barcodeList, List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList) {

        /**
         * 2022-03-12 bgkun ??????????????????????????????????????????????????????
         */
        Map<String, Object> map = new HashMap<>();
        map.clear();
        map.put("barcodeList", barcodeList);
        map.put("orgId", orgId);
        List<PalletAutoAsnDto> autoAsnDtos = mesSfcProductPalletDetService.findListGroupByWorkOrder(map);
        for (PalletAutoAsnDto palletAutoAsnDto : autoAsnDtos) {
            /*long count = mesSfcWorkOrderBarcodeDtoList.stream().filter(item -> item.getWorkOrderId().equals(palletAutoAsnDto.getSourceOrderId())).count();
            palletAutoAsnDto.setPackingQty(BigDecimal.valueOf(count));
            palletAutoAsnDto.setActualQty(BigDecimal.valueOf(count));*/
            palletAutoAsnDto.setStackingId(stackingId);
            List<BarPODto> barPODtos = new ArrayList<>();
            wanbaoBarcodeDtos.forEach(item -> {
                if (item.getMaterialId().equals(palletAutoAsnDto.getMaterialId())) {
                    BarPODto barPODto = new BarPODto();
                    barPODto.setBarCode(item.getBarcode());
                    barPODto.setPOCode(palletAutoAsnDto.getSamePackageCode());
                    barPODto.setCutsomerBarcode(item.getCustomerBarcode());
                    barPODto.setSalesBarcode(item.getSalesBarcode());
                    barPODtos.add(barPODto);
                }
            });
            log.info("============== ?????????????????????????????????????????? barPODtos ===========" + JSON.toJSONString(barPODtos));
            palletAutoAsnDto.setBarCodeList(barPODtos);
            //????????????
            SearchBaseMaterialOwner searchBaseMaterialOwner = new SearchBaseMaterialOwner();
            searchBaseMaterialOwner.setAsc((byte) 1);
            ResponseEntity<List<BaseMaterialOwnerDto>> baseMaterialOwnerDtos = baseFeignApi.findList(searchBaseMaterialOwner);
            if (StringUtils.isNotEmpty(baseMaterialOwnerDtos.getData())) {
                palletAutoAsnDto.setMaterialOwnerId(baseMaterialOwnerDtos.getData().get(0).getMaterialOwnerId());
                palletAutoAsnDto.setShipperName(baseMaterialOwnerDtos.getData().get(0).getMaterialOwnerName());
            } else {
                throw new BizErrorException("????????????????????????");
            }
            palletAutoAsnDto.setProductionDate(new Date());

            //2021-07-30 ?????????????????? by Dylan
            SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
            searchBasePackageSpecification.setMaterialId(palletAutoAsnDto.getMaterialId());
            searchBasePackageSpecification.setOrgId(orgId);
            ResponseEntity<List<BasePackageSpecificationDto>> basePackgeSpecifications = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification);
            if (StringUtils.isNotEmpty(basePackgeSpecifications.getData())) {
                List<BaseMaterialPackageDto> baseMaterialPackageDtos = basePackgeSpecifications.getData().get(0).getBaseMaterialPackages();
                if (StringUtils.isNotEmpty(baseMaterialPackageDtos)) {
                    BaseMaterialPackageDto baseMaterialPackageDto = baseMaterialPackageDtos.get(0);
                    palletAutoAsnDto.setPackingUnitName(baseMaterialPackageDto.getPackingUnitName());
                }
            }
            inFeignApi.palletAutoAsnOrder(palletAutoAsnDto);
        }
    }

    /**
     * ?????????????????????
     *
     * @param workOrderBarcodeId
     */
    private void updateAttachmentCodeStatus(Long workOrderBarcodeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeId", workOrderBarcodeId);
        List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
        if (!keyPartRelevanceDtos.isEmpty() && keyPartRelevanceDtos.size() > 0) {
            List<MesSfcWorkOrderBarcode> barcodes = new ArrayList<>();
            for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos) {
                if (keyPartRelevanceDto.getPartBarcode() != null) {
                    SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
                    searchMesSfcWorkOrderBarcode.setBarcode(keyPartRelevanceDto.getPartBarcode());
                    List<MesSfcWorkOrderBarcodeDto> barcodeDtos = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
                    if (!barcodeDtos.isEmpty()) {
                        MesSfcWorkOrderBarcode barcode = new MesSfcWorkOrderBarcode();
                        barcode.setWorkOrderBarcodeId(barcodeDtos.get(0).getWorkOrderBarcodeId());
                        barcode.setBarcodeStatus((byte) 2);
                        barcodes.add(barcode);
                    }
                }
            }
            if (barcodes.size() > 0) {
                mesSfcWorkOrderBarcodeService.batchUpdate(barcodes);
            }
        }
    }

    /**
     * ???????????????????????????????????????ID?????????ID
     *
     * @param barcode
     * @return
     */
    private Map<String, Object> cleanBarcode(String barcode) {
        Map<String, Object> map = new HashMap<>();
        long start = System.currentTimeMillis();
        if (barcode.length() != 23) {
            // ??????????????????????????????
            Example example = new Example(MesSfcBarcodeProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("customerBarcode", barcode + "%");
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = mesSfcBarcodeProcessService.selectByExample(example);
            if (!mesSfcBarcodeProcesses.isEmpty()) {
                map.put("barcode", barcode);
                map.put("workOrderBarcodeId", mesSfcBarcodeProcesses.get(0).getWorkOrderBarcodeId().toString());
                map.put("workOrderId", mesSfcBarcodeProcesses.get(0).getWorkOrderId().toString());
                log.info("=========== ????????????UK????????????" + (System.currentTimeMillis() - start));
                return map;
            }
        }

        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(barcode);
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "???????????????????????????????????????");
        }

        if (barcode.contains("391-") || barcode.contains("391D")){
            // ????????????
            map.put("partBarcode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findListByPallet(map);
            if (mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????");
            }
            map.put("barcode", mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            map.put("workOrderBarcodeId", mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderBarcodeId().toString());
            map.put("workOrderId", mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId().toString());
            log.info("=========== ???????????????????????????" + (System.currentTimeMillis() - start));
            return map;
        }

        log.info("=========== ?????????????????????" + (System.currentTimeMillis() - start));
        long time = System.currentTimeMillis();
        BaseLabelCategory labelCategory = baseFeignApi.findLabelCategoryDetail(mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId()).getData();
        log.info("=========== ???????????????????????????" + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        if (labelCategory.getLabelCategoryCode().equals("01")) {
            // ?????????
            map.put("barcode", mesSfcWorkOrderBarcodeDtoList.get(0).getBarcode());
            map.put("workOrderBarcodeId", mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId().toString());
            map.put("workOrderId", mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId().toString());
        } else {
            // ?????????
            map.put("partBarcode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findListByPallet(map);
            if (mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????");
            }
            map.put("barcode", mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            map.put("workOrderBarcodeId", mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderBarcodeId().toString());
            map.put("workOrderId", mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId().toString());
            log.info("=========== ????????????????????????" + (System.currentTimeMillis() - time));
        }
        return map;
    }

    /**
     * ???????????????
     * @param materialId
     * @param materialCode
     * @param processId
     * @param proCode
     * @param workOrderId
     * @return
     */
    private String createPalletCode(Long materialId, String materialCode, Long processId, String proCode, Long workOrderId){
        SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
        searchBasePackageSpecification.setMaterialId(materialId);
        searchBasePackageSpecification.setProcessId(processId);
        List<BasePackageSpecificationDto> basePackageSpecificationDtoList = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification).getData();
        if (basePackageSpecificationDtoList.isEmpty() || basePackageSpecificationDtoList.get(0).getBaseMaterialPackages().isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "??????????????????????????????????????????????????????");
        }
        // ?????????????????????????????????????????????
        String lastBarCode = null;
        String barcodeRule = "";
        // ??????????????????????????????
        List<BaseBarcodeRuleSpec> barcodeRuleSpecList = new LinkedList<>();
        for (BaseMaterialPackage baseMaterialPackage : basePackageSpecificationDtoList.get(0).getBaseMaterialPackages()) {
            if (processId.equals(baseMaterialPackage.getProcessId())) {
                BaseBarcodeRule baseBarcodeRule = baseFeignApi.baseBarcodeRuleDetail(baseMaterialPackage.getBarcodeRuleId()).getData();
                barcodeRule = baseBarcodeRule.getBarcodeRule();
                boolean hasKey = redisUtil.hasKey(baseBarcodeRule.getBarcodeRule());
                if (hasKey) {
                    // ???redis????????????????????????
                    lastBarCode = redisUtil.get(baseBarcodeRule.getBarcodeRule()).toString();
                }

                SearchBaseBarcodeRuleSpec baseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
                baseBarcodeRuleSpec.setBarcodeRuleId(baseMaterialPackage.getBarcodeRuleId());
                barcodeRuleSpecList = baseFeignApi.findSpec(baseBarcodeRuleSpec).getData();
                if (barcodeRuleSpecList.isEmpty()) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012023);
                }
                break;
            }
        }
        // ???????????????????????????????????????[p],[L],[C]
        Map<String, Object> baseBarcodeRuleSpecMap = new HashMap<>();
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : barcodeRuleSpecList) {
            if ("[P]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[P]"))) {
                baseBarcodeRuleSpecMap.put("[P]", materialCode);
            }
            if ("[L]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[L]"))) {
                baseBarcodeRuleSpecMap.put("[L]", proCode);
            }
            if ("[C]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[C]"))) {
                SearchBaseMaterialSupplier searchBaseMaterialSupplier = new SearchBaseMaterialSupplier();
                searchBaseMaterialSupplier.setMaterialId(materialId);
                List<BaseMaterialSupplierDto> baseMaterialSupplierDtoList = baseFeignApi.findBaseMaterialSupplierList(searchBaseMaterialSupplier).getData();
                if (baseMaterialSupplierDtoList.isEmpty()) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????");
                }
                baseBarcodeRuleSpecMap.put("[C]", baseMaterialSupplierDtoList.get(0).getMaterialSupplierCode());
            }
        }
        //?????????????????????
        String maxCode = baseFeignApi.generateMaxCode(barcodeRuleSpecList, lastBarCode).getData();
        String palletCode = baseFeignApi.newGenerateCode(barcodeRuleSpecList, maxCode, baseBarcodeRuleSpecMap, workOrderId.toString()).getData();
        redisUtil.set(barcodeRule, palletCode);
        if (StringUtils.isEmpty(palletCode)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012008.getCode(), "?????????????????????");
        }
        return palletCode;
    }

    // endregion
}
