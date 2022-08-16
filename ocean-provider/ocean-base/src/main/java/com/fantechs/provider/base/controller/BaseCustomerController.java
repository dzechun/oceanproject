package com.fantechs.provider.base.controller;


import com.fantechs.common.base.general.entity.basic.BaseCustomer;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseCustomer;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseCustomerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/27.
 */
@RestController
@Api(tags = "客户信息")
@RequestMapping("/baseCustomer")
@Validated
public class BaseCustomerController {

    @Resource
    private BaseCustomerService baseCustomerService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：customerCode、customerName",required = true)@RequestBody @Validated BaseCustomer baseCustomer) {
        return ControllerUtil.returnCRUD(baseCustomerService.save(baseCustomer));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam  @NotBlank(message="ids不能为空")String ids) {
        return ControllerUtil.returnCRUD(baseCustomerService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseCustomer.update.class) BaseCustomer baseCustomer) {
        return ControllerUtil.returnCRUD(baseCustomerService.update(baseCustomer));
    }

    @ApiOperation("获取客户信息详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseCustomer> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id) {
        BaseCustomer baseCustomer = baseCustomerService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseCustomer,StringUtils.isEmpty(baseCustomer)?0:1);
    }

    @ApiOperation("查询客户信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseCustomer>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseCustomer searchBaseCustomer) {
        Page<Object> page = PageHelper.startPage(searchBaseCustomer.getStartPage(), searchBaseCustomer.getPageSize());
        List<BaseCustomer> list = baseCustomerService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseCustomer));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                          @RequestBody(required = false) SearchBaseCustomer searchBaseCustomer){
    List<BaseCustomer> list = baseCustomerService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseCustomer));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出客户信息", "客户信息", BaseCustomer.class, "客户信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "必传：customer_code",required = true)@RequestBody @Validated BaseCustomer baseCustomer) {
        return ControllerUtil.returnCRUD(baseCustomerService.saveByApi(baseCustomer));
    }
}
