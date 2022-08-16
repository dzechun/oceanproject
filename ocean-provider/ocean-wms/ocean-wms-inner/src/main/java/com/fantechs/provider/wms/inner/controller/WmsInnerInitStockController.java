package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.InitStockImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStock;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInitStock;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@RestController
@Api(tags = "初始化盘点")
@RequestMapping("/wmsInnerInitStock")
@Validated
@Slf4j
public class WmsInnerInitStockController {

    @Resource
    private WmsInnerInitStockService wmsInnerInitStockService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerInitStock wmsInnerInitStock) {
        return ControllerUtil.returnCRUD(wmsInnerInitStockService.save(wmsInnerInitStock));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerInitStockService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerInitStock.update.class) WmsInnerInitStock wmsInnerInitStock) {
        return ControllerUtil.returnCRUD(wmsInnerInitStockService.update(wmsInnerInitStock));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerInitStock> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerInitStock  wmsInnerInitStock = wmsInnerInitStockService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerInitStock,StringUtils.isEmpty(wmsInnerInitStock)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInitStockDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInitStock searchWmsInnerInitStock) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInitStock.getStartPage(),searchWmsInnerInitStock.getPageSize());
        List<WmsInnerInitStockDto> list = wmsInnerInitStockService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInitStock));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerInitStock searchWmsInnerInitStock){
    List<WmsInnerInitStockDto> list = wmsInnerInitStockService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInitStock));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "初始化盘点", WmsInnerInitStockDto.class, "初始化盘点.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<InitStockImport> initStockImports = EasyPoiUtils.importExcel(file, 0, 1, InitStockImport.class);
            Map<String, Object> resultMap = wmsInnerInitStockService.importExcel(initStockImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
