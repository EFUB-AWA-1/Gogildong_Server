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

        if (!entity.isVerified()) {
            throw new GoGildongException(ExceptionCode.EMAIL_VERIFICATION_REQUIRED);
        }
    }

}

