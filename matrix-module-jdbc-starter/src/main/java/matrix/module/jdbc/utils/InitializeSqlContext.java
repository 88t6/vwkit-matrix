package matrix.module.jdbc.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.helper.encrypt.MD5;
import matrix.module.common.utils.StreamUtil;
import matrix.module.jdbc.properties.JdbcProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WangCheng
 * @date 2020/2/16
 */
public class InitializeSqlContext {

    private static Logger logger = LogManager.getLogger(InitializeSqlContext.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcProperties.InitSql initSql;

    private String locations;

    public void initialize(JdbcProperties.InitSql initSql, String locations) {
        this.initSql = initSql;
        this.locations = locations;
        this.initialize();
    }

    // 初始化
    private void initialize() {
        List<File> files = this.getSortFiles();
        if (CollectionUtils.isEmpty(files)) {
            logger.warn(locations + "  no init script");
            return;
        }
        existInitTable();
        List<InitSqlTableRow> initSqlTableRows = getInitSqlTableRow();
        Map<String, InitSqlTableRow> initSqlTableRowMap = initSqlTableRows.stream().collect(Collectors.toMap(InitSqlTableRow::getSqlFileName, item -> item, (o1, o2) -> o2));
        for (File file : files) {
            InitSqlTableRow initSqlTableRow = initSqlTableRowMap.get(file.getName());
            FileInputStream fis = null;
            if (initSqlTableRow == null) {
                try {
                    fis = new FileInputStream(file);
                    String sqlContent = StreamUtil.streamToString(fis).trim();
                    initSqlTableRow = new InitSqlTableRow()
                            .setSqlFileName(file.getName())
                            .setFileSignature(MD5.get32(sqlContent));
                    executeSql(sqlContent, initSqlTableRow);
                } catch (Exception e) {
                    throw new ServiceException(e);
                } finally {
                    StreamUtil.closeStream(fis);
                }
            }
        }
    }

    /**
     * 获取文件资源
     *
     * @return
     */
    private List<File> getSortFiles() {
        Assert.isNotNull(locations, "locations");
        try {
            File[] files = ResourceUtils.getFile(locations).listFiles();
            List<File> fileList = files != null && files.length > 0 ? Arrays.asList(files) : null;
            if (!CollectionUtils.isEmpty(fileList)) {
                fileList.removeIf(file -> {
                    String fileName = file.getName();
                    boolean isNeed = fileName.startsWith(initSql.getFileNamePrefix())
                            && fileName.endsWith(initSql.getFileNameSuffix())
                            && fileName.contains(initSql.getFileNameSeparator());
                    return !isNeed;
                });
                if (!CollectionUtils.isEmpty(fileList)) {
                    fileList.sort((o1, o2) -> {
                        String fileName1 = o1.getName().split(initSql.getFileNameSeparator())[0];
                        String fileName2 = o2.getName().split(initSql.getFileNameSeparator())[0];
                        return fileName1.compareTo(fileName2);
                    });
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 是否存在初始化表(不存在则新建)
     */
    private void existInitTable() {
        try {
            jdbcTemplate.execute(String.format("select 1 from %s", initSql.getTableName()));
        } catch (BadSqlGrammarException e) {
            //新建数据表
            String createTableSql = String.format("CREATE TABLE %s (" +
                    "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID(自增长)', " +
                    "  `SQL_FILE_NAME` VARCHAR(255) NOT NULL COMMENT 'sql文件名', " +
                    "  `FILE_SIGNATURE` CHAR(32) NOT NULL COMMENT '文件内容签名', " +
                    "  `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间', " +
                    "  PRIMARY KEY (`ID`)" +
                    ")", initSql.getTableName());
            jdbcTemplate.execute(createTableSql);
        }
    }

    /**
     * 获取初始化sql行
     *
     * @return
     */
    private List<InitSqlTableRow> getInitSqlTableRow() {
        return jdbcTemplate.query(String.format("select ID as id, " +
                        "SQL_FILE_NAME as sqlFileName, " +
                        "FILE_SIGNATURE as fileSignature " +
                        "from %s order by ID, CREATE_TIME", initSql.getTableName()),
                new Object[]{},
                new BeanPropertyRowMapper<>(InitSqlTableRow.class));
    }

    /**
     * 执行sql
     *
     * @param sqlContent
     * @param initSqlTableRow
     */
    @Transactional
    public void executeSql(String sqlContent, InitSqlTableRow initSqlTableRow) {
        if (!StringUtils.isEmpty(sqlContent) && initSqlTableRow != null) {
            //todo 这边应该取出来做下sql检查在处理
            jdbcTemplate.batchUpdate(sqlContent.split(";"));
            jdbcTemplate.execute(String.format("INSERT INTO %s " +
                            "(SQL_FILE_NAME, FILE_SIGNATURE, CREATE_TIME) " +
                            "VALUES ('%s', '%s', NOW())",
                    initSql.getTableName(),
                    initSqlTableRow.getSqlFileName(),
                    initSqlTableRow.getFileSignature()));
        }
    }

    @Data
    @Accessors(chain = true)
    private static class InitSqlTableRow implements Serializable {

        private Long id;

        private String sqlFileName;

        private String fileSignature;
    }

}
