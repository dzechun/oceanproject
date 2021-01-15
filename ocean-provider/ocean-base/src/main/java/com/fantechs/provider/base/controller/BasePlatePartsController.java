package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlateParts;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtPlatePartsService;
import com.fantechs.provider.base.service.BasePlatePartsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@RestController
@Api(tags = "部件组成")
@RequestMapping("/basePlateParts")
@Validated
public class BasePlatePartsController {

    @Autowired
    private BasePlatePartsService basePlatePartsService;
    @Resource
    private BaseHtPlatePartsService baseHtPlatePartsService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BasePlateParts basePlateParts) {
        return ControllerUtil.returnCRUD(basePlatePartsService.save(basePlateParts));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(basePlatePartsService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BasePlateParts.update.class) BasePlateParts basePlateParts) {
        return ControllerUtil.returnCRUD(basePlatePartsService.update(basePlateParts));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BasePlateParts> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BasePlateParts  basePlateParts = basePlatePartsService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(basePlateParts,StringUtils.isEmpty(basePlateParts)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BasePlatePartsDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlateParts searchBasePlateParts) {
        Page<Object> page = PageHelper.startPage(searchBasePlateParts.getStartPage(),searchBasePlateParts.getPageSize());
        List<BasePlatePartsDto> list =  basePlatePartsService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtPlateParts>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlateParts searchBasePlateParts) {
        Page<Object> page = PageHelper.startPage(searchBasePlateParts.getStartPage(),searchBasePlateParts.getPageSize());
        List<BaseHtPlateParts> list = baseHtPlatePartsService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBasePlateParts searchBasePlateParts){
    List<BasePlatePartsDto> list = basePlatePartsService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "部件组成导出信息", "部件组成信息", BasePlatePartsDto.class, "部件组成.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
