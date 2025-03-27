package com.infoworks.lab.domain.repositories;

import com.infoworks.lab.rest.models.SearchQuery;
import com.infoworks.lab.rest.models.pagination.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ElasticSearchableRepository<T, ID> {

    List<T> search(SearchQuery query, Class<T> type);

    default List<Criteria> getCriteriaList(SearchQuery query, String...skipKeys) {
        List<Criteria> criteriaList = new ArrayList<>();
        List<String> skipList = Arrays.asList(skipKeys);
        query.getProperties().stream()
                .filter(prop -> !skipList.contains(prop.getKey()))
                .filter(prop -> prop.getKey() != null)
                .forEach(prop -> {
                    switch (prop.getOperator()){
                        case GREATER_THAN:
                            criteriaList.add(Criteria.where(prop.getKey()).greaterThan(prop.getValue()));
                            break;
                        case GREATER_THAN_OR_EQUAL:
                            criteriaList.add(Criteria.where(prop.getKey()).greaterThanEqual(prop.getValue()));
                            break;
                        case LESS_THAN:
                            criteriaList.add(Criteria.where(prop.getKey()).lessThan(prop.getValue()));
                            break;
                        case LESS_THAN_OR_EQUAL:
                            criteriaList.add(Criteria.where(prop.getKey()).lessThanEqual(prop.getValue()));
                            break;
                        case IN:
                            Object[] inValues = prop.getValue()
                                    .replace("'", "")
                                    .split(",");
                            criteriaList.add(Criteria.where(prop.getKey()).in(inValues));
                            break;
                        case LIKE:
                            criteriaList.add(Criteria.where(prop.getKey()).contains(prop.getValue()));
                            break;
                        case IS_NULL:
                            criteriaList.add(Criteria.where(prop.getKey()).is(null));
                            break;
                        default:
                            criteriaList.add(Criteria.where(prop.getKey()).is(prop.getValue()));
                    }
                });
        return criteriaList;
    }

    default Query addSort(Query mQuery, SearchQuery query) {
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
        return mQuery;
    }

    default Query setPageable(Query mQuery, SearchQuery query) {
        int page = query.getPage();
        int size = query.getSize();
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        mQuery.setPageable(PageRequest.of(page, size));
        return mQuery;
    }
}
