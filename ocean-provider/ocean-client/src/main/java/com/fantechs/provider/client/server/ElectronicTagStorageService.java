package com.fantechs.provider.client.server;

import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.provider.client.dto.PtlJobOrderDTO;
import com.fantechs.provider.client.dto.PtlJobOrderDetPrintDTO;
import com.fantechs.provider.client.dto.RabbitMQDTO;
import com.fantechs.provider.client.dto.ResponseEntityDTO;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by lfz on 2020/12/9.
 */
public interface ElectronicTagStorageService {

    PtlJobOrder sendElectronicTagStorage(Long jobOrderId, Long warehouseAreaId, Integer type) throws Exception;
    ResponseEntityDTO createPtlJobOrder(List<PtlJobOrderDTO> ptlJobOrderDTOList) throws Exception;
    PtlJobOrderDto writeBackPtlJobOrder(Long jobOrderId, Integer type) throws Exception;
    List<PtlJobOrderDetPrintDTO> printPtlJobOrderLabel(Long jobOrderId, Long workUserId) throws Exception;
    int hangUpPtlJobOrderDet(String ids) throws Exception;
    ResponseEntityDTO cancelPtrlJobOrder(PtlJobOrderDTO ptlJobOrderDTO) throws Exception;
    int ptlJobOrderLightOff(Long jobOrderId) throws Exception;
    void fanoutSender(Integer code, RabbitMQDTO rabbitMQDTO, List<RabbitMQDTO> rabbitMQDTOList) throws Exception;
    String intercepting(String s, int number) throws UnsupportedEncodingException;

    int activateAndPrint(Long jobOrderId, Long workUserId) throws Exception;

    SysUser getPrinter() throws Exception;
}
