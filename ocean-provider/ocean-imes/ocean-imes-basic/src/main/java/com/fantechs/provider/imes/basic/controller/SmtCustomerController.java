package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.entity.basic.SmtCustomer;
import com.fantechs.common.base.entity.basic.search.SearchSmtCustomer;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtCustomerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/27.
 */
@RestController
@Api(tags = "客户信息")
@RequestMapping("/smtCustomer")
public class SmtCustomerController {

    @Autowired
    private SmtCustomerService smtCustomerService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SmtCustomer smtCustomer) {
        return ControllerUtil.returnCRUD(smtCustomerService.save(smtCustomer));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtCustomerService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtCustomer smtCustomer) {
        if(StringUtils.isEmpty(smtCustomer.getCustomerId()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtCustomerService.update(smtCustomer));
    }

    @ApiOperation("获取客户信息详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtCustomer> detail(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtCustomer  smtCustomer = smtCustomerService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtCustomer,StringUtils.isEmpty(smtCustomer)?0:1);
    }

    @ApiOperation("查询客户信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtCustomer>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtCustomer searchSmtCustomer) {
        Page<Object> page = PageHelper.startPage(searchSmtCustomer.getStartPage(),searchSmtCustomer.getPageSize());
        List<SmtCustomer> list = smtCustomerService.findList(searchSmtCustomer);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                          @RequestBody(required = false) SearchSmtCustomer searchSmtCustomer){
    List<SmtCustomer> list = smtCustomerService.findList(searchSmtCustomer);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出客户信息", "客户信息", SmtCustomer.class, "客户信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
