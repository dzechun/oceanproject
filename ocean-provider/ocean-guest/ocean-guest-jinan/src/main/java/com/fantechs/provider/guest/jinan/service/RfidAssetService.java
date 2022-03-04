package com.fantechs.provider.guest.jinan.service;

import com.fantechs.common.base.general.dto.jinan.Import.RfidAssetImport;
import com.fantechs.common.base.general.entity.jinan.RfidAsset;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface RfidAssetService extends IService<RfidAsset> {
    List<RfidAsset> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<RfidAssetImport> rfidAssetImports);
}