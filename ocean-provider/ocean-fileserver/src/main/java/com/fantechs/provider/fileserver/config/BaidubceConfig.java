package com.fantechs.provider.fileserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/30.
 */
@Component
@ConfigurationProperties(prefix = "baidubce")
@Data
public class BaidubceConfig implements Serializable {
    private static final long serialVersionUID = 7854711895903722264L;
    private    String grant_type;

    private    String client_id;

    private    String client_secret;

    private    String oauth_url;

    private    String rimage_process_url;
}
