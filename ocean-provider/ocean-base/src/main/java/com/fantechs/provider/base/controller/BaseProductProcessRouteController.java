package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductProcessRouteImport;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtProductProcessRouteService;
import com.fantechs.provider.base.service.BaseProductProcessRouteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
 * Created by wcz on 2020/09/30.
 */
@RestController
@Api(tags = "产品工艺路线信息管理")
@RequestMapping("/baseProductProcessRoute")
@Validated
@Slf4j
public class BaseProductProcessRouteController {

    @Resource
    private BaseProductProcessRouteService baseProductProcessRouteService;

    @Resource
    private BaseHtProductProcessRouteService baseHtProductProcessRouteService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：productType、productName、routeId",required = true)@RequestBody @Validated BaseProductProcessRoute baseProductProcessRoute) {
        return ControllerUtil.returnCRUD(baseProductProcessRouteService.save(baseProductProcessRoute));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductProcessRouteService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseProductProcessRoute.update.class) BaseProductProcessRoute baseProductProcessRoute) {
        return ControllerUtil.returnCRUD(baseProductProcessRouteService.update(baseProductProcessRoute));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductProcessRoute> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseProductProcessRoute baseProductProcessRoute = baseProductProcessRouteService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProductProcessRoute,StringUtils.isEmpty(baseProductProcessRoute)?0:1);
    }

    @ApiOperation("产品工艺路线信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductProcessRoute>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductProcessRoute searchBaseProductProcessRoute) {
        Page<Object> page = PageHelper.startPage(searchBaseProductProcessRoute.getStartPage(), searchBaseProductProcessRoute.getPageSize());
        List<BaseProductProcessRoute> list = baseProductProcessRouteService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessRoute));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("产品工艺路线信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductProcessRoute>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductProcessRoute searchBaseProductProcessRoute) {
        Page<Object> page = PageHelper.startPage(searchBaseProductProcessRoute.getStartPage(), searchBaseProductProcessRoute.getPageSize());
        List<BaseHtProductProcessRoute> list = baseHtProductProcessRouteService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessRoute));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchBaseProductProcessRoute searchBaseProductProcessRoute){
        List<BaseProductProcessRoute> list = baseProductProcessRouteService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessRoute));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "产品工艺路线信息", "产品工艺路线信息", "产品工艺路线信息.xls", response);

    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入部件组成信息",notes = "从excel导入部件组成信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseProductProcessRouteImport> baseProductProcessRouteImports = EasyPoiUtils.importExcel(file, 2, 1, BaseProductProcessRouteImport.class);
            Map<String, Object> resultMap = baseProductProcessRouteService.importExcel(baseProductProcessRouteImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }


    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/addOrUpdate")
    public ResponseEntity<BaseProductProcessRoute> addOrUpdate(@ApiParam(value = "必传：materialId、routeId",required = true)@RequestBody @Validated BaseProductProcessRoute baseProductProcessRoute) {
        BaseProductProcessRoute baseProductProcessRoutes = baseProductProcessRouteService.addOrUpdate(baseProductProcessRoute);
        return ControllerUtil.returnDataSuccess(baseProductProcessRoutes, StringUtils.isEmpty(baseProductProcessRoutes) ? 0 : 1);
    }

    @ApiOperation("产品工艺路线信息列表")
    @PostMapping("/findListByCondition")
    public ResponseEntity<List<BaseProductProcessRoute>> findListByCondition(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductProcessRoute searchBaseProductProcessRoute) {
        Page<Object> page = PageHelper.startPage(searchBaseProductProcessRoute.getStartPage(), searchBaseProductProcessRoute.getPageSize());
        List<BaseProductProcessRoute> list = baseProductProcessRouteService.findListByCondition(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessRoute));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

}
