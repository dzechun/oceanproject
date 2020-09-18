package ${basePackage}.service;

import ${basePackage}.model.${modelNameUpperCamel};
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by ${author} on ${date}.
 */

public interface ${modelNameUpperCamel}Service extends IService<${modelNameUpperCamel}> {

     List<${modelNameUpperCamel}> findList(Map<String,Object> map);
}
