abstract public interface observer {
    //Actualiza el valor del aspecto que le interesa al observer con el valor de subjectState
    public boolean Update(String stockName, double subjectState);
}
