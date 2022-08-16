package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeReprintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 生产管理-包箱栈板条码补打控制器
 *
 * @author bgkun
 * @date 2021-05-19
 */
@RestController
@Api(tags = "生产管理-PDA包箱栈板条码补打作业")
@RequestMapping("/mesSfcBarcodeReprint")
public class MesSfcBarcodeReprintController {

    @Resource
    MesSfcBarcodeReprintService mesSfcBarcodeReprintService;

    @ApiOperation("查询条码（模糊匹配）")
    @GetMapping("/findBarcode")
    public ResponseEntity<List<String>> findBarcode(@ApiParam(value = "号码", required = true) @RequestParam @NotNull(message = "keyword不能为空") String keyword,
                                              @ApiParam(value = "号码类型，1:包箱，2:栈板", required = true) @RequestParam @NotNull(message = "barocdeType不能为空") String barocdeType){
        List<String> list = mesSfcBarcodeReprintService.findCode(keyword, barocdeType);
        return ControllerUtil.returnDataSuccess(list, 1);
    }

    @ApiOperation("补打条码")
    @PostMapping("/reprintBarcode")
    public ResponseEntity reprintBarcode(@ApiParam(value = "号码", required = true) @RequestParam @NotNull(message = "keyword不能为空") String barCode,
                                         @ApiParam(value = "号码类型，1:包箱，2:栈板", required = true) @RequestParam @NotNull(message = "barocdeType不能为空") byte barocdeType,
                                         @ApiParam(value = "打印名称", required = true) @RequestParam String printName) throws Exception{
        return ControllerUtil.returnCRUD(mesSfcBarcodeReprintService.reprintBarcode(barCode, barocdeType, printName));
    }

}
