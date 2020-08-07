package matrix.module.common.utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author wangcheng
 */
public class SortUtil {
    public enum Mode {
        BUBBLESORT("bubbleSort", ModeParams.BUBBLEFORARRAYLIST, ModeParams.BUBBLEFORARRAY),
        INSERTSORT("insertSort", ModeParams.CHOOSEFORARRAYLIST, ModeParams.CHOOSEFORARRAY),
        CHOOSESORT("chooseSort", ModeParams.CHOOSEFORARRAYLIST, ModeParams.CHOOSEFORARRAY),
        SHELLSORT("shellSort", ModeParams.SHELLFORARRAYLIST, ModeParams.SHELLFORARRAY),
        FASTSORT("fastSort", ModeParams.FASTFORARRAYLIST, ModeParams.FASTFORARRAY);
        private String methodName;
        private ModeParams[] modeParams;

        Mode(String methodName, ModeParams... modeParams) {
            this.methodName = methodName;
            this.modeParams = modeParams;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public ModeParams[] getModeParams() {
            return modeParams;
        }

        public void setModeParams(ModeParams[] modeParams) {
            this.modeParams = modeParams;
        }
    }

    public enum Type {
        DESC(false), ASC(true);
        private boolean isAsc;

        private Type(boolean isAsc) {
            this.isAsc = isAsc;
        }

        public boolean isAsc() {
            return isAsc;
        }

        public void setAsc(boolean isAsc) {
            this.isAsc = isAsc;
        }
    }

    private enum ModeParams {
        BUBBLEFORARRAYLIST(List.class, String.class, Boolean.class),
        BUBBLEFORARRAY(Object.class, Boolean.class),
        INSERTFORARRAYLIST(List.class, String.class, Boolean.class),
        INSERTFORARRAY(Object.class, Boolean.class),
        CHOOSEFORARRAYLIST(List.class, String.class, Boolean.class),
        CHOOSEFORARRAY(Object.class, Boolean.class),
        SHELLFORARRAYLIST(List.class, String.class, Boolean.class),
        SHELLFORARRAY(Object.class, Boolean.class),
        FASTFORARRAYLIST(List.class, String.class, Integer.class, Integer.class, Boolean.class),
        FASTFORARRAY(Object.class, Integer.class, Integer.class, Boolean.class);
        private Class<?>[] classType;

        private ModeParams(Class<?>... classType) {
            this.setClassType(classType);
        }

        public Class<?>[] getClassType() {
            return classType;
        }

        public void setClassType(Class<?>[] classType) {
            this.classType = classType;
        }
    }

