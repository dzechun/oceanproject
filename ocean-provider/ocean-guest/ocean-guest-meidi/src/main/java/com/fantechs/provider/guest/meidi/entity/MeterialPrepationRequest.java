package com.fantechs.provider.guest.meidi.entity;

import lombok.Data;

@Data
public class MeterialPrepationRequest{

    /**
     * 请求编码
     * */
    private String requestCode;

    /**
     * 接口名称
     * */
    private String requsetName;

    /**
     * 请求参数
     * */
    private String requestData;
}
