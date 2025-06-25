package com.transport.tracking.repository;


import com.transport.tracking.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    public User findByXusrnameAndXpswd(String userName, String password);

    public User findByXloginAndXpswdAndXact(String userName, String password, int x);

    @Query("SELECT u FROM User u")
    public List<User> findAll();

    public Optional<User> findByXlogin(String xlogin);
}
