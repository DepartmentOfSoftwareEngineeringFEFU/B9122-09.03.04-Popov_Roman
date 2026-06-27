package plugin.prep.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.*;

import plugin.prep.auth.enums.*;

@Data
@Builder
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String login;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private GradeEnum grade;

    @Column
    private Long learningTrackId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinTable(name = "user_role",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Role role;

}
