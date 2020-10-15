package zip;

import matrix.module.common.helper.files.ZipHelper;
import org.junit.Test;

/**
 * @author wangcheng
 * 2020/10/15
 **/
public class ZipTest {

    @Test
    public void doCompress() {
        ZipHelper zipHelper = ZipHelper.getInstance("D:\\dist");
        String fileName = zipHelper.doCompress(new String[]{"D:\\774a2bf3-26ec-4f40-80ad-f86aad9ed7ea.doc", "D:\\09aff68000e04b8393b2709a70dd291e.xlsx"});
        System.out.println(fileName);
    }
}
