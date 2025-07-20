package tech.insight.spring;

import java.lang.reflect.Constructor;

/**
 * @author penggaofeng
 **/

public class BeanDefination {

    // 通过类对象拿到构建bean所需要的信息
    public BeanDefination(Class<?> type){

    }

    // bean需要有自己的名字
    public String getName(){
        return null;
    }

    public Constructor<?> getConstructor(){
        return null;
    }
}
