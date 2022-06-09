package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkShopImport;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShop;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShop;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtWorkShopService;
import com.fantechs.provider.base.service.BaseWorkShopService;
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
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
@RestController
@RequestMapping(value = "/baseWorkShop")
@Api(tags = "车间信息管理")
@Slf4j
@Validated
public class BaseWorkShopController {
    @Resource
    private BaseWorkShopService baseWorkShopService;

    @Resource
    private BaseHtWorkShopService baseHtWorkShopService;

    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation("查询列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWorkShopDto>> findList(
            @ApiParam(value = "查询对象")@RequestBody SearchBaseWorkShop searchBaseWorkShop
    ){
        Page<Object> page = PageHelper.startPage(searchBaseWorkShop.getStartPage(), searchBaseWorkShop.getPageSize());
        List<BaseWorkShopDto> smtFactoryDtos = baseWorkShopService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShop));
        return ControllerUtil.returnDataSuccess(smtFactoryDtos,(int)page.getTotal());
    }

    @ApiOperation("新增车间")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：workShopCode、workShopName、factoryId",required = true)@RequestBody @Validated BaseWorkShop baseWorkShop){
        return ControllerUtil.returnCRUD(baseWorkShopService.save(baseWorkShop));

    }

    @ApiOperation(value = "履历列表",notes = "履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtWorkShop>> findHtList(
            @ApiParam(value = "查询对象")@RequestBody SearchBaseWorkShop searchBaseWorkShop){
        Page<Object> page = PageHelper.startPage(searchBaseWorkShop.getStartPage(), searchBaseWorkShop.getPageSize());
        List<BaseHtWorkShop> menuList = baseHtWorkShopService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShop));
        return ControllerUtil.returnDataSuccess(menuList,(int)page.getTotal());
    }

    @ApiOperation("修改车间")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "车间信息对象，workShopId、workShopCode、workShopName、factoryId必传",required = true)@RequestBody @Validated BaseWorkShop baseWorkShop){
        return ControllerUtil.returnCRUD(baseWorkShopService.update(baseWorkShop));

    }

    @ApiOperation("删除车间")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "车间对象ID",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids){
        return ControllerUtil.returnCRUD(baseWorkShopService.batchDelete(ids));
    }

    @ApiOperation("获取车间详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWorkShop> detail(@ApiParam(value = "车间ID",required = true)@RequestParam @NotBlank String workShopId){
        BaseWorkShop baseWorkShop = baseWorkShopService.selectByKey(workShopId);
        return  ControllerUtil.returnDataSuccess(baseWorkShop,StringUtils.isEmpty(baseWorkShop)?0:1);
    }


    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出车间excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody SearchBaseWorkShop searchBaseWorkShop){
        List<BaseWorkShopDto> smtWorkShopDtos = baseWorkShopService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShop));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(smtWorkShopDtos, customExportParamList, "导出车间信息", "车间信息", "车间信息.xls", response);

    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入电子标签信息",notes = "从excel导入电子标签信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseWorkShopImport> baseWorkShopImports = EasyPoiUtils.importExcel(file, 2, 1, BaseWorkShopImport.class);
            Map<String, Object> resultMap = baseWorkShopService.importExcel(baseWorkShopImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation("批量添加")
    @PostMapping("/batchAdd")
    public ResponseEntity<List<BaseWorkShop>> batchAdd(@ApiParam(value = "",required = true)@RequestBody @Validated List<BaseWorkShop> baseWorkShops){
        List<BaseWorkShop> list = baseWorkShopService.batchAdd(baseWorkShops);
        return  ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }
}
