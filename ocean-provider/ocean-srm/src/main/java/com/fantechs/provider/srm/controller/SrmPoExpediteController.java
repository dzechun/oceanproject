package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.general.dto.srm.SrmPoExpediteDto;
import com.fantechs.common.base.general.entity.srm.SrmPoExpedite;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPoExpedite;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPoExpedite;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.srm.service.SrmHtPoExpediteService;
import com.fantechs.provider.srm.service.SrmPoExpediteService;
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
 * Created by leifengzhi on 2021/11/18.
 */
@RestController
@Api(tags = "订单跟催记录控制器")
@RequestMapping("/srmPoExpedite")
@Validated
public class SrmPoExpediteController {

    @Resource
    private SrmPoExpediteService srmPoExpediteService;
    @Resource
    private SrmHtPoExpediteService srmHtPoExpediteService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmPoExpedite srmPoExpedite) {
        return ControllerUtil.returnCRUD(srmPoExpediteService.save(srmPoExpedite));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmPoExpediteService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmPoExpedite.update.class) SrmPoExpedite srmPoExpedite) {
        return ControllerUtil.returnCRUD(srmPoExpediteService.update(srmPoExpedite));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmPoExpedite> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmPoExpedite  srmPoExpedite = srmPoExpediteService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmPoExpedite,StringUtils.isEmpty(srmPoExpedite)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmPoExpediteDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPoExpedite searchSrmPoExpedite) {
        Page<Object> page = PageHelper.startPage(searchSrmPoExpedite.getStartPage(),searchSrmPoExpedite.getPageSize());
        List<SrmPoExpediteDto> list = srmPoExpediteService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoExpedite));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmPoExpediteDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmPoExpedite searchSrmPoExpedite) {
        List<SrmPoExpediteDto> list = srmPoExpediteService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoExpedite));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtPoExpedite>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPoExpedite searchSrmPoExpedite) {
        Page<Object> page = PageHelper.startPage(searchSrmPoExpedite.getStartPage(),searchSrmPoExpedite.getPageSize());
        List<SrmHtPoExpedite> list = srmHtPoExpediteService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoExpedite));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmPoExpedite searchSrmPoExpedite){
    List<SrmPoExpediteDto> list = srmPoExpediteService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoExpedite));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "SrmPoExpedite信息", "SrmPoExpedite.xls", response);
    }
//
//    @PostMapping(value = "/import")
//    @ApiOperation(value = "从excel导入",notes = "从excel导入")
//    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
//        try {
//            // 导入操作
//            List<SrmPoExpedite> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, SrmPoExpedite.class);
//            Map<String, Object> resultMap = srmPoExpediteService.importExcel(baseAddressImports);
//            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error(e.getMessage());
//            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
//        }
//    }
}
