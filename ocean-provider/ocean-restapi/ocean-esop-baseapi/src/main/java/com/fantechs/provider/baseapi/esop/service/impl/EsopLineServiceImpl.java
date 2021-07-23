package com.fantechs.provider.baseapi.esop.service.impl;

import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.restapi.esop.EsopDept;
import com.fantechs.common.base.general.entity.restapi.esop.EsopLine;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.baseapi.esop.mapper.EsopLineMapper;
import com.fantechs.provider.baseapi.esop.service.EsopLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@Service
public class EsopLineServiceImpl extends BaseService<EsopLine> implements EsopLineService {

    @Resource
    private EsopLineMapper esopLineMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<EsopLine> addLine(Map<String, Object> map) {
        List<EsopLine> list = esopLineMapper.findList(map);
        List<EsopLine> esopLines = new ArrayList<EsopLine>();
        if(StringUtils.isNotEmpty(list)){
            for(EsopLine esopLine :list){
                esopLines.add(getEsopLine(esopLine));
            }
        }
     //   baseFeignApi.batchadd(esopLines);
        return null;
    }

    public EsopLine getEsopLine(EsopLine esopLine){
        EsopLine line = new EsopLine();


        return esopLine;
    }
}
