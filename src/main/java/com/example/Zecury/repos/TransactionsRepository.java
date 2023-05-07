package com.example.Zecury.repos;


import com.example.Zecury.models.Transactions;
import com.example.Zecury.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions,String> {
    List<Transactions> findAllByUser(User user);
    List<Transactions> findAllByUserOrderByDateCreatedDesc(User user);
    List<Transactions> findAllByUserAndDateCreatedBetween(User user, Date start, Date end);
    List<Transactions> findAllByMessageIsLikeAndDateCreatedBetween(String string2, Date start, Date end);

    List<Transactions> findAllByDateCreatedBetween(Date start,Date end);
}
