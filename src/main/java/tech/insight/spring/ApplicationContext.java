package tech.insight.spring;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {


    private Map<String, Object> ioc = new HashMap<>();
    private Map<String, BeanDefination> beanDefinationMap = new HashMap<>();
    private Map<String, Object> loadingIoc = new HashMap<>();

    public ApplicationContext(String packageName) throws IOException {
        initContext(packageName);
    }

    public void initContext(String packageName) throws IOException {
        scanPackage(packageName).stream().filter(this::scanCreate).forEach(this::wrapper);
        beanDefinationMap.values().stream().forEach(this::createBean);
    }


    protected Object createBean(BeanDefination bd){
        String name = bd.getName();
        if(ioc.containsKey(name)){
            return ioc.get(name);
        }
        Object bean = doCreateBean(bd);
        return bean;
    }

    private Object doCreateBean(BeanDefination bd){
        Constructor<?> constructor = bd.getConstructor();
        Object bean=null;
        try {
            bean = constructor.newInstance();
            loadingIoc.put(bd.getName(),bean);
            autowiredBean(bean,bd);
            Method postConstruct = bd.getPostConstruct();
            if(postConstruct!=null){
                postConstruct.invoke(bean);
            }
            ioc.put(bd.getName(), loadingIoc.remove(bd.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    private void autowiredBean(Object bean, BeanDefination bd) throws IllegalAccessException {
        for (Field autowiredField : bd.getAutowiredFields()) {
            autowiredField.setAccessible(true);
            Object value = getBean(autowiredField.getClass());
            if(value==null){
                BeanDefination bdtemp = beanDefinationMap.values().stream()
                        .filter(b->autowiredField.getType().isAssignableFrom(b.getBeanType()))
                        .findAny().orElse(null);
                if(bdtemp!=null){
                    if(loadingIoc.containsKey(bdtemp.getName())){
                        value=loadingIoc.get(bdtemp.getName());
                    }else{
                        value = doCreateBean(bdtemp);
                    }
                }
            }
            autowiredField.set(bean, value);
        }
    }

    private List<Class<?>> scanPackage(String packageName) throws IOException {

        List<Class<?>> list=new ArrayList<>();

        URL resource = ApplicationContext.class.getClassLoader().getResource(packageName.replace(".", "/"));
        Path path = Path.of(resource.getFile().substring(1));
        Files.walkFileTree(path, new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path absolutePath = file.toAbsolutePath();
                if(absolutePath.toString().endsWith(".class")){
                    String replace = absolutePath.toString().replace(File.separator, ".");
                    int i = replace.indexOf(packageName);
                    String className = replace.substring(i, replace.length() - ".class".length());
                    try {
                        list.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return list;
    }

    protected boolean scanCreate(Class<?> type){
        return type.isAnnotationPresent(Component.class);
    }

    protected BeanDefination wrapper(Class<?> type){
        BeanDefination beanDefination = new BeanDefination(type);
        if(beanDefinationMap.containsKey(beanDefination.getName())){
            throw new RuntimeException("bean名字重复");
        }

        beanDefinationMap.put(beanDefination.getName(), beanDefination);
        return beanDefination;
    }

    public Object getBean(String name){
        return this.ioc.get(name);
    }

    public <T> T getBean(Class<T> beanType){
        return this.ioc.values().stream()
                .filter(bean->beanType.isAssignableFrom(bean.getClass()))
                .map(bean->(T)bean)
                .findAny()
                .orElse(null);
    }


    public <T> List<T> getBeans(Class<T> beanType){
        return this.ioc.values().stream()
                .filter(bean->beanType.isAssignableFrom(bean.getClass()))
                .map(bean->(T)bean)
                .toList();
    }

}
