package com.fantechs.provider.guest.meidi.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.leisai.LeisaiProcessInputOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.HTTPUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.guest.meidi.entity.MeterialPrepation;
import com.fantechs.provider.guest.meidi.entity.MeterialPrepationRequest;
import com.fantechs.provider.guest.meidi.service.MeterialPrepationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */
@Service
public class MeterialPrepationServiceImpl extends BaseService<LeisaiProcessInputOrder> implements MeterialPrepationService {

    @Resource
    private SecurityFeignApi securityFeignApi;


    private String completedUrl= "http://IP:Port/FCS/PDA/MeterialPrepationCompleted";
    private String canceUrl= "http://IP:Port/FCS/PDA/MeterialPrepationCance";

    @Override
    public int send(MeterialPrepation meterialPrepation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(meterialPrepation.getStorageCode()) || StringUtils.isEmpty())
            throw  new BizErrorException("库位编码或接口请求类型不能为空");
        MeterialPrepationRequest req = new MeterialPrepationRequest();
    //    req.setRequestCode();
        String data = "{\"locationCode\":\""+meterialPrepation.getStorageCode()+"\",\""+user.getNickName()+"\":\"zhang\"}";
        req.setRequestData(data);
        if("1".equals(meterialPrepation.getType())){
            req.setRequsetName("MeterialPreparationCompleted");
            String result = HTTPUtils.postMap(completedUrl, new HashMap<>(), ControllerUtil.dynamicConditionByEntity(req));
            if(StringUtils.isNotEmpty(result)){
                Map<String,Object> map = JsonUtils.jsonToMap(result);

            }
        }else if("2".equals(meterialPrepation.getType())){
            req.setRequsetName("MeterialPreparationCancel");
        }else{
            throw  new BizErrorException("请求接口类型错误");
        }



        return 0;
    }
}
