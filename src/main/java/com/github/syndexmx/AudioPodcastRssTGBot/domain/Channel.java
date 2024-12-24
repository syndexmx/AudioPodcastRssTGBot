package com.github.syndexmx.AudioPodcastRssTGBot.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
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
@Table(name= "channels")
public class Channel {

    @Id
            @Column(name="url")
    String url;
    String channelName;
    String compressed;

    @OneToMany
    List<Podcast> podcasts;
}
