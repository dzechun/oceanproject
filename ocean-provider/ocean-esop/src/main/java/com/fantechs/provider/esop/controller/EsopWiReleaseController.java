package com.fantechs.provider.esop.controller;

import com.fantechs.common.base.general.dto.esop.EsopHtWiReleaseDto;
import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDto;
import com.fantechs.common.base.general.entity.esop.EsopWiRelease;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiRelease;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.esop.service.EsopHtWiReleaseService;
import com.fantechs.provider.esop.service.EsopWiReleaseService;
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
 * Created by leifengzhi on 2021/07/08.
 */
@RestController
@Api(tags = "WI发布管理")
@RequestMapping("/esopWiRelease")
@Validated
public class EsopWiReleaseController {

    @Resource
    private EsopWiReleaseService esopWiReleaseService;
    @Resource
    private EsopHtWiReleaseService esopHtWiReleaseService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EsopWiReleaseDto esopWiReleaseDto) {
        return ControllerUtil.returnCRUD(esopWiReleaseService.save(esopWiReleaseDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(esopWiReleaseService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EsopWiRelease.update.class) EsopWiReleaseDto esopWiReleaseDto) {
        return ControllerUtil.returnCRUD(esopWiReleaseService.update(esopWiReleaseDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EsopWiRelease> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EsopWiRelease  esopWiRelease = esopWiReleaseService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(esopWiRelease,StringUtils.isEmpty(esopWiRelease)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EsopWiReleaseDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEsopWiRelease searchEsopWiRelease) {
        Page<Object> page = PageHelper.startPage(searchEsopWiRelease.getStartPage(),searchEsopWiRelease.getPageSize());
        List<EsopWiReleaseDto> list = esopWiReleaseService.findList(searchEsopWiRelease);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EsopHtWiReleaseDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEsopWiRelease searchEsopWiRelease) {
        Page<Object> page = PageHelper.startPage(searchEsopWiRelease.getStartPage(),searchEsopWiRelease.getPageSize());
        List<EsopHtWiReleaseDto> list = esopHtWiReleaseService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEsopWiRelease));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stresop")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEsopWiRelease searchEsopWiRelease){
        List<EsopWiReleaseDto> list = esopWiReleaseService.findList(searchEsopWiRelease);
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "WI发布管理", "WI发布管理.xls", response);
    }

    @ApiOperation("审核")
    @PostMapping("/censor")
    public ResponseEntity censor(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EsopWiRelease.update.class) EsopWiRelease esopWiRelease) {
        return ControllerUtil.returnCRUD(esopWiReleaseService.censor(esopWiRelease));
    }

}
