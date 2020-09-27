package com.fantechs.controller.workshop;

import com.fantechs.entity.WorkshopSection;
import com.fantechs.service.WorkshopSectionService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/09/25.
 */
@RestController
@Api(tags = "workshopSection控制器")
@RequestMapping("/workshopSection")
public class WorkshopSectionController {

    @Autowired
    private WorkshopSectionService workshopSectionService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody WorkshopSection workshopSection) {
        return ControllerUtil.returnCRUD(workshopSectionService.save(workshopSection));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(workshopSectionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody WorkshopSection workshopSection) {
        if(StringUtils.isEmpty(workshopSection.getWorkshopSectionId()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(workshopSectionService.update(workshopSection));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WorkshopSection> detail(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        WorkshopSection  workshopSection = workshopSectionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(workshopSection,StringUtils.isEmpty(workshopSection)?0:1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WorkshopSection>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWorkshopSection searchWorkshopSection) {
        Page<Object> page = PageHelper.startPage(searchWorkshopSection.getStartPage(),searchWorkshopSection.getPageSize());
        List<WorkshopSection> list = workshopSectionService.findList(ControllerUtil.dynamicConditionByEntity(searchWorkshopSection));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
