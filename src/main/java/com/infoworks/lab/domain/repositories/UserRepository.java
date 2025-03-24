package com.infoworks.lab.domain.repositories;

import com.infoworks.lab.domain.entities.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, Integer>
        , ElasticSearchableRepository<User, Integer> {
    List<User> findByName(String name);
}
