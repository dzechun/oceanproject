package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.SmtProcessCategory;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtProcessCategoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProcessMapper;
import com.fantechs.provider.imes.basic.mapper.SmtRouteProcessMapper;
import com.fantechs.provider.imes.basic.service.SmtRouteProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class SmtRouteProcessServiceImpl extends BaseService<SmtRouteProcess> implements SmtRouteProcessService {

        @Resource
        private SmtRouteProcessMapper smtRouteProcessMapper;
        @Resource
        private SmtProcessMapper smtProcessMapper;
        @Resource
        private SmtProcessCategoryMapper smtProcessCategoryMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int configureRout(List<SmtRouteProcess> list) {
            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("没有配置工艺路线，请去配置");
            }
            Long routeId = list.get(0).getRouteId();

            // 删除上一次配置的工艺路线
            Example example = new Example(SmtRouteProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeId",routeId);
            smtRouteProcessMapper.deleteByExample(example);

            //获取维修工序的对象
            example =  new Example(SmtProcessCategory.class);
            criteria = example.createCriteria();
            criteria.andEqualTo("processCategoryCode","repair");
            SmtProcessCategory smtProcessCategory=  smtProcessCategoryMapper.selectOneByExample(example);

            //已检验工序
            List<Long> processIds=new ArrayList<>();
            for(SmtRouteProcess smtRouteProcess :list){
                //当前工序
                SmtProcess smtProcess = smtProcessMapper.selectByPrimaryKey(smtRouteProcess.getProcessId());
                //是否维修工序
                if(smtProcess.getProcessCategoryId()==smtProcessCategory.getProcessCategoryId()){
                    if(!processIds.contains(smtRouteProcess.getNextProcessId())){
                        throw new BizErrorException("维修工序返回工序有误");
                    }
                }else {
                    processIds.add(smtRouteProcess.getProcessId());
                }
            }
            return smtRouteProcessMapper.insertList(list);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int configureProcess(Map<String, Object> map) {
            return smtRouteProcessMapper.configureProcess(map);
        }

        @Override
        public List<SmtRouteProcess> findConfigureRout(Long routeId) {
            return smtRouteProcessMapper.findList(routeId);
        }
}
