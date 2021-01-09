package com.fantechs.provider.imes.apply.service.ht.impl;

import com.fantechs.common.base.entity.apply.history.MesHtOrderMaterial;
import com.fantechs.common.base.dto.apply.history.MesHtOrderMaterialDTO;
import com.fantechs.provider.imes.apply.service.ht.MesHtOrderMaterialService;
import com.fantechs.provider.imes.apply.mapper.MesHtOrderMaterialMapper;
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
 * @Date: 2021年1月9日 11:51
 * @Description: 销售订单与物料历史接口实现
 * @Version: 1.0
 */
@Service
public class MesHtOrderMaterialServiceImpl extends BaseService<MesHtOrderMaterial>  implements MesHtOrderMaterialService{

     @Resource
     private MesHtOrderMaterialMapper mesHtOrderMaterialMapper;

    @Override
    public List<MesHtOrderMaterial> selectAll(Map<String,Object> map) {
        Example example = new Example(MesHtOrderMaterial.class);
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
        return mesHtOrderMaterialMapper.selectByExample(example);
    }

    @Override
    public MesHtOrderMaterial selectByKey(Object id) {
        MesHtOrderMaterial mesHtOrderMaterial = mesHtOrderMaterialMapper.selectByPrimaryKey(id);
        if(mesHtOrderMaterial != null && (mesHtOrderMaterial.getIsDelete() != null && mesHtOrderMaterial.getIsDelete() == 0)){
        mesHtOrderMaterial = null;
        }
        return mesHtOrderMaterial;
    }

    @Override
    public int save(MesHtOrderMaterial mesHtOrderMaterial) {
        mesHtOrderMaterial.setCreateUserId(null);
        mesHtOrderMaterial.setIsDelete((byte)1);
        return mesHtOrderMaterialMapper.insertSelective(mesHtOrderMaterial);
    }

    @Override
    public int deleteByKey(Object id) {
        MesHtOrderMaterial mesHtOrderMaterial = new MesHtOrderMaterial();
        mesHtOrderMaterial.setHtOrderMaterialId((long)id);
        mesHtOrderMaterial.setIsDelete((byte)0);
        return update(mesHtOrderMaterial);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesHtOrderMaterial> mesHtOrderMaterials = selectAll(map);
        if (StringUtils.isNotEmpty(mesHtOrderMaterials)) {
            for (MesHtOrderMaterial mesHtOrderMaterial : mesHtOrderMaterials) {
                if(deleteByKey(mesHtOrderMaterial.getHtOrderMaterialId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesHtOrderMaterial mesHtOrderMaterial) {
        mesHtOrderMaterial.setModifiedUserId(null);
        return mesHtOrderMaterialMapper.updateByPrimaryKeySelective(mesHtOrderMaterial);
    }

   @Override
   public List<MesHtOrderMaterialDTO> selectFilterAll(Map<String, Object> map) {
       return mesHtOrderMaterialMapper.selectFilterAll(map);
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
