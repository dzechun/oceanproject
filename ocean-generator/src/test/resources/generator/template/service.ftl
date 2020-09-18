package ${basePackage}.service;

import ${basePackage}.model.${modelNameUpperCamel};
import com.fantechs.common.base.support.IService;

/**
 *
 * Created by ${author} on ${date}.
 */

public interface ${modelNameUpperCamel}Service extends IService<${modelNameUpperCamel}> {

     List<${modelNameUpperCamel}> findList(Map<String,Object> map);
}
