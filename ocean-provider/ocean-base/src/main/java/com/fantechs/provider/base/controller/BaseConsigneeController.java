package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseConsigneeDto;
import com.fantechs.common.base.general.entity.basic.BaseConsignee;
import com.fantechs.common.base.general.entity.basic.history.BaseHtConsignee;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseConsignee;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseConsigneeService;
import com.fantechs.provider.base.service.BaseHtConsigneeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/23.
 */
@RestController
@Api(tags = "收货人信息")
@RequestMapping("/baseConsignee")
@Validated
public class BaseConsigneeController {

    @Resource
    private BaseConsigneeService baseConsigneeService;

    @Resource
    private BaseHtConsigneeService baseHtConsigneeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：consigneeCode、consigneeName、linkManName、linkManPhone",required = true)@RequestBody @Validated BaseConsignee baseConsignee) {
        return ControllerUtil.returnCRUD(baseConsigneeService.save(baseConsignee));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseConsigneeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseConsignee.update.class) BaseConsignee baseConsignee) {
        return ControllerUtil.returnCRUD(baseConsigneeService.update(baseConsignee));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseConsignee> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseConsignee  baseConsignee = baseConsigneeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseConsignee,StringUtils.isEmpty(baseConsignee)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseConsigneeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseConsignee searchBaseConsignee) {
        Page<Object> page = PageHelper.startPage(searchBaseConsignee.getStartPage(),searchBaseConsignee.getPageSize());
        List<BaseConsigneeDto> list = baseConsigneeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseConsignee));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("查询所有")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseConsigneeDto>> findAll() {
        List<BaseConsigneeDto> list = baseConsigneeService.findList(new HashMap<>());
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtConsignee>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseConsignee searchBaseConsignee) {
        Page<Object> page = PageHelper.startPage(searchBaseConsignee.getStartPage(),searchBaseConsignee.getPageSize());
        List<BaseHtConsignee> list = baseHtConsigneeService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseConsignee));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseConsignee searchBaseConsignee){
    List<BaseConsigneeDto> list = baseConsigneeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseConsignee));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出收货人信息", "收货人信息", BaseConsigneeDto.class, "收货人信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
