package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.srm.SrmPoProductionInfoDto;
import com.fantechs.common.base.general.entity.srm.SrmPoProductionInfo;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPoProductionInfo;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPoProductionInfo;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.srm.service.SrmHtPoProductionInfoService;
import com.fantechs.provider.srm.service.SrmPoProductionInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/17.
 */
@RestController
@Api(tags = "订单生产状态控制器")
@RequestMapping("/srmPoProductionInfo")
@Validated
@Log4j
public class SrmPoProductionInfoController {

    @Resource
    private SrmPoProductionInfoService srmPoProductionInfoService;
    @Resource
    private SrmHtPoProductionInfoService srmHtPoProductionInfoService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmPoProductionInfo srmPoProductionInfo) {
        return ControllerUtil.returnCRUD(srmPoProductionInfoService.save(srmPoProductionInfo));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmPoProductionInfoService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmPoProductionInfo.update.class) SrmPoProductionInfo srmPoProductionInfo) {
        return ControllerUtil.returnCRUD(srmPoProductionInfoService.update(srmPoProductionInfo));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmPoProductionInfo> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmPoProductionInfo  srmPoProductionInfo = srmPoProductionInfoService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmPoProductionInfo,StringUtils.isEmpty(srmPoProductionInfo)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmPoProductionInfoDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPoProductionInfo searchSrmPoProductionInfo) {
        Page<Object> page = PageHelper.startPage(searchSrmPoProductionInfo.getStartPage(),searchSrmPoProductionInfo.getPageSize());
        List<SrmPoProductionInfoDto> list = srmPoProductionInfoService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoProductionInfo));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmPoProductionInfoDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmPoProductionInfo searchSrmPoProductionInfo) {
        List<SrmPoProductionInfoDto> list = srmPoProductionInfoService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoProductionInfo));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtPoProductionInfo>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPoProductionInfo searchSrmPoProductionInfo) {
        Page<Object> page = PageHelper.startPage(searchSrmPoProductionInfo.getStartPage(),searchSrmPoProductionInfo.getPageSize());
        List<SrmHtPoProductionInfo> list = srmHtPoProductionInfoService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoProductionInfo));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmPoProductionInfo searchSrmPoProductionInfo){
    List<SrmPoProductionInfoDto> list = srmPoProductionInfoService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoProductionInfo));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出订单生产状态信息", "订单生产状态信息", "SrmPoProductionInfo.xls", response);
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<SrmPoProductionInfoDto> baseAddressImports = EasyPoiUtils.importExcel(file, 2, 1, SrmPoProductionInfoDto.class);
            Map<String, Object> resultMap = srmPoProductionInfoService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
