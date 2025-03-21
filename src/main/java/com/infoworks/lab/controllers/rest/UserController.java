package com.infoworks.lab.controllers.rest;

import com.infoworks.lab.domain.entities.User;
import com.infoworks.lab.rest.models.ItemCount;
import com.infoworks.lab.rest.repository.RestRepository;
import com.it.soul.lab.data.base.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController implements RestRepository<User, String> {

    private DataSource<String, User> dataSource;

    @Autowired
    public UserController(@Qualifier("userService") DataSource<String, User> dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/rowCount")
    public ItemCount rowCount(){
        ItemCount count = new ItemCount();
        count.setCount(Integer.valueOf(dataSource.size()).longValue());
        return count;
    }

    @GetMapping
    public List<User> fetch(
            @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit
            , @RequestParam(value = "page", defaultValue = "0", required = false) Integer page){
        if (limit < 0) limit = 10;
        if (page < 0) page = 0;
        List<User> users = Arrays.asList(dataSource.readSync(page, limit));
        return users;
    }

    @PostMapping
    public User insert(@Valid @RequestBody User user){
        if (user.getId() == null || user.getId() <= 0)
            user.setId(dataSource.size() + 1);
        String key = dataSource.add(user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user
            , @ApiIgnore @RequestParam(value = "id", required = false) String id){
        id = user.getId().toString();
        User updated = dataSource.replace(id, user);
        if (updated == null) throw new RuntimeException( id + " not found!" );
        else return updated;
    }

    @DeleteMapping
    public boolean delete(@RequestParam("id") String id){
        User deleted = dataSource.remove(id);
        return deleted != null;
    }

    @Override
    public String getPrimaryKeyName() {
        return "id";
    }

    @Override
    public Class<User> getEntityType() {
        return User.class;
    }

}
