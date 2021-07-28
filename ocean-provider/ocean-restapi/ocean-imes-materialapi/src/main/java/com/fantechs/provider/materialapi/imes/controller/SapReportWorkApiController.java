package com.fantechs.provider.materialapi.imes.controller;

import com.fantechs.common.base.general.dto.restapi.SearchSapReportWorkApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.materialapi.imes.service.SapReportWorkApiService;
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
@Api(tags = "返写报工数据接口")
@RequestMapping("/reportWorkApi")
@Validated
public class SapReportWorkApiController {

    @Resource
    private SapReportWorkApiService sapReportWorkApiService;

    @ApiOperation(value = "返写报工数据",notes = "返写报工数据")
    @PostMapping("/sendReportWork")
    public ResponseEntity sendReportWork(@ApiParam(value = "查询对象")@RequestBody SearchSapReportWorkApi searchSapReportWorkApi) throws ParseException {
        return ControllerUtil.returnCRUD(sapReportWorkApiService.sendReportWork(searchSapReportWorkApi));
    }
}
