package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutDespatchOrderReJoReDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */
@RestController
@ApiIgnore
@RequestMapping("/wmsOutDespatchOrderReJoReDet")
@Validated
public class WmsOutDespatchOrderReJoReDetController {

    @Resource
    private WmsOutDespatchOrderReJoReDetService wmsOutDespatchOrderReJoReDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet) {
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderReJoReDetService.save(wmsOutDespatchOrderReJoReDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderReJoReDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutDespatchOrderReJoReDet.update.class) WmsOutDespatchOrderReJoReDet wmsOutDespatchOrderReJoReDet) {
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderReJoReDetService.update(wmsOutDespatchOrderReJoReDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutDespatchOrderReJoReDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutDespatchOrderReJoReDet  wmsOutDespatchOrderReJoReDet = wmsOutDespatchOrderReJoReDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutDespatchOrderReJoReDet,StringUtils.isEmpty(wmsOutDespatchOrderReJoReDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutDespatchOrderReJoReDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDespatchOrderReJoReDet.getStartPage(),searchWmsOutDespatchOrderReJoReDet.getPageSize());
        List<WmsOutDespatchOrderReJoReDetDto> list = wmsOutDespatchOrderReJoReDetService.findList(searchWmsOutDespatchOrderReJoReDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsOutDespatchOrderReJoReDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet) {
//        Page<Object> page = PageHelper.startPage(searchWmsOutDespatchOrderReJoReDet.getStartPage(),searchWmsOutDespatchOrderReJoReDet.getPageSize());
//        List<WmsOutDespatchOrderReJoReDet> list = wmsOutDespatchOrderReJoReDetService.findHtList(searchWmsOutDespatchOrderReJoReDet);
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet){
    List<WmsOutDespatchOrderReJoReDetDto> list = wmsOutDespatchOrderReJoReDetService.findList(searchWmsOutDespatchOrderReJoReDet);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutDespatchOrderReJoReDet信息", WmsOutDespatchOrderReJoReDetDto.class, "WmsOutDespatchOrderReJoReDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
