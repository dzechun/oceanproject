package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutFinishedProduct;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutFinishedProductService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/22.
 */
@RestController
@Api(tags = "wmsOutFinishedProduct控制器")
@RequestMapping("/wmsOutFinishedProduct")
@Validated
public class WmsOutFinishedProductController {

    @Resource
    private WmsOutFinishedProductService wmsOutFinishedProductService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutFinishedProduct wmsOutFinishedProduct) {
        return ControllerUtil.returnCRUD(wmsOutFinishedProductService.save(wmsOutFinishedProduct));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutFinishedProductService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutFinishedProduct.update.class) WmsOutFinishedProduct wmsOutFinishedProduct) {
        return ControllerUtil.returnCRUD(wmsOutFinishedProductService.update(wmsOutFinishedProduct));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutFinishedProduct> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutFinishedProduct  wmsOutFinishedProduct = wmsOutFinishedProductService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutFinishedProduct,StringUtils.isEmpty(wmsOutFinishedProduct)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutFinishedProductDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutFinishedProduct searchWmsOutFinishedProduct) {
        Page<Object> page = PageHelper.startPage(searchWmsOutFinishedProduct.getStartPage(),searchWmsOutFinishedProduct.getPageSize());
        List<WmsOutFinishedProductDto> list = wmsOutFinishedProductService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutFinishedProduct));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsOutFinishedProduct>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutFinishedProduct searchWmsOutFinishedProduct) {
//        Page<Object> page = PageHelper.startPage(searchWmsOutFinishedProduct.getStartPage(),searchWmsOutFinishedProduct.getPageSize());
//        List<WmsOutFinishedProductDto> list = wmsOutFinishedProductService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutFinishedProduct));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutFinishedProduct searchWmsOutFinishedProduct){
    List<WmsOutFinishedProductDto> list = wmsOutFinishedProductService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutFinishedProduct));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutFinishedProduct信息", WmsOutFinishedProductDto.class, "WmsOutFinishedProduct.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
