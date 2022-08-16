package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseAddressImport;
import com.fantechs.common.base.general.entity.basic.BaseAddress;
import com.fantechs.common.base.general.entity.basic.search.SearcBaseAddress;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseAddressService;
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
 * Created by leifengzhi on 2020/11/13.
 */
@RestController
@Api(tags = "地址信息管理")
@RequestMapping("/baseAddress")
@Validated
@Slf4j
public class BaseAddressController {

    @Resource
    private BaseAddressService baseAddressService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseAddress baseAddress) {
        return ControllerUtil.returnCRUD(baseAddressService.save(baseAddress));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseAddressService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseAddress.update.class) BaseAddress baseAddress) {
        return ControllerUtil.returnCRUD(baseAddressService.update(baseAddress));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseAddress> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseAddress baseAddress = baseAddressService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseAddress,StringUtils.isEmpty(baseAddress)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseAddressDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearcBaseAddress searcBaseAddress) {
        Page<Object> page = PageHelper.startPage(searcBaseAddress.getStartPage(), searcBaseAddress.getPageSize());
        List<BaseAddressDto> list = baseAddressService.findList(ControllerUtil.dynamicConditionByEntity(searcBaseAddress));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearcBaseAddress searcBaseAddress){
    List<BaseAddressDto> list = baseAddressService.findList(ControllerUtil.dynamicConditionByEntity(searcBaseAddress));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtAddress信息", BaseAddress.class, "SmtAddress.xls", response);
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
    @ApiOperation(value = "从excel导入地址信息",notes = "从excel导入地址信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseAddressImport> baseAddressImports = EasyPoiUtils.importExcel(file, 2, 1, BaseAddressImport.class);
            Map<String, Object> resultMap = baseAddressService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

}
