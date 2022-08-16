package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.history.BaseHtRoute;
import com.fantechs.common.base.general.dto.basic.imports.BaseRouteImport;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtRouteService;
import com.fantechs.provider.base.service.BaseRouteProcessService;
import com.fantechs.provider.base.service.BaseRouteService;
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
import java.util.NoSuchElementException;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@RestController
@Api(tags = "工艺路线信息管理")
@RequestMapping("/baseRoute")
@Validated
@Slf4j
public class BaseRouteController {

    @Resource
    private BaseRouteService baseRouteService;
    @Resource
    private BaseHtRouteService baseHtRouteService;
    @Resource
    private BaseRouteProcessService baseRouteProcessService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：routeCode、routeName",required = true)@RequestBody @Validated BaseRoute baseRoute) {
        return ControllerUtil.returnCRUD(baseRouteService.save(baseRoute));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseRouteService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseRoute.update.class) BaseRoute baseRoute) {
        return ControllerUtil.returnCRUD(baseRouteService.update(baseRoute));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseRoute> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseRoute baseRoute = baseRouteService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseRoute,StringUtils.isEmpty(baseRoute)?0:1);
    }

    @ApiOperation("工艺路线信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseRoute>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseRoute searchBaseRoute) {
        Page<Object> page = PageHelper.startPage(searchBaseRoute.getStartPage(), searchBaseRoute.getPageSize());
        List<BaseRoute> list = baseRouteService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseRoute));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("工艺路线信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtRoute>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseRoute searchBaseRoute) {
        Page<Object> page = PageHelper.startPage(searchBaseRoute.getStartPage(), searchBaseRoute.getPageSize());
        List<BaseHtRoute> list = baseHtRouteService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseRoute));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchBaseRoute searchBaseRoute){
    List<BaseRoute> list = baseRouteService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseRoute));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出工艺路线信息", "工艺路线信息", BaseRoute.class, "工艺路线信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/configureRout")
    @ApiOperation(value = "配置工艺路线",notes = "配置工艺路线")
    public ResponseEntity configureRout(@RequestBody BaseRoute baseRoute){
        return ControllerUtil.returnCRUD(baseRouteService.configureRout(baseRoute));
    }


    @GetMapping(value = "/findConfigureRout")
    @ApiOperation(value = "查询配置的工艺路线信息",notes = "查询配置的工艺路线信息")
    public ResponseEntity<List<BaseRouteProcess>> findConfigureRout(@ApiParam(value = "routeId",required = true) @RequestParam Long routeId){
        List<BaseRouteProcess> list = baseRouteProcessService.findConfigureRout(routeId);
        return ControllerUtil.returnDataSuccess("",list);
    }

    @PostMapping(value = "/addOrUpdateRoute")
    @ApiOperation(value = "新增或更新工艺路线",notes = "新增或更新工艺路线")
    public ResponseEntity addOrUpdateRoute(@ApiParam(value = "必传：routeId、routeCode、routeName",required = true)@RequestBody @Validated BaseRoute baseRoute){
        return ControllerUtil.returnCRUD(baseRouteService.addOrUpdateRoute(baseRoute));
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
            List<BaseRouteImport> baseRouteImports = EasyPoiUtils.importExcel(file, 2, 1, BaseRouteImport.class);
            Map<String, Object> resultMap = baseRouteService.importExcel(baseRouteImports);
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
    public ResponseEntity<BaseRoute> addOrUpdate(@ApiParam(value = "必传：routeCode、organizationId",required = true)@RequestBody @Validated BaseRoute baseRoute) {
        BaseRoute baseRoutes = baseRouteService.addOrUpdate(baseRoute);
        return ControllerUtil.returnDataSuccess(baseRoutes, StringUtils.isEmpty(baseRoutes) ? 0 : 1);
    }
}
