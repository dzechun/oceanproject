package com.fantechs.provider.imes.basic.service.impl;



import com.fantechs.common.base.entity.basic.history.SmtHtSpecItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtSpecItemMapper;
import com.fantechs.provider.imes.basic.service.SmtHtSpecItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SmtHtSpecItemServiceImpl extends BaseService<SmtHtSpecItem> implements SmtHtSpecItemService {
    @Resource
    private SmtHtSpecItemMapper smtHtSpecItemMapper;

    @Override
    public List<SmtHtSpecItem> findHtSpecItemList(Map<String, Object> map) {
       /* Example example = new Example(SmtHtSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(map)) {
            map.forEach((k, v) -> {
                if (!StringUtils.isEmpty(v)) {
                    switch (k) {
                        default:
                            criteria.andLike(k,"%" + v + "%");
                            break;
                    }
                }
            });
        }*/
        return smtHtSpecItemMapper.findHtSpecItemList(map);
    }
}
