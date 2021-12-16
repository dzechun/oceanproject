package com.fantechs.provider.restapi.mulinsen.controller;

import com.fantechs.common.base.general.dto.mulinsen.HrUserInfoDto;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchHrUserInfo;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.restapi.mulinsen.service.HrUserInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "SCM人员控制器")
@RequestMapping("/hrUserInfo")
@Validated
public class HrUserInfoController {

    @Resource
    private HrUserInfoService hrUserInfoService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<HrUserInfoDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchHrUserInfo searchHrUserInfo) {
        Page<Object> page = PageHelper.startPage(searchHrUserInfo.getStartPage(), searchHrUserInfo.getPageSize());
        List<HrUserInfoDto> list = hrUserInfoService.findList(searchHrUserInfo);
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("同步SCM人员资料")
    @PostMapping("/synchronizeHrUserInfo")
    public ResponseEntity synchronizeHrUserInfo() throws Exception {
        return ControllerUtil.returnCRUD(hrUserInfoService.synchronizeHrUserInfo());
    }

}
