package com.fantechs.service.impl.tab;

import com.fantechs.dao.mapper.BaseTabMapper;
import com.fantechs.model.BaseTab;
import com.fantechs.service.BaseTabService;
import org.springframework.stereotype.Service;

/**
 *
 * Created by leifengzhi on 2021/01/12.
 */
@Service
public class BaseTabServiceImpl  extends BaseService<BaseTab> implements BaseTabService {

         @Resource
         private BaseTabMapper baseTabMapper;
}
