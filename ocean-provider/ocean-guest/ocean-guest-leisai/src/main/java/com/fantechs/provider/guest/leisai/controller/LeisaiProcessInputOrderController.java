package com.fantechs.provider.guest.leisai.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.leisai.LeisaiHtProcessInputOrderDto;
import com.fantechs.common.base.general.dto.leisai.LeisaiProcessInputOrderDto;
import com.fantechs.common.base.general.dto.leisai.imports.LeisaiProcessInputOrderImport;
import com.fantechs.common.base.general.entity.leisai.LeisaiProcessInputOrder;
import com.fantechs.common.base.general.entity.leisai.search.SearchLeisaiProcessInputOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.leisai.service.LeisaiHtProcessInputOrderService;
import com.fantechs.provider.guest.leisai.service.LeisaiProcessInputOrderService;
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
 * Created by leifengzhi on 2021/10/26.
 */
@RestController
@Api(tags = "制程数据录入单")
@RequestMapping("/leisaiProcessInputOrder")
@Validated
@Slf4j
public class LeisaiProcessInputOrderController {

    @Resource
    private LeisaiProcessInputOrderService leisaiProcessInputOrderService;
    @Resource
    private LeisaiHtProcessInputOrderService leisaiHtProcessInputOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated LeisaiProcessInputOrder leisaiProcessInputOrder) {
        return ControllerUtil.returnCRUD(leisaiProcessInputOrderService.save(leisaiProcessInputOrder));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<LeisaiProcessInputOrder> list) {
        return ControllerUtil.returnCRUD(leisaiProcessInputOrderService.batchSave(list));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(leisaiProcessInputOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=LeisaiProcessInputOrder.update.class) LeisaiProcessInputOrder leisaiProcessInputOrder) {
        return ControllerUtil.returnCRUD(leisaiProcessInputOrderService.update(leisaiProcessInputOrder));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<LeisaiProcessInputOrder> list) {
        return ControllerUtil.returnCRUD(leisaiProcessInputOrderService.batchUpdate(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<LeisaiProcessInputOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        LeisaiProcessInputOrder  leisaiProcessInputOrder = leisaiProcessInputOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(leisaiProcessInputOrder,StringUtils.isEmpty(leisaiProcessInputOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<LeisaiProcessInputOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchLeisaiProcessInputOrder searchLeisaiProcessInputOrder) {
        Page<Object> page = PageHelper.startPage(searchLeisaiProcessInputOrder.getStartPage(),searchLeisaiProcessInputOrder.getPageSize());
        List<LeisaiProcessInputOrderDto> list = leisaiProcessInputOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchLeisaiProcessInputOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<LeisaiProcessInputOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchLeisaiProcessInputOrder searchLeisaiProcessInputOrder) {
        List<LeisaiProcessInputOrderDto> list = leisaiProcessInputOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchLeisaiProcessInputOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<LeisaiHtProcessInputOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchLeisaiProcessInputOrder searchLeisaiProcessInputOrder) {
        Page<Object> page = PageHelper.startPage(searchLeisaiProcessInputOrder.getStartPage(),searchLeisaiProcessInputOrder.getPageSize());
        List<LeisaiHtProcessInputOrderDto> list = leisaiHtProcessInputOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchLeisaiProcessInputOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchLeisaiProcessInputOrder searchLeisaiProcessInputOrder){
    List<LeisaiProcessInputOrderDto> list = leisaiProcessInputOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchLeisaiProcessInputOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "制程数据录入单", "制程数据录入单信息", LeisaiProcessInputOrderDto.class, "LeisaiProcessInputOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<LeisaiProcessInputOrderImport> leisaiProcessInputOrderImport = EasyPoiUtils.importExcel(file, 2, 1, LeisaiProcessInputOrderImport.class);
            Map<String, Object> resultMap = leisaiProcessInputOrderService.importExcel(leisaiProcessInputOrderImport);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
