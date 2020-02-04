package matrix.module.based.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 序列化Date类型为string
 *
 * @author wangcheng
 */
public class DateTimeSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String format = FORMAT.format(date);
        gen.writeString("1000-01-01 00:00:00".equals(format) ? "" : format);
    }
}
