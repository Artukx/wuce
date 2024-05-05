package Models;


import java.sql.ResultSet;
import java.sql.SQLException;

public class ListaCard {
        private int ID_lista;
        private int ID_usuario;
        private String nombre;
        private String ruta;
        private String faccion;

        public ListaCard(ResultSet rs) throws SQLException {
            this.ID_lista = rs.getInt("ID_lista");
            this.ID_usuario = rs.getInt("ID_usuario");
            this.nombre = rs.getString("nombre");
            this.ruta = rs.getString("ruta");
            this.faccion = rs.getString("faccion");
        }

        public int getID_lista() {
            return ID_lista;
        }

        public void setID_lista(int ID_lista) {
            this.ID_lista = ID_lista;
        }

        public int getID_usuario() {
            return ID_usuario;
        }

        public void setID_usuario(int ID_usuario) {
            this.ID_usuario = ID_usuario;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getRuta() {
            return ruta;
        }

        public void setRuta(String ruta) {
            this.ruta = ruta;
        }

        public String getFaccion() {return faccion;}

        public void setFaccion(String faccion) {this.faccion = faccion;}
    }

