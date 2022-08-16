package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseStaffImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkerImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkingAreaImport;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorker;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorker;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtWorkerService;
import com.fantechs.provider.base.service.BaseWorkerService;
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
 * Created by leifengzhi on 2021/04/23.
 */
@RestController
@Api(tags = "工作人员信息")
@RequestMapping("/baseWorker")
@Validated
@Slf4j
public class BaseWorkerController {

    @Resource
    private BaseWorkerService baseWorkerService;
    @Resource
    private BaseHtWorkerService baseHtWorkerService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseWorkerDto baseWorkerDto) {
        return ControllerUtil.returnCRUD(baseWorkerService.saveDto(baseWorkerDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseWorkerService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseWorker.update.class) BaseWorkerDto baseWorkerDto) {
        return ControllerUtil.returnCRUD(baseWorkerService.updateDto(baseWorkerDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWorkerDto> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseWorkerDto  baseWorkerDto = baseWorkerService.selectDtoByKey(id);
        return  ControllerUtil.returnDataSuccess(baseWorkerDto,StringUtils.isEmpty(baseWorkerDto)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWorkerDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorker searchBaseWorker) {
        Page<Object> page = PageHelper.startPage(searchBaseWorker.getStartPage(),searchBaseWorker.getPageSize());
        List<BaseWorkerDto> list = baseWorkerService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorker));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtWorker>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorker searchBaseWorker) {
        Page<Object> page = PageHelper.startPage(searchBaseWorker.getStartPage(),searchBaseWorker.getPageSize());
        List<BaseHtWorker> list = baseHtWorkerService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorker));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseWorker searchBaseWorker){
    List<BaseWorkerDto> list = baseWorkerService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorker));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "工作人员信息", BaseWorkerDto.class, "BaseWorker.xls", response);
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
    @ApiOperation(value = "从excel导入部件组成信息",notes = "从excel导入部件组成信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseWorkerImport> baseWorkerImports = EasyPoiUtils.importExcel(file, 2, 1, BaseWorkerImport.class);
            Map<String, Object> resultMap = baseWorkerService.importExcel(baseWorkerImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
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
