package com.fantechs.provider.api.wms.out;


import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Data 2021-01-19 10:31
 */
@FeignClient(name = "ocean-wms-out")
public interface OutFeignApi {



}
