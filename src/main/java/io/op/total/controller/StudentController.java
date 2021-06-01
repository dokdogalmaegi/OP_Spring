package io.op.total.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.op.total.model.UserService;
import io.op.total.model.UtilServiceImpl;
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
        List<Map<String, Object>> sqlMap = null;
        Map result = new HashMap<String, Object>();

        try {
            sqlMap = userService.getNowLogs();
        } catch (Exception e) {
            e.printStackTrace();

            result.put("result", "failed");
            result.put("msg", "잘못된 요청입니다.");

            return result;
        }


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

    // 당일 결석한 학생 전부를 조회
    @PostMapping("/getNowNotLogs/{platform}")
    public Map getNowNotLogs(@PathVariable String platform) {
        List<Map<String, Object>> sqlMap = null;
        Map result = new HashMap<String, Object>();

        try {
            sqlMap = userService.getNowNotLogs();
        } catch (Exception e) {
            e.printStackTrace();

            result.put("result", "failed");
            result.put("msg", "잘못된 요청입니다.");

            return result;
        }

        if(platform.equals("kakao")) {
            String simpleText = " 학년 | 반 | 수업 | 이름 \n";

            int count = 0;

            if(sqlMap.size() > 0) {
                String onlineFlag = "오프라인";
                for(Map<String, Object> vo : sqlMap) {
                    if((Boolean) vo.get("onlineFlag")) onlineFlag = "온라인";

                    if(count == sqlMap.size() - 1) simpleText += vo.get("grade").toString() + " | " + vo.get("class").toString() + " | " + onlineFlag + " | " + vo.get("nm").toString();
                    else simpleText += vo.get("grade").toString() + " | " + vo.get("class").toString() + " | " + onlineFlag + " | " + vo.get("nm").toString() + "\n";
                    count++;
                }
                result = utilService.kakaoChat(simpleText);

                return result;
            }
            simpleText = "오늘 결석한 인원은 없습니다.";
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

    // 당일 결석한 학생을 Request에 담겨진 학년과 반으로 조회
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

            List<Map<String, Object>> sqlMap = null;
            try {
                sqlMap = userService.getClassNotNowLogs(grade.getAsInt(), class_num.getAsInt());
            } catch (Exception e) {
                e.printStackTrace();

                result.put("result", "failed");
                result.put("msg", "잘못된 요청입니다.");

                return result;
            }
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

            List<Map<String, Object>> nowStudentList = null;
            try {
                nowStudentList = userService.getClassNotNowLogs(grade.getAsInt(), class_num.getAsInt());
            } catch (Exception e) {
                e.printStackTrace();

                result.put("result", "failed");
                result.put("msg", "잘못된 요청입니다.");

                return result;
            }

            result.put("Students", nowStudentList);

            return result;
        }
        result.put("result", "failed");
        result.put("msg", "플랫폼이 선택되지 않았습니다.");

        return result;
    }

    // 당일 출석한 학생을 Request에 담겨진 학년과 반으로 조회
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

            List<Map<String, Object>> sqlMap = null;
            try {
                sqlMap = userService.getClassNowLogs(grade.getAsInt(), class_num.getAsInt());
            } catch (Exception e) {
                e.printStackTrace();

                result.put("result", "failed");
                result.put("msg", "잘못된 요청입니다.");

                return result;
            }
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

            try {
                List<Map<String, Object>> sqlMap = userService.getClassNowLogs(grade.getAsInt(), class_num.getAsInt());

                result.put("Students", sqlMap);
            } catch (Exception e) {
                e.printStackTrace();

                result.put("result", "failed");
                result.put("msg", "잘못된 요청입니다.");
            }
            return result;
        }
        result.put("result", "failed");
        result.put("msg", "플랫폼이 선택되지 않았습니다.");

        return result;
    }

    @PostMapping("/addLog/{nowDay}")
    public Map insertAttendance(@RequestBody HashMap<String, String> params, @PathVariable("nowDay") String nowDay) {
        Map result = new HashMap<String, Object>();
        Boolean changed = false;

        if(!utilService.checkDate(nowDay)) {
            result.put("result", "failed");
            result.put("msg", "주소가 맞지 않습니다.");

            return result;
        }

        if(params.get("isNull").equals("True")) {
            try {
                userService.updateStudent(utilService.cryptoBase(params.get("pw")), params.get("phone"), params.get("email"));;

                changed = !changed;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(userService.checkStudent(params.get("email"), utilService.cryptoBase(params.get("pw")), params.get("phone")).size() > 0) {
            List<Map<String, Object>> logs = null;
            try {
                logs = userService.checkToDayLog(params.get("email"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(logs.size() > 0) {
                result.put("result", "already");
                result.put("msg", "이미 출석이 완료 되었습니다.");
                result.put("time", logs.get(0).get("time"));

                return result;
            }

            try {
                userService.insertLog(params.get("email"));
                result.put("result", "success");
                if(changed) result.put("msg", "출석과 계정 등록이 완료 되었습니다.");
                else result.put("msg", "성공적으로 출석이 완료 되었습니다.");

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.put("result", "failed");
                result.put("msg", "잘못된 요청입니다.");
            }
        }
        result.put("result", "failed");
        result.put("msg", "아이디 또는 비밀번호가 존재하지 않습니다.");

        return result;
    }

    @PostMapping("/checkOnline")
    public Map checkOnline(@RequestBody HashMap<String, String> params) {
        Map result = new HashMap<String, Object>();

        try {
            List<Map<String, Object>> sqlMap = userService.checkOnline(params.get("email"));

            if(sqlMap.size() > 0) {
                List<Map<String, Object>> sqlMap2 = userService.checkInsertStudent(params.get("email"));
                result.put("result", "success");
                result.put("online_flag", sqlMap.get(0).get("onlineFlag"));
                result.put("pw", sqlMap2.get(0).get("pw"));
                result.put("phone", sqlMap2.get(0).get("phone"));
            } else {
                result.put("result", "failed");
                result.put("msg", "이메일과 일치하는 학생이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "failed");
            result.put("msg", "잘못된 요청입니다.");
        }

        return result;
    }
}
