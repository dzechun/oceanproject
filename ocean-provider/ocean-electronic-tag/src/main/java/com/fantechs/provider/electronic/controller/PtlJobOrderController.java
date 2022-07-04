package com.fantechs.provider.electronic.controller;


import com.fantechs.common.base.electronic.dto.PtlJobOrderDetDto;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.electronic.entity.search.SearchPtlJobOrder;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.PtlJobOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/01.
 */
@RestController
@Api(tags = "电子标签作业任务控制器")
@RequestMapping("/ptlJobOrder")
@Validated
public class PtlJobOrderController {

    @Resource
    private PtlJobOrderService ptlJobOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    @Transactional
    @GlobalTransactional
    public ResponseEntity<PtlJobOrder> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlJobOrder ptlJobOrder) {
        int count = ptlJobOrderService.save(ptlJobOrder);
        if (count > 0) {
            return ControllerUtil.returnSuccess("操作成功", ptlJobOrder);
        } else {
            return ControllerUtil.returnFail(ErrorCodeEnum.GL99990005);
        }

    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    @Transactional
    @GlobalTransactional
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ptlJobOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @GlobalTransactional
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=PtlJobOrder.update.class) PtlJobOrder ptlJobOrder) {
        return ControllerUtil.returnCRUD(ptlJobOrderService.update(ptlJobOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<PtlJobOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        PtlJobOrder  ptlJobOrder = ptlJobOrderService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(ptlJobOrder,StringUtils.isEmpty(ptlJobOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<PtlJobOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchPtlJobOrder searchPtlJobOrder) {
        Page<Object> page = PageHelper.startPage(searchPtlJobOrder.getStartPage(),searchPtlJobOrder.getPageSize());
        List<PtlJobOrderDto> list = ptlJobOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlJobOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPtlJobOrder searchPtlJobOrder){
        searchPtlJobOrder.setType(1);
        Map<String, Object> map = ptlJobOrderService.export(ControllerUtil.dynamicConditionByEntity(searchPtlJobOrder));
    try {
        // 导出操作
        List<Class<?>> clzList = new LinkedList<>();
        clzList.add(PtlJobOrderDto.class);
        clzList.add(PtlJobOrderDetDto.class);
        EasyPoiUtils.exportExcelSheetList(map, clzList, "电子标签作业任务信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("根据拣货单号修改")
    @PostMapping("/updateByRelatedOrderCode")
    public ResponseEntity updateByRelatedOrderCode(@ApiParam(value = "对象，Id必传",required = true)@RequestBody PtlJobOrder ptlJobOrder) {
        try {
            int i = ptlJobOrderService.updateByRelatedOrderCode(ptlJobOrder);
            return ControllerUtil.returnCRUD(i);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }
}
