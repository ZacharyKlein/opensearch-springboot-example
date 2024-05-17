package com.example.learnopensearch.model;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface WidgetRepository extends ElasticsearchRepository<Widget, String> {

    List<Widget> findByBrandName(String brandName);

    Optional<Widget> findBySerialNumber(String serialNumber);

    List<Widget> findByName(String name);

    List<Widget> findByNameAndBrandName(String name, String brandName);


    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name\", \"brand_name\"], \"fuzziness\": 1}}")
    List<Widget> search(String query);

}


