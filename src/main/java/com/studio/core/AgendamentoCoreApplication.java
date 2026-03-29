package com.studio.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication // Anotação principal de inicialização de uma aplicação em Spring Boot. Ela funciona como um atalho (meta-annotation) que combina três anotações Ponto de entrada da aplicação Spring Boot (combina @Configuration, @EnableAutoConfiguration e @ComponentScan). @Configuration Indica que a classe contém configurações do Spring @EnableAutoConfiguration Ela permite que o framework configure automaticamente a aplicação com base nas dependências do classpath. O Spring automaticamente configura: servidor Apache Tomcat, DispatcherServlet, JSON com Jackson, MVC. @ComponentScan Instrui o Spring a procurar automaticamente componentes no pacote. Ele encontra classes anotadas com: @Component, @Service, @Repository, @Controller, @RestController
/*
main()
↓
SpringApplication.run()
↓
Cria ApplicationContext
↓
Executa AutoConfiguration
↓
Faz ComponentScan
↓
Inicializa Beans
↓
Start do servidor embutido (Tomcat/Jetty/Netty)
 */
@EnableScheduling // Ativa a execução de tarefas agendadas (@Scheduled) A anotação Spring Framework @EnableScheduling ativa o sistema de execução de tarefas agendadas (scheduled tasks) dentro da aplicação. Ela permite que métodos anotados com @Scheduled sejam executados automaticamente em intervalos de tempo definidos.
/*
Quando você ativa:
@EnableScheduling
O Spring registra internamente:
ScheduledAnnotationBeanPostProcessor
Esse componente:

escaneia beans
encontra métodos @Scheduled
cria ScheduledTask
agenda no TaskScheduler

Por padrão o Spring usa:
ThreadPoolTaskScheduler

@EnableScheduling
↓
habilita
↓
descoberta de métodos @Scheduled
↓
registro em TaskScheduler
↓
execução automática

ou sejá

Spring inicia
↓
@EnableScheduling ativa o scheduler
↓
Spring procura métodos @Scheduled
↓
Cria tasks em background
↓
Executa automaticamente

Tipos de agendamento
fixedRate
Executa em intervalo fixo independente do tempo de execução.
@Scheduled(fixedRate = 5000)
start → 5s → start → 5s → start
Mesmo que a tarefa ainda esteja rodando.
fixedDelay
Executa após terminar a execução anterior.
@Scheduled(fixedDelay = 5000)
executa
↓
termina
↓
espera 5s
↓
executa novamente

cron
Permite expressões cron.
@Scheduled(cron = "0 0 * * * *")
Executa a cada hora.
seg minuto hora dia mês diaSemana
exemplo 0 0 2 * * *
Executa todo dia às 02:00.
 */
@EnableAsync // Ativa a execução de métodos assíncronos (@Async)
/*
A anotação Spring Framework @EnableAsync ativa execução assíncrona de métodos dentro do container do Spring.
Ela permite que métodos anotados com @Async sejam executados em outra thread, sem bloquear o fluxo principal.
Sem async:
service.process();
nextStep();
process termina
↓
depois nextStep executa
Tudo ocorre na mesma thread.


Com async:
@Async
public void process() { }
thread principal chama process()
↓
Spring envia para thread pool
↓
nextStep executa imediatamente
execução paralela


O Spring Framework registra:
AsyncAnnotationBeanPostProcessor
Esse componente:

intercepta beans
detecta @Async
cria proxy
executa método via Executor

Caller
  ↓
Spring Proxy
  ↓
TaskExecutor
  ↓
Thread Pool
  ↓
Método executa


Retorno de métodos async
Future
@Async
public Future<String> task()
CompletableFuture
@Async
public CompletableFuture<String> task()


@Async não funciona dentro da mesma classe.
Exemplo de erro
@Service
public class UserService {

    public void methodA() {
        methodB();
    }

    @Async
    public void methodB() {}
}

E ele deve ser configurada pois se não fica ilimitada
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);

        executor.initialize();
        return executor;
    }
}
 */
public class AgendamentoCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgendamentoCoreApplication.class, args);
    }
}
