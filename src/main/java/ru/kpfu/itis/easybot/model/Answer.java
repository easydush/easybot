package ru.kpfu.itis.easybot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "answers")
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private Boolean isTrue;
}
