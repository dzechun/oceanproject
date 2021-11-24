package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.general.dto.srm.SrmCarportTimeQuantumDto;
import com.fantechs.common.base.general.entity.srm.SrmCarportTimeQuantum;
import com.fantechs.common.base.general.entity.srm.history.SrmHtCarportTimeQuantum;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmCarportTimeQuantum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmCarportTimeQuantumService;
import com.fantechs.provider.srm.service.SrmHtCarportTimeQuantumService;
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
 * Created by leifengzhi on 2021/11/23.
 */
@RestController
@Api(tags = "车位信息时间表")
@RequestMapping("/srmCarportTimeQuantum")
@Validated
public class SrmCarportTimeQuantumController {

    @Resource
    private SrmCarportTimeQuantumService srmCarportTimeQuantumService;
    @Resource
    private SrmHtCarportTimeQuantumService srmHtCarportTimeQuantumService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmCarportTimeQuantum srmCarportTimeQuantum) {
        return ControllerUtil.returnCRUD(srmCarportTimeQuantumService.save(srmCarportTimeQuantum));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmCarportTimeQuantumService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmCarportTimeQuantum.update.class) SrmCarportTimeQuantum srmCarportTimeQuantum) {
        return ControllerUtil.returnCRUD(srmCarportTimeQuantumService.update(srmCarportTimeQuantum));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmCarportTimeQuantum> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmCarportTimeQuantum  srmCarportTimeQuantum = srmCarportTimeQuantumService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmCarportTimeQuantum,StringUtils.isEmpty(srmCarportTimeQuantum)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmCarportTimeQuantumDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmCarportTimeQuantum searchSrmCarportTimeQuantum) {
        Page<Object> page = PageHelper.startPage(searchSrmCarportTimeQuantum.getStartPage(),searchSrmCarportTimeQuantum.getPageSize());
        List<SrmCarportTimeQuantumDto> list = srmCarportTimeQuantumService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmCarportTimeQuantum));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmCarportTimeQuantumDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmCarportTimeQuantum searchSrmCarportTimeQuantum) {
        List<SrmCarportTimeQuantumDto> list = srmCarportTimeQuantumService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmCarportTimeQuantum));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtCarportTimeQuantum>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmCarportTimeQuantum searchSrmCarportTimeQuantum) {
        Page<Object> page = PageHelper.startPage(searchSrmCarportTimeQuantum.getStartPage(),searchSrmCarportTimeQuantum.getPageSize());
        List<SrmHtCarportTimeQuantum> list = srmHtCarportTimeQuantumService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmCarportTimeQuantum));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
