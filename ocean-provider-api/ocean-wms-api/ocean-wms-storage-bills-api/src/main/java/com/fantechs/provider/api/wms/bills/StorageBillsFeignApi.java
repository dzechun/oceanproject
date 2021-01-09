package com.fantechs.provider.api.wms.bills;

import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.response.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Date 2021/1/7 19:28
 */
@FeignClient(name = "ocean-wms-storage-bills")
public interface StorageBillsFeignApi {

    @PostMapping("/pda/mesPackageManager/list")
    ResponseEntity<List<MesPackageManagerDTO>> list(@RequestBody(required = false) SearchMesPackageManagerListDTO searchMesPackageManagerListDTO);
}
