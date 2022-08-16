package com.fantechs.common.base.utils;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectUtils {

	/**
	 * 使用该方法必须满足，同时有三个键 1."c" + A 2. "c" + A + "Type" 3. "c" + A + "Prefix"
	 * 这样的键对，比如serialNumber、cSerialNumberType,而且 1 号键为String 2 号键为int类型。 而且没有冲突的。
	 * 排序HashMap<String,Integer> 字段名一定要为 orderBy 属性名和字段的对比 HashMap<String, String>
	 * propertyColumn;
	 * 
	 * @param object
	 * @param checkMap
	 * @throws Exception
	 */
	public static void setFieldValue(Object object, HashMap<String, String> checkMap) throws Exception {
		// 先获取来源对象的值
		List<String> stringList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		List<String> typeList = new ArrayList<>();
		Class<?> clz = object.getClass();
		Field[] fields = clz.getDeclaredFields();
		String orderBy = "";
		Field fieldOrderBy = null;
		for (Field field : fields) {
			String fieldName = field.getName();// 获取字段名
			String fieldType = field.getGenericType().toString();
			System.out.println(fieldName + "----------" + fieldType);// 打印该类的所有属性类型
			if (fieldName.startsWith("c") && !fieldName.endsWith("Type") && fieldType.endsWith(".String")) {
				stringList.add(fieldName);
			}
			if (fieldName.startsWith("c") && !fieldName.endsWith("Type") && fieldType.endsWith("java.util.Date")) {
				dateList.add(fieldName);
			}
			if (fieldName.startsWith("c") && fieldName.endsWith("Type")
					&& (fieldType.endsWith(".Integer") || fieldType.endsWith("int"))) {
				typeList.add(fieldName);
			}
			field.setAccessible(true);
			if (fieldName.equals("orderByMap") && field.get(object) != null) {// 排序拼接
				@SuppressWarnings("unchecked")
				HashMap<String, Integer> map = (HashMap<String, Integer>) field.get(object);
				for (String key : map.keySet()) {
					String keyAfter = "";
					if (key.startsWith("s") && key.endsWith("Type")
							&& checkMap.containsKey(key.substring(1, key.length() - 4))) {
						keyAfter = key.substring(1, key.length() - 4);
					} else {
						continue;
					}
					if ((!map.get(key).equals(1) && !map.get(key).equals(2))) {
						continue;
					}
					if (StringUtils.isEmpty(orderBy)) {
						orderBy = "order by ";
					}
					orderBy += checkMap.get(keyAfter) + (map.get(key) == 1 ? " asc," : " desc,");
				}
				orderBy = orderBy.length() > 1 ? orderBy.substring(0, orderBy.length() - 1) : null;
			}

			if (fieldName.equals("orderBy")) {
				fieldOrderBy = field;
			}
		}

		if (!StringUtils.isEmpty(orderBy) && fieldOrderBy != null) {// 设置最终的orderby
			fieldOrderBy.setAccessible(true);
			fieldOrderBy.set(object, orderBy);
		}

		for (String fieldName : stringList) {
			if (typeList.contains(fieldName + "Type")) {
				Field fType = clz.getDeclaredField(fieldName + "Type");
				fType.setAccessible(true);
				int type = (int) fType.get(object);
				// 获取该类的成员变量
				Field fValue = clz.getDeclaredField(fieldName);
				// 取消语言访问检查
				fValue.setAccessible(true);
				// 给变量赋值
				String value = "";
				if (fValue.get(object) != null && !StringUtils.isEmpty((String) fValue.get(object))) {
					if (type == 1) {
						value = "%" + (String) fValue.get(object) + "%";
						fValue.set(object, value);
						Field fTypePrefix = clz.getDeclaredField(fieldName + "Prefix");
						fTypePrefix.setAccessible(true);
						fTypePrefix.set(object, "like ");
					} else if (type == 2) {
						value = (String) fValue.get(object);
						fValue.set(object, value);
						Field fTypePrefix = clz.getDeclaredField(fieldName + "Prefix");
						fTypePrefix.setAccessible(true);
						fTypePrefix.set(object, "= ");
					} else if (type == 3) {
						value = "%" + (String) fValue.get(object) + "%";
						fValue.set(object, value);
						Field fTypePrefix = clz.getDeclaredField(fieldName + "Prefix");
						fTypePrefix.setAccessible(true);
						fTypePrefix.set(object, "not like ");
					} else if (type == 4) {
						value = (String) fValue.get(object) + "%";
						fValue.set(object, value);
						Field fTypePrefix = clz.getDeclaredField(fieldName + "Prefix");
						fTypePrefix.setAccessible(true);
						fTypePrefix.set(object, "like ");
					} else {
						fValue.set(object, value);
					}
				}
			}
		}

		for (String fieldName : dateList) {
			if (typeList.contains(fieldName + "Type")) {
				Field fType = clz.getDeclaredField(fieldName + "Type");
				fType.setAccessible(true);
				int type = (int) fType.get(object);
				// 获取该类的成员变量
				Field fValue = clz.getDeclaredField(fieldName);
				// 取消语言访问检查
				fValue.setAccessible(true);
				// 给变量赋值
				Date value = null;
				if (fValue.get(object) != null) {
					if (type == 1) {
						value = (Date) fValue.get(object);
						fValue.set(object, value);
						Field fTypePrefix = clz.getDeclaredField(fieldName + "Prefix");
						fTypePrefix.setAccessible(true);
						fTypePrefix.set(object, ">= ");
					} else if (type == 2) {
						value = (Date) fValue.get(object);
						fValue.set(object, value);
						Field fTypePrefix = clz.getDeclaredField(fieldName + "Prefix");
						fTypePrefix.setAccessible(true);
						fTypePrefix.set(object, "> ");
					} else if (type == 3) {
						value = (Date) fValue.get(object);
						fValue.set(object, value);
						Field fTypePrefix = clz.getDeclaredField(fieldName + "Prefix");
						fTypePrefix.setAccessible(true);
						fTypePrefix.set(object, "<= ");
					} else if (type == 4) {
						value = (Date) fValue.get(object);
						fValue.set(object, value);
						Field fTypePrefix = clz.getDeclaredField(fieldName + "Prefix");
						fTypePrefix.setAccessible(true);
						fTypePrefix.set(object, "< ");
					} else {
						fValue.set(object, value);
					}
				}
			}
		}
	}


	/**
	 *
	 * @param fromObject
	 * @param toObject
	 * @throws Exception
	 */
	public static void tranObjectValue(Object fromObject, Object toObject) throws Exception {
		// 先获取来源对象的值
		Map<String, Object> keyMap = new HashMap<>();
		Class<?> fromClz = fromObject.getClass();
		Field[] fromFields = fromClz.getDeclaredFields();
		for (Field field : fromFields) {
//			System.out.println(field.getName() + "----------" + field.getGenericType());// 打印该类的所有属性类型
			field.setAccessible(true);
			keyMap.put(field.getName().toLowerCase().replace("_", ""), field.get(fromObject));// 全部转成小写，并去除所有的下划线
		}

		Class<?> toClz = toObject.getClass();
		Field[] toFields = toClz.getDeclaredFields();
		for (Field field : toFields) {
//			System.err.println("设值：" + field.getName());
			String fieldName = field.getName().toLowerCase().replace("_", "");// 全部转成小写，并去除所有的下划线
			Object object = keyMap.get(fieldName);
			if (object == null) {
				continue;
			}
			field.setAccessible(true);
			String fieldType = field.getGenericType().toString();
			// Boolean、Integer、String
			if (fieldType.endsWith(".String")) {
				field.set(toObject, object.toString());
			} else if (fieldType.endsWith(".Integer") || fieldType.endsWith("int")) {
				field.set(toObject, Integer.valueOf(object.toString() + ""));
			} else if (fieldType.endsWith(".Double") || fieldType.endsWith("double")) {
				field.set(toObject, Double.valueOf(object.toString() + ""));
			} else if (fieldType.endsWith(".Date")) {
				field.set(toObject, (Date) object);
			} else if (fieldType.endsWith(".Boolean") || fieldType.endsWith("boolean")) {
				String valueType = object.getClass().getName();
				if (valueType.endsWith(".Boolean") || valueType.endsWith("boolean")) {
					field.set(toObject, object);
				} else if (valueType.endsWith(".Integer") || valueType.endsWith("int")) {
					field.set(toObject, Integer.valueOf(object.toString()) == 1 ? true : false);//这是啥意思的
				} else if (valueType.endsWith(".String")) {
					if (object.toString().toLowerCase().equals("true")) {
						field.set(toObject, true);
					} else if (object.toString().toLowerCase().equals("false")) {
						field.set(toObject, false);
					}
				}
			}
		}

	}
}
