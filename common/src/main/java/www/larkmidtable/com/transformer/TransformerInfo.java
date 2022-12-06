package www.larkmidtable.com.transformer;

import lombok.Data;

import java.util.List;

/**
 *
 * @Author: lzq
 * @Date: 2022/12/5 20:01
 */
@Data
public class TransformerInfo {

    /**
     * 创建名称
     */
    private String name;
    /**
     * 入参
     */
    private TransformerParameterInfo parameter;



}
