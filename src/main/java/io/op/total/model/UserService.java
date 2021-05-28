package io.op.total.model;

import io.op.total.vo.Student;
import io.op.total.vo.UpdateStudent;

import java.util.List;
import java.util.Map;

// VO 역할을 하는 것 같음

public interface UserService {
    String solt = "박찬휘임준영장인성";

    List<Map<String, Object>> getUsers();
    List<Map<String, Object>> getLogs();
    List<Map<String, Object>> getNowLogs();
    List<Map<String, Object>> getNowNotLogs();
    List<Map<String, Object>> checkStudent(String email, String pw, String phone);
    List<Map<String, Object>> checkInsertStudent(String email);
    List<Map<String, Object>> checkToDayLog(String email);
    List<Map<String, Object>> checkAdmin(String email, String adminKey);
    List<Map<String, Object>> checkOnline(String email);
    List<Map<String, Object>> getClassNowLogs(int grade, int class_num);
    List<Map<String, Object>> getClassNotNowLogs(int grade, int class_num);
    int insertLog(String email);
    int insertStudent(Student vo);
    int updateStudent(String pw, String phone, String email);
    int updateStudentCsharp(UpdateStudent vo);
}
