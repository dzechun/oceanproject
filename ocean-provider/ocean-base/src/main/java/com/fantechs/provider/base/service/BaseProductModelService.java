package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.dto.basic.imports.BaseProductModelImport;
import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface BaseProductModelService extends IService<BaseProductModel> {

    List<BaseProductModel> selectProductModels(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseProductModelImport> baseProductModelImports);

    /**
     * 保存并返回对象
     * @param productModel
     * @return
     */
    BaseProductModel addForReturn(BaseProductModel productModel);
}
