package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanStockListDto;
import com.fantechs.common.base.general.dto.mes.pm.imports.MesPmDailyPlanImport;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmHtDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanService;
import com.fantechs.provider.mes.pm.service.MesPmHtDailyPlanService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */
@RestController
@Api(tags = "工单日计划")
@RequestMapping("/mesPmDailyPlan")
@Validated
@Slf4j
public class MesPmDailyPlanController {

    @Resource
    private MesPmDailyPlanService mesPmDailyPlanService;
    @Resource
    private MesPmHtDailyPlanService mesPmHtDailyPlanService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmDailyPlanDto mesPmDailyPlanDto) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.save(mesPmDailyPlanDto));
    }

//    @ApiOperation(value = "批量新增",notes = "批量新增")
//    @PostMapping("/batchAdd")
//    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<MesPmDailyPlan> list){
//        return ControllerUtil.returnCRUD(mesPmDailyPlanService.batchSave(list));
//    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmDailyPlan.update.class) MesPmDailyPlanDto mesPmDailyPlanDto) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.update(mesPmDailyPlanDto));
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

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<MesPmDailyPlanStockListDto> mesPmDailyPlanStockListDtos) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanService.pushDown(mesPmDailyPlanStockListDtos));
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

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<MesPmDailyPlanImport> mesPmDailyPlanImports = EasyPoiUtils.importExcel(file, 2, 1, MesPmDailyPlanImport.class);
            Map<String, Object> resultMap = mesPmDailyPlanService.importExcel(mesPmDailyPlanImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

//    @ApiOperation("未来三日列表列表")
//    @PostMapping("/findDaysList")
//    public ResponseEntity<List<MesPmDailyPlanDto>> findDaysList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmDailyPlan searchMesPmDailyPlan) throws ParseException {
//        Page<Object> page = PageHelper.startPage(searchMesPmDailyPlan.getStartPage(),searchMesPmDailyPlan.getPageSize());
//        List<MesPmDailyPlanDto> list = mesPmDailyPlanService.findDaysList(searchMesPmDailyPlan);
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @ApiOperation("履历")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesPmHtDailyPlan>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmDailyPlan searchMesPmDailyPlan) {
        Page<Object> page = PageHelper.startPage(searchMesPmDailyPlan.getStartPage(),searchMesPmDailyPlan.getPageSize());
        List<MesPmHtDailyPlan> list = mesPmHtDailyPlanService.findList(searchMesPmDailyPlan);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
