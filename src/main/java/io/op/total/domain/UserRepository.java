package io.op.total.domain;

import io.op.total.vo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

// DAO 역할을 하는 것 같음

@Mapper
@Repository
public interface UserRepository {
    List<Map<String, Object>> getUsers();
    List<Map<String, Object>> getLogs();
    List<Map<String, Object>> getNowLogs();
    List<Map<String, Object>> checkStudent(String email, String pw);
    List<Map<String, Object>> checkToDayLog(String email);
    List<Map<String, Object>> checkAdmin(String email, String adminKey);
    int insertLog(String email);
    int insertStudent(Student vo);
}
