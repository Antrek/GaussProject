package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import model.Question;
import model.Test;
import model.User;
import view.Gui;

/**
 * Clase controller que implementa el controlador del programa gestionando la
 * interaccion de las demandas del usuario medainte la gui(view) con las
 * acciones realizables en la aplicacion(Modelo)
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class Controller implements ActionListener {

    /**
     * Interfaz grafica de la aplicacion.
     */
    private Gui gui;

    /**
     * Contador de tiempo para cada pregunta del test.
     */
    private int timeLeft = -1;

    /**
     * Constructor de la clase Contoller
     * 
     * @param gui
     *            Interfaz que usara dicho controlador.
     */
    public Controller(Gui gui) {
	this.setGui(gui);
    }

    /**
     * 
     * Captura las acciones realizadas para incluir una pregunta en la BD
     * 
     * @param compSrc
     *            - Componente que realiza la accion.
     */
    private void actionAddQuestion(Component compSrc) {

	if (compSrc.getName().equalsIgnoreCase("Volver")) {

	    gotoMenu();

	} else {
	    String preg = this.getGui().getQuestionTextArea().getText();
	    if (preg.length() < 1 || preg.length() > 150) {
		JOptionPane
			.showMessageDialog(compSrc,
				"Comprueba la longitud de la pregunta (max 150 caracteres)");
		return;
	    }
	    String answer = this.getGui().getAnswerTextArea().getText();
	    if (answer.length() < 1 || answer.length() > 45) {
		JOptionPane
			.showMessageDialog(compSrc,
				"Comprueba la longitud de la respuesta (max 45 caracteres)");
		return;
	    }
	    int diff;
	    if (this.getGui().getRdbtnFcil().isSelected())
		diff = Question.DIFFICULTY_EASY;
	    else if (this.getGui().getRdbtnMedio().isSelected())
		diff = Question.DIFFICULTY_MEDIUM;
	    else if (this.getGui().getRdbtnDificil().isSelected())
		diff = Question.DIFFICULTY_HARD;
	    else {
		JOptionPane.showMessageDialog(compSrc,
			"Seleccione una dificultad primero");
		return;
	    }
	    boolean check;
	    try {
		check = this.getGui().getAplicacion()
			.addQuestion(preg, answer, diff);

	    } catch (Exception e) {
		this.getGui()
			.showErrMsg(
				"No se pudo a\u00F1adir la pregunta: "
					+ e.getMessage());
		return;
	    }
	    if (check) {

		JOptionPane.showMessageDialog(compSrc,
			"Pregunta creada correctamente.");

		gotoMenu();

	    } else
		this.getGui()
			.showErrMsg(
				"Hubo un problema al crear la pregunta. Compruebe el formato introducido.");
	}
    }

    /**
     * Captura las acciones realizadas para cambiar la contrase�a.
     * 
     * @param user
     *            - Usuario a actualizar.
     */
    private void actionChangePwd(Component compSrc, User user) {

	String newPwd1 = String.copyValueOf(this.getGui().getNewPwd1()
		.getPassword());
	String newPwd2 = String.copyValueOf(this.getGui().getNewPwd2()
		.getPassword());

	if (compSrc.getName().equals("Atras")) {
	    gotoMenu();
	    return;
	}

	if (newPwd1.compareTo("") == 0 || newPwd2.compareTo("") == 0) { //$NON-NLS-2$

	    this.getGui().showErrMsg("Debe completar todos los campos.");
	    return;

	} else

	if (user == this.getGui().getAplicacion().getUser()) {

	    String oldPwd = String.copyValueOf(this.getGui().getOldPwd()
		    .getPassword());

	    if (oldPwd.compareTo("") == 0) {
		this.getGui().showErrMsg("Debe completar todos los campos.");
		return;
	    } else if (user.getPassword().compareTo(oldPwd) != 0) {

		this.getGui().showErrMsg("Su contrase\u00F1a es incorrecta.");
		return;

	    }
	}

	if (newPwd1.compareTo(newPwd2) != 0) {

	    this.getGui().showErrMsg("Las contrase\u00F1as no conciden.");

	} else {

	    if (this.getGui().getAplicacion().checkPassword(newPwd1) == true)
		user.setPassword(newPwd1);

	    if (this.getGui().getAplicacion().updateUser(user) == true) {

		this.getGui().showPopUp(
			"Contrase\u00F1a cambiada correctamente",
			"Contrase\u00F1a cambiada", JOptionPane.PLAIN_MESSAGE);
		gotoMenu();
		return;

	    }
	    this.getGui()
		    .showErrMsg(
			    "Lo sentimos, ha habido un problema al hacer el cambio.\nComprueba el formato de la nueva contrase\u00F1a");

	}

	return;
    }

    /**
     * Realiza las acciones correspondientes la peticion de borrar una pregunta
     * interactuando con el modelo.
     */
    private void actionDelQuestion() {

	String strId = this.getGui().showTextFieldWindow(
		"Escriba el identificador de la pregunta a borrar");

	if (strId != null) {

	    int id = -1;

	    try {
		id = Integer.parseInt(strId);
		if (this.getGui().getAplicacion().deleteQuestion(id)) {
		    this.getGui().showPopUp(
			    "Pregunta eliminada correctamente!",
			    "Pregunta eliminada", JOptionPane.PLAIN_MESSAGE);
		} else {
		    this.getGui().showErrMsg(
			    "No se ha podido eliminar la pregunta.");
		}
	    } catch (NumberFormatException e) {
		this.getGui().showErrMsg(
			"Debe introducir un identificador numérico.");
	    }

	}
    }

    /**
     * Realiza las acciones correspondientes la peticionde borrar un usuario
     * interactuando con el modelo.
     */
    private void actionDelUser() {

	String username = this.getGui().showTextFieldWindow(
		"Escriba el nombre del usuario a eliminar");

	if (username != null) {

	    User usr = this.getGui().getAplicacion().searchAccount(username);

	    if (usr != null) {

		if (usr.getUser().equalsIgnoreCase(
			this.getGui().getAplicacion().getUser().getUser())) {
		    this.getGui().showErrMsg(
			    "No puedes borrar al usuario actualmente logeado");
		    return;
		}
		if (this.getGui().getAplicacion().deleteAccount(username)) {

		    this.getGui()
			    .showPopUp(
				    "Usuario " + username
					    + " eliminado correctamente", "Usuario eliminado", //$NON-NLS-2$
				    JOptionPane.PLAIN_MESSAGE);

		} else {

		    this.getGui().showErrMsg(
			    "No se ha podido eliminar al usuario " + username);

		}

	    } else {
		this.getGui().showErrMsg(
			"No se ha podido encontrar al usuario " + username);
	    }

	}
    }

    /**
     * 
     * Captura las acciones realizadas dentro del menú del usuario editor y
     * realiza la operaciun oportuna.
     * 
     * @param compSrc
     *            - Componente que realiza la acciun.
     */
    private void actionEditorMenu(Component compSrc) {

	if (compSrc.getName().compareTo("Mostrar preguntas") == 0) {

	    int diff = this.getGui().selectDifficult();

	    if (diff != JOptionPane.CLOSED_OPTION)
		this.getGui().showList(
			this.getGui().getAplicacion().showQuestions(diff));

	} else if (compSrc.getName().compareTo("Borrar pregunta") == 0) {

	    actionDelQuestion();

	} else if (compSrc.getName().compareTo("A\u00F1adir Editor") == 0) {

	    this.getGui().showRegister(true);

	} else if (compSrc.getName().compareTo("Mostrar cuentas") == 0) {

	    this.getGui()
		    .showList(this.getGui().getAplicacion().showAccounts());

	} else if (compSrc.getName().compareTo("Buscar usuario") == 0) {

	    actionSearchUser();

	} else if (compSrc.getName().compareTo("Borrar cuenta") == 0) {

	    actionDelUser();

	} else if (compSrc.getName().compareTo("Perfil") == 0) {

	    this.getGui().showProfile();

	} else if (compSrc.getName().compareTo("Buscar pregunta") == 0) {

	    actionSearchQuestion();

	} else if (compSrc.getName().compareTo("A\u00F1adir pregunta") == 0) {

	    this.getGui().addQuest(null);

	} else if (compSrc.getName().compareTo("Logout") == 0) {

	    this.getGui().getAplicacion().logout();
	    this.getGui().showLogin();
	}
    }

    /**
     * 
     * Captura las acciones realizadas a la hora de la edicion de una pregunta
     * de la BD.
     * 
     * @param compSrc
     *            Componente que realiza la acciun.
     */
    private void actionEditQuestion(Component compSrc) {

	if (compSrc.getName().equalsIgnoreCase("Volver")) {

	    gotoMenu();

	} else {
	    String question = this.getGui().getQuestionTextArea().getText();
	    if (question.length() < 1 || question.length() > 150) {
		JOptionPane.showMessageDialog(compSrc,
			"Comprueba la longitud de la pregunta");
		return;
	    }
	    String resp = this.getGui().getAnswerTextArea().getText();
	    if (resp.length() < 1 || resp.length() > 45) {
		JOptionPane.showMessageDialog(compSrc,
			"Comprueba la longitud de la respuesta");
		return;
	    }
	    int diff;
	    if (this.getGui().getRdbtnFcil().isSelected())
		diff = 0;
	    else if (this.getGui().getRdbtnMedio().isSelected())
		diff = 1;
	    else
		diff = 2;
	    Question newQuest = new Question(question, resp, diff);
	    newQuest.setId(Integer.parseInt(this.getGui().getLblvalueId()
		    .getText()));
	    if (this.getGui().getAplicacion().updateQuestion(newQuest)) {
		JOptionPane.showMessageDialog(compSrc,
			"Pregunta actualizada correctamente.");
		gotoMenu();
	    } else
		JOptionPane.showMessageDialog(compSrc,
			"Hubo un problema al actualizar la pregunta.");
	}

    }

    /**
     * 
     * Captura las acciones realizadas para poder editar un usuario editor
     * 
     * @param compSrc
     *            - Componente que realiza la acciun.
     */
    private void actionEditUser(Component compSrc) {

	if (compSrc.getName().equalsIgnoreCase("atras")) {
	    gotoMenu();
	} else if (compSrc.getName().equalsIgnoreCase("Cambiar_pass")) {
	    this.getGui().showChangePwd();
	}

    }

    /**
     * Captura la acion de pulsar Creditos en el menuBar
     */
    private void actionJMenuCreditos() {
	this.getGui()
		.showInfoMsg(
			"Proyecto Gauss\t Version 0.2\n\nProyecto creado por:\n\nAlejandro Gil Torres\nJavier Estevez Aguado\nJavier Hernantes Anton\nSebastian Alvarez Mendez\nMarina Paz Garcia",
			"Creditos");
    }

    /**
     * Captura la acion de pulsar Logout en el menuBar
     */
    private void actionJMenuDesconectar() {
	if (JOptionPane.showConfirmDialog(this.getGui().getFrame(),
		"\u00bfEst\u00e1 seguro de que desea desconectarse?",
		"Desconexi\u00f3n", JOptionPane.YES_NO_OPTION, 2) == 0) {
	    this.getGui().getAplicacion().logout();
	    gotoMenu();
	}
	return;
    }

    /**
     * Captura la acion de pulsar Manual en el menuBar
     */
    private void actionJMenuManual() {
	this.getGui().showList(this.getGui().getAplicacion().showHelp());
    }

    /**
     * Captura la acion de pulsar Ranking en el menuBar. Muestra una ventana
     * preguntando el numero de usuarios a mostrar. Debe ser un valor positivo >
     * 0.
     */
    private void actionJMenuRanking() {
	String strNumber = this.getGui().showTextFieldWindow(
		"Escriba el numero de usuarios a visualizar");

	if (strNumber != null) {

	    int n = -1;

	    try {
		n = Integer.parseInt(strNumber);

		String rank = this.getGui().getAplicacion().getRanking(n);

		this.getGui().showPopUp(rank, "Ranking",
			JOptionPane.PLAIN_MESSAGE);

	    } catch (Exception e) {
		if (e.getClass() == NumberFormatException.class)
		    this.getGui().showErrMsg("Debe introducir un n\u00famero.");
		else
		    this.getGui()
			    .showErrMsg(
				    "Ha habido un error con la base de datos, por favor intente m\u00e1s tarde.");

	    }

	}
    }

    private void actionJMenuSalir() {
	if (JOptionPane
		.showConfirmDialog(
			this.getGui().getFrame(),
			"\u00bfEst\u00e1 seguro de que desea salir de la aplicaci\u00f3n?",
			"Salir", JOptionPane.YES_NO_OPTION, 2) == 0) {
	    this.getGui().getAplicacion().closeConexion();
	    System.exit(0);
	}
    }

    /**
     * Captura las acciones realizadas al logearse en la aplicacion
     * 
     * @param compSrc Componente que realiza la accion.
     */
    private void actionLogin(Component compSrc) {

	if (compSrc.getName().equals("Login")
		|| compSrc.getName().equals("passwordTextfield")) {
	    boolean accion = false;
	    String usr = this.getGui().getUsernameLoginTextArea().getText();
	    String pwd = String.copyValueOf(this.getGui()
		    .getPasswordFieldLogIn().getPassword());
	    accion = this.getGui().getAplicacion().login(usr, pwd);
	    if (!accion)
		JOptionPane.showMessageDialog(this.getGui().getFrame(),
			"Ops hay un error con tus datos.");
	    else
		gotoMenu();
	} else if (compSrc.getName().equals("Registrate")) {
	    this.getGui().showRegister(false);
	}

    }

    /**
     * Captura el item del menú que realiza la acciun e invoca al metodo
     * correspondiente para efectuarla.
     * 
     * @param item
     *            - Item que realiza la acciun.
     */
    private void actionMenuBar(JMenuItem item) {

	if (item.getText().compareTo("Ranking") == 0) {

	    actionJMenuRanking();

	} else if (item.getText().compareTo("Logout") == 0) {

	    actionJMenuDesconectar();

	} else if (item.getText().compareTo("Salir de la aplicaci\u00f3n") == 0) {

	    actionJMenuSalir();

	} else if (item.getText().compareTo("Manual de usuario") == 0) {

	    actionJMenuManual();

	} else if (item.getText().compareTo("Acerca de") == 0) {

	    actionJMenuCreditos();

	}
    }

    /**
     * Comienza un nuevo test pidiendo la dificultad en una ventana emergente.
     * Si no hay preguntas suficientes advierte al usuario con un mensaje de
     * error; en caso contrario invoca a la ventana inicial del test y espera a
     * confirmaciun del usuario.
     */
    private void actionNewTest() {

	int difficulty = this.getGui().selectDifficult();

	if (difficulty != JOptionPane.CLOSED_OPTION) {
	    if (!this.getGui().getAplicacion().newTest(difficulty, this)) {
		this.getGui()
			.showErrMsg(
				"Lo sentimos, no hay preguntas suficientes para realizar el test.");
	    } else {
		this.getGui().showTest(
			"Pulsa el bot\u00f3n para comenzar", "Comenzar", //$NON-NLS-2$
			"0");
	    }
	}
    }

    /**
     * @param e
     *            Evento que tiene el controlador asignado y ha desencadenado
     *            una accion.
     * 
     */
    public void actionPerformed(ActionEvent e) {

	String src = "";
	Component compSrc = null;

	try {
	    src = getButtonMainFrameName(e);
	    compSrc = (Component) e.getSource();
	} catch (ClassCastException e2) {
	}

	// Si no es null se hizo bien el casting y es un Component
	if (compSrc != null) {

	    if (compSrc.getName().equalsIgnoreCase("Volver_al_menu")) {

		if (src.equalsIgnoreCase("Test")) {
		    this.getGui().getAplicacion().stopTest();
		}
		gotoMenu();
		return;

	    } else if (src.equalsIgnoreCase("Login")) // MARINA
	    {
		actionLogin(compSrc);

	    } else if (src.equalsIgnoreCase("Registro")
		    || src.equalsIgnoreCase("Nuevo editor")) // MARINA
	    {
		actionRegister(compSrc);
	    } else if (src.equalsIgnoreCase("Menu-Jugador")) // JAVI
	    {
		actionPlayerMenu(compSrc);
	    } else if (src.equalsIgnoreCase("Menu-Editor")) // SEBAS
	    {
		actionEditorMenu(compSrc);
	    } else if (src.equalsIgnoreCase("Editar pregunta")) // DICREW
	    {
		actionEditQuestion(compSrc);
	    } else if (src.equalsIgnoreCase("A\u00F1adir pregunta")) // DICREW
	    {
		actionAddQuestion(compSrc);
	    } else if (src.equalsIgnoreCase("Editar usuario")
		    || src.equalsIgnoreCase("Perfil")) // DICREW
	    {
		actionEditUser(compSrc);
	    } else if (src.equalsIgnoreCase("Test")) // SEBAS
	    {
		actionTest(compSrc);
	    } else if (src.equalsIgnoreCase("Cambiar contrase\u00F1a")) // Javier
	    {
		actionChangePwd(compSrc, this.getGui().getModUser());
	    }
	}
	// Si es null es un Timer o un JMenuItem
	else {

	    Class<?> srcClass = e.getSource().getClass();
	    if (srcClass == Timer.class) {
		Timer t = (Timer) e.getSource();
		if (t.getActionCommand().equalsIgnoreCase("keepAlive")) {
		    try {
			this.getGui().getAplicacion().keepAlive();
		    } catch (SQLException e1) {
			this.getGui()
				.showErrMsg(
					"Ha habido un error con la conexion a la base de datos. Intentando reconexion.\n"
						+ e1.getMessage());
		    }

		    catch (Exception e1) {
			this.getGui()
				.showErrMsg(
					"Ha habido un error con la conexion a la base de datos. La aplicacion se cerrara.\n"
						+ e1.getMessage());
			System.exit(-1);
		    }

		} else
		    actionTest(e.getSource());

	    } else if (srcClass == JMenuItem.class) {

		actionMenuBar((JMenuItem) e.getSource());

	    }
	}

    }

    /**
     * 
     * Captura las acciones realizadas dentro del menu del usuario jugador y
     * realiza la operacion oportuna.
     * 
     * @param compSrc
     *            - Componente que realiza la acciun.
     */
    private void actionPlayerMenu(Component compSrc) {

	if (compSrc.getName().equals("Jugar")) {
	    actionNewTest();
	} else if (compSrc.getName().compareTo("Mi perfil") == 0) {
	    this.getGui().showProfile();
	} else if (compSrc.getName().compareTo("Logout") == 0) {
	    this.getGui().getAplicacion().logout();
	    this.getGui().showLogin();
	}
    }

    /**
     * Captura las acciones realizadas al registrarse, crea un nuevo usuario
     * 
     * @param compSrc Componente que realiza la acciun.
     */
    private void actionRegister(Component compSrc) {

	String usr_1 = this.getGui().getUsernameTextArea().getText();
	String pwd_2 = String.copyValueOf(this.getGui()
		.getPasswordFieldRegistro().getPassword());
	String pwd_3 = String.copyValueOf(this.getGui()
		.getPasswordField2Registro().getPassword());

	boolean isEditor = false;

	if (compSrc.getName().equals("Atras")) {
	    gotoMenu();
	    return;
	}

	if (compSrc.getName().equals("Crear_Cuenta_editor"))
	    isEditor = true;

	if (!pwd_2.equals(pwd_3)) {
	    JOptionPane.showMessageDialog(this.getGui().getFrame(),
		    "Las contrase\u00F1as no conciden.");
	    return;
	}
	boolean add;
	try {
	    add = this.getGui().getAplicacion()
		    .addAccount(usr_1, pwd_2, isEditor);
	} catch (Exception e) {
	    this.getGui().showErrMsg(
		    "No se pudo crear la cuenta: " + e.getMessage());
	    return;
	}
	if (add) {

	    JOptionPane.showMessageDialog(this.getGui().getFrame(),
		    "Cuenta Creada correctamente");
	    gotoMenu();
	    return;

	}
	this.getGui().showErrMsg("Ha habido un error al crear la cuenta.");
	return;

    }

    /**
     * Realiza la respuesta la peticion de buscar una pregunta.
     */
    private void actionSearchQuestion() {

	String input = this.getGui().showInputMsg(
		"Introduce el identificador de la pregunta.");

	if (input != null) {
	    try {
		int id = Integer.parseInt(input);
		Question question = this.getGui().getAplicacion().searchQuestion(id);
		if (question == null) {
		    this.getGui().showErrMsg(
			    "No se ha podido encontrar la pregunta.");
		} else {
		    this.getGui().addQuest(question);
		}
	    } catch (Exception e) {
		this.getGui().showErrMsg("Debes introducir un valor numerico");
	    }
	    return;
	}
    }

    /**
     * Realiza la respuesta la peticion de buscar un usuario.
     */
    private void actionSearchUser() {

	String username = this.getGui().showTextFieldWindow(
		"Escriba el nombre del usuario a buscar");

	if (username != null) {

	    User usr = this.getGui().getAplicacion().searchAccount(username);

	    if (usr != null) {

		this.getGui().modifyUser(usr);

	    } else {
		this.getGui().showErrMsg(
			"No se ha podido encontrar al usuario " + username);
	    }

	}
    }

    /**
     * 
     * Realiza las funciones del test dependiendo del botun que se haya pulsado
     * o del estado del temporizador.
     * 
     * @param src
     *            - Objeto que realiza la acciun; puede ser un JButton o un
     *            Timer
     */
    private void actionTest(Object src) {

	// La unica forma de que no sea null es haber pulsado "Hacer test",
	// que haya lanzado newTest() y por lo tanto creado un nuevo test
	if (this.getGui().getAplicacion().getTest() != null) {

	    // Vemos si es evento del timer (saltan cada segundo). Si lo es
	    // y todavia queda tiempo, actualiza el contador de la GUI. Si
	    // no queda tiempo, para el contador, avisa que se acabo el
	    // tiempo y checkea la pregunta.
	    if (src.equals(this.getGui().getAplicacion().getTest().getTimer())) {

		this.actionTestTimer((Timer) src);

	    } else {
		// Mira si la fuente es el boton inferior
		// (Continuar/Responder). Ademas, evita pulsaciones antes de
		// comenzar el test (timeLeft esta a -1 antes de comenzar)
		if (src.equals(this.getGui().getCheckButton())
			|| src.equals(this.getGui().getAnswerTextArea())) {

		    String srcName = ((Component) src).getName();
		    // Si el boton es "Responder" checkea la pregunta
		    if (srcName.compareTo("Responder") == 0) {

			actionTestAnswer();

		    } else {

			// Si es "Continuar", intenta lanzar la siguiente
			// pregunta. Si no hay mas preguntas, avisa que el
			// test ha finalizado.
			if ((srcName.compareTo("Continuar") == 0)
				|| (srcName.compareTo("Comenzar") == 0)) {

			    actionTestStartContinue();

			}
			// Si no es "Continuar" es "Jugar de nuevo"
			else {
			    if (srcName.compareTo("Jugar de nuevo") == 0)
				actionNewTest();
			}

		    }
		}
	    }
	}
    }

    /**
     * Comprueba la pregunta escrita en la interfaz.
     */
    private void actionTestAnswer() {

	String answer = this.getGui().getAplicacion().getTest().showAnswer();

	if (this.getGui()
		.getAplicacion()
		.getTest()
		.checkAnswer(this.getGui().getAnswerTextArea().getText(),
			this.timeLeft)) {

	    this.getGui().showPopUp("Enhorabuena, has acertado la pregunta!",
		    "Pregunta acertada", JOptionPane.PLAIN_MESSAGE);
	} else {
	    String message = "Lo sentimos, has fallado...\n\nRespuesta correcta: "
		    + answer;

	    this.getGui().showPopUp(message, "Pregunta fallada",
		    JOptionPane.WARNING_MESSAGE);
	}

	this.getGui().getCheckButton().setText("Continuar");
	this.getGui().getCheckButton().setName("Continuar");
	this.getGui().getAnswerTextArea().setName("Continuar");

    }

    /**
     * Realiza las acciones para comenzar o continuar el test. Si el test ha
     * terminado muestra las estadísticas.
     */
    private void actionTestStartContinue() {

	if (!this.getGui().getAplicacion().getTest().doTest()) {

	    this.getGui().showTest("Test finalizado", "Jugar de nuevo", "0"); //$NON-NLS-2$ //$NON-NLS-3$
	    this.timeLeft = -1;
	    this.getGui().getAplicacion().updatePlayerStats();

	    String testScore = "Preguntas jugadas: "
		    + this.getGui().getAplicacion().getTest()
			    .getPlayedQuestions();
	    testScore += "\nPreguntas acertadas: "
		    + this.getGui().getAplicacion().getTest()
			    .getCorrectQuestions();
	    testScore += "\nPreguntas falladas: "
		    + this.getGui().getAplicacion().getTest()
			    .getFailedQuestions();
	    testScore += "\nTiempo jugado: "
		    + this.getGui().getAplicacion().getTest()
			    .getTotalTimePlayed() + " segundos";
	    testScore += "\nMejor pregunta: "
		    + this.getGui().getAplicacion().getTest().getBestScore();
	    testScore += "\nPuntuaci\u00f3n partida: "
		    + this.getGui().getAplicacion().getTest().getTotalScore();

	    this.getGui().showInfoMsg(testScore,
		    "Puntuaci\u00f3n de la partida");

	} else {

	    this.timeLeft = Test.diffToTime(this.getGui().getAplicacion()
		    .getTest().getDifficulty());
	    this.getGui().showTest(
		    this.getGui().getAplicacion().getTest().showQuestion()/*
									   * ShowFormatedQuestion
									   * (
									   * 55)
									   */,
		    "Responder", Integer.toString(this.timeLeft));
	    this.getGui().getAnswerTextArea().setText("");
	}
    }

    /**
     * Realiza las acciones correspondientes resultado de la accion del
     * temporizador. Actualiza el label con el tiempo, cambia su color y actua
     * si se ha acabado el tiempo
     * 
     * @param timer
     *            - Temporizador que realiza la acciun.
     */
    private void actionTestTimer(Timer timer) {

	if (this.timeLeft > 0) {

	    if (this.getGui().getTimeLabel().getForeground() != Color.RED) {

		if (this.getGui().getAplicacion().getTest()
			.isTimeCritic(this.timeLeft - 1))
		    this.getGui().getTimeLabel().setForeground(Color.RED);

	    }

	    this.getGui().getTimeLabel()
		    .setText(Integer.toString(--this.timeLeft));

	} else {
	    if (this.timeLeft == 0) {

		timer.stop();

		this.getGui().getTimeLabel()
			.setText(Integer.toString(this.timeLeft));

		String message = "Se acab\u00f3 el tiempo!\n\nRespuesta correcta: "
			+ this.getGui().getAplicacion().getTest().showAnswer();

		this.getGui().showPopUp(message, "Timeout",
			JOptionPane.WARNING_MESSAGE);

		this.getGui()
			.getAnswerTextArea()
			.setText(
				this.getGui().getAplicacion().getTest()
					.showAnswer());

		this.getGui().getAplicacion().getTest().timeOut();

		this.getGui().getCheckButton().setText("Continuar");
		this.getGui().getCheckButton().setName("Continuar");
		this.getGui().getAnswerTextArea().setName("Continuar");
	    }
	}

    }

    /**
     * @param e
     *            Evento que ha desencandenado una accion en el controlador.
     * @return String s
     * @throws ClassCastException
     *             Excepcion al intentar establecer la clase origen del evento.
     */
    public String getButtonMainFrameName(ActionEvent e)
	    throws ClassCastException {
	Component sourceComponent;
	try {
	    sourceComponent = (Component) e.getSource();
	} catch (ClassCastException e2) {
	    throw e2;
	}
	Container sourceRootParent = sourceComponent.getParent();
	while (sourceRootParent.getParent() != null) {
	    sourceRootParent = sourceRootParent.getParent();
	}
	JFrame frame = (JFrame) sourceRootParent;
	// System.out.println(frame.getTitle() + "->" + x.getName()); //Debug
	return frame.getTitle();
    }

    /**
     * Gui cotrolada
     * 
     * @return the gui
     */
    public Gui getGui() {
	return gui;
    }

    /**
     * Lanza la ventana de menú correspondiente al usuario logueado en la
     * aplicacion.
     */
    private void gotoMenu() {
	if (this.getGui().getAplicacion().getUser() == null)
	    this.getGui().showLogin();
	else if (this.getGui().getAplicacion().getUser().isEditor())
	    this.getGui().editorMenu();
	else
	    this.getGui().playerMenu();
    }

    /**
     * Modifica la gui cotrolada
     * 
     * @param gui
     *            the gui to set
     */
    public void setGui(Gui gui) {
	this.gui = gui;
    }
}