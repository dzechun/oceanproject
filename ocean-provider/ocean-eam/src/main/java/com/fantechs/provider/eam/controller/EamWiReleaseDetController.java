package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamWiReleaseDetDto;
import com.fantechs.common.base.general.entity.eam.EamWiReleaseDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiReleaseDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamWiReleaseDetService;
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
@RequestMapping("/eamWiReleaseDet")
@Validated
public class EamWiReleaseDetController {

    @Resource
    private EamWiReleaseDetService eamWiReleaseDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamWiReleaseDet eamWiReleaseDet) {
        return ControllerUtil.returnCRUD(eamWiReleaseDetService.save(eamWiReleaseDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamWiReleaseDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= EamWiReleaseDet.update.class) EamWiReleaseDet eamWiReleaseDet) {
        return ControllerUtil.returnCRUD(eamWiReleaseDetService.update(eamWiReleaseDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamWiReleaseDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamWiReleaseDet  eamWiReleaseDet = eamWiReleaseDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamWiReleaseDet,StringUtils.isEmpty(eamWiReleaseDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamWiReleaseDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamWiReleaseDet searchEamWiReleaseDet) {
        Page<Object> page = PageHelper.startPage(searchEamWiReleaseDet.getStartPage(),searchEamWiReleaseDet.getPageSize());
        List<EamWiReleaseDetDto> list = eamWiReleaseDetService.findList(searchEamWiReleaseDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
