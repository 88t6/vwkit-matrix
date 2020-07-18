package poi;

import com.alibaba.fastjson.JSONObject;
import matrix.module.common.annotation.Excel;
import matrix.module.common.enums.ExcelEnum;
import matrix.module.common.helper.files.ExcelHelper;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class ExcelTest {

    @Test
    public void testExportForBean() throws IOException {
        ExcelHelper excelUtils = ExcelHelper.getInstance("D:\\");
        String fileName = excelUtils.exportMultiForBean(count -> {
            if (count <= 100) {
                Map<String, List<TestVo>> exportData = new HashMap<>();
                List<TestVo> data = new ArrayList<>();
                for (int i = 0; i < 10000; i++) {
                    data.add(new TestVo(i, "a", new Date()));
                }
                exportData.put("test", data);
                return exportData;
            }
            return null;
        }, ExcelEnum.EXCEL2007);
        System.out.println(fileName);
    }

    @Test
    public void testExportForMap() {
        ExcelHelper excelHelper = ExcelHelper.getInstance("D:\\");
        String fileName = excelHelper.exportMultiForMap(count -> {
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
                Map<String, List<LinkedHashMap<String, Object>>> params = new HashMap<>();
                params.put("sheet0", rows);
                params.put("sheet1", rows);
                return params;
            }
            return null;
        }, ExcelEnum.EXCEL2007);
        System.out.println(fileName);
    }

    @Test
    public void testImportForBean() throws IOException {
        ExcelHelper excelUtils = ExcelHelper.getInstance("D:\\");
        List<String> list = excelUtils.importExcel("e25c34c0-8cb8-47fa-b2d7-45b00a7603f0.xlsx", ExcelEnum.EXCEL2007, null, 10, new ExcelHelper.ImportCallBack<String, TestVo>() {
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
        List<String> list = excelUtils.importExcel("1333c23d-fe0a-423c-bb82-a39472254e71.xlsx", ExcelEnum.EXCEL2007, null, 10, new ExcelHelper.ImportCallBack<String, HashMap<String, Object>>() {
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
