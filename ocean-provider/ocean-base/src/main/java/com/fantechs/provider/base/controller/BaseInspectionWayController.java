package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionWay;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionWay;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtInspectionWayService;
import com.fantechs.provider.base.service.BaseInspectionWayService;
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
 * Created by leifengzhi on 2021/05/19.
 */
@RestController
@Api(tags = "检验方式控制器")
@RequestMapping("/baseInspectionWay")
@Validated
public class BaseInspectionWayController {

    @Resource
    private BaseInspectionWayService baseInspectionWayService;
    @Resource
    private BaseHtInspectionWayService baseHtInspectionWayService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseInspectionWay baseInspectionWay) {
        return ControllerUtil.returnCRUD(baseInspectionWayService.save(baseInspectionWay));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseInspectionWayService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseInspectionWay.update.class) BaseInspectionWay baseInspectionWay) {
        return ControllerUtil.returnCRUD(baseInspectionWayService.update(baseInspectionWay));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInspectionWay> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseInspectionWay  baseInspectionWay = baseInspectionWayService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseInspectionWay,StringUtils.isEmpty(baseInspectionWay)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInspectionWay>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionWay searchBaseInspectionWay) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionWay.getStartPage(),searchBaseInspectionWay.getPageSize());
        List<BaseInspectionWay> list = baseInspectionWayService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionWay));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInspectionWay>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionWay searchBaseInspectionWay) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionWay.getStartPage(),searchBaseInspectionWay.getPageSize());
        List<BaseHtInspectionWay> list = baseHtInspectionWayService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionWay));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseInspectionWay searchBaseInspectionWay){
        List<BaseInspectionWay> list = baseInspectionWayService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionWay));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "检验方式信息", "检验方式.xls", response);
    }
}
