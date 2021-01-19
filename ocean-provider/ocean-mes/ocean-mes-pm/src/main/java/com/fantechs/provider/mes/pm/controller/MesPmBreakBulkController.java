package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulk;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulk;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmBreakBulkService;
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
import java.util.List;

/**
 * @author mr.lei
 * @date 2021-01-18 11:30:42
 */
@RestController
@Api(tags = "拆批/合批作业")
@RequestMapping("/mesPmBreakBulk")
@Validated
public class MesPmBreakBulkController {

    @Resource
    private MesPmBreakBulkService mesPmBreakBulkService;

//    @ApiOperation(value = "新增",notes = "新增")
//    @PostMapping("/add")
//    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmBreakBulk mesPmBreakBulk) {
//        return ControllerUtil.returnCRUD(mesPmBreakBulkService.save(mesPmBreakBulk));
//    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(mesPmBreakBulkService.batchDelete(ids));
//    }
//
//    @ApiOperation("修改")
//    @PostMapping("/update")
//    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmBreakBulk.update.class) MesPmBreakBulk mesPmBreakBulk) {
//        return ControllerUtil.returnCRUD(mesPmBreakBulkService.update(mesPmBreakBulk));
//    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmBreakBulk> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmBreakBulk  mesPmBreakBulk = mesPmBreakBulkService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmBreakBulk,StringUtils.isEmpty(mesPmBreakBulk)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmBreakBulkDto>> findList(@ApiParam(value = "查询对象,breakBulkType 必填")@RequestBody SearchMesPmBreakBulk searchMesPmBreakBulk) {
        Page<Object> page = PageHelper.startPage(searchMesPmBreakBulk.getStartPage(),searchMesPmBreakBulk.getPageSize());
        List<MesPmBreakBulkDto> list = mesPmBreakBulkService.findList(searchMesPmBreakBulk);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<MesPmBreakBulk>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmBreakBulk searchMesPmBreakBulk) {
//        Page<Object> page = PageHelper.startPage(searchMesPmBreakBulk.getStartPage(),searchMesPmBreakBulk.getPageSize());
//        List<MesPmBreakBulk> list = mesPmBreakBulkService.findHtList(ControllerUtil.dynamicConditionByEntity(searchMesPmBreakBulk));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesPmBreakBulk searchMesPmBreakBulk){
    List<MesPmBreakBulkDto> list = mesPmBreakBulkService.findList(searchMesPmBreakBulk);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesPmBreakBulk信息", MesPmBreakBulkDto.class, "MesPmBreakBulk.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
