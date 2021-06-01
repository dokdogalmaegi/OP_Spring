package io.op.total.controller;

import io.op.total.model.UserService;
import io.op.total.model.UtilServiceImpl;
import io.op.total.vo.Student;
import io.op.total.vo.UpdateStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AdminController {
    @Autowired
    private UserService userService;

    private final UtilServiceImpl utilService = new UtilServiceImpl();

    @PostMapping("/updateStudent")
    public Map updateStudent(@RequestBody HashMap<String, String> params) {
        Map result = new HashMap<String, Object>();

        if(userService.checkAdmin(params.get("adminEmail"), utilService.cryptoBase(params.get("adminKey"))).size() == 0) {
            result.put("result", "failed");
            result.put("msg", "당신은 어드민이 아닙니다.");

            return result;
        }

        String email = params.get("email");
        String pw = null; String phone = null; boolean flag = false;

        if(params.get("pw").toString().equals("")) pw = "";
        else pw = utilService.cryptoBase(params.get("pw"));

        String nm = params.get("nm");
        int grade = Integer.parseInt(params.get("grade"));
        int class_num = Integer.parseInt(params.get("class_num"));
        int num = Integer.parseInt(params.get("num"));

        if(params.get("phone").toString().equals("")) phone = "";
        else phone = params.get("phone");

        if(params.get("flag").toString().equals("true")) flag = true;

        String changeEmail = params.get("changeEmail");

        if(email.equals("") ||  nm.equals("") || Integer.toString(grade).equals("") || Integer.toString(class_num).equals("") || Integer.toString(num).equals("")) {
            result.put("result", "failed");
            result.put("msg", "값이 누락 되었습니다.");

            return result;
        }
        try {

            UpdateStudent vo = new UpdateStudent(email, pw, nm, grade, class_num, num, phone, flag, changeEmail);
            userService.updateStudentCsharp(vo);

            result.put("result", "success");
            result.put("msg", "정상적으로 학생이 수정 되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "failed");
            result.put("msg", "알 수 없는 에러가 발생 했습니다.");
        }

        return result;
    }

    @PostMapping("/addStudent")
    public Map insertStudent(@RequestBody HashMap<String, String> params) {
        Map result = new HashMap<String, Object>();

        if(userService.checkAdmin(params.get("adminEmail"), utilService.cryptoBase(params.get("adminKey"))).size() == 0) {
            result.put("result", "failed");
            result.put("msg", "당신은 어드민이 아닙니다.");

            return result;
        }

        String email = params.get("email");
        String pw; String phone;

        if(params.get("pw").toString().equals("")) pw = "";
        else pw = utilService.cryptoBase(params.get("pw"));

        String nm = params.get("nm");
        int grade = Integer.parseInt(params.get("grade"));
        int class_num = Integer.parseInt(params.get("class_num"));
        int num = Integer.parseInt(params.get("num"));

        if(params.get("phone").toString().equals("")) phone = "";
        else phone = params.get("phone");

        if(email.equals("") ||  nm.equals("") || Integer.toString(grade).equals("") || Integer.toString(class_num).equals("") || Integer.toString(num).equals("")) {
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

            Student vo = new Student(email, pw, nm, grade, class_num, num, phone);
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

    @PostMapping("/deleteStudent")
    public Map deleteStudent(@RequestBody HashMap<String, String> params){
        Map result = new HashMap<String, Object>();

        if(userService.checkAdmin(params.get("adminEmail"), utilService.cryptoBase(params.get("adminKey"))).size() == 0) {
            result.put("result", "failed");
            result.put("msg", "당신은 어드민이 아닙니다.");

            return result;
        }

        String email = params.get("email");

        if(email.equals("")) {
            result.put("result", "failed");
            result.put("msg", "이메일이 누락 되었습니다.");

            return result;
        }
        try {

            if(userService.checkInsertStudent(email).size() <= 0) {
                result.put("result", "failed");
                result.put("msg", "존재하지 않는 이메일입니다.");

                return result;
            }
            userService.deleteStudent(email);

            result.put("result", "success");
            result.put("msg", "정상적으로 학생이 삭제 되었습니다.");
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
            result.put("grade", sqlMap.get(0).get("grade"));
            result.put("class", sqlMap.get(0).get("class_num"));
            result.put("permission", sqlMap.get(0).get("permission"));

            return result;
        }

        result.put("result", "failed");
        result.put("check", "False");

        return result;
    }
}
