package com.infoworks.lab.services;

import com.infoworks.lab.domain.entities.User;
import com.infoworks.lab.domain.repositories.ElasticSearchableRepository;
import com.infoworks.lab.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.SearchQuery;
import com.it.soul.lab.data.simple.SimpleDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService extends SimpleDataSource<String, User> {

    private UserRepository repository;
    private ElasticsearchOperations template;

    public UserService(UserRepository repository, ElasticsearchOperations template) {
        this.repository = repository;
        this.template = template;
    }

    @Override
    public User read(String key) {
        List<User> res = repository.findByName(key);
        return res != null && res.size() > 0 ? res.get(0) : null;
    }

    @Override
    public User[] readSync(int offset, int pageSize) {
        Page<User> finds = repository.findAll(PageRequest.of(offset, pageSize));
        return finds.getContent().toArray(new User[0]);
    }

    @Override
    public int size() {
        return Long.valueOf(repository.count()).intValue();
    }

    @Override
    public void put(String key, User user) {
        this.add(user);
    }

    @Override
    public String add(User user) {
        String key = Optional.ofNullable(user.getId()).orElse(-1).toString();
        repository.save(user);
        return key;
    }

    @Override
    public User replace(String key, User user) {
        User existing = repository.findById(Integer.valueOf(key)).orElse(null);
        if (existing != null && user != null) {
            user.setId(existing.getId());
            existing.unmarshallingFromMap(user.marshallingToMap(true), true);
            existing.setVersion(existing.getVersion() + 1);
            repository.save(existing);
        }
        return existing;
    }

    @Override
    public User remove(String key) {
        User existing = repository.findById(Integer.valueOf(key)).orElse(null);
        if (existing != null) {
            repository.deleteById(existing.getId());
        }
        return existing;
    }

    public List<User> search(SearchQuery searchQuery) {
        return repository.search(searchQuery, User.class);
    }

    public List<User> inclusiveSearch(SearchQuery searchQuery) {
        List<Criteria> criteriaList = ElasticSearchableRepository.getCriteriaList(searchQuery);
        //If-Query-Is-Empty: return empty list;
        if (criteriaList.isEmpty()) return new ArrayList<>();
        //Creating criteria chain from the list:
        Criteria searchCriteria = new Criteria();
        for (Criteria criteria : criteriaList) {
            searchCriteria = searchCriteria.and(criteria);
        }
        //Now create CriteriaQuery from criteria-chain:
        Query mQuery = new CriteriaQuery(searchCriteria);
        ElasticSearchableRepository.addSort(mQuery, searchQuery);
        ElasticSearchableRepository.setPageable(mQuery, searchQuery);
        SearchHits<User> iterable = template.search(mQuery, User.class);
        @SuppressWarnings("unchecked")
        List<User> items = (List<User>) SearchHitSupport.unwrapSearchHits(iterable);
        return items;
    }
}
