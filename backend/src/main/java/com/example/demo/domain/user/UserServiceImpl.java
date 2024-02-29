package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", email);
        return ((UserRepository) repository).findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public User register(User user) {
        log.debug("Registering new user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    @Override
    //This Method can be used for development and testing. the Password for the user will be set to "1234"
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode("1234"));
        return save(user);
    }

}
