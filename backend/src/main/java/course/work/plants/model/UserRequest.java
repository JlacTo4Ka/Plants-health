package course.work.plants.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_request_tab")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @EqualsAndHashCode.Include
    private UserModel userModel;

    @Column(name = "disease", nullable = false)
    @EqualsAndHashCode.Include
    private String disease;

    @Column(name = "image_url")
    @EqualsAndHashCode.Include
    private String imageUrl;

    @Column(name = "description")
    @EqualsAndHashCode.Include
    private String description;

    @Column(name = "created_at", nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime createdAt;

    public UserRequest(UserModel userModel, String disease, String description, LocalDateTime createdAt) {
        this.userModel = userModel;
        this.disease = disease;
        this.description = description;
        this.createdAt = createdAt;
    }
}
