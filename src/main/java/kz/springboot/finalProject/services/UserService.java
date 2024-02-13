package kz.springboot.finalProject.services;

import kz.springboot.finalProject.entities.Countries;
import kz.springboot.finalProject.entities.Roles;
import kz.springboot.finalProject.entities.ShopItems;
import kz.springboot.finalProject.entities.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.management.relation.Role;
import java.util.List;

public interface UserService extends UserDetailsService {

    Users getUserByEmail(String email);

    Users createUser(Users user);

    void deleteUser(Users user);
    Users saveUser(Users user);

    Users getUserById(Long id);
    List<Users> getAllUsers();

}
