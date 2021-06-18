package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.date.DateUtil;
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
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
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
        String serialNum = mesSfcReworkOrderDto.getReworkOrderCode().substring(mesSfcReworkOrderDto.getReworkOrderCode().length() - 4);
        Integer num = Integer.valueOf(serialNum) + 1;
        String reworkOrderCode = "FG" + DateUtil.format(new Date(), "YYYYMMDD") + num;
        generateReworkOrderCodeDto.setReworkOrderCode(reworkOrderCode);

        // 所有符合条码
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
        List<String> orderIds = new ArrayList<>();
        for (MesSfcBarcodeProcessDto dto : barcodeProcessDtos){
            if(!orderIds.contains(dto.getWorkOrderId().toString())){
                orderIds.add(dto.getWorkOrderId().toString());
            }
        }
        // 获取所有工单的部件清单并集
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderIds", orderIds);
        List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
        generateReworkOrderCodeDto.setKeyPartRelevanceDtos(keyPartRelevanceDtos);
        return generateReworkOrderCodeDto;
    }

    @Override
    public int save(DoReworkOrderDto doReworkOrderDto) throws Exception {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        // 工艺路线
        SearchBaseRoute searchBaseRoute = new SearchBaseRoute();
        searchBaseRoute.setRouteId(doReworkOrderDto.getRouteId());
        List<BaseRoute> baseRoutes = baseFeignApi.findRouteList(searchBaseRoute).getData();
        if(baseRoutes == null || baseRoutes.size() <= 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "工艺路线不存在或已被删除");
        }
        BaseRoute baseRoute = baseRoutes.get(0);
        // 工序
        BaseProcess baseProcess = baseFeignApi.processDetail(doReworkOrderDto.getProcessId()).getData();
        if(baseProcess == null){
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "工序不存在或已被删除");
        }

        // 工段
        BaseWorkshopSection workshopSection = baseFeignApi.sectionDetail(baseProcess.getSectionId()).getData();
        if(workshopSection == null){
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "工序所属工段不存在或已被删除");
        }

        MesSfcReworkOrder mesSfcReworkOrder = new MesSfcReworkOrder();
        mesSfcReworkOrder.setReworkOrderCode(doReworkOrderDto.getReworkOrderCode());
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
        mesSfcReworkOrderMapper.insert(mesSfcReworkOrder);

        // 获取需要处理条码
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(doReworkOrderDto.getSearchMesSfcBarcodeProcess()));

        List<String> orderIds = new ArrayList<>();
        List<String> workOrderBarcodeIds = new ArrayList<>();
        List<String> cartonCodeList = new ArrayList<>();
        List<String> palletCodeList = new ArrayList<>();
        for (MesSfcBarcodeProcessDto dto : barcodeProcessDtos){
            if(!orderIds.contains(dto.getWorkOrderId().toString())){
                orderIds.add(dto.getWorkOrderId().toString());
            }
            workOrderBarcodeIds.add(dto.getWorkOrderBarcodeId().toString());
            cartonCodeList.add(dto.getCartonCode());
            palletCodeList.add(dto.getPalletCode());
        }
        // 获取条码所有工单
        List<MesPmWorkOrder> mesPmWorkOrders = pmFeignApi.getWorkOrderList(orderIds).getData();
        if(mesPmWorkOrders == null || mesPmWorkOrders.size() <= 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "条码所属工单不存在或已被删除");
        }

        if(doReworkOrderDto.getClearCarton()){
        }
        // 清除彩盒
        if(doReworkOrderDto.getClearColorBox()){
        }
        // 清除栈板
        if(doReworkOrderDto.getClearPallet()){
        }

        List<MesSfcReworkOrderBarcode> mesSfcReworkOrderBarcodeList = new ArrayList<>();
        List<MesSfcHtReworkOrderBarcode> mesSfcHtReworkOrderBarcodeList = new ArrayList<>();
        List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = new ArrayList<>();
        for (MesSfcBarcodeProcessDto mesSfcBarcodeProcessDto : barcodeProcessDtos){
            // 判断是否最后一道工序，是则工单的完工数量减1
            for (MesPmWorkOrder mesPmWorkOrder : mesPmWorkOrders){
                if(mesSfcBarcodeProcessDto.getWorkOrderId().equals(mesPmWorkOrder.getWorkOrderId())
                        && mesPmWorkOrder.getOutputProcessId().equals(mesSfcBarcodeProcessDto.getNextProcessId())){
                    mesPmWorkOrder.setOutputQty(mesPmWorkOrder.getOutputQty().subtract(BigDecimal.ONE));
                    break;
                }
            }

            // 重置条码过站数据
            MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
            BeanUtils.copyProperties(mesSfcBarcodeProcessDto, mesSfcBarcodeProcess);
            mesSfcBarcodeProcess.setNextProcessId(doReworkOrderDto.getProcessId());
            mesSfcBarcodeProcess.setNextProcessCode(baseProcess.getProcessCode());
            mesSfcBarcodeProcess.setNextProcessName(baseRoute.getProcessName());
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
            mesSfcBarcodeProcess.setBarcodeStatus((byte) 1);
            mesSfcBarcodeProcess.setReworkOrderId(mesSfcReworkOrder.getReworkOrderId());
            // 清除包箱
            if(doReworkOrderDto.getClearCarton()){
                mesSfcBarcodeProcess.setCartonCode("");
            }
            // 清除彩盒
            if(doReworkOrderDto.getClearColorBox()){
                mesSfcBarcodeProcess.setColorBoxCode("");
            }
            // 清除栈板
            if(doReworkOrderDto.getClearPallet()){
                mesSfcBarcodeProcess.setPalletCode("");
            }
            mesSfcBarcodeProcessList.add(mesSfcBarcodeProcessDto);

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

        if(doReworkOrderDto.getClearCarton()){
            // 批量删除包箱条码关系表
            Example example = new Example(MesSfcProductCartonDet.class);
            example.createCriteria().andIn("workOrderBarcodeId", workOrderBarcodeIds);
            mesSfcProductCartonDetService.deleteByExample(example);
            // 清除包箱


        }
        if(doReworkOrderDto.getClearColorBox()){
            // 清除彩盒关系表

            // 清除彩盒

        }
        if(doReworkOrderDto.getClearPallet()){
            // 批量删除栈板条码关系表
            Example example = new Example(MesSfcProductPalletDet.class);
            example.createCriteria().andIn("workOrderBarcodeId", workOrderBarcodeIds);
            mesSfcProductPalletDetService.deleteByExample(example);
            // 清除栈板

        }

        // 清除条码部件关系表
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderBarcodeIds", workOrderBarcodeIds);
        List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
        List<MesSfcKeyPartRelevance> deleteKeypartRelevances = new ArrayList<>();
        for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : keyPartRelevanceDtos){
            for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto1 : doReworkOrderDto.getKeyPartRelevanceDtos()){
                if(keyPartRelevanceDto.getLabelCategoryId().equals(keyPartRelevanceDto1.getLabelCategoryId())
                        || keyPartRelevanceDto.getMaterialId().equals(keyPartRelevanceDto1.getMaterialId())){
                    deleteKeypartRelevances.add(keyPartRelevanceDto);
                    break;
                }
            }
        }
        mesSfcKeyPartRelevanceService.batchDelete(deleteKeypartRelevances);

        return 1;
    }
}
