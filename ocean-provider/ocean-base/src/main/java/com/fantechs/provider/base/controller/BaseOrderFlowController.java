package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseOrderFlowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/07.
 */
@RestController
@Api(tags = "baseOrderFlow 单据流配置控制器")
@RequestMapping("/baseOrderFlow")
@Validated
public class BaseOrderFlowController {

    @Resource
    private BaseOrderFlowService baseOrderFlowService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseOrderFlow baseOrderFlow) {
        return ControllerUtil.returnCRUD(baseOrderFlowService.save(baseOrderFlow));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseOrderFlowService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseOrderFlow.update.class) BaseOrderFlow baseOrderFlow) {
        return ControllerUtil.returnCRUD(baseOrderFlowService.update(baseOrderFlow));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseOrderFlow> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseOrderFlow  baseOrderFlow = baseOrderFlowService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseOrderFlow,StringUtils.isEmpty(baseOrderFlow)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseOrderFlowDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseOrderFlow searchBaseOrderFlow) {
        Page<Object> page = PageHelper.startPage(searchBaseOrderFlow.getStartPage(),searchBaseOrderFlow.getPageSize());
        List<BaseOrderFlowDto> list = baseOrderFlowService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlow));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseOrderFlowDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseOrderFlow searchBaseOrderFlow) {
        List<BaseOrderFlowDto> list = baseOrderFlowService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlow));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseOrderFlow searchBaseOrderFlow){
    List<BaseOrderFlowDto> list = baseOrderFlowService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseOrderFlow));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseOrderFlow信息", BaseOrderFlowDto.class, "BaseOrderFlow.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
