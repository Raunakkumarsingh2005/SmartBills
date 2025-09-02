package com.spring.smartbills.repository;

import com.spring.smartbills.entity.BillSearch;
import com.spring.smartbills.entity.Metadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillSearchRepository extends ElasticsearchRepository<BillSearch,String> {
    @Query("{\"match\" : {\"title\" : \"?0\"}}")
    List<BillSearch> findByTitle(String title);

}
