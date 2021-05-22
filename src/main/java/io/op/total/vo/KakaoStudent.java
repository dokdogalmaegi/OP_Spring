package io.op.total.vo;

import lombok.Data;
import lombok.NonNull;

@Data
public class KakaoStudent {
    private @NonNull int grade;
    private @NonNull int class_num;
}
