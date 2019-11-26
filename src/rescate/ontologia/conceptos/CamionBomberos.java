
package rescate.ontologia.conceptos;

import java.util.List;

public class CamionBomberos extends Concepto {

    private int[] posicion;
    private List<Jugador> jugadoresEnVehiculo;

    public int[] getPosicion() {
        return posicion;
    }

    public List<Jugador> getJugadoresEnVehiculo() {
        return jugadoresEnVehiculo;
    }

    public void setJugadoresEnVehiculo(List<Jugador> jugadoresEnVehiculo) {
        this.jugadoresEnVehiculo = jugadoresEnVehiculo;
    }

    public void setPosicion(int[] posicion) {
        this.posicion = posicion;
    }


}
