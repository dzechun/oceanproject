package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlowSourceDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlowSourceDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseOrderFlowSourceDetService;
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
 * Created by leifengzhi on 2022/02/15.
 */
@RestController
@Api(tags = "单据流数据源明细")
@RequestMapping("/baseOrderFlowSourceDet")
@Validated
public class BaseOrderFlowSourceDetController {

    @Resource
    private BaseOrderFlowSourceDetService baseOrderFlowSourceDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseOrderFlowSourceDet baseOrderFlowSourceDet) {
        return ControllerUtil.returnCRUD(baseOrderFlowSourceDetService.save(baseOrderFlowSourceDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseOrderFlowSourceDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseOrderFlowSourceDet.update.class) BaseOrderFlowSourceDet baseOrderFlowSourceDet) {
        return ControllerUtil.returnCRUD(baseOrderFlowSourceDetService.update(baseOrderFlowSourceDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseOrderFlowSourceDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseOrderFlowSourceDet  baseOrderFlowSourceDet = baseOrderFlowSourceDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseOrderFlowSourceDet,StringUtils.isEmpty(baseOrderFlowSourceDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseOrderFlowSourceDet>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseOrderFlowSourceDet searchBaseOrderFlowSourceDet) {
        Page<Object> page = PageHelper.startPage(searchBaseOrderFlowSourceDet.getStartPage(),searchBaseOrderFlowSourceDet.getPageSize());
        List<BaseOrderFlowSourceDet> list = baseOrderFlowSourceDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlowSourceDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseOrderFlowSourceDet>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseOrderFlowSourceDet searchBaseOrderFlowSourceDet) {
        List<BaseOrderFlowSourceDet> list = baseOrderFlowSourceDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlowSourceDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseOrderFlowSourceDet searchBaseOrderFlowSourceDet){
    List<BaseOrderFlowSourceDet> list = baseOrderFlowSourceDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlowSourceDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseOrderFlowSourceDet信息", BaseOrderFlowSourceDet.class, "BaseOrderFlowSourceDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
