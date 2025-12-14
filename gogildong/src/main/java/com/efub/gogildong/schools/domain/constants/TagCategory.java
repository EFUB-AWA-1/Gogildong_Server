package com.efub.gogildong.schools.domain.constants;

import com.efub.gogildong.schools.domain.TagName;

public enum TagCategory {
    all, restroom, elevator, classroom, etc;

    /*
    * param 값인 tagCategory를 데이터베이스에 저장된 tag 이름으로 변경합니다.
    * */
    public TagName toTagName() {
        return switch (this) {
            case restroom -> TagName.장애인_화장실;
            case elevator -> TagName.엘리베이터;
            case classroom -> TagName.교실;
            case etc -> TagName.기타;
            case all -> null;
        };
    }
}
