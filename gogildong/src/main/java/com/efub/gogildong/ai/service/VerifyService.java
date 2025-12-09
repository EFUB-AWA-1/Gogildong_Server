package com.efub.gogildong.ai.service;

import com.efub.gogildong.ai.dto.request.VerifyRequest;
import com.efub.gogildong.ai.dto.response.VerifyResultResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VerifyService {

    private final ObjectMapper om = new ObjectMapper();

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.api-url}")
    private String apiUrl;

    @Value("${spring.ai.openai.model}")
    private String model;

    private final WebClient webClient = WebClient.builder().build();

    public VerifyResultResponse verifyImageWithOpenAI(String imageUrl, VerifyRequest req) {
        System.out.println("\n======= [VerifyService] START =======");

        try {
            // Prompt 준비
            String prompt = """
                    You are a strict facility verification AI.
                    Identify the facility type in the image and compare it to the user's report.
                    
                    IMPORTANT RULE:
                    - Door type verification must be performed ONLY IF the facility is RESTROOM or CLASSROOM.
                    - Otherwise, predicted_door_type must be null and is_door_matched must also be null.

                    Output ONLY valid JSON with fields:
                    predicted_facility_type, is_match, confidence, predicted_door_type, is_door_matched, reason.
                    """;

            // OpenAI 요청 body
            Map<String, Object> textContent = new LinkedHashMap<>();
            textContent.put("type", "input_text");
            textContent.put("text", prompt + " User reported facility type: " + req.getReportedFacilityType() + " User reported door type: " + req.getReportedDoorType());

            Map<String, Object> imageContent = new LinkedHashMap<>();
            imageContent.put("type", "input_image");
            imageContent.put("image_url", imageUrl);

            Map<String, Object> userMessage = new LinkedHashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", List.of(textContent, imageContent));

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", "gpt-4.1-mini");
            body.put("input", List.of(userMessage));

            System.out.println("\n--- [REQUEST BODY] ---");
            System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(body));

            // API 호출
            String response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("\n--- [RAW OPENAI RESPONSE] ---");
            System.out.println(response);

            // JSON 파싱
            JsonNode root = om.readTree(response);
            JsonNode outputNode = root.path("output").get(0);
            JsonNode contentNode = outputNode.path("content").get(0);

            // output_text 안의 text를 문자열로 바로 가져오기
            String text = contentNode.path("text").asText();
            text = text.replaceAll("(?s)^```json", "")
                    .replaceAll("(?s)```$", "")
                    .trim();
            JsonNode json = om.readTree(text);

            // DTO 매핑
            VerifyResultResponse res = new VerifyResultResponse();
            res.setPredictedType(json.path("predicted_facility_type").asText());
            res.setMatched(json.path("is_match").asBoolean());
            res.setConfidence(json.path("confidence").asDouble());
            res.setReason(json.path("reason").asText());

            // Door 검증 facility type 조건 적용 (RESTROOM, CLASSROOM)
            String reportedType = req.getReportedFacilityType();

            boolean needDoorCheck =
                    "RESTROOM".equalsIgnoreCase(reportedType) ||
                            "CLASSROOM".equalsIgnoreCase(reportedType);

            if (needDoorCheck) {
                res.setPredictedDoorType(json.path("predicted_door_type").asText(null));
                res.setIsDoorMatched(json.path("is_door_matched").asBoolean());
            } else {
                res.setPredictedDoorType(null);
                res.setIsDoorMatched(null);
            }

            System.out.println("\n======= [VerifyService] END SUCCESS =======");
            return res;

        } catch (Exception e) {
            System.out.println("\n======= [VerifyService] ERROR =======");
            e.printStackTrace();
            throw new RuntimeException("OpenAI Verify Error: " + e.getMessage(), e);
        }
    }
}
