package academy.kata.calc_test1;

public class Arithmetic {                                     //Сложение
        static int sAdd(String a, String b) {              //Сложение
            return Integer.parseInt(a) + Integer.parseInt(b);
        }

        static int sSubtract(String a, String b) {         //Вычитание
            return Integer.parseInt(a) - Integer.parseInt(b);
        }

        static int sMultiply(String a, String b) {         //Умножение
            return Integer.parseInt(a) * Integer.parseInt(b);
        }

        static int sDivide(String a, String b) {           //Деление
            return Integer.parseInt(a) / Integer.parseInt(b);
        }
}
