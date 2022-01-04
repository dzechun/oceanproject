package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductCarton;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcProductCartonService;
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
 * Created by leifengzhi on 2021/05/08.
 */
@RestController
@Api(tags = "生产管理-包箱管理控制器")
@RequestMapping("/mesSfcProductCarton")
@Validated
public class MesSfcProductCartonController {

    @Resource
    private MesSfcProductCartonService mesSfcProductCartonService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcProductCarton mesSfcProductCarton) {
        return ControllerUtil.returnCRUD(mesSfcProductCartonService.save(mesSfcProductCarton));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcProductCartonService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcProductCarton.update.class) MesSfcProductCarton mesSfcProductCarton) {
        return ControllerUtil.returnCRUD(mesSfcProductCartonService.update(mesSfcProductCarton));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcProductCarton> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcProductCarton  mesSfcProductCarton = mesSfcProductCartonService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcProductCarton,StringUtils.isEmpty(mesSfcProductCarton)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcProductCartonDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcProductCarton searchMesSfcProductCarton) {
        Page<Object> page = PageHelper.startPage(searchMesSfcProductCarton.getStartPage(),searchMesSfcProductCarton.getPageSize());
        List<MesSfcProductCartonDto> list = mesSfcProductCartonService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcProductCarton));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcProductCarton searchMesSfcProductCarton){
    List<MesSfcProductCartonDto> list = mesSfcProductCartonService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcProductCarton));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcProductCarton信息", MesSfcProductCartonDto.class, "MesSfcProductCarton.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
