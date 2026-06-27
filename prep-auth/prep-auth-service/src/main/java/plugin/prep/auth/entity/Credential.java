package plugin.prep.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "credentials")
public class Credential {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String salt;

    @Column
    private String hash;

}
