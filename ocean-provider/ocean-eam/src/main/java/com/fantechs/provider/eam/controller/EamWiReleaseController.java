package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamHtWiReleaseDto;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDto;
import com.fantechs.common.base.general.entity.eam.EamWiRelease;
import com.fantechs.common.base.general.entity.eam.history.EamHtReturnOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamReturnOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiRelease;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtWiReleaseService;
import com.fantechs.provider.eam.service.EamWiReleaseService;
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
 * Created by leifengzhi on 2021/07/08.
 */
@RestController
@Api(tags = "WI发布管理")
@RequestMapping("/eamWiRelease")
@Validated
public class EamWiReleaseController {

    @Resource
    private EamWiReleaseService eamWiReleaseService;
    @Resource
    private EamHtWiReleaseService eamHtWiReleaseService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamWiReleaseDto eamWiReleaseDto) {
        return ControllerUtil.returnCRUD(eamWiReleaseService.save(eamWiReleaseDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamWiReleaseService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamWiRelease.update.class) EamWiReleaseDto eamWiReleaseDto) {
        return ControllerUtil.returnCRUD(eamWiReleaseService.update(eamWiReleaseDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamWiRelease> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamWiRelease  eamWiRelease = eamWiReleaseService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamWiRelease,StringUtils.isEmpty(eamWiRelease)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamWiReleaseDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamWiRelease searchEamWiRelease) {
        Page<Object> page = PageHelper.startPage(searchEamWiRelease.getStartPage(),searchEamWiRelease.getPageSize());
        List<EamWiReleaseDto> list = eamWiReleaseService.findList(searchEamWiRelease);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtWiReleaseDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamWiRelease searchEamWiRelease) {
        Page<Object> page = PageHelper.startPage(searchEamWiRelease.getStartPage(),searchEamWiRelease.getPageSize());
        List<EamHtWiReleaseDto> list = eamHtWiReleaseService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamWiRelease));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }



    @ApiOperation("审核")
    @PostMapping("/censor")
    public ResponseEntity censor(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamWiRelease.update.class) EamWiRelease eamWiRelease) {
        return ControllerUtil.returnCRUD(eamWiReleaseService.censor(eamWiRelease));
    }

}
