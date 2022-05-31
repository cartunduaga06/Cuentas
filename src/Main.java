import java.sql.*;

public class Main {

    // se definen las variables necesarias

    private final static String DB_JDBC_DRIVER =  "org.h2.Driver";
    private final static String DB_URL= "jdbc:h2:~/db_cuentas"; //  colocar siempre en minuscula
    private final static String DB_USER= "sa";
    private final static String DB_PASSWORD = "sa";

    //  se definen las sentencias SQL

    private static final String SQL_CREATE =  "DROP TABLE IF EXISTS cuentas; CREATE TABLE cuentas (nro_cuenta INT PRIMARY KEY, nombre VARCHAR(100), saldo NUMERIC(15,2)); ";



    //SQL PARA INSERTAR REGISTROS

    private static final String SQL_INSERT = "INSERT INTO cuentas(nro_cuenta, nombre, saldo) values(?,?,?)";

    //SQL PARA UPDTAE
    private static final String SQL_UPDATE =  "UPDATE cuentas SET saldo=? WHERE nro_cuenta =?";



    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world!");

        //crear un objeto de la clase cuenta

        Cuenta cuenta  = new Cuenta(123, "juan restrepo", 0);



        Connection connection = null;

        //levantar el driver

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER, DB_PASSWORD);
            

            //sentencia para la crear la tabla

            Statement statement = connection.createStatement();
            statement.execute(SQL_CREATE);

            //sentencia para ingresar los datos
            //preparar para agregar un registro a la tabla cuentas
            PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT);

            // se setean los parametros del insert
            psInsert.setInt(1, cuenta.getNroCuenta());
            psInsert.setString(2, cuenta.getNombre());
            psInsert.setDouble(3, cuenta.getSaldo());

            psInsert.execute();// se ejecuta el INSERT en la base de datos

            // se prepara para actualizar un registro
            PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);
            psUpdate.setDouble(1, 200.000);
            psUpdate.setInt(2, cuenta.getNroCuenta());
            psUpdate.execute();


            //comienza una transaccion


            connection.setAutoCommit(false);


            psUpdate.setDouble(1, 500.000);
            psUpdate.setInt(2, cuenta.getNroCuenta());
            psUpdate.execute();

            //generar una excepcion
            //int a = 4/0;


            //commit para que efectue las operaciones en la base de datos



            connection.commit();

            //restablecer el valor del autocommit
            connection.setAutoCommit(true);




        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();

        } finally {
            connection.close();

        }
        connection = DriverManager.getConnection(DB_URL,DB_USER, DB_PASSWORD);
        String sql =  "SELECT *  FROM cuentas";
        Statement stmt  = connection.createStatement();
        ResultSet rd = stmt.executeQuery(sql);

        while(rd.next()){
            System.out.println(rd.getInt(1)+ " "+  rd.getString(2)+ " "+  rd.getString(3)+ " ");
        }

    }
}