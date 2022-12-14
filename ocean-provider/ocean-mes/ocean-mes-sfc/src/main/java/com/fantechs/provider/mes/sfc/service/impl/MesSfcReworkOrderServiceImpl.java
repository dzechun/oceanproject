package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.date.DateUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcHtReworkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcHtReworkOrderMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcReworkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcReworkOrderMapper;
import com.fantechs.provider.mes.sfc.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2021/06/15.
 */
@Service
public class MesSfcReworkOrderServiceImpl extends BaseService<MesSfcReworkOrder> implements MesSfcReworkOrderService {

    @Resource
    private MesSfcReworkOrderMapper mesSfcReworkOrderMapper;
    @Resource
    private MesSfcHtReworkOrderMapper mesSfcHtReworkOrderMapper;
    @Resource
    private MesSfcReworkOrderBarcodeMapper mesSfcReworkOrderBarcodeMapper;
    @Resource
    private MesSfcHtReworkOrderBarcodeMapper mesSfcHtReworkOrderBarcodeMapper;
    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;
    @Resource
    private MesSfcProductCartonService mesSfcProductCartonService;
    @Resource
    private MesSfcProductCartonDetService mesSfcProductCartonDetService;
    @Resource
    private MesSfcProductPalletService mesSfcProductPalletService;
    @Resource
    private MesSfcProductPalletDetService mesSfcProductPalletDetService;
    @Resource
    private MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;

    @Override
    public List<MesSfcReworkOrderDto> findList(Map<String, Object> map) {
        return mesSfcReworkOrderMapper.findList(map);
    }

    @Override
    public List<MesSfcHtReworkOrderDto> findHtList(Map<String, Object> map) {
        return mesSfcHtReworkOrderMapper.findList(map);
    }

