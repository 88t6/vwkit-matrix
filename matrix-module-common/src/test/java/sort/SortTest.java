package sort;

import matrix.module.common.utils.SortUtil;
import org.junit.Test;

import java.util.*;

/**
 * @author wangcheng
 */
public class SortTest {

    private static ArrayList<Map<String, Object>> listInfo = null;
    private static int[] arrInfo = null;

    private static void generate(List<Map<String, Object>> list, int[] arr, int count) {
        if (listInfo == null || arrInfo == null) {
            listInfo = new ArrayList<Map<String, Object>>();
            arrInfo = new int[count];
            for (int i = 0; i < count; i++) {
                int value = (int) Math.round(Math.random() * count * 10);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("XH", value);
                listInfo.add(map);
                arrInfo[i] = value;
            }
        }
        list.clear();
        list.addAll(listInfo);
        arr = Arrays.copyOf(arrInfo, arrInfo.length);
    }

    @Test
    public void test() {
        long time = 0;
        int count = 100;
        boolean isPrint = false;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int[] arr = new int[count];

        System.out.println("----------------集合---------------");

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(list, "XH", SortUtil.Mode.BUBBLESORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(list.toString());
        System.out.println("冒泡排序集合:" + (System.currentTimeMillis() - time));

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(list, "XH", SortUtil.Mode.INSERTSORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(list.toString());
        System.out.println("插入排序集合:" + (System.currentTimeMillis() - time));

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(list, "XH", SortUtil.Mode.CHOOSESORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(list.toString());
        System.out.println("选择排序集合:" + (System.currentTimeMillis() - time));

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(list, "XH", SortUtil.Mode.SHELLSORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(list.toString());
        System.out.println("希尔排序集合:" + (System.currentTimeMillis() - time));

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(list, "XH", SortUtil.Mode.FASTSORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(list.toString());
        System.out.println("快速排序集合:" + (System.currentTimeMillis() - time));

        System.out.println("----------------数组---------------");

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(arr, SortUtil.Mode.BUBBLESORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(Arrays.toString(arr));
        System.out.println("冒泡排序数组:" + (System.currentTimeMillis() - time));

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(arr, SortUtil.Mode.INSERTSORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(Arrays.toString(arr));
        System.out.println("插入排序数组:" + (System.currentTimeMillis() - time));

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(arr, SortUtil.Mode.CHOOSESORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(Arrays.toString(arr));
        System.out.println("选择排序数组:" + (System.currentTimeMillis() - time));

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(arr, SortUtil.Mode.SHELLSORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(Arrays.toString(arr));
        System.out.println("希尔排序数组:" + (System.currentTimeMillis() - time));

        SortTest.generate(list, arr, count);
        time = System.currentTimeMillis();
        SortUtil.sort(arr, SortUtil.Mode.FASTSORT, SortUtil.Type.ASC);
        if (isPrint) System.out.println(Arrays.toString(arr));
        System.out.println("快速排序数组:" + (System.currentTimeMillis() - time));
    }
}
