package ${basePackage}.service.${sign};

import ${basePackage}.model.${modelNameUpperCamel};
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by ${author} on ${date}.
 */

public interface ${modelNameUpperCamel}Service extends IService<${modelNameUpperCamel}> {
    List<${modelNameUpperCamel}Dto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<${modelNameUpperCamel}> list);
}
