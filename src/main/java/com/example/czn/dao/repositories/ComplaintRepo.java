package com.example.czn.dao.repositories;

import com.example.czn.entity.custom.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ComplaintRepo extends JpaRepository<Complaint, Long> {
    int countComplaintById(int id);
    List<Complaint> findAllByPostDateBetween(Date dateBegin, Date dateEnd);

}
