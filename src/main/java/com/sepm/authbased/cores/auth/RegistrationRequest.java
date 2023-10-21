package com.sepm.authbased.cores.auth;

import com.sepm.authbased.modules.user.Role;

public record RegistrationRequest (
        String firstName,
        String lastName,
        String email,
        String password,
        Role role
){
}
