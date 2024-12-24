package com.github.syndexmx.AudioPodcastRssTGBot.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

    Date date;

    @ManyToOne
            @JoinColumn(name = "url")
    Podcast ownerChannel;

}
