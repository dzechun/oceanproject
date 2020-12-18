package com.fantechs.provider.bcm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.bcm.BcmLabelDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabel;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabel;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabel;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.bcm.service.BcmHtLabelService;
import com.fantechs.provider.bcm.service.BcmLabelService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@RestController
@Api(tags = "标签信息")
@RequestMapping("/bcmLabel")
@Validated
public class BcmLabelController {

    @Autowired
    private BcmLabelService bcmLabelService;
    @Autowired
    private BcmHtLabelService bcmHtLabelService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@Validated BcmLabel bcmLabel, MultipartFile file) {
        return ControllerUtil.returnCRUD(bcmLabelService.add(bcmLabel,file));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(bcmLabelService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true) @Validated(value=BcmLabel.update.class) BcmLabel bcmLabel,MultipartFile file) {
        return ControllerUtil.returnCRUD(bcmLabelService.update(bcmLabel,file));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BcmLabel> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BcmLabel  bcmLabel = bcmLabelService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(bcmLabel,StringUtils.isEmpty(bcmLabel)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BcmLabelDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBcmLabel searchBcmLabel) {
        Page<Object> page = PageHelper.startPage(searchBcmLabel.getStartPage(),searchBcmLabel.getPageSize());
        List<BcmLabelDto> list = bcmLabelService.findList(searchBcmLabel);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BcmLabelDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBcmLabel searchBcmLabel) {
        Page<Object> page = PageHelper.startPage(searchBcmLabel.getStartPage(),searchBcmLabel.getPageSize());
        List<BcmLabelDto> list = bcmHtLabelService.findList(searchBcmLabel);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBcmLabel searchBcmLabel){
    List<BcmLabelDto> list = bcmLabelService.findList(searchBcmLabel);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BcmLabel信息", BcmLabel.class, "BcmLabel.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
