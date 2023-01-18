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

        crearTabla();
        insertar();
        // actualizar();
        mostrarTabla();

    }

    public static void crearTabla() throws SQLException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");

            String createTable = "CREATE TABLE IF NOT EXISTS OficinaEmpleados (NombreEmpleado VARCHAR(50), NombreDepartamento VARCHAR(50), Salario double, Comision double)";
            Statement st = con.createStatement();
            st.executeUpdate(createTable);

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
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from oficinaempleados");
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDouble(4));
            }

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

            String sentencia = "SELECT e.nombre,d.nombre,e.salario from empleado e join departamento d on e.Dept_no=d.Dept_no";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sentencia);

            while (rs.next()) {

                String nombreEmpleado = rs.getString(1);
                String nombreDepartamento = rs.getString(2);
                int salario = rs.getInt(3);

                String sqlInsert = "INSERT INTO OficinaEmpleados (NombreEmpleado, NombreDepartamento, Salario) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sqlInsert);
                ps.setString(1, nombreEmpleado);
                ps.setString(2, nombreDepartamento);
                ps.setInt(3, salario);
                ps.executeUpdate();

            }
            
               /* if (nombreDepartamento.equalsIgnoreCase("Contabilidad")) {

                    psUpdate.setFloat(1, (salario * 0.1f));

                } else if (nombreDepartamento.equalsIgnoreCase("Investigacion")) {

                    psUpdate.setFloat(1, (salario * 0.2f));
                } else if (nombreDepartamento.equalsIgnoreCase("Ventas")) {

                    psUpdate.setFloat(1, (salario * 0.05f));
                } else if (nombreDepartamento.equalsIgnoreCase("Produccion")) {

                    psUpdate.setFloat(1, (salario * 0.15f));
                }*/
               
            

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
            ResultSet rs = st.executeQuery(sentencia);

            while (rs.next()) {

                String nombreEmpleado = rs.getString(1);

                double comision = rs.getDouble(2);

                if (comision < 300) {
                    comision += comision * 0.1;
                } else if (comision >= 400 && comision <= 600) {
                    comision += comision * 0.05;
                }

                sentencia = "update oficinaempleados set comision=? where nombre = ?";
                PreparedStatement psUpdate = con.prepareStatement(sentencia);
                psUpdate.setString(1, nombreEmpleado);
                psUpdate.setDouble(2, comision);
                psUpdate.executeUpdate();

            }

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
