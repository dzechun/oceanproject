package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsBadItemDto;
import com.fantechs.common.base.general.entity.qms.QmsBadItem;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsBadItem;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsBadItemService;
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
 * Created by leifengzhi on 2021/01/16.
 */
@RestController
@Api(tags = "不良项目")
@RequestMapping("/qmsBadItem")
@Validated
public class QmsBadItemController {

    @Resource
    private QmsBadItemService qmsBadItemService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsBadItem qmsBadItem) {
        return ControllerUtil.returnCRUD(qmsBadItemService.save(qmsBadItem));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsBadItemService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsBadItem.update.class) QmsBadItem qmsBadItem) {
        return ControllerUtil.returnCRUD(qmsBadItemService.update(qmsBadItem));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsBadItem> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsBadItem  qmsBadItem = qmsBadItemService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsBadItem,StringUtils.isEmpty(qmsBadItem)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsBadItemDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsBadItem searchQmsBadItem) {
        Page<Object> page = PageHelper.startPage(searchQmsBadItem.getStartPage(),searchQmsBadItem.getPageSize());
        List<QmsBadItemDto> list = qmsBadItemService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<QmsBadItem>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsBadItem searchQmsBadItem) {
//        Page<Object> page = PageHelper.startPage(searchQmsBadItem.getStartPage(),searchQmsBadItem.getPageSize());
//        List<QmsBadItem> list = qmsBadItemService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsBadItem));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsBadItem searchQmsBadItem){
    List<QmsBadItemDto> list = qmsBadItemService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadItem));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出不良项目信息", "不良项目信息", QmsBadItemDto.class, "不良项目.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
