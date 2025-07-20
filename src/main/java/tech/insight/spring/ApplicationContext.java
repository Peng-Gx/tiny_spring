package tech.insight.spring;

import java.util.List;

public class ApplicationContext {

    public ApplicationContext(String packageName){
        initContext(packageName);
    }

    public void initContext(String packageName){
        scanPackage(packageName).stream().filter(this::scanCreate).map(this::wrapper).forEach(this::creatBean);
    }

    protected boolean scanCreate(Class<?> type){
        return type.isAnnotationPresent(Component.class);
    }

    protected BeanDefination wrapper(Class<?> type){
        return new BeanDefination(type);
    }

    protected void creatBean(BeanDefination bd){

    }

    private List<Class<?>> scanPackage(String packageName){
        return null;
    }

    public Object getBean(String name){
        return null;
    }

    public <T> T getBean(Class<T> beanType){
        return null;
    }

    public <T> List<T> getBeans(Class<T> beanType){
        return null;
    }




}
