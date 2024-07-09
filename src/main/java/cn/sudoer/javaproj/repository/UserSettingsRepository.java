package cn.sudoer.javaproj.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import cn.sudoer.javaproj.entity.UserSettings;
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long>,JpaSpecificationExecutor<UserSettings>{
    UserSettings findByUsername(String userName);
    
}
