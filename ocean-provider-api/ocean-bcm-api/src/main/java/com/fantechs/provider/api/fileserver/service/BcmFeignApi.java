package com.fantechs.provider.api.fileserver.service;


import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by lfz on 2018/8/22.
 */
@FeignClient(value = "ocean-bcm")
public interface BcmFeignApi {


}
