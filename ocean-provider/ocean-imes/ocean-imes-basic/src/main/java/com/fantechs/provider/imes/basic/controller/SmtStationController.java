package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.entity.basic.SmtStation;
import com.fantechs.common.base.entity.basic.history.SmtHtStation;
import com.fantechs.common.base.entity.basic.search.SearchSmtStation;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtStationService;
import com.fantechs.provider.imes.basic.service.SmtStationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/27.
 */
@RestController
@Api(tags = "工位信息管理")
@RequestMapping("/smtStation")
@Validated
public class SmtStationController {

    @Autowired
    private SmtStationService smtStationService;

    @Autowired
    private SmtHtStationService smtHtStationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：stationCode、stationName、processId、sectionId",required = true)@RequestBody @Validated SmtStation smtStation) {
        return ControllerUtil.returnCRUD(smtStationService.save(smtStation));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtStationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtStation.update.class) SmtStation smtStation) {
        return ControllerUtil.returnCRUD(smtStationService.update(smtStation));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtStation> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtStation  smtStation = smtStationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtStation,StringUtils.isEmpty(smtStation)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStation>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStation searchSmtStation) {
        Page<Object> page = PageHelper.startPage(searchSmtStation.getStartPage(),searchSmtStation.getPageSize());
        List<SmtStation> list = smtStationService.findList(searchSmtStation);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtStation>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStation searchSmtStation) {
        Page<Object> page = PageHelper.startPage(searchSmtStation.getStartPage(),searchSmtStation.getPageSize());
        List<SmtHtStation> list = smtHtStationService.findList(searchSmtStation);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtStation searchSmtStation){
    List<SmtStation> list = smtStationService.findList(searchSmtStation);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出工位信息", "工位信息", SmtStation.class, "工位信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
