package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BasePartsInformationDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePartsInformationImport;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPartsInformation;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePartsInformation;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtPartsInformationService;
import com.fantechs.provider.base.service.BasePartsInformationService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/14.
 */
@RestController
@Api(tags = "部件信息管理")
@RequestMapping("/basePartsInformation")
@Validated
@Slf4j
public class BasePartsInformationController {

    @Autowired
    private BasePartsInformationService basePartsInformationService;
    @Resource
    private BaseHtPartsInformationService baseHtPartsInformationService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated BasePartsInformation basePartsInformation) {
        return ControllerUtil.returnCRUD(basePartsInformationService.save(basePartsInformation));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(basePartsInformationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BasePartsInformation.update.class) BasePartsInformation basePartsInformation) {
        return ControllerUtil.returnCRUD(basePartsInformationService.update(basePartsInformation));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BasePartsInformation> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        BasePartsInformation basePartsInformation = basePartsInformationService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(basePartsInformation, StringUtils.isEmpty(basePartsInformation) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BasePartsInformationDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBasePartsInformation searchBasePartsInformation) {
        Page<Object> page = PageHelper.startPage(searchBasePartsInformation.getStartPage(), searchBasePartsInformation.getPageSize());
        List<BasePartsInformationDto> list = basePartsInformationService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePartsInformation));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtPartsInformation>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchBasePartsInformation searchBasePartsInformation) {
        Page<Object> page = PageHelper.startPage(searchBasePartsInformation.getStartPage(), searchBasePartsInformation.getPageSize());
        List<BaseHtPartsInformation> list = baseHtPartsInformationService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBasePartsInformation));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBasePartsInformation searchBasePartsInformation) {
        List<BasePartsInformationDto> list = basePartsInformationService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePartsInformation));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出部件信息", "部件信息", "部件信息.xls", response);

    }

    /**
     * 从excel导入数据
     *
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入电子标签信息", notes = "从excel导入电子标签信息")
    public ResponseEntity importExcel(@ApiParam(value = "输入excel文件", required = true)
                                      @RequestPart(value = "file") MultipartFile file) {
        try {
            // 导入操作
            List<BasePartsInformationImport> basePartsInformationImports = EasyPoiUtils.importExcel(file, 2, 1, BasePartsInformationImport.class);
            Map<String, Object> resultMap = basePartsInformationService.importExcel(basePartsInformationImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
