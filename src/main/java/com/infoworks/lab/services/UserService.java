package com.infoworks.lab.services;

import com.infoworks.lab.domain.entities.User;
import com.infoworks.lab.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.SearchQuery;
import com.it.soul.lab.data.simple.SimpleDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService extends SimpleDataSource<String, User> {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
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
}
