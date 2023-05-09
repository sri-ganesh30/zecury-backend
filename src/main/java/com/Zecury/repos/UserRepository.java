package com.Zecury.repos;

import com.Zecury.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    List<User> findAllByDateCreatedBetween(Date startDate, Date endDate);

    Optional<User> findUserByuId(String uid);

    List<User> findTop5ByOrderByBalanceDesc();

    Optional<User> findUserByAccNo(String accno);


}