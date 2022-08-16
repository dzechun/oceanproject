package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionExemptedListImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionItemImport;
import com.fantechs.common.base.general.entity.basic.BaseInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionExemptedList;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtInspectionExemptedListService;
import com.fantechs.provider.base.service.BaseInspectionExemptedListService;
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
 * Created by leifengzhi on 2021/05/21.
 */
@RestController
@Api(tags = "免检清单")
@RequestMapping("/baseInspectionExemptedList")
@Validated
@Slf4j
public class BaseInspectionExemptedListController {

    @Resource
    private BaseInspectionExemptedListService baseInspectionExemptedListService;
    @Resource
    private BaseHtInspectionExemptedListService baseHtInspectionExemptedListService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseInspectionExemptedList baseInspectionExemptedList) {
        return ControllerUtil.returnCRUD(baseInspectionExemptedListService.save(baseInspectionExemptedList));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseInspectionExemptedListService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseInspectionExemptedList.update.class) BaseInspectionExemptedList baseInspectionExemptedList) {
        return ControllerUtil.returnCRUD(baseInspectionExemptedListService.update(baseInspectionExemptedList));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInspectionExemptedList> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseInspectionExemptedList  baseInspectionExemptedList = baseInspectionExemptedListService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseInspectionExemptedList,StringUtils.isEmpty(baseInspectionExemptedList)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInspectionExemptedList>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionExemptedList searchBaseInspectionExemptedList) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionExemptedList.getStartPage(),searchBaseInspectionExemptedList.getPageSize());
        List<BaseInspectionExemptedList> list = baseInspectionExemptedListService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionExemptedList));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInspectionExemptedList>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionExemptedList searchBaseInspectionExemptedList) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionExemptedList.getStartPage(),searchBaseInspectionExemptedList.getPageSize());
        List<BaseHtInspectionExemptedList> list = baseHtInspectionExemptedListService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionExemptedList));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseInspectionExemptedList searchBaseInspectionExemptedList){
    List<BaseInspectionExemptedList> list = baseInspectionExemptedListService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionExemptedList));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "免检清单", BaseInspectionExemptedList.class, "免检清单.xls", response);
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
            List<BaseInspectionExemptedListImport> baseInspectionExemptedListImports = EasyPoiUtils.importExcel(file, 2, 1, BaseInspectionExemptedListImport.class);
            Map<String, Object> resultMap = baseInspectionExemptedListService.importExcel(baseInspectionExemptedListImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
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
