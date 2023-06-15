package me.ch3ng.queue.schedule;

import java.util.function.Function;

public class FunctionType<T extends AbstractFunctionParam> implements ITimeType<Function<T,FunctionResult>>{

    private Function<T,FunctionResult> func;
    private T param;


    public FunctionType(T param,Function<T,FunctionResult> function) {
        this.func = function;
        this.param = param;
    }

    private FunctionType(Function<T, FunctionResult> function, T param) {
        this.func = function;
        this.param = param;
    }

    @Override
    public Result j() {
        return func.andThen(result -> {
            if(result.isRun){
                this.param.index++;
                if(this.param.index < this.param.count){
                    this.param.timestamp = result.timestamp;
                    result.isRemove = false;
                    return result;
                }
            }
            result.isRemove = true;
            return result;
        }).apply(param);
    }


}
