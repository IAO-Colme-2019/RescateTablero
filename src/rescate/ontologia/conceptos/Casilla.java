package rescate.ontologia.conceptos;

public class Casilla extends Concepto {

    /*** Constructor ***/
    public Casilla() {
    }

    /*** Atributos ***/
    private int[] posicion;
    // [x, y]

    private int[][] conexiones;
    // 0: arriba, 1: derecha, 2: abajo, 3: izquierda
    // 0: nada, 1: puerta, 2: pared
    // 0: cerrada/sin romper, 1: abierta/semirrota, 2: -/rota

    private int tieneFuego;
    // 0: nada, 1: humo, 2: fuego

    private boolean tieneMateriaPeligrosa;

    private boolean tieneFocoCalor;

    private int[] puntoInteres;
    // 0: sin descubrir, 1: descubierto
    // 0: falsa alarma, 1: víctima

    private boolean[] flecha;
    // 0: arriba, 1: derecha, 2: abajo, 3: izquierda

    private boolean camionBomberos;

    private boolean ambulancia;

    private boolean esAparcamientoCamion;

    private boolean esAparcamientoAmbulancia;

    /*** Getters & Setters ***/
    public int[] getPosicion() {
        return posicion;
    }

    public void setPosicion(int[] posicion) {
        this.posicion = posicion;
    }

    public int[][] getConexiones() {
        return conexiones;
    }

    public void setConexiones(int[][] conexiones) {
        this.conexiones = conexiones;
    }

    public int tieneFuego() {
        return tieneFuego;
    }

    public void setTieneFuego(int tieneFuego) {
        this.tieneFuego = tieneFuego;
    }

    public boolean tieneMateriaPeligrosa() {
        return tieneMateriaPeligrosa;
    }

    public void setTieneMateriaPeligrosa(boolean tieneMateriaPeligrosa) {
        this.tieneMateriaPeligrosa = tieneMateriaPeligrosa;
    }

    public boolean tieneFocoCalor() {
        return tieneFocoCalor;
    }

    public void setTieneFocoCalor(boolean tieneFocoCalor) {
        this.tieneFocoCalor = tieneFocoCalor;
    }

    public int[] getPuntoInteres() {
        return puntoInteres;
    }

    public void setPuntoInteres(int[] puntoInteres) {
        this.puntoInteres = puntoInteres;
    }

    public boolean[] getFlecha() {
        return flecha;
    }

    public void setFlecha(boolean[] flecha) {
        this.flecha = flecha;
    }

    public boolean isCamionBomberos() {
        return camionBomberos;
    }

    public void setCamionBomberos(boolean camionBomberos) {
        this.camionBomberos = camionBomberos;
    }

    public boolean isAmbulancia() {
        return ambulancia;
    }

    public void setAmbulancia(boolean ambulancia) {
        this.ambulancia = ambulancia;
    }

    public boolean esAparcamientoCamion() {
        return esAparcamientoCamion;
    }

    public void setEsAparcamientoCamion(boolean esAparcamientoCamion) {
        this.esAparcamientoCamion = esAparcamientoCamion;
    }

    public boolean esAparcamientoAmbulancia() {
        return esAparcamientoAmbulancia;
    }

    public void setEsAparcamientoAmbulancia(boolean esAparcamientoAmbulancia) {
        this.esAparcamientoAmbulancia = esAparcamientoAmbulancia;
    }

}