package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.general.dto.eng.EngMaterialMaintainLogDto;
import com.fantechs.common.base.general.entity.eng.EngMaterialMaintainLog;
import com.fantechs.common.base.general.entity.eng.search.SearchEngMaterialMaintainLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.service.EngMaterialMaintainLogService;
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
 * Created by leifengzhi on 2021/11/04.
 */
@RestController
@Api(tags = "材料维护控制器")
@RequestMapping("/engMaterialMaintainLog")
@Validated
public class EngMaterialMaintainLogController {

    @Resource
    private EngMaterialMaintainLogService engMaterialMaintainLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngMaterialMaintainLog engMaterialMaintainLog) {
        return ControllerUtil.returnCRUD(engMaterialMaintainLogService.save(engMaterialMaintainLog));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(engMaterialMaintainLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EngMaterialMaintainLog.update.class) EngMaterialMaintainLog engMaterialMaintainLog) {
        return ControllerUtil.returnCRUD(engMaterialMaintainLogService.update(engMaterialMaintainLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EngMaterialMaintainLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EngMaterialMaintainLog  engMaterialMaintainLog = engMaterialMaintainLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(engMaterialMaintainLog,StringUtils.isEmpty(engMaterialMaintainLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngMaterialMaintainLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngMaterialMaintainLog searchEngMaterialMaintainLog) {
        Page<Object> page = PageHelper.startPage(searchEngMaterialMaintainLog.getStartPage(),searchEngMaterialMaintainLog.getPageSize());
        List<EngMaterialMaintainLogDto> list = engMaterialMaintainLogService.findList(ControllerUtil.dynamicConditionByEntity(searchEngMaterialMaintainLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<EngMaterialMaintainLogDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchEngMaterialMaintainLog searchEngMaterialMaintainLog) {
        List<EngMaterialMaintainLogDto> list = engMaterialMaintainLogService.findList(ControllerUtil.dynamicConditionByEntity(searchEngMaterialMaintainLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }


//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchEngMaterialMaintainLog searchEngMaterialMaintainLog){
//    List<EngMaterialMaintainLogDto> list = engMaterialMaintainLogService.findList(ControllerUtil.dynamicConditionByEntity(searchEngMaterialMaintainLog));
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "EngMaterialMaintainLog信息", EngMaterialMaintainLogDto.class, "EngMaterialMaintainLog.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }

}
