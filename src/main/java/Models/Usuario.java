package Models;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
        private int ID_usuario;
        private String usuario;
        private String pass;
        private String nombre;
        private String apellidos;
        private String email;
        private String direccion;


        public Usuario(ResultSet rs) throws SQLException {
            this.ID_usuario = rs.getInt("ID_usuario");
            this.usuario = rs.getString("usuario");
            this.pass = rs.getString("pass");
            this.nombre = rs.getString("nombre");
            this.apellidos = rs.getString("apellidos");
            this.email = rs.getString("email");
            this.direccion = rs.getString("direccion");
        }

        public int getID_usuario() {
            return ID_usuario;
        }

        public void setID_usuario(int ID_usuario) {
            this.ID_usuario = ID_usuario;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellidos() {
            return apellidos;
        }

        public void setApellidos(String apellidos) {
            this.apellidos = apellidos;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }
    }
