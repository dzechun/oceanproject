package com.fantechs.provider.api.security.service;


import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * Created by lfz on 2020/8/6.
 */
@FeignClient(value = "ocean-security")
public interface SecurityFeignApi {

    @PostMapping(value = "/login")
    ResponseEntity login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);


    @GetMapping(value = "/logout")
    ResponseEntity logout();

    @GetMapping("/sysUser/detail")
    @ApiOperation(value = "通过ID获取单个用户信息",notes = "通过ID获取单个用户信息")
    ResponseEntity<SysUser> selectUserById(@ApiParam(value = "传入主键userId", required = true) @RequestParam(value="id") Long id);

    @ApiOperation("根据条件查询程序配置项列表")
    @PostMapping("/sysSpecItem/findList")
    ResponseEntity<List<SysSpecItem>> findSpecItemList(@ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSysSpecItem searchSysSpecItem);

}
