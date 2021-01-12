package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtAndinStorageQuarantine;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtAndinStorageQuarantineMapper;
import com.fantechs.provider.qms.service.QmsHtAndinStorageQuarantineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class QmsHtAndinStorageQuarantineServiceImpl extends BaseService<QmsHtAndinStorageQuarantine> implements QmsHtAndinStorageQuarantineService {

     @Resource
     private QmsHtAndinStorageQuarantineMapper qmsHtAndinStorageQuarantineMapper;

     @Override
     public List<QmsHtAndinStorageQuarantine> findHtList(Map<String, Object> map) {
          return qmsHtAndinStorageQuarantineMapper.findHtList(map);
     }
}
