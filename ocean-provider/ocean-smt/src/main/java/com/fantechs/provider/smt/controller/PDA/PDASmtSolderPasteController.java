package com.fantechs.provider.smt.controller.PDA;

import com.fantechs.common.base.general.dto.smt.SmtSolderPasteDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.smt.service.SmtSolderPasteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author mr.lei
 * @Date 2021/7/23
 */
@RestController
@Api(tags = "锡膏管理PDA")
@RequestMapping("/smtSolderPaste/PDA")
@Validated
public class PDASmtSolderPasteController {
    @Resource
    private SmtSolderPasteService smtSolderPasteService;

    @PostMapping("/scanSolder")
    @ApiOperation("PDA扫码")
    public ResponseEntity<SmtSolderPasteDto> scanSolder(@ApiParam("锡膏条码") @RequestParam String barCode,
                                                        @ApiParam("锡膏状态") @RequestParam Byte solderPasteStatus,
                                                        @ApiParam("0-第一次扫码 1-警告 2-生产日期过期执行")@RequestParam Integer PASS){

        SmtSolderPasteDto smtSolderPasteDto = smtSolderPasteService.scanSolder(barCode, solderPasteStatus, PASS);
        return ControllerUtil.returnDataSuccess(smtSolderPasteDto,1);
    }
}
