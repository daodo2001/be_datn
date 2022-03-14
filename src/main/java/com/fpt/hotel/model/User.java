package com.fpt.hotel.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"), })
public class User {

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String first_name;
	private String last_name;
	private String phone;
	private String email;

	@Temporal(TemporalType.DATE)
	private Date date_of_birth;
	private String address;
	private Integer enabled;
	private String image;
	private Integer sex;
	private Integer status;
	private String username;
	private String password;
	private String cccd;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	private Integer id_creator;

	@OneToMany(mappedBy = "user")
	private List<Transaction_Info> id_transaction_info;

	@ManyToOne()
	@JoinColumn(name = "id_hotel")
	private Hotel hotel;

}
