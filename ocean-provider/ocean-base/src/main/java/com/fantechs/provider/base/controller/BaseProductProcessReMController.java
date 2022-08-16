package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseMaterialSupplierImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductProcessReMImport;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessReM;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessReM;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseProductProcessReMService;
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
 * Created by leifengzhi on 2021/04/28.
 */
@RestController
@Api(tags = "产品关键物料清单")
@RequestMapping("/baseProductProcessReM")
@Validated
@Slf4j
public class BaseProductProcessReMController {

    @Resource
    private BaseProductProcessReMService baseProductProcessReMService;

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<BaseProductProcessReM> list) {
        return ControllerUtil.returnCRUD(baseProductProcessReMService.batchSave(list));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseProductProcessReM baseProductProcessReM) {
        return ControllerUtil.returnCRUD(baseProductProcessReMService.save(baseProductProcessReM));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductProcessReMService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseProductProcessReM.update.class) BaseProductProcessReM baseProductProcessReM) {
        return ControllerUtil.returnCRUD(baseProductProcessReMService.update(baseProductProcessReM));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductProcessReM> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseProductProcessReM  baseProductProcessReM = baseProductProcessReMService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProductProcessReM,StringUtils.isEmpty(baseProductProcessReM)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductProcessReM>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductProcessReM searchBaseProductProcessReM) {
        Page<Object> page = PageHelper.startPage(searchBaseProductProcessReM.getStartPage(),searchBaseProductProcessReM.getPageSize());
        List<BaseProductProcessReM> list = baseProductProcessReMService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessReM));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductProcessReM>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductProcessReM searchBaseProductProcessReM) {
        Page<Object> page = PageHelper.startPage(searchBaseProductProcessReM.getStartPage(),searchBaseProductProcessReM.getPageSize());
        List<BaseHtProductProcessReM> list = baseProductProcessReMService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessReM));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProductProcessReM searchBaseProductProcessReM){
    List<BaseProductProcessReM> list = baseProductProcessReMService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessReM));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "产品关键物料清单信息", BaseProductProcessReM.class, "产品关键物料清单信息.xls", response);
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
            List<BaseProductProcessReMImport> baseProductProcessReMImports = EasyPoiUtils.importExcel(file, 2, 1, BaseProductProcessReMImport.class);
            Map<String, Object> resultMap = baseProductProcessReMService.importExcel(baseProductProcessReMImports);
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
