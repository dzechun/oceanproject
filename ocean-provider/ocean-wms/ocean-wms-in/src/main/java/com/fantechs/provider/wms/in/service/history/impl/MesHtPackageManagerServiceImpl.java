package com.fantechs.provider.wms.in.service.history.impl;

import com.fantechs.common.base.entity.basic.history.MesHtPackageManager;
import com.fantechs.common.base.dto.basic.history.MesHtPackageManagerDTO;
import com.fantechs.provider.wms.in.service.history.MesHtPackageManagerService;
import com.fantechs.provider.wms.in.mapper.MesHtPackageManagerMapper;
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
 * @Date: 2021年1月9日 14:04
 * @Description: 包装管理履历表接口实现
 * @Version: 1.0
 */
@Service
public class MesHtPackageManagerServiceImpl extends BaseService<MesHtPackageManager>  implements MesHtPackageManagerService{

     @Resource
     private MesHtPackageManagerMapper mesHtPackageManagerMapper;

    @Override
    public List<MesHtPackageManager> selectAll(Map<String,Object> map) {
        Example example = new Example(MesHtPackageManager.class);
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
        return mesHtPackageManagerMapper.selectByExample(example);
    }

    @Override
    public MesHtPackageManager selectByKey(Object id) {
        MesHtPackageManager mesHtPackageManager = mesHtPackageManagerMapper.selectByPrimaryKey(id);
        if(mesHtPackageManager != null && (mesHtPackageManager.getIsDelete() != null && mesHtPackageManager.getIsDelete() == 0)){
        mesHtPackageManager = null;
        }
        return mesHtPackageManager;
    }

    @Override
    public int save(MesHtPackageManager mesHtPackageManager) {
        mesHtPackageManager.setCreateUserId(null);
        mesHtPackageManager.setIsDelete((byte)1);
        return mesHtPackageManagerMapper.insertSelective(mesHtPackageManager);
    }

    @Override
    public int deleteByKey(Object id) {
        MesHtPackageManager mesHtPackageManager = new MesHtPackageManager();
        mesHtPackageManager.setHtPackageManagerId((long)id);
        mesHtPackageManager.setIsDelete((byte)0);
        return update(mesHtPackageManager);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesHtPackageManager> mesHtPackageManagers = selectAll(map);
        if (StringUtils.isNotEmpty(mesHtPackageManagers)) {
            for (MesHtPackageManager mesHtPackageManager : mesHtPackageManagers) {
                if(deleteByKey(mesHtPackageManager.getHtPackageManagerId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesHtPackageManager mesHtPackageManager) {
        mesHtPackageManager.setModifiedUserId(null);
        return mesHtPackageManagerMapper.updateByPrimaryKeySelective(mesHtPackageManager);
    }

   @Override
   public List<MesHtPackageManagerDTO> selectFilterAll(Map<String, Object> map) {
       return mesHtPackageManagerMapper.selectFilterAll(map);
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

    /**
     * 记录操作历史
     * @param id
     * @param operation
     */
    private void recordHistory(Long id,String operation){
        /*HT ht= new HT();
        ht.setOperation(operation);
        MesSchedule mesSchedule = selectByKey(id);
        if (StringUtils.isEmpty(mesSchedule)){
            return;
        }
        BeanUtils.autoFillEqFields(mesSchedule,ht);
        htService.save(ht);*/
    }
}
