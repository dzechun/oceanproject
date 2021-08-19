package com.fantechs.provider.daq.controller;

import com.fantechs.common.base.general.dto.daq.DaqDataCollectDto;
import com.fantechs.common.base.general.dto.daq.DaqDataCollectModel;
import com.fantechs.common.base.general.entity.daq.DaqDataCollect;
import com.fantechs.common.base.general.entity.daq.search.SearchDaqDataCollect;
import com.fantechs.common.base.general.entity.daq.search.SearchDaqEquipmentDataGroup;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.service.DaqDataCollectService;
import com.fantechs.provider.daq.service.socket.SocketService;
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
@RequestMapping("/daqDataCollect")
@Validated
public class DaqDataCollectController {

    @Resource
    private DaqDataCollectService daqDataCollectService;

    @Resource
    private SocketService socketService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated DaqDataCollect daqDataCollect) {
        return ControllerUtil.returnCRUD(daqDataCollectService.save(daqDataCollect));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(daqDataCollectService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = DaqDataCollect.update.class) DaqDataCollect daqDataCollect) {
        return ControllerUtil.returnCRUD(daqDataCollectService.update(daqDataCollect));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<DaqDataCollect> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        DaqDataCollect daqDataCollect = daqDataCollectService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(daqDataCollect, StringUtils.isEmpty(daqDataCollect) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<DaqDataCollectDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchDaqDataCollect searchDaqDataCollect) {
        Page<Object> page = PageHelper.startPage(searchDaqDataCollect.getStartPage(), searchDaqDataCollect.getPageSize());
        List<DaqDataCollectDto> list = daqDataCollectService.findList(ControllerUtil.dynamicConditionByEntity(searchDaqDataCollect));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @GetMapping("/manyOpen")
    public ResponseEntity openService() throws IOException {
        socketService.openService();
        return ControllerUtil.returnCRUD(1);
    }

    @ApiOperation("根据设备id查询最后一条")
    @PostMapping("/findByGroup")
    public ResponseEntity<List<DaqDataCollectDto>> findByGroup(@ApiParam(value = "equipmentId") @RequestParam(required = false) Long equipmentId){
        List<DaqDataCollectDto> sfcDataCollectDtos = daqDataCollectService.findByEquipmentId(equipmentId);
        return ControllerUtil.returnDataSuccess(sfcDataCollectDtos, sfcDataCollectDtos.size());
    }

    @ApiOperation("根据分组查询最后一条")
    @PostMapping("/findByGroups")
    public ResponseEntity<DaqDataCollectModel> findByGroups(@ApiParam(value = "查询对象") @RequestBody SearchDaqEquipmentDataGroup searchDaqEquipmentDataGroup){
        DaqDataCollectModel model = daqDataCollectService.findByGroup(searchDaqEquipmentDataGroup);
        return ControllerUtil.returnDataSuccess(model, 1);
    }

}
