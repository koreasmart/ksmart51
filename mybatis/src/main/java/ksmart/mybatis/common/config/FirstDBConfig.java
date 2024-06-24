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
import org.springframework.context.annotation.Primary;

import lombok.RequiredArgsConstructor;

@Configuration
@MapperScan(sqlSessionFactoryRef = "firstSqlsessionFactory",
			basePackages = {
					"ksmart.mybatis.member.mapper",
					"ksmart.mybatis.goods.mapper",
					"ksmart.mybatis.order.mapper"
					})
@RequiredArgsConstructor
public class FirstDBConfig {

	private final ApplicationContext ctx;
	
	@Primary
	@Bean("firstDatasource")
	@ConfigurationProperties(prefix = "spring.first.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Primary
	@Bean("firstSqlsessionFactory")
	public SqlSessionFactory firstSqlsessionFactory(@Qualifier("firstDatasource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(ctx.getResources("classpath:mapper/**/*.xml"));
		sqlSessionFactoryBean.setTypeAliasesPackage("ksmart.mybatis.*.dto");
		return sqlSessionFactoryBean.getObject();
	}
	
	@Primary
	@Bean("firstSqlsessionTemplate")
	public SqlSessionTemplate firtSqlSessionTemplate(@Qualifier("firstSqlsessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}








