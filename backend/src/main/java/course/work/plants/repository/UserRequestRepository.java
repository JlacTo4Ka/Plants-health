package course.work.plants.repository;

import course.work.plants.model.UserModel;
import course.work.plants.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    List<UserRequest> findAllByUserModel(UserModel userModel);
}
