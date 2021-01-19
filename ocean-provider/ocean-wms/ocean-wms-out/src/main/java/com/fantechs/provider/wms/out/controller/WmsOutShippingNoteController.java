package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNote;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutShippingNote;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutShippingNoteService;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@RestController
@Api(tags = "出货通知单控制器")
@RequestMapping("/wmsOutShippingNote")
@Validated
public class WmsOutShippingNoteController {

    @Resource
    private WmsOutShippingNoteService wmsOutShippingNoteService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutShippingNote wmsOutShippingNote) {
        return ControllerUtil.returnCRUD(wmsOutShippingNoteService.save(wmsOutShippingNote));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutShippingNoteService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutShippingNote.update.class) WmsOutShippingNote wmsOutShippingNote) {
        return ControllerUtil.returnCRUD(wmsOutShippingNoteService.update(wmsOutShippingNote));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutShippingNote> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutShippingNote  wmsOutShippingNote = wmsOutShippingNoteService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutShippingNote,StringUtils.isEmpty(wmsOutShippingNote)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutShippingNoteDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutShippingNote searchWmsOutShippingNote) {
        Page<Object> page = PageHelper.startPage(searchWmsOutShippingNote.getStartPage(),searchWmsOutShippingNote.getPageSize());
        List<WmsOutShippingNoteDto> list = wmsOutShippingNoteService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutShippingNote));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("PDA列表")
    @PostMapping("/PDAfindList")
    public ResponseEntity<List<WmsOutShippingNote>> PDAfindList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutShippingNote searchWmsOutShippingNote) {
        Page<Object> page = PageHelper.startPage(searchWmsOutShippingNote.getStartPage(),searchWmsOutShippingNote.getPageSize());
        List<WmsOutShippingNote> list = wmsOutShippingNoteService.PDAfindList(searchWmsOutShippingNote);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutShippingNoteDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutShippingNote searchWmsOutShippingNote) {
        Page<Object> page = PageHelper.startPage(searchWmsOutShippingNote.getStartPage(),searchWmsOutShippingNote.getPageSize());
        List<WmsOutShippingNoteDto> list = wmsOutShippingNoteService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutShippingNote));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutShippingNote searchWmsOutShippingNote){
    List<WmsOutShippingNoteDto> list = wmsOutShippingNoteService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutShippingNote));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutShippingNote信息", WmsOutShippingNoteDto.class, "WmsOutShippingNote.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("PDA-提交")
    @PostMapping("/submit")
    public ResponseEntity submit(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutShippingNote wmsOutShippingNote) {
        return ControllerUtil.returnCRUD(wmsOutShippingNoteService.submit(wmsOutShippingNote));
    }


}
