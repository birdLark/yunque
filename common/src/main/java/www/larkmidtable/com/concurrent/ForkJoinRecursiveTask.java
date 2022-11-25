package www.larkmidtable.com.concurrent;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoin分治作业处理
 * @Description
 * @Author daizhong.liu
 * @Date 2022-11-25 14:34:42
 * @param <P> 参数：需要执行作业的参数
 * @param <R> 结果数据：中间数据经过加工后的最终结果数据
 **/
@Getter
@Setter
public abstract class ForkJoinRecursiveTask<P extends TaskParams, R> extends RecursiveTask<List<R>> {
    private List<P> taskParams;
    //每条线程处理的数量
    private int singleThreadSize = 2;// 不允许小于2

    public void setSingleThreadSize(int singleThreadSize) {
        this.singleThreadSize = Math.max(2, singleThreadSize);
    }

    @Override
    protected List<R> compute() {
        if (taskParams.size() <= singleThreadSize) {
            return process();
        }
        int index = taskParams.size() / 2;
        ForkJoinRecursiveTask<P, R> leftTask = buildTask();
        leftTask.setTaskParams(taskParams.subList(0,index));
        leftTask.setSingleThreadSize(singleThreadSize);
        ForkJoinRecursiveTask<P, R> rightTask = buildTask();
        rightTask.setTaskParams(taskParams.subList(index, taskParams.size()));
        rightTask.setSingleThreadSize(singleThreadSize);
        invokeAll(leftTask,rightTask);
        List<R> result = new ArrayList<>();
        result.addAll(leftTask.join());
        result.addAll(rightTask.join());
        return result;
    }
    /**
     * 处理所有业务
     * @Description 一次性处理所有数据，不使用多线程
     * @Author daizhong.liu
     * @Date 2022-11-25 15:53:49
     * @return java.util.List<R>
     **/
    public List<R> allProcess() {
        return process();
    }

    protected abstract ForkJoinRecursiveTask<P,R> buildTask();
    /**
     * 流程处理
     * @Description 定义流程模版，用于在处理业务时可进行前置和后置干预
     * @Author daizhong.liu
     * @Date 2022-11-25 15:28:45
     * @return java.util.List<R> 结果集
     **/
    private List<R> process() {
        preProcess(taskParams);
        List<R> result = postProcess(doProcess(taskParams));
        return result;
    }
    /**
     * 前置处理
     * @Description 处理流程前的干预
     * @Author daizhong.liu
     * @Date 2022-11-25 15:30:06
     * @param params
     **/
    protected void preProcess(List<P> params){}
    /**
     * 业务流程处理
     * @Description 真正的流程处理器，由子类实现具体的业务流程
     * @Author daizhong.liu
     * @Date 2022-11-25 15:30:32
     * @param params 作业参数
     * @return java.util.List<R> 结果集
     **/
    public abstract List<R> doProcess(List<P> params);
    /**
     * 后置处理
     * @Description 可对产生的结果集进行二次干预，默认处理直接返回结果集
     * @Author daizhong.liu
     * @Date 2022-11-25 15:31:16
     * @param result
     * @return java.util.List<R>
     **/
    protected List<R> postProcess(List<R> result){
        return result;
    }

}
