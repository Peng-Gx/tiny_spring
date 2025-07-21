package tech.insight.spring;

/**
 * @author penggaofeng
 **/

@Component
public class MyBeanPostProcessor implements BeanPostProcessor{

    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        System.out.println(beanName+"初始化完成");
        return bean;
    }
}
