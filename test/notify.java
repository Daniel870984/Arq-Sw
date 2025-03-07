class Notify {
    public void notifyUser(String message) {
        System.out.println("Notifying user: " + message);
    }
}

abstract class NotifyDecorator extends Notify {
    public abstract void notifyUser(String message);
    Notify nextNotify;
}

class NotifyWhatsapp extends NotifyDecorator {

    //public NotifyWhatsapp(Notify nexNotifier) {
    //    this.nextNotify = nexNotifier;
    //}

    public void notifyUser(String message) {
        System.out.println("Notifying user via Whatsapp: " + message);
        nextNotify.notifyUser(message);
    }
    void WhatsappDecorator(Notify nexNotifier) {
        this.nextNotify = nexNotifier;
    }
}

class NotifySms extends NotifyDecorator {
    public void notifyUser(String message) {
        System.out.println("Notifying user via SMS: " + message);
        if (nextNotify != null) {
            nextNotify.notifyUser(message);
        }
    }
    void WhatsappDecorator(Notify nexNotifier) {
        this.nextNotify = nexNotifier;
    }
}

class NotifyEmail extends NotifyDecorator {
    public void notifyUser(String message) {
        System.out.println("Notifying user via Email: " + message);
        if (nextNotify != null) {
            nextNotify.notifyUser(message);
        }
    }
    void WhatsappDecorator(Notify nexNotifier) {
        this.nextNotify = nexNotifier;
    }
}

