package DAO;

import MAIN.Vacancy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

class Vacancy_read extends DaoFactory {

    private static String URL;
    private static String USER;
    private static String DRIVER;
    private static  String PASSWORD;
    private static Properties PROPERTIES= new Properties();
    private static String CREATE_DB_VACANCIES;
    private static String CREATE_DB_SKILLS;

    Connection connection=null;
    PreparedStatement statement=null;
    ResultSet resultSet=null;
    InputStream is=null;


    {
        try {
            System.out.println("Trying to load props file..");
            is = new FileInputStream("resources/properties.config");
            PROPERTIES.load(is);
            USER = PROPERTIES.getProperty("DB_USER");
            PASSWORD = PROPERTIES.getProperty("DB_PASSWORD");
            URL=PROPERTIES.getProperty("DB_URL");
            DRIVER=PROPERTIES.getProperty("DB_DRIVER");
            CREATE_DB_VACANCIES=PROPERTIES.getProperty("CREATE_SQL");
            CREATE_DB_SKILLS=PROPERTIES.getProperty("CREATE_SQL2");
            Class.forName(DRIVER);
            System.out.println(USER);
            System.out.println(PASSWORD);
            try{
                is.close();
            }
            catch(IOException e){
                System.out.println("Can't close inputstream!");
            }

        } catch (FileNotFoundException e) {
            System.out.println("File properties.config hasn't been found! ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Properties can't find your data!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void createTable() {
        try{
            connection=DriverManager.getConnection(URL,USER,PASSWORD);
            statement=connection.prepareStatement(CREATE_DB_VACANCIES);
            statement=connection.prepareStatement(CREATE_DB_SKILLS);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Failed to establish connection!");
            e.printStackTrace();
        }
        finally {
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public void getData() {
        try{
            connection=DriverManager.getConnection(URL,USER,PASSWORD);
            statement=connection.prepareStatement("select*from vacancies");
            resultSet =statement.executeQuery();
            while(resultSet.next()){
                System.out.print(resultSet.getInt(1)+ " ");
                System.out.print(resultSet.getInt(2)+ " ");
                System.out.print(resultSet.getInt(3)+ " ");
                System.out.print(resultSet.getString(4)+ " ");
                System.out.print(resultSet.getString(5)+ " ");
                System.out.print(resultSet.getString(6)+ " ");
                System.out.println();

            }
        } catch (SQLException e) {
            System.out.println("Failed to establish connection!");
            e.printStackTrace();
        }
        finally {
            if(resultSet!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void getData(int salary) {

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.prepareStatement("select*from vacancies where salary >= "+salary);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                System.out.println(resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println("Failed to establish connection!");
            e.printStackTrace();

        }
        finally {
            if(resultSet!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    @Override
    public void deleteData(LocalDate date) {
        try {
            connection=DriverManager.getConnection(URL,USER,PASSWORD);
            statement=connection.
                    prepareStatement("delete from vacancies where"+date.toString() +" >= postingDate");
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }
    public void mapVacancy(Vacancy vacancy){
        try {
            connection=DriverManager.getConnection(URL,USER,PASSWORD);
            statement=connection.
                    prepareStatement("INSERT INTO vacancies VALUES(?,?,?,?,?,?)");
            statement.setInt(1,vacancy.getID());
            statement.setString(2,vacancy.getCity());
            statement.setString(3,vacancy.getCompany());
            statement.setInt(4,vacancy.getExpirience());
            statement.setDate(5,vacancy.getDate());
            statement.setInt(6,vacancy.getSalary());
            statement.setString(7,vacancy.getURL());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}