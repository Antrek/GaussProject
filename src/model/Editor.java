package model;

/**
 * Clase del editor del sistema. Parte del modelo.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class Editor extends User {

    /**
     * Constructor de la clase Editor
     * 
     * @param usr
     *            - String nombre del usuario
     * @param pwd
     *            - String contraseña del usuario
     */
    public Editor(String usr, String pwd) {
	super(usr, pwd, true);
    }

}
