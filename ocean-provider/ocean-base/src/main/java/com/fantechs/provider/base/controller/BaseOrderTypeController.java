package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseOrderTypeDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderType;
import com.fantechs.common.base.general.entity.basic.history.BaseHtOrderType;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderType;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtOrderTypeService;
import com.fantechs.provider.base.service.BaseOrderTypeService;
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
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/23.
 */
@RestController
@Api(tags = "单据类型信息管理")
@RequestMapping("/baseOrderType")
@Validated
public class BaseOrderTypeController {

    @Resource
    private BaseOrderTypeService baseOrderTypeService;
    @Resource
    private BaseHtOrderTypeService baseHtOrderTypeService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseOrderType baseOrderType) {
        return ControllerUtil.returnCRUD(baseOrderTypeService.save(baseOrderType));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseOrderTypeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseOrderType.update.class) BaseOrderType baseOrderType) {
        return ControllerUtil.returnCRUD(baseOrderTypeService.update(baseOrderType));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseOrderType> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseOrderType  baseOrderType = baseOrderTypeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseOrderType,StringUtils.isEmpty(baseOrderType)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseOrderTypeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseOrderType searchBaseOrderType) {
        Page<Object> page = PageHelper.startPage(searchBaseOrderType.getStartPage(),searchBaseOrderType.getPageSize());
        List<BaseOrderTypeDto> list = baseOrderTypeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderType));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtOrderType>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseOrderType searchBaseOrderType) {
        Page<Object> page = PageHelper.startPage(searchBaseOrderType.getStartPage(),searchBaseOrderType.getPageSize());
        List<BaseHtOrderType> list = baseHtOrderTypeService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderType));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseOrderType searchBaseOrderType){
    List<BaseOrderTypeDto> list = baseOrderTypeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderType));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "BaseOrderType信息", "BaseOrderType.xls", response);
    }
}
