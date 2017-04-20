package model;

/**
 * Clase modelo del jugador de la aplicacion. Parte del modelo.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class Player extends User {
    /**
     * Instancia de las estadisticas del usuario
     */
    private Stats stats;

    /**
     * Constructor de la clase Player
     * 
     * @param usr
     *            Nombre de usuario
     * @param pwd
     *            Contraseña
     */
    public Player(String usr, String pwd) {
	super(usr, pwd, false);
	this.stats = new Stats();
    }

    /**
     * Constructor de la clase Player
     * 
     * @param usr
     *            Nombre de usuario
     * @param pwd
     *            Contraseña
     * @param stats
     *            Estadisticas del player
     */
    public Player(String usr, String pwd, Stats stats) {
	super(usr, pwd, false);
	this.stats = stats;
    }

    /**
     * Devuelve el objeto Stats que contiene las estadisticas del usuario.
     * 
     * @return Estadisticas del usuario
     */
    public Stats getStats() {
	return this.stats;
    }

    /**
     * Modifica las estadisticas del usuario
     * 
     * @param stats
     *            Estadisticas nuevas del usuario.
     */
    public void setStats(Stats stats) {
	this.stats = stats;
    }

}
