package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.history.WmsInHtFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInFinishedProduct;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInFinishedProductService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by leifengzhi on 2021/01/07.
 */
@RestController
@Api(tags = "成品入库控制器")
@RequestMapping("/wmsInFinishedProduct")
@Validated
public class WmsInFinishedProductController {

    @Resource
    private WmsInFinishedProductService wmsInFinishedProductService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInFinishedProduct wmsInFinishedProduct) {
        int i = wmsInFinishedProductService.save(wmsInFinishedProduct);
        return ControllerUtil.returnDataSuccess(wmsInFinishedProduct,i);
    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(wmsInFinishedProductService.batchDelete(ids));
//    }
//
    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInFinishedProduct.update.class) WmsInFinishedProduct wmsInFinishedProduct) {
        return ControllerUtil.returnCRUD(wmsInFinishedProductService.update(wmsInFinishedProduct));
    }

    @ApiOperation("PDA-提交")
    @PostMapping("/PDASubmit")
    public ResponseEntity<WmsInFinishedProduct> PDASubmit(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInFinishedProduct wmsInFinishedProduct) {
        return  ControllerUtil.returnCRUD(wmsInFinishedProductService.PDASubmit(wmsInFinishedProduct));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInFinishedProduct> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInFinishedProduct  wmsInFinishedProduct = wmsInFinishedProductService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInFinishedProduct,StringUtils.isEmpty(wmsInFinishedProduct)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInFinishedProductDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInFinishedProduct searchWmsInFinishedProduct) {
        Page<Object> page = PageHelper.startPage(searchWmsInFinishedProduct.getStartPage(),searchWmsInFinishedProduct.getPageSize());
        List<WmsInFinishedProductDto> list = wmsInFinishedProductService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInFinishedProduct));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInHtFinishedProduct>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInFinishedProduct searchWmsInFinishedProduct) {
        Page<Object> page = PageHelper.startPage(searchWmsInFinishedProduct.getStartPage(),searchWmsInFinishedProduct.getPageSize());
        List<WmsInHtFinishedProduct> list = wmsInFinishedProductService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInFinishedProduct));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInFinishedProduct searchWmsInFinishedProduct){
    List<WmsInFinishedProductDto> list = wmsInFinishedProductService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInFinishedProduct));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInFinishedProduct信息", WmsInFinishedProductDto.class, "WmsInFinishedProduct.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
