package com.github.syndexmx.AudioPodcastRssTGBot.domain;

import jakarta.persistence.*;
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
@Table(name = "subscribers")
public class Subscriber {

    @Id
    Long id;

    @ManyToMany
    List<Channel> channels;

}
