package io.op.total.domain;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserRepository {
    List<Map<String, Object>> getUsers();
    List<Map<String, Object>> getLogs();
}
