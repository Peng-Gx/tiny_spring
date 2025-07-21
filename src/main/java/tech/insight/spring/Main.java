package tech.insight.spring;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("hello tiny spring");
        ApplicationContext applicationContext = new ApplicationContext("tech.insight.spring");
    }

}
