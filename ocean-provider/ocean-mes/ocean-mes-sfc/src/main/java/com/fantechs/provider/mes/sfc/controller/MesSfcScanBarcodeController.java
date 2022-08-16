package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.mes.sfc.service.MesSfcScanBarcodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 生产管理-生产过站
 *
 * @author
 * @date 2021-04-15
 */
@RestController
@Api(tags = "生产管理-生产过站")
@RequestMapping("/mesSfcScanBarcode")
public class MesSfcScanBarcodeController {

    @Resource
    MesSfcScanBarcodeService mesSfcScanBarcodeService;

    @ApiOperation("过站作业-用户登录信息校验")
    @PostMapping("/chkLogUserInfo")
    public ResponseEntity<BaseExecuteResultDto> chkLogUserInfo(@ApiParam(value = "用户登录信息校验", required = true) @RequestBody RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws Exception{
        return ControllerUtil.returnSuccess("", mesSfcScanBarcodeService.chkLogUserInfo(restapiChkLogUserInfoApiDto));
    }

}
