package com.fantechs.provider.api.security.service;


import com.fantechs.common.base.response.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by lfz on 2020/8/6.
 */
@FeignClient(value = "ocean-security")
public interface LoginFeignApi {

    @PostMapping(value = "/login")
    ResponseEntity login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);

}
