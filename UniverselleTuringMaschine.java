import java.util.ArrayList;

public class UniverselleTuringMaschine {

    private final String startingState;
    private final String acceptingState;
    private final String blankSymbol;
    private final String[] states;

    private final String[] inputAlphabet;
    private final String[] tapeAlphabet;

    private String tape;
    private String currentState;

    private String[] allTransitionFuntionsAsStrings;
    int calculatingStepsCounter = 0;

    int indexOfTheNextState = 2;
    int indexOfTheNewTapeCharacter = 3;
    int indexOfTheMovementDirection = 4;

    public UniverselleTuringMaschine(int x, int y) {
        startingState = "0"; // Q0
        blankSymbol = "0000"; // blank
        states = new String[]{"0","00","000","0000","00000","000000","0000000","00000000"}; // Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7
        acceptingState = "00000000"; // Q7
        inputAlphabet = new String[]{"0","00"}; // 0, 1
        tapeAlphabet = new String[]{"0","00","000","0000"}; // 0, 1, Y, blank

        currentState = "0"; // Q0
        this.tape = input(x, y);

        allTransitionFuntionsAsStrings = initializeTransitionFunctionsAsStrings();
    }

    public String input(int firstNumber, int secondNumber) {
        String firstNumberInUnicode = transformNumberIntoUnicodeAndAddSeparator(firstNumber);
        String secondNumberInUnicode = transformNumberIntoUnicodeAndAddSeparator(secondNumber);

        return firstNumberInUnicode + secondNumberInUnicode;
    }

    public String transformNumberIntoUnicodeAndAddSeparator(int number) {
        StringBuilder numberAsUnicode = new StringBuilder();
        for(int i = number; i > 0; --i) {
            numberAsUnicode.append(0);
        }
        numberAsUnicode.append(1);
        return numberAsUnicode.toString();
    }

    private String[] initializeTransitionFunctionsAsStrings() {
        String[] allTransitionFunctionsAsStrings = new String[TransitionFunctions.values().length];

        for(int i = 0; i < TransitionFunctions.values().length ; i++) {
            allTransitionFunctionsAsStrings[i] = TransitionFunctions.values()[i].getTransitionAsCode();
        }

        return allTransitionFunctionsAsStrings;
    }

    private int processTheTape(boolean stepMode) {
        ArrayList<String> tapeAsArrayList = new ArrayList<>();
        int currentPositionOnTape = 0;
        boolean tapeProcessing = true;

        for(int i = 0; i < tape.length(); ++i) {
            if(tape.charAt(i) == '1') {
                tapeAsArrayList.add("00");
            } else {
                tapeAsArrayList.add(tape.substring(i, i + 1));
            }
        }
        int maximalSize = tapeAsArrayList.size() * 2;

        for(int i = tapeAsArrayList.size(); i < maximalSize; i++) {
            tapeAsArrayList.add(i, "0000");
        }

            while(tapeProcessing) {
                if(stepMode) {
                    System.out.println("Please enter something to continue: ");
                    new java.util.Scanner(System.in).nextLine();
                }

                //This code segment is for printing
                String[] codeAsArray = getCorrectCodeAsArray(tapeAsArrayList, currentPositionOnTape);
                System.out.print(printTape(tapeAsArrayList));
                String printedString = printTape(tapeAsArrayList);
                int position = printedString.length()-tapeAsArrayList.size()+currentPositionOnTape;
                System.out.println();
                for(int i = 0; i < position; i++) {
                    System.out.print(" ");
                }
                System.out.print("↑");
                for(int j = position + 1; j < printedString.length(); j++) {
                    System.out.print(" ");
                }
                System.out.println();
                //end of printing segment

                currentState = codeAsArray[indexOfTheNextState];

                try {
                tapeAsArrayList.set(currentPositionOnTape, codeAsArray[indexOfTheNewTapeCharacter]);}
                catch(IndexOutOfBoundsException e) {
                    tapeAsArrayList.add(currentPositionOnTape, codeAsArray[indexOfTheNewTapeCharacter]);
                }
                if(codeAsArray[indexOfTheMovementDirection].equals("0")) {
                    currentPositionOnTape = currentPositionOnTape - 1;
                } else {
                    currentPositionOnTape = currentPositionOnTape + 1;
                }

                if(currentState.equals("00000000")) {
                    tapeProcessing = false;
                    System.out.println(printTape(tapeAsArrayList));
                }
                calculatingStepsCounter++;
            }

        int counter = 0;
        for(String elements : tapeAsArrayList) {
            if(elements.equals("0")) {
                counter++;
            }
        }
        return counter;
    }

    private String[] getCorrectCodeAsArray(ArrayList<String> tapeAsArrayList, int currentPosition) {
        String [] codeAsArray;
        try {
            codeAsArray = deployCorrectTransitionFunction(currentState, tapeAsArrayList.get(currentPosition)).split("1");
        } catch(IndexOutOfBoundsException e) {
            codeAsArray = deployCorrectTransitionFunction(currentState, "0000").split("1");
        }

        return codeAsArray;
    }

    public String deployCorrectTransitionFunction(String currentState, String currentElementFromTape) {
        int substringIndicator = currentState.length();
        int secondSubstringIndicator = substringIndicator + 1 + currentElementFromTape.length();
        String correctFunction = "";
        try {
            for (String codes : allTransitionFuntionsAsStrings) {
                if (codes.substring(0, substringIndicator).equals(currentState)
                        && codes.substring(substringIndicator + 1, secondSubstringIndicator).equals(currentElementFromTape)) {
                    correctFunction = codes;
                    break;
                }
            }
        } catch(StringIndexOutOfBoundsException e) {

        }
            return correctFunction;
    }

    public String printTape(ArrayList<String> tapeAsArrayList) {

        StringBuilder tape = new StringBuilder();

        for(String elements : tapeAsArrayList) {
            switch(elements) {
                case "0" -> tape.append(0);
                case "00" -> tape.append(1);
                case "000" -> tape.append("Y");
                case "0000" -> tape.append("_");
            }
        }
        return calculatingStepsCounter + "    " + States.getCorrespondingState(currentState) + "   " + tape.toString();
    }

    public static void main(String[] args) {
        System.out.println("Please enter two numbers: ");
        int firstNumber = new java.util.Scanner(System.in).nextInt();
        int secondNumber = new java.util.Scanner(System.in).nextInt();
        System.out.printf("Calculating %d * %d %n", firstNumber, secondNumber);

        UniverselleTuringMaschine universelleTuringMaschine = new UniverselleTuringMaschine(firstNumber,secondNumber);
        System.out.println("The result is: " + universelleTuringMaschine.processTheTape(true));
    }
}
