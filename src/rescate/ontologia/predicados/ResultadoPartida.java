package rescate.ontologia.predicados;

public class ResultadoPartida extends Predicado {

  private int resultado;

  public ResultadoPartida(int resultado) {
    this.resultado = resultado;
  }

  public int getResultado() {
    return resultado;
  }

}