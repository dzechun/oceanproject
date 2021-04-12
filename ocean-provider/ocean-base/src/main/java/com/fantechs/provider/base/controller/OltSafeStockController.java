package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.OltSafeStockDto;
import com.fantechs.common.base.general.entity.basic.OltSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchOltSafeStock;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.OltSafeStockService;
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
@RequestMapping("/oltSafeStock")
@Validated
public class OltSafeStockController {

    @Resource
    private OltSafeStockService oltSafeStockService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OltSafeStock oltSafeStock) {
        return ControllerUtil.returnCRUD(oltSafeStockService.save(oltSafeStock));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(oltSafeStockService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OltSafeStock.update.class) OltSafeStock oltSafeStock) {
        return ControllerUtil.returnCRUD(oltSafeStockService.update(oltSafeStock));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OltSafeStock> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OltSafeStock  oltSafeStock = oltSafeStockService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(oltSafeStock,StringUtils.isEmpty(oltSafeStock)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OltSafeStockDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOltSafeStock searchOltSafeStock) {
        Page<Object> page = PageHelper.startPage(searchOltSafeStock.getStartPage(),searchOltSafeStock.getPageSize());
        List<OltSafeStockDto> list = oltSafeStockService.findList(searchOltSafeStock);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OltSafeStockDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOltSafeStock searchOltSafeStock) {
        Page<Object> page = PageHelper.startPage(searchOltSafeStock.getStartPage(),searchOltSafeStock.getPageSize());
        List<OltSafeStockDto> list = oltSafeStockService.findHtList(searchOltSafeStock);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOltSafeStock searchOltSafeStock){
    List<OltSafeStockDto> list = oltSafeStockService.findList(searchOltSafeStock);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "安全库存信息", OltSafeStockDto.class, "安全库存信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @GetMapping("/inventeryWarning")
    @ApiOperation("预警")
    public ResponseEntity inventeryWarning(){
        return ControllerUtil.returnCRUD(oltSafeStockService.inventeryWarning());
    }
}
