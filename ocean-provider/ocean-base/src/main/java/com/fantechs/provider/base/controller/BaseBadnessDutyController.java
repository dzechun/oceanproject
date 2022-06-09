package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseBadnessDutyDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessDuty;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessDuty;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBadnessDuty;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseBadnessDutyService;
import com.fantechs.provider.base.service.BaseHtBadnessDutyService;
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
 * Created by leifengzhi on 2021/04/08.
 */
@RestController
@Api(tags = "不良责任控制器")
@RequestMapping("/baseBadnessDuty")
@Validated
public class BaseBadnessDutyController {

    @Resource
    private BaseBadnessDutyService baseBadnessDutyService;
    @Resource
    private BaseHtBadnessDutyService baseHtBadnessDutyService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBadnessDuty baseBadnessDuty) {
        return ControllerUtil.returnCRUD(baseBadnessDutyService.save(baseBadnessDuty));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseBadnessDutyService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseBadnessDuty.update.class) BaseBadnessDuty baseBadnessDuty) {
        return ControllerUtil.returnCRUD(baseBadnessDutyService.update(baseBadnessDuty));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBadnessDuty> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBadnessDuty  baseBadnessDuty = baseBadnessDutyService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBadnessDuty,StringUtils.isEmpty(baseBadnessDuty)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBadnessDutyDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessDuty searchBaseBadnessDuty) {
        Page<Object> page = PageHelper.startPage(searchBaseBadnessDuty.getStartPage(),searchBaseBadnessDuty.getPageSize());
        List<BaseBadnessDutyDto> list = baseBadnessDutyService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessDuty));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtBadnessDuty>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessDuty searchBaseBadnessDuty) {
        Page<Object> page = PageHelper.startPage(searchBaseBadnessDuty.getStartPage(),searchBaseBadnessDuty.getPageSize());
        List<BaseHtBadnessDuty> list = baseHtBadnessDutyService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessDuty));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseBadnessDuty searchBaseBadnessDuty){
    List<BaseBadnessDutyDto> list = baseBadnessDutyService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessDuty));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "BaseBadnessDuty信息", "BaseBadnessDuty.xls", response);

    }
}
