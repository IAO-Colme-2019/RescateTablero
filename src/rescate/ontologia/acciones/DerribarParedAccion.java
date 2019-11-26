package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class DerribarParedAccion extends Accion {

  private Casilla casilla;
  private int conexion;

  public DerribarParedAccion() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

  public int getConexion() {
    return conexion;
  }

  public void setConexion(int conexion) {
    this.conexion = conexion;
  }

}