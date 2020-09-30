package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtProductProcessRouteService;
import com.fantechs.provider.imes.basic.service.SmtProductProcessRouteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/30.
 */
@RestController
@Api(tags = "产品工艺路线信息")
@RequestMapping("/smtProductProcessRoute")
public class SmtProductProcessRouteController {

    @Autowired
    private SmtProductProcessRouteService smtProductProcessRouteService;

    @Autowired
    private SmtHtProductProcessRouteService smtHtProductProcessRouteService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SmtProductProcessRoute smtProductProcessRoute) {
        return ControllerUtil.returnCRUD(smtProductProcessRouteService.save(smtProductProcessRoute));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtProductProcessRouteService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtProductProcessRoute smtProductProcessRoute) {
        if(StringUtils.isEmpty(smtProductProcessRoute.getProductProcessRouteId()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtProductProcessRouteService.update(smtProductProcessRoute));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtProductProcessRoute> detail(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtProductProcessRoute  smtProductProcessRoute = smtProductProcessRouteService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtProductProcessRoute,StringUtils.isEmpty(smtProductProcessRoute)?0:1);
    }

    @ApiOperation("产品工艺路线信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtProductProcessRoute>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProductProcessRoute searchSmtProductProcessRoute) {
        Page<Object> page = PageHelper.startPage(searchSmtProductProcessRoute.getStartPage(),searchSmtProductProcessRoute.getPageSize());
        List<SmtProductProcessRoute> list = smtProductProcessRouteService.findList(searchSmtProductProcessRoute);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("产品工艺路线信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtProductProcessRoute>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProductProcessRoute searchSmtProductProcessRoute) {
        Page<Object> page = PageHelper.startPage(searchSmtProductProcessRoute.getStartPage(),searchSmtProductProcessRoute.getPageSize());
        List<SmtProductProcessRoute> list = smtHtProductProcessRouteService.findList(searchSmtProductProcessRoute);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtProductProcessRoute searchSmtProductProcessRoute){
    List<SmtProductProcessRoute> list = smtProductProcessRouteService.findList(searchSmtProductProcessRoute);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出产品工艺路线信息", "产品工艺路线信息", SmtProductProcessRoute.class, "产品工艺路线信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
