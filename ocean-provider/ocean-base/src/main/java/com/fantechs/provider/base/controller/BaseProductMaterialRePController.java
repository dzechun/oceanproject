package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductMaterialReP;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseProductMaterialRePService;
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
 *
 * Created by leifengzhi on 2021/04/28.
 */
@RestController
@Api(tags = "产品关键物料清单零件料")
@RequestMapping("/baseProductMaterialReP")
@Validated
public class BaseProductMaterialRePController {

    @Resource
    private BaseProductMaterialRePService baseProductMaterialRePService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseProductMaterialReP baseProductMaterialReP) {
        return ControllerUtil.returnCRUD(baseProductMaterialRePService.save(baseProductMaterialReP));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductMaterialRePService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseProductMaterialReP.update.class) BaseProductMaterialReP baseProductMaterialReP) {
        return ControllerUtil.returnCRUD(baseProductMaterialRePService.update(baseProductMaterialReP));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductMaterialReP> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseProductMaterialReP  baseProductMaterialReP = baseProductMaterialRePService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProductMaterialReP,StringUtils.isEmpty(baseProductMaterialReP)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductMaterialReP>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductMaterialReP searchBaseProductMaterialReP) {
        Page<Object> page = PageHelper.startPage(searchBaseProductMaterialReP.getStartPage(),searchBaseProductMaterialReP.getPageSize());
        List<BaseProductMaterialReP> list = baseProductMaterialRePService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductMaterialReP));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductMaterialReP>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductMaterialReP searchBaseProductMaterialReP) {
        Page<Object> page = PageHelper.startPage(searchBaseProductMaterialReP.getStartPage(),searchBaseProductMaterialReP.getPageSize());
        List<BaseHtProductMaterialReP> list = baseProductMaterialRePService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductMaterialReP));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProductMaterialReP searchBaseProductMaterialReP){
    List<BaseProductMaterialReP> list = baseProductMaterialRePService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductMaterialReP));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "产品关键物料清单零件料", BaseProductMaterialReP.class, "产品关键物料清单零件料.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
