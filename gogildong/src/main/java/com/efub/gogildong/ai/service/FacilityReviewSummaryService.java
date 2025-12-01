package com.efub.gogildong.ai.service;

import com.efub.gogildong.ai.dto.response.FacilityReviewSummaryResponse;
import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.common.OpenAiApiClientErrorException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityReviewSummaryService {
    private final OpenAiChatModel openAiChatModel;
    private final FacilityRepository facilityRepository;

    /*
    * 시설 AI 요약 - 비동기
    * */
    @Async
    public void summarizeFacilityReview(Facility facility) {
        List<FacilityReview> reviews = facility.getReviews();
        // 리뷰가 세개 이하면 ai 요약 X
        if(reviews.size() <= 3) {
            return;
        }

       String facilityType = facility.getFacilityType().name();

        // - (좋아요 n) 리뷰 내용
        String reviewListText = facility.getReviews().stream()
                .map(review -> "- (좋아요 " + review.getLikeCount() + ") " + review.getReviewText())
                .collect(Collectors.joining("\n"));
        try {
        ChatClient chatClient = ChatClient.create(openAiChatModel);

        SystemMessage systemMessage = new SystemMessage("""
                            당신은 이동약자를 위한 접근성 평가 전문가입니다.
                            아래 "시설 유형"과 "리뷰 목록"을 기반으로, 이동약자에게 중요한 관점으로 리뷰를 2~3줄로 요약하세요.
                
                            ## 요약 지침
                            - 불필요한 감상/의견은 배제하고 객관적 사실 중심으로 작성
                            - 부드러운 어조로 끝맺음
                            - 공격적/불법적/모욕적 발언 포함 리뷰 배제
                            - 시설 유형에 따라 다음 기준을 우선적으로 반영:
                              - 화장실: 문 폭, 손잡이, 좌변기 사용 편의성, 바닥 미끄러움, 접근 경로
                              - 엘리베이터: 이동약자 우선 사용 여부, 작동 여부, 내부 공간, 버튼 높이, 도어 속도, 접근 경로
                              - 교실/강의실: 출입로 폭, 경사, 접근성 방해 요소
                              - 그 외 시설: 이동약자가 이용할 때 영향을 주는 물리적 요소 중심으로
                            - 리뷰의 공통 패턴을 중심으로 핵심만 정리
                            - 100자 이내로 요약
                
                            ## 출력 형식
                            아래 JSON 형식으로만 응답:
                            {
                                "summary": ""
                            }
                
                """);

        UserMessage userMessage = new UserMessage("""
                    사용자가 다음 시설에 관한 리뷰 요약을 요청했습니다.
                    
                    [시설 유형]
                    %s
                    
                    [리뷰 목록]
                    %s
                    """.formatted(facilityType, reviewListText));

        AssistantMessage assistantMessage = new AssistantMessage("");

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage));

        FacilityReviewSummaryResponse response = chatClient.prompt(prompt)
                .call()
                .entity(FacilityReviewSummaryResponse.class);

        // 시설 리뷰 업데이트
        facility.updateSummary(response.getSummary());
        facilityRepository.save(facility);
        } catch (OpenAiApiClientErrorException e) {
            // 모델 호출 실패
            throw new GoGildongException(ExceptionCode.AI_REQUEST_FAILED);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof JsonProcessingException) {
                throw new GoGildongException(ExceptionCode.AI_RESPONSE_PARSE_ERROR);
            }
            throw new GoGildongException(ExceptionCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new GoGildongException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }
}
