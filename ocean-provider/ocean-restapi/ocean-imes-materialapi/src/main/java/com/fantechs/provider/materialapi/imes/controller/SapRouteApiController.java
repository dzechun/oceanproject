package com.fantechs.provider.materialapi.imes.controller;

import com.fantechs.common.base.general.dto.restapi.SearchSapRouteApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.materialapi.imes.service.SapRouteApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "雷赛工艺路线信息接口")
@RequestMapping("/routeApi")
@Validated
public class SapRouteApiController {

    @Resource
    private SapRouteApiService sapRouteApiService;

    @ApiOperation(value = "请求雷赛工艺路线信息",notes = "请求雷赛工艺路线信息")
    @PostMapping("/getRoute")
    public ResponseEntity getProLine(@ApiParam(value = "查询对象")@RequestBody SearchSapRouteApi searchSapRouteApi) throws ParseException {
        return ControllerUtil.returnCRUD(sapRouteApiService.getRoute(searchSapRouteApi));
    }


}
