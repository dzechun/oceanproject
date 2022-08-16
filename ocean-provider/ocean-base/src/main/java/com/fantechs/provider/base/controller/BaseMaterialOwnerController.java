package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwner;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtMaterialOwnerService;
import com.fantechs.provider.base.service.BaseMaterialOwnerService;
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
 * Created by leifengzhi on 2021/04/22.
 */
@RestController
@Api(tags = "货主信息")
@RequestMapping("/baseMaterialOwner")
@Validated
@Slf4j
public class BaseMaterialOwnerController {

    @Resource
    private BaseMaterialOwnerService baseMaterialOwnerService;

    @Resource
    private BaseHtMaterialOwnerService baseHtMaterialOwnerService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialOwnerCode、materialOwnerName",required = true)@RequestBody @Validated BaseMaterialOwner baseMaterialOwner) {
        return ControllerUtil.returnCRUD(baseMaterialOwnerService.save(baseMaterialOwner));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseMaterialOwnerService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseMaterialOwner.update.class) BaseMaterialOwner baseMaterialOwner) {
        return ControllerUtil.returnCRUD(baseMaterialOwnerService.update(baseMaterialOwner));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseMaterialOwner> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseMaterialOwner  baseMaterialOwner = baseMaterialOwnerService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseMaterialOwner,StringUtils.isEmpty(baseMaterialOwner)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseMaterialOwnerDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterialOwner searchBaseMaterialOwner) {
        Page<Object> page = PageHelper.startPage(searchBaseMaterialOwner.getStartPage(),searchBaseMaterialOwner.getPageSize());
        List<BaseMaterialOwnerDto> list = baseMaterialOwnerService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialOwner));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseMaterialOwnerDto>> findAll() {
        List<BaseMaterialOwnerDto> list = baseMaterialOwnerService.findAll();
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtMaterialOwner>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterialOwner searchBaseMaterialOwner) {
        Page<Object> page = PageHelper.startPage(searchBaseMaterialOwner.getStartPage(),searchBaseMaterialOwner.getPageSize());
        List<BaseHtMaterialOwner> list = baseHtMaterialOwnerService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialOwner));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseMaterialOwner searchBaseMaterialOwner) {
        List<BaseMaterialOwnerDto> list = baseMaterialOwnerService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialOwner));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出货主信息", "货主信息", BaseMaterialOwnerDto.class, "货主信息.xls", response);
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
            List<BaseMaterialOwnerDto> baseMaterialOwnerDtos = EasyPoiUtils.importExcel(file,2, 1, BaseMaterialOwnerDto.class);
            Map<String, Object> resultMap = baseMaterialOwnerService.importExcel(baseMaterialOwnerDtos);
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
