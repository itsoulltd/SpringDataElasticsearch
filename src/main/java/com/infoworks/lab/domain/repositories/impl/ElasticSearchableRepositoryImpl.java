package com.infoworks.lab.domain.repositories.impl;

import com.infoworks.lab.domain.repositories.ElasticSearchableRepository;
import com.infoworks.lab.rest.models.SearchQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ElasticSearchableRepositoryImpl<T, ID> implements ElasticSearchableRepository<T, ID> {

    private ElasticsearchOperations template;

    public ElasticSearchableRepositoryImpl(ElasticsearchOperations template) {
        this.template = template;
    }

    @Override
    public List<T> search(SearchQuery query, Class<T> type) {
        /*Query mQuery = convertIntoSearchQuery(query);
        //If-Query-Is-Empty: return empty list;
        if (mQuery.getQueryObject().isEmpty()) return new ArrayList<>();
        int page = query.getPage();
        int size = query.getSize();
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        mQuery.with(PageRequest.of(page, size));
        List<T> iterable = template.find(mQuery, type);
        return iterable;*/
        return new ArrayList<>();
    }

}
