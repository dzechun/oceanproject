package com.fantechs.provider.chinafiveringapi.api.service;

import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;

public interface ImportDataService {
    /**
     * 中国五环-合同量单接口
     * @return
     */
    BaseExecuteResultDto getPoDetails(String projectID) throws Exception;

    /**
     * 中国五环-领料单接口
     * @return
     */
    BaseExecuteResultDto getIssueDetails(String projectID) throws Exception;

    /**
     * 中国五环-材料信息接口
     * @return
     */
    BaseExecuteResultDto getPartNoInfo(String projectID) throws Exception;

    /**
     * 中国五环-货架信息接口 库位
     * @return
     */
    BaseExecuteResultDto getShelvesNo(String projectID) throws Exception;

    /**
     * 中国五环-施工单位信息接口 客户
     * @return
     */
    BaseExecuteResultDto getSubcontractor(String projectID) throws Exception;

    /**
     * 中国五环-供应商信息接口
     * @return
     */
    BaseExecuteResultDto getVendor() throws Exception;

    /**
     * 中国五环-请购单信息接口
     * @return
     */
    BaseExecuteResultDto getReqDetails(String projectID) throws Exception;

}
