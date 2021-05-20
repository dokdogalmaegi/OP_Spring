package io.op.total.domain;

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
}
