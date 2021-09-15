package com.fantechs.provider.tem.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.tem.history.TemHtVehicle;
import com.fantechs.common.base.general.entity.tem.search.SearchTemVehicle;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.tem.service.TemVehicleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/08.
 */
@RestController
@Api(tags = "temVehicle控制器")
@RequestMapping("/temVehicle")
@Validated
public class TemVehicleController {

    @Resource
    private TemVehicleService temVehicleService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated TemVehicle temVehicle) {
        return ControllerUtil.returnCRUD(temVehicleService.save(temVehicle));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(temVehicleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=TemVehicle.update.class) TemVehicle temVehicle) {
        return ControllerUtil.returnCRUD(temVehicleService.update(temVehicle));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<TemVehicle> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        TemVehicle  temVehicle = temVehicleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(temVehicle,StringUtils.isEmpty(temVehicle)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<TemVehicleDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchTemVehicle searchTemVehicle) {
        Page<Object> page = PageHelper.startPage(searchTemVehicle.getStartPage(),searchTemVehicle.getPageSize());
        List<TemVehicleDto> list = temVehicleService.findList(ControllerUtil.dynamicConditionByEntity(searchTemVehicle));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<TemHtVehicle>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchTemVehicle searchTemVehicle) {
        Page<Object> page = PageHelper.startPage(searchTemVehicle.getStartPage(),searchTemVehicle.getPageSize());
        List<TemHtVehicle> list = temVehicleService.findHtList(ControllerUtil.dynamicConditionByEntity(searchTemVehicle));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchTemVehicle searchTemVehicle){
    List<TemVehicleDto> list = temVehicleService.findList(ControllerUtil.dynamicConditionByEntity(searchTemVehicle));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "TemVehicle信息", TemVehicleDto.class, "TemVehicle.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
