package fmyxyz.mybatis_generatorConfig;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMapper {
	public List<SysObjects> getTables(@Param("dbType") String dbType);
}
