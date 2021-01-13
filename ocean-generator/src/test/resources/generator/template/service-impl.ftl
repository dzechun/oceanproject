package ${basePackage}.service.impl.${sign};

import ${basePackage}.dao.mapper.${modelNameUpperCamel}Mapper;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ${author} on ${date}.
 */
@Service
public class ${modelNameUpperCamel}ServiceImpl  extends BaseService<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Service {

    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

    @Override
    public List< ${modelNameUpperCamel}Dto> findList(Map<String, Object> map) {
        return ${modelNameLowerCamel}Mapper.findList(map);
    }
}
