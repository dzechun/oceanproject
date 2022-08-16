package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAcReDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlanAcRe;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSamplePlanAcRe;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseSamplePlanAcReService;
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
@RequestMapping("/baseSamplePlanAcRe")
@Validated
public class BaseSamplePlanAcReController {

    @Resource
    private BaseSamplePlanAcReService baseSamplePlanAcReService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSamplePlanAcRe baseSamplePlanAcRe) {
        return ControllerUtil.returnCRUD(baseSamplePlanAcReService.save(baseSamplePlanAcRe));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSamplePlanAcReService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseSamplePlanAcRe.update.class) BaseSamplePlanAcRe baseSamplePlanAcRe) {
        return ControllerUtil.returnCRUD(baseSamplePlanAcReService.update(baseSamplePlanAcRe));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSamplePlanAcRe> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSamplePlanAcRe baseSamplePlanAcRe = baseSamplePlanAcReService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSamplePlanAcRe,StringUtils.isEmpty(baseSamplePlanAcRe)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSamplePlanAcReDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSamplePlanAcRe searchBaseSamplePlanAcRe) {
        Page<Object> page = PageHelper.startPage(searchBaseSamplePlanAcRe.getStartPage(), searchBaseSamplePlanAcRe.getPageSize());
        List<BaseSamplePlanAcReDto> list = baseSamplePlanAcReService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplePlanAcRe));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
