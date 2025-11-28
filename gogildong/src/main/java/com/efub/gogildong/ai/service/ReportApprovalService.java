package com.efub.gogildong.ai.service;

import com.efub.gogildong.ai.dto.response.AccessDecisionResponse;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.dto.request.SchoolViewRequestRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.common.OpenAiApiClientErrorException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportApprovalService {

    private final OpenAiChatModel openAiChatModel;

    public AccessDecisionResponse decide(SchoolViewRequestRequest request) {
        try {
            ChatClient chatClient = ChatClient.create(openAiChatModel);

            // 메시지
            SystemMessage systemMessage = new SystemMessage("""
                    너는 학교 접근성 정보 플랫폼에서 학교에 대한 접근성 정보 열람 요청 권한 심사 AI야.
                    
                    [심사 규칙]
                    1. 사용자의 목적이 합리적인 경우만 승인한다.
                        예: 시험 응시 / 연구 / 장애 학생 도움 등
                    2. 장난 등의 불필요한 이유는 거절한다.
                    3. 공격적/불법적/모욕적 발언 포함 시 거절한다.
                    4. 승인/거절 여부를 반드시 논리적 이유와 함께 제공한다.
                    5. 응답은 **항상 JSON 형식**으로 반환한다.
                    6. JSON 외 다른 문자는 절대 포함하지 않는다.
                    7. 승인/거절 사유는 50자 이내로 작성한다.
                    8. 예시는 아래와 같이 작성:
                    
                    예시 1) 승인
                    {
                        "approved" : true,
                        "reason" : "친구가 휠체어 이용 학생이라 이동 경로를 파악하려고 함"
                    }
                    
                    예시 2) 거절
                    {
                        "approved" : false,
                        "reason" : "사유가 불명확하고 장난성으로 보임"
                    }
                    
                    응답:
                    {
                      "approved": <true/false>,
                      "reason": "<승인/거절 사유>"
                    }
                    """);
            UserMessage userMessage = new UserMessage("""
                    사용자가 학교에 대한 접근성 정보 열람을 요청했습니다.
                    
                    [요청 정보]
                    - 카테고리: %s
                    - 요청 이유 : %s
                    """.formatted(request.getReasonCategory().name(), request.getRequestReason()));
            AssistantMessage assistantMessage = new AssistantMessage("");

            // 프롬프트
            Prompt prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage));

            return chatClient.prompt(prompt)
                    .call()
                    .entity(AccessDecisionResponse.class);
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
