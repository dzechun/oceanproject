package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductYieldDto;
import com.fantechs.common.base.general.entity.basic.BaseProductYield;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductYield;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductYield;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtProductYieldService;
import com.fantechs.provider.base.service.BaseProductYieldService;
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
 * Created by leifengzhi on 2021/10/20.
 */
@RestController
@Api(tags = "产品良率")
@RequestMapping("/baseProductYield")
@Validated
public class BaseProductYieldController {

    @Resource
    private BaseProductYieldService baseProductYieldService;
    @Resource
    private BaseHtProductYieldService baseHtProductYieldService;


    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseProductYieldDto baseProductYieldDto) {
        return ControllerUtil.returnCRUD(baseProductYieldService.save(baseProductYieldDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductYieldService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseProductYield.update.class) BaseProductYield baseProductYield) {
        return ControllerUtil.returnCRUD(baseProductYieldService.update(baseProductYield));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductYield> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseProductYield  baseProductYield = baseProductYieldService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProductYield,StringUtils.isEmpty(baseProductYield)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductYieldDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductYield searchBaseProductYield) {
        Page<Object> page = PageHelper.startPage(searchBaseProductYield.getStartPage(),searchBaseProductYield.getPageSize());
        List<BaseProductYieldDto> list = baseProductYieldService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductYield));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseProductYieldDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductYield searchBaseProductYield) {
        List<BaseProductYieldDto> list = baseProductYieldService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductYield));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductYield>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductYield searchBaseProductYield) {
        Page<Object> page = PageHelper.startPage(searchBaseProductYield.getStartPage(),searchBaseProductYield.getPageSize());
        List<BaseHtProductYield> list = baseHtProductYieldService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductYield));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProductYield searchBaseProductYield){
        List<BaseProductYieldDto> list = baseProductYieldService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductYield));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "BaseProductYield信息", BaseProductYieldDto.class, "BaseProductYield.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

}
