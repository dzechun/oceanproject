package com.fantechs.controller.tab;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.entity.BaseTab;
import com.fantechs.service.BaseTabService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/12.
 */
@RestController
@Api(tags = "baseTab控制器")
@RequestMapping("/baseTab")
@Validated
public class BaseTabController {

    @Autowired
    private BaseTabService baseTabService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseTab baseTab) {
        return ControllerUtil.returnCRUD(baseTabService.save(baseTab));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseTabService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseTab.update.class) BaseTab baseTab) {
        return ControllerUtil.returnCRUD(baseTabService.update(baseTab));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseTab> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseTab  baseTab = baseTabService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseTab,StringUtils.isEmpty(baseTab)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseTab>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseTab searchBaseTab) {
        Page<Object> page = PageHelper.startPage(searchBaseTab.getStartPage(),searchBaseTab.getPageSize());
        List<BaseTab> list = baseTabService.findList(searchBaseTab);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseTab>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseTab searchBaseTab) {
        Page<Object> page = PageHelper.startPage(searchBaseTab.getStartPage(),searchBaseTab.getPageSize());
        List<BaseTab> list = baseTabService.findList(searchBaseTab);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseTab searchBaseTab){
    List<BaseTab> list = baseTabService.findList(searchBaseTab);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseTab信息", BaseTab.class, "BaseTab.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}