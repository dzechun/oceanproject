package com.fantechs.provider.restapi.imes.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "server-url")
public class RestURL {

    private String u9UrlTest;

    private String mesBase;

    private String mesProcess;

    private String mesAps;

    private String mesProduction;

    private String AGV;

    private String mesMR;

    private String qisGetProductInfoByBarCode;

    private String qisGetRecommendWHBymaterialNum;

    private String qisCreateGetReturnMaterial;

    private String qisCreateGetMaterialOrder;

    private String qisCreateTransferDoc;

    private String qisGetNewUpdateCW;

    private String qisReturnMaterialOrderByReturn;

    private String qisCreateWorkOrderAndSN;

    private String qisAddFinishedSN;
}