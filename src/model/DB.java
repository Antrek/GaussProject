package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase encargada de enlazar la base de datos con la aplicacion. Parte del
 * Modelo.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class DB {
    /**
     * Referencia estatica al nombre de la columna respuesta de la base de
     * datos.
     */
    public static final String COL_ANSWER = "`RESPUESTA`";
    /**
     * Referencia estatica al nombre de la columna mejor puntuacion de la base
     * de datos.
     */
    public static final String COL_BEST_SCORE = "`MEJOR_PUNTUACION`";
    /**
     * Referencia estatica al nombre de la columna dificultad de la base de
     * datos.
     */
    public static final String COL_DIFF = "`DIFICULTAD`";
    /**
     * Referencia estatica al nombre de la columna de permisos de editor de la
     * base de datos.
     */
    public static final String COL_EDT = "`EDIT`";
    /**
     * Referencia estatica al nombre de la columna ID de la base de datos.
     */
    public static final String COL_ID = "`ID`";
    /**
     * Referencia estatica al nombre de la columna contraseña de la base de
     * datos.
     */
    public static final String COL_PASSWORD = "`PASS`";
    /**
     * Referencia estatica al nombre de la columna preguntas acertadas de la
     * base de datos.
     */
    public static final String COL_QUEST_CORRECT = "`PACERTADAS`";
    /**
     * Referencia estatica al nombre de la columna pregunta de la base de datos.
     */
    public static final String COL_QUESTION = "`PREGUNTA`";
    /**
     * Referencia estatica al nombre de la columna partidas jugadas de la base
     * de datos.
     */
    public static final String COL_TESTS_PLAYED = "`PJUGADAS`";
    /**
     * Referencia estatica al nombre de la columna mejor puntuacion de la base
     * de datos.
     */
    public static final String COL_TOTAL_SCORE = "`PUNTUACION_TOTAL`";
    /**
     * Referencia estatica al nombre de la columna tiempo total de la base de
     * datos.
     */
    public static final String COL_TOTAL_TIME = "`TIEMPO_TOTAL`";
    /**
     * Referencia estatica al nombre de la columna Marca de tiempo de la base de
     * datos.
     */
    public static final String COL_TSTAMP = "`TSTAMP`";
    /**
     * Referencia estatica al nombre de la columna usuario de la base de datos.
     */
    public static final String COL_USER = "`USER`";
    /**
     * Referencia estatica al nombre de la tabla preguntas.
     */
    public static final String TABLE_QUESTIONS = "`preguntas`";
    /**
     * Referencia estatica al nombre de la tabla estadisticas.
     */
    public static final String TABLE_STATS = "`estadisticas`";
    /**
     * Referencia estatica al nombre de la tabla usuarios.
     */
    public static final String TABLE_USERS = "`usuarios`";
    /**
     * Conexion a la base de datos.
     */
    private Connection conexion;
    /**
     * Datos de conexion a la base de datos.
     */
    private String url, usr, pwd;

    /**
     * Constructor de la clase Database .
     * 
     * @param url
     *            - Direccion web del servidor MySQL.
     * @param usr
     *            - Usuario del servidor.
     * @param pwd
     *            - Contraseña.
     * @throws SQLException
     *             Excepcion al conectar con la base de datos.
     */
    public DB(String url, String usr, String pwd) throws SQLException {
	this.url = url;
	this.usr = usr;
	this.pwd = pwd;
    }

    /**
     * Cierra la conexion con el servidor MySQL.
     * 
     * @return True si pudo cerrar la conexion, False si no pudo cerrar la
     *         conexion.
     * @throws SQLException
     *             Excepcion al cerrar la conexion con la base de datos.
     */
    public boolean close() throws SQLException {
	try {
	    this.conexion.close();
	} catch (Exception e) {
	    System.err.println("Error cerrando conexiones: " + e.toString());
	}
	return this.conexion.isClosed();
    }

    /**
     * Cuenta el numero de registros que tiene la tabla table.
     * 
     * @param table
     *            - Tabla que queremos contar
     * @return int Numero de registros. -1 Si hubo algun error.
     * @throws SQLException
     *             Excepcion al contar las entradas de la tabla en la base de
     *             datos.
     */
    public int count(String table) throws SQLException {

	Statement statement = null;
	ResultSet rs = null;
	int cnt = -1;

	try {
	    statement = this.conexion.createStatement();
	    rs = statement.executeQuery("SELECT COUNT(*) FROM " + table);
	    while (rs.next()) {
		cnt = rs.getInt(1);
	    }
	} catch (Exception e) {
	    System.err.println(e);
	    return -1;
	} finally {
	    try {
		if (rs != null)
		    rs.close();
		if (statement != null)
		    statement.close();
	    } catch (Exception e) {
		System.err
			.println("Error cerrando conexiones: " + e.toString());
	    }
	}

	return cnt;

    }

    /**
     * Cuenta el numero de registros que tiene la tabla table cuya columna col
     * toma valor var.
     * 
     * @param table
     *            - Tabla que queremos contar
     * @param col
     *            - Columna a comprobar
     * @param var
     *            - Valor que ha de tomar
     * @return int Numero de coincidencias. -1 Si hubo algun error.
     * @throws SQLException
     *             Excepcion al contar las entradas de la tabla en la base de
     *             datos.
     */
    public int count(String table, String col, String var) throws SQLException {
	ResultSet rs = null;
	PreparedStatement statement = null;
	int cnt = -1;
	try {
	    String sentence = "SELECT COUNT(*) FROM " + table + " WHERE " + col
		    + " = ?";
	    statement = this.conexion.prepareStatement(sentence);
	    if (isInt(var))
		statement.setInt(1, Integer.parseInt(var));
	    else if (isFloat(var))
		statement.setFloat(1, Float.parseFloat(var));
	    else
		statement.setString(1, var);
	    rs = statement.executeQuery();
	    while (rs.next()) {
		cnt = rs.getInt(1);
	    }
	} catch (Exception e) {
	    System.err.println(e);
	    return -1;
	} finally {
	    try {
		if (rs != null)
		    rs.close();
		if (statement != null)
		    statement.close();
	    } catch (Exception e) {
		System.err
			.println("Error cerrando conexiones: " + e.toString());
	    }
	}
	return cnt;
    }

    /**
     * Elimina los registros de la tabla table que tenga en la columna col el
     * valor var.
     * 
     * @param table
     *            - Tabla de la que se eliminara el registro.
     * @param col
     *            - Columna clave donde poner la condicion.
     * @param var
     *            - Valor que ha de tomar el registro en la columna col para que
     *            sea eliminado.
     * @return True/False
     * @throws SQLException
     *             Excepcion al borrar elementos de la base de datos
     */
    public boolean delete(String table, String col, String var)
	    throws SQLException {
	boolean deleted = true;
	// Comprueba que ninguno de los parametros esta vacio.
	if (table.length() <= 0 || col.length() <= 0 || var.length() <= 0) {
	    System.err.println(this.getClass()
		    + ":Delete:Valores incorrectos en los parametros");
	    return false;
	}

	PreparedStatement statement = null;

	try {
	    // Crea la sentencia SQL
	    String delete = "DELETE FROM `gauss`." + table + " WHERE " + table
		    + "." + col + " = ?";
	    statement = this.conexion.prepareStatement(delete);

	    if (isInt(var))
		statement.setInt(1, Integer.parseInt(var));
	    else if (isFloat(var))
		statement.setFloat(1, Float.parseFloat(var));
	    else
		statement.setString(1, var);
	    statement.executeUpdate();

	    if (statement.getUpdateCount() == 0)
		deleted = false;
	} catch (Exception e) {
	    // Si da error lo muestra en consola y devuelve false.
	    System.err.println(e);
	    deleted = false;
	} finally {
	    try {
		// Cerramos posibles conexiones abiertas
		if (statement != null)
		    statement.close();
	    } catch (Exception e) {
		System.err
			.println("Error cerrando conexiones: " + e.toString());
	    }
	}
	return deleted;
    }

    /**
     * @param n
     *            Numero de resultados a devolver
     * @return String[][] Array de resultados ([Usuario]-[Puntuacion])
     * @throws SQLException
     *             Expceicon al consultar los valores de la abse de datos.
     */
    public String[][] getBestScores(int n) throws SQLException {
	if (n < 1)
	    return null;
	String[][] ranking = null;
	try {
	    Statement s = this.conexion.createStatement();
	    String query = "SELECT " + DB.COL_USER + "," + DB.COL_BEST_SCORE
		    + " FROM " + DB.TABLE_STATS + " order by "
		    + DB.COL_BEST_SCORE + " desc LIMIT " + String.valueOf(n);
	    ResultSet rs = s.executeQuery(query);
	    ranking = new String[rs.getMetaData().getColumnCount()][n];
	    for (int i = 0; i < ranking.length; i++) {
		for (int j = 0; j < ranking[i].length; j++) {
		    ranking[i][j] = null;
		}
	    }
	    int i = 0;
	    while (rs.next()) {
		for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
		    ranking[x - 1][i] = rs.getString(x);
		}
		i++;
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return ranking;
    }

    /**
     * Crea un registro(entrada/fila) en la tabla 'table' introduciondo en las
     * columnas del array entries, los valores del array vars. nvars indica el
     * numero de elementos que se han de guardar en el registro. Como minimo
     * deben pasarse 3 argumentos.
     * 
     * @param table
     *            - Tabla en la que queremos crear un registro.
     * @param entries
     *            - Conjunto de columnas a rellenar.
     * @param vars
     *            - Valores que van a tomar las columnas mencionadas.
     * @param nvars
     *            - Numero de columnas/valores. Debe ser como minimo 3.
     * @return True/False
     * @throws SQLException
     *             Excepcion al incluir un registro en la base de datos.
     */
    public boolean insert(String table, String[] entries, String[] vars,
	    int nvars) throws SQLException {
	// Comprueba si hay suficientes parametros.
	if (entries.length != nvars || vars.length != nvars || nvars < 3) {
	    System.err.println(this.getClass()
		    + ":Insert:Numero incorrecto de parametros");
	    return false;
	}
	PreparedStatement statement = null;
	boolean insercion = false;
	try {
	    // Se crea la sentencia SQL por partes.
	    String insert = "INSERT INTO `gauss`." + table + " (";
	    // Introduciomos en la sentencia las columnas que vamos a introducir
	    // con valores.
	    for (int i = 0; i < nvars; i++) {
		insert += entries[i];
		if (i < nvars - 1)
		    insert += ",";
	    }
	    insert += ") VALUES (";
	    // Introduciomos en la sentencia los valores vacios correspondientes
	    // a las
	    // columnas.
	    for (int i = 0; i < nvars; i++) {
		insert += "?";
		if (i < nvars - 1)
		    insert += ",";
	    }
	    insert += ")";
	    // System.out.println(insert);//DEBUG
	    statement = this.conexion.prepareStatement(insert);

	    // Insertamos de forma segura los parametros.
	    for (int i = 0; i < nvars; i++) {
		if (isInt(vars[i]))
		    statement.setInt(i + 1, Integer.parseInt(vars[i]));
		else if (isFloat(vars[i]))
		    statement.setFloat(i + 1, Float.parseFloat(vars[i]));
		else
		    statement.setString(i + 1, vars[i]);
	    }
	    statement.executeUpdate();
	    insercion = true;
	} catch (Exception e) {
	    // Si hubo un error lo muestra por consola y devuelve false;
	    e.printStackTrace();
	    insercion = false;
	} finally {
	    try {
		// Cerramos posibles conexiones abiertas
		if (statement != null)
		    statement.close();
	    } catch (Exception e) {
		System.err
			.println("Error cerrando conexiones: " + e.toString());
	    }
	}
	return insercion;
    }

    /**
     * Comprueba si el String que se pasa por parametros es float o no.
     * 
     * @param string
     *            - Cadena string a comprobar
     * @return True/False
     */
    private boolean isFloat(String string) {
	try {
	    Float.parseFloat(string);
	    return true;
	} catch (NumberFormatException n) {
	    return false;
	}
    }

    /**
     * Comprueba si el String que se pasa por parametros es Int o no.
     * 
     * @param string
     *            - Cadena string a comprobar
     * @return True/False
     */
    private boolean isInt(String string) {
	try {
	    Integer.parseInt(string);
	    return true;
	} catch (NumberFormatException n) {
	    return false;
	}
    }

    /**
     * Comprueba si la conexion esta aun activa y es valida.
     * 
     * @return Ture/False
     * @throws Exception
     *             Excepcion al validar la conexion con la base de datos.
     */
    public boolean isOpen() throws Exception {
	return this.conexion.isValid(3000);
    }

    /**
     * Establece conexion con el servidor MySQL.
     * 
     * @return True si pudo conectar, False si no pudo conectar.
     * @throws Exception
     *             Excepcion al abrir la conexion con la base de datos.
     */
    public boolean open() throws Exception {
	try {
	    this.conexion = DriverManager.getConnection(this.url, this.usr,
		    this.pwd);
	} catch (Exception e) {
	    System.err.println("Error al abrir la conexion: " + e.toString());
	    throw e;
	}
	return !this.conexion.isClosed();
    }

    /**
     * Devuelve la informacion completa de la tabla table en un array
     * bidimensional de m filas x n columnas.
     * 
     * @param table
     *            - tabla de la que queremos obtener todos los registros.
     * @return String[filas][columnas] registros.
     * @throws SQLException
     *             Excepcion al hacer una consulta a la base de datos.
     */
    public String[][] query(String table) throws SQLException {
	ResultSet rs = null;
	Statement s = null;
	String[][] result = null;
	try {
	    s = this.conexion.createStatement();
	    rs = s.executeQuery("SELECT * FROM " + table);
	    int m = count(table);
	    if (m <= 0)
		return null;
	    result = new String[m][rs.getMetaData().getColumnCount()];
	    int i = 0;
	    while (rs.next()) {
		for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
		    result[i][j] = rs.getString(j + 1);
		}
		i++;
	    }
	} catch (Exception e) {
	    System.err.println(e);
	    return null;
	} finally {
	    try {
		if (rs != null)
		    rs.close();
		if (s != null)
		    s.close();
	    } catch (Exception e) {
		System.err
			.println("Error cerrando conexiones: " + e.toString());
	    }
	}
	return result;
    }

    /**
     * Devuelve en un array bidimensional de m filas x n columnas las
     * coincidencias de la tabla table que tengan en la columna col el valor var
     * 
     * @param table
     *            - Tabla de la que queremos obtener todos los registros.
     * @param col
     *            - Columna que queremos comprobar.
     * @param var
     *            - Valor que ha de tomar la columna.
     * @return String[filas][columnas] registros.
     * @throws SQLException
     *             Excepcion al hacer una consulta a la base de datos.
     */
    public String[][] query(String table, String col, String var)
	    throws SQLException {
	ResultSet rs = null;
	PreparedStatement s = null;
	String[][] result = null;
	try {
	    String sentence = "SELECT * FROM " + table + " WHERE " + col
		    + " = ?";
	    s = this.conexion.prepareStatement(sentence);

	    if (isInt(var))
		s.setInt(1, Integer.parseInt(var));
	    else if (isFloat(var))
		s.setFloat(1, Float.parseFloat(var));
	    else
		s.setString(1, var);

	    rs = s.executeQuery();
	    int m = count(table, col, var);
	    if (m <= 0)
		return result;
	    result = new String[m][rs.getMetaData().getColumnCount()];
	    int i = 0;
	    while (rs.next()) {
		for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
		    result[i][j] = rs.getString(j + 1);
		}
		i++;
	    }
	} catch (Exception e) {
	    System.err.println(e.toString());
	    return null;
	} finally {
	    try {
		if (rs != null)
		    rs.close();
		if (s != null)
		    s.close();
	    } catch (Exception e) {
		System.err
			.println("Error cerrando conexiones: " + e.toString());
	    }
	}
	return result;
    }

    /**
     * Actualiza los valores de la columna col al valor value en los registros
     * de la tabla table que en la columna colKey tengan el valor valueKey.
     * 
     * @param table
     *            - Tablaa actualizar
     * @param colKey
     *            - Columna que ha de conincidir
     * @param valueKey
     *            - Valor que debe tener la columna para modificar ese registro
     * @param col
     *            - Columna en la que vamos a modificar el valor
     * @param value
     *            - Nuevo valor de la columna.
     * @return True/False
     */
    public boolean update(String table, String colKey, String valueKey,
	    String col, String value) {
	boolean updated = true;
	PreparedStatement s = null;

	try {
	    String update = "UPDATE `gauss`." + table + " SET " + col + " = ?";
	    update += " WHERE " + table + "." + colKey + " = ?";
	    s = this.conexion.prepareStatement(update);
	    if (isInt(value))
		s.setInt(1, Integer.parseInt(value));
	    else if (isFloat(value))
		s.setFloat(1, Float.parseFloat(value));
	    else
		s.setString(1, value);

	    if (isInt(valueKey))
		s.setInt(2, Integer.parseInt(valueKey));
	    else if (isFloat(valueKey))
		s.setFloat(2, Float.parseFloat(valueKey));
	    else
		s.setString(2, valueKey);
	    s.executeUpdate();
	    if (s.getUpdateCount() == 0)
		updated = false;
	} catch (Exception e) {
	    System.err.println(e);
	    updated = false;
	} finally {
	    try {
		if (s != null)
		    s.close();
	    } catch (Exception e) {
		System.err
			.println("Error cerrando conexiones: " + e.toString());
	    }
	}
	return updated;
    }
}
