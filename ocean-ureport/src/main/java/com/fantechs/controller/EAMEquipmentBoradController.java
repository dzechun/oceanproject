package com.fantechs.controller;


import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.entity.EAMEquipmentBorad;
import com.fantechs.entity.search.SearchProLineBoard;
import com.fantechs.service.EAMEquipmentBoradService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/eamEquipmentBorad")
@Api(tags = "雷赛-设备运行状态看板")
public class EAMEquipmentBoradController {

    @Resource
    private EAMEquipmentBoradService eamEquipmentBoradService;

    @ApiOperation("设备运行列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EAMEquipmentBorad>> findList(@ApiParam(value = "查询对象")@RequestBody SearchProLineBoard searchProLineBoard) throws ParseException {
        List<EAMEquipmentBorad> model = eamEquipmentBoradService.findList(searchProLineBoard);
        return ControllerUtil.returnDataSuccess(model,1);
    }

}
