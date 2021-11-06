package com.fantechs.provider.leisai.api.service;

import com.fantechs.common.base.general.entity.leisai.LeisaiWmsCarton;

public interface SyncWmsDataService {

    /**
     * 雷赛-包箱主表信息
     * @return
     */
    void syncCartonData(LeisaiWmsCarton LeisaiWmsCarton);

    /**
     * 雷赛-包箱明细表信息
     * @return
     */
    void syncCartonDetData();
}
