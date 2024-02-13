package kz.springboot.finalProject.services.impl;

import kz.springboot.finalProject.entities.Roles;
import kz.springboot.finalProject.entities.ShopItems;
import kz.springboot.finalProject.entities.Users;
import kz.springboot.finalProject.repositories.RoleRepository;
import kz.springboot.finalProject.repositories.UserRepository;
import kz.springboot.finalProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users myUser = userRepository.findByEmail(s);

        if(myUser!=null){
            User secUser = new User(myUser.getEmail(), myUser.getPassword(), myUser.getRoles());
            return secUser;
        }
        throw new UsernameNotFoundException("User Not Found");

    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users createUser(Users user) {
        Users checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser==null){
            Roles role = roleRepository.findByRole("ROLE_USER");

            if(role!=null){
                ArrayList<Roles> roles = new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                BCryptPasswordEncoder temp = new BCryptPasswordEncoder();
                user.setPassword(temp.encode(user.getPassword()));
                return userRepository.save(user);
            }
        }
        return null;
    }

    @Override
    public void deleteUser(Users user) {
        userRepository.delete(user);
    }

    @Override
    public Users saveUser(Users user) {
        Users checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser!=null){
            BCryptPasswordEncoder temp = new BCryptPasswordEncoder();
            user.setPassword(temp.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }


    @Override
    public Users getUserById(Long id) {
        return userRepository.findUsersById(id);
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
