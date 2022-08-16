package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJo;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJo;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutDespatchOrderReJoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
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
@Api(tags = "装车接口")
@RequestMapping("/wmsOutDespatchOrderReJo")
@Validated
public class WmsOutDespatchOrderReJoController {

    @Resource
    private WmsOutDespatchOrderReJoService wmsOutDespatchOrderReJoService;

    @ApiOperation(value = "装车",notes = "装车")
    @ApiIgnore
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<WmsOutDespatchOrderReJo> wmsOutDespatchOrderReJo) {
        int num = 0;
        for (WmsOutDespatchOrderReJo wms : wmsOutDespatchOrderReJo) {
            num += wmsOutDespatchOrderReJoService.save(wms);
        }
        return ControllerUtil.returnCRUD(num);
    }

    @ApiOperation("删除")
    @ApiIgnore
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderReJoService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @ApiIgnore
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutDespatchOrderReJo.update.class) WmsOutDespatchOrderReJo wmsOutDespatchOrderReJo) {
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderReJoService.update(wmsOutDespatchOrderReJo));
    }

    @ApiOperation("获取详情")
    @ApiIgnore
    @PostMapping("/detail")
    public ResponseEntity<WmsOutDespatchOrderReJo> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutDespatchOrderReJo  wmsOutDespatchOrderReJo = wmsOutDespatchOrderReJoService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutDespatchOrderReJo,StringUtils.isEmpty(wmsOutDespatchOrderReJo)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutDespatchOrderReJoDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDespatchOrderReJo searchWmsOutDespatchOrderReJo) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDespatchOrderReJo.getStartPage(),searchWmsOutDespatchOrderReJo.getPageSize());
        List<WmsOutDespatchOrderReJoDto> list = wmsOutDespatchOrderReJoService.findList(searchWmsOutDespatchOrderReJo);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsOutDespatchOrderReJo>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDespatchOrderReJo searchWmsOutDespatchOrderReJo) {
//        Page<Object> page = PageHelper.startPage(searchWmsOutDespatchOrderReJo.getStartPage(),searchWmsOutDespatchOrderReJo.getPageSize());
//        List<WmsOutDespatchOrderReJo> list = wmsOutDespatchOrderReJoService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDespatchOrderReJo));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutDespatchOrderReJo searchWmsOutDespatchOrderReJo){
    List<WmsOutDespatchOrderReJoDto> list = wmsOutDespatchOrderReJoService.findList(searchWmsOutDespatchOrderReJo);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutDespatchOrderReJo信息", WmsOutDespatchOrderReJoDto.class, "WmsOutDespatchOrderReJo.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
