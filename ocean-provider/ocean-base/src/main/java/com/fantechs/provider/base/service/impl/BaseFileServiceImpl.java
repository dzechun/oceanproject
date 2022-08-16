package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseFileMapper;
import com.fantechs.provider.base.service.BaseFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/13.
 */
@Service
public class BaseFileServiceImpl extends BaseService<BaseFile> implements BaseFileService {

    @Resource
    private BaseFileMapper baseFileMapper;

    @Override
    public List<BaseFile> findList(Map<String, Object> map) {
        return baseFileMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchAddFile(List<BaseFile> list) {

        Example example = new Example(BaseFile.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceId",list.get(0).getRelevanceId())
                .andEqualTo("relevanceTableName",list.get(0).getRelevanceTableName());
        baseFileMapper.deleteByExample(example);

        return baseFileMapper.insertList(list);
    }


}
