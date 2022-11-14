package top.mnsx.mnsx_system.component;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @BelongsProject: Mnsx-System
 * @User: Mnsx_x
 * @CreateTime: 2022/11/14 19:01
 * @Description:
 */
public class LongToStringFastJsonSerializer implements ObjectSerializer {

    public static final long MAX_LONG_TO_STRING = (long) Math.pow(10, 15);

    public static final LongToStringFastJsonSerializer instance = new LongToStringFastJsonSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object,
                      Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }

        if (fieldType.getTypeName().equals(Long.class.getTypeName())
                || fieldType.getTypeName().equals(Long.TYPE.getTypeName())) {
            long val = (Long) object;
            if (val > MAX_LONG_TO_STRING) {
                out.writeString(Long.toString(val));
            } else {
                out.writeLong(val);
            }
        } else {
            out.writeString(object.toString());
        }
    }
}
