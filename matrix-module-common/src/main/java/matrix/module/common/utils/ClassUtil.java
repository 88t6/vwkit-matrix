package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类工具
 *
 * @author WangCheng
 * date 2020/7/17
 */
public class ClassUtil {

    /**
     * 获取泛型
     *
     * @param clazz 参数
     * @param index 第几个泛型
     * @return 泛型类型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericSuperClassTypes(Class<T> clazz, Integer index) {
        if (clazz.isSynthetic()) {
            //泛型
            throw new ServiceException("lambda不支持取出泛型");
        } else {
            Type[] types = ((ParameterizedTypeImpl) clazz.getGenericSuperclass()).getActualTypeArguments();
            if (types[index] instanceof ParameterizedTypeImpl) {
                ParameterizedType parameterizedTypes = (ParameterizedType) types[index];
                return (Class<T>) parameterizedTypes.getRawType();
            }
            return (Class<T>) types[index];
        }
    }
}
