package io.op.total.controller;

import io.op.total.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class StudentController {
    public String getNowTime() {
        Calendar cal = Calendar.getInstance();
        Date d = new Date(cal.getTimeInMillis());
        SimpleDateFormat testDate = new SimpleDateFormat("yyyyMMdd");

        String result = testDate.format(d);

        return result;
    }

    @Autowired
    private UserService userService;

    @GetMapping("/getStudent")
    public List<Map<String, Object>> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/getLogs")
    public List<Map<String, Object>> getLogs() {
        return userService.getLogs();
    }

    @GetMapping("/get/{test}")
    public String getTest(@PathVariable("test") String test) {
        String raw = getNowTime();

        String hex = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(raw.getBytes());

            hex = String.format("%064x", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hex;
//        return "Hello World! " + test + " " + getNowTime();
    }
}
