package www.larkmidtable.com.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.List;
import java.util.Map;

@Slf4j
public class ListSerializer implements Serializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map map, boolean b) {

    }
    @Override
    public byte[] serialize(String s, Object obj) {
        try {
            List<String> data=(List<String>)obj;
            if (data == null){
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageDto to byte[]");
        }
    }

    @Override
    public void close() {

    }
}