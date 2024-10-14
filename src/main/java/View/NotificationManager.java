package View;

import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;
import javafx.util.Duration;

public class NotificationManager {
	
	public static void showNotification(String title, String message, NotificationType type) {
		Notifications notification = Notifications.create().title(title).text(message).hideAfter(Duration.seconds(5))
				.position(Pos.BOTTOM_RIGHT); // Define a posição da notificação

		switch (type) {
		case SUCCESS:
			notification.showInformation(); // Mostra a notificação de sucesso
			break;
		case ERROR:
			notification.showError(); // Mostra a notificação de erro
			break;
		case INFORMATION:
			notification.showInformation(); // Mostra a notificação informativa
			break;
		default:
			notification.show(); // Mostra a notificação padrão
			break;
		}
	}

	public enum NotificationType {
		SUCCESS, ERROR, INFORMATION
	}
}
