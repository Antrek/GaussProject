package model;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import view.Gui;

import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;


/**
 * Esta clase es la encargada de gestionar las acciones basicas del programa. Es
 * la principal clase del modelo.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class App {
    /**
     * @param args
     *            Argumentos introduccidos al arrancar el programa.
     * @throws SQLException
     *             Excepcion al intentar conectar con la base de datos.
     */
    public static void main(String args[]) throws SQLException {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    new Gui();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Instancia de la base de datos.
     */
    private DB db;

    private Timer keepAlive;

    private int retrys;

    private Test test;

    /**
     * Instancia de usuario logeado en el sistema.
     */
    private User user;

    /**
     * @param action
     *            Controlador de enventos que manejara los eventos del
     *            temporizador.
     * @throws SQLException
     *             Excepcion al intentar iniciar la conexion a la base de datos.
     */
    public App(ActionListener action) throws SQLException {
	this.user = null;
	try {
	    this.db = new DB(
		    "jdbc:mysql://instance30012.db.xeround.com:19460/gauss",
		    "Gauss", "gaussuc3m");
	    this.db.open();
	} catch (Exception e) {
	    JOptionPane
		    .showMessageDialog(
			    null,
			    "No se pudo establecer la conexion con la base de datos, la aplicaicon se cerrara.\n"
				    + e.getLocalizedMessage(), "Error",
			    JOptionPane.ERROR_MESSAGE);
	    throw new MySQLNonTransientConnectionException();
	}

	this.keepAlive = new Timer(5000, action);
	this.keepAlive.setActionCommand("keepAlive");
	this.keepAlive.setRepeats(true);
	this.keepAlive.start();
    }

    /**
     * Crea una cuenta de usuario a la base de datos si esta no existia
     * previamente.
     * 
     * @param usr
     *            - Usuario
     * @param pwd
     *            - ContraseÃ±a
     * @param edit
     *            - Privilegios de editor.
     * @return True/False
     * @throws Exception
     *             Excepcion al intentar crear un usuario.
     */
    public boolean addAccount(String usr, String pwd, boolean edit)
	    throws Exception {

	boolean isCorrect = false;
	int numUsr = 0;

	if (this.searchAccount(usr) == null) {

	    try {
		numUsr = this.db.count(DB.TABLE_USERS);
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }

	    if (numUsr < 99) {

		int usrLen = usr.length(), pwdLen = pwd.length(), u, p;

		if ((usrLen >= 1) && (usrLen <= 8) && (pwdLen >= 1)
			&& (pwdLen <= 8)) {

		    for (u = 0; u < usrLen; u++) {
			if (!Character.isLetterOrDigit(usr.charAt(u)))
			    break;
		    }

		    for (p = 0; p < pwdLen; p++) {
			if (!Character.isLetterOrDigit(pwd.charAt(p)))
			    break;
		    }

		    if ((u == usrLen) && (p == pwdLen)) {

			String cols[] = new String[] { DB.COL_USER,
				DB.COL_PASSWORD, DB.COL_EDT };
			String vars[] = new String[] { usr, pwd,
				Integer.toString(edit ? 1 : 0) };

			try {
			    isCorrect = this.db.insert(DB.TABLE_USERS, cols,
				    vars, 3);
			} catch (SQLException e) {
			    e.printStackTrace();
			}

			if (!edit) {

			    cols = new String[] { DB.COL_USER,
				    DB.COL_TESTS_PLAYED, DB.COL_QUEST_CORRECT,
				    DB.COL_TOTAL_TIME, DB.COL_BEST_SCORE,
				    DB.COL_TOTAL_SCORE };
			    vars = new String[] { usr, "0", "0", "0", "0", "0" };

			    try {
				this.db.insert(DB.TABLE_STATS, cols, vars, 6);
			    } catch (SQLException e) {
				e.printStackTrace();
			    }

			}
		    }
		}
	    } else {
		throw (new Exception("Base de datos llena"));
	    }

	}

	return isCorrect;

    }

    /**
     * Crea una pregunta en el repositorio valiendose de los metodos de la base
     * de datos.
     * 
     * @param question
     *            - Pregunta
     * @param answer
     *            - Respuesta
     * @param difficulty
     *            - Dificultad
     * @return True/False
     * @throws Exception
     *             Excepcion al intentar crear una pregunta.
     */
    public boolean addQuestion(String question, String answer, int difficulty)
	    throws Exception {

	// Debe comprobar que la pregunta cumple los requisitos
	// La respuesta debe ser unica sin espacios etc...

	boolean isCorrect = false;
	int numQ = 0;

	try {
	    numQ = this.db.count(DB.TABLE_QUESTIONS);
	} catch (SQLException e1) {
	    e1.printStackTrace();
	}

	if (numQ < 99) {

	    int qLen = question.length();
	    int aLen = answer.length();

	    // Comprueba longitud pregunta
	    if (qLen >= 1 && qLen <= 150) {

		// Comprueba longitud respuesta
		if (aLen >= 1 && aLen <= 45) {

		    // Si hay un espacio, el bucle termina antes
		    int i;
		    for (i = 0; i < qLen; i++) {
			char c = question.charAt(i);

			if ((!Character.isLetterOrDigit(c)) && (c != ' ')
				&& (c != '\u00bf') && (c != '?'))
			    break;
		    }
		    int j;
		    for (j = 0; j < aLen; j++) {
			char c = answer.charAt(j);
			if (!Character.isLetterOrDigit(c))
			    break;
		    }

		    // Si esta todo correcto, aÃ±ade la pregunta
		    if ((i == qLen) && (j == aLen) && (difficulty >= 0)
			    && (difficulty <= 2)) {

			// answer = answer.toLowerCase();

			// Crea argumentos para db.insert()
			String cols[] = new String[] { DB.COL_QUESTION,
				DB.COL_ANSWER, DB.COL_DIFF };
			String vars[] = new String[] { question, answer,
				Integer.toString(difficulty) };

			// Captura posible SQLException
			try {
			    isCorrect = this.db.insert(DB.TABLE_QUESTIONS,
				    cols, vars, 3);
			} catch (SQLException e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	} else {
	    throw (new Exception("Base de datos llena"));
	}
	return isCorrect;
    }

    /**
     * 
     * Comprueba que la contraseña esté entre 1 y 8 caracteres alfanuméricos.
     * 
     * @param pwd
     *            - Contraseña a comprobar.
     * @return true si es correcta y false si es incorrecta.
     */
    public boolean checkPassword(String pwd) {

	boolean correct = false;
	int pwdLen = 0;

	if (pwd != null) {

	    pwdLen = pwd.length();

	    if ((pwdLen >= 1) && (pwdLen <= 8)) {

		int i;
		for (i = 0; i < pwdLen; i++) {

		    if (!Character.isLetterOrDigit(pwd.charAt(i)))
			break;

		}

		if (i == pwdLen)
		    correct = true;
	    }

	}

	return correct;
    }

    /**
     * Cierra conexion con la base de datos
     */
    public void closeConexion() {
	try {
	    this.db.close();
	} catch (SQLException e) {

	    e.printStackTrace();
	}
    }

    /**
     * Borra a un usuario de la base de datos si este existe.
     * 
     * @param usr
     *            - Nombre de usuario a borrar
     * @return True/False
     */
    public boolean deleteAccount(String usr) {
	boolean isCorrect = false;
	User rmUser = this.searchAccount(usr);
	if (rmUser != null) {

	    try {
		isCorrect = this.db.delete(DB.TABLE_USERS, DB.COL_USER, usr);
		if (!rmUser.isEditor())
		    isCorrect = this.db
			    .delete(DB.TABLE_STATS, DB.COL_USER, usr);
	    } catch (SQLException e) {

		e.printStackTrace();
	    }
	}

	return isCorrect;
    }

    /**
     * Borra la pregunta indicada del repositorio.
     * 
     * @param id
     *            - Indentificador de la pregunta
     * @return True/False
     */
    public boolean deleteQuestion(int id) {
	boolean deleted = false;

	try {
	    deleted = this.db.delete(DB.TABLE_QUESTIONS, DB.COL_ID,
		    Integer.toString(id));
	} catch (SQLException e) {

	    e.printStackTrace();
	}

	return deleted;
    }

    /**
     * Obtiene los n mejores resultados de la base de datos y los devuelve en
     * forma de string
     * 
     * @param n
     *            Numero de resultados a mostrar. Ej: top(10) top(15).
     * @return String - Ranking
     * @throws SQLException
     *             Excepcion al recuperar la informacion de la base de datos.
     */
    public String getRanking(int n) throws SQLException {
	String[][] scores = this.db.getBestScores(n);
	if (scores == null)
	    return "Error";
	String resultado = "<html><pre>";
	resultado += "Pos\tNombre\tPuntuacion<br>";
	for (int i = 0; i < scores[0].length; i++) {
	    if (scores[0][i] == null || scores[1][i] == null) {
		resultado += "<br>No hay mas resultados. <br>";
		break;
	    }
	    resultado += (i + 1) + "\u00b0\t" + scores[0][i] + "\t"
		    + scores[1][i] + "<br>";
	}

	resultado += "</pre></html>";
	return resultado;
    }

    /**
     * Devuelve el test
     * 
     * @return the test
     */
    public Test getTest() {
	return this.test;
    }

    /**
     * Devuelve el usuario
     * 
     * @return the user
     */
    public User getUser() {
	return this.user;
    }

    /**
     * Comprueba si la conexion sige activa y valida. Si no es asi, intenta
     * reconectar. Si se perdio la conexion y no puede reconectar intentara
     * reconectar cada 30s hasta un maxio de 3 veces.
     * 
     * @throws Exception
     *             No pudo reactivar la conexion.
     */
    public void keepAlive() throws Exception {
	if (!this.db.isOpen()) {
	    System.out.println("keppAlive");
	    try {
		System.out.println(++this.retrys);
		if (this.db.close() && this.db.open()) {
		    this.keepAlive.stop();
		    this.keepAlive.setDelay(5 * 1000);
		    this.keepAlive.start();
		    // keepAlive.setCoalesce(true);
		    this.retrys = 0;
		}
	    } catch (Exception e) {
		if (this.retrys > 3) {
		    throw (new Exception(
			    "Imposible conectar, revisa la conexion a internet."));
		}
		// keepAlive.setCoalesce(false);
		this.keepAlive.stop();
		this.keepAlive.setDelay(30 * 1000);
		this.keepAlive.start();
		System.out.println("excpThrow Reconect in 30s");
		throw e;
	    }
	    System.out.println("isAlive");
	}
	this.retrys = 0;

    }

    /**
     * Coteja la informacion recibida con la base de datos. De existir instancia
     * al usuario con su info de la base de datos
     * 
     * @param usrName
     *            Nombre de usuario
     * 
     * @param pass
     *            - Contraseña
     * @return True/False
     */
    public boolean login(String usrName, String pass) {
	/*
	 * Consulta a la base de datos, coteja la informacion, de existir el
	 * usuario crea uno nuevo con la info de la base de datos incluyendo las
	 * estadisticas.
	 */

	boolean exists = false;

	User user = searchAccount(usrName);

	if (user != null) {

	    if ((user.getUser().equals(usrName))
		    && (user.getPassword().equals(pass))) {

		if (user.isEditor()) {
		    this.user = new Editor(user.getUser(), user.getPassword());
		} else {
		    this.user = new Player(user.getUser(), user.getPassword(),
			    ((Player) user).getStats());
		}

		exists = true;
	    }
	}
	return exists;
    }

    /**
     * Deslogea al usuario del sistema.
     */
    public void logout() {
	this.user = null;
    }

    /**
     * Recibe como parametros la dificultad del test a realizar y el controlador
     * que escuchara los eventos del timer. El controlador posee como atributos
     * la GUI y la app, no es posible crear el listener dentro del test, hay que
     * arrastrarlo desde el propio controlador que es quien llama a esta
     * funcion.
     * 
     * Tras crear el nuevo test, el objeto Test queda a la espera de comenzar el
     * mismo.
     * 
     * @param difficulty
     *            Dificultad del test.
     * @param timListener
     *            Controlador de eventos /temporizador) del test.
     * 
     * @return boolean: true si se ha podido crear un nuevo test y false en caso
     *         contrario
     */
    public boolean newTest(int difficulty, ActionListener timListener) {

	boolean enough = false;
	String rawquestions[][] = null;
	Question questions[] = null;

	int numQ = 0;

	try {
	    numQ = this.db.count(DB.TABLE_QUESTIONS, DB.COL_DIFF,
		    Integer.toString(difficulty));
	} catch (SQLException e) {

	    e.printStackTrace();
	}

	if (numQ >= 5) {

	    enough = true;

	    try {
		rawquestions = this.db.query(DB.TABLE_QUESTIONS, DB.COL_DIFF,
			Integer.toString(difficulty));
	    } catch (SQLException e) {

		e.printStackTrace();
	    }

	    questions = new Question[numQ];

	    int j = 0;
	    while (j < numQ) {
		questions[j] = new Question(rawquestions[j][1],
			rawquestions[j][2],
			Integer.parseInt(rawquestions[j][3]));
		j++;
	    }

	    this.test = new Test(questions, timListener);

	}

	return enough;
    }

    /**
     * Busca un usuario en la base de datos.
     * 
     * @param usr
     *            - Nombre de usuario
     * @return Devuelve el usuario si existe, Null si no existe.
     */
    public User searchAccount(String usr) {

	User user = null;
	String rawUser[][] = null;

	try {
	    rawUser = this.db.query(DB.TABLE_USERS, DB.COL_USER, usr);
	} catch (SQLException e) {

	    e.printStackTrace();
	}

	if (rawUser != null) {

	    if (rawUser[0][2].equals("0")) {

		user = new Player(rawUser[0][0], rawUser[0][1]);
		Stats stats = new Stats();
		String rawstats[][] = null;

		try {
		    rawstats = this.db.query(DB.TABLE_STATS, DB.COL_USER,
			    user.getUser());
		} catch (SQLException e) {

		    e.printStackTrace();
		}

		if (rawstats != null) {

		    stats.setqAnswer(Integer.parseInt(rawstats[0][1]));
		    stats.setqCorrect(Integer.parseInt(rawstats[0][2]));
		    stats.setTimeTotal(Integer.parseInt(rawstats[0][3]));
		    stats.setBestScore(Integer.parseInt(rawstats[0][4]));
		    stats.setTotalScore(Integer.parseInt(rawstats[0][5]));

		    stats.updateRecords();

		    ((Player) user).setStats(stats);

		    /*
		     * ((Player) user).getStats().setqAnswer(
		     * Integer.parseInt(rawstats[0][1])); ((Player)
		     * user).getStats().setqCorrect(
		     * Integer.parseInt(rawstats[0][2])); ((Player)
		     * user).getStats().setTimeTotal(
		     * Integer.parseInt(rawstats[0][3])); ((Player)
		     * user).getStats().setBestScore(
		     * Integer.parseInt(rawstats[0][4])); ((Player)
		     * user).getStats().setTotalScore(
		     * Integer.parseInt(rawstats[0][5]));
		     * 
		     * ((Player) user).getStats().updateRecords();
		     */

		}
	    } else {
		user = new Editor(rawUser[0][0], rawUser[0][1]);
	    }

	}

	return user;
    }

    /**
     * Busca una pregunta en la base de datos.
     * 
     * @param id
     *            - Identificador de la pregunta.
     * @return Question - Devuelve la pregunta de la base de datos si existe.
     *         Null si no existe.
     */
    public Question searchQuestion(int id) {
	Question question = null;
	String rawQuestion[][] = null;

	try {
	    rawQuestion = this.db.query(DB.TABLE_QUESTIONS, DB.COL_ID,
		    Integer.toString(id));
	} catch (SQLException e) {

	    e.printStackTrace();
	    System.out.println("WTF");
	}

	if (rawQuestion != null) {

	    try {
		question = new Question(rawQuestion[0][1], rawQuestion[0][2],
			Integer.parseInt(rawQuestion[0][3]));
		question.setId(Integer.parseInt(rawQuestion[0][0]));
	    } catch (NumberFormatException e) {
		e.printStackTrace();
	    }
	}

	return question;
    }

    /**
     * Modifica el atributo Test
     * 
     * @param test
     *            the test to set
     */
    public void setTest(Test test) {
	this.test = test;
    }

    /**
     * Devuelve la lista de nombres de usuario.
     * 
     * @return Array de nombres de usuario.
     */
    public String showAccounts() {
	String users[] = null;
	String rawUsers[][] = null;

	try {
	    rawUsers = this.db.query(DB.TABLE_USERS);
	} catch (SQLException e) {

	    e.printStackTrace();
	}

	if (rawUsers != null) {

	    int uLen = rawUsers.length, i = 0;

	    if (uLen > 0) {

		users = new String[uLen];

		while (i < uLen) {

		    users[i] = rawUsers[i][0];
		    i++;
		}
	    }
	}

	if (users == null) {
	    return "No hay cuentas que mostrar.";
	}
	// Ordenacion alfabetica
	boolean ordenado = false;
	String aux;
	do {
	    ordenado = true;
	    for (int i = 0; i < users.length - 1; i++) {
		if (users[i].compareToIgnoreCase(users[i + 1]) > 0) {
		    aux = users[i];
		    users[i] = users[i + 1];
		    users[i + 1] = aux;
		    ordenado = false;
		}
	    }
	} while (!ordenado);

	// Formato de texto.
	String texto = "";
	for (int i = 0; i < users.length; i++) {
	    texto += "\t" + (i + 1) + "    " + users[i] + "\n";
	}

	return texto;
    }

    /**
     * Devuelve el texto de ayuda.Lo lee del fichero Manual.txt
     * 
     * @return String - El texto de ayuda.
     */
    public String showHelp() {
	String help = "";
	try {
	    FileInputStream fstream = new FileInputStream(new java.io.File("Manual.txt"));
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    while ((strLine = br.readLine()) != null) {
		help += strLine + "\n";
	    }
	    in.close();
	} catch (Exception e) {
	    System.err.println("Error: " + e.getMessage());
	    help = "Hubo un problema al abrir el manual.";
	}
	return help;
    }

    /**
     * Devuelve la lista completa de preguntas para la dificultad indicada, -1
     * mostraria todas las dificultades. Null si no hay preguntas.
     * 
     * @param diff
     *            Dificultad de las preguntas a mostrar.
     * @return String
     */
    public String showQuestions(int diff) {

	Question questions[] = null;
	String rawQuestions[][] = null;

	try {
	    rawQuestions = this.db.query(DB.TABLE_QUESTIONS, DB.COL_DIFF,
		    Integer.toString(diff));
	} catch (SQLException e) {

	    e.printStackTrace();
	}

	if (rawQuestions != null) {

	    int qNum = rawQuestions.length, i = 0;

	    if (qNum > 0) {

		questions = new Question[qNum];

		while (i < qNum) {

		    try {
			questions[i] = new Question(rawQuestions[i][1],
				rawQuestions[i][2],
				Integer.parseInt(rawQuestions[i][3]));
			questions[i]
				.setId(Integer.parseInt(rawQuestions[i][0]));
		    } catch (NumberFormatException e) {
			e.printStackTrace();
		    }

		    i++;
		}
	    }
	}

	if (questions == null)
	    return "No hay preguntas en esta tabla.";
	// Ordenacion
	boolean ordenado = false;
	Question aux;
	do {
	    ordenado = true;
	    for (int i = 0; i < questions.length - 1; i++) {
		if ((questions[i].getQuestion())
			.compareToIgnoreCase(questions[i + 1].getQuestion()) > 0) {
		    aux = questions[i];
		    questions[i] = questions[i + 1];
		    questions[i + 1] = aux;
		    ordenado = false;
		}
	    }
	} while (!ordenado);
	// Formato de texto
	String texto = "";
	for (int i = 0; i < questions.length; i++) {
	    texto += "--------------------------\n";
	    texto += "ID:" + questions[i].getId() + " Dificultad: "
		    + Question.diff2String(questions[i].getDifficulty()) + "\n";
	    texto += "Pregunta: " + questions[i].getQuestion() + "\n";
	    texto += "Respuesta: " + questions[i].getAnswer() + "\n";
	}
	return texto;
    }

    /**
     * Para el test que se est� realizando
     */
    public void stopTest() {
	this.test.stop();
	this.test = null;
    }

    /**
     * Actualiza las estadisticas del jugador
     */
    public void updatePlayerStats() {

	this.test.saveStats((Player) this.user);

	String cols[] = new String[] { DB.COL_USER, DB.COL_TESTS_PLAYED,
		DB.COL_QUEST_CORRECT, DB.COL_TOTAL_TIME, DB.COL_BEST_SCORE,
		DB.COL_TOTAL_SCORE };
	String vars[] = new String[6];
	vars[0] = this.user.getUser();
	vars[1] = Integer
		.toString(((Player) this.user).getStats().getqAnswer());
	vars[2] = Integer.toString(((Player) this.user).getStats()
		.getqCorrect());
	vars[3] = Integer.toString(((Player) this.user).getStats()
		.getTimeTotal());
	vars[4] = Integer.toString(((Player) this.user).getStats()
		.getBestScore());
	vars[5] = Integer.toString(((Player) this.user).getStats()
		.getTotalScore());

	String user = vars[0];

	for (int i = 1; i < vars.length; i++) {

	    this.db.update(DB.TABLE_STATS, DB.COL_USER, user, cols[i], vars[i]);

	}

    }

    /**
     * Actualiza los campos que difieran en la base de datos con el objeto
     * enviado por parametros.
     * 
     * @param question
     *            - Objeto con la nueva informacion
     * @return True/False
     */
    public boolean updateQuestion(Question question) {
	// Debera comprobar que existe la pregunta que se quiere actualizar.

	boolean isCorrect = false;
	Question auxQ = null;

	if (question == null)
	    return isCorrect;

	if ((auxQ = this.searchQuestion(question.getId())) != null) {

	    boolean update[] = new boolean[3];

	    update[0] = !question.getQuestion().equals(auxQ.getQuestion());
	    update[1] = !question.getAnswer().equals(auxQ.getAnswer());
	    update[2] = question.getDifficulty() != auxQ.getDifficulty();

	    for (int i = 0; i < update.length; i++) {

		if (update[i] == true) {

		    String col = null;
		    String value = null;

		    switch (i) {
		    case 0:
			col = DB.COL_QUESTION;
			value = question.getQuestion();
			break;
		    case 1:
			col = DB.COL_ANSWER;
			value = question.getAnswer();
			break;
		    case 2:
			col = DB.COL_DIFF;
			value = Integer.toString(question.getDifficulty());
			break;
		    }

		    isCorrect = this.db.update(DB.TABLE_QUESTIONS, DB.COL_ID,
			    Integer.toString(auxQ.getId()), col, value);
		}
	    }

	}

	return isCorrect;
    }

    /**
     * Actualiza los campos que difieran en la base de datos con el objeto
     * enviado por parametros.
     * 
     * @param user
     *            - Objeto con la nueva informacion
     * @return True/False
     */
    public boolean updateUser(User user) {
	// Debera comprobar que existe el usuario que se quiere actualizar.
	boolean isCorrect = false;
	User auxU = null;

	if (user == null)
	    return isCorrect;

	if ((auxU = this.searchAccount(user.getUser())) != null) {

	    boolean update[] = new boolean[3];

	    update[0] = !user.getUser().equals(auxU.getUser());
	    update[1] = !user.getPassword().equals(auxU.getPassword());
	    update[2] = user.isEditor() != auxU.isEditor();

	    for (int i = 0; i < update.length; i++) {

		if (update[i] == true) {

		    String col = null;
		    String value = null;

		    switch (i) {
		    case 0:
			col = DB.COL_USER;
			value = user.getUser();
			break;
		    case 1:
			col = DB.COL_PASSWORD;
			value = user.getPassword();
			break;
		    case 2:
			col = DB.COL_EDT;
			value = Integer.toString(user.isEditor() ? 1 : 0);
			break;
		    }

		    isCorrect = this.db.update(DB.TABLE_USERS, DB.COL_USER,
			    auxU.getUser(), col, value);
		}
	    }

	}

	return isCorrect;
    }

}