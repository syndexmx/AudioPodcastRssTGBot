package com.github.syndexmx.AudioPodcastRssTGBot.repository;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, String> {
}
