package tech.insight.spring;

import tech.insight.spring.sub.Autowired;
import tech.insight.spring.sub.PostConstruct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author penggaofeng
 **/

public class BeanDefination {

    private String name;
    private Constructor<?> constructor;
    private Method postConstruct;
    private List<Field> autowiredFields;
    private Class<?> beanType;

    // 通过类对象拿到构建bean所需要的信息
    public BeanDefination(Class<?> type){
        this.beanType = type;
        Component declaredAnnotation = type.getDeclaredAnnotation(Component.class);


        // 为什么这里引入别名，让component支持定义别名
        // 因为spring的ioc容器把bean名字作为key取对象，而不同包下的同名类直接使用simplename会报错
        this.name = declaredAnnotation.name().isEmpty()?type.getSimpleName(): declaredAnnotation.name();
        try {
            this.constructor = type.getConstructor();
            this.postConstruct = Arrays.stream(type.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                    .findAny()
                    .orElse(null);
            this.autowiredFields = Arrays.stream(type.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Autowired.class))
                    .toList();
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

    public Method getPostConstruct(){
        return postConstruct;
    }

    public List<Field> getAutowiredFields(){
        return autowiredFields;
    }

    public Class<?> getBeanType(){
        return beanType;
    }
}
