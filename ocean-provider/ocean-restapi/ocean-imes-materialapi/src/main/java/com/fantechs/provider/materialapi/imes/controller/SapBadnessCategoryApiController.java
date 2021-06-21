package com.fantechs.provider.materialapi.imes.controller;

import com.fantechs.common.base.general.dto.restapi.SearchSapBadnessCategoryApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.materialapi.imes.service.SapBadnessCategoryApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "雷赛不良代码信息接口")
@RequestMapping("/routeApi")
@Validated
public class SapBadnessCategoryApiController {

    @Resource
    private SapBadnessCategoryApiService sapBadnessCategoryApiService;

    @ApiOperation(value = "请求雷赛不良代码信息",notes = "请求雷赛不良代码信息")
    @PostMapping("/getbadnessCategory")
    public ResponseEntity getProLine(@ApiParam(value = "查询对象")@RequestBody SearchSapBadnessCategoryApi searchSapBadnessCategoryApi) {
        return ControllerUtil.returnCRUD(sapBadnessCategoryApiService.getbadnessCategory(searchSapBadnessCategoryApi));
    }


}
