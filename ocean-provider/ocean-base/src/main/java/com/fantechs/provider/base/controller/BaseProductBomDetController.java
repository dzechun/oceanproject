package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseProductBomDetDto;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtProductBomDetService;
import com.fantechs.provider.base.service.BaseProductBomDetService;
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
 * Created by wcz on 2020/10/12.
 */
@RestController
@Api(tags = "产品BOM详细信息")
@RequestMapping("/baseProductBomDet")
@Validated
public class BaseProductBomDetController {

    @Resource
    private BaseProductBomDetService baseProductBomDetService;
    @Resource
    private BaseHtProductBomDetService baseHtProductBomDetService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：partMaterialId、processId", required = true) @RequestBody @Validated BaseProductBomDet baseProductBomDet) {
        return ControllerUtil.returnCRUD(baseProductBomDetService.save(baseProductBomDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        if (StringUtils.isEmpty(ids)) {
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(baseProductBomDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseProductBomDet.update.class) BaseProductBomDet baseProductBomDet) {
        if (StringUtils.isEmpty(baseProductBomDet.getProductBomDetId())) {
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(baseProductBomDetService.update(baseProductBomDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductBomDet> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        if (StringUtils.isEmpty(id)) {
            return ControllerUtil.returnFailByParameError();
        }
        BaseProductBomDet baseProductBomDet = baseProductBomDetService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(baseProductBomDet, StringUtils.isEmpty(baseProductBomDet) ? 0 : 1);
    }

    @ApiOperation("产品BOM详细信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductBomDet>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductBomDet searchBaseProductBomDet) {
        Page<Object> page = PageHelper.startPage(searchBaseProductBomDet.getStartPage(), searchBaseProductBomDet.getPageSize());
        List<BaseProductBomDet> list = baseProductBomDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductBomDet));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("产品BOM详细信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductBomDet>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductBomDet searchBaseProductBomDet) {
        Page<Object> page = PageHelper.startPage(searchBaseProductBomDet.getStartPage(), searchBaseProductBomDet.getPageSize());
        List<BaseHtProductBomDet> list = baseHtProductBomDetService.findList(searchBaseProductBomDet);
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProductBomDet searchBaseProductBomDet) {
        List<BaseProductBomDet> list = baseProductBomDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductBomDet));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出产品BOM祥细信息", "产品BOM祥细信息", BaseProductBomDet.class, "产品BOM祥细信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("查询下级明细")
    @PostMapping("/findNextLevelProductBomDet")
    public ResponseEntity<List<BaseProductBomDetDto>> findNextLevelProductBomDet(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductBomDet searchBaseProductBomDet) {
        Page<Object> page = PageHelper.startPage(searchBaseProductBomDet.getStartPage(), searchBaseProductBomDet.getPageSize());
        List<BaseProductBomDetDto> list = baseProductBomDetService.findNextLevelProductBomDet(searchBaseProductBomDet);
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/addOrUpdate")
    public ResponseEntity addOrUpdate(@ApiParam(value = "必传：productBomCode、materialId",required = true)@RequestBody @Validated List<BaseProductBomDet> bseProductBomDets) {
        int i = baseProductBomDetService.addOrUpdate(bseProductBomDets);
        return ControllerUtil.returnCRUD(i);
    }

    @ApiOperation("接口批量删除")
    @PostMapping("/batchApiDelete")
    public ResponseEntity batchApiDelete(@ApiParam(value = "抽样过程id",required = true) @RequestParam @NotNull(message="productBomId不能为空") Long productBomId) {
        return ControllerUtil.returnCRUD(baseProductBomDetService.batchApiDelete(productBomId));
    }
}
