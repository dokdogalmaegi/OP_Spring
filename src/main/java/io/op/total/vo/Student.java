package io.op.total.vo;

import lombok.*;

@Data
public class Student {
    private @NonNull String email;
    private @NonNull String pw;
    private @NonNull String nm;
    private @NonNull int grade;
    private @NonNull int class_num;
    private @NonNull int num;
    private @NonNull String phone;
}
