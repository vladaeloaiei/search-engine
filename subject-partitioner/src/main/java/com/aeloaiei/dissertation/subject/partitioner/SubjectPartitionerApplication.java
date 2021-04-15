package com.aeloaiei.dissertation.subject.partitioner;

import com.aeloaiei.dissertation.subject.partitioner.service.ClusteringService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "com.aeloaiei.dissertation.searchengine.api",
        "com.aeloaiei.dissertation.categoryhandler.api"
})
@SpringBootApplication
public class SubjectPartitionerApplication implements CommandLineRunner {
    private static final Logger LOGGER = LogManager.getLogger(SubjectPartitionerApplication.class);

    @Autowired
    private ClusteringService clusteringService;

    public static void main(String[] args) {
        SpringApplication.run(SubjectPartitionerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        clusteringService.run();
    }
}
