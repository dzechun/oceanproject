package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/9/8
 */
public interface EngPackingOrderTakeService {

    List<EngPackingOrderDto> findList(Map<String,Object> map);

    /**
     * 登记
     * @param id
     * @return
     */
    int register(List<Long> ids);

    /**
     * 整单收货
     * @param ids
     * @return
     */
    int allTask(List<Long> ids);

    /**
     * 按箱收货
     * @param engPackingOrderSummaryDtos
     * @return
     */
    int boxTask(List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos);

    /**
     * 单一收货
     * @param engPackingOrderSummaryDetDto
     * @return
     */
    int onlyTask(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto);

    /**
     * PC创建上架作业单
     * @param ids
     * @return
     */
    int createInnerJobOrder(List<Long> ids);

    /**
     * PDA创建上架单
     * @param engPackingOrderSummaryDetDtos
     * @return
     */
    int pdaCreateInnerJobOrder(List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos);

    /**
     * 单一取消
     * @param ids
     * @return
     */
    int cancelAll(List<Long> ids);

    /**
     * 整单取消
     * @param engPackingOrderSummaryDetDto
     * @return
     */
    int onlyCancel(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto);
}
