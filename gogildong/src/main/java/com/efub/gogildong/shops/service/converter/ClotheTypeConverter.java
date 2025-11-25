package com.efub.gogildong.shops.service.converter;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.shops.domain.ClotheType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClotheTypeConverter implements Converter<String, ClotheType> {
    @Override
    public ClotheType convert(String source) {
        if (source.isBlank())
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        try {
            return ClotheType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }
    }
}
