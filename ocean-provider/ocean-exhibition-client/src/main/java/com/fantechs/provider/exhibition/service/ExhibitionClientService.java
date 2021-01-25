package com.fantechs.provider.exhibition.service;

public interface ExhibitionClientService {
    int makingOrders();
    String agvStockTask(Long stockId);
    String agvStockTaskTest(String materialCode);
    String agvContinueTask();
}
