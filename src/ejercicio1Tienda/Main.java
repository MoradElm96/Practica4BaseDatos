/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejercicio1Tienda;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author alumno
 */
public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        File f = new File("datos.dat");
        System.out.println("Antes de actualizar");
        consultaProductos();
        leerFicheroBinario(f);
        System.out.println("Despues de actualizar");
        consultaProductos();

    }

    public static void consultaProductos() throws SQLException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");

            Statement stSelect = con.createStatement();
            String sentencia = "SELECT * FROM productos";

            ResultSet rs = stSelect.executeQuery(sentencia);

            while (rs.next()) {
                String codProducto = rs.getString(1);
                String nombre = rs.getString(2);
                String lineaProducto = rs.getString(3);
                int precioUnitario = rs.getInt(4);
                int stock = rs.getInt(5);

                System.out.println("codProducto: " + codProducto + " Nombre: " + nombre + " LineaProducto: " + lineaProducto + " precioUnitario: " + precioUnitario + " stock: " + stock);

            }

        } else {
            System.out.println("no se ha podido conectar");
        }
        cerrarConexion(con);
    }

    public static void leerFicheroBinario(File f) throws IOException, SQLException {
        try {
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);

            try {
                String opcion;
                while (true) {
                    opcion = dis.readUTF();
                    switch (opcion) {
                        case "A":
                            darAlta(dis);
                            break;

                        case "B":
                            darBaja(dis);
                            break;

                        case "M":
                            modificar(dis);
                            break;

                    }
                }

            } catch (EOFException es) {
                System.out.println("\nFin del fichero");

            }
            dis.close();
            fis.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void darAlta(DataInputStream dis) throws SQLException, IOException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");

            String sentencia = "SELECT * FROM PRODUCTOS WHERE CodProducto=?";
            PreparedStatement psSelect = con.prepareStatement(sentencia);

            sentencia = "INSERT INTO PRODUCTOS VALUES (?,?,?,?,?)";
            PreparedStatement psInsert = con.prepareStatement(sentencia);

            String codProducto = dis.readUTF();
            String nombre = dis.readUTF();
            String lineaProducto = dis.readUTF();
            int precioUnitario = dis.readInt();
            int stock = dis.readInt();
            System.out.println("Se va a dar de alta a :");
            System.out.println("codProducto: " + codProducto + " Nombre: " + nombre + " LineaProducto: " + lineaProducto
                    + " precioUnitario: " + precioUnitario + " stock: " + stock);

            //comprobar si existe
            psSelect.setString(1, codProducto);

            ResultSet rs = psSelect.executeQuery();
            if (rs.next()) {
                System.out.println("No se puede dar de alta porque ya existe");

            } else {
                psInsert.setString(1, codProducto);
                psInsert.setString(2, nombre);
                psInsert.setString(3, lineaProducto);
                psInsert.setInt(4, precioUnitario);
                psInsert.setInt(5, stock);
                psInsert.executeUpdate();

                System.out.println("Alta realizada con exito");
            }

        } else {
            System.out.println("no se ha podido conectar");
        }
        cerrarConexion(con);
    }

    private static void darBaja(DataInputStream dis) throws IOException, SQLException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");

            String codProducto = dis.readUTF();
            System.out.println("Se va a borrar el producto con codigo: " + codProducto);

            String sentencia = "DELETE FROM PRODUCTOS WHERE CodProducto=?";
            PreparedStatement psDelete = con.prepareStatement(sentencia);
            psDelete.setString(1, codProducto);

            if (psDelete.executeUpdate() == 0) {
                System.out.println("no se ha podido dar de baja el producto, no existe");
            } else {
                System.out.println("Se ha dado de baja de manera exitosa");
            }

        } else {
            System.out.println("no se ha podido conectar");
        }
        cerrarConexion(con);
    }

    private static void modificar(DataInputStream dis) throws SQLException, IOException {
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion

        if (con != null) {

            System.out.println("Se ha conectado a la base datos  correctamente");

            String codProducto = dis.readUTF();
            System.out.println("Se va a modificar el producto con codigo: " + codProducto);
            int porcentaje = dis.readInt();
            System.out.println("Porcentaje :" + porcentaje);

            String sentencia = "UPDATE PRODUCTOS SET PrecioUnitario=PrecioUnitario+((PrecioUnitario*?)/100)  where CodProducto=?";
            PreparedStatement psUpdate = con.prepareStatement(sentencia);

            psUpdate.setInt(1, porcentaje);
            psUpdate.setString(2, codProducto);

            if (psUpdate.executeUpdate() == 0) {
                System.out.println("NO se ha podido actualizar el producto");
            } else {
                System.out.println("Se ha actualizado de manera exitosa");
            }

        } else {
            System.out.println("no se ha podido conectar");
        }
        cerrarConexion(con);

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
