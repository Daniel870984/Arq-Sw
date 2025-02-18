abstract public interface subject {
    //Añade el observer inversor a la lista de observers suscritos al subject
    void attach(String stockName,observer inversor);

    //Elimina el observer inversor de la lista de observers suscritos al subject
    void detach(String stockName,observer inversor);

    //Notifica a todos los observers suscritos al sujeto de un cambio
    void inform(String stockName, double newValue);
}
