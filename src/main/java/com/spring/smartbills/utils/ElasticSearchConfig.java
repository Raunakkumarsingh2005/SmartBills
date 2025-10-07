package com.spring.smartbills.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.AbstractElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.query.AbstractElasticsearchRepositoryQuery;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.urls}")
    private String elasticsearchUrl;

    @Value("${spring.elasticsearch.password}")
    private String elasticsearchPassword;

    @Value("${spring.elasticsearch.username}")
    private String elasticsearchUsername;
    @Override
    public ClientConfiguration clientConfiguration() {
        if (elasticsearchUsername == null || elasticsearchPassword == null) {
            return ClientConfiguration.builder()
                    .connectedTo("localhost:9200")
                    .build();
        }
        String hostandport = elasticsearchUrl.substring(8);
        return ClientConfiguration.builder()
                .connectedTo(hostandport)
                .usingSsl()
                .withBasicAuth(elasticsearchUsername, elasticsearchPassword)
                .build();

    }
}
