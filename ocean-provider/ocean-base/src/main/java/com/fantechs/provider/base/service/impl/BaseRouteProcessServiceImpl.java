package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseProcessCategory;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseProcessCategoryMapper;
import com.fantechs.provider.base.mapper.BaseProcessMapper;
import com.fantechs.provider.base.mapper.BaseRouteProcessMapper;
import com.fantechs.provider.base.service.BaseRouteProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class BaseRouteProcessServiceImpl extends BaseService<BaseRouteProcess> implements BaseRouteProcessService {

        @Resource
        private BaseRouteProcessMapper baseRouteProcessMapper;
        @Resource
        private BaseProcessMapper baseProcessMapper;
        @Resource
        private BaseProcessCategoryMapper baseProcessCategoryMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int configureRout(List<BaseRouteProcess> list) {
            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("没有配置工艺路线，请去配置");
            }
            Long routeId = list.get(0).getRouteId();

            // 删除上一次配置的工艺路线
            Example example = new Example(BaseRouteProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeId",routeId);
            baseRouteProcessMapper.deleteByExample(example);

            //获取维修工序的对象
            example =  new Example(BaseProcessCategory.class);
            criteria = example.createCriteria();
            criteria.andEqualTo("processCategoryCode","repair");
            BaseProcessCategory baseProcessCategory =  baseProcessCategoryMapper.selectOneByExample(example);

            //已检验工序
            List<Long> processIds=new ArrayList<>();
            for(BaseRouteProcess baseRouteProcess :list){
                //当前工序
                BaseProcess baseProcess = baseProcessMapper.selectByPrimaryKey(baseRouteProcess.getProcessId());
                //是否维修工序
                if(baseProcess.getProcessCategoryId()== baseProcessCategory.getProcessCategoryId()){
                    if(!processIds.contains(baseRouteProcess.getNextProcessId())){
                        throw new BizErrorException("维修工序返回工序有误");
                    }
                }else {
                    processIds.add(baseRouteProcess.getProcessId());
                }
            }
            return baseRouteProcessMapper.insertList(list);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int configureProcess(Map<String, Object> map) {
            return baseRouteProcessMapper.configureProcess(map);
        }

        @Override
        public List<BaseRouteProcess> findConfigureRout(Long routeId) {
            Map<String, Object> map = new HashMap<>();
            map.put("routeId", routeId);
            return baseRouteProcessMapper.findList(map);
        }

    @Override
    public List<BaseRouteProcess> findList(Map<String, Object> map) {
        return baseRouteProcessMapper.findList(map);
    }


}
