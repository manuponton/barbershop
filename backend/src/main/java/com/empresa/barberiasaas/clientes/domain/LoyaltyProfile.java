package com.empresa.barberiasaas.clientes.domain;

import java.time.LocalDate;
import java.util.Objects;

public record LoyaltyProfile(String program, int points, String tier, LocalDate enrolledAt, LocalDate nextRewardAt, boolean active) {
    public LoyaltyProfile addPoints(int additionalPoints, LocalDate nextReward) {
        int updatedPoints = Math.max(0, points + additionalPoints);
        return new LoyaltyProfile(program, updatedPoints, tier, enrolledAt, nextReward, active);
    }

    public LoyaltyProfile withTier(String updatedTier) {
        return new LoyaltyProfile(program, points, updatedTier, enrolledAt, nextRewardAt, active);
    }

    public LoyaltyProfile ensureProgram(String desiredProgram) {
        if (Objects.equals(program, desiredProgram)) {
            return this;
        }
        return new LoyaltyProfile(desiredProgram, points, tier, enrolledAt, nextRewardAt, active);
    }
}
