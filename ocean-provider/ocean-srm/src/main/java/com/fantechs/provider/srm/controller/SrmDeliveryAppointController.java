package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.general.dto.srm.SrmDeliveryAppointDto;
import com.fantechs.common.base.general.dto.srm.SrmHtDeliveryAppointDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryAppoint;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmDeliveryAppoint;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.srm.service.SrmDeliveryAppointService;
import com.fantechs.provider.srm.service.SrmHtDeliveryAppointService;
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
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */
@RestController
@Api(tags = "送货预约")
@RequestMapping("/srmDeliveryAppoint")
@Validated
public class SrmDeliveryAppointController {

    @Resource
    private SrmDeliveryAppointService srmDeliveryAppointService;
    @Resource
    private SrmHtDeliveryAppointService srmHtDeliveryAppointService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SrmDeliveryAppointDto srmDeliveryAppointDto) {
        return ControllerUtil.returnCRUD(srmDeliveryAppointService.save(srmDeliveryAppointDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmDeliveryAppointService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SrmDeliveryAppointDto srmDeliveryAppointDto) {
        return ControllerUtil.returnCRUD(srmDeliveryAppointService.update(srmDeliveryAppointDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmDeliveryAppoint> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmDeliveryAppoint  srmDeliveryAppoint = srmDeliveryAppointService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmDeliveryAppoint,StringUtils.isEmpty(srmDeliveryAppoint)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmDeliveryAppointDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmDeliveryAppoint searchSrmDeliveryAppoint) {
        Page<Object> page = PageHelper.startPage(searchSrmDeliveryAppoint.getStartPage(),searchSrmDeliveryAppoint.getPageSize());
        List<SrmDeliveryAppointDto> list = srmDeliveryAppointService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmDeliveryAppoint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmDeliveryAppointDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmDeliveryAppoint searchSrmDeliveryAppoint) {
        List<SrmDeliveryAppointDto> list = srmDeliveryAppointService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmDeliveryAppoint));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtDeliveryAppointDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmDeliveryAppoint searchSrmDeliveryAppoint) {
        Page<Object> page = PageHelper.startPage(searchSrmDeliveryAppoint.getStartPage(),searchSrmDeliveryAppoint.getPageSize());
        List<SrmHtDeliveryAppointDto> list = srmHtDeliveryAppointService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmDeliveryAppoint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmDeliveryAppoint searchSrmDeliveryAppoint){
    List<SrmDeliveryAppointDto> list = srmDeliveryAppointService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmDeliveryAppoint));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "SrmDeliveryAppoint信息", "SrmDeliveryAppoint.xls", response);
    }
}
