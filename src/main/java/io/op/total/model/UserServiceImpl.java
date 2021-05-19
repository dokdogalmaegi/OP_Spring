package io.op.total.model;

import io.op.total.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Map<String, Object>> getUsers() {
        return userRepository.getUsers();
    }

    public List<Map<String, Object>> getLogs() {
        return userRepository.getLogs();
    }
}
