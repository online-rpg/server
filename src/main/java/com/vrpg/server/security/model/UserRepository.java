package com.vrpg.server.security.model;

import org.springframework.data.repository.CrudRepository;

interface UserRepository extends CrudRepository<User, String> {
}
