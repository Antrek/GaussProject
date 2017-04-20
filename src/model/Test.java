package model;

import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Clase modelo para los Test. Parte del modelo.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class Test {

    /**
     * Valor de referencia para el tiempo de la dificultad Facil.
     */
    public static final int TIME_EASY = 60;

    /**
     * Valor de referencia para el tiempo de la dificultad Dificil.
     */
    public static final int TIME_HARD = 15;

    /**
     * Valor de referencia para el tiempo de la dificultad Media.
     */
    public static final int TIME_MEDIUM = 30;

    /**
     * Numero de preguntas que compone a un test.
     */
    public static final int TOTAL_QUESTIONS = 5;

    /**
     * Convierte dificultad a tiempo por pregunta.
     * 
     * @param diff
     *            - Dificultad de la pregunta.
     * @return Tiempo total para responder la pregunta o -1 si el codigo de
     *         dificultad es incorrecto.
     */
    public static int diffToTime(int diff) {

	switch (diff) {

	case 0:
	    return TIME_EASY;

	case 1:
	    return TIME_MEDIUM;
	case 2:
	    return TIME_HARD;
	default:
	    return -1;
	}
    }

    /**
     * Maxima puntuacion de pregunta
     */
    private int bestScore;

    /**
     * Iteracion actual del test.
     */
    private int current;

    /**
     * Dificultad del test.
     */
    private int difficulty;

    /**
     * Numero de preguntas correctas.
     */
    private int qCorrect;

    /**
     * Array de preguntas a responder.
     */
    private Question questions[];

    /**
     * Flag de control para indicar si el test ha comenzado o no
     */
    private boolean started;

    /**
     * Temporizador para contar segundos
     */
    private Timer timer;

    /**
     * Puntuacion total.
     */
    private int totalScore;

    /**
     * Tiempo total transcurrido durante el test.
     */
    private int totalTime;

    /**
     * Construye un nuevo test a realizar. Inicializa los atributos, crea un
     * nuevo Timer y selecciona 5 preguntas al azar de las pasadas por
     * parametros para el nuevo test.
     * 
     * @param questions
     *            - Lista de preguntas de la cual el test seleccionara 5 al
     *            azar.
     * @param listener
     *            - ActionListener que utilizara° el timer.
     */
    public Test(Question[] questions, ActionListener listener) {

	this.difficulty = questions[0].getDifficulty();
	this.current = 0;
	this.qCorrect = 0;
	this.totalScore = 0;
	this.totalTime = 0;
	this.bestScore = 0;
	this.started = false;
	this.timer = new Timer(1000, listener);
	this.timer.setActionCommand("test");
	this.questions = new Question[TOTAL_QUESTIONS];
	randomQuestions(questions);

    }

    /**
     * Detiene el temporizador, comprueba la respuesta, suma el tiempo empleado
     * en contestar al tiempo total y calcula la puntuacion. Finalmente avanza a
     * la siguiente pregunta.
     * 
     * @param answer
     *            - Respuesta dada por el usuario.
     * @param timeLeft
     *            - Tiempo restante para responder la pregunta.
     * @return true si la respuesta es correcta y false si es incorrecta.
     */
    public boolean checkAnswer(String answer, int timeLeft) {

	this.timer.stop();

	boolean equals = false;

	if (answer.equalsIgnoreCase(showAnswer())) {

	    this.qCorrect++;
	    updateScore(this.difficulty, timeLeft);
	    equals = true;
	}

	this.totalTime += diffToTime(this.difficulty) - timeLeft;

	this.current++;

	return equals;
    }

    /**
     * Devuelve el nomero de pregunta de la iteracion actual del test.
     * 
     * @return Nomero de la pregunta entre 1 y 5.
     */
    public int currentQuestionNumber() {
	return this.current + 1;
    }

    /**
     * Metodo ejecutado en cada iteracion (pregunta) del test. Si el test no ha
     * comenzado, lo inicia y mientras queden preguntas por responder, el
     * temporizador comienza a contar.
     * 
     * @return true mientras no haya finalizado el test.
     */
    public boolean doTest() {

	if (!this.started)
	    this.started = true;

	boolean finished = (this.current >= TOTAL_QUESTIONS);

	if (!finished)
	    this.timer.start();
	else
	    this.started = false;

	return !finished;

    }

    /**
     * 
     * @return Mejor puntuacion en una pregunta.
     */
    public int getBestScore() {
	return this.bestScore;
    }

    /**
     * 
     * @return Preguntas acertadas.
     */
    public int getCorrectQuestions() {
	return this.qCorrect;
    }

    /**
     * 
     * @return Dificultad numerica del test.
     */
    public int getDifficulty() {
	return this.difficulty;
    }

    /**
     * 
     * @return Preguntas falladas.
     */
    public int getFailedQuestions() {
	return this.current - this.qCorrect;
    }

    /**
     * @return Preguntas jugadas.
     */
    public int getPlayedQuestions() {
	return this.current;
    }

    /**
     * @return Timer que cuenta los segundos en la partida.
     */
    public Timer getTimer() {
	return this.timer;
    }

    /**
     * 
     * @return Puntuacion total.
     */
    public int getTotalScore() {
	return this.totalScore;
    }

    /**
     * 
     * @return Tiempo total jugado.
     */
    public int getTotalTimePlayed() {
	return this.totalTime;
    }

    /**
     * Calcula en base al tiempo restante para responder la pregunta si el
     * tiempo es critico o no. Se considera critico si queda la mitad o menos
     * del tiempo total.
     * 
     * @param timeLeft
     *            - Tiempo restante
     * @return true si es cr√≠tico y false en caso contrario.
     */
    public boolean isTimeCritic(int timeLeft) {
	return timeLeft <= diffToTime(this.difficulty) / 2;
    }

    /**
     * Selecciona 5 preguntas aleatorias no repetidas de entre las pasadas por
     * parametros. No comprueba que hayan 5 preguntas o mas.
     * 
     * @param questions
     *            - Lista de preguntas.
     */
    private void randomQuestions(Question questions[]) {

	int i = 0;
	while (i < TOTAL_QUESTIONS) {

	    int randN = (int) (Math.random() * questions.length);

	    Question rand = questions[randN];

	    boolean idem = false;

	    // Comprueba que la pregunta no haya sido seleccionada
	    for (int j = 0; j < i; j++) {
		if (this.questions[j].equals(rand)) {
		    idem = true;
		    break;
		}
	    }

	    if (!idem) {

		this.questions[i] = rand;

		i++;
	    }

	}

    }

    /**
     * Actualiza la informacion de stats del usuario pasado por parametros con
     * las obtenidas en el test.
     * 
     * @param pj
     *            - Jugador de la partida al que actualizar las estadisticas.
     */
    public void saveStats(Player pj) {

	pj.getStats().addStats(this.current, this.qCorrect, this.totalTime,
		this.totalScore, this.totalScore);

    }

    /**
     * Actualiza la puntuacion maxima del test.
     * 
     * @param maxScore
     *            - Puntuaci√≥n maxima.
     */
    private void setBestScore(int maxScore) {
	this.bestScore = maxScore;
    }

    /**
     * Muestra la respuesta a la pregunta pendiente de responder.
     * 
     * @return Respuesta a la pregunta actual o null si hay un error.
     */
    public String showAnswer() {

	String answer = null;

	if (this.current >= 0 && this.current < 5)
	    answer = this.questions[this.current].getAnswer();

	return answer;
    }

    /**
     * Muestra la pregunta pendiente de responder.
     * 
     * @return Pregunta actual o null si hay un error.
     */
    public String showQuestion() {

	String question = null;

	if (this.current >= 0 && this.current < 5)
	    question = this.questions[this.current].getQuestion();

	return question;
    }

    /**
     * Detiene el test en curso.
     */
    public void stop() {
	this.timer.stop();
    }

    /**
     * Avisa al test que se ha acabado el tiempo para responder a la pregunta y
     * lo aÒade al contador del tiempo total. Avanza a la siguiente pregunta.
     */
    public void timeOut() {
	this.totalTime += diffToTime(this.difficulty);
	this.current++;
    }

    /**
     * Actualiza el puntaje obtenido con una pregunta acertada de dificultad
     * difficulty y con tiempo restante timLeft.
     * 
     * @param difficulty
     *            - Dificultad del test.
     * @param timeLeft
     *            - Tiempo restante de la pregunta.
     */
    private void updateScore(int difficulty, int timeLeft) {

	int score = (difficulty + 1) * timeLeft;

	switch (difficulty) {
	case 0:
	    score += TIME_HARD;
	    break;
	case 1:
	    score += TIME_MEDIUM;
	    break;
	case 2:
	    score += TIME_EASY;
	    break;
	default:
	    break;
	}

	if (this.bestScore < score)
	    setBestScore(score);

	this.totalScore += score;
    }

}
