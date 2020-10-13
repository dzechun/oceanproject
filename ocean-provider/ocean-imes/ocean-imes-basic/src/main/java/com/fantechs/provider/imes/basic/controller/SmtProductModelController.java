package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.history.SmtHtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtProductModelService;
import com.fantechs.provider.imes.basic.service.SmtProductModelService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: wcz
 * @Date: 2020/9/10 18:09
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/smtProductModel")
@Api(tags = "产品型号管理")
@Slf4j
@Validated
public class SmtProductModelController {

    @Autowired
    private SmtProductModelService smtProductModelService;

    @Autowired
    private SmtHtProductModelService smtHtProductModelService;


    @ApiOperation("根据条件查询产品型号信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtProductModel>> selectProductModels(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtProductModel searchSmtProductModel
    ){
        Page<Object> page = PageHelper.startPage(searchSmtProductModel.getStartPage(),searchSmtProductModel.getPageSize());
        List<SmtProductModel> smtProductModels = smtProductModelService.selectProductModels(searchSmtProductModel);
        return ControllerUtil.returnDataSuccess(smtProductModels,(int)page.getTotal());
    }


    @ApiOperation("新增产品型号")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：productModelCode、productModelName",required = true)@RequestBody @Validated SmtProductModel smtProductModel){
        return ControllerUtil.returnCRUD(smtProductModelService.save(smtProductModel));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtProductModel> detail(@ApiParam(value = "工厂ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtProductModel  smtProductModel = smtProductModelService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtProductModel,StringUtils.isEmpty(smtProductModel)?0:1);
    }

    @ApiOperation("修改产品型号")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "部门产品型号对象，产品型号信息Id必传",required = true)@RequestBody @Validated(value = SmtProductModel.update.class) SmtProductModel smtProductModel){
        return ControllerUtil.returnCRUD(smtProductModelService.update(smtProductModel));
    }

    @ApiOperation("删除产品型号")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "产品型号对象ID",required = true)@RequestParam @NotBlank(message = "ids不能为空") String ids){
        return ControllerUtil.returnCRUD(smtProductModelService.batchDelete(ids));
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出产品型号excel")
    public void exportProductModels(HttpServletResponse response, @ApiParam(value ="输入查询条件",required = false)
                                   @RequestBody(required = false) SearchSmtProductModel searchSmtProductModel){
        List<SmtProductModel> list = smtProductModelService.selectProductModels(searchSmtProductModel);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "产品型号信息导出", "产品型号信息", SmtProductModel.class, "产品型号信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }


    @PostMapping("/findHtList")
    @ApiOperation(value = "根据条件查询产品型号履历信息",notes = "根据条件查询产品型号履历信息")
    public ResponseEntity<List<SmtHtProductModel>> selectHtProductModels(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtProductModel searchSmtProductModel) {
        Page<Object> page = PageHelper.startPage(searchSmtProductModel.getStartPage(),searchSmtProductModel.getPageSize());
        List<SmtHtProductModel> smtHtProductModels=smtHtProductModelService.selectHtProductModels(searchSmtProductModel);
        return  ControllerUtil.returnDataSuccess(smtHtProductModels, (int)page.getTotal());
    }

}