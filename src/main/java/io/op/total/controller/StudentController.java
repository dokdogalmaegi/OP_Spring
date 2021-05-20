package io.op.total.controller;

import com.mysql.cj.protocol.Message;
import io.op.total.model.UserService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class StudentController {
    @Autowired
    private UserService userService;

    public Boolean checkDate(String now) {
        Calendar cal = Calendar.getInstance();
        Date d = new Date(cal.getTimeInMillis());
        SimpleDateFormat testDate = new SimpleDateFormat("yyyyMMdd");

        String nowDate = testDate.format(d);

        nowDate = nowDate + userService.solt;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(nowDate.getBytes());

            String baseString = Base64.getEncoder().encodeToString(md.digest());

            if(baseString.equals(now)) {
                return true;
            }

            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String cryptoBase(String text) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(text.getBytes());

            result = Base64.getEncoder().encodeToString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/getStudent")
    public List<Map<String, Object>> getUsers() { return userService.getUsers(); }

    @GetMapping("/getLogs")
    public List<Map<String, Object>> getLogs() {
        return userService.getLogs();
    }

    @GetMapping("/getNowLogs")
    public List<Map<String, Object>> getNowLogs() { return userService.getNowLogs(); }

    @PostMapping("/addLog/{nowDay}")
    public Map insertTest(@RequestBody HashMap<String, String> params, @PathVariable("nowDay") String nowDay) {
        Map result = new HashMap<String, Object>();

        if(!checkDate(nowDay)) {
            result.put("result", "failed");
            result.put("msg", "주소가 맞지 않습니다.");

            return result;
        }

        if(userService.checkStudent(params.get("email"), cryptoBase(params.get("pw"))).size() > 0) {
            if(userService.checkToDayLog(params.get("email")).size() > 0) {
                result.put("result", "failed");
                result.put("msg", "이미 출석이 완료 되었습니다.");

                return result;
            }
            userService.insertLog(params.get("email"));
            result.put("result", "success");
            result.put("msg", "성공적으로 출석이 완료 되었습니다.");

            return result;
        }
        result.put("result", "failed");
        result.put("msg", "아이디 또는 비밀번호가 존재하지 않습니다.");

        return result;
    }
}
