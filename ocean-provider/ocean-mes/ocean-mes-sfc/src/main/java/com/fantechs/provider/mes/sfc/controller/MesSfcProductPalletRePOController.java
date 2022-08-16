package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletRePODto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPalletRePO;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletRePOService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产管理-产品条码过站表
 * mes_sfc_barcode_process
 * @author hyc
 * @date 2021-04-09 15:29:27
 */
@RestController
@Api(tags = "更改条码匹配PO控制器")
@RequestMapping("/mesSfcProductPalletRePO")
@Validated
public class MesSfcProductPalletRePOController {

    @Resource
    private MesSfcProductPalletRePOService mesSfcProductPalletRePOService;


    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcProductPalletRePODto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcProductPalletRePO searchMesSfcProductPalletRePO) {
        Page<Object> page = PageHelper.startPage(searchMesSfcProductPalletRePO.getStartPage(),searchMesSfcProductPalletRePO.getPageSize());
        List<MesSfcProductPalletRePODto> list = mesSfcProductPalletRePOService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcProductPalletRePO));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("提交")
    @PostMapping("/submit")
    public ResponseEntity updateBarcodePO(@ApiParam(value = "对象")@RequestBody MesSfcProductPalletRePODto mesSfcProductPalletRePODto) throws Exception {
        return ControllerUtil.returnCRUD(mesSfcProductPalletRePOService.updateBarcodePO(mesSfcProductPalletRePODto));
    }

}
