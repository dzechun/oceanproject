package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanAcReDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlanAcRe;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSamplingPlanAcRe;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseSamplingPlanAcReService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@RestController
@Api(tags = "抽样方案AcRe")
@RequestMapping("/baseSamplingPlanAcRe")
@Validated
public class BaseSamplingPlanAcReController {

    @Resource
    private BaseSamplingPlanAcReService baseSamplingPlanAcReService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSamplingPlanAcRe baseSamplingPlanAcRe) {
        return ControllerUtil.returnCRUD(baseSamplingPlanAcReService.save(baseSamplingPlanAcRe));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSamplingPlanAcReService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseSamplingPlanAcRe.update.class) BaseSamplingPlanAcRe baseSamplingPlanAcRe) {
        return ControllerUtil.returnCRUD(baseSamplingPlanAcReService.update(baseSamplingPlanAcRe));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSamplingPlanAcRe> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSamplingPlanAcRe  baseSamplingPlanAcRe = baseSamplingPlanAcReService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSamplingPlanAcRe,StringUtils.isEmpty(baseSamplingPlanAcRe)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSamplingPlanAcReDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSamplingPlanAcRe searchBaseSamplingPlanAcRe) {
        Page<Object> page = PageHelper.startPage(searchBaseSamplingPlanAcRe.getStartPage(),searchBaseSamplingPlanAcRe.getPageSize());
        List<BaseSamplingPlanAcReDto> list = baseSamplingPlanAcReService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplingPlanAcRe));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
