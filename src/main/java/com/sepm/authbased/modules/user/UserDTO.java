package com.sepm.authbased.modules.user;

import java.util.List;

public record UserDTO (
        Integer id,
        String firstName,
        String lastName,
        String email,
        String password,
        List<String> roles){

}
