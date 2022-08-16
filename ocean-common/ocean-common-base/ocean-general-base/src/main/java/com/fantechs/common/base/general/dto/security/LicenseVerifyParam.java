package com.fantechs.common.base.general.dto.security;

import lombok.Data;

/**
 * License校验类需要的参数
 */
@Data
public class LicenseVerifyParam {

    /**
     * 证书subject
     */
    private String subject;

    /**
     * 公钥别称
     */
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    private String publicKeysStorePath;

    public LicenseVerifyParam() {

    }

    public LicenseVerifyParam(String subject, String publicAlias, String storePass, String licensePath, String publicKeysStorePath) {
        this.subject = subject;
        this.publicAlias = publicAlias;
        this.storePass = storePass;
        this.licensePath = licensePath;
        this.publicKeysStorePath = publicKeysStorePath;
    }

    @Override
    public String toString() {
        return "LicenseVerifyParam{" +
                "subject='" + subject + '\'' +
                ", publicAlias='" + publicAlias + '\'' +
                ", storePass='" + storePass + '\'' +
                ", licensePath='" + licensePath + '\'' +
                ", publicKeysStorePath='" + publicKeysStorePath + '\'' +
                '}';
    }
}
