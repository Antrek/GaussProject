package model;

/**
 * Clase modelo para usuario, generalizacion de usuario.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class User {

    /**
     * Convierte a texto los privilegios del usuario
     * 
     * @param edit
     *            Privilegios
     * @return String
     */
    public static String edit2String(int edit) {
	String s;
	if (edit == 0)
	    s = "Jugador";
	else
	    s = "Editor";
	return s;
    }

    /**
     * Privilegios
     */
    private boolean edit;
    /**
     * Contraseña
     */
    private String password;

    /**
     * Nombre de usuario
     */
    private String user;

    /**
     * Construccion generico con parametros usuario, contraseña y privilegios.
     * 
     * @param usr
     *            Nombre de usuario
     * @param pwd
     *            Contraseña
     * @param edt
     *            Privilegios
     */
    public User(String usr, String pwd, boolean edt) {
	this.user = usr;
	this.password = pwd;
	this.edit = edt;
    }

    /**
     * Devuelve el atributo contraseña
     * 
     * @return String - Password
     */
    public String getPassword() {
	return this.password;
    }

    /**
     * Devuelve el atributo usuario
     * 
     * @return String - Usuario
     */
    public String getUser() {
	return this.user;
    }

    /**
     * Devuelve si el usuario tiene privilegios
     * 
     * @return True/False
     */
    public boolean isEditor() {
	return this.edit;
    }

    /**
     * Modifica el atributo password
     * 
     * @param password
     *            Contraseña nueva
     */
    public void setPassword(String password) {
	this.password = password;
    }

}
