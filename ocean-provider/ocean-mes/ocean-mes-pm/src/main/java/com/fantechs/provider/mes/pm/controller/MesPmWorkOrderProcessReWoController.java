package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderProcessReWo;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmHtWorkOrderProcessReWoService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderProcessReWoService;
import com.fantechs.provider.mes.pm.vo.MesPmHtWorkOrderProcessReWoVo;
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
 * Created by leifengzhi on 2021/04/28.
 */
@RestController
@Api(tags = "关键物料工单")
@RequestMapping("/mesPmWorkOrderProcessReWo")
@Validated
public class MesPmWorkOrderProcessReWoController {

    @Resource
    private MesPmWorkOrderProcessReWoService mesPmWorkOrderProcessReWoService;

    @Resource
    private MesPmHtWorkOrderProcessReWoService mesPmHtWorkOrderProcessReWoService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesPmWorkOrderProcessReWo mesPmWorkOrderProcessReWo) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderProcessReWoService.save(mesPmWorkOrderProcessReWo));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@RequestBody List<MesPmWorkOrderProcessReWo> list) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderProcessReWoService.batchSave(list));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderProcessReWoService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmWorkOrderProcessReWo.update.class) MesPmWorkOrderProcessReWo mesPmWorkOrderProcessReWo) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderProcessReWoService.update(mesPmWorkOrderProcessReWo));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmWorkOrderProcessReWo> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmWorkOrderProcessReWo  mesPmWorkOrderProcessReWo = mesPmWorkOrderProcessReWoService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmWorkOrderProcessReWo,StringUtils.isEmpty(mesPmWorkOrderProcessReWo)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmWorkOrderProcessReWoDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo) {
        Page<Object> page = PageHelper.startPage(searchMesPmWorkOrderProcessReWo.getStartPage(),searchMesPmWorkOrderProcessReWo.getPageSize());
        List<MesPmWorkOrderProcessReWoDto> list = mesPmWorkOrderProcessReWoService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmWorkOrderProcessReWo));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesPmHtWorkOrderProcessReWoVo>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo) {
        Page<Object> page = PageHelper.startPage(searchMesPmWorkOrderProcessReWo.getStartPage(),searchMesPmWorkOrderProcessReWo.getPageSize());
        List<MesPmHtWorkOrderProcessReWoVo> list = mesPmHtWorkOrderProcessReWoService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmWorkOrderProcessReWo));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchMesPmWorkOrderProcessReWo searchMesPmWorkOrderProcessReWo){
//    List<MesPmWorkOrderProcessReWoDto> list = mesPmWorkOrderProcessReWoService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmWorkOrderProcessReWo));
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "MesPmWorkOrderProcessReWo信息", MesPmWorkOrderProcessReWoDto.class, "MesPmWorkOrderProcessReWo.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
}
