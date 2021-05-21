package io.op.total.model;

import io.op.total.vo.Student;

import java.util.List;
import java.util.Map;

// VO 역할을 하는 것 같음

public interface UserService {
    String solt = "박찬휘임준영";

    List<Map<String, Object>> getUsers();
    List<Map<String, Object>> getLogs();
    List<Map<String, Object>> getNowLogs();
    List<Map<String, Object>> checkStudent(String email, String pw);
    List<Map<String, Object>> checkInsertStudent(String email);
    List<Map<String, Object>> checkToDayLog(String email);
    List<Map<String, Object>> checkAdmin(String email, String adminKey);
    int insertLog(String email);
    int insertStudent(Student vo);
}
