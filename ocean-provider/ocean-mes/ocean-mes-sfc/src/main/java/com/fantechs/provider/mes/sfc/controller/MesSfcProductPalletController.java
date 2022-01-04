package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcPalletReportDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/05/08.
 */
@RestController
@Api(tags = "生产管理-栈板管理控制器")
@RequestMapping("/mesSfcProductPallet")
@Validated
public class MesSfcProductPalletController {

    @Resource
    private MesSfcProductPalletService mesSfcProductPalletService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcProductPallet mesSfcProductPallet) {
        return ControllerUtil.returnCRUD(mesSfcProductPalletService.save(mesSfcProductPallet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcProductPalletService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcProductPallet.update.class) MesSfcProductPallet mesSfcProductPallet) {
        return ControllerUtil.returnCRUD(mesSfcProductPalletService.update(mesSfcProductPallet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcProductPallet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcProductPallet  mesSfcProductPallet = mesSfcProductPalletService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcProductPallet,StringUtils.isEmpty(mesSfcProductPallet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcProductPalletDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcProductPallet searchMesSfcProductPallet) {
        Page<Object> page = PageHelper.startPage(searchMesSfcProductPallet.getStartPage(),searchMesSfcProductPallet.getPageSize());
        List<MesSfcProductPalletDto> list = mesSfcProductPalletService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcProductPallet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("栈板看板")
    @PostMapping("/getPalletReport")
    public ResponseEntity<List<MesSfcPalletReportDto>> getPalletReport(){
        List<MesSfcPalletReportDto> reportDtos = mesSfcProductPalletService.getPalletReport();
        return ControllerUtil.returnDataSuccess(reportDtos, reportDtos.size());

    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcProductPallet searchMesSfcProductPallet){
    List<MesSfcProductPalletDto> list = mesSfcProductPalletService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcProductPallet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcProductPallet信息", MesSfcProductPalletDto.class, "MesSfcProductPallet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
