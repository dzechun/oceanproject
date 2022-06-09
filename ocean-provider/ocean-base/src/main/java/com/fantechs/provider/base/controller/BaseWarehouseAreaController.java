package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarehouseAreaDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseAreaImport;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouseArea;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouseArea;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtWarehouseAreaService;
import com.fantechs.provider.base.service.BaseWarehouseAreaService;
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
 * Created by leifengzhi on 2020/09/23.
 */
@RestController
@Api(tags = "库区信息管理")
@RequestMapping("/baseWarehouseArea")
@Validated
@Slf4j
public class BaseWarehouseAreaController {

    @Resource
    private BaseWarehouseAreaService baseWarehouseAreaService;
    @Resource
    private BaseHtWarehouseAreaService baseHtWarehouseAreaService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：warehouseAreaCode、warehouseAreaName、warehouseId",required = true)@RequestBody @Validated BaseWarehouseArea baseWarehouseArea) {
        return ControllerUtil.returnCRUD(baseWarehouseAreaService.save(baseWarehouseArea));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotNull(message = "ids不能为空") @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseWarehouseAreaService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseWarehouseArea.update.class) BaseWarehouseArea baseWarehouseArea) {
        return ControllerUtil.returnCRUD(baseWarehouseAreaService.update(baseWarehouseArea));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWarehouseArea> detail(@ApiParam(value = "工厂ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseWarehouseArea baseWarehouseArea = baseWarehouseAreaService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseWarehouseArea,StringUtils.isEmpty(baseWarehouseArea)?0:1);
    }

    @ApiOperation("根据条件查询角色信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWarehouseAreaDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWarehouseArea searchBaseWarehouseArea) {
        Page<Object> page = PageHelper.startPage(searchBaseWarehouseArea.getStartPage(), searchBaseWarehouseArea.getPageSize());
        List<BaseWarehouseAreaDto> list = baseWarehouseAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWarehouseArea));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "获取物料履历列表",notes = "获取物料履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity< List<BaseHtWarehouseArea>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWarehouseArea searchBaseWarehouseArea){
        Page<Object> page = PageHelper.startPage(searchBaseWarehouseArea.getStartPage(), searchBaseWarehouseArea.getPageSize());
        List<BaseHtWarehouseArea> htMaterials = baseHtWarehouseAreaService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseWarehouseArea));
        return ControllerUtil.returnDataSuccess(htMaterials,(int)page.getTotal());
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出库区excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody SearchBaseWarehouseArea searchBaseWarehouseArea){
        List<BaseWarehouseAreaDto> list = baseWarehouseAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWarehouseArea));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        try {
            // 自定义导出操作
            EasyPoiUtils.customExportExcel(list, customExportParamList, "导出库区", "库区信息", "库区信息.xls", response);
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
            List<BaseWarehouseAreaImport> baseWarehouseAreaImports = EasyPoiUtils.importExcel(file, 2, 1, BaseWarehouseAreaImport.class);
            Map<String, Object> resultMap = baseWarehouseAreaService.importExcel(baseWarehouseAreaImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
