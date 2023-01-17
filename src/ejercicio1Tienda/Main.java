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
        leerFicheroBinario(f);
    }
    
    
    
    
     public static void leerFicheroBinario(File f) throws IOException, SQLException {
        try {
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);

            try {
                String opcion;
                while (true) {
                   opcion = dis.readUTF();
                    switch(opcion){
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
    
     
     
     
     
     public static void darAlta(DataInputStream dis) throws SQLException, IOException{
        Connection con = conexion(); //objeto para hacer conexion
        //conectamos con la ayuda del metodo conexion
        Scanner sc = new Scanner(System.in);

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
            
            //comprobar si existe
            psSelect.setString(1, codProducto);
            
            ResultSet rs = psSelect.executeQuery();
            if(rs.next()){
                System.out.println("No se puede dar de alta porque ya existe");
                
            }else{
            psInsert.setString(1,codProducto);
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
    
     
    private static void darBaja(DataInputStream dis) {
    }

    private static void modificar(DataInputStream dis) {
    
     
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

