package com.example.manager.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_user", schema = "user_management")
public class PetUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "c_username")
	private String username;
	
	@Column(name = "c_password")
	private String password;
	
	@ManyToMany
	@JoinTable(schema = "user_management", name = "t_user_authority", 
	joinColumns = @JoinColumn(name = "id_user"), 
	inverseJoinColumns = @JoinColumn(name = "id_authority"))
	private List<Authority> authorities;
}
