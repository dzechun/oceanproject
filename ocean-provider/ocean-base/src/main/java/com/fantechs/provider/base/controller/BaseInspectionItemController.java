package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItem;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionItem;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionItem;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtInspectionItemService;
import com.fantechs.provider.base.service.BaseInspectionItemService;
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
 * Created by leifengzhi on 2021/06/03.
 */
@RestController
@Api(tags = "检验项目")
@RequestMapping("/baseInspectionItem")
@Validated
public class BaseInspectionItemController {

    @Resource
    private BaseInspectionItemService baseInspectionItemService;
    @Resource
    private BaseHtInspectionItemService baseHtInspectionItemService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseInspectionItem baseInspectionItem) {
        return ControllerUtil.returnCRUD(baseInspectionItemService.save(baseInspectionItem));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseInspectionItemService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseInspectionItem.update.class) BaseInspectionItem baseInspectionItem) {
        return ControllerUtil.returnCRUD(baseInspectionItemService.update(baseInspectionItem));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInspectionItem> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseInspectionItem  baseInspectionItem = baseInspectionItemService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseInspectionItem,StringUtils.isEmpty(baseInspectionItem)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInspectionItem>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionItem searchBaseInspectionItem) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionItem.getStartPage(),searchBaseInspectionItem.getPageSize());
        List<BaseInspectionItem> list = baseInspectionItemService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInspectionItem>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionItem searchBaseInspectionItem) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionItem.getStartPage(),searchBaseInspectionItem.getPageSize());
        List<BaseHtInspectionItem> list = baseHtInspectionItemService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseInspectionItem searchBaseInspectionItem){
    List<BaseInspectionItem> list = baseInspectionItemService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionItem));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "检验项目", BaseInspectionItem.class, "检验项目.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
