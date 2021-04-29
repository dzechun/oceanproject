package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseProductProcessReMDto;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.support.IService;
import com.fantechs.provider.base.vo.BaseProductProcessReMVo;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/28.
 */

public interface BaseProductProcessReMService extends IService<BaseProductProcessReM> {
    List<BaseProductProcessReMVo> findList(Map<String, Object> map);

    List<BaseProductProcessReMVo> findHtList(Map<String, Object> map);
}
