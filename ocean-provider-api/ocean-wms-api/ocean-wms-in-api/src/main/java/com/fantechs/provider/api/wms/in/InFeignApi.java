package com.fantechs.provider.api.wms.in;

import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
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

    @PostMapping("/pda/mesPackageManager/list")
    ResponseEntity<List<MesPackageManagerDTO>> list(@RequestBody(required = false) SearchMesPackageManagerListDTO searchMesPackageManagerListDTO);

    @ApiOperation(value = "成品入库新增", notes = "成品入库新增")
    @PostMapping("/wmsInFinishedProduct/add")
    ResponseEntity inFinishedProductAdd(@ApiParam(value = "必传：", required = true) @RequestBody @Validated WmsInFinishedProduct wmsInFinishedProduct);
}
