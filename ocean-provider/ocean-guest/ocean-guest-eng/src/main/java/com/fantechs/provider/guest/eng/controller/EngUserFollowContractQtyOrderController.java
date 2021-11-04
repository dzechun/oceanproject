package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.general.entity.eng.EngUserFollowContractQtyOrder;
import com.fantechs.common.base.general.entity.eng.search.SearchEngUserFollowContractQtyOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.service.EngUserFollowContractQtyOrderService;
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
 * Created by leifengzhi on 2021/11/03.
 */
@RestController
@Api(tags = "用户关注合同量单")
@RequestMapping("/engUserFollowContractQtyOrder")
@Validated
@Slf4j
public class EngUserFollowContractQtyOrderController {

    @Resource
    private EngUserFollowContractQtyOrderService engUserFollowContractQtyOrderService;

    @ApiOperation("关注列表")
    @PostMapping("/findFollowList")
    public ResponseEntity<List<EngContractQtyOrderAndPurOrderDto>> findFollowList(@ApiParam(value = "查询对象")@RequestBody SearchEngUserFollowContractQtyOrder searchEngUserFollowContractQtyOrder) {
        Page<Object> page = PageHelper.startPage(searchEngUserFollowContractQtyOrder.getStartPage(),searchEngUserFollowContractQtyOrder.getPageSize());
        List<EngContractQtyOrderAndPurOrderDto> list = engUserFollowContractQtyOrderService.findFollowList(ControllerUtil.dynamicConditionByEntity(searchEngUserFollowContractQtyOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("关注")
    @PostMapping("/follow")
    public ResponseEntity follow(@ApiParam(value = "合同量单ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String contractQtyOrderIds) {
        return ControllerUtil.returnCRUD(engUserFollowContractQtyOrderService.follow(contractQtyOrderIds));
    }

    @ApiOperation("取消关注")
    @PostMapping("/cancelFollow")
    public ResponseEntity cancelFollow(@ApiParam(value = "合同量单ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String contractQtyOrderIds) {
        return ControllerUtil.returnCRUD(engUserFollowContractQtyOrderService.cancelFollow(contractQtyOrderIds));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngUserFollowContractQtyOrder engUserFollowContractQtyOrder) {
        return ControllerUtil.returnCRUD(engUserFollowContractQtyOrderService.save(engUserFollowContractQtyOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(engUserFollowContractQtyOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EngUserFollowContractQtyOrder.update.class) EngUserFollowContractQtyOrder engUserFollowContractQtyOrder) {
        return ControllerUtil.returnCRUD(engUserFollowContractQtyOrderService.update(engUserFollowContractQtyOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EngUserFollowContractQtyOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EngUserFollowContractQtyOrder  engUserFollowContractQtyOrder = engUserFollowContractQtyOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(engUserFollowContractQtyOrder,StringUtils.isEmpty(engUserFollowContractQtyOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngUserFollowContractQtyOrder>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngUserFollowContractQtyOrder searchEngUserFollowContractQtyOrder) {
        Page<Object> page = PageHelper.startPage(searchEngUserFollowContractQtyOrder.getStartPage(),searchEngUserFollowContractQtyOrder.getPageSize());
        List<EngUserFollowContractQtyOrder> list = engUserFollowContractQtyOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngUserFollowContractQtyOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<EngUserFollowContractQtyOrder>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchEngUserFollowContractQtyOrder searchEngUserFollowContractQtyOrder) {
        List<EngUserFollowContractQtyOrder> list = engUserFollowContractQtyOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngUserFollowContractQtyOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEngUserFollowContractQtyOrder searchEngUserFollowContractQtyOrder){
    List<EngUserFollowContractQtyOrder> list = engUserFollowContractQtyOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngUserFollowContractQtyOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EngUserFollowContractQtyOrder信息", EngUserFollowContractQtyOrder.class, "EngUserFollowContractQtyOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<EngUserFollowContractQtyOrder> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, EngUserFollowContractQtyOrder.class);
            Map<String, Object> resultMap = engUserFollowContractQtyOrderService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
