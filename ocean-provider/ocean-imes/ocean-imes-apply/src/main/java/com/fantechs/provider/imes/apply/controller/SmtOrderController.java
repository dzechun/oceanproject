package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtOrderDto;
import com.fantechs.common.base.entity.apply.SmtOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtOrder;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/10/13.
 */
@RestController
@Api(tags = "smtOrder控制器")
@RequestMapping("/smtOrder")
@Validated
public class SmtOrderController {

    @Autowired
    private SmtOrderService smtOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：orderCode、materialId、orderQuantity",required = true)@RequestBody @Validated SmtOrder smtOrder) {
        return ControllerUtil.returnCRUD(smtOrderService.save(smtOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtOrder.update.class) SmtOrder smtOrder) {
        return ControllerUtil.returnCRUD(smtOrderService.update(smtOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtOrder  smtOrder = smtOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtOrder,StringUtils.isEmpty(smtOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtOrder searchSmtOrder) {
        Page<Object> page = PageHelper.startPage(searchSmtOrder.getStartPage(),searchSmtOrder.getPageSize());
        List<SmtOrderDto> list = smtOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtOrder searchSmtOrder){
    List<SmtOrderDto> list = smtOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtOrder信息", SmtOrderDto.class, "SmtOrderDto.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
