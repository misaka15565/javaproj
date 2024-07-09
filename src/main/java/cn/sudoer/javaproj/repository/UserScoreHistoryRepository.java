package cn.sudoer.javaproj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import cn.sudoer.javaproj.entity.*;

@Repository
public interface UserScoreHistoryRepository
        extends JpaRepository<UserScoreHistoryEntity, Long>, JpaSpecificationExecutor<UserScoreHistoryEntity> {
    UserScoreHistoryEntity[] findByUsername(String userName);

}
