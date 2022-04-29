public enum States {

    Q0("0"),
    Q1("00"),
    Q2("000"),
    Q3("0000"),
    Q4("00000"),
    Q5("000000"),
    Q6("0000000"),
    Q7("00000000");

    States(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    private String stringRepresentation;

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public static States getCorrespondingState(String stringRepresentation) {
        States correspondingState = Q0;

        for (States state : States.values()) {
            if (stringRepresentation.equals(state.stringRepresentation)) {
                correspondingState = state;
            }
        }
        return correspondingState;
    }
}
