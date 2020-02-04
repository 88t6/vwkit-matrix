package matrix.module.common.utils;
import matrix.module.common.exception.ServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author wangcheng
 * @date 2018/7/9
 */
public class DeepCopyUtil {

    @SuppressWarnings("unchecked")
    public static <T> T deepCopyBySerialize(T src) {
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream out = null;
        ByteArrayInputStream byteIn = null;
        ObjectInputStream in = null;
        try {
            byteOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            in = new ObjectInputStream(byteIn);
            T dest = (T) in.readObject();
            return dest;
        } catch (Exception e) {
            throw new ServiceException("深度拷贝失败");
        } finally {
            StreamUtil.closeStream(in);
            StreamUtil.closeStream(byteIn);
            StreamUtil.closeStream(out);
            StreamUtil.closeStream(byteOut);
        }
    }
}
