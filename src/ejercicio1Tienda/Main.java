/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejercicio1Tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author alumno
 */
public class Main {
    public static void main(String[] args) {
        
    }
    
      public static Connection conexion() {

        String bbdd = "jdbc:mysql://localhost/Tienda";//se usa la base de datos america,facilitada por el script
        String usuario = "root";
        String clave = "";
        Connection conn = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(bbdd, usuario, clave);

        } catch (Exception ex) {
            System.out.println("Error al conectar con la base de datos.\n"
                    + ex.getMessage().toString());
        }

        return conn;
    }

    //metodo para cerrar la conexion a la bbdd
    public static void cerrarConexion(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("la conexion no se ha cerrado");
            System.out.println(e.getMessage().toString());
        }
    }
}
