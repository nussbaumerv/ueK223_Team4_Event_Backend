package com.example.demo.core.security.permissionevaluators;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserPermissionEvaluator {

  private final UserService userService;

  public UserPermissionEvaluator(UserService userService) {
    this.userService = userService;
  }

  public boolean isOwner(User requestingUser, UUID userId){
    User owner = userService.findById(userId);
    return requestingUser.getId().equals(owner.getId());
  }

}
