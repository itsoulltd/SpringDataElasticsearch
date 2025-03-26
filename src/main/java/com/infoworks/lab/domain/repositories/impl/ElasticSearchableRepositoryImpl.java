package com.infoworks.lab.domain.repositories.impl;

import com.infoworks.lab.domain.repositories.ElasticSearchableRepository;
import com.infoworks.lab.rest.models.SearchQuery;
import com.infoworks.lab.rest.models.pagination.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private ElasticsearchOperations template;

    public ElasticSearchableRepositoryImpl(ElasticsearchOperations template) {
        this.template = template;
    }

    @Override
    public List<T> search(SearchQuery query, Class<T> type) {
        List<Criteria> criteriaList = getCriteriaList(query);
        //If-Query-Is-Empty: return empty list;
        if (criteriaList.isEmpty()) return new ArrayList<>();
        //
        Criteria searchCriteria = new Criteria();
        //FIXME: not working
        criteriaList.forEach(criteria -> searchCriteria.or(criteria));
        Query mQuery = new CriteriaQuery(searchCriteria);
        if (!query.getDescriptors().isEmpty()){
            query.getDescriptors().stream()
                    .filter(descriptor -> descriptor.getKeys().size() > 0)
                    .forEach(descriptor -> {
                        Sort.Direction direction = (descriptor.getOrder() == SortOrder.ASC)
                                ? Sort.Direction.ASC
                                : Sort.Direction.DESC;
                        mQuery.addSort(Sort.by(direction, descriptor.getKeys().toArray(new String[0]))
                        );
                    });
        }
        //If-Query-Is-Empty: return empty list;
        if (mQuery.getFields().isEmpty()) return new ArrayList<>();
        int page = query.getPage();
        int size = query.getSize();
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        mQuery.setPageable(PageRequest.of(page, size));
        SearchHits<T> iterable = template.search(mQuery, type);
        //SearchPage<T> pages = SearchHitSupport.searchPageFor(iterable, mQuery.getPageable());
        @SuppressWarnings("unchecked")
        List<T> items = (List<T>) SearchHitSupport.unwrapSearchHits(iterable);
        return items;
    }

}
