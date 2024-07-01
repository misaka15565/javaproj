package cn.sudoer.javaproj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import cn.sudoer.javaproj.entity.SysUser;
@Repository
public interface SysUserRepostory extends JpaRepository<SysUser, Long>,JpaSpecificationExecutor<SysUser>{
    SysUser findByUsername(String userName);
}