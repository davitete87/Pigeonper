package kennedy.com.Pigeonper;

public class ValoresFitness {

    private float[] fitnessPorCromosoma, probPorCromosoma, probAcum;
    private float fitnessTotal;

//////////// CONSTRUCTORES

    public ValoresFitness(){}

    public ValoresFitness(float[] fitnessPorCromosoma, float fitnessTotal) {
        this.fitnessPorCromosoma = fitnessPorCromosoma;
        this.fitnessTotal = fitnessTotal;
    }

//////////////////////

    public float[] getFitnessPorCromosoma() {
        return fitnessPorCromosoma;
    }

    public void setFitnessPorCromosoma(float[] fitnessPorCromosoma) {
        this.fitnessPorCromosoma = fitnessPorCromosoma;
    }

/////////////////////GETERS Y SETERS

    public float getFitnessTotal() {
        return fitnessTotal;
    }

    public void setFitnessTotal(float fitnessTotal) {
        this.fitnessTotal = fitnessTotal;
    }

    public float[] getProbPorCromosoma() {
        return probPorCromosoma;
    }

    public void setProbPorCromosoma(float[] probPorCromosoma) {
        this.probPorCromosoma = probPorCromosoma;
    }

    public float[] getProbAcum() {
        return probAcum;
    }

    public void setProbAcum(float[] probAcum) {
        this.probAcum = probAcum;
    }
}
