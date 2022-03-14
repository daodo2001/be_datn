package com.fpt.hotel.repository;

import com.fpt.hotel.model.CostsIncurred;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostsIncurredRepository extends JpaRepository<CostsIncurred, Long> {
}
