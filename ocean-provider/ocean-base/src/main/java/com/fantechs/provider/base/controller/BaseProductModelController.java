package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductModelImport;
import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtProductModelService;
import com.fantechs.provider.base.service.BaseProductModelService;
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
 * @Date: 2020/9/10 18:09
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/baseProductModel")
@Api(tags = "产品型号信息管理")
@Slf4j
@Validated
public class BaseProductModelController {

    @Resource
    private BaseProductModelService baseProductModelService;

    @Resource
    private BaseHtProductModelService baseHtProductModelService;


    @ApiOperation("根据条件查询产品型号信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductModel>> selectProductModels(
            @ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchBaseProductModel searchBaseProductModel
    ) {
        Page<Object> page = PageHelper.startPage(searchBaseProductModel.getStartPage(), searchBaseProductModel.getPageSize());
        List<BaseProductModel> baseProductModels = baseProductModelService.selectProductModels(ControllerUtil.dynamicConditionByEntity(searchBaseProductModel));
        return ControllerUtil.returnDataSuccess(baseProductModels, (int) page.getTotal());
    }

    @ApiOperation("根据条件查询产品型号信息列表")
    @PostMapping("/getAll")
    public ResponseEntity<List<BaseProductModel>> getAll() {
        List<BaseProductModel> baseProductModels = baseProductModelService.selectProductModels(new HashMap<>());
        return ControllerUtil.returnDataSuccess(baseProductModels, baseProductModels.size());
    }


    @ApiOperation("新增产品型号")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：productModelCode、productModelName", required = true) @RequestBody @Validated BaseProductModel baseProductModel) {
        return ControllerUtil.returnCRUD(baseProductModelService.save(baseProductModel));
    }

    @ApiOperation("新增产品型号")
    @PostMapping("/addForReturnId")
    public ResponseEntity<Long> addForReturnId(@ApiParam(value = "必传：productModelCode、productModelName", required = true) @RequestBody @Validated BaseProductModel baseProductModel) {
        baseProductModel = baseProductModelService.addForReturn(baseProductModel);
        return ControllerUtil.returnSuccess("成功", baseProductModel.getProductModelId());
    }

    @ApiOperation("新增产品型号并返回对象")
    @PostMapping("/addForReturn")
    public ResponseEntity<BaseProductModel> addForReturn(@ApiParam(value = "必传：productModelCode、productModelName", required = true) @RequestBody @Validated BaseProductModel baseProductModel) {
        return ControllerUtil.returnSuccess("成功", baseProductModelService.addForReturn(baseProductModel));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductModel> detail(@ApiParam(value = "工厂ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        if (StringUtils.isEmpty(id)) {
            return ControllerUtil.returnFailByParameError();
        }
        BaseProductModel baseProductModel = baseProductModelService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(baseProductModel, StringUtils.isEmpty(baseProductModel) ? 0 : 1);
    }

    @ApiOperation("修改产品型号")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "部门产品型号对象，产品型号信息Id必传", required = true) @RequestBody @Validated(value = BaseProductModel.update.class) BaseProductModel baseProductModel) {
        return ControllerUtil.returnCRUD(baseProductModelService.update(baseProductModel));
    }

    @ApiOperation("删除产品型号")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "产品型号对象ID", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductModelService.batchDelete(ids));
    }

    /**
     * 导出数据
     *
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出产品型号excel", produces = "application/octet-stream")
    public void exportProductModels(HttpServletResponse response, @ApiParam(value = "输入查询条件", required = false)
    @RequestBody(required = false) SearchBaseProductModel searchBaseProductModel) {
        List<BaseProductModel> list = baseProductModelService.selectProductModels(ControllerUtil.dynamicConditionByEntity(searchBaseProductModel));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "产品型号信息导出", "产品型号信息", BaseProductModel.class, "产品型号信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }


    @PostMapping("/findHtList")
    @ApiOperation(value = "根据条件查询产品型号履历信息", notes = "根据条件查询产品型号履历信息")
    public ResponseEntity<List<BaseHtProductModel>> selectHtProductModels(
            @ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchBaseProductModel searchBaseProductModel) {
        Page<Object> page = PageHelper.startPage(searchBaseProductModel.getStartPage(), searchBaseProductModel.getPageSize());
        List<BaseHtProductModel> baseHtProductModels = baseHtProductModelService.selectHtProductModels(ControllerUtil.dynamicConditionByEntity(searchBaseProductModel));
        return ControllerUtil.returnDataSuccess(baseHtProductModels, (int) page.getTotal());
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
            List<BaseProductModelImport> baseProductModelImports = EasyPoiUtils.importExcel(file, 2, 1, BaseProductModelImport.class);
            Map<String, Object> resultMap = baseProductModelService.importExcel(baseProductModelImports);
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