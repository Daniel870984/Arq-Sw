import java.io.Serializable;

public class AsyncRequest implements Serializable {
    private Respuesta respuesta;
    private boolean delivered = false;
    private String clientId; // Nuevo campo para identificar al cliente

    public AsyncRequest(String clientId) {
        this.clientId = clientId;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getClientId() {
        return clientId;
    }
}
