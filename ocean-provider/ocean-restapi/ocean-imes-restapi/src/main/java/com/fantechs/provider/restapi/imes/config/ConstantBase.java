package com.fantechs.provider.restapi.imes.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "constant-base")
public class ConstantBase {
    /**
     * 一期只做广东纽恩泰的
     */
//    public static String DEFAULT_ORG_ID = "1001802050000491";

    public static final String UPDATE_MO = "UpdateMO";

    public static final String ISSUE_DOC = "IssueDoc";

    public static final String RTNISSUE_DOC = "RtnIssueDoc";

    public static final String TRANSFER_DOC = "TransferDoc";

    public static final Map<String, Integer> mapOrgNumToOrgId = new HashMap<>();

    static {
        mapOrgNumToOrgId.put("N", 1);
        mapOrgNumToOrgId.put("L", 2);
    }

    //******************************************REDIS KEY**********************************************
    /**
     * 产线的最后更新时间
     */
    public static final String API_LASTUPDATE_TIME_PRODUCT_LINE = "API_LASTUPDATE_TIME_PRODUCT_LINE";
    /**
     * 物料最后更新时间
     */
    public static final String API_LASTUPDATE_TIME_MATERIAL = "API_LASTUPDATE_TIME_MATERIAL";
    /**
     * 用户最后更新时间
     */
    public static final String API_LASTUPDATE_TIME_USER = "API_LASTUPDATE_TIME_USER";
    /**
     * 仓库最后更新时间
     */
    public static final String API_LASTUPDATE_TIME_WAREHOUSE = "API_LASTUPDATE_TIME_WAREHOUSE";
    /**
     * 同步QISc储位最后更新时间
     */
    public static final String API_LASTUPDATE_TIME_CW = "API_LASTUPDATE_TIME_CW";

    /**
     *  默认的组织ID
     */
    private String defaultOrgId;

    /**
     *  默认的组织编码
     */
    private String defaultOrgNum;

    /**
     * 默认的组织名称
     */
    private String defaultOrgName;

}
