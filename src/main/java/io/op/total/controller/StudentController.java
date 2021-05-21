package io.op.total.controller;

import com.mysql.cj.protocol.Message;
import io.op.total.model.UserService;
import io.op.total.vo.Student;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
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

    public Map kakaoChat(String simpleText) {
        Map result = new HashMap<String, Object>();
        Map outputs = new HashMap<String, Object>();
        Map simpleTextMap = new HashMap<String, Object>();
        Map textMap = new HashMap<String, Object>();
        result.put("version", "2.0");

        List<Map<String, Object>> sqlMap = userService.getNowLogs();

        List<Map<String, Object>> list = new ArrayList<>();

        textMap.put("text", simpleText);
        simpleTextMap.put("simpleText", textMap);

        list.add(simpleTextMap);

        outputs.put("outputs", list);
        result.put("template", outputs);

        return result;
    }

    @GetMapping("/getStudent")
    public List<Map<String, Object>> getUsers() { return userService.getUsers(); }

    @GetMapping("/getLogs")
    public List<Map<String, Object>> getLogs() {
        return userService.getLogs();
    }

    @PostMapping("/getNowLogs")
    public Map getNowLogs() {
        List<Map<String, Object>> sqlMap = userService.getNowLogs();
        String simpleText = " 이름   | 출석시간\n";

        int count = 0;

        for(Map<String, Object> vo : sqlMap) {
            if(count == sqlMap.size() - 1) simpleText += vo.get("nm").toString() + " | " + vo.get("time").toString();
            else simpleText += vo.get("nm").toString() + " | " + vo.get("time").toString() + "\n";
            count++;
        }

        return kakaoChat(simpleText);
    }

    // 학생 추가 - Admin Client 전용
    @PostMapping("/addStudent")
    public Map insertStudent(@RequestBody HashMap<String, String> params) {
        Map result = new HashMap<String, Object>();

        if(userService.checkAdmin(params.get("adminEmail"), cryptoBase(params.get("adminKey"))).size() == 0) {
            result.put("result", "failed");
            result.put("msg", "당신은 어드민이 아닙니다.");

            return result;
        }

        String email = params.get("email"); String pw = cryptoBase(params.get("pw")); String nm = params.get("nm"); int grade = Integer.parseInt(params.get("grade")); int class_num = Integer.parseInt(params.get("class_num")); int num = Integer.parseInt(params.get("num"));

        if(email.equals("") || pw.equals("") || nm.equals("") || Integer.toString(grade).equals("") || Integer.toString(class_num).equals("") || Integer.toString(num).equals("") ) {
            result.put("result", "failed");
            result.put("msg", "값이 누락 되었습니다.");

            return result;
        }
        try {

            if(userService.checkInsertStudent(email).size() > 0) {
                result.put("result", "failed");
                result.put("msg", "이미 존재하는 이메일입니다.");

                return result;
            }

            Student vo = new Student(email, pw, nm, grade, class_num, num);
            userService.insertStudent(vo);

            result.put("result", "success");
            result.put("msg", "정상적으로 학생이 추가 되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "failed");
            result.put("msg", "알 수 없는 에러가 발생 했습니다.");
        }

        return result;
    }

    @PostMapping("/addLog/{nowDay}")
    public Map insertAttendance(@RequestBody HashMap<String, String> params, @PathVariable("nowDay") String nowDay) {
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
