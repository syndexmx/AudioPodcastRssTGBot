package com.github.syndexmx.AudioPodcastRssTGBot.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "podcasts")
public class Podcast {

    @Id
    String podcastUrl;

    String title;
    String description;

    String date;

    @ManyToOne
            @JoinColumn(name = "url")
    Channel ownerChannel;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Podcast podcast = (Podcast) o;
        return Objects.equals(podcastUrl, podcast.podcastUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(podcastUrl);
    }
}
