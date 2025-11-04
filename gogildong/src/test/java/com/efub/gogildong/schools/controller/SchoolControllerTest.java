package com.efub.gogildong.schools.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class SchoolControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void 근처학교_조회API_200_반환() throws Exception {
        mvc.perform(get("/schools/nearby")
                        .param("lat", "37.5665")
                        .param("lng", "126.9780")
                        .param("radius", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schools").isArray());
    }

    @Test
    void 근처학교_조회API_404_에러() throws Exception {
        mvc.perform(get("/schools/nearby")
                .param("lat", "0")
                .param("lng", "0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void 검색어로_학교_조회API_200_반환() throws Exception {
        mvc.perform(get("/schools/search")
                        .param("query", "이화"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schools[*].schoolName",
                        Matchers.hasItem(Matchers.containsString("이화"))));

    }

    @Test
    void 검색어로_학교_조회API_404_반환() throws Exception {
        mvc.perform(get("/schools/search")
                        .param("query", "뷁똥"))
                .andExpect(status().isNotFound());
    }


}