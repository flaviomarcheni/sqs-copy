package com.sqs.copy;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.ValueProviderSupport;

@SpringBootApplication(scanBasePackageClasses = { CopyApplication.class })
@EnableConfigurationProperties
public class CopyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CopyApplication.class, args);

	}

	@ShellComponent("Tes")
	static class CLI {
		@ShellMethod("Teste shell.")
		void cp(@ShellOption(value = "from", help = "Nome fila que sera copiada") String from,
				@ShellOption(value = "to", help = "Nome da fila para onde as mensagens serão enviadas") String to,
				@ShellOption(value = "deleteMessages", help = "Deletar mensagens da fila de origem.", defaultValue = "false") Boolean delMessages) {
		}

	}

}
