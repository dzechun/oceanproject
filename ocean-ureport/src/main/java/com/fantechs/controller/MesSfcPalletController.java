package com.fantechs.controller;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcPalletReportDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 北归 on 2021/05/25.
 */
@RestController
@Api(tags = "栈板看板")
@RequestMapping("/sfcPallet")
@Validated
public class MesSfcPalletController {

    @Resource
    SFCFeignApi sfcFeignApi;

    @ApiOperation("栈板看板")
    @PostMapping("/getPalletList")
    public ResponseEntity<List<MesSfcPalletReportDto>> getPalletList(){
        return ControllerUtil.returnDataSuccess(sfcFeignApi.getPalletReport().getData(), sfcFeignApi.getPalletReport().getData().size());
    }
}
