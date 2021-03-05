package com.fantechs.security.config;

import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysSpecItemService;
import com.fantechs.security.service.impl.SysSpecItemServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName MyCommandLineRunner
 * @Description TODO
 * @Author jbb
 * @Date 2021/3/5 11:31
 * @Version v1.0
 */
@Order(2)
@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Resource
    private SysSpecItemService sysSpecItemService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void run(String... args){
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("specialWord");
        List<SysSpecItem> list = sysSpecItemService.findList(searchSysSpecItem);
        if (StringUtils.isNotEmpty(list)){
            SysSpecItem sysSpecItem = list.get(0);
            redisUtil.set("specialWord", sysSpecItem);
        }
    }
}