    @Override
    public GenerateReworkOrderCodeDto generateReworkOrderCode(SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess) {
        GenerateReworkOrderCodeDto generateReworkOrderCodeDto = new GenerateReworkOrderCodeDto();

        // ??????????????????
        MesSfcReworkOrderDto mesSfcReworkOrderDto = mesSfcReworkOrderMapper.getFirstTodayReworkOrder();
        String serialNum = "";
        if (mesSfcReworkOrderDto != null) {
            serialNum = mesSfcReworkOrderDto.getReworkOrderCode().substring(mesSfcReworkOrderDto.getReworkOrderCode().length() - 4);
        } else {
            serialNum = "0000";
        }
        Integer num = Integer.valueOf(serialNum) + 1;
        serialNum = num.toString();
        int digits = 4 - num.toString().length();
        for (int i = 1; i <= digits; i++) {
            serialNum = "0" + serialNum;
        }
        String reworkOrderCode = "FG" + DateUtil.format(new Date(), "YYYYMMdd") + serialNum;
        generateReworkOrderCodeDto.setReworkOrderCode(reworkOrderCode);

        // ??????????????????
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
        List<String> workOrderBarcodeIds = new ArrayList<>();
        for (MesSfcBarcodeProcessDto dto : barcodeProcessDtos) {
            if (!workOrderBarcodeIds.contains(dto.getWorkOrderBarcodeId().toString())) {
                workOrderBarcodeIds.add(dto.getWorkOrderBarcodeId().toString());
            }
        }
        // ???????????????????????????????????????
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeIds", workOrderBarcodeIds);
        List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findListForGroup(map);
        generateReworkOrderCodeDto.setKeyPartRelevanceDtos(keyPartRelevanceDtos);
        return generateReworkOrderCodeDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int save(DoReworkOrderDto doReworkOrderDto) throws Exception {
        // ??????????????????
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        // ????????????
        BaseRoute baseRoute = baseFeignApi.routeDetail(doReworkOrderDto.getRouteId()).getData();
        if (baseRoute == null) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "????????????????????????????????????");
        }
        // ??????
        BaseProcess baseProcess = baseFeignApi.processDetail(doReworkOrderDto.getProcessId()).getData();
        if (baseProcess == null) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "??????????????????????????????");
        }

        // ??????
        BaseWorkshopSection workshopSection = baseFeignApi.sectionDetail(baseProcess.getSectionId()).getData();
        if (workshopSection == null) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "??????????????????????????????????????????");
        }

        // ????????????????????????
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(doReworkOrderDto.getSearchMesSfcBarcodeProcess()));

        if (barcodeProcessDtos == null || barcodeProcessDtos.size() <= 0) {
            throw new BizErrorException("???????????????????????????");
        }

        MesSfcReworkOrder mesSfcReworkOrder = new MesSfcReworkOrder();
        mesSfcReworkOrder.setReworkOrderCode(doReworkOrderDto.getReworkOrderCode());
        mesSfcReworkOrder.setMaterialId(barcodeProcessDtos.get(0).getMaterialId());
        mesSfcReworkOrder.setCreateTime(new Date());
        mesSfcReworkOrder.setReworkStatus((byte) 1);
        mesSfcReworkOrder.setReworkRouteId(doReworkOrderDto.getRouteId());
        mesSfcReworkOrder.setStatus((byte) 1);
        mesSfcReworkOrder.setReworkStartProcessId(doReworkOrderDto.getProcessId());
        mesSfcReworkOrder.setOrgId(user.getOrganizationId());
        mesSfcReworkOrder.setIsDelete((byte) 1);
        mesSfcReworkOrder.setCreateUserId(user.getUserId());

        // ????????????????????????
        MesSfcHtReworkOrder mesSfcHtReworkOrder = new MesSfcHtReworkOrder();
        BeanUtils.copyProperties(mesSfcReworkOrder, mesSfcHtReworkOrder);
        mesSfcHtReworkOrderMapper.insert(mesSfcHtReworkOrder);
        // ???????????????
        mesSfcReworkOrderMapper.insertUseGeneratedKeys(mesSfcReworkOrder);

        List<String> orderIds = new ArrayList<>();
        List<String> workOrderBarcodeIds = new ArrayList<>();
        List<String> cartonCodeList = new ArrayList<>();
        List<String> palletCodeList = new ArrayList<>();
        for (MesSfcBarcodeProcessDto dto : barcodeProcessDtos) {
            if (!orderIds.contains(dto.getWorkOrderId().toString())) {
                orderIds.add(dto.getWorkOrderId().toString());
            }
            workOrderBarcodeIds.add(dto.getWorkOrderBarcodeId().toString());
            if (!cartonCodeList.contains(dto.getCartonCode()) && doReworkOrderDto.getClearCarton()) {
                cartonCodeList.add(dto.getCartonCode());
            }
            if (!cartonCodeList.contains(dto.getCartonCode()) && doReworkOrderDto.getClearPallet()) {
                palletCodeList.add(dto.getPalletCode());
            }
        }
        // ????????????????????????
        List<MesPmWorkOrder> mesPmWorkOrders = pmFeignApi.getWorkOrderList(orderIds).getData();
        if (mesPmWorkOrders != null && mesPmWorkOrders.size() > 0) {
            List<MesSfcReworkOrderBarcode> mesSfcReworkOrderBarcodeList = new ArrayList<>();
            List<MesSfcHtReworkOrderBarcode> mesSfcHtReworkOrderBarcodeList = new ArrayList<>();
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = new ArrayList<>();
            for (MesSfcBarcodeProcessDto mesSfcBarcodeProcessDto : barcodeProcessDtos) {
                // ???????????????????????????????????????????????????????????????1
                for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders) {
                    if (mesSfcBarcodeProcessDto.getWorkOrderId().equals(mesPmWorkOrder.getWorkOrderId())
                            && mesPmWorkOrder.getOutputProcessId().equals(mesSfcBarcodeProcessDto.getNextProcessId())) {
                        mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty().subtract(BigDecimal.ONE));
                        break;
                    }
                }

                // ????????????????????????
                MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                BeanUtils.copyProperties(mesSfcBarcodeProcessDto, mesSfcBarcodeProcess);
                mesSfcBarcodeProcess.setNextProcessId(doReworkOrderDto.getProcessId());
                mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
                mesSfcBarcodeProcess.setNextProcessName(baseProcess.getProcessName());
                mesSfcBarcodeProcess.setRouteId(doReworkOrderDto.getRouteId());
                mesSfcBarcodeProcess.setRouteCode(baseRoute.getRouteCode());
                mesSfcBarcodeProcess.setRouteName(baseRoute.getRouteName());
                mesSfcBarcodeProcess.setSectionId(workshopSection.getSectionId());
                mesSfcBarcodeProcess.setSectionCode(workshopSection.getSectionCode());
                mesSfcBarcodeProcess.setSectionName(workshopSection.getSectionName());
                mesSfcBarcodeProcess.setProLineId(null);
                mesSfcBarcodeProcess.setProCode(null);
                mesSfcBarcodeProcess.setProName(null);
                mesSfcBarcodeProcess.setStationId(null);
                mesSfcBarcodeProcess.setStationCode(null);
                mesSfcBarcodeProcess.setStationName(null);
                mesSfcBarcodeProcess.setProductionTime(null);
                mesSfcBarcodeProcess.setBarcodeStatus((byte) 1);
                mesSfcBarcodeProcess.setReworkOrderId(mesSfcReworkOrder.getReworkOrderId());
                // ????????????
                if (doReworkOrderDto.getClearCarton()) {
                    mesSfcBarcodeProcess.setCartonCode(null);
                }
                // ????????????
                if (doReworkOrderDto.getClearColorBox()) {
                    mesSfcBarcodeProcess.setColorBoxCode(null);
                }
                // ????????????
                if (doReworkOrderDto.getClearPallet()) {
                    mesSfcBarcodeProcess.setPalletCode(null);
                }
                mesSfcBarcodeProcessList.add(mesSfcBarcodeProcess);

                // ???????????????????????????
                MesSfcReworkOrderBarcode mesSfcReworkOrderBarcode = new MesSfcReworkOrderBarcode();
                mesSfcReworkOrderBarcode.setReworkOrderId(mesSfcReworkOrder.getReworkOrderId());
                mesSfcReworkOrderBarcode.setWorkOrderId(mesSfcBarcodeProcessDto.getWorkOrderId());
                mesSfcReworkOrderBarcode.setWorkOrderBarcodeId(mesSfcBarcodeProcessDto.getWorkOrderBarcodeId());
                mesSfcReworkOrderBarcode.setStatus((byte) 1);
                mesSfcReworkOrderBarcode.setCreateUserId(user.getUserId());
                mesSfcReworkOrderBarcode.setCreateTime(new Date());
                mesSfcReworkOrderBarcode.setOrgId(user.getOrganizationId());
                mesSfcReworkOrderBarcode.setIsDelete((byte) 1);
                mesSfcReworkOrderBarcodeList.add(mesSfcReworkOrderBarcode);

                // ?????????????????????????????????
                MesSfcHtReworkOrderBarcode mesSfcHtReworkOrderBarcode = new MesSfcHtReworkOrderBarcode();
                BeanUtils.copyProperties(mesSfcReworkOrderBarcode, mesSfcHtReworkOrderBarcode);
                mesSfcHtReworkOrderBarcodeList.add(mesSfcHtReworkOrderBarcode);

            }
            // ????????????????????????????????????
            mesSfcReworkOrderBarcodeMapper.insertList(mesSfcReworkOrderBarcodeList);
            // ??????????????????????????????????????????
            mesSfcHtReworkOrderBarcodeMapper.insertList(mesSfcHtReworkOrderBarcodeList);
            // ???????????????????????????
            mesSfcBarcodeProcessService.batchUpdate(mesSfcBarcodeProcessList);
            // ??????????????????????????????
            pmFeignApi.batchUpdate(mesPmWorkOrders);
            // ??????????????????????????????
            Example workOrderBarcodeExample = new Example(MesSfcWorkOrderBarcode.class);
            workOrderBarcodeExample.createCriteria().andIn("workOrderBarcodeId", workOrderBarcodeIds);
            List<MesSfcWorkOrderBarcode> workOrderBarcodes = mesSfcWorkOrderBarcodeService.selectByExample(workOrderBarcodeExample);
            if(workOrderBarcodes != null && workOrderBarcodes.size() > 0){
                for(MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode : workOrderBarcodes){
                    mesSfcWorkOrderBarcode.setBarcodeType((byte) 1);
                }
                mesSfcWorkOrderBarcodeService.batchUpdate(workOrderBarcodes);
            }

            if (doReworkOrderDto.getClearCarton() && cartonCodeList.size() > 0) {
                // ????????????
                Example cartonExample = new Example(MesSfcProductCarton.class);
                cartonExample.createCriteria().andIn("cartonCode", cartonCodeList);
                List<MesSfcProductCarton> sfcProductCartons = mesSfcProductCartonService.selectByExample(cartonExample);
                if (sfcProductCartons != null && sfcProductCartons.size() > 0) {
                    List<Long> cartonIds = new ArrayList<>();
                    for (MesSfcProductCarton mesSfcProductCarton : sfcProductCartons) {
                        cartonIds.add(mesSfcProductCarton.getProductCartonId());
                    }
                    // ?????????????????????????????????
                    Example cartonDetExample = new Example(MesSfcProductCartonDet.class);
                    cartonDetExample.createCriteria().andIn("productCartonId", cartonIds);
                    List<MesSfcProductCartonDet> productCartonDets = mesSfcProductCartonDetService.selectByExample(cartonDetExample);
                    // ???????????????????????????
                    Example cartonDetDeleteExample = new Example(MesSfcProductCartonDet.class);
                    cartonDetDeleteExample.createCriteria().andIn("workOrderBarcodeId", workOrderBarcodeIds);
                    List<MesSfcProductCartonDet> deleteProductCartonDets = mesSfcProductCartonDetService.selectByExample(cartonDetDeleteExample);
                    // ?????????????????????????????????
                    Map<Long, Integer> map = new HashMap<>();
                    for (MesSfcProductCarton mesSfcProductCarton : sfcProductCartons) {
                        for (MesSfcProductCartonDet mesSfcProductCartonDet : productCartonDets) {
                            if (mesSfcProductCarton.getProductCartonId().equals(mesSfcProductCartonDet.getProductCartonId())) {
                                Integer count = map.get(mesSfcProductCarton.getProductCartonId());
                                if (count != null) {
                                    map.put(mesSfcProductCarton.getProductCartonId(), ++count);
                                    continue;
                                }
                            }
                            map.put(mesSfcProductCarton.getProductCartonId(), 1);
                        }

                        for (MesSfcProductCartonDet mesSfcProductCartonDet : deleteProductCartonDets) {
                            if (mesSfcProductCarton.getProductCartonId().equals(mesSfcProductCartonDet.getProductCartonId())) {
                                Integer count = map.get(mesSfcProductCarton.getProductCartonId());
                                if (count != null) {
                                    map.put(mesSfcProductCarton.getProductCartonId(), count - 1);
                                    continue;
                                }
                            }
                        }
                    }
                    List<Long> deleteProductCartonIds = new ArrayList<>();
                    for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                        if (entry.getValue() <= 0) {
                            deleteProductCartonIds.add(entry.getKey());
                        }
                    }
                    // ????????????
                    if (deleteProductCartonIds.size() > 0) {
                        Example cartonDeleteExample = new Example(MesSfcProductCarton.class);
                        cartonDeleteExample.createCriteria().andIn("productCartonId", deleteProductCartonIds);
                        mesSfcProductCartonService.deleteByExample(cartonDeleteExample);
                    }
                    // ?????????????????????????????????
                    mesSfcProductCartonDetService.deleteByExample(cartonDetDeleteExample);
                }
            }
            if (doReworkOrderDto.getClearColorBox()) {
                // ?????????????????????

                // ????????????

            }
            if (doReworkOrderDto.getClearPallet() && palletCodeList.size() > 0) {
                // ????????????
                Example palletExample = new Example(MesSfcProductPallet.class);
                palletExample.createCriteria().andIn("palletCode", palletCodeList);
                List<MesSfcProductPallet> sfcProductPallets = mesSfcProductPalletService.selectByExample(palletExample);
                if (sfcProductPallets != null && sfcProductPallets.size() > 0) {
                    List<Long> palletIds = new ArrayList<>();
                    for (MesSfcProductPallet mesSfcProductPallet : sfcProductPallets) {
                        palletIds.add(mesSfcProductPallet.getProductPalletId());
                    }
                    // ?????????????????????????????????
                    Example palletDetExample = new Example(MesSfcProductPalletDet.class);
                    palletDetExample.createCriteria().andIn("productPalletId", palletIds);
                    List<MesSfcProductPalletDet> productPalletDets = mesSfcProductPalletDetService.selectByExample(palletDetExample);
                    // ???????????????????????????
                    Example palletDetDeleteExample = new Example(MesSfcProductCartonDet.class);
                    palletDetDeleteExample.createCriteria().andIn("workOrderBarcodeId", workOrderBarcodeIds);
                    List<MesSfcProductPalletDet> deleteProductPalletDets = mesSfcProductPalletDetService.selectByExample(palletDetDeleteExample);
                    // ?????????????????????????????????
                    Map<Long, Integer> map = new HashMap<>();
                    for (MesSfcProductPallet mesSfcProductPallet : sfcProductPallets) {
                        for (MesSfcProductPalletDet mesSfcProductPalletDet : productPalletDets) {
                            if (mesSfcProductPallet.getProductPalletId().equals(mesSfcProductPalletDet.getProductPalletId())) {
                                Integer count = map.get(mesSfcProductPallet.getProductPalletId());
                                if (count != null) {
                                    map.put(mesSfcProductPallet.getProductPalletId(), ++count);
                                    continue;
                                }
                            }
                            map.put(mesSfcProductPallet.getProductPalletId(), 1);
                        }

                        for (MesSfcProductPalletDet mesSfcProductPalletDet : deleteProductPalletDets) {
                            if (mesSfcProductPallet.getProductPalletId().equals(mesSfcProductPalletDet.getProductPalletId())) {
                                Integer count = map.get(mesSfcProductPallet.getProductPalletId());
                                if (count != null) {
                                    map.put(mesSfcProductPallet.getProductPalletId(), count - 1);
                                }
                            }
                        }
                    }
                    List<Long> deleteProductPalletIds = new ArrayList<>();
                    for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                        if (entry.getValue() <= 0) {
                            deleteProductPalletIds.add(entry.getKey());
                        }
                    }
                    // ????????????
                    if (deleteProductPalletIds.size() > 0) {
                        Example palletDeleteExample = new Example(MesSfcProductPallet.class);
                        palletDeleteExample.createCriteria().andIn("productPalletId", deleteProductPalletIds);
                        mesSfcProductPalletService.deleteByExample(palletDeleteExample);
                    }
                    // ?????????????????????????????????
                    mesSfcProductPalletDetService.deleteByExample(palletDetDeleteExample);
                }
            }

            // ???????????????????????????
            Map<String, Object> map = new HashMap<>();
            map.put("workOrderBarcodeIds", workOrderBarcodeIds);
            List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
            List<MesSfcKeyPartRelevance> deleteKeypartRelevances = new ArrayList<>();
            for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos) {
                for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto1 : doReworkOrderDto.getKeyPartRelevanceDtoList()) {
                    if ((keyPartRelevanceDto.getLabelCategoryId() != null && keyPartRelevanceDto.getLabelCategoryId().equals(keyPartRelevanceDto1.getLabelCategoryId()))
                            || (keyPartRelevanceDto.getMaterialId() != null && keyPartRelevanceDto.getMaterialId().equals(keyPartRelevanceDto1.getMaterialId()))) {
                        deleteKeypartRelevances.add(keyPartRelevanceDto);
                        break;
                    }
                }
            }
            mesSfcKeyPartRelevanceService.batchDelete(deleteKeypartRelevances);

            return 1;
        } else {
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "??????????????????????????????????????????");
        }

    }
}
