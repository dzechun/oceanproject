package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.SapPushMessageApi;

import java.text.ParseException;

public interface SapPushMessageApiService {

    int sendPushMessage(SapPushMessageApi sapPushMessageApi) throws ParseException;

}
