package fi;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Use Hibernate's SessionFactory
    @Bean
    public SessionFactory sessionFactory(final HibernateEntityManagerFactory hibernateEntityManagerFactory){
        return hibernateEntityManagerFactory.getSessionFactory();
    }

}
