package com.fantechs.provider.materialapi.imes.controller;

import com.fantechs.common.base.general.dto.restapi.SapPushMessageApi;
import com.fantechs.common.base.general.dto.restapi.SearchSapReportWorkApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.materialapi.imes.service.SapPushMessageApiService;
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
 * Created by leifengzhi on 2021/09/07.
 */
@RestController
@Api(tags = "消息推送接口")
@RequestMapping("/sapPushMessage")
@Validated
public class SapPushMessageApiController {

    @Resource
    private SapPushMessageApiService sapPushMessageApiService;

    @ApiOperation(value = "消息推送接口",notes = "消息推送接口")
    @PostMapping("/pushMessage")
    public ResponseEntity pushMessageApi(@ApiParam(value = "查询对象")@RequestBody SapPushMessageApi sapPushMessageApi) throws ParseException {
        return ControllerUtil.returnCRUD(sapPushMessageApiService.sendPushMessage(sapPushMessageApi));
    }
}
