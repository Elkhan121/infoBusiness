package com.example.czn.dao.repositories;

import com.example.czn.entity.custom.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CertificateRepo extends JpaRepository<Certificate , Long> {
    List<Certificate> findAllByDateBetween(Date startDate, Date stopDate);
}
