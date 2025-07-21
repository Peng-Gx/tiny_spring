package tech.insight.spring.sub;

import tech.insight.spring.Component;

/**
 * @author penggaofeng
 **/

@Component(name = "mydog")
public class Dog {

    @Autowired
    private Cat cat;

    @PostConstruct
    public void init(){
        System.out.println("Dog创建了 dog里面有一个属性" + cat);
    }
}
