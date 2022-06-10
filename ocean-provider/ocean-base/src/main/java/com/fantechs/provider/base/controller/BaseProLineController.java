package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseProLineImport;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtProLineService;
import com.fantechs.provider.base.service.BaseProLineService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wcz
 * @Date: 2020/9/1 16:30
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/baseProLine")
@Api(tags = "产线信息管理")
@Slf4j
@Validated
public class BaseProLineController {
    @Resource
    private BaseProLineService baseProLineService;

    @Resource
    private BaseHtProLineService baseHtProLineService;

    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation("根据条件查询生产线信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProLine>> selectProLines(@RequestBody(required = false) SearchBaseProLine searchBaseProLine){
        Page<Object> page = PageHelper.startPage(searchBaseProLine.getStartPage(), searchBaseProLine.getPageSize());
        List<BaseProLine> baseProLines = baseProLineService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProLine));
        return ControllerUtil.returnDataSuccess(baseProLines,(int)page.getTotal());
    }

    @ApiOperation("生产线信息列表")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseProLine>> findAll(){
        List<BaseProLine> baseProLines = baseProLineService.findList(new HashMap<>());
        return ControllerUtil.returnDataSuccess(baseProLines, baseProLines.size());
    }

    @ApiOperation("增加生产线信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：proCode、proName、factoryId、workShopId",required = true)@RequestBody @Validated BaseProLine baseProLine){
        return ControllerUtil.returnCRUD(baseProLineService.save(baseProLine));
    }

    @ApiOperation("修改生产线信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "生产线信息对象，生产线信息Id必传",required = true)@RequestBody @Validated(value = BaseProLine.update.class) BaseProLine baseProLine){
        return ControllerUtil.returnCRUD(baseProLineService.update(baseProLine));
    }

    @ApiOperation("删除生产线信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "生产线对象ID",required = true)@RequestParam @NotBlank(message = "ids不能为空") String ids){
        return ControllerUtil.returnCRUD(baseProLineService.batchDelete(ids));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProLine> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseProLine baseProLine = baseProLineService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProLine,StringUtils.isEmpty(baseProLine)?0:1);
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出生产线信息excel",notes = "导出生产线信息excel",produces = "application/octet-stream")
    public void exportProLines(HttpServletResponse response, @ApiParam(value = "查询对象")
                               @RequestBody(required = false) SearchBaseProLine searchBaseProLine){
        List<BaseProLine> list = baseProLineService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProLine));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "生产线信息导出", "生产线信息", "生产线信息.xls", response);

    }

    @PostMapping("/findHtList")
    @ApiOperation(value = "根据条件查询生产线履历信息",notes = "根据条件查询生产线履历信息")
    public ResponseEntity<List<BaseHtProLine>> selectHtProLines(@ApiParam(value = "查询对象")@RequestBody SearchBaseProLine searchBaseProLine) {
        Page<Object> page = PageHelper.startPage(searchBaseProLine.getStartPage(), searchBaseProLine.getPageSize());
        List<BaseHtProLine> smtHtDepts= baseHtProLineService.selectHtProLines(ControllerUtil.dynamicConditionByEntity(searchBaseProLine));
        return  ControllerUtil.returnDataSuccess(smtHtDepts, (int)page.getTotal());
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
            List<BaseProLineImport> baseProLineImports = EasyPoiUtils.importExcel(file, 2, 1, BaseProLineImport.class);
            Map<String, Object> resultMap = baseProLineService.importExcel(baseProLineImports);
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
    public ResponseEntity<BaseProLine> addOrUpdate(@ApiParam(value = "必传：productBomCode、materialId",required = true)@RequestBody @Validated BaseProLine baseProLine) {
        BaseProLine baseProLines = baseProLineService.addOrUpdate(baseProLine);
        return ControllerUtil.returnDataSuccess(baseProLines, StringUtils.isEmpty(baseProLines) ? 0 : 1);
    }

    @ApiOperation("批量添加")
    @PostMapping("/batchAdd")
    public ResponseEntity<List<BaseProLine>> batchAdd(@ApiParam(value = "",required = true)@RequestBody @Validated List<BaseProLine> baseProLines){
        List<BaseProLine> list = baseProLineService.batchAdd(baseProLines);
        return  ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }
}
