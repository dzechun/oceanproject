package com.fantechs.provider.api.wms.in;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Date 2021/1/7 19:28
 */
@FeignClient(name = "ocean-wms-in")
public interface InFeignApi {


}