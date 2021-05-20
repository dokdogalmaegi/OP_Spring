package io.op.total.controller;

import io.op.total.model.UserService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getStudent")
    public List<Map<String, Object>> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/getLogs")
    public List<Map<String, Object>> getLogs() {
        return userService.getLogs();
    }

    @GetMapping("/get/{nowDay}")
    public String getTest(@PathVariable("nowDay") String nowDay) {
        if(checkDate(nowDay)) {
            return "값이 일치 합니다.";
        } else {
            return "값이 일치 하지 않습니다.";
        }
    }

    @GetMapping("/set/{email}")
    public String insertTest(@PathVariable("email") String email) {
        userService.insertLog(email);

        return "성공 " + email;
    }
}
