package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDto;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanService;
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
 * Created by leifengzhi on 2021/06/02.
 */
@RestController
@Api(tags = "mesPmDailyPlan控制器")
@RequestMapping("/mesPmDailyPlan")
@Validated
public class MesPmDailyPlanController {

    @Resource
    private MesPmDailyPlanService mesPmDailyPlanService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmDailyPlan mesPmDailyPlan) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.save(mesPmDailyPlan));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<MesPmDailyPlan> list) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.batchSave(list));
    }

    @ApiOperation(value = "批量移除",notes = "批量移除")
    @PostMapping("/batchRemove")
    public ResponseEntity batchRemove(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<MesPmDailyPlan> list) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.batchRemove(list));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmDailyPlan.update.class) MesPmDailyPlan mesPmDailyPlan) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.update(mesPmDailyPlan));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmDailyPlan> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmDailyPlan  mesPmDailyPlan = mesPmDailyPlanService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmDailyPlan,StringUtils.isEmpty(mesPmDailyPlan)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmDailyPlanDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmDailyPlan searchMesPmDailyPlan) {
        Page<Object> page = PageHelper.startPage(searchMesPmDailyPlan.getStartPage(),searchMesPmDailyPlan.getPageSize());
        List<MesPmDailyPlanDto> list = mesPmDailyPlanService.findList(searchMesPmDailyPlan);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesPmDailyPlan searchMesPmDailyPlan){
    List<MesPmDailyPlanDto> list = mesPmDailyPlanService.findList(searchMesPmDailyPlan);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesPmDailyPlan信息", MesPmDailyPlanDto.class, "MesPmDailyPlan.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }



}
