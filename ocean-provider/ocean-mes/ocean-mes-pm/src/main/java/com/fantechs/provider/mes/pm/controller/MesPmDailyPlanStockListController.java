package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanStockListDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanStockList;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlanStockList;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanStockListService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@RestController
@Api(tags = "日计划物料明细控制器")
@RequestMapping("/mesPmDailyPlanStockList")
@Validated
public class MesPmDailyPlanStockListController {

    @Resource
    private MesPmDailyPlanStockListService mesPmDailyPlanStockListService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmDailyPlanStockList mesPmDailyPlanStockList) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanStockListService.save(mesPmDailyPlanStockList));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanStockListService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmDailyPlanStockList.update.class) MesPmDailyPlanStockList mesPmDailyPlanStockList) {
        return ControllerUtil.returnCRUD(mesPmDailyPlanStockListService.update(mesPmDailyPlanStockList));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmDailyPlanStockList> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmDailyPlanStockList  mesPmDailyPlanStockList = mesPmDailyPlanStockListService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmDailyPlanStockList,StringUtils.isEmpty(mesPmDailyPlanStockList)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmDailyPlanStockListDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmDailyPlanStockList searchMesPmDailyPlanStockList) {
        Page<Object> page = PageHelper.startPage(searchMesPmDailyPlanStockList.getStartPage(),searchMesPmDailyPlanStockList.getPageSize());
        List<MesPmDailyPlanStockListDto> list = mesPmDailyPlanStockListService.findList(searchMesPmDailyPlanStockList);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<MesPmDailyPlanStockListDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchMesPmDailyPlanStockList searchMesPmDailyPlanStockList) {
        List<MesPmDailyPlanStockListDto> list = mesPmDailyPlanStockListService.findList(searchMesPmDailyPlanStockList);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

}
