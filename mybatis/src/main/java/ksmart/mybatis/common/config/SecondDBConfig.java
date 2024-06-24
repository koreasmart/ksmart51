package ksmart.mybatis.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@MapperScan(sqlSessionFactoryRef = "secondSqlsessionFactory",
			basePackages = "ksmart.mybatis.subservice.mapper")
@RequiredArgsConstructor
public class SecondDBConfig {
	private final ApplicationContext ctx;
	
	@Bean("secondDatasource")
	@ConfigurationProperties(prefix = "spring.second.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean("secondSqlsessionFactory")
	public SqlSessionFactory secondSqlsessionFactory(@Qualifier("secondDatasource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(ctx.getResources("classpath:mapper/**/*.xml"));
		sqlSessionFactoryBean.setTypeAliasesPackage("ksmart.mybatis.*.dto");
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean("secondSqlsessionTemplate")
	public SqlSessionTemplate secondSqlSessionTemplate(@Qualifier("secondSqlsessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
