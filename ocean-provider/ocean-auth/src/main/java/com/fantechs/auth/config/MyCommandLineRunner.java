package com.fantechs.auth.config;

import com.fantechs.auth.service.SysMenuInfoService;
import com.fantechs.auth.service.SysSpecItemService;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
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
    private SysMenuInfoService sysMenuInfoService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void run(String... args){
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("specialWord");
        searchSysSpecItem.setIfHotData((byte) 0);
        List<SysSpecItem> list = sysSpecItemService.findList(searchSysSpecItem);
        if (StringUtils.isNotEmpty(list)){
            SysSpecItem sysSpecItem = list.get(0);
            redisUtil.set("specialWord", sysSpecItem);
        }
        searchSysSpecItem.setSpecCode(null);
        list = sysSpecItemService.findList(searchSysSpecItem);
        if (StringUtils.isNotEmpty(list)) {
            redisUtil.set("specItemList", JsonUtils.objectToJson(list));
        }
//        List<SysMenuInListDTO> menuList = sysMenuInfoService.findMenuList(ControllerUtil.dynamicCondition(
//                "parentId", "0",
//                "menuType", 2
//        ), null);
//        if (StringUtils.isNotEmpty(menuList)) {
//            redisUtil.set("menuList", JsonUtils.objectToJson(menuList));
//        }
    }
}
