import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class lexer {
    public static List<String> readFileLineByLine(String file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> linesList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                linesList.add(line);
            }
//            System.out.println(linesList);
            return linesList;
        }
    }

    public static String readFile(String file) throws IOException {
        List<String> lines = readFileLineByLine(file);
        StringBuilder sb = new StringBuilder();
        for (String l : lines) {
            sb.append(l).append("\n");
        }
//        System.out.println(sb.toString().trim());
        return sb.toString().trim();
    }

    public static String getStringifiedFile(String filePath) throws IOException {
        File fileObj = new File(filePath);
        try {
            String words = readFile(fileObj.getAbsolutePath());
            return words;
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static List<String[]> parseSb(List<String[]> tokens, StringBuilder sb) throws Exception {
        if (sb.length() != 0) {
            if (sb.charAt(0) == '!') throw new Exception("Unexpected '!'");
            String str = sb.toString();
            if (str.equals("int")) tokens.add(new String[]{"INT", str});
            else if (str.equals("if")) tokens.add(new String[]{"IF", str});
            else if (str.equals("else")) tokens.add(new String[]{"ELSE", str});
            else if (str.equals("while")) tokens.add(new String[]{"WHILE", str});
            else if (str.equals("return")) tokens.add(new String[]{"RETURN", str});
            else if (Character.isLetter(str.charAt(0))) tokens.add(new String[]{"ID", str});
            else if (Character.isDigit(str.charAt(0))) tokens.add(new String[]{"NUM", str});
            else if (str.equals("(")) tokens.add(new String[]{"LPAREN", str});
            else if (str.equals(")")) tokens.add(new String[]{"RPAREN", str});
            else if (str.equals("{")) tokens.add(new String[]{"LBRACE", str});
            else if (str.equals("}")) tokens.add(new String[]{"RBRACE", str});
            else if (str.equals("<")) tokens.add(new String[]{"LT", str});
            else if (str.equals("<=")) tokens.add(new String[]{"LE", str});
            else if (str.equals(">")) tokens.add(new String[]{"GT", str});
            else if (str.equals(">=")) tokens.add(new String[]{"GE", str});
            else if (str.equals("=")) tokens.add(new String[]{"BECOMES", str});
            else if (str.equals("==")) tokens.add(new String[]{"EQ", str});
            else if (str.equals("!=")) tokens.add(new String[]{"NE", str});
            else if (str.equals("+")) tokens.add(new String[]{"PLUS", str});
            else if (str.equals("-")) tokens.add(new String[]{"MINUS", str});
            else if (str.equals("*")) tokens.add(new String[]{"STAR", str});
            else if (str.equals("/")) tokens.add(new String[]{"SLASH", str});
            else if (str.equals("%")) tokens.add(new String[]{"PCT", str});
            else if (str.equals(",")) tokens.add(new String[]{"COMMA", str});
            else if (str.equals(";")) tokens.add(new String[]{"SEMI", str});

            else throw new Exception("wtf why is this here " + str);
        }
        return tokens;
    }

    public static void printTokens(List<String[]> l) {
        for (String[] s: l) {
            System.out.println(s[0] + " " + s[1]);
        }
    }

    public static List<String[]> tokenize(String file) throws Exception {
        StringBuilder sb = new StringBuilder();
        List<String[]> tokens = new ArrayList<>();

        try {
            for (char ch : file.toCharArray()) {
//                if (ch == ' ') System.out.println("SPACE");
//                else if (ch == '\n') System.out.println("NEWLINE");
//                else if (ch == '\t') System.out.println("TAB");
//                else if (Character.isLetter(ch)) System.out.println(ch + ", Letter");
//                else if (Character.isDigit(ch)) System.out.println(ch + ", Number");
//                else System.out.println(ch + " other character");

                if (ch == ' ' || ch == '\n' || ch == '\t') {
                    tokens = parseSb(tokens, sb);
                    sb = new StringBuilder();
                }
                else if (Character.isLetter(ch)) {
                    if (sb.length() > 0 && Character.isDigit(sb.charAt(0))) throw new Exception("IDs can't start with numbers");
                    if (sb.length() > 0 && sb.charAt(0) == '!') throw new Exception("Unexpectd '!'");
                    if (sb.length() > 0 && (sb.charAt(sb.length()-1) == '<' || sb.charAt(sb.length()-1) == '>')) {
                        tokens = parseSb(tokens, sb);
                        sb = new StringBuilder();
                    }
                    if (sb.length() > 0 && sb.charAt(sb.length()-1) == '=') {
                        tokens = parseSb(tokens, sb);
                        sb = new StringBuilder();
                    }
                    sb.append(ch);
                }
                else if (Character.isDigit(ch)) {
                    if (sb.length() > 0 && sb.charAt(0) == '0') throw new Exception("Numbers can't start with 0");
                    if (sb.length() > 0 && sb.charAt(0) == '!') throw new Exception("Unexpectd '!'");
                    if (sb.length() > 0 && (sb.charAt(sb.length()-1) == '<' || sb.charAt(sb.length()-1) == '>')) {
                        tokens = parseSb(tokens, sb);
                        sb = new StringBuilder();
                    }
                    if (sb.length() > 0 && sb.charAt(sb.length()-1) == '=') {
                        tokens = parseSb(tokens, sb);
                        sb = new StringBuilder();
                    }
                    sb.append(ch);
                }
                else if (ch == '=') {

                    if (sb.length() == 0) sb.append(ch);
                    else if (sb.charAt(sb.length()-1) == '=') {
                        tokens.add(new String[]{"EQ", "=="});
                        sb = new StringBuilder();
                    }
                    else if (sb.charAt(sb.length()-1) == '!') {
                        tokens.add(new String[]{"NE", "!="});
                        sb = new StringBuilder();
                    }
                    else if (sb.charAt(sb.length()-1) == '<') {
                        tokens.add(new String[]{"LE", "<="});
                        sb = new StringBuilder();
                    }
                    else if (sb.charAt(sb.length()-1) == '>') {
                        tokens.add(new String[]{"GE", ">="});
                        sb = new StringBuilder();
                    }
                    else {
                        tokens = parseSb(tokens, sb);
                        sb = new StringBuilder();
                        sb.append("=");
                    }
                }
                else if (ch == '!') {
                    tokens = parseSb(tokens, sb);
                    sb = new StringBuilder();
                    sb.append(ch);
                }
                else if (ch == '<') {
                    tokens = parseSb(tokens, sb);
                    sb = new StringBuilder();
                    sb.append(ch);
                }
                else if (ch == '>') {
                    tokens = parseSb(tokens, sb);
                    sb = new StringBuilder();
                    sb.append(ch);
                }
                else if (ch == '(') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"LPAREN", "("});
                    sb = new StringBuilder();
                }
                else if (ch == ')') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"RPAREN", ")"});
                    sb = new StringBuilder();
                }
                else if (ch == '{') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"LBRACE", "{"});
                    sb = new StringBuilder();
                }
                else if (ch == '}') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"RBRACE", "}"});
                    sb = new StringBuilder();
                }
                else if (ch == '+') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"PLUS", "+"});
                    sb = new StringBuilder();
                }
                else if (ch == '-') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"MINUS", "-"});
                    sb = new StringBuilder();
                }
                else if (ch == '*') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"STAR", "*"});
                    sb = new StringBuilder();
                }
                else if (ch == '/') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"SLASH", "/"});
                    sb = new StringBuilder();
                }
                else if (ch == '%') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"PCT", "%"});
                    sb = new StringBuilder();
                }
                else if (ch == ',') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"COMMA", ","});
                    sb = new StringBuilder();
                }
                else if (ch == ';') {
                    tokens = parseSb(tokens, sb);
                    tokens.add(new String[]{"SEMI", ";"});
                    sb = new StringBuilder();
                }
                else throw new Exception("Unexpected character");

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tokens;
    }

    public static void main(String[] args) {

        try {
            String file = getStringifiedFile("src/main/java/data/program.txt");
            List<String[]> tokens = tokenize(file);
            printTokens(tokens);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
