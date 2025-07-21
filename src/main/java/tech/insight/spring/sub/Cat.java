package tech.insight.spring.sub;

/**
 * @author penggaofeng
 **/

import tech.insight.spring.Component;

@Component
public class Cat {

    @Autowired
    private Dog dog;

    @Autowired
    private Cat cat;

    @PostConstruct
    public void init(){
        System.out.println("Cat创建了 cat里面有一个属性" + dog);
    }
}
