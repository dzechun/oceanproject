package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcDataCollect;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcDataCollectService;
import com.fantechs.provider.mes.sfc.service.socket.SocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 *
 * Created by leifengzhi on 2021/07/19.
 */
@RestController
@Api(tags = "mesSfcDataCollect控制器")
@RequestMapping("/mesSfcDataCollect")
@Validated
public class MesSfcDataCollectController {

    @Resource
    private MesSfcDataCollectService mesSfcDataCollectService;

    @Resource
    private SocketService socketService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcDataCollect mesSfcDataCollect) {
        return ControllerUtil.returnCRUD(mesSfcDataCollectService.save(mesSfcDataCollect));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcDataCollectService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcDataCollect.update.class) MesSfcDataCollect mesSfcDataCollect) {
        return ControllerUtil.returnCRUD(mesSfcDataCollectService.update(mesSfcDataCollect));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcDataCollect> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcDataCollect  mesSfcDataCollect = mesSfcDataCollectService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcDataCollect,StringUtils.isEmpty(mesSfcDataCollect)?0:1);
    }

    @GetMapping("/manyOpen")
    public ResponseEntity openService() throws IOException {
        socketService.openService();
        return ControllerUtil.returnCRUD(1);
    }

}
