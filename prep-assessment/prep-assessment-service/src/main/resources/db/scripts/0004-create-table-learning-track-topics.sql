create table if not exists learning_track_topics
(
    id                bigserial primary key,
    learning_track_id bigint not null,
    topic_id          bigint not null,

    constraint fk_learning_track_topics_track foreign key (learning_track_id) references learning_tracks (id),
    constraint fk_learning_track_topics_topic foreign key (topic_id) references topics (id)
);