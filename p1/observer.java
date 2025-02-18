abstract public interface observer {
    //Actualiza el valor del aspecto que le interesa al observer con el valor de subjectState
    public boolean Update(String stockName, double subjectState);
    //Notifica al inversor de que ahora sigue a una accion de la bolsa
    public void seguir(String stockName, double valorActual);
}
