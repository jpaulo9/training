package com.java.spring.services;


import com.java.spring.controllers.PersonController;
import com.java.spring.data.vo.v1.PersonVO;
import com.java.spring.exceptions.ResourceNotFounException;
import com.java.spring.mapper.DozerMapper;
import com.java.spring.model.User;
import com.java.spring.repository.PersonRepository;
import com.java.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {
    private Logger logger = Logger.getLogger(UserServices.class.getName());

    @Autowired
    private UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one user by name "+username+"!");
        User user = userRepository.findByUserName(username);
        if (user != null){
                logger.info("User found "+user.getUserName());
            return user;
        }else {
            throw new UsernameNotFoundException("Username "+username+" not found");
        }
    }
}
