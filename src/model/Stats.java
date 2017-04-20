package model;

/**
 * Clase modelode estadisticas de un player. Parte del modelo.
 * 
 * @author Alejandro Gil Torres
 * @author Javier Estevez Aguado
 * @author Javier Hernantes Anton
 * @author Marina Paz Garcia
 * @author Sebastian Alvaerz Mendez
 * 
 */
public class Stats {

    /**
     * Mejor puntuacion registrada.
     */
    private int bestScore;
    /**
     * Numero total de preguntas respondidas.
     */
    private int qAnswer;
    /**
     * Porcentaje de preguntas respondidas correctamente.
     */
    private float qAverage;
    /**
     * Numero de preguntas respondidas correctamente.
     */
    private int qCorrect;
    /**
     * Numero de preguntas respondidas incorrectamente.
     */
    private int qFail;
    /**
     * Tiempo medio para responder una pregunta.(En segundos)
     */
    private int timeAverage; // in seconds.
    /**
     * Tiempo total respondiedo preguntas
     */
    private int timeTotal; // in seconds.

    /**
     * Puntuacion total
     */
    private int totalScore;

    /**
     * Añade y actualiza estadísticas
     * 
     * @param qAnswered
     *            -preguntas contestadas
     * @param qCorrect
     *            -preguntas correctas
     * @param totalTime
     *            -tiempo total de juego
     * @param bestScore
     *            -Mejor puntuación de pregunta individual
     * @param totalScore
     *            -Puntuación total
     */
    public void addStats(int qAnswered, int qCorrect, int totalTime,
	    int bestScore, int totalScore) {

	this.qAnswer += qAnswered;
	this.qCorrect += qCorrect;
	this.timeTotal += totalTime;
	this.totalScore += totalScore;
	this.bestScore = (this.bestScore < bestScore) ? bestScore
		: this.bestScore;

	updateRecords();
    }

    /**
     * Devuelve las estadisticas en un array de String de la forma: 0 -
     * PreguntasRespondidas 1 - PreguntasAcertadas 2 - PreguntasFalladas 3 -
     * PorcentajeAcertadas 4 - TiempoMedioPorPregunta 5 - TiempoTotalJugado 6 -
     * MejorPuntuacion 7 - PuntuacionTotal
     * 
     * @return String [8] stats
     */
    public String[] getAllStats() {
	String stats[] = new String[8];

	stats[0] = Integer.toString(this.qAnswer);
	stats[1] = Integer.toString(this.qCorrect);
	stats[2] = Integer.toString(this.qFail);
	stats[3] = String.format("%.2f", this.qAverage);
	stats[4] = Integer.toString(this.timeAverage);
	stats[5] = Integer.toString(this.timeTotal);
	stats[6] = Integer.toString(this.bestScore);
	stats[7] = Integer.toString(this.totalScore);

	return stats;
    }

    /**
     * Devuelve el atributo bestScore
     * 
     * @return the bestScore
     */
    public int getBestScore() {
	return this.bestScore;
    }

    /**
     * Devuelve el atributo qAnswer
     * 
     * @return the qAnswer
     */
    public int getqAnswer() {
	return this.qAnswer;
    }

    /**
     * Devuelve el atributo qAverage
     * 
     * @return the qAverage
     */
    public float getqAverage() {
	return this.qAverage;
    }

    /**
     * Devuelve el atributo qCorrect
     * 
     * @return the qCorrect
     */
    public int getqCorrect() {
	return this.qCorrect;
    }

    /**
     * Devuelve el atributo qFail
     * 
     * @return the qFail
     */
    public int getqFail() {
	return this.qFail;
    }

    /**
     * Devuelve el atributo timeAverage
     * 
     * @return the timeAverage
     */
    public int getTimeAverage() {
	return this.timeAverage;
    }

    /**
     * @return the timeTotal
     */
    public int getTimeTotal() {
	return this.timeTotal;
    }

    /**
     * @return the totalScore
     */
    public int getTotalScore() {
	return this.totalScore;
    }

    /**
     * Actualiza el atributo timeTotal sumandole el tiempo pasado por
     * parametros.
     * 
     * @param time
     *            Tiempo que se sumara.
     */
    public void plusTimeTotal(int time) {
	this.timeTotal += time;
    }

    /**
     * Modifica bestScore.
     * 
     * @param bestScore
     *            Mejor puntuacion
     * 
     */
    public void setBestScore(int bestScore) {
	this.bestScore = bestScore;
    }

    /**
     * @param qAnswer
     *            Modifica qAnswer.
     */
    public void setqAnswer(int qAnswer) {
	this.qAnswer = qAnswer;
    }

    /**
     * @param qCorrect
     *            Modifica qCorrect.
     */
    public void setqCorrect(int qCorrect) {
	this.qCorrect = qCorrect;
    }

    /**
     * @param timeTotal
     *            Modifica timeTotal
     */
    public void setTimeTotal(int timeTotal) {
	this.timeTotal = timeTotal;
    }

    /**
     * @param totalScore
     *            the totalScore to set
     */
    public void setTotalScore(int totalScore) {
	this.totalScore = totalScore;
    }

    /**
     * Actualiza los valores de las estadisticas indirectas EJ: Preguntas
     * falladas = Preguntas respondidas - Preguntas acertadas
     */
    public void updateRecords() {
	this.qFail = this.qAnswer - this.qCorrect;

	if (this.qAnswer != 0)
	    this.qAverage = (float) (this.qCorrect * 100.0 / this.qAnswer);
	else
	    this.qAverage = 0;

	if (this.qAnswer != 0) {

	    this.timeAverage = Math.round(this.timeTotal / this.qAnswer);
	}

    }

}
