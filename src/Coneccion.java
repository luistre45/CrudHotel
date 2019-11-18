
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Coneccion {
    public static final String url="jdbc:mysql://localhost/dbhotel";
    public static final String usuario="root";
    public static final String contra="";
    
    public Connection conectar(){
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(url, usuario, contra);
        } catch(ClassNotFoundException ex){
            Logger.getLogger(Coneccion.class.getName()).log(Level.SEVERE, null, ex);
        } catch(SQLException ex){
            Logger.getLogger(Coneccion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
}
