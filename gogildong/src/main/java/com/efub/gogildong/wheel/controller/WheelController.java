package com.efub.gogildong.wheel.controller;

import com.efub.gogildong.auth.dto.CustomUserDetails;
import com.efub.gogildong.wheel.dto.WheelList;
import com.efub.gogildong.wheel.dto.request.WheelchairCreateRequest;
import com.efub.gogildong.wheel.dto.response.BookmarkToggleResponse;
import com.efub.gogildong.wheel.dto.response.WheelchairResponse;
import com.efub.gogildong.wheel.service.WheelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wheel")
@RequiredArgsConstructor
public class WheelController {

    private final WheelService wheelService;

    @GetMapping
    public ResponseEntity<WheelList> getWheelList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUserId();
        WheelList wheelList = wheelService.getWheelList(userId);
        return ResponseEntity.ok(wheelList);
    }

    @PostMapping
    public ResponseEntity<WheelchairResponse> createWheelchair(
            @RequestBody @Valid WheelchairCreateRequest request
    ) {
        WheelchairResponse response = wheelService.createWheelchair(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{wheelchairId}")
    public ResponseEntity<BookmarkToggleResponse> toggleBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long wheelchairId) {

        Long userId = userDetails.getUserId();
        boolean bookmarked = wheelService.toggleBookmark(userId, wheelchairId);
        return ResponseEntity.ok(new BookmarkToggleResponse(bookmarked));
    }

    @GetMapping("/search")
    public ResponseEntity<WheelList> searchWheelchairs(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = userDetails.getUserId();
        WheelList wheelList = wheelService.searchWheelchairs(userId, keyword, page, size);
        return ResponseEntity.ok(wheelList);
    }

    @DeleteMapping("/{wheelchairId}")
    public ResponseEntity<Void> deleteBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long wheelchairId) {

        Long userId = userDetails.getUserId();
        wheelService.deleteBookmark(userId, wheelchairId);
        return ResponseEntity.noContent().build();
    }
}
