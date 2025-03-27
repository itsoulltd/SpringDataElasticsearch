package com.infoworks.lab.domain.repositories.impl;

import com.infoworks.lab.domain.repositories.ElasticSearchableRepository;
import com.infoworks.lab.rest.models.SearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ElasticSearchableRepositoryImpl<T, ID> implements ElasticSearchableRepository<T, ID> {

    private static Logger LOG = LoggerFactory.getLogger("ElasticSearchableRepositoryImpl");
    private ElasticsearchOperations template;

    public ElasticSearchableRepositoryImpl(ElasticsearchOperations template) {
        this.template = template;
    }

    @Override
    public List<T> search(SearchQuery query, Class<T> type) {
        List<Criteria> criteriaList = getCriteriaList(query);
        //If-Query-Is-Empty: return empty list;
        if (criteriaList.isEmpty()) return new ArrayList<>();
        //Creating criteria chain from the list:
        Criteria searchCriteria = new Criteria();
        for (Criteria criteria : criteriaList) {
            searchCriteria = searchCriteria.or(criteria);
        }
        //Now create CriteriaQuery from criteria-chain:
        Query mQuery = new CriteriaQuery(searchCriteria);
        addSort(mQuery, query);
        setPageable(mQuery, query);
        SearchHits<T> iterable = template.search(mQuery, type);
        //SearchPage<T> pages = SearchHitSupport.searchPageFor(iterable, mQuery.getPageable());
        @SuppressWarnings("unchecked")
        List<T> items = (List<T>) SearchHitSupport.unwrapSearchHits(iterable);
        return items;
    }

}
