package ovh.olo.smok.smokwroclawski.Parser;

/**
 * This class converts string to numbers
 * and returns integer or double
 *
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
class NumberParser {

    /**
     * Parse string with number to double value.
     *
     * @param s     String to parse to double
     * @return      Double value, if string is wrong returns -1.0
     */
    Double parseDouble(String s) {
        if(s == null) return -1.0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    /**
     * Parse string with number to integer value.
     *
     * @param s     String to parse to integer
     * @return      Integer value, if string is wrong returns -1
     */
    int parseInt(String s) {
        if(s == null) return -1;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
