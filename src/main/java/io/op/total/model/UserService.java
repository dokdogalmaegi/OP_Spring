package io.op.total.model;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Map<String, Object>> getUsers();
    List<Map<String, Object>> getLogs();
}
