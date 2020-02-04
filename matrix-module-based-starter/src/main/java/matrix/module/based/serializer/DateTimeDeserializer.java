package matrix.module.based.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import matrix.module.common.exception.ServiceException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 反序列化Date类型为string
 *
 * @author wangcheng
 */
public class DateTimeDeserializer extends JsonDeserializer<Date> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = jsonParser.getText();
        try {
            return FORMAT.parse(date);
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
    }

}
