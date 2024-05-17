package com.example.learnopensearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
public class LearnopensearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnopensearchApplication.class, args);
    }

}
