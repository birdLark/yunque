package www.larkmidtable.com;
import www.larkmidtable.com.reader.Reader;

import java.util.List;
import java.util.Queue;

/**
 * reade kafka topic data
 */
public class KafkaReader extends Reader{

    @Override
    public void open() {

    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        return null;
    }

    @Override
    public String[] createInputSplits() {
        return new String[0];
    }

    @Override
    public void close() {

    }
}
