package com.github.syndexmx.AudioPodcastRssTGBot.repository;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
}
