package www.larkmidtable.com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.exception.YunQueException;
import www.larkmidtable.com.transformer.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Author: lzq
 * @Date: 2022/12/6 10:40
 */
public class TransformerUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TransformerUtil.class);

    public static List<TransformerExecution> buildTransformerInfo(List<TransformerInfo> transformerInfos) {

        List<TransformerExecution> result = new ArrayList<TransformerExecution>();
        /**
         * 延迟load 第三方插件的function，并按需load
         */
        //LOG.info(String.format(" user config tranformers [%s], loading...", functionNames));
        //TransformerRegistry.loadTransformerFromLocalStorage(functionNames);
        int i=0;
        for (TransformerInfo transformerInfo : transformerInfos) {
            Transformer transformer= TransformerRegistry.getTransformer(transformerInfo.getName());
            if (transformer == null) {
                throw new YunQueException(TransformerErrorCode.TRANSFORMER_NOTFOUND_ERROR, "name=" + transformerInfo.getName());
            }


            TransformerExecution transformerExecution = new TransformerExecution(transformer,transformerInfo);
            result.add(transformerExecution);
            i++;
            LOG.info(String.format(" %s of transformer init success. name=%s" , i, transformerInfo.getName()));
        }

        return result;

    }
}
