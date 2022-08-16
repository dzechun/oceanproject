package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPalletDet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPalletDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "生产管理-栈板条码管理控制器")
@RequestMapping("/mesSfcProductPalletDet")
@Validated
public class MesSfcProductPalletDetController {

    @Resource
    private MesSfcProductPalletDetService mesSfcProductPalletDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcProductPalletDet mesSfcProductPalletDet) {
        return ControllerUtil.returnCRUD(mesSfcProductPalletDetService.save(mesSfcProductPalletDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcProductPalletDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcProductPalletDet.update.class) MesSfcProductPalletDet mesSfcProductPalletDet) {
        return ControllerUtil.returnCRUD(mesSfcProductPalletDetService.update(mesSfcProductPalletDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcProductPalletDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcProductPalletDet  mesSfcProductPalletDet = mesSfcProductPalletDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcProductPalletDet,StringUtils.isEmpty(mesSfcProductPalletDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcProductPalletDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcProductPalletDet searchMesSfcProductPalletDet) {
        Page<Object> page = PageHelper.startPage(searchMesSfcProductPalletDet.getStartPage(),searchMesSfcProductPalletDet.getPageSize());
        List<MesSfcProductPalletDetDto> list = mesSfcProductPalletDetService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcProductPalletDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcProductPalletDet searchMesSfcProductPalletDet){
    List<MesSfcProductPalletDetDto> list = mesSfcProductPalletDetService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcProductPalletDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcProductPalletDet信息", MesSfcProductPalletDetDto.class, "MesSfcProductPalletDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
