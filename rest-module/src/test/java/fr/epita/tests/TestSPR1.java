package fr.epita.tests;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Test;

import fr.epita.config.AppConfig;

public class TestSPR1 {

    @Test
    public void testSpringBean() {
        // Initialize the Spring application context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the bean of type String
        String testBean = context.getBean(String.class);

        // Display the content of the bean
        System.out.println("Retrieved bean: " + testBean);

        // Assert that the bean contains the expected value
        assert testBean.equals("test from spring!") : "Bean content is incorrect!";
    }
}
