package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatePartsImport;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlateParts;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtPlatePartsService;
import com.fantechs.provider.base.service.BasePlatePartsService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@RestController
@Api(tags = "部件组成信息管理")
@RequestMapping("/basePlateParts")
@Validated
@Slf4j
public class BasePlatePartsController {

    @Autowired
    private BasePlatePartsService basePlatePartsService;
    @Resource
    private BaseHtPlatePartsService baseHtPlatePartsService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BasePlateParts basePlateParts) {
        return ControllerUtil.returnCRUD(basePlatePartsService.save(basePlateParts));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(basePlatePartsService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BasePlateParts.update.class) BasePlateParts basePlateParts) {
        return ControllerUtil.returnCRUD(basePlatePartsService.update(basePlateParts));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BasePlateParts> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BasePlateParts  basePlateParts = basePlatePartsService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(basePlateParts,StringUtils.isEmpty(basePlateParts)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BasePlatePartsDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlateParts searchBasePlateParts) {
        Page<Object> page = PageHelper.startPage(searchBasePlateParts.getStartPage(),searchBasePlateParts.getPageSize());
        List<BasePlatePartsDto> list =  basePlatePartsService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtPlateParts>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlateParts searchBasePlateParts) {
        Page<Object> page = PageHelper.startPage(searchBasePlateParts.getStartPage(),searchBasePlateParts.getPageSize());
        List<BaseHtPlateParts> list = baseHtPlatePartsService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBasePlateParts searchBasePlateParts){
    List<BasePlatePartsDto> list = basePlatePartsService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "部件组成导出信息", "部件组成信息", BasePlatePartsDto.class, "部件组成.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/download")
    @ApiOperation(value = "下载部件组成导入模板",notes = "下载部件组成导入模板",produces = "application/octet-stream")
    public void downloadTemplate(HttpServletResponse response
    ){
        BasePlatePartsImport basePlatePartsImport = new BasePlatePartsImport("WL-001","BJ-001","大","个", "1","红色","棉","填写了部件编码则工艺路线必填", "1","ZZ-001");
        ArrayList<BasePlatePartsImport> basePlatePartsImports = new ArrayList<>();
        basePlatePartsImports.add(basePlatePartsImport);
        try {
            // 生成excel模板
            EasyPoiUtils.exportExcel(basePlatePartsImports, "部件组成信息导入模板", "部件组成信息导入模板", BasePlatePartsImport.class, "部件组成信息导入模板.xls", response);
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
    @ApiOperation(value = "从excel导入部件组成信息",notes = "从excel导入部件组成信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BasePlatePartsImport> basePlatePartsImports = EasyPoiUtils.importExcel(file,1,2, BasePlatePartsImport.class);
            Map<String, Object> resultMap = basePlatePartsService.importExcel(basePlatePartsImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
