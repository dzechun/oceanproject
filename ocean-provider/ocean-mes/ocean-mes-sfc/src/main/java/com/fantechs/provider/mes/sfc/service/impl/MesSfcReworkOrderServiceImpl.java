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

        // 生成返工单号
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

        // 所有符合条码
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
        List<String> workOrderBarcodeIds = new ArrayList<>();
        for (MesSfcBarcodeProcessDto dto : barcodeProcessDtos) {
            if (!workOrderBarcodeIds.contains(dto.getWorkOrderBarcodeId().toString())) {
                workOrderBarcodeIds.add(dto.getWorkOrderBarcodeId().toString());
            }
        }
        // 获取所有条码的部件清单并集
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
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        // 工艺路线
        BaseRoute baseRoute = baseFeignApi.routeDetail(doReworkOrderDto.getRouteId()).getData();
        if (baseRoute == null) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "工艺路线不存在或已被删除");
        }
        // 工序
        BaseProcess baseProcess = baseFeignApi.processDetail(doReworkOrderDto.getProcessId()).getData();
        if (baseProcess == null) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "工序不存在或已被删除");
        }

        // 工段
        BaseWorkshopSection workshopSection = baseFeignApi.sectionDetail(baseProcess.getSectionId()).getData();
        if (workshopSection == null) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "工序所属工段不存在或已被删除");
        }

        // 获取需要处理条码
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(doReworkOrderDto.getSearchMesSfcBarcodeProcess()));

        if (barcodeProcessDtos == null || barcodeProcessDtos.size() <= 0) {
            throw new BizErrorException("查询不到需返工条码");
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

        // 保存返工单历史表
        MesSfcHtReworkOrder mesSfcHtReworkOrder = new MesSfcHtReworkOrder();
        BeanUtils.copyProperties(mesSfcReworkOrder, mesSfcHtReworkOrder);
        mesSfcHtReworkOrderMapper.insert(mesSfcHtReworkOrder);
        // 保存返工单
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
        // 获取条码所有工单
        List<MesPmWorkOrder> mesPmWorkOrders = pmFeignApi.getWorkOrderList(orderIds).getData();
        if (mesPmWorkOrders != null && mesPmWorkOrders.size() > 0) {
            List<MesSfcReworkOrderBarcode> mesSfcReworkOrderBarcodeList = new ArrayList<>();
            List<MesSfcHtReworkOrderBarcode> mesSfcHtReworkOrderBarcodeList = new ArrayList<>();
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = new ArrayList<>();
            for (MesSfcBarcodeProcessDto mesSfcBarcodeProcessDto : barcodeProcessDtos) {
                // 判断是否最后一道工序，是则工单的完工数量减1
                for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders) {
                    if (mesSfcBarcodeProcessDto.getWorkOrderId().equals(mesPmWorkOrder.getWorkOrderId())
                            && mesPmWorkOrder.getOutputProcessId().equals(mesSfcBarcodeProcessDto.getNextProcessId())) {
                        mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty().subtract(BigDecimal.ONE));
                        break;
                    }
                }

                // 重置条码过站数据
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
                // 清除包箱
                if (doReworkOrderDto.getClearCarton()) {
                    mesSfcBarcodeProcess.setCartonCode(null);
                }
                // 清除彩盒
                if (doReworkOrderDto.getClearColorBox()) {
                    mesSfcBarcodeProcess.setColorBoxCode(null);
                }
                // 清除栈板
                if (doReworkOrderDto.getClearPallet()) {
                    mesSfcBarcodeProcess.setPalletCode(null);
                }
                mesSfcBarcodeProcessList.add(mesSfcBarcodeProcess);

                // 构造返工单条码数据
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

                // 构造历史返工单条码数据
                MesSfcHtReworkOrderBarcode mesSfcHtReworkOrderBarcode = new MesSfcHtReworkOrderBarcode();
                BeanUtils.copyProperties(mesSfcReworkOrderBarcode, mesSfcHtReworkOrderBarcode);
                mesSfcHtReworkOrderBarcodeList.add(mesSfcHtReworkOrderBarcode);

            }
            // 批量保存返工单条码关系表
            mesSfcReworkOrderBarcodeMapper.insertList(mesSfcReworkOrderBarcodeList);
            // 批量保存返工单条码关系历史表
            mesSfcHtReworkOrderBarcodeMapper.insertList(mesSfcHtReworkOrderBarcodeList);
            // 批量修改条码过站表
            mesSfcBarcodeProcessService.batchUpdate(mesSfcBarcodeProcessList);
            // 批量修改工单完工数量
            pmFeignApi.batchUpdate(mesPmWorkOrders);
            // 重置生产订单条码状态
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
                // 所有包箱
                Example cartonExample = new Example(MesSfcProductCarton.class);
                cartonExample.createCriteria().andIn("cartonCode", cartonCodeList);
                List<MesSfcProductCarton> sfcProductCartons = mesSfcProductCartonService.selectByExample(cartonExample);
                if (sfcProductCartons != null && sfcProductCartons.size() > 0) {
                    List<Long> cartonIds = new ArrayList<>();
                    for (MesSfcProductCarton mesSfcProductCarton : sfcProductCartons) {
                        cartonIds.add(mesSfcProductCarton.getProductCartonId());
                    }
                    // 包箱下所有包箱条码关系
                    Example cartonDetExample = new Example(MesSfcProductCartonDet.class);
                    cartonDetExample.createCriteria().andIn("productCartonId", cartonIds);
                    List<MesSfcProductCartonDet> productCartonDets = mesSfcProductCartonDetService.selectByExample(cartonDetExample);
                    // 需删除包箱条码关系
                    Example cartonDetDeleteExample = new Example(MesSfcProductCartonDet.class);
                    cartonDetDeleteExample.createCriteria().andIn("workOrderBarcodeId", workOrderBarcodeIds);
                    List<MesSfcProductCartonDet> deleteProductCartonDets = mesSfcProductCartonDetService.selectByExample(cartonDetDeleteExample);
                    // 计算所有需要删除的包箱
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
                    // 清除包箱
                    if (deleteProductCartonIds.size() > 0) {
                        Example cartonDeleteExample = new Example(MesSfcProductCarton.class);
                        cartonDeleteExample.createCriteria().andIn("productCartonId", deleteProductCartonIds);
                        mesSfcProductCartonService.deleteByExample(cartonDeleteExample);
                    }
                    // 批量删除包箱条码关系表
                    mesSfcProductCartonDetService.deleteByExample(cartonDetDeleteExample);
                }
            }
            if (doReworkOrderDto.getClearColorBox()) {
                // 清除彩盒关系表

                // 清除彩盒

            }
            if (doReworkOrderDto.getClearPallet() && palletCodeList.size() > 0) {
                // 所有栈板
                Example palletExample = new Example(MesSfcProductPallet.class);
                palletExample.createCriteria().andIn("palletCode", palletCodeList);
                List<MesSfcProductPallet> sfcProductPallets = mesSfcProductPalletService.selectByExample(palletExample);
                if (sfcProductPallets != null && sfcProductPallets.size() > 0) {
                    List<Long> palletIds = new ArrayList<>();
                    for (MesSfcProductPallet mesSfcProductPallet : sfcProductPallets) {
                        palletIds.add(mesSfcProductPallet.getProductPalletId());
                    }
                    // 包箱下所有栈板条码关系
                    Example palletDetExample = new Example(MesSfcProductPalletDet.class);
                    palletDetExample.createCriteria().andIn("productPalletId", palletIds);
                    List<MesSfcProductPalletDet> productPalletDets = mesSfcProductPalletDetService.selectByExample(palletDetExample);
                    // 需删除栈板条码关系
                    Example palletDetDeleteExample = new Example(MesSfcProductCartonDet.class);
                    palletDetDeleteExample.createCriteria().andIn("workOrderBarcodeId", workOrderBarcodeIds);
                    List<MesSfcProductPalletDet> deleteProductPalletDets = mesSfcProductPalletDetService.selectByExample(palletDetDeleteExample);
                    // 计算所有需要删除的栈板
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
                    // 清除包箱
                    if (deleteProductPalletIds.size() > 0) {
                        Example palletDeleteExample = new Example(MesSfcProductPallet.class);
                        palletDeleteExample.createCriteria().andIn("productPalletId", deleteProductPalletIds);
                        mesSfcProductPalletService.deleteByExample(palletDeleteExample);
                    }
                    // 批量删除栈板条码关系表
                    mesSfcProductPalletDetService.deleteByExample(palletDetDeleteExample);
                }
            }

            // 清除条码部件关系表
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
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "条码所属工单不存在或已被删除");
        }

    }
}
