package io.op.total.model;

import java.util.List;
import java.util.Map;

// VO 역할을 하는 것 같음

public interface UserService {
    String solt = "박찬휘임준영";

    List<Map<String, Object>> getUsers();
    List<Map<String, Object>> getLogs();
    List<Map<String, Object>> getNowLogs();
    List<Map<String, Object>> checkStudent(String email, String pw);
    List<Map<String, Object>> checkToDayLog(String email);
    int insertLog(String email);
}
