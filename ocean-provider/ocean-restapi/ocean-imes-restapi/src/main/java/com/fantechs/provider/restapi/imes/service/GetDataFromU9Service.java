package com.fantechs.provider.restapi.imes.service;

public interface GetDataFromU9Service {

    //同步U9物料信息
    int updateMaterial() throws Exception;

    //同步U9仓库信息
    int updateWarehouse() throws Exception;
}
