public class Main {
    public static void main(String[] args) {
        NotifyEmail notifyEmail = new NotifyEmail();
        NotifySms notifySms = new NotifySms();
        NotifyWhatsapp notifyWhatsapp = new NotifyWhatsapp();

        notifyWhatsapp.WhatsappDecorator(notifySms);
        notifySms.WhatsappDecorator(notifyEmail);

        notifyWhatsapp.notifyUser("Hello, this is a test message!");
    }
}
