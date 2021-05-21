package io.op.total.model;

import io.op.total.domain.UserRepository;
import io.op.total.vo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Map<String, Object>> getUsers() { return userRepository.getUsers(); }

    public List<Map<String, Object>> getNowLogs() { return userRepository.getNowLogs(); }

    public List<Map<String, Object>> getLogs() {
        return userRepository.getLogs();
    }

    public List<Map<String, Object>> checkStudent(String email, String pw) { return userRepository.checkStudent(email, pw); }

    public List<Map<String, Object>> checkInsertStudent(String email) { return userRepository.checkInsertStudent(email); }

    public List<Map<String, Object>> checkToDayLog(String email) { return userRepository.checkToDayLog(email); }

    public List<Map<String, Object>> checkAdmin(String email, String adminKey) { return userRepository.checkAdmin(email, adminKey); }

    public int insertLog(String email) { return userRepository.insertLog(email); }

    public int insertStudent(Student vo) { return userRepository.insertStudent(vo); }
}
