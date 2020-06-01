package matrix.module.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcheng
 */
public class TreeUtil {

    public static <T extends Tree<T>> void toTree(List<T> list, Comparator<T> comparator) {
        for (T t1 : list) {
            for (T t2 : list) {
                if (comparator.isParentWithChild(t1, t2)) {
                    if (t1.getChildren() == null) {
                        t1.setChildren(new ArrayList<T>());
                    }
                    t1.getChildren().add(t2);
                }
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!comparator.isTop(list.get(i))) {
                list.remove(i);
            }
        }
    }

    public interface Comparator<T> {
        /**
         * 比较是否是父子
         * @param parent 参数
         * @param child 参数
         * @return boolean
         */
        boolean isParentWithChild(T parent, T child);

        /**
         * 是否是顶级
         * @param t 参数
         * @return boolean
         */
        boolean isTop(T t);
    }

    public static class Tree<T> implements Serializable {
		private static final long serialVersionUID = 1L;

		private List<T> children;

        public List<T> getChildren() {
            return children;
        }

        public void setChildren(List<T> children) {
            this.children = children;
        }
    }
}
