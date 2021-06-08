package com.fantechs.provider.client.server;

import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.provider.client.dto.PtlJobOrderDTO;
import com.fantechs.provider.client.dto.PtlJobOrderDetPrintDTO;
import com.fantechs.provider.client.dto.RabbitMQDTO;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by lfz on 2020/12/9.
 */
public interface ElectronicTagStorageService {

    PtlJobOrder sendElectronicTagStorage(Long jobOrderId, Long warehouseAreaId) throws Exception;
    int createPtlJobOrder(List<PtlJobOrderDTO> ptlJobOrderDTOList) throws Exception;
    String sendElectronicTagStorageLightTest(String materialCode, Integer code) throws Exception;
    PtlJobOrderDto writeBackPtlJobOrder(Long jobOrderId, String status) throws Exception;
    List<PtlJobOrderDetPrintDTO> printPtlJobOrderLabel(Long jobOrderId) throws Exception;
    void fanoutSender(Integer code, RabbitMQDTO rabbitMQDTO, List<RabbitMQDTO> rabbitMQDTOList) throws Exception;
    String intercepting(String s, int number) throws UnsupportedEncodingException;
}
