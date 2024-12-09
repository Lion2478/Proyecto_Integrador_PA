package Modelo;

import java.sql.*;


public class ConexionBD {

    public ConexionBD(){
    }

    // Variables para la conexión a MySQL
    private static final String urlMySQL = "jdbc:mysql://";
    private static final String usuarioMySQL = "root";
    private static final String passwordMySQL = "leonardo";
    private static final String bd = "Proy_PA";
    public static  String[] parametros = {"127.0.0.1:3306", bd, usuarioMySQL, passwordMySQL};

    public static String[] getParametros(){
        return parametros;
    }

    public static Connection conectarMySQL() throws SQLException {

        String cadenaconexion = urlMySQL + parametros[0] + "/" + parametros[1];
        try{
            Connection conexion = DriverManager.getConnection(cadenaconexion, parametros[2], parametros[3]);
            return conexion;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        // parametro 0 = localhost
        // parametro 1 = bd
        // parametro 2 = usuarioMySql
        // paramentro 3 = passwordMySQL
    }

    // // Variables para la conexión a PostgreSQL
    // private static final String urlPostgreSQL = "jdbc:postgresql://";
    // private static final String usuarioPostgreSQL = "tu_usuario_postgresql";
    // private static final String passwordPostgreSQL = "tu_contraseña_postgresql";

    // // Variables para la conexión a MariaDB
    // private static final String urlMariaDB = "jdbc:mariadb://";
    // private static final String usuarioMariaDB = "tu_usuario_mariadb";
    // private static final String passwordMariaDB = "tu_contraseña_mariadb";

    // // Variables para la conexión a Oracle
    // private static final String urlOracle = "jdbc:oracle:thin:@";
    // private static final String usuarioOracle = "tu_usuario_oracle";
    // private static final String passwordOracle = "tu_contraseña_oracle";

    // // Variables para la conexión a Access utilizando UCanAccess
    // private static final String urlAccess = "jdbc:ucanaccess://";

    // // Variables para la conexión a SQL Server Express
    // private static final String urlSQLServer = "jdbc:sqlserver://";
    // private static final String usuarioSQLServer = "tu_usuario_sqlserver";
    // private static final String passwordSQLServer = "tu_contraseña_sqlserver";

    // // Método para conectar a SQL Server Express
    // public static Connection conectarSQLServer(String[] parametros) throws SQLException {
    //     String cadenaconexion = urlSQLServer + parametros[0] + "\\SQLEXPRESS;databaseName=" + parametros[1] + ";";
    //     Connection conexion = DriverManager.getConnection(cadenaconexion, parametros[2], parametros[3]);
    //     return conexion;
    // }
    // Método para conectar a MySQL
   
    // // Método para conectar a PostgreSQL
    // public static Connection conectarPostgreSQL(String[] parametros) throws SQLException {
    //     String cadenaconexion = urlPostgreSQL + parametros[0] + ":" + parametros[1] + "/" + parametros[2];
    //     Connection conexion = DriverManager.getConnection(cadenaconexion, parametros[3], parametros[4]);
    //     return conexion;
    // }
    // // Método para conectar a MariaDB
    // public static Connection conectarMariaDB(String[] parametros) throws SQLException {
    //     String cadenaconexion = urlMariaDB + parametros[0] + ":" + parametros[1] + "/" + parametros[2];
    //     Connection conexion = DriverManager.getConnection(cadenaconexion, parametros[3], parametros[4]);
    //     return conexion;
    // }
    // // Método para conectar a Oracle
    // public static Connection conectarOracle(String[] parametros) throws SQLException {
    //     String cadenaconexion = urlOracle + parametros[0] + ":" + parametros[1] + ":" + parametros[2];
    //     Connection conexion = DriverManager.getConnection(cadenaconexion, parametros[3], parametros[4]);
    //     return conexion;
    // }
    // // Método para conectar a UCanAccess
    // public static Connection conectarUCanAccess(String parametros) throws SQLException {  
    //     String cadenaconexion = urlAccess + parametros;
    //     System.out.println(cadenaconexion);
    //     Connection conexion = DriverManager.getConnection(cadenaconexion);
    //     return conexion;
    // }
}
