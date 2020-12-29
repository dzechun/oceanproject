package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtherout;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtheroutDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutOtherout;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutOtheroutService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */
@RestController
@Api(tags = "其他杂出单管理")
@RequestMapping("/wmsOutOtherout")
@Validated
public class WmsOutOtheroutController {

    @Autowired
    private WmsOutOtheroutService wmsOutOtheroutService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：otheroutTime、operatorId",required = true)@RequestBody @Validated WmsOutOtherout wmsOutOtherout) {
        return ControllerUtil.returnCRUD(wmsOutOtheroutService.save(wmsOutOtherout));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutOtheroutService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutOtherout.update.class) WmsOutOtherout wmsOutOtherout) {
        return ControllerUtil.returnCRUD(wmsOutOtheroutService.update(wmsOutOtherout));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutOtherout> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutOtherout  wmsOutOtherout = wmsOutOtheroutService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutOtherout,StringUtils.isEmpty(wmsOutOtherout)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutOtheroutDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutOtherout searchWmsOutOtherout) {
        Page<Object> page = PageHelper.startPage(searchWmsOutOtherout.getStartPage(),searchWmsOutOtherout.getPageSize());
        List<WmsOutOtheroutDto> list = wmsOutOtheroutService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutOtherout));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

/*    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutOtherout>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutOtherout searchWmsOutOtherout) {
        Page<Object> page = PageHelper.startPage(searchWmsOutOtherout.getStartPage(),searchWmsOutOtherout.getPageSize());
        List<WmsOutOtherout> list = wmsOutOtheroutService.findList(searchWmsOutOtherout);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutOtherout searchWmsOutOtherout){
    List<WmsOutOtheroutDto> list = wmsOutOtheroutService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutOtherout));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutOtherout信息", WmsOutOtheroutDto.class, "WmsOutOtherout.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
