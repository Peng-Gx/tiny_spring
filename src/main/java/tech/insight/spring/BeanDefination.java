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
