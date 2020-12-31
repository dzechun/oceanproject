package com.fantechs.provider.wms.storageBills.service.history.impl;

import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBills;
import com.fantechs.common.base.dto.apply.history.WmsInHtStorageBillsDTO;
import com.fantechs.provider.wms.storageBills.service.history.WmsInHtStorageBillsService;
import com.fantechs.provider.wms.storageBills.mapper.history.WmsInHtStorageBillsMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月25日 9:35
 * @Description: 履历仓库清单表接口实现
 * @Version: 1.0
 */
@Service
public class WmsInHtStorageBillsServiceImpl extends BaseService<WmsInHtStorageBills>  implements WmsInHtStorageBillsService {

     @Resource
     private WmsInHtStorageBillsMapper wmsHtStorageBillsMapper;

    @Override
    public List<WmsInHtStorageBills> selectAll(Map<String,Object> map) {
        Example example = new Example(WmsInHtStorageBills.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    switch (k){
                        case "Name":
                            criteria.andLike(k,"%"+v+"%");
                            break;
                        default :
                            criteria.andEqualTo(k,v);
                            break;
                    }
                }
            });
        }
        return wmsHtStorageBillsMapper.selectByExample(example);
    }

    @Override
    public List<WmsInHtStorageBills> selectLikeAll(Map<String,Object> map) {
        Example example = new Example(WmsInHtStorageBills.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    criteria.orLike(k,"%"+v+"%");
                }
            });
        }
        return wmsHtStorageBillsMapper.selectByExample(example);
    }

    @Override
    public WmsInHtStorageBills selectByKey(Object id) {
        WmsInHtStorageBills wmsHtStorageBills = wmsHtStorageBillsMapper.selectByPrimaryKey(id);
        if(wmsHtStorageBills != null && (wmsHtStorageBills.getIsDelete() != null && wmsHtStorageBills.getIsDelete() == 0)){
        wmsHtStorageBills = null;
        }
        return wmsHtStorageBills;
    }

    @Override
    public WmsInHtStorageBills selectByMap(Map<String,Object> map) {
        List<WmsInHtStorageBills> wmsHtStorageBills = selectAll(map);
        if(StringUtils.isEmpty(wmsHtStorageBills)){
            return null;
        }
        if(wmsHtStorageBills.size()>1){
            return null;
        }
        return wmsHtStorageBills.get(0);
    }

    @Override
    public int save(WmsInHtStorageBills wmsHtStorageBills) {
        wmsHtStorageBills.setCreateUserId(null);
        wmsHtStorageBills.setIsDelete((byte)1);
        return wmsHtStorageBillsMapper.insertUseGeneratedKeys(wmsHtStorageBills);
    }

    @Override
    public int deleteByKey(Object id) {
        WmsInHtStorageBills wmsHtStorageBills = new WmsInHtStorageBills();
        wmsHtStorageBills.setHtStorageBillsId((long)id);
        wmsHtStorageBills.setIsDelete((byte)0);
        return update(wmsHtStorageBills);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<WmsInHtStorageBills> wmsHtStorageBillss = selectAll(map);
        if (StringUtils.isNotEmpty(wmsHtStorageBillss)) {
            for (WmsInHtStorageBills wmsHtStorageBills : wmsHtStorageBillss) {
                if(deleteByKey(wmsHtStorageBills.getHtStorageBillsId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(WmsInHtStorageBills wmsHtStorageBills) {
        wmsHtStorageBills.setModifiedUserId(null);
        return wmsHtStorageBillsMapper.updateByPrimaryKeySelective(wmsHtStorageBills);
    }

    @Override
    public String selectUserName(Object id) {
        return wmsHtStorageBillsMapper.selectUserName(id);
    }

     @Override
     public List<WmsInHtStorageBillsDTO> selectFilterAll(Map<String, Object> map) {
       return wmsHtStorageBillsMapper.selectFilterAll(map);
     }


    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
