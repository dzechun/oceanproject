package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.ScanBarCodeOut;
import com.fantechs.common.base.general.dto.wms.inner.WanbaoPlatform;
import com.fantechs.common.base.general.dto.wms.inner.WanbaoPlatformDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2022/06/27.
 */

public interface WanbaoPlatformService extends IService<WanbaoPlatform> {
    List<WanbaoPlatform> findList(Map<String, Object> map);

    /**
     * 读头扫码
     * @param scanBarCodeOut
     * @return
     */
    int doScan(ScanBarCodeOut scanBarCodeOut);

    /**
     * 绑定月台
     * @param wanbaoPlatform
     * @return
     */
    int bindingPlatform(WanbaoPlatform wanbaoPlatform);

    /**
     * 月台提交释放月台
     * @param ids
     * @return
     */
    int submit(String ids);

    List<WanbaoPlatformDetDto> findDetList(Map<String,Object> map);

    /**
     * 删除条码
     * @param platformDetId
     * @return
     */
    int delete(String platformDetId);

    /**
     * 公共机拣货单列表
     * @return
     */
    List<WmsInnerJobOrder> findJobOrderList();
}
