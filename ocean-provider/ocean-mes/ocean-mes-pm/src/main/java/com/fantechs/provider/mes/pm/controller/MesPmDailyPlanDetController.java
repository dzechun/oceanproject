package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlanDet;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanDetService;
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
 * Created by leifengzhi on 2021/12/21.
 */
@RestController
@Api(tags = "日计划明细控制器")
@RequestMapping("/mesPmDailyPlanDet")
@Validated
public class MesPmDailyPlanDetController {

    @Resource
    private MesPmDailyPlanDetService mesPmDailyPlanDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmDailyPlanDet mesPmDailyPlanDet) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanDetService.save(mesPmDailyPlanDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmDailyPlanDet.update.class) MesPmDailyPlanDet mesPmDailyPlanDet) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanDetService.update(mesPmDailyPlanDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmDailyPlanDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmDailyPlanDet  mesPmDailyPlanDet = mesPmDailyPlanDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmDailyPlanDet,StringUtils.isEmpty(mesPmDailyPlanDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmDailyPlanDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmDailyPlanDet searchMesPmDailyPlanDet) {
        Page<Object> page = PageHelper.startPage(searchMesPmDailyPlanDet.getStartPage(),searchMesPmDailyPlanDet.getPageSize());
        List<MesPmDailyPlanDetDto> list = mesPmDailyPlanDetService.findList(searchMesPmDailyPlanDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<MesPmDailyPlanDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchMesPmDailyPlanDet searchMesPmDailyPlanDet) {
        List<MesPmDailyPlanDetDto> list = mesPmDailyPlanDetService.findList(searchMesPmDailyPlanDet);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesPmDailyPlanDet searchMesPmDailyPlanDet){
    List<MesPmDailyPlanDetDto> list = mesPmDailyPlanDetService.findList(searchMesPmDailyPlanDet);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesPmDailyPlanDet信息", MesPmDailyPlanDetDto.class, "MesPmDailyPlanDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
