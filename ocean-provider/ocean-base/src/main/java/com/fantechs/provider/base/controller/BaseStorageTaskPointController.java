package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageTaskPointImport;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageTaskPoint;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseStorageTaskPointService;
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
 * Created by leifengzhi on 2021/09/09.
 */
@RestController
@Api(tags = "配送点信息")
@RequestMapping("/baseStorageTaskPoint")
@Validated
@Slf4j
public class BaseStorageTaskPointController {

    @Resource
    private BaseStorageTaskPointService baseStorageTaskPointService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseStorageTaskPoint baseStorageTaskPoint) {
        return ControllerUtil.returnCRUD(baseStorageTaskPointService.save(baseStorageTaskPoint));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseStorageTaskPointService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseStorageTaskPoint.update.class) BaseStorageTaskPoint baseStorageTaskPoint) {
        return ControllerUtil.returnCRUD(baseStorageTaskPointService.update(baseStorageTaskPoint));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseStorageTaskPoint> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseStorageTaskPoint  baseStorageTaskPoint = baseStorageTaskPointService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseStorageTaskPoint,StringUtils.isEmpty(baseStorageTaskPoint)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStorageTaskPoint>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStorageTaskPoint searchBaseStorageTaskPoint) {
        Page<Object> page = PageHelper.startPage(searchBaseStorageTaskPoint.getStartPage(),searchBaseStorageTaskPoint.getPageSize());
        List<BaseStorageTaskPoint> list = baseStorageTaskPointService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageTaskPoint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtStorageTaskPoint>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStorageTaskPoint searchBaseStorageTaskPoint) {
        Page<Object> page = PageHelper.startPage(searchBaseStorageTaskPoint.getStartPage(),searchBaseStorageTaskPoint.getPageSize());
        List<BaseHtStorageTaskPoint> list = baseStorageTaskPointService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageTaskPoint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseStorageTaskPoint searchBaseStorageTaskPoint){
    List<BaseStorageTaskPoint> list = baseStorageTaskPointService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageTaskPoint));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "配送点信息", BaseStorageTaskPoint.class, "配送点信息.xls", response);
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
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseStorageTaskPointImport> baseStorageTaskPointImports = EasyPoiUtils.importExcel(file, 2, 1, BaseStorageTaskPointImport.class);
            Map<String, Object> resultMap = baseStorageTaskPointService.importExcel(baseStorageTaskPointImports);
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
