package com.mirae.DailyBoost.challenge.domain.challenge.repository;

import com.mirae.DailyBoost.challenge.domain.challenge.repository.enums.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findAllByStatus(ChallengeStatus status);
    
    Optional<Challenge> findByIdAndStatus(Long id, ChallengeStatus status);
    
    List<Challenge> findAllByCreator_Id(Long creatorId);
    
    Boolean existsByCreator_IdAndTitleAndStatus(Long creatorId, String title, ChallengeStatus status);
}
