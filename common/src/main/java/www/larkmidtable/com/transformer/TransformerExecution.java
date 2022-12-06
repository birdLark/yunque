package www.larkmidtable.com.transformer;


import lombok.Data;

/**
 * 每个func对应一个实例.
 * Created by liqiang on 16/3/16.
 */
@Data
public class TransformerExecution {


    private final Transformer transformer;
    private final TransformerInfo transformerInfo;


    public TransformerExecution(Transformer transformer , TransformerInfo transformerInfo)  {
        this.transformer = transformer;
        this.transformerInfo = transformerInfo;
    }

}
