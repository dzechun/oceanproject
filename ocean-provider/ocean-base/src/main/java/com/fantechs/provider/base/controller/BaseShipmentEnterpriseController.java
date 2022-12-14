package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseShipmentEnterpriseDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseShipmentEnterpriseImport;
import com.fantechs.common.base.general.entity.basic.BaseShipmentEnterprise;
import com.fantechs.common.base.general.entity.basic.history.BaseHtShipmentEnterprise;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseShipmentEnterprise;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtShipmentEnterpriseService;
import com.fantechs.provider.base.service.BaseShipmentEnterpriseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by leifengzhi on 2020/12/16.
 */
@RestController
@Api(tags = "物流商信息管理")
@RequestMapping("/baseShipmentEnterprise")
@Validated
@Slf4j
public class BaseShipmentEnterpriseController {

    @Autowired
    private BaseShipmentEnterpriseService baseShipmentEnterpriseService;
    @Resource
    private BaseHtShipmentEnterpriseService baseHtShipmentEnterpriseService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseShipmentEnterprise baseShipmentEnterprise) {
        return ControllerUtil.returnCRUD(baseShipmentEnterpriseService.save(baseShipmentEnterprise));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseShipmentEnterpriseService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseShipmentEnterprise.update.class) BaseShipmentEnterprise baseShipmentEnterprise) {
        return ControllerUtil.returnCRUD(baseShipmentEnterpriseService.update(baseShipmentEnterprise));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseShipmentEnterprise> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseShipmentEnterprise  baseShipmentEnterprise = baseShipmentEnterpriseService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseShipmentEnterprise,StringUtils.isEmpty(baseShipmentEnterprise)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseShipmentEnterpriseDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseShipmentEnterprise searchBaseShipmentEnterprise) {
        Page<Object> page = PageHelper.startPage(searchBaseShipmentEnterprise.getStartPage(),searchBaseShipmentEnterprise.getPageSize());
        List<BaseShipmentEnterpriseDto> list = baseShipmentEnterpriseService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseShipmentEnterprise));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtShipmentEnterprise>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseShipmentEnterprise searchBaseShipmentEnterprise) {
        Page<Object> page = PageHelper.startPage(searchBaseShipmentEnterprise.getStartPage(),searchBaseShipmentEnterprise.getPageSize());
        List<BaseHtShipmentEnterprise> List = baseHtShipmentEnterpriseService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseShipmentEnterprise));
        return ControllerUtil.returnDataSuccess(List,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseShipmentEnterprise searchBaseShipmentEnterprise){
    List<BaseShipmentEnterpriseDto> list = baseShipmentEnterpriseService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseShipmentEnterprise));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "物流商信息", "物流商信息", BaseShipmentEnterpriseDto.class, "物流商信息表.xls", response);
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
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseShipmentEnterpriseImport> baseShipmentEnterpriseImports = EasyPoiUtils.importExcel(file, 2, 1, BaseShipmentEnterpriseImport.class);
            Map<String, Object> resultMap = baseShipmentEnterpriseService.importExcel(baseShipmentEnterpriseImports);
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
