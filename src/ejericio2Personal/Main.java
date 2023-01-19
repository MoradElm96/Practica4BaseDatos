/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejericio2Personal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Morad
 */
public class Main {

    public static void main(String[] args) throws SQLException {

        System.out.println("Se crea la tabla");
        crearTabla();
        System.out.println("Se insertan los datos");
        insertar();
        System.out.println("Tabla antes de actualizarse");
        mostrarTabla();
        //actualizamos, dentro del metodo muestra la tabla
        actualizar();
        

    }

    public static void crearTabla() throws SQLException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");

            String createTable = "create table if not exists OficinaEmpleados("
                    + "Nombre_Empleado varchar(25) primary key,"
                    + "Nombre_Departamento varchar(15),"
                    + "Salario int,"
                    + "Comision int)";

            Statement st = con.createStatement();
            st.execute(createTable);
            st.close();

        } else {
            System.out.println("no se ha podido conectar");
        }
        cerrarConexion(con);

    }

    public static void mostrarTabla() throws SQLException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery("select * from oficinaempleados");
            while (rs1.next()) {
                System.out.println(rs1.getString(1) + " " + rs1.getString(2) + " " + rs1.getInt(3) + " " + rs1.getInt(4));
            }
            
            rs1.close();
            st1.close();
           

        } else {
            System.out.println("no se ha podido conectar");
        }
        cerrarConexion(con);
    }

    public static void insertar() throws SQLException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");

            String sentencia = "select empleado.nombre,departamento.nombre,salario from empleado, departamento where empleado.dept_no=departamento.dept_no";
            Statement st1 = con.createStatement();
            ResultSet rs2 = st1.executeQuery(sentencia);

            int comision;
            while (rs2.next()) {

                if (rs2.getString(2).equalsIgnoreCase("Contabilidad")) {
                    comision = rs2.getInt(3) * 10 / 100;
                } else if (rs2.getString(2).equalsIgnoreCase("Investigacion")) {
                    comision = rs2.getInt(3) * 20 / 100;
                } else if (rs2.getString(2).equalsIgnoreCase("Ventas")) {
                    comision = rs2.getInt(3) * 5 / 100;
                } else {
                    comision = rs2.getInt(3) * 15 / 100;
                }

                String nombreEmpleado = rs2.getString(1);
                String nombreDepartamento = rs2.getString(2);
                int salario = rs2.getInt(3);

                sentencia = "insert into OficinaEmpleados values ('" + nombreEmpleado + "','" + nombreDepartamento + "'," + salario + "," + comision + ")";
                Statement st2 = con.createStatement();
                st2.executeUpdate(sentencia);
                st2.close();
               
                
                

            }
             rs2.close();
            

        } else {
            System.out.println("no se ha podido conectar");
        }
        cerrarConexion(con);
    }

    public static void actualizar() throws SQLException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");

            String sentencia = "select nombreempleado, comision from oficinaempleados";
            Statement st = con.createStatement();
            //ResultSet rs = st.executeQuery(sentencia);

              st.executeUpdate("update OficinaEmpleados set comision=comision+comision*10/100 where comision <300");
              st.executeUpdate("update OficinaEmpleados set comision=comision+comision*10/100 where comision between 400 and 600");
        
              System.out.println("Tabla despues de actualizar");
              mostrarTabla();
              st.close();

        } else {
            System.out.println("no se ha podido conectar");
        }
        cerrarConexion(con);
    }

    public static Connection conexion() {

        String bbdd = "jdbc:mysql://localhost/Personal";//se usa la base de datos america,facilitada por el script
        String usuario = "root";
        String clave = "";
        Connection conn = null;

        try {

            if (conn == null) {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(bbdd, usuario, clave);
            }

        } catch (Exception ex) {
            System.out.println("Error al conectar con la base de datos.\n"
                    + ex.getMessage().toString());
        }

        return conn;
    }

    //metodo para cerrar la conexion a la bbdd
    public static void cerrarConexion(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println("la conexion no se ha cerrado");
            System.out.println(e.getMessage().toString());
        }
    }
}
