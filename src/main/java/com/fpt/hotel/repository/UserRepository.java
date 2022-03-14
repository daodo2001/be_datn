package com.fpt.hotel.repository;

import java.util.List;
import java.util.Optional;

import com.fpt.hotel.owner.dto.response.Total;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpt.hotel.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);
	
	@Query(value = "SELECT * FROM `users` join user_roles on user_roles.user_id = users.id join roles on roles.id = user_roles.role_id \r\n"
			+ "where roles.name = ?1", nativeQuery = true)
	List<User> findAll(String roleName);

	@Query(value = "SELECT * FROM USERS join hotels on hotels.id = users.id_hotel where users.id = ?1", nativeQuery = true)
	User staffByHotel(Integer id);

	@Query(value = "select count(users.id) as total from " +
			"users join USER_ROLES on USER_ROLES.USER_ID = users.id where ROLE_ID = 1",nativeQuery = true)
	Total totalUsers();
}
