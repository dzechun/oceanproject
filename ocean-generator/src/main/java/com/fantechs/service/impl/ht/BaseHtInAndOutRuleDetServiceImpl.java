package com.fantechs.service.impl.ht;

import com.fantechs.dao.mapper.BaseHtInAndOutRuleDetMapper;
import com.fantechs.model.BaseHtInAndOutRuleDet;
import com.fantechs.service.BaseHtInAndOutRuleDetService;
import org.springframework.stereotype.Service;

/**
 *
 * Created by mr.lei on 2021/12/30.
 */
@Service
public class BaseHtInAndOutRuleDetServiceImpl extends BaseService<BaseHtInAndOutRuleDet> implements BaseHtInAndOutRuleDetService {

    @Resource
    private BaseHtInAndOutRuleDetMapper baseHtInAndOutRuleDetMapper;

    @Override
    public List<BaseHtInAndOutRuleDetDto> findList(Map<String, Object> map) {
        return baseHtInAndOutRuleDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseHtInAndOutRuleDet> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
