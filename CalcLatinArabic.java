package academy.kata.calc_test1;

/**
*                                 Калькулятор (+-/*) целых арабских  и латинских чисел <= 10
*/
import java.util.Scanner;
import static java.lang.System.*;

class CalcLatinArabic {
    static final String INPUT_ERROR = "!!!Некорректный ввод!!!";
    public static void main(String[] args) {
        boolean inputValidation;
        do {
            Scanner scanner = new Scanner(in);
            String announcement = """

                            Правила ввода:
                    Введите выражение или только с латинскими или только с целыми арабскими числами >0, но <=10.
                    В одной строке одновременно допускаются либо только арабские, либо только латинские цифры.
                                        
                    При ошибке в выражении будет выполнен повторный цикл ввода!!!
                                        
                                          """;
            out.println(announcement);
            String expression = scanner.nextLine();
            out.println();
            String validationIn = isValidateS(expression);
            out.println("\nРезультат: " + validationIn);
            inputValidation = validationIn.equals(INPUT_ERROR);
            if (inputValidation) out.println("\nПовторите, пожалуйста, ввод.");
        }
        while (inputValidation);
    }

    //Входная проверка выражения и подготовка к распределению на арабскую и латинскую ветки вычислений

    static String isValidateS(String expression) {            //Проверить корректности ввода

      final String startRegex = "^";
      final String reOperandArabic = "[ \t]*(?:[1-9]|10)[ \t]*"; //Цифра [1-10], а вокруг пробелы и Tabs
      final String reOperandLatin = "[ \t]*[IVX]{1,4}[ \t]*";    //Набор для латыни <=10, в пробелах и Tabs
      final String reOperators = "[-+*/]";

      final String regexArabicEx = startRegex + reOperandArabic + reOperators + reOperandArabic;//Регулярка араб. <= 10
      final String regexLatinEx = startRegex + reOperandLatin + reOperators + reOperandLatin;  //Регулярка лат. <= 4 зн.

      if (expression.matches(regexArabicEx)) {
          return fullTrimAndCutS(expression, false);
      }

      if (expression.matches(regexLatinEx)) {
          return resultLatinFromArabicS(fullTrimAndCutS(expression, true));
      }

      return INPUT_ERROR;
    }

    //    Порезать выражение и распределить на арабскую и латинскую ветки вычислений
    private static String fullTrimAndCutS(String workExpression, boolean ifLatin) {
        String[] trimExpS = new String[3];
        String trimS = workExpression.trim();                                 //Отбросить боковые пробелы и Tabs
        int lengthT = trimS.length();
        String operatorS = detectOperatorS(trimS);
        int intOperatorPos = trimS.indexOf(operatorS);                        //Вырезать поле с пробелами и оператором
        operatorS = trimS.substring(intOperatorPos, intOperatorPos + 1);      //Чистый оператор внутри поля
        trimExpS[0] = operatorS;                                              //Чистый оператор на экспорт
        trimExpS[1] = trimS.substring(0, intOperatorPos).trim();              //1-й строковый операнд
        trimExpS[2] = trimS.substring(intOperatorPos + 1, lengthT).trim();    //2-й строковый операнд
        return (!ifLatin) ? calculatorS(trimExpS)
                : calculatorS(latinToArabicS(trimExpS));
    }

    private static String detectOperatorS(String operator) {
        if (operator.contains("+")) return "+";
        if (operator.contains("-")) return "-";
        if (operator.contains("*")) return "*";
        return "/";
    }

    private static String calculatorS(String[] operand) {
      String operator = operand[0];
      return String.valueOf(switch (operator) {                         //К строковому виду
        case "+" -> Arithmetic.sAdd(operand[1], operand[2]);
        case "-" -> Arithmetic.sSubtract(operand[1], operand[2]);
        case "*" -> Arithmetic.sMultiply(operand[1], operand[2]);
        default -> Arithmetic.sDivide(operand[1], operand[2]);
        });
    }

    private static String[] latinToArabicS(String[] latinOperandS) {
        String[] arrLatinToArabicS = new String[3];                     //Новый массив в арабском виде
        arrLatinToArabicS[0] = latinOperandS[0];
        for (int i = 1; i <= 2; i++) {
                    try {
                LatinEnum.valueOf(latinOperandS[i]);
            } catch (IllegalArgumentException e) {
                err.println("!!! Неправильный ввод: " + latinOperandS[i] + " !!!\n");
                exit(0);
            }
            int latinToArabicInt = (LatinEnum.valueOf(latinOperandS[i]).ordinal()) + 1;//Индекс каждого латинского
                                                                     //операнда в Enum соответствует арабскому значению
            if (latinToArabicInt > 10) {
             err.println("\n!!! Ошибка, вводимые операнды должны быть <= 10. " + latinOperandS[i] + " больше 10 !!!\n");
                exit(0);
            }

            arrLatinToArabicS[i] = String.valueOf(latinToArabicInt);
        }
        return arrLatinToArabicS;
    }

    private static String resultLatinFromArabicS(String resultInArabicS) {
        int resultInArabicInt = Integer.parseInt(resultInArabicS);
        if (resultInArabicInt < 1) {
                err.println("\n!!! Результат операций с латиницей не может быть меньше 1 !!!\n");
                exit(0);
        }
        LatinEnum[] arrLatinFromEnum = LatinEnum.values();   //Слив Enum в массив для последующей выдачи рез-та латынью
        return String.valueOf(arrLatinFromEnum[resultInArabicInt - 1]);      //Приведение результата к строковой латыни
    }
}