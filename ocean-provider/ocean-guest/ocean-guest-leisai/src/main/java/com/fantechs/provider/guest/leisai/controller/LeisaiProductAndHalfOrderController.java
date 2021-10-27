package com.fantechs.provider.guest.leisai.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.leisai.LeisaiHtProductAndHalfOrderDto;
import com.fantechs.common.base.general.dto.leisai.LeisaiProductAndHalfOrderDto;
import com.fantechs.common.base.general.dto.leisai.imports.LeisaiProductAndHalfOrderImport;
import com.fantechs.common.base.general.entity.leisai.LeisaiProductAndHalfOrder;
import com.fantechs.common.base.general.entity.leisai.search.SearchLeisaiProductAndHalfOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.leisai.service.LeisaiHtProductAndHalfOrderService;
import com.fantechs.provider.guest.leisai.service.LeisaiProductAndHalfOrderService;
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
 * Created by leifengzhi on 2021/10/27.
 */
@RestController
@Api(tags = "成品和半成品对应表")
@RequestMapping("/leisaiProductAndHalfOrder")
@Validated
@Slf4j
public class LeisaiProductAndHalfOrderController {

    @Resource
    private LeisaiProductAndHalfOrderService leisaiProductAndHalfOrderService;
    @Resource
    private LeisaiHtProductAndHalfOrderService leisaiHtProductAndHalfOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated LeisaiProductAndHalfOrder leisaiProductAndHalfOrder) {
        return ControllerUtil.returnCRUD(leisaiProductAndHalfOrderService.save(leisaiProductAndHalfOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(leisaiProductAndHalfOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=LeisaiProductAndHalfOrder.update.class) LeisaiProductAndHalfOrder leisaiProductAndHalfOrder) {
        return ControllerUtil.returnCRUD(leisaiProductAndHalfOrderService.update(leisaiProductAndHalfOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<LeisaiProductAndHalfOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        LeisaiProductAndHalfOrder  leisaiProductAndHalfOrder = leisaiProductAndHalfOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(leisaiProductAndHalfOrder,StringUtils.isEmpty(leisaiProductAndHalfOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<LeisaiProductAndHalfOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchLeisaiProductAndHalfOrder searchLeisaiProductAndHalfOrder) {
        Page<Object> page = PageHelper.startPage(searchLeisaiProductAndHalfOrder.getStartPage(),searchLeisaiProductAndHalfOrder.getPageSize());
        List<LeisaiProductAndHalfOrderDto> list = leisaiProductAndHalfOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchLeisaiProductAndHalfOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<LeisaiProductAndHalfOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchLeisaiProductAndHalfOrder searchLeisaiProductAndHalfOrder) {
        List<LeisaiProductAndHalfOrderDto> list = leisaiProductAndHalfOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchLeisaiProductAndHalfOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<LeisaiHtProductAndHalfOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchLeisaiProductAndHalfOrder searchLeisaiProductAndHalfOrder) {
        Page<Object> page = PageHelper.startPage(searchLeisaiProductAndHalfOrder.getStartPage(),searchLeisaiProductAndHalfOrder.getPageSize());
        List<LeisaiHtProductAndHalfOrderDto> list = leisaiHtProductAndHalfOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchLeisaiProductAndHalfOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchLeisaiProductAndHalfOrder searchLeisaiProductAndHalfOrder){
    List<LeisaiProductAndHalfOrderDto> list = leisaiProductAndHalfOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchLeisaiProductAndHalfOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "LeisaiProductAndHalfOrder信息", LeisaiProductAndHalfOrderDto.class, "LeisaiProductAndHalfOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<LeisaiProductAndHalfOrderImport> productAndHalfOrderImport = EasyPoiUtils.importExcel(file, 2, 1, LeisaiProductAndHalfOrderImport.class);
            Map<String, Object> resultMap = leisaiProductAndHalfOrderService.importExcel(productAndHalfOrderImport);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
