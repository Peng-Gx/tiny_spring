package tech.insight.spring;

import java.lang.reflect.Constructor;

/**
 * @author penggaofeng
 **/

public class BeanDefination {

    private String name;
    private Constructor<?> constructor;

    // 通过类对象拿到构建bean所需要的信息
    public BeanDefination(Class<?> type){
        Component declaredAnnotation = type.getDeclaredAnnotation(Component.class);

        // 为什么这里引入别名，让component支持定义别名
        // 因为spring的ioc容器把bean名字作为key取对象，而不同包下的同名类直接使用simplename会报错
        this.name = declaredAnnotation.name().isEmpty()?type.getSimpleName(): declaredAnnotation.name();
        try {
            this.constructor = type.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // bean需要有自己的名字
    public String getName(){
        return name;
    }

    public Constructor<?> getConstructor(){
        return constructor;
    }
}
