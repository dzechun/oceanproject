package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderMaterialReP;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderMaterialReP;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmHtWorkOrderMaterialRePService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderMaterialRePService;
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
 * Created by leifengzhi on 2021/04/28.
 */
@RestController
@Api(tags = "关键物料工单零件料")
@RequestMapping("/mesPmWorkOrderMaterialReP")
@Validated
public class MesPmWorkOrderMaterialRePController {

    @Resource
    private MesPmWorkOrderMaterialRePService mesPmWorkOrderMaterialRePService;

    @Resource
    private MesPmHtWorkOrderMaterialRePService mesPmHtWorkOrderMaterialRePService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmWorkOrderMaterialReP mesPmWorkOrderMaterialReP) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderMaterialRePService.save(mesPmWorkOrderMaterialReP));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderMaterialRePService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmWorkOrderMaterialReP.update.class) MesPmWorkOrderMaterialReP mesPmWorkOrderMaterialReP) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderMaterialRePService.update(mesPmWorkOrderMaterialReP));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmWorkOrderMaterialReP> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmWorkOrderMaterialReP  mesPmWorkOrderMaterialReP = mesPmWorkOrderMaterialRePService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmWorkOrderMaterialReP,StringUtils.isEmpty(mesPmWorkOrderMaterialReP)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmWorkOrderMaterialRePDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrderMaterialReP searchMesPmWorkOrderMaterialReP) {
        Page<Object> page = PageHelper.startPage(searchMesPmWorkOrderMaterialReP.getStartPage(),searchMesPmWorkOrderMaterialReP.getPageSize());
        List<MesPmWorkOrderMaterialRePDto> list = mesPmWorkOrderMaterialRePService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmWorkOrderMaterialReP));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesPmHtWorkOrderMaterialReP>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrderMaterialReP searchMesPmWorkOrderMaterialReP) {
        Page<Object> page = PageHelper.startPage(searchMesPmWorkOrderMaterialReP.getStartPage(),searchMesPmWorkOrderMaterialReP.getPageSize());
        List<MesPmHtWorkOrderMaterialReP> list = mesPmHtWorkOrderMaterialRePService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmWorkOrderMaterialReP));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesPmWorkOrderMaterialReP searchMesPmWorkOrderMaterialReP){
    List<MesPmWorkOrderMaterialRePDto> list = mesPmWorkOrderMaterialRePService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmWorkOrderMaterialReP));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesPmWorkOrderMaterialReP信息", MesPmWorkOrderMaterialRePDto.class, "MesPmWorkOrderMaterialReP.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
