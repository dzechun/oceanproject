package com.fantechs.provider.om.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.imports.OmSalesCodeReSpcImport;
import com.fantechs.common.base.general.entity.om.OmSalesCodeReSpc;
import com.fantechs.common.base.general.entity.om.search.SearchOmSalesCodeReSpc;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmSalesCodeReSpcService;
import com.fantechs.provider.om.service.ht.OmHtSalesCodeReSpcService;
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
 * Created by leifengzhi on 2021/10/15.
 */
@RestController
@Api(tags = "销售编码与PO关联控制器")
@RequestMapping("/omSalesCodeReSpc")
@Validated
@Slf4j
public class OmSalesCodeReSpcController {

    @Resource
    private OmSalesCodeReSpcService omSalesCodeReSpcService;

    @Resource
    private OmHtSalesCodeReSpcService omHtSalesCodeReSpcService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmSalesCodeReSpc omSalesCodeReSpc) {
        return ControllerUtil.returnCRUD(omSalesCodeReSpcService.save(omSalesCodeReSpc));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omSalesCodeReSpcService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesCodeReSpc.update.class) OmSalesCodeReSpc omSalesCodeReSpc) {
        return ControllerUtil.returnCRUD(omSalesCodeReSpcService.update(omSalesCodeReSpc));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmSalesCodeReSpc> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmSalesCodeReSpc  omSalesCodeReSpc = omSalesCodeReSpcService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omSalesCodeReSpc,StringUtils.isEmpty(omSalesCodeReSpc)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmSalesCodeReSpcDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesCodeReSpc searchOmSalesCodeReSpc) {
        Page<Object> page = PageHelper.startPage(searchOmSalesCodeReSpc.getStartPage(),searchOmSalesCodeReSpc.getPageSize());
        List<OmSalesCodeReSpcDto> list = omSalesCodeReSpcService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesCodeReSpc));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<OmSalesCodeReSpcDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchOmSalesCodeReSpc searchOmSalesCodeReSpc) {
        List<OmSalesCodeReSpcDto> list = omSalesCodeReSpcService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesCodeReSpc));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtSalesCodeReSpcDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesCodeReSpc searchOmSalesCodeReSpc) {
        Page<Object> page = PageHelper.startPage(searchOmSalesCodeReSpc.getStartPage(),searchOmSalesCodeReSpc.getPageSize());
        List<OmHtSalesCodeReSpcDto> list = omHtSalesCodeReSpcService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesCodeReSpc));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmSalesCodeReSpc searchOmSalesCodeReSpc){
    List<OmSalesCodeReSpcDto> list = omSalesCodeReSpcService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesCodeReSpc));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "销售编码关联PO号信息", OmSalesCodeReSpcDto.class, "OmSalesCodeReSpc.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<OmSalesCodeReSpcImport> baseAddressImports = EasyPoiUtils.importExcel(file, 2, 1, OmSalesCodeReSpcImport.class);
            Map<String, Object> resultMap = omSalesCodeReSpcService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
