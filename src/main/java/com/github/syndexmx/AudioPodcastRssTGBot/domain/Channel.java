package com.github.syndexmx.AudioPodcastRssTGBot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Channel {

    @Id
            @Column(name="url")
    String url;
    String channelName;
    String compressed;

    @OneToMany
    List<Podcast> podcasts;
}