    /**
     * list排序
     *
     * @param list 参数
     * @param sortKey 参数
     * @param mode 参数
     */
    @SuppressWarnings("all")
    public static void sort(List<Map<String, Object>> list, String sortKey, Mode mode, Type type) {
        if (list != null && list.size() > 0) {
            int count = 0;
            for (Map<String, Object> temp : list) {
                if (temp.get(sortKey) != null) {
                    try {
                        double number = Double.parseDouble(temp.get(sortKey).toString());
                        number = 1.0;
                        count += number;
                    } catch (Exception e) {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (count == list.size()) {
                try {
                    Method method = SortUtil.class.getDeclaredMethod(mode.getMethodName(), mode.getModeParams()[0].getClassType());
                    if (mode.getModeParams()[0].getClassType().length == 3) {
                        method.invoke(SortUtil.class.newInstance(), list, sortKey, type.isAsc);
                    } else {
                        method.invoke(SortUtil.class.newInstance(), list, sortKey, 0, list.size() - 1, type.isAsc);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Mode类型错误Exception:" + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("排序字段不为数字，不支持排序");
            }
        }
    }

    /**
     * 数组排序
     *
     * @param arr 参数
     * @param mode 参数
     */
    @SuppressWarnings("all")
    public static void sort(int[] arr, Mode mode, Type type) {
        if (arr.length > 0) {
            try {
                Method method = SortUtil.class.getDeclaredMethod(mode.getMethodName(), mode.getModeParams()[1].getClassType());
                if (mode.getModeParams()[1].getClassType().length == 2) {
                    method.invoke(SortUtil.class.newInstance(), arr, type.isAsc);
                } else {
                    method.invoke(SortUtil.class.newInstance(), arr, 0, arr.length - 1, type.isAsc);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Mode类型错误Exception:" + e.getMessage());
            }
        }
    }

    /**
     * 冒泡排序
     */
    protected void bubbleSort(List<Map<String, Object>> list, String sortKey, Boolean isAsc) {
        Map<String, Object> temp;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                double num1 = Double.parseDouble(list.get(j).get(sortKey).toString());
                double num2 = Double.parseDouble(list.get(j + 1).get(sortKey).toString());
                if (isAsc ? num1 > num2 : num1 < num2) {
                    temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }

    }

    protected void bubbleSort(Object obj, Boolean isAsc) {
        int[] arr = (int[]) obj;
        int temp;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - 1; j++) {
                if (isAsc ? arr[j] > arr[j + 1] : arr[j] < arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }


    /**
     * 插入排序
     */
    protected void insertSort(List<Map<String, Object>> list, String sortKey, Boolean isAsc) {
        Map<String, Object> temp;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (isAsc ? Double.parseDouble(list.get(i).get(sortKey).toString()) > Double.parseDouble(list.get(j).get(sortKey).toString()) :
                        Double.parseDouble(list.get(i).get(sortKey).toString()) < Double.parseDouble(list.get(j).get(sortKey).toString())) {
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }

    protected void insertSort(Object obj, Boolean isAsc) {
        int[] arr = (int[]) obj;
        int temp;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (isAsc ? arr[i] > arr[j] : arr[i] < arr[j]) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * 选择排序
     */
    protected void chooseSort(List<Map<String, Object>> list, String sortKey, Boolean isAsc) {
        Map<String, Object> temp;
        for (int i = 0; i < list.size(); i++) {
            int index = i;
            for (int j = i + 1; j < list.size(); j++) {
                double num1 = Double.parseDouble(list.get(index).get(sortKey).toString());
                double num2 = Double.parseDouble(list.get(j).get(sortKey).toString());
                if (isAsc ? num1 > num2 : num1 < num2) {
                    index = j;
                }
            }
            if (index != i) {
                temp = list.get(i);
                list.set(i, list.get(index));
                list.set(index, temp);
            }
        }
    }

    protected void chooseSort(Object obj, Boolean isAsc) {
        int[] arr = (int[]) obj;
        int temp;
        for (int i = 0; i < arr.length; i++) {
            int index = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (isAsc ? arr[index] > arr[j] : arr[index] < arr[j]) {
                    index = j;
                }
            }
            if (index != i) {
                temp = arr[index];
                arr[index] = arr[i];
                arr[i] = temp;
            }
        }
    }

    /**
     * 希尔排序
     */
    protected void shellSort(List<Map<String, Object>> list, String sortKey, Boolean isAsc) {
        int length = list.size();
        Map<String, Object> temp;
        for (int gap = length / 2; gap >= 1; gap /= 2) {
            for (int i = gap; i < length; i++) {
                for (int j = i - gap; j >= 0; j -= gap) {
                    double num1 = Double.parseDouble(list.get(j).get(sortKey).toString());
                    double num2 = Double.parseDouble(list.get(j + gap).get(sortKey).toString());
                    if (isAsc ? num1 > num2 : num1 < num2) {
                        temp = list.get(j);
                        list.set(j, list.get(j + gap));
                        list.set(j + gap, temp);
                    }
                }
            }
        }
    }

    protected void shellSort(Object obj, Boolean isAsc) {
        int[] arr = (int[]) obj;
        int temp;
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                for (int j = i - gap; j >= 0; j -= gap) {
                    if (isAsc ? (arr[j] > arr[j + gap]) : arr[j] < arr[j + gap]) {
                        temp = arr[j];
                        arr[j] = arr[j + gap];
                        arr[j + gap] = temp;
                    }
                }
            }
        }
    }

    /**
     * 快速排序
     */
    protected void fastSort(List<Map<String, Object>> list, String sortKey, Integer low, Integer high, Boolean isAsc) {
        int i = low, j = high;
        if (i >= j) {
            return;
        }
        Map<String, Object> index = list.get(i);
        while (i < j) {
            if (isAsc) {
                while (i < j && Double.parseDouble(index.get(sortKey).toString()) <= Double.parseDouble(String.valueOf(list.get(j).get(sortKey)))) {
                    j--;
                }
                if (i < j) {
                    list.set(i++, list.get(j));
                }
                while (i < j && Double.parseDouble(index.get(sortKey).toString()) >= Double.parseDouble(String.valueOf(list.get(i).get(sortKey)))) {
                    i++;
                }
                if (i < j) {
                    list.set(j--, list.get(i));
                }
            } else {
                while (i < j && Double.parseDouble(index.get(sortKey).toString()) >= Double.parseDouble(list.get(j).get(sortKey).toString())) {
                    j--;
                }
                if (i < j) {
                    list.set(i++, list.get(j));
                }
                while (i < j && Double.parseDouble(index.get(sortKey).toString()) <= Double.parseDouble(list.get(i).get(sortKey).toString())) {
                    i++;
                }
                if (i < j) {
                    list.set(j--, list.get(i));
                }
            }
        }
        list.set(i, index);
        if (i > 0) {
            this.fastSort(list, sortKey, low, i - 1, isAsc);
        }
        if (j < high) {
            this.fastSort(list, sortKey, j + 1, high, isAsc);
        }
    }

    protected void fastSort(Object obj, Integer low, Integer high, Boolean isAsc) {
        int[] arr = (int[]) obj;
        int i = low, j = high;
        if (i >= j) {
            return;
        }
        int index = arr[low];
        while (i < j) {
            if (isAsc) {
                while (i < j && index < arr[j]) {
                    j--;
                }
                if (i < j) {
                    arr[i++] = arr[j];
                }
                while (i < j && index > arr[i]) {
                    i++;
                }
                if (i < j) {
                    arr[j--] = arr[i];
                }
            } else {
                while (i < j && index > arr[j]) {
                    j--;
                }
                if (i < j) {
                    arr[i++] = arr[j];
                }
                while (i < j && index < arr[i]) {
                    i++;
                }
                if (i < j) {
                    arr[j--] = arr[i];
                }
            }
        }
        arr[i] = index;
        this.fastSort(arr, low, i - 1, isAsc);
        this.fastSort(arr, j + 1, high, isAsc);
    }
}
