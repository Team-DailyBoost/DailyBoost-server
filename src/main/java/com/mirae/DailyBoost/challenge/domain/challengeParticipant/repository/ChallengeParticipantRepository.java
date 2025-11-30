package com.mirae.DailyBoost.challenge.domain.challengeParticipant.repository;

import com.mirae.DailyBoost.challenge.domain.challenge.repository.Challenge;
import com.mirae.DailyBoost.user.domain.repository.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    List<ChallengeParticipant> findAllByChallenge_Id(Long challengeId);
    
    List<ChallengeParticipant> findAllByUser_Id(Long userId);
    
    Optional<ChallengeParticipant> findByChallenge_IdAndUser_Id(Long challengeId, Long userId);
    
    Optional<ChallengeParticipant> findByChallengeAndUser(Challenge challenge, User user);
    
    Boolean existsByChallenge_IdAndUser_Id(Long challengeId, Long userId);
    
    void deleteByChallenge_IdAndUser_Id(Long challengeId, Long userId);
}
