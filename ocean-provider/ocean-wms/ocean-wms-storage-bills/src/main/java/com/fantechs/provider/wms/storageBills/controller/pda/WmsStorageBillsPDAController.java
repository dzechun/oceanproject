package com.fantechs.provider.wms.storageBills.controller.pda;

import com.fantechs.common.base.dto.storage.SaveBilssDet;
import com.fantechs.common.base.dto.storage.SearchWmsStorageBillsListDTO;
import com.fantechs.common.base.dto.storage.WmsStorageBillsDTO;
import com.fantechs.common.base.entity.storage.WmsStorageBills;
import com.fantechs.common.base.exception.SQLExecuteException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.storageBills.service.WmsStorageBillsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月17日 14:52
 * @Description: 仓库清单表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "仓库清单表管理",basePath = "pda/wmsStorageBills")
@RequestMapping("pda/wmsStorageBills")
@Slf4j
public class WmsStorageBillsPDAController {

    @Resource
    private WmsStorageBillsService wmsStorageBillsService;

    @ApiOperation("查询仓库清单表列表")
    @PostMapping("list")
    public ResponseEntity<List<WmsStorageBillsDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchWmsStorageBillsListDTO searchWmsStorageBillsListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize
    ){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsStorageBillsDTO> wmsStorageBillsList = wmsStorageBillsService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchWmsStorageBillsListDTO));
        return ControllerUtil.returnDataSuccess(wmsStorageBillsList,(int)page.getTotal());
    }

    @ApiOperation("修改仓库清单表数据")
    @PostMapping("saveBilssDet")
    public ResponseEntity<WmsStorageBills> saveBilssDet(
            @ApiParam(value = "保存对象",required = true)@RequestBody @Validated SaveBilssDet saveBilssDet) throws SQLExecuteException {
        WmsStorageBills wmsStorageBills = wmsStorageBillsService.saveBilssDet(saveBilssDet);
        return ControllerUtil.returnDataSuccess(wmsStorageBills,StringUtils.isEmpty(wmsStorageBills)?0:1);
    }
}
