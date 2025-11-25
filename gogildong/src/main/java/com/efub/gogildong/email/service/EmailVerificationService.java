package com.efub.gogildong.email.service;

import com.efub.gogildong.email.domain.EmailVerificationCode;
import com.efub.gogildong.email.repository.EmailVerificationCodeRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationCodeRepository codeRepository;
    private final EmailSender emailSender;

    private String generateCode() {
        Random random = new Random();
        int num = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(num);
    }

    // 인증번호 발송
    @Transactional
    public void sendCode(String email) {
        String code = generateCode();

        EmailVerificationCode entity = new EmailVerificationCode();
        // 필드 세팅 (생성자 대신 setter 안 쓰려면 여기서 직접 세팅 메서드 만들어도 됨)
        // Lombok @Setter 안 쓴다는 가정으로, 리플렉션 대신 생성자 사용해도 됨.
        // 간단하게 new 후 리플렉션 대신 아래처럼 엔티티에 전용 생성자 추가해도 됨.
        // 여기서는 편의상 엔티티에 생성자를 추가했다고 가정할 수도 있음.

        // ⇒ 엔티티에 이런 생성자를 추가하는게 더 깔끔:
        // public EmailVerificationCode(String email, String code, LocalDateTime expiresAt) { ... }

        entity = new EmailVerificationCode(email, code, LocalDateTime.now().plusMinutes(5));

        codeRepository.save(entity);

        String subject = "[고길동] 이메일 인증번호 안내";
        String content = "이메일 인증번호는 " + code + " 입니다.\n5분 안에 입력해주세요.";

        emailSender.send(email, subject, content);
    }

    // 인증번호 확인
    @Transactional
    public void verifyCode(String email, String code) {
        EmailVerificationCode entity = codeRepository.findByEmailAndCode(email, code)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.EMAIL_VERIFICATION_CODE_INVALID));

        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new GoGildongException(ExceptionCode.EMAIL_VERIFICATION_CODE_EXPIRED);
        }

        entity.markVerified();
    }

    // 인증 완료된 상태인지 확인
    @Transactional(readOnly = true)
    public void ensureVerified(String email) {
        EmailVerificationCode entity = codeRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.EMAIL_VERIFICATION_REQUIRED));

        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new GoGildongException(ExceptionCode.EMAIL_VERIFICATION_CODE_EXPIRED);
        }

        if (!entity.isVerified()) {
            throw new GoGildongException(ExceptionCode.EMAIL_VERIFICATION_REQUIRED);
        }
    }

}

