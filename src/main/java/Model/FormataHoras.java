package Model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import javafx.scene.control.TextField;

public class FormataHoras {

	public void formataHora(TextField horas) {
		horas.textProperty().addListener((observable, oldValue, newValue) -> {
			
			// Remove qualquer caractere que não seja dígito ou ":"
			String formatted = newValue.replaceAll("[^0-9]", "");

			if (newValue.length() < oldValue.length()) {
				return; // Permite apagar sem problemas
			}

			if (!verificaPrimeiroDigito(formatted)) {
				horas.setText(oldValue); // Restaura o valor antigo se for inválido
				return;
			}

			if (!verificaSegundoDigito(formatted)) {
				horas.setText(oldValue); // Restaura o valor antigo se for inválido
				return;
			}

			if (!verificaMinutoEsegundoDigito(formatted)) {
				horas.setText(oldValue); // Restaura o valor antigo se for inválido
				return;
			}

			// Limita o tamanho a 6 dígitos
			if (formatted.length() > 6) {
				formatted = formatted.substring(0, 6);
			}

			// Adiciona os ":" automaticamente ao formatar
			if (formatted.length() >= 2) {
				formatted = formatted.substring(0, 2) + ":" + formatted.substring(2);
			}
			if (formatted.length() >= 5) {
				formatted = formatted.substring(0, 5) + ":" + formatted.substring(5);
			}

			// Evitar loop de listener ao setar o texto
			horas.setText(formatted);
		});
	}

	public boolean verificaPrimeiroDigito(String hora) {
		if (hora.length() >= 1) { // Verifica se há ao menos um dígito
			int primeiroDigito = Character.getNumericValue(hora.charAt(0));
			// Verifica se o primeiro dígito é 0, 1 ou 2
			if (primeiroDigito >= 0 && primeiroDigito <= 2) {
				return true; // O primeiro dígito é válido
			}
		}
		return false; // O primeiro dígito é inválido ou não existe
	}

	// Verifica se o segundo dígito é válido
	public boolean verificaSegundoDigito(String hora) {
		if (hora.length() >= 2) { // Verifica se há pelo menos dois dígitos
			int primeiroDigito = Character.getNumericValue(hora.charAt(0));
			int segundoDigito = Character.getNumericValue(hora.charAt(1));

			// Se o primeiro dígito for 2, o segundo deve ser entre 0 e 3 (para o formato
			// 24h)
			if (primeiroDigito == 2 && segundoDigito > 3) {
				return false;
			}

			return (segundoDigito >= 0 && segundoDigito <= 9);
		}
		return true; // Se não houver segundo dígito, consideramos válido por enquanto
	}

	public boolean verificaMinutoEsegundoDigito(String hora) {
		if (hora.length() >= 3) { // Verifica se há pelo menos três dígitos
			int terceiroDigito = Character.getNumericValue(hora.charAt(2));
			if (terceiroDigito < 0 || terceiroDigito > 5) {
				return false; // O terceiro dígito deve estar entre 0 e 5 (minutos)
			}
		}
		if (hora.length() >= 5) { // Verifica se há pelo menos cinco dígitos
			int quintoDigito = Character.getNumericValue(hora.charAt(4));
			if (quintoDigito < 0 || quintoDigito > 5) {
				return false; // O quinto dígito deve estar entre 0 e 5 (segundos)
			}
		}
		return true; // Se não houver dígitos suficientes, consideramos válido por enquanto
	}

	public Object convertStringToTime(String input, Class<?> targetType) {
		if (input == null || input.isEmpty()) {
			return null;
		}

		try {
			if (targetType == Time.class) {
				return Time.valueOf(input);

			} else {
				throw new IllegalArgumentException("Tipo de conversão não suportado: " + targetType.getName());
			}
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	public Date convertLocalDateToSqlDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return Date.valueOf(localDate);
	}
}
