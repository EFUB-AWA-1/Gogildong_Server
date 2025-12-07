package com.efub.gogildong.wheel.repository;

import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.wheel.domain.MemberMainWheelchair;
import com.efub.gogildong.wheel.domain.Wheelchair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberMainWheelchairRepository extends JpaRepository<MemberMainWheelchair, Long> {

    Optional<MemberMainWheelchair> findByUserAndWheelchair(User user, Wheelchair wheelchair);

    List<MemberMainWheelchair> findByUser(User user);
}
