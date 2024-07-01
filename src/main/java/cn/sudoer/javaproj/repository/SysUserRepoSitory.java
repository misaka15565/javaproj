package cn.sudoer.javaproj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import cn.sudoer.javaproj.entity.SysUser;
@Repository
public interface SysUserRepoSitory extends JpaRepository<SysUser, Long>,JpaSpecificationExecutor<SysUser>{
}