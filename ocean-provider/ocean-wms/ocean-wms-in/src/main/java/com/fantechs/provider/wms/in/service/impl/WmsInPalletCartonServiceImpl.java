package com.fantechs.provider.wms.in.service.impl;


import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.general.entity.wms.in.WmsInPalletCarton;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.in.mapper.WmsInPalletCartonMapper;
import com.fantechs.provider.wms.in.service.WmsInPalletCartonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class WmsInPalletCartonServiceImpl extends BaseService<WmsInPalletCarton> implements WmsInPalletCartonService {

    @Resource
    private WmsInPalletCartonMapper wmsInPalletCartonMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;

    @Override
    public String checkPallet(String palletCode) {

        SearchSmtStoragePallet smtStoragePallet = new SearchSmtStoragePallet();
        smtStoragePallet.setPalletCode(palletCode);
        smtStoragePallet.setIsDelete((byte)1);
        smtStoragePallet.setIsBinding((byte)1);
        ResponseEntity<List<SmtStoragePalletDto>> smtStoragePallets =  storageInventoryFeignApi.findList(smtStoragePallet);
        if(smtStoragePallets.getData().size() > 0){
            return "false";
        }else{
            return "true";
        }
    }
}
