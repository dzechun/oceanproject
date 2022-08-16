package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseTabService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */
@RestController
@Api(tags = "页签信息管理")
@RequestMapping("/baseTab")
@Validated
public class BaseTabController {

    @Autowired
    private BaseTabService baseTabService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseTab baseTab) {
        return ControllerUtil.returnCRUD(baseTabService.save(baseTab));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "页签集合")@RequestBody @Validated List<BaseTab> baseTabs) {
        return ControllerUtil.returnCRUD(baseTabService.batchDelete(baseTabs));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseTab.update.class) BaseTab baseTab) {
        return ControllerUtil.returnCRUD(baseTabService.update(baseTab));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseTab> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseTab  baseTab = baseTabService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseTab,StringUtils.isEmpty(baseTab)?0:1);
    }

    @ApiOperation("页签信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseTabDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseTab searchBaseTab) {
        Page<Object> page = PageHelper.startPage(searchBaseTab.getStartPage(),searchBaseTab.getPageSize());
        List<BaseTabDto> list = baseTabService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseTab));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("页签信息列表")
    @PostMapping("/getAll")
    public ResponseEntity<List<BaseTabDto>> getAll() {
        List<BaseTabDto> list = baseTabService.findList(new HashMap<>());
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

/*    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseTab>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseTab searchBaseTab) {
        Page<Object> page = PageHelper.startPage(searchBaseTab.getStartPage(),searchBaseTab.getPageSize());
        List<BaseTab> list = baseTabService.findList(searchBaseTab);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseTab searchBaseTab){
    List<BaseTabDto> list = baseTabService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseTab));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseTab信息", BaseTab.class, "BaseTab.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/insertList")
    public ResponseEntity insertList(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<BaseTab> baseTabs) {
        return ControllerUtil.returnCRUD(baseTabService.insertList(baseTabs));
    }
}
