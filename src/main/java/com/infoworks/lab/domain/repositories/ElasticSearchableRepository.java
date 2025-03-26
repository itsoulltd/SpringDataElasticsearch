package com.infoworks.lab.domain.repositories;

import com.infoworks.lab.rest.models.SearchQuery;
import org.springframework.data.elasticsearch.core.query.Criteria;

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
}
