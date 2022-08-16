package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderMaterialReP;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderBomService;
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
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "工单Bom")
@RequestMapping("/mesPmWorkOrderBom")
@Validated
public class MesPmWorkOrderBomController {

    @Resource
    private MesPmWorkOrderBomService mesPmWorkOrderBomService;


    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmWorkOrderBom mesPmWorkOrderBom) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderBomService.save(mesPmWorkOrderBom));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderBomService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmWorkOrderBom.update.class) MesPmWorkOrderBom mesPmWorkOrderBom) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderBomService.update(mesPmWorkOrderBom));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmWorkOrderBom> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmWorkOrderBom  mesPmWorkOrderBom = mesPmWorkOrderBomService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmWorkOrderBom,StringUtils.isEmpty(mesPmWorkOrderBom)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmWorkOrderBomDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrderBom searchMesPmWorkOrderBom) {
        Page<Object> page = PageHelper.startPage(searchMesPmWorkOrderBom.getStartPage(),searchMesPmWorkOrderBom.getPageSize());
        List<MesPmWorkOrderBomDto> list = mesPmWorkOrderBomService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmWorkOrderBom));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<MesPmWorkOrderBom> mesPmWorkOrderBoms) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderBomService.batchAdd(mesPmWorkOrderBoms));
    }

}
