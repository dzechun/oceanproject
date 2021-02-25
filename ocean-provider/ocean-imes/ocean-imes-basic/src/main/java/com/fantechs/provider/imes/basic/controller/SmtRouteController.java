package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtRoute;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.entity.basic.history.SmtHtRoute;
import com.fantechs.common.base.dto.basic.imports.SmtRouteImport;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtRouteService;
import com.fantechs.provider.imes.basic.service.SmtRouteProcessService;
import com.fantechs.provider.imes.basic.service.SmtRouteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@RestController
@Api(tags = "工艺路线信息管理")
@RequestMapping("/smtRoute")
@Validated
@Slf4j
public class SmtRouteController {

    @Autowired
    private SmtRouteService smtRouteService;
    @Autowired
    private SmtHtRouteService smtHtRouteService;
    @Autowired
    private SmtRouteProcessService smtRouteProcessService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：routeCode、routeName",required = true)@RequestBody @Validated SmtRoute smtRoute) {
        return ControllerUtil.returnCRUD(smtRouteService.save(smtRoute));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtRouteService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtRoute.update.class) SmtRoute smtRoute) {
        return ControllerUtil.returnCRUD(smtRouteService.update(smtRoute));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtRoute> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtRoute  smtRoute = smtRouteService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtRoute,StringUtils.isEmpty(smtRoute)?0:1);
    }

    @ApiOperation("工艺路线信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtRoute>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtRoute searchSmtRoute) {
        Page<Object> page = PageHelper.startPage(searchSmtRoute.getStartPage(),searchSmtRoute.getPageSize());
        List<SmtRoute> list = smtRouteService.findList(searchSmtRoute);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("工艺路线信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtRoute>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtRoute searchSmtRoute) {
        Page<Object> page = PageHelper.startPage(searchSmtRoute.getStartPage(),searchSmtRoute.getPageSize());
        List<SmtHtRoute> list = smtHtRouteService.findList(searchSmtRoute);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtRoute searchSmtRoute){
    List<SmtRoute> list = smtRouteService.findList(searchSmtRoute);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出工艺路线信息", "工艺路线信息", SmtRoute.class, "工艺路线信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/configureRout")
    @ApiOperation(value = "配置工艺路线",notes = "配置工艺路线")
    public ResponseEntity configureRout(@RequestBody SmtRoute smtRoute){
        return ControllerUtil.returnCRUD(smtRouteService.configureRout(smtRoute));
    }


    @GetMapping(value = "/findConfigureRout")
    @ApiOperation(value = "查询配置的工艺路线信息",notes = "查询配置的工艺路线信息")
    public ResponseEntity<List<SmtRouteProcess>> findConfigureRout(@ApiParam(value = "routeId",required = true) @RequestParam Long routeId){
        List<SmtRouteProcess> list = smtRouteProcessService.findConfigureRout(routeId);
        return ControllerUtil.returnDataSuccess("",list);
    }

    @PostMapping(value = "/addOrUpdateRoute")
    @ApiOperation(value = "新增或更新工艺路线",notes = "新增或更新工艺路线")
    public ResponseEntity addOrUpdateRoute(@ApiParam(value = "必传：routeId、routeCode、routeName",required = true)@RequestBody @Validated SmtRoute smtRoute){
        return ControllerUtil.returnCRUD(smtRouteService.addOrUpdateRoute(smtRoute));
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
            List<SmtRouteImport> smtRouteImports = EasyPoiUtils.importExcel(file, 2, 1, SmtRouteImport.class);
            Map<String, Object> resultMap = smtRouteService.importExcel(smtRouteImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
