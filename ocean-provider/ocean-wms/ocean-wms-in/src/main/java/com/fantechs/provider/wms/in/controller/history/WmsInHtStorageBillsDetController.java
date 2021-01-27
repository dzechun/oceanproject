package com.fantechs.provider.wms.in.controller.history;

import com.fantechs.common.base.dto.basic.history.WmsInHtStorageBillsDetDTO;
import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBillsDet;
import com.fantechs.provider.wms.in.service.history.WmsInHtStorageBillsDetService;
import com.fantechs.common.base.general.dto.mes.pm.history.SearchWmsInHtStorageBillsDetListDTO;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Auther: bingo.ren
 * @Date: 2020年12月25日 14:01
 * @Description: 履历仓库清单详情表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "履历仓库清单详情表管理",basePath = "wmsHtStorageBillsDet")
@RequestMapping("wmsHtStorageBillsDet")
@Slf4j
public class WmsInHtStorageBillsDetController {

    @Resource
    private WmsInHtStorageBillsDetService wmsHtStorageBillsDetService;

    @ApiOperation("查询履历仓库清单详情表列表")
    @PostMapping("findList")
    public ResponseEntity<List<WmsInHtStorageBillsDetDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchWmsInHtStorageBillsDetListDTO searchWmsHtStorageBillsDetListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize
    ){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsInHtStorageBillsDetDTO> wmsInHtStorageBillsDetDTOList = wmsHtStorageBillsDetService.selectFilterAll(searchWmsHtStorageBillsDetListDTO.getStorageBillsId());
        return ControllerUtil.returnDataSuccess(wmsInHtStorageBillsDetDTOList,(int)page.getTotal());
    }
}
