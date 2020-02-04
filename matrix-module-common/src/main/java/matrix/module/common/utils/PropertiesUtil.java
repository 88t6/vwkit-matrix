package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;

import java.io.InputStream;
import java.util.Properties;

/**
 * 解析properties
 * @author 36509
 *
 */
public class PropertiesUtil {

	public static Properties get(Class<?> clazz,String path){
		Properties properties = new Properties();
		InputStream inputStream = null;
		try{
			inputStream = clazz.getResourceAsStream(path);
			properties.load(inputStream);
		}catch(Exception e){
			throw new ServiceException(e);
		}finally {
			StreamUtil.closeStream(inputStream);
		}
		return properties;
	}

}
