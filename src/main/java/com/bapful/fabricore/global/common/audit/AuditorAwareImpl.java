package com.bapful.fabricore.global.common.audit;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {

        return Optional.of("system"); //일단 시스템 사용자로 기록


// TODO SecurityContext에서 실제 사용자 정보 가져오는 코드로 변경 필요
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return Optional.of(authentication.getName());

    }
}
