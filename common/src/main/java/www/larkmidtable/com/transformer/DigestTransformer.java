package www.larkmidtable.com.transformer;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.exception.HongHuException;

import java.util.ArrayList;
import java.util.List;

/**
 *  加密转换器
 * @Author: lzq
 * @Date: 2022/12/6 8:20
 */
public class DigestTransformer extends Transformer {
    private static final Logger LOG = LoggerFactory.getLogger(DigestTransformer.class);

    private static final String MD5 = "md5";
    private static final String SHA1 = "sha1";
    private static final String TO_UPPER_CASE = "toUpperCase";
    private static final String TO_LOWER_CASE = "toLowerCase";

    public DigestTransformer() {
        setTransformerName("dx_digest");
    }

    @Override
    public List<String> evaluate(List<String> record, TransformerParameterInfo transformerParameterInfo) {
        LOG.debug("DigestTransformer evaluate begin");
        String columnName;
        String type;
        String charType;
        try {
            if (transformerParameterInfo.getParas().length != 2) {
                throw new RuntimeException("dx_digest paras length must be 2");
            }
            columnName = transformerParameterInfo.getColumnName();
            type = transformerParameterInfo.getParas()[0];
            charType =  transformerParameterInfo.getParas()[1];

            if (!StringUtils.equalsIgnoreCase(MD5, type) && !StringUtils.equalsIgnoreCase(SHA1, type)) {
                throw new RuntimeException("dx_digest paras index 1 must be md5 or sha1");
            }
            if (!StringUtils.equalsIgnoreCase(TO_UPPER_CASE, charType) && !StringUtils.equalsIgnoreCase(TO_LOWER_CASE, charType)) {
                throw new RuntimeException("dx_digest paras index 2 must be toUpperCase or toLowerCase");
            }
        } catch (Exception e) {
            throw new HongHuException(TransformerErrorCode.TRANSFORMER_ILLEGAL_PARAMETER, e);
        }
        List<String> newRecord=new ArrayList<>();
        try {
            record.forEach(oriRecord->{
                JSONObject jsonObject = JSONObject.parseObject(oriRecord);
                String oriValue = jsonObject.getString(columnName);
                if(!org.apache.commons.lang3.StringUtils.isEmpty(oriValue)){
                    String newValue;
                    if (MD5.equals(type)) {
                        newValue = DigestUtils.md5Hex(oriValue);
                    } else {
                        newValue = DigestUtils.sha1Hex(oriValue);
                    }
                    if (TO_UPPER_CASE.equals(charType)) {
                        newValue = newValue.toUpperCase();
                    } else {
                        newValue = newValue.toLowerCase();
                    }
                    jsonObject.put(columnName,newValue);
                    newRecord.add(JSONObject.toJSONString(jsonObject));
                }
            });
        } catch (Exception e) {
            throw new HongHuException(TransformerErrorCode.TRANSFORMER_RUN_EXCEPTION, e);
        }
        LOG.debug("DigestTransformer evaluate end");
        return newRecord;
    }

}
