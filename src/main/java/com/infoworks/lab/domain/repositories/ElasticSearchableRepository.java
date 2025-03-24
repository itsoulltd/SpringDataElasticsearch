package com.infoworks.lab.domain.repositories;

import com.infoworks.lab.rest.models.SearchQuery;
import com.infoworks.lab.rest.models.pagination.SortOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Arrays;
import java.util.List;

public interface ElasticSearchableRepository<T, ID> {

    List<T> search(SearchQuery query, Class<T> type);

    default Query convertIntoSearchQuery(SearchQuery query, String...skipKeys) {
        Query mQuery = new NativeSearchQueryBuilder().build();
        List<String> skipList = Arrays.asList(skipKeys);
        /*query.getProperties().stream()
                .filter(prop -> !skipList.contains(prop.getKey()))
                .filter(prop -> prop.getKey() != null)
                .forEach(prop -> {
                    switch (prop.getOperator()){
                        case GREATER_THAN:
                            mQuery.addCriteria(Criteria.where(prop.getKey()).gt(prop.getValue()));
                            break;
                        case GREATER_THAN_OR_EQUAL:
                            mQuery.addCriteria(Criteria.where(prop.getKey()).gte(prop.getValue()));
                            break;
                        case LESS_THAN:
                            mQuery.addCriteria(Criteria.where(prop.getKey()).lt(prop.getValue()));
                            break;
                        case LESS_THAN_OR_EQUAL:
                            mQuery.addCriteria(Criteria.where(prop.getKey()).lte(prop.getValue()));
                            break;
                        case IN:
                            Object[] inValues = prop.getValue()
                                    .replace("'", "")
                                    .split(",");
                            mQuery.addCriteria(Criteria.where(prop.getKey()).in(inValues));
                            break;
                        case LIKE:
                            mQuery.addCriteria(Criteria.where(prop.getKey()).regex(prop.getValue()));
                            break;
                        case IS_NULL:
                            mQuery.addCriteria(Criteria.where(prop.getKey()).is(null));
                            break;
                        default:
                            mQuery.addCriteria(Criteria.where(prop.getKey()).is(prop.getValue()));
                    }
                });
        if (!query.getDescriptors().isEmpty()){
            query.getDescriptors().stream()
                    .filter(descriptor -> descriptor.getKeys().size() > 0)
                    .forEach(descriptor -> {
                Sort.Direction direction = (descriptor.getOrder() == SortOrder.ASC)
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
                mQuery.with(Sort.by(direction
                        , descriptor.getKeys().toArray(new String[0]))
                );
            });
        }*/
        return mQuery;
    }
}
