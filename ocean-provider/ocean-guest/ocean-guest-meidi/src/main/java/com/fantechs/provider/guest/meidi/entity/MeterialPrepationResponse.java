package com.fantechs.provider.guest.meidi.entity;

import lombok.Data;

@Data
public class MeterialPrepationResponse{


    /**
     * 请求编码
     * */
    private String requestCode;

    /**
     * 返回结果 成功返回 success，失败返回 failure
     * */
    private String responseMessage;

    /**
     * 返回结果参数  成功返回 0，失败返回错误编号 1-N
     * */
    private String responseData;
}
