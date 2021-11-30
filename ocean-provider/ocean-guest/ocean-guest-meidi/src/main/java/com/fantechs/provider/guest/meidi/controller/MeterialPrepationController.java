package com.fantechs.provider.guest.meidi.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.guest.meidi.entity.MeterialPrepation;
import com.fantechs.provider.guest.meidi.service.MeterialPrepationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */
@RestController
@Api(tags = "制程数据录入单")
@RequestMapping("/leisaiProcessInputOrder")
@Validated
@Slf4j
public class MeterialPrepationController {

    @Resource
    private MeterialPrepationService meterialPrepationService;


    @ApiOperation(value = "调用接口",notes = "调用接口")
    @PostMapping("/send")
    public ResponseEntity send(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MeterialPrepation meterialPrepation) {
        return ControllerUtil.returnCRUD(meterialPrepationService.send(meterialPrepation));
    }

}
