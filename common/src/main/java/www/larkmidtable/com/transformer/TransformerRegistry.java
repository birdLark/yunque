package www.larkmidtable.com.transformer;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.exception.HongHuException;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Author: lzq
 * @Date: 2022/12/5 8:05
 */
public class TransformerRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(TransformerRegistry.class);
    private static Map<String, Transformer> registedTransformer = new HashMap();

    static {
        /**
         * add native transformer
         */
        registTransformer(new DigestTransformer());
        //registTransformer(new DataConvertTransformer());
        LOG.info("registedTransformer info{}", JSONObject.toJSONString(registedTransformer));
    }


    public static Transformer getTransformer(String transformerName) {
        Transformer result = registedTransformer.get(transformerName);
        return result;
    }

    public static synchronized void registTransformer(Transformer transformer) {
        checkName(transformer.getTransformerName());
        if (registedTransformer.containsKey(transformer.getTransformerName())) {
            throw new HongHuException(TransformerErrorCode.TRANSFORMER_DUPLICATE_ERROR);
        }
        registedTransformer.put(transformer.getTransformerName(),transformer);
    }

    private static void checkName(String functionName) {
        boolean checkResult = true;
        if (!functionName.startsWith("dx_")) {
            checkResult = false;
        }
        if (!checkResult) {
            throw new HongHuException(TransformerErrorCode.TRANSFORMER_NAME_ERROR);
        }
    }

}
