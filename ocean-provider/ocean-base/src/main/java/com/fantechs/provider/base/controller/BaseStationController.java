package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseStationImport;
import com.fantechs.common.base.general.entity.basic.BaseStation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtStationService;
import com.fantechs.provider.base.service.BaseStationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * Created by wcz on 2020/09/27.
 */
@RestController
@Api(tags = "工位信息管理")
@RequestMapping("/baseStation")
@Validated
@Slf4j
public class BaseStationController {

    @Resource
    private BaseStationService baseStationService;

    @Resource
    private BaseHtStationService baseHtStationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：stationCode、stationName、processId、sectionId",required = true)@RequestBody @Validated BaseStation baseStation) {
        return ControllerUtil.returnCRUD(baseStationService.save(baseStation));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseStationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseStation.update.class) BaseStation baseStation) {
        return ControllerUtil.returnCRUD(baseStationService.update(baseStation));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseStation> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseStation baseStation = baseStationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseStation,StringUtils.isEmpty(baseStation)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStation>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStation searchBaseStation) {
        Page<Object> page = PageHelper.startPage(searchBaseStation.getStartPage(), searchBaseStation.getPageSize());
        List<BaseStation> list = baseStationService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStation));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtStation>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStation searchBaseStation) {
        Page<Object> page = PageHelper.startPage(searchBaseStation.getStartPage(), searchBaseStation.getPageSize());
        List<BaseHtStation> list = baseHtStationService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStation));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchBaseStation searchBaseStation){
    List<BaseStation> list = baseStationService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStation));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出工位信息", "工位信息", BaseStation.class, "工位信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入电子标签信息",notes = "从excel导入电子标签信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseStationImport> baseStationImports = EasyPoiUtils.importExcel(file, 2,1, BaseStationImport.class);
            Map<String, Object> resultMap = baseStationService.importExcel(baseStationImports);
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
}
