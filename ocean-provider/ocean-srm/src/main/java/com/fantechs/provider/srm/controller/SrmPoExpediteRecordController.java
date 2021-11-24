package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmPoExpediteRecordDto;
import com.fantechs.common.base.general.entity.srm.SrmPoExpediteRecord;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPoExpediteRecord;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmPoExpediteRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/11/18.
 */
@RestController
@Api(tags = "订单跟催记录内容控制器")
@RequestMapping("/srmPoExpediteRecord")
@Validated
public class SrmPoExpediteRecordController {

    @Resource
    private SrmPoExpediteRecordService srmPoExpediteRecordService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmPoExpediteRecord srmPoExpediteRecord) {
        return ControllerUtil.returnCRUD(srmPoExpediteRecordService.save(srmPoExpediteRecord));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmPoExpediteRecordService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmPoExpediteRecord.update.class) SrmPoExpediteRecord srmPoExpediteRecord) {
        return ControllerUtil.returnCRUD(srmPoExpediteRecordService.update(srmPoExpediteRecord));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmPoExpediteRecord> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmPoExpediteRecord  srmPoExpediteRecord = srmPoExpediteRecordService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmPoExpediteRecord,StringUtils.isEmpty(srmPoExpediteRecord)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmPoExpediteRecordDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPoExpediteRecord searchSrmPoExpediteRecord) {
        Page<Object> page = PageHelper.startPage(searchSrmPoExpediteRecord.getStartPage(),searchSrmPoExpediteRecord.getPageSize());
        List<SrmPoExpediteRecordDto> list = srmPoExpediteRecordService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoExpediteRecord));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmPoExpediteRecordDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmPoExpediteRecord searchSrmPoExpediteRecord) {
        List<SrmPoExpediteRecordDto> list = srmPoExpediteRecordService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoExpediteRecord));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<SrmPoExpediteRecord>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPoExpediteRecord searchSrmPoExpediteRecord) {
//        Page<Object> page = PageHelper.startPage(searchSrmPoExpediteRecord.getStartPage(),searchSrmPoExpediteRecord.getPageSize());
//        List<SrmPoExpediteRecord> list = srmPoExpediteRecordService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSrmPoExpediteRecord));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchSrmPoExpediteRecord searchSrmPoExpediteRecord){
//    List<SrmPoExpediteRecordDto> list = srmPoExpediteRecordService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPoExpediteRecord));
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "SrmPoExpediteRecord信息", SrmPoExpediteRecordDto.class, "SrmPoExpediteRecord.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
//
//    @PostMapping(value = "/import")
//    @ApiOperation(value = "从excel导入",notes = "从excel导入")
//    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
//        try {
//            // 导入操作
//            List<SrmPoExpediteRecord> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, SrmPoExpediteRecord.class);
//            Map<String, Object> resultMap = srmPoExpediteRecordService.importExcel(baseAddressImports);
//            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error(e.getMessage());
//            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
//        }
//    }
}
