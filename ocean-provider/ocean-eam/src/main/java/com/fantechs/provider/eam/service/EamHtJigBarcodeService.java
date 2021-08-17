package com.fantechs.provider.eam.service;

        import com.fantechs.common.base.general.entity.eam.history.EamHtJigBarcode;
        import com.fantechs.common.base.support.IService;

        import java.util.List;
        import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamHtJigBarcodeService extends IService<EamHtJigBarcode> {
    List<EamHtJigBarcode> findHtList(Map<String, Object> map);
}
