package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInOtherinDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInOtherinDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInOtherinDetService;
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
 * Created by leifengzhi on 2021/01/12.
 */
@RestController
@Api(tags = "其他入库明细控制器")
@RequestMapping("/wmsInOtherinDet")
@Validated
public class WmsInOtherinDetController {

    @Resource
    private WmsInOtherinDetService wmsInOtherinDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInOtherinDet wmsInOtherinDet) {
        return ControllerUtil.returnCRUD(wmsInOtherinDetService.save(wmsInOtherinDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInOtherinDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInOtherinDet.update.class) WmsInOtherinDet wmsInOtherinDet) {
        return ControllerUtil.returnCRUD(wmsInOtherinDetService.update(wmsInOtherinDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInOtherinDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInOtherinDet  wmsInOtherinDet = wmsInOtherinDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInOtherinDet,StringUtils.isEmpty(wmsInOtherinDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInOtherinDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInOtherinDet searchWmsInOtherinDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInOtherinDet.getStartPage(),searchWmsInOtherinDet.getPageSize());
        List<WmsInOtherinDetDto> list = wmsInOtherinDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInOtherinDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsInOtherinDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInOtherinDet searchWmsInOtherinDet) {
//        Page<Object> page = PageHelper.startPage(searchWmsInOtherinDet.getStartPage(),searchWmsInOtherinDet.getPageSize());
//        List<WmsInOtherinDetDto> list = wmsInOtherinDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInOtherinDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInOtherinDet searchWmsInOtherinDet){
    List<WmsInOtherinDetDto> list = wmsInOtherinDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInOtherinDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInOtherinDet信息", WmsInOtherinDetDto.class, "WmsInOtherinDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
