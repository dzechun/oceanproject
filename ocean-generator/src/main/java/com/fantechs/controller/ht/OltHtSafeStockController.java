package com.fantechs.controller.ht;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.entity.OltHtSafeStock;
import com.fantechs.service.OltHtSafeStockService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
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
@Api(tags = "oltHtSafeStock控制器")
@RequestMapping("/oltHtSafeStock")
@Validated
public class OltHtSafeStockController {

    @Resource
    private OltHtSafeStockService oltHtSafeStockService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OltHtSafeStock oltHtSafeStock) {
        return ControllerUtil.returnCRUD(oltHtSafeStockService.save(oltHtSafeStock));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(oltHtSafeStockService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OltHtSafeStock.update.class) OltHtSafeStock oltHtSafeStock) {
        return ControllerUtil.returnCRUD(oltHtSafeStockService.update(oltHtSafeStock));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OltHtSafeStock> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OltHtSafeStock  oltHtSafeStock = oltHtSafeStockService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(oltHtSafeStock,StringUtils.isEmpty(oltHtSafeStock)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OltHtSafeStockDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOltHtSafeStock searchOltHtSafeStock) {
        Page<Object> page = PageHelper.startPage(searchOltHtSafeStock.getStartPage(),searchOltHtSafeStock.getPageSize());
        List<OltHtSafeStockDto> list = oltHtSafeStockService.findList(ControllerUtil.dynamicConditionByEntity(searchOltHtSafeStock));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OltHtSafeStock>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOltHtSafeStock searchOltHtSafeStock) {
        Page<Object> page = PageHelper.startPage(searchOltHtSafeStock.getStartPage(),searchOltHtSafeStock.getPageSize());
        List<OltHtSafeStock> list = oltHtSafeStockService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOltHtSafeStock));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOltHtSafeStock searchOltHtSafeStock){
    List<OltHtSafeStockDto> list = oltHtSafeStockService.findList(ControllerUtil.dynamicConditionByEntity(searchOltHtSafeStock));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OltHtSafeStock信息", OltHtSafeStockDto.class, "OltHtSafeStock.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
