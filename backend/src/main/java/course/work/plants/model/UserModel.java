package course.work.plants.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_tab")
@NoArgsConstructor
public class UserModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long userId;

    @Column(name = "login", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String login;

    @Column(name = "password", nullable = false)
    @EqualsAndHashCode.Include
    private String password;

    @Column(name = "created_at", nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime createdAt;

    public UserModel(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
