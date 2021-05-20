package com.fantechs.provider.api.wms.in;

import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Date 2021/1/7 19:28
 */
@FeignClient(name = "ocean-wms-in")
public interface InFeignApi {

    @PostMapping("/pda/mesPackageManager/findList")
    ResponseEntity<List<MesPackageManagerDTO>> list(@RequestBody(required = false) SearchMesPackageManagerListDTO searchMesPackageManagerListDTO);

    @ApiOperation(value = "成品入库新增", notes = "成品入库新增")
    @PostMapping("/wmsInFinishedProduct/add")
    ResponseEntity<WmsInFinishedProduct> inFinishedProductAdd(@ApiParam(value = "必传：", required = true) @RequestBody @Validated WmsInFinishedProduct wmsInFinishedProduct);

    @PostMapping("/wmsInAsnOrder/writeQty")
    ResponseEntity writeQty(@RequestBody WmsInAsnOrderDet wmsInAsnOrderDet);

    @ApiOperation("列表")
    @PostMapping("/wmsInAsnOrder/findList")
    ResponseEntity<List<WmsInAsnOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInAsnOrder searchWmsInAsnOrder);

    @ApiOperation("列表")
    @PostMapping("/wmsInAsnOrderDet/findList")
    ResponseEntity<List<WmsInAsnOrderDetDto>> findDetList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInAsnOrderDet searchWmsInAsnOrderDet);

    @ApiOperation("栈板作业生成完工入库单")
    @PostMapping("/wmsInAsnOrder/palletAutoAsnOrder")
    ResponseEntity palletAutoAsnOrder(@RequestBody PalletAutoAsnDto palletAutoAsnDto);
}
