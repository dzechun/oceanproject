package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseSupplierReUserService;
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
 * Created by leifengzhi on 2021/08/27.
 */
@RestController
@Api(tags = "供应商用户关系")
@RequestMapping("/baseSupplierReUser")
@Validated
public class BaseSupplierReUserController {

    @Resource
    private BaseSupplierReUserService baseSupplierReUserService;

    @ApiOperation("绑定用户")
    @PostMapping("/addUser")
    public ResponseEntity addUser(
            @ApiParam(value = "供应商Id",required = true)@RequestParam @NotNull(message = "供应商Id不能为空") Long supplierId,
            @ApiParam(value = "用户Id",required = true)@RequestBody @NotNull(message = "userIds不能为空") List<Long> userIds){
        return ControllerUtil.returnCRUD(baseSupplierReUserService.addUser(supplierId,userIds));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSupplierReUser baseSupplierReUser) {
        return ControllerUtil.returnCRUD(baseSupplierReUserService.save(baseSupplierReUser));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSupplierReUserService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseSupplierReUser.update.class) BaseSupplierReUser baseSupplierReUser) {
        return ControllerUtil.returnCRUD(baseSupplierReUserService.update(baseSupplierReUser));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSupplierReUser> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSupplierReUser  baseSupplierReUser = baseSupplierReUserService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSupplierReUser,StringUtils.isEmpty(baseSupplierReUser)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSupplierReUser>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSupplierReUser searchBaseSupplierReUser) {
        Page<Object> page = PageHelper.startPage(searchBaseSupplierReUser.getStartPage(),searchBaseSupplierReUser.getPageSize());
        List<BaseSupplierReUser> list = baseSupplierReUserService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSupplierReUser));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtSupplierReUser>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSupplierReUser searchBaseSupplierReUser) {
        Page<Object> page = PageHelper.startPage(searchBaseSupplierReUser.getStartPage(),searchBaseSupplierReUser.getPageSize());
        List<BaseHtSupplierReUser> list = baseSupplierReUserService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseSupplierReUser));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSupplierReUser searchBaseSupplierReUser){
    List<BaseSupplierReUser> list = baseSupplierReUserService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSupplierReUser));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "供应商用户关系", BaseSupplierReUser.class, "供应商用户关系.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSupplierReUser baseSupplierReUser) {
        return ControllerUtil.returnCRUD(baseSupplierReUserService.saveByApi(baseSupplierReUser));
    }
}
