package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseFactoryImport;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtFactory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFactory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseFactoryService;
import com.fantechs.provider.base.service.BaseHtFactoryService;
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
import java.util.NoSuchElementException;

/**
 * @Auther: leifengzhi
 * @Date: 2020/9/1
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/baseFactory")
@Api(tags = "厂别信息管理")
@Slf4j
@Validated
public class BaseFactoryController {
    @Resource
    private BaseFactoryService baseFactoryService;

    @Resource
    private BaseHtFactoryService baseHtFactoryService;

    @ApiOperation("厂别信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseFactoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseFactory searchBaseFactory){
        Page<Object> page = PageHelper.startPage(searchBaseFactory.getStartPage(), searchBaseFactory.getPageSize());
        List<BaseFactoryDto> smtFactoryDtos = baseFactoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseFactory));
        return ControllerUtil.returnDataSuccess(smtFactoryDtos,(int)page.getTotal());
    }

    @ApiOperation("增加厂别信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：factoryCode、factoryName",required = true)@RequestBody @Validated BaseFactory baseFactory){
        return ControllerUtil.returnCRUD(baseFactoryService.save(baseFactory));

    }

    @ApiOperation(value = "获取工厂列表",notes = "获取工厂列表")
    @PostMapping("/findHtList")
    public ResponseEntity< List<BaseHtFactory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseFactory searchBaseFactory){
        Page<Object> page = PageHelper.startPage(searchBaseFactory.getStartPage(), searchBaseFactory.getPageSize());
        List<BaseHtFactory> menuList = baseHtFactoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseFactory));
        return ControllerUtil.returnDataSuccess(menuList,(int)page.getTotal());
    }

    @ApiOperation("修改厂别信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "厂别信息对象，厂别信息Id必传",required = true)@RequestBody @Validated(value= BaseFactory.update.class) BaseFactory baseFactory){
            return ControllerUtil.returnCRUD(baseFactoryService.update(baseFactory));
    }

    @ApiOperation("删除厂别信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象Id,多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空")String ids){
        return ControllerUtil.returnCRUD(baseFactoryService.batchDelete(ids));
    }

    @ApiOperation("获取厂别详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseFactory> detail(@ApiParam(value = "工厂ID",required = true) @RequestParam @NotNull(message="factoryId不能为空") Long factoryId){
        BaseFactory baseFactory = baseFactoryService.selectByKey(factoryId);
        return  ControllerUtil.returnDataSuccess(baseFactory,StringUtils.isEmpty(baseFactory)?0:1);
    }


    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出工厂excel",notes = "导出工厂excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody SearchBaseFactory searchBaseFactory){
        List<BaseFactoryDto> smtFactoryDtos = baseFactoryService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseFactory));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(smtFactoryDtos, "工厂信息表", "工厂信息", BaseFactoryDto.class, "工厂信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入厂别信息",notes = "从excel导入厂别信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                                        @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseFactoryImport> baseFactoryImports = EasyPoiUtils.importExcel(file, 2, 1, BaseFactoryImport.class);
            Map<String, Object> resultMap = baseFactoryService.importExcel(baseFactoryImports);
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

}
