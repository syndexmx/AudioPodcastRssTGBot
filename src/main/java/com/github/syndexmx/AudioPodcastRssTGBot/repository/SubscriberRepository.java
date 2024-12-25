package com.github.syndexmx.AudioPodcastRssTGBot.repository;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
}
