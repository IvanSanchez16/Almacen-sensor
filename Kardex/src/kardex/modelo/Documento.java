package kardex.modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static kardex.KardexMain.con;

public class Documento
{
    private String DocCod;
    private String DocNom;
    private String DocEstReg;
        
    public Documento()
    {
        this("-1","NULL", "0");
    }
    
    public Documento(String DocCod, String DocNom, String DocEstReg)
    {
        this.DocCod = DocCod;
        this.DocNom = DocNom;
        this.DocEstReg = DocEstReg;
    }

    public String getDocCod()
    {
        return DocCod;
    }

    public void setDocCod(String DocCod)
    {
        this.DocCod = DocCod;
    }

    public String getDocNom()
    {
        return DocNom;
    }

    public void setDocNom(String DocNom)
    {
        this.DocNom = DocNom;
    }

    public String getDocEstReg()
    {
        return DocEstReg;
    }

    public void setDocEstReg(String DocEstReg)
    {
        this.DocEstReg = DocEstReg;
    }
    
    public String insertar()
    {
        try
        {
            con.ejecutar("INSERT INTO DOCUMENTO VALUES(DEFAULT, ?, ?)", new String[] {DocNom, DocEstReg}, false);
        }
        catch (SQLException ex)
        {
            return ex.getMessage();
        }
        return "";
    }
    
    public String modificar()
    {
        try
        {
            con.ejecutar("UPDATE DOCUMENTO SET DocNom = ? WHERE DocCod = ?", new String[] {DocNom, DocCod}, false);
        }
        catch (SQLException ex)
        {
            return ex.getMessage();
        }
        return "";
    }
    
    public String eliminar()
    {
        try
        {
            this.setDocEstReg("3");
            con.ejecutar("UPDATE DOCUMENTO SET DocEstReg = 3 WHERE DocCod = ?", new String[] {DocCod}, false);
        }
        catch (SQLException ex)
        {
             return ex.getMessage();
        }
        return "";
    }
    
    public static ArrayList<Documento> getLista()
    {
        ArrayList<Documento> documentos = new ArrayList<> ();
        try
        {
            ResultSet rs = con.ejecutar("SELECT * FROM DOCUMENTO ORDER BY DocEstReg ASC, DocCod ASC", null, true);
            while(rs.next())
            {
                String codigo = rs.getString("DocCod");
                String nombre = rs.getString("DocNom");
                String estado = rs.getString("DocEstReg");
                Documento doc = new Documento(codigo, nombre, estado);
                documentos.add(doc);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.\nConfigure la conexión correctamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return documentos;
    }   

    public String activar()
    {
        try
        {
            this.setDocEstReg("1");
            con.ejecutar("UPDATE DOCUMENTO SET DocEstReg = 1 WHERE DocCod = ?", new String[] {DocCod}, false);
        }
        catch (SQLException ex)
        {
             return ex.getMessage();
        }
        return "";
    }
    
    public String desactivar()
    {
        try
        {
            this.setDocEstReg("2");
            con.ejecutar("UPDATE DOCUMENTO SET DocEstReg = 2 WHERE DocCod = ?", new String[] {DocCod}, false);
        }
        catch (SQLException ex)
        {
             return ex.getMessage();
        }
        return "";
    }
    
    public static Documento buscar(String codigo)
    {
        Documento d = null;
        try
        {
            ResultSet rs = con.ejecutar("SELECT * FROM DOCUMENTO WHERE DocCod = ?", new String[] {codigo}, true);
            rs.next();
            d = new Documento();
            d.setDocCod(rs.getString("DocCod"));
            d.setDocNom(rs.getString("DocNom"));
            d.setDocEstReg(rs.getString("DocEstReg"));
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.\nConfigure la conexión correctamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return d;
    }   
    
    public static ArrayList<ArrayList<String>> getActivos()
    {
        ArrayList<ArrayList<String>> documentos = new ArrayList<>();
        try
        {        
            ResultSet rs = con.ejecutar("SELECT DocCod, DocNom FROM DOCUMENTO WHERE DocEstReg = 1", null, true);
            while(rs.next())
            {
                ArrayList<String> data = new ArrayList<>();
                String codigo = rs.getString("DocCod");
                String nombre = rs.getString("DocNom");
                data.add(codigo);
                data.add(nombre);
                documentos.add(data);
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.\nConfigure la conexión correctamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return documentos;
    }
    
    public static String sgteCodigo()
    {
        try
        {
            ResultSet rs = con.ejecutar("SELECT LPAD((SELECT COUNT(*) + 1 FROM DOCUMENTO), 6, '0') AS nextCod", null, true);
            rs.next();
            return rs.getString("nextCod");
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.\nConfigure la conexión correctamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return "000000";
    }
}