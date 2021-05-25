package io.op.total.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.op.total.model.UserService;
import io.op.total.model.UtilServiceImpl;
import io.op.total.vo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class StudentController {
    @Autowired
    private UserService userService;

    private final UtilServiceImpl utilService = new UtilServiceImpl();

    @GetMapping("/getStudent")
    public List<Map<String, Object>> getUsers() { return userService.getUsers(); }

    @GetMapping("/getLogs")
    public List<Map<String, Object>> getLogs() {
        return userService.getLogs();
    }

    // 당일 출석한 학생 전부를 조회
    @PostMapping("/getNowLogs/{platform}")
    public Map getNowLogs(@PathVariable String platform) {
        List<Map<String, Object>> sqlMap = userService.getNowLogs();
        Map result = new HashMap<String, Object>();
        if(platform.equals("kakao")) {
            String simpleText = " 이름   | 출석시간\n";

            int count = 0;

            if(sqlMap.size() > 0) {
                for(Map<String, Object> vo : sqlMap) {
                    if(count == sqlMap.size() - 1) simpleText += vo.get("nm").toString() + " | " + vo.get("time").toString();
                    else simpleText += vo.get("nm").toString() + " | " + vo.get("time").toString() + "\n";
                    count++;
                }
                result = utilService.kakaoChat(simpleText);

                return result;
            }
            simpleText = "오늘 출석한 인원은 없습니다.";
            result = utilService.kakaoChat(simpleText);
            return result;
        }
        if(platform.equals("csharp")) {
            result.put("Students", sqlMap);

            return result;
        }
        result.put("result", "failed");
        result.put("msg", "플랫폼이 선택되지 않았습니다.");

        return result;
    }

    @PostMapping("/getClassNowNotLogs/{platform}")
    public Map getClassNowNotLogs(@RequestBody String params, @PathVariable String platform) {
        Map result = new HashMap<String, Object>();
        if(platform.equals("kakao")) {
            JsonParser jsonParser = new JsonParser();

            JsonElement class_num = jsonParser.parse(params)
                    .getAsJsonObject().get("action")
                    .getAsJsonObject().get("params")
                    .getAsJsonObject().get("class_num");

            JsonElement grade = jsonParser.parse(params)
                    .getAsJsonObject().get("action")
                    .getAsJsonObject().get("params")
                    .getAsJsonObject().get("grade");

            List<Map<String, Object>> sqlMap = userService.getClassNotNowLogs(grade.getAsInt(), class_num.getAsInt());
            String simpleText = " 이름\n";

            int count = 0;

            if(sqlMap.size() > 0) {
                for(Map<String, Object> vo : sqlMap) {
                    if(count == sqlMap.size() - 1) simpleText += vo.get("nm").toString();
                    else simpleText += vo.get("nm").toString() + "\n";
                    count++;
                }

                result = utilService.kakaoChat(simpleText);
                return result;
            }
            simpleText = "오늘 " + grade.toString().replaceAll("\"", "") + "학년" + " " + class_num.toString().replaceAll("\"", "") + "반에 출석하지 않은 인원은 없습니다.";
            result = utilService.kakaoChat(simpleText);

            return result;
        }
        if(platform.equals("csharp")) {
            JsonParser jsonParser = new JsonParser();

            JsonElement class_num = jsonParser.parse(params)
                    .getAsJsonObject().get("class_num");

            JsonElement grade = jsonParser.parse(params)
                    .getAsJsonObject().get("grade");

            List<Map<String, Object>> nowStudentList = userService.getClassNotNowLogs(grade.getAsInt(), class_num.getAsInt());

            result.put("Students", nowStudentList);

            return result;
        }
        result.put("result", "failed");
        result.put("msg", "플랫폼이 선택되지 않았습니다.");

        return result;
    }

    @PostMapping("/getClassNowLogs/{platform}")
    public Map getClassNowLogs(@RequestBody String params, @PathVariable String platform) {
        Map result = new HashMap<String, Object>();
        if(platform.equals("kakao")) {
            JsonParser jsonParser = new JsonParser();

            JsonElement class_num = jsonParser.parse(params)
                    .getAsJsonObject().get("action")
                    .getAsJsonObject().get("params")
                    .getAsJsonObject().get("class_num");

            JsonElement grade = jsonParser.parse(params)
                    .getAsJsonObject().get("action")
                    .getAsJsonObject().get("params")
                    .getAsJsonObject().get("grade");

            List<Map<String, Object>> sqlMap = userService.getClassNowLogs(grade.getAsInt(), class_num.getAsInt());
            String simpleText = " 이름   | 출석시간\n";

            int count = 0;

            if(sqlMap.size() > 0) {
                for(Map<String, Object> vo : sqlMap) {
                    if(count == sqlMap.size() - 1) simpleText += vo.get("nm").toString() + " | " + vo.get("time").toString();
                    else simpleText += vo.get("nm").toString() + " | " + vo.get("time").toString() + "\n";
                    count++;
                }

                result = utilService.kakaoChat(simpleText);
                return result;
            }
            simpleText = "오늘 " + grade.toString().replaceAll("\"", "") + "학년" + " " + class_num.toString().replaceAll("\"", "") + "반에 출석한 인원은 없습니다.";
            result = utilService.kakaoChat(simpleText);

            return result;
        }
        if(platform.equals("csharp")) {
            JsonParser jsonParser = new JsonParser();

            JsonElement class_num = jsonParser.parse(params)
                    .getAsJsonObject().get("class_num");

            JsonElement grade = jsonParser.parse(params)
                    .getAsJsonObject().get("grade");

            List<Map<String, Object>> sqlMap = userService.getClassNowLogs(grade.getAsInt(), class_num.getAsInt());

            result.put("Students", sqlMap);

            return result;
        }
        result.put("result", "failed");
        result.put("msg", "플랫폼이 선택되지 않았습니다.");

        return result;
    }

    // 학생 추가 - Admin Client 전용
    @PostMapping("/addStudent")
    public Map insertStudent(@RequestBody HashMap<String, String> params) {
        Map result = new HashMap<String, Object>();

        if(userService.checkAdmin(params.get("adminEmail"), utilService.cryptoBase(params.get("adminKey"))).size() == 0) {
            result.put("result", "failed");
            result.put("msg", "당신은 어드민이 아닙니다.");

            return result;
        }

        String email = params.get("email"); String pw = utilService.cryptoBase(params.get("pw")); String nm = params.get("nm"); int grade = Integer.parseInt(params.get("grade")); int class_num = Integer.parseInt(params.get("class_num")); int num = Integer.parseInt(params.get("num")); String usim = params.get("usim");

        if(email.equals("") || pw.equals("") || nm.equals("") || Integer.toString(grade).equals("") || Integer.toString(class_num).equals("") || Integer.toString(num).equals("") || usim.equals("")) {
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

            Student vo = new Student(email, pw, nm, grade, class_num, num, usim);
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

    @PostMapping("/checkTeacher")
    public Map checkTeacher(@RequestBody HashMap<String, String> params) {
        Map result = new HashMap<String, Object>();

        List<Map<String, Object>> sqlMap = userService.checkAdmin(params.get("email").toString(), utilService.cryptoBase(params.get("pw").toString()));

        if(sqlMap.size() > 0) {
            result.put("result", "success");
            result.put("check", "True");
            result.put("name", sqlMap.get(0).get("nm"));

            return result;
        }

        result.put("result", "failed");
        result.put("check", "False");

        return result;
    }

    @PostMapping("/addLog/{nowDay}")
    public Map insertAttendance(@RequestBody HashMap<String, String> params, @PathVariable("nowDay") String nowDay) {
        Map result = new HashMap<String, Object>();

        if(!utilService.checkDate(nowDay)) {
            result.put("result", "failed");
            result.put("msg", "주소가 맞지 않습니다.");

            return result;
        }

        System.out.println(userService.checkStudent(params.get("email"), utilService.cryptoBase(params.get("pw")), utilService.cryptoBase(params.get("usim"))));

        if(userService.checkStudent(params.get("email"), utilService.cryptoBase(params.get("pw")), utilService.cryptoBase(params.get("usim"))).size() > 0) {
            List<Map<String, Object>> logs = userService.checkToDayLog(params.get("email"));
            if(logs.size() > 0) {
                result.put("result", "already");
                result.put("msg", "이미 출석이 완료 되었습니다.");
                result.put("time", logs.get(0).get("time"));

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
