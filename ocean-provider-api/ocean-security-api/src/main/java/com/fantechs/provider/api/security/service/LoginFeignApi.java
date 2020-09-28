package com.fantechs.provider.api.security.service;


import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by lfz on 2020/8/6.
 */
@FeignClient(value = "ocean-security")
public interface LoginFeignApi {

    @PostMapping(value = "/login")
    ResponseEntity login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);

    @GetMapping("/sysUser/detail")
    @ApiOperation(value = "通过ID获取单个用户信息",notes = "通过ID获取单个用户信息")
    ResponseEntity<SysUser> selectUserById(@ApiParam(value = "传入主键userId", required = true) @RequestParam Long id);

}
