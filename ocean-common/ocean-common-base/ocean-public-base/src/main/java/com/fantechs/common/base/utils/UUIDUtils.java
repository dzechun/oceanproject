package com.fantechs.common.base.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.genid.GenId;


/**
 * UUID Generator for high concurrency
 * @author sunny
 * @date 2019-9-8
 * 备注：
 */
@Service
public class UUIDUtils  implements GenId<String> {
	
	private static TimeBasedGenerator timeBasedGenerator=Generators.timeBasedGenerator(EthernetAddress.fromInterface());

	/**
	 * 获取UUID
	 * @return UUID字符串
	 */
	public static String getUUID(){
		
		return getRawUUID().replaceAll("-","");
	}
	
	/**
     * 获取原始UUID
     * 示例：df870a7a-dc58-11e5-b826-28b2bd440a9d
     * @return UUID字符串
     */
	public static String getRawUUID(){
	    
        return timeBasedGenerator.generate().toString();
    }

	public static void main(String[] args) {
		System.out.println(UUIDUtils.getUUID());
	}

	@Override
	public String genId(String s, String s1) {
		return getUUID();
	}
}
