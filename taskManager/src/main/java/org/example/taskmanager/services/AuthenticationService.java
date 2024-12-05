package org.example.taskmanager.services;

import org.example.taskmanager.auth.AuthRequest;
import org.example.taskmanager.auth.AuthenticationResponse;
import org.example.taskmanager.auth.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authentication(AuthRequest request);
}
