package MAIN;
import DAO.Vacancy_CRUD;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class Parser {
    private static String URL;
    private static final List<Vacancy>VACANCY_LIST =new ArrayList<>();
    final Properties properties = new Properties();
    InputStream inputStream = null;

    // FIXME более привычно видеть такое в кончтрукторе + спрятать в метод типа loadConfig()
    {
        try { // FIXME лучше использовать try-with-resources
//            FIXME читаем этот же файл в 3 местах => лучше иметь отедльный класс, который нам загрузит конфиг
            inputStream = new FileInputStream("src/main/resources/properties.config");
            properties.load(inputStream);
            URL = properties.getProperty("PAGE1");
        } catch (FileNotFoundException e) {
            System.out.println("Can't find config file!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't find correct URL in properties!");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void loadVacancies(List<Vacancy> list){
        Vacancy_CRUD vacancie_persistance = new Vacancy_CRUD();
        for (Vacancy vacancy : list) {
            vacancie_persistance.persistData(vacancy);

        }
    }

    // FIXME слишком общее имя, напр. parseVacancies() выглядит понятнее
    public void work() {
        try {

            Document doc = Jsoup.connect(URL)
                    // FIXME тут и далее - все strings в константы с понятными именами (что ищем?)
                    .userAgent("Chrome/4.0.249.0")
                    .get();
            doc.outputSettings().outline(true);

            Elements link = doc.select("a[href*=/vacancy/][href$=%20junior]");

            for (Element e : link) {
                String linkText = e.attr("abs:href");
                deepWork(linkText);
            }


            loadVacancies(VACANCY_LIST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FIXME слишком общее имя, напр. parseVacancy() выглядит понятнее
    private void deepWork(String url) {
        Vacancy vacancy=new Vacancy();
        vacancy.setURL(url);
        try {
            Document document = Jsoup.connect(url).
                    userAgent("Chrome/4.0.249.0 ")
                    .referrer("http://www.google.com")
                    .get();
            document.outputSettings().outline(true);

            Elements eCompany = document.select("a[class='vacancy-company-name']");
            for (Element e : eCompany) {
                String string = e.text();
                vacancy.setCompany(string);
            }

            Elements eCity = document.select("p[data-qa='vacancy-view-location']");
            for (Element e : eCity) {
                String string = HTML_Filter.filterLocation(e.text());
                vacancy.setCity(string);
            }
            Elements eExp = document.select("[data-qa='vacancy-experience']");
            for (Element e : eExp) {
                String string = HTML_Filter.filterExpirience(e.text());
                vacancy.setExperience(Integer.valueOf(string));
            }

            Elements eSalary = document.select("p[class='vacancy-salary']");
            for (Element e : eSalary) {
                String string = HTML_Filter.filterSalary(e.text());
                vacancy.setSalary(Integer.parseInt(string));

            }
            Elements eDescription = document.select("div[data-qa='vacancy-description']");
            for (Element e : eDescription) {
                String string = HTML_Filter.filterRequirements(e.text());
                List<String>listOfSkills= Arrays.asList(string.split(","));
                vacancy.setSkillsRequired(listOfSkills);

            }
            VACANCY_LIST.add(vacancy);
            System.out.println(vacancy);
        } catch (IOException e) {
            // FIXME как насчет логирования?

            e.printStackTrace();
        }

    }
}