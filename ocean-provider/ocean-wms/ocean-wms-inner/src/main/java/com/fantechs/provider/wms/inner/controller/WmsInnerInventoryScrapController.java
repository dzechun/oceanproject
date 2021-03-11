package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrap;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtInventoryScrap;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryScrap;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryScrapService;
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
 * Created by leifengzhi on 2021/03/10.
 */
@RestController
@Api(tags = "盘存转报废单控制器")
@RequestMapping("/wmsInnerInventoryScrap")
@Validated
public class WmsInnerInventoryScrapController {

    @Resource
    private WmsInnerInventoryScrapService wmsInnerInventoryScrapService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerInventoryScrap wmsInnerInventoryScrap) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryScrapService.save(wmsInnerInventoryScrap));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryScrapService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerInventoryScrap.update.class) WmsInnerInventoryScrap wmsInnerInventoryScrap) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryScrapService.update(wmsInnerInventoryScrap));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerInventoryScrap> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerInventoryScrap  wmsInnerInventoryScrap = wmsInnerInventoryScrapService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerInventoryScrap,StringUtils.isEmpty(wmsInnerInventoryScrap)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInventoryScrapDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventoryScrap searchWmsInnerInventoryScrap) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventoryScrap.getStartPage(),searchWmsInnerInventoryScrap.getPageSize());
        List<WmsInnerInventoryScrapDto> list = wmsInnerInventoryScrapService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryScrap));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInnerHtInventoryScrap>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventoryScrap searchWmsInnerInventoryScrap) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventoryScrap.getStartPage(),searchWmsInnerInventoryScrap.getPageSize());
        List<WmsInnerHtInventoryScrap> list = wmsInnerInventoryScrapService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryScrap));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerInventoryScrap searchWmsInnerInventoryScrap){
    List<WmsInnerInventoryScrapDto> list = wmsInnerInventoryScrapService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryScrap));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerInventoryScrap信息", WmsInnerInventoryScrapDto.class, "WmsInnerInventoryScrap.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
