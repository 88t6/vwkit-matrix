package poi;

import com.alibaba.fastjson.JSONObject;
import matrix.module.common.annotation.Excel;
import matrix.module.common.enums.ExcelEnum;
import matrix.module.common.helper.files.ExcelHelper;
import matrix.module.common.listener.ExportMultiSheetListener;
import matrix.module.common.listener.ImportSingleSheetCallBack;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class ExcelTest {

    @Test
    public void testExportForBean() throws IOException {
        ExcelHelper excelUtils = ExcelHelper.getInstance("D:\\");
        String fileName = excelUtils.exportMultiForBean(new ExportMultiSheetListener<TestVo>() {

            @Override
            public LinkedHashMap<String, List<TestVo>> getData(Integer count) {
                if (count <= 10) {
                    LinkedHashMap<String, List<TestVo>> exportData = new LinkedHashMap<>();
                    List<TestVo> data = new ArrayList<>();
                    for (int i = 0; i < 10000; i++) {
                        data.add(new TestVo(i, "a", new Date()));
                    }
                    exportData.put("test", data);
                    return exportData;
                }
                return null;
            }

        }, ExcelEnum.EXCEL2007);
        System.out.println(fileName);
    }

    @Test
    public void testExportForMap() {
        ExcelHelper excelHelper = ExcelHelper.getInstance("D:\\");
        String fileName = excelHelper.exportMultiForMap(new ExportMultiSheetListener<LinkedHashMap<String, Object>>() {
            @Override
            public LinkedHashMap<String, List<LinkedHashMap<String, Object>>> getData(Integer count) {
                if (count == 0) {
                    LinkedHashMap<String, Object> cells = new LinkedHashMap<>();
                    cells.put("date", new Date());
                    cells.put("number", 11111111111L);
                    cells.put("boolean", true);
                    cells.put("str", "asdajsdjaksd");
                    List<LinkedHashMap<String, Object>> rows = new ArrayList<>();
                    rows.add(cells);
                    rows.add(cells);
                    rows.add(cells);
                    LinkedHashMap<String, List<LinkedHashMap<String, Object>>> params = new LinkedHashMap<>();
                    params.put("sheet0", rows);
                    params.put("sheet1", rows);
                    return params;
                }
                return null;
            }
        }, ExcelEnum.EXCEL2007);
        System.out.println(fileName);
    }

    @Test
    public void testImportForBean() throws IOException {
        ExcelHelper excelUtils = ExcelHelper.getInstance("D:\\");
        List<String> list = excelUtils.importExcel("245a4c26-2060-4830-ad01-37cc2a0af55f.xlsx", ExcelEnum.EXCEL2007, null, 10, new ImportSingleSheetCallBack<String, TestVo>() {
            @Override
            public List<String> processData(String sheetName, List<TestVo> rows) {
                System.out.println(JSONObject.toJSONString(rows));
                return null;
            }
        });
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void testImportForMap() throws IOException {
        ExcelHelper excelUtils = ExcelHelper.getInstance("D:\\");
        List<String> list = excelUtils.importExcel("xlsx模板.xlsx", ExcelEnum.EXCEL2007, null, 10, new ImportSingleSheetCallBack<String, HashMap<String, Object>>() {
            @Override
            public List<String> processData(String sheetName, List<HashMap<String, Object>> rows) {
                System.out.println(JSONObject.toJSONString(rows));
                return null;
            }
        });
        System.out.println(JSONObject.toJSONString(list));
    }

    public static class TestVo implements Serializable {
        private static final long serialVersionUID = 1L;

        @Excel(value = "主键", width = 500)
        private Integer id;

        @Excel(value = "名称", width = 100)
        private String name;

        @Excel(value = "日期", width = 300)
        private Date now;

        public TestVo() {

        }

        public TestVo(Integer id, String name, Date now) {
            this.id = id;
            this.name = name;
            this.now = now;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getNow() {
            return now;
        }

        public void setNow(Date now) {
            this.now = now;
        }
    }
}
