package com.fantechs.provider.exhibition.service;

public interface ExhibitionClientService {
    int makingOrders(Long orderId) throws Exception;
    String agvStockTask(Long stockId, Integer type) throws Exception;
    String agvStockTaskTest(String materialCode) throws Exception;
    String agvContinueTask();
}
