package ru.devguru.springms.planner.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"ru.devguru.springms.planner"})
@EnableJpaRepositories(basePackages = {"ru.devguru.springms.planner.users"})
@RefreshScope // Позволяет динамически изменять состав настроек бинов (к примеру изменили настройки логирования в properties, поменяли значение переменной и т.д.)
// Это позволяет избежать повторного перезапуска config, всех проектов, потом api-gateway
// Но если изменения связаны с БД, с RabbitMQ, то желательно перезапускать все заново, так как настройки с GitHub считываются при старте
// Для правильной работы RefreshScope должны быть добавлены настройки actuator(должен быть включен и включены все его endpoints)
public class PlannerUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlannerUsersApplication.class, args);
	}

}
