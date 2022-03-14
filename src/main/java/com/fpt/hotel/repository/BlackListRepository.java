package com.fpt.hotel.repository;

import com.fpt.hotel.model.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList , Long> {
}
