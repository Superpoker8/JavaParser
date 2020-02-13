package MAIN;
import MAIN.Vacancy;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class HTML_Filter {

    // FIXME для этого лучше подойдет enum
    private final static List<String> CITIES = Arrays.asList(new String[]{"Москва",
            "Зеленоград","Дубна","Санкт-Петербург",
            "Химки","Воронеж","Тула","Екатеринбург",
            "Орел", "Брянск","Белгород","Минск",
            "Курск"});

    // FIXME для этого лучше подойдет enum
    private final static List<String> SKILLS = Arrays.asList(new String[]{
            "HIBERNATE","SPRING","JAVA8","EJB","GENERICS","SQL","NOSQL","AJAX","CSS","HTML",
            "JUNIT","LOG4J","ORACLE","POSTGRES","MONGODB","CASSANDRA","CORE","UI"
    });

    static String filterLocation(String parsingString) {
     for(String  string: CITIES){
         if (parsingString.contains(string))return  string;
     }
     // FIXME похоже на исключительную ситуацию, но вызывающий код не знает - вернулся нам город или какое-то предложени
     // FIXME опять же, если это будет enum, то можем вернуть что-то типа CITIES.FAR_RELOCATION и обработать, как хочется в клиентском вызове
     return "Required far relocation";
    }

    // FIXME опечатка "Expirience"
    static String filterExpirience(String parsingString) {
        // FIXME NPE, если parsingString == null
        // FIXME regexp - нечитаемы для человека, вынести в константы с норм. именами, чтоы понятно было, что мы ищем/фильтруем
        if(parsingString.matches(".*\\d.*")){
            return parsingString.replaceAll("[^0-9]", "").substring(0,1);
        }

        // FIXME что значит 0? и почему мы не используем int?
        return "0";
    }

    // FIXME те же замечания
    static String filterSalary(String parsingString){
        if(parsingString.matches(".*\\d.*")){
            Matcher matcher = Pattern.compile("\\d+").matcher(parsingString);
            matcher.find();
            String res = matcher.group();
            return res;
        }

        return"0";
    }

    // FIXME с первого взгляда неясно, что делает этот метод => может быть переписан и быть более читаемым
    static String filterRequirements(String parsingString){
        StringBuilder stringBuilder = new StringBuilder();
        for (String string: SKILLS){
         Pattern pattern = Pattern.compile(string,Pattern.CASE_INSENSITIVE);
         Matcher matcher = pattern.matcher(parsingString);
         while (matcher.find()){
             stringBuilder.append(matcher.group()).append(",");
             break;
            }
         }
        if(stringBuilder.lastIndexOf(",") >0 ){
            stringBuilder.deleteCharAt( stringBuilder.lastIndexOf(","));
        }
        return stringBuilder.toString();
     }


}