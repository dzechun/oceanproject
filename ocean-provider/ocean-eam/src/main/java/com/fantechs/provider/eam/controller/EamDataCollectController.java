package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamDataCollectDto;
import com.fantechs.common.base.general.dto.eam.EamDataCollectModel;
import com.fantechs.common.base.general.entity.eam.search.SearchEamDataCollect;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentDataGroup;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamDataCollectService;
import com.fantechs.provider.eam.service.socket.SocketService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Created by leifengzhi on 2021/07/19.
 */
@RestController
@Api(tags = "过站数据采集控制器")
@RequestMapping("/mesSfcDataCollect")
@Validated
public class EamDataCollectController {

    @Resource
    private EamDataCollectService eamDataCollectService;

    @Resource
    private SocketService socketService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated EamDataCollect eamDataCollect) {
        return ControllerUtil.returnCRUD(eamDataCollectService.save(eamDataCollect));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamDataCollectService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = EamDataCollect.update.class) EamDataCollect eamDataCollect) {
        return ControllerUtil.returnCRUD(eamDataCollectService.update(eamDataCollect));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamDataCollect> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        EamDataCollect eamDataCollect = eamDataCollectService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(eamDataCollect, StringUtils.isEmpty(eamDataCollect) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamDataCollectDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchEamDataCollect searchEamDataCollect) {
        Page<Object> page = PageHelper.startPage(searchEamDataCollect.getStartPage(), searchEamDataCollect.getPageSize());
        List<EamDataCollectDto> list = eamDataCollectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamDataCollect));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @GetMapping("/manyOpen")
    public ResponseEntity openService() throws IOException {
        socketService.openService();
        return ControllerUtil.returnCRUD(1);
    }

    @ApiOperation("根据设备id查询最后一条")
    @PostMapping("/findByGroup")
    public ResponseEntity<List<EamDataCollectDto>> findByGroup(@ApiParam(value = "equipmentId") @RequestParam(required = false) Long equipmentId){
        List<EamDataCollectDto> sfcDataCollectDtos = eamDataCollectService.findByEquipmentId(equipmentId);
        return ControllerUtil.returnDataSuccess(sfcDataCollectDtos, sfcDataCollectDtos.size());
    }

    @ApiOperation("根据分组查询最后一条")
    @PostMapping("/findByGroups")
    public ResponseEntity<EamDataCollectModel> findByGroups(@ApiParam(value = "查询对象") @RequestBody SearchEamEquipmentDataGroup searchEamEquipmentDataGroup){
        EamDataCollectModel model = eamDataCollectService.findByGroup(searchEamEquipmentDataGroup);
        return ControllerUtil.returnDataSuccess(model, 1);
    }

}
