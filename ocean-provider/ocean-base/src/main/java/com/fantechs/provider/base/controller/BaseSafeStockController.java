package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSafeStockDto;
import com.fantechs.common.base.general.entity.basic.BaseSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSafeStock;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseSafeStockService;
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
 * Created by mr.lei on 2021/03/04.
 */
@RestController
@Api(tags = "安全库存")
@RequestMapping("/baseSafeStock")
@Validated
public class BaseSafeStockController {

    @Resource
    private BaseSafeStockService baseSafeStockService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSafeStock baseSafeStock) {
        return ControllerUtil.returnCRUD(baseSafeStockService.save(baseSafeStock));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSafeStockService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseSafeStock.update.class) BaseSafeStock baseSafeStock) {
        return ControllerUtil.returnCRUD(baseSafeStockService.update(baseSafeStock));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSafeStock> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSafeStock baseSafeStock = baseSafeStockService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSafeStock,StringUtils.isEmpty(baseSafeStock)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSafeStockDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSafeStock searchBaseSafeStock) {
        Page<Object> page = PageHelper.startPage(searchBaseSafeStock.getStartPage(), searchBaseSafeStock.getPageSize());
        List<BaseSafeStockDto> list = baseSafeStockService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSafeStock));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseSafeStockDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSafeStock searchBaseSafeStock) {
        Page<Object> page = PageHelper.startPage(searchBaseSafeStock.getStartPage(), searchBaseSafeStock.getPageSize());
        List<BaseSafeStockDto> list = baseSafeStockService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseSafeStock));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSafeStock searchBaseSafeStock){
    List<BaseSafeStockDto> list = baseSafeStockService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSafeStock));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "安全库存信息", BaseSafeStockDto.class, "安全库存信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @GetMapping("/inventeryWarning")
    @ApiOperation("预警")
    public ResponseEntity inventeryWarning(){
        return ControllerUtil.returnCRUD(baseSafeStockService.inventeryWarning());
    }
}
