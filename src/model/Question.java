package model;

/**
 * Clase modelo de las preguntas.Parte del modelo de la aplicaicon.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class Question {
    /**
     * Valores de refeencia para las dificultades
     */
    public static final int DIFFICULTY_EASY = 0;
    /**
     * Valores de refeencia para las dificultades
     */
    public static final int DIFFICULTY_HARD = 2;
    /**
     * Valores de refeencia para las dificultades
     */
    public static final int DIFFICULTY_MEDIUM = 1;

    /**
     * Convierte de dificultad a string
     * 
     * @param diff
     *            Dificultad
     * @return String
     */
    public static String diff2String(int diff) {
	String string;
	switch (diff) {
	case Question.DIFFICULTY_EASY:
	    string = "F\u00E1cil";
	    break;
	case Question.DIFFICULTY_MEDIUM:
	    string = "Medio";
	    break;
	case Question.DIFFICULTY_HARD:
	    string = "Dif\u00edcil";
	    break;
	default:
	    string = String.valueOf(diff);
	    break;
	}
	return string;
    }

    /**
     * Respuesta, debe ser unica.
     */
    private String answer;
    /**
     * Dificultad
     */
    private int difficulty;
    /**
     * Identificador numerico de la pregunta. Debe ser unico, no lo asignamos
     * nosotros.
     */
    private int id;

    /**
     * Texto de la pregunta
     */
    private String question;

    /**
     * Constructor de preguntas
     * 
     * @param question
     *            - Pregunta
     * @param answer
     *            - Respuesta
     * @param difficulty
     *            - Dificultad
     */
    public Question(String question, String answer, int difficulty) {
	this.question = question;
	this.answer = answer;
	this.difficulty = difficulty;
    }

    /**
     * Devuelve la respuesta
     * 
     * @return the answer
     */
    public String getAnswer() {
	return this.answer;
    }

    /**
     * Devuelve la dificultad
     * 
     * @return the difficulty
     */
    public int getDifficulty() {
	return this.difficulty;
    }

    /**
     * Devuelve el identificador de la pregunta
     * 
     * @return the id
     */
    public int getId() {
	return this.id;
    }

    /**
     * Deuvelve el atributo pregunta
     * 
     * @return the question
     */
    public String getQuestion() {
	return this.question;
    }

    /**
     * Asigna al atributo respuesta la nueva respuesta
     * 
     * @param answer
     *            La nueva respuesta.
     */
    public void setAnswer(String answer) {
	this.answer = answer;
    }

    /**
     * Modifica la dificultad dela pregunta.
     * 
     * @param difficulty
     *            the difficulty to set
     */
    public void setDifficulty(int difficulty) {
	this.difficulty = difficulty;
    }

    /**
     * Modifica el identificador de la pregunta.
     * 
     * @param id
     *            the id to set
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * Asigna al atributo pregunta la nueva pregunta
     * 
     * @param question
     *            La nueva pregunta.
     */
    public void setQuestion(String question) {
	this.question = question;
    }
}
