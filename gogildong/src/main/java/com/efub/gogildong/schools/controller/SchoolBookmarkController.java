package com.efub.gogildong.schools.controller;

import com.efub.gogildong.schools.domain.SchoolBookmark;
import com.efub.gogildong.schools.service.SchoolBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class SchoolBookmarkController {

    private final SchoolBookmarkService bookmarkService;

    /*
    * 사용자가 학교를 즐겨찾기 합니다.
    * */
    @PostMapping("/{schoolId}")
    public ResponseEntity<Void> createBookmark(Authentication authentication, @PathVariable Long schoolId) {
        bookmarkService.createBookmark(authentication.getName(), schoolId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
     * 사용자가 학교에 대한 즐겨찾기를 취소합니다.
     * */
    @DeleteMapping("/{schoolId}")
    public ResponseEntity<Void> deleteBookmark(Authentication authentication, @PathVariable Long schoolId) {
        bookmarkService.deleteBookmark(authentication.getName(), schoolId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
