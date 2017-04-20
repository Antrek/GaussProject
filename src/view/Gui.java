package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.App;
import model.Player;
import model.Question;
import model.Test;
import model.User;
import controller.Controller;

/**
 * Clase encargada de mostrar la interfaz grafica. Parte principal de la vista.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class Gui {

    /**
     * Controlador de enventos de la aplicacion.
     */
    private Controller action;

    /**
     * Panel principal de la aplicaicon
     */
    private JPanel actual;
    /**
     * Campo de texto respuesta
     */
    private JTextField answerTextArea;

    /**
     * Aplicacion representada graficamente.
     */
    private App aplicacion;
    /**
     * Grupo de botones facil/medio/dificil.
     */
    private final ButtonGroup buttonGroup = new ButtonGroup();

    /**
     * 
     */
    private JButton checkButton;
    /**
     * Frame principal.
     */
    private JFrame frame;

    /**
     * Label de identificador de pregunta
     */
    private JLabel lblvalueId;
    // TODO
    private JMenuItem mntmLogout;
    // TODO
    private JMenuItem mntmRanking;
    /**
     * TODO
     */
    private User modUser;
    /**
     * Campos de contraseña del cambio de contraseña nueva contraseña 1
     */
    private JPasswordField newPwd1;
    /**
     * Campos de contraseña del cambio de contraseña nueva contraseña 2
     */
    private JPasswordField newPwd2;
    /**
     * Campos de contraseña del cambio de contraseña contraseña actual
     */
    private JPasswordField oldPwd;
    /**
     * Campos de contraseña del registro 2
     */
    private JPasswordField passwordField2Registro;
    /**
     * Campos de contraseña del login
     */
    private JPasswordField passwordFieldLogIn;

    /**
     * Campos de contraseña del registro 1
     */
    private JPasswordField passwordFieldRegistro;
    /**
     * Campo de texto pregunta.
     */
    private JTextField questionTextArea;
    /**
     * Boton de dificultad dificil en ver/editar o crear pregunta
     */
    private JRadioButton rdbtnDificil;
    /**
     * Boton de dificultad facil en ver/editar o crear pregunta
     */
    private JRadioButton rdbtnFcil;
    /**
     * Boton de dificultad medio en ver/editar o crear pregunta
     */
    private JRadioButton rdbtnMedio;
    /**
     * Lablel del contador de tiempo.
     */
    private JLabel timeLabel = new JLabel("0");
    /**
     * Campo de texto nombre de usuario del login,
     */
    private JTextField usernameLoginTextArea;
    /**
     * Campo de texyo nombre de usuario
     */
    private JTextField usernameTextArea;

    /**
     * Constructor de la interfaz grafica que inicializa a su vez la aplicacion.
     * 
     * @throws SQLException
     *             Error al intentar inicializar la conexion con la abse de
     *             datos.
     */
    public Gui() throws SQLException {

	// Inicio del controlador
	this.setAction(new Controller(this));

	// Inicio de la aplicaicon
	try {
	    this.setAplicacion(new App(this.getAction()));
	} catch (Exception e) {
	    System.err.println("Error al crear la aplicacion.");
	    e.printStackTrace();
	    System.exit(-1);
	}

	// Inicio del frame basico
	this.setFrame(new JFrame("Main"));
	this.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.actual = new JPanel();

	this.actual.setAutoscrolls(true);
	this.actual.setBorder(null);
	this.actual.setLayout(null);
	this.getFrame().setContentPane(this.actual);
	this.getFrame().setSize(450, 320);
	this.getFrame().setResizable(false);
	centerScreen(this.getFrame());

	this.getFrame().setVisible(true);

	JMenuBar menuBar = new JMenuBar();
	this.getFrame().setJMenuBar(menuBar);

	JMenu mnArchivo = new JMenu("Archivo");
	mnArchivo.setMnemonic(KeyEvent.VK_A);
	menuBar.add(mnArchivo);

	this.setMntmRanking(new JMenuItem("Ranking", KeyEvent.VK_R));
	this.getMntmRanking().addActionListener(this.getAction());
	this.getMntmRanking().setEnabled(false);
	mnArchivo.add(this.getMntmRanking());

	this.setMntmLogout(new JMenuItem("Logout", KeyEvent.VK_L));
	this.getMntmLogout().addActionListener(this.getAction());
	this.getMntmLogout().setEnabled(false);
	mnArchivo.add(this.getMntmLogout());

	JMenuItem mntmSalir = new JMenuItem("Salir de la aplicaci\u00f3n",
		KeyEvent.VK_S);
	mntmSalir.addActionListener(this.getAction());
	mnArchivo.add(mntmSalir);

	JMenu mnAyuda = new JMenu("Ayuda");
	mnAyuda.setMnemonic(KeyEvent.VK_Y);
	menuBar.add(mnAyuda);

	JMenuItem mntmManualDeUsuario = new JMenuItem("Manual de usuario",
		KeyEvent.VK_M);
	mntmManualDeUsuario.addActionListener(this.getAction());
	mnAyuda.add(mntmManualDeUsuario);

	JMenuItem mntmAcercaDe = new JMenuItem("Acerca de", KeyEvent.VK_A);
	mntmAcercaDe.addActionListener(this.getAction());
	mnAyuda.add(mntmAcercaDe);

	showLogin();

    }

    /**
     * Ventana gráfica para añadir/ver/editar preguntas.
     * 
     * @param question
     *            Pregunta. Si recibe null considera añadir pregunta. Si recibe
     *            una Question, considera ver/editar la pregunta.
     */
    public void addQuest(Question question) {

	dispose();

	this.getFrame().setTitle("A\u00F1adir pregunta");
	this.getFrame().setName("A\u00F1adir pregunta");

	JLabel lblPregunta = new JLabel("Pregunta:");
	lblPregunta.setBounds(89, 21, 71, 15);
	this.actual.add(lblPregunta);

	this.setQuestionTextArea(new JTextField());
	this.getQuestionTextArea().setToolTipText("Max. 150 caracteres");
	this.getQuestionTextArea().setBounds(89, 46, 271, 20);
	this.actual.add(this.getQuestionTextArea());
	this.getQuestionTextArea().setColumns(10);

	JLabel lblRespuesta = new JLabel("Respuesta:");
	lblRespuesta.setBounds(89, 77, 81, 15);
	this.actual.add(lblRespuesta);

	this.setAnswerTextArea(new JTextField());
	this.getAnswerTextArea().setToolTipText("Max. 45 caracteres");
	this.getAnswerTextArea().setBounds(89, 102, 271, 20);
	this.actual.add(this.getAnswerTextArea());
	this.getAnswerTextArea().setColumns(10);

	this.setRdbtnDificil(new JRadioButton("Dif\u00edcil"));
	this.getRdbtnDificil().setName("Dificil");
	this.buttonGroup.add(this.getRdbtnDificil());
	this.getRdbtnDificil().setToolTipText(
		"El tiempo para cada pregunta ser\u00e1 de 15 segundos.");
	this.getRdbtnDificil().setBounds(308, 129, 76, 23);
	this.actual.add(this.getRdbtnDificil());

	this.setRdbtnMedio(new JRadioButton("Medio"));
	this.getRdbtnMedio().setName("Medio");
	this.buttonGroup.add(this.getRdbtnMedio());
	this.getRdbtnMedio().setToolTipText(
		"El tiempo para cada pregunta ser\u00e1 de 30 segundos.");
	this.getRdbtnMedio().setBounds(230, 129, 76, 23);
	this.actual.add(getRdbtnMedio());

	setRdbtnFcil(new JRadioButton("F\u00E1cil"));
	getRdbtnFcil().setName("Facil");
	buttonGroup.add(getRdbtnFcil());
	getRdbtnFcil().setToolTipText(
		"El tiempo para cada pregunta ser\u00E1 de 60 segundos.");
	getRdbtnFcil().setBounds(152, 129, 76, 23);
	actual.add(getRdbtnFcil());

	JLabel lblDificultad = new JLabel("Dificultad:");
	lblDificultad.setBounds(89, 133, 72, 15);
	actual.add(lblDificultad);

	JButton btnAceptar = new JButton();
	btnAceptar.setText("A\u00f1adir");
	if (question == null) {
	    btnAceptar.setText("A\u00F1adir");
	    btnAceptar.setName("A\u00F1adir pregunta");
	} else {
	    btnAceptar.setText("Actualizar");
	    btnAceptar.setName("Actualizar pregunta");
	}
	btnAceptar.addActionListener(getAction());
	btnAceptar.setBounds(171, 177, 104, 23);
	actual.add(btnAceptar);

	JButton btnBack = new JButton("Volver");
	btnBack.setName("Volver");
	btnBack.addActionListener(getAction());
	btnBack.setBounds(12, 238, 89, 23);
	actual.add(btnBack);

	if (question != null) {
	    getFrame().setTitle("Editar pregunta");
	    getFrame().setName("Editar pregunta");

	    getQuestionTextArea().setText(question.getQuestion());
	    getAnswerTextArea().setText(question.getAnswer());

	    JLabel lblId = new JLabel("ID:");
	    lblId.setBounds(204, 11, 46, 14);
	    actual.add(lblId);

	    setLblvalueId(new JLabel(String.valueOf(question.getId())));
	    getLblvalueId().setBounds(221, 11, 76, 14);
	    actual.add(getLblvalueId());

	    switch (question.getDifficulty()) {
	    case Question.DIFFICULTY_EASY:
		getRdbtnFcil().setSelected(true);
		break;
	    case Question.DIFFICULTY_MEDIUM:
		getRdbtnMedio().setSelected(true);
		break;
	    case Question.DIFFICULTY_HARD:
		getRdbtnDificil().setSelected(true);
		break;
	    }
	}

	actual.updateUI();
    }

    /**
     * Centra las ventanas
     * 
     * @param _frame
     *            Jframe a centrar
     */
    public void centerScreen(JFrame _frame) {
	Dimension dim = _frame.getToolkit().getScreenSize();
	Rectangle abounds = _frame.getBounds();
	_frame.setLocation((dim.width - abounds.width) / 2,
		(dim.height - abounds.height) / 2);
	// super.setVisible(true);
	_frame.requestFocus();
    }

    /**
     * Libera los componentes del frame principal y redibuja el panel.
     */
    public void dispose() {
	actual.removeAll();
	actual.updateUI();
    }

    /**
     * Ventana gráfica del menú de editor
     */
    public void editorMenu() {

	dispose();

	getFrame().setTitle("Menu-Editor");
	getFrame().setName("Menu-Editor");


	getMntmRanking().setEnabled(true);
	getMntmLogout().setEnabled(true);

	JButton addUser = new JButton("Preguntas");
	addUser.setName("Mostrar preguntas");

	addUser.addActionListener(getAction());
	addUser.setBounds(166, 189, 108, 25);
	actual.add(addUser);

	JButton deleteUser = new JButton("Pregunta");
	deleteUser.setName("Borrar pregunta");
	deleteUser.addActionListener(getAction());

	deleteUser.setBounds(166, 92, 108, 25);
	actual.add(deleteUser);

	JButton addEditor = new JButton("Editor");
	addEditor.setName("A\u00F1adir Editor");
	addEditor.addActionListener(getAction());
	addEditor.setBounds(44, 58, 108, 25);
	actual.add(addEditor);

	JButton delEditor = new JButton("Usuarios");
	delEditor.setName("Mostrar cuentas");
	delEditor.addActionListener(getAction());
	delEditor.setBounds(166, 155, 108, 25);
	actual.add(delEditor);

	JButton addQuest = new JButton("Usuario");
	addQuest.setName("Buscar usuario");
	addQuest.addActionListener(getAction());
	addQuest.setBounds(44, 155, 108, 25);
	actual.add(addQuest);

	JButton delQuest = new JButton("Usuario");
	delQuest.setName("Borrar cuenta");
	delQuest.addActionListener(getAction());
	delQuest.setBounds(166, 58, 108, 25);
	actual.add(delQuest);

	JButton chngUsrPss = new JButton("Mi perfil");
	chngUsrPss.setName("Perfil");
	chngUsrPss.addActionListener(getAction());
	chngUsrPss.setToolTipText("Cambiar contrase\u00F1a");
	chngUsrPss.setBounds(288, 58, 108, 25);
	actual.add(chngUsrPss);

	JButton lFQuest = new JButton("Pregunta");
	lFQuest.setName("Buscar pregunta");
	lFQuest.addActionListener(getAction());
	lFQuest.setBounds(44, 189, 108, 25);
	actual.add(lFQuest);

	JButton lFUser = new JButton("Pregunta");
	lFUser.setName("A\u00F1adir pregunta");
	lFUser.addActionListener(getAction());
	lFUser.setBounds(44, 92, 108, 25);
	actual.add(lFUser);

	JButton btnLogOut = new JButton("Logout");
	btnLogOut.setName("Logout");
	btnLogOut.addActionListener(getAction());
	btnLogOut.setBounds(288, 92, 108, 25);
	actual.add(btnLogOut);

	JLabel lblCuentas = new JLabel("A\u00F1adir");
	lblCuentas.setHorizontalAlignment(SwingConstants.CENTER);
	lblCuentas.setBounds(44, 33, 108, 15);
	actual.add(lblCuentas);

	JLabel lblPreguntas = new JLabel("Ver/Editar");
	lblPreguntas.setHorizontalAlignment(SwingConstants.CENTER);
	lblPreguntas.setBounds(44, 129, 108, 15);
	actual.add(lblPreguntas);

	JLabel lblNewLabel = new JLabel("Borrar");
	lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	lblNewLabel.setBounds(166, 33, 108, 15);
	actual.add(lblNewLabel);

	JLabel lblNewLabel_1 = new JLabel("Mostrar");
	lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
	lblNewLabel_1.setBounds(166, 129, 108, 15);
	actual.add(lblNewLabel_1);

	JLabel lblMisAcciones = new JLabel("Otros");
	lblMisAcciones.setHorizontalAlignment(SwingConstants.CENTER);
	lblMisAcciones.setBounds(288, 33, 108, 15);
	actual.add(lblMisAcciones);
	actual.updateUI();
    }

    /**
     * @return the action
     */
    public Controller getAction() {
	return action;
    }

    /**
     * @return the answerTextArea
     */
    public JTextField getAnswerTextArea() {
	return answerTextArea;
    }

    /**
     * @return the aplicacion
     */
    public App getAplicacion() {
	return aplicacion;
    }

    /**
     * @return the checkButton
     */
    public JButton getCheckButton() {
	return checkButton;
    }

    /**
     * @return the frame
     */
    public JFrame getFrame() {
	return frame;
    }

    /**
     * @return the lblvalueId
     */
    public JLabel getLblvalueId() {
	return lblvalueId;
    }

    /**
     * @return the mntmLogout
     */
    JMenuItem getMntmLogout() {
	return mntmLogout;
    }

    /**
     * @return the mntmRanking
     */
    JMenuItem getMntmRanking() {
	return mntmRanking;
    }

    /**
     * @return the modUser
     */
    public User getModUser() {
	return modUser;
    }

    /**
     * @return the newPwd1
     */
    public JPasswordField getNewPwd1() {
	return newPwd1;
    }

    /**
     * @return the newPwd2
     */
    public JPasswordField getNewPwd2() {
	return newPwd2;
    }

    /**
     * @return the oldPwd
     */
    public JPasswordField getOldPwd() {
	return oldPwd;
    }

    /**
     * @return the passwordField2Registro
     */
    public JPasswordField getPasswordField2Registro() {
	return passwordField2Registro;
    }

    /**
     * @return the passwordFieldLogIn
     */
    public JPasswordField getPasswordFieldLogIn() {
	return passwordFieldLogIn;
    }

    /**
     * @return the passwordFieldRegistro
     */
    public JPasswordField getPasswordFieldRegistro() {
	return passwordFieldRegistro;
    }

    /**
     * @return the questionTextArea
     */
    public JTextField getQuestionTextArea() {
	return questionTextArea;
    }

    /**
     * @return the rdbtnDificil
     */
    public JRadioButton getRdbtnDificil() {
	return rdbtnDificil;
    }

    /**
     * @return the rdbtnFcil
     */
    public JRadioButton getRdbtnFcil() {
	return rdbtnFcil;
    }

    /**
     * @return the rdbtnMedio
     */
    public JRadioButton getRdbtnMedio() {
	return rdbtnMedio;
    }

    /**
     * @return the timeLabel
     */
    public JLabel getTimeLabel() {
	return timeLabel;
    }

    /**
     * @return the usernameLoginTextArea
     */
    public JTextField getUsernameLoginTextArea() {
	return usernameLoginTextArea;
    }

    /**
     * @return the usernameTextArea
     */
    public JTextField getUsernameTextArea() {
	return usernameTextArea;
    }

    /**
     * Ventana gráfica para la modificación de usuario
     * 
     * @param user
     *            Usuario que se quiere ver/editar.
     */

    public void modifyUser(User user) {

	this.setModUser(user);
	dispose();

	JLabel lblUsuario = new JLabel("Usuario:");
	lblUsuario.setHorizontalAlignment(SwingConstants.LEFT);
	lblUsuario.setBounds(20, 42, 60, 15);
	actual.add(lblUsuario);

	JLabel lblNewLabel = new JLabel(user.getUser());
	lblNewLabel.setBounds(88, 42, 97, 14);
	actual.add(lblNewLabel);

	if (!user.isEditor()) {

	    String stats[] = ((Player) user).getStats().getAllStats();

	    JLabel lblPartidasJugadas = new JLabel("Preguntas respondidas:");
	    lblPartidasJugadas.setHorizontalAlignment(SwingConstants.LEFT);
	    lblPartidasJugadas.setBounds(20, 68, 172, 14);
	    actual.add(lblPartidasJugadas);

	    JLabel lblNewLabel_1 = new JLabel(stats[0]);
	    lblNewLabel_1.setBounds(234, 68, 77, 14);
	    actual.add(lblNewLabel_1);

	    JLabel lblPreguntasAcertadas = new JLabel("Preguntas acertadas:");
	    lblPreguntasAcertadas.setHorizontalAlignment(SwingConstants.LEFT);
	    lblPreguntasAcertadas.setBounds(20, 93, 165, 15);
	    actual.add(lblPreguntasAcertadas);

	    JLabel lblNewLabel_2 = new JLabel(stats[1]);
	    lblNewLabel_2.setBounds(234, 95, 77, 14);
	    actual.add(lblNewLabel_2);

	    JLabel lblPreguntasFalladas = new JLabel("Preguntas falladas:");
	    lblPreguntasFalladas.setHorizontalAlignment(SwingConstants.LEFT);
	    lblPreguntasFalladas.setBounds(20, 118, 165, 15);
	    actual.add(lblPreguntasFalladas);

	    JLabel lblNewLabel_3 = new JLabel(stats[2]);
	    lblNewLabel_3.setBounds(234, 120, 77, 14);
	    actual.add(lblNewLabel_3);

	    JLabel lblPorcentajeDeAcierto = new JLabel("Porcentaje de acierto:");
	    lblPorcentajeDeAcierto.setHorizontalAlignment(SwingConstants.LEFT);
	    lblPorcentajeDeAcierto.setBounds(20, 143, 165, 15);
	    actual.add(lblPorcentajeDeAcierto);

	    JLabel lblNewLabel_4 = new JLabel(stats[3]);
	    lblNewLabel_4.setBounds(234, 144, 77, 14);
	    actual.add(lblNewLabel_4);

	    JLabel lblTiempoMedioDe = new JLabel("Tiempo medio de respuesta:");
	    lblTiempoMedioDe.setHorizontalAlignment(SwingConstants.LEFT);
	    lblTiempoMedioDe.setBounds(20, 168, 202, 14);
	    actual.add(lblTiempoMedioDe);

	    JLabel label = new JLabel(stats[4]);
	    label.setBounds(234, 169, 77, 14);
	    label.setToolTipText("Segundos");
	    actual.add(label);

	    JLabel lblTiempoTotal = new JLabel("Tiempo total jugado:");
	    lblTiempoTotal.setHorizontalAlignment(SwingConstants.LEFT);
	    lblTiempoTotal.setBounds(20, 193, 165, 15);
	    actual.add(lblTiempoTotal);

	    JLabel label1 = new JLabel(stats[5]);
	    label1.setBounds(234, 195, 77, 14);
	    label1.setToolTipText("Segundos");
	    actual.add(label1);

	    JLabel lblMejorPuntuacion = new JLabel("Mejor puntuaci\u00f3n:");
	    lblMejorPuntuacion.setHorizontalAlignment(SwingConstants.LEFT);
	    lblMejorPuntuacion.setBounds(20, 218, 165, 15);
	    actual.add(lblMejorPuntuacion);
	    JLabel lblEstadsitcasDeUsuario = new JLabel(
		    "Estad\u00edsticas de Usuario");
	    lblEstadsitcasDeUsuario.setBounds(134, 11, 167, 15);
	    actual.add(lblEstadsitcasDeUsuario);

	    JLabel lblNewLabel_5 = new JLabel(stats[6]);
	    lblNewLabel_5.setBounds(234, 220, 77, 14);
	    actual.add(lblNewLabel_5);

	    JLabel lblPuntuacinTotal = new JLabel("Puntuaci\u00F3n total:");
	    lblPuntuacinTotal.setHorizontalAlignment(SwingConstants.LEFT);
	    lblPuntuacinTotal.setBounds(20, 243, 165, 15);
	    actual.add(lblPuntuacinTotal);

	    JLabel label_2 = new JLabel(stats[7]);
	    label_2.setBounds(234, 244, 77, 14);
	    actual.add(label_2);

	    getFrame().setTitle("Perfil");
	    getFrame().setName("Perfil");
	} else {
	    getFrame().setTitle("Editar usuario");
	    getFrame().setName("Editar usuario");
	}

	JButton btnModificarUsuario = new JButton("Cambiar contrase\u00F1a");
	btnModificarUsuario.addActionListener(getAction());
	btnModificarUsuario.setName("Cambiar_pass");
	btnModificarUsuario.setBounds(257, 37, 177, 25);
	actual.add(btnModificarUsuario);

	JButton btnNewButton = new JButton("Atr\u00E1s");
	btnNewButton.addActionListener(getAction());
	btnNewButton.setName("atras");
	btnNewButton.setBounds(362, 237, 72, 25);
	actual.add(btnNewButton);

    }

    /**
     * Ventana gráfica del menú de usuario
     */
    public void playerMenu() {

	dispose();

	getFrame().setTitle("Menu-Jugador");
	getFrame().setName("Menu-Jugador");


	getMntmRanking().setEnabled(true);
	getMntmLogout().setEnabled(true);

	JButton btnViewStatics = new JButton("Mi perfil");
	btnViewStatics.setName("Mi perfil");
	btnViewStatics.addActionListener(getAction());
	btnViewStatics.setBounds(158, 148, 133, 23);
	actual.add(btnViewStatics);

	JButton btnPlay = new JButton("Jugar");
	btnPlay.setName("Jugar");
	btnPlay.addActionListener(getAction());
	btnPlay.setBounds(158, 88, 133, 23);
	actual.add(btnPlay);

	JButton btnLogOut = new JButton("Logout");
	btnLogOut.setName("Logout");
	btnLogOut.addActionListener(getAction());
	btnLogOut.setBounds(158, 208, 133, 23);
	actual.add(btnLogOut);

	JLabel lblTellMeWhat = new JLabel("Selecciona una opci\u00F3n:");
	lblTellMeWhat.setHorizontalAlignment(SwingConstants.CENTER);
	lblTellMeWhat.setBounds(139, 37, 171, 14);
	actual.add(lblTellMeWhat);

	actual.updateUI();
    }

    /**
     * Ventana gráfica para la selección de dificultad
     * 
     * @return entero con la dificultad seleccionada (Fácil, medio o difícil)
     */
    public int selectDifficult()

    {

	Object[] options = { "F\u00e1cil", "Medio", "Dif\u00edcil" };

	int n = JOptionPane.showOptionDialog(null,
		"Elija el nivel de dificultad", "Seleccionar Dificultad",
		JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
		null, options, options[2]);
	return n;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(Controller action) {
	this.action = action;
    }

    /**
     * @param answerTextArea
     *            the answerTextArea to set
     */
    public void setAnswerTextArea(JTextField answerTextArea) {
	this.answerTextArea = answerTextArea;
    }

    /**
     * @param aplicacion
     *            the aplicacion to set
     */
    public void setAplicacion(App aplicacion) {
	this.aplicacion = aplicacion;
    }

    /**
     * @param checkButton
     *            the checkButton to set
     */
    public void setCheckButton(JButton checkButton) {
	this.checkButton = checkButton;
    }

    /**
     * @param frame
     *            the frame to set
     */
    public void setFrame(JFrame frame) {
	this.frame = frame;
    }

    /**
     * @param lblvalueId
     *            the lblvalueId to set
     */
    public void setLblvalueId(JLabel lblvalueId) {
	this.lblvalueId = lblvalueId;
    }

    /**
     * @param mntmLogout
     *            the mntmLogout to set
     */
    void setMntmLogout(JMenuItem mntmLogout) {
	this.mntmLogout = mntmLogout;
    }

    /**
     * @param mntmRanking
     *            the mntmRanking to set
     */
    void setMntmRanking(JMenuItem mntmRanking) {
	this.mntmRanking = mntmRanking;
    }

    /**
     * @param modUser
     *            the modUser to set
     */
    public void setModUser(User modUser) {
	this.modUser = modUser;
    }

    /**
     * @param newPwd1
     *            the newPwd1 to set
     */
    public void setNewPwd1(JPasswordField newPwd1) {
	this.newPwd1 = newPwd1;
    }

    /**
     * @param newPwd2
     *            the newPwd2 to set
     */
    public void setNewPwd2(JPasswordField newPwd2) {
	this.newPwd2 = newPwd2;
    }

    /**
     * @param oldPwd
     *            the oldPwd to set
     */
    public void setOldPwd(JPasswordField oldPwd) {
	this.oldPwd = oldPwd;
    }

    /**
     * @param passwordField2Registro
     *            the passwordField2Registro to set
     */
    public void setPasswordField2Registro(JPasswordField passwordField2Registro) {
	this.passwordField2Registro = passwordField2Registro;
    }

    /**
     * @param passwordFieldLogIn
     *            the passwordFieldLogIn to set
     */
    public void setPasswordFieldLogIn(JPasswordField passwordFieldLogIn) {
	this.passwordFieldLogIn = passwordFieldLogIn;
    }

    /**
     * @param passwordFieldRegistro
     *            the passwordFieldRegistro to set
     */
    public void setPasswordFieldRegistro(JPasswordField passwordFieldRegistro) {
	this.passwordFieldRegistro = passwordFieldRegistro;
    }

    /**
     * @param questionTextArea
     *            the questionTextArea to set
     */
    public void setQuestionTextArea(JTextField questionTextArea) {
	this.questionTextArea = questionTextArea;
    }

    /**
     * @param rdbtnDificil
     *            the rdbtnDificil to set
     */
    public void setRdbtnDificil(JRadioButton rdbtnDificil) {
	this.rdbtnDificil = rdbtnDificil;
    }

    /**
     * @param rdbtnFcil
     *            the rdbtnFcil to set
     */
    public void setRdbtnFcil(JRadioButton rdbtnFcil) {
	this.rdbtnFcil = rdbtnFcil;
    }

    /**
     * @param rdbtnMedio
     *            the rdbtnMedio to set
     */
    public void setRdbtnMedio(JRadioButton rdbtnMedio) {
	this.rdbtnMedio = rdbtnMedio;
    }

    /**
     * @param timeLabel
     *            the timeLabel to set
     */
    public void setTimeLabel(JLabel timeLabel) {
	this.timeLabel = timeLabel;
    }

    /**
     * @param usernameLoginTextArea
     *            the usernameLoginTextArea to set
     */
    public void setUsernameLoginTextArea(JTextField usernameLoginTextArea) {
	this.usernameLoginTextArea = usernameLoginTextArea;
    }

    /**
     * @param usernameTextArea
     *            the usernameTextArea to set
     */
    public void setUsernameTextArea(JTextField usernameTextArea) {
	this.usernameTextArea = usernameTextArea;
    }

    /**
     * Muestra la ventana para cambiar la contraseña
     */
    public void showChangePwd() {
	dispose();

	getFrame().setTitle("Cambiar contrase\u00F1a");
	getFrame().setName("Cambiar contrase\u00F1a");

	JLabel lblNewPwd1 = new JLabel("Introduzca la nueva contrase\u00F1a");
	lblNewPwd1.setHorizontalAlignment(SwingConstants.CENTER);

	setNewPwd1(new JPasswordField());
	getNewPwd1().setName("newPwd1");

	JLabel lblNewPwd2 = new JLabel("Repita la nueva contrase\u00F1a");
	lblNewPwd2.setHorizontalAlignment(SwingConstants.CENTER);

	setNewPwd2(new JPasswordField());
	getNewPwd2().setName("newPwd2");

	if (getModUser() == this.getAplicacion().getUser()) {

	    JLabel lblOldPwd = new JLabel(
		    "Introduzca su contrase\u00F1a actual");
	    lblOldPwd.setHorizontalAlignment(SwingConstants.CENTER);
	    lblOldPwd.setBounds(110, 24, 230, 15);
	    actual.add(lblOldPwd);

	    setOldPwd(new JPasswordField());
	    getOldPwd().setName("oldPwd");
	    getOldPwd().setBounds(182, 49, 86, 20);
	    actual.add(getOldPwd());
	    getOldPwd().setColumns(10);

	    lblNewPwd1.setBounds(110, 80, 229, 15);
	    actual.add(lblNewPwd1);

	    getNewPwd1().setBounds(182, 105, 86, 20);
	    actual.add(getNewPwd1());

	    lblNewPwd2.setBounds(127, 136, 195, 15);
	    actual.add(lblNewPwd2);

	    getNewPwd2().setBounds(182, 161, 86, 20);
	    getNewPwd2().addActionListener(getAction());
	    actual.add(getNewPwd2());

	} else {

	    lblNewPwd1.setBounds(110, 24, 229, 15);
	    actual.add(lblNewPwd1);

	    getNewPwd1().setBounds(182, 49, 86, 20);
	    actual.add(getNewPwd1());

	    lblNewPwd2.setBounds(127, 80, 195, 15);
	    actual.add(lblNewPwd2);

	    getNewPwd2().setBounds(182, 105, 86, 20);
	    getNewPwd2().addActionListener(getAction());
	    actual.add(getNewPwd2());

	}

	JButton btnChangePwd = new JButton("Cambiar");
	btnChangePwd.setName("Cambiar_pass");

	btnChangePwd.addActionListener(getAction());
	btnChangePwd.setBounds(161, 206, 128, 25);
	actual.add(btnChangePwd);

	JButton btnBack = new JButton("Atras");
	btnBack.setName("Atras");
	btnBack.addActionListener(getAction());
	btnBack.setBounds(12, 238, 89, 23);
	actual.add(btnBack);
	actual.updateUI();
    }

    /**
     * Muestra el perfil del usuario editor.
     */

    private void showEditorProfile() {

	setModUser(getAplicacion().getUser());
	showChangePwd();
    }

    /**
     * Muestra un mensaje de error en una ventana popup haciendo uso de
     * showPopUp
     * 
     * @param msg
     *            mensaje del error
     */
    public void showErrMsg(String msg) {

	showPopUp(msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de información en una ventana popup haciendo uso de
     * showPopUp
     * 
     * @param msg
     *            mensaje de información
     * @param title
     *            Titulo de la ventana
     */
    public void showInfoMsg(String msg, String title) {

	showPopUp(msg, title, JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Crea una ventana generica para introducir informacion.
     * 
     * @param msg
     *            Mensaje que se quiere mostrar
     * @return Respuesta introducida por el usuario
     * 
     */
    public String showInputMsg(String msg) {
	return JOptionPane.showInputDialog(msg);
    }

    /**
     * @param texto
     *            Texto que se quiere mostrar.
     */
    public void showList(String texto) {
	if (texto == null) {
	    JOptionPane.showMessageDialog(actual,
		    "No hay informacion que mostrar.");
	    return;
	}
	JFrame frameList = new JFrame("Lista");
	frameList.setTitle("Mostrar");
	frameList.setName("Mostrar");
	frameList.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	JTextPane textArea = new JTextPane();
	frameList.setSize(400, 300);
	textArea.setEditable(false);
	textArea.setAutoscrolls(true);
	textArea.setBounds(0, 0, 600, 600);
	textArea.setText(texto);
	JScrollPane sbrText = new JScrollPane(textArea);
	sbrText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	frameList.getContentPane().add(sbrText);
	centerScreen(frameList);
	frameList.setVisible(true);
    }

    /***
     * Ventana gráfica de ingreso a la aplicación (LogIn)
     */
    public void showLogin() {

	dispose();

	getFrame().setTitle("Login");


	getMntmRanking().setEnabled(false);
	getMntmLogout().setEnabled(false);

	JLabel lblIntroduzcaSuUsuario = new JLabel(
		"Introduzca su Usuario y contrase\u00F1a para acceder");
	lblIntroduzcaSuUsuario.setHorizontalAlignment(SwingConstants.CENTER);
	lblIntroduzcaSuUsuario.setFocusable(false);
	lblIntroduzcaSuUsuario.setBorder(null);
	lblIntroduzcaSuUsuario.setAutoscrolls(true);
	lblIntroduzcaSuUsuario.setBounds(50, 11, 350, 15);
	actual.add(lblIntroduzcaSuUsuario);

	JLabel lblaNoTienes = new JLabel("\u00bfA\u00fan no tienes una cuenta?");
	lblaNoTienes.setHorizontalAlignment(SwingConstants.CENTER);
	lblaNoTienes.setBounds(127, 198, 196, 15);
	actual.add(lblaNoTienes);

	JButton LogIn = new JButton("Login");
	LogIn.setName("Login");
	LogIn.addActionListener(getAction());
	LogIn.setBounds(175, 163, 100, 24);
	actual.add(LogIn);

	JButton button = new JButton("Registrate");
	button.setName("Registrate");
	lblaNoTienes.setLabelFor(button);
	button.addActionListener(getAction());
	button.setBounds(171, 225, 109, 25);
	actual.add(button);

	setUsernameLoginTextArea(new JTextField());
	getUsernameLoginTextArea().setName("usernameTextfield");
	getUsernameLoginTextArea().setBounds(182, 65, 86, 20);
	actual.add(getUsernameLoginTextArea());
	getUsernameLoginTextArea().setColumns(10);

	setPasswordFieldLogIn(new JPasswordField());
	getPasswordFieldLogIn().setName("passwordTextfield");
	getPasswordFieldLogIn().setBounds(182, 124, 86, 20);
	getPasswordFieldLogIn().addActionListener(getAction());
	actual.add(getPasswordFieldLogIn());

	JLabel lblContrasea = new JLabel("Contrase\u00F1a");
	lblContrasea.setHorizontalAlignment(SwingConstants.CENTER);
	lblContrasea.setLabelFor(getPasswordFieldLogIn());
	lblContrasea.setBounds(182, 97, 86, 15);
	actual.add(lblContrasea);

	JLabel lblUsuario = new JLabel("Usuario");
	lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);
	lblUsuario.setLabelFor(getUsernameLoginTextArea());
	lblUsuario.setIconTextGap(1);
	lblUsuario.setRequestFocusEnabled(false);
	lblUsuario.setBounds(182, 38, 86, 15);
	actual.add(lblUsuario);

	actual.updateUI();
    }

    /**
     * Muestra el perfil del usuario jugador.
     */
    private void showPlayerProfile(Player player) {
	modifyUser(player);

    }

    /**
     * Muestra un popup generico.
     * 
     * @param msg
     *            mensaje del popup
     * @param title
     *            titulo de la ventana
     * @param message_code
     *            código del mensaje
     */
    public void showPopUp(String msg, String title, int message_code) {

	JOptionPane
		.showMessageDialog(this.getFrame(), msg, title, message_code);

    }

    /**
     * Muestra el perfil del usuario (genérico).
     */

    public void showProfile() {

	User user = this.getAplicacion().getUser();

	if (user.isEditor()) {
	    this.showEditorProfile();
	}

	else {
	    this.showPlayerProfile((Player) user);
	}

    }

    /**
     * Ventana gráfica para la creación de un nuevo usuario
     * 
     * @param editor
     *            : booleano que indica si el usuario a registrar es
     *            editor(true) o jugador (false)
     */
    public void showRegister(boolean editor) {

	dispose();

	if (editor)
	    getFrame().setTitle("Nuevo editor");
	else
	    getFrame().setTitle("Registro");

	getFrame().setName("Registro");


	JLabel lblUsuario = new JLabel("Usuario");
	lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);
	lblUsuario.setBounds(198, 24, 55, 15);
	actual.add(lblUsuario);

	setUsernameTextArea(new JTextField());
	getUsernameTextArea().setName("usernameField");
	getUsernameTextArea().setBounds(182, 49, 86, 20);
	actual.add(getUsernameTextArea());
	getUsernameTextArea().setColumns(10);

	JLabel lblContrasea = new JLabel("Contrase\u00F1a");
	lblContrasea.setHorizontalAlignment(SwingConstants.CENTER);
	lblContrasea.setBounds(184, 80, 83, 15);
	actual.add(lblContrasea);

	setPasswordFieldRegistro(new JPasswordField());
	getPasswordFieldRegistro().setName("passwordField1");
	getPasswordFieldRegistro().setBounds(182, 105, 86, 20);
	actual.add(getPasswordFieldRegistro());

	JLabel lblRepitaSuContrasea = new JLabel("Repita la contrase\u00F1a");
	lblRepitaSuContrasea.setHorizontalAlignment(SwingConstants.CENTER);
	lblRepitaSuContrasea.setBounds(151, 136, 148, 15);
	actual.add(lblRepitaSuContrasea);

	setPasswordField2Registro(new JPasswordField());
	getPasswordField2Registro().setName("passwordField2");
	getPasswordField2Registro().setBounds(182, 161, 86, 20);
	getPasswordField2Registro().addActionListener(getAction());
	actual.add(getPasswordField2Registro());

	JButton btnCrearCuenta = new JButton("Crear Cuenta");
	if (!editor)
	    btnCrearCuenta.setName("Crear_Cuenta");
	else
	    btnCrearCuenta.setName("Crear_Cuenta_editor");
	btnCrearCuenta.addActionListener(getAction());
	btnCrearCuenta.setBounds(161, 206, 128, 25);
	actual.add(btnCrearCuenta);

	JButton btnBack = new JButton("Atras");
	btnBack.setName("Atras");
	btnBack.addActionListener(getAction());
	btnBack.setBounds(12, 238, 89, 23);
	actual.add(btnBack);
	actual.updateUI();
    }

    /**
     * Crea o actualiza la ventana del text con la informacion.
     * 
     * @param qText
     *            Texto de la pregunta
     * @param bText
     *            Texto del boton de accion
     * @param time
     *            Tiempo
     */
    public void showTest(String qText, String bText, String time) {

	dispose();

	getFrame().setTitle("Test");
	getFrame().setName("Test");


	JTextPane question = new JTextPane();
	question.setOpaque(false);

	SimpleAttributeSet attribs = new SimpleAttributeSet();
	StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);

	question.setParagraphAttributes(attribs, true);

	question.setBounds(27, 84, 395, 94);
	question.setBackground(null);
	question.setEditable(false);
	question.setText(qText);
	actual.add(question);

	setAnswerTextArea(new JTextField());
	getAnswerTextArea().setName(bText);
	getAnswerTextArea().setHorizontalAlignment(SwingConstants.CENTER);
	getAnswerTextArea().setText("");
	getAnswerTextArea().setColumns(45);
	getAnswerTextArea().setBounds(27, 190, 395, 19);
	getAnswerTextArea().addActionListener(getAction());
	actual.add(getAnswerTextArea());

	JLabel tRestante = new JLabel("Tiempo restante:");
	tRestante.setBounds(12, 42, 128, 19);
	actual.add(tRestante);

	getTimeLabel().setText(time);
	getTimeLabel().setForeground(Color.BLACK);

	getTimeLabel().setBounds(152, 40, 22, 22);
	actual.add(getTimeLabel());

	setCheckButton(new JButton(bText));
	getCheckButton().setName(bText);
	getCheckButton().setBounds(155, 221, 140, 25);
	getCheckButton().addActionListener(getAction());
	actual.add(getCheckButton());

	JLabel lblPregunta = new JLabel("Pregunta ");
	lblPregunta.setBounds(174, 12, 70, 15);
	actual.add(lblPregunta);

	int qNum = 0;
	int totNum = Test.TOTAL_QUESTIONS;

	if (bText.compareTo("Comenzar") != 0) {
	    qNum = this.getAplicacion().getTest().currentQuestionNumber();

	    if (qNum > totNum)
		qNum = totNum;
	}

	String q = Integer.toString(qNum);
	q += "/" + Integer.toString(totNum);

	JLabel lblXy = new JLabel(q);
	lblXy.setBounds(256, 12, 70, 15);
	actual.add(lblXy);

	JButton goBack = new JButton("Atr\u00e1s");
	goBack.setName("Volver_al_menu");
	goBack.setBounds(12, 241, 72, 19);
	goBack.addActionListener(getAction());
	actual.add(goBack);

	actual.updateUI();

    }

    /**
     * Ventana para mostrar texto genérico
     * 
     * @param text
     *            texto a mostrar
     * @return String con el texto introducido
     */

    public String showTextFieldWindow(String text) {

	return JOptionPane.showInputDialog(text);
    }
}
