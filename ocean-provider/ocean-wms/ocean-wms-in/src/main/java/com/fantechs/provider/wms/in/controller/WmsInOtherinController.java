package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInOtherin;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInOtherin;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInOtherinService;
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
@Api(tags = "其他入库控制器")
@RequestMapping("/wmsInOtherin")
@Validated
public class WmsInOtherinController {

    @Resource
    private WmsInOtherinService wmsInOtherinService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInOtherin wmsInOtherin) {
        return ControllerUtil.returnCRUD(wmsInOtherinService.save(wmsInOtherin));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInOtherinService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInOtherin.update.class) WmsInOtherin wmsInOtherin) {
        return ControllerUtil.returnCRUD(wmsInOtherinService.update(wmsInOtherin));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInOtherin> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInOtherin  wmsInOtherin = wmsInOtherinService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInOtherin,StringUtils.isEmpty(wmsInOtherin)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInOtherinDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInOtherin searchWmsInOtherin) {
        Page<Object> page = PageHelper.startPage(searchWmsInOtherin.getStartPage(),searchWmsInOtherin.getPageSize());
        List<WmsInOtherinDto> list = wmsInOtherinService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInOtherin));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInOtherinDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInOtherin searchWmsInOtherin) {
        Page<Object> page = PageHelper.startPage(searchWmsInOtherin.getStartPage(),searchWmsInOtherin.getPageSize());
        List<WmsInOtherinDto> list = wmsInOtherinService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInOtherin));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInOtherin searchWmsInOtherin){
    List<WmsInOtherinDto> list = wmsInOtherinService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInOtherin));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInOtherin信息", WmsInOtherinDto.class, "WmsInOtherin.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
