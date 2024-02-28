package com.example.demo.domain.user;

import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import com.example.demo.domain.user.dto.UserRegisterDTO;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;
  private final UserMapper userMapper;

  @Autowired
  public UserController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  @Operation(summary = "Retrieve user by ID", description = "Returns a specific user by its unique ID.")
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> retrieveById(@PathVariable UUID id) {
    log.info("Retrieve user by ID endpoint called. ID: {}", id);
    User user = userService.findById(id);
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
  }

  @Operation(summary = "Retrieve all users", description = "Returns all users.")
  @GetMapping({"", "/"})
  public ResponseEntity<List<UserDTO>> retrieveAll() {
    log.info("Retrieve all users endpoint called.");
    List<User> users = userService.findAll();
    return new ResponseEntity<>(userMapper.toDTOs(users), HttpStatus.OK);
  }

  @Operation(summary = "Register a new user", description = "Registers a new user.")
  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
    log.info("Register new user endpoint called.");
    User user = userService.register(userMapper.fromUserRegisterDTO(userRegisterDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
  }

  @Operation(summary = "Register a new user without password", description = "Registers a new user without password.")
  @PostMapping("/registerUser")
  public ResponseEntity<UserDTO> registerWithoutPassword(@Valid @RequestBody UserDTO userDTO) {
    log.info("Register new user without password endpoint called.");
    User user = userService.registerUser(userMapper.fromDTO(userDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
  }

  @Operation(summary = "Update user by ID", description = "Updates a specific user by its unique ID.")
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('USER_MODIFY') && @userPermissionEvaluator.isUserAboveAge(authentication.principal.user,18)")
  public ResponseEntity<UserDTO> updateById(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
    log.info("Update user by ID endpoint called. ID: {}", id);
    User user = userService.updateById(id, userMapper.fromDTO(userDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
  }

  @Operation(summary = "Delete user by ID", description = "Deletes a specific user by its unique ID.")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('USER_DELETE')")
  public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
    log.info("Delete user by ID endpoint called. ID: {}", id);
    userService.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
