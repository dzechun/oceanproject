package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.entity.basic.SmtRoute;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.entity.basic.history.SmtHtRoute;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@RestController
@Api(tags = "工艺路线信息")
@RequestMapping("/smtRoute")
@Validated
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
    public ResponseEntity configureRout(@RequestBody List<SmtRouteProcess> list){
        return ControllerUtil.returnCRUD(smtRouteProcessService.configureRout(list));
    }

    @GetMapping(value = "/findConfigureRout")
    @ApiOperation(value = "查询配置的工艺路线信息",notes = "查询配置的工艺路线信息")
    public ResponseEntity<List<SmtRouteProcess>> findConfigureRout(@ApiParam(value = "routeId",required = true) @RequestParam Long routeId){
        List<SmtRouteProcess> list = smtRouteProcessService.findConfigureRout(routeId);
        return ControllerUtil.returnDataSuccess("查询成功",list);
    }
}
