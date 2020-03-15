package que_me_pongo.evento.listeners.notificaciones;

import que_me_pongo.evento.Evento;
import que_me_pongo.evento.listeners.EventoListener;
import que_me_pongo.usuario.Usuario;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailListener extends EventoListener {

	private String myMail = "quemepongoNotify@gmail.com";
	private String password = "quemepongo123";

	@Override
	public void sugerenciasRealizadas(Evento evento) {

		String cuerpo = "Ya fueron generados los atuendos para tu evento: ";
		String asunto = "Sugerencias realizadas!";
		this.enviarNotificacion(evento,cuerpo,asunto);
	}

	@Override
	public void alertaMeteorologica(Evento evento) {
		String cuerpo = "El clima cambio, seria mejor que revises las sugerencias de tu evento: ";
		String asunto = "Evento resugerido";
		this.enviarNotificacion(evento,cuerpo,asunto);
	}

	private void enviarNotificacion(Evento evento,String cuerpo,String asunto) {

		Usuario destinatario = evento.getUsuario();

		String toEmail = destinatario.getMail();

		if(toEmail==null) return;

		Session session = this.iniciarSesion();

		String contenido =
				"Hola " + destinatario.getNombre() + "!!\n"
						+ cuerpo + evento.getDescripcion();

		asunto = asunto + " (" + evento.getDescripcion() + ")";
		this.enviarMensaje(toEmail, session, asunto, contenido);
	}

	private Session iniciarSesion(){
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myMail,password);
			}
		});
		return session;
	}

	private void enviarMensaje(String toEmail, Session session, String asunto, String contenido){
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(myMail));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			msg.setSubject(asunto);
			msg.setText(contenido);

			Transport.send(msg);
			System.out.println("Email enviado");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
