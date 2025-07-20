package tech.insight.spring;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = new ApplicationContext("tech.insight.spring");
        System.out.println(applicationContext.getBean("Cat"));
        System.out.println(applicationContext.getBean("mydog"));
    }

}
