package www.larkmidtable.com.transformer;


import java.util.List;

/**
 *
 * @Author: lzq
 * @Date: 2022/12/3 15:42
 */
public abstract class Transformer {
    //transformerName的唯一性在hu中检查，或者提交到插件中心检查。
    private String transformerName;


    public String getTransformerName() {
        return transformerName;
    }

    public void setTransformerName(String transformerName) {
        this.transformerName = transformerName;
    }

    /**
     * @param record 行记录，UDF进行record的处理后，更新相应的record
     * @param transformerParameterInfo  transformer函数参数
     */
    abstract public List<String> evaluate(List<String> record,  TransformerParameterInfo transformerParameterInfo);
}
