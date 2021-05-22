package io.op.total.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UtilServiceImpl implements UtilService {

    @Autowired
    private UserService userService;

    @Override
    public Boolean checkDate(String now) {
        Calendar cal = Calendar.getInstance();
        Date d = new Date(cal.getTimeInMillis());
        SimpleDateFormat testDate = new SimpleDateFormat("yyyyMMdd");

        String nowDate = testDate.format(d);

        nowDate = nowDate + userService.solt;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(nowDate.getBytes());

            String hex = String.format("%0128x", new BigInteger(1, md.digest()));

            if(hex.equals(now)) {
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

        List<Map<String, Object>> list = new ArrayList<>();

        textMap.put("text", simpleText);
        simpleTextMap.put("simpleText", textMap);

        list.add(simpleTextMap);

        outputs.put("outputs", list);
        result.put("template", outputs);

        return result;
    }
}
