package com.fantechs.provider.api.qms;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ocean-qms")
public interface QmsFeignApi {

}

