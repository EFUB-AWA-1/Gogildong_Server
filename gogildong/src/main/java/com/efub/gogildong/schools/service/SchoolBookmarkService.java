package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.SchoolBookmark;
import com.efub.gogildong.schools.repository.SchoolBookmarkRepository;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SchoolBookmarkService {

    private final SchoolBookmarkRepository schoolBookmarkRepository;
    private final EntityFinder finder;

    /*
    * 사용자가 학교를 즐겨찾기 합니다.
    * */
    @Transactional
    public void createBookmark(String loginId, Long schoolId) {
        // 사용자 및 학교 객체 가져오기
        User user = finder.getUserByLoginId(loginId);
        School school = finder.getSchoolById(schoolId);

        // 해당 사용자가 이미 학교를 즐겨찾기 했는지 확인
        validateNoBookmarked(school, user);

        // 즐겨찾기 생성 후 저장
        SchoolBookmark bookmark = new SchoolBookmark(school, user);
        schoolBookmarkRepository.save(bookmark);
    }

    /*
     * 사용자가 학교를 즐겨찾기 취소합니다.
     * */
    @Transactional
    public void deleteBookmark(String loginId, Long schoolId) {
        // 사용자 및 학교 객체 가져오기
        User user = finder.getUserByLoginId(loginId);
        School school = finder.getSchoolById(schoolId);

        // 즐겨찾기 삭제
        SchoolBookmark bookmark = findBookmarkBySchoolAndUser(school, user);
        schoolBookmarkRepository.delete(bookmark);
    }

    /*
    * 해당 사용자의 학교에 대한 즐겨찾기 객체 반환
    * */
    private SchoolBookmark findBookmarkBySchoolAndUser(School school, User user) {
        return schoolBookmarkRepository.findBySchoolAndUser(school, user)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_BOOKMARK_NOT_FOUND));
    }

    /*
    * 해당 사용자가 학교를 이미 즐겨찾기 했는지 확인
    * */
    private void validateNoBookmarked(School school, User user) {
        if(!schoolBookmarkRepository.existsBySchoolAndUser(school, user)) {
            throw new GoGildongException(ExceptionCode.SCHOOL_ALREADY_BOOKMARKED);
        }
    }
}
