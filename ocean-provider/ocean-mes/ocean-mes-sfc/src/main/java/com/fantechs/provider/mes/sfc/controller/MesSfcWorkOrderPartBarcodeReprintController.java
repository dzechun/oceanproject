package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderPartBarcodeReprintDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcWorkOrderPartBarcodeReprint;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderPartBarcodeReprint;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderPartBarcodeReprintService;
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
 * Created by leifengzhi on 2021/06/17.
 */
@RestController
@Api(tags = "生产管理-生产订单部件条码补打管理控制器")
@RequestMapping("/mesSfcWorkOrderPartBarcodeReprint")
@Validated
public class MesSfcWorkOrderPartBarcodeReprintController {

    @Resource
    private MesSfcWorkOrderPartBarcodeReprintService mesSfcWorkOrderPartBarcodeReprintService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcWorkOrderPartBarcodeReprint mesSfcWorkOrderPartBarcodeReprint) {
        return ControllerUtil.returnCRUD(mesSfcWorkOrderPartBarcodeReprintService.save(mesSfcWorkOrderPartBarcodeReprint));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcWorkOrderPartBarcodeReprintService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcWorkOrderPartBarcodeReprint.update.class) MesSfcWorkOrderPartBarcodeReprint mesSfcWorkOrderPartBarcodeReprint) {
        return ControllerUtil.returnCRUD(mesSfcWorkOrderPartBarcodeReprintService.update(mesSfcWorkOrderPartBarcodeReprint));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcWorkOrderPartBarcodeReprint> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcWorkOrderPartBarcodeReprint  mesSfcWorkOrderPartBarcodeReprint = mesSfcWorkOrderPartBarcodeReprintService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcWorkOrderPartBarcodeReprint,StringUtils.isEmpty(mesSfcWorkOrderPartBarcodeReprint)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcWorkOrderPartBarcodeReprintDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcWorkOrderPartBarcodeReprint searchMesSfcWorkOrderPartBarcodeReprint) {
        Page<Object> page = PageHelper.startPage(searchMesSfcWorkOrderPartBarcodeReprint.getStartPage(),searchMesSfcWorkOrderPartBarcodeReprint.getPageSize());
        List<MesSfcWorkOrderPartBarcodeReprintDto> list = mesSfcWorkOrderPartBarcodeReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcWorkOrderPartBarcodeReprint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcWorkOrderPartBarcodeReprint searchMesSfcWorkOrderPartBarcodeReprint){
    List<MesSfcWorkOrderPartBarcodeReprintDto> list = mesSfcWorkOrderPartBarcodeReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcWorkOrderPartBarcodeReprint));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcWorkOrderPartBarcodeReprint信息", MesSfcWorkOrderPartBarcodeReprintDto.class, "MesSfcWorkOrderPartBarcodeReprint.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
