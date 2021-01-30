package com.fantechs.provider.exhibition.service;

public interface ExhibitionClientService {
    int makingOrders() throws Exception;
    String agvStockTask(Long stockId) throws Exception;
    String agvStockTaskTest(String materialCode) throws Exception;
    String agvContinueTask();
}
