package com.fantechs.provider.guest.wanbao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.basic.BasePlatform;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlatform;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.guest.wanbao.dto.WanbaoReadHeadDto;
import com.fantechs.provider.guest.wanbao.mapper.WanbaoReadHeadMapper;
import com.fantechs.provider.guest.wanbao.model.WanbaoReadHead;
import com.fantechs.provider.guest.wanbao.model.WanbaoStacking;
import com.fantechs.provider.guest.wanbao.service.WanbaoReadHeadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class WanbaoReadHeadServiceImpl extends BaseService<WanbaoReadHead> implements WanbaoReadHeadService {

    @Resource
    private WanbaoReadHeadMapper wanbaoReadHeadMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<WanbaoReadHeadDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", sysUser.getOrganizationId());
        return wanbaoReadHeadMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WanbaoReadHeadDto> list) {
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        // 所有月台
        List<BasePlatform> platforms = baseFeignApi.findPlatformAll(new SearchBasePlatform()).getData();
        // 已有读头数据
        List<WanbaoReadHeadDto> headDtos = this.findList(new HashMap<>());
        List<WanbaoReadHead> entitys = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            // 判断平台
            if (StringUtils.isEmpty(list.get(i).getPlatformName(), list.get(i).getReadHeadIp(), list.get(i).getReadHeadName())){
                fail.add(i + 2);
                continue;
            }
            for (BasePlatform platform : platforms){
                if (platform.getPlatformName().equals(list.get(i).getPlatformName())){
                    list.get(i).setPlatformId(platform.getPlatformId());
                    break;
                }
            }
            if (list.get(i).getPlatformId() == null){
                fail.add(i + 2);
                continue;
            }

            // 判断已有数据中是否存在已有读头IP
            boolean flag = false;
            for (WanbaoReadHeadDto dto : headDtos){
                if (dto.getReadHeadIp().equals(list.get(i).getReadHeadIp())){
                    flag = true;
                    break;
                }
            }
            if(flag){
                fail.add(i + 2);
                continue;
            }
            flag = false;
            for (WanbaoReadHead readHead : entitys){
                if (readHead.getReadHeadIp().equals(list.get(i).getReadHeadIp())){
                    flag = true;
                    break;
                }
            }
            if(flag){
                fail.add(i + 2);
                continue;
            }

            SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
            WanbaoReadHead head = new WanbaoReadHead();
            BeanUtil.copyProperties(list.get(i), head);
            head.setCreateTime(new Date());
            head.setCreateUserId(sysUser.getUserId());
            head.setModifiedTime(new Date());
            head.setModifiedUserId(sysUser.getUserId());
            head.setOrgId(sysUser.getOrganizationId());
            head.setStatus((byte) 1);
            entitys.add(head);
        }
        if (!entitys.isEmpty()){
            this.batchSave(entitys);
        }
        resultMap.put("操作成功总数", entitys.size());
        resultMap.put("操作失败行数", fail);
        return resultMap;
    }
}
