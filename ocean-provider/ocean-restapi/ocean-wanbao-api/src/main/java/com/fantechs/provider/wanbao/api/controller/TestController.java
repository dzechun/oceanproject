package com.fantechs.provider.wanbao.api.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fantechs.provider.wanbao.api.entity.MiddleProduct;
import com.fantechs.provider.wanbao.api.mapper.MiddleProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class TestController {


    @Resource
    private MiddleProductMapper middleProductMapper;

    @Test
    public void test() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        // 执行查询
        // DynamicDataSourceHolder.putDataSouce("thirdary");
        List<MiddleProduct> barcodeDatas = middleProductMapper.findBarcodeData(map);
        log.info("------barcodeDatas----------"+barcodeDatas);
      //   DynamicDataSourceHolder.removeDataSource();

    }

}
