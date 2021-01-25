package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItem;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionItem;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionItem;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsHtInspectionItemService;
import com.fantechs.provider.qms.service.QmsInspectionItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "检验项目")
@RequestMapping("/qmsInspectionItem")
@Validated
public class QmsInspectionItemController {

    @Autowired
    private QmsInspectionItemService qmsInspectionItemService;
    @Autowired
    private QmsHtInspectionItemService qmsHtInspectionItemService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsInspectionItem qmsInspectionItem) {
        return ControllerUtil.returnCRUD(qmsInspectionItemService.save(qmsInspectionItem));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsInspectionItemService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsInspectionItem.update.class) QmsInspectionItem qmsInspectionItem) {
        return ControllerUtil.returnCRUD(qmsInspectionItemService.update(qmsInspectionItem));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsInspectionItem> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsInspectionItem  qmsInspectionItem = qmsInspectionItemService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsInspectionItem,StringUtils.isEmpty(qmsInspectionItem)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsInspectionItemDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionItem searchQmsInspectionItem) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionItem.getStartPage(),searchQmsInspectionItem.getPageSize());
        List<QmsInspectionItemDto> list = qmsInspectionItemService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtInspectionItem>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionItem searchQmsInspectionItem) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionItem.getStartPage(),searchQmsInspectionItem.getPageSize());
        List<QmsHtInspectionItem> list = qmsHtInspectionItemService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsInspectionItem searchQmsInspectionItem){
    List<QmsInspectionItemDto> list = qmsInspectionItemService.exportExcel(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionItem));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "检验项目导出信息", "检验项目信息", QmsInspectionItem.class, "检验项目.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
